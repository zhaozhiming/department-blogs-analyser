package com.github.dba.controller;

import com.github.dba.html.CsdnFetcher;
import com.github.dba.html.IteyeFetcher;
import com.github.dba.model.Blog;
import com.github.dba.model.DepGroup;
import com.github.dba.model.DepMember;
import com.github.dba.repo.BlogRepository;
import com.github.dba.repo.DepGroupRepository;
import com.github.dba.repo.DepMemberRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    private DepGroupRepository depGroupRepository;

    @Autowired
    private DepMemberRepository depMemberRepository;

    @Autowired
    private BlogRepository blogRepository;

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

        for (String url : urlArray) {
            if (url.contains(CsdnFetcher.CSDN_KEY_WORD)) {
                csdnFetcher.fetch(url);
                continue;
            }
            iteyeFetcher.fetch(url);
        }
        log.debug("blog fetch finish");
    }

    @RequestMapping(value = "/group/create", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void createGroups() {
        log.debug("create groups start");
        log.debug("group names:" + groups);
        depGroupRepository.deleteAll();

        String[] groupNames = groups.split(",");
        for (String groupName : groupNames) {
            String[] texts = groupName.split("-");
            depGroupRepository.save(new DepGroup(texts[0], texts[1]));
        }
        log.debug("create groups finish");
    }

    @RequestMapping(value = "/member/create", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void createMembers() {
        log.debug("create members start");
        log.debug("member names:" + members);
        depMemberRepository.deleteAll();

        String[] membersArray = members.split(",");
        for (String member : membersArray) {
            String[] texts = member.split("-");
            depMemberRepository.save(new DepMember(texts[0], texts[1], texts[2]));
        }
        log.debug("create members finish");
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    public
    @ResponseBody
    String search() throws Exception {
        log.debug("search blog start");
        List<Blog> blogs = blogRepository.findAll();
        String resultArrayJson = mapper.writeValueAsString(blogs);
        log.debug(format("resultArrayJson: %s", resultArrayJson));
        log.debug("search blog finish");
        return resultArrayJson;
    }
}
