package com.lti.jobscheduler;


import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class DailyUpdateJob  extends TimerTask{

	static Log log = LogFactory.getLog(DailyUpdateJob.class);
	
	
	public void run() {
		
		DailyUpdateControler ut = DailyUpdateControler.getInstance();
		ut.start();		
	}

}
