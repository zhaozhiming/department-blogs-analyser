package com.github.dba.util;

import com.github.dba.html.CsdnFetcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import static java.lang.String.format;

public class DbaUtil {
    private static final Log log = LogFactory.getLog(DbaUtil.class);
    public static final String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String TOP_TEXT = "[置顶]";

    public static long parseTimeStringToLong(String time) throws ParseException {
        return parseTimeStringToLong(time, DEFAULT_TIME_FORMAT);
    }

    public static long parseTimeStringToLong(String time, String dateFormat) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        Date date = format.parse(time);
        return date.getTime();
    }

    public static long parseIteyeTime(String source) throws ParseException {
        if (source.contains("不到")) {
            return DateTime.now().getMillis();
        }

        if (source.contains("分钟前")) {
            return Time.MINUTE.parseTime(source);
        }

        if (source.contains("小时前")) {
            return Time.HOUR.parseTime(source);
        }

        if ("昨天".equals(source)) {
            return new DateTime().minusDays(1).getMillis();
        }

        if ("前天".equals(source)) {
            return new DateTime().minusDays(2).getMillis();
        }

        return parseTimeStringToLong(source);
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
        try {
            return Jsoup.connect(url).userAgent("Mozilla").get();
        } catch (Exception e) {
            log.debug(format("connect url(%s) failed:%s", url, e.getMessage()));
            return null;
        }
    }

    public static int fetchNumber(String source) {
        return valueOf(regex(source, Pattern.compile("\\d+")));
    }

    public static Long currentMonthFirstDay() {
        return DateTime.now().withDayOfMonth(1).withHourOfDay(0).getMillis();
    }

    private static String regex(String source, Pattern compile) {
        return DbaUtil.fetchNumber(source, compile, "can't find number with regex");
    }

    public static boolean isCsdn(String url) {
        return url.contains(CsdnFetcher.CSDN_KEY_WORD);
    }

    enum Time {
        HOUR {
            @Override
            protected Long minusTime(int num) {
                return new DateTime().minusHours(num).getMillis();
            }
        }, MINUTE {
            @Override
            protected Long minusTime(int num) {
                return new DateTime().minusMinutes(num).getMillis();
            }
        };

        public Long parseTime(String source) {
            String num = fetchNumber(source, Pattern.compile("\\d+"),
                    "parse iteye time error, not found any number");
            return minusTime(Integer.valueOf(num));
        }

        protected abstract Long minusTime(int num);
    }

}
