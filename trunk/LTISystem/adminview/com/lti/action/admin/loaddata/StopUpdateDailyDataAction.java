package com.lti.action.admin.loaddata;

import com.lti.action.Action;
import com.lti.jobscheduler.DailyUpdateControler;
import com.lti.jobscheduler.FastDailyUpdateControler;
import com.opensymphony.xwork2.ActionSupport;

public class StopUpdateDailyDataAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;


	public void validate(){
		
	}

	@Override
	public String execute() throws Exception {
		
		try {
			boolean isEverDailyUpdate = false;
			boolean isDailyUpdateAlive = false;
			if(DailyUpdateControler.curThread !=null){
				isEverDailyUpdate = true;
			}
			if(isEverDailyUpdate){
				isDailyUpdateAlive = DailyUpdateControler.curThread.isAlive();
			}
			
			boolean isEverFastUpdate = false;
			boolean isFastUpdateAlive = false;
			if(FastDailyUpdateControler.curThread !=null){
				isEverFastUpdate = true;
			}
			if(isEverFastUpdate){
				isFastUpdateAlive = FastDailyUpdateControler.curThread.isAlive();
			}
			
			if(DailyUpdateControler.isUpdating || (isEverDailyUpdate&&isDailyUpdateAlive))DailyUpdateControler.curThread.stop();
			if(FastDailyUpdateControler.isUpdating ||(isEverFastUpdate&&isFastUpdateAlive))FastDailyUpdateControler.curThread.stop();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return Action.SUCCESS;
	}

}
