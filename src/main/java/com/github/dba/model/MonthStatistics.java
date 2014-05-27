package com.github.dba.model;

import java.util.List;

public class MonthStatistics {
    private long month;
    private List<Top> tops;

    public MonthStatistics() {
    }

    public MonthStatistics(long month, List<Top> tops) {
        this.month = month;
        this.tops = tops;
    }

    public long getMonth() {
        return month;
    }

    public void setMonth(long month) {
        this.month = month;
    }

    public List<Top> getTops() {
        return tops;
    }

    public void setTops(List<Top> tops) {
        this.tops = tops;
    }
}
