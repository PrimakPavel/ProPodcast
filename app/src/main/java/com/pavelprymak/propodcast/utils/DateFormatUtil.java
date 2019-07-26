package com.pavelprymak.propodcast.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateFormatUtil {
    public static final int MS_AT_SEC =1000;
    public static final SimpleDateFormat PUBLISH_DATE_FORMAT = new SimpleDateFormat("yy.MM.dd", Locale.ENGLISH);

    public static String formatTimeHHmmss(int sec) {
        int hour = sec % 86400 / 3600;
        int min = sec % 3600 / 60;
        int second = sec % 60;

        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, min, second, Locale.ENGLISH);
        }
        if (min > 0) {
            return String.format("00:%02d:%02d", min, second, Locale.ENGLISH);
        }

        return String.format("%02d sec", second, Locale.ENGLISH);
    }
}
