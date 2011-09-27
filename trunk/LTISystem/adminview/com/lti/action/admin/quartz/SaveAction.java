package com.lti.action.admin.quartz;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.jobscheduler.Scheduler;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);
	
	private String action;
	
	private String updateDatabaseCornExpression;
	private String dailyExecutionCornExpression;
	private String clearPortfolioCornExpression;
	
	private String fastdailydataupdateOneCornExpression;	
	private String fastdailydataupdateTwoCornExpression;
	private String endofDateDailyExecutionOneCornExpression;
	private String endofDateDailyExecutionTwoCornExpression;
	
	
	public String getFastdailydataupdateOneCornExpression() {
		return fastdailydataupdateOneCornExpression;
	}

	public void setFastdailydataupdateOneCornExpression(
			String fastdailydataupdateOneCornExpression) {
		this.fastdailydataupdateOneCornExpression = fastdailydataupdateOneCornExpression;
	}

	public String getFastdailydataupdateTwoCornExpression() {
		return fastdailydataupdateTwoCornExpression;
	}

	public void setFastdailydataupdateTwoCornExpression(
			String fastdailydataupdateTwoCornExpression) {
		this.fastdailydataupdateTwoCornExpression = fastdailydataupdateTwoCornExpression;
	}

	public String getEndofDateDailyExecutionOneCornExpression() {
		return endofDateDailyExecutionOneCornExpression;
	}

	public void setEndofDateDailyExecutionOneCornExpression(
			String endofDateDailyExecutionOneCornExpression) {
		this.endofDateDailyExecutionOneCornExpression = endofDateDailyExecutionOneCornExpression;
	}

	public String getEndofDateDailyExecutionTwoCornExpression() {
		return endofDateDailyExecutionTwoCornExpression;
	}

	public void setEndofDateDailyExecutionTwoCornExpression(
			String endofDateDailyExecutionTwoCornExpression) {
		this.endofDateDailyExecutionTwoCornExpression = endofDateDailyExecutionTwoCornExpression;
	}

	public String getUpdateDatabaseCornExpression() {
		return updateDatabaseCornExpression;
	}

	public void setUpdateDatabaseCornExpression(String updateDatabaseCornExpression) {
		this.updateDatabaseCornExpression = updateDatabaseCornExpression;
	}

	public String getDailyExecutionCornExpression() {
		return dailyExecutionCornExpression;
	}




	public void setDailyExecutionCornExpression(String dailyExecutionCornExpression) {
		this.dailyExecutionCornExpression = dailyExecutionCornExpression;
	}




	public String getClearPortfolioCornExpression() {
		return clearPortfolioCornExpression;
	}




	public void setClearPortfolioCornExpression(String clearPortfolioCornExpression) {
		this.clearPortfolioCornExpression = clearPortfolioCornExpression;
	}

	
	public void validate(){
		
		if(this.action==null||this.action.equals(""))this.action=ACTION_VIEW;
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			if(!isValidExpression(updateDatabaseCornExpression)){
				addFieldError("updateDatabaseCornExpression","updateDatabaseCornExpression is not validate!");
				return;
			}else if(!isValidExpression(dailyExecutionCornExpression)){
				addFieldError("dailyExecutionCornExpression","dailyExecutionCornExpression is not validate!");
				return;
			}else if(!isValidExpression(clearPortfolioCornExpression)){
				addFieldError("clearPortfolioCornExpression","clearPortfolioCornExpression is not validate!");
				return;
			}
			
			else if(!isValidDisableExpression(fastdailydataupdateOneCornExpression) && !isValidExpression(fastdailydataupdateOneCornExpression)){
				addFieldError("fastdailydataupdateOneCornExpression", "fastdailydataupdateOneCornExpression is not validate!");
			}else if(!isValidDisableExpression(fastdailydataupdateTwoCornExpression) && !isValidExpression(fastdailydataupdateTwoCornExpression)){
				addFieldError("fastdailydataupdateTwoCornExpression", "fastdailydataupdateTwoCornExpression is not validate!");
			}else if(!isValidDisableExpression(endofDateDailyExecutionOneCornExpression) && !isValidExpression(endofDateDailyExecutionOneCornExpression)){
				addFieldError("endofDateDailyExecutionOneCornExpression", "endofDateDailyExecutionOneCornExpression is not validate!");
			}else if(!isValidDisableExpression(endofDateDailyExecutionTwoCornExpression) && !isValidExpression(endofDateDailyExecutionTwoCornExpression)){
				addFieldError("endofDateDailyExecutionTwoCornExpression", "endofDateDailyExecutionTwoCornExpression is not validate!");
			}
			
			//other fields
		}
		
	}
	
	public boolean isValidDisableExpression(String s){
		s=s.trim();
		if(s.equalsIgnoreCase("0"))
			return true;
		else
			return false;
	}
	
	public boolean isValidExpression(String s){
		DateFormat df=new SimpleDateFormat("HH:mm:ss");
		try {
			df.parse(s);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	
	
	@Override
	public String execute() throws Exception {
		
		
		//other fields
		
		if(this.action.equalsIgnoreCase(ACTION_SAVE)){
			
			try {
				
				PropertiesConfiguration config = new PropertiesConfiguration( ContextHolder.getServletPath() + "/WEB-INF/schedule.properties");
				
				config.setProperty("DAILY_PROCESSOR_TIME",clearPortfolioCornExpression);

				config.setProperty("DAILY_EXECUTION_TIME",dailyExecutionCornExpression);
				
				config.setProperty("DAILY_UPDATE_TIME",updateDatabaseCornExpression);
				
				config.setProperty("FAST_DAILY_UPDATE_TIME1", fastdailydataupdateOneCornExpression);
				
				config.setProperty("FAST_DAILY_UPDATE_TIME2", fastdailydataupdateTwoCornExpression);
				
				config.setProperty("END_OF_DAY_DAILY_EXECUTION_TIME1", endofDateDailyExecutionOneCornExpression);
				
				config.setProperty("END_OF_DAY_DAILY_EXECUTION_TIME2", endofDateDailyExecutionTwoCornExpression);
				
				config.save();
				
				Scheduler.schedule();
				
				addActionMessage("Update Successfully!"); 
			} catch (Exception e) {
				// TODO Auto-generated catch block
				
				e.printStackTrace();
				
				addActionMessage("Update Failed!"); 
			}
			
			this.action=ACTION_VIEW;
			
			return Action.INPUT;
		}
		
				
		else if(this.action.equalsIgnoreCase(ACTION_VIEW)){
			
			PropertiesConfiguration config = new PropertiesConfiguration( ContextHolder.getServletPath() + "/WEB-INF/schedule.properties");
			
			clearPortfolioCornExpression=config.getString("DAILY_PROCESSOR_TIME");

			dailyExecutionCornExpression=config.getString("DAILY_EXECUTION_TIME");
			
			updateDatabaseCornExpression=config.getString("DAILY_UPDATE_TIME");
			
			fastdailydataupdateOneCornExpression=config.getString("FAST_DAILY_UPDATE_TIME1");
			
			fastdailydataupdateTwoCornExpression=config.getString("FAST_DAILY_UPDATE_TIME2");
			
			endofDateDailyExecutionOneCornExpression=config.getString("END_OF_DAY_DAILY_EXECUTION_TIME1");
			
			endofDateDailyExecutionTwoCornExpression=config.getString("END_OF_DAY_DAILY_EXECUTION_TIME2");
			
			return Action.INPUT;
		}
		
		//add message
		
		return Action.ERROR;

	}



	public String getAction() {
		return action;
	}



	public void setAction(String action) {
		this.action = action;
	}




}
