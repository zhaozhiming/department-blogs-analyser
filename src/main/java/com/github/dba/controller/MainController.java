package com.github.dba.controller;

import com.github.dba.model.Author;
import com.github.dba.model.Blog;
import com.github.dba.util.DbaUtil;
import com.google.common.collect.Lists;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import static java.lang.Integer.valueOf;

@Controller
public class MainController {
    private static final Log log = LogFactory.getLog(MainController.class);
    private static final String TOP_TEXT = "[置顶]";

    @Value("${urls}")
    private String urls;

    @RequestMapping(value = "/parse", method = RequestMethod.GET)
    public
    @ResponseBody
    String parse() throws Exception {
        log.debug("parse url start");
        String[] urlArray = urls.split(",");
        log.debug("urls:" + Arrays.toString(urlArray));

        List<Blog> blogList = Lists.newArrayList();
        for (String url : urlArray) {
            Document doc = Jsoup.connect(url).userAgent("Mozilla").get();
            if (url.contains("csdn")) {
                blogList.addAll(fetchCsdnBlog(doc, "article_toplist"));
                blogList.addAll(fetchCsdnBlog(doc, "article_list"));
            } else {
                blogList.addAll(fetchIteyeBlog(doc, url));
            }
        }

        log.debug(blogList);
        log.debug("parse url finish");
        return "hehe";
    }

    private List<Blog> fetchIteyeBlog(Document doc, String url) throws Exception {
        List<Blog> blogList = Lists.newArrayList();
        Elements blogs = doc.select("#main div.blog_main");
        for (Element blog : blogs) {
            Element titleElement = blog.select("div.blog_title h3 a").get(0);
            String title = fetchTitle(titleElement);
            String link = url + titleElement.attr("href");
            String blogId = fetchBlogId(link);
            Elements tags = blog.select("div.blog_title div.news_tag a");
            Author author = getAuthor(tags);

            String time = DbaUtil.parseIteyeTime(
                    blog.select("div.blog_bottom li.date").get(0).text());

            int view = fetchNumber(
                    blog.select("div.blog_bottom li").get(1).text());
            int comment = fetchNumber(
                    blog.select("div.blog_bottom li").get(2).text());
            blogList.add(new Blog(title, link, view, comment, time, author, blogId, "iteye"));
        }
        return blogList;
    }


    private List<Blog> fetchCsdnBlog(Document doc, String elementId) throws Exception {
        List<Blog> blogList = Lists.newArrayList();
        Elements blogs = doc.select(String.format("#%s div.list_item.list_view", elementId));

        for (Element blog : blogs) {
            Element titleLink = blog.select("div.article_title span.link_title a").get(0);
            String link = titleLink.attr("href");
            String title = fetchTitle(titleLink);
            String blogId = fetchBlogId(link);
            String time = blog.select("div.article_manage span.link_postdate").get(0).text();

            int view = fetchNumber(blog.select(
                    "div.article_manage span.link_view").get(0).text());

            int comment = fetchNumber(blog.select(
                    "div.article_manage span.link_comments").get(0).text());

            Document detailDoc = Jsoup.connect(link).get();
            Elements tags = detailDoc.select("#article_details div.tag2box a");
            Author author = getAuthor(tags);
            blogList.add(new Blog(title, link, view, comment, time, author, blogId, "csdn"));
        }
        return blogList;
    }

    private String fetchBlogId(String link) {
        String[] parts = link.split("/");
        return parts[parts.length - 1];
    }

    private Author getAuthor(Elements tags) {
        if (tags.size() == 0) return Author.defaultAuthor();
        return new Author(tags.get(tags.size() - 1).text());
    }

    private String fetchTitle(Element titleLink) {
        String title = titleLink.text();
        if (title.contains(TOP_TEXT)) {
            return title.replace(TOP_TEXT, "").trim();
        }
        return title;
    }

    private int fetchNumber(String source) {
        return valueOf(regex(source, Pattern.compile("\\d+")));
    }

    private String regex(String source, Pattern compile) {
        return DbaUtil.fetchNumber(source, compile, "can't find with regex");
    }

}
