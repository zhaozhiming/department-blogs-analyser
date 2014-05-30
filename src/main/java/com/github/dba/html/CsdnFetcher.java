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
public class CsdnFetcher {
    private static final Log log = LogFactory.getLog(CsdnFetcher.class);
    private static final double CSDN_PAGE_COUNT = 50d;
    public static final String CSDN_KEY_WORD = "csdn";
    private static final String CSDN_INDEX = "http://blog.csdn.net";

    @Autowired
    private BlogReadRepository blogReadRepository;

    @Autowired
    private AuthorService authorService;

    public BatchBlogs fetch(String url) throws Exception {
        int totalPage = getTotalPage(url + "?viewmode=contents");
        log.debug(format("total page: %s%n", totalPage));

        BatchBlogs batchBlogs = new BatchBlogs();
        for (int i = 1; i <= totalPage; i++) {
            batchBlogs.addAllBatchBlogs(fetchPage(format("%s/article/list/%d?viewmode=contents", url, i)));
        }
        return batchBlogs;
    }

    private int getTotalPage(String url) throws Exception {
        Document doc = fetchUrlDoc(url);
        if (doc == null) return 1;

        Elements statistics = doc.select("#blog_statistics li");
        int total = 0;
        for (int i = 0; i <= 2; i++) {
            total += fetchNumber(statistics.get(i).text());
        }
        return (int) Math.ceil(total / CSDN_PAGE_COUNT);
    }

    private BatchBlogs fetchPage(String url) throws Exception {
        Document doc = fetchUrlDoc(url);
        if (doc == null) return new BatchBlogs();

        BatchBlogs batchBlogs = new BatchBlogs();
        batchBlogs.addAllBatchBlogs(fetchBlogs(doc, "article_toplist"));
        batchBlogs.addAllBatchBlogs(fetchBlogs(doc, "article_list"));
        return batchBlogs;
    }

    private BatchBlogs fetchBlogs(Document doc, String elementId) throws Exception {
        BatchBlogs batchBlogs = new BatchBlogs();
        Elements blogs = doc.select(format("#%s div.list_item.list_view", elementId));
        log.debug("blog size:" + blogs.size());

        for (Element blogElement : blogs) {
            Element titleLink = blogElement.select("div.article_title span.link_title a").get(0);
            String link = String.format("%s%s", CSDN_INDEX, titleLink.attr("href"));
            log.debug(format("blog detail link:%s", link));

            String title = fetchTitle(titleLink);
            String blogId = fetchBlogId(link);
            long time = parseTimeStringToLong(blogElement.select("div.article_manage span.link_postdate").get(0).text());

            int view = fetchNumber(blogElement.select(
                    "div.article_manage span.link_view").get(0).text());

            int comment = fetchNumber(blogElement.select(
                    "div.article_manage span.link_comments").get(0).text());

            Document detailDoc = fetchUrlDoc(link);

            Elements tags = detailDoc.select("#article_details div.tag2box a");
            Author author = authorService.fetchAuthor(tags);

            Blog blog = new Blog(title, link, view, comment, time, author, blogId, CSDN_KEY_WORD);
            Blog result = blogReadRepository.findByBlogIdAndWebsite(blogId, CSDN_KEY_WORD);
            if (result != null) {
                blog.setId(result.getId());
                batchBlogs.addUpdateBlogs(blog);
                continue;
            }

            batchBlogs.addInsertBlogs(blog);
        }
        return batchBlogs;
    }

    public int fetchView(String url) throws Exception {
        Document doc = fetchUrlDoc(url);
        if (doc == null) return -1;

        return fetchNumber(doc.select(
                "#article_details div.article_manage span.link_view").get(0).text());
    }
}
