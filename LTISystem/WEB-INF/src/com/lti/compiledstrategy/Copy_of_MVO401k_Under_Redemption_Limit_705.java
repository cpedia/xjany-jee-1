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
public class Copy_of_MVO401k_Under_Redemption_Limit_705 extends SimulateStrategy{
	public Copy_of_MVO401k_Under_Redemption_Limit_705(){
		super();
		StrategyID=705L;
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
	private int MVOMonths;
	public void setMVOMonths(int MVOMonths){
		this.MVOMonths=MVOMonths;
	}
	
	public int getMVOMonths(){
		return this.MVOMonths;
	}
	private double RAF;
	public void setRAF(double RAF){
		this.RAF=RAF;
	}
	
	public double getRAF(){
		return this.RAF;
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
		MVOMonths=(Integer)ParameterUtil.fetchParameter("int","MVOMonths", "12", parameters);
		RAF=(Double)ParameterUtil.fetchParameter("double","RAF", "8", parameters);
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


public List<Double> getMVOWeight(HashMap<String, String> AssetBenchmarkMap, List<String> AvailableAssetClassList, int MVOMonths, double RAF)
 throws Exception {
LTIMVOInterface li = new LTIMVOInterface();
List<String> BenchMarkList = new ArrayList<String>();
List<Double> lowerLimits = new ArrayList<Double>();
List<Double> upperLimits = new ArrayList<Double>();
List<Double> expectedExcessReturns = new ArrayList<Double>();
Date startDate = LTIDate.getNewNYSEMonth(CurrentDate, -MVOMonths);
Date endDate = CurrentDate;

double R = 0;
Security S ;
for(int i=0;i< AvailableAssetClassList.size(); i++)
{
    BenchMarkList.add(AssetBenchmarkMap.get(AvailableAssetClassList.get(i)));
    lowerLimits.add(0.0);
    upperLimits.add(1.0);
    S = new Security();
    S = getSecurity(BenchMarkList.get(i));
    printToLog("Benchmark calculating is " + S.getSymbol());
    R = S.getReturn(CurrentDate, TimeUnit.MONTHLY,-1,false) * MVOMonths;
    for(int j=1; j< MVOMonths;j++)
    {
        R += S.getReturn(LTIDate.getNewNYSEMonth(CurrentDate, -i), TimeUnit.MONTHLY,-i-1,false) * (MVOMonths-i);
    }
    expectedExcessReturns.add(R/ ((1+MVOMonths)*(MVOMonths/2)) /22);  /*Daily return*/
}

li.createModel(BenchMarkList, lowerLimits, upperLimits, expectedExcessReturns, RAF, startDate,  endDate, TimeUnit.DAILY, true); /* false means using NAV instead of price */

List<Double> Weights=null;
try{
   Weights = li.getMVOWeightsWithLimits();
}catch(Exception e){
   printToLog(StringUtil.getStackTraceString(e));
printToLog(startDate);
printToLog(endDate);
printToLog(RAF);
  throw e;
}

double SumWeight = 0;
for(int i=0; i< Weights.size(); i++)
    SumWeight += Weights.get(i);
double W = 0;
if(SumWeight != 1)
{
    for(int i=0; i< Weights.size(); i++)
    {   
        W = Weights.get(i);
        Weights.set(i, W/SumWeight);
    }   
}
return Weights;
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

Security Fund = null;
Security Bench = null;
double FundSD =0;
double BenchSD = 0;
for(int i=0; i< CandidateFund.length; i++)
{
    Fund = getSecurity(CandidateFund[i]);
    if(Fund == null) continue;
    try
    {
        Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());  
        if(!LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
             continue;
        if(!LTIDate.before(getEarliestAvaliablePriceDate(Fund), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
             continue;
    FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
    BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
    printToLog("Fund SD = " + FundSD + "  1.5* Bench SD = " + (1.5*BenchSD));
    }
        catch(Exception e)  {continue;}   
    if(FundSD < 1.5* BenchSD) 
        CandidateFundList.add(CandidateFund[i]);
}
System.out.println("CandidateFundList initialize ok");

printToLog("CandidateFundList size = " + CandidateFundList.size());

for(int i = 0; i < CandidateFundList.size(); i ++)
{
    printToLog("Available CandidateFund "+i+" = " + CandidateFundList.get(i));
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

printToLog("AvailableAssetClassList. size = " + AvailableAssetClassList.size());

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
for(int i=0; i<AvailableAssetClassList.size();i++)
{
    if(AvailableAssetClassList.get(i).equals("High Yield Bond"))
        AssetBenchmarkMap.put(AvailableAssetClassList.get(i), "VWEHX");
    if(AvailableAssetClassList.get(i).equals("REAL ESTATE"))
        AssetBenchmarkMap.put(AvailableAssetClassList.get(i), "VGSIX");    
    if(AvailableAssetClassList.get(i).equals("CASH"))
        AssetBenchmarkMap.put(AvailableAssetClassList.get(i), "CASH");    
    else
        AssetBenchmarkMap.put(AvailableAssetClassList.get(i), securityManager.get(assetClassManager.get(AvailableAssetClassList.get(i)).getBenchmarkID()).getSymbol());
    printToLog("Benchmark of " + AvailableAssetClassList.get(i) + " is : " + securityManager.get(assetClassManager.get(AvailableAssetClassList.get(i)).getBenchmarkID()).getSymbol());
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

List<Double> Weights = getMVOWeight( AssetBenchmarkMap, AvailableAssetClassList, MVOMonths,  RAF);

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
if(Weights.get(i)!=0){
    PresentativeFunds = new ArrayList<String>();
    PresentativeFunds = PresentativeAssetFundMap.get(AvailableAssetClassList.get(i));
    printToLog("Assets buy: "+ AvailableAssetClassList.get(i));
    printToLog("PresentativeFund buy: "+ PresentativeFunds.get(0));
    for(int j=0; j< PresentativeFunds.size(); j++){
       
       CurrentPortfolio.buy(CurrentPortfolio.getAsset(AvailableAssetClassList.get(i)).getName(), PresentativeFunds.get(j), TotalAmount*Weights.get(i) / PresentativeFunds.size(), CurrentDate); 
}
}
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
        sa = CurrentPortfolio.getAvailableTradingAmount(sn, RedemptionLimitMap.get(sn).intValue(), CurrentDate,false);
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
printToLog(CurrentPortfolio.getTotalAmount(CurrentDate));
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

Security Fund = null;
Security Bench = null;
double FundSD =0;
double BenchSD = 0;
List<HoldingItem> CurrentHoldings = CurrentPortfolio.getHoldingItems();
for(int i=0; i< CandidateFund.length; i++)
{
    Fund = getSecurity(CandidateFund[i]);
    if(Fund == null) continue;
    try
    {
        Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());  
        if(!LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
             continue;
        if(!LTIDate.before(getEarliestAvaliablePriceDate(Fund), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
             continue;
    FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
    BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
    }
        catch(Exception e)  {continue;}   
    if(FundSD < 1.5* BenchSD) 
        CandidateFundList.add(CandidateFund[i]);
    else
    {
        for(int j =0; j < CurrentHoldings.size(); j++)
        {
            if(CurrentHoldings.get(j).getSymbol().equals(CandidateFund[i]))
                       CandidateFundList.add(CandidateFund[i]);
        }

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

/*Get the Benchmark of each asset class*/

HashMap<String, String>  AssetBenchmarkMap = new HashMap<String,String>();
for(int i=0; i<AvailableAssetClassList.size();i++)
{
    if(AvailableAssetClassList.get(i).equals("High Yield Bond"))
        AssetBenchmarkMap.put(AvailableAssetClassList.get(i), "VWEHX");
    if(AvailableAssetClassList.get(i).equals("REAL ESTATE"))
        AssetBenchmarkMap.put(AvailableAssetClassList.get(i), "VGSIX");    
    if(AvailableAssetClassList.get(i).equals("CASH"))
        AssetBenchmarkMap.put(AvailableAssetClassList.get(i), "CASH");  
    else
        AssetBenchmarkMap.put(AvailableAssetClassList.get(i), securityManager.get(assetClassManager.get(AvailableAssetClassList.get(i)).getBenchmarkID()).getSymbol());
}

/*Select the presentative funds of each asset class*/

HashMap<String,List<String>>  PresentativeAssetFundMap = new HashMap<String,List<String>>();
PresentativeAssetFundMap = getPresentativeAssetFunds(AllAssetFundsMap,  AvailableAssetClassList, SharpeMonths, MaxOfEachAsset);

for(int i =0; i < PresentativeAssetFundMap.size(); i++)
{
    printToLog(AvailableAssetClassList.get(i) + "'s Presentative Fund number:  " + PresentativeAssetFundMap.get(AvailableAssetClassList.get(i)).size() );
}


/*Calculate weights with MVO*/

List<Double> Weights = getMVOWeight( AssetBenchmarkMap, AvailableAssetClassList, MVOMonths,  RAF);

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


/*calculate the asset trading percentage*/

HashMap<String,Double>  AssetTargetPercentageMap = new HashMap<String,Double>();
HashMap<String,Double>  HoldingAssetActualPercentage = new HashMap<String,Double>();
double ta = CurrentPortfolio.getTotalAmount(CurrentDate);
double aa;

for(int i=0; i < Weights.size(); i++)
    AssetTargetPercentageMap.put(AvailableAssetClassList.get(i), new Double(Weights.get(i)) );

for(int i = 0; i < HoldingAssetList.size(); i++)
{
    aa = CurrentPortfolio.getAssetAmount(HoldingAssetList.get(i).getName(),CurrentDate);
    HoldingAssetActualPercentage.put(HoldingAssetList.get(i).getName(), new Double(aa/ta));
}

HashMap<String,Double>  AssetActualTradingPercentage = new HashMap<String,Double>();
String AssetName;
double UnableToSellPercentage = 0 ;
double AvailableTradingPercentage;
double TargetPercentage;
double ActualPercentage;
double TotalTargetSellPercentage = 0;
double TotalActualTradingPercentage;

for(int i = 0; i < HoldingAssetList.size(); i++)
{
    AssetName = HoldingAssetList.get(i).getName();
    ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
   AvailableTradingPercentage =  AssetAvailableTradingPercentage.get(AssetName).doubleValue();
    if(AssetTargetPercentageMap.get(AssetName) != null)
        TargetPercentage = AssetTargetPercentageMap.get(AssetName).doubleValue();
    else TargetPercentage = 0;
    if(ActualPercentage > TargetPercentage)
    {
        TotalTargetSellPercentage += (ActualPercentage - TargetPercentage);
        if(AvailableTradingPercentage >= ActualPercentage - TargetPercentage)
            AssetActualTradingPercentage.put(AssetName, new Double(TargetPercentage - ActualPercentage));
        else
        {
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
int[] AbleToBuy = new int[AvailableAssetClassList.size()];
double[] PlanToBuyPercentage = new double[AvailableAssetClassList.size()];
for(int i = 0; i <  AvailableAssetClassList.size(); i++)
{
    AssetName =  AvailableAssetClassList.get(i);
    TargetPercentage = AssetTargetPercentageMap.get(AssetName).doubleValue();
    if(HoldingAssetActualPercentage.get(AssetName) != null)
        ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
    else ActualPercentage = 0;
    if(ActualPercentage <= TargetPercentage)
    {
        if((TargetPercentage - ActualPercentage)*BuyWeight > MinimumBuyingPercentage)
            AssetActualTradingPercentage.put(AssetName, new Double((TargetPercentage - ActualPercentage)*BuyWeight) );
        else
        {
            AssetActualTradingPercentage.put(AssetName, new Double(0));
            UnableToBuyPercentage += ((TargetPercentage - ActualPercentage)*BuyWeight); 
            BelowMinimum = true;
            if((TargetPercentage - ActualPercentage) >= MinimumBuyingPercentage)
                {AbleToBuy[i] = 1; PlanToBuyPercentage[i] = TargetPercentage - ActualPercentage;  AbleToBuyBoolean = true;}
            else AbleToBuy[i] = 0;
        }
    }
}

if(BelowMinimum && AbleToBuyBoolean)
{
    int i = 0;
    while(UnableToBuyPercentage > 0 && i < AvailableAssetClassList.size())
    {
        AssetName =  AvailableAssetClassList.get(i);
        if(AbleToBuy[i] == 1)
        {
            if(PlanToBuyPercentage[i] < UnableToBuyPercentage)
            {
                AssetActualTradingPercentage.put(AssetName, new Double(PlanToBuyPercentage[i]));
                UnableToBuyPercentage -= PlanToBuyPercentage[i];
            }
            else
            {
                AssetActualTradingPercentage.put(AssetName, new Double(UnableToBuyPercentage));  
                UnableToBuyPercentage = 0;
            }
        }
        i++;
    }
}

/*Calculate: use the left percentage to buy the asset with the max Target Percentage */
if(UnableToBuyPercentage >0)
{
    String MaxTargetPercentageAsset = AvailableAssetClassList.get(0);
    double MaxTargetPercentage = AssetTargetPercentageMap.get(MaxTargetPercentageAsset).doubleValue();
    double CompareTargetPercentage;
    for(int i =1; i < AvailableAssetClassList.size(); i++)
    {
        CompareTargetPercentage = AssetTargetPercentageMap.get(AvailableAssetClassList.get(i)).doubleValue();
        if(CompareTargetPercentage > MaxTargetPercentage)
        {
            MaxTargetPercentage = CompareTargetPercentage;
            MaxTargetPercentageAsset  = AvailableAssetClassList.get(i);
        }
    }
    double TempTrading =  AssetActualTradingPercentage.get(MaxTargetPercentageAsset).doubleValue();
    AssetActualTradingPercentage.put(MaxTargetPercentageAsset, new Double(TempTrading + UnableToBuyPercentage));
}

for(int i = 0; i < AvailableAssetClassList.size(); i++)
    printToLog(AvailableAssetClassList.get(i) + " trading percentage = " + AssetActualTradingPercentage.get(AvailableAssetClassList.get(i)).doubleValue());


/*Calculate trading percentage of each security, not rebalance*/

HashMap<String,HashMap<String, Double>>  AssetSecurityTradingPercentageMap = new  HashMap<String,HashMap<String, Double>>();
HashMap<String, Double>  SecurityActualTradingPercentage = null;
HashMap<String, Double>  SecurityTargetPercentageMap = new HashMap<String,Double>();
HashMap<String, Double>  HoldingSecurityActualPercentage = new HashMap<String,Double>();
List<HoldingItem> HoldingSecurityList = null;
List<String> PresentativeSecurityList = null;
double TotalBuyingPercentage;
double SellingPercentage;
double BuyingPercentage;
double SecurityActualPercentage;
double MaxOfSecurityPercentage;
String SecurityName;
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);

for(int i = 0; i < AvailableAssetClassList.size(); i++)
{
    AssetName =  AvailableAssetClassList.get(i);
    HoldingSecurityList = new ArrayList<HoldingItem>();
    PresentativeSecurityList = new ArrayList<String>();
    PresentativeSecurityList = PresentativeAssetFundMap.get(AssetName);
    TargetPercentage = AssetTargetPercentageMap.get(AssetName).doubleValue();
    if(HoldingAssetActualPercentage.get(AssetName) != null)
    {
        HoldingSecurityList = CurrentPortfolio.getAsset(AssetName).getHoldingItems();
        ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
        AvailableTradingPercentage =  AssetAvailableTradingPercentage.get(AssetName).doubleValue();
    }
    else  
        { HoldingSecurityList = null ; ActualPercentage = 0; AvailableTradingPercentage =0; }
     printToLog("i = " + i + "  ActualPercentage = " + ActualPercentage + "  TargetPercentage = " + TargetPercentage + "  AvailableTradingPercentage = " + AvailableTradingPercentage + "  AssetActualTradingPercentage = " + AssetActualTradingPercentage.get(AssetName).doubleValue() );

    if(AvailableTradingPercentage <= ActualPercentage - TargetPercentage  && AvailableTradingPercentage != 0 )
    {
        SecurityActualTradingPercentage = new HashMap<String, Double>();
        for(int j = 0; j< HoldingSecurityList.size(); j++)
        {
            SellingPercentage  = SecurityAvailableTradingPercentage.get(HoldingSecurityList.get(j).getSymbol());
            SecurityActualTradingPercentage.put(HoldingSecurityList.get(j).getSymbol(), new Double(-SellingPercentage));
        }
        AssetSecurityTradingPercentageMap.put(AssetName, SecurityActualTradingPercentage);
    }
    else if(AvailableTradingPercentage == 0 && ActualPercentage == 0)
    {
        SecurityActualTradingPercentage = new HashMap<String, Double>();
        BuyingPercentage = AssetActualTradingPercentage.get(AssetName).doubleValue() / PresentativeSecurityList.size();      
        for(int j = 0; j< PresentativeSecurityList.size(); j++)
            SecurityActualTradingPercentage.put(PresentativeSecurityList.get(j), new Double(BuyingPercentage));
        AssetSecurityTradingPercentageMap.put(AssetName, SecurityActualTradingPercentage);        
    }
    else if(AvailableTradingPercentage > ActualPercentage - TargetPercentage)
    {
        SecurityActualTradingPercentage = new HashMap<String, Double>();
        for(int j = 0; j< HoldingSecurityList.size(); j++)
        {
            SellingPercentage  = SecurityAvailableTradingPercentage.get(HoldingSecurityList.get(j).getSymbol());
            SecurityActualTradingPercentage.put(HoldingSecurityList.get(j).getSymbol(), new Double(-SellingPercentage));
        }
        TotalBuyingPercentage = AssetActualTradingPercentage.get(AssetName).doubleValue() + AvailableTradingPercentage ;          
        MaxOfSecurityPercentage = TargetPercentage / PresentativeSecurityList.size();
        printToLog(" Actual Target Percentage = " + (TotalBuyingPercentage + ActualPercentage - AvailableTradingPercentage));
        int j=0;
        while(TotalBuyingPercentage > 0.001)
        {
             SecurityName = PresentativeSecurityList.get(j);
             if(SecurityAvailableTradingPercentage.get(SecurityName) == null)
                 { SecurityActualPercentage = 0; SellingPercentage = 0; }
             else
             {
                 SecurityActualPercentage = CurrentPortfolio.getSecurityAmount(AssetName, SecurityName, CurrentDate) / TotalAmount; 
                 SellingPercentage = SecurityAvailableTradingPercentage.get(SecurityName);
             }
             printToLog("j = " + j + "  SecurityActualPercentage = " + SecurityActualPercentage + "  SellingPercentage = " + SellingPercentage + "  MaxOfSecurityPercentage = " + MaxOfSecurityPercentage  + "  TotalBuyingPercentage = " + TotalBuyingPercentage);
             if(SecurityActualPercentage - SellingPercentage >= MaxOfSecurityPercentage)
             {printToLog("condition 1");  }
             else if(SecurityActualPercentage - SellingPercentage + TotalBuyingPercentage > MaxOfSecurityPercentage+ 0.001)
             {
                 printToLog("condition 2"); 
                 SecurityActualTradingPercentage.put(SecurityName, new Double(MaxOfSecurityPercentage - SecurityActualPercentage));
                 TotalBuyingPercentage -= (MaxOfSecurityPercentage - SecurityActualPercentage + SellingPercentage);
             }
             else if(SecurityActualPercentage - SellingPercentage + TotalBuyingPercentage <= MaxOfSecurityPercentage + 0.001)
             {
                 printToLog("condition 3"); 
                 SecurityActualTradingPercentage.put(SecurityName, new Double(TotalBuyingPercentage - SellingPercentage));
                 TotalBuyingPercentage = 0;
             }
             j++;
        }
        AssetSecurityTradingPercentageMap.put(AssetName, SecurityActualTradingPercentage);     
    }
}

/*Caculate the security trading, with rebalance */



/*Do the selling transaction*/


for(int i = 0; i < AvailableAssetClassList.size(); i++)
{
    AssetName = AvailableAssetClassList.get(i);
    SecurityActualTradingPercentage = new HashMap<String, Double>();
    SecurityActualTradingPercentage = AssetSecurityTradingPercentageMap.get(AssetName);
    if(SecurityActualTradingPercentage != null)
    {
        Iterator iter = SecurityActualTradingPercentage.keySet().iterator();
        while (iter.hasNext())
        {      
        String TradingSecurity = (String)iter.next(); 
        double TradePercentage = SecurityActualTradingPercentage.get(TradingSecurity).doubleValue(); 
        if(TradePercentage < 0)
            CurrentPortfolio.sell(AssetName, TradingSecurity, TotalAmount * (- TradePercentage), CurrentDate);
        }
    }
}

/*Do the buying transaction*/

for(int i = 0; i < AvailableAssetClassList.size(); i++)
{
    AssetName = AvailableAssetClassList.get(i);
    SecurityActualTradingPercentage = new HashMap<String, Double>();
    SecurityActualTradingPercentage = AssetSecurityTradingPercentageMap.get(AssetName);
    if(SecurityActualTradingPercentage != null)
    {
        Iterator iter = SecurityActualTradingPercentage.entrySet().iterator();
        while (iter.hasNext())
        {
        Map.Entry entry = (Map.Entry) iter.next();         
        String TradingSecurity = (String)entry.getKey(); 
        Double TradePercentage = (Double)entry.getValue(); 
        if(TradePercentage.doubleValue() > 0)
            CurrentPortfolio.buy(AssetName, TradingSecurity, TotalAmount * TradePercentage.doubleValue(), CurrentDate);
        }
    }
}





/*trading and rebalance under redemption limit

HashMap<String,Integer> RedemptionMonthMap = new HashMap<String,Integer>(); 
HashMap<String,Integer> MaxOfAssetFundMap = new HashMap<String,Integer>(); 
Integer K ;
for(int i =0; i< CandidateFund.length;i++)
{ 
    K= new Integer(RedemptionLimit[i]);
    RedemptionMonthMap.put(CandidateFund[i], K);
}
for(int i =0; i< Weights.size();i++)
{ 
    K = new Integer(MaxOfEachAsset);
    MaxOfAssetFundMap.put(AvailableAssetClassList.get(i), K);
}

tradingUnderRedemptionLimit( RedemptionMonthMap, PresentativeAssetFundMap, MaxOfAssetFundMap , CurrentDate);

if(RebalanceFrequency.equals("monthly") || (RebalanceFrequency.equals("quarterly") && LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate))  || (RebalanceFrequency.equals("yearly") && LTIDate.isLastNYSETradingDayOfYear(CurrentDate)))
    rebalanceUnderRedemptionLimit(RedemptionMonthMap, PresentativeAssetFundMap, CurrentDate);
*/
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