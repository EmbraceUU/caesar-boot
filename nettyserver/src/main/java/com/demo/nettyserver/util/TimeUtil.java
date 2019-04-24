package com.demo.nettyserver.util;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {

	public static Date localDatetoDate(LocalDate date) {
		return Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault())
				.toInstant());
	}

	public static LocalDate DatetoLocalDate(Date date) {
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
		LocalDate localDate = localDateTime.toLocalDate();
		return localDate;
	}

	public static Date getMondayDate(Date calDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(calDate);
		// 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
		int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		if (1 == dayWeek) {
			cal.add(Calendar.DAY_OF_MONTH, -1);
		}
		cal.setFirstDayOfWeek(Calendar.MONDAY);// 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
		int day = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
		cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);// 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
		return cal.getTime();
	}

	public static Date getFirstDayOfMonth(Date calDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(calDate);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/*
	 * public static void main(String[] args) { Date date = new Date();
	 * System.out.println(TimeUtil.getFirstDayOfYear(date));
	 * System.out.println(TimeUtil.getLastDayOfYear(date)); }
	 */

	public static Date getFirstDayOfYear(Date calDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(calDate);
		cal.set(cal.get(Calendar.YEAR), 0, 1);
		return cal.getTime();
	}

	public static Date getLastDayOfYear(Date calDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(calDate);
		cal.set(cal.get(Calendar.YEAR), 11, 31);
		return cal.getTime();
	}

	// 转换到秒的时间戳
	public static String transferTimestampForSeconds(String ts) {
		// 转换格式精确到分钟
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
		long date_temp = Long.valueOf(ts);
		String date_string = sdf.format(new Date(date_temp * 1000L));
		return date_string;
	}

	// 转换到毫秒的时间戳
	public static String transferTimestamp(String ts) {
		// 转换格式精确到分钟
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm");
		long date_temp = Long.valueOf(ts);
		String date_string = sdf.format(new Date(date_temp));
		return date_string;
	}

	/**
	 * IOS时间格式的字符串转换成时间戳
	 * @param isostr 2018-12-21T06:30:20.878Z
	 * @return 1545379931129
	 */
	public static long IsostrToTimestamp(String isostr) {
		Instant in = null;
		long time = 0L;
		try {
			in = Instant.parse(isostr);
			time = in.toEpochMilli();
		} catch (DateTimeParseException e) {
			time = new Date().getTime();
		}
		return time;
	}
	
    /** 
     * 日期格式字符串转换成时间戳 
     * @param date 字符串日期 
     * @param format 如：yyyy-MM-dd HH:mm:ss 
     * @return 
     */  
    public static String dateTOTimeStamp(String date_str,String format){  
        try {  
            SimpleDateFormat sdf = new SimpleDateFormat(format);  
            return String.valueOf(sdf.parse(date_str).getTime());  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return "";  
    } 
}
