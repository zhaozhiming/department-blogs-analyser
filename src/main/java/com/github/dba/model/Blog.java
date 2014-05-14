package com.github.dba.model;

public class Blog {
    private String title;
    private String link;
    private int view;
    private int comment;
    private String time;
    private Author author;
    private String id;
    private String website;

    public Blog(String title, String link, int view,
                int comment, String time, Author author, String id, String website) {
        this.title = title;
        this.link = link;
        this.view = view;
        this.comment = comment;
        this.time = time;
        this.author = author;
        this.id = id;
        this.website = website;
    }

    @Override
    public String toString() {
        return String.format("Blog{title='%s', link='%s', view=%d, comment=%d, " +
                "time='%s', author=%s, id='%s', website='%s'}",
                title, link, view, comment, time, author, id, website);
    }
}
