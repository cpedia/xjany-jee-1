package com.lti.cache;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lti.util.LTIDate;

public class DateMap {
	private boolean isDebug=false;
	private Map<Long, Integer> datesMap=new HashMap<Long, Integer>();
	private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	private DateMap(){
		List<Date> dates=LTIDate.getTradingDates(null, null);
		Calendar calendar=Calendar.getInstance();
		calendar.setTime(dates.get(0));
		int year=calendar.get(Calendar.YEAR);
		int count=1;
		Integer number=year*1000+count;
		datesMap.put(dates.get(0).getTime(), number);
		if(isDebug)System.out.println(sdf.format(dates.get(0))+": "+number);
		
		
		for(int i=1;i<dates.size();i++){
			List<Date> betdates=getBetweenDates(dates.get(i-1), dates.get(i));
			for(int p=0;p<betdates.size();p++){
				datesMap.put(betdates.get(p).getTime(), number);
				if(isDebug)System.out.println(sdf.format(betdates.get(p))+": "+number);
			}
			
			calendar.setTime(dates.get(i));
			int newyear=calendar.get(Calendar.YEAR);
			if(newyear!=year){
				count=0;
				year=newyear;
			}
			count++;
			number=year*1000+count;
			datesMap.put(dates.get(i).getTime(), number);
			if(isDebug)System.out.println(sdf.format(dates.get(i))+": "+number);
		}
	}
	
	public int getNumber(Date date){
		return datesMap.get(date.getTime());
	}
	private Calendar calendar=Calendar.getInstance();
	private List<Date> getBetweenDates(Date start,Date end){
		List<Date> dates=new ArrayList<Date>();
		calendar.setTime(start);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.HOUR_OF_DAY,0);
		while(calendar.getTimeInMillis()<end.getTime()){
			dates.add(calendar.getTime());
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			calendar.set(Calendar.HOUR_OF_DAY,0);
		}
		return dates;
	}
	private static DateMap instance=null;
	public synchronized static DateMap getInstance(){
		if(instance==null){
			instance=new DateMap();
		}
		return instance;
	}
	
	public static void main(String[] args){
//		Date start=LTIDate.getDate(2007, 01, 02);
//		Date end=LTIDate.getDate(2007, 02, 03);
//		List<Long> dates=new DateMap().getBetweenDates(start, end);
//		for(int i=0;i<dates.size();i++){
//			System.out.println(dates.get(i));
//		}
//		DateMap dm=new DateMap();
		
		
	}
	
}
