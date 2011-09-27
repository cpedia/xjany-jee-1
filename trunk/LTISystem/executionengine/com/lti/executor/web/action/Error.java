package com.lti.executor.web.action;

import com.lti.executor.web.BasePage;
import com.lti.util.StringUtil;

public class Error extends BasePage {

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String execute() throws Exception {

		Long portfolioID = Long.parseLong(request.getParameter("portfolioID"));
		String err=(String) Action.session.get(portfolioID + ".error");
		if(err==null){
			err="No error messages.";
		}
		info = "<pre>" + err + "</pre>";
		return "info.ftl";
	}

}
