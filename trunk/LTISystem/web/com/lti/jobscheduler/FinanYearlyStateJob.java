package com.lti.jobscheduler;

import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FinanYearlyStateJob extends TimerTask{

static Log log = LogFactory.getLog(FinanYearlyStateJob.class);
	
	public synchronized void run() {
		
		FinanYearlyStateControler ft = new FinanYearlyStateControler();
		ft.startYearlyUpdateFSData();		
		
	}
	
	public synchronized boolean cancelYearly(){

		super.cancel();
		FinanYearlyStateControler ft = new FinanYearlyStateControler();
		ft.stopYearlyUpdateFSData();
		return false;
	}
}
