package com.lti.jobscheduler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.admin.fundlist.FundTableAction;
import com.lti.service.SecurityManager;
import com.lti.system.ContextHolder;
import com.lti.util.DailyUpdateListener;
import com.lti.util.SecurityManagerListener;

public class TickerSearchControler {
	
static Log log = LogFactory.getLog(TickerSearchControler.class);
	
	public static Date startTDate=new Date();
	
	public static Date endDate=new Date();
	
	public static int current_count=0;
	
	public static int total_count=0;
	
	public static String state="";
	
	public static Boolean isTSUpdating=false;

	public static TickerThread curTSThread=null;
	
	public static List<Long> planIDs = new ArrayList<Long>();
	
	private static DailyUpdateListener dailyUpdateListener=new DailyUpdateListener();
	
	public static class TickerThread extends Thread{
		public void run(){				
			if(TickerSearchControler.isTSUpdating == true){
				System.out.println("Excution is already running!");
				return;
			}
			try{
				update();
			}catch(Exception e){
				
				e.printStackTrace();
			}
			finally{
				isTSUpdating = false;
			}
		}
		public void update() {
			isTSUpdating=true;
			Date startDate = new Date();
			log.info("Start excuting ticker data at["+startDate+"]");
			System.out.println("Start excuting ticker data at["+startDate+"]");
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
				FundTableAction ft = new FundTableAction();
				ft.tickerTool(TickerSearchControler.planIDs);
				isTSUpdating=false;	
			} catch (RuntimeException e) {
				log.warn(e);
			}finally{
				isTSUpdating=false;
			}
			Date endDate=new Date();
			
			log.info("Complete excution at["+endDate+"]");
			System.out.println("Complete excution at["+endDate+"]");
		}
	}
	
	
	public synchronized static void startTicker(){
		if(TickerSearchControler.isTSUpdating == true){
			System.out.println("Execution is already running!");
			return;
		}
		curTSThread=new TickerThread();
		curTSThread.start();
	}
	
	public synchronized static void stopTicker(){
		if(!isTSUpdating)return;
		System.out.println("Execution ticker data is stopped!");
		Date date = new Date();
		log.info("Execution ticker data is stopped at["+date+"]");
		TickerSearchControler.curTSThread.stop();
		TickerSearchControler.startTDate=new Date();
		
	}

}
