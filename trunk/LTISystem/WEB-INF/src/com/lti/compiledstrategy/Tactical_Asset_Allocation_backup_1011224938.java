package com.lti.compiledstrategy;

import com.lti.Exception.Security.NoPriceException;
import com.lti.Exception.Strategy.ParameterException;
import com.lti.Exception.Strategy.VariableException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.*;

import com.lti.service.bo.*;
import java.util.*;
import com.lti.type.*;
import com.lti.type.finance.*;
import com.lti.type.executor.*;
import com.lti.util.*;
import com.tictactec.ta.lib.*;
import com.lti.util.simulator.ParameterUtil;

@SuppressWarnings({ "deprecation", "unused" })
public class Tactical_Asset_Allocation_backup_1011224938 extends SimulateStrategy{
	public Tactical_Asset_Allocation_backup_1011224938(){
		super();
		StrategyID=4938L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int SelectedPercentage;
	public void setSelectedPercentage(int SelectedPercentage){
		this.SelectedPercentage=SelectedPercentage;
	}
	
	public int getSelectedPercentage(){
		return this.SelectedPercentage;
	}
	private int MaxOfRiskyAsset;
	public void setMaxOfRiskyAsset(int MaxOfRiskyAsset){
		this.MaxOfRiskyAsset=MaxOfRiskyAsset;
	}
	
	public int getMaxOfRiskyAsset(){
		return this.MaxOfRiskyAsset;
	}
	private int[] RedemptionLimit;
	public void setRedemptionLimit(int[] RedemptionLimit){
		this.RedemptionLimit=RedemptionLimit;
	}
	
	public int[] getRedemptionLimit(){
		return this.RedemptionLimit;
	}
	private String CheckFrequency;
	public void setCheckFrequency(String CheckFrequency){
		this.CheckFrequency=CheckFrequency;
	}
	
	public String getCheckFrequency(){
		return this.CheckFrequency;
	}
	private boolean UseDefaultRedemption;
	public void setUseDefaultRedemption(boolean UseDefaultRedemption){
		this.UseDefaultRedemption=UseDefaultRedemption;
	}
	
	public boolean getUseDefaultRedemption(){
		return this.UseDefaultRedemption;
	}
	private int DefaultRedemptionLimit;
	public void setDefaultRedemptionLimit(int DefaultRedemptionLimit){
		this.DefaultRedemptionLimit=DefaultRedemptionLimit;
	}
	
	public int getDefaultRedemptionLimit(){
		return this.DefaultRedemptionLimit;
	}
	private String[] AssetClass;
	public void setAssetClass(String[] AssetClass){
		this.AssetClass=AssetClass;
	}
	
	public String[] getAssetClass(){
		return this.AssetClass;
	}
	private String[] CandidateFund;
	public void setCandidateFund(String[] CandidateFund){
		this.CandidateFund=CandidateFund;
	}
	
	public String[] getCandidateFund(){
		return this.CandidateFund;
	}
	private double CashScoreWeight;
	public void setCashScoreWeight(double CashScoreWeight){
		this.CashScoreWeight=CashScoreWeight;
	}
	
	public double getCashScoreWeight(){
		return this.CashScoreWeight;
	}
	private int MaxOfStableAsset;
	public void setMaxOfStableAsset(int MaxOfStableAsset){
		this.MaxOfStableAsset=MaxOfStableAsset;
	}
	
	public int getMaxOfStableAsset(){
		return this.MaxOfStableAsset;
	}
	private String[] MainAssetClass;
	public void setMainAssetClass(String[] MainAssetClass){
		this.MainAssetClass=MainAssetClass;
	}
	
	public String[] getMainAssetClass(){
		return this.MainAssetClass;
	}
	private int NumberOfMainRiskyClass;
	public void setNumberOfMainRiskyClass(int NumberOfMainRiskyClass){
		this.NumberOfMainRiskyClass=NumberOfMainRiskyClass;
	}
	
	public int getNumberOfMainRiskyClass(){
		return this.NumberOfMainRiskyClass;
	}
	private int NumberOfMainStableClass;
	public void setNumberOfMainStableClass(int NumberOfMainStableClass){
		this.NumberOfMainStableClass=NumberOfMainStableClass;
	}
	
	public int getNumberOfMainStableClass(){
		return this.NumberOfMainStableClass;
	}
	private double RiskProfile;
	public void setRiskProfile(double RiskProfile){
		this.RiskProfile=RiskProfile;
	}
	
	public double getRiskProfile(){
		return this.RiskProfile;
	}
	private boolean LetRiskProfileChange;
	public void setLetRiskProfileChange(boolean LetRiskProfileChange){
		this.LetRiskProfileChange=LetRiskProfileChange;
	}
	
	public boolean getLetRiskProfileChange(){
		return this.LetRiskProfileChange;
	}
	private double[] RiskProfileGroup;
	public void setRiskProfileGroup(double[] RiskProfileGroup){
		this.RiskProfileGroup=RiskProfileGroup;
	}
	
	public double[] getRiskProfileGroup(){
		return this.RiskProfileGroup;
	}
	private double[] YearsToRetireGroup;
	public void setYearsToRetireGroup(double[] YearsToRetireGroup){
		this.YearsToRetireGroup=YearsToRetireGroup;
	}
	
	public double[] getYearsToRetireGroup(){
		return this.YearsToRetireGroup;
	}
	private int CheckDate;
	public void setCheckDate(int CheckDate){
		this.CheckDate=CheckDate;
	}
	
	public int getCheckDate(){
		return this.CheckDate;
	}
	private double MinimumBuyingPercentage;
	public void setMinimumBuyingPercentage(double MinimumBuyingPercentage){
		this.MinimumBuyingPercentage=MinimumBuyingPercentage;
	}
	
	public double getMinimumBuyingPercentage(){
		return this.MinimumBuyingPercentage;
	}
	private double MinimumSellingPercentage;
	public void setMinimumSellingPercentage(double MinimumSellingPercentage){
		this.MinimumSellingPercentage=MinimumSellingPercentage;
	}
	
	public double getMinimumSellingPercentage(){
		return this.MinimumSellingPercentage;
	}
	private Long PlanID;
	public void setPlanID(Long PlanID){
		this.PlanID=PlanID;
	}
	
	public Long getPlanID(){
		return this.PlanID;
	}
	private boolean UseSMAFilter;
	public void setUseSMAFilter(boolean UseSMAFilter){
		this.UseSMAFilter=UseSMAFilter;
	}
	
	public boolean getUseSMAFilter(){
		return this.UseSMAFilter;
	}
	private int[] SMADays;
	public void setSMADays(int[] SMADays){
		this.SMADays=SMADays;
	}
	
	public int[] getSMADays(){
		return this.SMADays;
	}
	private boolean UseWMAFilter;
	public void setUseWMAFilter(boolean UseWMAFilter){
		this.UseWMAFilter=UseWMAFilter;
	}
	
	public boolean getUseWMAFilter(){
		return this.UseWMAFilter;
	}
	private int[] WMADays;
	public void setWMADays(int[] WMADays){
		this.WMADays=WMADays;
	}
	
	public int[] getWMADays(){
		return this.WMADays;
	}
	private boolean UseEMAFilter;
	public void setUseEMAFilter(boolean UseEMAFilter){
		this.UseEMAFilter=UseEMAFilter;
	}
	
	public boolean getUseEMAFilter(){
		return this.UseEMAFilter;
	}
	private int[] EMADays;
	public void setEMADays(int[] EMADays){
		this.EMADays=EMADays;
	}
	
	public int[] getEMADays(){
		return this.EMADays;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		SelectedPercentage=(Integer)ParameterUtil.fetchParameter("int","SelectedPercentage", "50", parameters);
		MaxOfRiskyAsset=(Integer)ParameterUtil.fetchParameter("int","MaxOfRiskyAsset", "2", parameters);
		RedemptionLimit=(int[])ParameterUtil.fetchParameter("int[]","RedemptionLimit", "3,3,3,3,3,3,3,3,3,3,3,0,0,0", parameters);
		CheckFrequency=(String)ParameterUtil.fetchParameter("String","CheckFrequency", "monthly", parameters);
		UseDefaultRedemption=(Boolean)ParameterUtil.fetchParameter("boolean","UseDefaultRedemption", "false", parameters);
		DefaultRedemptionLimit=(Integer)ParameterUtil.fetchParameter("int","DefaultRedemptionLimit", "3", parameters);
		AssetClass=(String[])ParameterUtil.fetchParameter("String[]","AssetClass", "US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, FIXED INCOME, FIXED INCOME, CASH,CASH,CASH", parameters);
		CandidateFund=(String[])ParameterUtil.fetchParameter("String[]","CandidateFund", "CHTVX,FOCPX, FUSVX, MSIVX, WFSVX, ^DWC, FSEMX, FIGRX, HAINX,FBNDX, VBMFX,CASH,CASH,CASH", parameters);
		CashScoreWeight=(Double)ParameterUtil.fetchParameter("double","CashScoreWeight", "1.3", parameters);
		MaxOfStableAsset=(Integer)ParameterUtil.fetchParameter("int","MaxOfStableAsset", "1", parameters);
		MainAssetClass=(String[])ParameterUtil.fetchParameter("String[]","MainAssetClass", "US EQUITY, INTERNATIONAL EQUITY, FIXED INCOME, REAL ESTATE, COMMODITIES, Emerging Market, INTERNATIONAL BONDS, High Yield Bond, CASH, BALANCE FUND", parameters);
		NumberOfMainRiskyClass=(Integer)ParameterUtil.fetchParameter("int","NumberOfMainRiskyClass", "2", parameters);
		NumberOfMainStableClass=(Integer)ParameterUtil.fetchParameter("int","NumberOfMainStableClass", "1", parameters);
		RiskProfile=(Double)ParameterUtil.fetchParameter("double","RiskProfile", "20", parameters);
		LetRiskProfileChange=(Boolean)ParameterUtil.fetchParameter("boolean","LetRiskProfileChange", "false", parameters);
		RiskProfileGroup=(double[])ParameterUtil.fetchParameter("double[]","RiskProfileGroup", "12,15,16,17,24,30,38,48,51,56,78,80,90", parameters);
		YearsToRetireGroup=(double[])ParameterUtil.fetchParameter("double[]","YearsToRetireGroup", "40,35,30,25,20,15,10,5,0,-5,-10,-12,-15", parameters);
		CheckDate=(Integer)ParameterUtil.fetchParameter("int","CheckDate", "31", parameters);
		MinimumBuyingPercentage=(Double)ParameterUtil.fetchParameter("double","MinimumBuyingPercentage", "0.03", parameters);
		MinimumSellingPercentage=(Double)ParameterUtil.fetchParameter("double","MinimumSellingPercentage", "0.03", parameters);
		PlanID=(Long)ParameterUtil.fetchParameter("Long","PlanID", "0", parameters);
		UseSMAFilter=(Boolean)ParameterUtil.fetchParameter("boolean","UseSMAFilter", "false", parameters);
		SMADays=(int[])ParameterUtil.fetchParameter("int[]","SMADays", "0", parameters);
		UseWMAFilter=(Boolean)ParameterUtil.fetchParameter("boolean","UseWMAFilter", "false", parameters);
		WMADays=(int[])ParameterUtil.fetchParameter("int[]","WMADays", "0", parameters);
		UseEMAFilter=(Boolean)ParameterUtil.fetchParameter("boolean","UseEMAFilter", "false", parameters);
		EMADays=(int[])ParameterUtil.fetchParameter("int[]","EMADays", "0", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double RiskyAllocation;
double StableAllocation;
Date StartDate;
double OriginalYearsToRetire = 0;
int[] RL;
String[] HoldList1;
String[] HoldList2;
int[] HoldListLimit1;
int[] HoldListLimit2;
String[] RiskyFunds;
String[] StableFunds;
String[] MoneyFunds;
int[] RiskyFundsLimit;
int[] StableFundsLimit;
int[] MoneyFundsLimit;
String[] CandidateFunds;
boolean Free1;
boolean Free2;
int[] InAfterDateFilter;
int[] TooVolatile;
boolean NewYear = false;
int LastActionMonth;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("RiskyAllocation: ");
		sb.append(RiskyAllocation);
		sb.append("\n");
		sb.append("StableAllocation: ");
		sb.append(StableAllocation);
		sb.append("\n");
		sb.append("StartDate: ");
		sb.append(StartDate);
		sb.append("\n");
		sb.append("OriginalYearsToRetire: ");
		sb.append(OriginalYearsToRetire);
		sb.append("\n");
		sb.append("RL: ");
		sb.append(RL);
		sb.append("\n");
		sb.append("HoldList1: ");
		sb.append(HoldList1);
		sb.append("\n");
		sb.append("HoldList2: ");
		sb.append(HoldList2);
		sb.append("\n");
		sb.append("HoldListLimit1: ");
		sb.append(HoldListLimit1);
		sb.append("\n");
		sb.append("HoldListLimit2: ");
		sb.append(HoldListLimit2);
		sb.append("\n");
		sb.append("RiskyFunds: ");
		sb.append(RiskyFunds);
		sb.append("\n");
		sb.append("StableFunds: ");
		sb.append(StableFunds);
		sb.append("\n");
		sb.append("MoneyFunds: ");
		sb.append(MoneyFunds);
		sb.append("\n");
		sb.append("RiskyFundsLimit: ");
		sb.append(RiskyFundsLimit);
		sb.append("\n");
		sb.append("StableFundsLimit: ");
		sb.append(StableFundsLimit);
		sb.append("\n");
		sb.append("MoneyFundsLimit: ");
		sb.append(MoneyFundsLimit);
		sb.append("\n");
		sb.append("CandidateFunds: ");
		sb.append(CandidateFunds);
		sb.append("\n");
		sb.append("Free1: ");
		sb.append(Free1);
		sb.append("\n");
		sb.append("Free2: ");
		sb.append(Free2);
		sb.append("\n");
		sb.append("InAfterDateFilter: ");
		sb.append(InAfterDateFilter);
		sb.append("\n");
		sb.append("TooVolatile: ");
		sb.append(TooVolatile);
		sb.append("\n");
		sb.append("NewYear: ");
		sb.append(NewYear);
		sb.append("\n");
		sb.append("LastActionMonth: ");
		sb.append(LastActionMonth);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(RiskyAllocation);
		stream.writeObject(StableAllocation);
		stream.writeObject(StartDate);
		stream.writeObject(OriginalYearsToRetire);
		stream.writeObject(RL);
		stream.writeObject(HoldList1);
		stream.writeObject(HoldList2);
		stream.writeObject(HoldListLimit1);
		stream.writeObject(HoldListLimit2);
		stream.writeObject(RiskyFunds);
		stream.writeObject(StableFunds);
		stream.writeObject(MoneyFunds);
		stream.writeObject(RiskyFundsLimit);
		stream.writeObject(StableFundsLimit);
		stream.writeObject(MoneyFundsLimit);
		stream.writeObject(CandidateFunds);
		stream.writeObject(Free1);
		stream.writeObject(Free2);
		stream.writeObject(InAfterDateFilter);
		stream.writeObject(TooVolatile);
		stream.writeObject(NewYear);
		stream.writeObject(LastActionMonth);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		RiskyAllocation=(Double)stream.readObject();;
		StableAllocation=(Double)stream.readObject();;
		StartDate=(Date)stream.readObject();;
		OriginalYearsToRetire=(Double)stream.readObject();;
		RL=(int[])stream.readObject();;
		HoldList1=(String[])stream.readObject();;
		HoldList2=(String[])stream.readObject();;
		HoldListLimit1=(int[])stream.readObject();;
		HoldListLimit2=(int[])stream.readObject();;
		RiskyFunds=(String[])stream.readObject();;
		StableFunds=(String[])stream.readObject();;
		MoneyFunds=(String[])stream.readObject();;
		RiskyFundsLimit=(int[])stream.readObject();;
		StableFundsLimit=(int[])stream.readObject();;
		MoneyFundsLimit=(int[])stream.readObject();;
		CandidateFunds=(String[])stream.readObject();;
		Free1=(Boolean)stream.readObject();;
		Free2=(Boolean)stream.readObject();;
		InAfterDateFilter=(int[])stream.readObject();;
		TooVolatile=(int[])stream.readObject();;
		NewYear=(Boolean)stream.readObject();;
		LastActionMonth=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	double version = 1.0;
private Map<String, List<String>> _storeData=null;
private Map<String,Map<String,Integer>> _holdingInfo = null;
private boolean needWriteTargetData = false;
private boolean needWriteHoldingData = false;

@Override
  public void afterExecuting()   throws Exception {
    // TODO Auto-generated method stub
    super.afterExecuting();
    if(UseSMAFilter || UseWMAFilter || UseEMAFilter)
      needWriteTargetData = false;
    if(needWriteTargetData)
        com.lti.util.PersistentUtil.writeGlobalObject(_storeData, "TAA_Data_Plan_" + PlanID,PlanID,PortfolioID);
    if(needWriteHoldingData)
        com.lti.util.PersistentUtil.writeGlobalObject(_holdingInfo, "HoldingInfoPlan_" + PlanID,PlanID,PortfolioID);
  }
  @Override
  public void afterUpdating()   throws Exception {
    super.afterUpdating();
    if(UseSMAFilter || UseWMAFilter || UseEMAFilter)
      needWriteTargetData = false;
    if(needWriteTargetData)
        com.lti.util.PersistentUtil.writeGlobalObject(_storeData, "TAA_Data_Plan_" + PlanID,PlanID,PortfolioID);
    if(needWriteHoldingData)
        com.lti.util.PersistentUtil.writeGlobalObject(_holdingInfo, "HoldingInfoPlan_" + PlanID,PlanID,PortfolioID);
  }
    

public static String getStackTraceString(Throwable e,int depth)   throws Exception {
    StackTraceElement[] ste = e.getStackTrace();
    StringBuffer sb = new StringBuffer();
    sb.append(e.getMessage() + "\n");
    for (int i = 0; i < ste.length; i++) {
      if(i>=depth)break;
      sb.append("\t" + ste[i].toString() + "\n");
    }
    return sb.toString();
  }

    
public Object[] rankScores(String[] key, double[] score, int rankNumber) throws Exception {
    double MaxScore;
    int MaxScoreIndex;
    for (int i = 0; i <= rankNumber - 1; i++) {

      MaxScore = score[i];
      MaxScoreIndex = i;

      for (int j = i + 1; j <= score.length - 1; j++) {
        if (MaxScore <= score[j]) {
          MaxScore = score[j];
          MaxScoreIndex = j;
        }
      }

      if (MaxScoreIndex != i) {
        String tmp = key[MaxScoreIndex];
        key[MaxScoreIndex] = key[i];
        key[i] = tmp;

        double t = score[MaxScoreIndex];
        score[MaxScoreIndex] = score[i];
        score[i] = t;
      }
    }
    Object[] obj = new Object[2];
    obj[0] = key;
    obj[1] = score;
    return obj;
}  
    

  /**
   * Plan参数
   */
  private int[] _RoundtripLimit = null;
  /**
   * Plan参数
   */
  private int[] _WaitingPeriod = null;

  /**
   * 剔除重复的candidate fund
   *
   * @param CandidateFund
   * @param RedemptionLimit
   * @param WaitingPeriod
   * @param RoundtripLimit
   * @return @
   */
  public Map<String, CandidateFundType> BuildCandidateFundMap(int[] WaitingPeriod, int[] RoundtripLimit)    throws Exception {
    Map<String, CandidateFundType> map = new HashMap<String, CandidateFundType>();
//printToLog("the whole InAfterDateFilter : " + InAfterDateFilter);
    for (int i = 0; i < CandidateFund.length; i++) {
      String symbol = CandidateFund[i];
      CandidateFundType cft = map.get(symbol);
      if (cft == null) {
        cft = new CandidateFundType();
        cft.setIndex(i);  
        cft.setSymbol(symbol);
        cft.setSecurity(getSecurity(symbol));
        //printToLog("Symbol : " + symbol);
        //printToLog("InAfterDateFilter  = " + InAfterDateFilter[i]);
        //cft.setInAfterDateFilter(InAfterDateFilter[i] == 1);
        cft.setTooSmallVolume(false);
        cft.setTooVolatile(false);
        
        try {
          cft.setAssetClass(_getAssetClass(cft.getSecurity().getClassID()));
        } catch (Exception e) {
          cft.setAssetClass(_getAssetClass(0l));
        }
        
        try {
          cft.setBenchmark(getSecurity(cft.getAssetClass().getBenchmarkID()));
        } catch (Exception e) {
        }

        if (UseDefaultRedemption) {
          if (symbol.equals("CASH")) {
            cft.setRedemptionLimit(0);
          } else {
            cft.setRedemptionLimit(DefaultRedemptionLimit);
          }
        } else {
          cft.setRedemptionLimit(RedemptionLimit[i]);
        }

        if (_RoundtripLimit!= null)
        cft.setRoundtripLimit(RoundtripLimit[i]);

        if (_WaitingPeriod!= null)
        cft.setWaitingPeriod(WaitingPeriod[i]);
  
        map.put(symbol, cft);
      } else {

      }
    }

    // Add CASH when the CandidateFund not contains CASH
    if (map.get("CASH") == null) {
      CandidateFundType cft = new CandidateFundType();
      cft.setSymbol("CASH");
      cft.setIndex(CandidateFund.length);
      cft.setSecurity(getSecurity("CASH"));
      cft.setAssetClass(_getAssetClass(0l));
      map.put("CASH", cft);
    }

    return map;

  }

  private Map<Long, AssetClass> _assetClasses = new HashMap<Long, AssetClass>();

  public AssetClass _getAssetClass(Long id)  throws Exception {
    AssetClass ac = _assetClasses.get(id);
    if (ac == null) {
      ac = assetClassManager.get(id);
      if (ac != null)
        _assetClasses.put(id, ac);
    }

    return ac;
  }

  private Map<String, AssetClass> _assetClasses2 = new HashMap<String, AssetClass>();

  public AssetClass _getAssetClass(String name)  throws Exception {
    AssetClass ac = _assetClasses2.get(name);
    if (ac == null) {
      ac = assetClassManager.get(name);
      if (ac != null)
        _assetClasses2.put(name, ac);
    }

    return ac;
  }

  // getVolume function ( used in the Function:
  // Choose_Available_CandidateFunds )
  public Long getMonthVolume(Long id, Date endDate)  throws Exception {
    Date startDate = LTIDate.getNewNYSEMonth(endDate, -3);
    String sql = "select sum(volume)*1.0/count(*) from ltisystem_securitydailydata where securityid=" + id;

    sql += " and date >= '" + _df.format(startDate) + "'";
    sql += " and date <= '" + _df.format(endDate) + "'";

    java.math.BigDecimal v = (java.math.BigDecimal) securityManager.findBySQL(sql).get(0);

    return v.longValue();
    /*
     * List<SecurityDailyData> dailyDataList =
     * securityManager.getDailyDatas(id, startDate, endDate); if
     * (dailyDataList != null && dailyDataList.size() > 0) { for (int i = 0;
     * i < dailyDataList.size(); ++i) { amount +=
     * dailyDataList.get(i).getVolume(); } amount /= dailyDataList.size(); }
     * return amount;
     */
  }
  private Map<String,Double> _fundmpt=null;
  
  @SuppressWarnings("unchecked")
public void initmpts(Map<String, CandidateFundType> CandidateFundMap)  throws Exception {
    _fundmpt=new HashMap<String, Double>();
    Iterator<String> iter = CandidateFundMap.keySet().iterator();
    List<Long> ids=new ArrayList<Long>(CandidateFundMap.size()*2);
    while (iter.hasNext()) {
      String symbol = iter.next();
      CandidateFundType candidateFund = CandidateFundMap.get(symbol);

      Security Fund = candidateFund.getSecurity();
      if (Fund == null || symbol.equals("SSSFX")) {
        continue;
      }

      // Omit those funds that DO NOT Have ParentClassID
      Security Bench = candidateFund.getBenchmark();
      if (Bench == null){
        continue;
      }
      if(!ids.contains(Fund.getID()))ids.add(Fund.getID());
      if(!ids.contains(Bench.getID()))ids.add(Bench.getID());
    }
    StringBuffer sql=new StringBuffer();
    sql.append("select securityid,year,standarddeviation from ltisystem_securitympt sm where sm.year>1900 and (");
    for(int i=0;i<ids.size();i++){
      sql.append("securityid=");
      sql.append(ids.get(i));
      if(i!=(ids.size()-1))sql.append(" or ");
    }
    sql.append(")");
    List<Object[]> mpts=securityManager.findBySQL(sql.toString());
    for(Object[] objs:mpts){
      _fundmpt.put(objs[0]+"."+objs[1], (Double)objs[2]);
    }
    
  }
  
  /**
   * 筛�?Candidate fund
   *
   * @param CandidateFundMap
   * @param isYear
   * @return @
   */
  public Map<String, CandidateFundType> ChooseAvailableCandidate(Map<String, CandidateFundType> CandidateFundMap)   throws Exception {

    Map<String, CandidateFundType> newMap = new HashMap<String, CandidateFundType>();

    Security Fund = null;
    Security Bench = null;

    int LastYear = getCurrentYear() - 1;

    Iterator<String> iter = CandidateFundMap.keySet().iterator();
    while (iter.hasNext()) {
      String symbol = iter.next();
      //printToLog("Candidate Fund Map contains : " +  symbol);
      CandidateFundType candidateFund = CandidateFundMap.get(symbol);

      boolean PutThisFund = true;
      boolean BenchHasPrice = true;
      Fund = candidateFund.getSecurity();
      if (Fund == null || symbol.equals("SSSFX")) {
        continue;
      }

      try {
        if (Fund.getIsClosed() && LTIDate.equals(Fund.getEndDate(), CurrentDate))
          continue;
      } catch (Exception e3) {
        printToLog(symbol + "'s EndDate is probably null");
        continue;
      }

      // Omit those funds that DO NOT Have ParentClassID
      Bench = candidateFund.getBenchmark();
      if (Bench == null)
        continue;
      // Omit those funds that DO NOT have currentprice
      try {
        Fund.getAdjClose(CurrentDate);
      } catch (Exception e) {
        continue;
      }

      try {
        Bench.getAdjClose(CurrentDate);
      } catch (Exception e) {
        BenchHasPrice = false;
      }

      if (SimulateHolding.getHoldingItem(symbol) != null ) {
        newMap.put(symbol, candidateFund);
        PutThisFund = false;
        // continue;   should not continue, has to judge if it is short history
       }

      if (LTIDate.before(Fund.getStartDate(), LTIDate.getNewNYSEMonth(CurrentDate, -12))) {
       // candidateFund.setInAfterDateFilter(true);

        // Omit those Funds that 12-month S.D> 1.5*Benchmark (Version
        // 2010.02.04)
        if (LTIDate.before(Bench.getStartDate(), LTIDate.getNewNYSEMonth(CurrentDate, -12))) {
          if (!NewYear && candidateFund.isTooVolatile()) {
            PutThisFund = false;
            printToLog("Too Volatile ; exclude " + symbol);
            continue;
          }// end if (!NewYear && candidateFund.isTooVolatile())
          if (NewYear || !candidateFund.isChosen()) {
            Double BenchSD=_fundmpt.get(Bench.getID()+"."+LastYear);
            Double FundSD=_fundmpt.get(Fund.getID()+"."+LastYear);
            //SecurityMPT BenchMPT = securityManager.getSecurityMPT(Bench.getID(), LastYear);
            //SecurityMPT FundMPT = securityManager.getSecurityMPT(Fund.getID(), LastYear);
            if (BenchSD == null) {
              BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
            }
            if (FundSD == null) {
              FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
            }
            if (FundSD == 0) // When the start date of the fund is
              // the last day of the year, it SD
              // equals 0
              FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);

            if (FundSD > BenchSD * 1.5) {
              PutThisFund = false;
              candidateFund.setTooVolatile(true);
              printToLog("Too Volatile ; exclude " + symbol);
              continue;
            }
          }
        } else
          printToLog("Benchmark's history prices is not long enough. " + symbol + " is included no matter of SD");

        // Omit those ETF that Average 3-Month Volume are less than
        // 200k;Configuration.SECURITY_TYPE_ETF=1;
        if (PutThisFund && Fund.getSecurityType() == 1) {
          int AverageVolumeMonth = 3;
          Long Volume = 0L;
          Date EndDate = CurrentDate;
          for (int j = 0; j < AverageVolumeMonth; j++) {
            Volume = Volume + getMonthVolume(Fund.getID(), EndDate);
            EndDate = LTIDate.getNewNYSEMonth(EndDate, -(j + 1));
          }
          Volume = Volume / AverageVolumeMonth;
          if (Volume < 200000) {
            PutThisFund = false;/* Testing */
            candidateFund.setTooSmallVolume(true);
            printToLog("Too Small Volume ;  exclude " + symbol);
          }
        }

        if (PutThisFund) {
          newMap.put(symbol, candidateFund);
        }
      }
      // Special Treatment towards ShortHistoryFunds , Added by WYJ on
      // 2010.05.13
      else if (LTIDate.before(Fund.getStartDate(), CurrentDate) && LTIDate.before(Bench.getStartDate(), LTIDate.getNewNYSEMonth(CurrentDate, -12)) && BenchHasPrice) {
        candidateFund.setShortHistory(true);
        newMap.put(symbol, candidateFund);
        printToLog(candidateFund.getSymbol() + " is Short History Fund .");
      }

    }// end while(iter.hasNext())

    if (newMap.get("CASH") == null) {
      CandidateFundType cft = CandidateFundMap.get("CASH");
      if (cft == null) {
        cft = new CandidateFundType();
        cft.setSecurity(getSecurity("CASH"));
        try {
          cft.setAssetClass(_getAssetClass(cft.getSecurity().getClassID()));
        } catch (Exception e) {
          cft.setAssetClass(_getAssetClass(0l));
        }
        cft.setSymbol("CASH");
      }

      newMap.put("CASH", cft);

    }
    return newMap;
  }

  private java.text.DateFormat _df = new java.text.SimpleDateFormat("yyyy-MM-dd");

  private final static String US_EQUITY = "US EQUITY";
  private final static String INTERNATIONAL_EQUITY = "INTERNATIONAL EQUITY";
  private final static String FIXED_INCOME = "FIXED INCOME";
  private final static String REAL_ESTATE = "REAL ESTATE";
  private final static String COMMODITIES = "COMMODITIES";
  private final static String EMERGING_MARKET = "Emerging Market";
  private final static String INTERNATIONAL_BONDS = "INTERNATIONAL BONDS";
  private final static String HIGH_YIELD_BOND = "High Yield Bond";
  private final static String BALANCE_FUND = "BALANCE FUND";

  private final static String RISKY_GROUP = "Risky Group";
  private final static String STABLE_GROUP = "Stable Group";

  /**
   * 把Candidate fund分到main asset class里面
   *
   * @param assetarr
   * @param candidatemap
   * @param longTermFlag
   * @return
   */
  public HashMap<String, List<CandidateFundType>> getAvailableAssetClassSet(String[] assetarr, Map<String, CandidateFundType> candidatemap, boolean longTermFlag)   throws Exception {
    HashMap<String, List<CandidateFundType>> availableAssetClassFundMap = new HashMap<String, List<CandidateFundType>>();
    HashMap<String, Boolean> availableAssetClassMap = new HashMap<String, Boolean>();
    if (assetarr != null && assetarr.length == 1 && assetarr[0].equalsIgnoreCase("ROOT")) {
      List<CandidateFundType> clist = new ArrayList<CandidateFundType>(candidatemap.size());
      Iterator<String> iter = candidatemap.keySet().iterator();
      while (iter.hasNext()) {
        String symbol = iter.next();
        clist.add(candidatemap.get(symbol));

      }
      availableAssetClassFundMap.put("ROOT", clist);
      return availableAssetClassFundMap;
    }
    boolean hasHighYieldBond = false;
    boolean hasLongTermBond = false;
    for (int i = 0; i < assetarr.length; ++i) {
      String assetClassName = assetarr[i];
      AssetClass assetClass = _getAssetClass(assetClassName);
      if (assetClass != null) {
        if (assetClassName.equalsIgnoreCase("High Yield Bond"))
          hasHighYieldBond = true;
        if (longTermFlag) {
          if (assetClassName.equalsIgnoreCase("Long-Term Bond"))
            hasLongTermBond = true;
        }
        availableAssetClassMap.put(assetClass.getName(), Boolean.TRUE);
      }
    }
    Iterator<String> iter = candidatemap.keySet().iterator();
    while (iter.hasNext()) {
      String symbol = iter.next();
      CandidateFundType candidateFundType = candidatemap.get(symbol);
      //if(candidateFundType ==null){
      //  printToLog("Error, "+symbol,candidatemap);
      //  continue;
      //}
      Security security = candidateFundType.getSecurity();
      if (security == null || security.getClassID() == null)
        continue;
      AssetClass assetClass = candidateFundType.getAssetClass();

      String assetClassName = assetClass.getName();
      String lowerCaseName = assetClassName.toLowerCase();
      if (symbol.equalsIgnoreCase("CASH")) {
        assetClassName = "CASH";
      } else if (assetClassName.equalsIgnoreCase("EQUITY")) {
        assetClassName = "US EQUITY";
      } else if (hasLongTermBond && lowerCaseName.contains("long") && !assetClassName.equalsIgnoreCase("Long-Short")) {
        assetClassName = "Long-Term Bond";
      } else if (hasHighYieldBond && lowerCaseName.contains("high yield")) {
        assetClassName = "High Yield Bond";
      } else {
        while (!availableAssetClassMap.containsKey(assetClassName) && !assetClassName.equalsIgnoreCase("ROOT")) {
          assetClass = _getAssetClass(assetClass.getParentID());
          assetClassName = assetClass.getName();
          lowerCaseName = assetClassName.toLowerCase();
          if (hasLongTermBond && lowerCaseName.contains("long") && !assetClassName.equalsIgnoreCase("Long-Short")) {
            assetClassName = "Long-Term Bond";
            break;
          } else if (hasHighYieldBond && lowerCaseName.contains("high yield")) {
            assetClassName = "High Yield Bond";
            break;
          }
        }
      }
      if (availableAssetClassMap.containsKey(assetClassName) || assetClassName.equals("CASH")) {
        List<CandidateFundType> availFundList = availableAssetClassFundMap.get(assetClassName);
        if (availFundList != null)
          availFundList.add(candidateFundType);
        else {
          availFundList = new ArrayList<CandidateFundType>();
          availFundList.add(candidateFundType);
          availableAssetClassFundMap.put(assetClassName, availFundList);
        }
      }
    }
    return availableAssetClassFundMap;
  }

  /**
   * 把Available asset class分成risky和stable
   *
   * @param map
   * @param NumberOfMainRiskyClass
   * @return @
   */
  public Object[] DivideAssetClass(Map<String, List<CandidateFundType>> map, int _NumberOfMainRiskyClass)   throws Exception {
    String[] AvailableAssets = new String[map.size()];
    int[] IndexOfStable = new int[AvailableAssets.length];
    Iterator<String> iterator = map.keySet().iterator();
    int n1 = 0;
    int n2 = 0;
    printToLog("-------------Available Assets  ----------------------- ");
    while (iterator.hasNext()) {
      String key = (String) iterator.next();
      if (key.equals("FIXED INCOME") || key.equals("INTERNATIONAL BONDS") || key.equals("CASH")) {
        IndexOfStable[n1] = 1;
        n2++;
      } else
        IndexOfStable[n1] = 0;
      AvailableAssets[n1] = key;
      n1++;
      printToLog("Asset " + n1 + " :  " + key);
    }

    if (n1 - n2 < _NumberOfMainRiskyClass)
      _NumberOfMainRiskyClass = n1 - n2; // Avoid keep selecting stable
    // assets in the risky group,
    // added by WYJ on 2010.05.10

    String[] RiskyAssets = new String[n1 - n2];
    String[] StableAssets = new String[n2];
    n1 = 0;
    n2 = 0;
    for (int i = 0; i < map.size(); i++) {
      if (IndexOfStable[i] == 0) {
        RiskyAssets[n1] = AvailableAssets[i];
        n1++;
      } else {
        StableAssets[n2] = AvailableAssets[i];
        n2++;
      }
    }
    printToLog("AvailableAssetClassList initialize ok");

    Object[] NewObject = new Object[4];
    NewObject[0] = AvailableAssets;
    NewObject[1] = RiskyAssets;
    NewObject[2] = StableAssets;
    NewObject[3] = _NumberOfMainRiskyClass;
    return NewObject;
  }

  public List<String> getSelectedAssetList(double[] score, String[] CandidateFunds, String[] StableFunds, int SelectedPercentage, int MaxOfRiskyGroup, int MaxOfStableGroup, Date CurrentDate) throws Exception {
    /* Rank the momentum scores */

    int k = CandidateFunds.length;
    for (int i = 0; i < CandidateFunds.length; i++) {
      if (score[i] == -100000)
        k -= 1;
    }
    int TargetSize = (int) java.lang.Math.floor((double) k * SelectedPercentage / 100);
    TargetSize = TargetSize > 1 ? TargetSize : 1;
    TargetSize = TargetSize > MaxOfRiskyGroup ? MaxOfRiskyGroup : TargetSize;

    //printToLog("Candidates (before ranking) : " + CandidateFunds);
    
    Object[] obj = rankScores(CandidateFunds, score ,TargetSize);
    CandidateFunds = (String[])obj[0];
    score = (double[])obj[1];

    //printToLog("Candidates (after ranking) : " + CandidateFunds);
    /* Temp Testing */
      String log_m = "Ranked Score" + CurrentDate.toString() + "\r\n";
      for (int i = 0; i < CandidateFunds.length; i++)
        log_m += CandidateFunds[i] + "  " + score[i] + "\r\n";
      printToLog(log_m);

    List<String> SelectedAssets = new ArrayList<String>();
    int RiskyNumber = TargetSize;
    int StableNumber = StableFunds.length > MaxOfStableGroup ? MaxOfStableGroup : StableFunds.length;
    //printToLog(" MaxOfRiskyGroup = " + MaxOfRiskyGroup + " MaxOfStableGroup = " + MaxOfStableGroup);
    //printToLog("TargetSize = " + TargetSize + " RiskyNumber = " + RiskyNumber + " StableNumber = " + StableNumber);
    int n1 = 0;
    int n2 = 0;
    int n3 = 0;
    boolean Add = true;
    boolean IsRisky = true;
    while (Add) {
      SelectedAssets.add(CandidateFunds[n1]);
      for (int i = 0; i < StableFunds.length; i++)
        if (CandidateFunds[n1].equals(StableFunds[i])) {
          n3++;
          IsRisky = false;
          break;
        }
      if (IsRisky) {
        n2++;
      }
      IsRisky = true;

      if (CandidateFunds[n1].equals("CASH") || n2 == RiskyNumber || n3 == StableNumber || n1 == CandidateFunds.length - 1)
        Add = false;
      n1++;
    }

    return SelectedAssets;
  }

  /**
   * 依据momentum分别选择risky group和stable group的Asset Class
   *
   * @param AvailableAssets
   * @param StableAssets
   * @param CashScoreWeight
   * @param SelectedPercentage
   * @param NumberOfMainRiskyClass
   * @param NumberOfMainStableClass
   * @return @
   */
  public Object[] SelectAssetClass(String[] AvailableAssets, String[] RiskyAssets, String[] StableAssets, double CashScoreWeight, int SelectedPercentage, int _NumberOfMainRiskyClass, int _NumberOfMainStableClass, Map<String, CandidateFundType> CandidateFundMap)  throws Exception {
    Security[] AssetBenchmarks = new Security[AvailableAssets.length];
    for (int i = 0; i < AvailableAssets.length; i++)
      AssetBenchmarks[i] = getSecurity(assetClassManager.get(AvailableAssets[i]).getBenchmarkID());

    double[] Score = new double[AvailableAssets.length];
    double[] tempScore = new double[AvailableAssets.length];

    Score = getMomentumScore(AssetBenchmarks, CashScoreWeight, CurrentDate);
    for (int i = 0; i < AvailableAssets.length; i++)
    {
      tempScore[i] = Score[i];
      printToLog(AvailableAssets[i] + "'s score = " + Score[i]);
    }

    // Risky Group selection
    List<String> RiskyGroupSelect = getSelectedAssetList(Score, AvailableAssets, StableAssets, SelectedPercentage, _NumberOfMainRiskyClass, _NumberOfMainStableClass, CurrentDate);
    printToLog("Risky Group selection on " + CurrentDate.toString() + "  =  " + RiskyGroupSelect);

    // Stable Group selection
    for (int i = 0; i < AvailableAssets.length; i++)
      if (AvailableAssets[i].equals("FIXED INCOME") || AvailableAssets[i].equals("INTERNATIONAL BONDS") || AvailableAssets[i].equals("CASH"))
        continue;
      else
        Score[i] = -10000;

    List<String> StableGroupSelect = getSelectedAssetList(Score, AvailableAssets, StableAssets, SelectedPercentage, _NumberOfMainRiskyClass, _NumberOfMainStableClass, CurrentDate);
    printToLog("Stable Group selection on " + CurrentDate.toString() + "  =  " + StableGroupSelect);

    // add the spare risky asset list if no risky asset is selected
    if(_is529Plan)
    {
      List<String> _risky = Arrays.asList(RiskyAssets);
      List<String> _riskySelect = new ArrayList<String>();
      _riskySelect.addAll(RiskyGroupSelect);
      _riskySelect.retainAll(_risky);
      
      boolean hasRiskyAsset = (_riskySelect.size() > 0);
      if(!hasRiskyAsset){
        RiskyGroupSelect.add("hasSpareRiskyAsset");
        int spareNumber = 0;
        Object[] obj = rankScores(AvailableAssets, tempScore, AvailableAssets.length);
        String[] rankAsset = (String[])obj[0];
        for(int i = 0; i < rankAsset.length; i++)
          if(_risky.indexOf(rankAsset[i]) != -1)
           {RiskyGroupSelect.add(rankAsset[i]); spareNumber ++;}
        printToLog("Spare Risky Asset Number = " + spareNumber);
      }
    }

    Object[] NewObject = new Object[2];
    NewObject[0] = RiskyGroupSelect;
    NewObject[1] = StableGroupSelect;
    return NewObject;
  }

  /**
   * 重复加入低风险的fund
   *
   * @param AllAssetFundsMap
   * @param StableGroupSelect
   * @return @
   */
  public Map<String, List<CandidateFundType>> AppendStableClass(Map<String, List<CandidateFundType>> AllAssetFundsMap, List<String> StableGroupSelect, Map<String, CandidateFundType> CandidateFundMap) throws Exception {
    List<String> Class2Times = new ArrayList<String>();
    List<String> Class3Times = new ArrayList<String>();
    Class3Times.add("INVESTMENT GRADE");
    Class3Times.add("Short-Term Bond");
    Class3Times.add("ULTRASHORT BOND");
    Class3Times.add("SHORT GOVERNMENT");
    Class2Times.add("Intermediate-Term Bond");
    Class2Times.add("Intermediate Government");

    // Add CASH when the CandidateFund not contains CASH
    if (CandidateFundMap.get("CASH") == null) {
      CandidateFundType cft = new CandidateFundType();
      cft.setSymbol("CASH");
      cft.setSecurity(getSecurity("CASH"));
      cft.setAssetClass(_getAssetClass(0l));
      CandidateFundMap.put("CASH", cft);
    }

    for (int i = 0; i < StableGroupSelect.size(); i++) {
      List<CandidateFundType> OriginalFundList = AllAssetFundsMap.get(StableGroupSelect.get(i).trim());
      List<CandidateFundType> TempFundList = new ArrayList<CandidateFundType>();

      if (OriginalFundList != null) {
        TempFundList.addAll(OriginalFundList);
        for (int j = 0; j < OriginalFundList.size(); j++) {
          if (Class2Times.indexOf(OriginalFundList.get(j).getAssetClass().getName()) != -1)
            TempFundList.add(OriginalFundList.get(j));
          else if (Class3Times.indexOf(OriginalFundList.get(j).getAssetClass().getName()) != -1) {
            TempFundList.add(OriginalFundList.get(j));
            TempFundList.add(OriginalFundList.get(j));
          }
        }
        TempFundList.add(CandidateFundMap.get("CASH"));
        TempFundList.add(CandidateFundMap.get("CASH"));
        TempFundList.add(CandidateFundMap.get("CASH"));
        AllAssetFundsMap.put(StableGroupSelect.get(i).trim(), TempFundList);
      }
    }
    return AllAssetFundsMap;
  }

  public int __count=0;
  public double[] getMomentumScore(Security[] Securities, double CashScoreWeight, Date CurrentDate)  throws Exception {
    double[][] returns = new double[4][Securities.length];
    int tb = 0;

    int[][] ranks = new int[4][Securities.length];
    for (int i = 0; i < Securities.length; i++) {
      Security security = Securities[i];
      for (int j = 0; j < 4; j++) {
        int month = -12;
        if (j == 1)
          month = -6;
        else if (j == 2)
          month = -3;
        else if (j == 3)
          month = -1;
        double r=0.0;
        try {
          r = security.getReturn(CurrentDate, TimeUnit.MONTHLY, month);
        } catch (Exception e) {
          r = -100000.0;
          printToLog("no return for "+security.getSymbol());
        }

        returns[j][i] = r;

      }
    }
    // 排序有重复？
    for (int i = 0; i < Securities.length; i++) {
      for (int j = 0; j < 4; j++) {
        int count = 0;
        for (int ii = 0; ii < Securities.length; ii++) {
          if (returns[j][i] >= returns[j][ii] && i != ii)
            count++;
        }
        ranks[j][i] = Securities.length - count;
        //printToLog("rank ["+j +","+ i +"] = " + ranks[j][i]);
      }
    }

    double[] scores = new double[Securities.length];
    for (int i = 0; i <= Securities.length - 1; i++) {
      double score = -100000;
      Security security = Securities[i];
      if (LTIDate.before(security.getStartDate(), LTIDate.getNewNYSEMonth(CurrentDate, -12))) {
        double a = (returns[0][i] + returns[1][i] + returns[2][i] + returns[3][i]) * 100 / 4;

        printToLog("Original Score: "+Securities[i].getSymbol()+" : "+a);

        /* Adj for Cash, * CashScoreWeight 2010.04.14 */
        if (Securities[i].equals("CASH"))
          a = a * CashScoreWeight;

        int acc = 0;
        for (int j = 0; j < 4; j++) {
          if (ranks[j][i] <= Securities.length / 2)
          {
            acc++; tb++;
            //printToLog(Securities[i] + " has bonus " + j);
          }
        }

        score = a + acc;
      }
      scores[i] = score;
      printToLog("Finanl Score: "+Securities[i].getSymbol()+" : "+ score);
    }
    //printToLog("Total bonus = " + tb);
    return scores;
  }

@SuppressWarnings( { "deprecation", "unchecked" })
public void initStoreData(String dateStr, Map<String, CandidateFundType> CandidateFundMap) throws Exception {
    if(_storeData==null ){
      try {
        _storeData = (Map<String, List<String>>) com.lti.util.PersistentUtil.readGlobalObject("TAA_Data_Plan_" + PlanID);
      } catch (Exception e1) {
       }
    }
    if(_storeData==null || PlanID == 0){
     _storeData = new HashMap<String, List<String>>();
    }
      
      // prepare Filter Data
      Map<String,Integer> SMADaysMap = null;
      Map<String,Integer> WMADaysMap = null;
      Map<String,Integer> EMADaysMap = null;
      if(UseSMAFilter){
       SMADaysMap = new HashMap<String,Integer>();
       for(int i = 0; i < CandidateFund.length; i ++)
         SMADaysMap.put(CandidateFund[i], SMADays[i]);
       SMADaysMap.put("CASH", 0);
      }
      if(UseWMAFilter){
        WMADaysMap = new HashMap<String,Integer>();
        for(int i = 0; i < CandidateFund.length; i ++)
          WMADaysMap.put(CandidateFund[i], WMADays[i]);
        WMADaysMap.put("CASH", 0);
      }
      if(UseEMAFilter){
        EMADaysMap = new HashMap<String,Integer>();
        for(int i = 0; i < CandidateFund.length; i ++)
          EMADaysMap.put(CandidateFund[i], EMADays[i]);
       EMADaysMap.put("CASH", 0);
      }
      Map<String,Boolean> filterOutMap = null;
      Map<String,CandidateFundType> adjustedFundMap = null;
      if(UseSMAFilter || UseWMAFilter || UseEMAFilter)
      {
        List<String> candidateFundList = Arrays.asList(CandidateFund);
        filterOutMap = getFilterOutMap(candidateFundList,SMADaysMap,WMADaysMap,EMADaysMap);
        printToLog("The funds been filtered out in rebalance action: " + filterOutMap);
        adjustedFundMap = new HashMap<String,CandidateFundType>();
        adjustedFundMap.putAll(CandidateFundMap);  
        Iterator<String> it = filterOutMap.keySet().iterator();
        while(filterOutMap.size() >0 && it.hasNext()){
          String key = it.next();
          adjustedFundMap.remove(key);
        }  
      }else{
        adjustedFundMap = CandidateFundMap;
      }

    int _NumberOfMainRiskyClass = NumberOfMainRiskyClass;
    int _NumberOfMainStableClass = NumberOfMainStableClass;
    boolean toCalculate = false;
    boolean finishInit = false;
    if(_storeData.get(RISKY_GROUP + "." + dateStr) == null)
      toCalculate = true;

  while(!finishInit)
  {  
    //printToLog("Beginning :  Finish InitialStoreData : " + finishInit + "  to Calculate : " + toCalculate);
    if (toCalculate) {
      printToLog("------------------ First Time Calculation ----------------------------");
      finishInit = true;
      
      // 初始化AvailableFundMap
      Map<String, CandidateFundType> AvailableFundMap = ChooseAvailableCandidate(adjustedFundMap);
    
      Map<String, List<CandidateFundType>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClass, AvailableFundMap, true);

      if (AllAssetFundsMap != null)
        AllAssetFundsMap.remove("ROOT");
    
      // Divide into Risky Group and Stable Group
      Object[] AssetClassObject = DivideAssetClass(AllAssetFundsMap, _NumberOfMainRiskyClass);
      String[] AvailableAssets = (String[]) AssetClassObject[0];
      String[] RiskyAssets = (String[]) AssetClassObject[1];
      String[] StableAssets = (String[]) AssetClassObject[2];
      _NumberOfMainRiskyClass = (Integer) AssetClassObject[3];

      // Calculate the momentum scores of benchmarks and select asset classes
      Object[] SelectClassObject = SelectAssetClass(AvailableAssets, RiskyAssets, StableAssets, CashScoreWeight, SelectedPercentage, _NumberOfMainRiskyClass, _NumberOfMainStableClass, CandidateFundMap);
      List<String> RiskyGroupSelect = (List<String>) SelectClassObject[0];
      List<String> StableGroupSelect = (List<String>) SelectClassObject[1];
    
      //get the ranked spare risky asset list when needed, 2010.10.4
      List<String> wholeRiskyGroupSelect =  new ArrayList<String>();
      wholeRiskyGroupSelect.addAll(RiskyGroupSelect);
      List<String> SpareRiskyAsset = new ArrayList<String>();
      if(_is529Plan)
      {
        int markIndex = RiskyGroupSelect.indexOf("hasSpareRiskyAsset");
        if(markIndex > -1)    // then it contains the spare risky assets
        {
          for(int i = markIndex + 1; i < RiskyGroupSelect.size(); i++)
             SpareRiskyAsset.add(RiskyGroupSelect.get(i));
          List<String> subRiskyGroupSelect = RiskyGroupSelect.subList(0,markIndex);
          RiskyGroupSelect = subRiskyGroupSelect;
        }
      }

      // Combine selected assets
      List<String> SelectedAssets = new ArrayList<String>();
      int SelectedAssetSize = RiskyGroupSelect.size() + StableGroupSelect.size();
      boolean Add = true;
      for (int i = 0; i < SelectedAssetSize; i++) {
        if (i < RiskyGroupSelect.size())
          SelectedAssets.add(RiskyGroupSelect.get(i));
        else {
          for (int j = 0; j < RiskyGroupSelect.size(); j++)
            if (StableGroupSelect.get(i - RiskyGroupSelect.size()).equals(RiskyGroupSelect.get(j))) {
              Add = false;
              break;
            }
          if (Add)
            SelectedAssets.add(StableGroupSelect.get(i - RiskyGroupSelect.size()));
        }
      }

      // Add CASH into the stable asset class, Add duplicate safe stable
      // funds into the stable classes, modified by WYJ on 2010.05.10
      AllAssetFundsMap = AppendStableClass(AllAssetFundsMap, StableGroupSelect, CandidateFundMap);
      printToLog("AllAssetFundsMap after appended : " + AllAssetFundsMap);

      /*
       * rank momentum and generate sorted funds for the selected assets to be saved
       */

      for (int i = 0; i < SelectedAssets.size() + SpareRiskyAsset.size(); i++) {
        String Asset = null;
        if(i < SelectedAssets.size())
          Asset = SelectedAssets.get(i);
        else
          Asset = SpareRiskyAsset.get(i - SelectedAssets.size());
        
        List<CandidateFundType> FundList = AllAssetFundsMap.get(Asset);
        List<CandidateFundType> _Funds = new ArrayList<CandidateFundType>();
        int SaveNumber = 5;
        SaveNumber = SaveNumber < FundList.size() ? SaveNumber : FundList.size();
        printToLog("Fund List of " + Asset + " : " + FundList);

        // Special Treatment towards ShortHistoryFunds , Added by WYJ on
        // 2010.05.13
        for (int j = 0; j < FundList.size(); j++) {
          CandidateFundType cft = FundList.get(j);
          printToLog("Fund : " + cft.getSymbol());
          if (!cft.isShortHistory())
            _Funds.add(cft);
          else {
            CandidateFundType cfttmp = new CandidateFundType();
            cfttmp.setSymbol(cft.getBenchmark().getSymbol());
            cfttmp.setSecurity(cft.getBenchmark());
            cfttmp.setSource(cft);
            _Funds.add(cfttmp);
            printToLog("Fund " + FundList.get(j) + " is replaced by PseudoFunds(its benchmark)" + cft.getSymbol() + " while ranking");
          }
        }

        double[] scores=getMomentumScore(toSecurityArray(_Funds), CashScoreWeight, CurrentDate);
        for(int ii=0;ii<scores.length;ii++){
          _Funds.get(ii).setScore(scores[ii]);
        }
       // printScore(_Funds);
        Collections.sort(_Funds, new CandidateFundComparator());
        //printScore(_Funds);

        // save sorted fund list, only the top savenumber funds, 2010.07

        String tmpkey = Asset.trim() + "." + dateStr;
        List<String> strs = new ArrayList<String>();
        for (int j = 0; j < SaveNumber; j++) {
          CandidateFundType cft = _Funds.get(j);
          String tmpsymbol = cft.getSymbol();
          if (cft.getSource() != null) {
            tmpsymbol = cft.getSource().getSymbol();
          }
          strs.add(tmpsymbol);
        }
        _storeData.put(tmpkey, strs);

      } // End of SelectedAsset Cause
      // End of generate sorted funds

      // Save the Group Selected List, may contain the spare risky Asset, so need to seperate them when scaling
      List<String> tslist = new ArrayList<String>();
      _storeData.put(STABLE_GROUP + "." + dateStr, tslist);
      needWriteTargetData = true;           // let it re-write data after update
      for (int i = 0; i < StableGroupSelect.size(); i++) {
        tslist.add(StableGroupSelect.get(i));
      }

      List<String> rslist = new ArrayList<String>();
      _storeData.put(RISKY_GROUP + "." + dateStr, rslist);
      for (int i = 0; i < wholeRiskyGroupSelect.size(); i++)
        rslist.add(wholeRiskyGroupSelect.get(i));

    } // End of the first time calculation
    else{
      finishInit = true;
      if((UseSMAFilter || UseWMAFilter || UseEMAFilter) && filterOutMap.size()>0)
      {
        List<String> ClassSelected = new ArrayList<String>();
        ClassSelected.addAll(_storeData.get(RISKY_GROUP + "." + dateStr));
        ClassSelected.addAll(_storeData.get(STABLE_GROUP + "." + dateStr));
        
        for(int i = 0; i < ClassSelected.size(); i++)
        {
          List<String> fundList = _storeData.get(ClassSelected.get(i).trim() + "." + dateStr);
          if(ClassSelected.get(i).trim().equals("hasSpareRiskyAsset") || fundList == null)
            continue;
          Iterator<String> it = filterOutMap.keySet().iterator();
          while(it.hasNext()){
            String key = it.next();
            if(fundList.indexOf(key) > -1)
              fundList.remove(fundList.indexOf(key));
          }
          if(fundList.size()==0){
            finishInit = false;
            toCalculate = true;
            printToLog("The filters empty an asset, let's re-calculate the data");
          }
        }
      }
      else{}
      if(finishInit)
        printToLog("----------------Scale Calculation for Strategy--------------------");        
    }
  //printToLog("Ending :  Finish InitialStoreData : " + finishInit + "  to Calculate : " + toCalculate);
  }     
}

  public void BuildSelectedAsset(Map<String, Double> SelectedAssetPercentagesMap, Map<String, List<String>> AssetRepresentFundMap, Map<String, Double> FundPercentageMap, Map<String, Boolean> CanNotBuyFundMap, String dateStr, Map<String, CandidateFundType> CandidateFundMap, List<String> SpareRiskyAsset, Map<String,List<String>> SpareAssetRepresentFundMap) throws Exception {
      
    // ===========================================================================================
    /*---------------------------- Target Percentage Determination Basing on Data  ------------------------------    */

    //Seperate the spare risky Asset when needed
      List<String> tmpRiskyGroupSelect = new ArrayList<String>();
      tmpRiskyGroupSelect.addAll(_storeData.get(RISKY_GROUP + "." + dateStr));  //may contain the spare risky assets
      if(_is529Plan)
      {
        int markIndex = tmpRiskyGroupSelect.indexOf("hasSpareRiskyAsset");
        if(markIndex > -1)    // then it contains the spare risky assets
        {
          for(int i = markIndex + 1; i < tmpRiskyGroupSelect.size(); i++)
             SpareRiskyAsset.add(tmpRiskyGroupSelect.get(i));
          List<String> subRiskyGroupSelect = tmpRiskyGroupSelect.subList(0,markIndex);
          tmpRiskyGroupSelect = subRiskyGroupSelect;
        }
      }
            
    // Combine selected assets and determine the asset percentages, modified
    // by WYJ in 2010.10*/
      
    int _MaxOfStableAsset = MaxOfStableAsset;
    for (int i = 0; i < tmpRiskyGroupSelect.size(); i++) {
      SelectedAssetPercentagesMap.put(tmpRiskyGroupSelect.get(i), RiskyAllocation / tmpRiskyGroupSelect.size() / 100);
    }

    double StablePerc = 0.0;
    List<String> tmpStableGroupSelect = new ArrayList<String>();
    tmpStableGroupSelect.addAll(_storeData.get(STABLE_GROUP + "." + dateStr));
    for (int i = 0; i < tmpStableGroupSelect.size(); i++) {
      if (SelectedAssetPercentagesMap.get(tmpStableGroupSelect.get(i)) != null) {
        StablePerc = (RiskyAllocation / tmpRiskyGroupSelect.size() + StableAllocation / tmpStableGroupSelect.size()) / 100;
      } else {
        StablePerc = StableAllocation / tmpStableGroupSelect.size() / 100;
      }
      SelectedAssetPercentagesMap.put(tmpStableGroupSelect.get(i), StablePerc);
    }
      printToLog("Selected Asset Percentage (After Combined) : " + SelectedAssetPercentagesMap);
      printToLog("number of funds selected from Stable Asset :" + _MaxOfStableAsset);

    // Make neccesary diversification of the stable asset classes, Added by
    // WYJ on 2010.05.08
    if (SelectedAssetPercentagesMap.size() == 1)
      _MaxOfStableAsset = 3;
    else if (SelectedAssetPercentagesMap.size() == 2)
      _MaxOfStableAsset = 2;

    double EachStableFundPec = StablePerc / _MaxOfStableAsset;
    if (EachStableFundPec > 0.67)
      _MaxOfStableAsset += 2;
    else if (EachStableFundPec > 0.34)
      _MaxOfStableAsset += 1;
    printToLog("number of funds selected from Stable Asset (After Diversification) :" + _MaxOfStableAsset + "EachStableFundPec = " + EachStableFundPec);

    if (SelectedAssetPercentagesMap.get("CASH") == null) {
      SelectedAssetPercentagesMap.put("CASH", 0.0);
    }

    /*
     * ---------------- Aggregate perferred funds and determine the target fund percentages -----------------
     */
    if (!FundPercentageMap.containsKey("CASH"))
      FundPercentageMap.put("CASH", 0.0);

    if(SpareRiskyAsset.size() > 0)     // Add spare assets into the SelectedAssetPercentagesMap in order to select preferable funds for them; need delete form the Map after selection
      for(int i = 0; i < SpareRiskyAsset.size(); i ++)
         SelectedAssetPercentagesMap.put(SpareRiskyAsset.get(i), 0.0);

    boolean NoInfo = false;               // for writeObject,2010.10
    Map<String,Integer> roundtripInfo = null;
    Map<String,Integer> waitingPeriodInfo = null;
    if(_RoundtripLimit!= null || _WaitingPeriod!= null) {
      roundtripInfo = _holdingInfo.get("Roundtrip." + dateStr);
      waitingPeriodInfo = _holdingInfo.get("Waiting." + dateStr);
      if(roundtripInfo == null && waitingPeriodInfo == null){
        roundtripInfo = new HashMap<String,Integer>();
        waitingPeriodInfo = new HashMap<String,Integer>();     
        NoInfo = true;
      }     
    }
    Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
    while (iter.hasNext()) {// asset treatment
      String selectedAsset = iter.next();
      boolean isSpareAsset = false;
      if(SpareRiskyAsset.indexOf(selectedAsset) > -1)
        isSpareAsset = true;
      int _MaxOfRiskyAsset = MaxOfRiskyAsset;
      
      List<String> SaveFundList = _storeData.get(selectedAsset.trim() + "." + dateStr);
      if (SaveFundList == null)
        SaveFundList = new ArrayList<String>();
      if (SaveFundList.size() == 0 || selectedAsset.equals("CASH"))
        SaveFundList.add("CASH");
      
      //    Calculate CanNotBuyFundList, Adj for RoundTrip Part 2 LJF 2010.07.02
      Map<String,Boolean> tempMap  = new HashMap<String,Boolean>();
      if(_RoundtripLimit!= null || _WaitingPeriod!= null)   // Some variable need to prepare
      {
         tempMap = ExcludeByRoundTripAndWaiting(SaveFundList, CandidateFundMap, NoInfo, roundtripInfo, waitingPeriodInfo);
         if(tempMap.size() > 0)
            CanNotBuyFundMap.putAll(tempMap);
      }      

      int RepresentNumber = 0;
      if (selectedAsset.equals("FIXED INCOME") || selectedAsset.equals("INTERNATIONAL BONDS"))
        RepresentNumber = _MaxOfStableAsset;
      else if (selectedAsset.equals("CASH"))
        RepresentNumber = 1;
      else if (isSpareAsset){
        double keepRiskyPerc = (double)1.0/3;
        double spareAssetPerc = (100-RiskProfile)/100 * keepRiskyPerc / NumberOfMainRiskyClass;
        while(spareAssetPerc/_MaxOfRiskyAsset < MinimumBuyingPercentage && _MaxOfRiskyAsset > 1){
          _MaxOfRiskyAsset --;
        }
        RepresentNumber = _MaxOfRiskyAsset;
      }
      else
        RepresentNumber = _MaxOfRiskyAsset;

      RepresentNumber = RepresentNumber < SaveFundList.size() ? RepresentNumber : SaveFundList.size();

      printToLog("selected fund number of " + selectedAsset + "  = " + RepresentNumber);

      List<String> RepresentFundList = new ArrayList<String>();
      double TempWeight;

      // aggregate the same representative funds, Added by WYJ on
      // 2010.05.08, modified on 05.10*/
      double Weight = SelectedAssetPercentagesMap.get(selectedAsset) / RepresentNumber;

      int MaxIndex = RepresentNumber;// Adj for Roundtrip Part 3
      // Defination LJF 2010.07.02
      for (int j = 0; j < MaxIndex; j++) {
        if (FundPercentageMap.get(SaveFundList.get(j).trim()) == null) {
          if (CanNotBuyFundMap.get(SaveFundList.get(j)) == null || SimulateHolding.getHoldingItem(SaveFundList.get(j)) != null) // Adj for RoundTrip Part 3 LJF 2010.07.02
          {
            if(RepresentFundList.indexOf(SaveFundList.get(j)) == -1)
              RepresentFundList.add(SaveFundList.get(j));
        
            if(!isSpareAsset)
            {
              FundPercentageMap.put(SaveFundList.get(j).trim(), Weight);
              printToLog(selectedAsset + " buy target :  " + SaveFundList.get(j) + "   target percentage : " + Weight);
            }
          } else { // Adj for RoundTrip Part 3 LJF 2010.07.02
            if (MaxIndex < SaveFundList.size()) {
              MaxIndex = MaxIndex + 1;
            }
            // else
            // {printToLog("LJF Important Notice: No Representive Funds selected for this asset due to Roundtrip&& Waitingperiod Limit");}
          }
        } else {
          if (RepresentFundList.indexOf(SaveFundList.get(j)) == -1)
            RepresentFundList.add(SaveFundList.get(j));// e.g, add "CASH" fund  under "CASH" Asset Class
          TempWeight = FundPercentageMap.get(SaveFundList.get(j).trim());
          TempWeight += Weight;
          FundPercentageMap.put(SaveFundList.get(j).trim(), TempWeight);
          printToLog(selectedAsset + " buy target :  " + SaveFundList.get(j) + " Cumulative target percentage : " + TempWeight);
        }
      }
      if(!isSpareAsset)
        AssetRepresentFundMap.put(selectedAsset, RepresentFundList);
      if(isSpareAsset)
        SpareAssetRepresentFundMap.put(selectedAsset, RepresentFundList);

    }// End of SelectedAsset treatment
    _holdingInfo.put("Roundtrip." + dateStr, roundtripInfo);
    _holdingInfo.put("Waiting." + dateStr, waitingPeriodInfo);
    //printToLog("waiting info after add : " + _holdingInfo.get("Waiting." + dateStr));

    printToLog("AssetRepresentFundMap when created:", AssetRepresentFundMap);
    if(SpareRiskyAsset.size() > 0)
      printToLog("Represent Fund Map for Spare Asset :" + SpareAssetRepresentFundMap);

    // Aggregate the CASH from different Assets
    Iterator<String> iter2 = SelectedAssetPercentagesMap.keySet().iterator();
    while (iter2.hasNext()) {// asset treatment
      String selectedAsset = iter2.next();
      if (!selectedAsset.equals("CASH") && SpareRiskyAsset.indexOf(selectedAsset) == -1) {
        List<String> RepreFundList = AssetRepresentFundMap.get(selectedAsset);
        int CASHFundIndex = RepreFundList.indexOf("CASH");
        if (CASHFundIndex == -1)
          continue;
        // Remove CASH from RepresentFundList
        RepreFundList.remove("CASH");
        AssetRepresentFundMap.put(selectedAsset, RepreFundList);

        double SumAssetPercent = 0;
        for (int iSecurity = 0; iSecurity < RepreFundList.size(); iSecurity++) {
          String FundSymbol = RepreFundList.get(iSecurity);
          SumAssetPercent += FundPercentageMap.get(FundSymbol);
        }
        double AddOnCASH = SelectedAssetPercentagesMap.get(selectedAsset) - SumAssetPercent;
        // Adjust CASH's AssetPercentage
        SelectedAssetPercentagesMap.put("CASH", SelectedAssetPercentagesMap.get("CASH") + AddOnCASH);
        // Adjust iAsset's AssetPercentage
        SelectedAssetPercentagesMap.put(selectedAsset, SumAssetPercent);
      }
    }

    if(CanNotBuyFundMap.size() != 0)       // adjust the target percentage of the canNotBuy Funds
        AdjustSelectedAsset(SelectedAssetPercentagesMap, AssetRepresentFundMap, FundPercentageMap, CanNotBuyFundMap, dateStr, CandidateFundMap);


    // Remove the selected asset with 0 percentage allocation

    Iterator<String> iter3 = SelectedAssetPercentagesMap.keySet().iterator();
    while (iter3.hasNext()) {
      String selectedAsset = iter3.next();
      if (SelectedAssetPercentagesMap.get(selectedAsset) <= 0) {
        AssetRepresentFundMap.remove(selectedAsset);
        //SelectedAssetPercentagesMap.remove(selectedAsset);
        iter3.remove();
      }
    }
    printToLog("SelectedAssetPercentagesMap after Remove zeros :" + SelectedAssetPercentagesMap);
  }

  private String[] toStringArray(Map<String, CandidateFundType> CandidateFundMap) {
    String[] arr = new String[CandidateFundMap.size()];
    Iterator<String> iter = CandidateFundMap.keySet().iterator();
    int i = 0;
    while (iter.hasNext()) {
      arr[i++] = iter.next();
    }
    return arr;
  }

  private Security[] toSecurityArray(List<CandidateFundType> CandidateFundMap) {
    Security[] arr = new Security[CandidateFundMap.size()];
    Iterator<CandidateFundType> iter = CandidateFundMap.iterator();
    int i = 0;
    while (iter.hasNext()) {
      arr[i++] = iter.next().getSecurity();
    }
    return arr;
  }

  public Map<String, Boolean> ExcludeByRoundTripAndWaiting(List<String> NeedTestFundList, Map<String, CandidateFundType> CandidateFundMap, boolean NoInfo, Map<String,Integer> roundtripInfo, Map<String,Integer> waitingPeriodInfo)  throws Exception {
    Map<String, Boolean> CanNotBuyFundMap = new HashMap<String, Boolean>();
    Map<String,CandidateFundType> NeedTestFundMap = new HashMap<String,CandidateFundType>();
    
    for (int i = 0; i < NeedTestFundList.size(); i++) {
      String fund = NeedTestFundList.get(i);
      //printToLog("No Info for roundtrip = " + NoInfo);
      if(NoInfo)     // When we have no data object of these two parameters yet, we assums that the cerrent fund table is the corresponding one
      {
        CandidateFundType cft = CandidateFundMap.get(fund);
        NeedTestFundMap.put(fund,cft);
        roundtripInfo.put(fund,cft.getRoundtripLimit());
        waitingPeriodInfo.put(fund, cft.getWaitingPeriod());
        printToLog(fund + "' Roundtrip limit = " + cft.getRoundtripLimit() + "  waiting period = " + cft.getWaitingPeriod());
      }
      else
      {
        CandidateFundType cft = new CandidateFundType();
        cft.setSymbol(fund);
        cft.setSecurity(getSecurity(fund));
        if(roundtripInfo.get(fund) != null)  
            cft.setRoundtripLimit(roundtripInfo.get(fund));
        else
           {cft.setRoundtripLimit(CandidateFundMap.get(fund).getRoundtripLimit()); roundtripInfo.put(fund,cft.getRoundtripLimit());}
        if(waitingPeriodInfo.get(fund) != null)
            cft.setWaitingPeriod(waitingPeriodInfo.get(fund));
        else
           {cft.setWaitingPeriod(CandidateFundMap.get(fund).getWaitingPeriod()); waitingPeriodInfo.put(fund, cft.getWaitingPeriod());}   
        NeedTestFundMap.put(fund,cft);
        printToLog(fund + "' Roundtrip limit = " + cft.getRoundtripLimit() + "  waiting period = " + cft.getWaitingPeriod());
      }
    }

    /*
     * 2.1 Detect CandidateFund, judge whether Roundtrip && waitingPeriod
     * Limitation works for the portfolio or not
     */
    boolean NeedToCalRoundtrip = false;
    boolean NeedToCalWaitingPeriod = false;
    if (_RoundtripLimit!= null) {
      Iterator<String> it =  NeedTestFundMap.keySet().iterator();
      while(it.hasNext()){
       String key = it.next();
       if (NeedTestFundMap.get(key).getRoundtripLimit() != 13) {
          NeedToCalRoundtrip = true;
          break;   
       }
      }     
     } // Default Roundtrip Limit is 13 times during latest 12 months
    if (_WaitingPeriod!= null) {
      Iterator<String> it =  NeedTestFundMap.keySet().iterator();
      while(it.hasNext()){
       String key = it.next();         
        if (NeedTestFundMap.get(key).getWaitingPeriod() != 0) {
          NeedToCalWaitingPeriod = true;
          break;
        }
      }
    }// Default WaitingPeriod is zero month(immediately) before a fund is bought back.

    /* 2.2 Tag CanNotBuyFund due to Roundtrip Limit */
    /* 2.2.1 Calculate No. of Roundtrips of CandidateFund in last 12 months */
    String[] funds = new String[NeedTestFundList.size()];
    for(int i = 0; i < NeedTestFundList.size(); i++)
        funds[i] = NeedTestFundList.get(i);

    if (NeedToCalRoundtrip) {
      //printToLog("To check RoundTrip.");
      for (int imonth = 12; imonth > 0; imonth--) {
        Date startDate = LTIDate.getNewDate(CurrentDate, TimeUnit.MONTHLY, -imonth);
        Date endDate = LTIDate.getNewDate(startDate, TimeUnit.MONTHLY, 1);

        int[] AppendArray = transactionDetection(startDate, endDate, funds); // (startDate,endDate,String[])

        for (int ifund = 0; ifund < funds.length; ifund++) {
          CandidateFundType cft = NeedTestFundMap.get(funds[ifund]);

          if (cft.getRecord() == 2 && AppendArray[ifund] == 1) {
            cft.setRecord(0);
            cft.setNoOfRoundtrip(cft.getNoOfRoundtrip() + 1);
          }
          if (cft.getRecord() == 1 && AppendArray[ifund] == 2) {
            cft.setRecord(2);
          }
          if (cft.getRecord() == 0) {
            cft.setRecord(AppendArray[ifund]);
          }
          NeedTestFundMap.put(funds[ifund],cft);
          // Notice: assume Cash has no WaitingPeriod and
          // RoundtripLimit restriction.
          if (AppendArray[ifund] == 3 && !funds[ifund].equals("CASH")) {
            printToLog("buy and sell transactions of the same fund occured within one month during &2.2-Calculate No. of Roundtrips in last 12 months " + ":  " + funds[ifund] + ". StartDate: " + startDate + ", EndDate: " + endDate);
          }
        }
      }
      /* 2.2.2Tag funds */
      for (int ifund = 0; ifund < funds.length; ifund++) {
        CandidateFundType cft = NeedTestFundMap.get(funds[ifund]);
        int iRoundtripLimit = cft.getRoundtripLimit();// get RoundtripLimit of the fund
        //printToLog(funds[ifund] + "'s roundtrip = " + cft.getNoOfRoundtrip() + "  its limit = " + iRoundtripLimit);
        if (cft.getNoOfRoundtrip() >= iRoundtripLimit) {
          CanNotBuyFundMap.put(funds[ifund], true);
        }
      }
      //printToLog("CanNotBuyFund due to RoundtripLimit: ", CanNotBuyFundMap);
    }

    /* 2.3 Tag CanNotBuyFund due to WaitingPeriod */
    if (NeedToCalWaitingPeriod) {
      //printToLog("To Check Waiting Period." + "  fund.length : " + funds.length);
      
      for (int ifund = 0; ifund < funds.length; ifund++) {
        CandidateFundType cft = NeedTestFundMap.get(funds[ifund]);
        int iWaitingPeriod = cft.getWaitingPeriod();// get Waiting Period of the fund
        Date startDate = LTIDate.getNewDate(CurrentDate, TimeUnit.MONTHLY, -iWaitingPeriod);
        int TransType = transactionDetection(startDate, CurrentDate, new String[] { funds[ifund] })[0];
        //printToLog("Historical Transaction Type : " + TransType);
        if (TransType == 3)
          printToLog("LJF Important Notice!!! startDate: " + startDate + ", CurrentDate: " + CurrentDate + ",iDuplicateFund: " + funds[ifund] + ", TransType: " + TransType);

        if (TransType >= 2) {
          CanNotBuyFundMap.put(funds[ifund], true);
          //printToLog("Add CanNotBuy Fund :" + funds[ifund]);
        }
      }
    }
    printToLog("CanNotBuyFunds are: ", CanNotBuyFundMap);
    // Adj for RoundTrip Part 2 LJF 2010.07.02 END

    return CanNotBuyFundMap;
  }

  public void AdjustSelectedAsset(Map<String, Double> SelectedAssetPercentagesMap, Map<String, List<String>> AssetRepresentFundMap, Map<String, Double> FundPercentageMap, Map<String, Boolean> CanNotBuyFundMap, String dateStr, Map<String, CandidateFundType> CandidateFundMap)   throws Exception {
    /* LJF temp testing */
    printToLog("AssetRepresentFundMap 2:", AssetRepresentFundMap);
    printToLog("LJF FundPercentageMap before Roundtrip Adj: ", FundPercentageMap);
    /*
     * 4.1 For funds in CanNotBuyFundList, Adj FundPercentageMap and
     * SelectedAssetPercentages
     */
    List<String> CanNotAdjRiskyAssetList = new ArrayList<String>();
    List<String> CanNotAdjStableAssetList = new ArrayList<String>();
    double UnAllocateRiskyPercentage = 0;
    double UnAllocateStablePercentage = 0;
    List<String> NeedTestAssetList = new ArrayList<String>();
    List<String> RepresentFundList = null;
    Map<String, List<CandidateFundType>> tmpMap = null;
    Map<String,CandidateFundType> CanNotBuyFundTypeMap = new HashMap<String,CandidateFundType>();

    Iterator<String> iter_cannot = CanNotBuyFundMap.keySet().iterator();
    while (iter_cannot.hasNext()) {
        String symbol = iter_cannot.next();
        CanNotBuyFundTypeMap.put(symbol,CandidateFundMap.get(symbol));
    }
    tmpMap = getAvailableAssetClassSet(MainAssetClass, CanNotBuyFundTypeMap,true);
    Iterator<String> it = tmpMap.keySet().iterator();
    while (it.hasNext()) {
        String asset = it.next();
        NeedTestAssetList.add(asset);
    }

   /* {
      Iterator<String> iter_cannot = CanNotBuyFundMap.keySet().iterator();
      while (iter_cannot.hasNext()) {
        String symbol = iter_cannot.next();
        CandidateFundType cft = CandidateFundMap.get(symbol);
        String assetName = cft.getAssetClass().getName();

        Iterator<String> iter_ms = SelectedAssetPercentagesMap.keySet().iterator();
        while (iter_ms.hasNext()) {
          String an = iter_ms.next();
          if (isUpperOrSameClass(an, assetName)) {
            assetName = an;
            break;
          }
        }
        if (NeedTestAssetList.indexOf(assetName) == -1) {
          NeedTestAssetList.add(assetName);
        }
      }
    }// 纯粹解决变量问题
    */

    List<String> RiskyAssets = _storeData.get(RISKY_GROUP + "." + dateStr);
    List<String> StableAssets = _storeData.get(STABLE_GROUP + "." + dateStr);

    for (int iasset = 0; iasset < NeedTestAssetList.size(); iasset++) {
      printToLog("LJF NeedTestAsset: " + NeedTestAssetList.get(iasset));
      String AssetName = NeedTestAssetList.get(iasset);
      int NoOfNormalFunds = 0;
      RepresentFundList = new ArrayList<String>();
      RepresentFundList = AssetRepresentFundMap.get(AssetName);
      double LeisurePercentageCollect = 0.0;
      // 4.1.1 If NeedTestAsset has no RepresentFund
      if (RepresentFundList == null || RepresentFundList.size() == 0) // might
      // not
      // enter
      // this
      // "if"
      {
        if (SelectedAssetPercentagesMap.get(AssetName) != null) {
          LeisurePercentageCollect = SelectedAssetPercentagesMap.get(AssetName);
          SelectedAssetPercentagesMap.put(AssetName, 0.0);
          printToLog("LJF SelectedAssetPercentages.set(" + AssetName + ")=0");
          int RiskyAssetIndex = -1;
          for (int itemp = 0; itemp < RiskyAssets.size(); itemp++) {
            if (RiskyAssets.get(itemp).equals(AssetName)) {
              RiskyAssetIndex = itemp;
              break;
            }
          }
          if (RiskyAssetIndex > -1) {
            UnAllocateRiskyPercentage += LeisurePercentageCollect;
            CanNotAdjRiskyAssetList.add(AssetName);
          } else {
            UnAllocateStablePercentage += LeisurePercentageCollect;
            CanNotAdjStableAssetList.add(AssetName);
          }
        }
        continue;
      }
      // 4.1.2 If NeedTestAsset RepresentFund!= null
      // First, Adj CanNotBuy Funds' value in the FundPercentageMap &&
      // Accumulate LeisurePercentageCollect
      for (int ifund = 0; ifund < RepresentFundList.size(); ifund++) {
        String FundName = RepresentFundList.get(ifund);
        if (CanNotBuyFundMap.get(FundName) == null) {
          NoOfNormalFunds += 1;
        } else {
          double ActualPercentage = SimulateHolding.getAsset(AssetName).getHoldingItem(FundName).getPercentage();
          if (ActualPercentage < FundPercentageMap.get(FundName)) // Detect
          // for
          // CanNotBuy
          // Funds
          // that
          // may
          // need
          // for
          // Buy
          // Rebalance
          // Action
          {
            double TempPercentage = FundPercentageMap.get(FundName);
            LeisurePercentageCollect += (TempPercentage - ActualPercentage);
            // FundPercentageMap.remove(FundName);
            FundPercentageMap.put(FundName, ActualPercentage);
            printToLog("LJF New FundPercentageMap RoundtripLimit adj 1 " + FundName + ", New Percentage: " + ActualPercentage);
          } else {
            printToLog("LJF Important Notice: CurrentHolding has this CanNotBuyFund " + FundName + ", but its actual percentage" + ActualPercentage + " > Target percentage " + FundPercentageMap.get(FundName));
          }
        }
      }
      // Second, Allocate LeisurePercentageCollect as much as possible
      // among other funds under the same asset class
      if (NoOfNormalFunds != 0 && LeisurePercentageCollect > 0) {
        double AddOn = LeisurePercentageCollect / NoOfNormalFunds;
        for (int ifund = 0; ifund < RepresentFundList.size(); ifund++) {
          String FundName = RepresentFundList.get(ifund);
          if (CanNotBuyFundMap.get(FundName) == null) {
            double TempPercentage = FundPercentageMap.get(FundName);
            // FundPercentageMap.remove(FundName);
            FundPercentageMap.put(FundName, TempPercentage + AddOn);
            printToLog("LJF New FundPercentageMap RoundtripLimit adj 2 " + FundName + ", New Percentage: " + (TempPercentage + AddOn));
          }
        }
      }
      // Third, for CanNotAdjAsset: Record LeisurePercentageCollect;Add to
      // UnAllocateRisky/StableAssetList
      if (NoOfNormalFunds == 0) {
        if (LeisurePercentageCollect > 0) {
          double Percent = SelectedAssetPercentagesMap.get(AssetName);
          SelectedAssetPercentagesMap.put(AssetName, Percent - LeisurePercentageCollect);
          printToLog("LJF Important Notice: CanNotAdjAsset && RepresentFundList!=null && RepresentFunds are CanNotBuyFund " + AssetName + ", RepresentFundList: " + RepresentFundList);
        }
        int RiskyAssetIndex = -1;
        for (int itemp = 0; itemp < RiskyAssets.size(); itemp++) {
          if (RiskyAssets.get(itemp).equals(AssetName)) {
            RiskyAssetIndex = itemp;
            break;
          }
        }
        if (RiskyAssetIndex > -1) {
          UnAllocateRiskyPercentage += LeisurePercentageCollect;
          CanNotAdjRiskyAssetList.add(AssetName);
        } else {
          UnAllocateStablePercentage += LeisurePercentageCollect;
          CanNotAdjStableAssetList.add(AssetName);
        }
      }

    }

    // 4.2 Allocate UnAllocateRiskyPercentage && UnAllocateStablePercentage
    // 4.2.1 Detect other Assets belonging to CanNotAdjRisky/StableAssetList
    for (int iasset = 0; iasset < RiskyAssets.size(); iasset++) {
      if (AssetRepresentFundMap.get(RiskyAssets.get(iasset)) == null && CanNotAdjRiskyAssetList.indexOf(RiskyAssets.get(iasset)) == -1)
        CanNotAdjRiskyAssetList.add(RiskyAssets.get(iasset));
    }
    for (int iasset = 0; iasset < StableAssets.size(); iasset++) {
      if (AssetRepresentFundMap.get(StableAssets.get(iasset)) == null && CanNotAdjStableAssetList.indexOf(StableAssets.get(iasset)) == -1)
        CanNotAdjStableAssetList.add(StableAssets.get(iasset));
    }
    CanNotAdjStableAssetList.add("CASH");// This special treatment towards
    // CASH aims to allocate
    // UnAllocateStablePercentage to
    // StableAssets other than CASH.
    // If No such other StableAsset,
    // the percentage is then
    // allocated to CASH.
    /*
     * printToLog("SelectedAssets: "+SelectedAssets);
     * printToLog("CanNotAdjRiskyAssetList:"+CanNotAdjRiskyAssetList);
     * String PrintLog="StableAssets: "; for(int
     * FI=0;FI<StableAssets.length;FI++){PrintLog+=StableAssets[FI];}
     * printToLog(PrintLog);
     * printToLog("CanNotAdjStableAssetList:"+CanNotAdjStableAssetList);
     */

    /* 4.2.2 LeisurePercentageCollect Adj among Assets */
    if (UnAllocateRiskyPercentage > 0 && (RiskyAssets.size() != CanNotAdjRiskyAssetList.size())) {
      double AddOn = UnAllocateRiskyPercentage / (RiskyAssets.size() - CanNotAdjRiskyAssetList.size());
      for (int iasset = 0; iasset < RiskyAssets.size(); iasset++) {
        String AssetName = RiskyAssets.get(iasset);
        if (CanNotAdjRiskyAssetList.indexOf(AssetName) == -1) {
          RepresentFundList = new ArrayList<String>();
          RepresentFundList = AssetRepresentFundMap.get(AssetName);
          if (RepresentFundList != null && RepresentFundList.size() != 0) {
            int NoOfNormalFund = 0;
            for (int ifund = 0; ifund < RepresentFundList.size(); ifund++) {
              if (CanNotBuyFundMap.get(RepresentFundList.get(ifund)) == null)
                NoOfNormalFund += 1;
            }

            for (int ifund = 0; ifund < RepresentFundList.size(); ifund++)
              if (CanNotBuyFundMap.get(RepresentFundList.get(ifund)) == null) {
                String FundName = RepresentFundList.get(ifund);
                double TempPercentage = FundPercentageMap.get(FundName);
                // FundPercentageMap.remove(FundName);
                FundPercentageMap.put(FundName, TempPercentage + AddOn / NoOfNormalFund);
                printToLog("LJF New FundPercentageMap RoundtripLimit adj 3 " + FundName + ", New Percentage: " + (TempPercentage + AddOn / NoOfNormalFund));
              }
            UnAllocateRiskyPercentage -= AddOn;

            double Percent = SelectedAssetPercentagesMap.get(AssetName);
            SelectedAssetPercentagesMap.put(AssetName, Percent + AddOn);
          } else {
            printToLog("An Error occured during &4.2 LeisurePercentageCollect Adj among Assets. Suspicious Asset:" + AssetName + ",  AddOn Amount:" + AddOn);
          }
        }
      }
    }
    if (UnAllocateRiskyPercentage > 0) {
      printToLog("LJF Important Notice: Additional RiskyAssetPercentage is allocated to StableAssets, cause RiskProfile not match!!!The additional Percentage =" + UnAllocateRiskyPercentage);
      UnAllocateStablePercentage += UnAllocateRiskyPercentage;
    }

    if (UnAllocateStablePercentage > 0 && (StableAssets.size() != CanNotAdjStableAssetList.size())) {
      double AddOn = UnAllocateStablePercentage / (StableAssets.size() - CanNotAdjStableAssetList.size());
      for (int iasset = 0; iasset < StableAssets.size(); iasset++) {
        String AssetName = StableAssets.get(iasset);
        if (CanNotAdjStableAssetList.indexOf(AssetName) == -1) {
          RepresentFundList = new ArrayList<String>();
          RepresentFundList = AssetRepresentFundMap.get(AssetName);
          if (RepresentFundList != null && RepresentFundList.size() != 0) {
            int NoOfNormalFund = 0;
            for (int ifund = 0; ifund < RepresentFundList.size(); ifund++) {
              if (CanNotBuyFundMap.get(RepresentFundList.get(ifund)) == null)
                NoOfNormalFund += 1;
            }
            for (int ifund = 0; ifund < RepresentFundList.size(); ifund++)
              if (CanNotBuyFundMap.get(RepresentFundList.get(ifund)) == null) {
                String FundName = RepresentFundList.get(ifund);
                double TempPercentage = FundPercentageMap.get(FundName);
                // FundPercentageMap.remove(FundName);
                FundPercentageMap.put(FundName, TempPercentage + AddOn / NoOfNormalFund);
                printToLog("LJF New FundPercentageMap RoundtripLimit adj 4 " + FundName + ", New Percentage: " + (TempPercentage + AddOn / NoOfNormalFund));
              }

            UnAllocateStablePercentage -= AddOn;
            double Percent = SelectedAssetPercentagesMap.get(AssetName);
            SelectedAssetPercentagesMap.put(AssetName, Percent + AddOn);
          } else {
            printToLog("An Error occured during &4.2 LeisurePercentageCollect Adj among Assets. Suspicious Asset:" + AssetName + ",  AddOn Amount:" + AddOn);
          }
        }
      }
    }
    if (UnAllocateStablePercentage > 0) {
      double TempPercentage = FundPercentageMap.get("CASH");
      // FundPercentageMap.remove("CASH");
      FundPercentageMap.put("CASH", TempPercentage + UnAllocateStablePercentage);
    }
    // Adj for RoundTrip Part 4 LJF 2010.07.02 END
    printToLog("LJF FundPercentageMap after Roundtrip Adj: " + FundPercentageMap);

  }
@SuppressWarnings("deprecation")
  public Object[] Adjust529SelectedAsset(Map<String, Double> SelectedAssetPercentagesMap, Map<String, List<String>> AssetRepresentFundMap, Map<String, Double> FundPercentageMap, String dateStr, Map<String, CandidateFundType> CandidateFundMap, int ToRebalance ,List<String> SpareRiskyAsset, Map<String,List<String>> SpareAssetRepresentFundMap)  throws Exception {
    // 1.Judge whether there are already sell/buy signals during this year,
    // if the answer is Yes, goto 4;
    // 2.Decide whether to trigger the Rebalance for this year NOW, if the
    // answer is NO, goto 4;
    // 3.Make the Reblance, with FundPercentageMap slightly changed. return;
    // 4.Endow the FundPercentageMap with CurrentSecurityPercentage's value.
    // return;
    Map<String, Double> NewSelectedAssetPercentagesMap = new HashMap<String, Double>();
    Map<String, List<String>> NewAssetRepresentFundMap = new HashMap<String, List<String>>();
    Map<String, Double> NewFundPercentageMap = new HashMap<String, Double>();

    boolean AlreadyAdjThisYear = false;
    boolean NeedToAdjNow = false;
    /* LJF temp testing */printToLog("TargetAssetPercentages:", SelectedAssetPercentagesMap);
    // 1.
    Date startDate = LTIDate.getFirstNYSETradingDayOfYear(CurrentDate);
    String[] DuplicateFund = toStringArray(CandidateFundMap);
    int[] DetectTransArray = CurrentPortfolio.transactionDetection(startDate, CurrentDate, DuplicateFund); // (startDate,endDate,String[])
    for (int Index = 0; Index < DetectTransArray.length; Index++) {
      //printToLog("DetectTrans of " + DuplicateFund[Index] + " = " + (int)DetectTransArray[Index]);
      if (DetectTransArray[Index] != 0) {
        AlreadyAdjThisYear = true;
        break;
      }
    }
    printToLog("AlreadyAdjThisYear = " + AlreadyAdjThisYear);

    // 2.
    if (!AlreadyAdjThisYear) {
      // Initialize the TriggerNo, this initial number will absolutely
      // trigger NeedToAdjNow
      int TriggerNo = MaxOfRiskyAsset;
      double JudgeWhetherLastTransactionIsType_2Trigger = 0.5;

      // get Current holding Risky Assets
      List<String> CurrRiskyAssetNameList = new ArrayList<String>();
      double CurrRiskyAmount = 0;
      List<Asset> CurrentAssetList = CurrentPortfolio.getCurrentAssetList();
      for (int iAsset = 0; iAsset < CurrentAssetList.size(); iAsset++) {
        String AssetName = CurrentAssetList.get(iAsset).getName();
        double AssetAmount = CurrentPortfolio.getAssetAmount(AssetName, CurrentDate);
        if (!AssetName.equals("FIXED INCOME") && !AssetName.equals("INTERNATIONAL BONDS") && !AssetName.equals("CASH") && AssetAmount > 0) {
          CurrRiskyAmount += AssetAmount;
          CurrRiskyAssetNameList.add(AssetName);
        }
      }
      ///* LJF temp testing */printToLog(CurrRiskyAmount / CurrentPortfolio.getTotalAmount(CurrentDate));
      ///* LJF temp testing */printToLog((100 - RiskProfile) / 100 * JudgeWhetherLastTransactionIsType_2Trigger * CurrRiskyAssetNameList.size() / MaxOfRiskyAsset);
      if (CurrRiskyAmount > 0 && CurrRiskyAmount / CurrentPortfolio.getTotalAmount(CurrentDate) < (100 - RiskProfile) / 100 * JudgeWhetherLastTransactionIsType_2Trigger * CurrRiskyAssetNameList.size() / MaxOfRiskyAsset) /*
                                                                                                                   * In
                                                                                                                   * this
                                                                                                                   * case
                                                                                                                   * ,
                                                                                                                   * ignore
                                                                                                                   * current
                                                                                                                   * risky
                                                                                                                   * assets
                                                                                                                   * because
                                                                                                                   * it
                                                                                                                   * implies
                                                                                                                   * the
                                                                                                                   * last
                                                                                                                   * transaction
                                                                                                                   * is
                                                                                                                   * Type
                                                                                                                   * 2
                                                                                                                   */
      {
        CurrRiskyAssetNameList = null;
        printToLog("During this action, we found that Last Transaction is Type 2-A combination of TargetAssets and CurrentAssets");
      }

      // get Target Risky Assets
      List<String> TargetRiskyAssetNameList = new ArrayList<String>();
      {
        Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
        while (iter.hasNext()) {
          String AssetName = iter.next();
          if (!AssetName.equals("FIXED INCOME") && !AssetName.equals("INTERNATIONAL BONDS") && !AssetName.equals("CASH")) {
            TargetRiskyAssetNameList.add(AssetName);
          }
        }
      }

      if (CurrRiskyAssetNameList == null || CurrRiskyAssetNameList.size() == 0) {
        TriggerNo = TargetRiskyAssetNameList.size();
      } else if (TargetRiskyAssetNameList == null || TargetRiskyAssetNameList.size() == 0) {
        TriggerNo = CurrRiskyAssetNameList.size();
      } else {
        for (int iAsset = 0; iAsset < CurrRiskyAssetNameList.size(); iAsset++) {
          String AssetName = CurrRiskyAssetNameList.get(iAsset);
          if (TargetRiskyAssetNameList.indexOf(AssetName) > -1) {
            TriggerNo -= 1;
          }
        }
      }
    //  /* LJF temp testing */printToLog("TriggerNo=" + TriggerNo + "; TargetRiskyAssetNameList:" + TargetRiskyAssetNameList + "; CurrRiskyAssetNameList:" + CurrRiskyAssetNameList);
    Date HalfOfYear=LTIDate.getDate(LTIDate.getYear(CurrentDate),7,1);
    Date ForceDate = null;
                if(CheckFrequency.equals("monthly"))
                    ForceDate = LTIDate.getDate(LTIDate.getYear(CurrentDate),11,1);
                if(CheckFrequency.equals("quarterly"))
                    ForceDate = LTIDate.getDate(LTIDate.getYear(CurrentDate),9,1);
                if(CheckFrequency.equals("yearly"))
                    ForceDate = LTIDate.getDate(LTIDate.getYear(CurrentDate),1,1);
    if ((TriggerNo>MaxOfRiskyAsset/2.0 && LTIDate.before(CurrentDate,HalfOfYear)) || (TriggerNo>=MaxOfRiskyAsset/3.0 && LTIDate.before(HalfOfYear,CurrentDate)) || (LTIDate.before(ForceDate,CurrentDate)) )
      {NeedToAdjNow=true;}  
    }

    // 3.
    if (NeedToAdjNow) {
      // Modify NewFundPercentageMap to a mixture of CurrentHolding and
      // TargetFund; Modify TargetAssets/TargetAssetPercentages to fit
      // NewFundPercentageMap;
      printToLog("Do NeedToAdjNow: 529 college plan " + CurrentDate);
      boolean HasRiskyAsset = false;
      {
        Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
        while (iter.hasNext()) {
          String AssetName = iter.next();
          if (!AssetName.equals("FIXED INCOME") && !AssetName.equals("INTERNATIONAL BONDS") && !AssetName.equals("CASH")) {
            HasRiskyAsset = true;
            break;
          }
        }
      }
      // remove the SpareAsset with no represent funds
      int[] removeSpareAsset = new int[SpareRiskyAsset.size()];
      for(int i = 0; i < SpareRiskyAsset.size(); i++)
      {
        if(SpareAssetRepresentFundMap.get(SpareRiskyAsset.get(i)).size() == 0)
        {
           removeSpareAsset[i] = 1;
           SpareAssetRepresentFundMap.remove(SpareRiskyAsset.get(i));
        }
      }
      for(int i = 0; i < removeSpareAsset.length; i++)
          if(removeSpareAsset[i] == 1)
            SpareRiskyAsset.remove(i);

      // Transaction Type 1, use spare risky assets
      if(!HasRiskyAsset && SpareRiskyAsset.size() > 0) {
        // add the spare asset funds
        double keepRiskyPerc = 1.0/3;
        double riskyPercentage = (100 - RiskProfile)/100 * keepRiskyPerc;
        int _NumberOfMainRiskyClass = NumberOfMainRiskyClass;
        while(riskyPercentage/_NumberOfMainRiskyClass < MinimumBuyingPercentage && _NumberOfMainRiskyClass > 1)
          _NumberOfMainRiskyClass --;
        _NumberOfMainRiskyClass = _NumberOfMainRiskyClass < SpareRiskyAsset.size() ? _NumberOfMainRiskyClass:SpareRiskyAsset.size();
 
        for(int i = 0; i < _NumberOfMainRiskyClass; i++)
        {
           String spareAsset =  SpareRiskyAsset.get(i);
           double assetPerc = riskyPercentage/_NumberOfMainRiskyClass;
           NewSelectedAssetPercentagesMap.put(spareAsset, assetPerc);
          
           List<String> spareAssetRepresentFunds = SpareAssetRepresentFundMap.get(spareAsset);
           int _MaxOfRiskyAsset = MaxOfRiskyAsset;
           while(assetPerc/_MaxOfRiskyAsset < MinimumBuyingPercentage && _MaxOfRiskyAsset > 1)
             _MaxOfRiskyAsset --;
          _MaxOfRiskyAsset = _MaxOfRiskyAsset < spareAssetRepresentFunds.size() ? _MaxOfRiskyAsset:spareAssetRepresentFunds.size();
          
           for(int j = 0; j < _MaxOfRiskyAsset; j++)
               NewFundPercentageMap.put(spareAssetRepresentFunds.get(j), assetPerc/_MaxOfRiskyAsset);
        }
        
        //add the original selected funds
        Iterator<String> iter1 = SelectedAssetPercentagesMap.keySet().iterator();
        while(iter1.hasNext())
        {
          String asset = iter1.next();
          double assetPerc = SelectedAssetPercentagesMap.get(asset);
          NewSelectedAssetPercentagesMap.put(asset, assetPerc*(1-riskyPercentage));
        }
        Iterator<String> iter2 = FundPercentageMap.keySet().iterator();
        while(iter2.hasNext())
        {
          String fund = iter2.next();
          double fundPerc = FundPercentageMap.get(fund);
          NewFundPercentageMap.put(fund, fundPerc*(1-riskyPercentage));
        }
       
        ///* LJF temp testing */printToLog("New SelectedAssets:", NewSelectedAssetPercentagesMap);
        ///* LJF Temp testing */printToLog("New FundPercentageMap 2:", NewFundPercentageMap);
      }
      else{          // Transaction Type 2, rebalance use the original percentage maps
        NewSelectedAssetPercentagesMap = SelectedAssetPercentagesMap;
        NewFundPercentageMap = FundPercentageMap;
        ///* LJF temp testing */printToLog("New SelectedAssets = " + NewSelectedAssetPercentagesMap);
        ///* LJF Temp testing */printToLog("New FundPercentageMap 1:" + NewFundPercentageMap);
      }
    }
    // 4.
    // Transaction Type 3, do not adjust now
    else {
      // Modify NewFundPercentageMap to CurrentHolding Percentages;Modify
      // TargetAssets/TargetAssetPercentages to CurrentHolding Assets;
      /* LJF temp testing */printToLog("Do not NeedToAdjNow: 529 college plan " + CurrentDate);
      double PortfolioTotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
      List<Asset> CurrentAssetList = new ArrayList<Asset>();
      CurrentAssetList = CurrentPortfolio.getCurrentAssetList();
      for (int iAsset = 0; iAsset < CurrentAssetList.size(); iAsset++) {
        double AssetAmount = CurrentPortfolio.getAssetAmount(CurrentAssetList.get(iAsset), CurrentDate);
        if (AssetAmount == 0) {
          continue;
        }
        List<HoldingItem> HoldingSecurityList = CurrentAssetList.get(iAsset).getHoldingItems();
        for (int iSecurity = 0; iSecurity < HoldingSecurityList.size(); iSecurity++) {
          String FundSymbol = HoldingSecurityList.get(iSecurity).getSymbol().trim();
          double SecurityHoldingAmount = CurrentPortfolio.getSecurityAmount(CurrentAssetList.get(iAsset).getName(), FundSymbol, CurrentDate);
          NewFundPercentageMap.put(FundSymbol, SecurityHoldingAmount / PortfolioTotalAmount);
        }
        NewSelectedAssetPercentagesMap.put(CurrentAssetList.get(iAsset).getName(), (AssetAmount / PortfolioTotalAmount));
      }
      ///* LJF temp testing */printToLog("New Selected Assets:", NewSelectedAssetPercentagesMap);
      ///* LJF Temp testing */printToLog("New FundPercentageMap 3:", NewFundPercentageMap);
    }

    
    Iterator<String> iter=NewFundPercentageMap.keySet().iterator();
    Map<String,CandidateFundType>  NewRepresentFundTypeMap = new HashMap<String,CandidateFundType>();
    Map<String,List<CandidateFundType>> tmpMap = new HashMap<String,List<CandidateFundType>>();
        while(iter.hasNext()){
            String FundSymbol=iter.next();
            if(NewFundPercentageMap.get(FundSymbol) > 0)
                NewRepresentFundTypeMap.put(FundSymbol,CandidateFundMap.get(FundSymbol));
        }

    tmpMap = getAvailableAssetClassSet(MainAssetClass, NewRepresentFundTypeMap,true);
    iter=tmpMap.keySet().iterator();
    while(iter.hasNext()){
        String key = iter.next();
        List<String> tmpList1 = new ArrayList<String>();
        List<CandidateFundType> tmpList2 = tmpMap.get(key);
        for(int i = 0; i < tmpList2.size(); i++)
          tmpList1.add(tmpList2.get(i).getSymbol());
        NewAssetRepresentFundMap.put(key,tmpList1);
    }

    ///* LJF temp testing */printToLog("New AssetRepresentFundMap:" + NewAssetRepresentFundMap);

        
    ToRebalance = 0;
    if(!AlreadyAdjThisYear && NeedToAdjNow)
        ToRebalance = 1;
    
    printToLog("New SelectedAssetPercentagesMap = " + NewSelectedAssetPercentagesMap);
    printToLog("New AssetRepresentFundMap = " + NewAssetRepresentFundMap);
    printToLog("New FundPercentageMap = " + NewFundPercentageMap);

    Object[] NewObject=new Object[4];
    NewObject[0]= ToRebalance;
    NewObject[1]= NewSelectedAssetPercentagesMap;
    NewObject[2]= NewAssetRepresentFundMap;
    NewObject[3]= NewFundPercentageMap;

    return NewObject;
  }
    
public double getWMA(String securityName,Date baseDate, int interval) throws Exception {
    Security S = getSecurity(securityName);
    double total = S.getAdjClose(baseDate) * interval;
    double WMA = 0;
    if(LTIDate.before(baseDate,LTIDate.getNewNYSETradingDay(S.getStartDate(),interval-1)))
       WMA = -100000;
    else{
      for(int i = 1; i < interval; i++)
      {
        Date date = LTIDate.getNewNYSETradingDay(baseDate, -i);
        total += S.getAdjClose(date) * (interval-1);
      }
      WMA = total / (interval*(interval+1)/2);
    }
    return WMA;    
}

public double getEMA(String securityName,Date baseDate, int interval) throws Exception {
    Security S = getSecurity(securityName);
    double a = 2.0/(1+interval);
    double EMA = 0;
    Date priceStartDate = S.getStartDate();
    Date initEMADate = LTIDate.getNewNYSETradingDay(priceStartDate, 9);
printToLog("EMA : Fund = " + securityName + "  initEMADate = " + initEMADate);
    if(LTIDate.before(baseDate,initEMADate))
      EMA = -100000;
    else{
      double initEMA = 0;
      double totalWeight = 0;
      Date date = null;
      if(LTIDate.before(initEMADate, baseDate))
      {
         EMA = S.getAdjClose(baseDate) * a;
         totalWeight = a;
         date = baseDate;
         for(int i = 1; i < 2*interval; i ++)
         {
           if(LTIDate.equals(LTIDate.getNewNYSETradingDay(baseDate, -i), initEMADate))
             break;           
           date = LTIDate.getNewNYSETradingDay(baseDate, -i);
           EMA += S.getAdjClose(date)* a * Math.pow(1-a, i);
           totalWeight += a* Math.pow(1-a, i);
           //totalWeight += a*(1-a)^i;
         }
      }      
      else if(LTIDate.equals(initEMADate, baseDate))
        date = LTIDate.getNewNYSETradingDay(initEMADate, 1);
      
      initEMA = S.getAdjClose(LTIDate.getNewNYSETradingDay(date, -1));
      for(int i = 2; i <= 10; i++)
         initEMA += S.getAdjClose(LTIDate.getNewNYSETradingDay(date,-i));  
      initEMA = initEMA/10;  
      
      EMA += initEMA * (1- totalWeight);
    }
    return EMA;   
}
  
public Map<String,Boolean> getFilterOutMap(List<String> fundList, Map<String,Integer> SMADaysMap, Map<String,Integer> WMADaysMap, Map<String,Integer> EMADaysMap)
 throws Exception {
Map<String,Boolean> filterOutMap = new HashMap<String,Boolean>();
    
    for (int j = 0; j < fundList.size(); j++) {
        String sn = fundList.get(j);
        Security S = getSecurity(sn);
        double curPrice = 0;
        try{
          curPrice = S.getAdjClose(CurrentDate);
        }catch(Exception eFilter){
          continue;
        }      
        if(UseSMAFilter && SMADaysMap.get(sn) != 0 && !LTIDate.before(CurrentDate, LTIDate.getNewNYSETradingDay(S.getStartDate(), SMADaysMap.get(sn)-1)))
            if(curPrice < S.getSMA(CurrentDate, SMADaysMap.get(sn), TimeUnit.DAILY,false))
                filterOutMap.put(sn, true);

        if(UseWMAFilter && WMADaysMap.get(sn) != 0 && !LTIDate.before(CurrentDate, LTIDate.getNewNYSETradingDay(S.getStartDate(), WMADaysMap.get(sn)-1)))
            if(curPrice < getWMA(sn, CurrentDate, WMADaysMap.get(sn)))
                filterOutMap.put(sn, true);

        if(UseEMAFilter && EMADaysMap.get(sn) != 0 && !LTIDate.before(CurrentDate, LTIDate.getNewNYSETradingDay(S.getStartDate(), 9)))             // we use 10-day SMA as initial EMA
            if(curPrice < getEMA(sn, CurrentDate, EMADaysMap.get(sn)))
                filterOutMap.put(sn, true);
    }
    return filterOutMap;
}  
  
//Adj for Roungtrip Part 0 LJF and adj for 529(actually Initial for getParameter Part)
    private boolean _is529Plan;
    private void initPlanParameters(){
      Strategy _plan=com.lti.system.ContextHolder.getStrategyManager().get(PlanID);
                         
      Map<String,String> tmp=null;                  
      if(_plan!=null)
                          tmp=_plan.getAttributes();
      printToLog("Parameters",tmp);
      
      if (tmp==null){_RoundtripLimit=null;_WaitingPeriod=null;_is529Plan=false;return;}
      
      String[] tmpRoundtrip=null;
      if(tmp.get("RoundtripLimit")!=null) tmpRoundtrip=tmp.get("RoundtripLimit").split(",");
      if (tmpRoundtrip!=null && tmpRoundtrip.length==CandidateFund.length){
        _RoundtripLimit=new int[CandidateFund.length];
        for(int i=0;i<tmpRoundtrip.length;i++) {
          _RoundtripLimit[i]=Integer.parseInt(tmpRoundtrip[i]);
        }
      }else {_RoundtripLimit=null;}
      
      String[] tmpWaiting=null;
      if(tmp.get("WaitingPeriod")!=null) tmpWaiting=tmp.get("WaitingPeriod").split(",");
      if(tmpWaiting!=null && tmpWaiting.length==CandidateFund.length){
        _WaitingPeriod=new int[CandidateFund.length];
        for (int i=0;i<tmpWaiting.length;i++){
          _WaitingPeriod[i]=Integer.parseInt(tmpWaiting[i]);
        }
      }else {_WaitingPeriod=null;} 

      if(tmp.get("is529Plan")!=null && tmp.get("is529Plan").trim().equalsIgnoreCase("true")){
        _is529Plan=true;
        printToLog("this portfolio belongs to a 529 college saving plan~compareTargetAndCurrentHolding Function will be activated~~");
        }
      else{_is529Plan=false;}

        }

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		//try{
// 初始化持久化变量
    initPlanParameters();
    LastActionMonth = getCurrentMonth() - 1;
    StableAllocation = RiskProfile;
    RiskyAllocation = 100 - RiskProfile;

    InAfterDateFilter = new int[CandidateFund.length];
    TooVolatile = new int[CandidateFund.length];
    NewYear = true;

    if(_holdingInfo == null)
    {
       try {
         _holdingInfo =  (Map<String,Map<String,Integer>>) com.lti.util.PersistentUtil.readGlobalObject("HoldingInfoPlan_" + PlanID);
       } catch (Exception e1) {}
    }
    if(_holdingInfo  == null || PlanID == 0)
       _holdingInfo = new HashMap<String,Map<String,Integer>>();


    // 初始化CandidateFundMap
    Map<String, CandidateFundType> CandidateFundMap = BuildCandidateFundMap(_WaitingPeriod, _RoundtripLimit);
    if(_fundmpt==null){
      initmpts(CandidateFundMap);
    }
    
    // 加载数据或计算数�?
    String dateStr = _df.format(CurrentDate);
    initStoreData(dateStr, CandidateFundMap);

    // 选择中的Asset的Percentage
    Map<String, Double> SelectedAssetPercentagesMap = new HashMap<String, Double>();

    // 每个Asset将要操作的fund的集�?
    Map<String, List<String>> AssetRepresentFundMap = new HashMap<String, List<String>>();

    // 每个fund的percentage
    Map<String, Double> FundPercentageMap = new HashMap<String, Double>();

    Map<String, Boolean> CanNotBuyFundMap = new HashMap<String, Boolean>();
    
    // the Spare Risky Asset List for 529 adjustment
    List<String> SpareRiskyAsset = new ArrayList<String>();
    Map<String,List<String>> SpareAssetRepresentFundMap = new HashMap<String,List<String>>();

   // 是否�?��Rebalance
   int ToRebalance = 1;

    BuildSelectedAsset(SelectedAssetPercentagesMap, AssetRepresentFundMap, FundPercentageMap, CanNotBuyFundMap, dateStr, CandidateFundMap, SpareRiskyAsset,SpareAssetRepresentFundMap);
        //printToLog("SelectedAssetPercentagesMap After Finish BuildSelectedAsset" + SelectedAssetPercentagesMap);
    //printToLog("waiting info in init action : " + _holdingInfo.get("Waiting." + dateStr));

    if (_is529Plan) {
      printToLog("Enter 529 Adjustment");
      Object[] obj = Adjust529SelectedAsset(SelectedAssetPercentagesMap, AssetRepresentFundMap, FundPercentageMap, dateStr, CandidateFundMap, ToRebalance, SpareRiskyAsset, SpareAssetRepresentFundMap);
      ToRebalance = (Integer)obj[0];
      SelectedAssetPercentagesMap = (HashMap<String,Double>)obj[1];
      AssetRepresentFundMap = (HashMap<String,List<String>>)obj[2];
      FundPercentageMap = (HashMap<String,Double>)obj[3];
    }

    //printToLog("( OutSide ) ToRebalance = " + ToRebalance + "   SelectedAssetPercentagesMap = " + SelectedAssetPercentagesMap);

if(ToRebalance == 1)
{
    double TotalAmount = SimulateHolding.getAmount();

    /* Create Assets */
    Asset CurrentAsset;

    // Create a "CASH" asset class for the buyAtNextOpen API Add on
    // 2010.04.28
    if (SelectedAssetPercentagesMap.get("CASH") == null) {
      CurrentAsset = new Asset();
      CurrentAsset.setName("CASH");
      // CurrentAsset.setClassID(getAssetClassID("CASH"));
      CurrentAsset.setTargetPercentage(0.0);
      SimulateHolding.addAsset(CurrentAsset);
    }

    /* Buy representative funds */
    double leftamount = TotalAmount;
    Iterator<String> iter4 = SelectedAssetPercentagesMap.keySet().iterator();
    while (iter4.hasNext()) {
      String selectedAsset = iter4.next();
      CurrentAsset = new Asset();
      CurrentAsset.setName(selectedAsset);
      CurrentAsset.setAssetClass(getAssetClass(selectedAsset));
      CurrentAsset.setTargetPercentage(SelectedAssetPercentagesMap.get(selectedAsset).doubleValue());
      SimulateHolding.addAsset(CurrentAsset);

      List<String> RepresentFundList = AssetRepresentFundMap.get(selectedAsset.trim());
      for (int j = 0; j < RepresentFundList.size(); j++) {
        double buyamount = TotalAmount * FundPercentageMap.get(RepresentFundList.get(j).trim()).doubleValue();
        if (leftamount < buyamount)
          buyamount = leftamount;
        if (buyamount == 0) {
          printToLog("no amount for buying " + selectedAsset + ", " + RepresentFundList.get(j) + "...");
        }
        CurrentPortfolio.buyAtNextOpen(selectedAsset, RepresentFundList.get(j), buyamount, CurrentDate, true);
        leftamount -= buyamount;
      }
    }
}
  
//}catch(Exception eInitial){
//    printToLog("Initial Error !!");
//}  

	}
	//----------------------------------------------------
	//re-initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void reinit() throws Exception{
		printToLog("--------------  Re-initial to version " + version +  " :  "+ CurrentDate + "   ----------------");
  

    InAfterDateFilter = new int[CandidateFund.length];
    TooVolatile = new int[CandidateFund.length];
    for(int i = 0 ; i < CandidateFund.length; i ++)
    {
         InAfterDateFilter[i] = 0;
         TooVolatile[i] = 0;
    }
    LastActionMonth = 13;   
    NewYear = true;

printToLog("--------------  Re-initial Finish, SAA strategy update to version " + version + " -------------------");
	}
	
	//----------------------------------------------------
	//action code
	//----------------------------------------------------	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void action() throws Exception{
	
		
		//printToLog(_is529Plan);		
long t1=System.currentTimeMillis();
		
		boolean ToCheck = false;
		Map<String, Integer> RedemptionLimitMap = new HashMap<String, Integer>();
		List<Asset> HoldingAssetList = new ArrayList<Asset>();
		Map<String, List<HoldingItem>> HoldingAssetSecurityMap = new HashMap<String, List<HoldingItem>>();
		Map<String, Double> SecurityAvailableTradingPercentage = new HashMap<String, Double>();
		Map<String, Double> AssetAvailableTradingPercentage = new HashMap<String, Double>();
                Map<String, HashMap<String, Double>> AssetSecurityTradingPercentageMap = null;

		// printToLog("Small cash in the portfolio  =  " +
		// CurrentPortfolio.getCash());
		//try{
if(( CheckFrequency.equalsIgnoreCase("monthly") && LTIDate.isLastNYSETradingDayOfMonth(LTIDate.getNewDate(CurrentDate, TimeUnit.DAILY, 31 - CheckDate))) 
|| ( CheckFrequency.equalsIgnoreCase("quarterly") && LTIDate.isLastNYSETradingDayOfQuarter(LTIDate.getNewDate(CurrentDate, TimeUnit.DAILY, 31 - CheckDate))) || (CheckFrequency.equals("annually")&&LTIDate.isLastNYSETradingDayOfYear(LTIDate.getNewDate(CurrentDate, TimeUnit.DAILY, 31 - CheckDate))) ||  (CheckFrequency.equals("weekly")&&LTIDate.isLastNYSETradingDayOfWeek(LTIDate.getNewDate(CurrentDate, TimeUnit.DAILY, 1))) ) {
			long sTime3 = System.currentTimeMillis();

                        String dateStr = _df.format(CurrentDate);
			Calendar tmpCa = Calendar.getInstance();
			tmpCa.setTime(CurrentDate);
			if (tmpCa.get(Calendar.MONTH) < LastActionMonth)
				NewYear = true;
			HoldingAssetList = CurrentPortfolio.getCurrentAssetList();
			/* form redemption limit map */
			Integer K;
			for (int i = 0; i < CandidateFund.length; i++) {
				if (UseDefaultRedemption)
					K = new Integer(DefaultRedemptionLimit);
				else
					K = new Integer(RedemptionLimit[i]);
				RedemptionLimitMap.put(CandidateFund[i].trim(), K);
			}
			RedemptionLimitMap.put("CASH", new Integer(0));
                        printToLog("Redemption Limit Map : " + RedemptionLimitMap);

			/* Form Holding Map , calculate available tading amount of each  security and each asset */
			List<HoldingItem> HoldingSecurityList = null;
			double ta = CurrentPortfolio.getTotalAmount(CurrentDate);
			String sn;
			double sa;
			double aa;

                         // In the mean time, save the redemption limit if Current Holding for fund-table-updated situation
                         Map<String,Integer> redemptionInfo = null;
                         
                        if(_holdingInfo == null)
                        {
                        try {
                           _holdingInfo =  (Map<String,Map<String,Integer>>) com.lti.util.PersistentUtil.readGlobalObject("HoldingInfoPlan_" + PlanID);
                       } catch (Exception e1) {}
                       }
                       if(_holdingInfo  == null || PlanID == 0)
                          _holdingInfo = new HashMap<String,Map<String,Integer>>();

                       boolean NoInfo = false;
                       redemptionInfo = _holdingInfo.get("Redemption." + dateStr);
                       if(redemptionInfo == null){
                           redemptionInfo = new HashMap<String,Integer>();
                           NoInfo = true;
                        }          

			//printToLog(getStackTraceString(new Throwable("Test"),3));
			for (int i = 0; i < HoldingAssetList.size(); i++) {
				HoldingSecurityList = new ArrayList<HoldingItem>();
				HoldingSecurityList = HoldingAssetList.get(i).getHoldingItems();
				HoldingAssetSecurityMap.put(HoldingAssetList.get(i).getName().trim(), HoldingSecurityList);
				aa = 0;
				for (int j = 0; j < HoldingSecurityList.size(); j++) {
					sn = HoldingSecurityList.get(j).getSymbol().trim();
                                        int rdl = 0;
					printToLog("Symbol: " + sn);
                                        if(NoInfo || (!NoInfo && redemptionInfo.get(sn) == null))
                                        {
                                            if(RedemptionLimitMap.get(sn) != null){
                                                rdl = RedemptionLimitMap.get(sn);
				    	        printToLog("RedemptionLimit = " + RedemptionLimitMap.get(sn));
                                            }else{
                                                rdl = 0;
                                                printToLog("Redemption not found!! It's probably not an candidate fund anymore. We have to sell it in this action." );
                                            }    
                                            redemptionInfo.put(sn,rdl);
                                        }else{                                           
                                                rdl = redemptionInfo.get(sn);
                                                printToLog("The redemption limit in data object = " + rdl);
                                        }

					sa = CurrentPortfolio.getAvailableTradingAmount(sn, rdl, CurrentDate, 1);
					printToLog("Available Trading Amount :  sa =" + sa);
					SecurityAvailableTradingPercentage.put(sn, new Double(sa / ta));
					aa += sa;
				}
				if (aa / ta > 0.02)
					ToCheck = true;
				printToLog("Asset : " + HoldingAssetList.get(i).getName() + "Available percentage =   " + aa / ta);
				AssetAvailableTradingPercentage.put(HoldingAssetList.get(i).getName().trim(), new Double(aa / ta));
			}
                        _holdingInfo.put("Redemption." + dateStr, redemptionInfo);
                       needWriteHoldingData = true;  // let it re-write data after update 

			long eTime3 = System.currentTimeMillis();
			printToLog("Commen Action Time Cost:  " + (eTime3 - sTime3));

}

		//System.out.println("before condition action Times:"+(System.currentTimeMillis()-t1));
		t1=System.currentTimeMillis();

//}catch(Exception eCommon){
//    printToLog("Common action error!!");    
//}

		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (ToCheck) {
		   //try{
		   // 初始化持久化变量

    initPlanParameters();
    LastActionMonth = getCurrentMonth();
    StableAllocation = RiskProfile;
    RiskyAllocation = 100 - RiskProfile;
    long sTime = System.currentTimeMillis();

    // 初始化CandidateFundMap
    Map<String, CandidateFundType> CandidateFundMap = BuildCandidateFundMap(_WaitingPeriod, _RoundtripLimit);
    if(_fundmpt==null){
      initmpts(CandidateFundMap);
    }
    
    // 加载数据或计算数�?
    String dateStr = _df.format(CurrentDate);
    initStoreData(dateStr, CandidateFundMap);

    // 选择中的Asset的Percentage
    Map<String, Double> SelectedAssetPercentagesMap = new HashMap<String, Double>();

    // 每个Asset将要操作的fund的集�?
    Map<String, List<String>> AssetRepresentFundMap = new HashMap<String, List<String>>();

    // 每个fund的percentage
    Map<String, Double> FundPercentageMap = new HashMap<String, Double>();

    Map<String, Boolean> CanNotBuyFundMap = new HashMap<String, Boolean>();

    // the Spare Risky Asset List for 529 adjustment
    List<String> SpareRiskyAsset = new ArrayList<String>();
    Map<String,List<String>> SpareAssetRepresentFundMap = new HashMap<String,List<String>>();

   // 是否�?��Rebalance
   int ToRebalance = 1;

    BuildSelectedAsset(SelectedAssetPercentagesMap, AssetRepresentFundMap, FundPercentageMap, CanNotBuyFundMap, dateStr, CandidateFundMap, SpareRiskyAsset,SpareAssetRepresentFundMap);
    

    if (_is529Plan) {
      printToLog("Enter 529 Adjustment");
      Object[] obj = Adjust529SelectedAsset(SelectedAssetPercentagesMap, AssetRepresentFundMap, FundPercentageMap, dateStr, CandidateFundMap, ToRebalance, SpareRiskyAsset, SpareAssetRepresentFundMap);
      ToRebalance = (Integer)obj[0];
      SelectedAssetPercentagesMap = (HashMap<String,Double>)obj[1];
      AssetRepresentFundMap = (HashMap<String,List<String>>)obj[2];
      FundPercentageMap = (HashMap<String,Double>)obj[3];
    }
    
    //printToLog("( OutSide ) ToRebalance = " + ToRebalance + "   SelectedAssetPercentagesMap = " + SelectedAssetPercentagesMap);

if(ToRebalance == 1)
{
     double TotalAmount = SimulateHolding.getAmount();

// =========================================================================================
			/* ----------------------- Actual Trading Percentage Determination ------------- */

			/*
			 * do not balance the portfolio funds that is representative again,
			 * except for the fix income funds
			 */
			String sn;
			double sa;
			double aa;
			double Tempsa;
			double TempTargetPercentage;
			double TempActualHoldingPercentage;
			List<String> RepresentFunds = null;
			List<HoldingItem> HoldingSecurityList = null;

			printToLog("--------------Adjusting the available percentage------------------------ ");
			double ta = CurrentPortfolio.getTotalAmount(CurrentDate);
			for (int i = 0; i < HoldingAssetList.size(); i++) {
				printToLog("Asset : " + HoldingAssetList.get(i).getName());
				if (HoldingAssetList.get(i).getName().equals("FIXED INCOME") || HoldingAssetList.get(i).getName().equals("INTERNATIONAL BONDS") || HoldingAssetList.get(i).getName().equals("CASH"))
					continue;
				else {
					HoldingSecurityList = new ArrayList<HoldingItem>();
					HoldingSecurityList = HoldingAssetList.get(i).getHoldingItems();
					aa = AssetAvailableTradingPercentage.get(HoldingAssetList.get(i).getName().trim());
					for (int j = 0; j < HoldingSecurityList.size(); j++) {
						sn = HoldingSecurityList.get(j).getSymbol().trim();
						if (AssetRepresentFundMap.get(HoldingAssetList.get(i).getName().trim()) != null) {
							// printToLog("Existing selected asset : " +
							// HoldingAssetList.get(i).getName());
							RepresentFunds = new ArrayList<String>();
							RepresentFunds = AssetRepresentFundMap.get(HoldingAssetList.get(i).getName().trim());
							for (int m = 0; m < RepresentFunds.size(); m++)
								if (sn.equals(RepresentFunds.get(m))) {
									TempTargetPercentage = FundPercentageMap.get(sn);
									TempActualHoldingPercentage = CurrentPortfolio.getSecurityAmount(HoldingAssetList.get(i).getName(), sn, CurrentDate) / ta;
									if (TempActualHoldingPercentage < TempTargetPercentage * 1.5) // still rebalance when too far from the target percentage, added by WYJ on 2010.05.10
									{
										sa = SecurityAvailableTradingPercentage.get(sn);
										aa = AssetAvailableTradingPercentage.get(HoldingAssetList.get(i).getName().trim());
										aa -= sa;
										SecurityAvailableTradingPercentage.put(sn, 0.00);
										AssetAvailableTradingPercentage.put(HoldingAssetList.get(i).getName().trim(), aa);
									}
									break;
								}
						}
					}
				}
				printToLog("adjusted available percentage = " + aa);
			}

			Asset CurrentAsset = null;
			boolean New;
			{
				Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
				while (iter.hasNext()) {
					New = true;
					String AssetName = iter.next();
					if (SimulateHolding.getAsset(AssetName) != null) {
						New = false;
					}
					if (!New) {
						CurrentAsset = new Asset();
						CurrentAsset = CurrentPortfolio.getAsset(AssetName);
						CurrentAsset.setTargetPercentage(SelectedAssetPercentagesMap.get(AssetName));
					} else {
						CurrentAsset = new Asset();
						CurrentAsset.setName(AssetName);
						CurrentAsset.setAssetClass(_getAssetClass(AssetName));
						CurrentAsset.setTargetPercentage(SelectedAssetPercentagesMap.get(AssetName));
						CurrentPortfolio.addAsset(CurrentAsset);
					}
				}
			}
			for (int i = 0; i < HoldingAssetList.size(); i++) {
				New = true;
				if (SelectedAssetPercentagesMap.get(HoldingAssetList.get(i).getName()) != null) {
					New = false;
				}

				if (New) {
					SelectedAssetPercentagesMap.put(HoldingAssetList.get(i).getName(), 0.0);
				}
			}

			/* calculate the asset trading percentage */

			HashMap<String, Double> HoldingAssetActualPercentage = new HashMap<String, Double>();
			aa = 0;

			for (int i = 0; i < HoldingAssetList.size(); i++) {
				aa = CurrentPortfolio.getAssetAmount(HoldingAssetList.get(i).getName(), CurrentDate);
				HoldingAssetActualPercentage.put(HoldingAssetList.get(i).getName().trim(), new Double(aa / ta));
			}

			HashMap<String, Double> AssetActualTradingPercentage = new HashMap<String, Double>();
			String AssetName;
			double UnableToSellPercentage = 0;
			double AvailableTradingPercentage;
			double TargetPercentage;
			double ActualPercentage;
			double TotalTargetSellPercentage = 0;
			double TotalActualTradingPercentage;

			for (int i = 0; i < HoldingAssetList.size(); i++) {
				AssetName = HoldingAssetList.get(i).getName().trim();
				ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
				if (AssetAvailableTradingPercentage.get(AssetName) == null) {
					AvailableTradingPercentage = 0.0;
					printToLog(AssetName + ": 0.0");
				} else {
					AvailableTradingPercentage = AssetAvailableTradingPercentage.get(AssetName).doubleValue();

				}
				if (SelectedAssetPercentagesMap.get(AssetName.trim()) != null)
					TargetPercentage = SelectedAssetPercentagesMap.get(AssetName.trim()).doubleValue();
				else
					TargetPercentage = 0;
				if (ActualPercentage > TargetPercentage) {
					TotalTargetSellPercentage += (ActualPercentage - TargetPercentage);
					if (AvailableTradingPercentage >= ActualPercentage - TargetPercentage)
						AssetActualTradingPercentage.put(AssetName, new Double(TargetPercentage - ActualPercentage));
					else {
						AssetActualTradingPercentage.put(AssetName, new Double(-AvailableTradingPercentage));
						UnableToSellPercentage += (ActualPercentage - TargetPercentage - AvailableTradingPercentage);
					}
				}
			}
			TotalActualTradingPercentage = TotalTargetSellPercentage - UnableToSellPercentage;

			double BuyWeight = TotalActualTradingPercentage / TotalTargetSellPercentage;
			double UnableToBuyPercentage = 0;
			boolean BelowMinimum = false;
			boolean AbleToBuyBoolean = false;
			Map<String, Integer> AbleToBuy = new HashMap<String, Integer>();
			Map<String, Double> PlanToBuyPercentage = new HashMap<String, Double>();
			{
				Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
				while (iter.hasNext()) {
					AssetName = iter.next();
					AbleToBuy.put(AssetName, 0);
					TargetPercentage = SelectedAssetPercentagesMap.get(AssetName.trim()).doubleValue();
					if (HoldingAssetActualPercentage.get(AssetName) != null)
						ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
					else
						ActualPercentage = 0;
					if (ActualPercentage <= TargetPercentage + 0.00001) {
						if ((TargetPercentage - ActualPercentage) * BuyWeight > MinimumBuyingPercentage)
							AssetActualTradingPercentage.put(AssetName, new Double((TargetPercentage - ActualPercentage) * BuyWeight));
                                                else if((TargetPercentage - ActualPercentage)*BuyWeight + UnableToBuyPercentage  > MinimumBuyingPercentage)
                                                {
                                                     AssetActualTradingPercentage.put(AssetName, new Double((TargetPercentage - ActualPercentage)*BuyWeight) +  UnableToBuyPercentage);
                                                     UnableToBuyPercentage = 0;
                                                }
						else {
							AssetActualTradingPercentage.put(AssetName, new Double(0));
							UnableToBuyPercentage += ((TargetPercentage - ActualPercentage) * BuyWeight);
							BelowMinimum = true;
							if ((TargetPercentage - ActualPercentage) >= MinimumBuyingPercentage) {
								AbleToBuy.put(AssetName, 1);
								PlanToBuyPercentage.put(AssetName, TargetPercentage - ActualPercentage);
								AbleToBuyBoolean = true;
							} else
								AbleToBuy.put(AssetName, 0);
						}
					}
				}
			}
                        printToLog("Asset Actual Trading Percentage (before adjusted 1) : " + AssetActualTradingPercentage);

			if (BelowMinimum && AbleToBuyBoolean) {
				{
					Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
					int i = 0;
					while (iter.hasNext()) {
						AssetName = iter.next();
						i++;
						if (UnableToBuyPercentage > 0 && i <= SelectedAssetPercentagesMap.size()) {
							if (AbleToBuy.get(AssetName) == 1) {
								if (PlanToBuyPercentage.get(AssetName) < UnableToBuyPercentage) {
									AssetActualTradingPercentage.put(AssetName, new Double(PlanToBuyPercentage.get(AssetName)));
									UnableToBuyPercentage -= PlanToBuyPercentage.get(AssetName);
								} else {
									AssetActualTradingPercentage.put(AssetName, new Double(UnableToBuyPercentage));
									UnableToBuyPercentage = 0;
								}
							}
						}
					}
				}
			}

                        printToLog("Asset Actual Trading Percentage (before adjusted 2) : " + AssetActualTradingPercentage);
			/*
			 * Calculate: use the left percentage to buy the asset with the max
			 * Target Percentage
			 */
                        printToLog("UnableToBuyPercentage = " + UnableToBuyPercentage);
                        printToLog("SelectedAssetPercentagesMap = " + SelectedAssetPercentagesMap);
			if (UnableToBuyPercentage > 0) {
				String MaxTargetPercentageAsset = null;
				double MaxTargetPercentage = Double.MIN_VALUE;
				double CompareTargetPercentage;
				{
					Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
					while (iter.hasNext()) {
						AssetName = iter.next();
                                                printToLog("Asset Name = " + AssetName);
						CompareTargetPercentage = SelectedAssetPercentagesMap.get(AssetName).doubleValue();
						if (CompareTargetPercentage > MaxTargetPercentage) {
                                                {
                                                    double tmpTrading =  AssetActualTradingPercentage.get(AssetName);
                                                    tmpTrading  +=  UnableToBuyPercentage;
                                                    printToLog("Adjusted percentage = " + tmpTrading);
                                                    if(tmpTrading < -MinimumSellingPercentage || tmpTrading > MinimumBuyingPercentage)
                                                    {
							MaxTargetPercentage = CompareTargetPercentage;
							MaxTargetPercentageAsset = AssetName;
						    }
					      	}
					}
				}
                                if(MaxTargetPercentageAsset != null)
                                {
				    double TempTrading = AssetActualTradingPercentage.get(MaxTargetPercentageAsset).doubleValue();
				    double NewActualTradingPercentage = TempTrading + UnableToBuyPercentage;
				    if (NewActualTradingPercentage > -0.001 & NewActualTradingPercentage < 0.001)
					NewActualTradingPercentage = 0;
				    AssetActualTradingPercentage.put(MaxTargetPercentageAsset, new Double(NewActualTradingPercentage));
       			         }
                       }
                   }

			printToLog("Asset Actual Trading Percentage  = " + AssetActualTradingPercentage);

			/* Calculate trading percentage of each security, not rebalance */

			AssetSecurityTradingPercentageMap = new HashMap<String, HashMap<String, Double>>();
			HashMap<String, Double> SecurityActualTradingPercentage = new HashMap<String, Double>();
			HashMap<String, Double> SecurityTargetPercentageMap = new HashMap<String, Double>();
			HashMap<String, Double> HoldingSecurityActualPercentage = new HashMap<String, Double>();
			HoldingSecurityList = null;
			List<String> PresentativeSecurityList = null;
			double TotalBuyingPercentage;
			double SellingPercentage;
			double BuyingPercentage;
			double SecurityActualPercentage;
			double SecurityTargetPercentage;
			String SecurityName;
			Map<String, Integer> NewAdd = new HashMap<String, Integer>();
			double UnTradePercentage = 0;
			{
				Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
				while (iter.hasNext()) {
					AssetName = iter.next();
					HoldingSecurityList = new ArrayList<HoldingItem>();
					PresentativeSecurityList = new ArrayList<String>();
					PresentativeSecurityList = AssetRepresentFundMap.get(AssetName);
					TargetPercentage = SelectedAssetPercentagesMap.get(AssetName).doubleValue();
                                        double AssetActualTrading =  AssetActualTradingPercentage.get(AssetName);
                                        double LastUntrade = 0;

					if (HoldingAssetActualPercentage.get(AssetName) != null) {
						HoldingSecurityList = CurrentPortfolio.getAsset(AssetName).getHoldingItems();
						ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
						if (AssetAvailableTradingPercentage.get(AssetName) == null) {
							AvailableTradingPercentage = 0.0;
							printToLog(AssetName + ": 0.0");
						} else {
							AvailableTradingPercentage = AssetAvailableTradingPercentage.get(AssetName).doubleValue();

						}
						NewAdd.put(AssetName, 0);
					} else {
						HoldingSecurityList = null;
						ActualPercentage = 0;
						AvailableTradingPercentage = 0;
						NewAdd.put(AssetName, 1);
					}

                                      // added by WYJ on 2010.08.10, let the calculation take care of  the un-traded trading fraction of the former asset class 
                                        if(UnTradePercentage != 0){
                                            LastUntrade = UnTradePercentage;
                                            UnTradePercentage = 0.0;
                                            printToLog("adjust the asset target percentage and reset the UnTradePercentage : " + AssetName + "  ");
                                        }

					// printToLog("i = " + i + "  ActualPercentage = " +
					// ActualPercentage + "  TargetPercentage = " +
					// TargetPercentage
					// + "  AvailableTradingPercentage = " +
					// AvailableTradingPercentage +
					// "  AssetActualTradingPercentage = " +
					// AssetActualTradingPercentage.get(AssetName).doubleValue()
					// );

					if ((AvailableTradingPercentage <= ActualPercentage - TargetPercentage && AvailableTradingPercentage != 0) || PresentativeSecurityList == null || PresentativeSecurityList.size() == 0 && HoldingSecurityList != null) {
						SecurityActualTradingPercentage = new HashMap<String, Double>();
						for (int j = 0; j < HoldingSecurityList.size(); j++) {
							SellingPercentage = SecurityAvailableTradingPercentage.get(HoldingSecurityList.get(j).getSymbol().trim());
							SecurityActualTradingPercentage.put(HoldingSecurityList.get(j).getSymbol().trim(), new Double(-SellingPercentage));
						}
						AssetSecurityTradingPercentageMap.put(AssetName.trim(), SecurityActualTradingPercentage);
                                                UnTradePercentage += LastUntrade;
					} else if (NewAdd.get(AssetName) == 1) {
						SecurityActualTradingPercentage = new HashMap<String, Double>();
						BuyingPercentage = (AssetActualTradingPercentage.get(AssetName) + LastUntrade) / PresentativeSecurityList.size();
						for (int j = 0; j < PresentativeSecurityList.size(); j++)
							SecurityActualTradingPercentage.put(PresentativeSecurityList.get(j), new Double(BuyingPercentage));
						AssetSecurityTradingPercentageMap.put(AssetName.trim(), SecurityActualTradingPercentage);
					} else if (AvailableTradingPercentage > ActualPercentage - TargetPercentage) {
						SecurityActualTradingPercentage = new HashMap<String, Double>();
						for (int j = 0; j < HoldingSecurityList.size(); j++) {
							SellingPercentage = SecurityAvailableTradingPercentage.get(HoldingSecurityList.get(j).getSymbol().trim());
							SecurityActualTradingPercentage.put(HoldingSecurityList.get(j).getSymbol().trim(), new Double(-SellingPercentage));
							printToLog("Only For Debug  -----  Selling all holdings Step :   " + HoldingSecurityList.get(j).getSymbol().trim() + "  Amount = " + TotalAmount * SellingPercentage);
						}
						TotalBuyingPercentage = AssetActualTradingPercentage.get(AssetName) + LastUntrade + AvailableTradingPercentage;
						// printToLog(" Actual Target Percentage = " +
						// (TotalBuyingPercentage + ActualPercentage -
						// AvailableTradingPercentage));
						int j = 0;
						while (TotalBuyingPercentage > 0.001) {
							SecurityName = PresentativeSecurityList.get(j).trim();
							SecurityTargetPercentage = FundPercentageMap.get(SecurityName) + (LastUntrade / PresentativeSecurityList.size());
							if (SecurityAvailableTradingPercentage.get(SecurityName) == null) {
								SecurityActualPercentage = 0;
								SellingPercentage = 0;
							} else {
								SecurityActualPercentage = CurrentPortfolio.getSecurityAmount(AssetName, SecurityName, CurrentDate) / TotalAmount;
								SellingPercentage = SecurityAvailableTradingPercentage.get(SecurityName);
							}
							// printToLog("j = " + j +
							// "  SecurityActualPercentage = " +
							// SecurityActualPercentage +
							// "  SellingPercentage = " +
							// SellingPercentage +
							// "  SecurityTargetPercentage = " +
							// SecurityTargetPercentage +
							// "  TotalBuyingPercentage = " +
							// TotalBuyingPercentage);
							if (SecurityActualPercentage - SellingPercentage >= SecurityTargetPercentage) {
								// printToLog("condition 1");
				
								if (SellingPercentage == 0 && j != PresentativeSecurityList.size() - 1) /*
																										 * modify
																										 * on
																										 * 2010.04
																										 * .15
																										 */
									SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
								else if (j == PresentativeSecurityList.size() - 1) {
									if (TotalBuyingPercentage - SellingPercentage < MinimumBuyingPercentage && TotalBuyingPercentage - SellingPercentage > -MinimumSellingPercentage) {
										UnTradePercentage += TotalBuyingPercentage - SellingPercentage;
										SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
									} else
										SecurityActualTradingPercentage.put(SecurityName, new Double(TotalBuyingPercentage - SellingPercentage));
									TotalBuyingPercentage = 0;
									// printToLog("condition 1-1");
								} else {
									if ((-SellingPercentage) < MinimumBuyingPercentage && (-SellingPercentage) > -MinimumSellingPercentage) {
										UnTradePercentage += (-SellingPercentage);
										SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
									} else
										SecurityActualTradingPercentage.put(SecurityName, new Double(-SellingPercentage));
									TotalBuyingPercentage -= SellingPercentage;
									// printToLog("condition 1-2");
								}
							} else if (SecurityActualPercentage - SellingPercentage + TotalBuyingPercentage > SecurityTargetPercentage + 0.001 && j < (PresentativeSecurityList.size() - 1)) {
								// printToLog("condition 2");
								if (SecurityTargetPercentage - SecurityActualPercentage < MinimumBuyingPercentage && SecurityTargetPercentage - SecurityActualPercentage > -MinimumSellingPercentage) {
									UnTradePercentage += (SecurityTargetPercentage - SecurityActualPercentage);
									SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
								} else
									SecurityActualTradingPercentage.put(SecurityName, new Double(SecurityTargetPercentage - SecurityActualPercentage));
								TotalBuyingPercentage -= (SecurityTargetPercentage - SecurityActualPercentage + SellingPercentage);
							} else {
								// printToLog("condition 3");
								if (TotalBuyingPercentage - SellingPercentage < MinimumBuyingPercentage && TotalBuyingPercentage - SellingPercentage > -MinimumSellingPercentage && SecurityActualPercentage != -(TotalBuyingPercentage - SellingPercentage)) {
									UnTradePercentage += (TotalBuyingPercentage - SellingPercentage);
									SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
								} else
									SecurityActualTradingPercentage.put(SecurityName, new Double(TotalBuyingPercentage - SellingPercentage));
								TotalBuyingPercentage = 0;
							}
							// printToLog("SecurityActualTradingPercentage = " +
							// SecurityActualTradingPercentage.get(SecurityName));
							j++;
						}
						AssetSecurityTradingPercentageMap.put(AssetName.trim(), SecurityActualTradingPercentage);
					}
                                        else
                                        {
                                            UnTradePercentage += LastUntrade;  printToLog("No Change Allocation to the asset : " + AssetName);
                                        }
				}
			}
                        printToLog("un-trade percentage after security allocation = " + UnTradePercentage);
			/* For Debug */printToLog("Actual trading Percentage Map (before adjusted) =  " + AssetSecurityTradingPercentageMap);

                        /*Adjust one buying transaction for the UnTradePercentage  Added by WYJ on 2010.05.10 */

			double TempPercentage;
			boolean Done = false;
			if (UnTradePercentage != 0)
			{
				Iterator iter1 = AssetSecurityTradingPercentageMap.keySet().iterator();
				Iterator iter2 = null;
				while (iter1.hasNext() && !Done) {
					String Asset = (String) iter1.next();
					HashMap<String, Double> TempTradingPercentageMap = AssetSecurityTradingPercentageMap.get(Asset.trim());
					iter2 = TempTradingPercentageMap.keySet().iterator();
					while (iter2.hasNext() && !Done) {
						String Security = (String) iter2.next();
						TempPercentage = TempTradingPercentageMap.get(Security.trim());
						if (TempPercentage * UnTradePercentage > 0 || TempPercentage == - UnTradePercentage) // Modified on 2010.08.10, combine the fraction trading into the other trading with the same sign
						{
							TempPercentage += UnTradePercentage;
							TempTradingPercentageMap.put(Security.trim(), TempPercentage);
							AssetSecurityTradingPercentageMap.put(Asset.trim(), TempTradingPercentageMap);
							Done = true;
						}
					}
				}
			}

			/* For Debug */printToLog("Actual trading Percentage Map (after adjusted) =  " + AssetSecurityTradingPercentageMap);

			printToLog("Total Amount  =   " + TotalAmount);

			/* Do the selling transaction */

			double TempRecord = CurrentPortfolio.getCash();

			HoldingAssetList = CurrentPortfolio.getCurrentAssetList();
			double SecurityCurrentAmount = 0;
			Map<String, Double> SecurityCurrentAmountMap = new HashMap<String, Double>();
			for (int i = 0; i < HoldingAssetList.size(); i++) {
				HoldingSecurityList = new ArrayList<HoldingItem>();
				HoldingSecurityList = HoldingAssetList.get(i).getHoldingItems();
				for (int j = 0; j < HoldingSecurityList.size(); j++) {
					sn = HoldingSecurityList.get(j).getSymbol().trim();
					SecurityCurrentAmount = CurrentPortfolio.getSecurityAmount(HoldingAssetList.get(i).getName(), sn, CurrentDate);
					SecurityCurrentAmountMap.put(sn, SecurityCurrentAmount);
					printToLog("Asset : " + HoldingAssetList.get(i).getName() + " Security Symbol: " + sn + "CurrentAmount = " + SecurityCurrentAmount);
				}
			}

//printToLog("SecurityActualTradingPercentageMap  = " + SecurityActualTradingPercentage);

			int TranNum = 0;

			{
				Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
				while (iter.hasNext()) {
					AssetName = iter.next();
					SecurityActualTradingPercentage = new HashMap<String, Double>();
					SecurityActualTradingPercentage = AssetSecurityTradingPercentageMap.get(AssetName.trim());
					if (SecurityActualTradingPercentage != null) {
						Iterator iter2 = SecurityActualTradingPercentage.keySet().iterator();
						while (iter2.hasNext()) {
                                                   String _fund = (String)iter2.next();
                                                   double _perc = SecurityActualTradingPercentage.get(_fund);
                                                   if(_perc > 0.0001 || _perc < -0.0001)
							TranNum++;
						}
					}
				}
			}
            printToLog("Transaction number = " + TranNum);

			//long sTime4 = System.currentTimeMillis();

			{
				Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
				while (iter.hasNext()) {
					AssetName = iter.next();
					SecurityActualTradingPercentage = new HashMap<String, Double>();
					SecurityActualTradingPercentage = AssetSecurityTradingPercentageMap.get(AssetName.trim());
					if (SecurityActualTradingPercentage != null) {
						Iterator iter2 = SecurityActualTradingPercentage.keySet().iterator();
						while (iter2.hasNext()) {
							String TradingSecurity = (String) iter2.next();
							double TradePercentage = SecurityActualTradingPercentage.get(TradingSecurity).doubleValue();
							// printToLog("To sell Asset : " +AssetName + "SellingSecurity : " + TradingSecurity +"   $ "+TotalAmount*(-TradePercentage));
							if (TranNum > 1 && TradePercentage < -0.0001) {    // ignore too small CASH selling 
								try {
									double CurrentAmount = SecurityCurrentAmountMap.get(TradingSecurity).doubleValue();
									double TradeAmount = (TradePercentage * TotalAmount) > (-CurrentAmount) ? (TradePercentage * TotalAmount) : (-CurrentAmount);
                                                                        if(CurrentAmount + TradeAmount < TotalAmount * 0.001)
                                                                           TradeAmount = - CurrentAmount;
                                                                        if((-TradeAmount) < TotalAmount*MinimumSellingPercentage && CurrentAmount + TradeAmount > TotalAmount * 0.0001)
                                                                        {}else{
									    CurrentPortfolio.sellAtNextOpen(AssetName, TradingSecurity, -TradeAmount, CurrentDate);
									    printToLog("Sell Signal: " + AssetName + "  " + TradingSecurity + "  $ " + -TradeAmount);
									    TempRecord += (-TradeAmount);
                                                                        }
								} catch (Exception e) {
									printToLog("Failed selling:  " + AssetName + "  " + TradingSecurity + "  $ " + TotalAmount * (-TradePercentage));
								}
							}
						}
					}
				}
			}

			/* Do the buying transaction */
			{
				Iterator<String> iter = SelectedAssetPercentagesMap.keySet().iterator();
				while (iter.hasNext()) {
					AssetName = iter.next();
					SecurityActualTradingPercentage = new HashMap<String, Double>();
					SecurityActualTradingPercentage = AssetSecurityTradingPercentageMap.get(AssetName.trim());
					if (SecurityActualTradingPercentage != null) {
						Iterator iter2 = SecurityActualTradingPercentage.entrySet().iterator();
						while (iter2.hasNext()) {
							Map.Entry entry = (Map.Entry) iter2.next();
							String TradingSecurity = (String) entry.getKey();
							double TradePercentage = ((Double) entry.getValue()).doubleValue();
							if (TranNum > 1 && TradePercentage > 0.0001) {
								double TradeAmount = (TotalAmount * TradePercentage) < TempRecord ? (TotalAmount * TradePercentage) : TempRecord;
                                                        if(TempRecord - TradeAmount < MinimumBuyingPercentage * TotalAmount)
                                                                TradeAmount = TempRecord;
                                                        CurrentPortfolio.buyAtNextOpen(AssetName, TradingSecurity,TradeAmount, CurrentDate,true);
                                                        printToLog("Buy Signal: "+AssetName+"  "+TradingSecurity+"  $ "+ TradeAmount);
                                                        if(TradeAmount < MinimumBuyingPercentage * TotalAmount)
                                                               printToLog("Error: the Schedule transaction is below the trading threshold !");
                                                        TempRecord -= TradeAmount;

								}
								
							}
						}
					}
				}			
			System.out.println("finish Times:"+(System.currentTimeMillis()-t1));
			t1=System.currentTimeMillis();
			if (NewYear)	NewYear = false;

//long eTime4 = System.currentTimeMillis();
//printToLog("Trading Time Cost:  " + (eTime4 - sTime4));

} // End of "if(ToRebalance == 1)"

if(UseSMAFilter || UseWMAFilter || UseEMAFilter)
    needWriteTargetData = false;        // The data generated under a specific filter won't be saved.

long eTime = System.currentTimeMillis();
printToLog("Action Time Cost:  " + (eTime - sTime));

//}catch(Exception eAction){
// printToLog("Action error !!");
//}
		}
		else if ((UseSMAFilter || UseWMAFilter || UseEMAFilter) && LTIDate.isNYSETradingDay(CurrentDate)) {
		   Map<String,Map<String,Boolean>> SellAssetFundMap = new HashMap<String,Map<String,Boolean>>();
Map<String,Integer> SMADaysMap = null;
Map<String,Integer> WMADaysMap = null;
Map<String,Integer> EMADaysMap = null;

// Form the Filter Map
if(UseSMAFilter){
    SMADaysMap = new HashMap<String,Integer>();
    for(int i = 0; i < CandidateFund.length; i ++)
        SMADaysMap.put(CandidateFund[i], SMADays[i]);
   SMADaysMap.put("CASH", 0);
}

if(UseWMAFilter){
    WMADaysMap = new HashMap<String,Integer>();
    for(int i = 0; i < CandidateFund.length; i ++)
        WMADaysMap.put(CandidateFund[i], WMADays[i]);
   WMADaysMap.put("CASH", 0);
}

if(UseEMAFilter){
    EMADaysMap = new HashMap<String,Integer>();
    for(int i = 0; i < CandidateFund.length; i ++)
        EMADaysMap.put(CandidateFund[i], EMADays[i]);
   EMADaysMap.put("CASH", 0);
}

//use the Filters to detect funds to sell
HoldingAssetList = CurrentPortfolio.getCurrentAssetList();	
for (int i = 0; i < HoldingAssetList.size(); i++) {
    String AssetName = HoldingAssetList.get(i).getName().trim();
    List<HoldingItem> HoldingSecurityList = HoldingAssetList.get(i).getHoldingItems();
    List<String> fundList = new ArrayList<String>();
    for (int j = 0; j < HoldingSecurityList.size(); j++) 
        if(HoldingSecurityList.get(j).getShare() > 0)
            fundList.add(HoldingSecurityList.get(j).getSymbol().trim());

    Map<String,Boolean> SellFundMap = getFilterOutMap(fundList, SMADaysMap,WMADaysMap,EMADaysMap);
    if(SellFundMap.size() > 0)
        SellAssetFundMap.put(AssetName, SellFundMap);
}

if(SellAssetFundMap.size() > 0)
{
printToLog("Filter to Sell : " + SellAssetFundMap);
//schedule the selling transactions
double BuyCashAmount = 0;
Iterator<String> iter1 = SellAssetFundMap.keySet().iterator();
while(iter1.hasNext()){
    String assetName = iter1.next();
    Map<String,Boolean> fundMap = SellAssetFundMap.get(assetName);
    Map<String, Double> scheduledTransaction = null;
    try{
       scheduledTransaction  = AssetSecurityTradingPercentageMap.get(assetName);
    }catch(Exception e){}

    Iterator<String> iter2 = fundMap.keySet().iterator();
    while(iter2.hasNext()){
        String fundName = iter2.next();
        double amount = CurrentPortfolio.getSecurityAmount(assetName, fundName, CurrentDate);
        double scheduleAmount  = 0;
        if(scheduledTransaction != null && scheduledTransaction.get(fundName) != null){
            scheduleAmount = scheduledTransaction.get(fundName) * CurrentPortfolio.getTotalAmount(CurrentDate);
        }
       if(scheduleAmount == 0){}
       else if(amount + scheduleAmount > 0){
            amount += scheduleAmount;
        }
       CurrentPortfolio.sellAtNextOpen(assetName, fundName, amount, CurrentDate);
        printToLog("sell at next open : " + assetName + ",      " + fundName + ",    Amount = " + amount);
        BuyCashAmount += amount;
    }
}

//Buy Cash
CurrentPortfolio.buyAtNextOpen("CASH", "CASH", BuyCashAmount, CurrentDate,true);
}
		}
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

//