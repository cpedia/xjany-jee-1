package com.lti.jobscheduler;


import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class FastDailyUpdateJob  extends TimerTask{

	private String path;
	
	public FastDailyUpdateJob(String path, int updateNum){
		this.path = path;
		this.updateNum = updateNum;
	}
	
	private int updateNum;
	
	
	static Log log = LogFactory.getLog(FastDailyUpdateJob.class);
	
	
	public void run() {
		FastDailyUpdateControler ut = FastDailyUpdateControler.getInstance();
		ut.setPath(path);
		ut.setUpdateNum(updateNum);
		ut.start();		
	}

}
