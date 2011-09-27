package com.lti.executor.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.lti.executor.ExecutorPool;
import com.lti.executor.web.BasePage;
import com.lti.service.bo.Portfolio;
import com.lti.type.finance.ExecutorPortfolio;
import com.lti.util.IPUtil;

public class Dailymessage extends BasePage {

	private boolean isUpdating = false;

	private Date startDate;

	private String runningPeriod;

	private Integer updateSize;

	private String processing;

	private Long portfolioID;

	private String portfolioName;

	private Date currentStartDate;

	private String currentRunningPortfolioPeriod;

	private List<String[]> executors;

	private String title;

	private String ip;
	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	private int mode=0;
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	@Override
	public String execute() throws Exception {

		ip=IPUtil.getLocalIP();
		try{
			mode=Integer.parseInt(request.getParameter("mode"));
		}catch(Exception e){}
		if(mode==0){
			isUpdating = Action.dailyUpdate!=null&&Action.dailyUpdate.isAlive();
			if (isUpdating) {
				try {
					
					startDate = Action.dailyUpdate.startDate;
					runningPeriod = String.valueOf(((new Date()).getTime() - Action.dailyUpdate.startDate.getTime()) * 1.0 / 1000 / 60);
					updateSize = Action.dailyUpdate.updateSize;
					processing = (Action.dailyUpdate.currentPointer * 100.0 / Action.dailyUpdate.updateSize) + "";
					portfolioID = Action.dailyUpdate.currentPortfolioID;
					portfolioName = Action.dailyUpdate.currentPortfolioName;
					currentStartDate = Action.dailyUpdate.currentStartDate;
					currentRunningPortfolioPeriod = String.valueOf(((new Date()).getTime() - Action.dailyUpdate.currentStartDate.getTime()) * 1.0 / 1000 / 60);

					executors = new ArrayList<String[]>();

					Iterator<Long> iter = ExecutorPool.getInstance().getPool().keySet().iterator();
					while (iter.hasNext()) {
						Long key = iter.next();
						ExecutorPortfolio ep =  ExecutorPool.getInstance().getPool().get(key);
						executors.add(new String[] { ep.getPortfolioID() + "", ep.getPortfolioName() });
					}
					title = Action.dailyUpdate.getTitle();

				} catch (RuntimeException e) {
					
				}

			} else {
			}//end of dailyUpdate
		}else{
			isUpdating = Action.batchUpdate!=null && Action.batchUpdate.isAlive();
			if (isUpdating) {
				try {
					
					startDate = Action.batchUpdate.startDate;
					runningPeriod = String.valueOf(((new Date()).getTime() - Action.batchUpdate.startDate.getTime()) * 1.0 / 1000 / 60);
					updateSize = Action.batchUpdate.updateSize;
					processing = (Action.batchUpdate.currentPointer * 100.0 / Action.batchUpdate.updateSize) + "";
					portfolioID = Action.batchUpdate.currentPortfolioID;
					portfolioName = Action.batchUpdate.currentPortfolioName;
					currentStartDate = Action.batchUpdate.currentStartDate;
					currentRunningPortfolioPeriod = String.valueOf(((new Date()).getTime() - Action.batchUpdate.currentStartDate.getTime()) * 1.0 / 1000 / 60);

					executors = new ArrayList<String[]>();

					Iterator<Long> iter = ExecutorPool.getInstance().getPool().keySet().iterator();
					while (iter.hasNext()) {
						Long key = iter.next();
						ExecutorPortfolio ep =  ExecutorPool.getInstance().getPool().get(key);
						executors.add(new String[] { ep.getPortfolioID() + "", ep.getPortfolioName() });
					}
					title = Action.batchUpdate.getTitle();

				} catch (RuntimeException e) {
					
				}

			} else {
			}//end of dailyUpdate
		}
		
		
		return "dailymessage.ftl";
	}

	public boolean getIsUpdating() {
		return isUpdating;
	}

	public void setIsUpdating(boolean isUpdating) {
		this.isUpdating = isUpdating;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public String getRunningPeriod() {
		return runningPeriod;
	}

	public void setRunningPeriod(String runningPeriod) {
		this.runningPeriod = runningPeriod;
	}

	public Integer getUpdateSize() {
		return updateSize;
	}

	public void setUpdateSize(Integer updateSize) {
		this.updateSize = updateSize;
	}

	public String getProcessing() {
		return processing;
	}

	public void setProcessing(String processing) {
		this.processing = processing;
	}

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public Date getCurrentStartDate() {
		return currentStartDate;
	}

	public void setCurrentStartDate(Date currentStartDate) {
		this.currentStartDate = currentStartDate;
	}

	public String getCurrentRunningPortfolioPeriod() {
		return currentRunningPortfolioPeriod;
	}

	public void setCurrentRunningPortfolioPeriod(String currentRunningPortfolioPeriod) {
		this.currentRunningPortfolioPeriod = currentRunningPortfolioPeriod;
	}

	public List<String[]> getExecutors() {
		return executors;
	}

	public void setExecutors(List<String[]> executors) {
		this.executors = executors;
	}

}
