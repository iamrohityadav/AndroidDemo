package com.mainli.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by pqpo on 2017/11/24.
 */
public class DateFileFormatter1 {
    private SimpleDateFormat simpleDateFormat = null;
    private Date date = new Date();

    public DateFileFormatter1() {
        this("MM-dd HH:mm:ss");
    }

    public DateFileFormatter1(String pattern) {
        simpleDateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
    }

    public synchronized String format(int logLevel, String tag, String msg) {
        date.setTime(System.currentTimeMillis());
        return String.format("%s %s/%s: %s\n", simpleDateFormat.format(date), Level.getShortLevelName(logLevel), tag, msg);
    }
}