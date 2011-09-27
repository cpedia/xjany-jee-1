package com.lti.jobscheduler;

import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FundStateJob  extends TimerTask{
	
static Log log = LogFactory.getLog(FundStateJob.class);
	
	public synchronized void run() {
		
		FundStateControler ft = new FundStateControler();
		ft.startExeFund();		
		
	}
	
	public synchronized boolean cancel(){

		super.cancel();
		FundStateControler ft = new FundStateControler();
		ft.stopExeFund();
		return false;
	}

}
