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
public class Tactical_Asset_Allocation_diversify_bonds_05081103 extends SimulateStrategy{
	public Tactical_Asset_Allocation_diversify_bonds_05081103(){
		super();
		StrategyID=1103L;
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
		MainAssetClass=(String[])ParameterUtil.fetchParameter("String[]","MainAssetClass", "US EQUITY, INTERNATIONAL EQUITY, FIXED INCOME, REAL ESTATE, COMMODITIES, Emerging Market, INTERNATIONAL BONDS, High Yield Bond, CASH", parameters);
		NumberOfMainRiskyClass=(Integer)ParameterUtil.fetchParameter("int","NumberOfMainRiskyClass", "2", parameters);
		NumberOfMainStableClass=(Integer)ParameterUtil.fetchParameter("int","NumberOfMainStableClass", "1", parameters);
		RiskProfile=(Double)ParameterUtil.fetchParameter("double","RiskProfile", "30", parameters);
		LetRiskProfileChange=(Boolean)ParameterUtil.fetchParameter("boolean","LetRiskProfileChange", "false", parameters);
		RiskProfileGroup=(double[])ParameterUtil.fetchParameter("double[]","RiskProfileGroup", "12,15,16,17,24,30,38,48,51,56,78,80,90", parameters);
		YearsToRetireGroup=(double[])ParameterUtil.fetchParameter("double[]","YearsToRetireGroup", "40,35,30,25,20,15,10,5,0,-5,-10,-12,-15", parameters);
		CheckDate=(Integer)ParameterUtil.fetchParameter("int","CheckDate", "31", parameters);
		MinimumBuyingPercentage=(Double)ParameterUtil.fetchParameter("double","MinimumBuyingPercentage", "0.01", parameters);
		MinimumSellingPercentage=(Double)ParameterUtil.fetchParameter("double","MinimumSellingPercentage", "0", parameters);
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
		NewYear=(Boolean)stream.readObject();;
		LastActionMonth=(Integer)stream.readObject();;
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

/*Temp Testing */
{
String log_m="Ranked Score" + CurrentDate.toString()+"\r\n";
for(int i=0;i<CandidateFunds.length;i++)
        log_m+=CandidateFunds[i] +"  " + score[i] + "\r\n";
 printToLog(log_m);
}


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

                Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(CurrentDate);
                LastActionMonth = tmpCa.get(Calendar.MONTH);  


if(LetRiskProfileChange)
{
for(int i = 0; i < RiskProfileGroup.length - 1; i++)
    if(RiskProfile >= RiskProfileGroup[i] && RiskProfile < RiskProfileGroup[i+1])
        OriginalYearsToRetire = YearsToRetireGroup[i];    
}
StableAllocation = RiskProfile;
RiskyAllocation = 100 - RiskProfile;

// To match the TAA on VaildFi.com when Risk Profile = 0, always select 3 asset unless CASH out performance
if(RiskProfile == 0)    
{
    NumberOfMainRiskyClass = 3;
    NumberOfMainStableClass = 3;
    MaxOfRiskyAsset  =  1;
    MaxOfStableAsset = 1;
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
        if(UseDefaultRedemption)
        {    
            if(CandidateFund[k].equals("CASH"))
                RedemptionLimit[k] = 0;
            else
                RedemptionLimit[k] = DefaultRedemptionLimit;
        }
        else
            RedemptionLimit[k] = Temp3[i];
        k++; 
    }
}

/*get the asset class of the candidate funds LJF changed Calculation of MPT 2010.03.16*/

AssetClass = new String[CandidateFund.length];
for(int i = 0; i < CandidateFund.length; i++)
    try{AssetClass[i] = getSecurity(CandidateFund[i]).getAssetClass().getName();}  
        catch(Exception e){AssetClass[i] = "Unknow";}

/*Choose the available candidate funds*/
Security Fund = null;
Security Bench= null;

		Calendar cal = Calendar.getInstance(); // for calculation of MPT
		cal.setTime(CurrentDate); //for calculation of MPT
		long LastYear = cal.get(Calendar.YEAR)-1; //for calculation of MPT

InAfterDateFilter = new int[CandidateFund.length];
long sTime = System.currentTimeMillis();
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
    
    if(Fund.equals("SSSFX"))  {PutThisFund = false; continue;}
    if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -12))){
 
       InAfterDateFilter[i] = 1;      
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
				{PutThisFund = false; printToLog("Too Volatile ; exclude "+ Fund.getID()); continue;} //TYPE 2

        if (PutThisFund && Fund.getSecurityType() == 1){     //Omit those ETF that Average 3-Month Volume are less than 200k;Configuration.SECURITY_TYPE_ETF=1;
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

        if ( PutThisFund ) {CandidateFundList.add(CandidateFund[i]);}
    }
    if(i == CandidateFund.length -1 && HaveCash == false)
        {CandidateFundList.add("CASH"); HaveCash = true;}	
}
printToLog("CandidateFundList initialize ok");
long eTime = System.currentTimeMillis();
System.out.println("Selection Time Cost:  " + (eTime - sTime));


/*Get the available asset classes */

for(int i=0; i< MainAssetClass.length; i++)
{
    MainAssetClassList.add(MainAssetClass[i]);
}

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);
    try{AllAssetFundsMap.remove("ROOT");}     // templately fix the problem of "getAvailableAssetClassSet" added on 2010.05.08  please delete it when the problem is fixed
        catch(Exception e){ } 

String[] AvailableAssets= new String[AllAssetFundsMap.size()];
int[] IndexOfStable = new int[AvailableAssets.length]; 
Iterator iterator = AllAssetFundsMap.keySet().iterator();
int n1 = 0; int n2 = 0;
printToLog("-------------Available Assets  ----------------------- ");
while(iterator.hasNext()){
String key = (String)iterator.next();
if(key.equals("FIXED INCOME") || key.equals("INTERNATIONAL BONDS") || key.equals("CASH"))
    {IndexOfStable[n1] = 1; n2++;}
else  IndexOfStable[n1] = 0;
AvailableAssets[n1] = key;
n1++;
printToLog("Asset " + n1 + " :  " + key);
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

/* make neccesary diversification of the stable asset classes, Added by WYJ on 2010.05.08  

if(SelectedAssets.size() == 1)
    MaxOfStableAsset = 3;
else if(SelectedAssets.size() == 2)
    MaxOfStableAsset = 2;

     //Add CASH into the stable asset class
    List<String> TempFundList = new ArrayList<String>();  
    for(int i = 0; i < StableGroupSelect.size(); i++)
    {    
         TempFundList =  AllAssetFundsMap.get(StableGroupSelect.get(i));
         if(TempFundList != null)
         {
             TempFundList.add("CASH");
             TempFundList.add("CASH");
             TempFundList.add("CASH");
             AllAssetFundsMap.put(StableGroupSelect.get(i), TempFundList);
         }
    } 
*/  

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
}



/*Create Assets*/

boolean HaveCashAsset  = false; 
Asset CurrentAsset;
for(int i=0; i < SelectedAssets.size(); i++)
{
if(SelectedAssets.get(i).equals("CASH"))    HaveCashAsset = true;  
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(SelectedAssets.get(i));
CurrentAsset.setClassID(getAssetClassID(SelectedAssets.get(i)));
CurrentAsset.setTargetPercentage(SelectedAssetPercentages.get(i).doubleValue());
CurrentPortfolio.addAsset(CurrentAsset);
}

//Create a "CASH" asset class for the buyAtNextOpen API      Add on 2010.04.28
if(!HaveCashAsset)
{
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("CASH");
CurrentAsset.setClassID(getAssetClassID("CASH"));
CurrentAsset.setTargetPercentage(0.0);
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
List<Asset> HoldingAssetList =  new ArrayList<Asset>();
HashMap<String,List<HoldingItem>> HoldingAssetSecurityMap = new HashMap<String,List<HoldingItem>>();
HashMap<String,Double>  SecurityAvailableTradingPercentage = new HashMap<String,Double>();
HashMap<String,Double>  AssetAvailableTradingPercentage = new HashMap<String,Double>();

if((CheckFrequency.equals("monthly")&&LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) || (CheckFrequency.equals("quarterly")&&LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate)) || 
(CheckFrequency.equals("yearly")&&LTIDate.isLastNYSETradingDayOfYear(CurrentDate)))
{
                Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(CurrentDate);
if(tmpCa.get(Calendar.MONTH) < LastActionMonth)    NewYear = true;
HoldingAssetList =  CurrentPortfolio.getCurrentAssetList();
/*form redemption limit map*/
Integer K ;
for(int i =0; i< CandidateFund.length;i++)
{ 
    if(UseDefaultRedemption)
        K= new Integer(DefaultRedemptionLimit);
    else
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
		   HoldingAssetList =  CurrentPortfolio.getCurrentAssetList();
List<String> CandidateFundList = new ArrayList<String>();
List<String> MainAssetClassList = new ArrayList<String>();
StartDate = CurrentPortfolio.getStartingDate();

                Calendar tmpCa = Calendar.getInstance();
		tmpCa.setTime(CurrentDate);
                LastActionMonth = tmpCa.get(Calendar.MONTH);  

printToLog("Cash = " + CurrentPortfolio.getCash());

if(LetRiskProfileChange)
{
for(int i = 0; i < RiskProfileGroup.length - 1; i++)
    if(RiskProfile >= RiskProfileGroup[i] && RiskProfile < RiskProfileGroup[i+1])
        OriginalYearsToRetire = YearsToRetireGroup[i];    
}
StableAllocation = RiskProfile;
RiskyAllocation = 100 - RiskProfile;

// To match the TAA on VaildFi.com when Risk Profile = 0, always select 3 asset unless CASH out performance
if(RiskProfile == 0)    
{
    NumberOfMainRiskyClass = 3;
    NumberOfMainStableClass = 3;
    MaxOfRiskyAsset  =  1;
    MaxOfStableAsset = 1;
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
        if(UseDefaultRedemption)
        {    
            if(CandidateFund[k].equals("CASH"))
                RedemptionLimit[k] = 0;
            else
                RedemptionLimit[k] = DefaultRedemptionLimit;
        }
        else
            RedemptionLimit[k] = Temp3[i];
        k++; 
    }
}

/*get the asset class of the candidate funds*/

AssetClass = new String[CandidateFund.length];
for(int i = 0; i < CandidateFund.length; i++)
    try{AssetClass[i] = getSecurity(CandidateFund[i]).getAssetClass().getName();}  
        catch(Exception e){AssetClass[i] = "Unknow";}

/*Choose the available candidate funds LJF Changed calculation of MPT 2010.03.16*/
Security Fund = null;
Security Bench= null;

		Calendar cal = Calendar.getInstance(); // for calculation of MPT
		cal.setTime(CurrentDate); //for calculation of MPT
		long LastYear = cal.get(Calendar.YEAR)-1; //for calculation of MPT

int[] OldIn = InAfterDateFilter;
long sTime = System.currentTimeMillis();
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
    
    if(Fund.equals("SSSFX"))  {PutThisFund = false; continue;}
    if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -12))){
     InAfterDateFilter[i] = 1;
     
      // Omit those Funds that 12-month S.D> 1.5*Benchmark  (Version 2010.02.04)
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
				{PutThisFund = false; printToLog("Too Volatile ; exclude "+ Fund.getID()); continue;} 
                        }


        //Omit those ETF that Average 3-Month Volume are less than 200k;Configuration.SECURITY_TYPE_ETF=1;
        if (PutThisFund && Fund.getSecurityType() == 1){     
            int AverageVolumeMonth = 3;
            Long Volume=0L;
            Date EndDate=CurrentDate;
            for (int j=0;j<AverageVolumeMonth;j++){
                Volume=Volume+getMonthVolume(Fund.getID(),EndDate);
                EndDate=LTIDate.getNewNYSEMonth(EndDate,-(j+1));
            }
            Volume=Volume/AverageVolumeMonth;
           if (Volume<200000) {PutThisFund = false;/*Testing*/printToLog("Too Small Volume ;  exclude " + CandidateFund[i]);}
       }

        if ( PutThisFund ) {CandidateFundList.add(CandidateFund[i]);}
    }
    if(i == CandidateFund.length -1 && HaveCash == false)
        {CandidateFundList.add("CASH"); HaveCash = true;}	
}
printToLog("CandidateFundList initialize ok");
long eTime = System.currentTimeMillis();
System.out.println("Selection Time Cost:  " + (eTime - sTime));

/*Get the available asset classes */

for(int i=0; i< MainAssetClass.length; i++)
{
    MainAssetClassList.add(MainAssetClass[i]);
}

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);
    try{AllAssetFundsMap.remove("ROOT");}     // templately fix the problem of "getAvailableAssetClassSet" added on 2010.05.08  please delete it when the problem is fixed
        catch(Exception e){ } 

String[] AvailableAssets= new String[AllAssetFundsMap.size()];
int[] IndexOfStable = new int[AvailableAssets.length]; 
Iterator iterator = AllAssetFundsMap.keySet().iterator();
int n1 = 0; int n2 = 0;
printToLog("-------------Available Assets  ----------------------- ");
while(iterator.hasNext()){
String key = (String)iterator.next();
if(key.equals("FIXED INCOME") || key.equals("INTERNATIONAL BONDS") || key.equals("CASH"))
    {IndexOfStable[n1] = 1; n2++;}
else  IndexOfStable[n1] = 0;
AvailableAssets[n1] = key;
n1++;
printToLog("Asset " + n1 + " :  " + key);
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

sTime = System.currentTimeMillis();
double[] Score = new double[AvailableAssets.length];
Score = getMomentumScore(AssetBenchmarks, CashScoreWeight, CurrentDate);
eTime = System.currentTimeMillis();
System.out.println("Asset Class Momentum Time Cost:  " + (eTime - sTime));

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

/* make neccesary diversification of the stable asset classes, Added by WYJ on 2010.05.08   

if(SelectedAssets.size() == 1)
    MaxOfStableAsset = 3;
else if(SelectedAssets.size() == 2)
    MaxOfStableAsset = 2;

     //Add CASH into the stable asset class
    List<String> TempFundList = new ArrayList<String>();  
    for(int i = 0; i < StableGroupSelect.size(); i++)
    {    
         TempFundList =  AllAssetFundsMap.get(StableGroupSelect.get(i));
         if(TempFundList != null)
         {
             TempFundList.add("CASH");
             TempFundList.add("CASH");
             TempFundList.add("CASH");
             AllAssetFundsMap.put(StableGroupSelect.get(i), TempFundList);
         }
    } 
*/  

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
    sTime = System.currentTimeMillis();
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
   eTime = System.currentTimeMillis();
   System.out.println(SelectedAssets.get(i) +" : "+ "Fund Momentum Time Cost:  " + (eTime - sTime));
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
}

/*do not balance the portfolio funds that is representative again, except for the fix income funds */
String sn;
double sa;
double aa;
List<String> RepresentFunds = null;
List<HoldingItem> HoldingSecurityList = null;

printToLog("--------------Adjusting the available percentage------------------------ ");
for(int i = 0; i < HoldingAssetList.size(); i++)
{
    printToLog("Asset : " + HoldingAssetList.get(i).getName() );
    if(HoldingAssetList.get(i).getName().equals("FIXED INCOME") || HoldingAssetList.get(i).getName().equals("INTERNATIONAL BONDS") || HoldingAssetList.get(i).getName().equals("CASH"))
        continue;
    else
    {
    HoldingSecurityList = new ArrayList<HoldingItem>();
    HoldingSecurityList = HoldingAssetList.get(i).getHoldingItems();
    aa = AssetAvailableTradingPercentage.get(HoldingAssetList.get(i).getName());
    for(int j = 0; j < HoldingSecurityList.size(); j++)
    {
        sn = HoldingSecurityList.get(j).getSymbol();
        if(AssetRepresentFundMap.get(HoldingAssetList.get(i).getName()) != null)
        {
            printToLog("Existing selected asset : " + HoldingAssetList.get(i).getName());
            RepresentFunds = new ArrayList<String>();
            RepresentFunds = AssetRepresentFundMap.get(HoldingAssetList.get(i).getName());
            for(int m = 0; m < RepresentFunds.size(); m++)
                if(sn.equals(RepresentFunds.get(m)))
                {
                    sa = SecurityAvailableTradingPercentage.get(sn);
                    aa = AssetAvailableTradingPercentage.get(HoldingAssetList.get(i).getName());
                    aa -= sa;
                    SecurityAvailableTradingPercentage.put(sn, 0.00);
                    AssetAvailableTradingPercentage.put(HoldingAssetList.get(i).getName(), aa);
                    break;
                }
        }
    }
    }
    printToLog("adjusted available percentage = " + aa );
}

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


/*calculate the asset trading percentage*/

HashMap<String,Double>  AssetTargetPercentageMap = new HashMap<String,Double>();
HashMap<String,Double>  HoldingAssetActualPercentage = new HashMap<String,Double>();
double ta = CurrentPortfolio.getTotalAmount(CurrentDate);
aa=0;

for(int i=0; i < SelectedAssetPercentages.size(); i++)
    AssetTargetPercentageMap.put(SelectedAssets.get(i), new Double(SelectedAssetPercentages.get(i)) );

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
int[] AbleToBuy = new int[SelectedAssets.size()];
double[] PlanToBuyPercentage = new double[SelectedAssets.size()];
for(int i = 0; i <  SelectedAssets.size(); i++)
{
    AssetName =  SelectedAssets.get(i);
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
    while(UnableToBuyPercentage > 0 && i < SelectedAssets.size())
    {
        AssetName =  SelectedAssets.get(i);
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
    String MaxTargetPercentageAsset = SelectedAssets.get(0);
    double MaxTargetPercentage = AssetTargetPercentageMap.get(MaxTargetPercentageAsset).doubleValue();
    double CompareTargetPercentage;
    for(int i =1; i < SelectedAssets.size(); i++)
    {
        CompareTargetPercentage = AssetTargetPercentageMap.get(SelectedAssets.get(i)).doubleValue();
        if(CompareTargetPercentage > MaxTargetPercentage)
        {
            MaxTargetPercentage = CompareTargetPercentage;
            MaxTargetPercentageAsset  = SelectedAssets.get(i);
        }
    }
    double TempTrading =  AssetActualTradingPercentage.get(MaxTargetPercentageAsset).doubleValue();
    double NewActualTradingPercentage = TempTrading + UnableToBuyPercentage;
    if(NewActualTradingPercentage < 0.0001) NewActualTradingPercentage = 0;
    AssetActualTradingPercentage.put(MaxTargetPercentageAsset, new Double(NewActualTradingPercentage));
}

for(int i = 0; i < SelectedAssets.size(); i++)
    printToLog(SelectedAssets.get(i) + " trading percentage = " + AssetActualTradingPercentage.get(SelectedAssets.get(i)).doubleValue());


/*Calculate trading percentage of each security, not rebalance*/

HashMap<String,HashMap<String, Double>>  AssetSecurityTradingPercentageMap = new  HashMap<String,HashMap<String, Double>>();
HashMap<String, Double>  SecurityActualTradingPercentage = null;
HashMap<String, Double>  SecurityTargetPercentageMap = new HashMap<String,Double>();
HashMap<String, Double>  HoldingSecurityActualPercentage = new HashMap<String,Double>();
HoldingSecurityList = null;
List<String> PresentativeSecurityList = null;
double TotalBuyingPercentage;
double SellingPercentage;
double BuyingPercentage;
double SecurityActualPercentage;
double MaxOfSecurityPercentage;
String SecurityName;
int[] NewAdd = new int[SelectedAssets.size()];
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);

for(int i = 0; i < SelectedAssets.size(); i++)
{
    AssetName =  SelectedAssets.get(i);
    HoldingSecurityList = new ArrayList<HoldingItem>();
    PresentativeSecurityList = new ArrayList<String>();
    PresentativeSecurityList = AssetRepresentFundMap.get(AssetName);
    TargetPercentage = AssetTargetPercentageMap.get(AssetName).doubleValue();
    if(HoldingAssetActualPercentage.get(AssetName) != null)
    {
        HoldingSecurityList = CurrentPortfolio.getAsset(AssetName).getHoldingItems();
        ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
        AvailableTradingPercentage =  AssetAvailableTradingPercentage.get(AssetName).doubleValue();
    }
    else  
        { HoldingSecurityList = null ; ActualPercentage = 0; AvailableTradingPercentage =0; NewAdd[i] = 1; }
     printToLog("i = " + i + "  ActualPercentage = " + ActualPercentage + "  TargetPercentage = " + TargetPercentage + "  AvailableTradingPercentage = " + AvailableTradingPercentage + "  AssetActualTradingPercentage = " + AssetActualTradingPercentage.get(AssetName).doubleValue() );

    if((AvailableTradingPercentage <= ActualPercentage - TargetPercentage  && AvailableTradingPercentage != 0) || PresentativeSecurityList == null)
    {
        SecurityActualTradingPercentage = new HashMap<String, Double>();
        for(int j = 0; j< HoldingSecurityList.size(); j++)
        {
            SellingPercentage  = SecurityAvailableTradingPercentage.get(HoldingSecurityList.get(j).getSymbol());
            SecurityActualTradingPercentage.put(HoldingSecurityList.get(j).getSymbol(), new Double(-SellingPercentage));
        }
        AssetSecurityTradingPercentageMap.put(AssetName, SecurityActualTradingPercentage);
    }
    else if(NewAdd[i] == 1)
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
             {
                 printToLog("condition 1");  
/*
                 if(j == PresentativeSecurityList.size() -1)      original before 2010.04.15
                     SecurityActualTradingPercentage.put(SecurityName, new Double(TotalBuyingPercentage - SellingPercentage));
                     TotalBuyingPercentage = 0;
*/
                 if(SellingPercentage == 0 &&  j != PresentativeSecurityList.size() -1)       /*modify on 2010.04.15*/
                      SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
                 else if(j == PresentativeSecurityList.size() -1)      
                 {
                     SecurityActualTradingPercentage.put(SecurityName, new Double(TotalBuyingPercentage - SellingPercentage));
                     TotalBuyingPercentage = 0;
                      printToLog("condition 1-1");
                  }
                  else
                  {
                       SecurityActualTradingPercentage.put(SecurityName, new Double(- SellingPercentage));
                       TotalBuyingPercentage -= SellingPercentage ;
                       printToLog("condition 1-2");
                   }
             }
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

 printToLog("Total Amount  =   "  +    TotalAmount );


/*Do the selling transaction*/

HoldingAssetList = CurrentPortfolio.getCurrentAssetList();
for(int i = 0; i < HoldingAssetList.size(); i++)
{
    HoldingSecurityList = new ArrayList<HoldingItem>();
    HoldingSecurityList = HoldingAssetList.get(i).getHoldingItems();
    for(int j = 0; j < HoldingSecurityList.size(); j++)
    {
        sn = HoldingSecurityList.get(j).getSymbol();
        //printToLog("Asset : " + HoldingAssetList.get(i).getName() +  " Security Symbol: "+ sn);
    }
}


for(int i = 0; i < SelectedAssets.size(); i++)
{
    AssetName = SelectedAssets.get(i);
    SecurityActualTradingPercentage = new HashMap<String, Double>();
    SecurityActualTradingPercentage = AssetSecurityTradingPercentageMap.get(AssetName);
    if(SecurityActualTradingPercentage != null)
    {
        Iterator iter = SecurityActualTradingPercentage.keySet().iterator();
        while (iter.hasNext())
        {      
        String TradingSecurity = (String)iter.next(); 
        double TradePercentage = SecurityActualTradingPercentage.get(TradingSecurity).doubleValue(); 
        //printToLog("To trading Asset : " +AssetName + "TradingSecurity : "  + TradingSecurity);
        if(TradePercentage < 0){
            CurrentPortfolio.sellAtNextOpen(AssetName, TradingSecurity, TotalAmount * (- TradePercentage), CurrentDate);
            printToLog("Sell Signal: "+AssetName+"  "+TradingSecurity+"  $ "+TotalAmount*(-TradePercentage));
            }
        }
    }
}

/*Do the buying transaction*/

for(int i = 0; i < SelectedAssets.size(); i++)
{
    AssetName = SelectedAssets.get(i);
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
        if(TradePercentage.doubleValue() > 0){
            CurrentPortfolio.buyAtNextOpen(AssetName, TradingSecurity, TotalAmount * TradePercentage.doubleValue(), CurrentDate,true);
            printToLog("Buy Signal: "+AssetName+"  "+TradingSecurity+"  $ "+TotalAmount*(TradePercentage));
            }
        }
    }
}

if(NewYear)  NewYear = false;


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