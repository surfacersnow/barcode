package com.ruisoft.common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created with IntelliJ IDEA.
 * User: LFC
 * Date: 2015/6/25
 * Time: 22:04
 * To change this template use File | Settings | File Templates.
 */
public class DateTool {
    /**
     * 获取当前系统日期 返回 8位 like 20050101
     *
     * @return
     */
    public static String GetToday()
    {
        Calendar calendar = Calendar.getInstance();
        String s = String.valueOf(calendar.get(1));
        int i = calendar.get(2) + 1;
        String s1 = "";
        String s2 = "";
        if (i < 10)
            s1 = String.valueOf("0" + i);
        else
            s1 = String.valueOf(i);
        int j = calendar.get(5);
        if (j < 10)
            s2 = String.valueOf("0" + j);
        else
            s2 = String.valueOf(j);
        return s + s1 + s2;
    }

    /**
     * 获取输入日期的下一天 返回 8位 like 20050101
     *
     * @param day
     * @return
     */
    public static String getNextDay(String day) {
        return getNextDay(day, 1);
    }

    /**
     * 获取输入日期的下 n 天 返回 8位 like 20050101
     *
     * @param day
     * @param n
     * @return
     */
    public static String getNextDay(String day, int n) {
        if (day == null || "".equals(day) || day.length() != 8) {
            throw new RuntimeException("由于缺少必要的参数，系统无法进行制定的日期换算.");
        }
        try {
            String sYear = day.substring(0, 4);
            int year = Integer.parseInt(sYear);
            String sMonth = day.substring(4, 6);
            int month = Integer.parseInt(sMonth);
            String sDay = day.substring(6, 8);
            int dday = Integer.parseInt(sDay);
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, dday);
            cal.add(Calendar.DATE, n);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            return df.format(cal.getTime());

        } catch (Exception e) {
            throw new RuntimeException("进行日期运算时输入得参数不符合系统规格." + e);

        }
    }

    /**
     * 获取输入 月份的下 n 月份 返回 6位 like 200501
     *
     * @param month
     *            like 200404
     * @param n
     * @return
     */
    public static String getNextMonth(String month, int n) {
        if (month == null || "".equals(month) || month.length() != 6) {
            throw new RuntimeException("由于缺少必要的参数，系统无法进行制定的月份换算.");
        }
        try {
            String sYear = month.substring(0, 4);
            int year = Integer.parseInt(sYear);
            String sMonth = month.substring(4, 6);
            int mon = Integer.parseInt(sMonth);
            Calendar cal = Calendar.getInstance();
            cal.set(year, mon - 1, 1);
            cal.add(Calendar.MARCH, n);
            SimpleDateFormat df = new SimpleDateFormat("yyyyMM");
            return df.format(cal.getTime());

        } catch (Exception e) {
            throw new RuntimeException("进行月份运算时输入得参数不符合系统规格." + e);

        }
    }

    /**
     * 返回星期 0 星期天 6 星期陆
     *
     * @param date
     *            20040101
     * @return
     */
    public static int getWeekday(String date) {
        if (date == null || date.length() != 8) {
            throw new RuntimeException("由于缺少必要的参数，系统无法进行制定的月份换算.");
        }
        String sYear = date.substring(0, 4);
        int year = Integer.parseInt(sYear);
        String sMonth = date.substring(4, 6);
        int mon = Integer.parseInt(sMonth);
        String sDay = date.substring(6, 8);
        int dday = Integer.parseInt(sDay);
        Calendar cal = Calendar.getInstance();
        cal.set(year, mon - 1, dday);
        int weekday = cal.get(Calendar.DAY_OF_WEEK) - 1;
        return weekday;

    }

    public static Date getDate(String date) {
        if (date == null || date.length() != 8) {
            throw new RuntimeException("由于缺少必要的参数，系统无法进行制定的月份换算.  "+date);
        }
        String sYear = date.substring(0, 4);
        int year = Integer.parseInt(sYear);
        String sMonth = date.substring(4, 6);
        int mon = Integer.parseInt(sMonth);
        String sDay = date.substring(6, 8);
        int dday = Integer.parseInt(sDay);
        Calendar cal = Calendar.getInstance();
        cal.set(year, mon - 1, dday);
        return cal.getTime();
    }
    /**
     *
     * @param time
     * @return
     */
    public static Date getTime(String time){
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }
    /**
     * 获取输入 月份的前 n 月份 返回 6位 like 200501
     *
     * @param month
     * @param n
     * @return
     */
    public static String getPreviousMonth(String month, int n) {
        return getNextMonth(month, -n);
    }

    /**
     * 获取输入日期的嵌 n 天 返回 8位 like 20050101
     *
     * @param day
     * @param n
     * @return
     */
    public static String getPreviousDay(String day, int n) {
        return getNextDay(day, -n);
    }

    /**
     * 获取当前系统时间 返回 12:12:12
     *
     * @return
     */
    public static String GetCurrentTime() {
        StringBuffer result = new StringBuffer();
        Calendar calendar = Calendar.getInstance();
        int h = calendar.get(11);
        int m = calendar.get(12);
        int s = calendar.get(13);
        if (h < 10) {
            result.append("0");
        }
        result.append(h);
        result.append(":");
        if (m < 10) {
            result.append("0");
        }
        result.append(m);
        result.append(":");
        if (s < 10) {
            result.append("0");
        }
        result.append(s);
        return result.toString();
    }

    /**
     * 获取当前系统时间 返回 12:12:12
     *
     * @return
     */
    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * @param ctm
     *            long 系统时间
     * @param format
     * @return
     */
    public static String getTimeMillisAsString(long ctm, String format) {
        Date date = new Date(ctm);
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return df.format(date);
    }
    /**
     * 日期 和 格式得到相应的字符日期     *
     * @param date
     * @param format
     * @return
     */
    public static String getDateAsString(Date date, String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        return df.format(date);
    }
    /**
     * 获取当前系统时间 返回 yyyyMMdd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTimeMillisAsString() {
        long currTimeM = getCurrentTimeMillis();
        return getTimeMillisAsString(currTimeM, "yyyyMMdd HH:mm:ss");
    }

    /**
     * 获取当前系统时间 参数 format yyyyMMddHHmmssS 其中的部分 返回与 format格式相同的时间
     *
     * @return
     */
    public static String getCurrentTimeMillisAsString(String format) {
        long currTimeM = getCurrentTimeMillis();
        return getTimeMillisAsString(currTimeM, format);
    }
    
    /**
     * Date数据转成Sring类型
     * @param date 日期
     * @param s 字符串Format（yyyyMMdd）
     * @return
     */
    public static String dateToString(Date date, String s)
    {
    	if (date == null)
    		return null;
    	if (s == null)
    	{
    		return null;
    	}
    	s = s.toLowerCase();
    	s = s.replaceFirst("mm", "MM");
    	s = s.replaceFirst("hh", "HH");
    	SimpleDateFormat simpledateformat = new SimpleDateFormat(s);
    	simpledateformat.setLenient(false);
    	String s1 = simpledateformat.format(date);
    	return s1;
    }

    /**
     * String类型转Date类型
     * @param s 字符串时间
     * @param s1 字符串Format（yyyyMMdd）
     * @return
     */
    public static Date stringToDate(String s, String s1)
    {
    	Date date = null;
    	SimpleDateFormat simpledateformat = null;
    	if (s == null)
    		return null;
    	if (s1 == null)
    		return null;
    	s = s.trim();
    	if (s.length() == 0)
    		return null;
    	if (s1 == null)
    		return null;
    	s1 = s1.trim();
    	s1 = s1.toLowerCase();
    	s1 = s1.replaceFirst("mm", "MM");
    	s1 = s1.replaceFirst("hh", "HH");
    	if (s.length() < s1.length())
    		throw new RuntimeException("日期格式转换错误!传入参数日期[" + s + "]和小于格式[" + s1 + "]的长度");
    	s = s.substring(0, s1.length());
    	simpledateformat = new SimpleDateFormat(s1);
    	simpledateformat.setLenient(false);
    	try
    	{
    		date = simpledateformat.parse(s);
    	}
    	catch (Exception exception)
    	{
    		throw new RuntimeException("日期格式转换错误!传入参数日期[" + s + "]和格式[" + s1 + "]");
    	}
    	return date;
    }

    /**
     * 增加一个月
     * @param s
     * @param s1
     * @param i
     * @return
     */
    public static String addMonth(String s, String s1, int i)
    {
    	Date date = stringToDate(s, s1);
    	date = addMonth(date, i);
    	String s2 = dateToString(date, s1);
    	return s2;
    }

    /**
     * 增加一个月
     * @param date
     * @param i
     * @return
     */
    public static Date addMonth(Date date, int i)
    {
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.add(2, i);
    	date = calendar.getTime();
    	return date;
    }

    /**
     * String转int
     * @param s
     * @return
     */
    public static int stringToInt(String s)
    {
    	if (s == null)
    		return 0;
    	s = s.trim();
    	if (s.equals(""))
    	{
    		return 0;
    	}

    	int i = Integer.parseInt(s);
    	return i;
    }

    /**
     * 两个日期之间相差月数
     * @param date
     * @param date1
     * @return
     */
    public static int monthsBetween(Date date, Date date1)
    {
    	if ((date == null) || (date1 == null))
    	{
    		return 0;
    	}

    	int i = stringToInt(dateToString(date, "yyyy"));
    	int j = stringToInt(dateToString(date1, "yyyy"));
    	int k = stringToInt(dateToString(date, "MM"));
    	int l = stringToInt(dateToString(date1, "MM"));
    	int i1 = (j - i) * 12 + l - k;
    	return i1;
    }
    
    public static double round(double d, int i)
    {
      double d1 = Math.pow(10.0D, i);
      return Math.round(d * d1) / d1;
    }

}
