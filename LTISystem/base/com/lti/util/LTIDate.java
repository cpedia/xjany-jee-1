package com.lti.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


import com.lti.service.HolidayManager;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.TimeUnit;


/*
 * LTIDate class
 * @author chaos
 */
public final class LTIDate extends java.util.Date {

	private static final long serialVersionUID = 1L;
	
	private static HolidayManager holidayManager = (HolidayManager)ContextHolder.getInstance().getApplicationContext().getBean("holidayManager");
	
	private static List<Date> tradingDateList = null;
	
	private static int oneDayMillSeconds = 86400000;
	
	public static void setTradingDateList(List<Date> dates){
		tradingDateList=dates;
	}
	
	/**
	 * the default download start date
	 * @return
	 */
	public static Date getSystemStartDate(){
		Date initialStartDate = LTIDate.getDate(1900, 0, 0);
		return LTIDate.add(initialStartDate, 1);
	}
	
	public static Date getDate(long time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time);
		return cal.getTime();
	}
	
	//calculate the interval between two dates 
	public static int calculateIntervalIgnoreHolidDay(Date startDate, Date endDate)
	{
		
		/*Date tmpDate;
		if(startDate.after(endDate))
		{
			tmpDate = startDate;
			startDate = endDate;
			endDate = tmpDate;
		}
		Calendar startCa = Calendar.getInstance();
		Calendar endCa = Calendar.getInstance();
		startCa.setTime(startDate);
		endCa.setTime(endDate);
		
		int count = 0;
		
		while(endCa.after(startCa))
		{		
			if(isNYSETradingDay(startCa.getTime()) ){
				count++;
			}
			startCa.add(Calendar.DAY_OF_YEAR, 1);
		}*/
		
		int count = 0;
		
		if(LTIDate.tradingDateList == null)
			LTIDate.getTradingDates();
		
		startDate = LTIDate.clearHMSM(startDate);
		endDate = LTIDate.clearHMSM(endDate);
		
		startDate = LTIDate.getRecentNYSETradingDayForward(startDate);
		endDate = LTIDate.getRecentNYSETradingDay(endDate);
		
		int start = tradingDateList.indexOf(startDate);
		int end = tradingDateList.indexOf(endDate);
		count = start - end;
		
		
		return count;
	}
	//calculate the interval between two dates 
//	public static int calculateInterval(Date startDate, Date endDate)
//	{		
//		startDate = LTIDate.clearHMSM(startDate);
//		endDate = LTIDate.clearHMSM(endDate);
//		Date tmpDate;
//		if(startDate.after(endDate))
//		{
//			tmpDate = startDate;
//			startDate = endDate;
//			endDate = tmpDate;
//		}
//		Calendar startCa = Calendar.getInstance();
//		Calendar endCa = Calendar.getInstance();
//		startCa.setTime(startDate);
//		endCa.setTime(endDate);
//		
//		int count = 0;
//		
//		while(endCa.after(startCa))
//		{		
//			count++;
//			startCa.add(Calendar.DAY_OF_YEAR, 1);
//		}
//				
//		return count;
//	}
	
	public static int calculateInterval(Date startDate, Date endDate){
		startDate = LTIDate.clearHMSM(startDate);
		endDate = LTIDate.clearHMSM(endDate);
		return (int) (Math.abs(startDate.getTime() - endDate.getTime())/ LTIDate.oneDayMillSeconds);
	}
	
	//calculate the next day of today
	public static Date getTomorrow(Date today)
	{
		Date to = new Date();
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(today);
		MyCa.add(Calendar.DAY_OF_YEAR,1);
		to = MyCa.getTime();
		to = LTIDate.clearHMSM(to);
		return to;
	}
	
	//calculate yesterday of today
	public static Date getYesterday(Date today)
	{
		Date ye = new Date();
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(today);
		MyCa.add(Calendar.DAY_OF_YEAR,-1);
		ye = MyCa.getTime();
		
		return LTIDate.clearHMSM(ye);
	}
	
	//calculate last year
	public static Date getLastYear(Date today){
		Date ye = new Date();
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(today);
		MyCa.add(Calendar.DAY_OF_YEAR,-365);
		ye = MyCa.getTime();
		
		return LTIDate.clearHMSM(ye);
	}
	
	public static Date getTwoWeeksAgo(Date today)
	{
		Date twoWeeksAgo = new Date();
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(today);
		MyCa.add(Calendar.DAY_OF_YEAR,-14);
		twoWeeksAgo = MyCa.getTime();
		
		return LTIDate.clearHMSM(twoWeeksAgo);
	}
	
	//judge whether the date is a work day
	/*public static boolean isNYSETradingDay(Date date)
	{		
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		if(tmpCa.get(GregorianCalendar.DAY_OF_WEEK) == Calendar.SATURDAY
				|| tmpCa.get(GregorianCalendar.DAY_OF_WEEK) == Calendar.SUNDAY
				|| isNYSEHoliday(date))
		{
			return false;
		}
		else
		{
			return true;
		}
	}*/
	
	public static void getTradingDates()
	{
		tradingDateList = holidayManager.getTradingDateList();
	}
	
	public static List<Date> getTradingDates(Date startDate,Date endDate){
		return holidayManager.getTradingDateList(startDate, endDate);
	}
	
	public static boolean isNYSETradingDay(Date date)
	{
		date = LTIDate.clearHMSM(date);
		
		if(tradingDateList == null)
			getTradingDates();
				
		if(tradingDateList.contains(date))
		{
			return true;
		}
		else return false;
		/*int head,tail,mid;
		head = 0;
		tail = tradingDateList.size();
		if(tradingDateList.get(head).getTradingDate().equals(date) || tradingDateList.get(tail).getTradingDate().equals(date))
			return true;
		while(head<tail)
		{
			mid = (head+tail)/2;
			Date tmp = tradingDateList.get(mid).getTradingDate()
			if(tmp.equals(date))
			{
				return true;
			}
			else if(tmp.after(date))
			{
				
			}
			else {
				
			}
		}*/
	}
	
	public static boolean isWeekDay(Date date)
	{
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		if(tmpCa.get(GregorianCalendar.DAY_OF_WEEK) == Calendar.SATURDAY
				|| tmpCa.get(GregorianCalendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	//judge whether the date is a holiday
	public static boolean isNYSEHoliday(Date date)
	{
		if(holidayManager.get(date) == null)return false;
		else return true;
	}
	
	//given the count, calculate new work day which is count days before of after the given date 
	public static Date getNewNYSETradingDay2(Date date,int count)
	{
		
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(date);
		Date newdate;
		int interval;
		if(count>0)
		{
			interval = 1;
		}
		else
		{
			interval = -1;
			count = -count;
		}
		int i = 0;
		while(true)
		{
			if(i == count)
				break;
			MyCa.add(Calendar.DAY_OF_YEAR, interval);
			newdate = MyCa.getTime();
			if(isNYSETradingDay(MyCa.getTime()))
			{
				i++;
			}
		}
		newdate = MyCa.getTime();
		newdate = LTIDate.clearHMSM(newdate);
		return newdate;
	}	
	
	
	//given the count, calculate new work day which is count days before of after the given date 
	public static Date getNewNYSETradingDay(Date date,int count)
	{
		
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(date);
		Date newdate;
		int interval;
		if(count>0)
		{
			interval = 1;
		}
		else
		{
			interval = -1;
		}
		if(tradingDateList==null)getTradingDates();
		
		int size = tradingDateList.size();
		
		Date cDate=LTIDate.clearHMSM(date);
		int index=0;
		
		int exist = tradingDateList.indexOf(date);
		if(exist>=0 && exist-count>=0 && exist-count<size)
		{
			return tradingDateList.get(exist-count);
		}
		
		for(int i=size-1;i>=0;i--){
			Date d=tradingDateList.get(i);
			if(i==size-1){
				if(cDate.before(d))return null;
			}
			if(i==0){
				if(cDate.after(d))return null;
			}
			if(cDate.equals(d)){
				index=i;
				break;
			}
			
			if(cDate.before(d)){
				if(interval==-1)index=i;
				else index=i+1;
				break;
			}
		}
		if(index-count>size||index-count<0)return null;
		return LTIDate.clearHMSM(tradingDateList.get(index-count));
		
		
	}
	
	public static Date getNewWeekDay(Date date, int count)
	{
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(date);
		Date newdate;
		int interval;
		if(count>0)
		{
			interval = 1;
		}
		else
		{
			interval = -1;
			count = -count;
		}
		int i = 0;
		while(true)
		{
			if(i == count)
				break;
			MyCa.add(Calendar.DAY_OF_YEAR, interval);
			if(isWeekDay(MyCa.getTime()))
			{
				i++;
			}
		}
		newdate = MyCa.getTime();
		newdate = LTIDate.clearHMSM(newdate);
		return newdate;
	}
	
	//given the count , calculate the new date(not work day)
	public static Date add(Date date,int count)
	{
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(date);
		MyCa.add(Calendar.DAY_OF_YEAR, count);
		return LTIDate.clearHMSM(MyCa.getTime());
	}

	//calculate new work day which is count weeks later 
	public static Date getNewNYSEWeek(Date date,int count)
	{
		Date newdate;
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(date);
		MyCa.add(Calendar.WEEK_OF_YEAR, count);
		if(isNYSETradingDay(MyCa.getTime()))
		{
			return MyCa.getTime();
		}
		else
		{
			newdate = getNewNYSETradingDay(MyCa.getTime(),-1);
		}
		newdate = LTIDate.clearHMSM(newdate);
		return newdate;
	}

	//calculate new work day which is count months later
	public static Date getNewNYSEMonth(Date date,int count)
	{
		Date newdate;
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(date);
		MyCa.add(Calendar.MONTH, count);
		if(isNYSETradingDay(MyCa.getTime()))
		{
			return MyCa.getTime();
		}
		else
		{
			newdate = getNewNYSETradingDay(MyCa.getTime(),-1);
		}
		newdate = LTIDate.clearHMSM(newdate);
		return newdate;
	}
	
	//calculate new work day which is count quarters later
	public static Date getNewNYSEQuarter(Date date,int count)
	{
		Date newdate;
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(date);
		int addYear = count / 4;
		if(addYear>0)
		{
			count = count % 4;
			MyCa.add(Calendar.YEAR, addYear);
		}
		MyCa.add(Calendar.MONTH, count*3); // quarter means 4 months
		if(isNYSETradingDay(MyCa.getTime()))
		{
			return MyCa.getTime();
		}
		else
		{
			newdate = getNewNYSETradingDay(MyCa.getTime(),-1);
		}
		newdate = LTIDate.clearHMSM(newdate);
		return newdate;
	}
	
	//calculate new work day which is count years later
	public static Date getNewNYSEYear(Date date,int count)
	{
		Date newdate;
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(date);
		MyCa.add(Calendar.YEAR, count);
		if(isNYSETradingDay(MyCa.getTime()))
		{
			return MyCa.getTime();
		}
		else
		{
			newdate = getNewNYSETradingDay(MyCa.getTime(),-1);
		}
		newdate = LTIDate.clearHMSM(newdate);
		return newdate;
	}
	
	//calculate the nearest work day backward
	public static Date getRecentNYSETradingDay(Date date)
	{
		if(isNYSETradingDay(date))
			return LTIDate.clearHMSM(date);
		else
			return getNewNYSETradingDay(date,-1);
	}
	
	public static Date getRecentNYSETradingDayForward(Date date)
	{
		if(isNYSETradingDay(date))
			return LTIDate.clearHMSM(date);
		else
			return getNewNYSETradingDay(date,1);
	}
	
	//calculate the nearest work day forward
	public static Date getNewNYSETradingDay(Date date)
	{
		if(isNYSETradingDay(date))
			return LTIDate.clearHMSM(date);
		else
			return getNewNYSETradingDay(date,1);
	}
	
	//judge whether the date is the last work day of the week
	public static boolean isLastNYSETradingDayOfWeek(Date date)
	{
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		tmpCa.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		Date newdate = getRecentNYSETradingDay(tmpCa.getTime());
		//if(date .equals( newdate))
		if(LTIDate.equals(date, newdate))
			return true;
		else
			return false;
	}
	
	public static boolean isLastSecondNYSETradingDayOfWeek(Date date){
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		tmpCa.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		Date newdate = LTIDate.getNewNYSETradingDay(tmpCa.getTime(), -2);
		if(LTIDate.equals(date, newdate))
			return true;
		else
			return false;
	}
	
	//judge whether the date is the first work day of the month
	public static boolean isFirstNYSETradingDayofMonth(Date date)
	{
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		tmpCa.set(Calendar.DAY_OF_MONTH,tmpCa.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date newdate = getNewNYSETradingDay(tmpCa.getTime());
		if(LTIDate.equals(date, newdate))
		{
			return true;
		}
		else
			return false;
	}
	
	//judge whether the date is the third Friday of the month
	public static boolean isThirdFridayofMonth(Date date)
	{
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		
		Date d1 = LTIDate.getDate(date.getYear()+1900, date.getMonth()+1, 1);
		Calendar tmpCa2 = Calendar.getInstance();
		tmpCa2.setTime(d1);
		int k = tmpCa2.get(Calendar.DAY_OF_WEEK);
		if(k>Calendar.FRIDAY)tmpCa.set(Calendar.WEEK_OF_MONTH, 4);
		else tmpCa.set(Calendar.WEEK_OF_MONTH, 3);
		
		tmpCa.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		Date newdate = tmpCa.getTime();
		if(LTIDate.equals(date, newdate))
		 {
			 return true;
		 }
		 else
			 return false;
	}
	
	//judge whether the date is the last work day of the month
	public static boolean isLastNYSETradingDayOfMonth(Date date)
	{
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		 tmpCa.set(Calendar.DAY_OF_MONTH, tmpCa.getActualMaximum(Calendar.DAY_OF_MONTH));
		 Date newdate = getRecentNYSETradingDay(tmpCa.getTime());
		 if(LTIDate.equals(date, newdate))
		 {
			 return true;
		 }
		 else
			 return false;
	}
	
	//judge whether the date is the last work day of the quarter
	public static boolean isLastNYSETradingDayOfQuarter(Date date)
	{
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		if( isLastNYSETradingDayOfMonth(date))
		{
			int month = ca.get(Calendar.MONTH);
			if( month == 2 || month == 5 || month == 8 || month == 11) {
				return true;
			} else
				return false;
		}
		else 
			return false;
	}
	
	//judge whether the date is the last work day of the year
	public static boolean  isLastNYSETradingDayOfYear(Date date)
	{
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		if( ca.get(Calendar.MONTH) == 11 && isLastNYSETradingDayOfMonth(date))
			return true;
		else 
			return false;
	}
	
	//judge whether the date is the specific date which fixes the given parameters
	public static boolean isNYSETradingDate(Date date, int year, int month, int week, int dayOfWeek, int dayOfMonth, int dayOfYear)
	{
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		if(year != -1)
		{
			tmpCa.set(Calendar.YEAR, year);
		}
		if(month != -1)
		{
			tmpCa.set(Calendar.MONTH, month-1);
		}
		if(week != -1)
		{
			tmpCa.set(Calendar.WEEK_OF_MONTH, week);
		}
		if(dayOfWeek != -1)
		{
			tmpCa.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		}
		if(dayOfMonth != -1)
		{
			tmpCa.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		}
		if(dayOfYear != -1)
		{
			tmpCa.set(Calendar.DAY_OF_YEAR, dayOfYear);
		}
		Date newDate = getRecentNYSETradingDay(tmpCa.getTime());
		if(LTIDate.equals(date, newDate))
		{
			return true;
		}
		else return false;
	}
	
	public static Date getFirstNYSETradingDayOfNextWeek(Date date){
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int weekOfYear = ca.get(Calendar.WEEK_OF_YEAR);
		ca.set(Calendar.WEEK_OF_YEAR, weekOfYear+1);
		ca.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		while(!isNYSETradingDay(ca.getTime())){
			ca.add(Calendar.DAY_OF_YEAR, 1);
		}
		return ca.getTime();
	}
	
	//calculate the last work day of the week
	public static Date getLastNYSETradingDayOfWeek(Date date)
	{		
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		tmpCa.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
		Date newdate = getRecentNYSETradingDay(tmpCa.getTime());
		newdate = LTIDate.clearHMSM(newdate);
        return newdate;
	}
	
	public static Date getFirstNYSETradingDayOfWeek(Date date)
	{		
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		tmpCa.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		Date newdate = getRecentNYSETradingDay(tmpCa.getTime());
		newdate = LTIDate.clearHMSM(newdate);
        return newdate;
	}
	
	public static Date getLastNYSETradingDayOfMonth(Date date)
	{		
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		tmpCa.set(Calendar.DAY_OF_MONTH, tmpCa.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date newdate = getRecentNYSETradingDay(tmpCa.getTime());
		newdate = LTIDate.clearHMSM(newdate);
        return newdate;
	}
	
	public static Date getFirstNYSETradingDayOfMonth(Date date)
	{		
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		tmpCa.set(Calendar.DAY_OF_MONTH, tmpCa.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date newdate = getRecentNYSETradingDayForward(tmpCa.getTime());
		newdate = LTIDate.clearHMSM(newdate);
        return newdate;
	}
	
	public static Date getLastNYSETradingDayOfQuarter(Date date)
	{		
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		int month = tmpCa.get(Calendar.MONTH);
		if(month<=2)tmpCa.set(Calendar.MONTH, Calendar.MARCH);
		else if(month<=5)tmpCa.set(Calendar.MONTH, Calendar.JUNE);
		else if(month<=8)tmpCa.set(Calendar.MONTH, Calendar.SEPTEMBER);
		else if(month<=11)tmpCa.set(Calendar.MONTH, Calendar.DECEMBER);
        return LTIDate.getLastNYSETradingDayOfMonth(tmpCa.getTime());
	}
	
	public static Date getFirstNYSETradingDayOfQuarter(Date date)
	{		
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		int month = tmpCa.get(Calendar.MONTH);
		if(month<=2)tmpCa.set(Calendar.MONTH, Calendar.MARCH);
		else if(month<=5)tmpCa.set(Calendar.MONTH, Calendar.JUNE);
		else if(month<=8)tmpCa.set(Calendar.MONTH, Calendar.SEPTEMBER);
		else if(month<=11)tmpCa.set(Calendar.MONTH, Calendar.DECEMBER);
        return LTIDate.getFirstNYSETradingDayOfMonth(tmpCa.getTime());
	}
	
	public static Date getLastNYSETradingDayOfYear(Date date)
	{		
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		tmpCa.set(Calendar.MONTH, Calendar.DECEMBER);
        return LTIDate.getLastNYSETradingDayOfMonth(tmpCa.getTime());
	}
	
	public static Date getLastYearLastNYSETradingDayOfYear(Date date){
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		tmpCa.add(Calendar.YEAR, -1);
		tmpCa.set(Calendar.MONTH, Calendar.DECEMBER);
        return LTIDate.getLastNYSETradingDayOfMonth(tmpCa.getTime());
	}
	                                                                  
	
	public static Date getFirstNYSETradingDayOfYear(Date date)
	{
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		tmpCa.set(Calendar.MONTH, Calendar.JANUARY);
        return LTIDate.getFirstNYSETradingDayOfMonth(tmpCa.getTime());
	}
	
	//calculate all the work day of the month
	public static List<Date> getNYSETradingDayOfMonth(Date date)
	{
		List<Date> workDays = new ArrayList<Date>();
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		tmpCa.set(Calendar.DAY_OF_MONTH, tmpCa.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date endDate = tmpCa.getTime();
		tmpCa.set(Calendar.DAY_OF_MONTH, tmpCa.getActualMinimum(Calendar.DAY_OF_MONTH));
		Date startDate = tmpCa.getTime();
		while(startDate.before(endDate))
		{
			if(isNYSETradingDay(startDate))
			{
				startDate = LTIDate.clearHMSM(startDate);
				workDays.add(startDate);
			}
			tmpCa.setTime(startDate);
			tmpCa.add(Calendar.DAY_OF_MONTH, 1);
			startDate = tmpCa.getTime();
		}
		return workDays;
	}

	public static boolean equals(Date d1,Date d2){
		Calendar ca=Calendar.getInstance();
		ca.setTime(d1);
		int y1=ca.get(Calendar.YEAR);
		int m1=ca.get(Calendar.MONTH);
		int day1=ca.get(Calendar.DAY_OF_YEAR);
		
		ca.setTime(d2);
		int y2=ca.get(Calendar.YEAR);
		int m2=ca.get(Calendar.MONTH);
		int day2=ca.get(Calendar.DAY_OF_YEAR);
		
		if(y1==y2&&m1==m2&&day1==day2){
			return true;
		}else return false;
	}
	

	public static Date clearHMSM(Date date){
		if(date==null)return null;
		java.util.Calendar ca=java.util.Calendar.getInstance();
		ca.setTimeInMillis(date.getTime());
		ca.set(java.util.Calendar.HOUR_OF_DAY, 0);
		ca.set(java.util.Calendar.MINUTE, 0);
		ca.set(java.util.Calendar.SECOND, 0);
		ca.set(java.util.Calendar.MILLISECOND, 0);
		return ca.getTime();
	}
	
	public static Date getDate(int year,int month,int day){
		
		Calendar ca=Calendar.getInstance();
		
		ca.set(Calendar.YEAR, year);
		
		ca.set(Calendar.MONTH, (month-1));
		
		ca.set(Calendar.DATE, day);
		
		ca.set(Calendar.HOUR_OF_DAY, 0);
		
		ca.set(Calendar.MINUTE, 0);
		
		ca.set(Calendar.SECOND, 0);
		
		ca.set(Calendar.MILLISECOND, 0);
		
		return ca.getTime();
	}
	public static boolean before(Date date1,Date date2){
		
		Calendar ca=Calendar.getInstance();
		
		ca.setTime(date1);
		
		int y1=ca.get(Calendar.YEAR);
		
		int m1=ca.get(Calendar.MONTH);
		
		int d1=ca.get(Calendar.DAY_OF_YEAR);
		
		ca.setTime(date2);
		
		int y2=ca.get(Calendar.YEAR);
		
		int m2=ca.get(Calendar.MONTH);
		
		int d2=ca.get(Calendar.DAY_OF_YEAR);
		
		if(y1<y2)return true;
		else if(y1>y2) return false;
		else{
			if(m1<m2)return true;
			else if(m1>m2) return false;
			else{
				if(d1<d2)return true;
				else return false;
			}
		}
	}
	public static boolean after(Date date1,Date date2){
		
		Calendar ca=Calendar.getInstance();
		
		ca.setTime(date1);
		
		int y1=ca.get(Calendar.YEAR);
		
		int m1=ca.get(Calendar.MONTH);
		
		int d1=ca.get(Calendar.DAY_OF_YEAR);
		
		ca.setTime(date2);
		
		int y2=ca.get(Calendar.YEAR);
		
		int m2=ca.get(Calendar.MONTH);
		
		int d2=ca.get(Calendar.DAY_OF_YEAR);
		
		if(y1>y2)return true;
		else if(y1<y2) return false;
		else{
			if(m1>m2)return true;
			else if(m1<m2) return false;
			else{
				if(d1>d2)return true;
				else return false;
			}
		}
	}
	
	public static boolean isWeekEnd(Date date){
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		tmpCa.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		Date newdate = tmpCa.getTime();
		if(LTIDate.equals(date, newdate))
			return true;
		else
			return false;
	}
	
	public static boolean isSaturday(Date date){
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		tmpCa.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
		Date newdate = tmpCa.getTime();
		if(LTIDate.equals(date, newdate))
			return true;
		else
			return false;
	}
	
	public static boolean isMonthEnd(Date date){
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		tmpCa.set(Calendar.DAY_OF_MONTH, tmpCa.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date newdate = tmpCa.getTime();
		if(LTIDate.equals(date, newdate))
		{
			return true;
		}
		else
			return false;
	}
	
	public static boolean isQuarterEnd(Date date){
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		if( isMonthEnd(date))
		{
			int month = ca.get(Calendar.MONTH);
			if( month == 2 || month == 5 || month == 8 || month == 11) {
				return true;
			} else
				return false;
		}
		else 
			return false;
	}
	
	public static boolean isHalfYearEnd(Date date){
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		if( ca.get(Calendar.MONTH) == 5 && isMonthEnd(date))
			return true;
		else 
			return false;
	}
	
	public static boolean isYearEnd(Date date){
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		if( ca.get(Calendar.MONTH) == 11 && isMonthEnd(date))
			return true;
		else 
			return false;
	}
	
	public static boolean isBeginning(TimeUnit tu, Date date)
	{
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);

		Date newdate = new Date();
		
		if(tu == TimeUnit.WEEKLY){
			newdate = LTIDate.getFirstNYSETradingDayOfWeek(date);
		}
		else if(tu == TimeUnit.MONTHLY){
			newdate = LTIDate.getFirstNYSETradingDayOfMonth(date);
		}
		else if(tu == TimeUnit.QUARTERLY){
			newdate = LTIDate.getFirstNYSETradingDayOfQuarter(date);
		}
		else if(tu == TimeUnit.YEARLY){
			newdate = LTIDate.getFirstNYSETradingDayOfYear(date);
		}
		
		if(date.equals(newdate))return true;
		else return false;
	}
	
	public static Date getNewTradingDate(Date date,TimeUnit tu,int count)
	{
		Date newDate = new Date();
		
		if(tu == TimeUnit.DAILY)
		{
			newDate = LTIDate.getNewNYSETradingDay(date, count);
		}
		else if(tu == TimeUnit.WEEKLY)
		{
			newDate = LTIDate.getNewNYSEWeek(date, count);
		}
		else if(tu == TimeUnit.MONTHLY)
		{
			//System.out.println(date+" "+count);
			Date dd=LTIDate.getDate(2008, 1, 2);
			if(LTIDate.equals(dd, date))
			{
				int abc=1;
				abc++;
			}
			newDate = LTIDate.getNewNYSEMonth(date, count);
		}
		else if(tu == TimeUnit.QUARTERLY)
		{
			newDate = LTIDate.getNewNYSEQuarter(date, count);
		}
		else if(tu == TimeUnit.YEARLY)
		{
			newDate = LTIDate.getNewNYSEYear(date, count);
		}
		newDate = LTIDate.clearHMSM(newDate);
		return newDate;
	}
	
	/***************************************************************************************/
	
	public static Date getNewTradingDateBackward(Date date,TimeUnit tu,int count)
	{
		Date newDate = new Date();
		
		if(tu == TimeUnit.DAILY)
		{
			newDate = LTIDate.getNewNYSETradingDay(date, -count);
		}
		else if(tu == TimeUnit.WEEKLY)
		{
			newDate = LTIDate.getNewNYSEWeek(date, -count);
		}
		else if(tu == TimeUnit.MONTHLY)
		{
			newDate = LTIDate.getNewNYSEMonth(date, -count);
		}
		else if(tu == TimeUnit.QUARTERLY)
		{
			newDate = LTIDate.getNewNYSEQuarter(date, -count);
		}
		else if(tu == TimeUnit.YEARLY)
		{
			newDate = LTIDate.getNewNYSEYear(date, -count);
		}
		newDate = LTIDate.clearHMSM(newDate);
		return newDate;
	}
	
	public static Date getNewTradingDateForward(Date date,TimeUnit tu,int count)
	{
		Date newDate = new Date();
		
		if(tu == TimeUnit.DAILY)
		{
			newDate = LTIDate.getNewNYSETradingDay(date, count);
		}
		else if(tu == TimeUnit.WEEKLY)
		{
			newDate = LTIDate.getNewNYSEWeekForward(date, count);
		}
		else if(tu == TimeUnit.MONTHLY)
		{
			newDate = LTIDate.getNewNYSEMonthForward(date, count);
		}
		else if(tu == TimeUnit.QUARTERLY)
		{
			newDate = LTIDate.getNewNYSEQuarterForward(date, count);
		}
		else if(tu == TimeUnit.YEARLY)
		{
			newDate = LTIDate.getNewNYSEYearForward(date, count);
		}
		newDate = LTIDate.clearHMSM(newDate);
		return newDate;
	}
	
	public static Date getNewNYSEWeekForward(Date date,int count)
	{
		Date newdate;
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(date);
		MyCa.add(Calendar.WEEK_OF_YEAR, count);
		if(isNYSETradingDay(MyCa.getTime()))
		{
			return MyCa.getTime();
		}
		else
		{
			newdate = getNewNYSETradingDay(MyCa.getTime(),1);
		}
		newdate = LTIDate.clearHMSM(newdate);
		return newdate;
	}

	//calculate new work day which is count months later
	public static Date getNewNYSEMonthForward(Date date,int count)
	{
		Date newdate;
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(date);
		MyCa.add(Calendar.MONTH, count);
		if(isNYSETradingDay(MyCa.getTime()))
		{
			return MyCa.getTime();
		}
		else
		{
			newdate = getNewNYSETradingDay(MyCa.getTime(),1);
		}
		newdate = LTIDate.clearHMSM(newdate);
		return newdate;
	}
	
	//calculate new work day which is count quarters later
	public static Date getNewNYSEQuarterForward(Date date,int count)
	{
		Date newdate;
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(date);
		int addYear = count / 4;
		if(addYear>0)
		{
			count = count % 4;
			MyCa.add(Calendar.YEAR, addYear);
		}
		MyCa.add(Calendar.MONTH, count*3); // quarter means 4 months
		if(isNYSETradingDay(MyCa.getTime()))
		{
			return MyCa.getTime();
		}
		else
		{
			newdate = getNewNYSETradingDay(MyCa.getTime(),1);
		}
		newdate = LTIDate.clearHMSM(newdate);
		return newdate;
	}
	
	//calculate new work day which is count years later
	public static Date getNewNYSEYearForward(Date date,int count)
	{
		Date newdate;
		Calendar MyCa = Calendar.getInstance();
		MyCa.setTime(date);
		MyCa.add(Calendar.YEAR, count);
		if(isNYSETradingDay(MyCa.getTime()))
		{
			return MyCa.getTime();
		}
		else
		{
			newdate = getNewNYSETradingDay(MyCa.getTime(),1);
		}
		newdate = LTIDate.clearHMSM(newdate);
		return newdate;
	}
	/***************************************************************************************/
	public static Date parseStringToDate(String s)
	{		
		/*Date newDate = new Date();
		
		newDate = LTIDate.clearHMSM(newDate);
		
		if(s.length() == 8 && s.indexOf("-")<0 && s.indexOf("/")<0)//20080101
		{
			int year = Integer.parseInt(s.substring(0, 4));
			int month = Integer.parseInt(s.substring(4, 6));
			int day = Integer.parseInt(s.substring(6, 8));
			newDate = LTIDate.getDate(year, month, day);
		}
		else if(s.indexOf("-")>0)//2008-01-01
		{
			String[] ss = s.split("-");
			int year = Integer.parseInt(ss[0]);
			int month = Integer.parseInt(ss[1]);
			int day = Integer.parseInt(ss[2]);
			newDate = LTIDate.getDate(year, month, day);
		}
		else // 3/24/2009
		{
			String[] ss = s.split("/");
			int year = Integer.parseInt(ss[2]);
			int month = Integer.parseInt(ss[0]);
			int day = Integer.parseInt(ss[1]);
			newDate = LTIDate.getDate(year, month, day); 
		}
		newDate = LTIDate.clearHMSM(newDate);
		return newDate;*/
		Date newDate = null;
		SimpleDateFormat  sdf=new SimpleDateFormat("yyyy-MM-dd");
		try {
			newDate = sdf.parse(s);
		} catch (Exception e) {
			newDate =null;
		}
		if(newDate!=null)return newDate;
		
		sdf=new SimpleDateFormat("yyyyMMdd");
		try {
			newDate = sdf.parse(s);
		} catch (Exception e) {
			newDate =null;
		}
		if(newDate!=null)return newDate;
		
		sdf=new SimpleDateFormat("MM/dd/yyyy");
		try {
			newDate = sdf.parse(s);
		} catch (Exception e) {
			newDate =null;
		}
		if(newDate!=null)return newDate;
		
		return null;
	}
	
	public static String parseDateToString(Date date)
	{
		if(date == null)
			return null;
		String dateS;
		Integer year = date.getYear()+1900;
		Integer month = date.getMonth()+1;
		Integer day = date.getDate();
		dateS = year.toString();
		dateS+="-";
		if(month<=9)
			dateS+="0";
		dateS+=month.toString();
		dateS+="-";
		if(day<=9)
			dateS+="0";
		dateS+=day.toString();
		return dateS;
	}
	
	//count<0 backward; count>0 forward
	public static Date getNewDate(Date date,TimeUnit tu,int amount)
	{
		Date dt = new Date();
		
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		
		if(tu == TimeUnit.DAILY)ca.add(ca.DAY_OF_YEAR, amount);
		else if(tu == TimeUnit.MONTHLY)ca.add(ca.MONTH, amount);
		else if(tu== TimeUnit.WEEKLY)ca.add(ca.WEEK_OF_YEAR, amount);
		else if(tu == TimeUnit.QUARTERLY)ca.add(ca.MONTH, 3*amount);
		else if(tu == TimeUnit.YEARLY)ca.add(ca.YEAR, amount);
		
		dt = ca.getTime();
		
		return dt;
	}
	
	
	public static Date getNDaysAgo(Date date, int n){
		date = clearHMSM(date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(ca.DATE, -1*n);
		Date dt = ca.getTime();
		dt = clearHMSM(dt);
		return dt;
	}
	
	public static int getQuarter(Date date){
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		int month = ca.get(Calendar.MONTH);
		if(month == 0||month == 1||month == 2)return 1;
		else if(month == 3||month == 4||month == 5)return 2;
		 else if(month == 6||month == 7||month == 8)return 3;
		  else return 4;
		}
	
	public static int getQuarter(int year,int month,int day){
		Date newdate = LTIDate.getDate(year,month,day);
		int quarter = getQuarter(newdate);
		return quarter;
	}
	
	public static Date getLastMonthDate(Date date){
		date = clearHMSM(date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.MONTH, -1);
		date = ca.getTime();
		return date;
	}
	
	public static Date getHoldingDateMonthEnd(Date benchDate){
		benchDate = clearHMSM(benchDate);
		Date date = getNDaysAgo(benchDate, Configuration.PORTFOLIO_DELAY_DAYS);
		date = getLastMonthDate(date);
		date = getMonthEnd(date);
		date = LTIDate.getRecentNYSETradingDay(date);
		return date;
	}
	
	public static Date getMonthEnd(Date date){
		clearHMSM(date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.add(Calendar.MONTH, 1);
		ca.set(Calendar.DATE, 1);
		ca.add(Calendar.DATE, -1);
		date = ca.getTime();
		return date;
	}
	
	public static Date getFirstDateOfMonth(Date date){
		clearHMSM(date);
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.set(Calendar.DATE, 1);
		date = ca.getTime();
		return date;
	}
	
	public static boolean isFirstDateOfMonth(Date date){
		return date.getDate()==1;
	}
	
	public static boolean isHoldingDate(Date date){
		clearHMSM(date);
		Date monthEnd = getMonthEnd(date);
		monthEnd = LTIDate.getRecentNYSETradingDay(monthEnd);
		if(date.equals(monthEnd))
			return true;
		else
			return false;
	}
	
	public static Date getLastDayOfQuarter(Date date)
	{		
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		int month = tmpCa.get(Calendar.MONTH);
		if(month<=2)tmpCa.set(Calendar.MONTH, Calendar.MARCH);
		else if(month<=5)tmpCa.set(Calendar.MONTH, Calendar.JUNE);
		else if(month<=8)tmpCa.set(Calendar.MONTH, Calendar.SEPTEMBER);
		else if(month<=11)tmpCa.set(Calendar.MONTH, Calendar.DECEMBER);
		tmpCa.set(Calendar.DAY_OF_MONTH, tmpCa.getActualMaximum(Calendar.DAY_OF_MONTH));
		Date quarterEnd = LTIDate.clearHMSM(tmpCa.getTime());
        return quarterEnd;
	}
	
	public static int getYear(Date date){
		return date.getYear()+1900;
	}
	
	public static boolean isWeekDay(Date CurrentDate, int day_of_week){
		Calendar ca=Calendar.getInstance();
		ca.setTime(CurrentDate);
		int dow=ca.get(Calendar.DAY_OF_WEEK);
		return day_of_week==dow;
	}
	//get the next month's first day
	public static Date getNextMonthFirstDay(Date date)
	{
		Date firstDate= date;
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		if(tmpCa.get(Calendar.MONTH)==11)
		{
			tmpCa.set(Calendar.MONTH, Calendar.JANUARY);
			tmpCa.set(Calendar.YEAR,tmpCa.get(Calendar.YEAR)+1);	
		}
		else
		{
			tmpCa.set(Calendar.MONTH, tmpCa.get(Calendar.MONTH)+1);	
		}
		firstDate=LTIDate.clearHMSM(tmpCa.getTime());
		return firstDate;
	}
	//get the last date in the month of date
	public static Date getAfterThreeMonth(Date date)
	{
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		if(tmpCa.get(Calendar.MONTH)>8)
		{
			tmpCa.set(Calendar.MONTH, (tmpCa.get(Calendar.MONTH)+3)%12);
			tmpCa.set(Calendar.YEAR,tmpCa.get(Calendar.YEAR)+1);	
		}
		else
		{
			tmpCa.set(Calendar.MONTH, tmpCa.get(Calendar.MONTH)+3);	
		}
		return LTIDate.clearHMSM(tmpCa.getTime());
	}
	
	public static boolean isSpecificTradingDateOfMonth(Date date, int day){
		Date resultDate,tempDate;
		Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(date);
		int[] dayOfMonth=new int[12];
		dayOfMonth[0]=dayOfMonth[2]=dayOfMonth[4]=dayOfMonth[6]=dayOfMonth[7]=dayOfMonth[9]=dayOfMonth[11]=31;
		dayOfMonth[3]=dayOfMonth[5]=dayOfMonth[8]=dayOfMonth[10]=30;
		dayOfMonth[1]=28;
		int year = tmpCa.get(Calendar.YEAR);
		int month = tmpCa.get(Calendar.MONTH);
		if((year%4==0&&year%100!=0)||year%400==0)
			dayOfMonth[1]++;
		int days = dayOfMonth[month];
		Calendar tt = Calendar.getInstance();
		if(day>days)
			day = days;
		tt.set(year, month, day);
		tempDate = tt.getTime();
		resultDate = LTIDate.getNewNYSETradingDay(tempDate);
		if(resultDate.getMonth()!=date.getMonth()){
			resultDate = LTIDate.getNewNYSETradingDay(tempDate, -1);
		}
		date = LTIDate.clearHMSM(date);
		return LTIDate.equals(date,resultDate);
	}
	
	public static Date getValidDate(Date curDate, int day){
		Calendar cal = Calendar.getInstance();
		cal.setTime(curDate);
		if(LTIDate.isLastNYSETradingDayOfMonth(curDate)){
			int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			if(maxDays > cal.get(Calendar.DAY_OF_MONTH)){
				if(day >= maxDays){
					cal.set(Calendar.DATE, maxDays);
					return cal.getTime();
				}else if(day > cal.get(Calendar.DAY_OF_MONTH)){
					cal.set(Calendar.DATE, day);
					return cal.getTime();
				}
			}
			cal.add(Calendar.MONTH, 1);
			maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			if(day > maxDays)
				cal.set(Calendar.DATE, maxDays);
			else
				cal.set(Calendar.DATE, day);
			Date firstTradingDate = LTIDate.getFirstNYSETradingDayOfMonth(cal.getTime());
			if(LTIDate.before(firstTradingDate, cal.getTime()))
				return cal.getTime();
			return firstTradingDate;
		}else{
			int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
			if(day > maxDays)
				cal.set(Calendar.DATE, maxDays);
			else
				cal.set(Calendar.DATE, day);
		}
		return cal.getTime();
	}
	
	
	public static boolean isScheduleDate(Date currentDate, Date referenceDate, String frequence){
		Calendar curCal = Calendar.getInstance();
		curCal.setTime(currentDate);
		Calendar refCal = Calendar.getInstance();
		refCal.setTime(referenceDate);
		int refDay = refCal.get(Calendar.DAY_OF_MONTH);
		Date scheduleDate = null;
		Date validDate = null;
		Calendar valCal = Calendar.getInstance();
		if(frequence.equalsIgnoreCase("monthly")){
			validDate = LTIDate.getValidDate(currentDate, refDay);
		}else if(frequence.equalsIgnoreCase("quarterly")){
			validDate = LTIDate.getValidDate(currentDate, refDay);
			valCal.setTime(validDate);
			int startMonth = refCal.get(Calendar.MONTH);
			int validMonth = valCal.get(Calendar.MONTH);
			if(Math.abs(startMonth-validMonth)%3 != 0)
				return false;
		}else if(frequence.equalsIgnoreCase("annually")){
			validDate = LTIDate.getValidDate(currentDate, refDay);
			valCal.setTime(validDate);
			int startMonth = refCal.get(Calendar.MONTH);
			int validMonth = valCal.get(Calendar.MONTH);
			if(startMonth != validMonth )
				return false;
		}
		if(validDate == null)
			return false;
		scheduleDate = LTIDate.getNewNYSETradingDay(validDate, -1);
		if(LTIDate.equals(scheduleDate, currentDate))
			return true;
		return false;
	}
	
	//for test
	public static void main(String[] args)
	{
     Date date = new Date();
     Date nextDate = LTIDate.getFirstNYSETradingDayOfNextWeek(date);
     System.out.println(nextDate);
		
	}
	
}
