package com.github.dba.util;

import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.valueOf;

public class DbaUtil {
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String NOW = DateTime.now().toString(DEFAULT_TIME_FORMAT);
    private static final String TOP_TEXT = "[置顶]";

    public static long parseTimeStringToLong(String time) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_TIME_FORMAT);
        Date date = format.parse(time);
        return date.getTime();
    }

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

        if ("前天".equals(source)) {
            return new DateTime().minusDays(2).toString(DEFAULT_TIME_FORMAT);
        }

        return source;
    }

    public static String fetchNumber(String source, Pattern compile, String error) {
        Matcher matcher = compile.matcher(source);
        if (matcher.find()) return matcher.group(0);

        throw new RuntimeException(error);
    }

    public static String fetchBlogId(String link) {
        String[] parts = link.split("/");
        return parts[parts.length - 1];
    }

    public static String fetchTitle(Element titleLink) {
        String title = titleLink.text();
        if (title.contains(TOP_TEXT)) {
            return title.replace(TOP_TEXT, "").trim();
        }
        return title;
    }

    public static Document fetchUrlDoc(String url) throws IOException {
        return Jsoup.connect(url).userAgent("Mozilla").get();
    }

    public static int fetchNumber(String source) {
        return valueOf(regex(source, Pattern.compile("\\d+")));
    }

    private static String regex(String source, Pattern compile) {
        return DbaUtil.fetchNumber(source, compile, "can't find number with regex");
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
