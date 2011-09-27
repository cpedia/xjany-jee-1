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
public class Strategic_Asset_Allocation_for_old_engine_backup_09204827 extends SimulateStrategy{
	public Strategic_Asset_Allocation_for_old_engine_backup_09204827(){
		super();
		StrategyID=4827L;
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
		PlanID=(Long)ParameterUtil.fetchParameter("Long","PlanID", "0", parameters);
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
	private double version=1.0;
@SuppressWarnings("unchecked")
@Override
public void reinit(com.lti.service.bo.Portfolio portfolio, Hashtable<String, String> parameters, Date pdate) throws Exception {
                 super.reinit(portfolio,parameters,pdate);
                printToLog("--------------  Re-initial to version " + version +  " :  "+ CurrentDate + "   ----------------");
  
    boolean UseDefaultRedemption = false;  // Please set as parameter if possible
    int DefaultRedemptionLimit = 3;
    LastActionMonth = -1;    // to let NewYear to be false  when calculated in the common action
    NewYear = false;

//  Delete Duplicate Candidates
Object[] CandidateObject = DeleteDuplicateCandidate(CandidateFund, RedemptionLimit, WaitingPeriod, RoundtripLimit, UseDefaultRedemption, DefaultRedemptionLimit);
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

                printToLog("--------------  Re-initial Finish, SAA strategy update to version " + version + " -------------------");
}

//==========================================================

/*Function to delete the duplicate candidate fund*/
public Object[] DeleteDuplicateCandidate(String[] CandidateFund, int[] RedemptionLimit, int[] WaitingPeriod, int[] RoundtripLimit, boolean UseDefaultRedemption, int DefaultRedemptionLimit)
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
public Object[] DivideAssetClass(HashMap<String,List<String>> AllAssetFundsMap)
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

String[] RiskyAssets= new String[n1 - n2];
String[] StableAssets = new String[n2];
n1 = 0; n2 = 0;
for(int i = 0; i < AllAssetFundsMap.size(); i++)
{
    if(IndexOfStable[i] == 0)  {RiskyAssets[n1] = AvailableAssets[i]; n1++;}
    else  {StableAssets[n2] = AvailableAssets[i]; n2++;}
}
printToLog("AvailableAssetClassList initialize ok");

	Object[] NewObject=new Object[3];
        NewObject[0]=AvailableAssets;
	NewObject[1]=RiskyAssets;
	NewObject[2]=StableAssets;
	return NewObject;
}
//================================================

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

public double[] getSharpeScore(String[] Securities, Date CurrentDate) throws Exception {
double[] score=new double[Securities.length];

for(int i=0; i<=Securities.length-1; i++)
{
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(Securities[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
{
    try{ 
        score[i]  = getSecurity(Securities[i]).getSharpeRatio(-12, CurrentDate, TimeUnit.MONTHLY, false);
        printToLog("Sharpe Ratio Score: "+Securities[i]+" : "+score[i]);
    }
    catch(Exception e1){
        score[i]=-100000;
        printToLog("get Sharpe Ratio Error : " + Securities[i]);
    }
}
else
    score[i]=-100000;
}
return score;
}

//====================================================

public Map<String,Double> AssetPercentageAllocation(List<String> RiskyAssets, List<String> StableAssets,  double RiskyAllocation, double[] MainAssetClassWeight, String[]  MainAssetClass) throws Exception {

Map<String, Double> RelativeWeightMap = new HashMap<String, Double>();
Map<String, Double> ActualWeightMap = new HashMap<String, Double>();

if ((MainAssetClassWeight.length!=MainAssetClass.length) || (MainAssetClassWeight.length==1 && MainAssetClassWeight[0]==0))
    for(int i = 0; i < MainAssetClass.length; i ++)
        RelativeWeightMap.put(MainAssetClass[i], 1.0);
else 
    for(int i = 0; i < MainAssetClass.length; i ++)
        RelativeWeightMap.put(MainAssetClass[i], MainAssetClassWeight[i]);

double _sumRisky = 0;
double _sumStable = 0;
for(int i = 0; i< RiskyAssets.size(); i++)
    _sumRisky += RelativeWeightMap.get(RiskyAssets.get(i));
for(int i = 0; i< StableAssets.size(); i++)
    _sumStable += RelativeWeightMap.get(StableAssets.get(i));

Iterator<String> it = RelativeWeightMap.keySet().iterator();
while(it.hasNext()){
    String asset = it.next();
    double perc = 0;
    if(RiskyAssets.indexOf(asset) > -1 && _sumRisky != 0)
        perc = (RiskyAllocation/100.0) * (RelativeWeightMap.get(asset) / _sumRisky);
    else if(StableAssets.indexOf(asset) > -1 && _sumStable != 0)
        perc = (1 - RiskyAllocation/100.0) * (RelativeWeightMap.get(asset) / _sumStable);
    else 
       perc = 0.0;
    ActualWeightMap.put(asset, perc);
}

return ActualWeightMap;
}

//=================================================================================

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
/*529 Plan : determine whether to rebalance*/

public int compareTargetAndHoldingFunds(Map<String, List<String>> PresentativeAssetFundMap)
 throws Exception {
	boolean AlreadyAdjThisYear=false;
	boolean NeedToAdjNow=false;
	//1.
	Date startDate=LTIDate.getFirstNYSETradingDayOfYear(CurrentDate);
	int[] DetectTransArray=CurrentPortfolio.transactionDetection(startDate,CurrentDate,CandidateFund); //(startDate,endDate,String[])
        printToLog("Candidate Funds Transaction in this year : " + DetectTransArray);   //For Test
	for (int Index=0;Index<DetectTransArray.length;Index++){
		if (DetectTransArray[Index]!=0){
		AlreadyAdjThisYear=true;
		break;
		}
	}
       //2. 
       if (!AlreadyAdjThisYear)
       {
          Map<String,Double> HoldingFundPercentageMap = new HashMap<String, Double>();   
          List<String> PresentativeFundList = new ArrayList<String>();       
          List<Asset> HoldingAssetList =  CurrentPortfolio.getCurrentAssetList(); 
          double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);

          //get Current holding Security percentages Map
          for(int i = 0; i < HoldingAssetList.size(); i++)
          {
             String AssetName = HoldingAssetList.get(i).getName();
             List<HoldingItem> HoldingSecurityList = HoldingAssetList.get(i).getHoldingItems();
             for(int j = 0; j < HoldingSecurityList.size(); j++)
             {
                 String SecurityName = HoldingSecurityList.get(j).getSymbol();
                 HoldingFundPercentageMap.put(SecurityName, CurrentPortfolio.getSecurityAmount(AssetName, SecurityName, CurrentDate)/TotalAmount);       
             }
          }

          // get Target Fund List
          Iterator<String>  it = PresentativeAssetFundMap.keySet().iterator();
          while(it.hasNext())
          {
              String key = (String)it.next();
              List<String> tmpList = PresentativeAssetFundMap.get(key);
              PresentativeFundList.addAll(tmpList);
          }

          // sum up the percentages of the unfavorable holding funds
          double UnFavorPercentage = 0;
          it = HoldingFundPercentageMap.keySet().iterator();
          while(it.hasNext())
          {
              String HoldingFund = (String)it.next();
              if(PresentativeFundList.indexOf(HoldingFund) == -1)
                  UnFavorPercentage += HoldingFundPercentageMap.get(HoldingFund);
          }
          if(HoldingFundPercentageMap.size() == 0)
              UnFavorPercentage = 1.0;

          printToLog("529 Plan Rebalance information : UnFavorPercentage = " + UnFavorPercentage);
          
          // determine whether to rebalance
          
         Date HalfOfYear=LTIDate.getDate(LTIDate.getYear(CurrentDate),7,1);
         Date ForceDate = null;
                if(RebalanceFrequency.equals("monthly"))
                    ForceDate = LTIDate.getDate(LTIDate.getYear(CurrentDate),11,1);
                if(RebalanceFrequency.equals("quarterly"))
                    ForceDate = LTIDate.getDate(LTIDate.getYear(CurrentDate),9,1);
                if(RebalanceFrequency.equals("yearly"))
                    ForceDate = LTIDate.getDate(LTIDate.getYear(CurrentDate),1,1);
		if((UnFavorPercentage >= 0.6 && LTIDate.before(CurrentDate,HalfOfYear)) || (UnFavorPercentage >= 0.3 && LTIDate.before(HalfOfYear,CurrentDate)) || LTIDate.before(ForceDate,CurrentDate) )
			{NeedToAdjNow=true;}	
	}
	
        int ToRebalance = 0;
        if(!AlreadyAdjThisYear && NeedToAdjNow)
            ToRebalance = 1;

return ToRebalance;
}

//=======================================================================

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
		//try{

initPlanParameters();  //Adj for Roundtrip Part 0 Initial for Parameter WaitingPeriod and Roundtrip
java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
String dateStr=df.format(CurrentDate);
long sTime = System.currentTimeMillis();
boolean UseDefaultRedemption = false;
int DefaultRedemptionLimit = 3;
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

try{ReadObj = (Object[])com.lti.util.PersistentUtil.readObject("SAA Data Plan " + PlanID);}
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
Object[] CandidateObject = DeleteDuplicateCandidate(CandidateFund, RedemptionLimit, WaitingPeriod, RoundtripLimit,UseDefaultRedemption, DefaultRedemptionLimit);
CandidateFund = (String[])CandidateObject[0];
RedemptionLimit = (int[])CandidateObject[1];
RoundtripLimit = (int[])CandidateObject[2];
WaitingPeriod = (int[])CandidateObject[3];

//   Choose the available candidate funds LJF Changed calculation of MPT 2010.03.16

Object[] AvailableObject = ChooseAvailableCandidate(CandidateFund, NewYear, InAfterDateFilter, TooVolatile);
CandidateFundList = (ArrayList<String>)AvailableObject[0];
ShortHistoryFundList = (ArrayList<String>)AvailableObject[1];
InAfterDateFilter = (int[])AvailableObject[2];
TooVolatile = (int[])AvailableObject[3];

//   Get the available asset classes , modified on 2010.08.25

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);

try{
        if(MainAssetClassList.indexOf("CASH") == -1)
            AllAssetFundsMap.remove("CASH");
        AllAssetFundsMap.remove("ROOT");
}     // templately fix the problem of "getAvailableAssetClassSet" added on 2010.05.08  please delete it when the problem is fixed
    catch(Exception e){ } 
/*
if(AllAssetFundsMap.size() == 0)
{
    List<String> tmpList = new ArrayList<String>();
    tmpList.add("CASH");
   AllAssetFundsMap.put("US EQUITY", tmpList);
    printToLog("Add Cash into US EQUITY when having no available candidate fund");

    AllAssetFundsMap.put("FIXED INCOME", tmpList);
    printToLog("Add Cash into FIXED INCOME when having no available candidate fund");
} 
*/
printToLog("AllAssetFundsMap when created  :" + AllAssetFundsMap);

//  Divide into Risky Group and Stable Group
Object[] AssetClassObject = DivideAssetClass(AllAssetFundsMap);
String[] AvailableAssets = (String[])AssetClassObject[0];
String[] RiskyAssets = (String[])AssetClassObject[1];
String[] StableAssets  = (String[])AssetClassObject[2];

SelectedAssets = new ArrayList<String>();
RiskyGroupSelect = new ArrayList<String>();
StableGroupSelect = new ArrayList<String>();
for(int i = 0; i < AvailableAssets.length; i++)
    SelectedAssets.add(AvailableAssets[i]);
for(int i = 0; i < RiskyAssets.length; i++)
    RiskyGroupSelect.add(RiskyAssets[i]);
for(int i = 0; i < StableAssets.length; i++)
    StableGroupSelect.add(StableAssets[i]);

printToLog("StableGroupSelect when Created :" + StableGroupSelect);

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
    Score = getSharpeScore(Funds, CurrentDate);
 
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

com.lti.util.PersistentUtil.writeObject(Ob,"SAA Data Plan " + PlanID);

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

//Add CASH when the CandidateFund not contains CASH
boolean _haveCash = false;
for(int i = 0; i < CandidateFund.length; i++)
    if(CandidateFund[i].equals("CASH"))
        _haveCash = true;
if(!_haveCash)
{
    String[] tmpCF = CandidateFund;
    int[] tmpRL = RedemptionLimit;
    int length = tmpCF.length + 1;
   CandidateFund = new String[length];
    RedemptionLimit = new int[length];
    for(int i = 0; i < length; i ++)
        if(i < length - 1){
            CandidateFund[i] = tmpCF[i];
            RedemptionLimit[i] = tmpRL[i];
       }else{
            CandidateFund[i] = "CASH";
            RedemptionLimit[i] = 0;
       }

    if(RoundtripLimit != null || WaitingPeriod != null) 
    {
        int[] tmpRTL = RoundtripLimit;
        int[] tmpWP = WaitingPeriod;
        RoundtripLimit = new int[length];
        WaitingPeriod = new int[length];
        for(int i = 0; i < length; i ++)
            if(i < length - 1){
                RoundtripLimit[i] = tmpRTL[i];
                WaitingPeriod[i] = tmpWP[i];
           }else{
                RoundtripLimit[i] = 13;
                WaitingPeriod[i] = 0;
           }        
     }
}

//Add CASH when the Risky Group or Stable Group is empty

if(RiskyGroupSelect.size() == 0)
{
   RiskyGroupSelect.add("US EQUITY");
    printToLog("Add  US EQUITY (to contain CASH) when the Risky Group is empty");
} 

if(StableGroupSelect.size() == 0)
{
   StableGroupSelect.add("FIXED INCOME");
    printToLog("Add  FIXED INCOME (to contain CASH) when the Stable Group is empty");
} 

printToLog("RiskyGroupSelect :" + RiskyGroupSelect);
printToLog("StableGroupSelect :" + StableGroupSelect);

//  Allocate Asset Target Percentages , including the assets with zero percentage
Map<String,Double> TargetAssetWeight = AssetPercentageAllocation(RiskyGroupSelect, StableGroupSelect, RiskyAllocation, MainAssetClassWeight,  MainAssetClass);

//    Make neccesary diversification of the stable asset classes, Added by WYJ on 2010.05.08
Map<String, Integer> MaxOfEachAssetMap = new HashMap<String, Integer>();
Iterator<String> it_asset = TargetAssetWeight.keySet().iterator();
while(it_asset.hasNext()){
    String asset = it_asset.next();
    double perc =  TargetAssetWeight.get(asset)/ MaxOfEachAsset;
    int number = MaxOfEachAsset;
    if(perc > 0.67)
        number += 2;
    else if (perc > 0.34)
        number += 1;

    if(asset.equals("US EQUITY") && number == 1 && perc > 0.1)
        number = 2;
    if(perc < 2*(BuyThreshold/100) && number >1)
        number -= 1;
    MaxOfEachAssetMap.put(asset, number);
}

//    create variables needed below, need to be modified later
SelectedAssets = new ArrayList<String>();
SelectedAssetPercentages = new ArrayList<Double>();
it_asset = TargetAssetWeight.keySet().iterator();
while(it_asset.hasNext()){
    String asset = it_asset.next();
    double perc =  TargetAssetWeight.get(asset);
    if(perc > 0)
    {
    SelectedAssets.add(asset);
    SelectedAssetPercentages.add(perc);
    }
}

/*
      // JUST FOR TESTING, please delete after testing
RoundtripLimit = new int[CandidateFund.length];
WaitingPeriod = new int[CandidateFund.length];
for(int i = 0; i < CandidateFund.length; i++)
{
    if(CandidateFund[i].equals("CASH")){
        RoundtripLimit[i] = 13;
        WaitingPeriod[i] = 0;
    }else{
    RoundtripLimit[i] = 2;
    WaitingPeriod[i] = 2;
    }
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
    if(SaveFundList.size() == 0)
        SaveFundList.add("CASH");

//    Calculate CanNotBuyFundList, Adj for RoundTrip Part 2 LJF 2010.07.02 
List<String> tempList  = new ArrayList<String>();
if(RoundtripLimit != null || WaitingPeriod != null)        // Some variable need to prepare
{
    tempList = ExcludeByRoundTripAndWaiting(SaveFundList, CandidateFund, RoundtripLimit, WaitingPeriod);
    if(tempList.size() > 0)
        CanNotBuyFundList.addAll(tempList);
}

    if(SelectedAssets.get(i).equals("CASH"))
        RepresentNumber = 1;
    else 
        RepresentNumber = MaxOfEachAssetMap.get(SelectedAssets.get(i));
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
        if(FundPercentageMap.get(SaveFundList.get(j).trim())==null)
		{	
			if (CanNotBuyFundList.indexOf(SaveFundList.get(j))== -1 || CurrentPortfolio.holdSecurity(getSecurity(SaveFundList.get(j)).getID())) //Adj for RoundTrip Part 3 LJF 2010.07.02
			{
			RepresentFundList.add(SaveFundList.get(j));
            FundPercentageMap.put(SaveFundList.get(j).trim(), Weight);
            printToLog(SelectedAssets.get(i) + " buy target :  " + SaveFundList.get(j) + "   target percentage : " + Weight);
			}
			else {	//Adj for RoundTrip Part 3 LJF 2010.07.02
				if (MaxIndex < SaveFundList.size()) {MaxIndex=MaxIndex+1;}     
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

/*LJF temp testing*/ printToLog("AssetRepresentFundMap 1:"+AssetRepresentFundMap);
//    Aggregate the CASH from different Assets, modified on 2010.08.25
if(SelectedAssets.indexOf("CASH") < 0)
{
    SelectedAssets.add("CASH");
    SelectedAssetPercentages.add(0.0);
}
for(int iAsset=0;iAsset<SelectedAssets.size();iAsset++){
	String AssetName=SelectedAssets.get(iAsset);
	if(!AssetName.equals("CASH")){
		List<String>RepreFundList=AssetRepresentFundMap.get(AssetName);
		int CASHFundIndex=RepreFundList.indexOf("CASH");
		if (CASHFundIndex==-1) continue;
		//Remove CASH from RepresentFundList
		RepreFundList.remove("CASH");
		AssetRepresentFundMap.put(AssetName,RepreFundList);
                List<String> tmpList = new ArrayList<String>();
                tmpList.add("CASH");
                AssetRepresentFundMap.put("CASH", tmpList);
		
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

//===============================================================
/*529 Plan : determine whether to rebalance*/

// is529Plan = true;   /*JUST FOR TEST*/
if(is529Plan)
    ToRebalance = compareTargetAndHoldingFunds(AssetRepresentFundMap);

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



//}catch(Exception eInit){printToLog("Action Error!!");}
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
boolean UseDefaultRedemption = false;
int DefaultRedemptionLimit = 3;
HashMap<String,Integer> RedemptionLimitMap = new HashMap<String,Integer>(); 
List<Asset> HoldingAssetList =  new ArrayList<Asset>();
HashMap<String,List<HoldingItem>> HoldingAssetSecurityMap = new HashMap<String,List<HoldingItem>>();
HashMap<String,Double>  SecurityAvailableTradingPercentage = new HashMap<String,Double>();
HashMap<String,Double>  AssetAvailableTradingPercentage = new HashMap<String,Double>();

//printToLog("Small cash in the portfolio  =  " +  CurrentPortfolio.getCash());
//try{
if(( RebalanceFrequency.equalsIgnoreCase("monthly") && LTIDate.isLastNYSETradingDayOfMonth(LTIDate.getNewDate(CurrentDate, TimeUnit.DAILY, (30- CheckDate)))) 
|| ( RebalanceFrequency.equalsIgnoreCase("quarterly") && LTIDate.isLastNYSETradingDayOfQuarter(LTIDate.getNewDate(CurrentDate, TimeUnit.DAILY, (30- CheckDate)))) || (RebalanceFrequency.equals("yearly")&&LTIDate.isLastNYSETradingDayOfYear(LTIDate.getNewDate(CurrentDate, TimeUnit.DAILY, (30- CheckDate)))))
{
long sTime3 = System.currentTimeMillis();

//printToLog("Frequncy = " + RebalanceFrequency  );
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
//}catch(Exception e){printToLog("Comman Action Error"); }



		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (ToCheck) {
		   //try{

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

try{ReadObj = (Object[])com.lti.util.PersistentUtil.readObject("SAA Data Plan " + PlanID);}
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
Object[] CandidateObject = DeleteDuplicateCandidate(CandidateFund, RedemptionLimit, WaitingPeriod, RoundtripLimit,UseDefaultRedemption, DefaultRedemptionLimit);
CandidateFund = (String[])CandidateObject[0];
RedemptionLimit = (int[])CandidateObject[1];
RoundtripLimit = (int[])CandidateObject[2];
WaitingPeriod = (int[])CandidateObject[3];

//   Choose the available candidate funds LJF Changed calculation of MPT 2010.03.16
Object[] AvailableObject = ChooseAvailableCandidate(CandidateFund, NewYear, InAfterDateFilter, TooVolatile);
CandidateFundList = (ArrayList<String>)AvailableObject[0];
ShortHistoryFundList = (ArrayList<String>)AvailableObject[1];
InAfterDateFilter = (int[])AvailableObject[2];
TooVolatile = (int[])AvailableObject[3];

//   Get the available asset classes , modified on 2010.08.25
HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);

try{
        if(MainAssetClassList.indexOf("CASH") == -1)
        AllAssetFundsMap.remove("CASH");
        AllAssetFundsMap.remove("ROOT");
}     // templately fix the problem of "getAvailableAssetClassSet" added on 2010.05.08  please delete it when the problem is fixed
    catch(Exception e){ } 
/*
if(AllAssetFundsMap.size() == 0)
{
    List<String> tmpList = new ArrayList<String>();
    tmpList.add("CASH");
   AllAssetFundsMap.put("US EQUITY", tmpList);
    printToLog("Add Cash into US EQUITY when having no available candidate fund");

    AllAssetFundsMap.put("FIXED INCOME", tmpList);
    printToLog("Add Cash into FIXED INCOME when having no available candidate fund");
} 
*/
printToLog("AllAssetFundsMap when created  :" + AllAssetFundsMap);

//  Divide into Risky Group and Stable Group
Object[] AssetClassObject = DivideAssetClass(AllAssetFundsMap);
String[] AvailableAssets = (String[])AssetClassObject[0];
String[] RiskyAssets = (String[])AssetClassObject[1];
String[] StableAssets  = (String[])AssetClassObject[2];

SelectedAssets = new ArrayList<String>();
RiskyGroupSelect = new ArrayList<String>();
StableGroupSelect = new ArrayList<String>();
for(int i = 0; i < AvailableAssets.length; i++)
    SelectedAssets.add(AvailableAssets[i]);
for(int i = 0; i < RiskyAssets.length; i++)
    RiskyGroupSelect.add(RiskyAssets[i]);
for(int i = 0; i < StableAssets.length; i++)
    StableGroupSelect.add(StableAssets[i]);

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
    Score = getSharpeScore(Funds, CurrentDate);
 
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

com.lti.util.PersistentUtil.writeObject(Ob,"SAA Data Plan " + PlanID);

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

//Add CASH when the CandidateFund not contains CASH
boolean _haveCash = false;
for(int i = 0; i < CandidateFund.length; i++)
    if(CandidateFund[i].equals("CASH"))
        _haveCash = true;
if(!_haveCash)
{
    String[] tmpCF = CandidateFund;
    int[] tmpRL = RedemptionLimit;
    int length = tmpCF.length + 1;
   CandidateFund = new String[length];
    RedemptionLimit = new int[length];
    for(int i = 0; i < length; i ++)
        if(i < length - 1){
            CandidateFund[i] = tmpCF[i];
            RedemptionLimit[i] = tmpRL[i];
       }else{
            CandidateFund[i] = "CASH";
            RedemptionLimit[i] = 0;
       }

    if(RoundtripLimit != null || WaitingPeriod != null) 
    {
        int[] tmpRTL = RoundtripLimit;
        int[] tmpWP = WaitingPeriod;
        RoundtripLimit = new int[length];
        WaitingPeriod = new int[length];
        for(int i = 0; i < length; i ++)
            if(i < length - 1){
                RoundtripLimit[i] = tmpRTL[i];
                WaitingPeriod[i] = tmpWP[i];
           }else{
                RoundtripLimit[i] = 13;
                WaitingPeriod[i] = 0;
           }        
     }
}

//Add CASH when the Risky Group or Stable Group is empty
if(RiskyGroupSelect.size() == 0)
{
   RiskyGroupSelect.add("US EQUITY");
    printToLog("Add  US EQUITY (to contain CASH) when the Risky Group is empty");
} 

if(StableGroupSelect.size() == 0)
{
   StableGroupSelect.add("FIXED INCOME");
    printToLog("Add  FIXED INCOME (to contain CASH) when the Stable Group is empty");
} 

//  Allocate Asset Target Percentages , including the assets with zero percentage
Map<String,Double> TargetAssetWeight = AssetPercentageAllocation(RiskyGroupSelect, StableGroupSelect, RiskyAllocation, MainAssetClassWeight,  MainAssetClass);

//    Make neccesary diversification of the stable asset classes, Added by WYJ on 2010.05.08
Map<String, Integer> MaxOfEachAssetMap = new HashMap<String, Integer>();
Iterator<String> it_asset = TargetAssetWeight.keySet().iterator();
while(it_asset.hasNext()){
    String asset = it_asset.next();
    double perc =  TargetAssetWeight.get(asset)/ MaxOfEachAsset;
    int number = MaxOfEachAsset;
    if(perc > 0.67)
        number += 2;
    else if (perc > 0.34)
        number += 1;

    if(asset.equals("US EQUITY") && number == 1 && perc > 0.1)
        number = 2;
   if(perc < 2*(BuyThreshold/100)  && number > 1)
        number -=1;
    MaxOfEachAssetMap.put(asset, number);
}

//    create variables needed below, need to be modified later
SelectedAssets = new ArrayList<String>();
SelectedAssetPercentages = new ArrayList<Double>();
it_asset = TargetAssetWeight.keySet().iterator();
while(it_asset.hasNext()){
    String asset = it_asset.next();
    double perc =  TargetAssetWeight.get(asset);
    if(perc > 0)
    {
    SelectedAssets.add(asset);
    SelectedAssetPercentages.add(perc);
    }
}

/*
      // JUST FOR TESTING, please delete after testing
RoundtripLimit = new int[CandidateFund.length];
WaitingPeriod = new int[CandidateFund.length];
for(int i = 0; i < CandidateFund.length; i++)
{
    if(CandidateFund[i].equals("CASH")){
        RoundtripLimit[i] = 13;
        WaitingPeriod[i] = 0;
    }else{
    RoundtripLimit[i] = 2;
    WaitingPeriod[i] = 2;
    }
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
    if(SaveFundList.size() == 0)
        SaveFundList.add("CASH");

//    Calculate CanNotBuyFundList, Adj for RoundTrip Part 2 LJF 2010.07.02 
List<String> tempList  = new ArrayList<String>();
if(RoundtripLimit != null || WaitingPeriod != null)        // Some variable need to prepare
{
    tempList = ExcludeByRoundTripAndWaiting(SaveFundList, CandidateFund, RoundtripLimit, WaitingPeriod);
    if(tempList.size() > 0)
        CanNotBuyFundList.addAll(tempList);
}

    if(SelectedAssets.get(i).equals("CASH"))
        RepresentNumber = 1;
    else 
        RepresentNumber = MaxOfEachAssetMap.get(SelectedAssets.get(i));
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
        if(FundPercentageMap.get(SaveFundList.get(j).trim())==null)
		{	
			if (CanNotBuyFundList.indexOf(SaveFundList.get(j))== -1 || CurrentPortfolio.holdSecurity(getSecurity(SaveFundList.get(j)).getID())) //Adj for RoundTrip Part 3 LJF 2010.07.02
			{
			RepresentFundList.add(SaveFundList.get(j));
            FundPercentageMap.put(SaveFundList.get(j).trim(), Weight);
            printToLog(SelectedAssets.get(i) + " buy target :  " + SaveFundList.get(j) + "   target percentage : " + Weight);
			}
			else {	//Adj for RoundTrip Part 3 LJF 2010.07.02
				if (MaxIndex < SaveFundList.size()) {MaxIndex=MaxIndex+1;}     
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

/*LJF temp testing*/ printToLog("AssetRepresentFundMap 1:"+AssetRepresentFundMap);
//    Aggregate the CASH from different Assets, modified on 2010.08.25
if(SelectedAssets.indexOf("CASH") < 0)
{
    SelectedAssets.add("CASH");
    SelectedAssetPercentages.add(0.0);
}
for(int iAsset=0;iAsset<SelectedAssets.size();iAsset++){
	String AssetName=SelectedAssets.get(iAsset);
	if(!AssetName.equals("CASH")){
		List<String>RepreFundList=AssetRepresentFundMap.get(AssetName);
		int CASHFundIndex=RepreFundList.indexOf("CASH");
		if (CASHFundIndex==-1) continue;
		//Remove CASH from RepresentFundList
		RepreFundList.remove("CASH");
		AssetRepresentFundMap.put(AssetName,RepreFundList);
                List<String> tmpList = new ArrayList<String>();
                tmpList.add("CASH");
                AssetRepresentFundMap.put("CASH", tmpList);
		
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

//===============================================================
/*529 Plan : determine whether to rebalance*/

// is529Plan = true;   /*JUST FOR TEST*/
if(is529Plan)
    ToRebalance = compareTargetAndHoldingFunds(AssetRepresentFundMap);

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

/*
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
*/

printToLog("Asset Available Percentage = " + AssetAvailableTradingPercentage);
printToLog("Security Available Percentage = " + SecurityAvailableTradingPercentage);

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

printToLog("Asset Target Percentage Map = " + AssetTargetPercentageMap );

for(int i = 0; i < HoldingAssetList.size(); i++)
{
    aa = CurrentPortfolio.getAssetAmount(HoldingAssetList.get(i).getName(),CurrentDate);
    HoldingAssetActualPercentage.put(HoldingAssetList.get(i).getName().trim(), new Double(aa/ta));
}

printToLog("Asset Current Percentage Map = " + HoldingAssetActualPercentage );

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
        if((TargetPercentage - ActualPercentage)*BuyWeight > (BuyThreshold/100) )
            AssetActualTradingPercentage.put(AssetName, new Double((TargetPercentage - ActualPercentage)*BuyWeight) );
        else if((TargetPercentage - ActualPercentage)*BuyWeight + UnableToBuyPercentage  > (BuyThreshold/100))
        {
            AssetActualTradingPercentage.put(AssetName, new Double((TargetPercentage - ActualPercentage)*BuyWeight) +  UnableToBuyPercentage);
            UnableToBuyPercentage = 0;
        }
        else
        {
            AssetActualTradingPercentage.put(AssetName, new Double(0));
            UnableToBuyPercentage += ((TargetPercentage - ActualPercentage)*BuyWeight); 
            BelowMinimum = true;
            if((TargetPercentage - ActualPercentage) >= (BuyThreshold/100) )
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

printToLog("Asset Trading Percentage (Before the last step) : " + AssetActualTradingPercentage);
printToLog("Unable to trading percentage (Before the last step): " + UnableToBuyPercentage);

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
            if(tmpTrading < -(SellThreshold/100) || tmpTrading > (BuyThreshold/100))
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

printToLog("For Debug------------ HoldingAssetActualPercentage :" + HoldingAssetActualPercentage);

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
             printToLog("j = " + j + "  SecurityActualPercentage = " + SecurityActualPercentage + "  SellingPercentage = " + SellingPercentage + "  SecurityTargetPercentage = " + SecurityTargetPercentage  + "  TotalBuyingPercentage = " + TotalBuyingPercentage);
             if(SecurityActualPercentage - SellingPercentage >= SecurityTargetPercentage)
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
                     if(TotalBuyingPercentage - SellingPercentage < (BuyThreshold/100) && TotalBuyingPercentage - SellingPercentage > -(SellThreshold/100))
                     {
                          UnTradePercentage += TotalBuyingPercentage - SellingPercentage;
                          SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
                     }
                     else
                         SecurityActualTradingPercentage.put(SecurityName, new Double(TotalBuyingPercentage - SellingPercentage));
                     TotalBuyingPercentage = 0;
                      printToLog("condition 1-1");
                  }
                  else
                  {
                      if((-SellingPercentage) < (BuyThreshold/100) && (-SellingPercentage) > -(SellThreshold/100))
                     {
                          UnTradePercentage += (-SellingPercentage);
                          SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
                     }
                     else
                           SecurityActualTradingPercentage.put(SecurityName, new Double(- SellingPercentage));
                       TotalBuyingPercentage -= SellingPercentage ;
                       printToLog("condition 1-2");
                   }
             }
             else if(SecurityActualPercentage - SellingPercentage + TotalBuyingPercentage > SecurityTargetPercentage+ 0.001 && j < (PresentativeSecurityList.size() -1))
             {
                 printToLog("condition 2"); 
                  if(SecurityTargetPercentage - SecurityActualPercentage < (BuyThreshold/100) && SecurityTargetPercentage - SecurityActualPercentage > -(SellThreshold/100))
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
                 printToLog("condition 3"); 
                 if(TotalBuyingPercentage - SellingPercentage < (BuyThreshold/100) && TotalBuyingPercentage - SellingPercentage > -(SellThreshold/100) && SecurityActualPercentage != -(TotalBuyingPercentage - SellingPercentage))
                 {
                      UnTradePercentage += (TotalBuyingPercentage - SellingPercentage);
                      SecurityActualTradingPercentage.put(SecurityName, new Double(0.0));
                 }
                 else
                     SecurityActualTradingPercentage.put(SecurityName, new Double(TotalBuyingPercentage - SellingPercentage));
                 TotalBuyingPercentage = 0;
             }
             printToLog("SecurityActualTradingPercentage = " + SecurityActualTradingPercentage.get(SecurityName));
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
            if(TempRecord - TradeAmount < (BuyThreshold/100)  * TotalAmount)
                TradeAmount = TempRecord;
            CurrentPortfolio.buyAtNextOpen(AssetName, TradingSecurity,TradeAmount, CurrentDate,true);
            printToLog("Buy Signal: "+AssetName+"  "+TradingSecurity+"  $ "+ TradeAmount);
            if(TradeAmount < (BuyThreshold/100) * TotalAmount)
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


/*long sTime7 = System.currentTimeMillis();*/


//}catch(Exception eInit){printToLog("Action Error!!");}
		}
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Strategic_Asset_Allocation_for_old_engine_backup_09204827.java:209: reinit() in com.lti.type.executor.SimulateStrategy cannot be applied to (com.lti.service.bo.Portfolio,java.util.Hashtable<java.lang.String,java.lang.String>,java.util.Date)
//                 super.reinit(portfolio,parameters,pdate);
//                      ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Strategic_Asset_Allocation_for_old_engine_backup_09204827.java:207: method does not override or implement a method from a supertype
//@Override
//^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Strategic_Asset_Allocation_for_old_engine_backup_09204827.java:607: incompatible types
//found   : java.util.Map<java.lang.String,java.lang.String>
//required: java.util.List<com.lti.type.Pair>
//                                   attrs=_plan.getAttributes();
//                                                            ^
//3 errors