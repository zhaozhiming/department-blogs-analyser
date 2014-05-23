package com.github.dba.model;

import javax.persistence.*;

@Entity(name = "blogs")
@Table(name = "blogs", uniqueConstraints = @UniqueConstraint(columnNames = {"blogId", "website"}))
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Basic
    private String title;
    @Basic
    private String link;
    @Basic
    private int view;
    @Basic
    private int comment;
    @Basic
    private Long time;
    @Embedded
    private Author author;
    @Basic
    private String blogId;
    @Basic
    private String website;

    public Blog() {
    }

    public Blog(String title, String link, int view,
                int comment, Long time, Author author, String blogId, String website) {
        this.title = title;
        this.link = link;
        this.view = view;
        this.comment = comment;
        this.time = time;
        this.author = author;
        this.blogId = blogId;
        this.website = website;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String toString() {
        return String.format("Blog{title='%s', link='%s', view=%d, comment=%d, " +
                "time=%d, author=%s, blogId='%s', website='%s'}",
                title, link, view, comment, time, author, blogId, website);
    }
}
