package com.github.dba.controller;

import com.github.dba.html.CsdnFetcher;
import com.github.dba.html.IteyeFetcher;
import com.github.dba.model.BatchBlogs;
import com.github.dba.model.Blog;
import com.github.dba.model.DepGroup;
import com.github.dba.model.DepMember;
import com.github.dba.repo.read.BlogReadRepository;
import com.github.dba.repo.write.BlogWriteRepository;
import com.github.dba.repo.write.DepGroupWriteRepository;
import com.github.dba.repo.write.DepMemberWriteRepository;
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

    @Value("${urls}")
    private String urls;

    @Value("${groups}")
    private String groups;

    @Value("${members}")
    private String members;
    private final ObjectMapper mapper = new ObjectMapper();

    @RequestMapping(value = "/blog/fetch", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void blogFetch() throws Exception {
        log.debug("blog fetch start");
        String[] urlArray = urls.split(",");
        log.debug("urls:" + Arrays.toString(urlArray));

        BatchBlogs batchBlogs = new BatchBlogs();
        for (String url : urlArray) {
            if (url.contains(CsdnFetcher.CSDN_KEY_WORD)) {
                batchBlogs.addAllBatchBlogs(csdnFetcher.fetch(url));
                continue;
            }
            batchBlogs.addAllBatchBlogs(iteyeFetcher.fetch(url));
        }

        List<Blog> updateBlogs = batchBlogs.getUpdateBlogs();
        for (Blog updateBlog : updateBlogs) {
            blogWriteRepository.updateBlogFor(updateBlog.getId(), updateBlog.getTitle(),
                    updateBlog.getView(), updateBlog.getComment(), updateBlog.getAuthor().getGroupName(),
                    updateBlog.getAuthor().getName());
        }

        List<Blog> insertBlogs = batchBlogs.getInsertBlogs();
        for (Blog insertBlog : insertBlogs) {
            blogWriteRepository.save(insertBlog);
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

    @RequestMapping(value = "/statistics", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public
    @ResponseBody
    String statistics() throws Exception {
        log.debug("statistics blogs start");

        Long threeMonthAgo = DateTime.now().minusMonths(3).getMillis();
        List groups = blogReadRepository.statistics(0L);

        String resultArrayJson = mapper.writeValueAsString(groups);
        log.debug(format("resultArrayJson: %s", resultArrayJson));

        log.debug("statistics blogs finish");
        return resultArrayJson;
    }
}
