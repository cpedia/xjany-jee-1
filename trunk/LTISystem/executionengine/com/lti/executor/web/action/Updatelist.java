package com.lti.executor.web.action;

import java.util.List;

import com.lti.executor.web.BasePage;
import com.lti.service.bo.Portfolio;
import com.lti.type.finance.ExecutorPortfolio;
import com.lti.util.StringUtil;

public class Updatelist extends BasePage {

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	private Integer totalSize;
	private Integer currentPointer;
	private Long currentPortfolioID;

	public Integer getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	public Integer getCurrentPointer() {
		return currentPointer;
	}

	public void setCurrentPointer(Integer currentPointer) {
		this.currentPointer = currentPointer;
	}

	public Long getCurrentPortfolioID() {
		return currentPortfolioID;
	}

	public void setCurrentPortfolioID(Long currentPortfolioID) {
		this.currentPortfolioID = currentPortfolioID;
	}

	public List<ExecutorPortfolio> portfolios;
	

	@Override
	public String execute() throws Exception {
		try {
			Integer mode = null;
			try{
				mode = Integer.parseInt(request.getParameter("mode"));
			}catch(Exception e){
				
			}
			if(mode == 0){
				if (Action.dailyUpdate!=null&&Action.dailyUpdate.isAlive()) {
					portfolios = Action.dailyUpdate.getPortfolios();
					totalSize = portfolios.size();
					currentPointer = Action.dailyUpdate.currentPointer;
					currentPortfolioID = Action.dailyUpdate.currentPortfolioID;

				} else {
					info = "Daily update is not running.";
					return "info.ftl";
				}
			}else{
				if (Action.batchUpdate!=null&&Action.batchUpdate.isAlive()) {
					portfolios = Action.batchUpdate.getPortfolios();
					totalSize = portfolios.size();
					currentPointer = Action.batchUpdate.currentPointer;
					currentPortfolioID = Action.batchUpdate.currentPortfolioID;

				} else {
					info = "Batch update is not running.";
					return "info.ftl";
				}
			}
			

			
		} catch (Exception e) {
			info = "<pre>" + StringUtil.getStackTraceString(e) + "</pre>";
			return "info.ftl";
		}
		return "updatelist.ftl";

	}

	public List<ExecutorPortfolio> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<ExecutorPortfolio> portfolios) {
		this.portfolios = portfolios;
	}

}
