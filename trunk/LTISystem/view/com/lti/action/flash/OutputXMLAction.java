package com.lti.action.flash;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.permission.PermissionChecker;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.User;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.lti.util.FormatUtil;
import com.lti.util.LTIDate;
import com.lti.system.Configuration;

public class OutputXMLAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(OutputXMLAction.class);

	private String resultString;
	
	private String portfolioID;
	
	private PortfolioManager portfolioManager;
	
	private UserManager userManager;
	
	private Date legalDate;
	
	public String getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(String portfolioID) {
		this.portfolioID = portfolioID;
	}

	

	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
		//Map session = (Map)ActionContext.getContext().get(ActionContext.SESSION);
		//legalDate = (Date)session.get("legalDate");
		if(legalDate == null)
		{
			User user = userManager.getLoginUser();
			Long userID;
			if(user != null){
				userID = user.getID();
			}
			else{
				userID = Configuration.USER_ANONYMOUS;
			}
			long id = Long.parseLong(portfolioID.replace(",", ""));
			Portfolio p=portfolioManager.get(id);
			PermissionChecker pc=new PortfolioPermissionChecker(p, ServletActionContext.getRequest());
			legalDate = pc.getLastLegalDate();
		}
	}

	@Override
	public String execute() throws Exception {
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Portfolio><Data>");
			if (portfolioID != null & !portfolioID.equals("")) 
			{
				long id = Long.parseLong(portfolioID.replace(",", ""));
				List<com.lti.service.bo.PortfolioDailyData> resultList = portfolioManager.getDailydataInPeirod(id, legalDate);
				for (int i = 0; i < resultList.size(); i++) 
				{
					if (resultList.get(i).getAmount() != null) 
					{
						sb.append( "<e ");
						sb.append( "v=\"");
						sb.append( FormatUtil.formatQuantity(resultList.get(i).getAmount(),2));
						sb.append( "\"");
						sb.append( "d=\"");
						sb.append( resultList.get(i).getDate().toString().substring(0, 10));
						sb.append( "\"");
						sb.append( "/>"); 
					}
				}
				Date lastDate=resultList.get(resultList.size()-1).getDate();
				sb.append("<e v=\"0\" d=\""+lastDate.toString().substring(0, 10)+"\"/>");//to solve the boundary problem
				
			}
		
		sb.append( "</Data>");
		sb.append("<Compare url=\"/LTISystem/jsp/flash/OutputCompare.action?\"/>");
		sb.append("<TL url=\"/LTISystem/jsp/flash/OutputTL.action?portfolioID="+portfolioID+"\"/>");
		sb.append("<ID>"+portfolioID+"</ID>");
		
		sb.append("</Portfolio>");
		resultString=sb.toString();
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

	public Date getLegalDate() {
		return legalDate;
	}

	public void setLegalDate(Date legalDate) {
		this.legalDate = legalDate;
	}
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}



	

}
