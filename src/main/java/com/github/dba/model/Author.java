package com.github.dba.model;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.select.Elements;

import javax.persistence.*;

@Embeddable
public class Author {
    private static final Log log = LogFactory.getLog(Author.class);

    @Basic
    private String groupName;
    @Basic
    private String name;

    public Author() {}

    public Author(String source) {
        String[] texts = source.split("-");
        if (texts.length != 2) {
            this.groupName = Group.W.group();
            this.name = "赵芝明";
        } else {
            this.groupName = fetchGroupName(texts[0]);
            this.name = texts[1];
        }
    }

    public static Author getAuthorBy(Elements tags) {
        if (tags.size() == 0) return Author.defaultAuthor();
        return new Author(tags.get(tags.size() - 1).text());
    }

    private static Author defaultAuthor() {
        return new Author("W-赵芝明");
    }

    private String fetchGroupName(String groupShort) {
        try {
            return Group.valueOf(groupShort).group();
        }catch (Exception e) {
            log.debug("not group match the name");
            return Group.W.group();
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String group) {
        this.groupName = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Author{name='%s', group='%s'}", name, groupName);
    }
}
