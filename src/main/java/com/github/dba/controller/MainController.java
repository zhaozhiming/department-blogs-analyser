package com.github.dba.controller;

import com.github.dba.html.CsdnFetcher;
import com.github.dba.html.IteyeFetcher;
import com.github.dba.model.DepGroup;
import com.github.dba.repo.DepGroupRepository1;
import com.github.dba.repo.DepMemberRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Arrays;

@Controller
public class MainController {
    private static final Log log = LogFactory.getLog(MainController.class);

    @Autowired
    private CsdnFetcher csdnFetcher;

    @Autowired
    private IteyeFetcher iteyeFetcher;

    @Autowired
    private DepGroupRepository1 depGroupRepository1;

    @Autowired
    private DepMemberRepository depMemberRepository;

    @Value("${urls}")
    private String urls;

    @Value("${groups}")
    private String groups;

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
        depGroupRepository1.deleteAll();

        String[] groupNames = groups.split(",");
        for (String groupName : groupNames) {
            String[] texts = groupName.split("-");
            depGroupRepository1.save(new DepGroup(texts[0], texts[1]));
        }
        log.debug("create groups end");
    }

    @RequestMapping(value = "/member/create", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void createMembers() {
        log.debug("create members start");
        depMemberRepository.createDepMembers();
        log.debug("create members end");
    }
}
