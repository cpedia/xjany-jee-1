package com.lti.action.admin.loaddata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.jobscheduler.DailyUpdateJob;
import com.lti.service.SecurityManager;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDownLoader;
import com.opensymphony.xwork2.ActionSupport;

public class UpdateSecurityDailyDataAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(UpdateSecurityDailyDataAction.class);

	public void validate() {

	}

	@Override
	public String execute() throws Exception {
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		//securityManager.closeCache();
		
		try {
			//LTIDownLoader dl = new LTIDownLoader();
			//dl.updateDailyData();
			
			DailyUpdateJob  uj = new DailyUpdateJob();
			uj.run();
			
		} catch (RuntimeException e) {
			return Action.ERROR;
		}
		
		//securityManager.openCache();
		return Action.SUCCESS;

	}

}
