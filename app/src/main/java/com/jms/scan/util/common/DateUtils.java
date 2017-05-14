package com.jms.scan.util.common;

import android.content.Context;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {

	public static final String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";

	// 用来全局控制 上一周，本周，下一周的周数变化
	private static int weeks = 0;

	// 根据日期取得星期几
	public static String getWeek(Date date) {
		String[] weeks = { "日", "一", "二", "三", "四", "五", "六" };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week_index < 0) {
			week_index = 0;
		}
		return weeks[week_index];
	}

	// 获得当前日期与本周一相差的天数
	private static int getMondayPlus() {
		Calendar cd = Calendar.getInstance();
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			return -6;
		} else {
			return 2 - dayOfWeek;
		}
	}

	// 获得上周星期一的日期
	public static String getPreviousMonday() {
		weeks--;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus + 7 * weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得本周星期一的日期
	public static String getCurrentMonday() {
		weeks = 0;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得本周星期一的日期
	public static int getCurrentMondayTime() {
		weeks = 0;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus);
		Date monday = currentDate.getTime();
		return monday.getDate();
	}

	// 获得下周星期一的日期
	public static String getNextMonday() {
		weeks++;
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus + 7 * weeks);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	// 获得相应周的周日的日期
	public static String getSunday(Context context) {
		int mondayPlus = getMondayPlus();
		GregorianCalendar currentDate = new GregorianCalendar();
		currentDate.add(Calendar.DATE, mondayPlus + 7 * weeks + 6);
		Date monday = currentDate.getTime();
		DateFormat df = DateFormat.getDateInstance();
		String preMonday = df.format(monday);
		return preMonday;
	}

	public static String timeToYMD(String str) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date parse = dateFormat.parse(str);
			String format = dateFormat.format(parse);
			return format;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String timeToYMD(String str, String formatStr) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
			Date parse = dateFormat.parse(str);
			String format = dateFormat.format(parse);
			return format;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String time2Date(String str, String format1, String format2) {
		try {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat(format1);
			SimpleDateFormat dateFormat2 = new SimpleDateFormat(format2);
			return dateFormat2.format(dateFormat1.parse(str));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String monthToYMD(String str) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date parse = dateFormat.parse(str);
			String format = dateFormat.format(parse);
			return format.substring(0, format.lastIndexOf("-"));
			// return format;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String mdyToYMD(String str) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
			Date parse = dateFormat.parse(str);

			SimpleDateFormat dateFormat_1 = new SimpleDateFormat("yyyy-MM-dd");
			String tablename = dateFormat_1.format(parse);
			return tablename;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String timeToHMS(String str) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
			String format = dateFormat.format(strToDate(str));
			return format;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date strToDate(String str) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			return dateFormat.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Date strToDate2(String str) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			return dateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Date strToDate3(String str) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
			return dateFormat.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取yyyyMMdd格式日期
	 * 
	 * @param time
	 * @return
	 */
	public static String getDate(Long time, String formatStr) {
		Date date = new Date(time);
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		String s = null;
		s = format.format(date);
		return s;
	}

	public static String formatDate(Context context, long date) {
		@SuppressWarnings("deprecation")
		int format_flags = android.text.format.DateUtils.FORMAT_NO_NOON_MIDNIGHT
				| android.text.format.DateUtils.FORMAT_ABBREV_ALL | android.text.format.DateUtils.FORMAT_CAP_AMPM
				| android.text.format.DateUtils.FORMAT_SHOW_DATE | android.text.format.DateUtils.FORMAT_SHOW_DATE
				| android.text.format.DateUtils.FORMAT_SHOW_TIME;
		return android.text.format.DateUtils.formatDateTime(context, date, format_flags);
	}

	public static int getDayForFormat(String time, String formatStr) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		// 字符串转为日期
		Date date = null;
		// String s=null;
		try {
			date = sdf.parse(time);
			// s=sdf.format(date);
			// LogUtil.i("date---"+s+"--date--"+date.getDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (date != null)
			return date.getDate();
		else
			return -1;
	}

	public static int getDayForWeek(String time, String formatStr) {
		Date dateNow = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		// 字符串转为日期
		Date date = null;
		// String s=null;
		try {
			date = sdf.parse(time);
			// s=sdf.format(date);
			// LogUtil.i("date---"+s+"--date--"+date.getDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (date != null)
			return date.getDay();
		else
			return -1;
	}

	public static long getDateForFormat(String time, String formatStr) {
		Date dateNow = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		// 字符串转为日期
		Date date = null;
		// String s=null;
		try {
			date = sdf.parse(time);
			// s=sdf.format(date);
			// LogUtil.i("date---"+s+"--date--"+date.getDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (date != null)
			return date.getTime();
		else
			return -1;
	}

	public static int getHourForFormat(String time, String formatStr) {
		Date dateNow = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		// 字符串转为日期
		Date date = null;
		// String s=null;
		try {
			date = sdf.parse(time);
			// s=sdf.format(date);
			// LogUtil.i("date---"+s+"--date--"+date.getDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (date != null)
			return date.getHours();
		else
			return -1;
	}

	public static int getMonthForFormat(String time, String formatStr) {
		Date dateNow = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		// 字符串转为日期
		Date date = null;
		// String s=null;
		try {
			date = sdf.parse(time);
			// s=sdf.format(date);
			// LogUtil.i("date---"+s+"--date--"+date.getDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (date != null)
			return date.getMonth() + 1;
		else
			return -1;
	}

	public static int getYearForSystem() {
		Calendar a = Calendar.getInstance();
		int year = a.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 获取相应offset 月份的第一天
	 * 
	 * @param offset
	 * @return
	 */
	public static Date getBeginDayOfMonth(int offset) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, offset);
		// 月份第一天
		cal.set(Calendar.DAY_OF_MONTH, 1);
		// cal.set(Calendar.HOUR_OF_DAY, 0);
		// cal.set(Calendar.MINUTE, 0);
		// cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static Date getEndDayOfMonth(int offset) {
		Calendar cal = Calendar.getInstance();
		// 月份最后一天
		cal.add(Calendar.MONTH, offset);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		// cal.set(Calendar.DAY_OF_MONTH,
		// cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		// cal.set(Calendar.HOUR_OF_DAY, 23);
		// cal.set(Calendar.MINUTE, 59);
		// cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	public static Date getLastYearDay() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		return cal.getTime();
	}

	public static String Date2FormatString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
		return sdf.format(date);
	}

	public static String Date2FormatString(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * 获得今天日期
     */
	public static long getToday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.MILLISECOND,0);
		return cal.getTimeInMillis();
	}

	/**
	 * 根据今天日期返回明天日期
     */
	public static long getTommrow() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(getToday());
		cal.add(Calendar.DAY_OF_YEAR, +1);
		cal.set(Calendar.HOUR,0);
		cal.set(Calendar.SECOND,0);
		cal.set(Calendar.MINUTE,0);
		cal.set(Calendar.MILLISECOND,0);
		return cal.getTimeInMillis();
	}

	public static void main(String[] args){
		System.out.println(getToday());
		System.out.println(getTommrow());
		System.out.println(DateUtils.getDate(getToday(),DEFAULT_FORMAT));
		System.out.println(DateUtils.getDate(getTommrow(),DEFAULT_FORMAT));
	}

}
