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
public class Tactical_Asset_Allocation_scale_backup_08154712 extends SimulateStrategy{
	public Tactical_Asset_Allocation_scale_backup_08154712(){
		super();
		StrategyID=4712L;
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
		MainAssetClass=(String[])ParameterUtil.fetchParameter("String[]","MainAssetClass", "US EQUITY, INTERNATIONAL EQUITY, FIXED INCOME, REAL ESTATE, COMMODITIES, Emerging Market, INTERNATIONAL BONDS, High Yield Bond, CASH", parameters);
		NumberOfMainRiskyClass=(Integer)ParameterUtil.fetchParameter("int","NumberOfMainRiskyClass", "2", parameters);
		NumberOfMainStableClass=(Integer)ParameterUtil.fetchParameter("int","NumberOfMainStableClass", "1", parameters);
		RiskProfile=(Double)ParameterUtil.fetchParameter("double","RiskProfile", "30", parameters);
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
		stream.writeObject(InAfterDateFilter);
		stream.writeObject(TooVolatile);
		stream.writeObject(NewYear);
		stream.writeObject(LastActionMonth);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		RiskyAllocation=(Double)stream.readObject();;
		StableAllocation=(Double)stream.readObject();;
		InAfterDateFilter=(int[])stream.readObject();;
		TooVolatile=(int[])stream.readObject();;
		NewYear=(Boolean)stream.readObject();;
		LastActionMonth=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	private double version=1.12;
@Override
public void reinit(com.lti.service.bo.Portfolio portfolio, Hashtable<String, String> parameters, Date pdate) throws Exception {
                 super.reinit(portfolio,parameters,pdate);
                printToLog("--------------  Re-initial to version " + version +  " :  "+ CurrentDate + "   ----------------");
  
    StableAllocation = 0;
    RiskyAllocation = 0;
    LastActionMonth =13;    // to let NewYear to be true  when calculated in the common action
    NewYear = true;
    

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

    InAfterDateFilter = new int[k];
    TooVolatile = new int[k];
    for(int i = 0; i < k; i++)
    {
        InAfterDateFilter[i] = 0;
        TooVolatile[i] = 0;
    }

                printToLog("--------------  Re-initial Finish, TAA strategy update to version " + version + " -------------------");
}


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

// printToLog("Original Score: "+Securities[i]+" : "+a);

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
		java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
String dateStr=df.format(CurrentDate);
long sTime = System.currentTimeMillis();

printToLog(PlanID);
boolean HaveDataFile = true;
boolean HaveData = true;
List<String> SelectedAssets = null;
List<Double> SelectedAssetPercentages = null;
List<String> RepresentFundList = null;
HashMap<String, List<String>> AssetRepresentFundMap = null;
HashMap<String, Double> FundPercentageMap = null;
Object[] ReadObj = null;

try{ReadObj = (Object[])com.lti.util.PersistentUtil.readObject("test TAA scaling all 4");}
    catch(Exception e1){HaveDataFile = false; HaveData = false;}

if(HaveDataFile)
{
    List<String> RiskyGroupSelect = (ArrayList<String>)ReadObj[0];
    if(RiskyGroupSelect.indexOf(dateStr) == -1)
        HaveData = false;
}

if(!HaveData)
{
   printToLog("------------------First Time Calculation ,  for data preparation----------------------------");

List<String> CandidateFundList = new ArrayList<String>();
List<String> ShortHistoryFundList = new ArrayList<String>();
List<String> MainAssetClassList = new ArrayList<String>();

Calendar tmpCa = Calendar.getInstance();
tmpCa.setTime(CurrentDate);
LastActionMonth = tmpCa.get(Calendar.MONTH);  

StableAllocation = RiskProfile;
RiskyAllocation = 100 - RiskProfile;

/*To match the TAA on VaildFi.com when Risk Profile = 0, always select 3 asset unless CASH out performance
if(RiskProfile == 0)    
{
    NumberOfMainRiskyClass = 3;
    NumberOfMainStableClass = 3;
    MaxOfRiskyAsset  =  1;
    MaxOfStableAsset = 1;
}
*/

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
TooVolatile = new int[CandidateFund.length];
//long sTime = System.currentTimeMillis();
boolean HaveCash = false;
double tempprice1; double  tempprice2;
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
    if(Fund.getIsClosed() && LTIDate.equals(Fund.getEndDate(),CurrentDate) )
        continue;
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
				{PutThisFund = false; TooVolatile[i] = 1;printToLog("Too Volatile ; exclude "+ CandidateFund[i]); continue;} 
        }
        else
             printToLog("Benchmark's history prices is not long enough. " + CandidateFund[i] +" is included no matter of SD");

        if (PutThisFund && Fund.getSecurityType() == 1){     //Omit those ETF that Average 3-Month Volume are less than 200k;Configuration.SECURITY_TYPE_ETF=1;
            int AverageVolumeMonth = 3;
            Long Volume=0L;
            Date EndDate=CurrentDate;
            for (int j=0;j<AverageVolumeMonth;j++){
                Volume=Volume+getMonthVolume(Fund.getID(),EndDate);
                EndDate=LTIDate.getNewNYSEMonth(EndDate,-(j+1));
            }
            Volume=Volume/AverageVolumeMonth;
           if (Volume<200000) {PutThisFund = false;/*Testing*/printToLog("Too small volume ;  exclude " + CandidateFund[i]);}
       }

        if ( PutThisFund ) {CandidateFundList.add(CandidateFund[i]);}
    }
    //Special Treatment towards ShortHistoryFunds , Added by WYJ on 2010.05.13
    else if( LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), CurrentDate) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -12)) && BenchHasPrice)
    {
	if((getSecurity(CandidateFund[i])).getAssetClass()!=null)
             {CandidateFundList.add(CandidateFund[i]); ShortHistoryFundList.add(CandidateFund[i]);printToLog("Using BenchMark : " +  CandidateFund[i]);}
    }

    if(i == CandidateFund.length -1 && HaveCash == false)
        {CandidateFundList.add("CASH"); HaveCash = true;}	
}

if(CandidateFundList.indexOf("CASH") == -1)
    CandidateFundList.add("CASH");

printToLog("CandidateFundList initialize ok");
//long eTime = System.currentTimeMillis();
//System.out.println("Selection Time Cost:  " + (eTime - sTime));


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

/*
for(int i =0; i <RiskyAssets.length;i++)
{ printToLog("RiskyAssets  " + RiskyAssets[i]);}

for(int i =0; i < StableAssets.length;i++)
{ printToLog("StableAssets  " + StableAssets[i]);}
*/
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
/*
for(int i = 0; i < AvailableAssets.length; i++)
    printToLog(AvailableAssets[i] + "'s score = " + Score[i]);
*/
List<String> StableGroupSelect = getSelectedAssetList(Score, AvailableAssets, StableAssets, SelectedPercentage, NumberOfMainRiskyClass, NumberOfMainStableClass, CurrentDate);

log_m="Stable Group selection" + CurrentDate.toString()+"\r\n";
for(int i=0;i<StableGroupSelect.size();i++)
        log_m+=StableGroupSelect.get(i) + "\r\n";
printToLog(log_m);




/*combine selected assets and determine the asset percentages, modified by WYJ in 2010.07*/

boolean Add = true;
double StablePerc = 0;
SelectedAssetPercentages = new ArrayList<Double>();
for(int i=0;i<RiskyGroupSelect.size();i++)
    SelectedAssetPercentages.add(RiskyAllocation/RiskyGroupSelect.size()/100);

SelectedAssets = new ArrayList<String>();
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

/* make neccesary diversification of the stable asset classes, Added by WYJ on 2010.05.08 */   

if(SelectedAssets.size() == 1)
    MaxOfStableAsset = 3;
else if(SelectedAssets.size() == 2)
    MaxOfStableAsset = 2;

double EachStableFundPec = StablePerc / MaxOfStableAsset ;
if(EachStableFundPec > 0.67)
    MaxOfStableAsset +=2;
else if(EachStableFundPec > 0.34)
    MaxOfStableAsset +=1;

     //Add CASH into the stable asset class, Add duplicate safe stable funds into the stable classes, modified by WYJ on 2010.05.10 

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
   
    
    

/*Select preferred funds for the selected assets*/

if(SelectedAssets.indexOf("CASH") < 0)
{
    SelectedAssets.add("CASH");
    SelectedAssetPercentages.add(0.0);
}

// initial the saved sorted fund lists, 2010.07
List<String> usequity = null;
List<String> internationalequity = null;
List<String> fixedincome = null;
List<String> realestate = null;
List<String> commodities = null;
List<String> emergingmarket = null;
List<String> internationalbonds =null;
List<String> highyieldbond = null;
List<String> balancefund = null;
if(!HaveDataFile)
{
    usequity = new ArrayList<String>();
    internationalequity = new ArrayList<String>();
    fixedincome = new ArrayList<String>();
    realestate = new ArrayList<String>();
    commodities = new ArrayList<String>();
    emergingmarket = new ArrayList<String>();
    internationalbonds = new ArrayList<String>();
    highyieldbond = new ArrayList<String>();
    balancefund = new ArrayList<String>();
}
else
{
    usequity = (ArrayList<String>)ReadObj[2];
    internationalequity = (ArrayList<String>)ReadObj[3];
    fixedincome = (ArrayList<String>)ReadObj[4];
    realestate = (ArrayList<String>)ReadObj[5];
    commodities = (ArrayList<String>)ReadObj[6];
    emergingmarket = (ArrayList<String>)ReadObj[7];
    internationalbonds = (ArrayList<String>)ReadObj[8];
    highyieldbond = (ArrayList<String>)ReadObj[9];
    balancefund = (ArrayList<String>)ReadObj[10];
}
usequity.add(dateStr);
internationalequity.add(dateStr);
fixedincome.add(dateStr);
realestate.add(dateStr);
commodities.add(dateStr);
emergingmarket.add(dateStr);
internationalbonds.add(dateStr);
highyieldbond.add(dateStr);
balancefund.add(dateStr);

AssetRepresentFundMap = new HashMap<String, List<String>>();
FundPercentageMap = new HashMap<String, Double>();
HashMap<String, String> PseudoMap = new HashMap<String, String>();
List<String> FundList = null;
RepresentFundList = null;
String[] Funds = null;
int RepresentNumber;
int SaveNumber;
double Weight;
for(int i = 0; i < SelectedAssets.size(); i++)
{
    FundList  = new ArrayList<String>();
    FundList  = AllAssetFundsMap.get(SelectedAssets.get(i).trim() );
    Funds = new String[FundList.size()];
    Score = new double[FundList.size()];
    if(SelectedAssets.get(i).equals("FIXED INCOME") || SelectedAssets.get(i).equals("INTERNATIONAL BONDS") )
        RepresentNumber = MaxOfStableAsset;
    else if(SelectedAssets.get(i).equals("CASH"))
        RepresentNumber = 1;
    else 
        RepresentNumber = MaxOfRiskyAsset;
    RepresentNumber = RepresentNumber < Funds.length?RepresentNumber : Funds.length;
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
for(int l=0; l<=SaveNumber -1; l++)
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
double TempWeight;
if(!FundPercentageMap.containsKey("CASH"))
    FundPercentageMap.put("CASH", 0.0);

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
        usequity.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL EQUITY"))
{
    for(int j = 0; j < SaveNumber; j++)
        internationalequity.add(Funds[j]);    
}
else if(SelectedAssets.get(i).trim().equals("FIXED INCOME"))
{
    for(int j = 0; j < SaveNumber; j++)
        fixedincome.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("REAL ESTATE"))
{
    for(int j = 0; j < SaveNumber; j++)
        realestate.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("COMMODITIES"))
{
    for(int j = 0; j < SaveNumber; j++)
        commodities.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("Emerging Market"))
{
    for(int j = 0; j < SaveNumber; j++)
        emergingmarket.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL BONDS"))
{
    for(int j = 0; j < SaveNumber; j++)
        internationalbonds.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("High Yield Bond"))
{
    for(int j = 0; j < SaveNumber; j++)
        highyieldbond.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("BALANCE FUND"))
{
    for(int j = 0; j < SaveNumber; j++)
        balancefund.add(Funds[j]);
}

/*aggregate the same representative funds, Added by WYJ on 2010.05.08, modified on 05.10*/
Weight = SelectedAssetPercentages.get(i) /RepresentNumber; 
for(int j = 0; j < RepresentNumber; j++)  
{
    if(!Funds[j].equals("CASH"))
    {
        if(RepresentFundList.indexOf(Funds[j]) == -1)
        {
            RepresentFundList.add(Funds[j] );
            FundPercentageMap.put(Funds[j].trim(), Weight);
            printToLog(SelectedAssets.get(i) + " buy target :  " + Funds[j] + "   target percentage : " + Weight);
        }
        else 
        {
            TempWeight = FundPercentageMap.get(Funds[j].trim());
            TempWeight += Weight;
            FundPercentageMap.put(Funds[j].trim(), TempWeight);
            printToLog(SelectedAssets.get(i) + " buy target :  " + Funds[j] + " Cumulative target percentage : " + TempWeight);
        }
    }
    else if(Funds[j].equals("CASH"))
    { 
        TempWeight = FundPercentageMap.get("CASH");
        TempWeight += Weight;
        FundPercentageMap.put("CASH", TempWeight);

        if(SelectedAssets.get(i).equals("CASH") && RepresentFundList.indexOf("CASH") == -1)      
        {
            RepresentFundList.add("CASH");
        }
        if(!SelectedAssets.get(i).equals("CASH"))
        {
            TempWeight = SelectedAssetPercentages.get(i);
            TempWeight  -= Weight;
            SelectedAssetPercentages.set(i, TempWeight);

            TempWeight = SelectedAssetPercentages.get(SelectedAssets.indexOf("CASH") );
            TempWeight  += Weight;
            SelectedAssetPercentages.set(SelectedAssets.indexOf("CASH"), TempWeight);
        }   
    }
}

if(i == SelectedAssets.size()-1)
  printToLog("CASH Buying Target Percentage : " + SelectedAssetPercentages.get(SelectedAssets.indexOf("CASH")));

AssetRepresentFundMap.put(SelectedAssets.get(i).trim(), RepresentFundList);
}

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


/*save the selected asset classes and the sorted fund lists, write objects, 2010.07*/

List<String> SaveRiskyGroupSelect = null;
List<String> SaveStableGroupSelect = null;
if(!HaveDataFile)
{   
    SaveRiskyGroupSelect = new ArrayList<String>();
    SaveStableGroupSelect = new ArrayList<String>();
}
else
{
    SaveRiskyGroupSelect = (ArrayList<String>)ReadObj[0];
    SaveStableGroupSelect = (ArrayList<String>)ReadObj[1];
}
SaveRiskyGroupSelect.add(dateStr);
SaveStableGroupSelect.add(dateStr);
for(int i = 0; i < RiskyGroupSelect.size(); i++)
    SaveRiskyGroupSelect.add(RiskyGroupSelect.get(i));
for(int i = 0; i < StableGroupSelect.size(); i++)
    SaveStableGroupSelect.add(StableGroupSelect.get(i));

Object[] Ob = new Object[]{SaveRiskyGroupSelect, SaveStableGroupSelect, usequity, internationalequity, fixedincome, realestate, commodities, emergingmarket,  internationalbonds, highyieldbond, balancefund};

com.lti.util.PersistentUtil.writeObject(Ob,"test TAA scaling all 4");

}   //End of the first try catch, which is done when calculating for the first time




if(HaveData)   //  After calculate the first portfolio of the plan
{
    printToLog("--------------------  Scaling Calculation ----------------------------");
    //ReadObj = (Object[])com.lti.util.PersistentUtil.readObject("test TAA scaling all 4");
    StableAllocation = RiskProfile;
    RiskyAllocation = 100 - RiskProfile;
    int RepresentNumber = 0;
    AssetRepresentFundMap = new HashMap<String, List<String>>(); 

    // Read Saved List for Scale Calculation
    List<String> SaveRiskyGroupSelect = (ArrayList<String>)ReadObj[0];
    List<String> SaveStableGroupSelect = (ArrayList<String>)ReadObj[1];
    List<String> Saveusequity = (ArrayList<String>)ReadObj[2];
    List<String> Saveinternationalequity = (ArrayList<String>)ReadObj[3];
    List<String> Savefixedincome = (ArrayList<String>)ReadObj[4];
    List<String> Saverealestate = (ArrayList<String>)ReadObj[5];
    List<String> Savecommodities = (ArrayList<String>)ReadObj[6];
    List<String> Saveemergingmarket = (ArrayList<String>)ReadObj[7];
    List<String> Saveinternationalbonds = (ArrayList<String>)ReadObj[8];
    List<String> Savehighyieldbond = (ArrayList<String>)ReadObj[9];
    List<String> Savebalancefund = (ArrayList<String>)ReadObj[10];

    List<String> RiskyGroupSelect = getSaveListOfThisAction(SaveRiskyGroupSelect, dateStr);
    List<String> StableGroupSelect = getSaveListOfThisAction(SaveStableGroupSelect, dateStr);
    List<String> usequity = new ArrayList<String>();
    List<String> internationalequity = new ArrayList<String>();
    List<String> fixedincome = new ArrayList<String>();
    List<String> realestate = new ArrayList<String>();
    List<String> commodities = new ArrayList<String>();
    List<String> emergingmarket = new ArrayList<String>();
    List<String> internationalbonds = new ArrayList<String>();
    List<String> highyieldbond = new ArrayList<String>();
    List<String> balancefund = new ArrayList<String>();

/*
for(int i = 0; i < RiskyGroupSelect.size(); i++)
{
    printToLog("Risky Group selected asset " + (i+1) + "  : " + RiskyGroupSelect.get(i));
}
for(int i = 0; i < StableGroupSelect.size(); i++)
{
    printToLog("Stable Group selected asset " + (i+1) + "  : " + StableGroupSelect.get(i));
}
*/

/*combine selected assets and determine the asset percentages, modified by WYJ in 2010.07*/
boolean Add = true;
double StablePerc = 0;
SelectedAssetPercentages = new ArrayList<Double>();
for(int i=0;i<RiskyGroupSelect.size();i++)
    SelectedAssetPercentages.add(RiskyAllocation/RiskyGroupSelect.size()/100);

SelectedAssets = new ArrayList<String>();
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

// get the subList of this action to calculate
if(SelectedAssets.indexOf("US EQUITY") > -1)
    usequity = getSaveListOfThisAction(Saveusequity, dateStr);
if(SelectedAssets.indexOf("INTERNATIONAL EQUITY") > -1)
    internationalequity = getSaveListOfThisAction(Saveinternationalequity, dateStr);
if(SelectedAssets.indexOf("FIXED INCOME") > -1)
    fixedincome = getSaveListOfThisAction(Savefixedincome, dateStr);
if(SelectedAssets.indexOf("REAL ESTATE") > -1)
    realestate = getSaveListOfThisAction(Saverealestate, dateStr);
if(SelectedAssets.indexOf("COMMODITIES") > -1)
    commodities = getSaveListOfThisAction(Savecommodities, dateStr);
if(SelectedAssets.indexOf("Emerging Market") > -1)
    emergingmarket = getSaveListOfThisAction(Saveemergingmarket, dateStr);
if(SelectedAssets.indexOf("High Yield Bond") > -1)
    highyieldbond = getSaveListOfThisAction(Savehighyieldbond, dateStr);
if(SelectedAssets.indexOf("INTERNATIONAL BONDS") > -1)
    internationalbonds = getSaveListOfThisAction(Saveinternationalbonds, dateStr);
if(SelectedAssets.indexOf("BALANCE FUND") > -1)
    balancefund = getSaveListOfThisAction(Savebalancefund, dateStr);


/* make neccesary diversification of the stable asset classes, Added by WYJ on 2010.05.08 */   

if(SelectedAssets.size() == 1)
    MaxOfStableAsset = 3;
else if(SelectedAssets.size() == 2)
    MaxOfStableAsset = 2;

double EachStableFundPec = StablePerc / MaxOfStableAsset ;         // need to be improved, WYJ
if(EachStableFundPec > 0.67)
    MaxOfStableAsset +=2;
else if(EachStableFundPec > 0.34)
    MaxOfStableAsset +=1;

printToLog("The max number of selected funds in the stable asset class = " + MaxOfStableAsset);

//Add CASH after diversify
if(SelectedAssets.indexOf("CASH") < 0)
{
    SelectedAssets.add("CASH");
    SelectedAssetPercentages.add(0.0);
}

/*to  form preferred fund list*/

double Weight = 0;
List<String>  SaveFundList = null;
RepresentFundList = null;
FundPercentageMap = new HashMap<String, Double>();
for(int i = 0; i < SelectedAssets.size(); i ++)     // start of selected asset treatment
{
    if(SelectedAssets.get(i).trim().equals("US EQUITY"))
        SaveFundList = usequity;
    else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL EQUITY"))
        SaveFundList = internationalequity;
    else if(SelectedAssets.get(i).trim().equals("FIXED INCOME"))
        SaveFundList = fixedincome;
    else if(SelectedAssets.get(i).trim().equals("REAL ESTATE"))
        SaveFundList = realestate;
    else if(SelectedAssets.get(i).trim().equals("COMMODITIES"))
        SaveFundList = commodities;
    else if(SelectedAssets.get(i).trim().equals("Emerging Market"))
        SaveFundList = emergingmarket;
    else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL BONDS"))
        SaveFundList = internationalbonds;
    else if(SelectedAssets.get(i).trim().equals("High Yield Bond"))
        SaveFundList = highyieldbond;
    else if(SelectedAssets.get(i).trim().equals("BALANCE FUND"))
        SaveFundList = balancefund;
    else if(SelectedAssets.get(i).trim().equals("CASH"))
    {
        SaveFundList = new ArrayList<String>();
        SaveFundList.add("CASH");
    }
    printToLog("Saved fund list size of " + SelectedAssets.get(i) + " = " + SaveFundList.size());

    if(SelectedAssets.get(i).equals("FIXED INCOME") || SelectedAssets.get(i).equals("INTERNATIONAL BONDS") )
        RepresentNumber = MaxOfStableAsset;
    else if(SelectedAssets.get(i).equals("CASH"))
        RepresentNumber = 1;
    else 
        RepresentNumber = MaxOfRiskyAsset;
    RepresentNumber = RepresentNumber < SaveFundList.size()?RepresentNumber : SaveFundList.size();
    printToLog("The number of selected funds = " + RepresentNumber);

    RepresentFundList = new ArrayList<String>();
    double TempWeight;
    if(!FundPercentageMap.containsKey("CASH"))
        FundPercentageMap.put("CASH", 0.0);        

/*aggregate the same representative funds, Added by WYJ on 2010.05.08, modified on 2010.07*/
Weight = SelectedAssetPercentages.get(i) /RepresentNumber; 
for(int j = 0; j < RepresentNumber; j++)  
{
    if(!SaveFundList.get(j).equals("CASH"))
    {
        if(RepresentFundList.indexOf( SaveFundList.get(j) ) == -1)
        {
            RepresentFundList.add(SaveFundList.get(j) );
            FundPercentageMap.put(SaveFundList.get(j).trim(), Weight);
            printToLog(SelectedAssets.get(i) + " buy target :  " + SaveFundList.get(j) + "   target percentage : " + Weight);
        }
        else 
        {
            TempWeight = FundPercentageMap.get(SaveFundList.get(j).trim());
            TempWeight += Weight;
            FundPercentageMap.put(SaveFundList.get(j).trim(), TempWeight);
            printToLog(SelectedAssets.get(i) + " buy target :  " + SaveFundList.get(j) + " Cumulative target percentage : " + TempWeight);
        }
    }
    else if(SaveFundList.get(j).equals("CASH"))
    { 
        TempWeight = FundPercentageMap.get("CASH");
        TempWeight += Weight;
        FundPercentageMap.put("CASH", TempWeight);

        if(SelectedAssets.get(i).equals("CASH") && RepresentFundList.indexOf("CASH") == -1)      
        {
            RepresentFundList.add("CASH");
        }
        if(!SelectedAssets.get(i).equals("CASH"))
        {
            TempWeight = SelectedAssetPercentages.get(i);
            TempWeight  -= Weight;
            SelectedAssetPercentages.set(i, TempWeight);

            TempWeight = SelectedAssetPercentages.get(SelectedAssets.indexOf("CASH") );
            TempWeight  += Weight;
            SelectedAssetPercentages.set(SelectedAssets.indexOf("CASH"), TempWeight);
        }   
    }
}  // End of aggregate the same funds

AssetRepresentFundMap.put(SelectedAssets.get(i).trim(), RepresentFundList);
}  // end of selected asset treatment 

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


}//  end of "HaveDate" calculation for non-first time monitor




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
	
		
		boolean ToCheck = false;
HashMap<String,Integer> RedemptionLimitMap = new HashMap<String,Integer>(); 
List<Asset> HoldingAssetList =  new ArrayList<Asset>();
HashMap<String,List<HoldingItem>> HoldingAssetSecurityMap = new HashMap<String,List<HoldingItem>>();
HashMap<String,Double>  SecurityAvailableTradingPercentage = new HashMap<String,Double>();
HashMap<String,Double>  AssetAvailableTradingPercentage = new HashMap<String,Double>();

//printToLog("Small cash in the portfolio  =  " +  CurrentPortfolio.getCash());
//try{
if((CheckFrequency.equals("monthly")&&LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) || (CheckFrequency.equals("quarterly")&&LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate)) || 
(CheckFrequency.equals("yearly")&&LTIDate.isLastNYSETradingDayOfYear(CurrentDate)))
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
		   java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
String dateStr=df.format(CurrentDate);
long sTime = System.currentTimeMillis();

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

try{ReadObj = (Object[])com.lti.util.PersistentUtil.readObject("test TAA scaling all 4");}
    catch(Exception e1){HaveDataFile = false; HaveData = false;}

if(HaveDataFile)
{
    List<String> RiskyGroupSelect = (ArrayList<String>)ReadObj[0];
    if(RiskyGroupSelect.indexOf(dateStr) == -1)
        HaveData = false;
}

if(!HaveData)
{
   printToLog("------------------First Time Calculation ,  for data preparation----------------------------");

List<String> CandidateFundList = new ArrayList<String>();
List<String> ShortHistoryFundList = new ArrayList<String>();
List<String> MainAssetClassList = new ArrayList<String>();

Calendar tmpCa = Calendar.getInstance();
tmpCa.setTime(CurrentDate);
LastActionMonth = tmpCa.get(Calendar.MONTH);  

StableAllocation = RiskProfile;
RiskyAllocation = 100 - RiskProfile;

/*To match the TAA on VaildFi.com when Risk Profile = 0, always select 3 asset unless CASH out performance
if(RiskProfile == 0)    
{
    NumberOfMainRiskyClass = 3;
    NumberOfMainStableClass = 3;
    MaxOfRiskyAsset  =  1;
    MaxOfStableAsset = 1;
}
*/

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

/*Choose the available candidate funds LJF Changed calculation of MPT 2010.03.16*/
Security Fund = null;
Security Bench= null;

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
HoldingSecurityList = null;
boolean isHolding = false;
for(int i=0; i< CandidateFund.length; i++)
{
    BenchHasPrice = true;
    if(CandidateFund[i].equals("CASH"))
        {CandidateFundList.add("CASH"); HaveCash = true; continue;}
    boolean PutThisFund=true;
    Fund = getSecurity(CandidateFund[i]);
    if(Fund == null) continue;
    if(Fund.getIsClosed() && LTIDate.equals(Fund.getEndDate(),CurrentDate) )
        continue;
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
             {CandidateFundList.add(CandidateFund[i]); ShortHistoryFundList.add(CandidateFund[i]);}
    }

    if(i == CandidateFund.length -1 && HaveCash == false)
        {CandidateFundList.add("CASH"); HaveCash = true;}	
}

if(CandidateFundList.indexOf("CASH") == -1)
    CandidateFundList.add("CASH");


printToLog("CandidateFundList initialize ok");
//long eTime = System.currentTimeMillis();
//System.out.println("Selection Time Cost:  " + (eTime - sTime));

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

/*
for(int i =0; i <RiskyAssets.length;i++)
{ printToLog("RiskyAssets  " + RiskyAssets[i]);}

for(int i =0; i < StableAssets.length;i++)
{ printToLog("StableAssets  " + StableAssets[i]);}
*/
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
/*
for(int i = 0; i < AvailableAssets.length; i++)
    printToLog(AvailableAssets[i] + "'s score = " + Score[i]);
*/
List<String> StableGroupSelect = getSelectedAssetList(Score, AvailableAssets, StableAssets, SelectedPercentage, NumberOfMainRiskyClass, NumberOfMainStableClass, CurrentDate);

log_m="Stable Group selection" + CurrentDate.toString()+"\r\n";
for(int i=0;i<StableGroupSelect.size();i++)
        log_m+=StableGroupSelect.get(i) + "\r\n";
printToLog(log_m);




/*combine selected assets and determine the asset percentages, modified by WYJ in 2010.07*/

boolean Add = true;
double StablePerc = 0;
SelectedAssetPercentages = new ArrayList<Double>();
for(int i=0;i<RiskyGroupSelect.size();i++)
    SelectedAssetPercentages.add(RiskyAllocation/RiskyGroupSelect.size()/100);

SelectedAssets = new ArrayList<String>();
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

/* make neccesary diversification of the stable asset classes, Added by WYJ on 2010.05.08 */   

if(SelectedAssets.size() == 1)
    MaxOfStableAsset = 3;
else if(SelectedAssets.size() == 2)
    MaxOfStableAsset = 2;

double EachStableFundPec = StablePerc / MaxOfStableAsset ;
if(EachStableFundPec > 0.67)
    MaxOfStableAsset +=2;
else if(EachStableFundPec > 0.34)
    MaxOfStableAsset +=1;

     //Add CASH into the stable asset class, Add duplicate safe stable funds into the stable classes, modified by WYJ on 2010.05.10 

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
   
    
    

/*Select preferred funds for the selected assets*/

if(SelectedAssets.indexOf("CASH") < 0)
{
    SelectedAssets.add("CASH");
    SelectedAssetPercentages.add(0.0);
}

// initial the saved sorted fund lists, 2010.07
List<String> usequity = null;
List<String> internationalequity = null;
List<String> fixedincome = null;
List<String> realestate = null;
List<String> commodities = null;
List<String> emergingmarket = null;
List<String> internationalbonds =null;
List<String> highyieldbond = null;
List<String> balancefund = null;
if(!HaveDataFile)
{
    usequity = new ArrayList<String>();
    internationalequity = new ArrayList<String>();
    fixedincome = new ArrayList<String>();
    realestate = new ArrayList<String>();
    commodities = new ArrayList<String>();
    emergingmarket = new ArrayList<String>();
    internationalbonds = new ArrayList<String>();
    highyieldbond = new ArrayList<String>();
    balancefund = new ArrayList<String>();
}
else
{
    usequity = (ArrayList<String>)ReadObj[2];
    internationalequity = (ArrayList<String>)ReadObj[3];
    fixedincome = (ArrayList<String>)ReadObj[4];
    realestate = (ArrayList<String>)ReadObj[5];
    commodities = (ArrayList<String>)ReadObj[6];
    emergingmarket = (ArrayList<String>)ReadObj[7];
    internationalbonds = (ArrayList<String>)ReadObj[8];
    highyieldbond = (ArrayList<String>)ReadObj[9];
    balancefund = (ArrayList<String>)ReadObj[10];
}
usequity.add(dateStr);
internationalequity.add(dateStr);
fixedincome.add(dateStr);
realestate.add(dateStr);
commodities.add(dateStr);
emergingmarket.add(dateStr);
internationalbonds.add(dateStr);
highyieldbond.add(dateStr);
balancefund.add(dateStr);

AssetRepresentFundMap = new HashMap<String, List<String>>();
FundPercentageMap = new HashMap<String, Double>();
HashMap<String, String> PseudoMap = new HashMap<String, String>();
List<String> FundList = null;
RepresentFundList = null;
String[] Funds = null;
int RepresentNumber;
int SaveNumber;
double Weight;
for(int i = 0; i < SelectedAssets.size(); i++)
{
    FundList  = new ArrayList<String>();
    FundList  = AllAssetFundsMap.get(SelectedAssets.get(i).trim() );
    Funds = new String[FundList.size()];
    Score = new double[FundList.size()];
    if(SelectedAssets.get(i).equals("FIXED INCOME") || SelectedAssets.get(i).equals("INTERNATIONAL BONDS") )
        RepresentNumber = MaxOfStableAsset;
    else if(SelectedAssets.get(i).equals("CASH"))
        RepresentNumber = 1;
    else 
        RepresentNumber = MaxOfRiskyAsset;
    RepresentNumber = RepresentNumber < Funds.length?RepresentNumber : Funds.length;
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
for(int l=0; l<=SaveNumber -1; l++)
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
double TempWeight;
if(!FundPercentageMap.containsKey("CASH"))
    FundPercentageMap.put("CASH", 0.0);

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
        usequity.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL EQUITY"))
{
    for(int j = 0; j < SaveNumber; j++)
        internationalequity.add(Funds[j]);    
}
else if(SelectedAssets.get(i).trim().equals("FIXED INCOME"))
{
    for(int j = 0; j < SaveNumber; j++)
        fixedincome.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("REAL ESTATE"))
{
    for(int j = 0; j < SaveNumber; j++)
        realestate.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("COMMODITIES"))
{
    for(int j = 0; j < SaveNumber; j++)
        commodities.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("Emerging Market"))
{
    for(int j = 0; j < SaveNumber; j++)
        emergingmarket.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL BONDS"))
{
    for(int j = 0; j < SaveNumber; j++)
        internationalbonds.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("High Yield Bond"))
{
    for(int j = 0; j < SaveNumber; j++)
        highyieldbond.add(Funds[j]);
}
else if(SelectedAssets.get(i).trim().equals("BALANCE FUND"))
{
    for(int j = 0; j < SaveNumber; j++)
        balancefund.add(Funds[j]);
}

/*aggregate the same representative funds, Added by WYJ on 2010.05.08, modified on 05.10*/
Weight = SelectedAssetPercentages.get(i) /RepresentNumber; 
for(int j = 0; j < RepresentNumber; j++)  
{
    if(!Funds[j].equals("CASH"))
    {
        if(RepresentFundList.indexOf(Funds[j]) == -1)
        {
            RepresentFundList.add(Funds[j] );
            FundPercentageMap.put(Funds[j].trim(), Weight);
            printToLog(SelectedAssets.get(i) + " buy target :  " + Funds[j] + "   target percentage : " + Weight);
        }
        else 
        {
            TempWeight = FundPercentageMap.get(Funds[j].trim());
            TempWeight += Weight;
            FundPercentageMap.put(Funds[j].trim(), TempWeight);
            printToLog(SelectedAssets.get(i) + " buy target :  " + Funds[j] + " Cumulative target percentage : " + TempWeight);
        }
    }
    else if(Funds[j].equals("CASH"))
    { 
        TempWeight = FundPercentageMap.get("CASH");
        TempWeight += Weight;
        FundPercentageMap.put("CASH", TempWeight);

        if(SelectedAssets.get(i).equals("CASH") && RepresentFundList.indexOf("CASH") == -1)      
        {
            RepresentFundList.add("CASH");
        }
        if(!SelectedAssets.get(i).equals("CASH"))
        {
            TempWeight = SelectedAssetPercentages.get(i);
            TempWeight  -= Weight;
            SelectedAssetPercentages.set(i, TempWeight);

            TempWeight = SelectedAssetPercentages.get(SelectedAssets.indexOf("CASH") );
            TempWeight  += Weight;
            SelectedAssetPercentages.set(SelectedAssets.indexOf("CASH"), TempWeight);
        }   
    }
}

if(i == SelectedAssets.size()-1)
  printToLog("CASH Buying Target Percentage : " + SelectedAssetPercentages.get(SelectedAssets.indexOf("CASH")));

AssetRepresentFundMap.put(SelectedAssets.get(i).trim(), RepresentFundList);
}

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



/*save the selected asset classes and the sorted fund lists, write objects, 2010.07*/

List<String> SaveRiskyGroupSelect = null;
List<String> SaveStableGroupSelect = null;
if(!HaveDataFile)
{   
    SaveRiskyGroupSelect = new ArrayList<String>();
    SaveStableGroupSelect = new ArrayList<String>();
}
else
{
    SaveRiskyGroupSelect = (ArrayList<String>)ReadObj[0];
    SaveStableGroupSelect = (ArrayList<String>)ReadObj[1];
}
SaveRiskyGroupSelect.add(dateStr);
SaveStableGroupSelect.add(dateStr);
for(int i = 0; i < RiskyGroupSelect.size(); i++)
    SaveRiskyGroupSelect.add(RiskyGroupSelect.get(i));
for(int i = 0; i < StableGroupSelect.size(); i++)
    SaveStableGroupSelect.add(StableGroupSelect.get(i));

Object[] Ob = new Object[]{SaveRiskyGroupSelect, SaveStableGroupSelect, usequity, internationalequity, fixedincome, realestate, commodities, emergingmarket,  internationalbonds, highyieldbond, balancefund};

com.lti.util.PersistentUtil.writeObject(Ob,"test TAA scaling all 4");

}   //End of the first try catch, which is done when calculating for the first time



if(HaveData)   //  After calculate the first portfolio of the plan
{
    long sTime1 = System.currentTimeMillis();
    printToLog("--------------------  Scaling Calculation ----------------------------");
    //ReadObj = (Object[])com.lti.util.PersistentUtil.readObject("test TAA scaling all 4");
    StableAllocation = RiskProfile;
    RiskyAllocation = 100 - RiskProfile;
    int RepresentNumber = 0;
    AssetRepresentFundMap = new HashMap<String, List<String>>(); 

    // Read Saved List for Scale Calculation
    List<String> SaveRiskyGroupSelect = (ArrayList<String>)ReadObj[0];
    List<String> SaveStableGroupSelect = (ArrayList<String>)ReadObj[1];
    List<String> Saveusequity = (ArrayList<String>)ReadObj[2];
    List<String> Saveinternationalequity = (ArrayList<String>)ReadObj[3];
    List<String> Savefixedincome = (ArrayList<String>)ReadObj[4];
    List<String> Saverealestate = (ArrayList<String>)ReadObj[5];
    List<String> Savecommodities = (ArrayList<String>)ReadObj[6];
    List<String> Saveemergingmarket = (ArrayList<String>)ReadObj[7];
    List<String> Saveinternationalbonds = (ArrayList<String>)ReadObj[8];
    List<String> Savehighyieldbond = (ArrayList<String>)ReadObj[9];
    List<String> Savebalancefund = (ArrayList<String>)ReadObj[10];

    List<String> RiskyGroupSelect = getSaveListOfThisAction(SaveRiskyGroupSelect, dateStr);
    List<String> StableGroupSelect = getSaveListOfThisAction(SaveStableGroupSelect, dateStr);
    List<String> usequity = new ArrayList<String>();
    List<String> internationalequity = new ArrayList<String>();
    List<String> fixedincome = new ArrayList<String>();
    List<String> realestate = new ArrayList<String>();
    List<String> commodities = new ArrayList<String>();
    List<String> emergingmarket = new ArrayList<String>();
    List<String> internationalbonds = new ArrayList<String>();
    List<String> highyieldbond = new ArrayList<String>();
    List<String> balancefund = new ArrayList<String>();

/*
for(int i = 0; i < RiskyGroupSelect.size(); i++)
{
    printToLog("Risky Group selected asset " + (i+1) + "  : " + RiskyGroupSelect.get(i));
}
for(int i = 0; i < StableGroupSelect.size(); i++)
{
    printToLog("Stable Group selected asset " + (i+1) + "  : " + StableGroupSelect.get(i));
}
*/

/*combine selected assets and determine the asset percentages, modified by WYJ in 2010.07*/
boolean Add = true;
double StablePerc = 0;
SelectedAssetPercentages = new ArrayList<Double>();
for(int i=0;i<RiskyGroupSelect.size();i++)
    SelectedAssetPercentages.add(RiskyAllocation/RiskyGroupSelect.size()/100);

SelectedAssets = new ArrayList<String>();
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

// get the subList of this action to calculate
if(SelectedAssets.indexOf("US EQUITY") > -1)
    usequity = getSaveListOfThisAction(Saveusequity, dateStr);
if(SelectedAssets.indexOf("INTERNATIONAL EQUITY") > -1)
    internationalequity = getSaveListOfThisAction(Saveinternationalequity, dateStr);
if(SelectedAssets.indexOf("FIXED INCOME") > -1)
    fixedincome = getSaveListOfThisAction(Savefixedincome, dateStr);
if(SelectedAssets.indexOf("REAL ESTATE") > -1)
    realestate = getSaveListOfThisAction(Saverealestate, dateStr);
if(SelectedAssets.indexOf("COMMODITIES") > -1)
    commodities = getSaveListOfThisAction(Savecommodities, dateStr);
if(SelectedAssets.indexOf("Emerging Market") > -1)
    emergingmarket = getSaveListOfThisAction(Saveemergingmarket, dateStr);
if(SelectedAssets.indexOf("High Yield Bond") > -1)
    highyieldbond = getSaveListOfThisAction(Savehighyieldbond, dateStr);
if(SelectedAssets.indexOf("INTERNATIONAL BONDS") > -1)
    internationalbonds = getSaveListOfThisAction(Saveinternationalbonds, dateStr);
if(SelectedAssets.indexOf("BALANCE FUND") > -1)
    balancefund = getSaveListOfThisAction(Savebalancefund, dateStr);

/* make neccesary diversification of the stable asset classes, Added by WYJ on 2010.05.08 */   

if(SelectedAssets.size() == 1)
    MaxOfStableAsset = 3;
else if(SelectedAssets.size() == 2)
    MaxOfStableAsset = 2;

double EachStableFundPec = StablePerc / MaxOfStableAsset ;         // need to be improved, WYJ
if(EachStableFundPec > 0.67)
    MaxOfStableAsset +=2;
else if(EachStableFundPec > 0.34)
    MaxOfStableAsset +=1;

//Add CASH after diversify
if(SelectedAssets.indexOf("CASH") < 0)
{
    SelectedAssets.add("CASH");
    SelectedAssetPercentages.add(0.0);
}

/*to  form preferred fund list*/

double Weight = 0;
List<String>  SaveFundList = null;
RepresentFundList = null;
FundPercentageMap = new HashMap<String, Double>();
for(int i = 0; i < SelectedAssets.size(); i ++)     // start of selected asset treatment
{
    if(SelectedAssets.get(i).trim().equals("US EQUITY"))
        SaveFundList = usequity;
    else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL EQUITY"))
        SaveFundList = internationalequity;
    else if(SelectedAssets.get(i).trim().equals("FIXED INCOME"))
        SaveFundList = fixedincome;
    else if(SelectedAssets.get(i).trim().equals("REAL ESTATE"))
        SaveFundList = realestate;
    else if(SelectedAssets.get(i).trim().equals("COMMODITIES"))
        SaveFundList = commodities;
    else if(SelectedAssets.get(i).trim().equals("Emerging Market"))
        SaveFundList = emergingmarket;
    else if(SelectedAssets.get(i).trim().equals("INTERNATIONAL BONDS"))
        SaveFundList = internationalbonds;
    else if(SelectedAssets.get(i).trim().equals("High Yield Bond"))
        SaveFundList = highyieldbond;
    else if(SelectedAssets.get(i).trim().equals("BALANCE FUND"))
        SaveFundList = balancefund;
    else if(SelectedAssets.get(i).trim().equals("CASH"))
    {
        SaveFundList = new ArrayList<String>();
        SaveFundList.add("CASH");
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

/*aggregate the same representative funds, Added by WYJ on 2010.05.08, modified on 2010.07*/
Weight = SelectedAssetPercentages.get(i) /RepresentNumber; 
printToLog("Before Aggregate:  Target Percentage of " + SelectedAssets.get(i) + "  = "  + SelectedAssetPercentages.get(i) );
for(int j = 0; j < RepresentNumber; j++)  
{
    if(!SaveFundList.get(j).equals("CASH"))
    {
        if(RepresentFundList.indexOf( SaveFundList.get(j) ) == -1)
        {
            RepresentFundList.add(SaveFundList.get(j) );
            FundPercentageMap.put(SaveFundList.get(j).trim(), Weight);
            printToLog(SelectedAssets.get(i) + " buy target :  " + SaveFundList.get(j) + "   target percentage : " + Weight);
        }
        else 
        {
            TempWeight = FundPercentageMap.get(SaveFundList.get(j).trim());
            TempWeight += Weight;
            FundPercentageMap.put(SaveFundList.get(j).trim(), TempWeight);
            printToLog(SelectedAssets.get(i) + " buy target :  " + SaveFundList.get(j) + " Cumulative target percentage : " + TempWeight);
        }
    }
    else if(SaveFundList.get(j).equals("CASH"))
    { 
        TempWeight = FundPercentageMap.get("CASH");
        TempWeight += Weight;
        FundPercentageMap.put("CASH", TempWeight);

        if(SelectedAssets.get(i).equals("CASH") && RepresentFundList.indexOf("CASH") == -1)      
        {
            RepresentFundList.add("CASH");
        }
        if(!SelectedAssets.get(i).equals("CASH"))
        {
            TempWeight = SelectedAssetPercentages.get(i);
            TempWeight  -= Weight;
            SelectedAssetPercentages.set(i, TempWeight);

            TempWeight = SelectedAssetPercentages.get(SelectedAssets.indexOf("CASH") );
            TempWeight  += Weight;
            SelectedAssetPercentages.set(SelectedAssets.indexOf("CASH"), TempWeight);
        }   
    }
}  // End of aggregate the same funds
printToLog("After Aggregate:  Target Percentage of " + SelectedAssets.get(i) + "  = "  + SelectedAssetPercentages.get(i) );


if(i == SelectedAssets.size()-1)
   printToLog("CASH Buying Target Percentage : " + SelectedAssetPercentages.get(SelectedAssets.indexOf("CASH")));
AssetRepresentFundMap.put(SelectedAssets.get(i).trim(), RepresentFundList);

}  // end of selected asset treatment 

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
long eTime1 = System.currentTimeMillis();
printToLog("Scale Calculation Time Cost:  " + (eTime1 - sTime1));

}//  end of "HaveDate" calculation for non-first time monitor

long sTime2 = System.currentTimeMillis();

/*do not balance the portfolio funds that is representative again, except for the fix income funds */
String sn;
double sa;
double aa;
double Tempsa;
double TempTargetPercentage;
double TempActualHoldingPercentage;
List<String> RepresentFunds = null;
HoldingSecurityList = null;

printToLog("--------------Adjusting the available percentage------------------------ ");
double ta = CurrentPortfolio.getTotalAmount(CurrentDate);
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
            MaxTargetPercentage = CompareTargetPercentage;
            MaxTargetPercentageAsset  = SelectedAssets.get(i);
        }
    }
    double TempTrading =  AssetActualTradingPercentage.get(MaxTargetPercentageAsset).doubleValue();
    double NewActualTradingPercentage = TempTrading + UnableToBuyPercentage;
    if(NewActualTradingPercentage < 0.001) NewActualTradingPercentage = 0;
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
    if(HoldingAssetActualPercentage.get(AssetName) != null)
    {
        HoldingSecurityList = CurrentPortfolio.getAsset(AssetName).getHoldingItems();
        ActualPercentage = HoldingAssetActualPercentage.get(AssetName).doubleValue();
        AvailableTradingPercentage =  AssetAvailableTradingPercentage.get(AssetName).doubleValue();
    }
    else  
        { HoldingSecurityList = null ; ActualPercentage = 0; AvailableTradingPercentage =0; NewAdd[i] = 1; }
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
    }
    else if(NewAdd[i] == 1)
    {
        SecurityActualTradingPercentage = new HashMap<String, Double>();
        BuyingPercentage = AssetActualTradingPercentage.get(AssetName).doubleValue() / PresentativeSecurityList.size();      
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
        TotalBuyingPercentage = AssetActualTradingPercentage.get(AssetName).doubleValue() + AvailableTradingPercentage ;          
        //printToLog(" Actual Target Percentage = " + (TotalBuyingPercentage + ActualPercentage - AvailableTradingPercentage));
        int j=0;
        while(TotalBuyingPercentage > 0.001)
        {
             SecurityName = PresentativeSecurityList.get(j).trim();
             SecurityTargetPercentage = FundPercentageMap.get(SecurityName);
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
}

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
        if(TempPercentage > 0)
        {
            TempPercentage += UnTradePercentage;
            TempTradingPercentageMap.put(Security.trim(), TempPercentage);
            AssetSecurityTradingPercentageMap.put(Asset.trim(), TempTradingPercentageMap);
            Done = true;
        }
    }
}
}

 printToLog("Total Amount  =   "  +    TotalAmount );


long eTime2 = System.currentTimeMillis();
printToLog("Actual Trade Determination Time Cost:  " + (eTime2 - sTime2));

long sTime4 = System.currentTimeMillis();

long sTime5;
long eTime5;
/*Do the selling transaction*/

HoldingAssetList = CurrentPortfolio.getCurrentAssetList();
for(int i = 0; i < HoldingAssetList.size(); i++)
{
    HoldingSecurityList = new ArrayList<HoldingItem>();
    HoldingSecurityList = HoldingAssetList.get(i).getHoldingItems();
    for(int j = 0; j < HoldingSecurityList.size(); j++)
    {
        sn = HoldingSecurityList.get(j).getSymbol().trim();
        //printToLog("Asset : " + HoldingAssetList.get(i).getName() +  " Security Symbol: "+ sn);
    }
}


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
        if(TradePercentage < 0){
          try{
            sTime5 = System.currentTimeMillis();
            CurrentPortfolio.sellAtNextOpen(AssetName, TradingSecurity, TotalAmount * (- TradePercentage), CurrentDate);
            eTime5 = System.currentTimeMillis();
            printToLog("Sell Signal: "+AssetName+"  "+TradingSecurity+"  $ "+TotalAmount*(-TradePercentage));
            printToLog("selling Time Cost:  " + (eTime5 - sTime5));
            }
         catch(Exception e){
            printToLog("Failed selling:  "+AssetName+"  "+TradingSecurity+"  $ "+TotalAmount*(-TradePercentage));
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
        Double TradePercentage = (Double)entry.getValue(); 
        if(TradePercentage.doubleValue() > 0){
            sTime5 = System.currentTimeMillis();
            CurrentPortfolio.buyAtNextOpen(AssetName, TradingSecurity, TotalAmount * TradePercentage.doubleValue(), CurrentDate,true);
            eTime5 = System.currentTimeMillis();
            printToLog("Buy Signal: "+AssetName+"  "+TradingSecurity+"  $ "+TotalAmount*(TradePercentage));
            printToLog("Buying Time Cost:  " + (eTime5 - sTime5));
            }
        }
    }
}

if(NewYear)  NewYear = false;


long eTime4 = System.currentTimeMillis();
printToLog("Trading Time Cost:  " + (eTime4 - sTime4));

long eTime = System.currentTimeMillis();
printToLog("Action Time Cost:  " + (eTime - sTime));
		}
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Tactical_Asset_Allocation_scale_backup_08154712.java:254: reinit() in com.lti.type.executor.SimulateStrategy cannot be applied to (com.lti.service.bo.Portfolio,java.util.Hashtable<java.lang.String,java.lang.String>,java.util.Date)
//                 super.reinit(portfolio,parameters,pdate);
//                      ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Tactical_Asset_Allocation_scale_backup_08154712.java:252: method does not override or implement a method from a supertype
//@Override
//^
//2 errors