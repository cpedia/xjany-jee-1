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
public class Tactical_Asset_Allocation_for_old_engine_backup_09204826 extends SimulateStrategy{
	public Tactical_Asset_Allocation_for_old_engine_backup_09204826(){
		super();
		StrategyID=4826L;
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
	private Long PlanID;
	public void setPlanID(Long PlanID){
		this.PlanID=PlanID;
	}
	
	public Long getPlanID(){
		return this.PlanID;
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
		MainAssetClass=(String[])ParameterUtil.fetchParameter("String[]","MainAssetClass", "US EQUITY, INTERNATIONAL EQUITY, FIXED INCOME, REAL ESTATE, COMMODITIES, Emerging Market, INTERNATIONAL BONDS, High Yield Bond, CASH, BALANCE FUND", parameters);
		NumberOfMainRiskyClass=(Integer)ParameterUtil.fetchParameter("int","NumberOfMainRiskyClass", "2", parameters);
		NumberOfMainStableClass=(Integer)ParameterUtil.fetchParameter("int","NumberOfMainStableClass", "1", parameters);
		RiskProfile=(Double)ParameterUtil.fetchParameter("double","RiskProfile", "20", parameters);
		LetRiskProfileChange=(Boolean)ParameterUtil.fetchParameter("boolean","LetRiskProfileChange", "false", parameters);
		RiskProfileGroup=(double[])ParameterUtil.fetchParameter("double[]","RiskProfileGroup", "12,15,16,17,24,30,38,48,51,56,78,80,90", parameters);
		YearsToRetireGroup=(double[])ParameterUtil.fetchParameter("double[]","YearsToRetireGroup", "40,35,30,25,20,15,10,5,0,-5,-10,-12,-15", parameters);
		CheckDate=(Integer)ParameterUtil.fetchParameter("int","CheckDate", "31", parameters);
		MinimumBuyingPercentage=(Double)ParameterUtil.fetchParameter("double","MinimumBuyingPercentage", "0.03", parameters);
		MinimumSellingPercentage=(Double)ParameterUtil.fetchParameter("double","MinimumSellingPercentage", "0.03", parameters);
		PlanID=(Long)ParameterUtil.fetchParameter("Long","PlanID", "0", parameters);
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
int[] TooVolatile;
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
		stream.writeObject(TooVolatile);
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
		TooVolatile=(int[])stream.readObject();;
		NewYear=(Boolean)stream.readObject();;
		LastActionMonth=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	private double version=1.0;
@SuppressWarnings("unchecked")
@Override
public void reinit(com.lti.service.bo.Portfolio portfolio, Hashtable<String, String> parameters, Date pdate) throws Exception {
                 super.reinit(portfolio,parameters,pdate);
                printToLog("--------------  Re-initial to version " + version +  " :  "+ CurrentDate + "   ----------------");
  
    LastActionMonth = -1;    // to let NewYear to be false  when calculated in the common action
    NewYear = false;

//  Delete Duplicate Candidates
Object[] CandidateObject = DeleteDuplicateCandidate(CandidateFund, RedemptionLimit, WaitingPeriod, RoundtripLimit);
CandidateFund = (String[])CandidateObject[0];
RedemptionLimit = (int[])CandidateObject[1];
RoundtripLimit = (int[])CandidateObject[2];
WaitingPeriod = (int[])CandidateObject[3];

InAfterDateFilter = new int[CandidateFund.length];
TooVolatile = new int[CandidateFund.length];

//   Choose the available candidate funds LJF Changed calculation of MPT 2010.03.16
Object[] AvailableObject = ChooseAvailableCandidate(CandidateFund, NewYear, InAfterDateFilter, TooVolatile);
List<String> CandidateFundList = (ArrayList<String>)AvailableObject[0];
List<String> ShortHistoryFundList = (ArrayList<String>)AvailableObject[1];
InAfterDateFilter = (int[])AvailableObject[2];
TooVolatile = (int[])AvailableObject[3];

                printToLog("--------------  Re-initial Finish, TAA strategy update to version " + version + " -------------------");
}

//==========================================================

/*Function to delete the duplicate candidate fund*/
public Object[] DeleteDuplicateCandidate(String[] CandidateFund, int[] RedemptionLimit, int[] WaitingPeriod, int[] RoundtripLimit)
 throws Exception {
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
int[] Temp4=WaitingPeriod;//Adj for Roundtrip Part 1 
int[] Temp5=RoundtripLimit;//Adj for Roundtrip Part 1

CandidateFund = new String[k];
RedemptionLimit = new int[k];
if (Temp4!=null ) WaitingPeriod=new int[k];//Adj for Roundtrip Part 1
if(Temp5!=null) RoundtripLimit=new int[k];//Adj for Roundtrip Part 1
k = 0;
for(int i = 0; i < Duplicate.length; i++)
{
    if(Duplicate[i] == 0)
    { 
        CandidateFund[k] = Temp1[i];
        if (Temp4!=null) WaitingPeriod[k]=Temp4[i]; //Adj for Roundtrip Part 1
        if (Temp5!=null) RoundtripLimit[k]=Temp5[i];//Adj for Roundtrip Part 1
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
	Object[] NewObject=new Object[4];
	NewObject[0]=CandidateFund;
	NewObject[1]=RedemptionLimit;
	NewObject[2]=RoundtripLimit;
	NewObject[3]=WaitingPeriod;
	return NewObject;
}

//================================================
/*Choose the available candidate funds LJF Changed calculation of MPT 2010.03.16*/

public Object[] ChooseAvailableCandidate(String[] CandidateFund, boolean NewYear, int[] InAfterDateFilter, int[] TooVolatile)
 throws Exception {
Security Fund = null;
Security Bench= null;
List<String> CandidateFundList = new ArrayList<String>();
List<String> ShortHistoryFundList = new ArrayList<String>();

Calendar cal = Calendar.getInstance(); // for calculation of MPT
cal.setTime(CurrentDate); //for calculation of MPT
long LastYear = cal.get(Calendar.YEAR)-1; //for calculation of MPT

int[] OldIn = InAfterDateFilter;
if(NewYear)
    TooVolatile = new int[CandidateFund.length];
//long sTime = System.currentTimeMillis();
boolean HaveCash = false;
double tempprice1; double tempprice2;
boolean BenchHasPrice = true;
List<Asset> HoldingAssetList =  CurrentPortfolio.getCurrentAssetList();
List<HoldingItem> HoldingSecurityList = null;
boolean isHolding = false;
for(int i=0; i< CandidateFund.length; i++)
{
    BenchHasPrice = true;
    if(CandidateFund[i].equals("CASH"))
        {CandidateFundList.add("CASH"); HaveCash = true; continue;}
    boolean PutThisFund=true;
    Fund = getSecurity(CandidateFund[i]);
    if(Fund == null) continue;
    if(CandidateFund[i].equals("SSSFX")) continue; /* There is price hole, which we can not deal with for now*/
    try{
        if(Fund.getIsClosed() && LTIDate.equals(Fund.getEndDate(),CurrentDate) )
            continue;
    }catch(Exception e3){printToLog(CandidateFund[i] + "'s EndDate is probably null");  continue;}
    try{Bench = getSecurity(Fund.getAssetClass().getBenchmarkID());}  
        catch(Exception e){continue;}             //Omit those funds that DO NOT Have ParentClassID
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
                    if(!NewYear && TooVolatile[i] == 1)
                        {PutThisFund = false; printToLog("Too Volatile ; exclude "+ CandidateFund[i]); continue;} 
		    if(NewYear || InAfterDateFilter[i] != OldIn[i] ){	
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
				{PutThisFund = false; TooVolatile[i] = 1;  printToLog("Too Volatile ; exclude "+ CandidateFund[i]); continue;} 
                        }
        }
        else
             printToLog("Benchmark's history prices is not long enough. " + CandidateFund[i] +" is included no matter of SD");

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
    //Special Treatment towards ShortHistoryFunds , Added by WYJ on 2010.05.13
    else if( LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), CurrentDate) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -12)) && BenchHasPrice)
    {
	if((getSecurity(CandidateFund[i])).getAssetClass()!=null)
             {
              if (CandidateFundList.indexOf(CandidateFund[i])==-1) CandidateFundList.add(CandidateFund[i]); 
              ShortHistoryFundList.add(CandidateFund[i]);
             }
    }

    if(i == CandidateFund.length -1 && HaveCash == false)
        {CandidateFundList.add("CASH"); HaveCash = true;}	
}

if(CandidateFundList.indexOf("CASH") == -1)
    CandidateFundList.add("CASH");
printToLog("CandidateFundList initialize ok");

	Object[] NewObject=new Object[4];
	NewObject[0]=CandidateFundList;
	NewObject[1]=ShortHistoryFundList;
	NewObject[2]=InAfterDateFilter;
	NewObject[3]=TooVolatile;
	return NewObject;
}

//=================================================
//  Divide the Asset Classes into Risky Group and Stable Group
public Object[] DivideAssetClass(HashMap<String,List<String>> AllAssetFundsMap, int NumberOfMainRiskyClass)
 throws Exception {
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

if(n1-n2 < NumberOfMainRiskyClass)  
    NumberOfMainRiskyClass = n1-n2;  //Avoid keep selecting stable assets in the risky group, added by WYJ on 2010.05.10

String[] RiskyAssets= new String[n1 - n2];
String[] StableAssets = new String[n2];
n1 = 0; n2 = 0;
for(int i = 0; i < AllAssetFundsMap.size(); i++)
{
    if(IndexOfStable[i] == 0)  {RiskyAssets[n1] = AvailableAssets[i]; n1++;}
    else  {StableAssets[n2] = AvailableAssets[i]; n2++;}
}
printToLog("AvailableAssetClassList initialize ok");

	Object[] NewObject=new Object[4];
        NewObject[0]=AvailableAssets;
	NewObject[1]=RiskyAssets;
	NewObject[2]=StableAssets;
	NewObject[3]=NumberOfMainRiskyClass;
	return NewObject;
}
//================================================

//   Calculate the momentum scores of benchmarks and select asset classes 
public Object[] SelectAssetClass(String[] AvailableAssets, String[] StableAssets, double CashScoreWeight, int SelectedPercentage, int NumberOfMainRiskyClass, int NumberOfMainStableClass)
 throws Exception {
String[] AssetBenchmarks = new String[AvailableAssets.length];
for(int i = 0; i < AvailableAssets.length; i++)
    AssetBenchmarks[i] = getSecurity(assetClassManager.get(AvailableAssets[i]).getBenchmarkID()).getSymbol();

double[] Score = new double[AvailableAssets.length];
Score = getMomentumScore(AssetBenchmarks, CashScoreWeight, CurrentDate);
for(int i = 0; i < AvailableAssets.length; i++)
    printToLog(AvailableAssets[i] + "'s score = " + Score[i]);

//Risky Group selection
List<String> RiskyGroupSelect = getSelectedAssetList(Score, AvailableAssets, StableAssets, SelectedPercentage, NumberOfMainRiskyClass, NumberOfMainStableClass, CurrentDate);
printToLog("Risky Group selection on " + CurrentDate.toString()+"  =  "  + RiskyGroupSelect);

//Stable Group selection
for(int i = 0; i < AvailableAssets.length; i++)
    if(AvailableAssets[i].equals("FIXED INCOME") || AvailableAssets[i].equals("INTERNATIONAL BONDS") || AvailableAssets[i].equals("CASH"))
       continue;
    else
        Score[i] = -10000;

List<String> StableGroupSelect = getSelectedAssetList(Score, AvailableAssets, StableAssets, SelectedPercentage, NumberOfMainRiskyClass, NumberOfMainStableClass, CurrentDate);
printToLog("Stable Group selection on " + CurrentDate.toString()+"  =  "  + StableGroupSelect);

	Object[] NewObject=new Object[2];
        NewObject[0]=RiskyGroupSelect;
	NewObject[1]=StableGroupSelect;
	return NewObject;
}
//================================================

//    Add CASH into the stable asset class, Add duplicate safe stable funds into the stable classes, modified by WYJ on 2010.05.10 
public HashMap<String,List<String>> AppendStableClass(HashMap<String,List<String>> AllAssetFundsMap, List<String> StableGroupSelect)
 throws Exception {
    List<String> TempFundList = null;
    List<String> OriginalFundList = null;
    List<String> Class2Times = new ArrayList<String>();
    List<String> Class3Times = new ArrayList<String>();
    Class3Times.add("INVESTMENT GRADE");
    Class3Times.add("Short-Term Bond");
    Class3Times.add("ULTRASHORT BOND");
    Class3Times.add("SHORT GOVERNMENT");
    Class2Times.add("Intermediate-Term Bond");
    Class2Times.add("Intermediate Government");

    for(int i = 0; i < StableGroupSelect.size(); i++)
    {    
         TempFundList = new ArrayList<String>();  
         OriginalFundList = new ArrayList<String>();  
         OriginalFundList =  AllAssetFundsMap.get(StableGroupSelect.get(i).trim());
         for(int j = 0; j <  OriginalFundList.size(); j++)
             TempFundList.add(OriginalFundList.get(j));
         if( OriginalFundList != null)
         {
             for(int j = 0; j <  OriginalFundList.size(); j++)
             {
                 if(Class2Times.indexOf(getSecurity( OriginalFundList.get(j)).getAssetClass().getName()) != -1)
                     TempFundList.add( OriginalFundList.get(j));
                 else if(Class3Times.indexOf(getSecurity( OriginalFundList.get(j)).getAssetClass().getName()) != -1)
                 {
                     TempFundList.add( OriginalFundList.get(j));
                     TempFundList.add( OriginalFundList.get(j));
                 }
             }
             TempFundList.add("CASH");
             TempFundList.add("CASH");
             TempFundList.add("CASH");
             AllAssetFundsMap.put(StableGroupSelect.get(i).trim(), TempFundList);
         }
    } 
return AllAssetFundsMap;    
}

//=================================================

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

printToLog("newArray1 : " + newArray1);
printToLog("newArray2 : " + newArray2);
printToLog("newArray3 : " + newArray3);
printToLog("newArray4 : " + newArray4);

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

 printToLog("Original Score: "+Securities[i]+" : "+a);

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
        {bp1=1; printToLog(Securities[i] + " has bonus (1)");break; }
     else
        bp1=0;
    }
}


for(int j=0; j<=newArray2.length-1; j++)
{
    if(Securities[i].equals(newArray2[j]))
   { 
     if(j<=score.length/2-1)
        {bp2=1; printToLog(Securities[i] + " has bonus (2)");break; }
     else
        bp2=0;
   }
}


for(int j=0; j<=newArray3.length-1; j++)
{
    if(Securities[i].equals(newArray3[j]))
    { 
     if(j<=score.length/2-1)
        {bp3=1; printToLog(Securities[i] + " has bonus (3)");break; }
     else
        bp3=0;
    }
}


for(int j=0; j<=newArray4.length-1;j++)
{
 if(Securities[i].equals(newArray4[j]))
     { 
      if(j<=score.length/2-1)
        {bp4=1; printToLog(Securities[i] + " has bonus (4)");break; }
      else
         bp4=0;
     }
}
score[i]=a+bp1+bp2+bp3+bp4;
printToLog("Final Score: "+Securities[i]+" : "+score[i]);
}
else
    score[i]=-100000;
}


return score;
}

//====================================================

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
        public void writelog(String mes)  throws Exception {

                   FileOperator.appendMethodA("/usr/apache-tomcat-6.0.16/webapps/LTISystem/taa.txt",mes);

        }
        public void printToLog(String mes)// throws Exception {
{
                 try{
                  // writelog(mes);
              } catch(Exception e){}
                   super.printToLog(mes);
        }

//==========================================================
//==========================================================
//Adj for Roungtrip Part 0 LJF and adj for 529(actually Initial for getParameter Part)
		private int[] RoundtripLimit;
		private int[] WaitingPeriod;
		private boolean is529Plan;
                private boolean rewriteData;
		private void initPlanParameters(){
			Strategy _plan=com.lti.system.ContextHolder.getStrategyManager().get(PlanID);
                         List<Pair> attrs=null;
                        if(_plan!=null)
                                   attrs=_plan.getAttributes();
			Map<String,String> tmp=new HashMap<String,String>();
			if (attrs!=null)
                            for(Pair p:attrs){
				tmp.put(p.getPre(),p.getPost());
			    }
                        else {RoundtripLimit=null;WaitingPeriod=null;is529Plan=false;rewriteData=false;return;}
			
			String[] tmpRoundtrip=null;
			if(tmp.get("RoundtripLimit")!=null) tmpRoundtrip=tmp.get("RoundtripLimit").split(",");
			if (tmpRoundtrip!=null && tmpRoundtrip.length==CandidateFund.length){
				RoundtripLimit=new int[CandidateFund.length];
				for(int i=0;i<tmpRoundtrip.length;i++) {
					RoundtripLimit[i]=Integer.parseInt(tmpRoundtrip[i]);
				}
			}else {RoundtripLimit=null;}
			
			String[] tmpWaiting=null;
			if(tmp.get("RoundtripLimit")!=null) tmpRoundtrip=tmp.get("WaitingPeriod").split(",");
			if(tmpWaiting!=null && tmpWaiting.length==CandidateFund.length){
				WaitingPeriod=new int[CandidateFund.length];
				for (int i=0;i<tmpWaiting.length;i++){
					WaitingPeriod[i]=Integer.parseInt(tmpWaiting[i]);
				}
			}else {WaitingPeriod=null;}	

			if(tmp.get("is529Plan")!=null && tmp.get("is529Plan").trim().equalsIgnoreCase("true")){
				is529Plan=true;
				printToLog("this portfolio belongs to a 529 college saving plan~compareTargetAndCurrentHolding Function will be activated~~");
				}
			else{is529Plan=false;}

			if(tmp.get("rewriteData")!=null && tmp.get("rewriteData").trim().equalsIgnoreCase("true")){
				rewriteData=true;
				}
			else{rewriteData=false;}
        }

//==========================================

//    Calculate CanNotBuyFundList for Roundtrip limitation and WaitingPeriod limitation
//Adj for RoundTrip Part 2 LJF 2010.07.02 BEGIN
public List<String> ExcludeByRoundTripAndWaiting(List<String> CandidateFundList, String[] CandidateFund, int[] RoundtripLimit, int[] WaitingPeriod )
 throws Exception {
printToLog("Enter Roundtrip Fundction Part 2");

List<String> CanNotBuyFundList=new ArrayList<String>();
List<String> NeedTestFundList=new ArrayList<String>();
NeedTestFundList=CandidateFundList;     /*Attention: just deliver the address*/
String[] DuplicateFund=new String[NeedTestFundList.size()];
int[] NoOfRoundtrip=new int[DuplicateFund.length];
int[] RecordArray= new int[DuplicateFund.length];
int[] AppendArray= new int[DuplicateFund.length];

for (int ifund=0;ifund<NeedTestFundList.size();ifund++)
	{NoOfRoundtrip[ifund]=0;RecordArray[ifund]=0;DuplicateFund[ifund]=NeedTestFundList.get(ifund);}
/*2.1 Detect CandidateFund, judge whether Roundtrip && waitingPeriod Limitation works for the portfolio or not*/
boolean NeedToCalRoundtrip=false;
boolean NeedToCalWaitingPeriod=false;
if (RoundtripLimit!=null) {
	for(int ifund=0;ifund<RoundtripLimit.length;ifund++)
		{if (RoundtripLimit[ifund]!=13){NeedToCalRoundtrip=true;break;}}
} //Default Roundtrip Limit is 13 times during latest 12 months
if (WaitingPeriod!=null) {
	for(int ifund=0;ifund<WaitingPeriod.length;ifund++)
		{if (WaitingPeriod[ifund]!=0){NeedToCalWaitingPeriod=true;break;}}
}//Default WaitingPeriod is zero month(immediately) before a fund is bought back.

/*2.2 Tag CanNotBuyFund due to Roundtrip Limit */
/*2.2.1 Calculate No. of Roundtrips of CandidateFund in last 12 months*/
if (NeedToCalRoundtrip){
for (int imonth=12;imonth>0;imonth--)
{
	Date startDate=LTIDate.getNewDate(CurrentDate,TimeUnit.MONTHLY,-imonth);
	Date endDate=LTIDate.getNewDate(startDate, TimeUnit.MONTHLY,1 );
	AppendArray=CurrentPortfolio.transactionDetection(startDate,endDate,DuplicateFund); //(startDate,endDate,String[])
	for (int ifund=0;ifund<DuplicateFund.length;ifund++){
	if (RecordArray[ifund]==2 && AppendArray[ifund]==1) {NoOfRoundtrip[ifund]+=1;RecordArray[ifund]=0;}
	if (RecordArray[ifund]==1 && AppendArray[ifund]==2) {RecordArray[ifund]=2;}
	if (RecordArray[ifund]==0) {RecordArray[ifund]=AppendArray[ifund];}
	//Notice: assume Cash has no WaitingPeriod and RoundtripLimit restriction.
	if (AppendArray[ifund]==3 && !DuplicateFund[ifund].equals("CASH")) {printToLog("buy and sell transactions of the same fund occured within one month during &2.2-Calculate No. of Roundtrips in last 12 months "+":  "+DuplicateFund[ifund]+". StartDate: "+startDate+", EndDate: "+endDate);}
	}
}
/*2.2.2Tag funds*/
for (int ifund=0;ifund<DuplicateFund.length;ifund++)
{
	int FundIndex=CandidateFund.length;
	for (int itemp=0;itemp<CandidateFund.length;itemp++)
	{if (CandidateFund[itemp].equals(DuplicateFund[ifund])) {FundIndex = itemp;break;} }
	int iRoundtripLimit=RoundtripLimit[FundIndex];// get RoundtripLimit of the fund

	if ( NoOfRoundtrip[ifund]>=iRoundtripLimit  && CanNotBuyFundList.indexOf(DuplicateFund[ifund])==-1)
	{CanNotBuyFundList.add(DuplicateFund[ifund]);}
}
/*LJF Temp Testing*/printToLog("CanNotBuyFund due to RoundtripLimit: "+CanNotBuyFundList);
}

/*2.3 Tag CanNotBuyFund due to WaitingPeriod */
if (NeedToCalWaitingPeriod){
for (int ifund=0;ifund<DuplicateFund.length;ifund++)
{
	int FundIndex=CandidateFund.length;
	for (int itemp=0;itemp<CandidateFund.length;itemp++)
	{if (CandidateFund[itemp].equals(DuplicateFund[ifund])) {FundIndex = itemp;break;} }
	int iWaitingPeriod =WaitingPeriod[FundIndex];// get WaitingPeriod of the fund
	Date startDate=LTIDate.getNewDate(CurrentDate,TimeUnit.MONTHLY,-iWaitingPeriod);
	String[] iDuplicateFund=new String[1]; iDuplicateFund[0]=DuplicateFund[ifund];
	int TransType =CurrentPortfolio.transactionDetection(startDate,CurrentDate,iDuplicateFund)[0];
if (TransType==3) printToLog("LJF Important Notice!!! startDate: "+startDate+", CurrentDate: "+CurrentDate+",iDuplicateFund: "+iDuplicateFund[0]+", TransType: "+TransType);

	if (TransType>=2 && CanNotBuyFundList.indexOf(DuplicateFund[ifund])==-1)
	{CanNotBuyFundList.add(DuplicateFund[ifund]);}
}
}
printToLog("CanNotBuyFundList is "+CanNotBuyFundList);
//Adj for RoundTrip Part 2 LJF 2010.07.02 END

return CanNotBuyFundList;
}
//==========================================

//   Adjust FundPercentageMap for CanNotBuyFundList, caused by RoundTrip and Waiting Period
//Adj for RoundTrip Part 4 LJF 2010.07.02 BEGIN
public Object[] AdjustTargetPercentage(List<String> CanNotBuyFundList, List<Double> SelectedAssetPercentages, HashMap<String,Double> FundPercentageMap, HashMap<String, List<String>> AssetRepresentFundMap, List<String> SelectedAssets, List<String> MainAssetClassList)
 throws Exception {
    printToLog("Enter RoundTrip Function Part 4");
    /*LJF temp testing*/printToLog("AssetRepresentFundMap 2:"+AssetRepresentFundMap);
	                               printToLog("LJF FundPercentageMap before Roundtrip Adj: "+FundPercentageMap);
        List<Asset> HoldingAssetList =  CurrentPortfolio.getCurrentAssetList();
        List<String> AvailableAssetList = new ArrayList<String>();
        List<String> RiskyAssets = new ArrayList<String>();
        List<String> StableAssets = new ArrayList<String>();
        
        for(int i = 0; i < HoldingAssetList.size(); i++)
            AvailableAssetList.add(HoldingAssetList.get(i).getName().trim());
        for(int i = 0; i < SelectedAssets.size(); i++)
            if(AvailableAssetList.indexOf(SelectedAssets.get(i)) == -1)
                AvailableAssetList.add(SelectedAssets.get(i));
        
        for(int i = 0; i < AvailableAssetList.size(); i++)
        {
            String TempClass = AvailableAssetList.get(i);
            if(TempClass.equals("FIXED INCOME") || TempClass.equals("INTERNATIONAL BONDS") || TempClass.equals("CASH"))
               StableAssets.add(TempClass);
            else
               RiskyAssets.add(TempClass);
        }

	/*4.1 For funds in CanNotBuyFundList, Adj FundPercentageMap and SelectedAssetPercentages */
	List<String> CanNotAdjRiskyAssetList=new ArrayList<String>();List<String> CanNotAdjStableAssetList=new ArrayList<String>();
	double UnAllocateRiskyPercentage=0;double UnAllocateStablePercentage=0;
	List<String> NeedTestAssetList=new ArrayList<String>();
        List<String> RepresentFundList = null;
        Map<String, List<String>> tmpMap = null; 

        tmpMap = getAvailableAssetClassSet(MainAssetClassList, CanNotBuyFundList);
        Iterator<String> it = tmpMap.keySet().iterator();
        while (it.hasNext()) {
            String asset = it.next();
            NeedTestAssetList.add(asset);
        }
/*     not right !
	for (int ifund=0;ifund<CanNotBuyFundList.size();ifund++)
	{
		String AssetName=getSecurity(CanNotBuyFundList.get(ifund)).getAssetClass().getName(); 
		for (int NameIndex=0;NameIndex<MainAssetClassList.size();NameIndex++)   // May use getAllAssetFundMap API
		{
			if (isUpperOrSameClass(MainAssetClassList.get(NameIndex),AssetName))
			 {AssetName=MainAssetClassList.get(NameIndex);break;}
		}
		if ( NeedTestAssetList.indexOf(AssetName)==-1 ) {NeedTestAssetList.add(AssetName);}
	}
*/	
	for(int iasset=0;iasset<NeedTestAssetList.size();iasset++)
	{
		printToLog("LJF NeedTestAsset: "+NeedTestAssetList.get(iasset));
		String AssetName=NeedTestAssetList.get(iasset);
		int NoOfNormalFunds=0;
		double LeisurePercentageCollect=0;
		RepresentFundList= new ArrayList<String>();
		RepresentFundList=AssetRepresentFundMap.get(AssetName);
		//4.1.1 If NeedTestAsset has no RepresentFund
		if (RepresentFundList==null || RepresentFundList.size()==0)     // might not enter this "if"
		{			
			int iAssetName=SelectedAssets.indexOf(AssetName);
			if(iAssetName > -1)
			{
				LeisurePercentageCollect=SelectedAssetPercentages.get(iAssetName);
				SelectedAssetPercentages.set(iAssetName,0.0);
				printToLog("LJF SelectedAssetPercentages.set("+AssetName+")=0");
				int RiskyAssetIndex=-1;
				for (int itemp=0;itemp<RiskyAssets.size();itemp++)
					{if (RiskyAssets.get(itemp).equals(AssetName)) {RiskyAssetIndex = itemp;break;} }
				if (RiskyAssetIndex> -1) 
					{UnAllocateRiskyPercentage+=LeisurePercentageCollect;CanNotAdjRiskyAssetList.add(AssetName);}
				else 
				{UnAllocateStablePercentage+=LeisurePercentageCollect;CanNotAdjStableAssetList.add(AssetName);}
			}
			continue;
		}
		//4.1.2 If NeedTestAsset RepresentFund!= null
		//First, Adj CanNotBuy Funds' value in the FundPercentageMap && Accumulate LeisurePercentageCollect
		for (int ifund=0;ifund<RepresentFundList.size();ifund++)
		{
			String FundName=RepresentFundList.get(ifund);
			if(CanNotBuyFundList.indexOf(FundName)== -1) {NoOfNormalFunds+=1;}
			else 
			{
                                double ActualPercentage = CurrentPortfolio.getSecurityAmount(AssetName,FundName,CurrentDate)/CurrentPortfolio.getTotalAmount(CurrentDate);
				if (ActualPercentage < FundPercentageMap.get(FundName)) // Detect for CanNotBuy Funds that may need for Buy Rebalance Action
				{
					double TempPercentage=FundPercentageMap.get(FundName);
					LeisurePercentageCollect+=(TempPercentage-ActualPercentage);
					//FundPercentageMap.remove(FundName);
					FundPercentageMap.put(FundName,ActualPercentage);
					printToLog("LJF New FundPercentageMap RoundtripLimit adj 1 "+FundName+", New Percentage: "+ActualPercentage);
				}
				else {printToLog("LJF Important Notice: CurrentHolding has this CanNotBuyFund "+FundName+", but its actual percentage"+ ActualPercentage +" > Target percentage "+FundPercentageMap.get(FundName));}
			}
		}
		//Second, Allocate LeisurePercentageCollect as much as possible among other funds under the same asset class
		if (NoOfNormalFunds != 0 && LeisurePercentageCollect > 0)
		{
			double AddOn = LeisurePercentageCollect/NoOfNormalFunds;
			for(int ifund=0;ifund<RepresentFundList.size();ifund++)
			{
			String FundName=RepresentFundList.get(ifund);
			if (CanNotBuyFundList.indexOf(FundName)== -1)
				{
				double TempPercentage=FundPercentageMap.get(FundName);
				//FundPercentageMap.remove(FundName);
				FundPercentageMap.put(FundName,TempPercentage+AddOn);
				printToLog("LJF New FundPercentageMap RoundtripLimit adj 2 "+FundName+", New Percentage: "+(TempPercentage+AddOn));
				}
			}
		}
		//Third, for CanNotAdjAsset: Record LeisurePercentageCollect;Add to UnAllocateRisky/StableAssetList
		if (NoOfNormalFunds == 0)
		{
			if (LeisurePercentageCollect > 0)
			{
			int iAssetName=SelectedAssets.indexOf(AssetName);
			double Percent=SelectedAssetPercentages.get(iAssetName);
			SelectedAssetPercentages.set(iAssetName,Percent-LeisurePercentageCollect);
			printToLog("LJF Important Notice: CanNotAdjAsset && RepresentFundList!=null && RepresentFunds are CanNotBuyFund "+AssetName+", RepresentFundList: "+RepresentFundList);
			}
			int RiskyAssetIndex=-1;
			for (int itemp=0;itemp<RiskyAssets.size();itemp++)
				{if (RiskyAssets.get(itemp).equals(AssetName)) {RiskyAssetIndex = itemp;break;} }
			if (RiskyAssetIndex> -1) 
				{UnAllocateRiskyPercentage+=LeisurePercentageCollect;CanNotAdjRiskyAssetList.add(AssetName);}
			else 
				{UnAllocateStablePercentage+=LeisurePercentageCollect;CanNotAdjStableAssetList.add(AssetName);}
		}
		
	}
	
	//4.2 Allocate UnAllocateRiskyPercentage && UnAllocateStablePercentage
	//4.2.1 Detect other Assets belonging to CanNotAdjRisky/StableAssetList, treat the un-selected assets as CanNotAdjAsset, we won't buy them
	for(int iasset=0;iasset<RiskyAssets.size();iasset++)
	{
		if (AssetRepresentFundMap.get(RiskyAssets.get(iasset))==null && CanNotAdjRiskyAssetList.indexOf(RiskyAssets.get(iasset))==-1) CanNotAdjRiskyAssetList.add(RiskyAssets.get(iasset));
	}
	for(int iasset=0;iasset<StableAssets.size();iasset++)
	{
		if (AssetRepresentFundMap.get(StableAssets.get(iasset))==null && CanNotAdjStableAssetList.indexOf(StableAssets.get(iasset))==-1) CanNotAdjStableAssetList.add(StableAssets.get(iasset));
	}
	CanNotAdjStableAssetList.add("CASH");//This special treatment towards CASH aims to allocate UnAllocateStablePercentage to StableAssets other than CASH. If No such other StableAsset, the percentage is then allocated to CASH.
/*
printToLog("SelectedAssets: "+SelectedAssets);
printToLog("CanNotAdjRiskyAssetList:"+CanNotAdjRiskyAssetList);
String PrintLog="StableAssets: ";
for(int FI=0;FI<StableAssets.length;FI++){PrintLog+=StableAssets[FI];}
printToLog(PrintLog);
printToLog("CanNotAdjStableAssetList:"+CanNotAdjStableAssetList);
*/
	
	/*4.2.2 LeisurePercentageCollect Adj among Assets*/
        int CanAdjNumber = RiskyAssets.size();
        for(int i = 0; i< RiskyAssets.size(); i++)
            if(CanNotAdjRiskyAssetList.indexOf(RiskyAssets.get(i)) != -1)
                CanAdjNumber -= 1;

	if (UnAllocateRiskyPercentage > 0 && CanAdjNumber > 0)
	{
		double AddOn=UnAllocateRiskyPercentage/CanAdjNumber;
		for (int iasset=0;iasset<RiskyAssets.size();iasset++)
		{
			String AssetName=RiskyAssets.get(iasset);
			if (CanNotAdjRiskyAssetList.indexOf(AssetName)==-1)
			{
				RepresentFundList= new ArrayList<String>();		
				RepresentFundList=AssetRepresentFundMap.get(AssetName);
				if(RepresentFundList!=null && RepresentFundList.size()!=0)
				{					
					int NoOfNormalFund=0;
					for(int ifund=0;ifund<RepresentFundList.size();ifund++)
					{int Index=CanNotBuyFundList.indexOf(RepresentFundList.get(ifund));	if (Index == -1) NoOfNormalFund+=1;}
					
					for (int ifund=0;ifund<RepresentFundList.size();ifund++)
						if (CanNotBuyFundList.indexOf(RepresentFundList.get(ifund))==-1)
						{
						String FundName=RepresentFundList.get(ifund);
						double TempPercentage=FundPercentageMap.get(FundName);
						//FundPercentageMap.remove(FundName);
						FundPercentageMap.put(FundName,TempPercentage+AddOn/NoOfNormalFund);
						printToLog("LJF New FundPercentageMap RoundtripLimit adj 3 "+FundName+", New Percentage: "+(TempPercentage+AddOn/NoOfNormalFund));
						}
					UnAllocateRiskyPercentage-=AddOn;
				
					int iAssetName=SelectedAssets.indexOf(AssetName);
					double Percent=SelectedAssetPercentages.get(iAssetName);
					SelectedAssetPercentages.set(iAssetName,Percent+AddOn);
				} else 	{printToLog("An Error occured during &4.2 LeisurePercentageCollect Adj among Assets. Suspicious Asset:"+AssetName+",  AddOn Amount:"+AddOn);}
			}
		}
	}
	if (UnAllocateRiskyPercentage >0)
	{
	printToLog("LJF Important Notice: Additional RiskyAssetPercentage is allocated to StableAssets, cause RiskProfile not match!!!The additional Percentage ="+UnAllocateRiskyPercentage);
	UnAllocateStablePercentage+=UnAllocateRiskyPercentage;
	}
	
        for(int i = 0; i< StableAssets.size(); i++)
            if(CanNotAdjStableAssetList.indexOf(StableAssets.get(i)) != -1)
                CanAdjNumber -= 1;
	if (UnAllocateStablePercentage > 0 && CanAdjNumber > 0)
	{
		double AddOn=UnAllocateStablePercentage / CanAdjNumber;
		for (int iasset=0;iasset<StableAssets.size();iasset++)
		{
			String AssetName=StableAssets.get(iasset);
			if (CanNotAdjStableAssetList.indexOf(AssetName)==-1)
			{
				RepresentFundList= new ArrayList<String>();		
				RepresentFundList=AssetRepresentFundMap.get(AssetName);
				if(RepresentFundList!=null && RepresentFundList.size()!=0)
				{
					int NoOfNormalFund=0;
					for(int ifund=0;ifund<RepresentFundList.size();ifund++)
					{int Index=CanNotBuyFundList.indexOf(RepresentFundList.get(ifund));	if (Index == -1) NoOfNormalFund+=1;}
					for (int ifund=0;ifund<RepresentFundList.size();ifund++)
						if (CanNotBuyFundList.indexOf(RepresentFundList.get(ifund))==-1)
						{
						String FundName=RepresentFundList.get(ifund);
						double TempPercentage=FundPercentageMap.get(FundName);
						//FundPercentageMap.remove(FundName);
						FundPercentageMap.put(FundName,TempPercentage+AddOn/NoOfNormalFund);
						printToLog("LJF New FundPercentageMap RoundtripLimit adj 4 "+FundName+", New Percentage: "+(TempPercentage+AddOn/NoOfNormalFund));
						}

					UnAllocateStablePercentage-=AddOn;
					int iAssetName=SelectedAssets.indexOf(AssetName);
					double Percent=SelectedAssetPercentages.get(iAssetName);
					SelectedAssetPercentages.set(iAssetName,Percent+AddOn);
				}else {printToLog("An Error occured during &4.2 LeisurePercentageCollect Adj among Assets. Suspicious Asset:"+AssetName+",  AddOn Amount:"+AddOn);}
			}
		}
	}
	if (UnAllocateStablePercentage > 0)
	{
	double TempPercentage=FundPercentageMap.get("CASH");
	//FundPercentageMap.remove("CASH");
	FundPercentageMap.put("CASH",TempPercentage+UnAllocateStablePercentage);
	double Percent = SelectedAssetPercentages.get(SelectedAssets.indexOf("CASH"));
	SelectedAssetPercentages.set(SelectedAssets.indexOf("CASH"),Percent+UnAllocateStablePercentage);        
	}
	//Adj for RoundTrip Part 4 LJF 2010.07.02 END
	printToLog("LJF FundPercentageMap after Roundtrip Adj: "+FundPercentageMap);

Object[] NewObject=new Object[2];
NewObject[0] = SelectedAssetPercentages;
NewObject[1] = FundPercentageMap;
return NewObject;
}

//========================================





//==========================================
//Adj for 529 college saving plan compareTargetAndHoldingAssets Function
public Object[] compareTargetAndHoldingAssets(String[] DuplicateFund,List<String> TargetAssets,List<Double>TargetAssetPercentages,HashMap<String,Double>TargetFundPercentageMap, List<String> MainAssetClassList) throws Exception {
//1.Judge whether there are already sell/buy signals during this year, if the answer is Yes, goto 4;
//2.Decide whether to trigger the Rebalance for this year NOW, if the answer is NO, goto 4;
//3.Make the Reblance, with FundPercentageMap slightly changed. return;
//4.Endow the FundPercentageMap with CurrentSecurityPercentage's value. return;
	 
        printToLog("Enter 529 function");
	List<String> NewTargetAssets=new ArrayList<String>();
	List<Double>NewTargetAssetPercentages=new ArrayList<Double>();
	HashMap<String,Double> NewFundPercentageMap=new HashMap<String,Double>();
	HashMap<String,List<String>>NewAssetRepresentFundMap=new HashMap<String,List<String>>();
	
	boolean AlreadyAdjThisYear=false;
	boolean NeedToAdjNow=false;
	/*LJF temp testing*/printToLog("TargetAssets:"+TargetAssets);
	/*LJF temp testing*/printToLog("TargetAssetPercentages:"+TargetAssetPercentages);
	//1.
	Date startDate=LTIDate.getFirstNYSETradingDayOfYear(CurrentDate);
	int[] DetectTransArray=CurrentPortfolio.transactionDetection(startDate,CurrentDate,DuplicateFund); //(startDate,endDate,String[])
	for (int Index=0;Index<DetectTransArray.length;Index++){
		if (DetectTransArray[Index]!=0){
		AlreadyAdjThisYear=true;
		break;
		}
	}
	
	//2.
	if (!AlreadyAdjThisYear){
		//Initialize the TriggerNo, this initial number will absolutely trigger NeedToAdjNow
		int TriggerNo=MaxOfRiskyAsset;
		double JudgeWhetherLastTransactionIsType_2Trigger=0.5;
		
		//get Current holding Risky Assets
		List<String>CurrRiskyAssetNameList=new ArrayList<String>();
		double CurrRiskyAmount=0;
		List<Asset> CurrentAssetList =CurrentPortfolio.getCurrentAssetList();
		for(int iAsset=0;iAsset<CurrentAssetList.size();iAsset++){
			String AssetName=CurrentAssetList.get(iAsset).getName();
			double AssetAmount=CurrentPortfolio.getAssetAmount(AssetName,CurrentDate);
			if(!AssetName.equals("FIXED INCOME") && !AssetName.equals("INTERNATIONAL BONDS") && !AssetName.equals("CASH") && AssetAmount>0){
				CurrRiskyAmount+=AssetAmount;
				CurrRiskyAssetNameList.add(AssetName);
			}
		}
/*LJF temp testing*/printToLog(CurrRiskyAmount/CurrentPortfolio.getTotalAmount(CurrentDate));
/*LJF temp testing*/printToLog((100-RiskProfile)/100*JudgeWhetherLastTransactionIsType_2Trigger*CurrRiskyAssetNameList.size()/MaxOfRiskyAsset);
		if (CurrRiskyAmount>0 &&CurrRiskyAmount/CurrentPortfolio.getTotalAmount(CurrentDate) < (100-RiskProfile)/100*JudgeWhetherLastTransactionIsType_2Trigger*CurrRiskyAssetNameList.size()/MaxOfRiskyAsset) /*In this case, ignore current risky assets because it implies the last transaction is Type 2*/
			{CurrRiskyAssetNameList=null;printToLog("During this action, we found that Last Transaction is Type 2-A combination of TargetAssets and CurrentAssets");}
		
		//get Target Risky Assets
		List<String> TargetRiskyAssetNameList=new ArrayList<String>();
		for(int iAsset=0;iAsset<TargetAssets.size();iAsset++){
			String AssetName=TargetAssets.get(iAsset);
			if(!AssetName.equals("FIXED INCOME") && !AssetName.equals("INTERNATIONAL BONDS") && !AssetName.equals("CASH")){
				TargetRiskyAssetNameList.add(AssetName);
			}
		}
		
		if( CurrRiskyAssetNameList==null || CurrRiskyAssetNameList.size()==0)
			{TriggerNo=TargetRiskyAssetNameList.size();}
		else if (TargetRiskyAssetNameList==null || TargetRiskyAssetNameList.size()==0 ){TriggerNo=CurrRiskyAssetNameList.size();}
			else {
				for(int iAsset=0;iAsset<CurrRiskyAssetNameList.size();iAsset++){
					String AssetName=CurrRiskyAssetNameList.get(iAsset);
					if (TargetRiskyAssetNameList.indexOf(AssetName)>-1)
						{TriggerNo-=1;}
				}
			}
		/*LJF temp testing*/printToLog("TriggerNo="+TriggerNo+"; TargetRiskyAssetNameList:"+TargetRiskyAssetNameList+"; CurrRiskyAssetNameList:"+CurrRiskyAssetNameList);
		Date HalfOfYear=LTIDate.getDate(LTIDate.getYear(CurrentDate),7,1);
		Date ForceDate = null;
                if(CheckFrequency.equals("monthly"))
                    ForceDate = LTIDate.getDate(LTIDate.getYear(CurrentDate),11,1);
                if(CheckFrequency.equals("quarterly"))
                    ForceDate = LTIDate.getDate(LTIDate.getYear(CurrentDate),9,1);
                if(CheckFrequency.equals("yearly"))
                    ForceDate = LTIDate.getDate(LTIDate.getYear(CurrentDate),1,1);
		if ((TriggerNo>MaxOfRiskyAsset/2.0 && LTIDate.before(CurrentDate,HalfOfYear)) || (TriggerNo>=MaxOfRiskyAsset/3.0 && LTIDate.before(HalfOfYear,CurrentDate)) || (LTIDate.before(ForceDate,CurrentDate)) )
			{NeedToAdjNow=true;}	
	}
	
	//3.
	if(NeedToAdjNow){
		//Modify NewFundPercentageMap to a mixture of CurrentHolding and TargetFund; Modify TargetAssets/TargetAssetPercentages to fit NewFundPercentageMap;
		printToLog("Do NeedToAdjNow: 529 college plan "+CurrentDate);
		boolean HasRiskyAsset=false;
		for(int iAsset=0;iAsset<TargetAssets.size();iAsset++){
			String AssetName=TargetAssets.get(iAsset);
			if (!AssetName.equals("FIXED INCOME") && !AssetName.equals("INTERNATIONAL BONDS") && !AssetName.equals("CASH") ){
				HasRiskyAsset=true;
				break;
			}
		}

/*LJF temp testing begin*/
String TempAssetLog="LJF compared with TargetAssets, CurrentAssets:";
String TempSecurLog="LJF compared with TargetSecurities, CurrentSecurities:";
List<Asset>CurrAssetList=CurrentPortfolio.getCurrentAssetList();
for (int iAsset=0;iAsset<CurrAssetList.size();iAsset++){
     String AssetName=CurrAssetList.get(iAsset).getName();
     double AssetAmount=CurrentPortfolio.getAssetAmount(AssetName,CurrentDate);
     if(AssetAmount>0){
            TempAssetLog+=(" "+AssetName+AssetAmount/CurrentPortfolio.getTotalAmount(CurrentDate));
            TempSecurLog+=(" "+AssetName+":");
            List<HoldingItem> HoldSecurList=CurrAssetList.get(iAsset).getHoldingItems();
            for(int iSecurity=0;iSecurity<HoldSecurList.size();iSecurity++){
                  String FundSymbol=HoldSecurList.get(iSecurity).getSymbol().trim();
                  double SecurHoldAmount=CurrentPortfolio.getSecurityAmount(AssetName,FundSymbol,CurrentDate);
                  TempSecurLog+=(FundSymbol+SecurHoldAmount/CurrentPortfolio.getTotalAmount(CurrentDate));
            }
     }
}
printToLog(TempAssetLog);
printToLog(TempSecurLog);
/*LJF temp testing end*/

		//Transaction Type 1~~~
		if(HasRiskyAsset) {
			NewTargetAssets=TargetAssets;
			NewTargetAssetPercentages=TargetAssetPercentages;
			NewFundPercentageMap=TargetFundPercentageMap;
			/*LJF temp testing*/printToLog("New SelectedAssets= SelecetdAssets");
			/*LJF temp testing*/printToLog("New SelectedAssetPercentages= SelectedAssetPercentages");
			/*LJF Temp testing*/printToLog("New FundPercentageMap 1:"+NewFundPercentageMap);
		}

		//Transaction Type 2~~~
		else {
			double KeepStill=1.0/3;	
			//--for current holding risky asset and their funds(Add KeepStill*ActualPercentage to NewFundPercentageMap)
			double SumHoldingFundPercent=0;
			double PortfolioTotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
			List<Asset> CurrentAssetList = new ArrayList<Asset>();
			CurrentAssetList=CurrentPortfolio.getCurrentAssetList();

			for(int iAsset=0;iAsset<CurrentAssetList.size();iAsset++){
				String AssetName=CurrentAssetList.get(iAsset).getName();
				double AssetAmount=CurrentPortfolio.getAssetAmount(AssetName,CurrentDate);
				if(!AssetName.equals("FIXED INCOME") && !AssetName.equals("INTERNATIONAL BONDS") && !AssetName.equals("CASH") && AssetAmount>0 ){
					List<HoldingItem> HoldingSecurityList=new ArrayList<HoldingItem>();
					HoldingSecurityList=CurrentAssetList.get(iAsset).getHoldingItems();
					for(int iSecurity=0;iSecurity<HoldingSecurityList.size();iSecurity++){
						String FundSymbol=HoldingSecurityList.get(iSecurity).getSymbol().trim();
						double SecurityHoldingAmount=CurrentPortfolio.getSecurityAmount(CurrentAssetList.get(iAsset).getName(),FundSymbol,CurrentDate);
						NewFundPercentageMap.put(FundSymbol,SecurityHoldingAmount/PortfolioTotalAmount*KeepStill);
						SumHoldingFundPercent+=SecurityHoldingAmount/PortfolioTotalAmount*KeepStill;
					}
					/*LJF temp testing */if(TargetAssets.indexOf(AssetName)!=-1)printToLog("LJF warning:An Error occured!!!TargetAssets contains risky Asset");
					NewTargetAssets.add(AssetName);
					NewTargetAssetPercentages.add(AssetAmount/PortfolioTotalAmount*KeepStill);
				}
			}
			
			//--for target assets(Adjust TargetAssetPercentages.get()*(1-SumHoldingFundPercent))
			for(int iAsset=0;iAsset<TargetAssetPercentages.size();iAsset++){
				String AssetName=TargetAssets.get(iAsset);
				if (AssetName.equals("FIXED INCOME") || AssetName.equals("INTERNATIONAL BONDS") || AssetName.equals("CASH")){
					/*LJF temp testing*/if(NewTargetAssets.indexOf(AssetName)!=-1)printToLog("LJF Important Warning: An error occured!!! This situation need more attention");
					NewTargetAssets.add(AssetName);
					NewTargetAssetPercentages.add(TargetAssetPercentages.get(iAsset)*(1-SumHoldingFundPercent));
				}
			}
			//--for target Favorable funds (Add TargetFundPercentageMap.get(FavSecurity)*(1-SumHoldingFundPercent) to NewFundPercentageMap)
			Iterator<String> iter=TargetFundPercentageMap.keySet().iterator();  
            while(iter.hasNext()){  
                String FundSymbol = iter.next();
                double PercentAmount= TargetFundPercentageMap.get(FundSymbol);
                NewFundPercentageMap.put(FundSymbol,PercentAmount*(1-SumHoldingFundPercent));
            }
		/*LJF temp testing*/printToLog("New SelectedAssets:"+NewTargetAssets);
		/*LJF temp testing*/printToLog("New SelectedAssetPercentages:"+NewTargetAssetPercentages);
		/*LJF Temp testing*/printToLog("New FundPercentageMap 2:"+NewFundPercentageMap);
		}
	}
	//4.
	//Transaction Type 3~~~
	else {
		//Modify NewFundPercentageMap to CurrentHolding Percentages;Modify TargetAssets/TargetAssetPercentages to CurrentHolding Assets;
printToLog("Do not NeedToAdjNow: 529 college plan "+CurrentDate);
		double PortfolioTotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
		List<Asset> CurrentAssetList = new ArrayList<Asset>();
		CurrentAssetList=CurrentPortfolio.getCurrentAssetList();
		for(int iAsset=0;iAsset<CurrentAssetList.size();iAsset++){
			double AssetAmount=CurrentPortfolio.getAssetAmount(CurrentAssetList.get(iAsset),CurrentDate);
			if (AssetAmount==0){continue;}
			List<HoldingItem>HoldingSecurityList = new ArrayList<HoldingItem>();
			HoldingSecurityList = CurrentAssetList.get(iAsset).getHoldingItems();
			for (int iSecurity=0;iSecurity<HoldingSecurityList.size();iSecurity++){
				String FundSymbol = HoldingSecurityList.get(iSecurity).getSymbol().trim();
				double SecurityHoldingAmount = CurrentPortfolio.getSecurityAmount(CurrentAssetList.get(iAsset).getName(), FundSymbol, CurrentDate);
				NewFundPercentageMap.put(FundSymbol,SecurityHoldingAmount/PortfolioTotalAmount);
			}
			NewTargetAssets.add(CurrentAssetList.get(iAsset).getName());
			NewTargetAssetPercentages.add(AssetAmount/PortfolioTotalAmount);
		}
                printToLog("New Selected Assets:"+NewTargetAssets);
		printToLog("New Selected Asset Percentages:"+NewTargetAssetPercentages);
		printToLog("New FundPercentageMap 3:"+NewFundPercentageMap);
	}
		

	Iterator<String> iter=NewFundPercentageMap.keySet().iterator();
        List<String>  NewRepresentFundList = new ArrayList<String>();
        while(iter.hasNext()){
            String FundSymbol=iter.next();
            if(NewFundPercentageMap.get(FundSymbol) > 0)
                NewRepresentFundList.add(FundSymbol);
        }
        NewAssetRepresentFundMap = getAvailableAssetClassSet(MainAssetClassList, NewRepresentFundList);

/*	while(iter.hasNext()){                                // Not right
		String FundSymbol=iter.next();
		String AssetName=null;
		for(int iAsset=0;iAsset<NewTargetAssets.size();iAsset++){
			String TempAssetName=NewTargetAssets.get(iAsset);
			if ((isUpperOrSameClassOfSymbol(TempAssetName,FundSymbol) && AssetName==null) || (AssetName!=null && isUpperOrSameClass(AssetName,TempAssetName)))
				{AssetName=TempAssetName;}
		}
		List<String>RepresentFundList=new ArrayList<String>();
		if(NewAssetRepresentFundMap.get(AssetName)!=null)	{RepresentFundList=NewAssetRepresentFundMap.get(AssetName);NewAssetRepresentFundMap.remove(AssetName);}
		RepresentFundList.add(FundSymbol);
		if(AssetName==null) AssetName="CASH";//CASH Asset is not an asset class in the MainAssetClass
		NewAssetRepresentFundMap.put(AssetName,RepresentFundList);
	}
*/
	printToLog("New AssetRepresentFundMap:"+NewAssetRepresentFundMap);

       
        int ToRebalance = 0;
        if(!AlreadyAdjThisYear && NeedToAdjNow)
            ToRebalance = 1;

	Object[] NewObject=new Object[5];
	NewObject[0]=NewTargetAssets;
	NewObject[1]=NewTargetAssetPercentages;
	NewObject[2]=NewFundPercentageMap;
	NewObject[3]=NewAssetRepresentFundMap;
        NewObject[4] = ToRebalance;
	return NewObject;
}

//for scaling
public List<String> getSaveListOfThisAction(List<String> SourceList, String StartString) throws Exception {  

    List<String> TargetList = new ArrayList<String>();
    int StartIndex = SourceList.indexOf(StartString) + 1;
    java.util.regex.Pattern p = java.util.regex.Pattern.compile("\\d{4}\\-\\d{2}\\-\\d{2}");
    java.util.regex.Matcher match = null;
    boolean isDateStr = false;
    for(int i = StartIndex; i < SourceList.size(); i++ )
    {
        match = p.matcher(SourceList.get(i));
        isDateStr = match.find();
        if(!isDateStr)
            TargetList.add(SourceList.get(i));
        else
            break;
    }   
    return TargetList;
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		initPlanParameters();  //Adj for Roundtrip Part 0 Initial for Parameter WaitingPeriod and Roundtrip
java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
String dateStr=df.format(CurrentDate);
long sTime = System.currentTimeMillis();
double StableAllocation = RiskProfile;
double RiskyAllocation = 100 - RiskProfile;
int ToRebalance = 1;
InAfterDateFilter = new int[CandidateFund.length];
TooVolatile = new int[CandidateFund.length];
NewYear = true;
List<String> MainAssetClassList = new ArrayList<String>(); 

printToLog(PlanID);
boolean HaveDataFile = true;
boolean HaveData = true;
List<String> SelectedAssets = null;
List<Double> SelectedAssetPercentages = null;
List<String> RepresentFundList = null;
HashMap<String, List<String>> AssetRepresentFundMap = null;
HashMap<String, Double> FundPercentageMap = null;
List<HoldingItem> HoldingSecurityList = null;
Object[] ReadObj = null;
List<String> RiskyGroupSelect = null;
List<String> StableGroupSelect = null;
List<String> SaveRiskyGroupSelect = null;
List<String> SaveStableGroupSelect = null;
List<String> Saveusequity = null;
List<String> Saveinternationalequity = null;
List<String> Savefixedincome = null;
List<String> Saverealestate = null;
List<String> Savecommodities = null;
List<String> Saveemergingmarket = null;
List<String> Saveinternationalbonds =null;
List<String> Savehighyieldbond = null;
List<String> Savebalancefund = null;
List<String> CanNotBuyFundList = null;

for(int i=0; i< MainAssetClass.length; i++)
    MainAssetClassList.add(MainAssetClass[i]);

//============================================================
/*  judge whether have data for this action. No: calculate and save the data ;  Yes: load the data */

try{ReadObj = (Object[])com.lti.util.PersistentUtil.readObject("TAA Data Plan " + PlanID);}
    catch(Exception e1){HaveDataFile = false; HaveData = false;}

if(rewriteData || PlanID == 0)
{
HaveDataFile = false;
HaveData = false;
}

if(HaveDataFile)
{
    SaveRiskyGroupSelect = (ArrayList<String>)ReadObj[0];
    SaveStableGroupSelect = (ArrayList<String>)ReadObj[1];
    Saveusequity = (ArrayList<String>)ReadObj[2];
    Saveinternationalequity = (ArrayList<String>)ReadObj[3];
    Savefixedincome = (ArrayList<String>)ReadObj[4];
    Saverealestate = (ArrayList<String>)ReadObj[5];
    Savecommodities = (ArrayList<String>)ReadObj[6];
    Saveemergingmarket = (ArrayList<String>)ReadObj[7];
    Saveinternationalbonds = (ArrayList<String>)ReadObj[8];
    Savehighyieldbond = (ArrayList<String>)ReadObj[9];
    Savebalancefund = (ArrayList<String>)ReadObj[10];
    if(SaveRiskyGroupSelect.indexOf(dateStr) == -1)
        HaveData = false;
}

if(!HaveData)
{
   printToLog("------------------First Time Calculation ,  for data preparation----------------------------");

List<String> CandidateFundList = new ArrayList<String>();
List<String> ShortHistoryFundList = new ArrayList<String>();

Calendar tmpCa = Calendar.getInstance();
tmpCa.setTime(CurrentDate);
LastActionMonth = tmpCa.get(Calendar.MONTH);  

//  Delete Duplicate Candidates
Object[] CandidateObject = DeleteDuplicateCandidate(CandidateFund, RedemptionLimit, WaitingPeriod, RoundtripLimit);
CandidateFund = (String[])CandidateObject[0];
RedemptionLimit = (int[])CandidateObject[1];
RoundtripLimit = (int[])CandidateObject[2];
WaitingPeriod = (int[])CandidateObject[3];

//  get the asset class of the candidate funds LJF changed Calculation of MPT 2010.03.16
AssetClass = new String[CandidateFund.length];
for(int i = 0; i < CandidateFund.length; i++)
    try{AssetClass[i] = getSecurity(CandidateFund[i]).getAssetClass().getName();}  
        catch(Exception e){AssetClass[i] = "Unknow";}

//   Choose the available candidate funds LJF Changed calculation of MPT 2010.03.16

Object[] AvailableObject = ChooseAvailableCandidate(CandidateFund, NewYear, InAfterDateFilter, TooVolatile);
CandidateFundList = (ArrayList<String>)AvailableObject[0];
ShortHistoryFundList = (ArrayList<String>)AvailableObject[1];
InAfterDateFilter = (int[])AvailableObject[2];
TooVolatile = (int[])AvailableObject[3];

//   Get the available asset classes 

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);
    try{AllAssetFundsMap.remove("ROOT");}     // templately fix the problem of "getAvailableAssetClassSet" added on 2010.05.08  please delete it when the problem is fixed
        catch(Exception e){ } 

//  Divide into Risky Group and Stable Group
Object[] AssetClassObject = DivideAssetClass(AllAssetFundsMap, NumberOfMainRiskyClass);
String[] AvailableAssets = (String[])AssetClassObject[0];
String[] RiskyAssets = (String[])AssetClassObject[1];
String[] StableAssets  = (String[])AssetClassObject[2];
NumberOfMainRiskyClass = (Integer)AssetClassObject[3];

//    Calculate the momentum scores of benchmarks and select asset classes 
Object[] SelectClassObject = SelectAssetClass(AvailableAssets, StableAssets, CashScoreWeight, SelectedPercentage, NumberOfMainRiskyClass, NumberOfMainStableClass);
RiskyGroupSelect = (ArrayList<String>)SelectClassObject[0];
StableGroupSelect = (ArrayList<String>)SelectClassObject[1];

//    Combine selected assets 
SelectedAssets = new ArrayList<String>();
int SelectedAssetSize = RiskyGroupSelect.size() + StableGroupSelect.size();
boolean Add = true;
for(int i = 0; i < SelectedAssetSize; i++)
{
    if(i < RiskyGroupSelect.size())
        SelectedAssets.add(RiskyGroupSelect.get(i));
    else
    {
        for(int j = 0; j < RiskyGroupSelect.size(); j++)
            if(StableGroupSelect.get(i-RiskyGroupSelect.size()).equals(RiskyGroupSelect.get(j)))
                {Add = false; break;}
        if(Add)
            SelectedAssets.add(StableGroupSelect.get(i - RiskyGroupSelect.size()));
    }
}

//    Add CASH into the stable asset class, Add duplicate safe stable funds into the stable classes, modified by WYJ on 2010.05.10 
AllAssetFundsMap = AppendStableClass(AllAssetFundsMap, StableGroupSelect);
printToLog("AllAssetFundsMap after appended : " + AllAssetFundsMap);

/*rank momentum and generate sorted funds for the selected assets to be saved*/

// initial the saved sorted fund lists, 2010.07
if(!HaveDataFile)
{
    Saveusequity = new ArrayList<String>();
    Saveinternationalequity = new ArrayList<String>();
    Savefixedincome = new ArrayList<String>();
    Saverealestate = new ArrayList<String>();
    Savecommodities = new ArrayList<String>();
    Saveemergingmarket = new ArrayList<String>();
    Saveinternationalbonds = new ArrayList<String>();
    Savehighyieldbond = new ArrayList<String>();
    Savebalancefund = new ArrayList<String>();
}
Saveusequity.add(dateStr);
Saveinternationalequity.add(dateStr);
Savefixedincome.add(dateStr);
Saverealestate.add(dateStr);
Savecommodities.add(dateStr);
Saveemergingmarket.add(dateStr);
Saveinternationalbonds.add(dateStr);
Savehighyieldbond.add(dateStr);
Savebalancefund.add(dateStr);

HashMap<String, String> PseudoMap = new HashMap<String, String>();
List<String> FundList = null;
String[] Funds = null;
int SaveNumber;
RepresentFundList = null;
double[] Score;

for(int i = 0; i < SelectedAssets.size(); i++)
{
    FundList  = new ArrayList<String>();
    FundList  = AllAssetFundsMap.get(SelectedAssets.get(i).trim());
    Funds = new String[FundList.size()];
    Score = new double[FundList.size()];
    SaveNumber = 5;
    SaveNumber = SaveNumber < Funds.length?SaveNumber : Funds.length;

    // Special Treatment towards ShortHistoryFunds   , Added by WYJ on 2010.05.13
    for(int j = 0; j< FundList.size(); j++)
    {
        if(ShortHistoryFundList.indexOf(FundList.get(j)) == -1)
            Funds[j] = FundList.get(j);    
        else
        {
            Funds[j] = getSecurity(getSecurity(FundList.get(j)).getAssetClass().getBenchmarkID()).getSymbol();
            PseudoMap.put(Funds[j].trim(), FundList.get(j));
            printToLog("Fund "+FundList.get(j) +" is replaced by PseudoFunds(its benchmark)"+Funds[j]+" while ranking");          
        }
    }
    Score = getMomentumScore(Funds, CashScoreWeight, CurrentDate);
 
double MaxScore;
int MaxScoreIndex;
for(int l=0; l< SaveNumber ; l++)
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

//Change the PseudoFunds back to candidate funds, Added by WYJ on 2010.05.13
for(int j = 0; j < Funds.length; j++)
{
    if(FundList.indexOf(Funds[j]) == -1)
    {
        Funds[j] = PseudoMap.get(Funds[j].trim());
    }
}

// save sorted fund list, only the top savenumber funds, 2010.07
if(SelectedAssets.get(i).trim().equals("US EQUITY"))
{
    for(int j = 0; j < SaveNumber; j++)
        Saveusequity.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL EQUITY"))
{
    for(int j = 0; j < SaveNumber; j++)
        Saveinternationalequity.add(Funds[j]);    
}
else if(SelectedAssets.get(i).trim().equals("FIXED INCOME"))
{
    for(int j = 0; j < SaveNumber; j++)
        Savefixedincome.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("REAL ESTATE"))
{
    for(int j = 0; j < SaveNumber; j++)
        Saverealestate.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("COMMODITIES"))
{
    for(int j = 0; j < SaveNumber; j++)
        Savecommodities.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("Emerging Market"))
{
    for(int j = 0; j < SaveNumber; j++)
        Saveemergingmarket.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL BONDS"))
{
    for(int j = 0; j < SaveNumber; j++)
        Saveinternationalbonds.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("High Yield Bond"))
{
    for(int j = 0; j < SaveNumber; j++)
        Savehighyieldbond.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("BALANCE FUND"))
{
    for(int j = 0; j < SaveNumber; j++)
        Savebalancefund.add(Funds[j]);
}

}  //End of SelectedAsset Cause    
//End of  generate sorted funds

/*save the selected asset classes and the sorted fund lists, write objects, 2010.07*/

if(!HaveDataFile)
{   
    SaveRiskyGroupSelect = new ArrayList<String>();
    SaveStableGroupSelect = new ArrayList<String>();
}
SaveRiskyGroupSelect.add(dateStr);
SaveStableGroupSelect.add(dateStr);
for(int i = 0; i < RiskyGroupSelect.size(); i++)
    SaveRiskyGroupSelect.add(RiskyGroupSelect.get(i));
for(int i = 0; i < StableGroupSelect.size(); i++)
    SaveStableGroupSelect.add(StableGroupSelect.get(i));

Object[] Ob = new Object[]{SaveRiskyGroupSelect, SaveStableGroupSelect, Saveusequity, Saveinternationalequity, Savefixedincome, Saverealestate, Savecommodities, Saveemergingmarket,  Saveinternationalbonds, Savehighyieldbond, Savebalancefund};

com.lti.util.PersistentUtil.writeObject(Ob,"TAA Data Plan " + PlanID);

}   //End of the first try catch, which is done when calculating for the first time
//============================================================================================

if(HaveData)   //  After calculate the first portfolio of the plan
{

    printToLog("--------------------  Scaling Calculation ----------------------------");

    RiskyGroupSelect = getSaveListOfThisAction(SaveRiskyGroupSelect, dateStr);
    StableGroupSelect = getSaveListOfThisAction(SaveStableGroupSelect, dateStr);

}//  end of "HaveDate" calculation for non-first time monitor

//===========================================================================================
/*---------------------------- Target Percentage Determination Basing on Data  ------------------------------    */

//    Combine selected assets and determine the asset percentages, modified by WYJ in 2010.07*/
boolean Add = true;
double StablePerc = 0;
SelectedAssetPercentages = new ArrayList<Double>();
SelectedAssets = new ArrayList<String>();
for(int i=0;i<RiskyGroupSelect.size();i++)
    SelectedAssetPercentages.add(RiskyAllocation/RiskyGroupSelect.size()/100);
for(int i = 0; i< RiskyGroupSelect.size(); i++)
    SelectedAssets.add(RiskyGroupSelect.get(i));
for(int i =0; i < StableGroupSelect.size();i++)
{
    for(int j = 0; j < RiskyGroupSelect.size(); j++)
         if(StableGroupSelect.get(i).equals(RiskyGroupSelect.get(j)))
         {
             StablePerc = (RiskyAllocation/RiskyGroupSelect.size() + StableAllocation/StableGroupSelect.size())/100;
             SelectedAssetPercentages.set(j, StablePerc );
             Add = false;
             break;
         }
    if(Add)
    {
        StablePerc = StableAllocation/StableGroupSelect.size()/100;
        SelectedAssets.add(StableGroupSelect.get(i));
        SelectedAssetPercentages.add(StablePerc);
    }
    Add = true;
}

//    Make neccesary diversification of the stable asset classes, Added by WYJ on 2010.05.08
if(SelectedAssets.size() == 1)
    MaxOfStableAsset = 3;
else if(SelectedAssets.size() == 2)
    MaxOfStableAsset = 2;

double EachStableFundPec = StablePerc / MaxOfStableAsset ;
if(EachStableFundPec > 0.67)
    MaxOfStableAsset +=2;
else if(EachStableFundPec > 0.34)
    MaxOfStableAsset +=1;

// Add CASH after Diversification
if(SelectedAssets.indexOf("CASH") < 0)
{
    SelectedAssets.add("CASH");
    SelectedAssetPercentages.add(0.0);
}

/*
       JUST FOR TESTING, please delete after testing
RoundtripLimit = new int[CandidateFund.length];
WaitingPeriod = new int[CandidateFund.length];
for(int i = 0; i < CandidateFund.length; i++)
{
    RoundtripLimit[i] = 2;
    WaitingPeriod[i] = 2;
}
*/

/* ----------------   Aggregate perferred funds and determine the target fund percentages ----------------- */
AssetRepresentFundMap = new HashMap<String, List<String>>();
FundPercentageMap = new HashMap<String, Double>();
HashMap<String, String> PseudoMap = new HashMap<String, String>();
List<String>  SaveFundList = null;
RepresentFundList = null;
int RepresentNumber;
double Weight;
CanNotBuyFundList  = new ArrayList<String>();

for(int i = 0; i < SelectedAssets.size(); i ++)     // start of selected asset treatment
{
    if(SelectedAssets.get(i).trim().equals("US EQUITY"))
        SaveFundList = getSaveListOfThisAction(Saveusequity, dateStr);
    else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL EQUITY"))
        SaveFundList = getSaveListOfThisAction(Saveinternationalequity, dateStr);
    else if(SelectedAssets.get(i).trim().equals("FIXED INCOME"))
        SaveFundList = getSaveListOfThisAction(Savefixedincome, dateStr);
    else if(SelectedAssets.get(i).trim().equals("REAL ESTATE"))
        SaveFundList = getSaveListOfThisAction(Saverealestate, dateStr);
    else if(SelectedAssets.get(i).trim().equals("COMMODITIES"))
        SaveFundList = getSaveListOfThisAction(Savecommodities, dateStr);
    else if(SelectedAssets.get(i).trim().equals("Emerging Market"))
        SaveFundList = getSaveListOfThisAction(Saveemergingmarket, dateStr);
    else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL BONDS"))
        SaveFundList = getSaveListOfThisAction(Saveinternationalbonds, dateStr);
    else if(SelectedAssets.get(i).trim().equals("High Yield Bond"))
        SaveFundList = getSaveListOfThisAction(Savehighyieldbond, dateStr);
    else if(SelectedAssets.get(i).trim().equals("BALANCE FUND"))
        SaveFundList = getSaveListOfThisAction(Savebalancefund, dateStr);
    else if(SelectedAssets.get(i).trim().equals("CASH"))
    {
        SaveFundList = new ArrayList<String>();
        SaveFundList.add("CASH");
    }

//    Calculate CanNotBuyFundList, Adj for RoundTrip Part 2 LJF 2010.07.02 
List<String> tempList  = new ArrayList<String>();
if(RoundtripLimit != null || WaitingPeriod != null)        // Some variable need to prepare
{
    tempList = ExcludeByRoundTripAndWaiting(SaveFundList, CandidateFund, RoundtripLimit, WaitingPeriod);
    if(tempList.size() > 0)
        CanNotBuyFundList.addAll(tempList);
}

    if(SelectedAssets.get(i).equals("FIXED INCOME") || SelectedAssets.get(i).equals("INTERNATIONAL BONDS") )
        RepresentNumber = MaxOfStableAsset;
    else if(SelectedAssets.get(i).equals("CASH"))
        RepresentNumber = 1;
    else 
        RepresentNumber = MaxOfRiskyAsset;
    RepresentNumber = RepresentNumber < SaveFundList.size()?RepresentNumber : SaveFundList.size();
    printToLog("selected fund number of " + SelectedAssets.get(i) + "  = " + RepresentNumber);

    RepresentFundList = new ArrayList<String>();
    double TempWeight;
    if(!FundPercentageMap.containsKey("CASH"))
        FundPercentageMap.put("CASH", 0.0);        

//    aggregate the same representative funds, Added by WYJ on 2010.05.08, modified on 05.10*/
Weight = SelectedAssetPercentages.get(i) /RepresentNumber; 
int MaxIndex=RepresentNumber;//Adj for Roundtrip Part 3 Defination LJF 2010.07.02
for(int j = 0; j < MaxIndex; j++)  
{
        printToLog("qualified fund "+ j + " of  " + SelectedAssets.get(i) + ": " + SaveFundList.get(j));
        if(FundPercentageMap.get(SaveFundList.get(j).trim())==null)
		{	
			if (CanNotBuyFundList.indexOf(SaveFundList.get(j))== -1 || CurrentPortfolio.holdSecurity(getSecurity(SaveFundList.get(j)).getID())) //Adj for RoundTrip Part 3 LJF 2010.07.02
			{
			RepresentFundList.add(SaveFundList.get(j));
            FundPercentageMap.put(SaveFundList.get(j).trim(), Weight);
            printToLog(SelectedAssets.get(i) + " buy target :  " + SaveFundList.get(j) + "   target percentage : " + Weight);
			}
			else {	//Adj for RoundTrip Part 3 LJF 2010.07.02
				if (MaxIndex < SaveFundList.size()) {MaxIndex=MaxIndex+1; printToLog("throw !!!");}     
				//else {printToLog("LJF Important Notice: No Representive Funds selected for this asset due to Roundtrip&& Waitingperiod Limit");}
				} 
		}
        else 
        {
            if(RepresentFundList.indexOf(SaveFundList.get(j))==-1) RepresentFundList.add(SaveFundList.get(j));//e.g, add "CASH" fund under "CASH" Asset Class
            TempWeight = FundPercentageMap.get(SaveFundList.get(j).trim());
            TempWeight += Weight;
            FundPercentageMap.put(SaveFundList.get(j).trim(), TempWeight);
            printToLog(SelectedAssets.get(i) + " buy target :  " + SaveFundList.get(j) + " Cumulative target percentage : " + TempWeight);
        }
}
AssetRepresentFundMap.put(SelectedAssets.get(i).trim(), RepresentFundList);
}   //End of SelectedAsset  treatment

/*LJF temp testing*/printToLog("AssetRepresentFundMap 1:"+AssetRepresentFundMap);
//    Aggregate the CASH from different Assets
for(int iAsset=0;iAsset<SelectedAssets.size();iAsset++){
	String AssetName=SelectedAssets.get(iAsset);
	if(!AssetName.equals("CASH")){
		List<String>RepreFundList=AssetRepresentFundMap.get(AssetName);
		int CASHFundIndex=RepreFundList.indexOf("CASH");
		if (CASHFundIndex==-1) continue;
		//Remove CASH from RepresentFundList
		RepreFundList.remove("CASH");
		AssetRepresentFundMap.put(AssetName,RepreFundList);
		
		double SumAssetPercent=0;
		for(int iSecurity=0;iSecurity<RepreFundList.size();iSecurity++){
			String FundSymbol=RepreFundList.get(iSecurity);
			SumAssetPercent+=FundPercentageMap.get(FundSymbol);
		}
		int CASHAssetIndex=SelectedAssets.indexOf("CASH");
		double AddOnCASH=SelectedAssetPercentages.get(iAsset)-SumAssetPercent;
		//Adjust CASH's AssetPercentage
SelectedAssetPercentages.set(CASHAssetIndex,SelectedAssetPercentages.get(CASHAssetIndex)+AddOnCASH);
		//Adjust iAsset's AssetPercentage
		SelectedAssetPercentages.set(iAsset,SumAssetPercent);
	}
}

//    Adjust FundPercentageMap for CanNotBuyFundList, caused by RoundTrip and Waiting Period
//    Adj for RoundTrip Part 4 LJF 2010.07.02 BEGIN
if(CanNotBuyFundList.size() != 0)
{
Object[] AdjustPercentageObject = AdjustTargetPercentage(CanNotBuyFundList, SelectedAssetPercentages, FundPercentageMap, AssetRepresentFundMap, SelectedAssets, MainAssetClassList);
SelectedAssetPercentages = (ArrayList<Double>)AdjustPercentageObject[0]; 
FundPercentageMap = (HashMap<String,Double>)AdjustPercentageObject[1]; 
}

//    Remove the selected asset with 0 percentage allocation
List<String> TempAssetList = SelectedAssets;
List<Double> TempAssetPercentages = SelectedAssetPercentages;
SelectedAssets = new ArrayList<String>();
SelectedAssetPercentages = new ArrayList<Double>();
for(int i = 0; i < TempAssetList.size(); i++)
{
    if(TempAssetPercentages.get(i) > 0)
    {
        SelectedAssets.add(TempAssetList.get(i));
        SelectedAssetPercentages.add(TempAssetPercentages.get(i));
    }
    else
        AssetRepresentFundMap.remove( TempAssetList.get(i));
}

/*LJF adj for 529 college saving plan's annual adjustment*/
//Check whether the portfolio belongs to 520 college saving plan: 
//Condition 1.if the answer is Yes: Trigger the adjustment  or endow SelectedAssets(and also PresentiveFunds, FundPercentageMap) with actual holding assets/PresentiveFunds.
//Condition 2.if the answer is NO : do nothing in this part 

//is529Plan = true;   /*JUST FOR TEST*/
if(is529Plan) {                  
Object[] NewObject =compareTargetAndHoldingAssets(CandidateFund, SelectedAssets,SelectedAssetPercentages,FundPercentageMap, MainAssetClassList);
SelectedAssets=(ArrayList<String>)NewObject[0];
SelectedAssetPercentages=(ArrayList<Double>)NewObject[1];
FundPercentageMap=(HashMap<String,Double>)NewObject[2];
AssetRepresentFundMap=(HashMap<String,List<String>>)NewObject[3];
ToRebalance = (Integer)NewObject[4];
}

if(ToRebalance == 1)
{
//=========================================================================================
/* -----------------------  Actual Trading Percentage Determination --------------*/



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
    RepresentFundList = AssetRepresentFundMap.get(SelectedAssets.get(i).trim());
        for(int j=0; j< RepresentFundList.size(); j++)
           CurrentPortfolio.buyAtNextOpen(SelectedAssets.get(i), RepresentFundList.get(j), TotalAmount*FundPercentageMap.get(RepresentFundList.get(j).trim()).doubleValue(), CurrentDate,true); 
}

}  // End of "if(ToRebalance == 1)"

long eTime = System.currentTimeMillis();
printToLog("Initial Time Cost:  " + (eTime - sTime));

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
	
		
		/*long eTime7 = System.currentTimeMillis();
if(sTime7 != 0)
    printToLog("Time Cost Between Actions : " + (eTime7 - sTime7));
*/
boolean ToCheck = false;
HashMap<String,Integer> RedemptionLimitMap = new HashMap<String,Integer>(); 
List<Asset> HoldingAssetList =  new ArrayList<Asset>();
HashMap<String,List<HoldingItem>> HoldingAssetSecurityMap = new HashMap<String,List<HoldingItem>>();
HashMap<String,Double>  SecurityAvailableTradingPercentage = new HashMap<String,Double>();
HashMap<String,Double>  AssetAvailableTradingPercentage = new HashMap<String,Double>();

//printToLog("Small cash in the portfolio  =  " +  CurrentPortfolio.getCash());
//try{
if(( CheckFrequency.equalsIgnoreCase("monthly") && LTIDate.isLastNYSETradingDayOfMonth(LTIDate.getNewDate(CurrentDate, TimeUnit.DAILY, (31- CheckDate)))) 
|| ( CheckFrequency.equalsIgnoreCase("quarterly") && LTIDate.isLastNYSETradingDayOfQuarter(LTIDate.getNewDate(CurrentDate, TimeUnit.DAILY, (31- CheckDate)))) || (CheckFrequency.equals("yearly")&&LTIDate.isLastNYSETradingDayOfYear(LTIDate.getNewDate(CurrentDate, TimeUnit.DAILY, (31- CheckDate)))))
{
long sTime3 = System.currentTimeMillis();

//printToLog("Frequncy = " + CheckFrequency  );
//if(LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate))
   // printToLog("It's the end of quarter.");

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
    RedemptionLimitMap.put(CandidateFund[i].trim(), K);
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
    HoldingAssetSecurityMap.put(HoldingAssetList.get(i).getName().trim(),HoldingSecurityList);
    aa = 0;
    for(int j = 0; j < HoldingSecurityList.size(); j++)
    {
        sn = HoldingSecurityList.get(j).getSymbol().trim();
        printToLog("Symbol: "+ sn);
        printToLog("RedemptionLimit = " + RedemptionLimitMap.get(sn));
       //printToLog("Symbol: "+ sn + "   RedemptionLimit = " + RedemptionLimitMap.get(sn).intValue());
        sa = CurrentPortfolio.getAvailableTradingAmount(sn, RedemptionLimitMap.get(sn).intValue(), CurrentDate, 1);
        printToLog("Available Trading Amount :  sa =" + sa);
       SecurityAvailableTradingPercentage.put(sn, new Double(sa/ta));
        aa += sa;
    }
    if(aa/ta > 0.02) ToCheck = true;
    printToLog("Asset : " + HoldingAssetList.get(i).getName() + "Available percentage =   " + aa/ta);
    AssetAvailableTradingPercentage.put(HoldingAssetList.get(i).getName().trim(), new Double(aa/ta));
}

long eTime3 = System.currentTimeMillis();
printToLog("Commen Action Time Cost:  " + (eTime3 - sTime3));

}
//}catch(Exception e){printToLog("Failed in comman action"); }

		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (ToCheck) {
		   initPlanParameters();  //Adj for Roundtrip Part 0 Initial for Parameter WaitingPeriod and Roundtrip
java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
String dateStr=df.format(CurrentDate);
long sTime = System.currentTimeMillis();
double StableAllocation = RiskProfile;
double RiskyAllocation = 100 - RiskProfile;
List<String> MainAssetClassList = new ArrayList<String>(); 
int ToRebalance = 1;

long sTime6;
long eTime6;
printToLog(PlanID);
boolean HaveDataFile = true;
boolean HaveData = true;
List<String> SelectedAssets = null;
List<Double> SelectedAssetPercentages = null;
List<String> RepresentFundList = null;
HashMap<String, List<String>> AssetRepresentFundMap = null;
HashMap<String, Double> FundPercentageMap = null;
List<HoldingItem> HoldingSecurityList = null;
Object[] ReadObj = null;
List<String> RiskyGroupSelect = null;
List<String> StableGroupSelect = null;
List<String> SaveRiskyGroupSelect = null;
List<String> SaveStableGroupSelect = null;
List<String> Saveusequity = null;
List<String> Saveinternationalequity = null;
List<String> Savefixedincome = null;
List<String> Saverealestate = null;
List<String> Savecommodities = null;
List<String> Saveemergingmarket = null;
List<String> Saveinternationalbonds =null;
List<String> Savehighyieldbond = null;
List<String> Savebalancefund = null;
List<String> CanNotBuyFundList = null;

for(int i=0; i< MainAssetClass.length; i++)
    MainAssetClassList.add(MainAssetClass[i]);

//============================================================
/*  judge whether have data for this action. No: calculate and save the data ;  Yes: load the data */

try{ReadObj = (Object[])com.lti.util.PersistentUtil.readObject("TAA Data Plan " + PlanID);}
    catch(Exception e1){HaveDataFile = false; HaveData = false;}

if(rewriteData || PlanID == 0)
{
HaveDataFile = false;
HaveData = false;
}

if(HaveDataFile)
{
    SaveRiskyGroupSelect = (ArrayList<String>)ReadObj[0];
    SaveStableGroupSelect = (ArrayList<String>)ReadObj[1];
    Saveusequity = (ArrayList<String>)ReadObj[2];
    Saveinternationalequity = (ArrayList<String>)ReadObj[3];
    Savefixedincome = (ArrayList<String>)ReadObj[4];
    Saverealestate = (ArrayList<String>)ReadObj[5];
    Savecommodities = (ArrayList<String>)ReadObj[6];
    Saveemergingmarket = (ArrayList<String>)ReadObj[7];
    Saveinternationalbonds = (ArrayList<String>)ReadObj[8];
    Savehighyieldbond = (ArrayList<String>)ReadObj[9];
    Savebalancefund = (ArrayList<String>)ReadObj[10];
    if(SaveRiskyGroupSelect.indexOf(dateStr) == -1)
        HaveData = false;
}

if(!HaveData)
{
   printToLog("------------------First Time Calculation ,  for data preparation----------------------------");
sTime6 = System.currentTimeMillis();

List<String> CandidateFundList = new ArrayList<String>();
List<String> ShortHistoryFundList = new ArrayList<String>();

Calendar tmpCa = Calendar.getInstance();
tmpCa.setTime(CurrentDate);
LastActionMonth = tmpCa.get(Calendar.MONTH);  


//  Delete Duplicate Candidates
Object[] CandidateObject = DeleteDuplicateCandidate(CandidateFund, RedemptionLimit, WaitingPeriod, RoundtripLimit);
CandidateFund = (String[])CandidateObject[0];
RedemptionLimit = (int[])CandidateObject[1];
RoundtripLimit = (int[])CandidateObject[2];
WaitingPeriod = (int[])CandidateObject[3];

//   get the asset class of the candidate funds LJF changed Calculation of MPT 2010.03.16
AssetClass = new String[CandidateFund.length];
for(int i = 0; i < CandidateFund.length; i++)
    try{AssetClass[i] = getSecurity(CandidateFund[i]).getAssetClass().getName();}  
        catch(Exception e){AssetClass[i] = "Unknow";}

//   Choose the available candidate funds LJF Changed calculation of MPT 2010.03.16
Object[] AvailableObject = ChooseAvailableCandidate(CandidateFund, NewYear, InAfterDateFilter, TooVolatile);
CandidateFundList = (ArrayList<String>)AvailableObject[0];
ShortHistoryFundList = (ArrayList<String>)AvailableObject[1];
InAfterDateFilter = (int[])AvailableObject[2];
TooVolatile = (int[])AvailableObject[3];

//   Get the available asset classes 
for(int i=0; i< MainAssetClass.length; i++)
    MainAssetClassList.add(MainAssetClass[i]);

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);
    try{AllAssetFundsMap.remove("ROOT");}     // templately fix the problem of "getAvailableAssetClassSet" added on 2010.05.08  please delete it when the problem is fixed
        catch(Exception e){ } 

//  Divide into Risky Group and Stable Group
Object[] AssetClassObject = DivideAssetClass(AllAssetFundsMap, NumberOfMainRiskyClass);
String[] AvailableAssets = (String[])AssetClassObject[0];
String[] RiskyAssets = (String[])AssetClassObject[1];
String[] StableAssets  = (String[])AssetClassObject[2];
NumberOfMainRiskyClass = (Integer)AssetClassObject[3];

//    Calculate the momentum scores of benchmarks and select asset classes 
Object[] SelectClassObject = SelectAssetClass(AvailableAssets, StableAssets, CashScoreWeight, SelectedPercentage, NumberOfMainRiskyClass, NumberOfMainStableClass);
RiskyGroupSelect = (ArrayList<String>)SelectClassObject[0];
StableGroupSelect = (ArrayList<String>)SelectClassObject[1];


//    Combine selected assets 
SelectedAssets = new ArrayList<String>();
int SelectedAssetSize = RiskyGroupSelect.size() + StableGroupSelect.size();
boolean Add = true;
for(int i = 0; i < SelectedAssetSize; i++)
{
    if(i < RiskyGroupSelect.size())
        SelectedAssets.add(RiskyGroupSelect.get(i));
    else
    {
        for(int j = 0; j < RiskyGroupSelect.size(); j++)
            if(StableGroupSelect.get(i-RiskyGroupSelect.size()).equals(RiskyGroupSelect.get(j)))
                {Add = false; break;}
        if(Add)
            SelectedAssets.add(StableGroupSelect.get(i - RiskyGroupSelect.size()));
    }
}

//    Add CASH into the stable asset class, Add duplicate safe stable funds into the stable classes, modified by WYJ on 2010.05.10 
AllAssetFundsMap = AppendStableClass(AllAssetFundsMap, StableGroupSelect);
printToLog("AllAssetFundsMap after appended : " + AllAssetFundsMap);

/*rank momentum and generate sorted funds for the selected assets to be saved*/

// initial the saved sorted fund lists, 2010.07
if(!HaveDataFile)
{
    Saveusequity = new ArrayList<String>();
    Saveinternationalequity = new ArrayList<String>();
    Savefixedincome = new ArrayList<String>();
    Saverealestate = new ArrayList<String>();
    Savecommodities = new ArrayList<String>();
    Saveemergingmarket = new ArrayList<String>();
    Saveinternationalbonds = new ArrayList<String>();
    Savehighyieldbond = new ArrayList<String>();
    Savebalancefund = new ArrayList<String>();
}
Saveusequity.add(dateStr);
Saveinternationalequity.add(dateStr);
Savefixedincome.add(dateStr);
Saverealestate.add(dateStr);
Savecommodities.add(dateStr);
Saveemergingmarket.add(dateStr);
Saveinternationalbonds.add(dateStr);
Savehighyieldbond.add(dateStr);
Savebalancefund.add(dateStr);

HashMap<String, String> PseudoMap = new HashMap<String, String>();
List<String> FundList = null;
String[] Funds = null;
int SaveNumber;
RepresentFundList = null;
double[] Score;

for(int i = 0; i < SelectedAssets.size(); i++)
{
    FundList  = new ArrayList<String>();
    FundList  = AllAssetFundsMap.get(SelectedAssets.get(i).trim());
    Funds = new String[FundList.size()];
    Score = new double[FundList.size()];
    SaveNumber = 5;
    SaveNumber = SaveNumber < Funds.length?SaveNumber : Funds.length;

    // Special Treatment towards ShortHistoryFunds   , Added by WYJ on 2010.05.13
    for(int j = 0; j< FundList.size(); j++)
    {
        if(ShortHistoryFundList.indexOf(FundList.get(j)) == -1)
            Funds[j] = FundList.get(j);    
        else
        {
            Funds[j] = getSecurity(getSecurity(FundList.get(j)).getAssetClass().getBenchmarkID()).getSymbol();
            PseudoMap.put(Funds[j].trim(), FundList.get(j));
            printToLog("Fund "+FundList.get(j) +" is replaced by PseudoFunds(its benchmark)"+Funds[j]+" while ranking");          
        }
    }
    Score = getMomentumScore(Funds, CashScoreWeight, CurrentDate);
 
double MaxScore;
int MaxScoreIndex;
for(int l=0; l< SaveNumber ; l++)
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

//Change the PseudoFunds back to candidate funds, Added by WYJ on 2010.05.13
for(int j = 0; j < Funds.length; j++)
{
    if(FundList.indexOf(Funds[j]) == -1)
    {
        Funds[j] = PseudoMap.get(Funds[j].trim());
    }
}

// save sorted fund list, only the top savenumber funds, 2010.07
if(SelectedAssets.get(i).trim().equals("US EQUITY"))
{
    for(int j = 0; j < SaveNumber; j++)
        Saveusequity.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL EQUITY"))
{
    for(int j = 0; j < SaveNumber; j++)
        Saveinternationalequity.add(Funds[j]);    
}
else if(SelectedAssets.get(i).trim().equals("FIXED INCOME"))
{
    for(int j = 0; j < SaveNumber; j++)
        Savefixedincome.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("REAL ESTATE"))
{
    for(int j = 0; j < SaveNumber; j++)
        Saverealestate.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("COMMODITIES"))
{
    for(int j = 0; j < SaveNumber; j++)
        Savecommodities.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("Emerging Market"))
{
    for(int j = 0; j < SaveNumber; j++)
        Saveemergingmarket.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL BONDS"))
{
    for(int j = 0; j < SaveNumber; j++)
        Saveinternationalbonds.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("High Yield Bond"))
{
    for(int j = 0; j < SaveNumber; j++)
        Savehighyieldbond.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("BALANCE FUND"))
{
    for(int j = 0; j < SaveNumber; j++)
        Savebalancefund.add(Funds[j]);
}

}  //End of SelectedAsset Cause    
//End of  generate sorted funds

/*save the selected asset classes and the sorted fund lists, write objects, 2010.07*/

if(!HaveDataFile)
{   
    SaveRiskyGroupSelect = new ArrayList<String>();
    SaveStableGroupSelect = new ArrayList<String>();
}
SaveRiskyGroupSelect.add(dateStr);
SaveStableGroupSelect.add(dateStr);
for(int i = 0; i < RiskyGroupSelect.size(); i++)
    SaveRiskyGroupSelect.add(RiskyGroupSelect.get(i));
for(int i = 0; i < StableGroupSelect.size(); i++)
    SaveStableGroupSelect.add(StableGroupSelect.get(i));

Object[] Ob = new Object[]{SaveRiskyGroupSelect, SaveStableGroupSelect, Saveusequity, Saveinternationalequity, Savefixedincome, Saverealestate, Savecommodities, Saveemergingmarket,  Saveinternationalbonds, Savehighyieldbond, Savebalancefund};

com.lti.util.PersistentUtil.writeObject(Ob,"TAA Data Plan " + PlanID);

eTime6 = System.currentTimeMillis();
printToLog("Prepare Data Time Cost:  " + (eTime6 - sTime6));
}   //End of the first try catch, which is done when calculating for the first time

//============================================================================================


if(HaveData)   //  After calculate the first portfolio of the plan
{
    printToLog("--------------------  Scaling Calculation ----------------------------");

//  Read Saved List for Scale Calculation

    RiskyGroupSelect = getSaveListOfThisAction(SaveRiskyGroupSelect, dateStr);
    StableGroupSelect = getSaveListOfThisAction(SaveStableGroupSelect, dateStr);

}//  end of "HaveDate" calculation for non-first time monitor


//===========================================================================================

/*---------------------------- Target Percentage Determination Basing on Data  ------------------------------    */
sTime6 = System.currentTimeMillis();

//    Combine selected assets and determine the asset percentages, modified by WYJ in 2010.07*/
boolean Add = true;
double StablePerc = 0;
SelectedAssetPercentages = new ArrayList<Double>();
SelectedAssets = new ArrayList<String>();
for(int i=0;i<RiskyGroupSelect.size();i++)
    SelectedAssetPercentages.add(RiskyAllocation/RiskyGroupSelect.size()/100);
for(int i = 0; i< RiskyGroupSelect.size(); i++)
    SelectedAssets.add(RiskyGroupSelect.get(i));
for(int i =0; i < StableGroupSelect.size();i++)
{
    for(int j = 0; j < RiskyGroupSelect.size(); j++)
         if(StableGroupSelect.get(i).equals(RiskyGroupSelect.get(j)))
         {
             StablePerc = (RiskyAllocation/RiskyGroupSelect.size() + StableAllocation/StableGroupSelect.size())/100;
             SelectedAssetPercentages.set(j, StablePerc );
             Add = false;
             break;
         }
    if(Add)
    {
        StablePerc = StableAllocation/StableGroupSelect.size()/100;
        SelectedAssets.add(StableGroupSelect.get(i));
        SelectedAssetPercentages.add(StablePerc);
    }
    Add = true;
}

//    Make neccesary diversification of the stable asset classes, Added by WYJ on 2010.05.08
if(SelectedAssets.size() == 1)
    MaxOfStableAsset = 3;
else if(SelectedAssets.size() == 2)
    MaxOfStableAsset = 2;

double EachStableFundPec = StablePerc / MaxOfStableAsset ;
if(EachStableFundPec > 0.67)
    MaxOfStableAsset +=2;
else if(EachStableFundPec > 0.34)
    MaxOfStableAsset +=1;

// Add CASH after Diversification
if(SelectedAssets.indexOf("CASH") < 0)
{
    SelectedAssets.add("CASH");
    SelectedAssetPercentages.add(0.0);
}

/*
      JUST FOR TESTING, please delete after testing
RoundtripLimit = new int[CandidateFund.length];
WaitingPeriod = new int[CandidateFund.length];
for(int i = 0; i < CandidateFund.length; i++)
{
    RoundtripLimit[i] = 2;
    WaitingPeriod[i] = 2;
}
*/

/* ----------------   Aggregate perferred funds and determine the target fund percentages ----------------- */
AssetRepresentFundMap = new HashMap<String, List<String>>();
FundPercentageMap = new HashMap<String, Double>();
HashMap<String, String> PseudoMap = new HashMap<String, String>();
List<String>  SaveFundList = null;
RepresentFundList = null;
int RepresentNumber;
double Weight;
CanNotBuyFundList  = new ArrayList<String>();

for(int i = 0; i < SelectedAssets.size(); i ++)     // start of selected asset treatment
{
    if(SelectedAssets.get(i).trim().equals("US EQUITY"))
        SaveFundList = getSaveListOfThisAction(Saveusequity, dateStr);
    else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL EQUITY"))
        SaveFundList = getSaveListOfThisAction(Saveinternationalequity, dateStr);
    else if(SelectedAssets.get(i).trim().equals("FIXED INCOME"))
        SaveFundList = getSaveListOfThisAction(Savefixedincome, dateStr);
    else if(SelectedAssets.get(i).trim().equals("REAL ESTATE"))
        SaveFundList = getSaveListOfThisAction(Saverealestate, dateStr);
    else if(SelectedAssets.get(i).trim().equals("COMMODITIES"))
        SaveFundList = getSaveListOfThisAction(Savecommodities, dateStr);
    else if(SelectedAssets.get(i).trim().equals("Emerging Market"))
        SaveFundList = getSaveListOfThisAction(Saveemergingmarket, dateStr);
    else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL BONDS"))
        SaveFundList = getSaveListOfThisAction(Saveinternationalbonds, dateStr);
    else if(SelectedAssets.get(i).trim().equals("High Yield Bond"))
        SaveFundList = getSaveListOfThisAction(Savehighyieldbond, dateStr);
    else if(SelectedAssets.get(i).trim().equals("BALANCE FUND"))
        SaveFundList = getSaveListOfThisAction(Savebalancefund, dateStr);
    else if(SelectedAssets.get(i).trim().equals("CASH"))
    {
        SaveFundList = new ArrayList<String>();
        SaveFundList.add("CASH");
    }

//    Calculate CanNotBuyFundList, Adj for RoundTrip Part 2 LJF 2010.07.02 
List<String> tempList  = new ArrayList<String>();
if(RoundtripLimit != null || WaitingPeriod != null)        // Some variable need to prepare
{
    tempList = ExcludeByRoundTripAndWaiting(SaveFundList, CandidateFund, RoundtripLimit, WaitingPeriod);
    if(tempList.size() > 0)
        CanNotBuyFundList.addAll(tempList);
}

    if(SelectedAssets.get(i).equals("FIXED INCOME") || SelectedAssets.get(i).equals("INTERNATIONAL BONDS") )
        RepresentNumber = MaxOfStableAsset;
    else if(SelectedAssets.get(i).equals("CASH"))
        RepresentNumber = 1;
    else 
        RepresentNumber = MaxOfRiskyAsset;
    RepresentNumber = RepresentNumber < SaveFundList.size()?RepresentNumber : SaveFundList.size();
    printToLog("selected fund number of " + SelectedAssets.get(i) + "  = " + RepresentNumber);

    RepresentFundList = new ArrayList<String>();
    double TempWeight;
    if(!FundPercentageMap.containsKey("CASH"))
        FundPercentageMap.put("CASH", 0.0);        

//    aggregate the same representative funds, Added by WYJ on 2010.05.08, modified on 05.10*/
Weight = SelectedAssetPercentages.get(i) /RepresentNumber; 
int MaxIndex=RepresentNumber;//Adj for Roundtrip Part 3 Defination LJF 2010.07.02
for(int j = 0; j < MaxIndex; j++)  
{
        printToLog("qualified fund "+ j + " of  " + SelectedAssets.get(i) + ": " + SaveFundList.get(j));
        if(FundPercentageMap.get(SaveFundList.get(j).trim())==null)
		{	
			if (CanNotBuyFundList.indexOf(SaveFundList.get(j))== -1 || CurrentPortfolio.holdSecurity(getSecurity(SaveFundList.get(j)).getID())) //Adj for RoundTrip Part 3 LJF 2010.07.02
			{
			RepresentFundList.add(SaveFundList.get(j));
            FundPercentageMap.put(SaveFundList.get(j).trim(), Weight);
            printToLog(SelectedAssets.get(i) + " buy target :  " + SaveFundList.get(j) + "   target percentage : " + Weight);
			}
			else {	//Adj for RoundTrip Part 3 LJF 2010.07.02
				if (MaxIndex < SaveFundList.size()) {MaxIndex=MaxIndex+1; printToLog("throw !!!");}     
				//else {printToLog("LJF Important Notice: No Representive Funds selected for this asset due to Roundtrip&& Waitingperiod Limit");}
				} 
		}
        else 
        {
            if(RepresentFundList.indexOf(SaveFundList.get(j))==-1) RepresentFundList.add(SaveFundList.get(j));//e.g, add "CASH" fund under "CASH" Asset Class
            TempWeight = FundPercentageMap.get(SaveFundList.get(j).trim());
            TempWeight += Weight;
            FundPercentageMap.put(SaveFundList.get(j).trim(), TempWeight);
            printToLog(SelectedAssets.get(i) + " buy target :  " + SaveFundList.get(j) + " Cumulative target percentage : " + TempWeight);
        }
}
AssetRepresentFundMap.put(SelectedAssets.get(i).trim(), RepresentFundList);
}   //End of SelectedAsset  treatment

/*LJF temp testing*/printToLog("AssetRepresentFundMap 1:"+AssetRepresentFundMap);
//    Aggregate the CASH from different Assets
for(int iAsset=0;iAsset<SelectedAssets.size();iAsset++){
	String AssetName=SelectedAssets.get(iAsset);
	if(!AssetName.equals("CASH")){
		List<String>RepreFundList=AssetRepresentFundMap.get(AssetName);
		int CASHFundIndex=RepreFundList.indexOf("CASH");
		if (CASHFundIndex==-1) continue;
		//Remove CASH from RepresentFundList
		RepreFundList.remove("CASH");
		AssetRepresentFundMap.put(AssetName,RepreFundList);
		
		double SumAssetPercent=0;
		for(int iSecurity=0;iSecurity<RepreFundList.size();iSecurity++){
			String FundSymbol=RepreFundList.get(iSecurity);
			SumAssetPercent+=FundPercentageMap.get(FundSymbol);
		}
		int CASHAssetIndex=SelectedAssets.indexOf("CASH");
		double AddOnCASH=SelectedAssetPercentages.get(iAsset)-SumAssetPercent;
		//Adjust CASH's AssetPercentage
SelectedAssetPercentages.set(CASHAssetIndex,SelectedAssetPercentages.get(CASHAssetIndex)+AddOnCASH);
		//Adjust iAsset's AssetPercentage
		SelectedAssetPercentages.set(iAsset,SumAssetPercent);
	}
}

//    Adjust FundPercentageMap for CanNotBuyFundList, caused by RoundTrip and Waiting Period
//    Adj for RoundTrip Part 4 LJF 2010.07.02 BEGIN
if(CanNotBuyFundList.size() != 0)
{
Object[] AdjustPercentageObject = AdjustTargetPercentage(CanNotBuyFundList, SelectedAssetPercentages, FundPercentageMap, AssetRepresentFundMap, SelectedAssets, MainAssetClassList);
SelectedAssetPercentages = (ArrayList<Double>)AdjustPercentageObject[0]; 
FundPercentageMap = (HashMap<String,Double>)AdjustPercentageObject[1]; 
}
printToLog("After Roundtrip adj , SelectedAssetPercentages :" + SelectedAssetPercentages);
printToLog("After Roundtrip adj , FundPercentageMap :" + FundPercentageMap);

//    Remove the selected asset with 0 percentage allocation
List<String> TempAssetList = SelectedAssets;
List<Double> TempAssetPercentages = SelectedAssetPercentages;
SelectedAssets = new ArrayList<String>();
SelectedAssetPercentages = new ArrayList<Double>();
for(int i = 0; i < TempAssetList.size(); i++)
{
    if(TempAssetPercentages.get(i) > 0)
    {
        SelectedAssets.add(TempAssetList.get(i));
        SelectedAssetPercentages.add(TempAssetPercentages.get(i));
    }
    else
        AssetRepresentFundMap.remove( TempAssetList.get(i));
}

/*LJF adj for 529 college saving plan's annual adjustment*/
//Check whether the portfolio belongs to 520 college saving plan: 
//Condition 1.if the answer is Yes: Trigger the adjustment  or endow SelectedAssets(and also PresentiveFunds, FundPercentageMap) with actual holding assets/PresentiveFunds.
//Condition 2.if the answer is NO : do nothing in this part 

//is529Plan = true;   /*JUST FOR TEST, please delete after testing*/
if(is529Plan) {                      
Object[] NewObject =compareTargetAndHoldingAssets(CandidateFund, SelectedAssets,SelectedAssetPercentages,FundPercentageMap, MainAssetClassList);
SelectedAssets=(ArrayList<String>)NewObject[0];
SelectedAssetPercentages=(ArrayList<Double>)NewObject[1];
FundPercentageMap=(HashMap<String,Double>)NewObject[2];
AssetRepresentFundMap=(HashMap<String,List<String>>)NewObject[3];
ToRebalance = (Integer)NewObject[4];
}

printToLog("To Rebalance = " + ToRebalance);

eTime6 = System.currentTimeMillis();
printToLog("Target Percentage Determination Time Cost:  " + (eTime6 - sTime6));

if(PlanID == 676)
    ToRebalance = 0;

if(ToRebalance == 1)
{
//=========================================================================================
/* -----------------------  Actual Trading Percentage Determination --------------*/

/*do not balance the portfolio funds that is representative again, except for the fix income funds */
String sn;
double sa;
double aa;
double Tempsa;
double TempTargetPercentage;
double TempActualHoldingPercentage;
List<String> RepresentFunds = null;
HoldingSecurityList = null;
double ta = CurrentPortfolio.getTotalAmount(CurrentDate);

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
    aa = AssetAvailableTradingPercentage.get(HoldingAssetList.get(i).getName().trim());
    for(int j = 0; j < HoldingSecurityList.size(); j++)
    {
        sn = HoldingSecurityList.get(j).getSymbol().trim();
        if(AssetRepresentFundMap.get(HoldingAssetList.get(i).getName().trim()) != null)
        {
            //printToLog("Existing selected asset : " + HoldingAssetList.get(i).getName());
            RepresentFunds = new ArrayList<String>();
            RepresentFunds = AssetRepresentFundMap.get(HoldingAssetList.get(i).getName().trim());
            for(int m = 0; m < RepresentFunds.size(); m++)
                if(sn.equals(RepresentFunds.get(m)))
                {
                 TempTargetPercentage = FundPercentageMap.get(sn);
                    TempActualHoldingPercentage = CurrentPortfolio.getSecurityAmount(HoldingAssetList.get(i).getName(), sn, CurrentDate)/ ta;
                    if(TempActualHoldingPercentage < TempTargetPercentage * 1.5)  // still rebalance when too far from the target percentage, added by WYJ on 2010.05.10
                    { 
                        sa = SecurityAvailableTradingPercentage.get(sn);
                        aa = AssetAvailableTradingPercentage.get(HoldingAssetList.get(i).getName().trim());
                        aa -= sa;
                        SecurityAvailableTradingPercentage.put(sn, 0.00);
                        AssetAvailableTradingPercentage.put(HoldingAssetList.get(i).getName().trim(), aa);
                    }
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
aa=0;

for(int i=0; i < SelectedAssetPercentages.size(); i++)
    AssetTargetPercentageMap.put(SelectedAssets.get(i).trim(), new Double(SelectedAssetPercentages.get(i)) );

for(int i = 0; i < HoldingAssetList.size(); i++)
{
    aa = CurrentPortfolio.getAssetAmount(HoldingAssetList.get(i).getName(),CurrentDate);
    HoldingAssetActualPercentage.put(HoldingAssetList.get(i).getName().trim(), new Double(aa/ta));
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
    AssetName = HoldingAssetList.get(i).getName().trim();
    ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
   AvailableTradingPercentage =  AssetAvailableTradingPercentage.get(AssetName).doubleValue();
    if(AssetTargetPercentageMap.get(AssetName.trim()) != null)
        TargetPercentage = AssetTargetPercentageMap.get(AssetName.trim()).doubleValue();
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
    AssetName =  SelectedAssets.get(i).trim();
    TargetPercentage = AssetTargetPercentageMap.get(AssetName.trim()).doubleValue();
    if(HoldingAssetActualPercentage.get(AssetName) != null)
        ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
    else ActualPercentage = 0;
    if(ActualPercentage <= TargetPercentage + 0.00001)
    {
        if((TargetPercentage - ActualPercentage)*BuyWeight > MinimumBuyingPercentage)
            AssetActualTradingPercentage.put(AssetName, new Double((TargetPercentage - ActualPercentage)*BuyWeight) );
        else if((TargetPercentage - ActualPercentage)*BuyWeight + UnableToBuyPercentage  > MinimumBuyingPercentage)
        {
            AssetActualTradingPercentage.put(AssetName, new Double((TargetPercentage - ActualPercentage)*BuyWeight) +  UnableToBuyPercentage);
            UnableToBuyPercentage = 0;
        }
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

printToLog("Asset Actual Trading Percentage (before adjusted 1) : " + AssetActualTradingPercentage);

if(BelowMinimum && AbleToBuyBoolean)
{
    int i = 0;
    while(UnableToBuyPercentage > 0 && i < SelectedAssets.size())
    {
        AssetName =  SelectedAssets.get(i).trim();
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

printToLog("Asset Actual Trading Percentage (before adjusted 2) : " + AssetActualTradingPercentage);

/*Calculate: use the left percentage to buy the asset with the max Target Percentage */
if(UnableToBuyPercentage >0)
{
    String MaxTargetPercentageAsset = SelectedAssets.get(0);
    double MaxTargetPercentage = AssetTargetPercentageMap.get(MaxTargetPercentageAsset.trim()).doubleValue();
    double CompareTargetPercentage;
    for(int i =1; i < SelectedAssets.size(); i++)
    {
        CompareTargetPercentage = AssetTargetPercentageMap.get(SelectedAssets.get(i).trim()).doubleValue();
        if(CompareTargetPercentage > MaxTargetPercentage)
        {
            double tmpTrading =  AssetActualTradingPercentage.get(SelectedAssets.get(i).trim());
            tmpTrading  +=  UnableToBuyPercentage;
            if(tmpTrading < -MinimumSellingPercentage || tmpTrading > MinimumBuyingPercentage)
            {
                MaxTargetPercentage = CompareTargetPercentage;
                MaxTargetPercentageAsset  = SelectedAssets.get(i);
            }
        }
    }
    double TempTrading =  AssetActualTradingPercentage.get(MaxTargetPercentageAsset).doubleValue();
    double NewActualTradingPercentage = TempTrading + UnableToBuyPercentage;
    if(NewActualTradingPercentage > -0.001 & NewActualTradingPercentage < 0.001) NewActualTradingPercentage = 0;
    AssetActualTradingPercentage.put(MaxTargetPercentageAsset, new Double(NewActualTradingPercentage));
}

for(int i = 0; i < SelectedAssets.size(); i++)
    printToLog(SelectedAssets.get(i) + " trading percentage = " + AssetActualTradingPercentage.get(SelectedAssets.get(i)).doubleValue());



/*Calculate trading percentage of each security, not rebalance*/

HashMap<String,HashMap<String, Double>>  AssetSecurityTradingPercentageMap = new  HashMap<String,HashMap<String, Double>>();
HashMap<String, Double>  SecurityActualTradingPercentage = new HashMap<String, Double>();
HashMap<String, Double>  SecurityTargetPercentageMap = new HashMap<String,Double>();
HashMap<String, Double>  HoldingSecurityActualPercentage = new HashMap<String,Double>();
HoldingSecurityList = null;
List<String> PresentativeSecurityList = null;
double TotalBuyingPercentage;
double SellingPercentage;
double BuyingPercentage;
double SecurityActualPercentage;
double SecurityTargetPercentage;
String SecurityName;
int[] NewAdd = new int[SelectedAssets.size()];
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
double UnTradePercentage = 0;

for(int i = 0; i < SelectedAssets.size(); i++)
{
    AssetName =  SelectedAssets.get(i).trim();
    HoldingSecurityList = new ArrayList<HoldingItem>();
    PresentativeSecurityList = new ArrayList<String>();
    PresentativeSecurityList = AssetRepresentFundMap.get(AssetName);
    TargetPercentage = AssetTargetPercentageMap.get(AssetName).doubleValue();
    double AssetActualTrading =  AssetActualTradingPercentage.get(AssetName);
    double LastUntrade = 0;

    if(HoldingAssetActualPercentage.get(AssetName) != null)
    {
        HoldingSecurityList = CurrentPortfolio.getAsset(AssetName).getHoldingItems();
        ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
        AvailableTradingPercentage =  AssetAvailableTradingPercentage.get(AssetName).doubleValue();
    }
    else  
        { HoldingSecurityList = null ; ActualPercentage = 0; AvailableTradingPercentage =0; NewAdd[i] = 1; }

    // added by WYJ on 2010.08.10, let the calculation take care of  the un-traded trading fraction of the former asset class 
    if(UnTradePercentage != 0){
        LastUntrade = UnTradePercentage;
        printToLog("adjust the asset target percentage and reset the UnTradePercentage : " + AssetName);
        TargetPercentage += UnTradePercentage; 
        UnTradePercentage = 0.0; 
    }

     //printToLog("i = " + i + "  ActualPercentage = " + ActualPercentage + "  TargetPercentage = " + TargetPercentage + "  AvailableTradingPercentage = " + AvailableTradingPercentage + "  AssetActualTradingPercentage = " + AssetActualTradingPercentage.get(AssetName).doubleValue() );

    if((AvailableTradingPercentage <= ActualPercentage - TargetPercentage  && AvailableTradingPercentage != 0) || PresentativeSecurityList == null || PresentativeSecurityList.size() == 0 && HoldingSecurityList != null)
    {
        SecurityActualTradingPercentage = new HashMap<String, Double>();
        for(int j = 0; j< HoldingSecurityList.size(); j++)
        {
            SellingPercentage  = SecurityAvailableTradingPercentage.get(HoldingSecurityList.get(j).getSymbol().trim());
            SecurityActualTradingPercentage.put(HoldingSecurityList.get(j).getSymbol().trim(), new Double(-SellingPercentage));
        }
        AssetSecurityTradingPercentageMap.put(AssetName.trim(), SecurityActualTradingPercentage);
        UnTradePercentage += LastUntrade;
    }
    else if(NewAdd[i] == 1)
    {
        SecurityActualTradingPercentage = new HashMap<String, Double>();
        BuyingPercentage = (AssetActualTradingPercentage.get(AssetName) + LastUntrade) / PresentativeSecurityList.size();      
        for(int j = 0; j< PresentativeSecurityList.size(); j++)
            SecurityActualTradingPercentage.put(PresentativeSecurityList.get(j), new Double(BuyingPercentage));
        AssetSecurityTradingPercentageMap.put(AssetName.trim(), SecurityActualTradingPercentage);        
    }
    else if(AvailableTradingPercentage > ActualPercentage - TargetPercentage)
    {
        SecurityActualTradingPercentage = new HashMap<String, Double>();
        for(int j = 0; j< HoldingSecurityList.size(); j++)
        {
            SellingPercentage  = SecurityAvailableTradingPercentage.get(HoldingSecurityList.get(j).getSymbol().trim());
            SecurityActualTradingPercentage.put(HoldingSecurityList.get(j).getSymbol().trim(), new Double(-SellingPercentage));
            printToLog("Only For Debug  -----  Selling all holdings Step :   " + HoldingSecurityList.get(j).getSymbol().trim() + "  Amount = " + TotalAmount*SellingPercentage );
        }
        TotalBuyingPercentage = AssetActualTradingPercentage.get(AssetName) + LastUntrade + AvailableTradingPercentage ;          
        //printToLog(" Actual Target Percentage = " + (TotalBuyingPercentage + ActualPercentage - AvailableTradingPercentage));
        int j=0;
        while(TotalBuyingPercentage > 0.001)
        {
             SecurityName = PresentativeSecurityList.get(j).trim();
             SecurityTargetPercentage = FundPercentageMap.get(SecurityName) + (LastUntrade / PresentativeSecurityList.size());
             if(SecurityAvailableTradingPercentage.get(SecurityName) == null)
                 { SecurityActualPercentage = 0; SellingPercentage = 0; }
             else
             {
                 SecurityActualPercentage = CurrentPortfolio.getSecurityAmount(AssetName, SecurityName, CurrentDate) / TotalAmount; 
                 SellingPercentage = SecurityAvailableTradingPercentage.get(SecurityName);
             }
             //printToLog("j = " + j + "  SecurityActualPercentage = " + SecurityActualPercentage + "  SellingPercentage = " + SellingPercentage + "  SecurityTargetPercentage = " + SecurityTargetPercentage  + "  TotalBuyingPercentage = " + TotalBuyingPercentage);
             if(SecurityActualPercentage - SellingPercentage >= SecurityTargetPercentage)
             {
                 //printToLog("condition 1");  
/*
                 if(j == PresentativeSecurityList.size() -1)      original before 2010.04.15
                     SecurityActualTradingPercentage.put(SecurityName, new Double(TotalBuyingPercentage - SellingPercentage));
                     TotalBuyingPercentage = 0;
*/
                 if(SellingPercentage == 0 &&  j != PresentativeSecurityList.size() -1)       /*modify on 2010.04.15*/
                      SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
                 else if(j == PresentativeSecurityList.size() -1)      
                 {
                     if(TotalBuyingPercentage - SellingPercentage < MinimumBuyingPercentage && TotalBuyingPercentage - SellingPercentage > -MinimumSellingPercentage)
                     {
                          UnTradePercentage += TotalBuyingPercentage - SellingPercentage;
                          SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
                     }
                     else
                         SecurityActualTradingPercentage.put(SecurityName, new Double(TotalBuyingPercentage - SellingPercentage));
                     TotalBuyingPercentage = 0;
                      //printToLog("condition 1-1");
                  }
                  else
                  {
                      if((-SellingPercentage) < MinimumBuyingPercentage && (-SellingPercentage) > -MinimumSellingPercentage)
                     {
                          UnTradePercentage += (-SellingPercentage);
                          SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
                     }
                     else
                           SecurityActualTradingPercentage.put(SecurityName, new Double(- SellingPercentage));
                       TotalBuyingPercentage -= SellingPercentage ;
                       //printToLog("condition 1-2");
                   }
             }
             else if(SecurityActualPercentage - SellingPercentage + TotalBuyingPercentage > SecurityTargetPercentage+ 0.001 && j < (PresentativeSecurityList.size() -1))
             {
                 //printToLog("condition 2"); 
                  if(SecurityTargetPercentage - SecurityActualPercentage < MinimumBuyingPercentage && SecurityTargetPercentage - SecurityActualPercentage > -MinimumSellingPercentage)
                 {
                      UnTradePercentage += (SecurityTargetPercentage - SecurityActualPercentage);
                      SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
                 }
                 else
                     SecurityActualTradingPercentage.put(SecurityName, new Double(SecurityTargetPercentage - SecurityActualPercentage));
                 TotalBuyingPercentage -= (SecurityTargetPercentage - SecurityActualPercentage + SellingPercentage);
             }
             else
             {
                 //printToLog("condition 3"); 
                 if(TotalBuyingPercentage - SellingPercentage < MinimumBuyingPercentage && TotalBuyingPercentage - SellingPercentage > -MinimumSellingPercentage && SecurityActualPercentage != -(TotalBuyingPercentage - SellingPercentage))
                 {
                      UnTradePercentage += (TotalBuyingPercentage - SellingPercentage);
                      SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
                 }
                 else
                     SecurityActualTradingPercentage.put(SecurityName, new Double(TotalBuyingPercentage - SellingPercentage));
                 TotalBuyingPercentage = 0;
             }
             //printToLog("SecurityActualTradingPercentage = " + SecurityActualTradingPercentage.get(SecurityName));
             j++;
        }
        AssetSecurityTradingPercentageMap.put(AssetName.trim(), SecurityActualTradingPercentage);     
    }
    else
    {
        UnTradePercentage += LastUntrade;  printToLog("No Change Allocation to the asset : " + AssetName);
    }
}
printToLog("un-trade percentage after security allocation = " + UnTradePercentage);
/*For Debug*/ printToLog("Actual trading Percentage Map (before adjusted) =  " +  AssetSecurityTradingPercentageMap);

/*Adjust one buying transaction for the UnTradePercentage  Added by WYJ on 2010.05.10 */

double TempPercentage;
boolean Done = false;
if(UnTradePercentage != 0);
{
Iterator iter1 = AssetSecurityTradingPercentageMap.keySet().iterator();
Iterator iter2 = null;
while(iter1.hasNext() && !Done)
{
    String Asset = (String)iter1.next();
    HashMap<String, Double> TempTradingPercentageMap = AssetSecurityTradingPercentageMap.get(Asset.trim());
    iter2 = TempTradingPercentageMap.keySet().iterator();
    while(iter2.hasNext() && !Done)
    {
        String Security = (String)iter2.next();
        TempPercentage = TempTradingPercentageMap.get(Security.trim());
        if(TempPercentage * UnTradePercentage >0 || TempPercentage == - UnTradePercentage )      // Modified on 2010.08.10, combine the fraction trading into the other trading with the same sign
        {
            TempPercentage += UnTradePercentage;
            TempTradingPercentageMap.put(Security.trim(), TempPercentage);
            AssetSecurityTradingPercentageMap.put(Asset.trim(), TempTradingPercentageMap);
            Done = true;
        }
    }
}
}

/*For Debug*/  printToLog("Actual trading Percentage Map (after adjusted) =  " +  AssetSecurityTradingPercentageMap);

 printToLog("Total Amount  =   "  +    TotalAmount );




/*Do the selling transaction*/

double TempRecord = CurrentPortfolio.getCash();

HoldingAssetList = CurrentPortfolio.getCurrentAssetList();
double SecurityCurrentAmount = 0;
Map<String, Double> SecurityCurrentAmountMap = new HashMap<String, Double>();
for(int i = 0; i < HoldingAssetList.size(); i++)
{
    HoldingSecurityList = new ArrayList<HoldingItem>();
    HoldingSecurityList = HoldingAssetList.get(i).getHoldingItems();
    for(int j = 0; j < HoldingSecurityList.size(); j++)
    {
        sn = HoldingSecurityList.get(j).getSymbol().trim();
        SecurityCurrentAmount = CurrentPortfolio.getSecurityAmount(HoldingAssetList.get(i).getName(), sn, CurrentDate);
        SecurityCurrentAmountMap.put(sn, SecurityCurrentAmount);
        printToLog("Asset : " + HoldingAssetList.get(i).getName() +  " Security Symbol: "+ sn + "CurrentAmount = " + SecurityCurrentAmount );
    }
}

//printToLog("SecurityActualTradingPercentageMap  = " + SecurityActualTradingPercentage);

int TranNum = 0;
for(int i = 0; i < SelectedAssets.size(); i++)
{
    AssetName = SelectedAssets.get(i).trim();
    SecurityActualTradingPercentage = new HashMap<String, Double>();
    SecurityActualTradingPercentage = AssetSecurityTradingPercentageMap.get(AssetName.trim());
    if(SecurityActualTradingPercentage != null)
    {
        Iterator iter = SecurityActualTradingPercentage.keySet().iterator();
        while (iter.hasNext()){
            String _fund = (String)iter.next();
            double _perc = SecurityActualTradingPercentage.get(_fund);
            if(_perc > 0.0001 || _perc < -0.0001)
                TranNum++; 
         }
    }
}

printToLog("Transaction number = " + TranNum);

long sTime4 = System.currentTimeMillis();

for(int i = 0; i < SelectedAssets.size(); i++)
{
    AssetName = SelectedAssets.get(i).trim();
    SecurityActualTradingPercentage = new HashMap<String, Double>();
    SecurityActualTradingPercentage = AssetSecurityTradingPercentageMap.get(AssetName.trim());
    if(SecurityActualTradingPercentage != null)
    {
        Iterator iter = SecurityActualTradingPercentage.keySet().iterator();
        while (iter.hasNext())
        {      
        String TradingSecurity = (String)iter.next(); 
        double TradePercentage = SecurityActualTradingPercentage.get(TradingSecurity).doubleValue(); 
        //printToLog("To sell Asset : " +AssetName + "SellingSecurity : "  + TradingSecurity +"   $ "+TotalAmount*(-TradePercentage));
        if(TranNum > 1 && TradePercentage < -0.0001){        // ignore too small CASH selling 
          try{
            double CurrentAmount = SecurityCurrentAmountMap.get(TradingSecurity).doubleValue();
            double TradeAmount = (TradePercentage*TotalAmount) > (-CurrentAmount) ? (TradePercentage*TotalAmount):(-CurrentAmount);
            CurrentPortfolio.sellAtNextOpen(AssetName, TradingSecurity,-TradeAmount, CurrentDate);
            printToLog("Sell Signal: "+AssetName+"  "+TradingSecurity+"  $ "+ -TradeAmount);
            TempRecord += (-TradeAmount);
            }
         catch(Exception e){
            printToLog("Error: Failed selling  "+AssetName+"  "+TradingSecurity+"  $ "+TotalAmount*(-TradePercentage));
            }
          }
        }
    }
}

/*Do the buying transaction*/

for(int i = 0; i < SelectedAssets.size(); i++)
{
    AssetName = SelectedAssets.get(i).trim();
    SecurityActualTradingPercentage = new HashMap<String, Double>();
    SecurityActualTradingPercentage = AssetSecurityTradingPercentageMap.get(AssetName.trim());
    if(SecurityActualTradingPercentage != null)
    {
        Iterator iter = SecurityActualTradingPercentage.entrySet().iterator();
        while (iter.hasNext())
        {
        Map.Entry entry = (Map.Entry) iter.next();         
        String TradingSecurity = (String)entry.getKey(); 
        double TradePercentage = (Double)entry.getValue(); 
        if(TranNum > 1 && TradePercentage > 0.0001){
            double TradeAmount =  (TotalAmount * TradePercentage) < TempRecord?  (TotalAmount * TradePercentage):TempRecord;
            if(TempRecord - TradeAmount < MinimumBuyingPercentage * TotalAmount)
                TradeAmount = TempRecord;
            CurrentPortfolio.buyAtNextOpen(AssetName, TradingSecurity,TradeAmount, CurrentDate,true);
            printToLog("Buy Signal: "+AssetName+"  "+TradingSecurity+"  $ "+ TradeAmount);
            if(TradeAmount < MinimumBuyingPercentage * TotalAmount)
               printToLog("Error: the Schedule transaction is below the trading threshold !");
            TempRecord -= TradeAmount;
            }
        }
    }
}

printToLog("The difference between sell amount and buy amount = " +  TempRecord);

if(NewYear)  NewYear = false;

long eTime4 = System.currentTimeMillis();
printToLog("Trading Time Cost:  " + (eTime4 - sTime4));

} // End of "if(ToRebalance == 1)"

long eTime = System.currentTimeMillis();
printToLog("Action Time Cost:  " + (eTime - sTime));


//long sTime7 = System.currentTimeMillis();
		}
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Tactical_Asset_Allocation_for_old_engine_backup_09204826.java:378: reinit() in com.lti.type.executor.SimulateStrategy cannot be applied to (com.lti.service.bo.Portfolio,java.util.Hashtable<java.lang.String,java.lang.String>,java.util.Date)
//                 super.reinit(portfolio,parameters,pdate);
//                      ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Tactical_Asset_Allocation_for_old_engine_backup_09204826.java:376: method does not override or implement a method from a supertype
//@Override
//^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Tactical_Asset_Allocation_for_old_engine_backup_09204826.java:930: incompatible types
//found   : java.util.Map<java.lang.String,java.lang.String>
//required: java.util.List<com.lti.type.Pair>
//                                   attrs=_plan.getAttributes();
//                                                            ^
//3 errors