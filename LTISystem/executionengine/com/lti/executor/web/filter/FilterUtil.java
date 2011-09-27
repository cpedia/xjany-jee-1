package com.lti.executor.web.filter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioState;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;

public abstract class FilterUtil {
	public static List<Portfolio> skipUpdated(List<Portfolio> updatePortfolios) {
		return skipUpdated(updatePortfolios, false);
	}
	
	public static List<Portfolio> skipUpdated(List<Portfolio> updatePortfolios,boolean forceMonitor) {
		if (updatePortfolios == null || updatePortfolios.size() == 0) {
			return null;
		}
		if(forceMonitor){
			return updatePortfolios;
		}
		List<Portfolio> list = new ArrayList<Portfolio>();
		Calendar ca = Calendar.getInstance();
		PortfolioManager pm = ContextHolder.getPortfolioManager();
		for (Portfolio p : updatePortfolios) {
			PortfolioState ps = pm.getPortfolioState(p.getID());
			if (ps == null || ps.getState() == null || (ps.getState() != Configuration.PORTFOLIO_RUNNING_STATE_FINISHED && ps.getState() != Configuration.PORTFOLIO_RUUNING_STATE_DAILYEXECUTION_FINISHED)) {
				//if(forceMonitor)list.add(p);
				continue;
			}
			if (p.getEndDate() != null) {
				ca.setTimeInMillis(System.currentTimeMillis());
				if (ca.get(Calendar.HOUR_OF_DAY) < 16) {
					ca.add(Calendar.DAY_OF_YEAR, -1);
				}
				Date tdate = LTIDate.getRecentNYSETradingDay(ca.getTime());

				if (LTIDate.equals(tdate, p.getEndDate())) {
					continue;
				}
			}
			list.add(p);
		}
		return list;
	}
	/**
	 * skip those portfolio.endDate equal or after endDate
	 * @param updatePortfolios
	 * @param endDate
	 * @return
	 */
	public static List<Portfolio> skipByEndDate(List<Portfolio> updatePortfolios, Date endDate, Integer dateMode, boolean forceMonitor){
		if (updatePortfolios == null || updatePortfolios.size() == 0) {
			return null;
		}
		if(forceMonitor){
			return updatePortfolios;
		}
		List<Portfolio> list = new ArrayList<Portfolio>();
		PortfolioManager pm = ContextHolder.getPortfolioManager();
		for (Portfolio p : updatePortfolios) {
			PortfolioState ps = pm.getPortfolioState(p.getID());
			if (ps == null || ps.getState() == null || (ps.getState() != Configuration.PORTFOLIO_RUNNING_STATE_FINISHED && ps.getState() != Configuration.PORTFOLIO_RUUNING_STATE_DAILYEXECUTION_FINISHED)) {
				continue;
			}
			switch(dateMode){
				case 0://before and on
					if (p.getEndDate() == null || !LTIDate.after(p.getEndDate(), endDate))
						list.add(p);
					break;
				case 1://on
					if (p.getEndDate() != null && LTIDate.equals(p.getEndDate(), endDate))
						list.add(p);
					break;
				case 2://on and after
					if(p.getEndDate() != null && !LTIDate.before(p.getEndDate(), endDate))
						list.add(p);
					break;
			}
		}
		return list;
	}

}
