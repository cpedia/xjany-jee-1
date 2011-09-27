package com.lti.action.ajax;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Transaction;
import com.opensymphony.xwork2.ActionSupport;



public class GetTransactionTxtAction  extends ActionSupport implements Action{

	private static final long serialVersionUID = 1L;

	PortfolioManager portfolioManager;
	
	SecurityManager securityManager;

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}



	public String getResultString() {
		return resultString;
	}



	public void setResultString(String resultString) {
		this.resultString = resultString;
	}



	public String getIdString() {
		return idString;
	}



	public void setIdString(String idString) {
		this.idString = idString;
	}



	public String getDateString() {
		return dateString;
	}



	public void setDateString(String dateString) {
		this.dateString = dateString;
	}



	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	private String resultString;
	
	private String idString;
	
	private String dateString; 

	public String execute() throws Exception {
		resultString="";
		try{
			SimpleDateFormat   sf   =   new   SimpleDateFormat   ("yyyy-MM-dd hh:mm:ss");   
			Date date ;
			try {
				date = (Date) sf.parseObject(dateString);
			} catch (Exception e) {
				resultString="Please commit a date to get the history information";
				return Action.SUCCESS;
			}
			long id;
			try {
				id=Long.parseLong(idString);
			} catch (Exception e) {
				resultString="Please commit a date to get the history information";
				return Action.SUCCESS;
			}
			
			List<Transaction> resultList=portfolioManager.getTransactions(id, date);
			resultString+="<table width=\"90%\">";
			if(resultList.size()!=0){
				resultString+="<tr>";
				resultString+="<td>Operation</td>";
				resultString+="<td>Asset</td>";
				resultString+="<td>Security</td>";
				resultString+="<td>Amount</td>";
				resultString+="<tr>";
				for(int i=0;i<resultList.size();i++){			
					Transaction transaction=resultList.get(i);
					resultString+="<tr>";
					resultString+="<td>"+transaction.getOperation()+"</td>";
					resultString+="<td>"+transaction.getAssetName()+"</td>";
					java.lang.Long securityid=transaction.getSecurityID();
					String securityName;
					if(securityid==null||securityid==0){
						securityName="";
					}else{
						securityName=securityManager.get(securityid).getName();
					}
					resultString+="<td>"+securityName+"</td>";
					resultString+="<td>"+transaction.getAmount()+"</td>";
					resultString+="<tr>";
				}
			}else{
				resultString+="<tr><td>There was no transaction!</td></tr>";
			}
			resultString+="</table>";
			
			return Action.SUCCESS;
		}catch(Exception ex){
			String resultString="<table width=\"100%\">";
			resultString+="<tr><td>There was no transaction!</td></tr>";
			resultString+="</table>";
			return Action.SUCCESS;
		}
	}
}
