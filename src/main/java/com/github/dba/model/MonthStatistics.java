package com.github.dba.model;

import java.util.List;

public class MonthStatistics {
    private long month;
    private List<StatisticsDetail> groups;

    public MonthStatistics() {
    }

    public MonthStatistics(long month, List<StatisticsDetail> groups) {
        this.month = month;
        this.groups = groups;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public List<StatisticsDetail> getGroups() {
        return groups;
    }

    public void setGroups(List<StatisticsDetail> groups) {
        this.groups = groups;
    }
}
