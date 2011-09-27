package com.lti.executor.web.filter;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.lti.executor.web.PortfoliosFilter;
import com.lti.permission.MPIQChecker;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.StringUtil;

/**
 * Only update portfolios which is in email alert list.
 * 
 * @author Administrator
 * 
 */
public class EmailAlertFilter implements PortfoliosFilter {

	@Override
	public List<Portfolio> getPortfolios(boolean forceMonitor) {
		List<Portfolio> updatePortfolios = new ArrayList<Portfolio>();
		PortfolioManager pm = ContextHolder.getPortfolioManager();
		UserManager um = ContextHolder.getUserManager();
		List<Object> ids = null;
		try {
			ids = pm.findBySQL("select distinct(portfolioid) from " + Configuration.TABLE_EMAILNOTIFICATION);
		} catch (Exception e1) {
			System.out.println(StringUtil.getStackTraceString(e1));
		}
		DateFormat df=new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss:");
		for (Object o : ids) {
			Portfolio p = pm.get(((BigInteger) (o)).longValue());
			if (p != null && p.getUserID()!=null ){
				MPIQChecker mp=new MPIQChecker(p.getUserID());
				if(mp.hasSubscred()||p.isSAAPortfolio()){
					updatePortfolios.add(p);
				}else{
					System.out.println(df.format(new Date())+"["+p.getID()+"]["+p.getName()+"],["+p.getUserID()+"]["+p.getUserName()+"]");
				}
			}
		}
		if (updatePortfolios.size() != 0) {
			if (skiped)
				updatePortfolios = FilterUtil.skipUpdated(updatePortfolios);
			return updatePortfolios;
		}
		return null;
	}

	@Override
	public void setParamters(Map parameters) {

	}

	private boolean skiped = true;



}
