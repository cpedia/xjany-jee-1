package com.lti.jobscheduler;

import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TickerSearchJob extends TimerTask{
	
	static Log log = LogFactory.getLog(TickerSearchJob.class);
		
		public synchronized void run() {
			
			TickerSearchControler ft = new TickerSearchControler();
			ft.startTicker();
		}
		
		public synchronized boolean cancel(){

			super.cancel();
			TickerSearchControler ft = new TickerSearchControler();
			ft.stopTicker();
			return false;
		}
}
