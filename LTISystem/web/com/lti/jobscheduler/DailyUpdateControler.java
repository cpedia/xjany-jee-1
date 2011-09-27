package com.lti.jobscheduler;


import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.service.DataManager;
import com.lti.service.SecurityManager;
import com.lti.system.ContextHolder;
import com.lti.util.DailyUpdateListener;
import com.lti.util.LTIDate;
import com.lti.util.PlanRankingUtil;
import com.lti.util.SecurityManagerListener;


public class DailyUpdateControler  extends Thread{
		
	static Log log = LogFactory.getLog(DailyUpdateJob.class);
	
	public static Date startDate=new Date();
	
	public static Date endDate=new Date();
	
	public static Boolean isUpdating=false;
	
	public static Boolean sst = false;
	
	public static int current_count=0;
	
	public static int total_count=0;
	
	public static String state="";
	
	public static DailyUpdateControler curThread=null;
	
	private DailyUpdateListener dailyUpdateListener=new DailyUpdateListener();
	/**********************************************************/
	
	public static DailyUpdateControler singleThread;
	
	private DailyUpdateControler(){
	}
	
	public static DailyUpdateControler getInstance() {
		return DailyUpdateControler.singleThread = new DailyUpdateControler();
	}
	
	public void stopUpdateDailyData(){
		
		if(!isUpdating)return;

		System.out.println("Update daily data is stopped!");
		Date date = new Date();
		log.info("Update daily data is stopped at["+date+"]");
		
		this.stop();
	}
	
	public void run(){
		
		Date nDate = new Date();
		Calendar ca = Calendar.getInstance();
		ca.setTime(nDate);
		int dayOfYear = ca.get(Calendar.DAY_OF_YEAR);
		int hourOfDay = ca.get(Calendar.HOUR_OF_DAY);
		int year = ca.get(Calendar.YEAR);
//		if(this.isUpdating == true || fastDailyUpdate)
//		{
//			System.out.println("Updating is already running!");
//			return;
//		}
		boolean fastDailyUpdate = FastDailyUpdateControler.isUpdating;
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

		if(this.isUpdating || (isEverDailyUpdate&&isDailyUpdateAlive)){
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
		
		if(fastDailyUpdate ||(isEverFastUpdate&&isFastUpdateAlive)){
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
	
	public void update() {
		isUpdating=true;
		startDate=new Date();
		Calendar ca = Calendar.getInstance();
		ca.setTime(startDate);
		int dayOfWeek = ca.get(Calendar.DAY_OF_WEEK);
		int timeOfDay = ca.get(Calendar.HOUR_OF_DAY);
		log.info("Start updating daily data at["+startDate+"]");
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		DataManager dataManager= (DataManager)ContextHolder.getInstance().getApplicationContext().getBean("dataManager");
		//securityManager.closeCache();
		try {
			dailyUpdateListener.setSecurityManagerListener(new SecurityManagerListener(){

				public void copy_num(int param) {
					// TODO Auto-generated method stub
					current_count=param;
				}

				public void init(int param) {
					total_count=param;
					// TODO Auto-generated method stub
				}

				@Override
				public void copy_string(String param) {
					// TODO Auto-generated method stub
					state=param;
				}
				
			});
			securityManager.addListener(dailyUpdateListener);
			dataManager.addListener(dailyUpdateListener);
			com.lti.util.LTIDownLoader downloader=new com.lti.util.LTIDownLoader();
			downloader.updateDailyData();
			//downloader.updateDailyDataNew();
			dataManager.removeListener(dailyUpdateListener);		
			   /**part 16: update MPT***/		
			if((dayOfWeek == Calendar.FRIDAY && timeOfDay >= 17) ||  dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY){
					com.lti.util.LTIDownLoader dld=new com.lti.util.LTIDownLoader();						
					dld.writeLog("ALL Security UpDate part 16 start", new Date(), "\n************************************\n part 16:update MPT start\n************************************\n");
					isUpdating=false;
					PlanRankingUtil pru = new PlanRankingUtil();
					try {
						pru.rankPlan();   // compute plan rating
					} catch (Exception e) {
						System.out.println("calculate plan error after daily update");
					}			
					state="Update MPT";
					if(!sst){
						sst=true;
						try{
							securityManager.getAllSecurityMPT(startDate,true);
						}catch(Exception e){
							sst=false;
						}
						sst=false;
					}
					securityManager.removeListener(dailyUpdateListener);
					dld.writeLog("ALL Security UpDate part 16 end", new Date(), "\n************************************\n part 16:update MPT end\n************************************\n");
								
			}
	     } catch (RuntimeException e) {
			//securityManager.openCache();
			log.warn(e);
	}finally{		
		isUpdating=false;
	}
		
		com.lti.util.LTIDownLoader ldl=new com.lti.util.LTIDownLoader();
		Date endDate=new Date();
		ldl.writeLog("ALL Security UpDate finish", endDate, "\r\n************************************\r\nFinish all update data.\r\nPlease see more detail in the FinancialLog.\r\n************************************\r\n");
		log.info("Complete updating daily data at["+endDate+"]");
	}

	public static void main(String[] args)throws Exception{
		
		DailyUpdateControler dp=DailyUpdateControler.getInstance();
		
		dp.start();
	}
}
