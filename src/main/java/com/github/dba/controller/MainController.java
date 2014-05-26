package com.github.dba.controller;

import com.github.dba.html.CsdnFetcher;
import com.github.dba.html.IteyeFetcher;
import com.github.dba.model.*;
import com.github.dba.repo.read.BlogReadRepository;
import com.github.dba.repo.write.BlogViewWriteRepository;
import com.github.dba.repo.write.BlogWriteRepository;
import com.github.dba.repo.write.DepGroupWriteRepository;
import com.github.dba.repo.write.DepMemberWriteRepository;
import com.github.dba.service.MailService;
import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

import static com.github.dba.util.DbaUtil.currentMonthFirstDay;
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
        mailService.sendTops(getCurrentMonthTops());
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
            if (isCsdn(url)) {
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
                mapper.writeValueAsString(getCurrentMonthTops());
        log.debug(format("resultArrayJson: %s", resultArrayJson));

        log.debug("top blogs finish");
        return resultArrayJson;
    }

    @RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public
    @ResponseBody
    String statistics() throws Exception {
        log.debug("statistics blogs start");

        List<MonthStatistics> months = lastThreeMonthsStatistics();
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
        List<Blog> blogs = blogReadRepository.findAfterTime(threeMonthsAgo);

        long now = DateTime.now().getMillis();
        List<BlogView> blogViews = Lists.newArrayList();
        for (Blog blog : blogs) {
            Long blogId = blog.getId();
            Long blogTime = blog.getTime();
            int preView = blog.getView();
            String link = blog.getLink();
            int total = isCsdn(link) ?
                    csdnFetcher.fetchView(link) : iteyeFetcher.fetchView(link);
            int increment = total - preView;

            blogViews.add(new BlogView(blogId, total, increment, blogTime, now));
        }
        log.debug(format("blog views: %s", blogViews));
        blogViewWriteRepository.save(blogViews);

        log.debug("generate blog views finish");
    }

    private List<Top> getCurrentMonthTops() {
        Long currentMonthFirstDay = currentMonthFirstDay();
        List<Object[]> result = blogReadRepository.top(currentMonthFirstDay);

        return encapsulateResult(currentMonthFirstDay, result);
    }

    private List<MonthStatistics> lastThreeMonthsStatistics() {
        List<MonthStatistics> months = Lists.newArrayList();
        long currentMonthFirstDay = currentMonthFirstDay();

        months.add(getMonthStatisticsDetails(
                currentMonthFirstDay, DateTime.now().getMillis()));

        long lastMonthFirstDay =
                DateTime.now().minusMonths(1).withDayOfMonth(1).withHourOfDay(0).getMillis();
        months.add(getMonthStatisticsDetails(lastMonthFirstDay, currentMonthFirstDay));

        long beforeLastMonthFirstDay =
                DateTime.now().minusMonths(2).withDayOfMonth(1).withHourOfDay(0).getMillis();
        months.add(getMonthStatisticsDetails(
                beforeLastMonthFirstDay, lastMonthFirstDay));
        return months;
    }

    private MonthStatistics getMonthStatisticsDetails(long monthStartDay, long monthEndDay) {
        List<Object[]> result = blogReadRepository.statistics(monthStartDay, monthEndDay);

        List<StatisticsDetail> statisticsDetails = Lists.newArrayList();
        for (Object[] statistics : result) {
            log.debug(format("statistics result :%s", Arrays.toString(statistics)));
            String groupName = statistics[0].toString();
            long count = (Long) statistics[1];
            long view = (Long) statistics[2];

            statisticsDetails.add(new StatisticsDetail(groupName, count, view));
        }
        return new MonthStatistics(monthStartDay, statisticsDetails);
    }

    private List<Top> encapsulateResult(Long currentMonthFirstDay, List<Object[]> result) {
        List<Top> tops = Lists.newArrayList();
        for (Object[] top : result) {
            log.debug(format("group result :%s", Arrays.toString(top)));
            String groupName = top[0].toString();
            long count = (Long) top[1];
            long view = (Long) top[2];

            List<Blog> blogs = blogReadRepository.topDetail(currentMonthFirstDay, groupName);
            tops.add(new Top(groupName, count, view, blogs));
        }
        return tops;
    }

    private boolean isCsdn(String url) {
        return url.contains(CsdnFetcher.CSDN_KEY_WORD);
    }
}
