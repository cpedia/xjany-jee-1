package com.lti.action.ajax;

import java.util.Date;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionSupport;

public class EmailAlertAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	private String resultString;

	private PortfolioManager portfolioManager;

	private UserManager userManager;

	private Long portfolioID;

	public String email() {
		System.out.println(portfolioID);
		User user = userManager.getLoginUser();
		Long userID;
		if (user == null || user.getID() == Configuration.USER_ANONYMOUS || portfolioID == null || portfolioID.longValue() == 0l) {
			resultString = "fail";
			return Action.SUCCESS;
		} else {
			userID = user.getID();
		}
		if(userManager.getEmailNotification(userID, portfolioID)!=null){
			resultString = "success";
			return Action.SUCCESS;
		}
		EmailNotification en = new EmailNotification();
		en.setPortfolioID(portfolioID);
		en.setUserID(userID);
		en.setSpan(0);
		Date today = new Date();
		Date LastSentDate = portfolioManager.getTransactionLatestDate(portfolioID, today);
		if (LastSentDate == null) {
			// Date date = new Date();
			// date.setYear(0);
			// date.setDate(1);
			// date.setMonth(0);
			LastSentDate = today;
		}
		LastSentDate = LTIDate.clearHMSM(LastSentDate);
		en.setLastSentDate(LastSentDate);
		try {
			userManager.addEmailNotification(en);
			//set the update mode to be emailalert instead of others
			//portfolioManager.updatePortfolioMode(portfolioID, Configuration.PORTFOLIO_UPDATEMODE_EMAILALERT);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			resultString = "fail addEmailNotification";
		}
		resultString = "success";
		return Action.SUCCESS;
	}

	public String emailRemove() {
		System.out.println(portfolioID);
		User user = userManager.getLoginUser();
		Long userID;
		if (user == null || user.getID() == Configuration.USER_ANONYMOUS || portfolioID == null || portfolioID.longValue() == 0l) {
			resultString = "fail";
			return Action.SUCCESS;
		} else {
			userID = user.getID();
		}
		EmailNotification en = userManager.getEmailNotification(userID, portfolioID);
		if (en == null) {
			resultString = "fail";
			return Action.ERROR;
		}
		try {
			userManager.deleteEmailNotification(en.getID());
			//int userCount = userManager.getEmailNotificationsByPortfolioID(portfolioID);
			//if(userCount == 0)// set the udpate mode to be alive instead of emailalert
				//portfolioManager.updatePortfolioMode(portfolioID, Configuration.PORTFOLIO_UPDATEMODE_RELEASE);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			resultString = "fail";
		}
		resultString = "success";
		return Action.SUCCESS;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}
}
