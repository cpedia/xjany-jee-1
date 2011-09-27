package com.lti.action.register;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.lti.action.Action;
import com.lti.bean.EmailAlertBean;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class EmailAlertListAction extends ActionSupport implements Action {
	private UserManager userManager;
	
	private PortfolioManager portfolioManager;
	
	private List<EmailAlertBean> emailList;

	public List<EmailAlertBean> getEmailList() {
		return emailList;
	}

	public void setEmailList(List<EmailAlertBean> emailList) {
		this.emailList = emailList;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}
	
	public String viewEmailList(){
		User user = userManager.getLoginUser();
		Long userID;
		if(user == null || user.getID().equals( Configuration.USER_ANONYMOUS))
		{
			addActionError("You haven't logged in");
			return Action.ERROR;
		}
		else
		{
			userID = user.getID();
		}
		List<EmailNotification> ens = userManager.getEmailNotificationsByUser(userID);
		if(ens == null || ens.size() < 1){
			emailList = null;
			return Action.SUCCESS;
		}
		emailList = new ArrayList<EmailAlertBean>();
		SimpleDateFormat  sdf=new SimpleDateFormat("MM/dd/yyyy");
		for(int i = 0; i < ens.size(); i++){
			EmailAlertBean emailAlertBean = new EmailAlertBean();
			EmailNotification en = ens.get(i);
			emailAlertBean.setChoosed(true);
			emailAlertBean.setPortfolioID(en.getPortfolioID());
			try {
				Portfolio p = portfolioManager.get(en.getPortfolioID());
				emailAlertBean.setPortfolioName(p.getName());
				emailAlertBean.setLastSentDate(sdf.format(en.getLastSentDate()));
				if(p.getLastTransactionDate()!=null)emailAlertBean.setLastTransactionDate(sdf.format(p.getLastTransactionDate()));
				else emailAlertBean.setLastTransactionDate("NA");
				emailList.add(emailAlertBean);
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Action.SUCCESS;
	}
	
	public String saveEmailList(){
		User user = userManager.getLoginUser();
		Long userID;
		if(user == null || user.getID() == Configuration.USER_ANONYMOUS)
		{
			addActionError("You haven't logged in");
			return Action.ERROR;
		}
		else
		{
			userID = user.getID();
		}
		if(emailList != null && emailList.size() > 0){
			for(int i = 0; i < emailList.size(); i++){
				EmailAlertBean emailAlertBean = emailList.get(i);
				Boolean choosed;
				if(emailAlertBean.getChoosed() == null || emailAlertBean.getChoosed() == false){
					choosed = false;
				}
				else
					choosed = true;
				if(choosed == false)
				{
					try {
						userManager.deleteEmailNotification(userID,emailAlertBean.getPortfolioID());
						emailList.remove(emailAlertBean);
						i--;
					} catch (RuntimeException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
			
		return Action.SUCCESS;
	}
}
