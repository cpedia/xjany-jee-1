package com.lti.jobscheduler;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.admin.fundlist.FundTableAction;
import com.lti.service.SecurityManager;
import com.lti.system.ContextHolder;
import com.lti.util.DailyUpdateListener;
import com.lti.util.SecurityManagerListener;

public class FundStateControler {

static Log log = LogFactory.getLog(FundStateControler.class);
	
	public static Date startDate=new Date();
	
	public static Date endDate=new Date();
	
	public static int current_count=0;
	
	public static int total_count=0;
	
	public static String state="";
	
	public static Boolean isFSUpdating=false;

	public static FundStateThread curFSThread=null;
	
	private static DailyUpdateListener dailyUpdateListener=new DailyUpdateListener();
	
	public static class FundStateThread extends Thread{
		public void run(){				
			if(FundStateControler.isFSUpdating == true){
				System.out.println("Excution is already running!");
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
			log.info("Start excuting FundTable List data at["+FundStateControler.startDate+"]");
			System.out.println("Start excuting FundTable List data at["+FundStateControler.startDate+"]");
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
				ft.complete401kFile();
				isFSUpdating=false;	
			} catch (RuntimeException e) {
				log.warn(e);
			}finally{
				isFSUpdating=false;
			}
			Date endDate=new Date();
			
			log.info("Complete excution at["+FundStateControler.endDate+"]");
		}
	}
	
	
	public synchronized static void startExeFund(){
		if(FundStateControler.isFSUpdating == true){
			System.out.println("Execution is already running!");
			return;
		}
		curFSThread=new FundStateThread();
		curFSThread.start();
	}
	
	public synchronized static void stopExeFund(){
		if(!isFSUpdating)return;
		System.out.println("Execution data is stopped!");
		Date date = new Date();
		log.info("Execution data is stopped at["+date+"]");
		FundStateControler.curFSThread.stop();
		FundStateControler.startDate=new Date();
		
	}
}
