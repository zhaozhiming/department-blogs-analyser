package com.github.dba.model;

import com.google.common.collect.Lists;

import java.util.List;

public class BatchBlogs {
    private List<Blog> updateBlogs = Lists.newArrayList();
    private List<Blog> insertBlogs = Lists.newArrayList();

    public List<Blog> getUpdateBlogs() {
        return updateBlogs;
    }

    public List<Blog> getInsertBlogs() {
        return insertBlogs;
    }

    public void addUpdateBlogs(Blog blog) {
        updateBlogs.add(blog);
    }

    public void addInsertBlogs(Blog blog) {
        insertBlogs.add(blog);
    }

    public void addAllBatchBlogs(BatchBlogs batchBlogs) {
        updateBlogs.addAll(batchBlogs.getUpdateBlogs());
        insertBlogs.addAll(batchBlogs.getInsertBlogs());
    }
}
