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
public class Guru_401k_Under_Redemption_Limit720 extends SimulateStrategy{
	public Guru_401k_Under_Redemption_Limit720(){
		super();
		StrategyID=720L;
		StrategyClassID=5L;
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

/*Choose the available candidate funds*/

for(int i=0; i< CandidateFund.length; i++)
{
    if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
    CandidateFundList.add(CandidateFund[i]);
}
System.out.println("CandidateFundList initialize ok");

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

System.out.println("PresentativeAssetFundMap initialize ok");
printToLog("PresentativeAssetFundMap initialize ok");

/*Get the Benchmark of each asset class*/

HashMap<String, String>  AssetBenchmarkMap = new HashMap<String,String>();
String[] AssetBenchmarks = new String[AvailableAssetClassList.size()];
for(int i=0; i<AvailableAssetClassList.size();i++)
{
    AssetBenchmarks[i] = securityManager.get(assetClassManager.get(AvailableAssetClassList.get(i)).getBenchmarkID()).getSymbol();
    AssetBenchmarkMap.put(AvailableAssetClassList.get(i), AssetBenchmarks[i]);
}

System.out.println("AssetBenchmarkMap initialize ok");
printToLog("AssetBenchmarkMap  initialize ok");
/*Calculate weights with MVO*/


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

List<Double> Weights = null;
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
CurrentAsset.setTargetPercentage(Weights.get(i));
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
	
		
		
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (((CheckFrequency.equals("monthly")  || RebalanceFrequency.equals("monthly")) && LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) || ((CheckFrequency.equals("quarterly")  || RebalanceFrequency.equals("quarterly")) && LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate)) ) {
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

/*Choose the available candidate funds*/

for(int i=0; i< CandidateFund.length; i++)
{
    if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
    CandidateFundList.add(CandidateFund[i]);
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

/*Get the Benchmark of each asset class*/

HashMap<String, String>  AssetBenchmarkMap = new HashMap<String,String>();
String[] AssetBenchmarks = new String[AvailableAssetClassList.size()];
for(int i=0; i<AvailableAssetClassList.size();i++)
{
    AssetBenchmarks[i] = securityManager.get(assetClassManager.get(AvailableAssetClassList.get(i)).getBenchmarkID()).getSymbol();
    AssetBenchmarkMap.put(AvailableAssetClassList.get(i), AssetBenchmarks[i]);
}


/*Calculate weights with MVO*/

double[] WeightDouble = getWeightFromGuru( GuruTargetFunds, AssetBenchmarks, TargetTopN, SharpeMonths,  MoneyAllocation, RAAMethod, RAAInterval, RiskAversion, CurrentDate, TimeUnit.DAILY);
/*getWeightFromGuru(String[] targetFunds,String[] factorFunds,int topN,int sharpeMonth,double moneyAllocation,String WLSOrOLS,int interval,String targetType,Date curDate,TimeUnit tu) */

List<Double> Weights = null;
for(int i = 0; i< WeightDouble.length; i++)
    Weights.add(WeightDouble[i]);

Asset CurrentAsset = null;
for(int i=0; i < Weights.size(); i++)
{
    CurrentAsset = CurrentPortfolio.getAsset(AvailableAssetClassList.get(i));
    if(CurrentAsset != null)
    {
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


/*trading and rebalance under redemption limit*/

HashMap<String,Integer> RedemptionMonthMap = new HashMap<String,Integer>(); 
HashMap<String,Integer> MaxOfAssetFundMap = new HashMap<String,Integer>(); 
Integer K ;
for(int i =0; i< CandidateFund.length;i++)
{ 
    K = new Integer(RedemptionLimit[i]);
    RedemptionMonthMap.put(CandidateFund[i], K);
}
for(int i =0; i< Weights.size();i++)
{ 
    K = new Integer(MaxOfEachAsset);
    MaxOfAssetFundMap.put(AvailableAssetClassList.get(i), K);
}

CurrentPortfolio.tradingUnderRedemptionLimit( RedemptionMonthMap, PresentativeAssetFundMap, MaxOfAssetFundMap , CurrentDate);

if(RebalanceFrequency.equals("monthly") || (RebalanceFrequency.equals("quarterly") && LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate))  || (RebalanceFrequency.equals("yearly") && LTIDate.isLastNYSETradingDayOfYear(CurrentDate)))
    CurrentPortfolio.rebalanceUnderRedemptionLimit(RedemptionMonthMap, PresentativeAssetFundMap, CurrentDate);
		}
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Guru_401k_Under_Redemption_Limit720.java:523: cannot find symbol
//symbol  : method tradingUnderRedemptionLimit(java.util.HashMap<java.lang.String,java.lang.Integer>,java.util.HashMap<java.lang.String,java.util.List<java.lang.String>>,java.util.HashMap<java.lang.String,java.lang.Integer>,java.util.Date)
//location: class com.lti.type.executor.SimulateStrategy
//CurrentPortfolio.tradingUnderRedemptionLimit( RedemptionMonthMap, PresentativeAssetFundMap, MaxOfAssetFundMap , CurrentDate);
//                ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Guru_401k_Under_Redemption_Limit720.java:526: cannot find symbol
//symbol  : method rebalanceUnderRedemptionLimit(java.util.HashMap<java.lang.String,java.lang.Integer>,java.util.HashMap<java.lang.String,java.util.List<java.lang.String>>,java.util.Date)
//location: class com.lti.type.executor.SimulateStrategy
//    CurrentPortfolio.rebalanceUnderRedemptionLimit(RedemptionMonthMap, PresentativeAssetFundMap, CurrentDate);
//                    ^
//2 errors