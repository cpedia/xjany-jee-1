package com.lti.executor.web.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.Exception.Executor.NotAllowRunningException;
import com.lti.cache.YearCache;
import com.lti.executor.BatchExecutorThread;
import com.lti.executor.Executor;
import com.lti.executor.ExecutorPool;
import com.lti.executor.web.BasePage;
import com.lti.executor.web.PortfoliosFilter;
import com.lti.service.bo.Portfolio;
import com.lti.type.finance.ExecutorPortfolio;
import com.lti.util.StringUtil;

public class Dailystart extends BasePage {

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	@Override
	public String execute() throws Exception {
		int mode = 0;
		try {
			mode = Integer.parseInt(request.getParameter("mode"));
		} catch (Exception e) {
		}

		if (mode == 0 && Action.isDailyUpdate()) {
			info = "Daily update is running.";
			return "info.ftl";
		}
		
//		if (mode == 1 && Action.isBatchUpdate()) {
//			info = "Batch update is running.";
//			return "info.ftl";
//		}
		
		Long planID = null;
		if(planID == null){
			try{
				planID = Long.parseLong(request.getParameter("planID"));
			}catch(Exception e){
				
			}
		}
		
		String title = request.getParameter("title");
		if (title == null) {
			if(mode==0)title = "Daily Update";
			else title="Batch Update";
		}
		String filter = request.getParameter("filter");

		if (filter == null) {
			if (mode == 0)
				filter = "DefaultFilter";
			if (mode == 1)
				filter = "PlanFilter";
		}
		PortfoliosFilter portfoliosFilter;
		try {
			portfoliosFilter = null;
			Class cls = Class.forName("com.lti.executor.web.filter." + filter);
			portfoliosFilter = (PortfoliosFilter) cls.newInstance();
			portfoliosFilter.setParamters(request.getParameterMap());
		} catch (Exception e) {
			info = "<pre>" + StringUtil.getStackTraceString(e) + "<pre>";
			return "info.ftl";
		}
		
		int priority = 3;
		try{
			priority = Integer.parseInt(request.getParameter("priority"));
		}catch(Exception e){
		}
		
		boolean forceMonitor=false;
		try {
			forceMonitor = Boolean.parseBoolean(request.getParameter("forceMonitor"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (mode == 0) {
			Action.dailyUpdate = new BatchExecutorThread("Daily update");
			Action.dailyUpdate.setFilter(portfoliosFilter);
			Action.dailyUpdate.setMode(0);
			Action.dailyUpdate.setTitle(title);
			Action.dailyUpdate.setForceMonitor(forceMonitor);
			YearCache.getInstance().removeAll();
			Action.dailyUpdate.start();
		} else if(mode == 1){
			if(Action.batchUpdate == null || !Action.batchUpdate.isAlive()){
				Action.batchUpdate = new BatchExecutorThread("Batch update");
				Action.batchUpdate.setMode(1);
				Action.batchUpdate.setTitle(title);
			}
			List<Portfolio> portfolioList = portfoliosFilter.getPortfolios(forceMonitor);
			List<ExecutorPortfolio> executorPortfolioList = new ArrayList<ExecutorPortfolio>();
			for(Portfolio p : portfolioList){
				ExecutorPortfolio ep = new ExecutorPortfolio();
				ep.setPriority(priority);
				ep.setForceMonitor(forceMonitor);
				ep.setEndDate(p.getEndDate());
				ep.setPortfolioID(p.getID());
				ep.setPortfolioName(p.getName());
				executorPortfolioList.add(ep);
			}
			Action.batchUpdate.addPlanPortfolios(planID, executorPortfolioList);
			if(!Action.batchUpdate.isAlive())
				Action.batchUpdate.start();
		}

		info = "OK";
		System.out.println(new Date());
		return "info.ftl";
	}

}
