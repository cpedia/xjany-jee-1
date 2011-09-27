/**
 * 
 */
package com.lti.executor.web.action;

import java.util.List;

import com.lti.executor.web.BasePage;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.Portfolio;
import com.lti.system.ContextHolder;
import com.lti.util.StringUtil;

/**
 * @author CCD
 *
 */
public class ClearEmailAlertForCustomize extends BasePage{

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public String setup() {
		StringBuffer sb = new StringBuffer();
		UserManager userManager = ContextHolder.getUserManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		List<EmailNotification> emailNotificationList = userManager.getAllEmailNotification();
		try{
			for(EmailNotification emailNotification : emailNotificationList) {
				Portfolio p = portfolioManager.get(emailNotification.getPortfolioID());
				if(p == null)
					userManager.deleteEmailNotification(emailNotification.getID());
				sb.append(emailNotification.getPortfolioID());
				sb.append("<br>\r\n");
			}
		}catch(Exception e){
			sb.append("<pre>" + StringUtil.getStackTraceString(e) + "</pre>");
		}
		return sb.toString();
	}
	
	@Override
	public String execute() throws Exception {
		info = setup();
		return "info.ftl";
	}

}
