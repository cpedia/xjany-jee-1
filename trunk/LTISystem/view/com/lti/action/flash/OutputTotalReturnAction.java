package com.lti.action.flash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityMPT;
import com.lti.system.Configuration;
import com.lti.util.SecurityMPTIDComp;
import com.lti.util.SecurityUtil;
import com.opensymphony.xwork2.ActionSupport;

public class OutputTotalReturnAction extends ActionSupport implements Action {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SecurityManager securityManager;
	
	private PortfolioManager portfolioManager;
	
	private String symbolList;
	
	private String xml;	
	
	@Override
	public void validate() {
		// TODO Auto-generated method stub
		super.validate();
		if(symbolList == null)
			addActionError("No Securities Are Selected!");
	}
	
	@Override
	public String execute() throws Exception {
		// TODO Auto-generated method stub
		String[] symbols = symbolList.split(",");
		List<Security> securityList = new ArrayList<Security>();
		for(int i = 0; i < symbols.length; i++){
			Security s = securityManager.get(symbols[i]);
			securityList.add(s);			
		}
		xml = generateXML(securityList);
		System.out.print(xml);
		return Action.SUCCESS;
	}

	private String generateXML(List<Security> securities){
		List<Long> sids = new ArrayList<Long>(); 
		List<Long> pids = new ArrayList<Long>();
		List<Security> notPortfolio = new ArrayList<Security>();
		for (int i = 0; i < securities.size(); i++) {
			Security s = securities.get(i);
			if (s.getSecurityType() != Configuration.SECURITY_TYPE_PORTFOLIO){
				notPortfolio.add(s);
				sids.add(s.getID());
			}
			else {
				String symbol = s.getSymbol();
				String IDstr = symbol.substring(symbol.indexOf("_") + 1);
				Long pid = Long.parseLong(IDstr);
				pids.add(pid);
			}
		}
		if((sids == null || sids.size() == 0) && (pids == null || pids.size() == 0))
			return null;
		
		
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<Beta><Data>");
		List<List<SecurityMPT>> mpts = new ArrayList<List<SecurityMPT>>();
		Long[] years = {-1l, -3l, -5l, 0l};
		Comparator<SecurityMPT> comparator = new SecurityMPTIDComp();
		if(sids != null && sids.size() > 0){
			Collections.sort(sids);	
			for(int i = 0; i < years.length - 1; i++) //securitympt doesn't have the up-to-date data "0"
			{
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(SecurityMPT.class);
				detachedCriteria.add(Restrictions.in("SecurityID", sids));
				detachedCriteria.add(Restrictions.eq("Year", years[i]));
				detachedCriteria.addOrder(Order.asc("SecurityID"));
				List<SecurityMPT> mpt = securityManager.getSecurityByMPT(detachedCriteria);
				Collections.sort(mpt, comparator);
				mpts.add(mpt);
			}
			List<SecurityMPT> uptodateMPTs = new ArrayList<SecurityMPT>();
			SecurityUtil.translateSecurityToMPT(notPortfolio, uptodateMPTs);
			for(int i = 0; i < notPortfolio.size(); i++){
				Security s = notPortfolio.get(i);
				Date stratDate = securityManager.getStartDate(s.getID());
				uptodateMPTs.get(i).setYear(0l);
				try {					
					uptodateMPTs.get(i).setReturn(s.getReturn(stratDate, securityManager.getLatestDailydata(s.getID()).getDate()));
				} catch (Exception e) {
					// TODO: handle exception
					uptodateMPTs.get(i).setReturn(0.0);
					e.printStackTrace();
				}
			}
			Collections.sort(uptodateMPTs, comparator);
			mpts.add(uptodateMPTs);
		}
		if(pids != null && pids.size() > 0){
			Collections.sort(pids);
			for(int i = 0; i < years.length; i++) //securitympt doesn't have the up-to-date data "0"
			{
				DetachedCriteria detachedCriteria = DetachedCriteria.forClass(PortfolioMPT.class);
				detachedCriteria.add(Restrictions.in("portfolioID", pids));
				detachedCriteria.add(Restrictions.eq("year", years[i]));
				detachedCriteria.addOrder(Order.asc("portfolioID"));
				List<PortfolioMPT> pmpt = portfolioManager.findByCriteria(detachedCriteria);
				List<SecurityMPT> smpt = SecurityUtil.translatePortToSecMPT(pmpt, null);
				Collections.sort(smpt, comparator);
				mpts.get(i).addAll(smpt);
			}
		}
		for(int i = 0; i < years.length; i++){
			String date = "";
			List<SecurityMPT> mpt = mpts.get(i);
			switch (years[i].intValue()) {
			case -1:
				date = "last one year";
				break;
			case -3:
				date = "last three year";
				break;
			case -5:
				date = "last five year";
				break;
			case 0:
				date = "up to date";
				break;
			default:
				break;
			}
			String betas = "";
			int j = 0, k = 0;
			while(j < sids.size() + pids.size() && k < mpt.size()){
				Long id;
				if(j < sids.size())
					id = sids.get(j);
				else
					id = pids.get(j - sids.size());
				SecurityMPT se = mpt.get(k);
				if(id.longValue() == se.getSecurityID().longValue()){
					betas += se.getReturn();
					j++; k++;
				}
				else
				{
					betas += 0.0;
					j++;
				}
				if(j != sids.size() + pids.size())
					betas += ",";
			}
			while(j < sids.size() + pids.size()){
				betas += 0.0;
				if(j != sids.size() + pids.size())
					betas += ",";
				j++;
			}
			sb.append("<E d='" + date + "' v='" + betas + "'/>");
		}
		sb.append("</Data></Beta>");
		return sb.toString();
	}
	

	public String getSymbolList() {
		return symbolList;
	}

	public void setSymbolList(String symbolList) {
		this.symbolList = symbolList;
	}

	public String getXml() {
		return xml;
	}

	public void setXml(String xml) {
		this.xml = xml;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}
}
