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
public class Copy_of_Tactical_Asset_Allocation_401k1036 extends SimulateStrategy{
	public Copy_of_Tactical_Asset_Allocation_401k1036(){
		super();
		StrategyID=1036L;
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
	private double SellThreshold;
	public void setSellThreshold(double SellThreshold){
		this.SellThreshold=SellThreshold;
	}
	
	public double getSellThreshold(){
		return this.SellThreshold;
	}
	private double BuyThreshold;
	public void setBuyThreshold(double BuyThreshold){
		this.BuyThreshold=BuyThreshold;
	}
	
	public double getBuyThreshold(){
		return this.BuyThreshold;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		SelectedPercentage=(Integer)ParameterUtil.fetchParameter("int","SelectedPercentage", "50", parameters);
		MaxOfRiskyAsset=(Integer)ParameterUtil.fetchParameter("int","MaxOfRiskyAsset", "2", parameters);
		RedemptionLimit=(int[])ParameterUtil.fetchParameter("int[]","RedemptionLimit", "3,3,3,3,3,3,3,3,3,3,3,0,0,0", parameters);
		RebalanceFrequency=(String)ParameterUtil.fetchParameter("String","RebalanceFrequency", "monthly", parameters);
		UseDefaultRedemption=(Boolean)ParameterUtil.fetchParameter("boolean","UseDefaultRedemption", "false", parameters);
		DefaultRedemptionLimit=(Integer)ParameterUtil.fetchParameter("int","DefaultRedemptionLimit", "3", parameters);
		AssetClass=(String[])ParameterUtil.fetchParameter("String[]","AssetClass", "US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, US EQUITY, FIXED INCOME, FIXED INCOME, CASH,CASH,CASH", parameters);
		CandidateFund=(String[])ParameterUtil.fetchParameter("String[]","CandidateFund", "CHTVX,FOCPX, FUSVX, MSIVX, WFSVX, ^DWC, FSEMX, FIGRX, HAINX,FBNDX, VBMFX,CASH,CASH,CASH", parameters);
		CashScoreWeight=(Double)ParameterUtil.fetchParameter("double","CashScoreWeight", "1.3", parameters);
		MaxOfStableAsset=(Integer)ParameterUtil.fetchParameter("int","MaxOfStableAsset", "1", parameters);
		MainAssetClass=(String[])ParameterUtil.fetchParameter("String[]","MainAssetClass", "US EQUITY, INTERNATIONAL EQUITY, FIXED INCOME, REAL ESTATE, COMMODITIES, Emerging Market, INTERNATIONAL BONDS, High Yield Bond, CASH", parameters);
		NumberOfMainRiskyClass=(Integer)ParameterUtil.fetchParameter("int","NumberOfMainRiskyClass", "2", parameters);
		NumberOfMainStableClass=(Integer)ParameterUtil.fetchParameter("int","NumberOfMainStableClass", "1", parameters);
		RiskProfile=(Double)ParameterUtil.fetchParameter("double","RiskProfile", "40", parameters);
		LetRiskProfileChange=(Boolean)ParameterUtil.fetchParameter("boolean","LetRiskProfileChange", "false", parameters);
		RiskProfileGroup=(double[])ParameterUtil.fetchParameter("double[]","RiskProfileGroup", "12,15,16,17,24,30,38,48,51,56,78,80,90", parameters);
		YearsToRetireGroup=(double[])ParameterUtil.fetchParameter("double[]","YearsToRetireGroup", "40,35,30,25,20,15,10,5,0,-5,-10,-12,-15", parameters);
		CheckDate=(Integer)ParameterUtil.fetchParameter("int","CheckDate", "31", parameters);
		MinimumBuyingPercentage=(Double)ParameterUtil.fetchParameter("double","MinimumBuyingPercentage", "0.01", parameters);
		MinimumSellingPercentage=(Double)ParameterUtil.fetchParameter("double","MinimumSellingPercentage", "0", parameters);
		SellThreshold=(Double)ParameterUtil.fetchParameter("double","SellThreshold", "1", parameters);
		BuyThreshold=(Double)ParameterUtil.fetchParameter("double","BuyThreshold", "0", parameters);
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
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
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



public List<String> getSelectedAssetList(double[] score, String[] CandidateFunds, String[] StableFunds, int SelectedPercentage, int MaxOfRiskyGroup, int MaxOfStableGroup, Date CurrentDate )
 throws Exception {
/*Rank the momentum scores*/

int k = CandidateFunds.length;
for(int i = 0; i < CandidateFunds.length; i++)
{
    if(score[i] == -100000) k -=1;
}
int TargetSize =  (int)java.lang.Math.floor((double)k * SelectedPercentage/100);
TargetSize= TargetSize >1?TargetSize : 1;
TargetSize = TargetSize >MaxOfRiskyGroup?MaxOfRiskyGroup : TargetSize ;

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
   
     double t=score[MaxScoreIndex];
     score[MaxScoreIndex]=score[i];
     score[i]=t;
   }
}
/*
String log_m="Ranked Score" + CurrentDate.toString()+"\r\n";
for(int i=0;i<TargetSize;i++)
        log_m+=CandidateFunds[i] +"  " + score[i] + "\r\n";
 printToLog(log_m);
*/
List<String> SelectedAssets = new ArrayList<String>();
int RiskyNumber = TargetSize;
int StableNumber = StableFunds.length> MaxOfStableGroup? MaxOfStableGroup: StableFunds.length ;
int n1 = 0; int n2 = 0;  int n3 = 0;
boolean Add = true;
boolean IsRisky = true;
while(Add)
{
    SelectedAssets.add(CandidateFunds[n1]);
    for(int i = 0; i< StableFunds.length; i++)
        if(CandidateFunds[n1].equals(StableFunds[i]))
            {n3++; IsRisky = false; break;}
    if(IsRisky)
            {n2++;}
    IsRisky = true;

    if(CandidateFunds[n1].equals("CASH") || n2 == RiskyNumber || n3 == StableNumber || n1 == CandidateFunds.length-1)
        Add = false;
    n1++;
}

return SelectedAssets;
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
StartDate = CurrentPortfolio.getStartingDate();

if(LetRiskProfileChange)
{
for(int i = 0; i < RiskProfileGroup.length - 1; i++)
    if(RiskProfile >= RiskProfileGroup[i] && RiskProfile < RiskProfileGroup[i+1])
        OriginalYearsToRetire = YearsToRetireGroup[i];    
}
StableAllocation = RiskProfile;
RiskyAllocation = 100 - RiskProfile;

if(RiskyAllocation == 100)
    MaxOfRiskyAsset ++;

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

/*Choose the available candidate funds  LJF changed Calculation of MPT 2010.03.16*/
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
        catch(Exception e){continue;}             //Omit those funds that DO NOT Have ParentClassID

    if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -12))){
        if (Fund.getSecurityType() == 1){     //Omit those ETF that Average 3-Month Volume are less than 200k;Configuration.SECURITY_TYPE_ETF=1;
            int AverageVolumeMonth = 3;
            Long Volume=0L;
            Date EndDate=CurrentDate;
            for (int j=0;j<AverageVolumeMonth;j++){
                Volume=Volume+getMonthVolume(Fund.getID(),EndDate);
                EndDate=LTIDate.getNewNYSEMonth(EndDate,-(j+1));
            }
            Volume=Volume/AverageVolumeMonth;
           if (Volume<200000) {PutThisFund = false;/*Testing*/printToLog("TYPE 1 ERROR ;  exclude " + CandidateFund[i]);}
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
				{PutThisFund = false; printToLog("TYPE 2 ERROR ; exclude "+ Fund.getID()); continue;} //TYPE 2

         if ( PutThisFund ) {CandidateFundList.add(CandidateFund[i]);}
    }
    if(i == CandidateFund.length -1 && HaveCash == false)
        {CandidateFundList.add("CASH"); HaveCash = true;}	
}

/*Get the available asset classes */

for(int i=0; i< MainAssetClass.length; i++)
{
    MainAssetClassList.add(MainAssetClass[i]);
}

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);

String[] AvailableAssets= new String[AllAssetFundsMap.size()];
int[] IndexOfStable = new int[AvailableAssets.length]; 
Iterator iterator = AllAssetFundsMap.keySet().iterator();
int n1 = 0; int n2 = 0;
while(iterator.hasNext()){
String key = (String)iterator.next();
if(key.equals("FIXED INCOME") || key.equals("INTERNATIONAL BONDS") || key.equals("CASH"))
    {IndexOfStable[n1] = 1; n2++;}
else  IndexOfStable[n1] = 0;
AvailableAssets[n1] = key;
n1++;
}

String[] RiskyAssets= new String[n1 - n2];
String[] StableAssets = new String[n2];
n1 = 0; n2 = 0;
for(int i = 0; i < AllAssetFundsMap.size(); i++)
{
    if(IndexOfStable[i] == 0)  {RiskyAssets[n1] = AvailableAssets[i]; n1++;}
    else  {StableAssets[n2] = AvailableAssets[i]; n2++;}
}

for(int i =0; i <RiskyAssets.length;i++)
{ printToLog("RiskyAssets  " + RiskyAssets[i]);}

for(int i =0; i < StableAssets.length;i++)
{ printToLog("StableAssets  " + StableAssets[i]);}

printToLog("AvailableAssetClassList initialize ok");


/*calculate the momentum scores of benchmarks and select asset classes */

String[] AssetBenchmarks = new String[AvailableAssets.length];
for(int i = 0; i < AvailableAssets.length; i++)
    AssetBenchmarks[i] = getSecurity(assetClassManager.get(AvailableAssets[i]).getBenchmarkID()).getSymbol();

double[] Score = new double[AvailableAssets.length];
Score = getMomentumScore(AssetBenchmarks, CashScoreWeight, CurrentDate);
for(int i = 0; i < AvailableAssets.length; i++)
    printToLog(AvailableAssets[i] + "'s score = " + Score[i]);

//Risky Group selection
List<String> RiskyGroupSelect = getSelectedAssetList(Score, AvailableAssets, StableAssets, SelectedPercentage, NumberOfMainRiskyClass, NumberOfMainStableClass, CurrentDate);

String log_m="Risky Group selection" + CurrentDate.toString()+"\r\n";
for(int i=0;i<RiskyGroupSelect.size();i++)
        log_m+=RiskyGroupSelect.get(i) + "\r\n";
printToLog(log_m);

//Stable Group selection
for(int i = 0; i < AvailableAssets.length; i++)
    if(AvailableAssets[i].equals("FIXED INCOME") || AvailableAssets[i].equals("INTERNATIONAL BONDS") || AvailableAssets[i].equals("CASH"))
       continue;
    else
        Score[i] = -10000;

for(int i = 0; i < AvailableAssets.length; i++)
    printToLog(AvailableAssets[i] + "'s score = " + Score[i]);

List<String> StableGroupSelect = getSelectedAssetList(Score, AvailableAssets, StableAssets, SelectedPercentage, NumberOfMainRiskyClass, NumberOfMainStableClass, CurrentDate);

log_m="Stable Group selection" + CurrentDate.toString()+"\r\n";
for(int i=0;i<StableGroupSelect.size();i++)
        log_m+=StableGroupSelect.get(i) + "\r\n";
printToLog(log_m);

/*combine selected assets and determine the asset percentages*/

boolean Add = true;
List<String> SelectedAssets = RiskyGroupSelect;
List<Double> SelectedAssetPercentages = new ArrayList<Double>();
for(int i=0;i<RiskyGroupSelect.size();i++)
    SelectedAssetPercentages.add(RiskyAllocation/RiskyGroupSelect.size()/100);

for(int i =0; i < StableGroupSelect.size();i++)
{
    for(int j = 0; j < RiskyGroupSelect.size(); j++)
         if(StableGroupSelect.get(i).equals(RiskyGroupSelect.get(j)))
         {
             SelectedAssetPercentages.set(j, (RiskyAllocation/RiskyGroupSelect.size() + StableAllocation/StableGroupSelect.size())/100 );
             Add = false;
             break;
         }
    if(Add)
    {
        SelectedAssets.add(StableGroupSelect.get(i));
        SelectedAssetPercentages.add(StableAllocation/StableGroupSelect.size()/100);
    }
    Add = true;
}

/*Select preferred funds for the selected assets*/

HashMap<String, List<String>> AssetRepresentFundMap = new HashMap<String, List<String>>();
HashMap<String, Double> FundPercentageMap = new HashMap<String, Double>();
List<String> FundList = null;
List<String> RepresentFundList = null;
String[] Funds = null;
int RepresentNumber;
double Weight;
for(int i = 0; i < SelectedAssets.size(); i++)
{
    FundList  = new ArrayList<String>();
    FundList  = AllAssetFundsMap.get(SelectedAssets.get(i) );
    Funds = new String[FundList.size()];
    Score = new double[FundList.size()];
    for(int j = 0; j< FundList.size(); j++)
        Funds[j] = FundList.get(j);
    if(SelectedAssets.get(i).equals("FIXED INCOME") || SelectedAssets.get(i).equals("INTERNATIONAL BONDS") || SelectedAssets.get(i).equals("CASH"))
        RepresentNumber = MaxOfStableAsset;
    else 
        RepresentNumber = MaxOfRiskyAsset;
    RepresentNumber = RepresentNumber < Funds.length?RepresentNumber : Funds.length;
    Score = getMomentumScore(Funds, CashScoreWeight, CurrentDate);
 
double MaxScore;
int MaxScoreIndex;
for(int l=0; l<=RepresentNumber -1; l++)
{
MaxScore = Score[l];
MaxScoreIndex =l;
  
   for(int j=l+1; j<=Score.length-1; j++)
   {     
     if(MaxScore <= Score[j])
     {
      MaxScore = Score[j];
      MaxScoreIndex = j;
     }
   }
   
   if(MaxScoreIndex !=l)
   {
     String tmp= Funds[MaxScoreIndex];
     Funds[MaxScoreIndex]= Funds[l];
     Funds[l]=tmp; 	 
   
     double t=Score[MaxScoreIndex];
     Score[MaxScoreIndex]=Score[l];
     Score[l]=t;
   }
}

RepresentFundList = new ArrayList<String>();
for(int j = 0; j < RepresentNumber; j++)
{
    RepresentFundList.add(Funds[j] );
    Weight = SelectedAssetPercentages.get(i)/RepresentNumber;
    FundPercentageMap.put(Funds[j], Weight);
    printToLog(SelectedAssets.get(i) + " buy target :  " + Funds[j] + "   target percentage : " + Weight);
}

AssetRepresentFundMap.put(SelectedAssets.get(i), RepresentFundList);
/*testing*/printToLog("AssetRepresentFundMap: "+AssetRepresentFundMap);
}



/*Create Assets*/

Asset CurrentAsset;
for(int i=0; i < SelectedAssets.size(); i++)
{
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(SelectedAssets.get(i));
CurrentAsset.setClassID(getAssetClassID(SelectedAssets.get(i)));
CurrentAsset.setTargetPercentage(SelectedAssetPercentages.get(i).doubleValue());
CurrentPortfolio.addAsset(CurrentAsset);
}

/*Buy representative funds*/

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);

for(int i =0; i < SelectedAssets.size(); i++)
{
    RepresentFundList = new ArrayList<String>();
    RepresentFundList = AssetRepresentFundMap.get(SelectedAssets.get(i));
    for(int j=0; j< RepresentFundList.size(); j++)
       CurrentPortfolio.buyAtNextOpen(SelectedAssets.get(i), RepresentFundList.get(j), TotalAmount*FundPercentageMap.get(RepresentFundList.get(j)).doubleValue(), CurrentDate,true); 
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

if((RebalanceFrequency.equals("monthly")&&LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) || (RebalanceFrequency.equals("quarterly")&&LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate)) || 
(RebalanceFrequency.equals("yearly")&&LTIDate.isLastNYSETradingDayOfYear(CurrentDate)))
{
/*form redemption limit map*/
Integer K ;
for(int i =0; i< CandidateFund.length;i++)
{ 
    K= new Integer(RedemptionLimit[i]);
    RedemptionLimitMap.put(CandidateFund[i], K);
}
RedemptionLimitMap.put("CASH", new Integer(0));

/*Form Holding Map , calculate available tading amount of each security and  each asset*/
List<HoldingItem> HoldingSecurityList = null;
double ta = CurrentPortfolio.getTotalAmount(CurrentDate);
String sn;
double sa;
double aa;

printToLog("---------------------Available percentage at the beginning-------------------------");
for(int i = 0; i < HoldingAssetList.size(); i++)
{
    HoldingSecurityList = new ArrayList<HoldingItem>();
    HoldingSecurityList = HoldingAssetList.get(i).getHoldingItems();
    HoldingAssetSecurityMap.put(HoldingAssetList.get(i).getName(),HoldingSecurityList);
    aa = 0;
    for(int j = 0; j < HoldingSecurityList.size(); j++)
    {
        sn = HoldingSecurityList.get(j).getSymbol();
        printToLog("Symbol: "+ sn + "   RedemptionLimit = " + RedemptionLimitMap.get(sn).intValue());
        sa = CurrentPortfolio.getAvailableTradingAmount(sn, RedemptionLimitMap.get(sn).intValue(), CurrentDate, 1);
        printToLog("Available Trading Amount :  sa =" + sa);
       SecurityAvailableTradingPercentage.put(sn, new Double(sa/ta));
        aa += sa;
    }
    if(aa/ta > 0.02) ToCheck = true;
    printToLog("Asset : " + HoldingAssetList.get(i).getName() + "Available percentage =   " + aa/ta);
    AssetAvailableTradingPercentage.put(HoldingAssetList.get(i).getName(), new Double(aa/ta));
}
}


		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (ToCheck) {
		   List<String> CandidateFundList = new ArrayList<String>();
List<String> MainAssetClassList = new ArrayList<String>();
StartDate = CurrentPortfolio.getStartingDate();

printToLog("Cash = " + CurrentPortfolio.getCash());

if(LetRiskProfileChange)
{
for(int i = 0; i < RiskProfileGroup.length - 1; i++)
    if(RiskProfile >= RiskProfileGroup[i] && RiskProfile < RiskProfileGroup[i+1])
        OriginalYearsToRetire = YearsToRetireGroup[i];    
}
StableAllocation = RiskProfile;
RiskyAllocation = 100 - RiskProfile;

if(RiskyAllocation == 100)
    MaxOfRiskyAsset ++;

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

/*Choose the available candidate funds  LJF changed Calculation of MPT 2010.03.16*/
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
        catch(Exception e){continue;}             //Omit those funds that DO NOT Have ParentClassID

    if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -12))){
        if (Fund.getSecurityType() == 1){     //Omit those ETF that Average 3-Month Volume are less than 200k;Configuration.SECURITY_TYPE_ETF=1;
            int AverageVolumeMonth = 3;
            Long Volume=0L;
            Date EndDate=CurrentDate;
            for (int j=0;j<AverageVolumeMonth;j++){
                Volume=Volume+getMonthVolume(Fund.getID(),EndDate);
                EndDate=LTIDate.getNewNYSEMonth(EndDate,-(j+1));
            }
            Volume=Volume/AverageVolumeMonth;
           if (Volume<200000) {PutThisFund = false;/*Testing*/printToLog("TYPE 1 ERROR ;  exclude " + CandidateFund[i]);}
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
				{PutThisFund = false; printToLog("TYPE 2 ERROR ; exclude "+ Fund.getID()); continue;} //TYPE 2

         if ( PutThisFund ) {CandidateFundList.add(CandidateFund[i]);}
    }
    if(i == CandidateFund.length -1 && HaveCash == false)
        {CandidateFundList.add("CASH"); HaveCash = true;}	
}

printToLog("CandidateFundList initialize ok");

/*Get the available asset classes */

for(int i=0; i< MainAssetClass.length; i++)
{
    MainAssetClassList.add(MainAssetClass[i]);
}

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);

String[] AvailableAssets= new String[AllAssetFundsMap.size()];
int[] IndexOfStable = new int[AvailableAssets.length]; 
Iterator iterator = AllAssetFundsMap.keySet().iterator();
int n1 = 0; int n2 = 0;
while(iterator.hasNext()){
String key = (String)iterator.next();
if(key.equals("FIXED INCOME") || key.equals("INTERNATIONAL BONDS") || key.equals("CASH"))
    {IndexOfStable[n1] = 1; n2++;}
else  IndexOfStable[n1] = 0;
AvailableAssets[n1] = key;
n1++;
}

String[] RiskyAssets= new String[n1 - n2];
String[] StableAssets = new String[n2];
n1 = 0; n2 = 0;
for(int i = 0; i < AllAssetFundsMap.size(); i++)
{
    if(IndexOfStable[i] == 0)  {RiskyAssets[n1] = AvailableAssets[i]; n1++;}
    else  {StableAssets[n2] = AvailableAssets[i]; n2++;}
}
/*
for(int i =0; i <RiskyAssets.length;i++)
{ printToLog("RiskyAssets  " + RiskyAssets[i]);}

for(int i =0; i < StableAssets.length;i++)
{ printToLog("StableAssets  " + StableAssets[i]);}

printToLog("AvailableAssetClassList initialize ok");
*/

/*calculate the momentum scores of benchmarks and select asset classes */

String[] AssetBenchmarks = new String[AvailableAssets.length];
for(int i = 0; i < AvailableAssets.length; i++)
    AssetBenchmarks[i] = getSecurity(assetClassManager.get(AvailableAssets[i]).getBenchmarkID()).getSymbol();

double[] Score = new double[AvailableAssets.length];
Score = getMomentumScore(AssetBenchmarks, CashScoreWeight, CurrentDate);

//Risky Group selection
List<String> RiskyGroupSelect = getSelectedAssetList(Score, AvailableAssets, StableAssets, SelectedPercentage, NumberOfMainRiskyClass, NumberOfMainStableClass, CurrentDate);

String log_m="Risky Group selection" + CurrentDate.toString()+"\r\n";
for(int i=0;i<RiskyGroupSelect.size();i++)
        log_m+=RiskyGroupSelect.get(i) + "\r\n";
printToLog(log_m);

//Stable Group selection
for(int i = 0; i < AvailableAssets.length; i++)
    if(AvailableAssets[i].equals("FIXED INCOME") || AvailableAssets[i].equals("INTERNATIONAL BONDS") || AvailableAssets[i].equals("CASH"))
        continue;
    else
        Score[i] = -10000;

List<String> StableGroupSelect = getSelectedAssetList(Score, AvailableAssets, StableAssets, SelectedPercentage, NumberOfMainRiskyClass, NumberOfMainStableClass, CurrentDate);

log_m="Stable Group selection" + CurrentDate.toString()+"\r\n";
for(int i=0;i<StableGroupSelect.size();i++)
        log_m+=StableGroupSelect.get(i) + "\r\n";
printToLog(log_m);

/*combine selected assets and determine the asset percentages*/

boolean Add = true;
List<String> SelectedAssets = RiskyGroupSelect;
List<Double> SelectedAssetPercentages = new ArrayList<Double>();
for(int i=0;i<RiskyGroupSelect.size();i++)
    SelectedAssetPercentages.add(RiskyAllocation/RiskyGroupSelect.size()/100);

for(int i =0; i < StableGroupSelect.size();i++)
{
    for(int j = 0; j < RiskyGroupSelect.size(); j++)
         if(StableGroupSelect.get(i).equals(RiskyGroupSelect.get(j)))
         {
             SelectedAssetPercentages.set(j, (RiskyAllocation/RiskyGroupSelect.size() + StableAllocation/StableGroupSelect.size())/100 );
             Add = false;
             break;
         }
    if(Add)
    {
        SelectedAssets.add(StableGroupSelect.get(i));
        SelectedAssetPercentages.add(StableAllocation/StableGroupSelect.size()/100);
    }
    Add = true;
}

/*Select preferred funds for the selected assets*/

HashMap<String, List<String>> AssetRepresentFundMap = new HashMap<String, List<String>>();
HashMap<String, Double> FundPercentageMap = new HashMap<String, Double>();
List<String> FundList = null;
List<String> RepresentFundList = null;
String[] Funds = null;
int RepresentNumber;
double Weight;
for(int i = 0; i < SelectedAssets.size(); i++)
{
    FundList  = new ArrayList<String>();
    FundList  = AllAssetFundsMap.get(SelectedAssets.get(i) );
    Funds = new String[FundList.size()];
    Score = new double[FundList.size()];
    for(int j = 0; j< FundList.size(); j++)
        Funds[j] = FundList.get(j);
    if(SelectedAssets.get(i).equals("FIXED INCOME") || SelectedAssets.get(i).equals("INTERNATIONAL BONDS") || SelectedAssets.get(i).equals("CASH"))
        RepresentNumber = MaxOfStableAsset;
    else 
        RepresentNumber = MaxOfRiskyAsset;
    RepresentNumber = RepresentNumber < Funds.length?RepresentNumber : Funds.length;
    Score = getMomentumScore(Funds, CashScoreWeight, CurrentDate);
 
double MaxScore;
int MaxScoreIndex;
for(int l=0; l<=RepresentNumber -1; l++)
{
MaxScore = Score[l];
MaxScoreIndex =l;
  
   for(int j=l+1; j<=Score.length-1; j++)
   {     
     if(MaxScore <= Score[j])
     {
      MaxScore = Score[j];
      MaxScoreIndex = j;
     }
   }
   
   if(MaxScoreIndex !=l)
   {
     String tmp= Funds[MaxScoreIndex];
     Funds[MaxScoreIndex]= Funds[l];
     Funds[l]=tmp; 	 
   
     double t=Score[MaxScoreIndex];
     Score[MaxScoreIndex]=Score[l];
     Score[l]=t;
   }
}

RepresentFundList = new ArrayList<String>();
for(int j = 0; j < RepresentNumber; j++)
{
    RepresentFundList.add(Funds[j] );
    Weight = SelectedAssetPercentages.get(i)/RepresentNumber;
    FundPercentageMap.put(Funds[j], Weight);
    printToLog(SelectedAssets.get(i) + " buy target :  " + Funds[j] + "   target percentage : " + Weight);
}

AssetRepresentFundMap.put(SelectedAssets.get(i), RepresentFundList);
/*testing*/printToLog("AssetRepresentFundMap: "+AssetRepresentFundMap);
}


//========================================================================================
//========================================================================================
Asset CurrentAsset = null;
boolean New;
for(int i=0; i < SelectedAssets.size(); i++)
{
    New = true;
    for(int j = 0; j < HoldingAssetList.size(); j++)
        if(SelectedAssets.get(i).equals(HoldingAssetList.get(j).getName()))
            New = false;
    if(!New)
    {
        CurrentAsset=new Asset();
        CurrentAsset = CurrentPortfolio.getAsset(SelectedAssets.get(i));
        CurrentAsset .setTargetPercentage(SelectedAssetPercentages.get(i));
    }
    else
    {
        CurrentAsset=new Asset();
        CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
        CurrentAsset.setName(SelectedAssets.get(i));
        CurrentAsset.setClassID(getAssetClassID(SelectedAssets.get(i)));
        CurrentAsset.setTargetPercentage(SelectedAssetPercentages.get(i));
        CurrentPortfolio.addAsset(CurrentAsset);
    }
}
for(int i = 0;i < HoldingAssetList.size(); i++)
{
    New = true;
    for(int j = 0; j < SelectedAssets.size(); j ++)
        if(SelectedAssets.get(j).equals(HoldingAssetList.get(i).getName()))
            {New = false; break;} 
    if(New)
    {
        SelectedAssets.add(HoldingAssetList.get(i).getName());
        SelectedAssetPercentages.add(0.00);        

        CurrentAsset=new Asset();
        CurrentAsset = CurrentPortfolio.getAsset(HoldingAssetList.get(i).getName());
        CurrentAsset .setTargetPercentage(0.00);
    }
}
//========================================================================================
//========================================================================================
List<String> AvailableAssetClassList = new ArrayList<String>();
/*Get the available asset classes and divide the funds.  */

for(int i=0; i< MainAssetClass.length; i++)
{
    MainAssetClassList.add(MainAssetClass[i]);
}

for(int i = 0; i< MainAssetClass.length; i++)
{
    if(AllAssetFundsMap.get(MainAssetClass[i]) != null)
    {
        AvailableAssetClassList.add(MainAssetClass[i]);
    }
}

/*Select the presentative funds of each asset class*/

HashMap<String,List<String>>  PresentativeAssetFundMap = new HashMap<String,List<String>>();

PresentativeAssetFundMap = AssetRepresentFundMap;

//========================================================================================
//Adj for BuyThreshold and SellThreshold
BuyThreshold = MinimumBuyingPercentage*100;
SellThreshold =MinimumSellingPercentage*100;
//Adj for TargetPercentage !!! 0-100 NOT 0-1
HoldingAssetList =  CurrentPortfolio.getCurrentAssetList();
CurrentAsset = null;
for(int kk = 0; kk < HoldingAssetList.size(); kk++)
{
CurrentAsset = CurrentPortfolio.getAsset(HoldingAssetList.get(kk).getName());
CurrentAsset .setTargetPercentage(CurrentAsset.getTargetPercentage()*100);
/*Testing*/printToLog("TargetPercentage"+CurrentAsset.getTargetPercentage());
}
//========================================================================================
//========================================================================================
//Version 2010.04.01
/*Trading and Rebalancing Under Redemption Limit*/

HashMap<String,Integer> RedemptionMonthMap = new HashMap<String,Integer>();  

Integer K;
for(int i =0; i< CandidateFund.length;i++)
{ 
    K = new Integer(RedemptionLimit[i]);
    RedemptionMonthMap.put(CandidateFund[i], K);
}

double TotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);

/*First, Sell  as much as possible; and Detect the unFavorable Funds in each AssetClass of CurrentPortfolio
1.the poor-performance funds (i.e funds not in the PresentativeAssetFundMap)
2.Those FavorableFunds(in the PresnetativeAssetFundMap) may >TargetPercentage */
List<Asset>  CurrentAssetList = CurrentPortfolio.getCurrentAssetList(); /*List<Asset> CurrentAssetList has already declared*/ //Adj for TAA
HashMap<String, Double>UnfavClassAmountMap=new HashMap<String, Double>();	/*UnfavClassAmountMap Stores the amount of which those funds should be sold, but under RedemptionLimit they can not*/
List <String> FavSecurNameList=new ArrayList<String>();   

printToLog("------SELL SIGNAL ------");

//Since it's Sell signal(happen at next open), not actually sell action, getCash() & getAssetAmount() & getSecurityAmount() APIs can not be used. 
//AvailCapital & AssetAmountMap & SellSecurityAmountMap Stores these data relatively.
//Unfinished getSecurityAmount!!!
double AvailCapital= CurrentPortfolio.getCash(); /*Available Capital that can be used to buy other funds*/
printToLog("CurrentPortfolio.getCash() ="+CurrentPortfolio.getCash());
HashMap<String, Double> AssetAmountMap = new HashMap<String, Double>();
HashMap<String, Double> SellSecurityAmountMap= new HashMap<String, Double>();


for (int i=0; i<CurrentAssetList.size();i++)
{
    CurrentAsset =CurrentAssetList.get(i);   
    List<HoldingItem> CurrSecurList=CurrentAsset.getHoldingItems();  
    double UnfavClassAmount=0;//Adj for TAA
	if (PresentativeAssetFundMap.get(CurrentAsset.getName())!= null)       
        {FavSecurNameList=PresentativeAssetFundMap.get(CurrentAsset.getName());}   
       else printToLog("Unexpected error occured while trading, or this Whole Asset"+CurrentAsset.getName()+"  is Unfavorable");   //Adj for TAA   
    
	double AssetAmount=CurrentPortfolio.getAssetAmount(CurrentAsset, CurrentDate);
	printToLog("---"+"\r\n"+CurrentAsset.getName()+"'s asset amount  before Sell Signal : "+ AssetAmount);
	
    for(int j=0; j<CurrSecurList.size();j++)
	{   
        if (FavSecurNameList.indexOf(CurrSecurList.get(j).getSymbol())<0)      /*Sell the poor-performance funds as much as possible*/
		{    
            double SellAmount=CurrentPortfolio.getAvailableTradingAmount(CurrSecurList.get(j).getSymbol(), RedemptionMonthMap.get(CurrSecurList.get(j).getSymbol()), CurrentDate,1);
			/*Testing*/printToLog("AvailSellAmount = "+SellAmount);
            if (SellAmount>TotalAmount*SellThreshold/100){
            CurrentPortfolio.sellAtNextOpen(CurrentAsset.getName(), CurrSecurList.get(j).getSymbol(), SellAmount, CurrentDate);   
            printToLog("SELL SIGNAL type 1:"+CurrentAsset.getName()+"  "+CurrSecurList.get(j).getSymbol()+"  "+SellAmount);
			AvailCapital+=SellAmount;//record the SellAmount
			AssetAmount-=SellAmount;// record for AssetAmountMap
			SellSecurityAmountMap.put(CurrSecurList.get(j).getSymbol(),new Double(SellAmount));//record for SellSecurityAmountMap
			} 
						
            UnfavClassAmount+=(CurrentPortfolio.getAvailableTradingAmount(CurrSecurList.get(j).getSymbol(), 0, CurrentDate,1)-SellAmount);   // getTotalAmount() adj TAA ??
        }   
        else 
		{
        double TargetSecurPercentPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size();/*Allocate Averagely :Equal Weight for each funds in a certain AssetClass*/
        double CurrentSecurPercentPortLevel=CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),CurrSecurList.get(j).getSymbol(),CurrentDate)/TotalAmount*100;
		double AvailSellAmount;
		printToLog("TargetSecurPercentPortLevel "+TargetSecurPercentPortLevel+"; CurrentSecurPercentPortLevel "+CurrentSecurPercentPortLevel);
		
			/*Get Available Amount Action for the CASH ASSET*/
			if (!CurrSecurList.get(j).getSymbol().equals("CASH")){AvailSellAmount=CurrentPortfolio.getAvailableTradingAmount(CurrSecurList.get(j).getSymbol(), RedemptionMonthMap.get(CurrSecurList.get(j).getSymbol()), CurrentDate,1);}
			else {AvailSellAmount=CurrentPortfolio.getAvailableTradingAmount(CurrSecurList.get(j).getSymbol(), 0, CurrentDate,1);}  //getTotalAmount() adj TAA ??
			
		printToLog("AvailSellAmount ="+AvailSellAmount+" ; "+" SpreadAmount= "+(CurrentSecurPercentPortLevel-TargetSecurPercentPortLevel)*TotalAmount/100);
        double SellAmount=java.lang.Math.min(AvailSellAmount/TotalAmount,(CurrentSecurPercentPortLevel-TargetSecurPercentPortLevel)/100)*TotalAmount;
            if (SellAmount>TotalAmount*SellThreshold/100)
			{
            CurrentPortfolio.sellAtNextOpen(CurrentAsset.getName(),CurrSecurList.get(j).getSymbol(),SellAmount, CurrentDate);   
			printToLog("SELL SIGNAL type 2:"+CurrentAsset.getName()+"  "+CurrSecurList.get(j).getSymbol()+"  "+SellAmount);
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
	if (AssetAmountMap.get(CurrentAsset.getName())!= null){Decimal=CurrentAsset.getTargetPercentage()/100-AssetAmountMap.get(CurrentAsset.getName())/TotalAmount;}
		else {Decimal = CurrentAsset.getTargetPercentage()/100 - 0;  /*new favorable asset added*/}
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
  
printToLog("AvailCapital = "+AvailCapital);//Available Capital records Available Amount that can be used to BUY other funds
double UseCapital=0;//initial the Use_Capital varialbe which records the Actual Amount for BUY ACTION

printToLog("--------BUY SIGNAL--------");

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
	double TotalAssetAmount=0;
	if (AssetAmountMap.get(CurrentAsset.getName())!=null){AssetAmountMap.get(CurrentAsset.getName());}
	else {printToLog("Testing ???! ~~~");}
	printToLog("---"+"\r\n"+CurrentAsset.getName());
	
	if (CurrentAsset.getTargetPercentage()-UnfavPercent>BuyThreshold) 
	{
		double TargetSecurPercentAtPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size();/*Allocate Averagely :Equal Weight for each funds in a certain AssetClass*/
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
					{SpreadPercent+=SellSecurityAmountMap.get(FavSecurNameList.get(i))/TotalAmount*100;}
				
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
//???<>BuyThreshold not so sure
		if (TempPercentTotal > BuyThreshold)
		{
			for (int i=0; i<FavSecurNameList.size();i++)
			{
			SecurWeight[i]=SecurWeight[i]/TempPercentTotal;
			}
			
			/*buy Securities*/
			for (int i=0; i<FavSecurNameList.size();i++)
			{
				printToLog("Security: "+FavSecurNameList.get(i)+" --SecurWeight[i]= "+SecurWeight[i]+" ; "+" ClassWeight: = "+ClassWeightMap.get(AssetName));
				double Amount=AvailCapital*SecurWeight[i]*ClassWeightMap.get(AssetName);
				if (Amount>TotalAmount*BuyThreshold/100) 
				{
					CurrentPortfolio.buyAtNextOpen(CurrentAsset.getName(),FavSecurNameList.get(i),Amount,CurrentDate,true);
					printToLog("BUY SIGNAL type 1  "+CurrentAsset.getName()+" "+FavSecurNameList.get(i)+" "+Amount);
					UseCapital+=Amount;
				} 
			}
		} //  End if (TempPercentTotal>BuyThreshold)
		
		if (TempPercentTotal <BuyThreshold)
		{
			int Index=FavSecurNameList.size()-1;
			for (int i=0; i<FavSecurNameList.size();i++)
			{
				double CurrentSecurPercentPortLevel=CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),FavSecurNameList.get(i),CurrentDate)/TotalAmount*100;
				if (SellSecurityAmountMap.get(FavSecurNameList.get(i))!=null) //SellAtNextOpen is just sell signals, no actual action today, so getSecurityAmount is the same regardless of SellAtNextOpen
					{CurrentSecurPercentPortLevel-=SellSecurityAmountMap.get(FavSecurNameList.get(i))/TotalAmount*100;}
				double TargetSecurPercentPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size();/*Allocate Averagely :Equal Weight for each funds in a certain AssetClass*/
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
			double Amount=AvailCapital*SecurWeight[Index]*ClassWeightMap.get(AssetName);
			if (Amount > TotalAmount*BuyThreshold/100)
			{
			CurrentPortfolio.buyAtNextOpen(CurrentAsset.getName(),FavSecurNameList.get(Index),Amount, CurrentDate,true);
			printToLog("BUY SIGNAL type 2  "+CurrentAsset.getName()+"   "+FavSecurNameList.get(Index)+" "+Amount);
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
    Map.Entry<String,List<String>> entry = (Map.Entry<String,List<String>>) iter.next(); 
    AssetName = entry.getKey(); 
    FavSecurNameList = entry.getValue(); 
	CurrentAsset=CurrentPortfolio.getAsset(AssetName);
	CurrentPortfolio.buyAtNextOpen(AssetName,getSecurity(FavSecurNameList.get(0)).getSymbol(), SalvageAmount, CurrentDate,true);
	printToLog("BUY SIGNAL type 3 "+CurrentAsset.getName()+"  "+FavSecurNameList.get(0)+" "+SalvageAmount);
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