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
public class Optimized_Asset_Allocation_746 extends SimulateStrategy{
	public Optimized_Asset_Allocation_746(){
		super();
		StrategyID=746L;
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
	private boolean UseRiskProfile;
	public void setUseRiskProfile(boolean UseRiskProfile){
		this.UseRiskProfile=UseRiskProfile;
	}
	
	public boolean getUseRiskProfile(){
		return this.UseRiskProfile;
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
	private double[] YearsToRetireGroup;
	public void setYearsToRetireGroup(double[] YearsToRetireGroup){
		this.YearsToRetireGroup=YearsToRetireGroup;
	}
	
	public double[] getYearsToRetireGroup(){
		return this.YearsToRetireGroup;
	}
	private double[] RiskProfileGroup;
	public void setRiskProfileGroup(double[] RiskProfileGroup){
		this.RiskProfileGroup=RiskProfileGroup;
	}
	
	public double[] getRiskProfileGroup(){
		return this.RiskProfileGroup;
	}
	private int CheckDate;
	public void setCheckDate(int CheckDate){
		this.CheckDate=CheckDate;
	}
	
	public int getCheckDate(){
		return this.CheckDate;
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
		RebalanceFrequency=(String)ParameterUtil.fetchParameter("String","RebalanceFrequency", "monthly", parameters);
		SharpeMonths=(Integer)ParameterUtil.fetchParameter("int","SharpeMonths", "12", parameters);
		MVOMonths=(Integer)ParameterUtil.fetchParameter("int","MVOMonths", "12", parameters);
		RAF=(Double)ParameterUtil.fetchParameter("double","RAF", "8", parameters);
		MinimumBuyingPercentage=(Double)ParameterUtil.fetchParameter("double","MinimumBuyingPercentage", "0.01", parameters);
		MinimumSellingPercentage=(Double)ParameterUtil.fetchParameter("double","MinimumSellingPercentage", "0", parameters);
		UseRiskProfile=(Boolean)ParameterUtil.fetchParameter("boolean","UseRiskProfile", "false", parameters);
		RiskProfile=(Double)ParameterUtil.fetchParameter("double","RiskProfile", "20", parameters);
		LetRiskProfileChange=(Boolean)ParameterUtil.fetchParameter("boolean","LetRiskProfileChange", "false", parameters);
		YearsToRetireGroup=(double[])ParameterUtil.fetchParameter("double[]","YearsToRetireGroup", "40,35,30,25,20,15,10,5,0,-5,-10,-12,-15", parameters);
		RiskProfileGroup=(double[])ParameterUtil.fetchParameter("double[]","RiskProfileGroup", "12,15,16,17,24,30,38,48,51,56,78,80,90", parameters);
		CheckDate=(Integer)ParameterUtil.fetchParameter("int","CheckDate", "31", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double OriginalYearsToRetire = 0;
Date StartDate;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("OriginalYearsToRetire: ");
		sb.append(OriginalYearsToRetire);
		sb.append("\n");
		sb.append("StartDate: ");
		sb.append(StartDate);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(OriginalYearsToRetire);
		stream.writeObject(StartDate);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		OriginalYearsToRetire=(Double)stream.readObject();;
		StartDate=(Date)stream.readObject();;
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






public double[] getMVOWeight(HashMap<String, String> AssetBenchmarkMap, List<String> AvailableAssetClassList, int MVOMonths, double RAF, double RiskProfile)
 throws Exception {
String Method = "EMA";
String[] BenchMarks = new String[AvailableAssetClassList.size()];

for(int i=0;i< AvailableAssetClassList.size(); i++)
{
    BenchMarks[i] = AssetBenchmarkMap.get(AvailableAssetClassList.get(i));
    printToLog("Benck " + i + " = " + BenchMarks[i]);
}

int[] IsStableAsset = new int[AvailableAssetClassList.size()];
for(int i =0; i < IsStableAsset.length; i++)
{
    if(AvailableAssetClassList.get(i).equals("FIXED INCOME") || AvailableAssetClassList.get(i).equals("INTERNATIONAL BONDS") || AvailableAssetClassList.get(i).equals("CASH"))
        IsStableAsset[i] = 1;
}

double[][]  Constraints = new double[AvailableAssetClassList.size()+1][AvailableAssetClassList.size()+1];
for(int i = 0 ; i < AvailableAssetClassList.size(); i++)
    if(i < AvailableAssetClassList.size())
    {  
        Constraints[i][i] = -1; 
        if(IsStableAsset[i] == 1)  
            Constraints[i][AvailableAssetClassList.size()] = -1;
        else
            Constraints[i][AvailableAssetClassList.size()] = (double)-2/3;
    }

for(int i = 0; i < AvailableAssetClassList.size()+1; i++)
{
    if(i < AvailableAssetClassList.size())
        Constraints[AvailableAssetClassList.size()][i] = IsStableAsset[i];
    else
        Constraints[AvailableAssetClassList.size()][i] = RiskProfile/100;
}

/*
for(int i = 0; i < AvailableAssetClassList.size()+1; i++)
{
    if(i < AvailableAssetClassList.size())
    {
        if(IsStableAsset[i] == 0)
            Constraints[AvailableAssetClassList.size()+1][i] = -1;
        else
            Constraints[AvailableAssetClassList.size()+1][i] = 0;
    }
    else
        Constraints[AvailableAssetClassList.size()+1][i] = -(1-RiskProfile/100);
}
*/

String con = new String();
for(int i=0;i<AvailableAssetClassList.size()+1;++i){
      for(int j=0;j<AvailableAssetClassList.size()+1;++j){
      con+= Constraints[i][j]+" ";
}
      con+="\n";
}
printToLog(con); 

double[] Weights = MVOwithLinearLimit(BenchMarks , RAF, 12, Method, Constraints, CurrentDate);

return Weights;

}

/*
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
    expectedExcessReturns.add(R/ ((1+MVOMonths)*(MVOMonths/2)) /22);  //Daily return
}

li.createModel(BenchMarkList, lowerLimits, upperLimits, expectedExcessReturns, RAF, startDate,  endDate, TimeUnit.DAILY, true);   // false means using NAV instead of price 

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
*/


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
double ConservativeRiskProfile = 60;
double ModerateRiskProfile = 40;
double GrowthRiskProfile = 20;
double StandardRiskProfile = 0;
StartDate = CurrentPortfolio.getStartingDate();

if(UseRiskProfile)
{
    if(LetRiskProfileChange)
    {
    for(int i = 0; i < RiskProfileGroup.length - 1; i++)
        if(RiskProfile >= RiskProfileGroup[i] && RiskProfile < RiskProfileGroup[i+1])
            OriginalYearsToRetire = YearsToRetireGroup[i];
    }
    
    if(RiskProfile <= GrowthRiskProfile)
        { StandardRiskProfile = GrowthRiskProfile; RAF = 4; }
    else if(RiskProfile <= ModerateRiskProfile)
        { StandardRiskProfile = ModerateRiskProfile; RAF = 8; }
    else 
        { StandardRiskProfile = ConservativeRiskProfile ; RAF = 12 ; }

    RAF = RAF * RiskProfile / StandardRiskProfile;
    printToLog("RAF = " + RAF);
}

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
        RedemptionLimit[k] = Temp3[i];
        k++; 
    }
} 

/*get the asset class of the candidate funds*/

AssetClass = new String[CandidateFund.length];
for(int i = 0; i < CandidateFund.length; i++)
    try{AssetClass[i] = getSecurity(CandidateFund[i]).getAssetClass().getName();}  
        catch(Exception e){AssetClass[i] = "Unknow";}

/* Choose_Available_CandidateFunds (Version 2010.02.04)*/
Security Fund = null;
Security Bench= null;

Calendar cal = Calendar.getInstance(); // for calculation of MPT
cal.setTime(CurrentDate); //for calculation of MPT
long LastYear = cal.get(Calendar.YEAR)-1; //for calculation of MPT

boolean HaveCash = false;
for(int i=0; i< CandidateFund.length; i++)
{
	if(CandidateFund[i].equals("CASH"))
        {CandidateFundList.add("CASH"); HaveCash = true; continue;}
	
	boolean PutThisFund=true;
	Fund = getSecurity(CandidateFund[i]);
	
	if(Fund == null) continue;
	try{Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());}
		catch(Exception e){continue;}

	if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -12))){
		AssetClass assetClass = Fund.getAssetClass();
		
		//Omit those funds that DO NOT Have ParentClassID
		if (assetClass != null) {
		
			//Omit those ETF that Average 3-Month Volume are less than 200k;Configuration.SECURITY_TYPE_ETF=1;
			if (Fund.getSecurityType() == 1){
				int AverageVolumeMonth = 3;
				Long Volume=0L;
				Date EndDate=CurrentDate;
				for ( int t = 0; t<AverageVolumeMonth;t++){
					EndDate=LTIDate.getNewNYSEMonth(EndDate,-t);
					Volume=Volume+getMonthVolume(Fund.getID(),EndDate);
				}
				Volume=Volume/AverageVolumeMonth;
				if (Volume < 200000) 
					{PutThisFund = false; printToLog("TYPE 1 Exclude "+ Fund.getID()); continue;} //TYPE 1
			}
			
			// Omit those Funds that 12-month S.D> 1.5*Benchmark  (Version 2010.02.04)
			double BenchSD; double FundSD;
			Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());
			SecurityMPT BenchMPT = securityManager.getSecurityMPT(Bench.getID(),LastYear);
			SecurityMPT FundMPT = securityManager.getSecurityMPT(Fund.getID(),LastYear);
			if (BenchMPT != null){BenchSD = BenchMPT.getStandardDeviation();}
				else {BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);}
			if (FundMPT != null) {FundSD =FundMPT.getStandardDeviation();}
				else {FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);}
			
			if (FundSD > BenchSD * 1.5) 
				{PutThisFund = false; printToLog("TYPE 2 Exclude "+ Fund.getID()); continue;} //TYPE 2
			
		} // End if (assetClass != null)
		else { PutThisFund = false; printToLog("TYPE 3 Exclude "+ Fund.getID()); continue;}  //TYPE 3
		
		if ( PutThisFund ) {CandidateFundList.add(CandidateFund[i]);}
	}
	
}
if(HaveCash == false) {CandidateFundList.add("CASH");}


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
    if(AvailableAssetClassList.get(i).equals("CASH"))
        AssetBenchmarkMap.put(AvailableAssetClassList.get(i), "CASH");    
    else
        AssetBenchmarkMap.put(AvailableAssetClassList.get(i), securityManager.get(assetClassManager.get(AvailableAssetClassList.get(i)).getBenchmarkID()).getSymbol());
    printToLog("Benchmark of " + AvailableAssetClassList.get(i) + " is : " + AssetBenchmarkMap.get(AvailableAssetClassList.get(i)));
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
double[] Weights = getMVOWeight( AssetBenchmarkMap, AvailableAssetClassList, MVOMonths,  RAF, RiskProfile);

/*Create Assets*/

Asset CurrentAsset = null;
for(int i=0; i < Weights.length; i++)
{
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(AvailableAssetClassList.get(i));
CurrentAsset.setClassID(getAssetClassID(AvailableAssetClassList.get(i)));
CurrentAsset.setTargetPercentage(Weights[i]);
CurrentPortfolio.addAsset(CurrentAsset);
printToLog("Assets: "+ CurrentAsset.getName() + "  weight : " + Weights[i]);

}
System.out.println("Assets initialize ok");
printToLog("Assets initialize ok");

/*Buy representative funds*/

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);

List<String> PresentativeFunds = null;
for(int i =0; i < Weights.length ; i++)
{
    PresentativeFunds = new ArrayList<String>();
    PresentativeFunds = PresentativeAssetFundMap.get(AvailableAssetClassList.get(i));
    printToLog("Assets buy: "+ AvailableAssetClassList.get(i));
    printToLog("PresentativeFund buy: "+ PresentativeFunds.get(0));
    for(int j=0; j< PresentativeFunds.size(); j++)
       CurrentPortfolio.buyAtNextOpen(AvailableAssetClassList.get(i), PresentativeFunds.get(j), TotalAmount*Weights[i]/ PresentativeFunds.size(), CurrentDate,true); 
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
RedemptionLimitMap.put("CASH", 0); //Important Amendment~~~ 2010.02.05
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
        printToLog("Holdings : " + sn);
        sa = CurrentPortfolio.getAvailableTradingAmount(sn, RedemptionLimitMap.get(sn).intValue(), CurrentDate, 1);
       SecurityAvailableTradingPercentage.put(sn, new Double(sa/ta));
        aa += sa;
    }
    if(aa != 0) ToCheck = true;
    AssetAvailableTradingPercentage.put(HoldingAssetList.get(i).getName(), new Double(aa/ta));
}
}

		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (ToCheck && ((RebalanceFrequency.equals("monthly") && LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) || (RebalanceFrequency.equals("quarterly") && LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate))) ) {
		   List<String> CandidateFundList = new ArrayList<String>();
List<String> MainAssetClassList = new ArrayList<String>();
List<String> AvailableAssetClassList = new ArrayList<String>();
double ConservativeRiskProfile = 60;
double ModerateRiskProfile = 40;
double GrowthRiskProfile = 20;
double StandardRiskProfile = 0;

if(UseRiskProfile)
{
    if(LetRiskProfileChange)
    {
    double YearsToRetire = OriginalYearsToRetire -  java.lang.Math.floor(LTIDate.calculateInterval(StartDate,CurrentDate) / 365);   
    printToLog("YearsToRetire = " + YearsToRetire);
    for(int i = 0; i < YearsToRetireGroup.length - 1; i++)
        if(YearsToRetire <= YearsToRetireGroup[i] && YearsToRetire > YearsToRetireGroup[i+1])
            RiskProfile = RiskProfileGroup[i];
    }

    if(RiskProfile <= GrowthRiskProfile)
        { StandardRiskProfile = GrowthRiskProfile; RAF = 4; }
    else if(RiskProfile <= ModerateRiskProfile)
        { StandardRiskProfile = ModerateRiskProfile; RAF = 8; }
    else 
        { StandardRiskProfile = ConservativeRiskProfile ; RAF = 12 ; }

    RAF *= RiskProfile / StandardRiskProfile;
}


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

AssetClass = new String[CandidateFund.length];
for(int i = 0; i < CandidateFund.length; i++)
    try{AssetClass[i] = getSecurity(CandidateFund[i]).getAssetClass().getName();}  
        catch(Exception e){AssetClass[i] = "Unknow";}

/* Choose_Available_CandidateFunds (Version 2010.02.04)*/
Security Fund = null;
Security Bench= null;

Calendar cal = Calendar.getInstance(); // for calculation of MPT
cal.setTime(CurrentDate); //for calculation of MPT
long LastYear = cal.get(Calendar.YEAR)-1; //for calculation of MPT

boolean HaveCash = false;
for(int i=0; i< CandidateFund.length; i++)
{
	if(CandidateFund[i].equals("CASH"))
        {CandidateFundList.add("CASH"); HaveCash = true; continue;}
	
	boolean PutThisFund=true;
	Fund = getSecurity(CandidateFund[i]);
	
	if(Fund == null) continue;
	try{Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());}
		catch(Exception e){continue;}

	if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -12))){
		AssetClass assetClass = Fund.getAssetClass();
		
		//Omit those funds that DO NOT Have ParentClassID
		if (assetClass != null) {
		
			//Omit those ETF that Average 3-Month Volume are less than 200k;Configuration.SECURITY_TYPE_ETF=1;
			if (Fund.getSecurityType() == 1){
				int AverageVolumeMonth = 3;
				Long Volume=0L;
				Date EndDate=CurrentDate;
				for ( int t = 0; t<AverageVolumeMonth;t++){
					EndDate=LTIDate.getNewNYSEMonth(EndDate,-t);
					Volume=Volume+getMonthVolume(Fund.getID(),EndDate);
				}
				Volume=Volume/AverageVolumeMonth;
				if (Volume < 200000) 
					{PutThisFund = false; printToLog("TYPE 1 Exclude "+ Fund.getID()); continue;} //TYPE 1
			}
			
			// Omit those Funds that 12-month S.D> 1.5*Benchmark  (Version 2010.02.04)
			double BenchSD; double FundSD;
			Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());
			SecurityMPT BenchMPT = securityManager.getSecurityMPT(Bench.getID(),LastYear);
			SecurityMPT FundMPT = securityManager.getSecurityMPT(Fund.getID(),LastYear);
			if (BenchMPT != null){BenchSD = BenchMPT.getStandardDeviation();}
				else {BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);}
			if (FundMPT != null) {FundSD =FundMPT.getStandardDeviation();}
				else {FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);}
			
			if (FundSD > BenchSD * 1.5) 
				{PutThisFund = false; printToLog("TYPE 2 Exclude "+ Fund.getID()); continue;} //TYPE 2
			
		} // End if (assetClass != null)
		else { PutThisFund = false; printToLog("TYPE 3 Exclude "+ Fund.getID()); continue;}  //TYPE 3
		
		if ( PutThisFund ) {CandidateFundList.add(CandidateFund[i]);}
	}
	
}
if(HaveCash == false) {CandidateFundList.add("CASH");}

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
for(int i=0; i<AvailableAssetClassList.size();i++)
{
    if(AvailableAssetClassList.get(i).equals("CASH"))
        AssetBenchmarkMap.put(AvailableAssetClassList.get(i), "CASH");    
    else
        AssetBenchmarkMap.put(AvailableAssetClassList.get(i), securityManager.get(assetClassManager.get(AvailableAssetClassList.get(i)).getBenchmarkID()).getSymbol());
}



/*Calculate weights with MVO*/

double[] Weights = getMVOWeight( AssetBenchmarkMap, AvailableAssetClassList, MVOMonths,  RAF, RiskProfile);

Asset CurrentAsset = null;
for(int i=0; i < Weights.length; i++)
{
    if(AssetAvailableTradingPercentage.get(AvailableAssetClassList.get(i)) != null)
    {
        CurrentAsset=new Asset();
        CurrentAsset = CurrentPortfolio.getAsset(AvailableAssetClassList.get(i));
        CurrentAsset .setTargetPercentage(Weights[i]);
        printToLog("Assets: "+ AvailableAssetClassList.get(i) + "  weight : " + Weights[i]);
    }
    else
    {
        CurrentAsset=new Asset();
        CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
        CurrentAsset.setName(AvailableAssetClassList.get(i));
        CurrentAsset.setClassID(getAssetClassID(AvailableAssetClassList.get(i)));
        CurrentAsset.setTargetPercentage(Weights[i]);
        CurrentPortfolio.addAsset(CurrentAsset);
        printToLog("New Assets: "+ CurrentAsset.getName() + "  weight : " + Weights[i]);
    }
}


/*calculate the asset trading percentage*/

HashMap<String,Double>  AssetTargetPercentageMap = new HashMap<String,Double>();
HashMap<String,Double>  HoldingAssetActualPercentage = new HashMap<String,Double>();
double ta = CurrentPortfolio.getTotalAmount(CurrentDate);
double aa;

for(int i=0; i < Weights.length; i++)
    AssetTargetPercentageMap.put(AvailableAssetClassList.get(i), Weights[i] );

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
    if(ActualPercentage <= TargetPercentage + 0.00001)
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

        Iterator iter = AssetActualTradingPercentage.keySet().iterator();
        while (iter.hasNext())
        {      
            String key = (String)iter.next(); 
            printToLog("AssetActualTradingPercentage contains : " + key);
        }

for(int i = 0; i < AvailableAssetClassList.size(); i++)
    printToLog("AvailableAssetClassList"  + AvailableAssetClassList.get(i) );

for(int i = 0; i < AvailableAssetClassList.size(); i++)
    printToLog( AssetActualTradingPercentage.get(AvailableAssetClassList.get(i)) );


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
 else if(SecurityActualPercentage - SellingPercentage + TotalBuyingPercentage > MaxOfSecurityPercentage+ 0.001 && j < (PresentativeSecurityList.size() -1))
             {
                 printToLog("condition 2"); 
                 SecurityActualTradingPercentage.put(SecurityName, new Double(MaxOfSecurityPercentage - SecurityActualPercentage));
                 TotalBuyingPercentage -= (MaxOfSecurityPercentage - SecurityActualPercentage + SellingPercentage);
             }
             else
             {
                 printToLog("condition 3"); 
                 SecurityActualTradingPercentage.put(SecurityName, new Double(TotalBuyingPercentage - SellingPercentage));
                 TotalBuyingPercentage = 0;
             }
             printToLog("SecurityActualTradingPercentage = " + SecurityActualTradingPercentage.get(SecurityName));
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
        iter = SecurityActualTradingPercentage.keySet().iterator();
        while (iter.hasNext())
        {      
        String TradingSecurity = (String)iter.next(); 
        double TradePercentage = SecurityActualTradingPercentage.get(TradingSecurity).doubleValue(); 
        if(TradePercentage < 0)
            CurrentPortfolio.sellAtNextOpen(AssetName, TradingSecurity, TotalAmount * (- TradePercentage), CurrentDate);
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
        iter = SecurityActualTradingPercentage.entrySet().iterator();
        while (iter.hasNext())
        {
        Map.Entry entry = (Map.Entry) iter.next();         
        String TradingSecurity = (String)entry.getKey(); 
        Double TradePercentage = (Double)entry.getValue(); 
        if(TradePercentage.doubleValue() > 0)
            CurrentPortfolio.buyAtNextOpen(AssetName, TradingSecurity, TotalAmount * TradePercentage.doubleValue(), CurrentDate,true);
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
for(int i =0; i< Weights.length;i++)
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