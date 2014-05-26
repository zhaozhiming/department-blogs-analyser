package com.github.dba.model;

import javax.persistence.*;

@Entity(name = "blog_views")
public class BlogView {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private Long blogID;

    @Basic
    private int total;

    @Basic
    private int increment;

    @Basic
    private Long blogTime;

    @Basic
    private Long recordTime;

    public BlogView() {
    }

    public BlogView(Long blogID, int total, int increment, Long blogTime, Long recordTime) {
        this.blogID = blogID;
        this.total = total;
        this.increment = increment;
        this.blogTime = blogTime;
        this.recordTime = recordTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBlogID() {
        return blogID;
    }

    public void setBlogID(Long blogID) {
        this.blogID = blogID;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public Long getBlogTime() {
        return blogTime;
    }

    public void setBlogTime(Long blogTime) {
        this.blogTime = blogTime;
    }

    public Long getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Long recordTime) {
        this.recordTime = recordTime;
    }

    @Override
    public String toString() {
        return String.format("BlogView{recordTime=%d, blogTime=%d, " +
                "increment=%d, total=%d, blogID=%d}",
                recordTime, blogTime, increment, total, blogID);
    }
}

