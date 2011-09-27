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
public class Copy_of_Guru_Asset_Allocation_Strategy_Under_Redemption_Limit722 extends SimulateStrategy{
	public Copy_of_Guru_Asset_Allocation_Strategy_Under_Redemption_Limit722(){
		super();
		StrategyID=722L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String[] CandidateFund;
	public void setCandidateFund(String[] CandidateFund){
		this.CandidateFund=CandidateFund;
	}
	
	public String[] getCandidateFund(){
		return this.CandidateFund;
	}
	private String[] AssetClass;
	public void setAssetClass(String[] AssetClass){
		this.AssetClass=AssetClass;
	}
	
	public String[] getAssetClass(){
		return this.AssetClass;
	}
	private int[] RedemptionLimit;
	public void setRedemptionLimit(int[] RedemptionLimit){
		this.RedemptionLimit=RedemptionLimit;
	}
	
	public int[] getRedemptionLimit(){
		return this.RedemptionLimit;
	}
	private String[] MainAssetClass;
	public void setMainAssetClass(String[] MainAssetClass){
		this.MainAssetClass=MainAssetClass;
	}
	
	public String[] getMainAssetClass(){
		return this.MainAssetClass;
	}
	private int MaxOfEachAsset;
	public void setMaxOfEachAsset(int MaxOfEachAsset){
		this.MaxOfEachAsset=MaxOfEachAsset;
	}
	
	public int getMaxOfEachAsset(){
		return this.MaxOfEachAsset;
	}
	private String CheckFrequency;
	public void setCheckFrequency(String CheckFrequency){
		this.CheckFrequency=CheckFrequency;
	}
	
	public String getCheckFrequency(){
		return this.CheckFrequency;
	}
	private String RebalanceFrequency;
	public void setRebalanceFrequency(String RebalanceFrequency){
		this.RebalanceFrequency=RebalanceFrequency;
	}
	
	public String getRebalanceFrequency(){
		return this.RebalanceFrequency;
	}
	private int SharpeMonths;
	public void setSharpeMonths(int SharpeMonths){
		this.SharpeMonths=SharpeMonths;
	}
	
	public int getSharpeMonths(){
		return this.SharpeMonths;
	}
	private int RAAInterval;
	public void setRAAInterval(int RAAInterval){
		this.RAAInterval=RAAInterval;
	}
	
	public int getRAAInterval(){
		return this.RAAInterval;
	}
	private String RAAMethod;
	public void setRAAMethod(String RAAMethod){
		this.RAAMethod=RAAMethod;
	}
	
	public String getRAAMethod(){
		return this.RAAMethod;
	}
	private int TargetTopN;
	public void setTargetTopN(int TargetTopN){
		this.TargetTopN=TargetTopN;
	}
	
	public int getTargetTopN(){
		return this.TargetTopN;
	}
	private String[] GuruTargetFunds;
	public void setGuruTargetFunds(String[] GuruTargetFunds){
		this.GuruTargetFunds=GuruTargetFunds;
	}
	
	public String[] getGuruTargetFunds(){
		return this.GuruTargetFunds;
	}
	private double MoneyAllocation;
	public void setMoneyAllocation(double MoneyAllocation){
		this.MoneyAllocation=MoneyAllocation;
	}
	
	public double getMoneyAllocation(){
		return this.MoneyAllocation;
	}
	private String RiskAversion;
	public void setRiskAversion(String RiskAversion){
		this.RiskAversion=RiskAversion;
	}
	
	public String getRiskAversion(){
		return this.RiskAversion;
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
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		CandidateFund=(String[])ParameterUtil.fetchParameter("String[]","CandidateFund", "CHTVX,FOCPX, FUSVX, MSIVX, WFSVX, ^DWC, FSEMX, FIGRX, HAINX,FBNDX, VBMFX,CASH", parameters);
		AssetClass=(String[])ParameterUtil.fetchParameter("String[]","AssetClass", "US EQUITY, International Equity, US EQUITY, Real Estate, Real Estate, US EQUITY, US EQUITY, US EQUITY, US EQUITY, FIXED INCOME, FIXED INCOME, CASH", parameters);
		RedemptionLimit=(int[])ParameterUtil.fetchParameter("int[]","RedemptionLimit", "3,3,3,3,3,3,3,3,3,3,3,0", parameters);
		MainAssetClass=(String[])ParameterUtil.fetchParameter("String[]","MainAssetClass", "US EQUITY, INTERNATIONAL EQUITY, FIXED INCOME, REAL ESTATE, COMMODITIES, Emerging Market, INTERNATIONAL BONDS, High Yield Bond, CASH", parameters);
		MaxOfEachAsset=(Integer)ParameterUtil.fetchParameter("int","MaxOfEachAsset", "1", parameters);
		CheckFrequency=(String)ParameterUtil.fetchParameter("String","CheckFrequency", "monthly", parameters);
		RebalanceFrequency=(String)ParameterUtil.fetchParameter("String","RebalanceFrequency", "quarterly", parameters);
		SharpeMonths=(Integer)ParameterUtil.fetchParameter("int","SharpeMonths", "12", parameters);
		RAAInterval=(Integer)ParameterUtil.fetchParameter("int","RAAInterval", "30", parameters);
		RAAMethod=(String)ParameterUtil.fetchParameter("String","RAAMethod", "WLS", parameters);
		TargetTopN=(Integer)ParameterUtil.fetchParameter("int","TargetTopN", "3", parameters);
		GuruTargetFunds=(String[])ParameterUtil.fetchParameter("String[]","GuruTargetFunds", "CHTVX,FOCPX, FUSVX, MSIVX, WFSVX, ^DWC, FSEMX, FIGRX, HAINX,FBNDX, VBMFX,CASH", parameters);
		MoneyAllocation=(Double)ParameterUtil.fetchParameter("double","MoneyAllocation", "0", parameters);
		RiskAversion=(String)ParameterUtil.fetchParameter("String","RiskAversion", "Growth", parameters);
		MinimumBuyingPercentage=(Double)ParameterUtil.fetchParameter("double","MinimumBuyingPercentage", "0.01", parameters);
		MinimumSellingPercentage=(Double)ParameterUtil.fetchParameter("double","MinimumSellingPercentage", "0", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	public HashMap<String,List<String>> getPresentativeAssetFunds(HashMap<String,List<String>> AllAssetFundsMap,  List<String> AvailableAssetClassList, int SharpeMonths, int MaxOfEachAsset)
 throws Exception {
HashMap<String,List<String>>  PresentativeAssetFundMap = new HashMap<String,List<String>>();
String[] Funds;
List<String> FundList;
List<String> SelectedFunds;
String[] newArray =null;

for(int i =0; i< AvailableAssetClassList.size(); i++)
{
  if(AvailableAssetClassList.get(i).equals("CASH"))
  {
    SelectedFunds = new ArrayList<String>();
    SelectedFunds.add("CASH");
    PresentativeAssetFundMap.put(AvailableAssetClassList.get(i), SelectedFunds);  
  }
  else
  {
    FundList = new ArrayList<String>();
    FundList =  AllAssetFundsMap.get(AvailableAssetClassList.get(i));
    Funds = new String[FundList.size()];
    for(int j = 0; j< FundList.size(); j++)
        Funds[j] = FundList.get(j);
    if(SharpeMonths != 0)
    {
        newArray = new String[Funds.length];
        newArray = getTopSecurityArray(Funds,-SharpeMonths, CurrentDate, TimeUnit.MONTHLY, SortType.SHARPE, false);
    }
    int FundNumber = MaxOfEachAsset > Funds.length ? Funds.length : MaxOfEachAsset ;
    SelectedFunds = new ArrayList<String>();
    for(int j = 0; j< FundNumber; j++)
        SelectedFunds.add(newArray[j]);
    PresentativeAssetFundMap.put(AvailableAssetClassList.get(i), SelectedFunds);
  }
}
return PresentativeAssetFundMap;
}


//getVolume function ( used in the Function: Choose_Available_CandidateFunds )
	public Long getMonthVolume(Long id,Date endDate) throws Exception {
		Long amount= 0L;
		Date startDate = LTIDate.getNewNYSEMonth(endDate, -3);
		List<SecurityDailyData> dailyDataList= securityManager.getDailyDatas(id, startDate, endDate);
		if(dailyDataList!=null && dailyDataList.size()>0){
			for(int i=0;i<dailyDataList.size();++i){
				amount+= dailyDataList.get(i).getVolume();
			}
			amount/=dailyDataList.size();
		}
	return amount;	
	}
	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		List<String> CandidateFundList = new ArrayList<String>();
List<String> MainAssetClassList = new ArrayList<String>();
List<String> AvailableAssetClassList = new ArrayList<String>();

/*delete the duplicate candidate fund*/

int[] Duplicate = new int[CandidateFund.length];
int k = 0;
for(int i = 0; i < CandidateFund.length; i++)
{
    for(int j = i +1; j < CandidateFund.length; j++)
    {
        if(CandidateFund[i].equals(CandidateFund[j]))
            { Duplicate[i] = 1;  break; }        
    }
    if(Duplicate[i] != 1)
        { Duplicate[i] = 0; k++; }
}

String[] Temp1 = CandidateFund;
String[] Temp2 = AssetClass;
int[] Temp3= RedemptionLimit ;
CandidateFund = new String[k];
AssetClass = new String[k];
RedemptionLimit = new int[k];
k = 0;
for(int i = 0; i < Duplicate.length; i++)
{
    if(Duplicate[i] == 0)
    { 
        CandidateFund[k] = Temp1[i];
        AssetClass[k] = Temp2[i];
        RedemptionLimit[k] = Temp3[i];
        k++; 
    }
} 

/*Choose_Available_CandidateFunds*/
Security Fund = null;
Security Bench= null;
for(int i=0; i< CandidateFund.length; i++)
{
	boolean PutThisFund=true;
	Fund = getSecurity(CandidateFund[i]);
	if(Fund == null) continue;
	try{Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());}
		catch(Exception e){continue;}

	if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -12))){
		AssetClass assetClass = Fund.getAssetClass();
		if (assetClass != null) {  //Omit those funds that DO NOT Have ParentClassID
		
			if (Fund.getSecurityType() == 1){//Omit those ETF that Average 3-Month Volume are less than 200k;Configuration.SECURITY_TYPE_ETF=1;
				int AverageVolumeMonth = 3;
				Long Volume=0L;
				Date EndDate=CurrentDate;
				for (i=0;i<AverageVolumeMonth;i++){
					Volume=Volume+getMonthVolume(Fund.getID(),EndDate);
					EndDate=LTIDate.getNewNYSEMonth(EndDate,-(i+1));
				}
				Volume=Volume/AverageVolumeMonth;
				if (Volume<200000) {PutThisFund = false;/*Testing*/printToLog("TYPE 1 ERROR");}
			}
			
			// Omit those Funds that 12-month S.D> 1.5*Benchmark
			Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());
			double BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
			double FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
			if (FundSD > BenchSD*1.5) {PutThisFund = false;/*Testing*/printToLog("TYPE 2 ERROR");}
			
		} // End if (assetClass != null)
		else {PutThisFund = false;/*Testing*/printToLog("TYPE 3 ERROR");}
		
		if ( PutThisFund ) {CandidateFundList.add(CandidateFund[i]);}
	}
	
}
printToLog("CandidateFundList initialize ok");

/*Get the available asset classes and divide the funds*/

for(int i=0; i< MainAssetClass.length; i++)
{
    MainAssetClassList.add(MainAssetClass[i]);
}
System.out.println("MainAssetClassList initialize ok");

printToLog("MainAssetClassList initialize ok");

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);
Iterator iter = AllAssetFundsMap.keySet().iterator();
while(iter.hasNext()){
String key = (String) iter.next();
System.out.println(key);
System.out.println(AllAssetFundsMap.get(key));
}
for(int i = 0; i< MainAssetClass.length; i++)
{
    if(AllAssetFundsMap.get(MainAssetClass[i]) != null)
        AvailableAssetClassList.add(MainAssetClass[i]);
}
System.out.println("AvailableAssetClassList initialize ok");
for(int i=0;i<AvailableAssetClassList.size();++i){
System.out.println(AvailableAssetClassList.get(i));
}

printToLog("AvailableAssetClassList initialize ok");

/*Select the presentative funds of each asset class*/

HashMap<String,List<String>>  PresentativeAssetFundMap = new HashMap<String,List<String>>();
PresentativeAssetFundMap = getPresentativeAssetFunds(AllAssetFundsMap,  AvailableAssetClassList, SharpeMonths, MaxOfEachAsset);

for(int i =0; i < PresentativeAssetFundMap.size(); i++)
{
    printToLog(AvailableAssetClassList.get(i) + "'s Presentative Fund number:  " + PresentativeAssetFundMap.get(AvailableAssetClassList.get(i)).size() );
}

System.out.println("PresentativeAssetFundMap initialize ok");

printToLog("PresentativeAssetFundMap initialize ok");

/*Get the Benchmark of each asset class*/

HashMap<String, String>  AssetBenchmarkMap = new HashMap<String,String>();
String[] AssetBenchmarks = new String[AvailableAssetClassList.size()];
for(int i=0; i<AvailableAssetClassList.size();i++)
{
    if(AvailableAssetClassList.get(i).equals("High Yield Bond"))
        AssetBenchmarks[i] = "VWEHX";
    if(AvailableAssetClassList.get(i).equals("REAL ESTATE"))
        AssetBenchmarks[i] = "VGSIX";
    if(AvailableAssetClassList.get(i).equals("CASH"))
        AssetBenchmarks[i] = "CASH";
    else
        AssetBenchmarks[i] = securityManager.get(assetClassManager.get(AvailableAssetClassList.get(i)).getBenchmarkID()).getSymbol();
    AssetBenchmarkMap.put(AvailableAssetClassList.get(i), AssetBenchmarks[i]);
}

System.out.println("AssetBenchmarkMap initialize ok");
printToLog("AssetBenchmarkMap  initialize ok");

/*Calculate weights with Guru*/

Iterator iterator = AssetBenchmarkMap.keySet().iterator();
while(iterator.hasNext()){
String key = (String)iterator.next();
System.out.println(key);
System.out.println(AssetBenchmarkMap.get(key));
}
System.out.println("--------------");
for(int i=0;i<AvailableAssetClassList.size();++i){
System.out.println(AvailableAssetClassList.get(i));
}

double[] WeightDouble = getWeightFromGuru( GuruTargetFunds, AssetBenchmarks, TargetTopN, SharpeMonths,  MoneyAllocation, RAAMethod, RAAInterval, RiskAversion, CurrentDate, TimeUnit.DAILY);
/*getWeightFromGuru(String[] targetFunds,String[] factorFunds,int topN,int sharpeMonth,double moneyAllocation,String WLSOrOLS,int interval,String targetType,Date curDate,TimeUnit tu) */

for(int i = 0; i< WeightDouble.length; i++)
    printToLog("Weight of " + AvailableAssetClassList.get(i) + " =  " +  WeightDouble[i]);

List<Double> Weights = new ArrayList<Double>();
for(int i = 0; i< WeightDouble.length; i++)
    Weights.add(WeightDouble[i]);

/*Create Assets*/

Asset CurrentAsset;
for(int i=0; i < Weights.size(); i++)
{
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(AvailableAssetClassList.get(i));
CurrentAsset.setClassID(getAssetClassID(AvailableAssetClassList.get(i)));
CurrentAsset.setTargetPercentage(Weights.get(i).doubleValue());
CurrentPortfolio.addAsset(CurrentAsset);
printToLog("Assets: "+ CurrentAsset.getName() + "  weight : " + Weights.get(i));

}
System.out.println("Assets initialize ok");
printToLog("Assets initialize ok");

/*Buy representative funds*/

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
//CurrentPortfolio.sellAssetCollection(CurrentDate);

List<String> PresentativeFunds = null;
for(int i =0; i < Weights.size(); i++)
{
    PresentativeFunds = new ArrayList<String>();
    PresentativeFunds = PresentativeAssetFundMap.get(AvailableAssetClassList.get(i));
    printToLog("Assets buy: "+ AvailableAssetClassList.get(i));
    printToLog("PresentativeFund buy: "+ PresentativeFunds.get(0));
    for(int j=0; j< PresentativeFunds.size(); j++)
       CurrentPortfolio.buy(CurrentPortfolio.getAsset(AvailableAssetClassList.get(i)).getName(), PresentativeFunds.get(j), TotalAmount*Weights.get(i) / PresentativeFunds.size(), CurrentDate); 
}

	}
	//----------------------------------------------------
	//re-initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void reinit() throws Exception{
		
	}
	
	//----------------------------------------------------
	//action code
	//----------------------------------------------------	
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void action() throws Exception{
	
		
		boolean ToCheck = false;
HashMap<String,Integer> RedemptionLimitMap = new HashMap<String,Integer>(); 
List<Asset> HoldingAssetList =  CurrentPortfolio.getCurrentAssetList();
HashMap<String,List<HoldingItem>> HoldingAssetSecurityMap = new HashMap<String,List<HoldingItem>>();
HashMap<String,Double>  SecurityAvailableTradingPercentage = new HashMap<String,Double>();
HashMap<String,Double>  AssetAvailableTradingPercentage = new HashMap<String,Double>();

if(LTIDate.isLastNYSETradingDayOfMonth(CurrentDate))
{
/*form redemption limit map*/
Integer K ;
for(int i =0; i< CandidateFund.length;i++)
{ 
    K= new Integer(RedemptionLimit[i]);
    RedemptionLimitMap.put(CandidateFund[i], K);
}

/*Form Holding Map , calculate available tading amount of each security and  each asset*/
List<HoldingItem> HoldingSecurityList = null;
double ta = CurrentPortfolio.getTotalAmount(CurrentDate);
String sn;
double sa;
double aa;

for(int i = 0; i < HoldingAssetList.size(); i++)
{
    HoldingSecurityList = new ArrayList<HoldingItem>();
    HoldingSecurityList = HoldingAssetList.get(i).getHoldingItems();
    HoldingAssetSecurityMap.put(HoldingAssetList.get(i).getName(),HoldingSecurityList);
    aa = 0;
    for(int j = 0; j < HoldingSecurityList.size(); j++)
    {
        sn = HoldingSecurityList.get(j).getSymbol();
        sa = CurrentPortfolio.getAvailableTradingAmount(sn, RedemptionLimitMap.get(sn).intValue(), CurrentDate,1);
       SecurityAvailableTradingPercentage.put(sn, new Double(sa/ta));
        aa += sa;
    }
    if(aa != 0) ToCheck = true;
    AssetAvailableTradingPercentage.put(HoldingAssetList.get(i).getName(), new Double(aa/ta));
}
}

		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (ToCheck && (((CheckFrequency.equals("monthly")  || RebalanceFrequency.equals("monthly")) && LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) || ((CheckFrequency.equals("quarterly")  || RebalanceFrequency.equals("quarterly")) && LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate))) ) {
		   List<String> CandidateFundList = new ArrayList<String>();
List<String> MainAssetClassList = new ArrayList<String>();
List<String> AvailableAssetClassList = new ArrayList<String>();

/*delete the duplicate candidate fund*/

int[] Duplicate = new int[CandidateFund.length];
int k = 0;
for(int i = 0; i < CandidateFund.length; i++)
{
    for(int j = i +1; j < CandidateFund.length; j++)
    {
        if(CandidateFund[i].equals(CandidateFund[j]))
            { Duplicate[i] = 1;  break; }        
    }
    if(Duplicate[i] != 1)
        { Duplicate[i] = 0; k++; }
}

String[] Temp1 = CandidateFund;
String[] Temp2 = AssetClass;
int[] Temp3= RedemptionLimit ;
CandidateFund = new String[k];
AssetClass = new String[k];
RedemptionLimit = new int[k];
k = 0;
for(int i = 0; i < Duplicate.length; i++)
{
    if(Duplicate[i] == 0)
    { 
        CandidateFund[k] = Temp1[i];
        AssetClass[k] = Temp2[i];
        RedemptionLimit[k] = Temp3[i];
        k++; 
    }
}

/*Choose_Available_CandidateFunds*/
Security Fund = null;
Security Bench= null;
for(int i=0; i< CandidateFund.length; i++)
{
	boolean PutThisFund=true;
	Fund = getSecurity(CandidateFund[i]);
	if(Fund == null) continue;
	try{Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());}
		catch(Exception e){continue;}

	if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -12))){
		AssetClass assetClass = Fund.getAssetClass();
		if (assetClass != null) {  //Omit those funds that DO NOT Have ParentClassID
		
			if (Fund.getSecurityType() == 1){//Omit those ETF that Average 3-Month Volume are less than 200k;Configuration.SECURITY_TYPE_ETF=1;
				int AverageVolumeMonth = 3;
				Long Volume=0L;
				Date EndDate=CurrentDate;
				for (i=0;i<AverageVolumeMonth;i++){
					Volume=Volume+getMonthVolume(Fund.getID(),EndDate);
					EndDate=LTIDate.getNewNYSEMonth(EndDate,-(i+1));
				}
				Volume=Volume/AverageVolumeMonth;
				if (Volume<200000) {PutThisFund = false;/*Testing*/printToLog("TYPE 1 ERROR");}
			}
			
			// Omit those Funds that 12-month S.D> 1.5*Benchmark
			Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());
			double BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
			double FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
			if (FundSD > BenchSD*1.5) {PutThisFund = false;/*Testing*/printToLog("TYPE 2 ERROR");}
			
		} // End if (assetClass != null)
		else {PutThisFund = false;/*Testing*/printToLog("TYPE 3 ERROR");}
		
		if ( PutThisFund ) {CandidateFundList.add(CandidateFund[i]);}
	}
	
}
/*Get the available asset classes and divide the funds.  What if there comes an extra availble asset class ?*/

for(int i=0; i< MainAssetClass.length; i++)
{
    MainAssetClassList.add(MainAssetClass[i]);
}

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);
for(int i = 0; i< MainAssetClass.length; i++)
{
    if(AllAssetFundsMap.get(MainAssetClass[i]) != null)
        AvailableAssetClassList.add(MainAssetClass[i]);
}

/*Select the presentative funds of each asset class*/

HashMap<String,List<String>>  PresentativeAssetFundMap = new HashMap<String,List<String>>();
PresentativeAssetFundMap = getPresentativeAssetFunds(AllAssetFundsMap,  AvailableAssetClassList, SharpeMonths, MaxOfEachAsset);

for(int i =0; i < PresentativeAssetFundMap.size(); i++)
{
    printToLog(AvailableAssetClassList.get(i) + "'s Presentative Fund number:  " + PresentativeAssetFundMap.get(AvailableAssetClassList.get(i)).size() );
}

/*Get the Benchmark of each asset class*/

HashMap<String, String>  AssetBenchmarkMap = new HashMap<String,String>();
String[] AssetBenchmarks = new String[AvailableAssetClassList.size()];
for(int i=0; i<AvailableAssetClassList.size();i++)
{
    if(AvailableAssetClassList.get(i).equals("High Yield Bond"))
        AssetBenchmarks[i] = "VWEHX";
    if(AvailableAssetClassList.get(i).equals("REAL ESTATE"))
        AssetBenchmarks[i] = "VGSIX";
    if(AvailableAssetClassList.get(i).equals("CASH"))
        AssetBenchmarks[i] = "CASH";
    else
        AssetBenchmarks[i] = securityManager.get(assetClassManager.get(AvailableAssetClassList.get(i)).getBenchmarkID()).getSymbol();
    AssetBenchmarkMap.put(AvailableAssetClassList.get(i), AssetBenchmarks[i]);
}


/*Calculate weights with MVO*/

double[] WeightDouble = getWeightFromGuru( GuruTargetFunds, AssetBenchmarks, TargetTopN, SharpeMonths,  MoneyAllocation, RAAMethod, RAAInterval, RiskAversion, CurrentDate, TimeUnit.DAILY);
/*getWeightFromGuru(String[] targetFunds,String[] factorFunds,int topN,int sharpeMonth,double moneyAllocation,String WLSOrOLS,int interval,String targetType,Date curDate,TimeUnit tu) */

List<Double> Weights = new ArrayList<Double>();
for(int i = 0; i< WeightDouble.length; i++)
    Weights.add(WeightDouble[i]);

Asset CurrentAsset = null;
for(int i=0; i < Weights.size(); i++)
{
    if(AssetAvailableTradingPercentage.get(AvailableAssetClassList.get(i)) != null)
    {
        CurrentAsset=new Asset();
        CurrentAsset = CurrentPortfolio.getAsset(AvailableAssetClassList.get(i));
        CurrentAsset .setTargetPercentage(Weights.get(i));
        printToLog("Assets: "+ AvailableAssetClassList.get(i) + "  weight : " + Weights.get(i));
    }
    else
    {
        CurrentAsset=new Asset();
        CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
        CurrentAsset.setName(AvailableAssetClassList.get(i));
        CurrentAsset.setClassID(getAssetClassID(AvailableAssetClassList.get(i)));
        CurrentAsset.setTargetPercentage(Weights.get(i));
        CurrentPortfolio.addAsset(CurrentAsset);
        printToLog("New Assets: "+ CurrentAsset.getName() + "  weight : " + Weights.get(i));
    }
}

//========================================================================================
//========================================================================================
//========================================================================================
//updated 2010/01/28
/*trading and rebalance under redemption limit*/

/*Unsolved:Dividend caused the scrap holdings*/
HashMap<String,Integer> RedemptionMonthMap = new HashMap<String,Integer>(); 
HashMap<String,Integer> MaxOfAssetFundMap = new HashMap<String,Integer>(); 
Integer K;
for(int i =0; i< CandidateFund.length;i++)
{ 
    K = new Integer(RedemptionLimit[i]);

    RedemptionMonthMap.put(CandidateFund[i], K);
}
for(int i =0; i< Weights.size();i++)
{ 
    K= new Integer(MaxOfEachAsset);
    MaxOfAssetFundMap.put(AvailableAssetClassList.get(i), K);
}
double TotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);

/*First, Sell  as much as possible; and Detect the unFavorable Funds in each AssetClass of CurrentPortfolio
1.the poor-performance funds (i.e funds not in the PresentativeAssetFundMap)
2.Those FavorableFunds(in the PresnetativeAssetFundMap) may >TargetPercentage */
List<Asset> CurrentAssetList = CurrentPortfolio.getCurrentAssetList(); /*List<Asset> CurrentAssetList has already declared*/
HashMap<String, Double>UnfavClassAmountMap=new HashMap<String, Double>();	/*UnfavClassAmountMap Stores the amount of which those funds should be sold, but under RedemptionLimit they can not*/
List <String> FavSecurNameList=new ArrayList<String>();   
printToLog("------Sell Action ------");

for (int i=0; i<CurrentAssetList.size();i++){   
    CurrentAsset =CurrentAssetList.get(i);   
    List<HoldingItem> CurrSecurList=CurrentAsset.getHoldingItems();  
    if (PresentativeAssetFundMap.get(CurrentAsset.getName())!= null)       
        {FavSecurNameList=PresentativeAssetFundMap.get(CurrentAsset.getName());}   
    else printToLog("Unexpected error occured while trading");   
    double UnfavClassAmount=0;   
	
    for(int j=0; j<CurrSecurList.size();j++){   
        if (FavSecurNameList.indexOf(CurrSecurList.get(j).getSymbol())<0)      /*Sell the poor-performance funds as much as possible*/{    
            double SellAmount=CurrentPortfolio.getAvailableTradingAmount(CurrSecurList.get(j).getSymbol(), RedemptionMonthMap.get(CurrSecurList.get(j).getSymbol()), CurrentDate,1);
			/*Testing*/printToLog("AvailSellAmount ="+SellAmount);
            if (SellAmount>TotalAmount*MinimumSellingPercentage){   
            CurrentPortfolio.sell(CurrentAsset.getName(), CurrSecurList.get(j).getSymbol(), SellAmount, CurrentDate);   
            printToLog("SELL type 1:"+CurrentAsset.getName()+"  "+CurrSecurList.get(j).getSymbol()+"  "+SellAmount);   
			} 
			/*Unsolved: small piece of securities*/
			
            UnfavClassAmount+=(CurrSecurList.get(j).getShare()*CurrSecurList.get(j).getPrice()-SellAmount);   
        }   
        else {
        double TargetSecurPercentPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size();/*Allocate Averagely :Equal Weight for each funds in a certain AssetClass*/
        double CurrentSecurPercentPortLevel=CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),CurrSecurList.get(j).getSymbol(),CurrentDate)/TotalAmount;
		double AvailSellAmount;
			/*Action for the CASH ASSET*/
			if (!CurrSecurList.get(j).getSymbol().equals("CASH")){AvailSellAmount=CurrentPortfolio.getAvailableTradingAmount(CurrSecurList.get(j).getSymbol(), RedemptionMonthMap.get(CurrSecurList.get(j).getSymbol()), CurrentDate,1);}
			else {AvailSellAmount=CurrSecurList.get(j).getShare()*CurrSecurList.get(j).getPrice();}
			
		/*Testing*/printToLog("AvailSellAmount ="+AvailSellAmount+" ; "+" SpreadAmount= "+(CurrentSecurPercentPortLevel-TargetSecurPercentPortLevel)*TotalAmount);
        double SellAmount=java.lang.Math.min(AvailSellAmount/TotalAmount,(CurrentSecurPercentPortLevel-TargetSecurPercentPortLevel))*TotalAmount;
            if (SellAmount>TotalAmount*MinimumSellingPercentage){   
            CurrentPortfolio.sell(CurrentAsset.getName(),CurrSecurList.get(j).getSymbol(),SellAmount, CurrentDate);   
			printToLog("SELL type 2:"+CurrentAsset.getName()+"  "+CurrSecurList.get(j).getSymbol()+"  "+SellAmount);    
            }  
        }   
    }
	
    UnfavClassAmountMap.put(CurrentAsset.getName(),new Double(UnfavClassAmount));   
}  

/*Give weight to each FavorableFunds in PresentativeAssetFundMap, according to Target Percentage in ClasssLevel*/
/*???Target Percentage is in ClasssLevel, NOT the RISKY/STABLE level */

HashMap<String,Double> ClassWeightMap=new HashMap<String, Double>();
String AssetName=new String();
FavSecurNameList=new ArrayList<String>();
/*  1.Assign Weight At Class Level*/
double TempSum=0;
Iterator iter =PresentativeAssetFundMap.entrySet().iterator();
while (iter.hasNext()){
    Map.Entry<String,List<String>> entry = (Map.Entry<String,List<String>>) iter.next(); 
    AssetName = entry.getKey(); 
    FavSecurNameList = entry.getValue(); 
	CurrentAsset=CurrentPortfolio.getAsset(AssetName);
	Double Amount=CurrentAsset.getTargetPercentage()-CurrentPortfolio.getAssetAmount(CurrentAsset,CurrentDate)/TotalAmount;
	if (Amount>MinimumBuyingPercentage){
	ClassWeightMap.put(AssetName,Amount);
	TempSum=TempSum+Amount;
	/*Testing*/printToLog("Weight at Class Level:  "+AssetName+",   SpreadPercentage="+Amount);
	/*Testing*/printToLog("CurrentAssetAmount="+CurrentPortfolio.getAssetAmount(CurrentAsset,CurrentDate));
	}
	else ClassWeightMap.put(AssetName,new Double(0));
}

iter =PresentativeAssetFundMap.entrySet().iterator();
while (iter.hasNext()){
    Map.Entry<String,List<String>> entry = (Map.Entry<String,List<String>>) iter.next(); 
    AssetName = entry.getKey(); 
    FavSecurNameList = entry.getValue(); 
	CurrentAsset=CurrentPortfolio.getAsset(AssetName);
	ClassWeightMap.put(AssetName,ClassWeightMap.get(AssetName)/TempSum);
}

/*  2.Assign Weight At Security Level and Buy Action at the same time*/
double AvailCapital=CurrentPortfolio.getCash();   /*available capital that can be used to buy other funds*/  
/*Testing*/printToLog("AvailCapital = "+AvailCapital);
printToLog("--------BUY ACTION--------");

iter = PresentativeAssetFundMap.entrySet().iterator(); 
while (iter.hasNext()) { 
    Map.Entry<String,List<String>> entry = (Map.Entry<String,List<String>>) iter.next(); 
    AssetName = entry.getKey(); 
    FavSecurNameList = entry.getValue(); 
	Double UnfavPercent;
	CurrentAsset=CurrentPortfolio.getAsset(AssetName);
	double[] SecurWeight=new double[FavSecurNameList.size()];
	if (UnfavClassAmountMap.get(AssetName)!=null) {UnfavPercent=UnfavClassAmountMap.get(AssetName)/TotalAmount;}
	else {UnfavPercent=new Double(0);}
	double TotalAssetAmount=CurrentPortfolio.getAssetAmount(CurrentAsset,CurrentDate);
	if (CurrentAsset.getTargetPercentage()-UnfavPercent>MinimumBuyingPercentage) {
		double TargetSecurPercentAtPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size();/*Allocate Averagely :Equal Weight for each funds in a certain AssetClass*/
		double TempWeightTotal=0;
		for (int i=0;i<FavSecurNameList.size();i++){
			if (CurrentAsset.getHoldingItems().contains(FavSecurNameList.get(i)))/*for funds already exist*/{
				double SpreadPercent=TargetSecurPercentAtPortLevel-CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),FavSecurNameList.get(i),CurrentDate)/TotalAmount;
				if (SpreadPercent>MinimumBuyingPercentage) {SecurWeight[i]=SpreadPercent;TempWeightTotal+=SecurWeight[i];}
				}
			else if (CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),FavSecurNameList.get(i),CurrentDate)/TotalAmount>MinimumBuyingPercentage) /*for new funds*/
				{SecurWeight[i]=CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),FavSecurNameList.get(i),CurrentDate)/TotalAmount;TempWeightTotal+=SecurWeight[i];}
		}
		
		if (TempWeightTotal>0.01){
			for (int i=0; i<FavSecurNameList.size();i++){
			SecurWeight[i]=SecurWeight[i]/TempWeightTotal;
			}
			/*buy Securities*/
			for (int i=0; i<FavSecurNameList.size();i++){
				/*Testing*/printToLog("SecurWeight[i]= "+SecurWeight[i]+" ; "+" ClassWeight: = "+ClassWeightMap.get(AssetName));
				double Amount=AvailCapital*SecurWeight[i]*ClassWeightMap.get(AssetName);
				if (Amount>TotalAmount*MinimumBuyingPercentage) {
					CurrentPortfolio.buy(CurrentAsset.getName(),FavSecurNameList.get(i),Amount,CurrentDate);
					printToLog("BUY TYPE 1  "+CurrentAsset.getName()+" "+FavSecurNameList.get(i)+" "+Amount);
				
				//Unfinished
				
				} 
			}
		} //  End if (TempWeightTotal>0.01)
		
		if (TempWeightTotal<=0.01){
			int Index=FavSecurNameList.size()-1;
			for (int i=0; i<FavSecurNameList.size();i++){
				double CurrentSecurPercentPortLevel=CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),FavSecurNameList.get(i),CurrentDate)/TotalAmount;
				double TargetSecurPercentPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size();/*Allocate Averagely :Equal Weight for each funds in a certain AssetClass*/
				if (CurrentSecurPercentPortLevel + MinimumBuyingPercentage< TargetSecurPercentPortLevel){
					Index=i;
					break;
				}
			}
			/*buy Securities*/
			SecurWeight[Index]=1.0;
			double Amount=AvailCapital*SecurWeight[Index]*ClassWeightMap.get(AssetName);
			if (Amount > TotalAmount*MinimumBuyingPercentage){
			CurrentPortfolio.buy(CurrentAsset.getName(),FavSecurNameList.get(Index),Amount, CurrentDate);
			printToLog("BUY TYPE 2  "+CurrentAsset.getName()+"   "+FavSecurNameList.get(Index)+" "+Amount);
			} 
		} //  End if (TempWeightTotal<=0.01)
		
	}
}	// End of While

double Amount=CurrentPortfolio.getCash();
if (Amount > 1) {
	iter = PresentativeAssetFundMap.entrySet().iterator(); 
    Map.Entry<String,List<String>> entry = (Map.Entry<String,List<String>>) iter.next(); 
    AssetName = entry.getKey(); 
    FavSecurNameList = entry.getValue(); 
	CurrentAsset=CurrentPortfolio.getAsset(AssetName);
	CurrentPortfolio.buy(AssetName,getSecurity(FavSecurNameList.get(0)).getSymbol(), Amount, CurrentDate);
	printToLog("The Last chance to BUY: BUY TYPE 3 "+CurrentAsset.getName()+"   "+FavSecurNameList.get(0)+" "+Amount);
	}
printToLog("--------End of Action --------");
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