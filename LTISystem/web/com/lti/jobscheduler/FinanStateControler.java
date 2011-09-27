package com.lti.jobscheduler;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.service.DataManager;
import com.lti.service.SecurityManager;
import com.lti.system.ContextHolder;
import com.lti.util.DailyUpdateListener;
import com.lti.util.SecurityManagerListener;

/**
 * @author SuPing
 * Control the independent FinancialStatement Update
 * 2009-11-6
 */
public class FinanStateControler{

	static Log log = LogFactory.getLog(FinanStateJob.class);
	
	public static Date startDate=new Date();
	
	public static Date endDate=new Date();
	
	public static int current_count=0;
	
	public static int total_count=0;
	
	public static String state="";
	
	public static Boolean isFSUpdating=false;

	public static FinanStateThread curFSThread=null;
	
	private static DailyUpdateListener dailyUpdateListener=new DailyUpdateListener();
	
	public static class FinanStateThread extends Thread{
		public void run(){				
			if(FinanStateControler.isFSUpdating == true){
				System.out.println("Updating is already running!");
				return;
			}
			try{
				update();
			}catch(Exception e){
				
				e.printStackTrace();
			}
			finally{
				isFSUpdating = false;
			}
		}
		public void update() {
			isFSUpdating=true;
			log.info("Start updating FinancialStatement data at["+startDate+"]");
			System.out.println("Start updating FinancialStatement data at["+startDate+"]");
			SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
			try {
				dailyUpdateListener.setSecurityManagerListener(new SecurityManagerListener(){
					@Override
					public void copy_num(int param) {
						current_count=param;
					}

					@Override
					public void init(int param) {
						total_count=param;
					}

					@Override
					public void copy_string(String param) {
						state=param;
					}					
				});
				securityManager.addListener(dailyUpdateListener);
				com.lti.util.LTIDownLoader downloader=new com.lti.util.LTIDownLoader();
				downloader.updateFinanStateData();
				isFSUpdating=false;	
			} catch (RuntimeException e) {
				log.warn(e);
			}finally{
				isFSUpdating=false;
			}
			Date endDate=new Date();
			
			log.info("Complete updating daily data at["+endDate+"]");
		}
	}
	
	
	public synchronized static void startUpdateFSData(){
		if(FinanStateControler.isFSUpdating == true){
			System.out.println("Updating is already running!");
			return;
		}
		curFSThread=new FinanStateThread();
		curFSThread.start();
	}
	
	public synchronized static void stopUpdateFSData(){
		if(!isFSUpdating)return;
		System.out.println("Update FinancialStatement data is stopped!");
		Date date = new Date();
		log.info("Update FinancialStatement data is stopped at["+date+"]");
		FinanStateControler.curFSThread.stop();
		startDate=new Date();
		
	}
}
