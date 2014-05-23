package com.github.dba.model;

import java.util.List;

public class Top {
    private String groupName;
    private long count;
    private long view;
    private List<Blog> blogs;

    public Top() {
    }

    public Top(String groupName, long count, long view, List<Blog> blogs) {
        this.groupName = groupName;
        this.count = count;
        this.view = view;
        this.blogs = blogs;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getView() {
        return view;
    }

    public void setView(long view) {
        this.view = view;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }
}
