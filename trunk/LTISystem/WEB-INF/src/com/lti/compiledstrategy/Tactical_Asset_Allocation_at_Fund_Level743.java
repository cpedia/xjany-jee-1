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
public class Tactical_Asset_Allocation_at_Fund_Level743 extends SimulateStrategy{
	public Tactical_Asset_Allocation_at_Fund_Level743(){
		super();
		StrategyID=743L;
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
	private int MaxOfEachGroup;
	public void setMaxOfEachGroup(int MaxOfEachGroup){
		this.MaxOfEachGroup=MaxOfEachGroup;
	}
	
	public int getMaxOfEachGroup(){
		return this.MaxOfEachGroup;
	}
	private double RiskyAllocation;
	public void setRiskyAllocation(double RiskyAllocation){
		this.RiskyAllocation=RiskyAllocation;
	}
	
	public double getRiskyAllocation(){
		return this.RiskyAllocation;
	}
	private double MoneyAllocation;
	public void setMoneyAllocation(double MoneyAllocation){
		this.MoneyAllocation=MoneyAllocation;
	}
	
	public double getMoneyAllocation(){
		return this.MoneyAllocation;
	}
	private int[] RedemptionLimit;
	public void setRedemptionLimit(int[] RedemptionLimit){
		this.RedemptionLimit=RedemptionLimit;
	}
	
	public int[] getRedemptionLimit(){
		return this.RedemptionLimit;
	}
	private double LimitDiscount;
	public void setLimitDiscount(double LimitDiscount){
		this.LimitDiscount=LimitDiscount;
	}
	
	public double getLimitDiscount(){
		return this.LimitDiscount;
	}
	private String RebalanceFrequency;
	public void setRebalanceFrequency(String RebalanceFrequency){
		this.RebalanceFrequency=RebalanceFrequency;
	}
	
	public String getRebalanceFrequency(){
		return this.RebalanceFrequency;
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
	private boolean IncludeCash;
	public void setIncludeCash(boolean IncludeCash){
		this.IncludeCash=IncludeCash;
	}
	
	public boolean getIncludeCash(){
		return this.IncludeCash;
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
	private int MaxOfStableGroup;
	public void setMaxOfStableGroup(int MaxOfStableGroup){
		this.MaxOfStableGroup=MaxOfStableGroup;
	}
	
	public int getMaxOfStableGroup(){
		return this.MaxOfStableGroup;
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
	private boolean LetRiskProfileChange;
	public void setLetRiskProfileChange(boolean LetRiskProfileChange){
		this.LetRiskProfileChange=LetRiskProfileChange;
	}
	
	public boolean getLetRiskProfileChange(){
		return this.LetRiskProfileChange;
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
		SelectedPercentage=(Integer)ParameterUtil.fetchParameter("int","SelectedPercentage", "40", parameters);
		MaxOfEachGroup=(Integer)ParameterUtil.fetchParameter("int","MaxOfEachGroup", "2", parameters);
		RiskyAllocation=(Double)ParameterUtil.fetchParameter("double","RiskyAllocation", "70", parameters);
		MoneyAllocation=(Double)ParameterUtil.fetchParameter("double","MoneyAllocation", "0", parameters);
		RedemptionLimit=(int[])ParameterUtil.fetchParameter("int[]","RedemptionLimit", "3,3,3,3,3,3,3,3,3,3,3,0,0,0", parameters);
		LimitDiscount=(Double)ParameterUtil.fetchParameter("double","LimitDiscount", "0", parameters);
		RebalanceFrequency=(String)ParameterUtil.fetchParameter("String","RebalanceFrequency", "monthly", parameters);
		UseDefaultRedemption=(Boolean)ParameterUtil.fetchParameter("boolean","UseDefaultRedemption", "false", parameters);
		DefaultRedemptionLimit=(Integer)ParameterUtil.fetchParameter("int","DefaultRedemptionLimit", "3", parameters);
		IncludeCash=(Boolean)ParameterUtil.fetchParameter("boolean","IncludeCash", "true", parameters);
		AssetClass=(String[])ParameterUtil.fetchParameter("String[]","AssetClass", "US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, FIXED INCOME, FIXED INCOME, CASH,CASH,CASH", parameters);
		CandidateFund=(String[])ParameterUtil.fetchParameter("String[]","CandidateFund", "CHTVX,FOCPX, FUSVX, MSIVX, WFSVX, ^DWC, FSEMX, FIGRX, HAINX,FBNDX, VBMFX,CASH,CASH,CASH", parameters);
		CashScoreWeight=(Double)ParameterUtil.fetchParameter("double","CashScoreWeight", "1.3", parameters);
		MaxOfStableGroup=(Integer)ParameterUtil.fetchParameter("int","MaxOfStableGroup", "1", parameters);
		UseRiskProfile=(Boolean)ParameterUtil.fetchParameter("boolean","UseRiskProfile", "false", parameters);
		RiskProfile=(Double)ParameterUtil.fetchParameter("double","RiskProfile", "20", parameters);
		RiskProfileGroup=(double[])ParameterUtil.fetchParameter("double[]","RiskProfileGroup", "12,15,16,17,24,30,38,48,51,56,78,80,90", parameters);
		YearsToRetireGroup=(double[])ParameterUtil.fetchParameter("double[]","YearsToRetireGroup", "40,35,30,25,20,15,10,5,0,-5,-10,-12,-15", parameters);
		LetRiskProfileChange=(Boolean)ParameterUtil.fetchParameter("boolean","LetRiskProfileChange", "false", parameters);
		CheckDate=(Integer)ParameterUtil.fetchParameter("int","CheckDate", "31", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double OriginalYearsToRetire = 0;
Date StartDate;
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
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("OriginalYearsToRetire: ");
		sb.append(OriginalYearsToRetire);
		sb.append("\n");
		sb.append("StartDate: ");
		sb.append(StartDate);
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
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(OriginalYearsToRetire);
		stream.writeObject(StartDate);
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
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		OriginalYearsToRetire=(Double)stream.readObject();;
		StartDate=(Date)stream.readObject();;
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
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	public double[] getMomemtumScore(String[] Securities, int[] RedemptionLimit, double LimitDiscount, double CashScoreWeight , Date CurrentDate) throws Exception {
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
   try{
        Fund = getSecurity(Securities[i]);
        Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());
        FundSD = Fund.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
        BenchSD = Bench.getStandardDeviation(-12, CurrentDate, TimeUnit.MONTHLY, false);
        if(FundSD > 1.5* BenchSD)
            {score[i]=-100000; continue;}  
    }
        catch(Exception e)  {score[i]=-100000; continue;}  
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(Securities[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
{
a =(getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12)
+ getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6)
+ getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -3)
+ getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -1) )*100/4;

a = (1 - LimitDiscount * RedemptionLimit[i]) * a;

for(int j=0; j<=newArray1.length-1; j++)
{
if(Securities[i].equals(newArray1[j]))
    { 
     if(j<=score.length/2)
        bp1=1;
     else
        bp1=0;
    }
}


for(int j=0; j<=newArray2.length-1; j++)
{
    if(Securities[i].equals(newArray2[j]))
   { 
     if(j<=score.length/2)
        bp2=1;
     else
        bp2=0;
   }
}


for(int j=0; j<=newArray3.length-1; j++)
{
    if(Securities[i].equals(newArray3[j]))
    { 
     if(j<=score.length/2)
        bp3=1;
     else
        bp3=0;
    }
}


for(int j=0; j<=newArray4.length-1;j++)
{
 if(Securities[i].equals(newArray4[j]))
     { 
      if(j<=score.length/2)
        bp4=1;
      else
         bp4=0;
     }
}
score[i]=a+bp1+bp2+bp3+bp4;
if(Securities[i].equals("CASH"))   score[i] =score[i] * CashScoreWeight;
}
else
score[i]=-100000;
}
return score;
}



public Object[] getBuyList(double[] score, String[] CandidateFunds, String[] RiskyFunds, String[] StableFunds, String[] MoneyFunds, int[] RedemptionLimit, int SelectedPercentage, int MaxOfEachGroup, int MaxOfStableGroup, Date CurrentDate )
 throws Exception {
/*Rank the momentum scores*/

int k = CandidateFunds.length;
for(int i = 0; i < CandidateFunds.length; i++)
{
    if(score[i] == -100000) k -=1;
}
int TargetSize =  (int)java.lang.Math.floor((double)k * SelectedPercentage/100);
TargetSize= TargetSize >1?TargetSize : 1;
TargetSize = TargetSize >MaxOfEachGroup?MaxOfEachGroup : TargetSize ;

double MaxScore;
int MaxScoreIndex;
for(int i=0; i<=TargetSize -1; i++)
{
 
 MaxScore = score[i];
 MaxScoreIndex =i;
  
   for(int j=i+1; j<=score.length-1; j++)
   {     
     if(MaxScore <= score[j])
     {
      MaxScore = score[j];
      MaxScoreIndex = j;
     }
   }
   
   if(MaxScoreIndex !=i)
   {
     String tmp= CandidateFunds[MaxScoreIndex];
     CandidateFunds[MaxScoreIndex]= CandidateFunds[i];
     CandidateFunds[i]=tmp;

     int r = RedemptionLimit[MaxScoreIndex];
     RedemptionLimit[MaxScoreIndex]= RedemptionLimit[i];
     RedemptionLimit[i]= r;       
   
     double t=score[MaxScoreIndex];
     score[MaxScoreIndex]=score[i];
     score[i]=t;
   }
}

String log_m="Ranked Score" + CurrentDate.toString()+"\r\n";
for(int i=0;i<TargetSize;i++)
        log_m+=CandidateFunds[i] +"  " + score[i] + "\r\n";
 printToLog(log_m);

/*form the BuyList*/

int StableIndex = CandidateFunds.length;
int CashIndex = CandidateFunds.length;
int n1 = 0;
int n2 = StableFunds.length> MaxOfStableGroup? MaxOfStableGroup: StableFunds.length ;
for(int i = 0; i < CandidateFunds.length ; i++)
{
    for(int j = 0 ; j < StableFunds.length; j++)
    {
        if(CandidateFunds[i].equals(StableFunds[j]) || CandidateFunds[i].equals("CASH"))
            {
                n1+=1;
                if(n1 == n2)  {StableIndex = i;  break; }
            }
    }
    if(StableIndex == i)  break;
}
if(IncludeCash)
{
    for(int i = CandidateFunds.length -1; i >=0  ; i--)
    {
        if(CandidateFunds[i].equals("CASH")) {CashIndex = i;  break; }
    }
}
else
{
    for(int i = 0; i < CandidateFunds.length; i++)
    {
        if(CandidateFunds[i].equals("CASH")) {CashIndex = i;  break; }
    }
}
int CompareIndex;
int BuyListSize;
if(CashIndex == 0)  
    BuyListSize = 1;
else
{
    if(IncludeCash)
        CompareIndex = StableIndex > CashIndex ? CashIndex : StableIndex ;
    else
        CompareIndex = StableIndex > (CashIndex -1) ? (CashIndex - 1) : StableIndex ;   
    BuyListSize = TargetSize > (CompareIndex +1) ? (CompareIndex +1)  : TargetSize;
}

String[] BuyList = new String[BuyListSize];
int[] Limit = new int[BuyListSize];
for(int i=0; i < BuyListSize; i++)
{
    BuyList[i] = CandidateFunds[i];
    Limit[i] = RedemptionLimit[i];
}
return new Object[] {BuyList, Limit};
}


public Object[] formAdjustList( String[] HoldList, int[] HoldListLimit, String[] BuyList, int[] BuyListLimit, int MaxOfEachGroup)
 throws Exception {

String[] SellList = new String[1];
String[] FinalBuyList = new String[1];
int[] FinalBuyListLimit = new int[1];
int[] H = new int[HoldList.length];
int[] B = new int[BuyList.length];
int h = HoldList.length;
int b = BuyList.length;
int s =0;
int f =0 ; 
int available = MaxOfEachGroup - h;
boolean ConfirmCash = false;

//compare 
for(int i =0; i< HoldList.length; i++)
{
    for(int j = 0; j<BuyList.length; j++)
    {
        if(HoldList[i].equals(BuyList[j])) 
            {
                H[i]=1; B[j]=1; h -=1; b -=1;
                if(ConfirmCash && BuyList[j].equals("CASH"))  b +=1;
                if(BuyList[j].equals("CASH"))  ConfirmCash = true;
                break;
            }   
    }
    if(H[i]==0 && HoldListLimit[i] > 0) {H[i]=1; h -=1; }
}
available = available + h;

if(h != 0)
{
//sell list
SellList = new String[h];
for(int i =0; i< HoldList.length; i++)
{
    if(H[i] ==0) {SellList[s] = HoldList[i]; s +=1;}
}

// final buy list
FinalBuyList = new String[b];
FinalBuyListLimit = new int[b];
if(b>0 && available>0)
{
for(int i =0; i< BuyList.length; i++)
{
    if(B[i] == 0 && available >0) {FinalBuyList[f] = BuyList[i]; FinalBuyListLimit[f] = BuyListLimit[i] ; f+=1; available -=1; }
}
}
}
int[] S = new int[1];  S[0] = s;
int[] F = new int[1];   F[0] = f;

    return new Object[] { S, F, SellList, FinalBuyList, FinalBuyListLimit, H };
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
		StartDate = CurrentPortfolio.getStartingDate();

if(UseRiskProfile)
{
    if(LetRiskProfileChange)
    {
    for(int i = 0; i < RiskProfileGroup.length - 1; i++)
        if(RiskProfile >= RiskProfileGroup[i] && RiskProfile < RiskProfileGroup[i+1])
            OriginalYearsToRetire = YearsToRetireGroup[i];    
    }
    RiskyAllocation = 100 - RiskProfile;
    MoneyAllocation = 0;
}

if( (RiskyAllocation + MoneyAllocation ) == 100)
    MaxOfEachGroup ++;

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
    AssetClass[i] = getSecurity(CandidateFund[i]).getAssetClass().getName();  

/*divide the candidate funds into three groups according to their asset classes*/

int[] A = new int[CandidateFund.length];
int a1 = 0; int a2 = 0; int a0 = 0; int  aa = 0;
for(int i =0; i< CandidateFund.length; i++)
{
    if(isUpperOrSameClass("EQUITY", AssetClass[i]) || isUpperOrSameClass("HIGH YIELD BOND", AssetClass[i])) 
        {A[i] = 1; a1+=1;}
    if(isUpperOrSameClass("FIXED INCOME", AssetClass[i]) && !isUpperOrSameClass("HIGH YIELD BOND", AssetClass[i]))
        {A[i] = 2; a2 +=1;} 
    if(isUpperOrSameClass("CASH", AssetClass[i])) 
        {A[i] = 0; a0 +=1;} 
}
if(a1 != 0)  {RiskyFunds = new String[a1]; RiskyFundsLimit = new int[a1]; }
if(a2 != 0)  {StableFunds = new String[a2]; StableFundsLimit = new int[a2];}
if(a0 != 0)  {MoneyFunds = new String[a0]; MoneyFundsLimit = new int[a0]; aa = a0;}
a1 = 0;   a2 = 0;   a0 = 0;
for(int i =0; i< CandidateFund.length; i++)
{
    if(A[i] ==1)  {RiskyFunds[a1] = CandidateFund[i];  RiskyFundsLimit[a1] = RedemptionLimit[i];  a1+=1;}
    if(A[i] ==2)  {StableFunds[a2] = CandidateFund[i];  StableFundsLimit[a2] = RedemptionLimit[i]; a2+=1;}
    if(A[i] ==0 && aa !=0)  {MoneyFunds[a0] = CandidateFund[i];  MoneyFundsLimit[a0] = RedemptionLimit[i]; a0+=1;}
}
if(aa ==0) 
    { MoneyFunds = new String[1];  MoneyFundsLimit = new int[1]; MoneyFunds[0] = "CASH"; MoneyFundsLimit[0] = 0;  }
if(a2 == 0)
    { StableFunds = new String[1];  StableFundsLimit = new int[1]; StableFunds[0] = "CASH"; StableFundsLimit[0] = 0;  }   

CandidateFunds = new String[a1+a2+MaxOfEachGroup+1];
RL = new int[a1+a2+MaxOfEachGroup+1];
for(int i =0; i < CandidateFunds.length;i++)
{
    if(i<a1) {CandidateFunds[i] = RiskyFunds[i]; RL[i] =  RiskyFundsLimit[i];} 
    else if(i < a1 + a2)  {CandidateFunds[i] = StableFunds[i - a1]; RL[i] =  StableFundsLimit[i-a1];} 
    else { CandidateFunds[i] = "CASH"; RL[i] = 0; }
}

for(int i =0; i < RiskyFunds.length;i++)
{ printToLog("RiskyFunds  " + RiskyFunds[i]);}

for(int i =0; i < StableFunds.length;i++)
{ printToLog("StableFunds  " + StableFunds[i]);}

for(int i =0; i < MoneyFunds.length;i++)
{ printToLog("MoneyFunds  " + MoneyFunds[i]);}

/*form the redemption limit list*/
if(UseDefaultRedemption)
{
    for(int i = 0; i < RL.length; i++)
    {
        if(CandidateFunds[i].equals("CASH"))    RL[i] =  0;
        else RL[i] = DefaultRedemptionLimit;
    }
}

for(int i=0; i < CandidateFunds.length; i++)
    printToLog(CandidateFunds[i] + "   " + RL[i]);



/*calculate the momentum scores and form the buy lists */

double[] score = new double[CandidateFunds.length];
score = getMomemtumScore(CandidateFunds, RL, LimitDiscount, CashScoreWeight, CurrentDate);

//Form HoldList1


Object[] objs1 = getBuyList(score, CandidateFunds, RiskyFunds, StableFunds, MoneyFunds, RL, SelectedPercentage, MaxOfEachGroup, MaxOfStableGroup,CurrentDate );
HoldList1 = (String[])objs1[0];
HoldListLimit1 = (int[])objs1[1];

String log_m="HoldList1" + CurrentDate.toString()+"\r\n";
for(int i=0;i<HoldList1.length;i++)
        log_m+=HoldList1[i] +"  " + score[i]  + "Redemption Limit  = " + HoldListLimit1[i] + " month(s)  \r\n";
 printToLog(log_m);

//Form HoldList2
if(100 - RiskyAllocation - MoneyAllocation >0)
{
for(int i =0; i< score.length; i++)
{
    for(int j=0; j< RiskyFunds.length; j++)
    {
        if(CandidateFunds[i].equals(RiskyFunds[j])) {score[i] = -100000; break;}
    }
}

Object[] objs2 = getBuyList(score, CandidateFunds, RiskyFunds, StableFunds, MoneyFunds, RL, SelectedPercentage, MaxOfEachGroup, MaxOfStableGroup,CurrentDate );
HoldList2 = (String[])objs2[0];
HoldListLimit2 = (int[])objs2[1];

log_m="HoldList2" + CurrentDate.toString()+"\r\n";
for(int i=0;i<HoldList2.length;i++)
        log_m+=HoldList2[i] +"  Redemption Limit  = " + HoldListLimit2[i] + " month(s)  \r\n";
 printToLog(log_m);
}

/*Buy the Funds*/

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Risky Asset");
CurrentAsset.setClassID(getAssetClassID("EQUITY"));
CurrentAsset.setTargetPercentage((double)RiskyAllocation/100);
CurrentPortfolio.addAsset(CurrentAsset);

if(100 - RiskyAllocation- MoneyAllocation >0)
{
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Stable Asset");
CurrentAsset.setClassID(getAssetClassID("FIXED INCOME"));
CurrentAsset.setTargetPercentage((double)(100 - RiskyAllocation - MoneyAllocation) /100);
CurrentPortfolio.addAsset(CurrentAsset);
}

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Money Asset");
CurrentAsset.setClassID(getAssetClassID("CASH"));
CurrentAsset.setTargetPercentage((double)MoneyAllocation /100);
CurrentPortfolio.addAsset(CurrentAsset);

for(int i=0; i<HoldList1.length; i++)
 CurrentPortfolio.buyAtNextOpen("Risky Asset", HoldList1[i], TotalAmount * RiskyAllocation/100/HoldList1.length, CurrentDate,true);

if(100 - RiskyAllocation - MoneyAllocation >0)
{
for(int i=0; i<HoldList2.length; i++)
    CurrentPortfolio.buyAtNextOpen("Stable Asset", HoldList2[i], TotalAmount * (100-RiskyAllocation-MoneyAllocation)/100/HoldList2.length, CurrentDate,true);
}

CurrentPortfolio.buyAtNextOpen("Money Asset","CASH", TotalAmount *MoneyAllocation/100, CurrentDate,true);

boolean Free1 = false;
boolean Free2 = false;


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
	
		
		if(LTIDate.isLastNYSETradingDayOfMonth(CurrentDate))
{
    printToLog("enter");
    for(int i=0; i<HoldList1.length; i++)
    {
        HoldListLimit1[i] -=1;
        if(HoldListLimit1[i] <= 0)  Free1 = true;
    }
    if(100 - RiskyAllocation- MoneyAllocation >0)
    {
    for(int i=0; i<HoldList2.length; i++)
    {
        HoldListLimit2[i] -=1;
        if(HoldListLimit2[i] <= 0)  Free2 = true;
    }
    }
}

boolean ToCheck = false;
if((RebalanceFrequency.equals("monthly")&&LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) || (RebalanceFrequency.equals("quarterly")&&LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate)) || 
(RebalanceFrequency.equals("yearly")&&LTIDate.isLastNYSETradingDayOfYear(CurrentDate)))
    {ToCheck = true; }


		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (ToCheck && (Free1 || Free2)) {
		   //try{
String[] HoldListOld1 = HoldList1;
int[] HLLOld1 = HoldListLimit1;
int[] H1 = new int[1];
int f1 =0; 
int s1 =0; 
int h1 =0;
String[] SellList1 = new String[1];
String[] FinalBuyList1= new String[1];
int[] FinalBuyListLimit1= new int[1];
boolean ConfirmCash;

String[] HoldListOld2 = HoldList2;
int[] HLLOld2 = HoldListLimit2;
int[] H2 = new int[1];
int f2 =0;
int s2 =0;
int h2 =0;
String[] SellList2= new String[1];
String[] FinalBuyList2= new String[1];
int[] FinalBuyListLimit2= new int[1];

double Cash = CurrentPortfolio.getCash();
printToLog("Cash = " + Cash);

if(UseRiskProfile)
{
    if(LetRiskProfileChange)
    {
    double YearsToRetire = OriginalYearsToRetire -  java.lang.Math.floor(LTIDate.calculateInterval(StartDate,CurrentDate) / 365);   
    for(int i = 0; i < YearsToRetireGroup.length - 1; i++)
        if(YearsToRetire <= YearsToRetireGroup[i] && YearsToRetire > YearsToRetireGroup[i+1])
            RiskProfile = RiskProfileGroup[i];
    }
    RiskyAllocation = 100 - RiskProfile;
    MoneyAllocation = 0;
}

if( (RiskyAllocation + MoneyAllocation ) == 100)
    MaxOfEachGroup ++;

/*
for(int i =0; i < HoldList1.length;i++)
{ printToLog("HoldList1  " + HoldList1[i] + " Amount = " + CurrentPortfolio.getSecurityAmount("Risky Asset", getSecurity(HoldList1[i]).getName(), CurrentDate));}
if(100 - RiskyAllocation - MoneyAllocation >0)
{
for(int i =0; i < HoldList2.length;i++)
{ printToLog("HoldList2  " + HoldList2[i] + " Amount = " + CurrentPortfolio.getSecurityAmount("Stable Asset", getSecurity(HoldList2[i]).getName(), CurrentDate));}
}
*/

double[] score = new double[CandidateFunds.length];
score = getMomemtumScore(CandidateFunds, RL, LimitDiscount, CashScoreWeight, CurrentDate);

if(Free1)
{
//Form FinalBuyList1 and SellList1
Object[] objs1 = getBuyList(score, CandidateFunds, RiskyFunds, StableFunds, MoneyFunds, RL, SelectedPercentage, MaxOfEachGroup,MaxOfStableGroup, CurrentDate );
String[] BuyList1 = (String[])objs1[0];
int[] BuyListLimit1 = (int[])objs1[1];

Object[] objs3 =  formAdjustList( HoldList1, HoldListLimit1, BuyList1, BuyListLimit1, MaxOfEachGroup);
int[] S1 = (int[])objs3[0];
int[] F1 = (int[])objs3[1];
s1 = S1[0];
f1 = F1[0];
if(s1 >0)
{
SellList1= new String[s1];
SellList1= (String[])objs3[2];
}
if(f1 > 0)
{
FinalBuyList1= new String[f1];
FinalBuyListLimit1= new int[f1];
FinalBuyList1= (String[])objs3[3] ;
FinalBuyListLimit1= (int[])objs3[4] ;
}
H1 = new int[HoldList1.length];
H1 = (int[])objs3[5]; 

}


if(Free2)
{
for(int i =0; i< score.length; i++)
{
    for(int j=0; j< RiskyFunds.length; j++)
    {
        if(CandidateFunds[i].equals(RiskyFunds[j])) {score[i] = -100000; break;}
    }
}

Object[] objs2 = getBuyList(score, CandidateFunds, RiskyFunds, StableFunds, MoneyFunds, RL, SelectedPercentage, MaxOfEachGroup, MaxOfStableGroup,CurrentDate );
String[] BuyList2 = (String[])objs2[0];
int[] BuyListLimit2 = (int[])objs2[1];

Object[] objs4 =  formAdjustList( HoldList2, HoldListLimit2, BuyList2, BuyListLimit2, MaxOfEachGroup);
int[] S2 = (int[])objs4[0];
int[] F2 = (int[])objs4[1];
s2 = S2[0];
f2 = F2[0];
if(s2 >0)
{
SellList2= new String[s2];
SellList2= (String[])objs4[2];
}
if(f2 > 0)
{
FinalBuyList2= new String[f2];
FinalBuyListLimit2 = new int[f2];
FinalBuyList2 = (String[])objs4[3] ;
FinalBuyListLimit2 = (int[])objs4[4] ;
}
H2 = new int[HoldList2.length];
H2 = (int[])objs4[5]; 

}

/*Transaction*/
//sell
double SecurityAmount;
if(s1>0)
{
    ConfirmCash = false;
    for(int i=0; i<s1;i++)
    {
        if( !SellList1[i].equals("CASH") || (SellList1[i].equals("CASH") && !ConfirmCash) )
        {
        SecurityAmount = CurrentPortfolio.getSecurityAmount("Risky Asset", getSecurity(SellList1[i]).getName(), CurrentDate);
        if(SecurityAmount != 0)
        {
            printToLog("Risky Asset :  sell " + SellList1[i] +  "  Amount  " + SecurityAmount);
            CurrentPortfolio.sellAtNextOpen("Risky Asset", SellList1[i], SecurityAmount,CurrentDate);  
            Cash += SecurityAmount;
        }
        }
        if( SellList1[i].equals("CASH"))    ConfirmCash = true;
    }
}
if(s2>0)
{
    ConfirmCash = false;
    for(int i=0; i<s2;i++)
    {
        if( !SellList2[i].equals("CASH") || (SellList2[i].equals("CASH") && !ConfirmCash) )
        {
        SecurityAmount = CurrentPortfolio.getSecurityAmount("Stable Asset", getSecurity(SellList2[i]).getName(), CurrentDate);
        if(SecurityAmount != 0)
        {
        printToLog("Stable Asset:  sell " + SellList2[i] +  "  Amount  " + SecurityAmount);
        CurrentPortfolio.sellAtNextOpen("Stable Asset", SellList2[i], SecurityAmount,CurrentDate); 
        Cash += SecurityAmount;
        }
        }
        if( SellList2[i].equals("CASH"))    ConfirmCash = true;          
    }
}
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
double MoneyAmount = CurrentPortfolio.getSecurityAmount("Money Asset", "CASH" ,CurrentDate);
printToLog( "Sell 1 = " + s1 + "Sell 2= " +  s2 + " Cash Amount  = " + Cash);
if(s1>0 || s2>0)
{
     if(MoneyAmount > TotalAmount*MoneyAllocation/100) 
     {
        printToLog("Money Asset:  sell CASH " +  "  Amount  " + (MoneyAmount - TotalAmount*MoneyAllocation/100));
        CurrentPortfolio.sellAtNextOpen("Money Asset", "CASH", MoneyAmount - TotalAmount*MoneyAllocation/100, CurrentDate);
        Cash += MoneyAmount - TotalAmount*MoneyAllocation/100;
    }
    if(MoneyAmount < TotalAmount*MoneyAllocation/100) 
    {
        printToLog("Money Asset:  buy CASH " +  "  Amount  " + (TotalAmount*MoneyAllocation/100 - MoneyAmount));
        CurrentPortfolio.buyAtNextOpen("Money Asset","CASH", TotalAmount*MoneyAllocation/100 - MoneyAmount, CurrentDate,true);       
        Cash -= TotalAmount*MoneyAllocation/100 - MoneyAmount;
    }
}

//Buy
if(s1>0 || s2>0)
{
    double RiskyAssetAmount = CurrentPortfolio.getAssetAmount("Risky Asset", CurrentDate);
    
    if( f1>0 )
    {
        if(RiskyAssetAmount < TotalAmount*RiskyAllocation/100)
        {
            double AvailableForRisky = (TotalAmount*RiskyAllocation/100 - RiskyAssetAmount) < Cash ? (TotalAmount*RiskyAllocation/100 - RiskyAssetAmount) : Cash ;
            for(int i = 0; i < f1; i++){
                printToLog("Risky Asset:  buy " + FinalBuyList1[i]  +  "  Amount  " + AvailableForRisky / f1);
                CurrentPortfolio.buyAtNextOpen("Risky Asset",  FinalBuyList1[i], AvailableForRisky / f1, CurrentDate,true);
            }
            Cash -= AvailableForRisky;

        }
        else  f1=0;       
    }

    if(f2>0)
    {
        if(Cash >0)
        {
            for(int i = 0; i < f2; i++){
                printToLog("Stable Asset:  buy " + FinalBuyList2[i]  +  "  Amount  " + Cash / f2);
                CurrentPortfolio.buyAtNextOpen("Stable Asset",  FinalBuyList2[i], Cash / f2, CurrentDate,true);  
            }
            Cash -= Cash / f2; 
        }
        else  f2=0;
    }
    if(Cash >0 && f2==0)
   {
             printToLog("Money Asset:  buy " + "CASH "  +  "  Amount  " + Cash);
            CurrentPortfolio.buyAtNextOpen("Money Asset", "CASH", Cash, CurrentDate,true);  
            Cash = 0;
    }
}

/*adjust the HoldList1*/
if(s1>0)
{
HoldList1 = new String[HoldListOld1.length-s1+f1];
HoldListLimit1 = new int[HoldListOld1.length-s1+f1];
h1=0;
for(int i =0; i< HoldListOld1.length; i++)
{
    if(H1[i] != 0)  {HoldList1[h1] = HoldListOld1[i]; HoldListLimit1[h1] = HLLOld1[i]; h1 +=1;  }
}
if(f1>0)
{
for(int i =0; i< f1; i++)
    {HoldList1[h1] = FinalBuyList1[i]; HoldListLimit1[h1] = FinalBuyListLimit1[i]; h1 +=1;  }
}
}

/*adjust the HoldList2*/
if(100 - RiskyAllocation - MoneyAllocation >0)
{
if(s2>0)
{
HoldList2 = new String[HoldListOld2.length-s2+f2];
HoldListLimit2 = new int[HoldListOld2.length-s2+f2];
h2=0;
for(int i =0; i< HoldListOld2.length; i++)
{
    if(H2[i] != 0)  {HoldList2[h2] = HoldListOld2[i]; HoldListLimit2[h2] = HLLOld2[i]; h2 +=1;  }
}
if(f2>0)
{
for(int i =0; i< f2; i++)
    {HoldList2[h2] = FinalBuyList2[i]; HoldListLimit2[h2] = FinalBuyListLimit2[i]; h2 +=1;  }
}
}
}




Free1 = false;
Free2 = false;

//}catch(Exception eAction){
//    printToLog("Action Error.");
//}


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