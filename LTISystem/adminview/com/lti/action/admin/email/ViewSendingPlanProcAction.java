package com.lti.action.admin.email;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;

public class ViewSendingPlanProcAction {
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public String execute(){
		StringBuffer sb=new StringBuffer();
		ServletContext session=ServletActionContext.getServletContext();
		Map<String,String> send_plan_mail_messages=(Map<String, String>) session.getAttribute("send_plan_mail_messages");
		if(send_plan_mail_messages!=null){
			Iterator<String> iter=send_plan_mail_messages.keySet().iterator();
			while(iter.hasNext()){
				String key=iter.next();
				String val=send_plan_mail_messages.get(key);
				sb.append(key).append(":").append(val).append("<br>");
			}
		}
		message=sb.toString();
		return Action.MESSAGE;
	}
}
