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
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

public class Stop extends BasePage {

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
			Long portfolioID = Long.parseLong(request.getParameter("portfolioID"));

			Portfolio p=ContextHolder.getPortfolioManager().get(portfolioID);
			if(p==null){
				info="the portfolio does not exsit.";
			}else if(!ExecutorPool.getInstance().isRunning(portfolioID)){
				info="the portfolio is not running.";
			}else{
				ExecutorPool.getInstance().getThreads().get(portfolioID).stop();
				info="ok";
				
				if(func!=null){
					info=func+"(\"ok\")";
					return "info.ftl";
				}
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
