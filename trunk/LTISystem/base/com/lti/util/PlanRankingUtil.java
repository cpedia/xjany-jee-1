/**
 * 
 */
package com.lti.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.lti.service.AssetClassManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.KeyValuePair;
import com.lti.service.bo.PlanScore;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.PortfolioPerformance;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityMPT;
import com.lti.service.bo.Strategy;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.SortType;

/**
 * @author CCD
 * the class to calculate the relative score for the plan ranking
 */
public class PlanRankingUtil {
	//The weight for calculating security quality
	
	private final Double delta = 0.000001;
	private Double securityAlphaWeight=0.25;
	private Double securityTreynorWeight=0.25;
	private Double securityARWeight=0.5;
	
	//The weight for calculating portfolio construction capability
	private Double portfolioARWeight=0.5;
	private Double portfolioSortinoWeight=0.2;
	private Double portfolioSharpeWeight=0.1;
	private Double portfolioTreynorWeight=0.0;
	private Double portfolioDrawDownWeight=0.1;
	private Double portfolioWinningWeight=0.1;
	
	//The weight for plan construction capability
	private Double planTAAWeight=0.8;
	private Double planSAAWeight=0.2;
	
	//The weight for plan overall investment
	private Double planFundQualityWeight=0.3;
	private Double planCoverageWeight=0.3;
	private Double planCapabilityWeight=0.4;
	
	private List<String> availableAssetClassList;
	private HashSet<String> availableAssetClassSet;
	
	private List<PlanScore> planScoreList;
	private PlanScore planScore;
	
	private List<PortfolioPerformance> portfolioPerformanceList;
	private StrategyManager strategyManager = (StrategyManager) ContextHolder.getInstance().getApplicationContext().getBean("strategyManager");
	private PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
	public List<String> getAvailableAssetClassList() {
		return availableAssetClassList;
	}
	public void setAvailableAssetClassList(List<String> availableAssetClassList) {
		this.availableAssetClassList = availableAssetClassList;
	}
	
	
	public PlanRankingUtil(){
		AssetClassManager acm = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		String[] aac= {"US EQUITY", "INTERNATIONAL EQUITY", "FIXED INCOME", "REAL ESTATE", "COMMODITIES", "Emerging Market", "INTERNATIONAL BONDS", "High Yield Bond", "CASH", "BALANCE FUND","Long-Term Bond"};
		availableAssetClassList = new ArrayList<String>();
		for(int i=0;i<aac.length;++i)
			availableAssetClassList.add(aac[i]);
		availableAssetClassSet = acm.getAvailableAssetClassSet(availableAssetClassList);
		portfolioPerformanceList = new ArrayList<PortfolioPerformance>();
	}
	
	private boolean checkWeight(){
		Double securityWeight = securityAlphaWeight+securityTreynorWeight+securityARWeight;
		if(Math.abs(1.0 - securityWeight) > delta)
			return false;
		Double portfolioWeight = portfolioARWeight+portfolioSortinoWeight+portfolioSharpeWeight+portfolioTreynorWeight+portfolioDrawDownWeight+portfolioWinningWeight;
		if(Math.abs(1.0 - portfolioWeight) > delta)
			return false;
		Double planConstructionWeight = planTAAWeight+planSAAWeight;
		if(Math.abs(1.0 - planConstructionWeight) > delta)
			return false;
		Double planWeight = planFundQualityWeight+planCoverageWeight+planCapabilityWeight;
		if(Math.abs(1.0 - planWeight) > delta)
			return false;
		return true;
	}
	public PlanRankingUtil(Double securityAlphaWeight, Double securityTreynorWeight, Double securityARWeight, 
			Double portfolioARWeight, Double portfolioSortinoWeight, Double portfolioSharpeWeight, 
			Double portfolioTreynorWeight, Double portfolioDrawDownWeight, Double portfolioWinningWeight, 
			Double planTAAWeight, Double planSAAWeight, Double planFundQualityWeight, 
			Double planCoverageWeight, Double planCapabilityWeight){
		this.setSecurityAlphaWeight(securityAlphaWeight);
		this.setSecurityTreynorWeight(securityTreynorWeight);
		this.setSecurityARWeight(securityARWeight);
		this.setPortfolioARWeight(portfolioARWeight);
		this.setPortfolioSortinoWeight(portfolioSortinoWeight);
		this.setPortfolioSharpeWeight(portfolioSharpeWeight);
		this.setPortfolioTreynorWeight(portfolioTreynorWeight);
		this.setPortfolioDrawDownWeight(portfolioDrawDownWeight);
		this.setPortfolioWinningWeight(portfolioWinningWeight);
		this.setPlanTAAWeight(planTAAWeight);
		this.setPlanSAAWeight(planSAAWeight);
		this.setPlanFundQualityWeight(planFundQualityWeight);
		this.setPlanCoverageWeight(planCoverageWeight);
		this.setPlanCapabilityWeight(planCapabilityWeight);
		AssetClassManager acm = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		String[] aac= {"US EQUITY", "INTERNATIONAL EQUITY", "FIXED INCOME", "REAL ESTATE", "COMMODITIES", "Emerging Market", "INTERNATIONAL BONDS", "High Yield Bond", "CASH", "BALANCE FUND"};
		availableAssetClassList = new ArrayList<String>();
		for(int i=0;i<aac.length;++i)
			availableAssetClassList.add(aac[i]);
		availableAssetClassSet = acm.getAvailableAssetClassSet(availableAssetClassList);
		portfolioPerformanceList = new ArrayList<PortfolioPerformance>();
	}
	
	public void rankPlan()throws Exception{
		
		if(!checkWeight())
			throw new Exception("The total weight is not equal to 1, please check the parameters");
		long t1=System.currentTimeMillis();
		long t2;
		System.out.println("Rank Securities");
		this.rankSecurity();
		t2=System.currentTimeMillis();
		System.out.println("cost time: " + (t2-t1));
		t1=t2;
		System.out.println("Initialize Plan Score For All Plans");
		initPlanScoreForAllPlan();
		t2 = System.currentTimeMillis();
		System.out.println("cost time: " + (t2-t1));
		t1=t2;
		System.out.println("Calculate Plan Capability Score For All Plans");
		this.calculatePlanCapability();
		t2 = System.currentTimeMillis();
		System.out.println("cost time: " + (t2-t1));
		t1=t2;
		System.out.println("Calculate Plan Fund Quality Score For All Plans");
		this.calculatePlanFundQuality();
		t2 = System.currentTimeMillis();
		System.out.println("cost time: " + (t2-t1));
		t1=t2;
		System.out.println("Calculate Plan Coverage Score For All Plans");
		this.calculatePlanCoverage();
		t2 = System.currentTimeMillis();
		System.out.println("cost time: " + (t2-t1));
		t1=t2;
		System.out.println("Calculate Plan Investment For All Plans");
		this.calculatePlanInvestment();
		t2 = System.currentTimeMillis();
		System.out.println("cost time: " + (t2-t1));
		t1=t2;
		System.out.println("Calculate Plan Return For All Plans");
		this.calculatePlanModerateReturn();
		t2 = System.currentTimeMillis();
		t1=t2;
		this.calculateMajorAssetClassCount();
		t2 = System.currentTimeMillis();
		System.out.println("cost time: " + (t2-t1));
		strategyManager.saveOrUpdateAllPlanScore(planScoreList);
		portfolioManager.deletePortfolioPerformance();
		portfolioManager.saveAllPortfolioPerformance(portfolioPerformanceList);
		
	}
	public void rankSecurity(){
		this.calculateFundQuality();
	}
	
	public void setPlanScoreName(){
		List<Strategy> plans = strategyManager.getStrategiesByType(Configuration.STRATEGY_401K_TYPE);
		List<PlanScore> planScoreList = new ArrayList<PlanScore>();
		if(plans!=null && plans.size()>0){
			for(int i=0;i<plans.size();++i){
				PlanScore ps = strategyManager.getPlanScoreByPlanID(plans.get(i).getID());
				ps.setPlanName(plans.get(i).getName());
				planScoreList.add(ps);
			}
			strategyManager.saveOrUpdateAllPlanScore(planScoreList);
		}
	}
	public void initPlanScoreForAllPlan(){
		List<Strategy> plans = strategyManager.getStrategiesByType(Configuration.STRATEGY_TYPE_401K);
		planScoreList = new ArrayList<PlanScore>();
		if(plans!=null && plans.size()>0){
			for(int i=0;i<plans.size();++i){
				PlanScore ps = strategyManager.getPlanScoreByPlanID(plans.get(i).getID());
				if(ps!=null){
					ps.clear();
					ps.setPlanName(plans.get(i).getName());
				}else{
					ps = new PlanScore();
					ps.setPlanID(plans.get(i).getID());
					ps.setPlanName(plans.get(i).getName());
				}
				planScoreList.add(ps);
			}
			//strategyManager.saveOrUpdateAllPlanScore(planScoreList);
		}
	}
	
	public Double calculateReturn(Long portfolioID){
		List<Integer> years = new ArrayList<Integer>();
		years.add(-5);years.add(-3);years.add(-1);years.add(0);
		List<PortfolioMPT> pmpts = portfolioManager.getPortfolioMPTsByYearArray(portfolioID, years);
		//{-5,-3,-1,0}
		try{
			if(pmpts!=null && pmpts.size()== 4){
				Double re = pmpts.get(0).getAR() * 0.4 + pmpts.get(1).getAR() * 0.4 + pmpts.get(2).getAR() * 0.1 + pmpts.get(3).getAR() * 0.1;
				return re;
			}
		}catch(Exception e){
			System.out.println(portfolioID);
		}
		return 0.0;
	}
	public Double getFiveYearReturn(Long portfolioID){
		PortfolioMPT mpt = portfolioManager.getPortfolioMPT(portfolioID, -5);
		if(mpt!=null)
			return mpt.getAR();
		return 0.0;
	}
	public void calculatePlanModerateReturn(){
		if(planScoreList!=null && planScoreList.size()>0){
			for(int i=0;i<planScoreList.size();++i){
				PlanScore ps = planScoreList.get(i);
				if(ps.getStatus() == 0){
					List<Portfolio> portfolios = strategyManager.getModeratePortfolios(ps.getPlanID());
					System.out.println("Moderate Return: " + i + "--" + planScoreList.size());
					if(portfolios!=null){
						for(int j=0;j<portfolios.size();++j){
							if(portfolios.get(j).getName().endsWith("Tactical Asset Allocation Moderate")){//TAA Moderate
								//Double TAAReturn = this.calculateReturn(portfolios.get(j).getID());
								Double TAAReturn = this.getFiveYearReturn(portfolios.get(j).getID());
								ps.setTAAReturn(TAAReturn);
							}else if(portfolios.get(j).getName().contains("Strategic Asset Allocation Moderate")){//SAA Moderate
								//Double SAAReturn = this.calculateReturn(portfolios.get(j).getID());
								Double SAAReturn = this.getFiveYearReturn(portfolios.get(j).getID());
								ps.setSAAReturn(SAAReturn);
							}
						}
					}
				}
			}
			//strategyManager.saveOrUpdateAllPlanScore(planScoreList);
		}
	}
	
	public void calculateMajorAssetClassCount(){
		if(planScoreList!=null && planScoreList.size()>0){
			for(int i=0;i<planScoreList.size();++i){
				PlanScore ps = planScoreList.get(i);

					try {
						int majorAssetClassCount = strategyManager.getMajorAssetCountByPlanID(ps.getPlanID());
						ps.setMajorAssetClass(majorAssetClassCount);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
	}
	
	public void calculatePlanCapability(){
		Date today  = new Date();
		today = LTIDate.getNDaysAgo(today, 15);
		//List<Portfolio> moderateTAAPortfolios = new ArrayList<Portfolio>();
		//List<Portfolio> moderateSAAPortfolios = new ArrayList<Portfolio>();
		HashMap<Long, Long> planTAAMap = new HashMap<Long, Long>();
		HashMap<Long, Long> planSAAMap = new HashMap<Long, Long>();
		HashMap<Long, Double> capabilityScoreMap =  new HashMap<Long, Double>();
		List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();
		List<PortfolioPerformance> moderateTAAPerformanceList = new ArrayList<PortfolioPerformance>();
		List<PortfolioPerformance> moderateSAAPerformanceList = new ArrayList<PortfolioPerformance>();
		//find all the moderate portfolios into TAA and SAA groups
		if(planScoreList!=null){
			for(int i=0;i<planScoreList.size();++i){
				boolean hasSAA=false, hasTAA=false;
				PlanScore ps = planScoreList.get(i);
				List<Portfolio> portfolios = strategyManager.getModeratePortfolios(ps.getPlanID());
				if(portfolios!=null){
					for(int j=0;j<portfolios.size();++j){
						Portfolio p = portfolios.get(j);
						if(p.getName().contains("Tactical")&&!hasTAA){
							if(p.getEndDate()!=null && LTIDate.before(today, p.getEndDate())){
								PortfolioPerformance pp = new PortfolioPerformance();
								pp.setPlanID(ps.getPlanID());
								pp.setPortfolioID(p.getID());
								moderateTAAPerformanceList.add(pp);
								//moderateTAAPortfolios.add(p);
								planTAAMap.put(ps.getPlanID(), p.getID());
								hasTAA=true;
							}else
								ps.setStatus(1);
						}else if(p.getName().contains("Strategic")&&!hasSAA){
							if(p.getEndDate()!=null && LTIDate.before(today, p.getEndDate())){
								PortfolioPerformance pp = new PortfolioPerformance();
								pp.setPlanID(ps.getPlanID());
								pp.setPortfolioID(p.getID());
								moderateSAAPerformanceList.add(pp);
								//moderateSAAPortfolios.add(p);
								planSAAMap.put(ps.getPlanID(), p.getID());
								hasSAA=true;
							}else
								ps.setStatus(1);
						}
					}
				}
				if(!hasTAA || !hasSAA)
					ps.setStatus(1);
			}
			HashMap<Long, Double> TAAScoreMap= new HashMap<Long, Double>();
			HashMap<Long, Double> SAAScoreMap= new HashMap<Long, Double>();
			if(moderateTAAPerformanceList.size()!=0)
				TAAScoreMap = this.calculatePlanConstructionCapability(moderateTAAPerformanceList);
			if(moderateSAAPerformanceList.size()!=0)
				SAAScoreMap = this.calculatePlanConstructionCapability(moderateSAAPerformanceList);
			for(int i=0;i<planScoreList.size();++i){
				PlanScore ps = planScoreList.get(i);
				if(ps.getStatus()==0){
					ps.setTAAScore(TAAScoreMap.get(planTAAMap.get(ps.getPlanID())));
					ps.setSAAScore(SAAScoreMap.get(planSAAMap.get(ps.getPlanID())));
					if(ps.getTAAScore()==0.0 || ps.getSAAScore()==0.0)
						ps.setStatus(1);
					else{
						Double capabilityValue = ps.getTAAScore() * planTAAWeight + ps.getSAAScore() * planSAAWeight;
						ps.setCapabilityValue(capabilityValue);
						kvpList.add(new KeyValuePair(ps.getPlanID(), capabilityValue));
					}
				}
			}
			if(kvpList.size()!=0){
				KeyValuePairComparator kvpc = new KeyValuePairComparator();
				Collections.sort(kvpList, kvpc);
				Double curScore = 1.0;
				Double delta = 1.0/ kvpList.size();
				for(int i=0;i<kvpList.size();++i){
					capabilityScoreMap.put(kvpList.get(i).getKey(), curScore);
					curScore-=delta;
				}
				for(int i=0;i<planScoreList.size();++i){
					PlanScore ps = planScoreList.get(i);
					if(ps.getStatus()==0)
						ps.setCapabilityScore(capabilityScoreMap.get(ps.getPlanID()));
				}
				//strategyManager.saveOrUpdateAllPlanScore(planScoreList);
			}
			
		}
	}
	/**
	 * @author CCD
	 */
	public void calculatePlanInvestment(){
		if(planScoreList!=null){
			for(int i=0;i<planScoreList.size();++i){
				PlanScore ps = planScoreList.get(i);
				if(ps.getStatus() == 0)//FQ=0.3,Coverage=0.3,Capability=0.4
					ps.setInvestmentScore(ps.getFundQualityScore() * planFundQualityWeight + ps.getCoverageScore() * planCoverageWeight + ps.getCapabilityScore() * planCapabilityWeight);
			}
		}
	}
	/**
	 * @author CCD
	 * @param planID
	 */
	public Double calculatePlanFundQualityByPlanID(Long planID){
		AssetClassManager acm = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		SecurityManager sm = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		List<String> availableFundList = strategyManager.getSymbolListForPlanWithoutN(planID);
		HashMap<String,List<String>> availableAssetClassFundMap = acm.getAvailableAssetClassSet(availableAssetClassSet, availableFundList, true, true);
		Collection<List<String>> fundsList =  availableAssetClassFundMap.values();
		Iterator<List<String>> vIterator = fundsList.iterator();
		Double finalScore = 0.0;
		int classSize = 0;
		while(vIterator.hasNext()){
			Double score = 0.0;
			int size = 0;
			List<String> securityList = vIterator.next();
			for(int j=0;j<securityList.size();++j){
				String symbol = securityList.get(j);
				Security se = sm.getBySymbol(symbol);
				if(se.getQuality()!=null){
					score += se.getQuality();
					++size;
				}
			}
			if(size>0){
				finalScore += score/size;
				++classSize;
			}
		}
		if(classSize>0)
			finalScore/=classSize;
		return finalScore;
	}
	/**
	 * @author CCD
	 * @param availableAssetClassList
	 * the interface to calculate the fund quality score for each plan
	 */
	public void calculatePlanFundQuality(){
		HashMap<Long, Double> scoreMap = new HashMap<Long, Double>();
		List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();
		if(planScoreList!=null){
			for(int i=0;i<planScoreList.size();++i){
				PlanScore ps = planScoreList.get(i);
				if(ps.getStatus() == 0){
					System.out.println("Fund Quality: Current Plan:" + i +"--"+ planScoreList.size());
					Double fundQualityValue = calculatePlanFundQualityByPlanID(ps.getPlanID());
					ps.setFundQualityValue(fundQualityValue);
					kvpList.add(new KeyValuePair(ps.getPlanID(), fundQualityValue));
				}
			}
			KeyValuePairComparator kvpComp = new KeyValuePairComparator();
			Collections.sort(kvpList, kvpComp);
			Double delta = 1.0/ kvpList.size();
			Double curScore = 1.0;
			for(int i=0;i<kvpList.size();++i){
				scoreMap.put(kvpList.get(i).getKey(), curScore);
				curScore-=delta;
			}
			for(int i=0;i<planScoreList.size();++i){
				PlanScore  ps = planScoreList.get(i);
				if(ps.getStatus() == 0)
					ps.setFundQualityScore(scoreMap.get(ps.getPlanID()));
			}
			//strategyManager.saveOrUpdateAllPlanScore(planScoreList);
		}
	}
	/**
	 * @author CCD
	 * @param planID
	 * @return
	 * calculate the coverage(diversify factor) with ID planID
	 */
	public Double calculateCoverageByPlanID(Long planID){
		List<String> availableFundList = strategyManager.getSymbolListForPlan(planID);
		AssetClassManager acm = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		HashMap<String,List<String>> availableAssetClassFundMap = acm.getAvailableAssetClassSet(availableAssetClassSet, availableFundList, true, true);
		Double diversifyFactor = 0.0;
		if(availableAssetClassFundMap != null && availableAssetClassFundMap.size() > 0){
			Set<String> keySet = availableAssetClassFundMap.keySet();
			for(String key: keySet){
				List<String> funds = availableAssetClassFundMap.get(key);
				int fundNum = funds.size();
				if(key.equalsIgnoreCase("US EQUITY")){
					if(fundNum <= 2)
						diversifyFactor += 1.0;
					else if(fundNum <= 4)
						diversifyFactor += 1.1;
					else if(fundNum <= 6)
						diversifyFactor += 1.2;
					else if(fundNum <= 8)
						diversifyFactor += 1.3;
					else if(fundNum == 9)
						diversifyFactor += 1.2;
					else if(fundNum == 10)
						diversifyFactor += 1.1;
					else if(fundNum == 11)
						diversifyFactor += 1.0;
					else if(fundNum <= 14)
						diversifyFactor += 0.9;
					else 
						diversifyFactor += 0.8;
				}else{
					if(fundNum <= 1)
						diversifyFactor += 1.0;
					else if(fundNum == 2)
						diversifyFactor += 1.1;
					else if(fundNum == 3)
						diversifyFactor += 1.2;
					else if(fundNum <= 5)
						diversifyFactor += 1.3;
					else if(fundNum == 6)
						diversifyFactor += 1.2;
					else if(fundNum == 7)
						diversifyFactor += 1.1;
					else if(fundNum == 8)
						diversifyFactor += 1.0;
					else if(fundNum <= 10)
						diversifyFactor += 0.9;
					else 
						diversifyFactor += 0.8;
				}
			}
		}
		
		return diversifyFactor;
	}
	/**
	 * @author CCD
	 * rank the plan coverage according to their diversifyfactor
	 */
	public void calculatePlanCoverage(){
		HashMap<Long, Double> scoreMap = new HashMap<Long, Double>();
		List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();
		if(planScoreList!=null){
			for(int i=0;i<planScoreList.size();++i){
				PlanScore ps = planScoreList.get(i);
				if(ps.getStatus() == 0){
					System.out.println("Coverage: Current Plan:" + i +"--"+ planScoreList.size());
					Double coverageValue = calculateCoverageByPlanID(ps.getPlanID());
					ps.setCoverageValue(coverageValue);
					kvpList.add(new  KeyValuePair(ps.getPlanID(), coverageValue));
				}
			}
			KeyValuePairComparator kvpComp = new KeyValuePairComparator();
			Collections.sort(kvpList, kvpComp);
			Double highScore = 1.0;
			Double delta = 1.0/kvpList.size();
			Double curScore = 1.0;
			Double preValue = 0.0;
			int startIndex = 0;
			int endIndex = 0;
			boolean flag = true;
			for(int i=0;i<kvpList.size();){
				if(flag){
					startIndex = endIndex = i;
					preValue = kvpList.get(i).getValue();
					highScore = curScore;
					flag = false;
					++i;
					continue;
				}
				if(kvpList.get(i).getValue().equals(preValue)){
					endIndex = i++;
					curScore -= delta;
				}else{
					flag=true;
					Double avgScore = (highScore + curScore)/2;
					for(int j=startIndex;j<=endIndex;++j)
						scoreMap.put(kvpList.get(j).getKey(), avgScore);
					curScore-=delta;
				}
			}
			Double avgScore = (highScore + curScore)/2;
			for(int j=startIndex;j<=endIndex;++j)
				scoreMap.put(kvpList.get(j).getKey(), avgScore);
			for(int i=0;i<planScoreList.size();++i){
				PlanScore ps = planScoreList.get(i);
				if(ps.getStatus() == 0)
					ps.setCoverageScore(scoreMap.get(ps.getPlanID()));
			}
			//strategyManager.saveOrUpdateAllPlanScore(planScoreList);
		}
	}
	
	
	/**
	 * @author CCD
	 * @param securityIDs
	 * @param smptList
	 * @param deltaWeight
	 * @param inceptionWightMap
	 * adjust the inception weight according to their security mpt records
	 */
	public void adjustSMInceptionWeight(List<Long> securityIDs, List<SecurityMPT> smptList, Double deltaWeight, HashMap<Long, Double> inceptionWightMap){
		//they are all order by securityID, and the securityIDS contains all securityID in smptList
		int i,j;
		for(i=0,j=0;i<securityIDs.size() && j<smptList.size();++i){
			if(!smptList.get(j).getSecurityID().equals(securityIDs.get(i))){
				if(smptList.get(j).getSecurityID()< securityIDs.get(i))
					while(j<smptList.size() && smptList.get(j).getSecurityID()< securityIDs.get(i))++j;
				else{
					Double curWeight = inceptionWightMap.get(securityIDs.get(i));
					curWeight += deltaWeight;
					inceptionWightMap.put(securityIDs.get(i), curWeight);
				}
			}else
				++j;
		}
		while(i<securityIDs.size()){
			Double curWeight = inceptionWightMap.get(securityIDs.get(i));
			curWeight += deltaWeight;
			inceptionWightMap.put(securityIDs.get(i), curWeight);
			++i;
		}
	}
	/**
	 * @author CCD
	 * @param portfolioIDs
	 * @param pmptList
	 * @param deltaWeight
	 * @param inceptionWeightMap
	 * adjust the inception weight according to their portfolio mpt records
	 */
	public void adjustPMInceptionWeight(List<Long> portfolioIDs, List<PortfolioMPT> pmptList, Double deltaWeight, HashMap<Long, Double> inceptionWeightMap){
		//they are all order by securityID, and the securityIDS contains all securityID in smptList
		int i,j;
		for(i=0,j=0;i<portfolioIDs.size() && j<pmptList.size();++i){
			if(!pmptList.get(j).getPortfolioID().equals(portfolioIDs.get(i))){
				if(pmptList.get(j).getPortfolioID()< portfolioIDs.get(i))
					while(j<pmptList.size() && pmptList.get(j).getPortfolioID()< portfolioIDs.get(i))++j;
				else{
					Double curWeight = inceptionWeightMap.get(portfolioIDs.get(i));
					curWeight += deltaWeight;
					inceptionWeightMap.put(portfolioIDs.get(i), curWeight);
				}
			}else
				++j;
		}
		while(i<portfolioIDs.size()){
			Double curWeight = inceptionWeightMap.get(portfolioIDs.get(i));
			curWeight += deltaWeight;
			inceptionWeightMap.put(portfolioIDs.get(i), curWeight);
			++i;
		}
	}
	/**
	 * @author CCD
	 * @param sortedMPTs
	 * @param weight
	 * @param scoreMap
	 * @return
	 * calculate the ranking score of the special quality and give it a score(take weight into consideration)
	 */
	public void getSecurityRankScore(List<SecurityMPT> sortedMPTs, Double weight, HashMap<Long, Double> scoreMap){
		Double delta = 1.0/ sortedMPTs.size();
		Double curScore = 1.0;
		for(int i=0;i<sortedMPTs.size();++i){
			Double score = scoreMap.get(sortedMPTs.get(i).getSecurityID()) + curScore * weight;
			scoreMap.put(sortedMPTs.get(i).getSecurityID(), score);
			curScore-=delta;
		}
	}
	/**
	 * @author CCD
	 * @param sortedMPTs
	 * @param weight
	 * @param scoreMap
	 * @return
	 * calculate the ranking score of the special quality and give it a score(take weight into consideration)
	 */
	public void getPortfolioRankScore(List<PortfolioMPT> sortedMPTs, Double weight, HashMap<Long, Double> scoreMap){
		Double delta = 1.0/ sortedMPTs.size();
		Double curScore = 1.0;
		for(int i=0;i<sortedMPTs.size();++i){
			Double score = scoreMap.get(sortedMPTs.get(i).getPortfolioID()) + curScore * weight;
			scoreMap.put(sortedMPTs.get(i).getPortfolioID(), score);
			curScore-=delta;
		}
	}
	/**
	 * @author CCD
	 * @param sortedMPTs
	 * @param weightMap
	 * @param scoreMap
	 * calculate the ranking score of the special quality and give it a score for the security MPT
	 * this API is special for inception calculate, as the inception weight may be different among funds
	 */
	public void getSecurityRankScore(List<SecurityMPT> sortedMPTs, HashMap<Long, Double> weightMap, HashMap<Long, Double> scoreMap){
		Double delta = 1.0/ sortedMPTs.size();
		Double curScore = 1.0;
		for(int i=0;i<sortedMPTs.size();++i){
			Double score = scoreMap.get(sortedMPTs.get(i).getSecurityID()) + curScore * weightMap.get(sortedMPTs.get(i).getSecurityID());
			scoreMap.put(sortedMPTs.get(i).getSecurityID(), score);
			curScore-=delta;
		}
	}
	/**
	 * @author CCD
	 * @param sortedMPTs
	 * @param weightMap
	 * @param scoreMap
	 * calculate the ranking score of the special quality and give it a score for the portfolio MPT
	 * this API is special for inception calculate, as the inception weight may be different among funds
	 */
	public void getPortfolioRankScore(List<PortfolioMPT> sortedMPTs, HashMap<Long, Double> weightMap, HashMap<Long, Double> scoreMap){
		Double delta = 1.0/ sortedMPTs.size();
		Double curScore = 1.0;
		for(int i=0;i<sortedMPTs.size();++i){
			Double score = scoreMap.get(sortedMPTs.get(i).getPortfolioID()) + curScore * weightMap.get(sortedMPTs.get(i).getPortfolioID());
			scoreMap.put(sortedMPTs.get(i).getPortfolioID(), score);
			curScore-=delta;
		}
	}
	/**
	 * @author CCD
	 * @param mpts5
	 * @param mpts3
	 * @param mpts1
	 * @param mpts0
	 * @param scoreMap
	 * @param weightMap
	 * get the score for the quality and sum them with weight for security
	 */
	public void getSecurityScore(List<SecurityMPT> smpts5, List<SecurityMPT> smpts3, List<SecurityMPT> smpts1, List<SecurityMPT> smpts0, HashMap<Long,Double> scoreMap, HashMap<Long,Double> weightMap){
		getSecurityRankScore(smpts5, 0.4, scoreMap);
		getSecurityRankScore(smpts3, 0.4, scoreMap);
		getSecurityRankScore(smpts1, 0.1, scoreMap);
		getSecurityRankScore(smpts0, weightMap, scoreMap);
	}
	/**
	 * @author CCD
	 * @param mpts5
	 * @param mpts3
	 * @param mpts1
	 * @param mpts0
	 * @param scoreMap
	 * @param weightMap
	 * get the score for the quality and sum them with weight for portfolio
	 */
	private void getPortfolioScore(List<PortfolioMPT> pmpts5, List<PortfolioMPT> pmpts3, List<PortfolioMPT> pmpts1, List<PortfolioMPT> pmpts0, HashMap<Long,Double> scoreMap, HashMap<Long,Double> weightMap){
		getPortfolioRankScore(pmpts5, 0.4, scoreMap);
		getPortfolioRankScore(pmpts3, 0.4, scoreMap);
		getPortfolioRankScore(pmpts1, 0.1, scoreMap);
		getPortfolioRankScore(pmpts0, weightMap, scoreMap);
	}

	/**
	 * @author CCD
	 * the interface for calculating fund quality group by assetclass
	 */
	private void calculateFundQuality(){
		AssetClassManager acm = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		List<AssetClass> acList = acm.getClasses();
		System.out.println(acList.size());
		for(int i=0;i<acList.size();){
			AssetClass ac = acList.get(i);
			if(ac.getID()==0)
				acList.remove(i);
			/*****COMMODITIES id = 6   ================   SECTOR EQUITY id = 162   *****/
			/*****there are just one level below them in the assetclass tree*****/
			else if(ac.getParentID()==6 || ac.getParentID()==162)//COMMODITIES
				acList.remove(i);
			else
				++i;
		}
		System.out.println(acList.size());
		rankFundQualityByAssetClass(acList);
	}
	/**
	 * @author CCD
	 * @param assetClassNameList
	 * calculate the fund quality for all the funds group by the assetclass
	 * year=weight
	 * 0=0.1, 1=0.1, 3=0.4, 5=0.4
	 */
	public void rankFundQualityByAssetClass(List<AssetClass> assetClassList){
		Date endDate = LTIDate.getLastMonthDate(new Date());
		AssetClassManager acm = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		SecurityManager sm = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		//Integer[] years = {0,-1,-3,-5};
		//double[] weights = {0.1,0.1,0.4,0.4};
		if(assetClassList!=null){
			
			for(int i=0;i<assetClassList.size();++i){
//				if(i==5){
//					System.out.println("here");
//				}
				AssetClass ac = assetClassList.get(i);
				if(ac!=null){
					HashMap<Long, Double> securityAlphaMap = new HashMap<Long, Double>();
					HashMap<Long, Double> securityTreynorMap = new HashMap<Long, Double>();
					HashMap<Long, Double> securityARMap = new HashMap<Long, Double>();
					HashMap<Long, Double> inceptionWeightMap = new HashMap<Long, Double>();
					List<Security> securityList = null;
					String acName = ac.getName();
					if(acName.equalsIgnoreCase("SECTOR EQUITY") || acName.equalsIgnoreCase("COMMODITIES"))
						securityList = sm.getFundsByClassIDAndEndDate(ac.getID(), endDate);
					else
						securityList = sm.getSecuritiesByAssetIDAndEndDate(ac.getID(), endDate);
					List<Long> securityIDs = new ArrayList<Long>();
					if(securityList!=null && securityList.size()>20){
						//initial score map and inception map
						for(int j=0;j<securityList.size();++j){
							Security se = securityList.get(j);
							securityIDs.add(se.getID());
							securityAlphaMap.put(se.getID(), 0.0);
							securityTreynorMap.put(se.getID(), 0.0);
							securityARMap.put(se.getID(), 0.0);
							inceptionWeightMap.put(se.getID(), 0.1);
						}
					}
					else
						continue;
					//get mpts for last 5, last 3, last 1 year and inception
					List<SecurityMPT> mpts5 = sm.getSecurityMPTBySecurityIDs(securityIDs, -5L);
					adjustSMInceptionWeight(securityIDs, mpts5, 0.4, inceptionWeightMap);
					List<SecurityMPT> mpts3 = sm.getSecurityMPTBySecurityIDs(securityIDs, -3L);
					adjustSMInceptionWeight(securityIDs, mpts3, 0.4, inceptionWeightMap);
					List<SecurityMPT> mpts1 = sm.getSecurityMPTBySecurityIDs(securityIDs, -1L);
					adjustSMInceptionWeight(securityIDs, mpts1, 0.1, inceptionWeightMap);
					List<SecurityMPT> mpts0 = sm.getSecurityMPTBySecurityIDs(securityIDs, 0L);
					//adjust the inception weight
					//get Alpha score
					SecurityMPTComparator smptc = new SecurityMPTComparator(SortType.ALPHA);
					Collections.sort(mpts5, smptc);
					Collections.sort(mpts3, smptc);
					Collections.sort(mpts1, smptc);
					Collections.sort(mpts0, smptc);
					getSecurityScore(mpts5,mpts3,mpts1,mpts0,securityAlphaMap,inceptionWeightMap);
					//get Treynor score
					smptc.setSt(SortType.TREYNOR);
					Collections.sort(mpts5, smptc);
					Collections.sort(mpts3, smptc);
					Collections.sort(mpts1, smptc);
					Collections.sort(mpts0, smptc);
					getSecurityScore(mpts5,mpts3,mpts1,mpts0,securityTreynorMap,inceptionWeightMap);
					//get AR score
					smptc.setSt(SortType.ANNULIZEDRETURN);
					Collections.sort(mpts5, smptc);
					Collections.sort(mpts3, smptc);
					Collections.sort(mpts1, smptc);
					Collections.sort(mpts0, smptc);
					getSecurityScore(mpts5,mpts3,mpts1,mpts0,securityARMap,inceptionWeightMap);
					////get fund quality//
					List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();
					for(int j=0;j<securityList.size();++j){
						Security se = securityList.get(j);
						double alphaRating = securityAlphaMap.get(se.getID());
						double arRating = securityARMap.get(se.getID());
						double treynorRating = securityTreynorMap.get(se.getID());
						//System.out.println(se.getID());
					//	System.out.println(securityAlphaMap.get(se.getID()) + " " + securityTreynorMap.get(se.getID()) + " " + securityARMap.get(se.getID()));
						Double score = alphaRating * securityAlphaWeight + treynorRating * securityTreynorWeight + arRating * securityARWeight;
						//se.setQuality(score);
						se.setAlphaRating(alphaRating);
						se.setARRating(arRating);
						se.setTreynorRating(treynorRating);
						kvpList.add(new KeyValuePair(se.getID(), score));
					}
					HashMap<Long, Double> scoreMap = new HashMap<Long, Double>();
					KeyValuePairComparator kvpComp = new KeyValuePairComparator();
					Collections.sort(kvpList, kvpComp);
					Double delta = 1.0/ kvpList.size();
					Double curScore = 1.0;
					for(int j=0; j < kvpList.size();++j){
						scoreMap.put(kvpList.get(j).getKey(), curScore);
						curScore-=delta;
					}
					for(int j=0;j<securityList.size();++j){
						Security se = securityList.get(j);
						se.setQuality(scoreMap.get(se.getID()));
					}
					
					sm.saveOrUpdateAllSecurity(securityList);
					System.out.println(i+ "  " + acName + " is done.");
				}
			}
		}
	}
	private boolean compareList(List<Double> firstList, List<Double> secondList){
		if(firstList==null)
			return true;
		if(secondList==null)
			return false;
		int length = firstList.size()<secondList.size()?firstList.size():secondList.size();
		int firstScore = 0, secondScore = 0;
		for(int i=0;i<length;++i){
			if(firstList.get(i)>secondList.get(i))
				++firstScore;
			else if(firstList.get(i)< secondList.get(i))
				++secondScore;
		}
		return firstScore<secondScore;
	}
	public void bubbleSort(List<List<Double>> PAListList, HashMap<Long, Integer> IDPosMap, List<Long> portfolioIDs){
		Long temID;
		for(int i=0;i<portfolioIDs.size();++i){
			for(int j=i+1;j<portfolioIDs.size();++j){
				List<Double> firstList = PAListList.get(IDPosMap.get(portfolioIDs.get(i)));
				List<Double> secondList = PAListList.get(IDPosMap.get(portfolioIDs.get(j)));
				if(compareList(firstList,secondList)){
					temID = portfolioIDs.get(i);
					portfolioIDs.set(i, portfolioIDs.get(j));
					portfolioIDs.set(j, temID);
				}
			}
		}
	}
	
	public void calculateOnePlanConstructionCapability(List<PortfolioPerformance> ppList){
		
	}
	
	/**
	 * @author CCD
	 * the interface to calculate plan construction capability
	 */
	public HashMap<Long, Double> calculatePlanConstructionCapability(List<PortfolioPerformance> ppList){
		
		HashMap<Long, Double> scoreMap = new HashMap<Long, Double>();
		HashMap<Long, Double> portfolioARMap = new HashMap<Long, Double>();
		HashMap<Long, Double> portfolioSortinoMap = new HashMap<Long, Double>();
		HashMap<Long, Double> portfolioSharpeRatioMap = new HashMap<Long, Double>();
		HashMap<Long, Double> portfolioTreynorMap = new HashMap<Long, Double>();
		HashMap<Long, Double> portfolioDrawDownMap = new HashMap<Long, Double>();
		HashMap<Long, Double> inceptionWeightMap = new HashMap<Long, Double>();
		HashMap<Long, Double> portfolioWinningMap = new HashMap<Long, Double>();
		HashMap<Long, Integer> portfolioIDPositionMap = new HashMap<Long, Integer>();
		List<List<Double>> portfolioAmountListList = new ArrayList<List<Double>>();
		List<Long> portfolioIDs = new ArrayList<Long>();
		for(int i=0;i<ppList.size();++i){
			PortfolioPerformance pp = ppList.get(i);
			Long portfolioID = pp.getPortfolioID();
			List<Double> portfolioAmountList = portfolioManager.getPortfolioDailyAmount(portfolioID);
			portfolioAmountListList.add(portfolioAmountList);
			portfolioIDPositionMap.put(portfolioID, i);
			portfolioIDs.add(portfolioID);
			portfolioARMap.put(portfolioID, 0.0);
			portfolioSortinoMap.put(portfolioID, 0.0);
			portfolioSharpeRatioMap.put(portfolioID, 0.0);
			portfolioTreynorMap.put(portfolioID, 0.0);
			portfolioDrawDownMap.put(portfolioID, 0.0);
			portfolioWinningMap.put(portfolioID, 0.0);
			inceptionWeightMap.put(portfolioID, 0.1);
		}
		Collections.sort(portfolioIDs);
		List<PortfolioMPT> pmpts5 = portfolioManager.getPortfolioMPTsByYear(portfolioIDs, -5);
		adjustPMInceptionWeight(portfolioIDs, pmpts5, 0.4, inceptionWeightMap);
		List<PortfolioMPT> pmpts3 = portfolioManager.getPortfolioMPTsByYear(portfolioIDs, -3);
		adjustPMInceptionWeight(portfolioIDs, pmpts3, 0.4, inceptionWeightMap);
		List<PortfolioMPT> pmpts1 = portfolioManager.getPortfolioMPTsByYear(portfolioIDs, -1);
		adjustPMInceptionWeight(portfolioIDs, pmpts1, 0.1, inceptionWeightMap);
		List<PortfolioMPT> pmpts0 = portfolioManager.getPortfolioMPTsByYear(portfolioIDs, 0);
		PortfolioMPTComparator pmptc = new PortfolioMPTComparator(SortType.ANNULIZEDRETURN);
		Collections.sort(pmpts5, pmptc);
		Collections.sort(pmpts3, pmptc);
		Collections.sort(pmpts1, pmptc);
		Collections.sort(pmpts0, pmptc);
		getPortfolioScore(pmpts5,pmpts3,pmpts1,pmpts0, portfolioARMap, inceptionWeightMap);
		pmptc.setSt(SortType.SORTINO);
		Collections.sort(pmpts5, pmptc);
		Collections.sort(pmpts3, pmptc);
		Collections.sort(pmpts1, pmptc);
		Collections.sort(pmpts0, pmptc);
		getPortfolioScore(pmpts5,pmpts3,pmpts1,pmpts0, portfolioSortinoMap, inceptionWeightMap);
		pmptc.setSt(SortType.SHARPE);
		Collections.sort(pmpts5, pmptc);
		Collections.sort(pmpts3, pmptc);
		Collections.sort(pmpts1, pmptc);
		Collections.sort(pmpts0, pmptc);
		getPortfolioScore(pmpts5,pmpts3,pmpts1,pmpts0, portfolioSharpeRatioMap, inceptionWeightMap);
		pmptc.setSt(SortType.TREYNOR);
		Collections.sort(pmpts5, pmptc);
		Collections.sort(pmpts3, pmptc);
		Collections.sort(pmpts1, pmptc);
		Collections.sort(pmpts0, pmptc);
		getPortfolioScore(pmpts5,pmpts3,pmpts1,pmpts0, portfolioTreynorMap, inceptionWeightMap);
		pmptc.setSt(SortType.DRAWDOWN);
		Collections.sort(pmpts5, pmptc);
		Collections.sort(pmpts3, pmptc);
		Collections.sort(pmpts1, pmptc);
		Collections.sort(pmpts0, pmptc);
		getPortfolioScore(pmpts5,pmpts3,pmpts1,pmpts0, portfolioDrawDownMap, inceptionWeightMap);
		//calculate winning ratexs
		bubbleSort(portfolioAmountListList, portfolioIDPositionMap, portfolioIDs);
		int size = portfolioIDs.size();
		double delta = 1.0/size;
		double curScore = 1.0;
		for(int i=0;i<size;++i){
			portfolioWinningMap.put(portfolioIDs.get(i), curScore);
			curScore -= delta;
		}
		//AR=0.15, Sortino=0.2,SharpeRatio=0.2,Treynor=0.15,DrawDown=0.3
		for(int j=0;j<ppList.size();++j){
			PortfolioPerformance pp = ppList.get(j);
			double arRating = portfolioARMap.get(pp.getPortfolioID());
			double sortinoRating = portfolioSortinoMap.get(pp.getPortfolioID());
			double sharpeRating = portfolioSharpeRatioMap.get(pp.getPortfolioID());
			double treynorRating = portfolioTreynorMap.get(pp.getPortfolioID());
			double drawdownRating = portfolioDrawDownMap.get(pp.getPortfolioID());
			double winningRating = portfolioWinningMap.get(pp.getPortfolioID());
			pp.setARRating(arRating);
			pp.setSortinoRating(sortinoRating);
			pp.setSharpeRating(sharpeRating);
			pp.setTreynorRating(treynorRating);
			pp.setDrawdownRating(drawdownRating);
			pp.setWinningRating(winningRating);
			Double score = arRating * portfolioARWeight + sortinoRating * portfolioSortinoWeight 
						 + sharpeRating * portfolioSharpeWeight + treynorRating * portfolioTreynorWeight
						 + drawdownRating * portfolioDrawDownWeight + winningRating * portfolioWinningWeight;
			scoreMap.put(pp.getPortfolioID(), score);
		}
		portfolioPerformanceList.addAll(ppList);
		return scoreMap;
	}
	
	public Double calculateOnePlanConstructionCapability(List<PortfolioPerformance> ppList, Long planID){
		
		HashMap<Long, Double> portfolioARMap = new HashMap<Long, Double>();
		HashMap<Long, Double> portfolioSortinoMap = new HashMap<Long, Double>();
		HashMap<Long, Double> portfolioSharpeRatioMap = new HashMap<Long, Double>();
		HashMap<Long, Double> portfolioTreynorMap = new HashMap<Long, Double>();
		HashMap<Long, Double> portfolioDrawDownMap = new HashMap<Long, Double>();
		HashMap<Long, Double> inceptionWeightMap = new HashMap<Long, Double>();
		HashMap<Long, Double> portfolioWinningMap = new HashMap<Long, Double>();
		HashMap<Long, Integer> portfolioIDPositionMap = new HashMap<Long, Integer>();
		List<Long> portfolioIDs = new ArrayList<Long>();
		for(int i=0;i<ppList.size();++i){
			PortfolioPerformance pp = ppList.get(i);
			Long portfolioID = pp.getPortfolioID();
			portfolioIDPositionMap.put(portfolioID, i);
			portfolioIDs.add(portfolioID);
			portfolioARMap.put(portfolioID, 0.0);
			portfolioSortinoMap.put(portfolioID, 0.0);
			portfolioSharpeRatioMap.put(portfolioID, 0.0);
			portfolioTreynorMap.put(portfolioID, 0.0);
			portfolioDrawDownMap.put(portfolioID, 0.0);
			portfolioWinningMap.put(portfolioID, 0.0);
			inceptionWeightMap.put(portfolioID, 0.1);
		}
		Collections.sort(portfolioIDs);
		List<PortfolioMPT> pmpts5 = portfolioManager.getPortfolioMPTsByYear(portfolioIDs, -5);
		adjustPMInceptionWeight(portfolioIDs, pmpts5, 0.4, inceptionWeightMap);
		List<PortfolioMPT> pmpts3 = portfolioManager.getPortfolioMPTsByYear(portfolioIDs, -3);
		adjustPMInceptionWeight(portfolioIDs, pmpts3, 0.4, inceptionWeightMap);
		List<PortfolioMPT> pmpts1 = portfolioManager.getPortfolioMPTsByYear(portfolioIDs, -1);
		adjustPMInceptionWeight(portfolioIDs, pmpts1, 0.1, inceptionWeightMap);
		List<PortfolioMPT> pmpts0 = portfolioManager.getPortfolioMPTsByYear(portfolioIDs, 0);
		PortfolioMPTComparator pmptc = new PortfolioMPTComparator(SortType.ANNULIZEDRETURN);
		Collections.sort(pmpts5, pmptc);
		Collections.sort(pmpts3, pmptc);
		Collections.sort(pmpts1, pmptc);
		Collections.sort(pmpts0, pmptc);
		getPortfolioScore(pmpts5,pmpts3,pmpts1,pmpts0, portfolioARMap, inceptionWeightMap);
		pmptc.setSt(SortType.SORTINO);
		Collections.sort(pmpts5, pmptc);
		Collections.sort(pmpts3, pmptc);
		Collections.sort(pmpts1, pmptc);
		Collections.sort(pmpts0, pmptc);
		getPortfolioScore(pmpts5,pmpts3,pmpts1,pmpts0, portfolioSortinoMap, inceptionWeightMap);
		pmptc.setSt(SortType.SHARPE);
		Collections.sort(pmpts5, pmptc);
		Collections.sort(pmpts3, pmptc);
		Collections.sort(pmpts1, pmptc);
		Collections.sort(pmpts0, pmptc);
		getPortfolioScore(pmpts5,pmpts3,pmpts1,pmpts0, portfolioSharpeRatioMap, inceptionWeightMap);
		pmptc.setSt(SortType.TREYNOR);
		Collections.sort(pmpts5, pmptc);
		Collections.sort(pmpts3, pmptc);
		Collections.sort(pmpts1, pmptc);
		Collections.sort(pmpts0, pmptc);
		getPortfolioScore(pmpts5,pmpts3,pmpts1,pmpts0, portfolioTreynorMap, inceptionWeightMap);
		pmptc.setSt(SortType.DRAWDOWN);
		Collections.sort(pmpts5, pmptc);
		Collections.sort(pmpts3, pmptc);
		Collections.sort(pmpts1, pmptc);
		Collections.sort(pmpts0, pmptc);
		getPortfolioScore(pmpts5,pmpts3,pmpts1,pmpts0, portfolioDrawDownMap, inceptionWeightMap);
		//AR=0.15, Sortino=0.2,SharpeRatio=0.2,Treynor=0.15,DrawDown=0.3
		for(PortfolioPerformance pp: ppList){
			if(pp.getPlanID().equals(planID)){
				double arRating = portfolioARMap.get(pp.getPortfolioID());
				double sortinoRating = portfolioSortinoMap.get(pp.getPortfolioID());
				double sharpeRating = portfolioSharpeRatioMap.get(pp.getPortfolioID());
				double treynorRating = portfolioTreynorMap.get(pp.getPortfolioID());
				double drawdownRating = portfolioDrawDownMap.get(pp.getPortfolioID());
				pp.setARRating(arRating);
				pp.setSortinoRating(sortinoRating);
				pp.setSharpeRating(sharpeRating);
				pp.setTreynorRating(treynorRating);
				pp.setDrawdownRating(drawdownRating);
				Double score = arRating * portfolioARWeight + sortinoRating * portfolioSortinoWeight 
				 + sharpeRating * portfolioSharpeWeight + treynorRating * portfolioTreynorWeight
				 + drawdownRating * portfolioDrawDownWeight;
				portfolioPerformanceList.add(pp);
				return score;
			}
		}
		return 0.0;
	}
	
	
	
	/**
	 * @author CCD
	 * calculating one plan's coverage when a new plan insert and calculate planscore
	 */
	public void calculateOnePlanCoverage(){
		if(planScore.getStatus() == 0){
			Double coverageValue = calculateCoverageByPlanID(planScore.getPlanID());
			planScore.setCoverageValue(coverageValue);
			PlanScore geScore = strategyManager.getGEPlanScoreByValueType(coverageValue, Configuration.PLANSCORE_TYPE_COVERAGE);
			PlanScore leScore = strategyManager.getLEPlanScoreByValueType(coverageValue, Configuration.PLANSCORE_TYPE_COVERAGE);
			if(geScore == null && leScore == null)
				return;
			if(geScore == null)
				planScore.setCoverageScore(leScore.getCoverageScore());
			else if(leScore == null)
				planScore.setCoverageScore(geScore.getCoverageScore());
			else
				planScore.setCoverageScore( ( leScore.getCoverageScore() + geScore.getCoverageScore() )/2 );
		} 
	}
	
	/**
	 * @author CCD
	 * calculating one plan's fund quality when insert a new plan and calculate planscore
	 */
	public void calculateOnePlanFundQuality(){
		if(planScore.getStatus() == 0){
			Double fundQualityValue = this.calculatePlanFundQualityByPlanID(planScore.getPlanID());
			planScore.setFundQualityValue(fundQualityValue);
			PlanScore geScore = strategyManager.getGEPlanScoreByValueType(fundQualityValue, Configuration.PLANSCORE_TYPE_FUNDQUALITY);
			PlanScore leScore = strategyManager.getLEPlanScoreByValueType(fundQualityValue, Configuration.PLANSCORE_TYPE_FUNDQUALITY);
			if(geScore == null && leScore == null)
				return;
			if(geScore == null)
				planScore.setFundQualityScore(leScore.getFundQualityScore());
			else if(leScore == null)
				planScore.setFundQualityScore(geScore.getFundQualityScore());
			else
				planScore.setFundQualityScore( ( leScore.getFundQualityScore() + geScore.getFundQualityScore() )/2 );
				
		}
	}
	
	/**
	 * @author CCD
	 * calculating one plan's capability when insert a new plan and calculate planscore
	 */
	public void calculateOnePlanCapability(){
		Date today  = new Date();
		today = LTIDate.getNDaysAgo(today, 15);
		HashMap<Long, Double> capabilityScoreMap =  new HashMap<Long, Double>();
		List<KeyValuePair> kvpList = new ArrayList<KeyValuePair>();
		List<PortfolioPerformance> moderateTAAPerformanceList = new ArrayList<PortfolioPerformance>();
		List<PortfolioPerformance> moderateSAAPerformanceList = new ArrayList<PortfolioPerformance>();
		if(planScoreList!=null){
			for(int i=0;i<planScoreList.size();++i){
				boolean hasSAA=false, hasTAA=false;
				PlanScore ps = planScoreList.get(i);
				List<Portfolio> portfolios = strategyManager.getModeratePortfolios(ps.getPlanID());
				if(portfolios!=null){
					for(int j=0;j<portfolios.size();++j){
						Portfolio p = portfolios.get(j);
						if(p.getName().contains("Tactical")&&!hasTAA){
							if(p.getEndDate()!=null && LTIDate.before(today, p.getEndDate())){
								PortfolioPerformance pp = new PortfolioPerformance();
								pp.setPlanID(ps.getPlanID());
								pp.setPortfolioID(p.getID());
								moderateTAAPerformanceList.add(pp);
								hasTAA=true;
							}else
								ps.setStatus(1);
						}else if(p.getName().contains("Strategic")&&!hasSAA){
							if(p.getEndDate()!=null && LTIDate.before(today, p.getEndDate())){
								PortfolioPerformance pp = new PortfolioPerformance();
								pp.setPlanID(ps.getPlanID());
								pp.setPortfolioID(p.getID());
								moderateSAAPerformanceList.add(pp);
								hasSAA=true;
							}else
								ps.setStatus(1);
						}
					}
				}
				if(!hasTAA || !hasSAA)
					ps.setStatus(1);
				if(ps.getPlanID().equals(planScore.getPlanID())&& ps.getStatus() == 1){
					planScore.setStatus(1);
					return;
				}
			}
			Double TAAScore = null;
			Double SAAScore = null;
			if(moderateTAAPerformanceList.size()!=0)
				TAAScore = this.calculateOnePlanConstructionCapability(moderateTAAPerformanceList, planScore.getPlanID());
			if(moderateSAAPerformanceList.size()!=0)
				SAAScore = this.calculateOnePlanConstructionCapability(moderateSAAPerformanceList, planScore.getPlanID());
			if(planScore.getStatus() == 0){
				planScore.setTAAScore(TAAScore);
				planScore.setSAAScore(SAAScore);
				if(planScore.getTAAScore() == 0.0 || planScore.getSAAScore() == 0.0)
					planScore.setStatus(1);
				else{
					Double capabilityValue = planScore.getTAAScore() * planTAAWeight + planScore.getSAAScore() * planSAAWeight;
					planScore.setCapabilityValue(capabilityValue);
					PlanScore geScore = strategyManager.getGEPlanScoreByValueType(capabilityValue, Configuration.PLANSCORE_TYPE_CAPABILITY);
					PlanScore leScore = strategyManager.getLEPlanScoreByValueType(capabilityValue, Configuration.PLANSCORE_TYPE_CAPABILITY);
					if(geScore == null || leScore == null)
						return;
					if(geScore == null)
						planScore.setCapabilityScore(leScore.getCapabilityScore());
					else if(leScore == null)
						planScore.setCapabilityScore(geScore.getCapabilityScore());
					else
						planScore.setCapabilityScore( ( leScore.getCapabilityScore() + geScore.getCapabilityScore() )/2 );
				}
			}
			
		}
	}
	/**
	 * @author CCD
	 * calculating one plan's investment when insert a new plan and calculate planscore
	 */
	public void calculateOnePlanInvestment(){
		if(planScore.getStatus() == 0)
			planScore.setInvestmentScore(planScore.getFundQualityScore() * planFundQualityWeight + planScore.getCoverageScore() * planCoverageWeight + planScore.getCapabilityScore() * planCapabilityWeight);
	}
	
	public void calculateOndePlanModerateReturn(){
		if(planScore.getStatus() == 0){
			List<Portfolio> portfolios = strategyManager.getModeratePortfolios(planScore.getPlanID());
			if(portfolios!=null){
				for(int j=0;j<portfolios.size();++j){
					if(portfolios.get(j).getName().contains("Tactical")){//TAA Moderate
						Double TAAReturn = this.getFiveYearReturn(portfolios.get(j).getID());
						planScore.setTAAReturn(TAAReturn);
					}else if(portfolios.get(j).getName().contains("Strategic")){//SAA Moderate
						Double SAAReturn = this.getFiveYearReturn(portfolios.get(j).getID());
						planScore.setSAAReturn(SAAReturn);
					}
				}
			}
		}
	}
	
	public void initPlanScoreForOnePlan(Long planID){
		planScoreList = strategyManager.getPlanScore();
		boolean found = false;
		if(planScoreList!=null && planScoreList.size()>0){
			for(PlanScore ps: planScoreList){
				if(ps.getPlanID().equals(planID)){
					planScore = ps;
					found = true;
					break;
				}
			}
			if(!found){
				planScore = new PlanScore();
				Strategy plan = strategyManager.get(planID);
				planScore.setPlanName(plan.getName());
				planScore.setStatus(0);
				planScoreList.add(planScore);
			}
		}
		portfolioARWeight += portfolioWinningWeight;
		portfolioWinningWeight = 0.0;
	}
	
	public void initialForReCalculation(Long planID){
		planScore = strategyManager.getPlanScoreByPlanID(planID);
		if(planScore == null){
			Strategy plan = strategyManager.get(planID);
			planScore = new PlanScore();
			planScore.setStatus(0);
			planScore.setPlanID(planID);
			planScore.setPlanName(plan.getName());
		}else{
			planScore.setStatus(0);
		}
	}
	
	public double reCalculatePlanFundQuality(Long planID){
		initialForReCalculation(planID);
		calculateOnePlanFundQuality();
		//planScore.setInvestmentScore(0.0);
		//strategyManager.saveOrUpdatePlanScore(planScore);
		return planScore.getFundQualityScore();
	}
	
	public double reCalculatePlanCoverage(Long planID){
		initialForReCalculation(planID);
		calculateOnePlanCoverage();
		//planScore.setInvestmentScore(0.0);
		//strategyManager.saveOrUpdatePlanScore(planScore);
		return planScore.getCoverageScore();
	}
	
	public double reCalculatePlanCapability(Long planID) throws Exception{
		initPlanScoreForOnePlan(planID);
		calculateOnePlanCapability();
		//planScore.setInvestmentScore(0.0);
		//strategyManager.saveOrUpdatePlanScore(planScore);
		//portfolioManager.deletePortfolioPerformanceByPlanID(planID);
		//portfolioManager.saveAllPortfolioPerformance(portfolioPerformanceList);
		return planScore.getCapabilityScore();
	}
	
	public void calculateOnePlan(Long planID) throws Exception{
		System.out.println("intialize\n");
		initPlanScoreForOnePlan(planID);
		System.out.println("capability\n");
		calculateOnePlanCapability();
		System.out.println("coverage\n");
		calculateOnePlanCoverage();
		System.out.println("fundquality\n");
		calculateOnePlanFundQuality();
		System.out.println("investment\n");
		calculateOnePlanInvestment();
		System.out.println("moderate return\n");
		calculateOndePlanModerateReturn();
		strategyManager.saveOrUpdatePlanScore(planScore);
		portfolioManager.deletePortfolioPerformanceByPlanID(planScore.getPlanID());
		portfolioManager.saveAllPortfolioPerformance(portfolioPerformanceList);
	}
	
	public static void main(String[] args)throws Exception{
		//PlanRankingUtil pru = new PlanRankingUtil();
		//pru.rankPlan();
		//pru.calculateOnePlan(1148L);
		//pru.rankSecurity();
//		double cp = 1.088818117480537197077570319924;
		System.out.println(82.6572929621435 / 75.9146928537594 - 1);
	}
	public Double getSecurityAlphaWeight() {
		return securityAlphaWeight;
	}
	public void setSecurityAlphaWeight(Double securityAlphaWeight) {
		this.securityAlphaWeight = securityAlphaWeight;
	}
	public Double getSecurityTreynorWeight() {
		return securityTreynorWeight;
	}
	public void setSecurityTreynorWeight(Double securityTreynorWeight) {
		this.securityTreynorWeight = securityTreynorWeight;
	}
	public Double getSecurityARWeight() {
		return securityARWeight;
	}
	public void setSecurityARWeight(Double securityARWeight) {
		this.securityARWeight = securityARWeight;
	}
	public Double getPortfolioARWeight() {
		return portfolioARWeight;
	}
	public void setPortfolioARWeight(Double portfolioARWeight) {
		this.portfolioARWeight = portfolioARWeight;
	}
	public Double getPortfolioSortinoWeight() {
		return portfolioSortinoWeight;
	}
	public void setPortfolioSortinoWeight(Double portfolioSortinoWeight) {
		this.portfolioSortinoWeight = portfolioSortinoWeight;
	}
	public Double getPortfolioSharpeWeight() {
		return portfolioSharpeWeight;
	}
	public void setPortfolioSharpeWeight(Double portfolioSharpeWeight) {
		this.portfolioSharpeWeight = portfolioSharpeWeight;
	}
	public Double getPortfolioTreynorWeight() {
		return portfolioTreynorWeight;
	}
	public void setPortfolioTreynorWeight(Double portfolioTreynorWeight) {
		this.portfolioTreynorWeight = portfolioTreynorWeight;
	}
	public Double getPortfolioDrawDownWeight() {
		return portfolioDrawDownWeight;
	}
	public void setPortfolioDrawDownWeight(Double portfolioDrawDownWeight) {
		this.portfolioDrawDownWeight = portfolioDrawDownWeight;
	}
	public Double getPortfolioWinningWeight() {
		return portfolioWinningWeight;
	}
	public void setPortfolioWinningWeight(Double portfolioWinningWeight) {
		this.portfolioWinningWeight = portfolioWinningWeight;
	}
	public Double getPlanTAAWeight() {
		return planTAAWeight;
	}
	public void setPlanTAAWeight(Double planTAAWeight) {
		this.planTAAWeight = planTAAWeight;
	}
	public Double getPlanSAAWeight() {
		return planSAAWeight;
	}
	public void setPlanSAAWeight(Double planSAAWeight) {
		this.planSAAWeight = planSAAWeight;
	}
	public Double getPlanFundQualityWeight() {
		return planFundQualityWeight;
	}
	public void setPlanFundQualityWeight(Double planFundQualityWeight) {
		this.planFundQualityWeight = planFundQualityWeight;
	}
	public Double getPlanCoverageWeight() {
		return planCoverageWeight;
	}
	public void setPlanCoverageWeight(Double planCoverageWeight) {
		this.planCoverageWeight = planCoverageWeight;
	}
	public Double getPlanCapabilityWeight() {
		return planCapabilityWeight;
	}
	public void setPlanCapabilityWeight(Double planCapabilityWeight) {
		this.planCapabilityWeight = planCapabilityWeight;
	}
	public List<PlanScore> getPlanScoreList() {
		return planScoreList;
	}
	public void setPlanScoreList(List<PlanScore> planScoreList) {
		this.planScoreList = planScoreList;
	}
	public List<PortfolioPerformance> getPortfolioPerformanceList() {
		return portfolioPerformanceList;
	}
	public void setPortfolioPerformanceList(
			List<PortfolioPerformance> portfolioPerformanceList) {
		this.portfolioPerformanceList = portfolioPerformanceList;
	}
}
