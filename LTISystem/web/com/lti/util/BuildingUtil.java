package com.lti.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lti.jobscheduler.DailyExecutionJob;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.ContextHolder;

public class BuildingUtil {
	public static long build(String portfolioname, long userid, Strategy plan, Portfolio templateportfolio,  Map<String,String> map)throws Exception {
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();

		Portfolio p = null;
		if (templateportfolio == null) {
			p = portfolioManager.get(0l).clone();
		}else{
			p = templateportfolio.clone();
		}

		Map<String, String> ht = p.getStrategies().getAssetAllocationStrategy().getParameter();
		
		if(ht==null){
			ht=new HashMap<String, String>();
			p.getStrategies().getAssetAllocationStrategy().setParameter(ht);
		}
		
		if(map!=null){
			Iterator<String> keys=map.keySet().iterator();
			while(keys.hasNext()){
				String key=keys.next();
				String value=map.get(key);
				ht.put(key, value);
			}
		}
		
		p.setID(null);
		if (portfolioname != null && !portfolioname.equals("")) {
			p.setName(portfolioname);
		} else {
			p.setName(plan.getID() + " " + System.currentTimeMillis());
		}
		p.setUserID(userid);

		List<VariableFor401k> variables = plan.getVariablesFor401k();
		StringBuffer AssetClass = new StringBuffer();
		StringBuffer CandidateFunds = new StringBuffer();
		StringBuffer RedemptionLimit = new StringBuffer();

		if (variables != null) {
			for (int i = 0; i < variables.size(); i++) {
				if (variables.get(i).getMemo() != null && variables.get(i).getMemo().toLowerCase().startsWith("n"))
					continue;
				AssetClass.append(variables.get(i).getAssetClassName());
				CandidateFunds.append(variables.get(i).getSymbol());
				RedemptionLimit.append(variables.get(i).getRedemption());
				if (i != variables.size() - 1) {
					AssetClass.append(",");
					CandidateFunds.append(",");
					RedemptionLimit.append(",");
				}
			}
		}
		ht.put("AssetClass", AssetClass.toString());
		ht.put("CandidateFund", CandidateFunds.toString());
		ht.put("RedemptionLimit", RedemptionLimit.toString());

		p.setProduction(true);
		p.setOriginalPortfolioID(null);
		p.setMainStrategyID(null);
		portfolioManager.save(p);
		return p.getID();
	}
	
	public static void monitor(long portfolioid){
		String url = "http://127.0.0.1:8081/execute?isCustomized=true&portfolioID=" + portfolioid;
		try {
			DailyExecutionJob.sendRequest(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
