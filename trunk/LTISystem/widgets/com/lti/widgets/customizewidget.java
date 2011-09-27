package com.lti.widgets;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import java.util.List;
import java.util.Map;

import com.lti.action.Action;
import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.service.AssetClassManager;
import com.lti.service.PortfolioManager;
import com.lti.service.ProfileManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.AllocationTemplate;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.EmailNotification;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Profile;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.Menu;
import com.lti.util.BuildingUtil;
import com.lti.util.LTIDate;
import com.lti.util.Sort;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONBuilder;

public class customizewidget extends framework {

	private Long portfolioID;
	private Long planID;
	private static Map<String, List<String>> assetClassFundMap;
	private static List<AssetClass> planAssetClassList;
	private static List<String> symbolList;
	private static List<AssetClass> riskyAssetClassList;
	private static List<AssetClass> stableAssetClassList;

	public String basic_inf() {
		StrategyManager sm = ContextHolder.getStrategyManager();
		PortfolioManager pm = ContextHolder.getPortfolioManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		AssetClassManager acm = ContextHolder.getAssetClassManager();
		UserManager userManager = ContextHolder.getUserManager();
		User user = userManager.getLoginUser();
		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		if(user == null||user.getID()==0){
			jb.object().key("result").value(false);
			jb.key("message").value("You should login before customize a portfolio.");
			jb.endObject();
			json = w.toString();
			return Action.JSON;
		}
		if (portfolioID != null) {			
			Portfolio portfolio = pm.get(portfolioID);
			String planName = "";
			Long strategyID = portfolio.getStrategies().getAssetAllocationStrategy().getID();
			if (strategyID == null)
				strategyID = 771l;
			planID = Long.parseLong(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID"));
			if (planID != null) {
				planName = sm.get(planID).getName();
			} else {
				planName = "Hewlett Packard 401K";
				planID = 690l;
			}
			String portfolioName = "";
			String strategyName = "";
			double riskprofileNum = 0;
			riskprofileNum = Double.parseDouble(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("RiskProfile").replaceAll("\"", ""));
			if (riskprofileNum == 0) {
				riskprofileNum = 40;
			}

			if (strategyID == 771l) {
				strategyName = "Strategic Asset Allocation(SAA)";
				portfolioName = user.getUserName()+"_" + planName + "_Strategic Asset Allocation";
			} else {
				strategyName = "Tactical Asset Allocation(TAA)";
				portfolioName = user.getUserName()+"_"+ planName + "_Tactical Asset Allocation";
			}
			
			jb.object().key("result").value(true).key("portfolioName").value(portfolioName);
			jb.key("strategyName").value(strategyName);
			jb.key("planName").value(planName);
			jb.key("riskprofile").value(riskprofileNum);
			jb.key("planID").value(planID);
			jb.key("strategyID").value(strategyID);

			/*************************** Allocation Template ****************************************/
			planAssetClassList = new ArrayList<AssetClass>();
			List<String> planAssetClassNameList = new ArrayList<String>();
			symbolList = new ArrayList<String>();
			List<Security> securityList = new ArrayList<Security>();
			Strategy plan = sm.get(planID);
			List<VariableFor401k> candidateList = plan.getVariablesFor401k();
			for (VariableFor401k vf : candidateList) {
				Security se = securityManager.getBySymbol(vf.getSymbol());
				symbolList.add(vf.getSymbol());
				if (se != null && se.getAssetClass() != null) {
					securityList.add(se);
					String assetClassName = se.getAssetClass().getName();
					if (!assetClassName.trim().equals("ROOT") && !planAssetClassNameList.contains(assetClassName.trim().toLowerCase())) {
						AssetClass assetClass = se.getAssetClass();
						planAssetClassList.add(assetClass);
						planAssetClassNameList.add(assetClassName.trim().toLowerCase());
					}

				}
			}
			if(!symbolList.contains("CASH")){
				symbolList.add("CASH");
			}

			if (planAssetClassList != null && planAssetClassList.size() != 0) {
				List<AssetClass> childAssetClassList = planAssetClassList;
				for (;;) {
					List<AssetClass> parentList = new ArrayList<AssetClass>();
					parentList = acm.getParentAssetClassList(childAssetClassList);
					if (parentList == null || parentList.size() == 0) {
						break;
					} else {
						for (AssetClass a : parentList) {
							if (!planAssetClassNameList.contains(a.getName().trim().toLowerCase())) {
								planAssetClassList.add(a);
								planAssetClassNameList.add(a.getName().trim().toLowerCase());
							}

						}
						childAssetClassList = parentList;
					}
				}
			}

			riskyAssetClassList = new ArrayList<AssetClass>();
			for (AssetClass ac : planAssetClassList) {
				String result = sm.getRiskyOrStableAsset(ac.getName().trim());
				if (result.equals("Risky Asset")) {
					riskyAssetClassList.add(ac);
				}
			}

			stableAssetClassList = new ArrayList<AssetClass>();
			for (AssetClass ac : planAssetClassList) {
				String result = sm.getRiskyOrStableAsset(ac.getName().trim());
				if (result.equals("Stable Asset")) {
					stableAssetClassList.add(ac);
				}
			}

			/*************************************** default allocation ******************************/
			AllocationTemplate defaultAct = sm.getAllocationTemplateById(1l);
			String defaultDescription = defaultAct.getDescription();
			List<String> defaultRiskyAssetClassList = new ArrayList<String>();
			List<String> defaultRiskyPercentageList = new ArrayList<String>();
			List<String> defaultRiskyFundNumList = new ArrayList<String>();
			List<String> defaultStableAssetClassList = new ArrayList<String>();
			List<String> defaultStablePercentageList = new ArrayList<String>();
			List<String> defaultStableFundNumList = new ArrayList<String>();
			if (defaultAct != null) {
				String defaultAssetClass = defaultAct.getAssetClassName();
				String defaultPercentage = defaultAct.getAssetClassWeight();
				String[] defaultAssetItem = defaultAssetClass.split(",");
				String[] defaultPercentItem = defaultPercentage.split(",");

				assetClassFundMap = new HashMap<String, List<String>>();
				assetClassFundMap = acm.getAvailableAssetClassSet(defaultAssetItem, symbolList, false, false);

				for (int i = 0; i < defaultAssetItem.length; i++) {
					String str = defaultAssetItem[i];
					String result = sm.getRiskyOrStableAsset(str);
					if (result.equals("Risky Asset")) {
						defaultRiskyAssetClassList.add(str);
						defaultRiskyPercentageList.add(defaultPercentItem[i]);
					} else if (result.equals("Stable Asset")) {
						defaultStableAssetClassList.add(str);
						defaultStablePercentageList.add(defaultPercentItem[i]);
					}
				}
				for (String s : defaultRiskyAssetClassList) {
					List<String> riskyFundList = assetClassFundMap.get(s);
					if (riskyFundList != null) {
						defaultRiskyFundNumList.add(String.valueOf(riskyFundList.size()));
					} else {
						defaultRiskyFundNumList.add("0");
					}
				}

				for (String ss : defaultStableAssetClassList) {
					List<String> stableFundList = assetClassFundMap.get(ss);
					if (stableFundList != null) {
						defaultStableFundNumList.add(String.valueOf(stableFundList.size()));
					} else {
						defaultStableFundNumList.add("0");
					}
				}

			}

			double trpercentage = 0;
			double tspercentage = 0;
			DecimalFormat df = new DecimalFormat("###.00");
			for (String s : defaultRiskyPercentageList) {
				trpercentage += Double.parseDouble(s);
			}

			String defaultTotalRiskyPercentage = df.format(trpercentage);
			for (String s : defaultStablePercentageList) {
				tspercentage += Double.parseDouble(s);
			}
			String defaultTotalStablePercentage = df.format(tspercentage);
			List<AllocationTemplate> templateList = sm.getAllallocationTemplate();

			jb.key("defaultDescription").value(defaultDescription);
			jb.key("defaultRiskyAssetClassList").value(JSONArray.fromObject(defaultRiskyAssetClassList));
			jb.key("defaultRiskyPercentageList").value(JSONArray.fromObject(defaultRiskyPercentageList));
			jb.key("defaultRiskyFundNumList").value(JSONArray.fromObject(defaultRiskyFundNumList));
			jb.key("defaultStableAssetClassList").value(JSONArray.fromObject(defaultStableAssetClassList));
			jb.key("defaultStablePercentageList").value(JSONArray.fromObject(defaultStablePercentageList));
			jb.key("defaultStableFundNumList").value(JSONArray.fromObject(defaultStableFundNumList));
			jb.key("defaultTotalRiskyPercentage").value(defaultTotalRiskyPercentage);
			jb.key("defaultTotalStablePercentage").value(defaultTotalStablePercentage);
			jb.key("templateList").value(JSONArray.fromObject(templateList));
			jb.endObject();
			json = w.toString();

		} 

		return Action.JSON;
	}

	private String portfolioName;
	private String planName;
	private String frequency;
	private Long strategyID;
	private Double risknumber;
	private Integer maxOfRiskyAsset;
	private Integer numberOfMainRiskyClass;
	private String MainAssetClass;
	private String MainAssetClassWeight;
	private String AssetFundString;
	private String BuyThreshold;
	private String SellThreshold;
	private String SpecifyAssetFund;
	private String UseDataObject;

	public String generatePortfolio() {
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		ProfileManager profileManager = ContextHolder.getProfileManager();
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);		
		
		portfolioID = 0l;
		jb.object();
		try {
			// 检查plan是否存在
			Strategy plan = strategyManager.get(planID);
			if (plan == null) {
				String message = "The plan doesn't exist.";
				jb.key("message").value(message).key("portfolioID").value(portfolioID);
				json = w.toString();
				return Action.JSON;
			}
			
			User user = ContextHolder.getUserManager().getLoginUser();
			if(user==null|| user.getID()==0){
				String message = "you have to login first";
				jb.key("message").value(message).key("portfolioID").value(portfolioID);
				json = w.toString();
				return Action.JSON;
			}
			
			Long userID = user.getID();

			Profile profile = profileManager.getProfileByPortfolioID(0l);
			Strategy center = strategyManager.get(Configuration.STRATEGY_401K);
			List<Portfolio> templateportfolios = portfolioManager.getModelPortfolios(center.getID());
			Portfolio p = null;
			for (int i = 0; i < templateportfolios.size(); i++) {
				Portfolio tp = templateportfolios.get(i);
				if (tp.getStrategies() != null && tp.getStrategies().getAssetAllocationStrategy().getID().equals(strategyID)) {
					p = tp.clone();
					break;
				}
			}
			if (p == null) {
				p = portfolioManager.get(0l).clone();
			}

			String strategyName = "";
			if (strategyID == 771L) {
				strategyName = "Strategic Asset Allocation";
			} else {
				strategyName = "Tactical Asset Allocation";
			}

			p.getStrategies().getAssetAllocationStrategy().setID(strategyID);
			p.getStrategies().getAssetAllocationStrategy().setName(strategyName);
			Map<String, String> ht = p.getStrategies().getAssetAllocationStrategy().getParameter();
			ht.put("UseRiskProfile", "true");
			ht.put("RiskProfile", risknumber + "");
			if (frequency == null) {
				frequency = "monthly";
			}
			ht.put("MaxOfRiskyAsset", maxOfRiskyAsset + "");
			if (maxOfRiskyAsset != 2) {
				ht.put("UseDataObject", "false");
			}
			ht.put("NumberOfMainRiskyClass", numberOfMainRiskyClass + "");
			if (numberOfMainRiskyClass != 2) {
				ht.put("UseDataObject", "false");
			}
			if (SpecifyAssetFund.equals("true") && strategyID == 771l) {
				AssetFundString = AssetFundString.replaceAll("@", "#");
				ht.put("MainAssetClass", MainAssetClass);
				ht.put("MainAssetClassWeight", MainAssetClassWeight);
				ht.put("SpecifyAssetFund", "true");
				ht.put("AssetFundString", AssetFundString);
				ht.put("BuyThreshold", BuyThreshold);
				ht.put("SellThreshold", SellThreshold);
				if (UseDataObject.equals("true")) {
					ht.put("UseDataObject", "true");
				} else {
					ht.put("UseDataObject", "false");
				}
			}
			ht.put("Frequency", frequency);
			ht.put("CheckFrequency", frequency);
			ht.put("RebalanceFrequency", frequency);
			ht.put("PlanID", planID + "");
			p.setID(null);

			if (portfolioName != null) {
				p.setName(portfolioName);
			} else {
				if (planName != null) {
					p.setName(planName + " " + System.currentTimeMillis());
				}
			}
			p.setUserID(userID);

			List<VariableFor401k> variables = plan.getVariablesFor401k();
			StringBuffer AssetClass = new StringBuffer();
			StringBuffer CandidateFunds = new StringBuffer();
			StringBuffer RedemptionLimit = new StringBuffer();
			StringBuffer WaitingPeriod = new StringBuffer();
			StringBuffer RoundtripLimit = new StringBuffer();

			if (variables != null) {
				for (int i = 0; i < variables.size(); i++) {
					if (variables.get(i).getMemo() != null && variables.get(i).getMemo().toLowerCase().startsWith("n"))
						continue;
					AssetClass.append(variables.get(i).getAssetClassName());
					CandidateFunds.append(variables.get(i).getSymbol());
					RedemptionLimit.append(variables.get(i).getRedemption());
					WaitingPeriod.append(variables.get(i).getWaitingPeriod());
					RoundtripLimit.append(variables.get(i).getRoundtripLimit());
					if (i != variables.size() - 1) {
						AssetClass.append(",");
						CandidateFunds.append(",");
						RedemptionLimit.append(",");
						WaitingPeriod.append(",");
						RoundtripLimit.append(",");
					}
				}
			}
			ht.put("AssetClass", AssetClass.toString());
			ht.put("CandidateFund", CandidateFunds.toString());
			ht.put("RedemptionLimit", RedemptionLimit.toString());
			ht.put("WaitingPeriod", WaitingPeriod.toString());
			ht.put("RoundtripLimit", RoundtripLimit.toString());

			p.setProduction(true);
			p.setOriginalPortfolioID(null);
			p.setMainStrategyID(null);
			p.setEndDate(p.getStartingDate());
			try {
				portfolioManager.save(p);
			} catch (Exception e) {
				String message = e.getMessage();
				jb.key("message").value(message);
				jb.key("code").value("500");
				jb.key("portfolioID").value("0");
				jb.endObject();
				json = w.toString();
				return Action.JSON;
			}

			profile.setPortfolioID(p.getID());
			profile.setIsGenerated(true);
			profile.setPlanID(planID);
			portfolioID = p.getID();

			try {
				profile.setUserID(userID);
				profile.setUserName(ContextHolder.getUserManager().getLoginUser().getUserName());
				if (portfolioID == 0 && planID == 0) {
					Profile _default = profileManager.get(portfolioID, userID, planID);
					if (_default != null) {
						profileManager.update(profile);
					} else {
						profile.setPlanName("STATIC");
						profile.setPortfolioName("New Portfolio");
						profileManager.save(profile);
					}
				} else {
					Portfolio p2 = ContextHolder.getPortfolioManager().get(portfolioID);
					if (p2 != null) {
						profile.setPortfolioName(p2.getName());
					}
					Strategy plan2 = ContextHolder.getStrategyManager().get(planID);
					if (plan2 != null) {
						profile.setPlanName(plan2.getName());
					}
					profileManager.save(profile);
				}

			} catch (Exception e) {
				String message = "Failed to create profile.error:-1";
				jb.key("message").value(message);
				jb.key("code").value("500");
				jb.key("portfolioID").value("0");
				jb.endObject();
				json = w.toString();
				return Action.JSON;
			}

			setEmailNotification(userID, portfolioID);
			 mpm.afterPortfolioCustomize(userID, portfolioID, planID);

			BuildingUtil.monitor(p.getID());
			String message = "customize portfolio success.";
			jb.key("message").value(message);
			jb.key("code").value("400");
			jb.key("portfolioID").value(portfolioID);
		} catch (Exception e) {
			e.printStackTrace();
			String message = "Create portfolio failed";
			jb.key("message").value(message);
			jb.key("code").value("500");
			jb.key("portfolioID").value("0");
		}
		jb.endObject();
		json = w.toString();
		return Action.JSON;
	}

	private void setEmailNotification(Long userID, Long portfolioID) {
		UserManager userManager = ContextHolder.getUserManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		if (userManager.getEmailNotification(userID, portfolioID) != null)
			return;
		EmailNotification en = new EmailNotification();
		en.setUserID(userID);
		en.setPortfolioID(portfolioID);
		en.setSpan(0);
		Date today = new Date();
		Date lastSentDate = portfolioManager.getRealTransactionLatestDate(portfolioID, today);
		if (lastSentDate == null)
			lastSentDate = today;
		en.setLastSentDate(LTIDate.clearHMSM(lastSentDate));
		userManager.addEmailNotification(en);
	}

	private int size;

	public String planName() {
		StrategyManager sm = ContextHolder.getStrategyManager();
		String sql = "select distinct(strategyname),strategyid from cache_group_strategy gpi where gpi.Type=" + Configuration.STRATEGY_401K_TYPE;
		if (planName != null && !planName.equals("")) {
			sql += " and gpi.StrategyName like '%" + planName + "%'";
		}

		if (size > 0) {
			sql += " limit 0," + size;
		}

		List<Object[]> strategies = null;
		try {
			strategies = sm.findBySQL(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}

		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		jb.object().key("size").value(size);
		jb.key("planNames");
		JSONArray ja = new JSONArray();
		if (strategies != null && strategies.size() != 0) {
			for (Object[] arr : strategies) {
				JSONObject jo = new JSONObject();
				jo.accumulate("planName", ((String) arr[0]).replace("\"", "\\\""));
				ja.add(jo);
			}
		}
		jb.value(ja);
		jb.endObject();
		json = w.toString();

		return Action.JSON;
	}

	private boolean updateInfo;

	public String planFundtable() {
		StrategyManager sm = ContextHolder.getStrategyManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		AssetClassManager acm = ContextHolder.getAssetClassManager();
		Strategy plan = sm.get(planName);
		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		jb.object();
		if (plan == null) {
			jb.key("result").value(false);
			jb.endObject();
			json = w.toString();
			return Action.JSON;
		} else {
			jb.key("result").value(true);
			Map<String, List<VariableFor401k>> fundtableMap = sm.getSixMajorAssetClassForPlan(plan.getID());
			jb.key("fundtable");
			jb.value(JSONObject.fromObject(fundtableMap));

		}
		jb.endObject();
		json = w.toString();

		if (updateInfo) {
			planAssetClassList = new ArrayList<AssetClass>();
			List<String> planAssetClassNameList = new ArrayList<String>();
			symbolList = new ArrayList<String>();
			List<Security> securityList = new ArrayList<Security>();
			List<VariableFor401k> candidateList = plan.getVariablesFor401k();
			for (VariableFor401k vf : candidateList) {
				Security se = securityManager.getBySymbol(vf.getSymbol());
				symbolList.add(vf.getSymbol());
				if (se != null && se.getAssetClass() != null) {
					securityList.add(se);
					String assetClassName = se.getAssetClass().getName();
					if (!assetClassName.trim().equals("ROOT") && !planAssetClassNameList.contains(assetClassName.trim().toLowerCase())) {
						AssetClass assetClass = se.getAssetClass();
						planAssetClassList.add(assetClass);
						planAssetClassNameList.add(assetClassName.trim().toLowerCase());
					}

				}
			}

			if (planAssetClassList != null && planAssetClassList.size() != 0) {
				List<AssetClass> childAssetClassList = planAssetClassList;
				for (;;) {
					List<AssetClass> parentList = new ArrayList<AssetClass>();
					parentList = acm.getParentAssetClassList(childAssetClassList);
					if (parentList == null || parentList.size() == 0) {
						break;
					} else {
						for (AssetClass a : parentList) {
							if (!planAssetClassNameList.contains(a.getName().trim().toLowerCase())) {
								planAssetClassList.add(a);
								planAssetClassNameList.add(a.getName().trim().toLowerCase());
							}

						}
						childAssetClassList = parentList;
					}
				}
			}

			riskyAssetClassList = new ArrayList<AssetClass>();
			for (AssetClass ac : planAssetClassList) {
				String result = sm.getRiskyOrStableAsset(ac.getName().trim());
				if (result.equals("Risky Asset")) {
					riskyAssetClassList.add(ac);
				}
			}

			stableAssetClassList = new ArrayList<AssetClass>();
			for (AssetClass ac : planAssetClassList) {
				String result = sm.getRiskyOrStableAsset(ac.getName().trim());
				if (result.equals("Stable Asset")) {
					stableAssetClassList.add(ac);
				}
			}
		}

		return Action.JSON;
	}

	private int assetType;
	private String term;

	public String searchAssetClass() {

		AssetClassManager acm = ContextHolder.getAssetClassManager();
		StrategyManager sm = ContextHolder.getStrategyManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		PortfolioManager pm = ContextHolder.getPortfolioManager();
		List<AssetClass> matchAssetClassList = new ArrayList<AssetClass>();
		String message = "";

		if (planAssetClassList == null || planAssetClassList.size() == 0) {
			if (portfolioID == null)
				return Action.MESSAGE;
			Portfolio portfolio = pm.get(portfolioID);
			try {
				planID = Long.parseLong(portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID"));
			} catch (Exception e) {
				// e.printStackTrace();
			}
			if (planID == null)
				planID = portfolio.getMainStrategyID();
			planAssetClassList = new ArrayList<AssetClass>();
			List<String> planAssetClassNameList = new ArrayList<String>();
			List<Security> securityList = new ArrayList<Security>();
			Strategy plan = sm.get(planID);
			List<VariableFor401k> candidateList = plan.getVariablesFor401k();
			for (VariableFor401k vf : candidateList) {
				Security se = securityManager.getBySymbol(vf.getSymbol());
				if (se != null && se.getAssetClass() != null) {
					securityList.add(se);
					String assetClassName = se.getAssetClass().getName();
					if (!assetClassName.trim().equals("ROOT") && !planAssetClassNameList.contains(assetClassName.trim().toLowerCase())) {
						AssetClass assetClass = se.getAssetClass();
						planAssetClassList.add(assetClass);
						planAssetClassNameList.add(assetClassName.trim().toLowerCase());
					}

				}
			}

			if (planAssetClassList != null && planAssetClassList.size() != 0) {
				List<AssetClass> childAssetClassList = planAssetClassList;
				for (;;) {
					List<AssetClass> parentList = new ArrayList<AssetClass>();
					parentList = acm.getParentAssetClassList(childAssetClassList);
					if (parentList == null || parentList.size() == 0) {
						break;
					} else {
						for (AssetClass a : parentList) {
							if (!planAssetClassNameList.contains(a.getName().trim().toLowerCase())) {
								planAssetClassList.add(a);
								planAssetClassNameList.add(a.getName().trim().toLowerCase());
							}

						}
						childAssetClassList = parentList;
					}
				}
			}
		}

		if (riskyAssetClassList == null || riskyAssetClassList.size() == 0) {
			if (riskyAssetClassList == null)
				riskyAssetClassList = new ArrayList<AssetClass>();
			for (AssetClass ac : planAssetClassList) {
				String result = sm.getRiskyOrStableAsset(ac.getName().trim());
				if (result.equals("Risky Asset")) {
					riskyAssetClassList.add(ac);
				}
			}
		}

		if (stableAssetClassList == null || stableAssetClassList.size() == 0) {
			if (stableAssetClassList == null)
				stableAssetClassList = new ArrayList<AssetClass>();
			for (AssetClass ac : planAssetClassList) {
				String result = sm.getRiskyOrStableAsset(ac.getName().trim());
				if (result.equals("Stable Asset")) {
					stableAssetClassList.add(ac);
				}
			}
		}

		if (term != null && !term.equals("riskyspace") && !term.equals("stablespace")) {
			if (assetType == 0) {
				matchAssetClassList = new ArrayList<AssetClass>();
				for (AssetClass rac : riskyAssetClassList) {
					String assetClassName = rac.getName().trim().toLowerCase();
					if (assetClassName.contains(term.trim().toLowerCase())) {
						matchAssetClassList.add(rac);
					}
				}
			} else if (assetType == 1) {
				matchAssetClassList = new ArrayList<AssetClass>();
				for (AssetClass sac : stableAssetClassList) {
					String assetClassName = sac.getName().trim().toLowerCase();
					if (assetClassName.contains(term.trim().toLowerCase())) {
						matchAssetClassList.add(sac);
					}
				}
			}
		} else {
			if (term.equals("riskyspace") && assetType == 0) {
				matchAssetClassList = riskyAssetClassList;
				Menu menu = acm.getMenuByAssetList(matchAssetClassList, 0);
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[\r\n");
				this.buildJsonString(menu);
				if (count < matchAssetClassList.size()) {
					String curJson = sb.toString();
					for (AssetClass ac : matchAssetClassList) {
						String name = ac.getName();
						if (!curJson.contains(name)) {
							if (count != 0) {
								sb.append(",");
							}
							sb.append("{\"name\":\"");
							sb.append(name);
							sb.append("\",\"data\":true}\r\n");
							count++;
						}
					}
				}
				if (count != 0) {
					sb.append(",");
				}
				sb.append("{\"name\":\"CASH\",\"data\":true}\r\n]");
				sb2.append(sb.toString());
				message = sb2.toString();
				json = "{\"AssetClassName\":" + message + "}";
			} else if (term.equals("stablespace") && assetType == 1) {
				matchAssetClassList = stableAssetClassList;
				Menu menu = acm.getMenuByAssetList(matchAssetClassList, 0);
				StringBuffer sb2 = new StringBuffer();
				sb2.append("[\r\n");
				this.buildJsonString(menu);
				if (count != 0) {
					sb.append(",");
				}
				sb.append("{\"name\":\"CASH\",\"data\":true}\r\n]");
				sb2.append(sb.toString());
				message = sb2.toString();
				json = "{\"AssetClassName\":" + message + "}";
			}
			return Action.JSON;
		}

		if (matchAssetClassList == null || matchAssetClassList.size() == 0) {
			message = "[\r\n{\"name\":\"CASH\",\"data\":true}\r\n]";
			json = "{\"AssetClassName\":" + message + "}";
			return Action.JSON;
		}
		matchAssetClassList = Sort.assetClassSort(matchAssetClassList);
		StringBuffer strb = new StringBuffer();
		strb.append("[\r\n");
		for (int i = 0; i < matchAssetClassList.size(); i++) {
			if (i != 0)
				strb.append(",");
			AssetClass a = matchAssetClassList.get(i);
			strb.append("{\"name\":\"");
			strb.append(a.getName());
			strb.append("\",\"data\":true}\r\n");
		}

		strb.append(",{\"name\":\"CASH\",\"data\":true}\r\n]");
		message = strb.toString();
		json = "{\"AssetClassName\":" + message + "}";

		return Action.JSON;
	}

	private StringBuffer sb = new StringBuffer();
	private int count = 0;
	private int j = 0;

	public void buildJsonString(Menu menu) {
		if (!menu.getText().equalsIgnoreCase("ROOT")) {
			if (count != 0) {
				sb.append(",");
			}
			sb.append("{\"name\":\"");
			for (int i = 0; i < j - 1; i++) {
				sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
			}
			sb.append(menu.getText());
			sb.append("\",\"data\":true}\r\n");
			count++;
		}
		if (menu.getChildren() == null || menu.getChildren().size() == 0) {
			return;
		} else {
			j++;
			List<Menu> lists = menu.getChildren();
			for (int k = 0; k < lists.size(); k++) {
				buildJsonString(lists.get(k));
			}
			j--;
		}

	}

	private String assetClassName;

	public String getAssetFundNum() {
		List<String> numList = new ArrayList<String>();
		AssetClassManager acm = ContextHolder.getAssetClassManager();
		String[] assetClassItem = assetClassName.split(",");
		if (symbolList != null && symbolList.size() != 0) {
			assetClassFundMap = acm.getAvailableAssetClassSet(assetClassItem, symbolList, false, false);
			if (assetClassFundMap != null && assetClassFundMap.size() != 0) {
				for (int i = 0; i < assetClassItem.length; i++) {
					int len = 0;
					if (assetClassFundMap.get(assetClassItem[i]) == null)
						len = 0;
					else {
						len = assetClassFundMap.get(assetClassItem[i]).size();
					}

					numList.add(String.valueOf(len));
				}
			}
		}
		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		jb.object().key("size").value(numList.size());
		jb.key("number").value(JSONArray.fromObject(numList));
		jb.endObject();
		json = w.toString();
		return Action.JSON;
	}

	public String getFundtable() {

		StrategyManager sm = ContextHolder.getStrategyManager();
		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		jb.object();
		Strategy plan = null;
		if (planName != null && planName != "") {
			plan = sm.get(planName);
			planID = plan.getID();
		}
		if (plan == null) {
			jb.key("result").value(false);
			jb.endObject();
			json = w.toString();
			return Action.JSON;
		}
		List<String> lists = new ArrayList<String>();
		if (assetClassFundMap != null && assetClassFundMap.size() != 0) {
			lists = assetClassFundMap.get(assetClassName);
			if (lists.size() != 0) {
				jb.key("result").value(true);
				jb.key("items");
				JSONArray arr = new JSONArray();
				for (int i = 0; i < lists.size(); i++) {
					String symbol = lists.get(i);
					VariableFor401k var = sm.getVariable401K(planID, symbol);
					JSONObject o = new JSONObject();
					o.accumulate("symbol", symbol);

					if (var == null) {
						o.accumulate("description", " ");
					} else {
						if (var.getDescription() == null || var.getDescription().equals("")) {
							o.accumulate("description", " ");
						} else {
							o.accumulate("description", var.getDescription());
						}
					}
					arr.add(o);
				}// end for

				jb.value(arr);
			}// end if
		}// end if
		jb.endObject();
		json = w.toString();
		return Action.JSON;
	}

	public String searchFund() {
		if (symbolList != null && symbolList.size() != 0) {
			StringWriter w = new StringWriter();
			JSONBuilder jb = new JSONBuilder(w);
			boolean containCash = false;
			jb.object().key("funds");
			JSONArray arr = new JSONArray();
			for (String ss : symbolList) {
				String lowString = ss.toLowerCase();
				term = term.toLowerCase();
				if (lowString.contains(term)) {
					if(lowString.equalsIgnoreCase("cash")){
						containCash = true;
					}
					JSONObject o = new JSONObject();
					o.accumulate("fund", ss);
					arr.add(o);
				}
			}
			if(!containCash){
				JSONObject o = new JSONObject();
				o.accumulate("fund", "CASH");
				arr.add(o);
			}
			jb.value(arr);
			jb.endObject();
			json = w.toString();
		}
		return Action.JSON;
	}

	private String fundName;

	public String getFundType() {
		SecurityManager sm = ContextHolder.getSecurityManager();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		jb.object().key("type");
		boolean isfund = false;
		if (fundName != null) {
			for (String ss : symbolList) {
				if (ss.equalsIgnoreCase(fundName)) {
					isfund = true;
					break;
				}
			}
			if (!isfund) {
				jb.value("not fund");
				jb.endObject();
				json = w.toString();
				return Action.JSON;
			}
			Security se = sm.getBySymbol(fundName);
			if (se != null && se.getAssetClass() != null) {
				String result="";
				if(fundName.equalsIgnoreCase("cash")){
					result = "Stable Asset";
				}else{
					String assetName = se.getAssetClass().getName();
					result = strategyManager.getRiskyOrStableAsset(assetName);
				}
				
				jb.value(result);
				jb.endObject();
				json = w.toString();
			}
			if (symbolList != null && symbolList.size() != 0) {
				for (int i = 0; i < symbolList.size(); i++) {
					String str = symbolList.get(i);
					if (str.equalsIgnoreCase(fundName)) {
						symbolList.remove(i);
						break;
					}
				}
			}
		}
		return Action.JSON;
	}

	public String deleteFund() {
		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		jb.object();
		if (fundName != null) {
			symbolList.add(fundName);
			jb.key("result").value(true);
		}
		jb.endObject();
		json = w.toString();
		return Action.JSON;
	}

	public String getFundInfo() {
		StrategyManager sm = ContextHolder.getStrategyManager();
		Strategy plan = null;
		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		jb.object();
		if (planName != null && planName != "") {
			plan = sm.get(planName);
			planID = plan.getID();
		}
		if (plan == null) {
			jb.key("result").value(false);
			jb.endObject();
			json = w.toString();
			return Action.JSON;
		}
		if (fundName != null) {
			jb.key("result").value(true);
			VariableFor401k var = sm.getVariable401K(planID, fundName);
			if (var != null) {
				jb.key("symbol").value(var.getSymbol());
				if (var.getDescription() != null) {
					jb.key("description").value(var.getDescription());
				} else {
					jb.key("description").value(" ");
				}
			}
		}
		jb.endObject();
		json = w.toString();
		return Action.JSON;
	}

	public String getSingleFundInfo() {
		if (fundName != null) {
			StringWriter w = new StringWriter();
			JSONBuilder jb = new JSONBuilder(w);
			List<String> numList = new ArrayList<String>();
			String[] fundItem = fundName.split(",");
			for (int i = 0; i < fundItem.length; i++) {
				String name = fundItem[i];
				int num = symbolList.size();
				int j = 0;
				for (; j < num; j++) {
					String symbol = symbolList.get(j);
					if (symbol.equalsIgnoreCase(name)) {
						symbolList.remove(j);
						break;
					}
				}
				if (j == num) {
					numList.add("0");
				} else {
					numList.add("1");
				}
			}
			jb.object().key("size").value(numList.size());
			jb.key("number").value(JSONArray.fromObject(numList));
			jb.endObject();
		}
		return Action.JSON;
	}

	private String templateName;

	public String getTemplateInfo() {
		StrategyManager sm = ContextHolder.getStrategyManager();
		AssetClassManager acm = ContextHolder.getAssetClassManager();
		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		jb.object();
		if (templateName != null && !templateName.equals("")) {
			AllocationTemplate at = sm.getAllocationTemplateByName(templateName).get(0);
			if (at != null) {
				List<String> riskyAssetClassList = new ArrayList<String>();
				List<String> riskyPercentageList = new ArrayList<String>();
				List<String> riskyFundNumList = new ArrayList<String>();
				List<String> stableAssetClassList = new ArrayList<String>();
				List<String> stablePercentageList = new ArrayList<String>();
				List<String> stableFundNumList = new ArrayList<String>();
				String description = at.getDescription();
				String assetClass = at.getAssetClassName();
				String percentage = at.getAssetClassWeight();
				String[] assetItem = assetClass.split(",");
				String[] percentItem = percentage.split(",");
				String[] fundItem = fundName.split(",");
				for (String str : fundItem) {
					symbolList.add(str);
				}

				assetClassFundMap = new HashMap<String, List<String>>();
				assetClassFundMap = acm.getAvailableAssetClassSet(assetItem, symbolList, false, false);

				for (int i = 0; i < assetItem.length; i++) {
					String str = assetItem[i];
					String result = sm.getRiskyOrStableAsset(str);
					if (result.equals("Risky Asset")) {
						riskyAssetClassList.add(str);
						riskyPercentageList.add(percentItem[i]);
					} else if (result.equals("Stable Asset")) {
						stableAssetClassList.add(str);
						stablePercentageList.add(percentItem[i]);
					}
				}
				for (String s : riskyAssetClassList) {
					List<String> riskyFundList = assetClassFundMap.get(s);
					if (riskyFundList != null) {
						riskyFundNumList.add(String.valueOf(riskyFundList.size()));
					} else {
						riskyFundNumList.add("0");
					}
				}

				for (String ss : stableAssetClassList) {
					List<String> stableFundList = assetClassFundMap.get(ss);
					if (stableFundList != null) {
						stableFundNumList.add(String.valueOf(stableFundList.size()));
					} else {
						stableFundNumList.add("0");
					}
				}
				jb.key("riskyAssetClassList").value(JSONArray.fromObject(riskyAssetClassList));
				jb.key("riskyPercentageList").value(JSONArray.fromObject(riskyPercentageList));
				jb.key("riskyFundNumList").value(JSONArray.fromObject(riskyFundNumList));
				jb.key("stableAssetClassList").value(JSONArray.fromObject(stableAssetClassList));
				jb.key("stablePercentageList").value(JSONArray.fromObject(stablePercentageList));
				jb.key("stableFundNumList").value(JSONArray.fromObject(stableFundNumList));
				jb.key("description").value(description);
				jb.endObject();
				json = w.toString();
			}

		}
		return Action.JSON;
	}

	public String getAllAssetFund() {
		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		jb.object();
		String[] assetClassItem = assetClassName.split(",");
		String message = "";
		if (assetClassFundMap != null && assetClassFundMap.size() != 0) {
			for (int i = 0; i < assetClassItem.length; i++) {
				String ss = assetClassItem[i];
				List<String> lists = assetClassFundMap.get(ss);
				if (lists != null && lists.size() != 0) {
					if (i != 0) {
						message += ",";
					}
					StringBuffer sb = new StringBuffer();
					sb.append(ss);
					for (String s : lists) {
						sb.append("@");
						sb.append(s);
					}
					message += sb.toString();
				} else {
					if (i != 0) {
						message += ",";
					}
					StringBuffer sb = new StringBuffer();
					String fund = "$" + ss;
					sb.append(fund);
					sb.append("@");
					sb.append(ss);
					message += sb.toString();
				}
			}
			jb.key("message").value(message);
			jb.endObject();
			json = w.toString();
		}
		return Action.JSON;
	}

	public long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public long getPlanID() {
		return planID;
	}

	public void setPlanID(long planID) {
		this.planID = planID;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public Long getStrategyID() {
		return strategyID;
	}

	public void setStrategyID(Long strategyID) {
		this.strategyID = strategyID;
	}

	public Double getRisknumber() {
		return risknumber;
	}

	public void setRisknumber(Double risknumber) {
		this.risknumber = risknumber;
	}

	public Integer getMaxOfRiskyAsset() {
		return maxOfRiskyAsset;
	}

	public void setMaxOfRiskyAsset(Integer maxOfRiskyAsset) {
		this.maxOfRiskyAsset = maxOfRiskyAsset;
	}

	public Integer getNumberOfMainRiskyClass() {
		return numberOfMainRiskyClass;
	}

	public void setNumberOfMainRiskyClass(Integer numberOfMainRiskyClass) {
		this.numberOfMainRiskyClass = numberOfMainRiskyClass;
	}

	public String getPortfolioName() {
		return portfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		this.portfolioName = portfolioName;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getAssetType() {
		return assetType;
	}

	public void setAssetType(int assetType) {
		this.assetType = assetType;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public String getAssetClassName() {
		return assetClassName;
	}

	public void setAssetClassName(String assetClassName) {
		this.assetClassName = assetClassName;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public boolean isUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(boolean updateInfo) {
		this.updateInfo = updateInfo;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getMainAssetClass() {
		return MainAssetClass;
	}

	public void setMainAssetClass(String mainAssetClass) {
		MainAssetClass = mainAssetClass;
	}

	public String getMainAssetClassWeight() {
		return MainAssetClassWeight;
	}

	public void setMainAssetClassWeight(String mainAssetClassWeight) {
		MainAssetClassWeight = mainAssetClassWeight;
	}

	public String getAssetFundString() {
		return AssetFundString;
	}

	public void setAssetFundString(String assetFundString) {
		AssetFundString = assetFundString;
	}

	public String getBuyThreshold() {
		return BuyThreshold;
	}

	public void setBuyThreshold(String buyThreshold) {
		BuyThreshold = buyThreshold;
	}

	public String getSellThreshold() {
		return SellThreshold;
	}

	public void setSellThreshold(String sellThreshold) {
		SellThreshold = sellThreshold;
	}

	public String getSpecifyAssetFund() {
		return SpecifyAssetFund;
	}

	public void setSpecifyAssetFund(String specifyAssetFund) {
		SpecifyAssetFund = specifyAssetFund;
	}

	public String getUseDataObject() {
		return UseDataObject;
	}

	public void setUseDataObject(String useDataObject) {
		UseDataObject = useDataObject;
	}

}
