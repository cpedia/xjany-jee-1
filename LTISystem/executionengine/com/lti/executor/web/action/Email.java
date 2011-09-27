package com.lti.executor.web.action;

import com.lti.executor.ExecutorPool;
import com.lti.executor.web.BasePage;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.EmailUtil;

public class Email extends BasePage {

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	

	@Override
	public String execute() throws Exception {
		
		String operation=request.getParameter("operation");
		if(operation==null)operation="info";
		
		if(operation.equals("start")){
			Configuration.setSendMail(true);
			info="OK, true.";
		}else if(operation.equals("stop")){
			Configuration.setSendMail(false);
			info="OK, false";
		}else{
			info=""+Configuration.getSendMail();
		}
		
		return "info.ftl";
	}

}
