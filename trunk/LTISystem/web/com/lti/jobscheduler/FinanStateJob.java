package com.lti.jobscheduler;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class FinanStateJob extends TimerTask{

	static Log log = LogFactory.getLog(FinanStateJob.class);
	
	public synchronized void run() {
		
		FinanStateControler ft = new FinanStateControler();
		ft.startUpdateFSData();		
		
	}
	
	public synchronized boolean cancel(){

		super.cancel();
		FinanStateControler ft = new FinanStateControler();
		ft.stopUpdateFSData();
		return false;
	}

}
