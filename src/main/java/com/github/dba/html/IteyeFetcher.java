package com.github.dba.html;

import com.github.dba.model.Author;
import com.github.dba.model.Blog;
import com.github.dba.repo.BlogRepository;
import com.github.dba.service.AuthorService;
import com.github.dba.util.DbaUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.github.dba.util.DbaUtil.*;
import static java.lang.String.format;

@Service
public class IteyeFetcher {
    private static final Log log = LogFactory.getLog(IteyeFetcher.class);
    private static final double ITEYE_PAGE_COUNT = 15d;
    private static final String ITEYE_KEY_WORD = "iteye";
    private boolean giveUp = false;

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private AuthorService authorService;

    public void fetch(String url) throws Exception {
        fetchBlogs(url);
        if (giveUp) return;

        double totalPage = getTotalPage(url);
        for (int i = 2; i <= totalPage; i++) {
            if (giveUp) return;

            fetchBlogs(format("%s/?page=%d", url, i));
        }
    }

    private double getTotalPage(String url) throws Exception {
        Document doc = fetchUrlDoc(url);
        int total = fetchNumber(doc.select("#blog_menu a").get(0).text());
        return Math.ceil(total / ITEYE_PAGE_COUNT);
    }

    private void fetchBlogs(String url) throws Exception {
        Document doc = fetchUrlDoc(url);
        Elements blogs = doc.select("#main div.blog_main");
        log.debug("blog size:" + blogs.size());

        for (Element blog : blogs) {
            Element titleElement = blog.select("div.blog_title h3 a").get(0);
            String title = fetchTitle(titleElement);
            String link = url + titleElement.attr("href");
            log.debug(format("blog detail link:%s", link));
            String blogId = fetchBlogId(link);
            Elements tags = blog.select("div.blog_title div.news_tag a");
            Author author = authorService.fetchAuthor(tags);

            String time = DbaUtil.parseIteyeTime(
                    blog.select("div.blog_bottom li.date").get(0).text());

            int view = fetchNumber(
                    blog.select("div.blog_bottom li").get(1).text());
            int comment = fetchNumber(
                    blog.select("div.blog_bottom li").get(2).text());

            if (blogRepository.isBlogExist(ITEYE_KEY_WORD, blogId)) {
                giveUp = true;
                return;
            }

            blogRepository.createBlog(new Blog(title, link, view, comment, time, author, blogId, ITEYE_KEY_WORD));
        }
    }
}
