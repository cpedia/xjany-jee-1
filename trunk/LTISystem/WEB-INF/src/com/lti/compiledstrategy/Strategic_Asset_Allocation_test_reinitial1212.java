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
public class Strategic_Asset_Allocation_test_reinitial1212 extends SimulateStrategy{
	public Strategic_Asset_Allocation_test_reinitial1212(){
		super();
		StrategyID=1212L;
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
		RedemptionLimit=(int[])ParameterUtil.fetchParameter("int[]","RedemptionLimit", "3,3,3,3,3,3,3,3,3,3,3,0", parameters);
		MainAssetClass=(String[])ParameterUtil.fetchParameter("String[]","MainAssetClass", "US EQUITY, INTERNATIONAL EQUITY, FIXED INCOME, REAL ESTATE, COMMODITIES, Emerging Market, INTERNATIONAL BONDS, High Yield Bond, BALANCE FUND", parameters);
		MainAssetClassWeight=(double[])ParameterUtil.fetchParameter("double[]","MainAssetClassWeight", "0", parameters);
		MaxOfEachAsset=(Integer)ParameterUtil.fetchParameter("int","MaxOfEachAsset", "1", parameters);
		RebalanceFrequency=(String)ParameterUtil.fetchParameter("String","RebalanceFrequency", "monthly", parameters);
		SharpeMonths=(Integer)ParameterUtil.fetchParameter("int","SharpeMonths", "12", parameters);
		BuyThreshold=(Double)ParameterUtil.fetchParameter("double","BuyThreshold", "3.0", parameters);
		SellThreshold=(Double)ParameterUtil.fetchParameter("double","SellThreshold", "3.0", parameters);
		UseRiskProfile=(Boolean)ParameterUtil.fetchParameter("boolean","UseRiskProfile", "true", parameters);
		RiskyAllocation=(Double)ParameterUtil.fetchParameter("double","RiskyAllocation", "50", parameters);
		RiskProfile=(Double)ParameterUtil.fetchParameter("double","RiskProfile", "50", parameters);
		CheckDate=(Integer)ParameterUtil.fetchParameter("int","CheckDate", "30", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	//List Type should not be defined here, system will recycle it when update.

int[] InAfterDateFilter;
int[] TooVolatile;
boolean NewYear = false;
int LastActionMonth;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
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
		stream.writeObject(InAfterDateFilter);
		stream.writeObject(TooVolatile);
		stream.writeObject(NewYear);
		stream.writeObject(LastActionMonth);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		InAfterDateFilter=(int[])stream.readObject();;
		TooVolatile=(int[])stream.readObject();;
		NewYear=(Boolean)stream.readObject();;
		LastActionMonth=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	//Functions
/*
public HashMap<String,List<String>> getPresentativeAssetFunds(HashMap<String,List<String>> AllAssetFundsMap,  List<String> AvailableAssetClassList, int SharpeMonths, int[] MaxOfEachAssetArray)
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
    printToLog("NOTE!!!CASH is in the PresentativeAssetFundMap.");
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
    int FundNumber = MaxOfEachAssetArray[i] > Funds.length ? Funds.length : MaxOfEachAssetArray[i] ;
    SelectedFunds = new ArrayList<String>();
    for(int j = 0; j< FundNumber; j++)
        SelectedFunds.add(newArray[j]);
    PresentativeAssetFundMap.put(AvailableAssetClassList.get(i), SelectedFunds);
  }
}
return PresentativeAssetFundMap;
}
*/

//================================================================
//Another version of getPresentativeAssetFunds( :,boolean HaveShortHistoryFunds);
//================================================================
public HashMap<String,List<String>> getPresentativeAssetFunds(HashMap<String,List<String>> AllAssetFundsMap,  List<String> AvailableAssetClassList, int SharpeMonths, int[] MaxOfEachAssetArray, boolean HaveShortHistoryFunds)
 throws Exception {
HashMap<String,List<String>>  PresentativeAssetFundMap = new HashMap<String,List<String>>();
String[] Funds;
List<String> FundsNameList;
List<String> SelectedFunds;
String[] newArray =null;

for(int i =0; i< AvailableAssetClassList.size(); i++)
{
    printToLog("debug --------   Available Asset class : " + AvailableAssetClassList.get(i));
  if(AvailableAssetClassList.get(i).equals("CASH"))
  {
    SelectedFunds = new ArrayList<String>();
    SelectedFunds.add("CASH");
    PresentativeAssetFundMap.put(AvailableAssetClassList.get(i), SelectedFunds);  
    printToLog("NOTE!!!CASH is in the PresentativeAssetFundMap.");
  }
  else
  {
    List<String> InferiorFundsNameList=new ArrayList<String>();
	FundsNameList = new ArrayList<String>();
    FundsNameList =  AllAssetFundsMap.get(AvailableAssetClassList.get(i));
    Funds = new String[FundsNameList.size()];
    printToLog("Funds size : " + FundsNameList.size());
    for(int j = 0; j< FundsNameList.size(); j++)
        Funds[j] = FundsNameList.get(j);
    if(SharpeMonths != 0)
    {
		if (HaveShortHistoryFunds!=true)  //Special Treatment towards ShortHistoryFunds Part 3
		{
         printToLog("Enter normal dealing");
        newArray = new String[Funds.length];
        newArray = getTopSecurityArray(Funds,-SharpeMonths, CurrentDate, TimeUnit.MONTHLY, SortType.SHARPE, false);
		}
		else //Special Treatment towards ShortHistoryFunds Part 3
			{
                        printToLog("Enter Short history fund dealing");
			String[] PseudoFunds = new String[Funds.length];
			for (int TempIndex=0;TempIndex<Funds.length;TempIndex++)
				{
				if (!LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(Funds[TempIndex])), LTIDate.getNewNYSEMonth(CurrentDate, -SharpeMonths)))
					{
					Long BenchmarkID =getSecurity(Funds[TempIndex]).getAssetClass().getBenchmarkID();
					PseudoFunds[TempIndex]=getSecurity(BenchmarkID).getSymbol();
					printToLog("Fund "+Funds[TempIndex]+" is replaced by PseudoFunds(its benchmark)"+PseudoFunds[TempIndex]+" while ranking");
					InferiorFundsNameList.add(Funds[TempIndex]);
					}
				else PseudoFunds[TempIndex]=Funds[TempIndex];
				}
			newArray = new String[PseudoFunds.length];
			newArray = getTopSecurityArray(PseudoFunds,-SharpeMonths, CurrentDate, TimeUnit.MONTHLY, SortType.SHARPE, false);
		}
    }
    int FundNumber = MaxOfEachAssetArray[i] > Funds.length ? Funds.length : MaxOfEachAssetArray[i] ;
    SelectedFunds = new ArrayList<String>();
    for(int j = 0; j< FundNumber; j++)	//To obtain SelectedFunds List
		{       
               if (FundsNameList.indexOf(newArray[j])> -1) // when newArray[j] is true funds in PresentativeAssetFundMap
			{SelectedFunds.add(newArray[j]);} 
                
		else // when newArray[j] is a PseudoFund, find the fund in PresentativeAssetFundMap whose benchmark is newArray[j]
			for (int TempIndex=0;TempIndex<InferiorFundsNameList.size();TempIndex++)
			{
			String InferiorFundsName=InferiorFundsNameList.get(TempIndex);
			Long BenchmarkID=getSecurity(InferiorFundsName).getAssetClass().getBenchmarkID();
			if (getSecurity(BenchmarkID).getSymbol().equals(newArray[j]) && SelectedFunds.indexOf(InferiorFundsName)== -1)
				{SelectedFunds.add(InferiorFundsName);break;}
			}
		}
    PresentativeAssetFundMap.put(AvailableAssetClassList.get(i), SelectedFunds);
  }
}
return PresentativeAssetFundMap;
}

//=================================================================
//=================================================================

/*NOTE: AssetPercentageAllocation function_ Percentage ranges from 0-100, NOT 0-1*/
public Map<String,Double> AssetPercentageAllocation(List<String> CandidateAsset, double RiskyAllocation, double[] MainAssetClassWeight, List<String> MainAssetClassList) throws Exception {

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

if ((a1==0)&&(RiskyAllocation!=0)) {
printToLog("ERROR-AssignClassPercentage: No candidate Risky_Assets!");
RiskyAllocation=0;//NOTE This may be troublesome 2010/03/23
}

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
		// Initial

                Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(CurrentDate);
                LastActionMonth = tmpCa.get(Calendar.MONTH);  

/*Assign Percentage to Risky Assets and Stable Assets according to the the RiskProfile*/
if (UseRiskProfile){
RiskyAllocation=100-RiskProfile;}

List<String> CandidateFundList = new ArrayList<String>();
List<String> MainAssetClassList = new ArrayList<String>();
List<String> AvailableAssetClassList = new ArrayList<String>();


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

/* Choose_Available_CandidateFunds (Version 2010.04.28)*/
/*ShortHistoryFunds Adjusted*/
Security Fund = null;
Security Bench= null;
	//Special Treatment towards ShortHistoryFunds Part 1
	boolean HaveShortHistoryFunds = false;
	
Calendar cal = Calendar.getInstance(); // for calculation of MPT
cal.setTime(CurrentDate); //for calculation of MPT
long LastYear = cal.get(Calendar.YEAR)-1; //for calculation of MPT

InAfterDateFilter = new int[CandidateFund.length];// for calculation of MPT ??
TooVolatile = new int[CandidateFund.length];
double tempprice1; double  tempprice2;
boolean BenchHasPrice = true;
List<Asset> HoldingAssetList =  CurrentPortfolio.getCurrentAssetList();
List<HoldingItem> HoldingSecurityList = null;
boolean isHolding = false;
for(int i=0; i< CandidateFund.length; i++)
{
        BenchHasPrice = true;
	if(CandidateFund[i].equals("CASH"))
        {CandidateFundList.add("CASH"); continue;}
	
	boolean PutThisFund=true;
	Fund = getSecurity(CandidateFund[i]);
	
	if(Fund == null) continue;
        if(Fund.getIsClosed() && LTIDate.equals(Fund.getEndDate(),CurrentDate) )
            continue;
	try{Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());}
		catch(Exception e){continue;}
    try{tempprice1 = Fund.getAdjClose(CurrentDate);  }
        catch(Exception e){continue;}          // Omit those funds that DO NOT have currentprice 
    try{tempprice2 = Bench.getAdjClose(CurrentDate);}
        catch(Exception e){BenchHasPrice = false; }  

    isHolding = false;
    for(int m = 0; m < HoldingAssetList.size(); m++)
    {
        HoldingSecurityList = new ArrayList<HoldingItem>();
        HoldingSecurityList = HoldingAssetList.get(m).getHoldingItems();
        for(int n = 0; n < HoldingSecurityList.size(); n++)
        {
            if(CandidateFund[i].equals(HoldingSecurityList.get(n).getSymbol()))
                {isHolding = true; break;}
        }
        if(isHolding)  break;
    } 
    
    if(isHolding)
    {
        CandidateFundList.add(CandidateFund[i]);
        PutThisFund = false; 
    }
    if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
    {
     if(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])) != null)
         {InAfterDateFilter[i] = 1; printToLog("After Date Filter : " + CandidateFund[i]);}
     else 
         PutThisFund = false;   

      // Omit those Funds that 12-month S.D> 1.5*Benchmark  (Version 2010.02.04)
      if(LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
      {
			double BenchSD; double FundSD;
			Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());
			SecurityMPT BenchMPT = securityManager.getSecurityMPT(Bench.getID(),LastYear);
			SecurityMPT FundMPT = securityManager.getSecurityMPT(Fund.getID(),LastYear);
			if (BenchMPT != null){BenchSD = BenchMPT.getStandardDeviation();}
				else {BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);}
			if (FundMPT != null) {FundSD =FundMPT.getStandardDeviation();}
				else {FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);}
			
			if (FundSD > BenchSD * 1.5) 
				{PutThisFund = false; TooVolatile[i] = 1; printToLog("Too Volatile ; exclude "+ CandidateFund[i]); continue;} 
     }
     else
             printToLog("Benchmark's history prices is not long enough. " + CandidateFund[i] +" is included no matter of SD");

			//Omit those ETF that Average 3-Month Volume are less than 200k;Configuration.SECURITY_TYPE_ETF=1;
			if (PutThisFund && Fund.getSecurityType() == 1)
			{
				int AverageVolumeMonth = 3;
				Long Volume=0L;
				Date EndDate=CurrentDate;
				for ( int t = 0; t<AverageVolumeMonth;t++){
					EndDate=LTIDate.getNewNYSEMonth(EndDate,-t);
					Volume=Volume+getMonthVolume(Fund.getID(),EndDate);
				}
				Volume=Volume/AverageVolumeMonth;
				if (Volume < 200000) 
					{PutThisFund = false; printToLog("TYPE 1 Exclude "+ Fund.getSymbol()); continue;} //TYPE 1
			}
			
		
		if ( PutThisFund ) {CandidateFundList.add(CandidateFund[i]);/*testing*/printToLog(Fund.getSymbol()+"---"+Fund.getAssetClass().getName());}
	}	//End if getEarliestAvaliablePriceDate()
	//Special Treatment towards ShortHistoryFunds Part 2
	else if( LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), CurrentDate) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -SharpeMonths)) && BenchHasPrice)
	{
	if ((getSecurity(CandidateFund[i])).getAssetClass()!=null){CandidateFundList.add(CandidateFund[i]);HaveShortHistoryFunds=true;}
	}
}

HaveShortHistoryFunds=true; // testing

if(CandidateFundList.indexOf("CASH") == -1)
    CandidateFundList.add("CASH");

printToLog("CandidateFundList initialize ok");

/*Get the available asset classes and Match funds with appropriate asset class*/

printToLog("-------MainAssetClassList-------");
for(int i=0; i< MainAssetClass.length; i++)
{
    if (MainAssetClassList.indexOf(MainAssetClass[i])==-1) 
        {MainAssetClassList.add(MainAssetClass[i]);
        printToLog(MainAssetClass[i]);}
}
printToLog("MainAssetClassList initialize ok");

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);
   
     try{AllAssetFundsMap.remove("ROOT");}        
         catch(Exception e){ }     //  templately fix the problem of "getAvailableAssetClassSet" added by WYJ  on 2010.05.08  please delete it when the problem is fixed


      //Add CASH into the "Fixed Income"  modified by WYJ on 2010.04.30 , modified on 05.12
     List<String> TempFundList = new ArrayList<String>();      
     if(AllAssetFundsMap.containsKey("FIXED INCOME"))
          TempFundList =  AllAssetFundsMap.get("FIXED INCOME");
      TempFundList.add("CASH");
      TempFundList.add("CASH");
      AllAssetFundsMap.put("FIXED INCOME", TempFundList);

printToLog("All Asset Funds Map:");
Iterator iter =AllAssetFundsMap.entrySet().iterator();
while (iter.hasNext()){
    Map.Entry<String,List<String>> entry = (Map.Entry<String,List<String>>) iter.next(); 
    String AssetName = entry.getKey(); 
    List<String> SecurNameList = entry.getValue(); 
    printToLog(AssetName+" : "+SecurNameList);
}

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

	/*Select the presentative funds of each asset class*/

	/*Determine Target FundsNumber of each asset class--->MaxOfEachAssetArray 2010/04/06*/
	int[] MaxOfEachAssetArray = new int[AvailableAssetClassList.size()];
	
	if (MaxOfEachAssetArray.length >2){
		for (int TempIndex=0;TempIndex < MaxOfEachAssetArray.length;TempIndex++)
		{
		if (!AvailableAssetClassList.get(TempIndex).equals("US EQUITY")) {MaxOfEachAssetArray[TempIndex] = MaxOfEachAsset;}
		else {MaxOfEachAssetArray[TempIndex] = MaxOfEachAsset+1;/*Temptesting*/printToLog("US EQUITY's TargetFundNumber = MaxOfEachAsset + 1");}
		}
	}
	else if (MaxOfEachAssetArray.length ==2){
		/*TempTesting*/printToLog("MaxOfEachAssetArray.length=2");
		MaxOfEachAssetArray[0]=2;
		MaxOfEachAssetArray[1]=2;
		}
		else {MaxOfEachAssetArray[0]=3;/*TempTesting*/printToLog("MaxOfEachAssetArray.length=1");}

HashMap<String,List<String>>  PresentativeAssetFundMap = new HashMap<String,List<String>>();
PresentativeAssetFundMap = getPresentativeAssetFunds(AllAssetFundsMap,  AvailableAssetClassList, SharpeMonths, MaxOfEachAssetArray, HaveShortHistoryFunds);

printToLog("PresentativeAssetFundMap initialize ok!  "+PresentativeAssetFundMap);

/*Calculate Strategic Weights */
Map<String, Double> WeightMapInPercent = AssetPercentageAllocation(AvailableAssetClassList,RiskyAllocation,  MainAssetClassWeight, MainAssetClassList);
/*AssetPercentageAllocation(List<String> CandidateAsset, double RiskyAllocation, double[] MainAssetClassWeight, List<String>  MainAssetClassList )*/
printToLog("Strategic weights alloction initialize ok");

List<Double> WeightsInPercent = new ArrayList<Double>();
for(int i=0; i< WeightMapInPercent.size(); i++){
    WeightsInPercent.add(WeightMapInPercent.get(AvailableAssetClassList.get(i)));
}

/*Create Assets*/
printToLog("---------Assets Creation&Set TargetPercentage---------");
boolean HaveCashAsset  = false; 
Asset CurrentAsset;
for(int i=0; i < WeightsInPercent.size(); i++)
{
if(AvailableAssetClassList.get(i).equals("CASH"))    HaveCashAsset = true;  
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(AvailableAssetClassList.get(i));
CurrentAsset.setClassID(getAssetClassID(AvailableAssetClassList.get(i)));
CurrentAsset.setTargetPercentage(WeightsInPercent.get(i).doubleValue()/100);
CurrentPortfolio.addAsset(CurrentAsset);
printToLog( CurrentAsset.getName() + "  weight InPercent : " + WeightsInPercent.get(i).doubleValue());
}
printToLog("Assets initialize ok");

    //Special treatment towards CASH 2010.04.28
    if (!HaveCashAsset)
       {
       CurrentAsset = new Asset();
       CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
       CurrentAsset.setName("CASH");
       CurrentAsset.setClassID(getAssetClassID("CASH"));
       CurrentAsset.setTargetPercentage(new Double(0));
       CurrentPortfolio.addAsset(CurrentAsset);
       printToLog(CurrentAsset.getName() + "weight InPercent :"+0+ " also is included~");
      }


/*Buy representative funds*/

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
//CurrentPortfolio.sellAssetCollection(CurrentDate);

printToLog("-------Buy initial Assets-------");
List<String> PresentativeFunds = null;
for(int i =0; i < WeightsInPercent.size(); i++)
{
    if(WeightsInPercent.get(i).doubleValue() !=0){
        PresentativeFunds = new ArrayList<String>();
        PresentativeFunds = PresentativeAssetFundMap.get(AvailableAssetClassList.get(i));
        for(int j=0; j< PresentativeFunds.size(); j++)
            {CurrentPortfolio.buyAtNextOpen(CurrentPortfolio.getAsset(AvailableAssetClassList.get(i)).getName(), PresentativeFunds.get(j), TotalAmount*WeightsInPercent.get(i).doubleValue() / PresentativeFunds.size()/100, CurrentDate,true); 
            printToLog("buy: Asset "+ AvailableAssetClassList.get(i)+", PresentativeFund : "+ PresentativeFunds.get(j));}
           }
}
printToLog("-------END OF INITIAL-------");
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
		
		 
		else if (( RebalanceFrequency.equalsIgnoreCase("monthly") && LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) 
|| ( RebalanceFrequency.equalsIgnoreCase("quarterly") && LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate))) {
		   //Testing2010.06.17 try{
List<String> CandidateFundList = new ArrayList<String>();
List<String> MainAssetClassList = new ArrayList<String>();
List<String> AvailableAssetClassList = new ArrayList<String>();

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
 
/* Choose_Available_CandidateFunds (Version 2010.04.28)*/
/*ShortHistoryFunds Adjusted*/
Security Fund = null;
Security Bench= null;
	//Special Treatment towards ShortHistoryFunds Part 1
	boolean HaveShortHistoryFunds = false;
	
Calendar cal = Calendar.getInstance(); // for calculation of MPT
cal.setTime(CurrentDate); //for calculation of MPT
long LastYear = cal.get(Calendar.YEAR)-1; //for calculation of MPT

     Calendar tmpCa = Calendar.getInstance();// for calculation of MPT
     tmpCa.setTime(CurrentDate);// for calculation of MPT
     if(tmpCa.get(Calendar.MONTH) < LastActionMonth) {NewYear = true;}    else {NewYear = false;} // for calculation of MPT
      LastActionMonth = tmpCa.get(Calendar.MONTH);  // for calculation of MPT

int[] OldIn = InAfterDateFilter; // for calculation of MPT
if(NewYear)
    TooVolatile = new int[CandidateFund.length];
double tempprice1; double  tempprice2;
boolean BenchHasPrice = true;
List<Asset> HoldingAssetList =  CurrentPortfolio.getCurrentAssetList();
List<HoldingItem> HoldingSecurityList = null;
boolean isHolding = false;
for(int i=0; i< CandidateFund.length; i++)
{
        BenchHasPrice = true;
	if(CandidateFund[i].equals("CASH"))
        {CandidateFundList.add("CASH"); continue;}
	
	boolean PutThisFund=true;
	Fund = getSecurity(CandidateFund[i]);
	
	if(Fund == null) continue;
        if(Fund.getIsClosed() && LTIDate.equals(Fund.getEndDate(),CurrentDate) )
            continue;
	try{Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());}
		catch(Exception e){continue;}
       try{tempprice1 = Fund.getAdjClose(CurrentDate);  }
        catch(Exception e){continue;}          // Omit those funds that DO NOT have currentprice 
       try{tempprice2 = Bench.getAdjClose(CurrentDate);}
        catch(Exception e){BenchHasPrice = false; }  

    isHolding = false;
    for(int m = 0; m < HoldingAssetList.size(); m++)
    {
        HoldingSecurityList = new ArrayList<HoldingItem>();
        HoldingSecurityList = HoldingAssetList.get(m).getHoldingItems();
        for(int n = 0; n < HoldingSecurityList.size(); n++)
        {
            if(CandidateFund[i].equals(HoldingSecurityList.get(n).getSymbol()))
                {isHolding = true; break;}
        }
        if(isHolding)  break;
    } 
    
    if(isHolding)
    {
        CandidateFundList.add(CandidateFund[i]);
        PutThisFund = false; 
    }
    if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
    {
     if(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])) != null)
         {InAfterDateFilter[i] = 1; printToLog("After Date Filter : " + CandidateFund[i]);}
     else 
         PutThisFund = false;   
  
			// Omit those Funds that 12-month S.D> 1.5*Benchmark  (Version 2010.04.21)
      if(LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
      {
                    if(!NewYear && TooVolatile[i] == 1)
                        {PutThisFund = false; printToLog("Too Volatile ; exclude "+ CandidateFund[i]); continue;} 
		    if(NewYear || InAfterDateFilter[i] != OldIn[i]){	
			double BenchSD; double FundSD;
			Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());
			SecurityMPT BenchMPT = securityManager.getSecurityMPT(Bench.getID(),LastYear);
			SecurityMPT FundMPT = securityManager.getSecurityMPT(Fund.getID(),LastYear);
			if (BenchMPT != null){BenchSD = BenchMPT.getStandardDeviation();}
				else {BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);}
			if (FundMPT != null) {FundSD =FundMPT.getStandardDeviation();}
				else {FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);}
                        if(FundSD == 0)      // When the start date of the fund is the last day of the year, it SD equals 0
			        FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
			
			if (FundSD > BenchSD * 1.5) 
				{PutThisFund = false; TooVolatile[i] = 1; printToLog("Too Volatile ; exclude "+ Fund.getSymbol()); continue;}
                      }
        }
        else
             printToLog("Benchmark's history prices is not long enough. " + CandidateFund[i] +" is included no matter of SD");

			//Omit those ETF that Average 3-Month Volume are less than 200k;Configuration.SECURITY_TYPE_ETF=1;
			if (PutThisFund && Fund.getSecurityType() == 1)
			{
				int AverageVolumeMonth = 3;
				Long Volume=0L;
				Date EndDate=CurrentDate;
				for ( int t = 0; t<AverageVolumeMonth;t++){
					EndDate=LTIDate.getNewNYSEMonth(EndDate,-t);
					Volume=Volume+getMonthVolume(Fund.getID(),EndDate);
				}
				Volume=Volume/AverageVolumeMonth;
				if (Volume < 200000) 
					{PutThisFund = false; printToLog("TYPE 1 Exclude "+ Fund.getSymbol()); continue;} //TYPE 1
			}
			
		
		if ( PutThisFund ) {CandidateFundList.add(CandidateFund[i]);/*testing*/printToLog(Fund.getSymbol()+"---"+Fund.getAssetClass().getName());}
	}	//End if getEarliestAvaliablePriceDate()
	//Special Treatment towards ShortHistoryFunds Part 2
	else if( LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), CurrentDate) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -SharpeMonths)) && BenchHasPrice)
	{
	if ((getSecurity(CandidateFund[i])).getAssetClass()!=null){CandidateFundList.add(CandidateFund[i]);HaveShortHistoryFunds=true;}
	}
	
}

HaveShortHistoryFunds=true; // testing

if(CandidateFundList.indexOf("CASH") == -1)
    CandidateFundList.add("CASH");

/*Get the available asset classes and match funds with appropriate asset class. */

for(int i=0; i< MainAssetClass.length; i++)
{
    MainAssetClassList.add(MainAssetClass[i]);
}
printToLog("MainAssetClassList initial OK");

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);
   
     try{AllAssetFundsMap.remove("ROOT");}     // templately fix the problem of "getAvailableAssetClassSet" added  by WYJ on 2010.05.08  please delete it when the problem is fixed
        catch(Exception e){ } 

      //Add CASH into the "Fixed Income"  modified by WYJ on 2010.04.30 
     List<String> TempFundList = new ArrayList<String>();      
     if(AllAssetFundsMap.containsKey("FIXED INCOME"))
          TempFundList =  AllAssetFundsMap.get("FIXED INCOME");
     TempFundList.add("CASH");
     TempFundList.add("CASH");
     AllAssetFundsMap.put("FIXED INCOME", TempFundList);


AvailableAssetClassList = new ArrayList<String>();
for(int i = 0; i< MainAssetClass.length; i++)
{
    if(AllAssetFundsMap.get(MainAssetClass[i]) != null)
    {
        AvailableAssetClassList.add(MainAssetClass[i]);
    }
}

/*Select the presentative funds of each asset class*/

	/*Select the presentative funds of each asset class*/

	/*Determine Target FundsNumber of each asset class--->MaxOfEachAssetArray 2010/04/06*/
	int[] MaxOfEachAssetArray = new int[AvailableAssetClassList.size()];
	
	if (MaxOfEachAssetArray.length >2){
		for (int TempIndex=0;TempIndex < MaxOfEachAssetArray.length;TempIndex++)
		{
		if (!AvailableAssetClassList.get(TempIndex).equals("US EQUITY")) {MaxOfEachAssetArray[TempIndex] = MaxOfEachAsset;}
		else {MaxOfEachAssetArray[TempIndex] = MaxOfEachAsset+1;/*Temptesting*/printToLog("US EQUITY's TargetFundNumber = MaxOfEachAsset + 1");}
		}
	}
	else if (MaxOfEachAssetArray.length ==2){
		/*TempTesting*/printToLog("MaxOfEachAssetArray.length=2");
		MaxOfEachAssetArray[0]=2;
		MaxOfEachAssetArray[1]=2;
		}
		else {MaxOfEachAssetArray[0]=3;/*TempTesting*/printToLog("MaxOfEachAssetArray.length=1");}

HashMap<String,List<String>>  PresentativeAssetFundMap = new HashMap<String,List<String>>();
PresentativeAssetFundMap = getPresentativeAssetFunds(AllAssetFundsMap,  AvailableAssetClassList, SharpeMonths, MaxOfEachAssetArray,HaveShortHistoryFunds);

printToLog("PresentativeAssetFundMap initialize ok!  "+PresentativeAssetFundMap);

/*Calculate Strategic Weights*/

Map<String, Double> WeightMapInPercent = AssetPercentageAllocation(AvailableAssetClassList,RiskyAllocation, MainAssetClassWeight, MainAssetClassList);
/*AssetPercentageAllocation(List<String> CandidateAsset, double RiskyAllocation, double[]MainAssetClassWeight, List<String>  MainAssetClassList)*/

List<Double> WeightsInPercent = new ArrayList<Double>();
for(int i=0; i< WeightMapInPercent.size(); i++)
    WeightsInPercent.add(WeightMapInPercent.get(AvailableAssetClassList.get(i)));

List<Asset>  CurrentAssetList = CurrentPortfolio.getCurrentAssetList();
List<String> CurrentAssetNameList=new ArrayList<String>();
for(int i=0;i<CurrentAssetList.size();i++){
CurrentAssetNameList.add(CurrentAssetList.get(i).getName());
}

printToLog("---------Assets Creation&Set TargetPercentage---------");
Asset CurrentAsset = null;
for(int i=0; i < WeightsInPercent.size(); i++)
{
    int index = CurrentAssetNameList.indexOf(AvailableAssetClassList.get(i));
    if(index>-1)
    {
	CurrentAsset=CurrentPortfolio.getAsset(AvailableAssetClassList.get(i));
        CurrentAsset .setTargetPercentage(WeightsInPercent.get(i).doubleValue()/100);
        printToLog("Assets: "+ AvailableAssetClassList.get(i) + "  weight InPercent : " + WeightsInPercent.get(i).doubleValue());
    }
    else
    {
        CurrentAsset=new Asset();
        CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
        CurrentAsset.setName(AvailableAssetClassList.get(i));
        CurrentAsset.setClassID(getAssetClassID(AvailableAssetClassList.get(i)));
        CurrentAsset.setTargetPercentage(WeightsInPercent.get(i).doubleValue()/100);
        CurrentPortfolio.addAsset(CurrentAsset);
        printToLog("New Assets: "+ CurrentAsset.getName() + "  weight InPercent : " + WeightsInPercent.get(i).doubleValue());
    }
}


//========================================================================================
//========================================================================================
//========================================================================================
//Version 2010.04.02
/*Trading and Rebalancing Under Redemption Limit*/

HashMap<String,Integer> RedemptionMonthMap = new HashMap<String,Integer>(); 

Integer K;
for(int i =0; i< CandidateFund.length;i++)
{ 
    K = new Integer(RedemptionLimit[i]);
    RedemptionMonthMap.put(CandidateFund[i], K);
}
RedemptionMonthMap.put("CASH", new Integer(0));

double TotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
printToLog("CurrentPortfolio.getTotalAmount= "+TotalAmount);

/*First, Sell  as much as possible; and Detect the unFavorable Funds in each AssetClass of CurrentPortfolio
1.the poor-performance funds (i.e funds not in the PresentativeAssetFundMap)
2.Those FavorableFunds(in the PresnetativeAssetFundMap) may >TargetPercentage */
CurrentAssetList = CurrentPortfolio.getCurrentAssetList(); /*List<Asset> CurrentAssetList has already declared*/
HashMap<String, Double>UnfavClassAmountMap=new HashMap<String, Double>();	/*UnfavClassAmountMap Stores the amount of which those funds should be sold, but under RedemptionLimit they can not*/
List <String> FavSecurNameList=new ArrayList<String>();   

printToLog("------SELL SIGNAL ------");

//Since it's Sell signal(happen at next open), not actually sell action, getCash() & getAssetAmount() & getSecurityAmount() APIs can not be used. 
//AvailCapital & AssetAmountMap & SellSecurityAmountMap Stores these data relatively.

double AvailCapital= CurrentPortfolio.getCash(); /*Available Capital that can be used to buy other funds*/
printToLog("CurrentPortfolio.getCash() ="+CurrentPortfolio.getCash());
HashMap<String, Double> AssetAmountMap = new HashMap<String, Double>();
HashMap<String, Double> SellSecurityAmountMap= new HashMap<String, Double>();

for (int i=0; i<CurrentAssetList.size();i++)
{
    CurrentAsset =CurrentAssetList.get(i);   
    List<HoldingItem> CurrSecurList=CurrentAsset.getHoldingItems();  
    if (PresentativeAssetFundMap.get(CurrentAsset.getName())!= null)       
        {FavSecurNameList=PresentativeAssetFundMap.get(CurrentAsset.getName());}   
    else printToLog("Unexpected error occured while trading");   
    double UnfavClassAmount=0;
	double AssetAmount=CurrentPortfolio.getAssetAmount(CurrentAsset, CurrentDate);
	printToLog("---"+"\r\n"+CurrentAsset.getName()+"'s asset amount  before Sell Signal : "+ AssetAmount);
	
    for(int j=0; j<CurrSecurList.size();j++)
	{   
        if (FavSecurNameList.indexOf(CurrSecurList.get(j).getSymbol())<0)      /*Sell the poor-performance funds as much as possible*/
		{    
/*modified trim part by LJF 2010.06.17*/
            double SellAmount=CurrentPortfolio.getAvailableTradingAmount(CurrSecurList.get(j).getSymbol(), RedemptionMonthMap.get(CurrSecurList.get(j).getSymbol().toString().trim()), CurrentDate,1);
			/*Testing*/printToLog("AvailSellAmount = "+SellAmount);
            if (SellAmount>TotalAmount*SellThreshold/100){
            CurrentPortfolio.sellAtNextOpen(CurrentAsset.getName(), CurrSecurList.get(j).getSymbol(), SellAmount, CurrentDate);   
            printToLog("<=>SELL SIGNAL type 1:"+CurrentAsset.getName()+"  "+CurrSecurList.get(j).getSymbol()+"  "+SellAmount);
			AvailCapital+=SellAmount;//record the SellAmount
			AssetAmount-=SellAmount;// record for AssetAmountMap
			SellSecurityAmountMap.put(CurrSecurList.get(j).getSymbol(),new Double(SellAmount));//record for SellSecurityAmountMap
			} 
						
            UnfavClassAmount+=(CurrentPortfolio.getAvailableTradingAmount(CurrSecurList.get(j).getSymbol(), 0, CurrentDate,1)-SellAmount);   
        }   
        else 
		{
        double TargetSecurPercentPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size()*100;/*Allocate Averagely :Equal Weight for each funds in a certain AssetClass*/
        double CurrentSecurPercentPortLevel=CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),CurrSecurList.get(j).getSymbol(),CurrentDate)/TotalAmount*100;
		double AvailSellAmount;
		printToLog("TargetSecurPercentPortLevel "+TargetSecurPercentPortLevel+"; CurrentSecurPercentPortLevel "+CurrentSecurPercentPortLevel);
		
			/*Get Available Amount Action for the CASH ASSET*/
/*modified trim part by LJF 2010.06.17*/
			if (!CurrSecurList.get(j).getSymbol().equals("CASH")){AvailSellAmount=CurrentPortfolio.getAvailableTradingAmount(CurrSecurList.get(j).getSymbol(), RedemptionMonthMap.get(CurrSecurList.get(j).getSymbol().toString().trim()), CurrentDate,1);}
			else {AvailSellAmount=CurrentPortfolio.getAvailableTradingAmount(CurrSecurList.get(j).getSymbol(), 0, CurrentDate,1);}
			
		printToLog("AvailSellAmount ="+AvailSellAmount+" ; "+" SpreadAmount= "+(CurrentSecurPercentPortLevel-TargetSecurPercentPortLevel)*TotalAmount/100);
        double SellAmount=java.lang.Math.min(AvailSellAmount/TotalAmount,(CurrentSecurPercentPortLevel-TargetSecurPercentPortLevel)/100)*TotalAmount;
            if (SellAmount>TotalAmount*SellThreshold/100)
			{
            CurrentPortfolio.sellAtNextOpen(CurrentAsset.getName(),CurrSecurList.get(j).getSymbol(),SellAmount, CurrentDate);   
			printToLog("<=>SELL SIGNAL type 2:"+CurrentAsset.getName()+"  "+CurrSecurList.get(j).getSymbol()+"  "+SellAmount);
			AvailCapital+=SellAmount;//record the SellAmount
			AssetAmount-=SellAmount;// record for AssetAmountMap
			SellSecurityAmountMap.put(CurrSecurList.get(j).getSymbol(),new Double(SellAmount));//record for SellSecurityAmountMap
            }  
        }   
    }
	
    UnfavClassAmountMap.put(CurrentAsset.getName(),new Double(UnfavClassAmount));
	/*testing*/printToLog(CurrentAsset.getName()+"'s asset amount after sell signal: " +AssetAmount);
	AssetAmountMap.put(CurrentAsset.getName(), new Double(AssetAmount));
}  

/*Give weight to each FavorableFunds in PresentativeAssetFundMap, according to Target Percentage in ClasssLevel*/
/*Target Percentage is in ClasssLevel, NOT the RISKY/STABLE level */

HashMap<String,Double> ClassWeightMap=new HashMap<String, Double>();
String AssetName=new String();
FavSecurNameList=new ArrayList<String>();
/* 1.Assign Weight At Class Level */
double TempSum=0;
printToLog("--AssignWeightAtClassLevel:");
Iterator iter =PresentativeAssetFundMap.entrySet().iterator();
while (iter.hasNext())
{
    Map.Entry<String,List<String>> entry = (Map.Entry<String,List<String>>) iter.next(); 
    AssetName = entry.getKey(); 
    FavSecurNameList = entry.getValue(); 
	CurrentAsset=CurrentPortfolio.getAsset(AssetName);
	Double Decimal= new Double(0);
	if (AssetAmountMap.get(CurrentAsset.getName())!= null){Decimal=CurrentAsset.getTargetPercentage()-AssetAmountMap.get(CurrentAsset.getName()).doubleValue()/TotalAmount;}
		else {Decimal = CurrentAsset.getTargetPercentage() - 0;  /*new favorable asset added*/}
	if (Decimal*100>BuyThreshold)
	{
	ClassWeightMap.put(AssetName,Decimal);
	TempSum=TempSum+Decimal;
	/*Testing*/printToLog("Weight at Class Level:  "+AssetName+",   SpreadPercentage= "+Decimal*100);
	}
	else {ClassWeightMap.put(AssetName,new Double(0));printToLog("Weight at Class Level: "+AssetName+", SpreadPercentage=" +" 0");}
}

iter =PresentativeAssetFundMap.entrySet().iterator();
while (iter.hasNext()){
    Map.Entry<String,List<String>> entry = (Map.Entry<String,List<String>>) iter.next(); 
    AssetName = entry.getKey(); 
    FavSecurNameList = entry.getValue(); 
	CurrentAsset=CurrentPortfolio.getAsset(AssetName);
	ClassWeightMap.put(AssetName,ClassWeightMap.get(AssetName)/TempSum);
}

/* 2.Assign Weight At Security Level and Buy Action at the same time */
  
printToLog("--"+"\r\n"+"AvailCapital = "+AvailCapital);//Available Capital records Available Amount that can be used to BUY other funds
double UseCapital=0; //Initial the Use_Capital varialbe which records the Actual Amount for BUY ACTION

printToLog("--------BUY SIGNAL--------");

iter = PresentativeAssetFundMap.entrySet().iterator(); 
while (iter.hasNext()) { 
    Map.Entry<String,List<String>> entry = (Map.Entry<String,List<String>>) iter.next(); 
    AssetName = entry.getKey(); 
    FavSecurNameList = entry.getValue(); 
	Double UnfavPercent;
	CurrentAsset=CurrentPortfolio.getAsset(AssetName);
	double[] SecurWeight=new double[FavSecurNameList.size()];
	if (UnfavClassAmountMap.get(AssetName)!=null) {UnfavPercent=UnfavClassAmountMap.get(AssetName).doubleValue()/TotalAmount*100;}
		else {UnfavPercent=new Double(0);}

	printToLog("---"+"\r\n"+CurrentAsset.getName());
	if (CurrentAsset.getTargetPercentage()*100-UnfavPercent>BuyThreshold) 
	{
		double TargetSecurPercentAtPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size()*100;/*Allocate Averagely :Equal Weight for each funds in a certain AssetClass*/
		double TempPercentTotal=0;
		for (int i=0;i<FavSecurNameList.size();i++)
		{
		
			boolean InCurrentSecurityList=false;
			for(int tt=0;tt<CurrentAsset.getHoldingItems().size();tt++)
			{
				if (CurrentAsset.getHoldingItems().get(tt).getSymbol().equals(FavSecurNameList.get(i)))
				{
				InCurrentSecurityList=true;
				/*testing*/printToLog("FavSecurNameList.get(i).getSymbol() is found in CurrentAsset.getHoldingItems()!!!");
				
				double SpreadPercent=TargetSecurPercentAtPortLevel-CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),FavSecurNameList.get(i),CurrentDate)/TotalAmount*100;
				if (SellSecurityAmountMap.get(FavSecurNameList.get(i)) !=null) //SellAtNextOpen is just sell signals, no actual action today, so getSecurityAmount is the same regardless of SellAtNextOpen
					{SpreadPercent+=SellSecurityAmountMap.get(FavSecurNameList.get(i)).doubleValue()/TotalAmount*100;}
				
				if (SpreadPercent>BuyThreshold) 
					{SecurWeight[i]=SpreadPercent; TempPercentTotal+=SecurWeight[i];}
				break;
				}//If FavSecurNameList.get(i) is in CurrentSecurityList
			}
			if (( InCurrentSecurityList != true)&&(TargetSecurPercentAtPortLevel > BuyThreshold)) /* If FavSecurNameList.get(i) is NOT in CurrentSecurityList, new favorable funds*/	
			{
				SecurWeight[i]=TargetSecurPercentAtPortLevel;
				TempPercentTotal+=SecurWeight[i];
				printToLog("New favorable Funds included: "+ FavSecurNameList.get(i));
			}
		}
		
		/*Testing*/printToLog("TempPercentTotal = "+TempPercentTotal);

		if (TempPercentTotal > BuyThreshold)
		{
			for (int i=0; i<FavSecurNameList.size();i++)
			{
			SecurWeight[i]=SecurWeight[i]/TempPercentTotal;
			}
			
			/*buy Securities*/
			for (int i=0; i<FavSecurNameList.size();i++)
			{
				printToLog("Security: "+FavSecurNameList.get(i)+" --SecurWeight[i]= "+SecurWeight[i]+" ; "+" ClassWeight: = "+ClassWeightMap.get(AssetName).doubleValue());
				double Amount=AvailCapital*SecurWeight[i]*ClassWeightMap.get(AssetName).doubleValue();
				if (Amount>TotalAmount*BuyThreshold/100) 
				{
					CurrentPortfolio.buyAtNextOpen(CurrentAsset.getName(),FavSecurNameList.get(i),Amount,CurrentDate,true);
					printToLog("<=>BUY SIGNAL type 1  "+CurrentAsset.getName()+" "+FavSecurNameList.get(i)+" "+Amount);
					UseCapital+=Amount;
				} 
			}
		} //  End if (TempPercentTotal>BuyThreshold)
		
		if (TempPercentTotal <BuyThreshold) //seems useless???BUY SIGNAL type 2 Never occurs.
		{
			int Index=FavSecurNameList.size()-1;
			for (int i=0; i<FavSecurNameList.size();i++)
			{
				double CurrentSecurPercentPortLevel=CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),FavSecurNameList.get(i),CurrentDate)/TotalAmount*100;
				if (SellSecurityAmountMap.get(FavSecurNameList.get(i))!=null) //SellAtNextOpen is just sell signals, no actual action today, so getSecurityAmount is the same regardless of SellAtNextOpen
					{CurrentSecurPercentPortLevel-=SellSecurityAmountMap.get(FavSecurNameList.get(i)).doubleValue()/TotalAmount*100;}
				double TargetSecurPercentPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size()*100;/*Allocate Averagely :Equal Weight for each funds in a certain AssetClass*/
				if (CurrentSecurPercentPortLevel + BuyThreshold< TargetSecurPercentPortLevel)
				{
					Index=i;
					/*Testing*/printToLog("Find Index, "+CurrentSecurPercentPortLevel+" + "+BuyThreshold+" < "+TargetSecurPercentPortLevel);
					break;
				}
			} 
			
			/*testing*/if (Index==FavSecurNameList.size()-1) printToLog("Index equals FavSecurNameList.size()-1");
			
			/*buy Securities*/
			SecurWeight[Index]=1.0;
			double Amount=AvailCapital*SecurWeight[Index]*ClassWeightMap.get(AssetName).doubleValue();
			if (Amount > TotalAmount*BuyThreshold/100)
			{
			CurrentPortfolio.buyAtNextOpen(CurrentAsset.getName(),FavSecurNameList.get(Index),Amount, CurrentDate,true);
			printToLog("<=>BUY SIGNAL type 2  "+CurrentAsset.getName()+"   "+FavSecurNameList.get(Index)+" "+Amount);
			UseCapital+=Amount;
			} 
		} //  End if (TempPercentTotal<=BuyThreshold)

	}
}	// End of While

double SalvageAmount=AvailCapital-UseCapital;
/*testing*/printToLog("AvailCapital - UseCapital = "+SalvageAmount);
if (SalvageAmount > 1) 
{

	iter = PresentativeAssetFundMap.entrySet().iterator(); 
if (iter.hasNext()) {                                                                                             //Modified by WYJ on 2010.05.04 
    Map.Entry<String,List<String>> entry = (Map.Entry<String,List<String>>) iter.next(); 
    AssetName = entry.getKey(); 
    FavSecurNameList = entry.getValue(); 
	CurrentAsset=CurrentPortfolio.getAsset(AssetName);
	CurrentPortfolio.buyAtNextOpen(AssetName,getSecurity(FavSecurNameList.get(0)).getSymbol(), SalvageAmount, CurrentDate,true);
	printToLog("<=>BUY SIGNAL type 3 "+CurrentAsset.getName()+"  "+FavSecurNameList.get(0)+" "+SalvageAmount);
}
}
printToLog("--------End of Action --------");

//} catch (Exception e){printToLog("Fail to Excute action!!!");}

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