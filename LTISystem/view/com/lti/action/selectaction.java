package com.lti.action;

import java.math.BigInteger;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.lti.action.ajax.EmailAlertAction;
import com.lti.bean.ModelPortfoliosBean;
import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.permission.PermissionChecker;
import com.lti.permission.PortfolioPermissionChecker;
import com.lti.service.AssetClassManager;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.ProfileManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.AllocationTemplate;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Profile;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.StrategyCode;
import com.lti.service.bo.User;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.Menu;
import com.lti.type.executor.CodeInf;
import com.lti.util.Sort;
import com.lti.util.validate.TotalReturnValidator;

public class selectaction {

	private Long strategyID;
	private Long portfolioID;
	private Long planID;
	private Portfolio portfolio;
	private String frequency;
	private String planName;
	private String name;
	private String strategyName;
	private Double riskProfileNumber;
	private String portfolioName;
	private String candidateFund;
	private String EMAValue;
	private String SMAValue;
	private String WMAValue;
	private String WMAUnit;
	private String EMAUnit;
	private String SMAUnit;
	private List<String> fundList;
	private List<Integer> EMAValueList;
	private List<Integer> SMAValueList;
	private List<Integer> WMAValueList;
	private String useEMAFlag;
	private String useSMAFlag;
	private String useWMAFlag;
	private PermissionChecker pc;
	
	
	public PermissionChecker getPc() {
		return pc;
	}

	public void setPc(PermissionChecker pc) {
		this.pc = pc;
	}

	public List<AllocationTemplate> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<AllocationTemplate> templateList) {
		this.templateList = templateList;
	}
	

	public String getJsonString() {
		return jsonString;
	}

	public void setJsonString(String jsonString) {
		this.jsonString = jsonString;
	}


	/**
	 * 准备customize portfolio
	 */
	 private List<AllocationTemplate> templateList;
	 private String jsonString;
	 private static Map<String,List<String>> assetClassFundMap;
	 private static List<AssetClass> planAssetClassList;
	 private static List<String> symbolList ;
	 private String defaultDescription;
	 private List<String> defaultRiskyAssetClassList;
	 private List<String> defaultRiskyPercentageList;
	 private List<String> defaultRiskyFundNumList;
	 private List<String> defaultStableAssetClassList;
	 private List<String> defaultStablePercentageList;
	 private List<String> defaultStableFundNumList;
     private Map<String,List<VariableFor401k>> fundtableMap;
	 private String defaultTotalRiskyPercentage;
	 private String defaultTotalStablePercentage;
	
	public String entry() {
		if (portfolioID == null) {
			return Action.SUCCESS;
		}
		PortfolioManager pm = ContextHolder.getPortfolioManager();
		AssetClassManager acm = ContextHolder.getAssetClassManager();
		StrategyManager sm = ContextHolder.getStrategyManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		ProfileManager profileManager=(ProfileManager)ContextHolder.getInstance().getApplicationContext().getBean("profileManager");
		portfolio = pm.get(portfolioID);
		if(strategyID == null)
			strategyID = portfolio.getStrategies().getAssetAllocationStrategy().getID();
		if(strategyID!=null){
			if(strategyID.longValue()==Configuration.STRATEGY_SAA_ID){
				portfolio.getStrategies().getAssetAllocationStrategy().setName("Strategic Asset Allocation");
				portfolio.getStrategies().getAssetAllocationStrategy().setID(Configuration.STRATEGY_SAA_ID);
				portfolio.setName(portfolio.getName().replaceAll("Tactical", "Strategic"));
			}
			if(strategyID.longValue()==Configuration.STRATEGY_TAA_ID){
				portfolio.getStrategies().getAssetAllocationStrategy().setName("Tactical Asset Allocation");
				portfolio.getStrategies().getAssetAllocationStrategy().setID(Configuration.STRATEGY_TAA_ID);
				portfolio.setName(portfolio.getName().replaceAll("Strategic", "Tactical"));
			}
		}
		
		long userID = 0l;
		if(name!=null&&!name.trim().equals("")){
			User user=ContextHolder.getUserManager().getLoginUser();
			portfolio.setUserID(user.getID());
			userID = user.getID();
		}
		pc = new PortfolioPermissionChecker(portfolio, ServletActionContext.getRequest());
		if (!pc.hasViewRole()) {
			message = "You are not authorized to visit this page.";
			return Action.MESSAGE;
		}
		frequency = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("RebalanceFrequency");
		if (frequency == null || frequency.equals("")) {
			frequency = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("CheckFrequency");
		}
		
		
		//riskProfileNumber = Double.parseDouble(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("RiskProfile").replace("\"", ""));
		strategyName = portfolio.getStrategies().getAssetAllocationStrategy().getName();
		strategyID = portfolio.getStrategies().getAssetAllocationStrategy().getID();
		try {
			planID = Long.parseLong(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID"));
		} catch (Exception e) {
			// e.printStackTrace();
		}
		if(planID==null)planID=portfolio.getMainStrategyID();
		
		
		if(name!=null&&!name.trim().equals("")){
			planName = name;
			planID = sm.get(name).getID();
			DecimalFormat def=new DecimalFormat("0");
			Profile profile = profileManager.get(0l, userID, 0l);
			if(profile!=null && profile.getRiskNumber()!=null)
				riskProfileNumber = profile.getRiskNumber();
			else
				riskProfileNumber=Double.parseDouble(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("RiskProfile").replaceAll("\"", ""));
			portfolioName=ContextHolder.getUserManager().getLoginUser().getUserName()+"_"+name+"_RiskProfile_"+def.format(riskProfileNumber);
		}
		else{
			planName=sm.get(planID).getName();
			riskProfileNumber=Double.parseDouble(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("RiskProfile").replaceAll("\"", ""));
			portfolioName=ContextHolder.getUserManager().getLoginUser().getUserName()+"_"+portfolio.getName();
		}
		candidateFund = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("CandidateFund");
		EMAValue = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("EMADays");
		SMAValue = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("SMADays");
		WMAValue = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("WMADays");
		useEMAFlag = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("UseEMAFilter");
		useSMAFlag = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("UseSMAFilter");
		useWMAFlag = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("UseWMAFilter");
		if(useEMAFlag == null || useEMAFlag == "")
			useEMAFlag = "false";
		if(useSMAFlag == null || useSMAFlag == "")
			useSMAFlag = "false";
		if(useWMAFlag == null || useWMAFlag == "")
			useWMAFlag = "false";
		fundList = new ArrayList<String>();
		EMAValueList = new ArrayList<Integer>();
		SMAValueList = new ArrayList<Integer>();
		WMAValueList = new ArrayList<Integer>();
		if(candidateFund != null){
			String[] funds = candidateFund.split(",");
			for(String fund: funds)
				fundList.add(fund);
		}
		
		if(EMAValue != null && !EMAValue.equals("0")){
			String[] emaValues = EMAValue.split(",");
			for(String emaValue: emaValues)
				EMAValueList.add(Integer.parseInt(emaValue));
		}
		
		if(SMAValue != null && !SMAValue.equals("0")){
			String[] smaValues = SMAValue.split(",");
			for(String smaValue: smaValues)
				SMAValueList.add(Integer.parseInt(smaValue));
		}
		
		if(WMAValue != null && !WMAValue.equals("0")){
			String[] wmaValues = WMAValue.split(",");
			for(String wmaValue: wmaValues)
				WMAValueList.add(Integer.parseInt(wmaValue));
		}
		
		if(EMAValueList.size() == 0){
			for(int i=0;i<fundList.size();++i)
				EMAValueList.add(0);
		}
		
		if(SMAValueList.size() == 0){
			for(int i=0;i<fundList.size();++i)
				SMAValueList.add(0);
		}
		
		if(WMAValueList.size() == 0){
			for(int i=0;i<fundList.size();++i)
				WMAValueList.add(0);
		}
		
		/***********************************************Asset Allocation*********************************/
		
		planAssetClassList = new ArrayList<AssetClass>();
		List<String> planAssetClassNameList = new ArrayList<String>();
		symbolList = new ArrayList<String>();
		List<Security> securityList = new ArrayList<Security>();
		Strategy plan = sm.get(planID);
		List<VariableFor401k> candidateList = plan.getVariablesFor401k();
		for(VariableFor401k vf:candidateList){
			Security se = securityManager.getBySymbol(vf.getSymbol());
			symbolList.add(vf.getSymbol());
			if(se != null && se.getAssetClass()!=null){
				securityList.add(se);
				String assetClassName = se.getAssetClass().getName();
				if(!assetClassName.trim().equals("ROOT") && !planAssetClassNameList.contains(assetClassName.trim().toLowerCase())){
					AssetClass assetClass = se.getAssetClass();
					planAssetClassList.add(assetClass);
					planAssetClassNameList.add(assetClassName.trim().toLowerCase());
				}
				
			}
		}
		if(!symbolList.contains("CASH")){
			symbolList.add("CASH");
		}
		
		if(planAssetClassList != null && planAssetClassList.size()!=0){
			List<AssetClass> childAssetClassList = planAssetClassList;
			for(;;){
				List<AssetClass> parentList = new ArrayList<AssetClass>();
				parentList = acm.getParentAssetClassList(childAssetClassList);
				if(parentList ==null || parentList.size()==0){
					break;
				}else{
					for(AssetClass a:parentList){
						if(!planAssetClassNameList.contains(a.getName().trim().toLowerCase())){
							planAssetClassList.add(a);
							planAssetClassNameList.add(a.getName().trim().toLowerCase());
						}
						
					}
					childAssetClassList = parentList;
				}
			}
		}
		
		riskyAssetClassList = new ArrayList<AssetClass>();
		for(AssetClass ac : planAssetClassList){
			String result = sm.getRiskyOrStableAsset(ac.getName().trim());
			if(result.equals("Risky Asset")){
				riskyAssetClassList.add(ac);
			}
		}
		
		stableAssetClassList = new ArrayList<AssetClass>();
		for(AssetClass ac : planAssetClassList){
			String result = sm.getRiskyOrStableAsset(ac.getName().trim());
			if(result.equals("Stable Asset")){
				stableAssetClassList.add(ac);
			}
		}
		
		/*************************************** default allocation******************************/
		AllocationTemplate defaultAct = sm.getAllocationTemplateById(1l);
		if(defaultAct != null){
			defaultDescription = defaultAct.getDescription();
			defaultRiskyAssetClassList = new ArrayList<String>();
			defaultRiskyPercentageList = new ArrayList<String>();
			defaultRiskyFundNumList = new ArrayList<String>() ;
			defaultStableAssetClassList = new ArrayList<String>();
			defaultStablePercentageList = new ArrayList<String>();
			defaultStableFundNumList = new ArrayList<String>();
			String defaultAssetClass = defaultAct.getAssetClassName();
			String defaultPercentage = defaultAct.getAssetClassWeight();
			String []defaultAssetItem = defaultAssetClass.split(",");
			String []defaultPercentItem = defaultPercentage.split(",");
			
			assetClassFundMap = new HashMap<String,List<String>>();
			assetClassFundMap = acm.getAvailableAssetClassSet(defaultAssetItem, symbolList, false, false);
				
			for(int i=0;i<defaultAssetItem.length;i++){
				String str = defaultAssetItem[i];
				String result = sm.getRiskyOrStableAsset(str);
				if(result.equals("Risky Asset")){
					defaultRiskyAssetClassList.add(str);
					defaultRiskyPercentageList.add(defaultPercentItem[i]);
				}else if(result.equals("Stable Asset")){
					defaultStableAssetClassList.add(str);
					defaultStablePercentageList.add(defaultPercentItem[i]);
				}
			}
			for(String s: defaultRiskyAssetClassList){
				List<String> riskyFundList = assetClassFundMap.get(s);
				if(riskyFundList != null){
					defaultRiskyFundNumList.add(String.valueOf(riskyFundList.size()));
				}else{
					defaultRiskyFundNumList.add("0");
				}
			}
			
			for(String ss: defaultStableAssetClassList){
				List<String> stableFundList = assetClassFundMap.get(ss);
				if(stableFundList != null){
					defaultStableFundNumList.add(String.valueOf(stableFundList.size()));
				}else{
					defaultStableFundNumList.add("0");
				}
			}
			
		}
		
		double trpercentage = 0;
		double tspercentage = 0;
		DecimalFormat df = new DecimalFormat("###.00");
		for(String s: defaultRiskyPercentageList){
			trpercentage+=Double.parseDouble(s);
		}

		defaultTotalRiskyPercentage = df.format(trpercentage);
		for(String s: defaultStablePercentageList){
			tspercentage +=Double.parseDouble(s);
		}
		defaultTotalStablePercentage = df.format(tspercentage);
		
		
		
		templateList = sm.getAllallocationTemplate();
		fundtableMap = sm.getSixMajorAssetClassForPlan(planID);
		
		
		
		return Action.SUCCESS;
	}
	
	
	public int getAssetType() {
		return assetType;
	}

	public void setAssetType(int assetType) {
		this.assetType = assetType;
	}

	private int assetType;
	private static List<AssetClass>riskyAssetClassList;
	private static List<AssetClass>stableAssetClassList; 	
	public String searchAssetClass(){
		
		AssetClassManager acm = ContextHolder.getAssetClassManager();
		StrategyManager sm = ContextHolder.getStrategyManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		PortfolioManager pm = ContextHolder.getPortfolioManager();
		List<AssetClass> matchAssetClassList = new ArrayList<AssetClass>();
		message = "";
		
		if(planAssetClassList==null || planAssetClassList.size()==0){
			if(portfolioID ==null) return Action.MESSAGE;
			portfolio = pm.get(portfolioID);
			try {
				planID = Long.parseLong(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID"));
			} catch (Exception e) {
				// e.printStackTrace();
			}
			if(planID==null)planID=portfolio.getMainStrategyID();
			planAssetClassList = new ArrayList<AssetClass>();
			List<String> planAssetClassNameList = new ArrayList<String>();
			List<Security> securityList = new ArrayList<Security>();
			Strategy plan = sm.get(planID);
			List<VariableFor401k> candidateList = plan.getVariablesFor401k();
			for(VariableFor401k vf:candidateList){
				Security se = securityManager.getBySymbol(vf.getSymbol());
				if(se != null && se.getAssetClass()!=null){
					securityList.add(se);
					String assetClassName = se.getAssetClass().getName();
					if(!assetClassName.trim().equals("ROOT") && !planAssetClassNameList.contains(assetClassName.trim().toLowerCase())){
						AssetClass assetClass = se.getAssetClass();
						planAssetClassList.add(assetClass);
						planAssetClassNameList.add(assetClassName.trim().toLowerCase());
					}
					
				}
			}
			
			if(planAssetClassList != null && planAssetClassList.size()!=0){
				List<AssetClass> childAssetClassList = planAssetClassList;
				for(;;){
					List<AssetClass> parentList = new ArrayList<AssetClass>();
					parentList = acm.getParentAssetClassList(childAssetClassList);
					if(parentList ==null || parentList.size()==0){
						break;
					}else{
						for(AssetClass a:parentList){
							if(!planAssetClassNameList.contains(a.getName().trim().toLowerCase())){
								planAssetClassList.add(a);
								planAssetClassNameList.add(a.getName().trim().toLowerCase());
							}
							
						}
						childAssetClassList = parentList;
					}
				}
			}
		}
		
		if(riskyAssetClassList == null || riskyAssetClassList.size()==0){
			if(riskyAssetClassList==null)
				riskyAssetClassList = new ArrayList<AssetClass>();
			for(AssetClass ac : planAssetClassList){
				String result = sm.getRiskyOrStableAsset(ac.getName().trim());
				if(result.equals("Risky Asset")){
					riskyAssetClassList.add(ac);
				}
			}
		}
		
		if(stableAssetClassList == null || stableAssetClassList.size()==0){
			if(stableAssetClassList==null)
				stableAssetClassList = new ArrayList<AssetClass>();
			for(AssetClass ac : planAssetClassList){
				String result = sm.getRiskyOrStableAsset(ac.getName().trim());
				if(result.equals("Stable Asset")){
					stableAssetClassList.add(ac);
				}
			}
		}
		
		if(term != null && !term.equals("riskyspace") && !term.equals("stablespace")){
			if(assetType == 0){				
					matchAssetClassList = new ArrayList<AssetClass>();
					for(AssetClass rac : riskyAssetClassList){
						String assetClassName = rac.getName().trim().toLowerCase();
						if(assetClassName.contains(term.trim().toLowerCase())){
							matchAssetClassList.add(rac);
						}
					}				
			}else if(assetType == 1){					
					matchAssetClassList = new ArrayList<AssetClass>();
					for(AssetClass sac : stableAssetClassList){
						String assetClassName = sac.getName().trim().toLowerCase();
						if(assetClassName.contains(term.trim().toLowerCase())){
							matchAssetClassList.add(sac);
						}
					}				
			}
		}else{
			if(term.equals("riskyspace") && assetType==0){
				matchAssetClassList = riskyAssetClassList;
				Menu menu = acm.getMenuByAssetList(matchAssetClassList, 0);
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[\r\n");
				this.buildJsonString(menu);
				if(count < matchAssetClassList.size()){
					String curJson = sb.toString();
					for(AssetClass ac : matchAssetClassList){
						String name = ac.getName();
						if(!curJson.contains(name)){
							if(count != 0){
								sb.append(",");							
							}
							sb.append("{\"name\":\"");
							sb.append(name);
							sb.append("\",\"data\":true}\r\n");
							count++;
						}
					}
				}
				if(count !=0){
					sb.append(",");
				}
				sb.append("{\"name\":\"CASH\",\"data\":true}\r\n]");
				sb2.append(sb.toString());
				message= sb2.toString();
			}else if(term.equals("stablespace") && assetType==1){
				matchAssetClassList = stableAssetClassList;
				Menu menu = acm.getMenuByAssetList(matchAssetClassList, 0);
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[\r\n");
				this.buildJsonString(menu);
				if(count !=0){
					sb.append(",");
				}
				sb.append("{\"name\":\"CASH\",\"data\":true}\r\n]");
				sb2.append(sb.toString());
			    message= sb2.toString();
			}			
		    return Action.MESSAGE;
		}
		
		
		if(matchAssetClassList == null || matchAssetClassList.size()==0){
			message= "[\r\n{\"name\":\"CASH\",\"data\":true}\r\n]";
			return Action.MESSAGE;
		}
		
		
		matchAssetClassList = Sort.assetClassSort(matchAssetClassList);
		StringBuffer strb = new StringBuffer();
		strb.append("[\r\n");
		for(int i=0;i<matchAssetClassList.size();i++){
			if(i!=0)strb.append(",");
			AssetClass a = matchAssetClassList.get(i);
			strb.append("{\"name\":\"");
			strb.append(a.getName());
			strb.append("\",\"data\":true}\r\n");
		}
		
		strb.append(",{\"name\":\"CASH\",\"data\":true}\r\n]");
	    message= strb.toString();
		
				
		return Action.MESSAGE;
	}
	
	
	private StringBuffer sb = new StringBuffer();
	private int count = 0;
	private int j=0;
	public void buildJsonString(Menu menu){
		if(!menu.getText().equalsIgnoreCase("ROOT")){
			if(count != 0){
				sb.append(",");
			}
			sb.append("{\"name\":\"");
			for(int i=0;i<j-1;i++){
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
			}				
			sb.append(menu.getText());
			sb.append("\",\"data\":true}\r\n");
			count++;
		}
		if(menu.getChildren()==null || menu.getChildren().size()==0){
			return;
		}else{
			j++;
			List<Menu> lists = menu.getChildren();
			for(int k=0;k<lists.size();k++){
				buildJsonString(lists.get(k));
			}
			j--;
		}
		
	}
	
	
	private String assetClassName;
	/**
	 * get AsstClass's Funds
	 * @return
	 */
	public String getFundtable(){
		message ="";
		StringBuffer sb = new StringBuffer();
		StrategyManager sm = ContextHolder.getStrategyManager();
		Strategy plan = null;
		if(planName != null&&planName!=""){
			 plan = sm.get(planName);
			 planID = plan.getID();
		}
		if(plan == null){
			message = "The plan name is not correct.";
			return Action.MESSAGE;
		}
		List<String> lists = new ArrayList<String>();
		if(assetClassFundMap != null && assetClassFundMap.size()!=0){
			lists = assetClassFundMap.get(assetClassName);		
		if(lists.size()!=0){ 
			sb.append("<table cellspacing='5px'><thead><tr><th>Ticker</th><th>Description</th></tr></thead><tbody>");
			for(int i=0;i<lists.size();i++){
                 String symbol = lists.get(i);
                 VariableFor401k var = sm.getVariable401K(planID, symbol);
                 sb.append("<tr>");
                 sb.append("<td>");
                 sb.append(symbol);
                 sb.append("</td>");
                 sb.append("<td>");
                 if(var ==null){
                	 sb.append(" ");
                 }else{
                	 if(var.getDescription()==null || var.getDescription().equals("")){
                    	 sb.append(" ");
                     }else{
                    	 sb.append(var.getDescription());
                     } 
                 }
                                
                 sb.append("</td>");
                 sb.append("</tr>");				 	
			}
			sb.append("</tbody></table>");
			message = sb.toString();
		}
	}
		return Action.MESSAGE;
	}
	
	
	public String getPlanInfo(){
		message="";
		StrategyManager sm = ContextHolder.getStrategyManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		AssetClassManager acm = ContextHolder.getAssetClassManager();
		Strategy plan = sm.get(planName);
		if(plan==null){
			message = "The plan name is not correct.";
			return Action.MESSAGE;
		}else{
			fundtableMap = sm.getSixMajorAssetClassForPlan(plan.getID());
			StringBuffer sb = new StringBuffer();
			sb.append("<table cellspacing=10px><thead><tr><th style='align:left'>Asset Class</th><th style='align:left'>Description(Ticker)</th></tr></thead><tbody>");
			Set<String> set = fundtableMap.keySet();
			for(String str: set){
                sb.append("<tr class='assetTitle'><td>");
                sb.append(str);
                sb.append("</td><td></td></tr>");
                List<VariableFor401k> lists = fundtableMap.get(str);
                for(int i=0;i<lists.size();i++){
                	VariableFor401k var = lists.get(i);
                	if(i%2==0){
                		sb.append("<tr class='odd'>");
                	}else{
                		sb.append("<tr class='even'>");
                	}
                	sb.append("<td>");
                	sb.append(var.getAssetClassName());
                	sb.append("</td><td>");
                	sb.append(var.getDescription());
                	sb.append("(");
                	sb.append(var.getSymbol());
                	sb.append(")");
                	sb.append("</td></tr>");
                }
			}
			sb.append("</tbody></table>");
			message = sb.toString();
			
			planAssetClassList = new ArrayList<AssetClass>();
			List<String> planAssetClassNameList = new ArrayList<String>();
			symbolList = new ArrayList<String>();
			List<Security> securityList = new ArrayList<Security>();
			List<VariableFor401k> candidateList = plan.getVariablesFor401k();
			for(VariableFor401k vf:candidateList){
				Security se = securityManager.getBySymbol(vf.getSymbol());
				symbolList.add(vf.getSymbol());
				if(se != null && se.getAssetClass()!=null){
					securityList.add(se);
					String assetClassName = se.getAssetClass().getName();
					if(!assetClassName.trim().equals("ROOT") && !planAssetClassNameList.contains(assetClassName.trim().toLowerCase())){
						AssetClass assetClass = se.getAssetClass();
						planAssetClassList.add(assetClass);
						planAssetClassNameList.add(assetClassName.trim().toLowerCase());
					}
					
				}
			}
			
			if(planAssetClassList != null && planAssetClassList.size()!=0){
				List<AssetClass> childAssetClassList = planAssetClassList;
				for(;;){
					List<AssetClass> parentList = new ArrayList<AssetClass>();
					parentList = acm.getParentAssetClassList(childAssetClassList);
					if(parentList ==null || parentList.size()==0){
						break;
					}else{
						for(AssetClass a:parentList){
							if(!planAssetClassNameList.contains(a.getName().trim().toLowerCase())){
								planAssetClassList.add(a);
								planAssetClassNameList.add(a.getName().trim().toLowerCase());
							}
							
						}
						childAssetClassList = parentList;
					}
				}
			}
			
			riskyAssetClassList = new ArrayList<AssetClass>();
			for(AssetClass ac : planAssetClassList){
				String result = sm.getRiskyOrStableAsset(ac.getName().trim());
				if(result.equals("Risky Asset")){
					riskyAssetClassList.add(ac);
				}
			}
			
			stableAssetClassList = new ArrayList<AssetClass>();
			for(AssetClass ac : planAssetClassList){
				String result = sm.getRiskyOrStableAsset(ac.getName().trim());
				if(result.equals("Stable Asset")){
					stableAssetClassList.add(ac);
				}
			}
			
		}
		
		return Action.MESSAGE;
	}
	
	/**
	 * get all of the assetClass's fund number.
	 * @return
	 */
	public String getAssetFundNum(){
		message="";		
		AssetClassManager acm = ContextHolder.getAssetClassManager();
		String[] assetClassItem = assetClassName.split(",");
		if(symbolList!= null && symbolList.size() != 0){
		 assetClassFundMap = acm.getAvailableAssetClassSet(assetClassItem, symbolList, false, false);
			if(assetClassFundMap!=null && assetClassFundMap.size()!=0){
				for(int i=0;i<assetClassItem.length;i++){
					int len=0;
					if(assetClassFundMap.get(assetClassItem[i]) == null)
						len =0;
					else{
						len = assetClassFundMap.get(assetClassItem[i]).size();
					}					
					if(i!=0)
						message+=",";
					message+=String.valueOf(len);
				}
			}
		}
		
		return Action.MESSAGE;
	}
	
	
	/**
	 * get asset and it's funds,save it for build a portfolio 
	 * @return
	 */
	public String getAllAssetFund(){
		message="";		
		String[] assetClassItem = assetClassName.split(",");
		if(assetClassFundMap != null && assetClassFundMap.size()!=0){
			for(int i=0;i<assetClassItem.length;i++){
				String ss = assetClassItem[i];
				List<String> lists = assetClassFundMap.get(ss);
				if(lists!=null&&lists.size()!=0){
					if(i!=0){
						message+=",";
					}
					StringBuffer sb = new StringBuffer();
					sb.append(ss);
					for(String s:lists){
						sb.append("#");
						sb.append(s);
					}
					message+=sb.toString();
				}else{
					if(i!=0){
						message+=",";
					}
					StringBuffer sb = new StringBuffer();
					String fund = "$"+ss;
					sb.append(fund);
					sb.append("#");
					sb.append(ss);
					message+=sb.toString();
				}
			}
		}else{
			if(assetClassName!=null&&assetClassItem.length > 0){
				for(int i=0;i<assetClassItem.length;i++){
					String ss = assetClassItem[i];
					if(i!=0){
						message+=",";
					}
					StringBuffer sb = new StringBuffer();
					String fund = "$"+ss;
					sb.append(fund);
					sb.append("#");
					sb.append(ss);
					message+=sb.toString();
				}
			}
		}
		return Action.MESSAGE;
	}
	
	private String templateName;
	public String getTemplateDesc(){
		StrategyManager sm = ContextHolder.getStrategyManager();
		message = "";
		if(templateName != null && !templateName.equals("")){
			AllocationTemplate at = sm.getAllocationTemplateByName(templateName).get(0);
			message = at.getDescription();
		}
		return Action.MESSAGE;
	}
	
	
	
	public String searchFund(){
		message = "";
		if(symbolList!=null && symbolList.size()!=0){
			StringBuffer strb = new  StringBuffer();
			strb.append("[\r\n");
			int count = 0;
			for(String ss: symbolList){
				String lowString = ss.toLowerCase();
				term = term.toLowerCase();
				if(lowString.contains(term)){
					if(count !=0){
						strb.append(",");
					}
					strb.append("{\"name\":\"");
					strb.append(ss);
					strb.append("\",\"data\":true}\r\n");
					count++;
				}				
			}
			if(strb.toString().indexOf("CASH")==-1){
				if(count !=0){
					strb.append(",");
				}
				strb.append("{\"name\":\"");
				strb.append("CASH");
				strb.append("\",\"data\":true}\r\n");
			}
			strb.append("]");
			message = strb.toString();
		}
		return Action.MESSAGE;
	}
    
	
	/**
	 * if the template change, get the template info
	 * @return
	 */
	
	public String getTemplateInfo(){
		StrategyManager sm = ContextHolder.getStrategyManager();
		AssetClassManager acm = ContextHolder.getAssetClassManager();
		message = "";
		if(templateName != null && !templateName.equals("")){
			AllocationTemplate at = sm.getAllocationTemplateByName(templateName).get(0);
			if(at!=null){
				List<String>riskyAssetClassList = new ArrayList<String>();
				List<String>riskyPercentageList = new ArrayList<String>();
				List<String>riskyFundNumList = new ArrayList<String>() ;
				List<String>stableAssetClassList = new ArrayList<String>();
				List<String>stablePercentageList = new ArrayList<String>();
				List<String>stableFundNumList = new ArrayList<String>();
				String assetClass = at.getAssetClassName();
				String percentage = at.getAssetClassWeight();
				String []assetItem = assetClass.split(",");
				String []percentItem = percentage.split(",");
				String []fundItem = fundName.split(",");
				for(String str:fundItem){
					symbolList.add(str);
				}
				
				assetClassFundMap = new HashMap<String,List<String>>();
				assetClassFundMap = acm.getAvailableAssetClassSet(assetItem, symbolList, false, false);
					
				for(int i=0;i<assetItem.length;i++){
					String str = assetItem[i];
					String result = sm.getRiskyOrStableAsset(str);
					if(result.equals("Risky Asset")){
						riskyAssetClassList.add(str);
						riskyPercentageList.add(percentItem[i]);
					}else if(result.equals("Stable Asset")){
						stableAssetClassList.add(str);
						stablePercentageList.add(percentItem[i]);
					}
				}
				for(String s: riskyAssetClassList){
					List<String> riskyFundList = assetClassFundMap.get(s);
					if(riskyFundList != null){
						riskyFundNumList.add(String.valueOf(riskyFundList.size()));
					}else{
						riskyFundNumList.add("0");
					}
				}
				
				for(String ss: stableAssetClassList){
					List<String> stableFundList = assetClassFundMap.get(ss);
					if(stableFundList != null){
						stableFundNumList.add(String.valueOf(stableFundList.size()));
					}else{
						stableFundNumList.add("0");
					}
				}
				String riskyAssetString = "";
				String riskyFundNumString = "";
				String riskyPercentString = "";
				String stableAssetString ="";
				String stableFundNumString="";
				String stablePercentString = "";
				
				for(int i=0;i<riskyAssetClassList.size();i++){
					if(i!=0){
						riskyAssetString+=",";
						riskyFundNumString+=",";
						riskyPercentString+=",";
					}						
					riskyAssetString+= riskyAssetClassList.get(i);
					riskyFundNumString +=riskyFundNumList.get(i);
					riskyPercentString +=riskyPercentageList.get(i);
				}
				
				for(int j=0;j<stableAssetClassList.size();j++){
					if(j!=0){
						stableAssetString+=",";
						stableFundNumString+=",";
						stablePercentString+=",";
					}						
					stableAssetString+= stableAssetClassList.get(j);
					stableFundNumString +=stableFundNumList.get(j);
					stablePercentString +=stablePercentageList.get(j);
				}
				
				message+=riskyAssetString+"#"+riskyFundNumString+"#"+riskyPercentString;
				message+="@";
				message+=stableAssetString+"#"+stableFundNumString+"#"+stablePercentString;
			}
			
		}
		return Action.MESSAGE;
	}
	
	private String fundName;
	/**
	 * get fund type, stable asset or risk asset
	 * @return
	 */
	public String getFundType(){
		message = "";
		SecurityManager sm = ContextHolder.getSecurityManager();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		boolean isfund = false;
		if(fundName != null){
			for(String ss:symbolList){
				if(ss.equalsIgnoreCase(fundName)){
					isfund = true;
					break;
				}
			}
			if(!isfund){
				message = "not fund";
				return Action.MESSAGE;
			}
			if(fundName.equalsIgnoreCase("cash")){
				message = "Stable Asset";
			}else{
				Security se = sm.getBySymbol(fundName);
				if(se != null && se.getAssetClass()!=null){
					String assetName = se.getAssetClass().getName();
					String result = strategyManager.getRiskyOrStableAsset(assetName);
					message = result;
				}
			}
			
			if(symbolList != null && symbolList.size()!=0){
				for(int i=0;i<symbolList.size();i++){
					String str = symbolList.get(i);
					if(str.equalsIgnoreCase(fundName)){
						symbolList.remove(i);
						break;
					}
				}
			}
		}
		return Action.MESSAGE;
	}
	
	/**
	 * delete fund and recovery symbol list
	 * @return
	 */
	public String deleteFund(){
		message="";
		if(fundName!=null){			
				symbolList.add(fundName);			
		}
		
		return Action.MESSAGE;
	}
	
	
	/**
	 * get a fund's description and get it's table.
	 * @return
	 */
	public String getFundInfo(){
		message = "";
		StringBuffer sb = new StringBuffer();
		StrategyManager sm = ContextHolder.getStrategyManager();
		Strategy plan = null;
		if(planName != null&&planName!=""){
			 plan = sm.get(planName);
			 planID = plan.getID();
		}
		if(plan == null){
			message = "The plan name is not correct.";
			return Action.MESSAGE;
		}
		if(fundName!=null){
			sb.append("<table cellspacing='5px'><thead><tr><th>Ticker</th><th>Description</th></tr></thead><tbody>");
			VariableFor401k var = sm.getVariable401K(planID, fundName);
			if(var !=null){
				sb.append("<tr><td>");
				sb.append(var.getSymbol());
				sb.append("</td>");
				if(var.getDescription()!=null){
					sb.append("<td>");
					sb.append(var.getDescription());
					sb.append("</td></tr>");
				}else{
					sb.append("<td>");
					sb.append(" ");
					sb.append("</td></tr>");
				}
			}
			sb.append("</tbody></table>");
			message = sb.toString();
		}
		return Action.MESSAGE;
	}
	
	
	public String getSingleFundInfo(){
		message="";
		if(fundName!=null){
			String[] fundItem = fundName.split(",");
			for(int i=0;i<fundItem.length;i++){
				if(i!=0) message+=",";
				String name = fundItem[i];
				int num = symbolList.size();
				int j=0;
				for(;j<num;j++){
					String symbol = symbolList.get(j);
					if(symbol.equalsIgnoreCase(name)){
						symbolList.remove(j);
						break;
					}
				}
				if(j==num){
					message+="0";
				}else{
					message+="1";
				}								
			}
		}
		return Action.MESSAGE;
	}
	
	private String assetClassWeight;
	private String description;
	public String saveTemplate(){
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		templateList = strategyManager.getAllallocationTemplate();
		boolean exist = false;
		for(AllocationTemplate at:templateList){
			String name = at.getTemplateName();
			if(name.equalsIgnoreCase(templateName)){
				exist = true;
				break;
			}			
		}		
		if(exist){
			message="The template Name is exist";
			return Action.MESSAGE;
			
		}else{
			AllocationTemplate newAt = new AllocationTemplate();
			newAt.setTemplateName(templateName);
			newAt.setAssetClassName(assetClassName);
			newAt.setAssetClassWeight(assetClassWeight);
			newAt.setDescription(description);
			strategyManager.addAllocationTemplate(newAt);
			message="Save the template success";
		}
		return Action.MESSAGE;
	}
	
	/**
	 * 模拟portfolio过程中的页面
	 */
	public String building() {
		UserManager um = ContextHolder.getUserManager();
		PortfolioManager pm = ContextHolder.getPortfolioManager();
		EmailAlertAction eaa = new EmailAlertAction();
		eaa.setPortfolioID(portfolioID);
		eaa.setPortfolioManager(pm);
		eaa.setUserManager(um);
		eaa.email();
		return entry();
	}

	private String term;
	private int size = 10;
	private String message;

	/**
	 * Plan name的下拉提示
	 */
	public String ajaxplanname() {
		StrategyManager sm = ContextHolder.getStrategyManager();
		String sql = "select distinct(strategyname),strategyid from cache_group_strategy gpi where gpi.Type=" + Configuration.STRATEGY_401K_TYPE;
		if (term != null && !term.equals("")) {
			sql += " and gpi.StrategyName like '%" + term + "%'";
		}
		long userID = ContextHolder.getUserManager().getLoginUser().getID();
		if (userID == Configuration.SUPER_USER_ID) {

		} else {
			GroupManager gm = ContextHolder.getGroupManager();
			Object[] gids = gm.getGroupIDs(userID);
			if (gids.length == 1) {
				sql += " and (gpi.GroupID=" + gids[0] + " or gpi.UserID=" + userID + ")";
			} else {
				sql += " and (";
				for (Object gid : gids) {
					sql += "gpi.GroupID=" + gid;
					sql += " or ";
				}
				sql += "gpi.UserID=" + userID + ")";
			}
		}

		if (size > 0) {
			sql += " limit 0," + size;
		}
		System.out.println(sql);

		List<Object[]> strategies = null;
		try {
			strategies = sm.findBySQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[\r\n");
		boolean find = false;
		if (strategies != null && strategies.size() != 0) {
			find = true;
			boolean f = true;
			for (Object[] arr : strategies) {
				if (!f) {
					sb.append(",");
				}
				f = false;
				sb.append("{\"name\":\"");
				sb.append(((String) arr[0]).replace("\"", "\\\""));
				sb.append("\",\"id\":\"");
				sb.append(((BigInteger) arr[1]).longValue());
				sb.append("\",\"data\":true}\r\n");

			}
		}
		if (find)
			sb.append(",");
		sb.append("{\"id\":\"" + term + "\",\"name\":\"Show all result for\",\"data\":false}\r\n");
		sb.append("]");
		message = sb.toString();
		// System.out.println(message);
		return Action.MESSAGE;
	}
	
	/**
	 * Plan name的下拉提示
	 */
	public String ajaxportfolioname() {
		PortfolioManager pm = ContextHolder.getPortfolioManager();
		String sql = "select distinct(portfolioname),portfolioid from cache_group_portfolio gpi where 1=1 ";
		if (term != null && !term.equals("")) {
			sql += " and gpi.PortfolioName like '%" + term + "%'";
		}
		long userID = ContextHolder.getUserManager().getLoginUser().getID();
		if (userID == Configuration.SUPER_USER_ID) {

		} else {
			GroupManager gm = ContextHolder.getGroupManager();
			Object[] gids = gm.getGroupIDs(userID);
			if (gids.length == 1) {
				sql += " and (gpi.GroupID=" + gids[0] + " or gpi.UserID=" + userID + ")";
			} else {
				sql += " and (";
				for (Object gid : gids) {
					sql += "gpi.GroupID=" + gid;
					sql += " or ";
				}
				sql += "gpi.UserID=" + userID + ")";
			}
		}

		if (size > 0) {
			sql += " limit 0," + size;
		}
		System.out.println(sql);

		List<Object[]> strategies = null;
		try {
			strategies = pm.findBySQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[\r\n");
		boolean find = false;
		if (strategies != null && strategies.size() != 0) {
			find = true;
			boolean f = true;
			for (Object[] arr : strategies) {
				if (!f) {
					sb.append(",");
				}
				f = false;
				sb.append("\"");
				sb.append(arr[0]);
				sb.append("\"");

			}
		}
		if (find)
			sb.append(",");
		sb.append("\"Show more result for ");
		sb.append(term.replace("\"", "\""));
		sb.append("\"]");
		message = sb.toString();
		// System.out.println(message);
		return Action.MESSAGE;
	}

	/**
	 * Plan name的下拉提示
	 */
	public String ajaxplansuggest() {
		return ajaxstrategysuggest(Configuration.STRATEGY_TYPE_401K);
	}

	public String getCandidateFund() {
		return candidateFund;
	}

	public void setCandidateFund(String candidateFund) {
		this.candidateFund = candidateFund;
	}

	public String getEMAValue() {
		return EMAValue;
	}

	public void setEMAValue(String value) {
		EMAValue = value;
	}

	public String getSMAValue() {
		return SMAValue;
	}

	public void setSMAValue(String value) {
		SMAValue = value;
	}

	public String getEMAUnit() {
		return EMAUnit;
	}

	public void setEMAUnit(String unit) {
		EMAUnit = unit;
	}

	public String getSMAUnit() {
		return SMAUnit;
	}

	public void setSMAUnit(String unit) {
		SMAUnit = unit;
	}

	public List<String> getFundList() {
		return fundList;
	}

	public void setFundList(List<String> fundList) {
		this.fundList = fundList;
	}

	public List<Integer> getEMAValueList() {
		return EMAValueList;
	}

	public void setEMAValueList(List<Integer> valueList) {
		EMAValueList = valueList;
	}

	public List<Integer> getSMAValueList() {
		return SMAValueList;
	}

	public void setSMAValueList(List<Integer> valueList) {
		SMAValueList = valueList;
	}

	/**
	 * Strategy name的下拉提示
	 */
	public String ajaxstrategysuggest() {
		return ajaxstrategysuggest(Configuration.STRATEGY_TYPE_NORMAL);
	}
	
	public String modify(){
		entry();
		ProfileManager profileManager=(ProfileManager)ContextHolder.getInstance().getApplicationContext().getBean("profileManager");
		portfolioName = portfolio.getName();
		User user=ContextHolder.getUserManager().getLoginUser();
		portfolio.setUserID(user.getID());
		Long userID = user.getID();
		Profile profile = profileManager.get(0l, userID, 0l);
		riskProfileNumber=Double.parseDouble(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("RiskProfile").replaceAll("\"", ""));
		return Action.SUCCESS;
		
	}

	/**
	 * Security name的下拉提示
	 */
	public String ajaxsecuritysuggest() {
		if (term != null && !term.equals("")) {
			if (term.indexOf("(") != -1) {
				term = term.substring(0,term.indexOf("("));
			}
		}
		term = term.trim();
		
		StrategyManager sm = ContextHolder.getStrategyManager();
		String sql = "select id,symbol,name from ltisystem_security s ";
		if (term != null && !term.equals("")) {
			sql += "where s.Symbol like '%" + term + "%' or s.name like '%" + term + "%'";
		}

		if (size > 0) {
			sql += " limit 0," + size;
		}

		List<Object[]> securities = null;
		try {
			securities = sm.findBySQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[\r\n");
		boolean find = false;
		if (securities != null && securities.size() != 0) {
			find = true;
			boolean f = true;
			for (Object[] arr : securities) {
				if (!f) {
					sb.append(",");
				}
				f = false;
				sb.append("\"");
				sb.append(arr[1]);
				sb.append("(");
				sb.append(((String) arr[2]).replace("\"", "\\\""));
				sb.append(")");
				sb.append("\"");

			}
		}
		if (find)
			sb.append(",");
		sb.append("\"Show more result for ");
		sb.append(term.replace("\"", "\""));
		sb.append("\"]");
		message = sb.toString();
		// System.out.println(message);
		return Action.MESSAGE;
	}

	public String ajaxstrategysuggest(long type) {
		StrategyManager sm = ContextHolder.getStrategyManager();
		String sql = "select distinct(strategyname),strategyid from cache_group_strategy gpi where gpi.Type & " + type + " >0 ";
		if (term != null && !term.equals("")) {
			sql += " and gpi.StrategyName like '" + term + "%'";
		}
		long userID = ContextHolder.getUserManager().getLoginUser().getID();
		if (userID == Configuration.SUPER_USER_ID) {

		} else {
			GroupManager gm = ContextHolder.getGroupManager();
			Object[] gids = gm.getGroupIDs(userID);
			if (gids.length == 1) {
				sql += " and (gpi.GroupID=" + gids[0] + " or gpi.UserID=" + userID + ")";
			} else {
				sql += " and (";
				for (Object gid : gids) {
					sql += "gpi.GroupID=" + gid;
					sql += " or ";
				}
				sql += "gpi.UserID=" + userID + ")";
			}
		}
		sql+=" order by gpi.StrategyName asc";

		if (size > 0) {
			sql += " limit 0," + size;
		}

		List<Object[]> strategies = null;
		try {
			strategies = sm.findBySQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		StringBuffer sb = new StringBuffer();
		sb.append("[\r\n");
		boolean find = false;
		if (strategies != null && strategies.size() != 0) {
			find = true;
			boolean f = true;
			for (Object[] arr : strategies) {
				if (!f) {
					sb.append(",");
				}
				f = false;
				sb.append("\"");
				sb.append(arr[0]);
				sb.append("\"");

			}
		}
		if (find)
			sb.append(",");
		sb.append("\"Show more result for ");
		sb.append(term.replace("\"", "\""));
		sb.append("\"]");
		message = sb.toString();
		// System.out.println(message);
		return Action.MESSAGE;
	}

	/*
	 * { "map": [
	 * {"name":"tab","title":"int tab","type":"text","css":"","default":"10"},
	 * {"name"
	 * :"arr","title":"String[] arr","type":"text","css":"","default":"\"a\",\"b\""
	 * } ]};
	 */
	/**
	 * 获取strategy 的parameter结构
	 */
	public String ajaxparametersuggest() {
		message = "{\"size\":\"0\"}";
		if (strategyName != null && strategyName.equals("STATIC")) {
			return Action.MESSAGE;
		}
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		Strategy strategy = strategyManager.get(strategyName);
		if (strategy != null) {
			StrategyCode sc = strategyManager.getLatestStrategyCode(strategy.getID());
			if (sc != null && sc.getCode() != null) {
				CodeInf ci = sc.getCode();
				if (ci != null) {
					String json = ci.getParameterStructure();
					if (json == null || json.equals("")) {
						json = "{\"size\":\"0\"}";
					}
					message = json;
				}
			}
		}
		//System.out.println(message);
		return Action.MESSAGE;
	}

	public String ajaxstrategyclass() {
		return Action.MESSAGE;
	}

	public static Map<Long, AssetClass> AssetClasses = null;

	/**
	 * 此函数当Asset Class的表修改后，无法反映改变
	 */
	public static synchronized void buildAssetClasses() {
		if (AssetClasses == null) {
			AssetClasses = new HashMap<Long, AssetClass>();
			AssetClassManager acm = ContextHolder.getAssetClassManager();
			List<AssetClass> acs = acm.getClasses();
			for (AssetClass ac : acs) {
				AssetClasses.put(ac.getID(), ac);
			}
			for (AssetClass ac : acs) {
				List<String> list = new ArrayList<String>(5);
				AssetClass tmp = ac;
				while (tmp.getParentID() != null && !tmp.getName().equals("ROOT")) {
					list.add(tmp.getName());
					tmp = AssetClasses.get(tmp.getParentID());
				}
				StringBuffer sb = new StringBuffer();
				for (int i = list.size() - 1; i >= 0; i--) {
					sb.append(list.get(i));
					if (i != 0)
						sb.append(" -> ");
				}
				ac.setFullName(sb.toString());
			}
		}
	}
	

	public String ajaxassetclass() {

		buildAssetClasses();

		if (term != null && !term.equals("")) {
			if (term.indexOf(".") != -1) {
				term = term.substring(term.indexOf(".")+1);
			}
		}
		term = term.trim();

		List<AssetClass> assetClasses = null;
		assetClasses = new ArrayList<AssetClass>();

		java.util.Iterator<Long> iter = AssetClasses.keySet().iterator();
		while (iter.hasNext()) {
			AssetClass ac = AssetClasses.get(iter.next());
			if (ac.getName().toLowerCase().indexOf(term.toLowerCase()) != -1 || ac.getFullName().toLowerCase().indexOf(term.toLowerCase()) != -1) {
				assetClasses.add(ac);
			}
			if (size > 0 && assetClasses.size() >= size)
				break;
		}

		if (assetClasses.size() == 0) {
			iter = AssetClasses.keySet().iterator();
			while (iter.hasNext()) {
				AssetClass ac = AssetClasses.get(iter.next());
				if (ac.getParentID() == 0l) {
					assetClasses.add(ac);
				}
			}
		}

		StringBuffer sb = new StringBuffer();
		sb.append("[\r\n");
		boolean find = false;
		if (assetClasses != null && assetClasses.size() != 0) {
			find = true;
			boolean f = true;
			for (AssetClass ac : assetClasses) {
				if (!f) {
					sb.append(",");
				}
				f = false;
				sb.append("\"");
				sb.append(ac.getID());
				sb.append(". ");
				sb.append(ac.getFullName());
				sb.append("\"");

			}
		}
		if (find)
			sb.append(",");
		sb.append("\"Show more result for ");
		sb.append(term.replace("\"", "\""));
		sb.append("\"]");
		message = sb.toString();

		return Action.MESSAGE;
	}

	
	private Date date;
	public String ajaxprice(){
		try{
			if(term.indexOf("(")!=-1){
				term= term.substring(0,term.indexOf("("));
			}
			Security s=ContextHolder.getSecurityManager().get(term.trim());
			message=""+ContextHolder.getSecurityManager().getPrice(s.getID(), date);
		}catch(Exception e){
			message="0.0";
		}
		return Action.MESSAGE;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getStrategyID() {
		return strategyID;
	}

	public void setStrategyID(Long strategyID) {
		this.strategyID = strategyID;
	}

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public Long getPlanID() {
		return planID;
	}

	public void setPlanID(Long planID) {
		this.planID = planID;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getStrategyName() {
		return strategyName;
	}

	public void setStrategyName(String strategyName) {
		this.strategyName = strategyName;
	}

	public Double getRiskProfileNumber() {
		return riskProfileNumber;
	}

	public void setRiskProfileNumber(Double riskProfileNumber) {
		this.riskProfileNumber = riskProfileNumber;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getstarted_1(){
		return Action.SUCCESS;
	}
	private List<ModelPortfoliosBean> modelPortfoliosBeans;
	public String getstarted_2(){
		StrategyManager strategyManager=ContextHolder.getStrategyManager();
		PortfolioManager portfolioManager=ContextHolder.getPortfolioManager();
		Strategy plan=strategyManager.get(planName);
		if(plan==null){
			planID=null;
		}else{
			planID=plan.getID();
			modelPortfoliosBeans = new ArrayList<ModelPortfoliosBean>(2);
			List<Portfolio> portfolios = strategyManager.getModelPortfolios(planID);
			long[] strategyids=new long[]{Configuration.STRATEGY_SAA_ID,Configuration.STRATEGY_TAA_ID};
			String[] strategynames=new String[]{"Strategic Asset Allocation","Tactical Asset Allocation"};
			for (int i = 0; i < 2; i++) {
				ModelPortfoliosBean scb = new ModelPortfoliosBean();
				scb.setStrategyID(strategyids[i]);
				scb.setStrategyName(strategynames[i]);
				modelPortfoliosBeans.add(scb);
				if (portfolios == null || portfolios.size() == 0)
					continue;
				List<CachePortfolioItem> items = new ArrayList<CachePortfolioItem>();
				for (int j = 0; j < portfolios.size(); j++) {
					Portfolio p = portfolios.get(j);
					if (p.getStrategies()!=null&&p.getStrategies().getAssetAllocationStrategy().getID() != null && p.getStrategies().getAssetAllocationStrategy().getID().longValue() == strategyids[i]) {
						CachePortfolioItem cpi = null;
						PortfolioPermissionChecker pc=new PortfolioPermissionChecker(p, ServletActionContext.getRequest());
						if(!pc.hasViewRole()){
							continue;
						}
						List<CachePortfolioItem> pitems = null;
						pitems=strategyManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + p.getID() + " and cp.GroupID=0 and cp.RoleID="	+ Configuration.ROLE_PORTFOLIO_REALTIME_ID);
						if (pitems != null && pitems.size() > 0) {
							cpi = pitems.get(0);
						} else {
							cpi = new CachePortfolioItem();
							cpi.setPortfolioID(p.getID());
							cpi.setPortfolioName(p.getName());
						}
						items.add(cpi);

					}
				}
				if (items.size() > 0)
					scb.setModelPortfolios(items);

			}//end for
		}//end if plan is not null
		return Action.SUCCESS;
	}
	
	public List<ModelPortfoliosBean> getModelPortfoliosBeans() {
		return modelPortfoliosBeans;
	}

	public void setModelPortfoliosBeans(List<ModelPortfoliosBean> modelPortfoliosBeans) {
		this.modelPortfoliosBeans = modelPortfoliosBeans;
	}

	public String getstarted_3(){
		return Action.SUCCESS;
	}
	
	public String getstarted_4(){
		return Action.SUCCESS;
	}
	private int step=1;
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String nav(){
		return Action.SUCCESS;
	}

	public String getUseEMAFlag() {
		return useEMAFlag;
	}

	public void setUseEMAFlag(String useEMAFlag) {
		this.useEMAFlag = useEMAFlag;
	}

	public String getUseSMAFlag() {
		return useSMAFlag;
	}

	public void setUseSMAFlag(String useSMAFlag) {
		this.useSMAFlag = useSMAFlag;
	}

	public String getUseWMAFlag() {
		return useWMAFlag;
	}

	public void setUseWMAFlag(String useWMAFlag) {
		this.useWMAFlag = useWMAFlag;
	}

	public List<Integer> getWMAValueList() {
		return WMAValueList;
	}

	public void setWMAValueList(List<Integer> valueList) {
		WMAValueList = valueList;
	}

	public String getWMAValue() {
		return WMAValue;
	}

	public void setWMAValue(String value) {
		WMAValue = value;
	}

	public String getWMAUnit() {
		return WMAUnit;
	}

	public void setWMAUnit(String unit) {
		WMAUnit = unit;
	}

	public List<String> getDefaultRiskyAssetClassList() {
		return defaultRiskyAssetClassList;
	}

	public void setDefaultRiskyAssetClassList(
			List<String> defaultRiskyAssetClassList) {
		this.defaultRiskyAssetClassList = defaultRiskyAssetClassList;
	}

	public List<String> getDefaultRiskyPercentageList() {
		return defaultRiskyPercentageList;
	}

	public void setDefaultRiskyPercentageList(
			List<String> defaultRiskyPercentageList) {
		this.defaultRiskyPercentageList = defaultRiskyPercentageList;
	}

	public List<String> getDefaultRiskyFundNumList() {
		return defaultRiskyFundNumList;
	}

	public void setDefaultRiskyFundNumList(List<String> defaultRiskyFundNumList) {
		this.defaultRiskyFundNumList = defaultRiskyFundNumList;
	}

	public List<String> getDefaultStableAssetClassList() {
		return defaultStableAssetClassList;
	}

	public void setDefaultStableAssetClassList(
			List<String> defaultStableAssetClassList) {
		this.defaultStableAssetClassList = defaultStableAssetClassList;
	}

	public List<String> getDefaultStablePercentageList() {
		return defaultStablePercentageList;
	}

	public void setDefaultStablePercentageList(
			List<String> defaultStablePercentageList) {
		this.defaultStablePercentageList = defaultStablePercentageList;
	}

	public List<String> getDefaultStableFundNumList() {
		return defaultStableFundNumList;
	}

	public void setDefaultStableFundNumList(List<String> defaultStableFundNumList) {
		this.defaultStableFundNumList = defaultStableFundNumList;
	}

	public String getDefaultTotalRiskyPercentage() {
		return defaultTotalRiskyPercentage;
	}

	public void setDefaultTotalRiskyPercentage(String defaultTotalRiskyPercentage) {
		this.defaultTotalRiskyPercentage = defaultTotalRiskyPercentage;
	}

	public String getDefaultTotalStablePercentage() {
		return defaultTotalStablePercentage;
	}

	public void setDefaultTotalStablePercentage(String defaultTotalStablePercentage) {
		this.defaultTotalStablePercentage = defaultTotalStablePercentage;
	}

	public String getAssetClassName() {
		return assetClassName;
	}

	public void setAssetClassName(String assetClassName) {
		this.assetClassName = assetClassName;
	}

	public String getDefaultDescription() {
		return defaultDescription;
	}

	public void setDefaultDescription(String defaultDescription) {
		this.defaultDescription = defaultDescription;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Map<String, List<VariableFor401k>> getFundtableMap() {
		return fundtableMap;
	}

	public void setFundtableMap(Map<String, List<VariableFor401k>> fundtableMap) {
		this.fundtableMap = fundtableMap;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getAssetClassWeight() {
		return assetClassWeight;
	}

	public void setAssetClassWeight(String assetClassWeight) {
		this.assetClassWeight = assetClassWeight;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	
}
