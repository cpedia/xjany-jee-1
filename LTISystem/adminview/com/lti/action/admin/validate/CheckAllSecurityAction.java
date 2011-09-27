package com.lti.action.admin.validate;

import com.lti.action.Action;
import com.lti.util.validate.TotalReturnValidator;
import com.opensymphony.xwork2.ActionSupport;

public class CheckAllSecurityAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private java.lang.Double difference; 
	
	
      public void validate(){
		
		if(this.difference==null||this.difference.equals("")){
			
			addFieldError("difference","difference is not validate!");
			
			return;
		}
		
	}
	
	@Override
	public String execute() throws Exception {
		TotalReturnValidator tv = new TotalReturnValidator();
		tv.checkSecurityMpt(difference);
		return  Action.SUCCESS;
	}
	
	public Double getDifference() {
		return difference;
	}

	public void setDifference(Double difference) {
		this.difference = difference;
	}

}
