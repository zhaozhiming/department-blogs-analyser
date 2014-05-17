package com.github.dba.model;

import javax.persistence.Basic;
import javax.persistence.Embeddable;

@Embeddable
public class Author {
    @Basic
    private String groupName;
    @Basic
    private String name;

    public Author() {
    }

    public Author(String groupName, String name) {
        this.groupName = groupName;
        this.name = name;
    }

    public static Author defaultAuthor() {
        return new Author("unknown", "unknown");
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
