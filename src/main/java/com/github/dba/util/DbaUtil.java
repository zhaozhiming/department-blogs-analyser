package com.github.dba.util;

import org.joda.time.DateTime;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbaUtil {
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String NOW = DateTime.now().toString(DEFAULT_TIME_FORMAT);

    public static String parseIteyeTime(String source) {
        if (source.contains("不到")) {
            return NOW;
        }

        if (source.contains("分钟前")) {
            return Time.MINUTE.parseTime(source);
        }

        if (source.contains("小时前")) {
            return Time.HOUR.parseTime(source);
        }

        if ("昨天".equals(source)) {
            return new DateTime().minusDays(1).toString(DEFAULT_TIME_FORMAT);
        }

        return source;
    }

    public static String fetchNumber(String source, Pattern compile, String error) {
        Matcher matcher = compile.matcher(source);
        if (matcher.find()) return matcher.group(0);

        throw new RuntimeException(error);
    }

    enum Time {
        HOUR {
            @Override
            protected String minusTime(int num) {
                return new DateTime().minusHours(num).toString(DEFAULT_TIME_FORMAT);
            }
        }, MINUTE {
            @Override
            protected String minusTime(int num) {
                return new DateTime().minusMinutes(num).toString(DEFAULT_TIME_FORMAT);
            }
        };

        public String parseTime(String source) {
            String num = fetchNumber(source, Pattern.compile("\\d+"),
                    "parse iteye time error, not found any number");
            return minusTime(Integer.valueOf(num));
        }
        protected abstract String minusTime(int num);
    }

}
