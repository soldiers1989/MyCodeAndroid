package com.hxjr.p2p.ad5.utils;

import android.annotation.SuppressLint;

import com.dm.utils.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class TimeUtils {
    public static final SimpleDateFormat DATE_FORMAT_DATE;

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale
            .CHINA);

    public static final SimpleDateFormat DATE_FORMAT_DATE2 = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);

    static {
        DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");
    }

    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    public static String getCurrentTimeInString(SimpleDateFormat simpleDateFormat) {
        return getTime(getCurrentTimeInLong(), simpleDateFormat);
    }

    public static String getTime(long timeMillis) {
        return getTime(timeMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * 长整型数字转日期, 返回字符串形式的日期
     *
     * @param
     */
    public static String getTime(long timeMillis, SimpleDateFormat simpleDateFormat) {
        return simpleDateFormat.format(new Date(timeMillis));
    }

    public static Date stringToDate(String date) {
        Date localObject = null;
        try {
            localObject = DATE_FORMAT_DATE.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return localObject;
    }

    public static String stringToDateDetail(Date date) {
        String localObject = "";
        try {
            localObject = DATE_FORMAT_DATE.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localObject;
    }

    /***
     * 将String格式的日期转化成long类型的
     *
     * @param sourceTime
     * @return
     */
    public static long string2LongTime2(String sourceTime) {
        if (!StringUtils.isEmpty(sourceTime))
            return stringToDateTime(sourceTime).getTime();
        return 0;
    }

    public static String stringToDateDetail2(Date date) {
        String localObject = "";
        try {
            localObject = DATE_FORMAT_DATE2.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localObject;
    }

    public static Date stringToDateTime(String date) {
        Date localObject = null;
        try {
            localObject = DEFAULT_DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return localObject;
    }

    /**
     * @param mss 要转换的毫秒数
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static String formatDuring(long mss) {
        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;
        StringBuilder sb = new StringBuilder("");
        if (days > 0) {
            sb.append(days).append("天");
        }
        //		if (hours > 0)
        //		{
        sb.append(hours).append("小时");
        //		}
        if (minutes > 0) {
            sb.append(minutes).append("分");
        }
        sb.append(seconds).append("秒");
        return sb.toString();
    }

    /**
     * @param retime 注册的时间
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static String diffTime(String retime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = new Date(Long.parseLong(retime));
            String time = DEFAULT_DATE_FORMAT.format(d);
            Date d1 = df.parse(getCurrentTimeInString());
            Date d2 = df.parse(time);
            long diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = diff / (1000 * 60 * 60);
            if (days - 3 > 0) {
                return "已经注册超过三天";
            } else {
                if (hours - 6 > 0) {
                    return "已经注册超过6小时";
                } else {
                    return "注册未超过6小时";
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "异常";
    }

    /**
     * @param retime 注册的时间
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     */
    public static Boolean diffsTime(String retime) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = new Date(string2LongTime2(retime+" 00:00:00"));
            String time = DEFAULT_DATE_FORMAT.format(d);
            Date d1 = df.parse(getCurrentTimeInString());
            Date d2 = df.parse(time);
            long diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            //			long hours = diff / (1000 * 60 * 60);
            if (days - 90 > 0) {
                return false;
            } else {

                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String diffsDays(){
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date(string2LongTime2("2016-06-19 10:00:00"));
            String sdate = DEFAULT_DATE_FORMAT.format(date);
            long today = getCurrentTimeInLong();
            //        Date date=new Date(string2LongTime2("2016-06-09 10:00:00"));
            Date dateOld = df.parse(sdate);
            long startDay = dateOld.getTime();
            long days =  (today - startDay) / (1000 * 60 * 60 * 24);
            return days + "";
        }catch (ParseException e){
            e.printStackTrace();
            return null;
        }
    }
}