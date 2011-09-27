package com.lti.executor.web.filter;

import java.util.List;
import java.util.Map;

import com.lti.executor.web.PortfoliosFilter;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.ContextHolder;

public class DefaultFilter implements PortfoliosFilter {

	
	private List<Portfolio> portfolios;
	
	@Override
	public List<Portfolio> getPortfolios(boolean forceMonitor) {
		PortfolioManager pm=ContextHolder.getPortfolioManager();
		portfolios=pm.getSimplePortfolios(-1,-1);
		portfolios=FilterUtil.skipUpdated(portfolios);
		return portfolios;
	}

	@Override
	public void setParamters(Map parameters) {
		
		
	}
	
	public static void main(String[] args){
		PortfolioManager pm=ContextHolder.getPortfolioManager();
		long t1=System.currentTimeMillis();
		pm.getSimplePortfolios(-1,-1);
		long t2=System.currentTimeMillis();
		pm.getPortfolios(-1,-1);
		long t3=System.currentTimeMillis();
		System.out.println("时间1："+(t2-t1));
		System.out.println("时间2："+(t3-t2));
	}

}
