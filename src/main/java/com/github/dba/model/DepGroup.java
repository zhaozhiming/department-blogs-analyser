package com.github.dba.model;

import javax.persistence.*;

@Entity(name = "dep_groups")
public class DepGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String groupShort;

    @Basic
    private String name;

    public DepGroup() {
    }

    public DepGroup(String groupShort, String name) {
        this.groupShort = groupShort;
        this.name = name;
    }

    public String getGroupShort() {
        return groupShort;
    }

    public void setGroupShort(String groupShort) {
        this.groupShort = groupShort;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

