package com.lti.jobscheduler;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.util.LTIDate;



public class FastDailyUpdateControler  extends Thread{
		
	static Log log = LogFactory.getLog(FastDailyUpdateJob.class);
	
	public static Date startDate=new Date();
	
	public static Date endDate=new Date();
	
	public static Boolean isUpdating=false;
	
	public String path;
	
	public int updateNum;
	
	public static FastDailyUpdateControler curThread=null;
	
	/**********************************************************/
	
	public static FastDailyUpdateControler singleThread;
	
	private FastDailyUpdateControler(){
	}
	
	public static FastDailyUpdateControler getInstance() {
		return FastDailyUpdateControler.singleThread = new FastDailyUpdateControler();
	}	
	
	public void run(){				
		Date nDate = new Date();
		Calendar ca = Calendar.getInstance();
		ca.setTime(nDate);
		int dayOfYear = ca.get(Calendar.DAY_OF_YEAR);
		int hourOfDay = ca.get(Calendar.HOUR_OF_DAY);
        int year = ca.get(Calendar.YEAR);
		
		boolean dailyUpdate = DailyUpdateControler.isUpdating;
		boolean isEverDailyUpdate = false;
		boolean isDailyUpdateAlive = false;
		if(DailyUpdateControler.curThread !=null){
			isEverDailyUpdate = true;
		}
		if(isEverDailyUpdate){
			isDailyUpdateAlive = DailyUpdateControler.curThread.isAlive();
		}
		
		boolean isEverFastUpdate = false;
		boolean isFastUpdateAlive = false;
		if(FastDailyUpdateControler.curThread !=null){
			isEverFastUpdate = true;
		}
		if(isEverFastUpdate){
			isFastUpdateAlive = FastDailyUpdateControler.curThread.isAlive();
		}

		if(dailyUpdate || (isEverDailyUpdate&&isDailyUpdateAlive)){
			Date dailyDate = DailyUpdateControler.startDate;
			Calendar preCa = Calendar.getInstance();			
			preCa.setTime(dailyDate);
			int preDayOfYear = preCa.get(Calendar.DAY_OF_YEAR);
			int preHourOfDay = preCa.get(Calendar.HOUR_OF_DAY);
			int preYear = preCa.get(Calendar.YEAR);
			
			Calendar weekLastCa = Calendar.getInstance();
			Calendar nextWeekCa = Calendar.getInstance();
			Date weekLastTradingDate = LTIDate.getLastNYSETradingDayOfWeek(dailyDate);
			Date nextWeekFirstTradingDate = LTIDate.getFirstNYSETradingDayOfNextWeek(dailyDate);
			weekLastCa.setTime(weekLastTradingDate);
			nextWeekCa.setTime(nextWeekFirstTradingDate);
			
			weekLastCa.set(Calendar.HOUR_OF_DAY, 17);
			weekLastCa.set(Calendar.MINUTE, 0);
			weekLastCa.set(Calendar.SECOND, 0);
			
			nextWeekCa.set(Calendar.HOUR_OF_DAY, 17);
			nextWeekCa.set(Calendar.MINUTE, 0);
			nextWeekCa.set(Calendar.SECOND, 0);
			
			weekLastTradingDate = weekLastCa.getTime();
			nextWeekFirstTradingDate = nextWeekCa.getTime();
			
			if(year!=preYear){
				if(DailyUpdateControler.curThread !=null){
					DailyUpdateControler.curThread.stop();
				}
			}else if(preDayOfYear == dayOfYear && preHourOfDay < 17 && hourOfDay < 17){
				return;
			}else if(preDayOfYear == dayOfYear && preHourOfDay >= 17 && hourOfDay >= 17){
				return;
			}else if(dayOfYear - preDayOfYear ==1 && preHourOfDay >=17 && hourOfDay < 17 ){
				return;
			}else if(dailyDate.after(weekLastTradingDate) && nDate.before(nextWeekFirstTradingDate)){
				return;
			}else{
				if(DailyUpdateControler.curThread !=null){
					DailyUpdateControler.curThread.stop();
				}
			}
		}
		
		if(FastDailyUpdateControler.isUpdating ||(isEverFastUpdate&&isFastUpdateAlive)){
			Date fastDate = FastDailyUpdateControler.startDate;
			Calendar preCa = Calendar.getInstance();
			preCa.setTime(fastDate);
			int preDayOfYear = preCa.get(Calendar.DAY_OF_YEAR);
			int preHourOfDay = preCa.get(Calendar.HOUR_OF_DAY);
			int preYear = preCa.get(Calendar.YEAR);
			
			Calendar weekLastCa = Calendar.getInstance();
			Calendar nextWeekCa = Calendar.getInstance();
			Date weekLastTradingDate = LTIDate.getLastNYSETradingDayOfWeek(fastDate);
			Date nextWeekFirstTradingDate = LTIDate.getFirstNYSETradingDayOfNextWeek(fastDate);
			weekLastCa.setTime(weekLastTradingDate);
			nextWeekCa.setTime(nextWeekFirstTradingDate);
			
			weekLastCa.set(Calendar.HOUR_OF_DAY, 17);
			weekLastCa.set(Calendar.MINUTE, 0);
			weekLastCa.set(Calendar.SECOND, 0);
			
			nextWeekCa.set(Calendar.HOUR_OF_DAY, 17);
			nextWeekCa.set(Calendar.MINUTE, 0);
			nextWeekCa.set(Calendar.SECOND, 0);
			
			weekLastTradingDate = weekLastCa.getTime();
			nextWeekFirstTradingDate = nextWeekCa.getTime();
			
			if(year!=preYear){
				if(FastDailyUpdateControler.curThread !=null){
					FastDailyUpdateControler.curThread.stop();
				}
			}else if(preDayOfYear == dayOfYear && preHourOfDay < 17 && hourOfDay < 17){
				return;
			}else if(preDayOfYear == dayOfYear && preHourOfDay >= 17 && hourOfDay >= 17){
				return;
			}else if(dayOfYear - preDayOfYear ==1 && preHourOfDay >=17 && hourOfDay < 17 ){
				return;
			}else if(fastDate.after(weekLastTradingDate) && nDate.before(nextWeekFirstTradingDate) ){
				return;
			}else{
				if(FastDailyUpdateControler.curThread !=null){
					FastDailyUpdateControler.curThread.stop();
				}
			}		
		}
		try{
			curThread=this;
			this.update();
		}catch( Exception e){
			e.printStackTrace();
		}
		finally{
			isUpdating = false;
		}
	}
	
	public void update() throws FileNotFoundException {
		isUpdating=true;
		startDate=new Date();
		log.info("Start End of Date updating daily data at["+startDate+"]");
		try {
			com.lti.util.LTIDownLoader downloader=new com.lti.util.LTIDownLoader();
			downloader.fastUpdateDailyData(this.getPath(), this.getUpdateNum());
		} catch (RuntimeException e) {
			//securityManager.openCache();
			log.warn(e);
		}
		
		Date endDate=new Date();
		isUpdating=false;
		log.info("Complete End of Date updating daily data at["+endDate+"]");
	}

	public static void main(String[] args)throws Exception{
		
		FastDailyUpdateControler dp=FastDailyUpdateControler.getInstance();
		
		dp.start();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getUpdateNum() {
		return updateNum;
	}

	public void setUpdateNum(int updateNum) {
		this.updateNum = updateNum;
	}

}
