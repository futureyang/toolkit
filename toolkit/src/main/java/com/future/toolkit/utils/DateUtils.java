package com.future.toolkit.utils;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.future.toolkit.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by yangqc on 2019/9/23
 *
 * @Author yangqc
 */
public class DateUtils {

    public static final String YEAR_MOUTH_DAY ="yyyy-MM-dd";
    public static final String DATE_TIME_DETAIL = "yyyy-MM-dd HH:mm:ss";

    public static final String FORMAT_UTC_PATTERN = "yyyy-MM-dd'T'HH:mm:ss+SSS Z";
    public static final String UTC_ZONE = "UTC";

    public static String format(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String format(Context context, @NonNull Date date, @StringRes int formatId) {
        String format = context.getResources().getString(formatId);
        return format(date, format);
    }

    public static String format(Date date) {
        return format(date, YEAR_MOUTH_DAY);
    }

    public static Calendar fromString(Context context, String string, @StringRes int formatId) {
        String format = context.getResources().getString(formatId);
        return fromString(string, format);
    }

    public static String formatDetail(long timeStamp){
        return format(new Date(timeStamp), DATE_TIME_DETAIL);
    }

    public static Calendar fromString(String string, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getDefault());
        try {
            Date date = dateFormat.parse(string);
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 yyyy年MM月dd日格式的日期
     * @return e.g.
     */
    public static String getCurrentDayString(Context context){
        return format(context,new Date(), R.string.dateformat_date_with_year_zh);
    }

    /**
     * 当天是周几
     * @param context
     * @return 星期一 星期二 ...
     */
    public static String getWeekOfDate(Context context) {
        String[] weekDays = context.getResources().getStringArray(R.array.WeekDays);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return weekDays[w];
    }


    /**
     *  是否是当天 true 是 false 否
     */
    public static boolean isToday(String date){
        String nowDay = getCurrentDate();
        return date.equals(nowDay);
    }


    public static String getCurrentDate(){

        SimpleDateFormat sf = new SimpleDateFormat(YEAR_MOUTH_DAY);
        //获取今天的日期
        String nowDay = sf.format(System.currentTimeMillis());

        return nowDay;
    }

    public static String format2YMD(long l){
        SimpleDateFormat sf = new SimpleDateFormat(YEAR_MOUTH_DAY);
        return sf.format(l);
    }

    public static String getTimeSpanWithUtc(String utcTime) {
        if (TextUtils.isEmpty(utcTime)) {
            return "";
        }
        Date date = getDateFromUtc(utcTime);
        if (date == null) {
            return "";
        }
        return getTimeSpan(date);
    }

    public static String getyyyyMMddWithUtc(String utcTime) {
        if (TextUtils.isEmpty(utcTime)) {
            return "";
        }
        Date date = getDateFromUtc(utcTime);
        if (date == null) {
            return "";
        }
        return format(date);
    }

    public static Date getDateFromUtc(String utcTime) {
        if (TextUtils.isEmpty(utcTime)){
            return null;
        }
        utcTime = utcTime + " " + UTC_ZONE;
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_UTC_PATTERN);
        dateFormat.setTimeZone(TimeZone.getTimeZone(UTC_ZONE));// 时区定义并进行时间获取
        Date gpsUTCDate = null;
        try {
            gpsUTCDate = dateFormat.parse(utcTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return gpsUTCDate;
    }

    public static long getTimestampFromUtc(String utcTime) {
        Date date = getDateFromUtc(utcTime);
        return date != null ? date.getTime() : 0L;
    }

    public static String dateDistance(Long start, Long end) {
        boolean hasMinute = false;
        if (start == null || end == null) {
            return "";
        }
        long timeLong = (end - start) / 1000;
        if (timeLong <= 0) {
            return "";
        }
        long day = timeLong / 60 / 60 / 24;
        timeLong -= day * 60 * 60 * 24;
        long hour = timeLong / 60 / 60;
        timeLong -= hour * 60 * 60;
        long minute = timeLong / 60;
        long second = timeLong - minute * 60;
        String res = "";
        if (day > 0) {
            res += day + "天";
        }
        if (hour > 0) {
            res += hour + "小时";
        }
        if (minute > 0) {
            res += minute + "分";
            hasMinute = true;
        }
        if (!hasMinute) {
            if (second > 0) {
                res += second + "秒";
            }
        }
        res += "后响铃";
        return res;
    }


    public static String dateDistanceForTabFakeCalendar(Calendar tabCalendar, Calendar nextCalendar) {
        final Calendar now = Calendar.getInstance(Locale.getDefault());
        Calendar fakeNextCalendar = Calendar.getInstance();
        fakeNextCalendar.setTimeInMillis(nextCalendar.getTimeInMillis());
        int diff = daysCount(tabCalendar, nextCalendar);
        fakeNextCalendar.add(Calendar.DAY_OF_YEAR, diff);
        if (now.after(fakeNextCalendar)) {
            return "已执行";
        }
        if(now.before(fakeNextCalendar) && fakeNextCalendar.before(nextCalendar)){
            return "已跳过";
        }
        return dateDistance(now.getTimeInMillis(), fakeNextCalendar.getTimeInMillis());
    }

    /**
     * 获取同一年/或连续两年的两个calendar中的间隔天数
     * */
    public static int daysCount(Calendar tabCalendar, Calendar nextCalendar) {
        int diff = 0;
        switch (yearsComparison(tabCalendar, nextCalendar)) {
            case 0:
                diff = tabCalendar.get(Calendar.DAY_OF_YEAR) - nextCalendar.get(Calendar.DAY_OF_YEAR);
                break;
            case 1:
                diff = leftDaysOfYear(nextCalendar) + tabCalendar.get(Calendar.DAY_OF_YEAR);
                break;
            case -1:
                diff = -(leftDaysOfYear(tabCalendar) + nextCalendar.get(Calendar.DAY_OF_YEAR));
                break;
            default:
                ;
        }
        return diff;
    }

    /**
     * 计算一年的剩余天数
     * */
    private static int leftDaysOfYear(Calendar calendar) {
        int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
        int days = isTheLeapYear(calendar) ? 366 : 365;
        return days - dayOfYear;
    }

    /**
     * 是否是闰年
     * */
    private static boolean isTheLeapYear(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        return (year % 4 == 0 && year % 100 != 0 || year % 400 == 0);
    }

    /**
     * 判断是否是同一年
     */
    public static boolean isTheSameYear(Calendar calendar1, Calendar calendar2) {
        return calendar1.get(Calendar.ERA) == calendar2.get(Calendar.ERA) &&
                calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR);
    }

    public static int yearsComparison(Calendar firstCalendar, Calendar secondCalendar) {
        return firstCalendar.get(Calendar.YEAR) - secondCalendar.get(Calendar.YEAR);
    }


    public static String getTimeSpan(Date start) {
        Date end = new Date();
        long timeLong = (end.getTime() - start.getTime()) / 1000;
        if (timeLong <= 0) {
            return "";
        }
        long day = timeLong / 60 / 60 / 24;

        if (day >= 1){
            return format(start, YEAR_MOUTH_DAY);
        }

        timeLong -= day * 60 * 60 * 24;
        long hour = timeLong / 60 / 60;

        if (hour >= 1){
            return hour + "小时前";
        }

        timeLong -= hour * 60 * 60;
        long minute = timeLong / 60;

        if (minute >= 1){
            return minute + "分钟前";
        }

        return "不到1分钟前";
    }


    /**
     * 对比两个日期的天数差
     * calendar2 - calendar1
     * @param calendar1
     * @param calendar2
     * @return
     */
    public static int compareDays(Calendar calendar1, Calendar calendar2) {
        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);
        if (year1 == year2) {
            return day2 - day1;
        } else {
            int symbol = 1;
            if (year1 > year2) {
                symbol = -1;
                int temp = day1;
                day1 = day2;
                day2 = temp;
                temp = year1;
                year1 = year2;
                year2 = temp;
            }
            int dayCount = 0;
            for (int i = year1; i < year2; i++) {
                if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                    dayCount += 366;
                } else {
                    dayCount += 365;
                }
            }
            return (dayCount + (day2 - day1)) * symbol;
        }
    }
}

