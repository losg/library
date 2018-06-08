package com.losg.library.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by losg on 2017/12/20.
 */

public class TimeUtils {

    public static String parseTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }


    public static String parseNoMinuteTime(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String getTime(String time) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parse = simpleDateFormat.parse(time);
            time = parse.getTime() + "";
        } catch (ParseException e) {
            time = "";
            e.printStackTrace();
        }

        if (TextUtils.isEmpty(time)) return "";
        long timeSecond = Long.parseLong(time) / 1000;
        long currentTime = System.currentTimeMillis() / 1000;
        if (timeSecond > currentTime) {
            return "刚刚";
        }
        int dTime = (int) (currentTime - timeSecond);
        if (dTime < 60) {
            //小于一分钟
            return "刚刚";
        } else if (dTime < 60 * 60) {
            //小于一小时
            return dTime / (60) + "分钟前";
        } else if (dTime < 60 * 60 * 24) {
            //小于一天
            return dTime / (60 * 60) + "小时前";
        } else if (dTime < 60 * 60 * 24 * 30) {
            //小于30天
            return dTime / (60 * 60 * 24) + "天前";
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeSecond * 1000);
            return parseNoMinuteTime(calendar.getTime());
        }
    }
}
