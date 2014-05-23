package com.github.dba.html;

import com.github.dba.model.Author;
import com.github.dba.model.BatchBlogs;
import com.github.dba.model.Blog;
import com.github.dba.repo.read.BlogReadRepository;
import com.github.dba.service.AuthorService;
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

    @Autowired
    private BlogReadRepository blogReadRepository;

    @Autowired
    private AuthorService authorService;

    public BatchBlogs fetch(String url) throws Exception {
        int totalPage = getTotalPage(url);
        BatchBlogs batchBlogs = new BatchBlogs();
        for (int i = 1; i <= totalPage; i++) {
            batchBlogs.addAllBatchBlogs(fetchBlogs(format("%s/?page=%d", url, i)));
        }
        return batchBlogs;
    }

    private int getTotalPage(String url) throws Exception {
        Document doc = fetchUrlDoc(url);
        int total = fetchNumber(doc.select("#blog_menu a").get(0).text());
        return (int) Math.ceil(total / ITEYE_PAGE_COUNT);
    }

    private BatchBlogs fetchBlogs(String url) throws Exception {
        BatchBlogs batchBlogs = new BatchBlogs();
        Document doc = fetchUrlDoc(url);

        Elements blogs = doc.select("#main div.blog_main");
        log.debug("blog size:" + blogs.size());

        for (Element blog : blogs) {
            Element titleElement = blog.select("div.blog_title h3 a").get(0);
            String title = fetchTitle(titleElement);
            String link = url.substring(0, url.lastIndexOf("/")) + titleElement.attr("href");
            log.debug(format("blog detail link:%s", link));
            String blogId = fetchBlogId(link);
            Elements tags = blog.select("div.blog_title div.news_tag a");
            Author author = authorService.fetchAuthor(tags);

            long time = parseTimeStringToLong(parseIteyeTime(
                    blog.select("div.blog_bottom li.date").get(0).text()));

            int view = fetchNumber(
                    blog.select("div.blog_bottom li").get(1).text());
            int comment = fetchNumber(
                    blog.select("div.blog_bottom li").get(2).text());

            Blog result = blogReadRepository.findByBlogIdAndWebsite(blogId, ITEYE_KEY_WORD);
            if (result != null) {
                result.setTitle(title);
                result.setView(view);
                result.setComment(comment);
                result.setAuthor(author);
                batchBlogs.addUpdateBlogs(result);
                continue;
            }

            batchBlogs.addInsertBlogs(new Blog(title, link, view, comment, time, author, blogId, ITEYE_KEY_WORD));
        }

        return batchBlogs;
    }
}
