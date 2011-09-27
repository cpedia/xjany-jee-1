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
public class Strategic_Asset_Allocation_at_close741 extends SimulateStrategy{
	public Strategic_Asset_Allocation_at_close741(){
		super();
		StrategyID=741L;
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
	private double[] MainAssetClassWeight;
	public void setMainAssetClassWeight(double[] MainAssetClassWeight){
		this.MainAssetClassWeight=MainAssetClassWeight;
	}
	
	public double[] getMainAssetClassWeight(){
		return this.MainAssetClassWeight;
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
	private double BuyThreshold;
	public void setBuyThreshold(double BuyThreshold){
		this.BuyThreshold=BuyThreshold;
	}
	
	public double getBuyThreshold(){
		return this.BuyThreshold;
	}
	private double SellThreshold;
	public void setSellThreshold(double SellThreshold){
		this.SellThreshold=SellThreshold;
	}
	
	public double getSellThreshold(){
		return this.SellThreshold;
	}
	private boolean UseRiskProfile;
	public void setUseRiskProfile(boolean UseRiskProfile){
		this.UseRiskProfile=UseRiskProfile;
	}
	
	public boolean getUseRiskProfile(){
		return this.UseRiskProfile;
	}
	private double RiskyAllocation;
	public void setRiskyAllocation(double RiskyAllocation){
		this.RiskyAllocation=RiskyAllocation;
	}
	
	public double getRiskyAllocation(){
		return this.RiskyAllocation;
	}
	private double RiskProfile;
	public void setRiskProfile(double RiskProfile){
		this.RiskProfile=RiskProfile;
	}
	
	public double getRiskProfile(){
		return this.RiskProfile;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		CandidateFund=(String[])ParameterUtil.fetchParameter("String[]","CandidateFund", "CHTVX,FOCPX, FUSVX, MSIVX, WFSVX, ^DWC, FSEMX, FIGRX, HAINX,FBNDX, VBMFX,CASH", parameters);
		RedemptionLimit=(int[])ParameterUtil.fetchParameter("int[]","RedemptionLimit", "3,3,3,3,3,3,3,3,3,3,3,0", parameters);
		MainAssetClass=(String[])ParameterUtil.fetchParameter("String[]","MainAssetClass", "US EQUITY, INTERNATIONAL EQUITY, FIXED INCOME, REAL ESTATE, COMMODITIES, Emerging Market, INTERNATIONAL BONDS, High Yield Bond, CASH", parameters);
		MainAssetClassWeight=(double[])ParameterUtil.fetchParameter("double[]","MainAssetClassWeight", "0", parameters);
		MaxOfEachAsset=(Integer)ParameterUtil.fetchParameter("int","MaxOfEachAsset", "2", parameters);
		CheckFrequency=(String)ParameterUtil.fetchParameter("String","CheckFrequency", "quarterly", parameters);
		RebalanceFrequency=(String)ParameterUtil.fetchParameter("String","RebalanceFrequency", "quarterly", parameters);
		SharpeMonths=(Integer)ParameterUtil.fetchParameter("int","SharpeMonths", "12", parameters);
		BuyThreshold=(Double)ParameterUtil.fetchParameter("double","BuyThreshold", "1.0", parameters);
		SellThreshold=(Double)ParameterUtil.fetchParameter("double","SellThreshold", "1.0", parameters);
		UseRiskProfile=(Boolean)ParameterUtil.fetchParameter("boolean","UseRiskProfile", "true", parameters);
		RiskyAllocation=(Double)ParameterUtil.fetchParameter("double","RiskyAllocation", "50", parameters);
		RiskProfile=(Double)ParameterUtil.fetchParameter("double","RiskProfile", "50", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	List<String> CandidateFundList = new ArrayList<String>();
List<String> MainAssetClassList = new ArrayList<String>();
List<String> AvailableAssetClassList = new ArrayList<String>();
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		return;
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		return;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	//Functions
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
	/*Testing*/printToLog("NOTE!!!CASH is in the PresentativeAssetFundMap.");
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

/*NOTE: AssetPercentageAllocation function_ Percentage ranges from 0-100, NOT 0-1*/
public Map<String,Double> AssetPercentageAllocation(List<String> CandidateAsset, double RiskyAllocation, double[] MainAssetClassWeight) throws Exception {

double a1=0;double a2=0;double a0=0;
int[] A = new int[CandidateAsset.size()];
Double[] IdealWeight=new Double[MainAssetClass.length];/*IdealWeight stores the customer's choice of CandidateAsset Weight*/
Map<String,Double> amap=new HashMap<String,Double>();
/* From MainAssetClassWeight-->Get IdealWeight Vector*/
if ((MainAssetClassWeight.length!=MainAssetClass.length) || (MainAssetClassWeight.length==1 && MainAssetClassWeight[0]==0)){
	printToLog("The element number of Variable MainAssetClassWeight mismatch with the MainAssetClass.  Default Value(Average Allocation) is used!!");
	for (int i=0;i<IdealWeight.length;i++)
		{IdealWeight[i]=1.0;}
}
else {
	for (int i=0;i<IdealWeight.length;i++)
		{IdealWeight[i]=MainAssetClassWeight[i];}
}
/*Detect each Asset belongs to RiskyAsset or StableAsset*/
for(int i =0; i< CandidateAsset.size(); i++){
	if((isUpperOrSameClass("FIXED INCOME", CandidateAsset.get(i)) && !isUpperOrSameClass("HIGH YIELD BOND", CandidateAsset.get(i)) && !isUpperOrSameClass("HIGH YIELD BONDS", CandidateAsset.get(i)))|| isUpperOrSameClass("INTERNATIONAL BONDS",CandidateAsset.get(i))){
		A[i] = 2; 
		int Index=MainAssetClassList.indexOf(CandidateAsset.get(i));
		a2 +=IdealWeight[Index];
		}
	else if (isUpperOrSameClass("CASH", CandidateAsset.get(i)))	/*CASH also belongs to STABLE ASSETS*/{
		A[i] = 2; 
		int Index=MainAssetClassList.indexOf(CandidateAsset.get(i));
		a2 +=IdealWeight[Index];
		}
		else 	/*All the other assets belong to RISKY ASSETS*/{
			A[i] = 1; 
			int Index=MainAssetClassList.indexOf(CandidateAsset.get(i));
			a1 +=IdealWeight[Index];
			}
}

if ((a1==0)&&(RiskyAllocation!=0)) {printToLog("ERROR-AssignClassPercentage: No candidate Risky_Assets!");}

/* Assign weight */
for (int i=0; i<CandidateAsset.size(); i++){
	if (A[i]==1){
		int Index=MainAssetClassList.indexOf(CandidateAsset.get(i));
		Double Weight=RiskyAllocation*IdealWeight[Index]/a1;
		amap.put(CandidateAsset.get(i),Weight);   
	}	
	else if (A[i]==2){
		int Index=MainAssetClassList.indexOf(CandidateAsset.get(i));
		Double Weight=(100-RiskyAllocation)*IdealWeight[Index]/a2;
		amap.put(CandidateAsset.get(i),Weight);    
	}
}

return amap;
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
		/*Assign Percentage to Risky Assets and Stable Assets according to the the RiskProfile*/
if (UseRiskProfile){
RiskyAllocation=100-RiskProfile;}

/*delete the duplicate Candidate Fund*/

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

/* Choose_Available_CandidateFunds (Version 2010.02.03)*/
Security Fund = null;
Security Bench= null;

Calendar cal = Calendar.getInstance(); // for calculation of MPT
cal.setTime(CurrentDate); //for calculation of MPT
long LastYear = cal.get(Calendar.YEAR)-1; //for calculation of MPT

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
				for ( int t = 0; t<AverageVolumeMonth;t++){
					EndDate=LTIDate.getNewNYSEMonth(EndDate,-t);
					Volume=Volume+getMonthVolume(Fund.getID(),EndDate);
				}
				Volume=Volume/AverageVolumeMonth;
				if (Volume < 200000) {PutThisFund = false;/*Testing*/printToLog("TYPE 1 Delete "+ Fund.getID());}
			}
			
			// Omit those Funds that 12-month S.D> 1.5*Benchmark  (Version 2010.02.03)
			double BenchSD; double FundSD;
			Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());
			SecurityMPT BenchMPT = securityManager.getSecurityMPT(Bench.getID(),LastYear);
			SecurityMPT FundMPT = securityManager.getSecurityMPT(Fund.getID(),LastYear);
			if (BenchMPT != null){BenchSD = BenchMPT.getStandardDeviation();}
				else {BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);}
			if (FundMPT != null) {FundSD =FundMPT.getStandardDeviation();}
				else {FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);}

			//(Version 2010.02.01)
			/* Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());
			double BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
			double FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);*/
			
			if (FundSD > BenchSD * 1.5) {PutThisFund = false;/*Testing*/printToLog("TYPE 2 Delete "+ Fund.getID());}
			
		} // End if (assetClass != null)
		else { PutThisFund = false;/*Testing*/printToLog("TYPE 3 Delete "+ Fund.getID());}
		
		if ( PutThisFund ) {CandidateFundList.add(CandidateFund[i]);}
	}
	
}

printToLog("CandidateFundList initialize ok");

/*Get the available asset classes and divide the funds*/

printToLog("-------MainAssetClassList-------");
for(int i=0; i< MainAssetClass.length; i++)
{
    if (MainAssetClassList.indexOf(MainAssetClass[i])==-1) 
        {MainAssetClassList.add(MainAssetClass[i]);
        printToLog(MainAssetClass[i]);}
}
printToLog("MainAssetClassList initialize ok");

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);

/*Testing */
printToLog("All Asset Funds Map:");
Iterator iter =AllAssetFundsMap.entrySet().iterator();
while (iter.hasNext()){
    Map.Entry<String,List<String>> entry = (Map.Entry<String,List<String>>) iter.next(); 
    String AssetName = entry.getKey(); 
    List<String> SecurNameList = entry.getValue(); 
    printToLog(AssetName+" : "+SecurNameList);
}
printToLog("----------------------------");

for(int i = 0; i< MainAssetClass.length; i++)
{
    if(AllAssetFundsMap.get(MainAssetClass[i]) != null)
    {
        AvailableAssetClassList.add(MainAssetClass[i]);
    }
}

printToLog("-------AvailableAssetClassList-------");
for(int i=0;i<AvailableAssetClassList.size();i++){
printToLog("["+i+"] :  "+AvailableAssetClassList.get(i));
}

printToLog("AvailableAssetClassList initialize ok");

/*Select the presentative funds of each asset class*/

HashMap<String,List<String>>  PresentativeAssetFundMap = new HashMap<String,List<String>>();
PresentativeAssetFundMap = getPresentativeAssetFunds(AllAssetFundsMap,  AvailableAssetClassList, SharpeMonths, MaxOfEachAsset);

printToLog("PresentativeAssetFundMap initialize ok");

/*Calculate Strategic weights */
Map<String, Double> WeightMap = AssetPercentageAllocation(AvailableAssetClassList,RiskyAllocation,  MainAssetClassWeight);
/*AssetPercentageAllocation(List<String> CandidateAsset, double RiskyAllocation, double[]) MainAssetClassWeight */
printToLog("Strategic weights alloction initialize ok");

List<Double> Weights = new ArrayList<Double>();
for(int i=0; i< WeightMap.size(); i++){
    Weights.add(WeightMap.get(AvailableAssetClassList.get(i)));
}

/*Create Assets*/
printToLog("---------Assets Creation&Set TargetPercentage---------");
Asset CurrentAsset;
for(int i=0; i < Weights.size(); i++)
{
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(AvailableAssetClassList.get(i));
CurrentAsset.setClassID(getAssetClassID(AvailableAssetClassList.get(i)));
CurrentAsset.setTargetPercentage(Weights.get(i));
CurrentPortfolio.addAsset(CurrentAsset);
printToLog( CurrentAsset.getName() + "  weight : " + Weights.get(i));
}
printToLog("Assets initialize ok");

/*Buy representative funds*/

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
//CurrentPortfolio.sellAssetCollection(CurrentDate);

printToLog("-------Buy initial Assets-------");
List<String> PresentativeFunds = null;
for(int i =0; i < Weights.size(); i++)
{
    if(Weights.get(i)!=0){
        PresentativeFunds = new ArrayList<String>();
        PresentativeFunds = PresentativeAssetFundMap.get(AvailableAssetClassList.get(i));
        printToLog("buy: Asset "+ AvailableAssetClassList.get(i)+", PresentativeFund : "+ PresentativeFunds.get(0));
        for(int j=0; j< PresentativeFunds.size(); j++)
            CurrentPortfolio.buy(CurrentPortfolio.getAsset(AvailableAssetClassList.get(i)).getName(), PresentativeFunds.get(j), TotalAmount*Weights.get(i) / PresentativeFunds.size()/100, CurrentDate); 
     }
}
printToLog("-------END OF ACTION-------");
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
		   CandidateFundList = new ArrayList<String>();
MainAssetClassList = new ArrayList<String>();
AvailableAssetClassList = new ArrayList<String>();

/*Assign Percentage to Risky Assets and Stable Assets according to the the RiskProfile*/
if (UseRiskProfile){
RiskyAllocation=100-RiskProfile;}

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
 
/* Choose_Available_CandidateFunds (Version 2010.02.03)*/
Security Fund = null;
Security Bench= null;

Calendar cal = Calendar.getInstance(); // for calculation of MPT
cal.setTime(CurrentDate); //for calculation of MPT
long LastYear = cal.get(Calendar.YEAR)-1; //for calculation of MPT

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
				for ( int t = 0; t<AverageVolumeMonth;t++){
					EndDate=LTIDate.getNewNYSEMonth(EndDate,-t);
					Volume=Volume+getMonthVolume(Fund.getID(),EndDate);
				}
				Volume=Volume/AverageVolumeMonth;
				if (Volume < 200000) {PutThisFund = false;/*Testing*/printToLog("TYPE 1 Delete "+ Fund.getID());}
			}
			
			// Omit those Funds that 12-month S.D> 1.5*Benchmark  (Version 2010.02.03)
			double BenchSD; double FundSD;
			Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());
			SecurityMPT BenchMPT = securityManager.getSecurityMPT(Bench.getID(),LastYear);
			SecurityMPT FundMPT = securityManager.getSecurityMPT(Fund.getID(),LastYear);
			if (BenchMPT != null){BenchSD = BenchMPT.getStandardDeviation();}
				else {BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);}
			if (FundMPT != null) {FundSD =FundMPT.getStandardDeviation();}
				else {FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);}

			//(Version 2010.02.01)
			/* Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());
			double BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
			double FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);*/
			
			if (FundSD > BenchSD * 1.5) {PutThisFund = false;/*Testing*/printToLog("TYPE 2 Delete "+ Fund.getID());}
			
		} // End if (assetClass != null)
		else { PutThisFund = false;/*Testing*/printToLog("TYPE 3 Delete "+ Fund.getID());}
		
		if ( PutThisFund ) {CandidateFundList.add(CandidateFund[i]);}
	}
	
}

/*Get the available asset classes and divide the funds.*/

for(int i=0; i< MainAssetClass.length; i++)
{
    MainAssetClassList.add(MainAssetClass[i]);
}

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);

for(int i = 0; i< MainAssetClass.length; i++)
{
    if(AllAssetFundsMap.get(MainAssetClass[i]) != null)
    {
        AvailableAssetClassList.add(MainAssetClass[i]);
    }
}

/*Select the presentative funds of each asset class*/

HashMap<String,List<String>>  PresentativeAssetFundMap = new HashMap<String,List<String>>();
PresentativeAssetFundMap = getPresentativeAssetFunds(AllAssetFundsMap,  AvailableAssetClassList, SharpeMonths, MaxOfEachAsset);

/*Calculate strategic weights*/

Map<String, Double> WeightMap = AssetPercentageAllocation(AvailableAssetClassList,RiskyAllocation, MainAssetClassWeight);
/*AssetPercentageAllocation(List<String> CandidateAsset, double RiskyAllocation, double[]MainAssetClassWeight)*/

List<Double> Weights = new ArrayList<Double>();
for(int i=0; i< WeightMap.size(); i++)
    Weights.add(WeightMap.get(AvailableAssetClassList.get(i)));

List<Asset>  CurrentAssetList = CurrentPortfolio.getCurrentAssetList();
List<String> CurrentAssetNameList=new ArrayList<String>();
for(int i=0;i<CurrentAssetList.size();i++){
CurrentAssetNameList.add(CurrentAssetList.get(i).getName());
}

printToLog("---------Assets Creation&Set TargetPercentage---------");
Asset CurrentAsset = null;
for(int i=0; i < Weights.size(); i++)
{
    int index = CurrentAssetNameList.indexOf(AvailableAssetClassList.get(i));
    if(index>-1)
    {
		CurrentAsset=CurrentPortfolio.getAsset(AvailableAssetClassList.get(i));
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
CurrentAssetList = CurrentPortfolio.getCurrentAssetList(); /*List<Asset> CurrentAssetList has already declared*/
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
            if (SellAmount>TotalAmount*SellThreshold/100){   
            CurrentPortfolio.sell(CurrentAsset.getName(), CurrSecurList.get(j).getSymbol(), SellAmount, CurrentDate);   
            printToLog("SELL type 1:"+CurrentAsset.getName()+"  "+CurrSecurList.get(j).getSymbol()+"  "+SellAmount);   
			} 
			/*Unsolved: small piece of securities*/
			
            UnfavClassAmount+=(CurrSecurList.get(j).getShare()*CurrSecurList.get(j).getPrice()-SellAmount);   
        }   
        else {
        double TargetSecurPercentPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size();/*Allocate Averagely :Equal Weight for each funds in a certain AssetClass*/
        double CurrentSecurPercentPortLevel=CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),CurrSecurList.get(j).getSymbol(),CurrentDate)/TotalAmount*100;
		double AvailSellAmount;
/*Testing*/printToLog("TargetSecurPercentPortLevel "+TargetSecurPercentPortLevel+"; CurrentSecurPercentPortLevel "+CurrentSecurPercentPortLevel);
			/*Action for the CASH ASSET*/
			if (!CurrSecurList.get(j).getSymbol().equals("CASH")){AvailSellAmount=CurrentPortfolio.getAvailableTradingAmount(CurrSecurList.get(j).getSymbol(), RedemptionMonthMap.get(CurrSecurList.get(j).getSymbol()), CurrentDate,1);}
			else {AvailSellAmount=CurrSecurList.get(j).getShare()*CurrSecurList.get(j).getPrice();}
			
		/*Testing*/printToLog("AvailSellAmount ="+AvailSellAmount+" ; "+" SpreadAmount= "+(CurrentSecurPercentPortLevel-TargetSecurPercentPortLevel)*TotalAmount/100);
        double SellAmount=java.lang.Math.min(AvailSellAmount/TotalAmount,(CurrentSecurPercentPortLevel-TargetSecurPercentPortLevel)/100)*TotalAmount;
            if (SellAmount>TotalAmount*SellThreshold/100){   
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
	Double Amount=CurrentAsset.getTargetPercentage()/100-CurrentPortfolio.getAssetAmount(CurrentAsset,CurrentDate)/TotalAmount;
	if (Amount*100>BuyThreshold){
	ClassWeightMap.put(AssetName,Amount);
	TempSum=TempSum+Amount;
	/*Testing*/printToLog("Weight at Class Level:  "+AssetName+",   SpreadPercentage="+Amount*100);
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
	if (UnfavClassAmountMap.get(AssetName)!=null) {UnfavPercent=UnfavClassAmountMap.get(AssetName)/TotalAmount*100;}
	else {UnfavPercent=new Double(0);}
	double TotalAssetAmount=CurrentPortfolio.getAssetAmount(CurrentAsset,CurrentDate);
	if (CurrentAsset.getTargetPercentage()-UnfavPercent>BuyThreshold) {
		double TargetSecurPercentAtPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size()*100;/*Allocate Averagely :Equal Weight for each funds in a certain AssetClass*/
		double TempWeightTotal=0;
		for (int i=0;i<FavSecurNameList.size();i++){
			if (CurrentAsset.getHoldingItems().contains(FavSecurNameList.get(i)))/*for funds already exist*/{
				double SpreadPercent=TargetSecurPercentAtPortLevel-CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),FavSecurNameList.get(i),CurrentDate)/TotalAmount*100;
				if (SpreadPercent>BuyThreshold) {SecurWeight[i]=SpreadPercent;TempWeightTotal+=SecurWeight[i];}
				}
			else if (CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),FavSecurNameList.get(i),CurrentDate)/TotalAmount>BuyThreshold) /*for new funds*/
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
				if (Amount>TotalAmount*BuyThreshold/100) {
					CurrentPortfolio.buy(CurrentAsset.getName(),FavSecurNameList.get(i),Amount,CurrentDate);
					printToLog("BUY TYPE 1  "+CurrentAsset.getName()+" "+FavSecurNameList.get(i)+" "+Amount);
				
				//Unfinished
				
				} 
			}
		} //  End if (TempWeightTotal>0.01)
		
		if (TempWeightTotal<=0.01){
			int Index=FavSecurNameList.size()-1;
			for (int i=0; i<FavSecurNameList.size();i++){
				double CurrentSecurPercentPortLevel=CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),FavSecurNameList.get(i),CurrentDate)/TotalAmount*100;
				double TargetSecurPercentPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size();/*Allocate Averagely :Equal Weight for each funds in a certain AssetClass*/
				if (CurrentSecurPercentPortLevel + BuyThreshold< TargetSecurPercentPortLevel){
					Index=i;
/*Testing*/printToLog("Find Index, "+CurrentSecurPercentPortLevel+" + "+BuyThreshold+" < "+TargetSecurPercentPortLevel);
					break;
				}
			}
/*testing*/if (Index==FavSecurNameList.size()-1) printToLog("Index equals FavSecurNameList.size()-1");
			/*buy Securities*/
			SecurWeight[Index]=1.0;
			double Amount=AvailCapital*SecurWeight[Index]*ClassWeightMap.get(AssetName);
			if (Amount > TotalAmount*BuyThreshold/100){
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