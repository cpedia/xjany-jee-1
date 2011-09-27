package com.lti.executor.web.filter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lti.executor.web.PortfoliosFilter;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;

public class PortfolioIDFilter implements PortfoliosFilter {

	private long[] ids;
	
	private List<Portfolio> portfolios=new ArrayList<Portfolio>();
	
	private Date endDate = null;
	
	private Integer dateMode = 0;
	
	@Override
	public List<Portfolio> getPortfolios(boolean forceMonitor) {
		PortfolioManager pm=ContextHolder.getPortfolioManager();
		for(long id:ids){
			Portfolio ps=pm.getBasicPortfolio(id);
			if(ps!=null){
				portfolios.add(ps);
			}
		}
		portfolios=FilterUtil.skipUpdated(portfolios, forceMonitor);
		if(endDate != null)
			portfolios = FilterUtil.skipByEndDate(portfolios, endDate, dateMode ,forceMonitor);
		return portfolios;
	}

	@Override
	public void setParamters(Map parameters) {
		String _ids=((String[]) parameters.get("ids"))[0];
		String[] _strs=_ids.split(",");
		ids=new long[_strs.length];
		for(int i=0;i<_strs.length;i++){
			ids[i]=Long.parseLong(_strs[i].trim());
		}
		Object oDate = parameters.get("endDate");
		if(oDate != null){
			String _endDate = ((String[]) oDate)[0];
			endDate = LTIDate.parseStringToDate(_endDate);
		}
		Object oMode = parameters.get("dateMode");
		if(oMode != null){
			String _dateMode = ((String[])oMode)[0];
			dateMode = Integer.parseInt(_dateMode);
		}
	}


}
