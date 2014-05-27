package com.github.dba.model;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class BlogTest {
    private static final BlogView LESS_THAN_1_MONTH_BLOG_VIEW = new BlogView(1L, 100, 100,
            DateTime.now().minusDays(1).getMillis(),
            DateTime.now().getMillis());
    private static final BlogView LARGER_THAN_1_MONTH_BLOG_VIEW = new BlogView(1L, 100, 100,
            DateTime.now().minusMonths(1).getMillis(),
            DateTime.now().getMillis());
    private static final BlogView LARGER_THAN_2_MONTH_BLOG_VIEW = new BlogView(1L, 100, 100,
            DateTime.now().minusMonths(2).getMillis(),
            DateTime.now().getMillis());

    @Test
    public void should_return_100_view_when_given_blog_view_with_100_and_less_than_1_month() throws Exception {
        List<BlogView> blogViews = Lists.newArrayList(LESS_THAN_1_MONTH_BLOG_VIEW);

        Blog blog = new Blog();
        blog.statisticsViewByBlogViews(blogViews);

        assertThat(blog.getView(), is(100));
    }

    @Test
    public void should_return_50_view_when_given_blog_view_with_100_and_larger_than_2_months() throws Exception {
        List<BlogView> blogViews = Lists.newArrayList(LARGER_THAN_1_MONTH_BLOG_VIEW);

        Blog blog = new Blog();
        blog.statisticsViewByBlogViews(blogViews);

        assertThat(blog.getView(), is(50));
    }

    @Test
    public void should_return_25_view_when_given_blog_view_with_100_and_larger_than_3_months() throws Exception {
        List<BlogView> blogViews = Lists.newArrayList(LARGER_THAN_2_MONTH_BLOG_VIEW);

        Blog blog = new Blog();
        blog.statisticsViewByBlogViews(blogViews);

        assertThat(blog.getView(), is(25));
    }

    @Test
    public void should_return_25_view_when_given_different_blog_views() throws Exception {
        List<BlogView> blogViews = Lists.newArrayList(LESS_THAN_1_MONTH_BLOG_VIEW,
                LARGER_THAN_1_MONTH_BLOG_VIEW, LARGER_THAN_2_MONTH_BLOG_VIEW);

        Blog blog = new Blog();
        blog.statisticsViewByBlogViews(blogViews);

        assertThat(blog.getView(), is(175));
    }

}