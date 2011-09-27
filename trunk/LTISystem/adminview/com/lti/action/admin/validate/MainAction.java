package com.lti.action.admin.validate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.action.admin.validate.MainAction;
import com.lti.service.bo.Security;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static Log log = LogFactory.getLog(MainAction.class);
	
public String execute() throws Exception {

	return Action.SUCCESS;

	}

}
