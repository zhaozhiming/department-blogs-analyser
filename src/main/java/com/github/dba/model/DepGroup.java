package com.github.dba.model;

import javax.persistence.*;

@Entity
@Table(name="dep_groups", uniqueConstraints=@UniqueConstraint(columnNames={"name"}))
public class DepGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Basic
    private String name;

    public DepGroup() {
    }

    public DepGroup(String name) {
        this.name = name;
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

