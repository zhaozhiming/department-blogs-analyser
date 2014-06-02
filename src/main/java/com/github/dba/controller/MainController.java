package com.github.dba.controller;

import com.github.dba.html.CsdnFetcher;
import com.github.dba.html.IteyeFetcher;
import com.github.dba.model.*;
import com.github.dba.repo.read.BlogReadRepository;
import com.github.dba.repo.read.BlogViewReadRepository;
import com.github.dba.repo.write.BlogViewWriteRepository;
import com.github.dba.repo.write.BlogWriteRepository;
import com.github.dba.repo.write.DepGroupWriteRepository;
import com.github.dba.repo.write.DepMemberWriteRepository;
import com.github.dba.service.MailService;
import com.github.dba.util.DbaUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.String.format;

@Controller
public class MainController {
    private static final Log log = LogFactory.getLog(MainController.class);

    @Autowired
    private CsdnFetcher csdnFetcher;

    @Autowired
    private IteyeFetcher iteyeFetcher;

    @Autowired
    private DepGroupWriteRepository depGroupWriteRepository;

    @Autowired
    private DepMemberWriteRepository depMemberWriteRepository;

    @Autowired
    private BlogReadRepository blogReadRepository;

    @Autowired
    private BlogWriteRepository blogWriteRepository;

    @Autowired
    private BlogViewWriteRepository blogViewWriteRepository;

    @Autowired
    private BlogViewReadRepository blogViewReadRepository;

    @Autowired
    private MailService mailService;

    @Value("${urls}")
    private String urls;

    @Value("${groups}")
    private String groups;

    @Value("${members}")
    private String members;
    private final ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/mail/top", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void mailTop() {
        log.debug("mail top start");
        mailService.sendTops(getMonthTops(DateTime.now().getMillis()));
        log.debug("mail top finish");
    }

    @RequestMapping(value = "/blog/fetch", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void blogFetch() throws Exception {
        log.debug("blog fetch start");
        String[] urlArray = urls.split(",");
        log.debug(format("urls:%s", Arrays.toString(urlArray)));

        BatchBlogs batchBlogs = new BatchBlogs();
        for (String url : urlArray) {
            if (DbaUtil.isCsdn(url)) {
                batchBlogs.addAllBatchBlogs(csdnFetcher.fetch(url));
                continue;
            }
            batchBlogs.addAllBatchBlogs(iteyeFetcher.fetch(url));
        }
        blogWriteRepository.save(batchBlogs.merge());

        List<Blog> insertBlogs = batchBlogs.getInsertBlogs();
        if (!insertBlogs.isEmpty()) {
            mailService.sendNewBlogs(insertBlogs);
        }

        log.debug("blog fetch finish");
    }

    @RequestMapping(value = "/group/create", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void createGroups() {
        log.debug("create groups start");
        log.debug("group names:" + groups);
        depGroupWriteRepository.deleteAll();

        String[] groupNames = groups.split(",");
        for (String groupName : groupNames) {
            String[] texts = groupName.split("-");
            depGroupWriteRepository.save(new DepGroup(texts[0], texts[1]));
        }
        log.debug("create groups finish");
    }

    @RequestMapping(value = "/member/create", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void createMembers() {
        log.debug("create members start");
        log.debug("member names:" + members);
        depMemberWriteRepository.deleteAll();

        String[] membersArray = members.split(",");
        for (String member : membersArray) {
            String[] texts = member.split("-");
            depMemberWriteRepository.save(new DepMember(texts[0], texts[1], texts[2]));
        }
        log.debug("create members finish");
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public
    @ResponseBody
    String search(@RequestParam("depGroup") String depGroup,
                  @RequestParam("website") String website,
                  @RequestParam("startDate") String startDate,
                  @RequestParam("endDate") String endDate) throws Exception {
        log.debug("search blog start");
        log.debug(format("group name:%s, website:%s, start date:%s, end date:%s",
                depGroup, website, startDate, endDate));

        List<Blog> blogs = blogReadRepository.findAll(
                Blog.querySpecification(depGroup, website, startDate, endDate),
                new Sort(Sort.Direction.DESC, "time"));
        String resultArrayJson = mapper.writeValueAsString(blogs);
        log.debug(format("resultArrayJson: %s", resultArrayJson));
        log.debug("search blog finish");
        return resultArrayJson;
    }

    @RequestMapping(value = "/top", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public
    @ResponseBody
    String top() throws Exception {
        log.debug("top blogs start");

        String resultArrayJson =
                mapper.writeValueAsString(getMonthTops(DateTime.now().getMillis()));
        log.debug(format("resultArrayJson: %s", resultArrayJson));

        log.debug("top blogs finish");
        return resultArrayJson;
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public
    @ResponseBody
    String statistics(@RequestParam("statisticsDate") String statisticsDate) throws Exception {
        log.debug("statistics blogs start");
        log.debug(format("statistics date: %s", statisticsDate));

        DateTime time = Strings.isNullOrEmpty(statisticsDate) ? DateTime.now()
                : DateTime.parse(statisticsDate, DateTimeFormat.forPattern("yyyy-MM"));
        //get last day of select month
        time = time.withDayOfMonth(1).plusMonths(1).minusDays(1);

        List<MonthStatistics> months = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            long statisticsTime = time.minusMonths(i).getMillis();
            List<Top> monthTops = getMonthTops(statisticsTime);
            months.add(new MonthStatistics(statisticsTime, monthTops));
        }

        String resultArrayJson = mapper.writeValueAsString(months);
        log.debug(format("resultArrayJson: %s", resultArrayJson));

        log.debug("statistics blogs finish");
        return resultArrayJson;
    }

    @RequestMapping(value = "/blog/view", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void generateBlogViews() throws Exception {
        log.debug("generate blog views start");

        long threeMonthsAgo = DateTime.now().minusMonths(3).getMillis();
        blogViewWriteRepository.clear(threeMonthsAgo);

        List<Blog> blogs = blogReadRepository.findAfterTime(threeMonthsAgo);
        long now = DateTime.now().getMillis();
        List<BlogView> blogViews = Lists.newArrayList();
        for (Blog blog : blogs) {
            Long blogId = blog.getId();
            Long blogTime = blog.getTime();

            String link = blog.getLink();
            int total = DbaUtil.isCsdn(link) ?
                    csdnFetcher.fetchView(link) : iteyeFetcher.fetchView(link);
            //total equals -1 mean can't connect to the url
            if (total == -1) continue;

            List<BlogView> existBlogViews = blogViewReadRepository.findByBlogId(blogId);
            int increment = existBlogViews.isEmpty() ? total :
                    total - existBlogViews.get(existBlogViews.size() - 1).getTotal();
            log.debug(format("blogid: %d, total=%d, increment=%d",
                    blogId, total, increment));

            blogViews.add(new BlogView(blogId, total, increment, blogTime, now));
        }
        log.debug(format("blog views: %s", blogViews));
        blogViewWriteRepository.save(blogViews);

        log.debug("generate blog views finish");
    }

    private List<Top> getMonthTops(long time) {
        return encapsulateResult(time, blogReadRepository.top(time));
    }

    private List<Top> encapsulateResult(Long afterTime, List<Object[]> groupResult) {
        List<Top> tops = Lists.newArrayList();
        for (Object[] result : groupResult) {
            log.debug(format("group result :%s", Arrays.toString(result)));
            String groupName = result[0].toString();
            long count = (Long) result[1];

            List<Blog> blogs = blogReadRepository.topDetail(afterTime, groupName);
            for (Blog blog : blogs) {
                List<BlogView> blogViews = blogViewReadRepository.findByBlogId(blog.getId());
                blog.statisticsViewByBlogViews(blogViews);
            }
            Top top = new Top(groupName, count, blogs);
            top.calcView();
            tops.add(top);
        }
        sortTopsByView(tops);
        return tops;
    }

    private void sortTopsByView(List<Top> tops) {
        Collections.sort(tops, new Comparator<Top>() {
            @Override
            public int compare(Top top1, Top top2) {
                return (int) (top2.getView() - top1.getView());
            }
        });
    }

}
