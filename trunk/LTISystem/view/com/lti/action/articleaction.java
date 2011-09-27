package com.lti.action;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.permission.PortfolioPermissionChecker;
import com.lti.service.AssetClassManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.PlanArticle;
import com.lti.service.bo.PlanScore;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.SecurityMPT;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.CreateLineChart;
import com.lti.util.LTIDate;
import com.lti.util.LTIDownLoader;
import com.lti.util.PersistentUtil;
import com.lti.util.SecurityUtil;
import com.lti.util.TimeSeriesChartGenerator;
import com.lti.util.TimeSeriesChartGenerator2;
import com.lti.util.html.ParseHtmlTable;

import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

public class articleaction extends TemplateAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long ID;
	private Long reportID;
	private String ticker;
	private Strategy plan;
	private Long compareID;
	private String title;
	private Map<String, List<String>> planMajorAssets;

	private Map<String, List<String>> planMinorAssets;
	
	private Map<String,List<String>> planMinorFunds;
	
	private Map<String,List<String>> compareMinorFunds;
	
	private List<VariableFor401k> compareCandidateFunds;

	private Map<String, List<String>> comparePlanMajorAssets;
	
	private Map<String,List<String>> comparePlanMinorAssets;

	private Map<String,List<String>> compareMajorAssetsSta;
	
	private Map<String,List<String>> comparePlanSta;
	
	private PlanScore comparePlanScore;

	private Map<String, List<String>> majorAssetsSta;

	private PlanScore planScore;

	private Strategy sibPlan;

	private List<CachePortfolioItem> performanceItems;

	private Portfolio portfolio;

	private Map<String, List<String>> portfolioMajorAssets;

	private List<VariableFor401k> candidateFunds;

	private List<String> etfs;
	
	private List<String> mutualFundList;
	
	private List<String> etfsList;

	private List<Portfolio> portfolios;

	private SecurityManager securityManager;

	private StrategyManager strategyManager;

	private PortfolioManager portfolioManager;

	private List<String> topMajorAssets;

	private List<String> planTopMajorAssets;

	public String plan() throws Exception {
		strategyManager = ContextHolder.getStrategyManager();
		securityManager = ContextHolder.getSecurityManager();
		portfolioManager = ContextHolder.getPortfolioManager();
		etfsList = new ArrayList<String>();
		mutualFundList = new ArrayList<String>();
		
		
		etfs = new ArrayList<String>();
		if (reportID != null ) {
			PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
			Portfolio por = portfolioManager.get(reportID);			
			
			if (por == null)
				return Action.SUCCESS;
			rankTable = new HashMap<String, List<String>>();
			Long stragetyID = por.getStrategies().getAssetAllocationStrategy().getID();
			if (stragetyID != null) {
				if (stragetyID == 1003L) {
					isTAA = true;
				} else if (stragetyID == 771L) {
					isSAA = true;
				}
			}
			rankTable = buildRankTable(reportID);
			String planIDStr = por.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID");
			ID = Long.parseLong(planIDStr);
			plan = strategyManager.get(ID);
			if(plan == null) return Action.SUCCESS;
			ticker = plan.getTicker();
		} else if (ID == null) {
			plan = strategyManager.get(ticker);
			ID = plan.getID();
		} else {
			    plan = strategyManager.get(ID);
			     if (plan == null) {
                   return Action.SUCCESS;
			      }
			ticker = plan.getTicker();
		}
		
		if (ticker != null) {
			etfs.add(ticker);
		}

		candidateFunds = plan.getVariablesFor401k();
		if (candidateFunds == null || candidateFunds.isEmpty()) {
			return Action.SUCCESS;
		}
		
		Date date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("MMddyyyy");
		title = plan.getName();
		if (title.contains("&")) {
			title = title.replace("&", "");
		}
		title = title + " Report On " + dateformat.format(date);

		assetNames = buildAssetNames(candidateFunds);

		planMinorFunds = generateMinorFunds(candidateFunds);
		planMajorAssets = generatePlanMajorAssets(assetNames);
		planMinorAssets = generatePlanMinorAssets(assetNames);
		majorAssetsSta = generateMajorAssetsSta(ID);

		planScore = strategyManager.getPlanScoreByPlanID(ID);

		List<String> sibsclass = buildSIBInfor(planMajorAssets);
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		portfolios = portfolioManager.getModelPortfolios(ID);
		Iterator<Portfolio> iter = portfolios.iterator();
		while (iter.hasNext()) {
			Portfolio p = iter.next();
			if (!p.getName().toLowerCase().contains("tactical asset allocation moderate") && !p.getName().toLowerCase().contains("strategic asset allocation moderate") && !p.getName().toLowerCase().contains("tactical_asset_allocation_moderate") && !p.getName().toLowerCase().contains("strategic_asset_allocation_moderate")) {
				iter.remove();
			} else {
				if (p.getName().toLowerCase().contains("tactical asset allocation moderate") || p.getName().toLowerCase().contains("tactical_asset_allocation_moderate"))
					portfolio = p;
			}
		}

		long splanid = 0;
		if (compareID != null) {
			splanid = compareID;
		} else if (sibsclass != null && sibsclass.size() >= 4) {
			splanid = Long.parseLong(sibsclass.get(1));
		}
		if (splanid != 0) {
			List<Portfolio> comparePortfolios = portfolioManager.getModelPortfolios(splanid);
			Iterator<Portfolio> iterator = comparePortfolios.iterator();
			while (iterator.hasNext()) {
				Portfolio port = iterator.next();
				if (port.getName().toLowerCase().contains("tactical asset allocation moderate") || port.getName().toLowerCase().contains("tactical_asset_allocation_moderate")) {
					Portfolio p1 = port;
					portfolios.add(p1);
				}
				if (port.getName().toLowerCase().contains("strategic asset allocation moderate") || port.getName().toLowerCase().contains("strategic_asset_allocation_moderate")) {
					Portfolio p2 = port;
					portfolios.add(p2);
				}
			}
			sibPlan = strategyManager.get(splanid);
			comparePlanScore =strategyManager.getPlanScoreByPlanID(splanid);

		}

		performanceItems = buildPerformanceItems(portfolios);
		portfolioMajorAssets = buildPortfolioMajorAssets(portfolio);

		if (splanid != 0 && sibPlan != null) {
			compareCandidateFunds = sibPlan.getVariablesFor401k();
			if (compareCandidateFunds == null || compareCandidateFunds.isEmpty()) {
				return Action.SUCCESS;
			}
			compareAssetNames = buildAssetNames(compareCandidateFunds);
			comparePlanMajorAssets = generatePlanMajorAssets(compareAssetNames);
			comparePlanMinorAssets = generatePlanMinorAssets(compareAssetNames);
			comparePlanSta = generateMajorAssetsSta(splanid);
			compareMinorFunds = generateMinorFunds(compareCandidateFunds);
		}

		for(String s:etfsList){
			etfs.add(s);
		}
		
		for(String ss: mutualFundList){
			etfs.add(ss);
		}
		
		if(majorAssetsSta!=null && comparePlanSta!=null){
			compareMajorAssetsSta = generateCompareAssetSta(majorAssetsSta,comparePlanSta);
		}
		
		
		
		topMajorAssets = new ArrayList<String>();
		planTopMajorAssets = new ArrayList<String>();
		Object rank = PersistentUtil.readGlobalObject("AssetMomentumRank_19543");
		//Object rank = PersistentUtil.readGlobalObject("AssetMomentumRank_20524");
		Object objPlan = PersistentUtil.readGlobalObject("SAA_Data_Plan_" + plan.getID());
		if (rank != null) {
			String[] assetsRank = (String[]) rank;
			for (int i = 0; i < 3; i++) {
				topMajorAssets.add(assetsRank[i].toLowerCase());
			}
		}
		if (objPlan != null) {
			Map<String, List<String>> saaDataPlan = (Map<String, List<String>>) objPlan;
			Set<String> lists = saaDataPlan.keySet();
			for (String ss : lists) {
				String[] str = ss.split("\\.");
				str[0] = str[0].toLowerCase();
				if (topMajorAssets.contains(str[0]) && !planTopMajorAssets.contains(str[0])) {
					planTopMajorAssets.add(str[0]);
				}
			}
		}

		
		Object tabledata = PersistentUtil.readGlobalObject("MajorAssetReturnTable.datas");
		returntable = (String[][]) tabledata;
		return Action.SUCCESS;
	}

	private List<String> assetNames;
	private List<String> compareAssetNames;

	private List<String> buildAssetNames(List<VariableFor401k> candidateFunds) {
		List<String> assetnames = new ArrayList<String>();
		securityManager = ContextHolder.getSecurityManager();
		if (candidateFunds == null) {
			return null;
		}
		for (VariableFor401k var : candidateFunds) {
			Security se = securityManager.getBySymbol(var.getSymbol());
			if (se != null) {
				String assetName = se.getAssetClass().getName().toLowerCase();
				if (!assetnames.contains(assetName))
					assetnames.add(assetName);
			}
		}
		return assetnames;
	}

	private Map<String,List<String>> generateMinorFunds(List<VariableFor401k> candidateFunds){
		Map<String,List<String>> map = new HashMap<String, List<String>>();
		for(VariableFor401k var: candidateFunds){
			if(!var.getSymbol().equalsIgnoreCase("unknow") && !var.getSymbol().equalsIgnoreCase("noticker")&& !var.getSymbol().equalsIgnoreCase("notfound")){
				Security se = securityManager.getBySymbol(var.getSymbol());
				if(se != null && se.getAssetClass()!= null){
					String assetName = se.getAssetClass().getName();
					if(assetName != null){
						if(map.get(assetName)==null){
							List<String> list = new ArrayList<String>();
							list.add(var.getSymbol());
							map.put(assetName, list);
						}else{
							map.get(assetName).add(var.getSymbol());
						}
						
						
						if(var.getSymbol().length()==5){
							if(!mutualFundList.contains(var.getSymbol())){
								mutualFundList.add(var.getSymbol());
							}
							
						}else{
							if(!etfsList.contains(var.getSymbol())){
								etfsList.add(var.getSymbol());
							}
						}
					}
				}
			}
			
		}
		return map;
	}
	
	private Map<String, List<String>> generatePlanMajorAssets(List<String> assetNames) throws IOException {
		Map<String, List<String>> assets = new HashMap<String, List<String>>();
		Map<String, String> asset_class_map = build_asset_class_map();
		for (String assetName : assetNames) {
			String major = asset_class_map.get(assetName.trim().toLowerCase());
			if (major != null) {
				if (!major.equals("Other") && assets.get(major) == null) {
					assets.put(major.toLowerCase(), new ArrayList<String>());
				}
			}// end if marjor != null
		}// end for

		buildMajorAssets(assets);

		return assets;
	}

	private void buildMajorAssets(Map<String, List<String>> assets) throws IOException {
		File majorFile = new File(URLDecoder.decode(com.lti.action.articleaction.class.getResource("ETFMajorAssets.csv").getFile(), "utf-8"));
		CsvListReader clr = new CsvListReader(new FileReader(majorFile), CsvPreference.STANDARD_PREFERENCE);
		List<String> csvlist = null;
		while ((csvlist = clr.read()) != null) {
			String major = csvlist.get(0).toLowerCase();
			List<String> etflist = assets.get(major);
			if (etflist != null) {
				for (int j = 1; j < csvlist.size(); j++) {
					if (!csvlist.get(j).equals("")) {
						etflist.add(csvlist.get(j));
						
					}// end if !csvlist.get(j).equals("")
				}
			}// end if etflist != null
		}// end if while

	}
    
	private Map<String,List<String>> generateCompareAssetSta(Map<String,List<String>> Map1,Map<String,List<String>> Map2){
		Map<String,List<String>> map = new HashMap<String, List<String>>();
		if(Map1.get("US Equity").size()!=0 ||Map2.get("US Equity").size()!=0){
			List<String> list = new ArrayList<String>();
			list.add(String.valueOf(Map1.get("US Equity").size()));
			list.add(String.valueOf(Map2.get("US Equity").size()));
			map.put("US Equity", list);
		}
		if(Map1.get("Foreign Equity").size()!=0 ||Map2.get("Foreign Equity").size()!=0){
			List<String> list = new ArrayList<String>();
			list.add(String.valueOf(Map1.get("Foreign Equity").size()));
			list.add(String.valueOf(Map2.get("Foreign Equity").size()));
			map.put("Foreign Equity", list);
		}
		if(Map1.get("Balanced Fund").size()!=0 ||Map2.get("Balanced Fund").size()!=0){
			List<String> list = new ArrayList<String>();
			list.add(String.valueOf(Map1.get("Balanced Fund").size()));
			list.add(String.valueOf(Map2.get("Balanced Fund").size()));
			map.put("Balanced Fund", list);
		}
		if(Map1.get("Fixed Income").size()!=0 ||Map2.get("Fixed Income").size()!=0){
			List<String> list = new ArrayList<String>();
			list.add(String.valueOf(Map1.get("Fixed Income").size()));
			list.add(String.valueOf(Map2.get("Fixed Income").size()));
			map.put("Fixed Income", list);
		}
		if(Map1.get("Emerging Market Equity").size()!=0 ||Map2.get("Emerging Market Equity").size()!=0){
			List<String> list = new ArrayList<String>();
			list.add(String.valueOf(Map1.get("Emerging Market Equity").size()));
			list.add(String.valueOf(Map2.get("Emerging Market Equity").size()));
			map.put("Emerging Market Equity", list);
		}
		if(Map1.get("Commodity").size()!=0 ||Map2.get("Commodity").size()!=0){
			List<String> list = new ArrayList<String>();
			list.add(String.valueOf(Map1.get("Commodity").size()));
			list.add(String.valueOf(Map2.get("Commodity").size()));
			map.put("Commodity", list);
		}
		if(Map1.get("REITs").size()!=0 ||Map2.get("REITs").size()!=0){
			List<String> list = new ArrayList<String>();
			list.add(String.valueOf(Map1.get("REITs").size()));
			list.add(String.valueOf(Map2.get("REITs").size()));
			map.put("REITs", list);
		}
		
		if(Map1.get("Sector Fund")!=null ||Map2.get("Sector Fund")!=null){
			List<String> list = new ArrayList<String>();
			if(Map1.get("Sector Fund")!=null){
				list.add(String.valueOf(Map1.get("Sector Fund").size()));
			}
			else{
				list.add("0");
			}
			if(Map2.get("Sector Fund")!=null){
				list.add(String.valueOf(Map2.get("Sector Fund").size()));
			}
			else{
				list.add("0");
			}
			map.put("Sector Fund", list);
		}
		if(Map1.get("Other")!=null ||Map2.get("Other")!=null){
			List<String> list = new ArrayList<String>();
			if(Map1.get("Other")!=null){
				list.add(String.valueOf(Map1.get("Other").size()));
			}
			else{
				list.add("0");
			}
			if(Map2.get("Other")!=null){
				list.add(String.valueOf(Map2.get("Other").size()));
			}
			else{
				list.add("0");
			}
			map.put("Other", list);
		}
		return map;
	}
	
	private List<String> tickerList;
	private boolean containOther;
	private Map<String, List<String>> generateMajorAssetsSta(Long ID) throws Exception {
		AssetClassManager assetclassmanager = ContextHolder.getAssetClassManager();
		strategyManager = ContextHolder.getStrategyManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		tickerList = strategyManager.getPlanTickers(ID);
		if (tickerList != null) {
			List<String> assetsList = strategyManager.getPlanMinorAssets(tickerList);
			List<String> majorList = strategyManager.getPlanMajorAssets(assetsList);
			Map<String, String> asset_class_map = build_asset_class_map();
			for (String s : majorList) {
				List<String> tmpList = new ArrayList<String>();
				map.put(s, tmpList);
			}
			for (String s : tickerList) {
				Security se = securityManager.getBySymbol(s);
				if (se != null && se.getAssetClass() != null) {
					String assetName = se.getAssetClass().getName();
					String str = asset_class_map.get(assetName.trim().toLowerCase());
					if(str==null){
						if(map.get("Other")==null){
							List<String> list = new ArrayList<String>();
							list.add(s);
							map.put("Other", list);
						}else{
							map.get("Other").add(s);
						}
					}else{
						map.get(str).add(s);	
					}					
				}
				else{
					if(map.get("Other")==null){
						List<String> list = new ArrayList<String>();
						list.add(s);
						map.put("Other", list);
					}else{
						map.get("Other").add(s);
					}
				}
			}
			List<String> usEquList = map.get("US Equity");
			if (usEquList != null) {
				List<String> lists = new ArrayList<String>();
				for (String s : usEquList) {
					Security se = securityManager.getBySymbol(s);
					if (se != null && se.getAssetClass() != null) {
						String assetName = se.getAssetClass().getName();
						if (assetclassmanager.isUpperOrSameClass("BALANCE FUND", assetName)) {
							lists.add(s);
						}
					}

				}
				for (String ss : lists) {
					int num = map.get("US Equity").indexOf(ss);
					map.get("US Equity").remove(num);
				}
				map.put("Balanced Fund", lists);
			}
            
			List<String> setorEquList = map.get("Other");
			if (setorEquList != null) {
				List<String> lists = new ArrayList<String>();
				for (String s : setorEquList) {
					Security se = securityManager.getBySymbol(s);
					if (se != null && se.getAssetClass() != null) {
						String assetName = se.getAssetClass().getName();
						if (assetclassmanager.isUpperOrSameClass("SECTOR EQUITY", assetName)) {
							lists.add(s);
						}
					}

				}
				for (String ss : lists) {
					int num = map.get("Other").indexOf(ss);
					map.get("Other").remove(num);
				}
				map.put("Sector Fund", lists);
			}
			
			Set<String> set = map.keySet();
			if (set.contains("Other"))
					containOther = true;
			else
				    containOther = false;
			if (!set.contains("US Equity"))
				map.put("US Equity", new ArrayList<String>());
			if (!set.contains("Foreign Equity"))
				map.put("Foreign Equity", new ArrayList<String>());
			if (!set.contains("Balanced Fund"))
				map.put("Balanced Fund", new ArrayList<String>());
			if (!set.contains("Fixed Income"))
				map.put("Fixed Income", new ArrayList<String>());
			if (!set.contains("Emerging Market Equity"))
				map.put("Emerging Market Equity", new ArrayList<String>());
			if (!set.contains("Commodity"))
				map.put("Commodity", new ArrayList<String>());
			if (!set.contains("REITs"))
				map.put("REITs", new ArrayList<String>());			

		}

		return map;
	}

	/* generate the MinorAssets and it's Etfs */

	private Map<String, List<String>> generatePlanMinorAssets(List<String> assetNames) throws IOException {

		Map<String, List<String>> minorAssets = new HashMap<String, List<String>>();

		File etfFile = new File(URLDecoder.decode(com.lti.action.articleaction.class.getResource("ETFs_of_Each_Asset_Class.csv").getFile(), "utf-8"));
		CsvListReader clr = new CsvListReader(new FileReader(etfFile), CsvPreference.STANDARD_PREFERENCE);

		clr.read();

		List<String> list = null;
		while ((list = clr.read()) != null) {
			String assetname = list.get(1).toLowerCase();

			if (assetname.equalsIgnoreCase("ROOT"))
				continue;

			if (assetNames.contains(assetname.toLowerCase())) {
				List<String> tmplist = new ArrayList<String>();
				for (int i = 2; i < list.size() - 1; i++) {
					if (!list.get(i).equals("")) {
						String[] strs = list.get(i).split(",");
						for (int k = 0; k < strs.length; k++) {
							tmplist.add(strs[k]);
							
						}
					}
				}
				minorAssets.put(assetname, tmplist);
			}
		}
		return minorAssets;
	}

	/* build the current holding portfolio funds of the majorassets */

	public Map<String, List<String>> buildPortfolioMajorAssets(Portfolio p) throws IOException {
		Map<String, List<String>> assets = new HashMap<String, List<String>>();
		if (portfolio != null) {
			PortfolioInf pinf = portfolioManager.getPortfolioInf(p.getID(), Configuration.PORTFOLIO_HOLDING_CURRENT);
			Map<String, String> asset_class_map = build_asset_class_map();
			if (pinf != null && pinf.getHolding() != null) {
				for (HoldingItem hi : pinf.getHolding().getHoldingItems()) {
					Security se = securityManager.get(hi.getSecurityID());
					String assetName = se.getAssetClass().getName().toLowerCase();

					String major = asset_class_map.get(assetName).toLowerCase();
					if (major != null && assets.get(major) == null) {
						if (!major.equals("other")) {
							assets.put(major, new ArrayList<String>());
						}

					}
				}
			}
		}
		buildMajorAssets(assets);
		return assets;
	}

	/* build the minorassets and it majorassets */

	private Map<String, String> build_asset_class_map() throws IOException {
		Map<String, String> assetclass = new HashMap<String, String>();
		File csvFile = new File(URLDecoder.decode(com.lti.action.articleaction.class.getResource("6_main_asset_classes.csv").getFile(), "utf-8"));
		CsvListReader clr = new CsvListReader(new FileReader(csvFile), CsvPreference.STANDARD_PREFERENCE);
		List<String> head = clr.read();
		String[] arrs = new String[head.size()];
		head.toArray(arrs);
		List<String> list = null;
		while ((list = clr.read()) != null) {
			for (int i = 0; i < 7; i++) {
				String cell = list.get(i).trim().toLowerCase();
				if (!cell.equals("")) {
					assetclass.put(cell, arrs[i]);
				}
			}

		}

		return assetclass;
	}

	/* build the compare lines */
	public List<String> buildSIBInfor(Map<String, List<String>> majorAssets) throws IOException {
		File sibFile = new File(URLDecoder.decode(com.lti.action.articleaction.class.getResource("SIBs.csv").getFile(), "utf-8"));
		CsvListReader sibs = new CsvListReader(new FileReader(sibFile), CsvPreference.STANDARD_PREFERENCE);
		List<String> cmplist = sibs.read();

		List<String> sibhead = new ArrayList<String>();
		for (int i = 4; i < 10; i++) {
			sibhead.add(cmplist.get(i));
		}

		String cmp1 = "";

		Iterator<String> iterator = sibhead.iterator();
		while (iterator.hasNext()) {
			String s = iterator.next().toLowerCase();
			if (majorAssets.get(s) != null)
				cmp1 = cmp1 + "1";
			else
				cmp1 = cmp1 + "0";
		}

		while ((cmplist = sibs.read()) != null) {
			Iterator<String> iterator2 = cmplist.iterator();
			String cmp2 = "";
			while (iterator2.hasNext()) {

				String s2 = iterator2.next();
				if (s2.equals("1") || s2.equals("0"))
					cmp2 = cmp2 + s2;
			}
			if (cmp1.equals(cmp2))
				break;
		}
		return cmplist;
	}

	/* build the table */

	public List<CachePortfolioItem> buildPerformanceItems(List<Portfolio> portfolios) throws IOException {
		List<CachePortfolioItem> portfolioItem = new ArrayList<CachePortfolioItem>();
		for (int i = 0; i < portfolios.size(); i++) {
			Portfolio portfolio = portfolios.get(i);
			List<CachePortfolioItem> pitems = strategyManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + portfolio.getID() + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_REALTIME_ID);
			if (pitems != null && pitems.size() > 0) {
				portfolioItem.add(pitems.get(0));
			} else {
				CachePortfolioItem cpi = new CachePortfolioItem();
				cpi.setPortfolioID(portfolio.getID());
				cpi.setPortfolioName(portfolio.getSymbol());
				portfolioItem.add(pitems.get(0));
			}
		}
		return portfolioItem;
	}

	private InputStream fis;
	private String portfolioID;
	private String securityID;
	private int width = 640;
	private int height = 480;
	private int type = 0;
	private int yearNum=0;
	private int monthNum=0;
	
	public String viewchart() throws Exception {
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		List<List> lists = new ArrayList<List>();
		List<String> titles = new ArrayList<String>();
		if (portfolioID != null&&!portfolioID.equals("")) {
			List<Long> ids = new ArrayList<Long>();
			String[] str = portfolioID.split(",");
			for (String s : str) {
				ids.add(Long.parseLong(s));
			}
			List<Portfolio> portfolioList = new ArrayList<Portfolio>();
			for (Long id : ids) {
				Portfolio p = portfolioManager.get(id);
				portfolioList.add(p);
			}
			for (Portfolio p : portfolioList) {
				if (type == 1) {
					Date delayDate = LTIDate.getHoldingDateMonthEnd(p.getEndDate());
					List<PortfolioDailyData> pdd = portfolioManager.getDailydatas(p.getID());
					List<PortfolioDailyData> pdds = new ArrayList<PortfolioDailyData>();
					for (PortfolioDailyData pd : pdd) {
						if (pd.getDate().before(delayDate) || LTIDate.equals(pd.getDate(), delayDate)) {
							pdds.add(pd);
						}
					}
					lists.add(pdds);
				} else if (type == 0) {
					List<PortfolioDailyData> pdds = portfolioManager.getDailydatas(p.getID());
					lists.add(pdds);
				}

				titles.add(p.getName());
			}
		}
		if (securityID != null&&!securityID.equals("")) {
			List<Long> ids = new ArrayList<Long>();
			String[] str = securityID.split(",");
			for (String s : str) {
				ids.add(Long.parseLong(s));
			}
			List<Security> securityList = new ArrayList<Security>();
			for (Long id : ids) {
				Security s = securityManager.get(id);
				securityList.add(s);
			}
			for (Security s : securityList) {
				if (type == 1) {
					Date delayDate = LTIDate.getHoldingDateMonthEnd(s.getEndDate());
					List<SecurityDailyData> sdd = securityManager.getDailydatas(s.getID());
					List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
					for (SecurityDailyData sd : sdd) {
						if (sd.getDate().before(delayDate) || LTIDate.equals(sd.getDate(), delayDate)) {
							sdds.add(sd);
						}
					}
					lists.add(sdds);
				} else if (type == 0) {
					List<SecurityDailyData> sdds = securityManager.getDailydatas(s.getID());
					lists.add(sdds);
				}
				titles.add(s.getName());
			}
		}

		TimeSeriesChartGenerator generator = new TimeSeriesChartGenerator();
		bytes = generator.getLineXYChart("Portfolio Comparison", "From MyPlanIQ", "Date", "Amount", width, height, titles, lists);

		fis = new ByteArrayInputStream(bytes);
		return Action.IMAGE;
	}
	private byte[] bytes;
	
	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}

	public String viewchart2() throws Exception {
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		List<List> lists = new ArrayList<List>();
		List<String> titles = new ArrayList<String>();
		if (portfolioID != null&&!portfolioID.equals("")) {
			List<Long> ids = new ArrayList<Long>();
			String[] str = portfolioID.split(",");
			for (String s : str) {
				ids.add(Long.parseLong(s));
			}
			List<Portfolio> portfolioList = new ArrayList<Portfolio>();
			for (Long id : ids) {
				Portfolio p = portfolioManager.get(id);
				portfolioList.add(p);
			}
			for (Portfolio p : portfolioList) {
				if (type == 1) {
					Date delayDate = LTIDate.getHoldingDateMonthEnd(p.getEndDate());
					List<PortfolioDailyData> pdd = portfolioManager.getDailydatas(p.getID());
					List<PortfolioDailyData> pdds = new ArrayList<PortfolioDailyData>();
					for (PortfolioDailyData pd : pdd) {
						if (pd.getDate().before(delayDate) || LTIDate.equals(pd.getDate(), delayDate)) {
							pdds.add(pd);
						}
					}
					lists.add(pdds);
				} else if (type == 0&&yearNum==0&&monthNum==0) {
					List<PortfolioDailyData> pdds = portfolioManager.getDailydatas(p.getID());
					lists.add(pdds);
				}else if(type==0 && monthNum==3){
					List<PortfolioDailyData> pdd = portfolioManager.getDailydatas(p.getID());
					Date startDate = LTIDate.getNewNYSEMonthForward(p.getEndDate(), -3);
					List<PortfolioDailyData> pdds = new ArrayList<PortfolioDailyData>();
					for (PortfolioDailyData pd : pdd) {
						if (pd.getDate().after(startDate) || LTIDate.equals(pd.getDate(), startDate)) {
							pdds.add(pd);
						}
					}
					lists.add(pdds);
				}else if(type==0 && yearNum==1){
					System.out.print(monthNum);
					List<PortfolioDailyData> pdd = portfolioManager.getDailydatas(p.getID());
					Date startDate = LTIDate.getNewNYSEYearForward(p.getEndDate(), -1);
					List<PortfolioDailyData> pdds = new ArrayList<PortfolioDailyData>();
					for (PortfolioDailyData pd : pdd) {
						if (pd.getDate().after(startDate) || LTIDate.equals(pd.getDate(), startDate)) {
							pdds.add(pd);
						}
					}
					lists.add(pdds);
				}else if(type==0 && yearNum==3){
					List<PortfolioDailyData> pdd = portfolioManager.getDailydatas(p.getID());
					Date startDate = LTIDate.getNewNYSEYearForward(p.getEndDate(), -3);
					List<PortfolioDailyData> pdds = new ArrayList<PortfolioDailyData>();
					for (PortfolioDailyData pd : pdd) {
						if (pd.getDate().after(startDate) || LTIDate.equals(pd.getDate(), startDate)) {
							pdds.add(pd);
						}
					}
					lists.add(pdds);
				}else if(type==0 && yearNum==5){
					List<PortfolioDailyData> pdd = portfolioManager.getDailydatas(p.getID());
					Date startDate = LTIDate.getNewNYSEYearForward(p.getEndDate(), -5);
					List<PortfolioDailyData> pdds = new ArrayList<PortfolioDailyData>();
					for (PortfolioDailyData pd : pdd) {
						if (pd.getDate().after(startDate) || LTIDate.equals(pd.getDate(), startDate)) {
							pdds.add(pd);
						}
					}
					lists.add(pdds);
				}

				titles.add(p.getName());
			}
		}
		if (securityID != null&&!securityID.equals("")) {
			List<Long> ids = new ArrayList<Long>();
			String[] str = securityID.split(",");
			for (String s : str) {
				ids.add(Long.parseLong(s));
			}
			List<Security> securityList = new ArrayList<Security>();
			for (Long id : ids) {
				Security s = securityManager.get(id);
				securityList.add(s);
			}
			for (Security s : securityList) {
				if (type == 1) {
					Date delayDate = LTIDate.getHoldingDateMonthEnd(s.getEndDate());
					List<SecurityDailyData> sdd = securityManager.getDailydatas(s.getID());
					List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
					for (SecurityDailyData sd : sdd) {
						if (sd.getDate().before(delayDate) || LTIDate.equals(sd.getDate(), delayDate)) {
							sdds.add(sd);
						}
					}
					lists.add(sdds);
				} else if (type == 0 && yearNum==0 && monthNum==0) {
					List<SecurityDailyData> sdds = securityManager.getDailydatas(s.getID());
					lists.add(sdds);
				}else if(type==0&&monthNum==3){
					List<SecurityDailyData> sdd = securityManager.getDailydatas(s.getID());
					List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
					Date startDate = LTIDate.getNewNYSEMonthForward(s.getEndDate(), -3);
					for (SecurityDailyData sd : sdd) {
						if (sd.getDate().after(startDate) || LTIDate.equals(sd.getDate(), startDate)) {
							sdds.add(sd);
						}
					}
					lists.add(sdds);
				}else if(type==0&&yearNum==1){
					List<SecurityDailyData> sdd = securityManager.getDailydatas(s.getID());
					List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
					Date startDate = LTIDate.getNewNYSEYearForward(s.getEndDate(), -1);
					for (SecurityDailyData sd : sdd) {
						if (sd.getDate().after(startDate) || LTIDate.equals(sd.getDate(), startDate)) {
							sdds.add(sd);
						}
					}
					lists.add(sdds);
				}else if(type==0&&yearNum==3){
					List<SecurityDailyData> sdd = securityManager.getDailydatas(s.getID());
					List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
					Date startDate = LTIDate.getNewNYSEYearForward(s.getEndDate(), -3);
					for (SecurityDailyData sd : sdd) {
						if (sd.getDate().after(startDate) || LTIDate.equals(sd.getDate(), startDate)) {
							sdds.add(sd);
						}
					}
					lists.add(sdds);
				}else if(type==0&&yearNum==5){
					List<SecurityDailyData> sdd = securityManager.getDailydatas(s.getID());
					List<SecurityDailyData> sdds = new ArrayList<SecurityDailyData>();
					Date startDate = LTIDate.getNewNYSEYearForward(s.getEndDate(), -5);
					for (SecurityDailyData sd : sdd) {
						if (sd.getDate().after(startDate) || LTIDate.equals(sd.getDate(), startDate)) {
							sdds.add(sd);
						}
					}
					lists.add(sdds);
				}
				titles.add(s.getName());
			}
		}

		TimeSeriesChartGenerator2 generator = new TimeSeriesChartGenerator2();
		bytes = generator.getLineXYChart("Portfolio Comparison", "From MyPlanIQ", "Date", "Amount", width, height, titles, lists);

		fis = new ByteArrayInputStream(bytes);
		return Action.IMAGE;
	}
	
	private String fileName;
	private String templateName;
	private String idsOrTickers;
	private String symbols;
	private String info;
	private File uploadFile;
	private String uploadFileFileName;
	private List<String> newArticleList;

	/*
	 * *************************************************** batch generator
	 */
	public String generate() throws Exception {
		Map<String, Object> dataMap = new HashMap<String, Object>();
		strategyManager = ContextHolder.getStrategyManager();
		freemarker.template.Configuration cfg = new freemarker.template.Configuration();
		cfg.setDirectoryForTemplateLoading(new File(ContextHolder.getServletPath() + "/jsp/article"));
		cfg.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
		if (templateName != null) {
			Template template = cfg.getTemplate(templateName);
			List<PlanArticle> existList = new ArrayList<PlanArticle>();
			newArticleList = new ArrayList<String>();

			if (uploadFile != null) {
				if (uploadFileFileName.endsWith(".csv")) {
					LTIDownLoader dl = new LTIDownLoader();
					String filePath = dl.systemPath;
					InputStream stream = new FileInputStream(uploadFile);
					OutputStream bos = new FileOutputStream(filePath + uploadFile.getName());
					int bytesRead = 0;
					byte[] buffer = new byte[8192];
					while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
						bos.write(buffer, 0, bytesRead);
					}
					bos.close();
					stream.close();
					File file = new File(filePath + uploadFile.getName());
					CsvListReader csv = new CsvListReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE);
					List<String> list = null;
					csv.read();
					while ((list = csv.read()) != null) {
						idsOrTickers = idsOrTickers + list.get(0) + ",";
					}
				}

			}
			info = "";
			if (idsOrTickers != null) {
				idsOrTickers = idsOrTickers.trim();
				String[] str = idsOrTickers.split(",");
				for (String s : str) {
					try {
						if (s.matches("[0-9].*[0-9]")) {
							ID = Long.parseLong(s);
							plan = strategyManager.get(ID);
							ticker = plan.getTicker();
						} else {
							if (!s.equals("")) {
								ticker = s;
								plan = strategyManager.get(ticker);
								ID = plan.getID();
							}
						}
						fileName = plan.getName();
						if (fileName.contains("&")) {
							fileName = fileName.replace("&", "");
						}
						Date date = new Date();
						SimpleDateFormat dateformat = new SimpleDateFormat("MMddyyyy");
						fileName = fileName + " Report On " + dateformat.format(date);
						plan();
					} catch (Exception e) {
						info += "The Plan " + s + " failed to generate article\n";
						continue;
					}

					if (!newArticleList.contains(fileName)) {
						newArticleList.add(fileName);
					}
					existList = strategyManager.getPlanArticleByTitle(fileName);

					symbols = "";
					if (etfs != null) {
						for (String ss : etfs) {
							if (!symbols.equals("")) {
								symbols += ",";
							}
							symbols += ss;
						}
						symbols += ",";
						symbols += ticker;
					}
					dataMap.put("plan", plan);
					dataMap.put("planScore", planScore);
					dataMap.put("sibPlan", sibPlan);
					dataMap.put("planMajorAssets", planMajorAssets);
					dataMap.put("planMinorAssets", planMinorAssets);
					dataMap.put("performanceItems", performanceItems);
					dataMap.put("portfolio", portfolio);
					dataMap.put("portfolios", portfolios);
					dataMap.put("portfolioMajorAssets", portfolioMajorAssets);
					dataMap.put("candidateFunds", candidateFunds);
					dataMap.put("etfs", etfs);
					dataMap.put("comparePlanMajorAssets", comparePlanMajorAssets);
					dataMap.put("title", title);
					Writer out = new StringWriter();
					template.process(dataMap, out);
					PlanArticle pa = new PlanArticle();
					if (existList != null) {
						pa = existList.get(0);
						pa.setContent(out.toString());
						pa.setDate(new Date());
						pa.setSymbols(symbols);
						strategyManager.updatePlanArticle(pa);
					} else {
						pa.setContent(out.toString());
						pa.setArticleTitle(fileName);
						pa.setPlanID(ID);
						pa.setDate(new Date());
						pa.setLink("");
						pa.setDisplay(false);
						pa.setSymbols(symbols);
						strategyManager.addPlanArticle(pa);
					}

				}

			}

			/**
			 * *********************generate new article
			 * links***************************************
			 */
			LTIDownLoader dl = new LTIDownLoader();
			Date date = new Date();
			SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd-yyyy");
			String filePath = dl.systemPath + "articleLink On" + dateformat.format(date) + ".csv";
			File reportfile = new File(filePath);
			FileOutputStream fos = new FileOutputStream(reportfile);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			CsvListWriter clw = new CsvListWriter(osw, CsvPreference.STANDARD_PREFERENCE);
			String[] header = { "Article Name", "Links" };
			List<String> head = Arrays.asList(header);
			if (head != null)
				clw.write(head);
			if (newArticleList != null) {
				for (String s : newArticleList) {
					List<String> lists = new ArrayList<String>();
					lists.add(s);
					lists.add("www.myplaniq.com/LTISystem/article_view.action?articleTitle=" + s);
					if (lists != null)
						clw.write(lists);
				}
			} else {
				List<String> lists = new ArrayList<String>();
				lists.add("There was no articles generated");
			}
			clw.close();
			fos.close();
			/** **************************************************************************************** */

		}
		return Action.SUCCESS;
	}

	public String downloadlinks() throws Exception {
		LTIDownLoader dl = new LTIDownLoader();
		Date date = new Date();
		SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd-yyyy");
		String filePath = dl.systemPath + "articleLink On" + dateformat.format(date) + ".csv";
		File reportfile = new File(filePath);
		if (reportfile.exists()) {
			fis = new FileInputStream(reportfile);
			planName = reportfile.getName();
		}
		return Action.DOWNLOAD;
	}

	private boolean usePlanList;

	public String downloadList() throws Exception {
		if (usePlanList) {
			File listFile = new File(URLDecoder.decode(com.lti.action.articleaction.class.getResource("Plan Article Generate List.csv").getFile(), "utf-8"));
			fis = new FileInputStream(listFile);
			planName = listFile.getName();
		} else {
			File listFile = new File(URLDecoder.decode(com.lti.action.articleaction.class.getResource("report.csv").getFile(), "utf-8"));
			fis = new FileInputStream(listFile);
			planName = listFile.getName();
		}

		return Action.DOWNLOAD;
	}

	private String titleStr;

	public String showinPlan() {
		strategyManager = ContextHolder.getStrategyManager();
		StringBuffer buf = new StringBuffer();
		if (titleStr != null) {
			String[] display = titleStr.split("#");
			for (String s : display) {
				if (s.endsWith("_")) {
					int end = s.indexOf("_");
					String subs = s.substring(0, end);
					List<PlanArticle> list = strategyManager.getPlanArticleByTitle(subs);
					if (list != null) {
						for (PlanArticle p : list) {
							p.setDisplay(false);
							buf.append("Update " + p.getArticleTitle() + "\n");
							strategyManager.updatePlanArticle(p);

						}
					}
				} else {
					List<PlanArticle> list = strategyManager.getPlanArticleByTitle(s);
					if (list != null) {
						for (PlanArticle p : list) {
							p.setDisplay(true);
							buf.append("Update " + p.getArticleTitle() + "\n");
							strategyManager.updatePlanArticle(p);

						}
					}
				}

			}
			message = buf.toString();
		}
		return Action.MESSAGE;

	}

	public String delete() {
		strategyManager = ContextHolder.getStrategyManager();
		StringBuffer sb = new StringBuffer();
		if (titleStr != null) {
			String[] display = titleStr.split("#");
			for (String s : display) {
				if (!s.endsWith("_")) {
					strategyManager.removePlanArticle(s);
					sb.append("Delete " + s + "\n");
				}
			}
			message = sb.toString();
		}
		return Action.MESSAGE;
	}

	private String planName;
	private List<PlanArticle> articleList;
    private long articleID;
	public String articleList() {
		strategyManager = ContextHolder.getStrategyManager();
		articleList = new ArrayList<PlanArticle>();
		articleList = strategyManager.getPlanArticles(ID);
		return Action.SUCCESS;
	}

	private PlanArticle articleContent;
	private String articleTitle;

	public String view() {
		strategyManager = ContextHolder.getStrategyManager();
		List<PlanArticle>articleContentList = strategyManager.getPlanArticleByTitle(articleTitle);
		articleContent = articleContentList.get(0);
		return Action.SUCCESS;
	}

	public String edit() {
		strategyManager = ContextHolder.getStrategyManager();
		List<PlanArticle>articleContentList = strategyManager.getPlanArticleByTitle(articleTitle);
		articleContent = articleContentList.get(0);
		return Action.SUCCESS;
	}

	private String content;

	public void save() {
		strategyManager = ContextHolder.getStrategyManager();
		articleContent = strategyManager.getPlanArticleByID(articleID);
            articleContent.setArticleTitle(articleTitle);
		    articleContent.setContent(content);
			strategyManager.updatePlanArticle(articleContent);


	}

	/*
	 * create article of tables
	 */
	private String tableName;
	private String[][] returntable;
	private String[][] majorReturnTable;
	private String[][] usStyleReturnTable;

	public String markettrend() throws Exception {
		Object tabledata = PersistentUtil.readGlobalObject(tableName + ".datas");
		returntable = (String[][]) tabledata;

		return Action.SUCCESS;
	}

	public String toptablechart() throws Exception {
		Object topvalueobj = PersistentUtil.readGlobalObject(tableName + ".topvalues");
		Object toptitleobj = PersistentUtil.readGlobalObject(tableName + ".toptitles");
		Object topdateobj = PersistentUtil.readGlobalObject(tableName + ".topdates");
		CreateLineChart generator = new CreateLineChart();
		double[][] topvalues = (double[][]) topvalueobj;
		String[] toptitles = (String[]) toptitleobj;
		String[][] topdates = (String[][]) topdateobj;
		byte[] bytes = generator.getChart("TOP FUNDS", "From MyPlanIQ", "Time Horizon", "Average Weekly Return", toptitles, topdates, topvalues);
		fis = new ByteArrayInputStream(bytes);
		return Action.IMAGE;
	}

	public String lowtablechart() throws Exception {
		CreateLineChart generator = new CreateLineChart();
		Object lowvalueobj = PersistentUtil.readGlobalObject(tableName + ".lowvalues");
		Object lowtitleobj = PersistentUtil.readGlobalObject(tableName + ".lowtitles");
		Object lowdateobj = PersistentUtil.readGlobalObject(tableName + ".lowdates");
		double[][] lowvalues = (double[][]) lowvalueobj;
		String[] lowtitles = (String[]) lowtitleobj;
		String[][] lowdates = (String[][]) lowdateobj;
		byte[] bytes = generator.getChart("Bottom FUNDS", "From MyPlanIQ", "Time Horizon", "Average Weekly Return", lowtitles, lowdates, lowvalues);
		fis = new ByteArrayInputStream(bytes);
		return Action.IMAGE;
	}
	
	public String etfWatch() throws Exception{
		Object etfTable = PersistentUtil.readGlobalObject(tableName);
		Object majorAssetTable = PersistentUtil.readGlobalObject("MajorAssetReturnTable.datas");
		Object usStyleAssetTable = PersistentUtil.readGlobalObject("USStyleReturnTable.datas");
		returntable = (String[][])etfTable;
		majorReturnTable = (String[][])majorAssetTable;
		usStyleReturnTable = (String[][])usStyleAssetTable;
		return Action.SUCCESS;
	}
	
	private String symbol;
	public String smartMoney() throws Exception{
		Object smartTable = PersistentUtil.readGlobalObject("SmartTable."+symbol);
		returntable = (String[][])smartTable;
		return Action.SUCCESS;
	}

	private long userID = -1l;
	private boolean isAdmin = false;

	public articleaction() {
		userID = ContextHolder.getUserManager().getLoginUser().getID();
		if (userID == Configuration.SUPER_USER_ID) {
			isAdmin = true;
		}
	}

	/**
	 * @return
	 * @throws Exception
	 */
	private User user;
	private Map<String, List<String>> clientInfo;

	public String portfolios() throws Exception {
		if (uploadFile != null) {
			clientInfo = new HashMap<String, List<String>>();
			if (uploadFileFileName.endsWith(".csv")) {
				LTIDownLoader dl = new LTIDownLoader();
				String filePath = dl.systemPath;
				InputStream stream = new FileInputStream(uploadFile);
				OutputStream bos = new FileOutputStream(filePath + uploadFile.getName());
				int bytesRead = 0;
				byte[] buffer = new byte[8192];
				while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
					bos.write(buffer, 0, bytesRead);
				}
				bos.close();
				stream.close();
				File file = new File(filePath + uploadFile.getName());
				CsvListReader csv = new CsvListReader(new FileReader(file), CsvPreference.STANDARD_PREFERENCE);
				List<String> list = new ArrayList<String>();
				csv.read();

				while ((list = csv.read()) != null) {
					String s = list.get(0);
					list.remove(0);
					List<String> tmpList = new ArrayList<String>();
					tmpList.addAll(list);
					clientInfo.put(s, tmpList);
				}
			}

		}

		user = ContextHolder.getUserManager().getLoginUser();

		return Action.SUCCESS;
	}

	private Portfolio rPortfolio;
	private PortfolioPermissionChecker pc;
	private List<HoldingItem> holdingItems;
	private Map<String, List<String>> rankTable;
	private String clientName;
	private String clientAddr;
	private String clientAddrCity;
	private String clientAddrState;
	private String clientAddrCountry;
	private String clientAddrZip;
	private boolean isTAA = false;
	private boolean isSAA = false;

	public String portfolioReport() throws Exception {
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		rPortfolio = portfolioManager.get(ID);
		pc = new PortfolioPermissionChecker(rPortfolio, ServletActionContext.getRequest());
		rankTable = new HashMap<String, List<String>>();
		Long stragetyID = rPortfolio.getStrategies().getAssetAllocationStrategy().getID();
		if (stragetyID != null) {
			if (stragetyID == 1003L) {
				isTAA = true;
			} else if (stragetyID == 771L) {
				isSAA = true;
			}
		}
		rankTable = buildRankTable(ID);
		if (userID != -1l)
			user = ContextHolder.getUserManager().get(userID);
		return Action.SUCCESS;
	}

	public Map<String, List<String>> buildRankTable(long id) throws Exception {
		Map<String, List<String>> table = new HashMap<String, List<String>>();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		Portfolio p = portfolioManager.get(id);
		Long stragetyID = p.getStrategies().getAssetAllocationStrategy().getID();
		String planIDStr = p.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID");
		long planID = Long.parseLong(planIDStr);
		if (stragetyID != null) {
			if (stragetyID == 1003L) {
				isTAA = true;
			} else if (stragetyID == 771L) {
				isSAA = true;
			}
		}

		Object object = new Object();
		if (isTAA) {
			object = PersistentUtil.readGlobalObject("TAA_Data_Plan_" + planID);
		} else if (isSAA) {
			object = PersistentUtil.readGlobalObject("SAA_Data_Plan_" + planID);
		}
		Map<String, List<String>> major_rank = (Map<String, List<String>>) object;
		Set<String> setlist = major_rank.keySet();
		String date = new String();
		Iterator<String> iter = setlist.iterator();
		date = "";
		while (iter.hasNext()) {
			String ss = iter.next();
			String[] string = ss.split("\\.");
			String curdate = string[1];
			int result = curdate.compareTo(date);
			if (result > 0) {
				date = curdate;
			}

		}
		for (String s : setlist) {
			if (s.contains(date) && !s.contains("Stable") && !s.contains("Risky")) {
				List<String> lists = new ArrayList<String>();
				List<String> templist = major_rank.get(s);
				for (int i = 0; i < templist.size(); i++) {
					lists.add(templist.get(i));
					if (i == 1)
						break;
				}
				String[] ss = s.split("\\.");
				String key = ss[0].toLowerCase();
				table.put(key, lists);
			}
		}
		return table;
	}
	
	private String table;
	private String management;
	private String summary;
	private Map<String, CachePortfolioItem> portMap;
	private String fundCompareLink;
	public String fund(){
		strategyManager = ContextHolder.getStrategyManager();
		portfolioManager = ContextHolder.getPortfolioManager();
		securityManager = ContextHolder.getSecurityManager();
		String url = "http://finance.yahoo.com/q/hl?s="+symbol+"+Holdings";
		String html = ParseHtmlTable.getHtml(url);	
		List<String>portfolioName = new ArrayList<String>();
		if(html!=null){
			int start = html.indexOf("id=\"yfncsumtab\"");
			int tempEnd = html.indexOf("Bond Holdings");
			tempEnd = html.indexOf("</table>", tempEnd);
			tempEnd = html.indexOf("</table>", tempEnd+8);
			int end = html.indexOf("</table>", tempEnd+8);
			table = html.substring(start, end);
			table ="<table width=\"580\" "+table;
			table +="</table></td></tr></tbody></table>";
			
			table = table.replaceAll("yfncsumtab", "mpiq_table");
			table = table.replaceAll("yfnc_modtitle1", "mpiq_modtitle");
			table = table.replaceAll("yfnc_mod_table_title1", "mpiq_mod_tabletitle");
			table = table.replaceAll("yfnc_datamodoutline1", "mpiq_datamodoutline");
			table = table.replaceAll("yfnc_datamodlabel1", "mpiq_datalable");
			table = table.replaceAll("yfnc_datamoddata1", "mpiq_datamoddata");
			table = table.replaceAll("yfnc_tableout1", "mpiq_tableout");
			table = table.replaceAll("yfnc_tablehead1", "mpiq_tablehead");
			table = table.replaceAll("yfnc_tabledata1", "mpiq_tabledata");
			table = table.replaceAll("#666666", "#FF4500");
			
			int smallStart = table.indexOf("<small>");
			if(smallStart!=-1){
				int smallEnd = table.indexOf("</td>",smallStart);
				String smallStr = table.substring(smallStart, smallEnd);
				table = table.replace(smallStr, "");
			}
						
			int linkStart = table.indexOf("<b><a");
			if(linkStart!=-1){
				int linkEnd = table.indexOf("</td>",linkStart);
				String linkStr = table.substring(linkStart, linkEnd);
				table = table.replace(linkStr, "");
			}
						
			Pattern p = Pattern.compile("<a href.+?>(.+?)</a>");
			Matcher m = p.matcher(table);
			while(m.find()){
				String str1 = m.group(0);
				String str2 = m.group(1);
				String replaceStr = "<a href='../LTISystem/f401k_symbols.action?symbol="+str2+"'>"+str2+"</a>";
				table = table.replace(str1, replaceStr);
			}
			
		}
		
		String purl = "http://finance.yahoo.com/q/pr?s="+symbol+"+Profile";
		String profileHtml = ParseHtmlTable.getHtml(purl);	
		if(profileHtml!=null){
			int profileStart = profileHtml.indexOf("Management Information");
			profileStart = profileHtml.indexOf("<table",profileStart+1);
			int profileEnd = profileHtml.indexOf("</table>",profileStart);
			profileEnd = profileHtml.indexOf("</table>",profileEnd+8);
			management = profileHtml.substring(profileStart,profileEnd);
			management = management +"</table>";
			
			int summaryStart = profileHtml.indexOf("Fund Summary</span>");
			summaryStart = profileHtml.indexOf("<table",summaryStart);
			int summaryEnd = profileHtml.indexOf("</table>",summaryStart);
			summary = profileHtml.substring(summaryStart, summaryEnd);
			summary +="</table>";
		}
		
		if(portfolioID!=null){
			List<Long> pids = new ArrayList<Long>();
			String []idItem = portfolioID.split(",");
			portMap = new HashMap<String, CachePortfolioItem>();
			for(String s:idItem){
				pids.add(Long.parseLong(s));
			}
			for(int i=0;i<pids.size();i++){
				List<CachePortfolioItem> pitems = null;
				Portfolio portfolio = portfolioManager.get(pids.get(i));
				portfolioName.add(portfolio.getName());
				pitems=strategyManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + pids.get(i) + " and cp.GroupID=0 and cp.RoleID="	+ Configuration.ROLE_PORTFOLIO_REALTIME_ID);
				String surl="<a href=\"/LTISystem/jsp/portfolio/ViewPortfolio.action?ID="+pids.get(i)+"\">"+portfolio.getName()+"</a>";
				if (pitems != null && pitems.size() > 0){
					portMap.put(surl, pitems.get(0));
				}
				else{
					CachePortfolioItem cpi = new CachePortfolioItem();
					cpi.setPortfolioID(pids.get(i));
					cpi.setPortfolioName(portfolio.getSymbol());
					portMap.put(surl, cpi);
				}
			}
			
			Security se = securityManager.getBySymbol(symbol);
			if(se==null)
				return Action.SUCCESS;
			securityID = String.valueOf(se.getID());
			CachePortfolioItem cpi = new CachePortfolioItem();
			cpi.setPortfolioID(se.getID());
			cpi.setPortfolioName(se.getSymbol());
			String surl="<a href=\"/LTISystem/jsp/fundcenter/View.action?type=1&symbol=" + se.getSymbol() + "\">" + se.getSymbol() + "</a>";
			SecurityMPT mpt1 = securityManager.getSecurityMPT(se.getID(), PortfolioMPT.LAST_ONE_YEAR);
			if(mpt1 != null){
				cpi.setAR1(mpt1.getAR());
				cpi.setSharpeRatio1(mpt1.getSharpeRatio());
			}
			SecurityMPT mpt3 = securityManager.getSecurityMPT(se.getID(), PortfolioMPT.LAST_THREE_YEAR);
			if(mpt3 != null){
				cpi.setAR3(mpt3.getAR());
				cpi.setSharpeRatio3(mpt3.getSharpeRatio());
			}
			SecurityMPT mpt5 = securityManager.getSecurityMPT(se.getID(), PortfolioMPT.LAST_FIVE_YEAR);
			if(mpt5 != null){
				cpi.setAR5(mpt5.getAR());
				cpi.setSharpeRatio5(mpt5.getSharpeRatio());
			}
			portMap.put(surl, cpi);
			fundCompareLink = "http://www.myplaniq.com/LTISystem/jsp/portfolio/ComparePortfolio.action?portfolioString="+symbol;
			for(String s:portfolioName){
				fundCompareLink +="%2C"+s;
			}
		}
		
		return Action.SUCCESS;
	}
	

	public InputStream getFis() {
		return fis;
	}

	public void setFis(InputStream fis) {
		this.fis = fis;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public Long getCompareID() {
		return compareID;
	}

	public void setCompareID(Long compareID) {
		this.compareID = compareID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public List<Portfolio> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<Portfolio> portfolios) {
		this.portfolios = portfolios;
	}

	public Strategy getPlan() {
		return plan;
	}

	public void setPlan(Strategy plan) {
		this.plan = plan;
	}

	public Map<String, List<String>> getPlanMajorAssets() {
		return planMajorAssets;
	}

	public void setPlanMajorAssets(Map<String, List<String>> planMajorAssets) {
		this.planMajorAssets = planMajorAssets;
	}

	public Map<String, List<String>> getMajorAssetsSta() {
		return majorAssetsSta;
	}

	public void setMajorAssetsSta(Map<String, List<String>> majorAssetsSta) {
		this.majorAssetsSta = majorAssetsSta;
	}

	public Map<String, List<String>> getPlanMinorAssets() {
		return planMinorAssets;
	}

	public void setPlanMinorAssets(Map<String, List<String>> planMinorAssets) {
		this.planMinorAssets = planMinorAssets;
	}

	public List<VariableFor401k> getCompareCandidateFunds() {
		return compareCandidateFunds;
	}

	public void setCompareCandidateFunds(List<VariableFor401k> compareCandidateFunds) {
		this.compareCandidateFunds = compareCandidateFunds;
	}

	public Map<String, List<String>> getComparePlanMinorAssets() {
		return comparePlanMinorAssets;
	}

	public void setComparePlanMinorAssets(
			Map<String, List<String>> comparePlanMinorAssets) {
		this.comparePlanMinorAssets = comparePlanMinorAssets;
	}

	public Map<String, List<String>> getComparePlanMajorAssets() {
		return comparePlanMajorAssets;
	}

	public void setComparePlanMajorAssets(Map<String, List<String>> comparePlanMajorAssets) {
		this.comparePlanMajorAssets = comparePlanMajorAssets;
	}

	public Map<String, List<String>> getCompareMajorAssetsSta() {
		return compareMajorAssetsSta;
	}

	public void setCompareMajorAssetsSta(
			Map<String, List<String>> compareMajorAssetsSta) {
		this.compareMajorAssetsSta = compareMajorAssetsSta;
	}

	public PlanScore getComparePlanScore() {
		return comparePlanScore;
	}

	public void setComparePlanScore(PlanScore comparePlanScore) {
		this.comparePlanScore = comparePlanScore;
	}

	public PlanScore getPlanScore() {
		return planScore;
	}

	public void setPlanScore(PlanScore planScore) {
		this.planScore = planScore;
	}

	public Strategy getSibPlan() {
		return sibPlan;
	}

	public void setSibPlan(Strategy sibPlan) {
		this.sibPlan = sibPlan;
	}

	public List<CachePortfolioItem> getPerformanceItems() {
		return performanceItems;
	}

	public void setPerformanceItems(List<CachePortfolioItem> performanceItems) {
		this.performanceItems = performanceItems;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public List<VariableFor401k> getCandidateFunds() {
		return candidateFunds;
	}

	public void setCandidateFunds(List<VariableFor401k> candidateFunds) {
		this.candidateFunds = candidateFunds;
	}

	public List<String> getEtfs() {
		return etfs;
	}

	public void setEtfs(List<String> etfs) {
		this.etfs = etfs;
	}

	public Map<String, List<String>> getPortfolioMajorAssets() {
		return portfolioMajorAssets;
	}

	public void setPortfolioMajorAssets(Map<String, List<String>> portfolioMajorAssets) {
		this.portfolioMajorAssets = portfolioMajorAssets;
	}

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public List<PlanArticle> getArticleList() {
		return articleList;
	}

	public void setArticleList(List<PlanArticle> articleList) {
		this.articleList = articleList;
	}

	public PlanArticle getArticleContent() {
		return articleContent;
	}

	public void setArticleContent(PlanArticle articleContent) {
		this.articleContent = articleContent;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public String getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(String portfolioID) {
		this.portfolioID = portfolioID;
	}

	public String getIdsOrTickers() {
		return idsOrTickers;
	}

	public void setIdsOrTickers(String idsOrTickers) {
		this.idsOrTickers = idsOrTickers;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public File getUploadFile() {
		return uploadFile;
	}

	public void setUploadFile(File uploadFile) {
		this.uploadFile = uploadFile;
	}

	public String getUploadFileFileName() {
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName) {
		this.uploadFileFileName = uploadFileFileName;
	}

	public List<String> getNewArticleList() {
		return newArticleList;
	}

	public void setNewArticleList(List<String> newArticleList) {
		this.newArticleList = newArticleList;
	}

	public String getTitleStr() {
		return titleStr;
	}

	public void setTitleStr(String titleStr) {
		this.titleStr = titleStr;
	}

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String[][] getReturntable() {
		return returntable;
	}

	public void setReturntable(String[][] returntable) {
		this.returntable = returntable;
	}

	public String getSecurityID() {
		return securityID;
	}

	public void setSecurityID(String securityID) {
		this.securityID = securityID;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public long getUserID() {
		return userID;
	}

	public void setUserID(long userID) {
		this.userID = userID;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public List<HoldingItem> getHoldingItems() {
		return holdingItems;
	}

	public void setHoldingItems(List<HoldingItem> holdingItems) {
		this.holdingItems = holdingItems;
	}

	public List<String> getTopMajorAssets() {
		return topMajorAssets;
	}

	public void setTopMajorAssets(List<String> topMajorAssets) {
		this.topMajorAssets = topMajorAssets;
	}

	public List<String> getPlanTopMajorAssets() {
		return planTopMajorAssets;
	}

	public void setPlanTopMajorAssets(List<String> planTopMajorAssets) {
		this.planTopMajorAssets = planTopMajorAssets;
	}

	public Map<String, List<String>> getRankTable() {
		return rankTable;
	}

	public void setRankTable(Map<String, List<String>> rankTable) {
		this.rankTable = rankTable;
	}

	public boolean getisTAA() {
		return isTAA;
	}

	public void setisTAA(boolean isTAA) {
		this.isTAA = isTAA;
	}

	public boolean getisSAA() {
		return isSAA;
	}

	public void setisSAA(boolean isSAA) {
		this.isSAA = isSAA;
	}

	public PortfolioPermissionChecker getPc() {
		return pc;
	}

	public void setPc(PortfolioPermissionChecker pc) {
		this.pc = pc;
	}

	public Portfolio getRPortfolio() {
		return rPortfolio;
	}

	public void setRPortfolio(Portfolio portfolio) {
		rPortfolio = portfolio;
	}

	public List<String> getAssetNames() {
		return assetNames;
	}

	public void setAssetNames(List<String> assetNames) {
		this.assetNames = assetNames;
	}

	public boolean isUsePlanList() {
		return usePlanList;
	}

	public void setUsePlanList(boolean usePlanList) {
		this.usePlanList = usePlanList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Map<String, List<String>> getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(Map<String, List<String>> clientInfo) {
		this.clientInfo = clientInfo;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}

	public String getClientAddr() {
		return clientAddr;
	}

	public void setClientAddr(String clientAddr) {
		this.clientAddr = clientAddr;
	}

	public String getClientAddrCity() {
		return clientAddrCity;
	}

	public void setClientAddrCity(String clientAddrCity) {
		this.clientAddrCity = clientAddrCity;
	}

	public String getClientAddrState() {
		return clientAddrState;
	}

	public void setClientAddrState(String clientAddrState) {
		this.clientAddrState = clientAddrState;
	}

	public String getClientAddrCountry() {
		return clientAddrCountry;
	}

	public void setClientAddrCountry(String clientAddrCountry) {
		this.clientAddrCountry = clientAddrCountry;
	}

	public String getClientAddrZip() {
		return clientAddrZip;
	}

	public void setClientAddrZip(String clientAddrZip) {
		this.clientAddrZip = clientAddrZip;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Long getReportID() {
		return reportID;
	}

	public void setReportID(Long reportID) {
		this.reportID = reportID;
	}

	public List<String> getTickerList() {
		return tickerList;
	}

	public void setTickerList(List<String> tickerList) {
		this.tickerList = tickerList;
	}

	public boolean getContainOther() {
		return containOther;
	}

	public void setContainOther(boolean containOther) {
		this.containOther = containOther;
	}

	public String[][] getMajorReturnTable(){
		return majorReturnTable;
	}
	
	public void setMajorReturnTable(String[][] majorReturnTable){
		this.majorReturnTable = majorReturnTable;
	}

	public String[][] getUsStyleReturnTable() {
		return usStyleReturnTable;
	}

	public void setUsStyleReturnTable(String[][] usStyleReturnTable) {
		this.usStyleReturnTable = usStyleReturnTable;
	}
	
	public String getSymbol(){
		return symbol;
	}
	
	public void setSymbol(String symbol){
		this.symbol = symbol;
	}

	public Map<String, List<String>> getPlanMinorFunds() {
		return planMinorFunds;
	}

	public void setPlanMinorFunds(Map<String, List<String>> planMinorFunds) {
		this.planMinorFunds = planMinorFunds;
	}

	public Map<String, List<String>> getCompareMinorFunds() {
		return compareMinorFunds;
	}

	public void setCompareMinorFunds(Map<String, List<String>> compareMinorFunds) {
		this.compareMinorFunds = compareMinorFunds;
	}

	public int getYearNum() {
		return yearNum;
	}

	public void setYearNum(int yearNum) {
		this.yearNum = yearNum;
	}
	
	public int getMonthNum() {
		return monthNum;
	}

	public void setMonthNum(int monthNum) {
		this.monthNum = monthNum;
	}

	public List<String> getMutualFundList() {
		return mutualFundList;
	}

	public void setMutualFundList(List<String> mutualFundList) {
		this.mutualFundList = mutualFundList;
	}

	public List<String> getEtfsList() {
		return etfsList;
	}

	public void setEtfsList(List<String> etfsList) {
		this.etfsList = etfsList;
	}

	public long getArticleID() {
		return articleID;
	}

	public void setArticleID(long articleID) {
		this.articleID = articleID;
	}

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}
	
	public String getManagement() {
		return management;
	}

	public void setManagement(String management) {
		this.management = management;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Map<String, CachePortfolioItem> getPortMap() {
		return portMap;
	}

	public void setPortMap(Map<String, CachePortfolioItem> portMap) {
		this.portMap = portMap;
	}

	public String getFundCompareLink() {
		return fundCompareLink;
	}

	public void setFundCompareLink(String fundCompareLink) {
		this.fundCompareLink = fundCompareLink;
	}
}
