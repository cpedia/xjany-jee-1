package com.lti.action.admin.strategy;

import java.io.File;
import java.io.FileReader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.lti.action.Action;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;

public class SimilarIssuesAction extends ActionSupport implements Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String planID;
	
	public String getPlanID() {
		return planID;
	}
	public void setPlanID(String planID) {
		this.planID = planID;
	}
	
	public String execute() throws Exception{
		StrategyManager stragetyManager = ContextHolder.getStrategyManager();
		if(planID.equals("")){
			List<Strategy> planList = stragetyManager.getStrategiesByType(Configuration.STRATEGY_TYPE_401K);
			statistic(planList);
		}else{
			String [] idList = planID.split(",");
			List<Strategy> planList = new ArrayList<Strategy>();
			for(String ss:idList){
				long ID = Long.parseLong(ss);
				Strategy plan = stragetyManager.get(ID);
				if(!plan.is401K())continue;
				
				if(plan!=null){
					planList.add(plan);	
				}				
			}
		 statistic(planList);
		}
		
		return Action.SUCCESS;
	}
	public static void statistic(List<Strategy> planList) throws Exception{		
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		StrategyManager stragetyManager = ContextHolder.getStrategyManager();
		for(Strategy plan:planList){
			String message="";
			long ID = plan.getID();
			List<VariableFor401k> candidateFunds = stragetyManager.getVariable401KByStrategyID(ID);
			if(candidateFunds == null)continue;
			List<String> tickerList = new ArrayList<String>();
			List<String> assetsList = new ArrayList<String>();
			List<String> majorList = new ArrayList<String>();
			for(VariableFor401k va : candidateFunds){
				String s = va.getSymbol();
				tickerList.add(s);
			}
			for(String ss:tickerList){
				Security se = securityManager.getBySymbol(ss);
				if(se!= null&&se.getAssetClass()!=null){
					String assetName = se.getAssetClass().getName();
					if(!assetsList.contains(assetName)&&!assetName.equals("ROOT")) assetsList.add(assetName);
				}
			}
			Map<String, String> assetclass = new HashMap<String, String>();
			File csvFile = new File(URLDecoder.decode(com.lti.action.admin.strategy.SimilarIssuesAction.class.getResource("6_main_asset_classes.csv").getFile(), "utf-8"));
			CsvListReader clr = new CsvListReader(new FileReader(csvFile), CsvPreference.STANDARD_PREFERENCE);
			List<String> head = clr.read();
			String[] arrs = new String[head.size()];
			head.toArray(arrs);

			List<String> list = null;

			while ((list = clr.read()) != null) {
				for (int i = 0; i < 7; i++) {
					String cell = list.get(i).trim();
					if (!cell.equals("")) {
						assetclass.put(cell, arrs[i]);
					}
				}

			}				
			for(String asset:assetsList){
				String major = assetclass.get(asset.trim());
				if (major != null ) {
					if (!major.equals("Other")&&!majorList.contains(major)){
						majorList.add(major);
					}
			    }
		     }
			message = message+"The plan consists of "+tickerList.size()+" funds. ";
			message = message+"It covers "+majorList.size()+" major asset classes and "+assetsList.size()+" minor asset classes. ";
			message = message+"The major asset classes it covers are";
			for(int i =0;i<majorList.size();i++){
				message = message+" "+majorList.get(i);
				if(i<majorList.size()-2){
					message+=",";
				}
				if(i==majorList.size()-2){
					message+=" and";
				}
			}
			message+=".";
			stragetyManager.updateStrategySimilarIssues(ID, message);
		}
	}
}
