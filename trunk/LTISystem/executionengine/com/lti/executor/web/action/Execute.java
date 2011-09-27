package com.lti.executor.web.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lti.executor.ExecutorPool;
import com.lti.executor.ExecutorThread;
import com.lti.executor.web.BasePage;
import com.lti.executor.web.ExecutorListenerForWeb;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.finance.ExecutorPortfolio;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

public class Execute extends BasePage {

	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	
	

	@Override
	public String execute() throws Exception {
		
		
		try {
			Long portfolioID = Long.parseLong(request.getParameter("portfolioID"));
			Boolean isCustomized = false;
			try{
				isCustomized = Boolean.parseBoolean(request.getParameter("isCustomized"));
			}catch(Exception e){
				isCustomized = false;
			}
			
			DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
			DateFormat df2=new SimpleDateFormat("MM/dd/yyyy");
			Date _endDate=null;
			try {
				_endDate=df.parse(request.getParameter("endDate"));
			} catch (Exception e) {
				try {
					_endDate=df2.parse(request.getParameter("endDate"));
				} catch (Exception e1) {
				}
			}
			int _portfolioUpdate=Configuration.PORTFOLIO_UPDATE_IGNOREIFCANNOTUPDATE;
			try {
				_portfolioUpdate=Integer.parseInt(request.getParameter("portfolioUpdate"));
			} catch (Exception e) {
			}	
			Portfolio p=ContextHolder.getPortfolioManager().get(portfolioID);
			if(p==null){
				info="the portfolio does not exist.";
				return "info.ftl";
			}
			ExecutorPortfolio ep = new ExecutorPortfolio();
			ep.setEndDate(p.getEndDate());
			ep.setPortfolioID(p.getID());
			ep.setPortfolioName(p.getName());
			ep.setPriority(3);
			ExecutorThread et=new ExecutorThread(ep, _endDate, _portfolioUpdate, isCustomized, Action.listener);
			
			et.start();
			
			info="start.";
			String func=request.getParameter("function");
			if(func!=null){
				info=func+"(\"ok\")";
			}
		} catch (Exception e) {
			info = "<pre>" + StringUtil.getStackTraceString(e) + "</pre>";
			String func=request.getParameter("function");
			if(func!=null){
				info=func+"(\"error\")";
			}
		}
		return "info.ftl";
	}

}
