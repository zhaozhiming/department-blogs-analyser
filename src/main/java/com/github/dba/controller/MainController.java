package com.github.dba.controller;

import com.github.dba.html.CsdnFetcher;
import com.github.dba.html.IteyeFetcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

@Controller
public class MainController {
    private static final Log log = LogFactory.getLog(MainController.class);
    private static final String CSDN_KEY_WORD = "csdn";

    @Autowired
    private CsdnFetcher csdnFetcher;

    @Autowired
    private IteyeFetcher iteyeFetcher;

    @Value("${urls}")
    private String urls;

    @RequestMapping(value = "/parse", method = RequestMethod.GET)
    public
    @ResponseBody
    String parse() throws Exception {
        log.debug("parse url start");
        String[] urlArray = urls.split(",");
        log.debug("urls:" + Arrays.toString(urlArray));

        for (String url : urlArray) {
            if (url.contains(CSDN_KEY_WORD)) {
                csdnFetcher.fetch(url);
                continue;
            }
            iteyeFetcher.fetch(url);
        }

        log.debug("parse url finish");
        return "hehe";
    }
}
