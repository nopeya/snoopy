package com.nopeya.fooapi.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

public class DateUtils {

    public final static String FORMAT_DATE = "yyyy-MM-dd";
    public final static String FORMAT_TIME = "hh:mm:ss";
    public final static String FORMAT_DATETIME = FORMAT_DATE + " " + FORMAT_TIME;

    public static String formatDateTime(Date date) {
        return format(date, FORMAT_DATETIME);
    }

    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }
}
