package com.zfd.bill2db.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * gpt 写的日期工具类
 */
public class DateUtils {

    public static final String PATTERN_STAND = "yyyy-MM-dd HH:mm:ss";
    public static final String PATTERN_YYYY_MM_DD = "yyyy-MM-dd";
    public static final String PATTERN_yyyyMMddHHmmss = "yyyyMMddHHmmss";

    public static final Long DEFAULT_OFFSET_8_HOUR_MS = 28800_000L;

    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL_STAND = ThreadLocal.withInitial(() -> new SimpleDateFormat(PATTERN_STAND));
    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL_YYYY_MM_DD = ThreadLocal.withInitial(() -> new SimpleDateFormat(PATTERN_YYYY_MM_DD));
    private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL_yyyyMMddHHmmss = ThreadLocal.withInitial(() -> new SimpleDateFormat(PATTERN_yyyyMMddHHmmss));

    private static final Map<String, ThreadLocal<SimpleDateFormat>> THREAD_LOCAL_MAP = new HashMap<>();

    static  {
        THREAD_LOCAL_MAP.put(PATTERN_STAND, THREAD_LOCAL_STAND);
        THREAD_LOCAL_MAP.put(PATTERN_YYYY_MM_DD, THREAD_LOCAL_YYYY_MM_DD);
        THREAD_LOCAL_MAP.put(PATTERN_yyyyMMddHHmmss, THREAD_LOCAL_yyyyMMddHHmmss);
    }

    public static Date parse(String date,String pattern) {
        try {
            return THREAD_LOCAL_MAP.get(pattern).get().parse(date);
        } catch (Exception e) {
        }
        return null;
    }

    public static String format(Date date) {
        return THREAD_LOCAL_STAND.get().format(date);
    }


    public static String format(Date date, String pattern) {
        ThreadLocal<SimpleDateFormat> threadLocal = THREAD_LOCAL_MAP.get(pattern);
        if (threadLocal == null) {
            threadLocal = THREAD_LOCAL_STAND;
        }
        return threadLocal.get().format(date);
    }

    public static boolean isWithinRecentOneHour(Date date) {
        // 获取日期的时间戳
        long timestamp = date.getTime();
        // 获取当前时间的时间戳
        long now = System.currentTimeMillis();
        long diff = now - timestamp;
        return diff <= TimeUnit.HOURS.toMillis(1);
    }

    public static Date toUTC0(Date utc8Date) {
        // 将UTC+8的时间的毫秒数减去偏移量得到UTC时间的毫秒数
        long utcTimestamp = utc8Date.getTime() - DEFAULT_OFFSET_8_HOUR_MS;
        // 创建一个新的Date对象，表示对应的UTC时间
        Date utcDate = new Date(utcTimestamp);
        return utcDate;
    }

    public static Date toUTC8(Date utc0Date) {
        long utcTimestamp = utc0Date.getTime() + DEFAULT_OFFSET_8_HOUR_MS;
        Date utc8Date = new Date(utcTimestamp);
        return utc8Date;
    }


    public static void main(String[] args) {
        // 示例用法
        Date date = new Date();  // 替换为你的 UTC0 时间
        Date utc8Date = DateUtils.toUTC0(date);

        Calendar instance = Calendar.getInstance();
        instance.setTime(utc8Date);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        instance.set(Calendar.MILLISECOND, 0);

        Date time = instance.getTime();

        System.out.println("UTC0时间：" + utc8Date);
        System.out.println("UTC0时间 calendar：" + time);
    }

    public static boolean between(Date startTime, Date endTime, Date time) {
        return time.compareTo(startTime) >= 0 && time.compareTo(endTime) <= 0;
    }

    public static Date getRecentOneHourStart(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.HOUR_OF_DAY, -1);
        return instance.getTime();
    }

    public static Date covert2DateStart(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.HOUR_OF_DAY, 0);
        instance.set(Calendar.MINUTE, 0);
        instance.set(Calendar.SECOND, 0);
        return instance.getTime();
    }

    public static Date covert2DateEnd(Date date) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.set(Calendar.HOUR_OF_DAY, 23);
        instance.set(Calendar.MINUTE, 59);
        instance.set(Calendar.SECOND, 59);
        return instance.getTime();
    }
}
