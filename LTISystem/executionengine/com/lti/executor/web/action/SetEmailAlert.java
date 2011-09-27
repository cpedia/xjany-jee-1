package com.lti.executor.web.action;

import java.util.Date;

import com.lti.action.Action;
import com.lti.executor.web.BasePage;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.Portfolio;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

public class SetEmailAlert extends BasePage {

	
	private String portfolioID;
	public String getPortfolioID() {
		return portfolioID;
	}
	public void setPortfolioID(String portfolioID) {
		this.portfolioID = portfolioID;
	}
	
	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		StringBuffer sb=new StringBuffer();
		UserManager userManager = ContextHolder.getUserManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		if(request == null){
			System.out.println("nulll");
		}
	    portfolioID = (request.getParameter("portfolioIDs"));
		String []portfolioIDs = portfolioID.split(",");
		for(String s: portfolioIDs){
			try{
				long ID = Long.parseLong(s);
				if(ID ==0l){
					sb.append("NoNeed," + s);
					continue;
				}
				Portfolio p = ContextHolder.getPortfolioManager().get(ID);
				long userID = p.getUserID();
				
				if(userManager.getEmailNotification(userID, ID)!=null){
					sb.append("NoNeed," + s);
					continue;
				}
				EmailNotification en = new EmailNotification();
				en.setPortfolioID(ID);
				en.setUserID(userID);
				en.setSpan(0);
				Date today = new Date();
				Date LastSentDate = portfolioManager.getTransactionLatestDate(ID, today);
				if (LastSentDate == null) {
					LastSentDate = today;
				}
				LastSentDate = LTIDate.clearHMSM(LastSentDate);
				en.setLastSentDate(LastSentDate);
				sb.append("Success," + s);
				try {
					userManager.addEmailNotification(en);
					//set the update mode to be emailalert instead of others
					//portfolioManager.updatePortfolioMode(portfolioID, Configuration.PORTFOLIO_UPDATEMODE_EMAILALERT);
				} catch (RuntimeException e) {
					// TODO Auto-generated catch block
					//info = StringUtil.getStackTraceString(e);
				}
				
			}catch(Exception e){
				sb.append("Fail," + s);
			}
			sb.append("<br>");
		}
				
		
		info = sb.toString();
		return "info.ftl";
	}

}
