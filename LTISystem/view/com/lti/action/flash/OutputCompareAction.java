package com.lti.action.flash;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.lti.action.Action;
import com.lti.service.SecurityManager;
import com.opensymphony.xwork2.ActionSupport;

public class OutputCompareAction extends ActionSupport implements Action{
	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(OutputCompareAction.class);

	private String resultString;
	private String analoguePortfolioName;
	private java.util.Date startDate;
	private java.util.Date endDate;
	private String startDateStr;
	private String endDateStr;
	private long analoguePortfolioID;
	
	private SecurityManager securityManager;
	
	public String getAnaloguePortfolioName() {
		return analoguePortfolioName;
	}
	public void setAnaloguePortfolioName(String analoguePortfolioName) {
		this.analoguePortfolioName = analoguePortfolioName;
	}
	
	
	public String getEndDateStr() {
		return endDateStr;
	}
	public void setEndDateStr(String endDateStr) {
		this.endDateStr = endDateStr;
	}
	public String getStartDateStr() {
		return startDateStr;
	}
	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}
	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}
	
	@Override
	public String execute() throws Exception {
		
		StringBuffer sb=new StringBuffer();
		
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><Compare><Data>");
		if (analoguePortfolioName!= null & !analoguePortfolioName.equals("")) 
		{
			
			SimpleDateFormat   sf   =   new   SimpleDateFormat   ("yyyy-MM-dd");
			startDate = (Date)sf.parseObject(startDateStr);
			endDate = (Date)sf.parseObject(endDateStr);
			
			try{
				analoguePortfolioID = securityManager.getBySymbol(analoguePortfolioName).getID();
			}
			catch(NullPointerException e){
				try{
					analoguePortfolioID = securityManager.get(analoguePortfolioName).getID();
				}
				catch(NullPointerException f){
					sb.append("</Data></Compare> ");
					return Action.SUCCESS;
				}
			}
			
			List<com.lti.service.bo.SecurityDailyData> securityResultList = securityManager.getDailyDatas(analoguePortfolioID,startDate,endDate);
			for(int i=0;i<securityResultList.size();i++)
			{
				sb.append("<e ");
				sb.append("adjustClose=\"");
				sb.append(securityResultList.get(i).getAdjClose());
				sb.append("\"");
				sb.append("d=\"");
				sb.append(securityResultList.get(i).getDate().toString().substring(0, 10));
				sb.append("\"");
				sb.append("/>");
			} 
			Date lastDate=securityResultList.get(securityResultList.size()-1).getDate();
			sb.append("<e adjustClose=\"0\" d=\""+lastDate.toString().substring(0, 10)+"\"/>");//to solve the boundary problem
			sb.append("</Data></Compare>");
		}
			
		resultString=sb.toString();
		return Action.SUCCESS;
		}
	}
