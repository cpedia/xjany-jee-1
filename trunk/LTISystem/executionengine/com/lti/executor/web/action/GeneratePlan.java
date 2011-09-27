/**
 * 
 */
package com.lti.executor.web.action;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.lti.executor.web.BasePage;
import com.lti.permission.PublicMaker;
import com.lti.permission.SubscrPlanChecker;
import com.lti.service.AssetClassManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.executor.StrategyInf;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

/**
 * @author ccd
 *
 */
public class GeneratePlan extends BasePage {
	
	
	public final String FUND_NAME = "Fund Name";
	public SubscrPlanChecker spc = new SubscrPlanChecker();
	public StrategyManager strategyManager = ContextHolder.getStrategyManager();
	public SecurityManager securityManager = ContextHolder.getSecurityManager();
	public PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
	public AssetClassManager assetClassManager = ContextHolder.getAssetClassManager();
	public UserManager userManager = ContextHolder.getUserManager();
	public HashMap<Long, String> assetName = new HashMap<Long, String>();
	public User admin;
	public Set<String> symbolNoFoundList = new HashSet<String>();
	
	public GeneratePlan(){
		admin = userManager.get("Admin");
		List<AssetClass> assetClassList = assetClassManager.getClasses();
		for(AssetClass ac: assetClassList){
			assetName.put(ac.getID(), ac.getName());
		}
	}
	
	/**
	 * 创建一个plan
	 * @param name
	 * @throws Exception 
	 */
	public Strategy generateOnePlan(String name, List<VariableFor401k> variables) throws Exception{
		Strategy strategy = strategyManager.get(Configuration.STRATEGY_401K);
		Strategy newStrategy = strategy.clone();
		newStrategy.setVariablesFor401k(variables);
		newStrategy.setID(null);
		newStrategy.setUserName(admin.getUserName());
		newStrategy.setUserID(admin.getID());
		newStrategy.setName(name);
		newStrategy.set401K(true);
		newStrategy.setPlanType(Configuration.PLAN_CATEGORY_VARIABLE_ANNUITIES);
		newStrategy.setStatus(Configuration.PLAN_STATUS_LIVE);
		long planID = strategyManager.add(newStrategy);
		spc.savePlanCreate(admin.getID(), planID);
		fundBackup(name, variables);
		generatePortfolios(newStrategy);
		return newStrategy;
	}
	
	public void generatePortfolios(Strategy plan) throws CloneNotSupportedException {
		Date startingdate = null;
		try {
			startingdate = LTIDate.parseStringToDate(plan.getAttributes().get("startingdate"));
		} catch (RuntimeException e) {}
		Strategy strategy = strategyManager.get(Configuration.STRATEGY_401K);

		List<VariableFor401k> configurations = strategy.getVariablesFor401k();
		List<VariableFor401k> variables = plan.getVariablesFor401k();
		StringBuffer AssetClass = new StringBuffer();
		StringBuffer CandidateFunds = new StringBuffer();
		StringBuffer RedemptionLimit = new StringBuffer();
		StringBuffer WatingPeriod = new StringBuffer();
		StringBuffer RoundtripLimit = new StringBuffer();

		for (int i = 0; i < variables.size(); i++) {
			if (variables.get(i).getMemo() != null && variables.get(i).getMemo().toLowerCase().startsWith("n"))
				continue;
			AssetClass.append(variables.get(i).getAssetClassName());
			CandidateFunds.append(variables.get(i).getSymbol());
			RedemptionLimit.append(variables.get(i).getRedemption());
			WatingPeriod.append(variables.get(i).getWaitingPeriod());
			RoundtripLimit.append(variables.get(i).getRoundtripLimit());
			
			if (i != variables.size() - 1) {
				AssetClass.append(",");
				CandidateFunds.append(",");
				RedemptionLimit.append(",");
				WatingPeriod.append(",");
				RoundtripLimit.append(",");
			}
		}

		for (int i = 0; i < configurations.size(); i++) {
			String portfolioName = plan.getName() + " " + configurations.get(i).getPortfolioName();
			Portfolio portfolio = portfolioManager.get(portfolioName);
			boolean isnew=false;
			if(portfolio==null){
				portfolio = portfolioManager.get(configurations.get(i).getPortfolioID()).clone();
				isnew=true;
				portfolio.setID(null);
			}
			StrategyInf si=portfolio.getStrategies();
			if(si==null){
				si=new StrategyInf();
				portfolio.setStrategies(si);
			}
			Map<String,String> ht=portfolio.getStrategies().getAssetAllocationStrategy().getParameter();
			if(ht==null){
				ht=new HashMap<String, String>();
				portfolio.getStrategies().getAssetAllocationStrategy().setParameter(ht);
			}
			
			ht.put("AssetClass", AssetClass.toString());
			ht.put("CandidateFund", CandidateFunds.toString());
			ht.put("RedemptionLimit", RedemptionLimit.toString());
			ht.put("WaitingPeriod", WatingPeriod.toString());
			ht.put("RoundtripLimit", RoundtripLimit.toString());
			ht.put("PlanID", plan.getID()+"");
			
			portfolio.setName(portfolioName);
			
			portfolio.setUserID(admin.getID());
			portfolio.setState(Configuration.PORTFOLIO_STATE_ALIVE);
			portfolio.setProduction(true);
			portfolio.setMainStrategyID(plan.getID());
			if(startingdate!=null){
				portfolio.setStartingDate(startingdate);
			}
			if (!isnew) {
				portfolioManager.update(portfolio);
			} else {
				portfolioManager.save(portfolio);
			}
		}
	}
	/**
	 * 将plan和model portfolio 设置为public
	 * @param plan
	 */
	public void makepublic(Strategy plan){
		PublicMaker planPublicMaker = new PublicMaker(plan);
		try {
			planPublicMaker.makePublic();
		} catch (Exception e) {
		}
		List<Portfolio> portfolioList= portfolioManager.getModelPortfolios(plan.getID());
		if(portfolioList != null && portfolioList.size()>0){
			for(Portfolio portfolio:portfolioList){
				try {
					PublicMaker portfolioPM=new PublicMaker(portfolio);
					portfolioPM.makePublic();
				} catch (Exception e) {
				}
			}
		}
	}
	/**
	 * 
	 * @param plan
	 * @throws Exception
	 */
	public void setSimilarIssues(Strategy plan) throws Exception{
		StrategyManager stragetyManager = ContextHolder.getStrategyManager();
		String message = "";
		List<String> tickerList = stragetyManager.getPlanTickers(plan.getID());
		List<String> assetsList = stragetyManager.getPlanMinorAssets(tickerList);
		List<String> majorList = stragetyManager.getPlanMajorAssets(assetsList);
		message = message + "The plan consists of " + tickerList.size() + " funds. ";
		message = message + "It covers " + majorList.size() + " major asset classes and " + assetsList.size() + " minor asset classes. ";
		message = message + "The major asset classes it covers are";
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
		stragetyManager.updateStrategySimilarIssues(plan.getID(), message);
	}
	/**
	 * 对fund table 的数据进行备份
	 * @throws Exception
	 */
	public void fundBackup(String name, List<VariableFor401k> variables) throws Exception {

		File root = new File(Configuration.getFundTable(), name);
		if (!root.exists()) {
			root.mkdirs();
		}
		File fundBkFile = new File(root, "2000_12_31_00_00_00.htm");
		if (!fundBkFile.exists()) {
			fundBkFile.createNewFile();
		}
		freemarker.template.Configuration conf = new freemarker.template.Configuration();
		conf.setDirectoryForTemplateLoading(new File(ContextHolder.getServletPath()));
		freemarker.template.Template itemplate = null;
		itemplate = conf.getTemplate("f401kaction_fund.ftl");

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("variables", variables);

		BufferedWriter fr = new BufferedWriter(new FileWriter(fundBkFile));
		itemplate.process(data, fr);
		fr.close();
	}
	
	public VariableFor401k getVariable(String description, String symbol, String memo){
		VariableFor401k variable = new VariableFor401k();
		variable.setDescription(description);
		if(symbol.equals("")){
			symbol="NoTicker";
			variable.setMemo("n");
			variable.setAssetClassName("ROOT");
			variable.setName(description);
		}else{
			Security se = securityManager.getBySymbol(symbol);
			if(se != null){
				if(se.getClassID() != null){
					variable.setMemo(memo);
					variable.setAssetClassName(assetName.get(se.getClassID()));
					variable.setName(se.getName());
				}
				else{
					symbol="NoTicker";
					variable.setMemo("n");
					variable.setAssetClassName("ROOT");
					variable.setName(description);
				}
			}else{
				variable.setMemo("n");
				variable.setAssetClassName("ROOT");
				variable.setName(description);
				symbolNoFoundList.add(symbol);
			}
		
		}
		variable.setSymbol(symbol);
		variable.setRedemption(3);
		variable.setRoundtripLimit(60);
		variable.setWaitingPeriod(0);
		return variable;
	}
	
	private int hasPlan(List<List<String>> planSymbolList, List<String> symbolList){
		for(int i=0;i<planSymbolList.size();++i){
			List<String> strList = planSymbolList.get(i);
			if(symbolList.size() == strList.size() && strList.containsAll(symbolList)){
				return i;
			}
		}
		return -1;
	}
	
	
	private String info;

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	@Override
	public String execute(){
		StringBuffer sb=new StringBuffer();
		try{
			FileReader fr = new FileReader(ContextHolder.getServletPath()+"/plan.csv");
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			List<List<String>> planSymbolList =  new ArrayList<List<String>>();
			List<Strategy> strategyList = new ArrayList<Strategy>();
			while(line != null){
				String[] pairs = line.split(",");
				if(pairs[0].equalsIgnoreCase(FUND_NAME)){
					//开始进入一个plan的数据
					List<VariableFor401k> variables = new ArrayList<VariableFor401k>();
					line = br.readLine();
					String companyName = line.split(",")[0];
					line = br.readLine();
					String planName = line.split(",")[0];
					String name = companyName + " " + planName;
					//开始读fund table的数据
					line = br.readLine();
					String[] variable = line.split(",");
					List<String> symbolList = new ArrayList<String>();
					while(!variable[0].equalsIgnoreCase(FUND_NAME)){
						String description = variable[0];
						String symbol = variable[1];
						String memo = "";
						if(variable.length > 2)
							memo = variable[2];
						variables.add(getVariable(description, symbol, memo));
						symbolList.add(symbol);
						line = br.readLine();
						if(line == null) break;
						variable = line.split(",");
					}
					try {
						int index = hasPlan(planSymbolList, symbolList);
						if(index == -1){
							Strategy plan = generateOnePlan(name, variables);
							makepublic(plan);
							setSimilarIssues(plan);
							strategyList.add(plan);
							sb.append("success: " + "ID: " + plan.getID() + " Name: " + name);
							sb.append("<br>\r\n");
							planSymbolList.add(symbolList);
						}else{
							sb.append("fail: Plan with name " + name + " exists. The same as PlanID: " + strategyList.get(index).getID() + " Name: "+ strategyList.get(index).getName());
							sb.append("<br>\r\n");
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						sb.append("Create Error: " + name);
						sb.append("<br>\r\n");
						e.printStackTrace();
					}
				}
			}
			sb.append("<br>\r\n");
			sb.append("<br>\r\n");
			sb.append("Symbol No Found:");
			sb.append("<br>\r\n");
			for(String str: symbolNoFoundList){
				sb.append(str);
				sb.append("<br>\r\n");
			}
		}catch(Exception e){
			info = StringUtil.getStackTraceString(e);
		}
		info = sb.toString();
		return "info.ftl";
	}
}
