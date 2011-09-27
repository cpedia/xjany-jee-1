package com.lti.action.admin.loaddata;


import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.SecurityManager;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDownLoader;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	

	@Override
	public String execute() throws Exception {
		
		return Action.SUCCESS;

	}


}
