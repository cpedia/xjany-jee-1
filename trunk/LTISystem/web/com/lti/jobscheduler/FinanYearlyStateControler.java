package com.lti.jobscheduler;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.service.SecurityManager;
import com.lti.system.ContextHolder;
import com.lti.util.DailyUpdateListener;
import com.lti.util.SecurityManagerListener;

public class FinanYearlyStateControler extends Thread{

static Log log = LogFactory.getLog(FinanYearlyStateControler.class);
	
	public static Date startYDate=new Date();
	
	public static Date endDate=new Date();
	
	public static int current_count=0;
	
	public static int total_count=0;
	
	public static String state="";
	
	public static Boolean isYFSUpdating=false;

	public static FinanYearlyStateThread curFSThread=null;
	
	private static DailyUpdateListener dailyUpdateListener=new DailyUpdateListener();
	
	public static class FinanYearlyStateThread extends Thread{
		public void run(){				
			if(FinanYearlyStateControler.isYFSUpdating == true){
				System.out.println("Yearly Updating is already running!");
				return;
			}
			try{
				update();
			}catch(Exception e){
				
				e.printStackTrace();
			}
			finally{
				isYFSUpdating = false;
			}
		}
		public void update() {
			isYFSUpdating=true;
			log.info("Start updating Yearly FinancialStatement data at["+startYDate+"]");
			System.out.println("Start Yearly updating FinancialStatement data at["+startYDate+"]");
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
				downloader.updateYearlyFinanStateData();
				isYFSUpdating=false;	
			} catch (RuntimeException e) {
				log.warn(e);
			}finally{
				isYFSUpdating=false;
			}
			Date endDate=new Date();
			
			log.info("Complete updating yearly data at["+endDate+"]");
		}
	}
	
	
	public synchronized static void startYearlyUpdateFSData(){
		if(FinanYearlyStateControler.isYFSUpdating == true){
			System.out.println("Yearly Updating is already running!");
			return;
		}
		curFSThread=new FinanYearlyStateThread();
		curFSThread.start();
	}
	
	public synchronized static void stopYearlyUpdateFSData(){
		if(!isYFSUpdating)return;
		System.out.println("Yearly Update FinancialStatement data is stopped!");
		Date date = new Date();
		log.info("Yearly Update FinancialStatement data is stopped at["+date+"]");
		FinanYearlyStateControler.curFSThread.stop();
		startYDate=new Date();
		
	}
}
