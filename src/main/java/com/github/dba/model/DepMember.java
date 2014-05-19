package com.github.dba.model;

import javax.persistence.*;

@Entity(name = "dep_members")
public class DepMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String memberShort;

    @Basic
    private String groupShort;

    @Basic
    private String name;

    public DepMember() {
    }

    public DepMember(String memberShort, String groupShort, String name) {
        this.memberShort = memberShort;
        this.groupShort = groupShort;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberShort() {
        return memberShort;
    }

    public void setMemberShort(String memberShort) {
        this.memberShort = memberShort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupShort() {
        return groupShort;
    }

    public void setGroupShort(String groupShort) {
        this.groupShort = groupShort;
    }
}
