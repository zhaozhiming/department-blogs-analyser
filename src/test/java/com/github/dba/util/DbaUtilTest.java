package com.github.dba.util;

import org.joda.time.DateTime;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.github.dba.util.DbaUtil.DEFAULT_TIME_FORMAT;
import static com.github.dba.util.DbaUtil.parseIteyeTime;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class DbaUtilTest {

    @Test
    public void should_return_current_time_when_given_1_minute_not_enough() throws Exception {
        assertThat(dateLongToString(parseIteyeTime("不到1分钟")),
                is(DateTime.now().toString(DEFAULT_TIME_FORMAT)));
    }

    @Test
    public void should_return_correct_time_when_given_x_minute_before() throws Exception {
        assertThat(dateLongToString(parseIteyeTime("1 分钟前")),
                is(new DateTime().minusMinutes(1).toString(DEFAULT_TIME_FORMAT)));
        assertThat(dateLongToString(parseIteyeTime("15 分钟前")),
                is(new DateTime().minusMinutes(15).toString(DEFAULT_TIME_FORMAT)));
        assertThat(dateLongToString(parseIteyeTime("30 分钟前")),
                is(new DateTime().minusMinutes(30).toString(DEFAULT_TIME_FORMAT)));
        assertThat(dateLongToString(parseIteyeTime("59 分钟前")),
                is(new DateTime().minusMinutes(59).toString(DEFAULT_TIME_FORMAT)));
    }

    @Test
    public void should_return_correct_time_when_given_x_hour_before() throws Exception {
        assertThat(dateLongToString(parseIteyeTime("1 小时前")),
                is(new DateTime().minusHours(1).toString(DEFAULT_TIME_FORMAT)));
        assertThat(dateLongToString(parseIteyeTime("6 小时前")),
                is(new DateTime().minusHours(6).toString(DEFAULT_TIME_FORMAT)));
        assertThat(dateLongToString(parseIteyeTime("13 小时前")),
                is(new DateTime().minusHours(13).toString(DEFAULT_TIME_FORMAT)));
        assertThat(dateLongToString(parseIteyeTime("23 小时前")),
                is(new DateTime().minusHours(23).toString(DEFAULT_TIME_FORMAT)));
    }

    @Test
    public void should_return_correct_time_when_given_yesterday() throws Exception {
        assertThat(dateLongToString(parseIteyeTime("昨天")),
                is(new DateTime().minusDays(1).toString(DEFAULT_TIME_FORMAT)));
    }

    @Test
    public void should_return_correct_time_when_given_the_day_before_yesterday() throws Exception {
        assertThat(dateLongToString(parseIteyeTime("前天")),
                is(new DateTime().minusDays(2).toString(DEFAULT_TIME_FORMAT)));
    }

    @Test
    public void should_return_correct_time_when_given_exact_time() throws Exception {
        assertThat(dateLongToString(parseIteyeTime("2014-04-24 09:58")), is("2014-04-24 09:58"));
    }

    private String dateLongToString(Long time) {
        return new SimpleDateFormat(DEFAULT_TIME_FORMAT).format(new Date(time));
    }

}