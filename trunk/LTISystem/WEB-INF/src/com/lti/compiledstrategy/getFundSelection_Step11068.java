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
public class getFundSelection_Step11068 extends SimulateStrategy{
	public getFundSelection_Step11068(){
		super();
		StrategyID=1068L;
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
	private int SharpeMonths;
	public void setSharpeMonths(int SharpeMonths){
		this.SharpeMonths=SharpeMonths;
	}
	
	public int getSharpeMonths(){
		return this.SharpeMonths;
	}
	private double CashScore;
	public void setCashScore(double CashScore){
		this.CashScore=CashScore;
	}
	
	public double getCashScore(){
		return this.CashScore;
	}
	private String Frequency;
	public void setFrequency(String Frequency){
		this.Frequency=Frequency;
	}
	
	public String getFrequency(){
		return this.Frequency;
	}
	private String[] MainAssetClass;
	public void setMainAssetClass(String[] MainAssetClass){
		this.MainAssetClass=MainAssetClass;
	}
	
	public String[] getMainAssetClass(){
		return this.MainAssetClass;
	}
	private int MaxFavorFundsNumber;
	public void setMaxFavorFundsNumber(int MaxFavorFundsNumber){
		this.MaxFavorFundsNumber=MaxFavorFundsNumber;
	}
	
	public int getMaxFavorFundsNumber(){
		return this.MaxFavorFundsNumber;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		CandidateFund=(String[])ParameterUtil.fetchParameter("String[]","CandidateFund", "CHTVX,FOCPX, FUSVX, MSIVX, WFSVX, ^DWC, FSEMX, FIGRX, HAINX,FBNDX, VBMFX,CASH", parameters);
		AssetClass=(String[])ParameterUtil.fetchParameter("String[]","AssetClass", "US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, FIXED INCOME, FIXED INCOME, CASH,CASH,CASH", parameters);
		SharpeMonths=(Integer)ParameterUtil.fetchParameter("int","SharpeMonths", "12", parameters);
		CashScore=(Double)ParameterUtil.fetchParameter("double","CashScore", "1.3", parameters);
		Frequency=(String)ParameterUtil.fetchParameter("String","Frequency", "monthly", parameters);
		MainAssetClass=(String[])ParameterUtil.fetchParameter("String[]","MainAssetClass", "US EQUITY, INTERNATIONAL EQUITY, FIXED INCOME, REAL ESTATE, COMMODITIES, Emerging Market, INTERNATIONAL BONDS, High Yield Bond,BALANCE FUND", parameters);
		MaxFavorFundsNumber=(Integer)ParameterUtil.fetchParameter("int","MaxFavorFundsNumber", "10", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	HashMap<String,List<String>> AssetFundsRankingSharpeMap = null;
HashMap<String,List<String>> AssetFundsRankingMomentumMap = null;

int LastActionMonth;  // Definition for Calculating MPT
boolean NewYear = false; // Definition for Calculating MPT
int[] InAfterDateFilter; // Definition for Calculating MPT

Object[][] RankingObj=new Object[240][3];
int iMonths=0;	// for counting of RankingObj, iMonth++ once Action Code is executed.
//???!!! CASH not adjusted to Fixed Income

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
	@Override
	public void afterExecuting(Portfolio portfolio, Date CurrentDate)   throws Exception {
		super.afterExecuting(portfolio, CurrentDate);
		Object[] RankOneTime;
		RankOneTime=new Object[]{CurrentDate, AssetFundsRankingSharpeMap, AssetFundsRankingMomentumMap};
		
		RankingObj[iMonths][0]=CurrentDate;
		RankingObj[iMonths][1]=AssetFundsRankingSharpeMap;
		RankingObj[iMonths][2]=AssetFundsRankingMomentumMap;
		
		com.lti.util.PersistentUtil.writeObject(RankingObj, CurrentPortfolio.getName());
                printToLog("Enter  afterExecuting");
	}
@Override
	public void afterUpdating(Portfolio portfolio, Date CurrentDate)   throws Exception {
		super.afterExecuting(portfolio, CurrentDate);
		Object[] RankOneTime;
		RankOneTime=new Object[] {CurrentDate,AssetFundsRankingSharpeMap, AssetFundsRankingMomentumMap};
		
		RankingObj[iMonths][0]=CurrentDate;
		RankingObj[iMonths][1]=AssetFundsRankingSharpeMap;
		RankingObj[iMonths][2]=AssetFundsRankingMomentumMap;
		
		com.lti.util.PersistentUtil.writeObject(RankingObj, CurrentPortfolio.getName());
                printToLog("Enter  afterUpdating");
	}

//===============================================
//===============================================
//===============================================
public double[] getMomentumScore(String[] Securities,  double CashScoreWeight , Date CurrentDate) throws Exception {
double[] score=new double[Securities.length];
for(int i=0; i<score.length; i++)
{
score[i]=0;
}

double a;
double bp1=0;
double bp2=0;
double bp3=0;
double bp4=0;

String[] newArray1=getTopSecurityArray(Securities,-12, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray2=getTopSecurityArray(Securities, -6, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray3=getTopSecurityArray(Securities, -3, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray4=getTopSecurityArray(Securities, -1, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);

Security Fund = null;
Security Bench = null;
double FundSD = 0;
double BenchSD = 0;

for(int i=0; i<=Securities.length-1; i++)
{
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(Securities[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
{
a =(getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12)
+ getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6)
+ getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -3)
+ getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -1) )*100/4;

/*Testing*/printToLog("Original Score: "+Securities[i]+" : "+a);

/*Adj for Cash, * CashScoreWeight 2010.04.14*/
if(Securities[i].equals("CASH"))   a =a * CashScoreWeight;

bp1=0;
bp2=0;
bp3=0;
bp4=0;

for(int j=0; j<=newArray1.length-1; j++)
{
if(Securities[i].equals(newArray1[j]))
    { 
     if(j<=score.length/2-1)
        bp1=1;
     else
        bp1=0;
    }
}


for(int j=0; j<=newArray2.length-1; j++)
{
    if(Securities[i].equals(newArray2[j]))
   { 
     if(j<=score.length/2-1)
        bp2=1;
     else
        bp2=0;
   }
}


for(int j=0; j<=newArray3.length-1; j++)
{
    if(Securities[i].equals(newArray3[j]))
    { 
     if(j<=score.length/2-1)
        bp3=1;
     else
        bp3=0;
    }
}


for(int j=0; j<=newArray4.length-1;j++)
{
 if(Securities[i].equals(newArray4[j]))
     { 
      if(j<=score.length/2-1)
        bp4=1;
      else
         bp4=0;
     }
}
score[i]=a+bp1+bp2+bp3+bp4;

}
else
score[i]=-100000;
}
return score;
}

//===============================================
//===============================================
//===============================================
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
//===============================================
//===============================================
//===============================================
public  List<String> getSharpeRanking(String[] Funds,int SharpeMonths,Date CurrentDate,int MaxNo) throws Exception {

	String[] TopSecurityArray = new String[Funds.length];
	List<String> TopMaxNoFunds = new ArrayList<String>();
	
	TopSecurityArray = getTopSecurityArray(Funds,-SharpeMonths, CurrentDate, TimeUnit.MONTHLY, SortType.SHARPE, false);//???getTop too slow??
	
	/*TopSecurityArray-->TopMaxNoFunds*/
	for (int i=0;i<MaxNo;i++)
	{TopMaxNoFunds.add(TopSecurityArray[i]);}
	
return TopMaxNoFunds;
}

public  List<String> getMomentumRanking(String[] Funds, double CashScore, Date CurrentDate,int MaxNo) throws Exception {
	
	String[] TopSecurityArray = new String[Funds.length];
	List<String> TopMaxNoFunds = new ArrayList<String>();
	
	double[] MomentumScore = getMomentumScore(Funds, CashScore, CurrentDate);
	for(int i=0;i<Funds.length;i++)
	{TopSecurityArray[i]=Funds[i];}
	
	/*Sort MomentumScore to get Top MaxNo Funds*/
	double MaxScore;
	int MaxScoreIndex;
	for(int k=0; k < MaxNo; k++)
	{
		MaxScore = MomentumScore[k];
		MaxScoreIndex =k;
	
		for(int j=k+1; j < MomentumScore.length; j++)
		{   
			if(MaxScore <= MomentumScore[j])
			{
			MaxScore = MomentumScore[j];
			MaxScoreIndex = j;
			}
		}
   
		if(MaxScoreIndex != k)
		{
			String tmp= TopSecurityArray[MaxScoreIndex];
			TopSecurityArray[MaxScoreIndex]= TopSecurityArray[k];
			TopSecurityArray[k]=tmp; 	 
	
			double t=MomentumScore[MaxScoreIndex];
			MomentumScore[MaxScoreIndex]=MomentumScore[k];
			MomentumScore[k]=t;
		}
	}
	
	/*TopSecurityArray-->TopMaxNoFunds*/
	for (int i=0;i<MaxNo;i++)
	{TopMaxNoFunds.add(TopSecurityArray[i]);}
	
return TopMaxNoFunds;
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		//Intro to this get FundSelection_Step1
//It Should be Updated prior to getSAA/TAA etc. AssetAllocation_Portfolio(step2) and Individual_Portfolio(Step3)
//Sort Method Sharpe and Momentum Score Both used and updated periodically
//Time schedule for Fund Ranking: for example, 2003.01.01-2003.01.31, FundRanking process is done at the first trading day of Feb, 2003.02
//Update Frequency is set Monthly, Weekly or Quarterly can also be used
//Portfolio using this strategy has no visionable output, only FundRankingArray stored in**

//Sharpe and Momentum Score Parameter--SharpeMonths and CashScore ---are fixed, can be extended to alpha etc.
//If most of the candidate fund have a short history less than SharpeMonths or 1 year, what is appropriate getFundSelection??

/*testing*/
printToLog("CurrentPortfolio.getName()="+CurrentPortfolio.getName());
//=====================================
//=====================================
//=====================================
/*Delete the duplicate candidate fund*/

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
CandidateFund = new String[k];
k = 0;
for(int i = 0; i < Duplicate.length; i++)
{
    if(Duplicate[i] == 0)
    { 
        CandidateFund[k] = Temp1[i];
        k++; 
    }
}

InAfterDateFilter = new int[CandidateFund.length];// for calculation of MPT
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
		
		 
		else if (( Frequency.equalsIgnoreCase("monthly") && LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) 
|| ( Frequency.equalsIgnoreCase("quarterly") && LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate))|| ( Frequency.equalsIgnoreCase("weekly") && LTIDate.isLastNYSETradingDayOfWeek(CurrentDate))) {
		   List<String> CandidateFundList = new ArrayList<String>();
List<String> MainAssetClassList = new ArrayList<String>();
List<String> AvailableAssetClassList = new ArrayList<String>();

/*Delete the duplicate candidate fund*/

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
CandidateFund = new String[k];
k = 0;
for(int i = 0; i < Duplicate.length; i++)
{
    if(Duplicate[i] == 0)
    { 
        CandidateFund[k] = Temp1[i];
        k++; 
    }
}
 
//====================================================
//====================================================
//====================================================

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

for(int i=0; i< CandidateFund.length; i++)
{
	if(CandidateFund[i].equals("CASH"))
        {CandidateFundList.add("CASH"); continue;}
	
	boolean PutThisFund=true;
	Fund = getSecurity(CandidateFund[i]);
	
	if(Fund == null) continue;
	try{Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());}
		catch(Exception e){continue;}

	if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate.getNewNYSEMonth(CurrentDate, -SharpeMonths)) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -SharpeMonths)))
	{
		AssetClass assetClass = Fund.getAssetClass();
		
		//Omit those funds that DO NOT Have ParentClassID
		if (assetClass != null) 
		{
		         InAfterDateFilter[i] = 1;     
			// Omit those Funds that 12-month S.D> 1.5*Benchmark  (Version 2010.04.21)
		    if(NewYear || InAfterDateFilter[i] != OldIn[i]){	
			double BenchSD; double FundSD;
			Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());
			SecurityMPT BenchMPT = securityManager.getSecurityMPT(Bench.getID(),LastYear);
			SecurityMPT FundMPT = securityManager.getSecurityMPT(Fund.getID(),LastYear);
			if (BenchMPT != null){BenchSD = BenchMPT.getStandardDeviation();}
				else {BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);}
			if (FundMPT != null) {FundSD =FundMPT.getStandardDeviation();}
				else {FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);}
			
			if (FundSD > BenchSD * 1.5) 
				{PutThisFund = false; printToLog("TYPE 2 Exclude "+ Fund.getSymbol()); continue;} //TYPE 2
                      }

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
			
		} // End if (assetClass != null)
		else { PutThisFund = false; printToLog("TYPE 3 Exclude "+ Fund.getSymbol()); continue;}  //TYPE 3
		
		if ( PutThisFund ) {CandidateFundList.add(CandidateFund[i]);/*testing*/printToLog("Found : "+Fund.getSymbol()+"---"+Fund.getAssetClass().getName());}
	}	//End if getEarliestAvaliablePriceDate()
	//Special Treatment towards ShortHistoryFunds Part 2
	else if( LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), CurrentDate) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -SharpeMonths)))
	{
	if ((getSecurity(CandidateFund[i])).getAssetClass()!=null){CandidateFundList.add(CandidateFund[i]);HaveShortHistoryFunds=true;}
	}
	
}

//====================================================
//====================================================
//====================================================

/*Get AllAssetFundsMap*/
for(int i=0; i< MainAssetClass.length; i++)
{
    if (MainAssetClassList.indexOf(MainAssetClass[i])==-1) 
	{
	MainAssetClassList.add(MainAssetClass[i]);
	}
}

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);

try{AllAssetFundsMap.remove("ROOT");}        
         catch(Exception e){ }     //  templately fix the problem of "getAvailableAssetClassSet" added by WYJ  on 2010.05.08  please delete it when the problem is fixed


      //Add CASH into the "Fixed Income"  modified by WYJ on 2010.04.30 
     List<String> TempFundList = new ArrayList<String>();      
     TempFundList =  AllAssetFundsMap.get("FIXED INCOME");
     if(TempFundList != null)
     {
         TempFundList.add("CASH");
         TempFundList.add("CASH");
         AllAssetFundsMap.put("FIXED INCOME", TempFundList);
     }


printToLog("AssetFundsMap initialize ok: "+AllAssetFundsMap);

/*Get AvailableAssetClassList*/
for(int i = 0; i< MainAssetClass.length; i++)
{
    if(AllAssetFundsMap.get(MainAssetClass[i]) != null)
    {        AvailableAssetClassList.add(MainAssetClass[i]);      }
}

//====================================================
//====================================================
//====================================================
/*Calculate FundsRanking by both Sharpe and Momentum Score*/
/*1.Sharpe Rank funds for each AssetClass_Parameter SharpeMonths_Restore results in AssetFundsRankingSharpeMap*/
/*2.Momentum Score Rank funds for each AssetClass_Parameter CashScore_Restore Results in AssetFundsRankingMomentumMap*/
	
HashMap<String,List<String>>  AssetFundsRankingSharpeMap = new HashMap<String,List<String>>();
HashMap<String,List<String>>  AssetFundsRankingMomentumMap = new HashMap<String,List<String>>();

String[] Funds;
List<String> FundsNameList;
List<String> SelectedFunds;

for(int i =0; i< AvailableAssetClassList.size(); i++)
{
  if(AvailableAssetClassList.get(i).equals("CASH"))
  {
    SelectedFunds = new ArrayList<String>();
    SelectedFunds.add("CASH");
    AssetFundsRankingSharpeMap.put(AvailableAssetClassList.get(i), SelectedFunds);  
    AssetFundsRankingMomentumMap.put(AvailableAssetClassList.get(i), SelectedFunds);  
    printToLog("NOTE!!!CASH is in the AssetFundsRanking.");
  }
  else
  {
    List<String> InferiorFundsNameList=new ArrayList<String>();
    FundsNameList = new ArrayList<String>();
    FundsNameList =  AllAssetFundsMap.get(AvailableAssetClassList.get(i));
    Funds = new String[FundsNameList.size()];
    for(int j = 0; j< FundsNameList.size(); j++)
        {Funds[j] = FundsNameList.get(j);}
	
	int MaxNo = Funds.length>MaxFavorFundsNumber? MaxFavorFundsNumber :Funds.length;
	
    if (HaveShortHistoryFunds!=true)  //Special Treatment towards ShortHistoryFunds Part 3
	{
		//Sharpe Ranking=========
		SelectedFunds = getSharpeRanking(Funds,SharpeMonths,CurrentDate,MaxNo);
		AssetFundsRankingSharpeMap.put(AvailableAssetClassList.get(i),SelectedFunds);

		//Momentum Ranking=======
		SelectedFunds = getMomentumRanking(Funds,CashScore,CurrentDate,MaxNo);
		AssetFundsRankingMomentumMap.put(AvailableAssetClassList.get(i),SelectedFunds);
		
	}
	else //Special Treatment towards ShortHistoryFunds Part 3
	{
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
		
		//Sharpe Ranking
		SelectedFunds = getSharpeRanking(PseudoFunds,SharpeMonths, CurrentDate,MaxNo);
		for(int j = 0; j< MaxNo; j++)	//To obtain the true SelectedFunds List
		{
			if (FundsNameList.indexOf(SelectedFunds.get(j)) == -1)  // when SelectedFunds.get(j) is a PseudoFund, find the fund in AssetFundsRankingSharpe whose benchmark is NewArraySharpe[j]
			{
				for (int TempIndex=0;TempIndex<InferiorFundsNameList.size();TempIndex++)
				{
				String InferiorFundsName=InferiorFundsNameList.get(TempIndex);
				Long BenchmarkID=getSecurity(InferiorFundsName).getAssetClass().getBenchmarkID();
				if (getSecurity(BenchmarkID).getSymbol().equals(SelectedFunds.get(j)) && SelectedFunds.indexOf(InferiorFundsName)== -1)
					{SelectedFunds.set(j,InferiorFundsName);break;}
				}
			}
		}
		AssetFundsRankingSharpeMap.put(AvailableAssetClassList.get(i),SelectedFunds);

		//Momentum Ranking	
		SelectedFunds = getMomentumRanking(PseudoFunds, CashScore,CurrentDate,MaxNo);
		for(int j = 0; j< MaxNo; j++)	//To obtain the true SelectedFunds List
		{
			if (FundsNameList.indexOf(SelectedFunds.get(j)) == -1)  // when SelectedFunds.get(j) is a PseudoFund, find the fund in AssetFundsRankingMomentum whose benchmark is NewArrayMomentum[j]
			{
				for (int TempIndex=0;TempIndex<InferiorFundsNameList.size();TempIndex++)
				{
				String InferiorFundsName=InferiorFundsNameList.get(TempIndex);
				Long BenchmarkID=getSecurity(InferiorFundsName).getAssetClass().getBenchmarkID();
				if (getSecurity(BenchmarkID).getSymbol().equals(SelectedFunds.get(j)) && SelectedFunds.indexOf(InferiorFundsName)== -1)
					{SelectedFunds.set(j,InferiorFundsName);break;}
				}
			}
		}
		AssetFundsRankingMomentumMap.put(AvailableAssetClassList.get(i),SelectedFunds);
	}
	
  }
}


iMonths++;	//Record for the RankingObj
/*Testing*/printToLog("iMonths : "+iMonths);
/*Testing*/printToLog("AssetFundsRankingSharpeMap: "+AssetFundsRankingSharpeMap);
/*Testing*/printToLog("AssetFundsRankingMomentumMap: "+AssetFundsRankingMomentumMap);
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