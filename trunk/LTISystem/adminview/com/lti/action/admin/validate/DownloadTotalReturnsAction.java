package com.lti.action.admin.validate;

import com.lti.action.Action;
import com.lti.util.validate.TotalReturnValidator;
import com.opensymphony.xwork2.ActionSupport;

public class DownloadTotalReturnsAction extends ActionSupport implements Action {
	
	/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	public String execute() throws Exception {
			
	//	TotalReturnValidator tv = new TotalReturnValidator();
		TotalReturnValidator.downloadSecurityMpt();
			
			
			return Action.SUCCESS;

		}

}
