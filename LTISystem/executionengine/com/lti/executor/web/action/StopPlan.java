package com.lti.executor.web.action;

import java.util.ArrayList;
import java.util.List;

import com.lti.executor.web.BasePage;
import com.lti.service.bo.Portfolio;
import com.lti.system.ContextHolder;

public class StopPlan extends BasePage {

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	

	@Override
	public String execute() throws Exception {
		
		String func=request.getParameter("function");
		try {
			Long planID = Long.parseLong(request.getParameter("planID"));
			if(Action.batchUpdate != null && Action.batchUpdate.isAlive() && Action.batchUpdate.containPlanID(planID)){
				List<Portfolio> portfolioList = ContextHolder.getStrategyManager().getModelPortfolios(planID);
				List<Long> portfolioIDList = new ArrayList<Long>();
				if(portfolioList.size() > 0){
					for(Portfolio p: portfolioList){
						portfolioIDList.add(p.getID());
					}
					Action.batchUpdate.stopPlanPortfolios(planID, portfolioIDList);
				}
			}else{
				info = "the plan is not running.";
			}
			info="ok";
			if(func!=null){
				info=func+"(\"ok\")";
				return "info.ftl";
			}
		} catch (Throwable e) {
			info = "error:"+e.getMessage();
		}
		if(func!=null){
			info=func+"(\"error\")";
		}
		return "info.ftl";
	}

}
