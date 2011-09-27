/**
 * 
 */
package com.lti.executor.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.executor.web.BasePage;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.UserResource;
import com.lti.system.ContextHolder;
import com.lti.system.Configuration;
import com.lti.util.StringUtil;

/**
 * @author CCD
 *
 */
public class SetupEmailAlertForCustomize extends BasePage{

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	public String setup() {
		StringBuffer sb = new StringBuffer();
		Date today = new Date();
		UserManager userManager = ContextHolder.getUserManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		List<UserResource> userResourceList = userManager.getUserResourceByResourceType(13);
		List<EmailNotification> emailNotificationList = new ArrayList<EmailNotification>();
		try{
			if(userResourceList != null) {
				for(UserResource userResource : userResourceList) {
					Long userID = userResource.getUserID();
					Long portfolioID = userResource.getResourceId();
					EmailNotification en = userManager.getEmailNotification(userID, portfolioID);
					if(en == null){
						en = new EmailNotification();
						en.setUserID(userID);
						en.setPortfolioID(portfolioID);
						en.setSpan(0);
						Date lastSentDate = portfolioManager.getTransactionLatestDate(portfolioID, today);
						if (lastSentDate == null) lastSentDate = today;
						emailNotificationList.add(en);
						sb.append(userID + "," + portfolioID);
						sb.append("<br>\r\n");
					}
				}
				userManager.addEmailNotification(emailNotificationList);
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
