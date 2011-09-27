package com.lti.action.ajax;

import com.lti.action.Action;
import com.lti.action.admin.loaddata.ImportSecurityDataAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GetImportSecurityDataMessageAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	private String resultString;
	
	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public void validate() {

	}

	@Override
	public String execute() throws Exception {
		resultString = "";

		try {
			resultString=ImportSecurityDataAction.getMessage( ActionContext.getContext().getSession());

		} catch (Exception e) {
			resultString = "Wrong operation.";
		}

		return Action.SUCCESS;

	}

}
