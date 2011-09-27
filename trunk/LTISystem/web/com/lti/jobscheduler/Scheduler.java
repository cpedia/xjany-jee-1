package com.lti.jobscheduler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.lti.system.ContextHolder;
import com.lti.system.Configuration;

public class Scheduler {
	public static Timer DAILY_EXECUTION_TIMER;
	public static Timer DAILY_UPDATE_TIMER;
	public static Timer DAILY_PROCESSOR_TIMER;
	
	public static Timer FAST_DAILY_UPDATE_TIMER1;
	
	public static Timer FAST_DAILY_UPDATE_TIMER2;
	public static Timer END_OF_DAY_DAILY_EXECUTION_TIMER1;
	public static Timer END_OF_DAY_DAILY_EXECUTION_TIMER2;
	
	//public static Timer STRATEGY_TABLE_CACHE_TIMER;
	
	public static Date getDate(Date date){
		Calendar calendar=Calendar.getInstance();
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH);
		int day=calendar.get(Calendar.DATE);
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.DATE, day);
		if(calendar.getTime().before(new Date())){
			calendar.add(Calendar.DAY_OF_YEAR, 1);
		}
		return calendar.getTime();
	}
	
	public static void schedule(){
//		try{
//			STRATEGY_TABLE_CACHE_TIMER.cancel();
//		}catch(Exception e2){
//		}
//		STRATEGY_TABLE_CACHE_TIMER=new Timer();
//		try{
//			Date d= new Date();
//			Calendar ca=Calendar.getInstance();
//			ca.add(Calendar.HOUR, 1);
//			STRATEGY_TABLE_CACHE_TIMER.schedule(new TimerTask(){
//
//				@Override
//				public void run() {
//					TableCache.main(null);
//				}
//				
//			},ca.getTime(),60*60*1000);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		try {
			DAILY_EXECUTION_TIMER.cancel();
		} catch (Exception e2) {
		}
		try {
			DAILY_UPDATE_TIMER.cancel();
		} catch (Exception e2) {
		}
		try {
			DAILY_PROCESSOR_TIMER.cancel();
		} catch (Exception e2) {
		}
		try{
			FAST_DAILY_UPDATE_TIMER1.cancel();
		}catch(Exception e2){
		}
		try{
			FAST_DAILY_UPDATE_TIMER2.cancel();
		}catch(Exception e2){
		}
		try{
			END_OF_DAY_DAILY_EXECUTION_TIMER1.cancel();
		}catch(Exception e2){
		}
		try{
			END_OF_DAY_DAILY_EXECUTION_TIMER2.cancel();
		}catch(Exception e2){
		}
		
		
		
		
		DAILY_EXECUTION_TIMER=new Timer();
		DAILY_UPDATE_TIMER=new Timer();
		DAILY_PROCESSOR_TIMER=new Timer("DAILY_PROCESSOR_TIMER");
		FAST_DAILY_UPDATE_TIMER1 = new Timer();
		FAST_DAILY_UPDATE_TIMER2 = new Timer();
		END_OF_DAY_DAILY_EXECUTION_TIMER1 = new Timer();
		END_OF_DAY_DAILY_EXECUTION_TIMER2 = new Timer();
		
		
		
		PropertiesConfiguration config = null;
		try {
			config = new PropertiesConfiguration( ContextHolder.getServletPath() + "/WEB-INF/schedule.properties");
		} catch (Exception e1) {
		}
		
		DateFormat df=new SimpleDateFormat("HH:mm:ss");
		
		Date DAILY_EXECUTION_TIME=null;
		try {
			DAILY_EXECUTION_TIME = getDate(df.parse(config.getString("DAILY_EXECUTION_TIME")));
			String interval = (String)Configuration.get("PORTFOLIO_UPDATE_MODE");
			String priorityArray = (String)Configuration.get("PORTFOLIO_UPDATE_PRIORITY");
			DailyExecutionJob d=new  DailyExecutionJob();
			d.setTitle("Daily executing by quartz using update mode.");
			//d.setFilter("PriorityPortfolioFilter");
			//d.setFilter("UpdateModePortfolioFilter");
			//d.setInterval(interval);
			//d.setPriorityArray(priorityArray);
			DAILY_EXECUTION_TIMER.schedule(d, DAILY_EXECUTION_TIME,24*60*60*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Date DAILY_UPDATE_TIME=null;
		try {
			DAILY_UPDATE_TIME = getDate(df.parse(config.getString("DAILY_UPDATE_TIME")));
			DAILY_UPDATE_TIMER.schedule(new DailyUpdateJob(), DAILY_UPDATE_TIME,24*60*60*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Date DAILY_PROCESSOR_TIME=null;
		try {
			DAILY_PROCESSOR_TIME = getDate(df.parse(config.getString("DAILY_PROCESSOR_TIME")));
			DAILY_PROCESSOR_TIMER.schedule(new DailyProcessorJob(), DAILY_PROCESSOR_TIME,24*60*60*1000);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		/*******************************************************************************************************************/
		Date FAST_DAILY_UPDATE_TIME1=null;
		try {
			String tmp = config.getString("FAST_DAILY_UPDATE_TIME1").trim();
			if(!tmp.equalsIgnoreCase("0"))
			{
				FAST_DAILY_UPDATE_TIME1 = getDate(df.parse(config.getString("FAST_DAILY_UPDATE_TIME1")));
				FAST_DAILY_UPDATE_TIMER1.schedule(new FastDailyUpdateJob("fast1", 1), FAST_DAILY_UPDATE_TIME1,24*60*60*1000);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Date FAST_DAILY_UPDATE_TIME2=null;
		try {
			String tmp = config.getString("FAST_DAILY_UPDATE_TIME2").trim();
			if(!tmp.equalsIgnoreCase("0"))
			{
				FAST_DAILY_UPDATE_TIME2 = getDate(df.parse(config.getString("FAST_DAILY_UPDATE_TIME2")));
				FAST_DAILY_UPDATE_TIMER1.schedule(new FastDailyUpdateJob("fast2", 2), FAST_DAILY_UPDATE_TIME2,24*60*60*1000);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Date END_OF_DAY_DAILY_EXECUTION_TIME1=null;
		try {
			String tmp = config.getString("END_OF_DAY_DAILY_EXECUTION_TIME1").trim();
			if(!tmp.equalsIgnoreCase("0"))
			{
				END_OF_DAY_DAILY_EXECUTION_TIME1 = getDate(df.parse(config.getString("END_OF_DAY_DAILY_EXECUTION_TIME1")));
				DailyExecutionJob d=new  DailyExecutionJob();
				d.setTitle("Fast daily executing1 by quartz(EMail Only).");
				d.setFilter("EmailAlertFilter");
				FAST_DAILY_UPDATE_TIMER1.schedule(d, END_OF_DAY_DAILY_EXECUTION_TIME1,24*60*60*1000);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Date END_OF_DAY_DAILY_EXECUTION_TIME2=null;
		try {
			String tmp = config.getString("END_OF_DAY_DAILY_EXECUTION_TIME2").trim();
			if(!tmp.equalsIgnoreCase("0"))
			{
				END_OF_DAY_DAILY_EXECUTION_TIME2 = getDate(df.parse(config.getString("END_OF_DAY_DAILY_EXECUTION_TIME2")));
				DailyExecutionJob d=new  DailyExecutionJob();
				d.setTitle("Fast daily executing2 by quartz(EMail Only).");
				d.setFilter("EmailAlertFilter");
				FAST_DAILY_UPDATE_TIMER1.schedule(d, END_OF_DAY_DAILY_EXECUTION_TIME2,24*60*60*1000);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		
		
	}
}
