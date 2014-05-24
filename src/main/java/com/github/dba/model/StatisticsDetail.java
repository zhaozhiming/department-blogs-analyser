package com.github.dba.model;

public class StatisticsDetail {
    private String groupName;
    private long count;
    private long view;

    public StatisticsDetail() {
    }

    public StatisticsDetail(String groupName, long count, long view) {
        this.groupName = groupName;
        this.count = count;
        this.view = view;
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
}
