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
public class Copy_of_Guru_Directed_Asset_Allocation_1039 extends SimulateStrategy{
	public Copy_of_Guru_Directed_Asset_Allocation_1039(){
		super();
		StrategyID=1039L;
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
	private int RAAInterval;
	public void setRAAInterval(int RAAInterval){
		this.RAAInterval=RAAInterval;
	}
	
	public int getRAAInterval(){
		return this.RAAInterval;
	}
	private String RAAMethod;
	public void setRAAMethod(String RAAMethod){
		this.RAAMethod=RAAMethod;
	}
	
	public String getRAAMethod(){
		return this.RAAMethod;
	}
	private int TargetTopN;
	public void setTargetTopN(int TargetTopN){
		this.TargetTopN=TargetTopN;
	}
	
	public int getTargetTopN(){
		return this.TargetTopN;
	}
	private String[] GuruTargetFunds;
	public void setGuruTargetFunds(String[] GuruTargetFunds){
		this.GuruTargetFunds=GuruTargetFunds;
	}
	
	public String[] getGuruTargetFunds(){
		return this.GuruTargetFunds;
	}
	private double MoneyAllocation;
	public void setMoneyAllocation(double MoneyAllocation){
		this.MoneyAllocation=MoneyAllocation;
	}
	
	public double getMoneyAllocation(){
		return this.MoneyAllocation;
	}
	private String RiskAversion;
	public void setRiskAversion(String RiskAversion){
		this.RiskAversion=RiskAversion;
	}
	
	public String getRiskAversion(){
		return this.RiskAversion;
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
	private double RiskProfile;
	public void setRiskProfile(double RiskProfile){
		this.RiskProfile=RiskProfile;
	}
	
	public double getRiskProfile(){
		return this.RiskProfile;
	}
	private boolean UseRiskProfile;
	public void setUseRiskProfile(boolean UseRiskProfile){
		this.UseRiskProfile=UseRiskProfile;
	}
	
	public boolean getUseRiskProfile(){
		return this.UseRiskProfile;
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
		CandidateFund=(String[])ParameterUtil.fetchParameter("String[]","CandidateFund", "CHTVX,FOCPX, FUSVX, MSIVX, WFSVX, ^DWC, FSEMX, FIGRX, HAINX,FBNDX, VBMFX,CASH", parameters);
		AssetClass=(String[])ParameterUtil.fetchParameter("String[]","AssetClass", "US EQUITY, International Equity, US EQUITY, Real Estate, Real Estate, US EQUITY, US EQUITY, US EQUITY, US EQUITY, FIXED INCOME, FIXED INCOME, CASH", parameters);
		RedemptionLimit=(int[])ParameterUtil.fetchParameter("int[]","RedemptionLimit", "3,3,3,3,3,3,3,3,3,3,3,0", parameters);
		MainAssetClass=(String[])ParameterUtil.fetchParameter("String[]","MainAssetClass", "US EQUITY, INTERNATIONAL EQUITY, FIXED INCOME, REAL ESTATE, COMMODITIES, Emerging Market, INTERNATIONAL BONDS, High Yield Bond, CASH", parameters);
		MaxOfEachAsset=(Integer)ParameterUtil.fetchParameter("int","MaxOfEachAsset", "1", parameters);
		RebalanceFrequency=(String)ParameterUtil.fetchParameter("String","RebalanceFrequency", "monthly", parameters);
		SharpeMonths=(Integer)ParameterUtil.fetchParameter("int","SharpeMonths", "12", parameters);
		RAAInterval=(Integer)ParameterUtil.fetchParameter("int","RAAInterval", "30", parameters);
		RAAMethod=(String)ParameterUtil.fetchParameter("String","RAAMethod", "WLS", parameters);
		TargetTopN=(Integer)ParameterUtil.fetchParameter("int","TargetTopN", "2", parameters);
		GuruTargetFunds=(String[])ParameterUtil.fetchParameter("String[]","GuruTargetFunds", "HSGFX, HSTRX, GGHEX,GBMFX, LCORX, PASDX,WASYX,VAAPX, VWINX,GLRBX,FPACX, TFSMX, DHFCX, GATEX, WYASX, JABAX", parameters);
		MoneyAllocation=(Double)ParameterUtil.fetchParameter("double","MoneyAllocation", "0", parameters);
		RiskAversion=(String)ParameterUtil.fetchParameter("String","RiskAversion", "null", parameters);
		MinimumBuyingPercentage=(Double)ParameterUtil.fetchParameter("double","MinimumBuyingPercentage", "0.01", parameters);
		MinimumSellingPercentage=(Double)ParameterUtil.fetchParameter("double","MinimumSellingPercentage", "0", parameters);
		RiskProfile=(Double)ParameterUtil.fetchParameter("double","RiskProfile", "40", parameters);
		UseRiskProfile=(Boolean)ParameterUtil.fetchParameter("boolean","UseRiskProfile", "true", parameters);
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
List<String> AvailableStableAssetClassList = new ArrayList<String>();
StartDate = CurrentPortfolio.getStartingDate();
MoneyAllocation = 0;


//author WYJ RiskAversion Growth or Moderate of Conservative
/* 
double ConservativeRiskProfile = 60;
double ModerateRiskProfile = 40;
double GrowthRiskProfile = 20;
double StandardRiskProfile = 0;
if(UseRiskProfile)
{
    if(LetRiskProfileChange)
    {
    for(int i = 0; i < RiskProfileGroup.length - 1; i++)
        if(RiskProfile >= RiskProfileGroup[i] && RiskProfile < RiskProfileGroup[i+1])
            OriginalYearsToRetire = YearsToRetireGroup[i];
    }

    if(RiskProfile <= GrowthRiskProfile)
        { StandardRiskProfile = GrowthRiskProfile; RiskAversion = "Growth"; }
    else if(RiskProfile <= ModerateRiskProfile)
        { StandardRiskProfile = ModerateRiskProfile; RiskAversion = "Moderate"; }
    else 
        { StandardRiskProfile = ConservativeRiskProfile ; RiskAversion = "Conservative"; }
}
printToLog("standard risk profile = " + StandardRiskProfile);*/

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

	if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -SharpeMonths)) && LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -SharpeMonths))){
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
if(HaveCash == false) {CandidateFundList.add("CASH"); } //NOTE temp testing for CASH 

System.out.println("CandidateFundList initialize ok");

printToLog("CandidateFundList initialize ok");

/*Get the available asset classes and divide the funds*/

for(int i=0; i< MainAssetClass.length; i++)
{
    MainAssetClassList.add(MainAssetClass[i]);
}

printToLog("MainAssetClassList initialize ok");

HashMap<String,List<String>> AllAssetFundsMap = getAvailableAssetClassSet(MainAssetClassList, CandidateFundList);
/*Iterator iter = AllAssetFundsMap.keySet().iterator();
while(iter.hasNext()){
String key = (String) iter.next();
System.out.println(key);
System.out.println(AllAssetFundsMap.get(key));
}*/
for(int i = 0; i< MainAssetClass.length; i++)
{
    if(AllAssetFundsMap.get(MainAssetClass[i]) != null)
    {
        AvailableAssetClassList.add(MainAssetClass[i]);
        if(MainAssetClass[i].equals("FIXED INCOME") || MainAssetClass[i].equals("INTERNATIONAL BONDS") || MainAssetClass[i].equals("CASH"))
            AvailableStableAssetClassList.add(MainAssetClass[i]);
    }
}

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
String[] AssetBenchmarks = new String[AvailableAssetClassList.size()];
for(int i=0; i<AvailableAssetClassList.size();i++)
{
    if(AvailableAssetClassList.get(i).equals("CASH"))
        AssetBenchmarks[i] = "CASH";
    else
        AssetBenchmarks[i] = securityManager.get(assetClassManager.get(AvailableAssetClassList.get(i)).getBenchmarkID()).getSymbol();
    AssetBenchmarkMap.put(AvailableAssetClassList.get(i), AssetBenchmarks[i]);
	/*Testing*/printToLog(AvailableAssetClassList.get(i)+" ' Benchmark is "+ AssetBenchmarks[i]);
}

System.out.println("AssetBenchmarkMap initialize ok");
printToLog("AssetBenchmarkMap  initialize ok");

/*Calculate weights with Guru*/

//Adjust  when RiskAversion == null_getWeightFromGuru_version 2010.03.01
double[] WeightDouble;
if ((RiskAversion != null) && (! RiskAversion.equalsIgnoreCase("null") )) 
	{WeightDouble = getWeightFromGuru( GuruTargetFunds, AssetBenchmarks, TargetTopN, SharpeMonths,  MoneyAllocation, RAAMethod, RAAInterval, RiskAversion, CurrentDate, TimeUnit.DAILY);}
else { 	WeightDouble = getWeightFromGuru( GuruTargetFunds, AssetBenchmarks, TargetTopN, SharpeMonths,  MoneyAllocation, RAAMethod, RAAInterval, null, CurrentDate, TimeUnit.DAILY);
		/*testing*/String[] TopSecurity = getTopSecurityArray(GuruTargetFunds, -SharpeMonths,CurrentDate,TimeUnit.MONTHLY, SortType.SHARPE, false);
		/*testing*/printToLog("Top_2_Security: "+TopSecurity[0]+" , "+ TopSecurity[1]);
	}


//NOTE!!!When Interval changes, pay attention the Interval may > factorFunds. getEarliestAvailabePriceDate and WeightDouble returns 0!!!
/*getWeightFromGuru(String[] targetFunds,String[] factorFunds,int topN,int sharpeMonth,double moneyAllocation,String WLSOrOLS,int interval,String targetType,Date curDate,TimeUnit tu) */


//Adjustments for WeightDouble according to different RiskProfile_Version 2010.03.08
for(int i = 0; i< WeightDouble.length; i++)
    printToLog("Standard Suggested Weight Of " + AvailableAssetClassList.get(i) + " =  " +  WeightDouble[i]);

	//StandardRiskProfile should equal to ??, NOW we use 40 and the scale function is two lines connected the 0, standard, and 100 RP
double StandardRiskProfile = 40;
if(UseRiskProfile)
{
double SumStableWeight = 0;
int[] IsStable = new int[WeightDouble.length];

	//Determine the Scale Factor
	for(int i = 0; i< WeightDouble.length; i++)
	{
	IsStable[i] = 0;
    for(int j = 0 ; j< AvailableStableAssetClassList.size(); j++)
        if(AvailableAssetClassList.get(i).equals(AvailableStableAssetClassList.get(j)))
        {
			IsStable[i] = 1;
			SumStableWeight += WeightDouble[i];
			break;
		}
	}
	printToLog("SumStableWeight = " + SumStableWeight);
	double ScaleFactor;
	if ( RiskProfile >= StandardRiskProfile )
		{ScaleFactor = (1 - SumStableWeight)/(100 - StandardRiskProfile)*(RiskProfile - StandardRiskProfile)+1;}
	else 
		{ScaleFactor = (SumStableWeight - 0)/(StandardRiskProfile - 0)*(StandardRiskProfile - 0)+ 0;}
	
	// Scale the Stable Assets
	for(int i = 0; i< WeightDouble.length; i++)
	    if (IsStable [i] == 1){
            printToLog("Stable Asset Class: "+AvailableAssetClassList.get(i));
			printToLog("Suggested Weight = "+WeightDouble[i] + "  Scale Factor = " + ScaleFactor);
			WeightDouble[i] = WeightDouble[i] * ScaleFactor; //Using Threshold Function
            printToLog("Adjusted Weight = " + WeightDouble[i]);
        }
	// Scale the Risky Assets
	for(int i = 0; i< WeightDouble.length; i++)
	{
        if(IsStable[i] == 0)   
        {
			printToLog("Risky Asset Class: "+ AvailableAssetClassList.get(i));
			printToLog("Suggested Weight = "+WeightDouble[i] + "  Scale Factor = " + (1-SumStableWeight*ScaleFactor) / (1-SumStableWeight));             
			WeightDouble[i] = (1-SumStableWeight*ScaleFactor)* WeightDouble[i]/ (1-SumStableWeight); 
			printToLog("Adjusted weight = "+WeightDouble[i]);
        }
	}
}

for(int i = 0; i< WeightDouble.length; i++)
    printToLog("Weight Of " + AvailableAssetClassList.get(i) + " =  " +  WeightDouble[i]);

List<Double> Weights = new ArrayList<Double>();
for(int i = 0; i< WeightDouble.length; i++)
    Weights.add(WeightDouble[i]);

/*Create Assets*/

Asset CurrentAsset;
for(int i=0; i < Weights.size(); i++)
{
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(AvailableAssetClassList.get(i));
CurrentAsset.setClassID(getAssetClassID(AvailableAssetClassList.get(i)));
CurrentAsset.setTargetPercentage(Weights.get(i).doubleValue());
CurrentPortfolio.addAsset(CurrentAsset);
printToLog("Assets: "+ CurrentAsset.getName() + "  weight : " + Weights.get(i));

}
System.out.println("Assets initialize ok");
printToLog("Assets initialize ok");

/*Buy representative funds*/

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);

List<String> PresentativeFunds = null;
for(int i =0; i < Weights.size(); i++)
{
    PresentativeFunds = new ArrayList<String>();
    PresentativeFunds = PresentativeAssetFundMap.get(AvailableAssetClassList.get(i));
    printToLog("Assets buy: "+ AvailableAssetClassList.get(i));
    printToLog("PresentativeFund buy: "+ PresentativeFunds.get(0));
    for(int j=0; j< PresentativeFunds.size(); j++)
       CurrentPortfolio.buyAtNextOpen(AvailableAssetClassList.get(i), PresentativeFunds.get(j), TotalAmount*Weights.get(i) / PresentativeFunds.size(), CurrentDate,true); 
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
RedemptionLimitMap.put("CASH",0); //NOTE temp testing for CASH 

printToLog("Common 1");
/*Form Holding Map , calculate available tading amount of each security and  each asset*/
List<HoldingItem> HoldingSecurityList = null;
double ta = CurrentPortfolio.getTotalAmount(CurrentDate);
String sn;
double sa;
double aa;

for(int i = 0; i < HoldingAssetList.size(); i++)
{
    printToLog("Common 2");
    HoldingSecurityList = new ArrayList<HoldingItem>();
    HoldingSecurityList = HoldingAssetList.get(i).getHoldingItems();
    HoldingAssetSecurityMap.put(HoldingAssetList.get(i).getName(),HoldingSecurityList);
    aa = 0;
    for(int j = 0; j < HoldingSecurityList.size(); j++)
    {
        sn = HoldingSecurityList.get(j).getSymbol();
        sa = CurrentPortfolio.getAvailableTradingAmount(sn, RedemptionLimitMap.get(sn).intValue(), CurrentDate,1);
       SecurityAvailableTradingPercentage.put(sn, new Double(sa/ta));
        aa += sa;
    }
    if(aa != 0) ToCheck = true;
    AssetAvailableTradingPercentage.put(HoldingAssetList.get(i).getName(), new Double(aa/ta));
    printToLog("Common 3" + aa);
}
}



		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (ToCheck && ((RebalanceFrequency.equals("monthly") && LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) || (RebalanceFrequency.equals("quarterly") && LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate))) ) {
		   List<String> CandidateFundList = new ArrayList<String>();
List<String> MainAssetClassList = new ArrayList<String>();
List<String> AvailableAssetClassList = new ArrayList<String>();
List<String> AvailableStableAssetClassList = new ArrayList<String>();
MoneyAllocation = 0;

//
/*
double ConservativeRiskProfile = 60;
double ModerateRiskProfile = 40;
double GrowthRiskProfile = 20;
double StandardRiskProfile = 0;
if(UseRiskProfile)
{
    if(LetRiskProfileChange)
    {
    double YearsToRetire = OriginalYearsToRetire -  java.lang.Math.floor(LTIDate.calculateInterval(StartDate,CurrentDate) / 365);   
    for(int i = 0; i < YearsToRetireGroup.length - 1; i++)
        if(YearsToRetire <= YearsToRetireGroup[i] && YearsToRetire > YearsToRetireGroup[i+1])
            RiskProfile = RiskProfileGroup[i];
    }
 
    if(RiskProfile <= GrowthRiskProfile)
        { StandardRiskProfile = GrowthRiskProfile; RiskAversion = "Growth"; }
    else if(RiskProfile <= ModerateRiskProfile)
        { StandardRiskProfile = ModerateRiskProfile; RiskAversion = "Moderate"; }
    else 
        { StandardRiskProfile = ConservativeRiskProfile ; RiskAversion = "Conservative"; }
} */


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

/* Choose_Available_CandidateFunds (Version 2010.03.04)*/
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

	if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(CandidateFund[i])), LTIDate. getNewNYSEMonth(CurrentDate, -SharpeMonths)) && 

LTIDate.before(getEarliestAvaliablePriceDate(Bench), LTIDate. getNewNYSEMonth(CurrentDate, -SharpeMonths))){
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
if(HaveCash == false) {CandidateFundList.add("CASH");}  //NOTE temp testing for CASH 


/*Get the available asset classes and divide the funds*/

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
        if(MainAssetClass[i].equals("FIXED INCOME") || MainAssetClass[i].equals("INTERNATIONAL BONDS") || MainAssetClass[i].equals("CASH"))
            AvailableStableAssetClassList.add(MainAssetClass[i]);
    }
}
printToLog("MainAssetClassList initialize ok");

/*Select the presentative funds of each asset class*/

HashMap<String,List<String>>  PresentativeAssetFundMap = new HashMap<String,List<String>>();
PresentativeAssetFundMap = getPresentativeAssetFunds(AllAssetFundsMap,  AvailableAssetClassList, SharpeMonths, MaxOfEachAsset);

for(int i =0; i < PresentativeAssetFundMap.size(); i++)
{
    printToLog(AvailableAssetClassList.get(i) + " has " + PresentativeAssetFundMap.get(AvailableAssetClassList.get(i)).size() +" Presentative Fund/Funds ");
	/*Testing*/printToLog(PresentativeAssetFundMap.get(AvailableAssetClassList.get(i)));
}

/*Get the Benchmark of each asset class*/

HashMap<String, String>  AssetBenchmarkMap = new HashMap<String,String>();
String[] AssetBenchmarks = new String[AvailableAssetClassList.size()];
for(int i=0; i<AvailableAssetClassList.size();i++)
{
    if(AvailableAssetClassList.get(i).equals("CASH"))
        AssetBenchmarks[i] = "CASH";
    else
        AssetBenchmarks[i] = securityManager.get(assetClassManager.get(AvailableAssetClassList.get(i)).getBenchmarkID()).getSymbol();
    AssetBenchmarkMap.put(AvailableAssetClassList.get(i), AssetBenchmarks[i]);
	/*Testing*/printToLog(AvailableAssetClassList.get(i)+" ' Benchmark is "+ AssetBenchmarks[i]);
}

/*Calculate weights with Guru*/

//Adjust  when RiskAversion == null_getWeightFromGuru_version 2010.03.01
double[] WeightDouble;
if ((RiskAversion != null) && (! RiskAversion.equalsIgnoreCase("null") )) 
	{WeightDouble = getWeightFromGuru( GuruTargetFunds, AssetBenchmarks, TargetTopN, SharpeMonths,  MoneyAllocation, RAAMethod, RAAInterval, RiskAversion, CurrentDate, TimeUnit.DAILY);}
else { 	WeightDouble = getWeightFromGuru( GuruTargetFunds, AssetBenchmarks, TargetTopN, SharpeMonths,  MoneyAllocation, RAAMethod, RAAInterval, null, CurrentDate, TimeUnit.DAILY);
		/*testing*/String[] TopSecurity = getTopSecurityArray(GuruTargetFunds, -SharpeMonths,CurrentDate, TimeUnit.MONTHLY, SortType.SHARPE, false);
		/*testing*/printToLog("Top_2_Security: "+TopSecurity[0]+" , "+ TopSecurity[1]);
	}


//NOTE!!!When Interval changes, pay attention the Interval may > factorFunds. getEarliestAvailabePriceDate and WeightDouble returns 0!!!
/*getWeightFromGuru(String[] targetFunds,String[] factorFunds,int topN,int sharpeMonth,double moneyAllocation,String WLSOrOLS,int interval,String targetType,Date curDate,TimeUnit tu) */


//Adjustments for WeightDouble according to different RiskProfile_Version 2010.03.08
for(int i = 0; i< WeightDouble.length; i++)
    printToLog("Standard Suggested Weight Of " + AvailableAssetClassList.get(i) + " =  " +  WeightDouble[i]);

	//StandardRiskProfile should equal to ??, NOW we use 40 and the scale function is two lines connected the 0, standard, and 100 RP
double StandardRiskProfile = 40;
if(UseRiskProfile)
{
double SumStableWeight = 0;
int[] IsStable = new int[WeightDouble.length];

	//Determine the Scale Factor
	for(int i = 0; i< WeightDouble.length; i++)
	{
	IsStable[i] = 0;
    for(int j = 0 ; j< AvailableStableAssetClassList.size(); j++)
        if(AvailableAssetClassList.get(i).equals(AvailableStableAssetClassList.get(j)))
        {
			IsStable[i] = 1;
			SumStableWeight += WeightDouble[i];
			break;
		}
	}
	printToLog("SumStableWeight = " + SumStableWeight);
	double ScaleFactor;
	if ( RiskProfile >= StandardRiskProfile )
		{ScaleFactor = (1 - SumStableWeight)/(100 - StandardRiskProfile)*(RiskProfile - StandardRiskProfile)+1;}
	else 
		{ScaleFactor = (SumStableWeight - 0)/(StandardRiskProfile - 0)*(StandardRiskProfile - 0)+ 0;}
	
	// Scale the Stable Assets
	for(int i = 0; i< WeightDouble.length; i++)
	    if (IsStable [i] == 1){
            printToLog("Stable Asset Class: "+AvailableAssetClassList.get(i));
			printToLog("Suggested Weight = "+WeightDouble[i] + "  Scale Factor = " + ScaleFactor);
			WeightDouble[i] = WeightDouble[i] * ScaleFactor; //Using Threshold Function
            printToLog("Adjusted Weight = " + WeightDouble[i]);
        }
	// Scale the Risky Assets
	for(int i = 0; i< WeightDouble.length; i++)
	{
        if(IsStable[i] == 0)   
        {
			printToLog("Risky Asset Class: "+ AvailableAssetClassList.get(i));
			printToLog("Suggested Weight = "+WeightDouble[i] + "  Scale Factor = " + (1-SumStableWeight*ScaleFactor) / (1-SumStableWeight));      

       
			WeightDouble[i] = (1-SumStableWeight*ScaleFactor)* WeightDouble[i]/ (1-SumStableWeight); 
			printToLog("Adjusted weight = "+WeightDouble[i]);
        }
	}
}

for(int i = 0; i< WeightDouble.length; i++)
    printToLog("Weight Of " + AvailableAssetClassList.get(i) + " =  " +  WeightDouble[i]);

List<Double> Weights = new ArrayList<Double>();
for(int i = 0; i< WeightDouble.length; i++)
    Weights.add(WeightDouble[i]);

//Asset Creatation and Set TargetPercentage
Asset CurrentAsset = null;
for(int i=0; i < Weights.size(); i++)
{
    if(AssetAvailableTradingPercentage.get(AvailableAssetClassList.get(i)) != null)
    {
        CurrentAsset=new Asset();
        CurrentAsset = CurrentPortfolio.getAsset(AvailableAssetClassList.get(i));
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
//updated 2010/02/04
/*trading and rebalance under redemption limit*/

double AvailCapital=CurrentPortfolio.getCash();
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
List<Asset> CurrentAssetList = CurrentPortfolio.getCurrentAssetList(); /*List<Asset> CurrentAssetList has already declared*/
HashMap<String, Double>UnfavClassAmountMap=new HashMap<String, Double>();	/*UnfavClassAmountMap Stores the amount of which those funds should be sold, 

but under RedemptionLimit they can not*/
List <String> FavSecurNameList=new ArrayList<String>();   
printToLog("------Sell Action ------");

for (int i=0; i<CurrentAssetList.size();i++){   
    CurrentAsset = CurrentAssetList.get(i);  
    printToLog("Asset name: "+CurrentAsset.getName());
    List<HoldingItem> CurrSecurList=CurrentAsset.getHoldingItems();
    if (PresentativeAssetFundMap.get(CurrentAsset.getName())!= null)       
        {FavSecurNameList=PresentativeAssetFundMap.get(CurrentAsset.getName());}   
    else printToLog(" Unexpected error occured while trading ");   
    double UnfavClassAmount=0;   
	
    for(int j=0; j<CurrSecurList.size();j++){   
        if (FavSecurNameList.indexOf(CurrSecurList.get(j).getSymbol())<0)      /*Sell the poor-performance funds as much as possible*/{    
printToLog(CurrSecurList.get(j));
printToLog("Symbol :" +CurrSecurList.get(j).getSymbol());
            double SellAmount=CurrentPortfolio.getAvailableTradingAmount(CurrSecurList.get(j).getSymbol(), RedemptionMonthMap.get(CurrSecurList.get

(j).getSymbol()), CurrentDate,1);
			/*Testing*/printToLog("AvailSellAmount ="+SellAmount);
            if (SellAmount>TotalAmount*MinimumSellingPercentage){   
            printToLog("Asset Name: "+CurrentAsset.getName());
            CurrentPortfolio.sellAtNextOpen(CurrentAsset.getName(), CurrSecurList.get(j).getSymbol(), SellAmount, CurrentDate);   
            printToLog("SELL type 1:"+CurrentAsset.getName()+"  "+CurrSecurList.get(j).getSymbol()+"  "+SellAmount);   
AvailCapital+=SellAmount;
			} 
			
		 UnfavClassAmount+=CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(), CurrSecurList.get(j).getSymbol(),CurrentDate)-SellAmount;
           
        }   
        else {
        double TargetSecurPercentPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size();/*Allocate Averagely :Equal Weight for each funds in a 

certain AssetClass*/
        double CurrentSecurPercentPortLevel=CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),CurrSecurList.get(j).getSymbol

(),CurrentDate)/TotalAmount;
		double AvailSellAmount;
			/*Action for the CASH ASSET*/
			if (!CurrSecurList.get(j).getSymbol().equals("CASH")){AvailSellAmount=CurrentPortfolio.getAvailableTradingAmount(CurrSecurList.get

(j).getSymbol(), RedemptionMonthMap.get(CurrSecurList.get(j).getSymbol()), CurrentDate,1);}
			else {AvailSellAmount= CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(), CurrSecurList.get(j).getSymbol(),CurrentDate);}
		/*Testing*/printToLog("AvailSellAmount ="+AvailSellAmount+" ; "+" SpreadAmount= "+(CurrentSecurPercentPortLevel-TargetSecurPercentPortLevel)

*TotalAmount);
        double SellAmount=java.lang.Math.min(AvailSellAmount/TotalAmount,(CurrentSecurPercentPortLevel-TargetSecurPercentPortLevel))*TotalAmount;
            if (SellAmount>TotalAmount*MinimumSellingPercentage){   
            CurrentPortfolio.sellAtNextOpen(CurrentAsset.getName(),CurrSecurList.get(j).getSymbol(),SellAmount, CurrentDate);   
			printToLog("SELL type 2:"+CurrentAsset.getName()+"  "+CurrSecurList.get(j).getSymbol()+"  "+SellAmount);    
AvailCapital+=SellAmount;
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
	Double Amount=CurrentAsset.getTargetPercentage()-CurrentPortfolio.getAssetAmount(CurrentAsset,CurrentDate)/TotalAmount;
	if (Amount>MinimumBuyingPercentage){
	ClassWeightMap.put(AssetName,Amount);
	TempSum=TempSum+Amount;
	/*Testing*/printToLog("Weight at Class Level:  "+AssetName+",   SpreadPercentage="+Amount);
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
/*Testing*/printToLog("AvailCapital = "+AvailCapital);/*available capital that can be used to buy other funds*/  
double UseCapital=0;//UseCapital Stores the amount already used for Buy Action

printToLog("--------BUY ACTION--------");

iter = PresentativeAssetFundMap.entrySet().iterator(); 
while (iter.hasNext()) { 
    Map.Entry<String,List<String>> entry = (Map.Entry<String,List<String>>) iter.next(); 
    AssetName = entry.getKey(); 
    FavSecurNameList = entry.getValue(); 
	Double UnfavPercent;
	CurrentAsset=CurrentPortfolio.getAsset(AssetName);
	double[] SecurWeight=new double[FavSecurNameList.size()];
	if (UnfavClassAmountMap.get(AssetName)!=null) {UnfavPercent=UnfavClassAmountMap.get(AssetName)/TotalAmount;}
	else {UnfavPercent=new Double(0);}
	double TotalAssetAmount=CurrentPortfolio.getAssetAmount(CurrentAsset,CurrentDate);
	if (CurrentAsset.getTargetPercentage()-UnfavPercent>MinimumBuyingPercentage) {
		double TargetSecurPercentAtPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size();/*Allocate Averagely :Equal Weight for each 

funds in a certain AssetClass*/
		double TempWeightTotal=0;
		for (int i=0;i<FavSecurNameList.size();i++){
			if (CurrentAsset.getHoldingItems().contains(FavSecurNameList.get(i)))/*for funds already exist*/{
				double SpreadPercent=TargetSecurPercentAtPortLevel-CurrentPortfolio.getSecurityAmount(CurrentAsset.getName

(),FavSecurNameList.get(i),CurrentDate)/TotalAmount;
				if (SpreadPercent>MinimumBuyingPercentage) {SecurWeight[i]=SpreadPercent;TempWeightTotal+=SecurWeight[i];}
				}
			else if (CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),FavSecurNameList.get

(i),CurrentDate)/TotalAmount>MinimumBuyingPercentage) /*for new funds*/
				{SecurWeight[i]=CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),FavSecurNameList.get

(i),CurrentDate)/TotalAmount;TempWeightTotal+=SecurWeight[i];}
		}
		
		if (TempWeightTotal>0.01){
			for (int i=0; i<FavSecurNameList.size();i++){
			SecurWeight[i]=SecurWeight[i]/TempWeightTotal;
			}
			/*buy Securities*/
			for (int i=0; i<FavSecurNameList.size();i++){
				/*Testing*/printToLog("SecurWeight[i]= "+SecurWeight[i]+" ; "+" ClassWeight: = "+ClassWeightMap.get(AssetName));
				double Amount=AvailCapital*SecurWeight[i]*ClassWeightMap.get(AssetName);
				if (Amount>TotalAmount*MinimumBuyingPercentage) {
					CurrentPortfolio.buyAtNextOpen(CurrentAsset.getName(),FavSecurNameList.get(i),Amount,CurrentDate,true);
					printToLog("BUY TYPE 1  "+CurrentAsset.getName()+" "+FavSecurNameList.get(i)+" "+Amount);
UseCapital+=Amount;				
				//Unfinished
				
				} 
			}
		} //  End if (TempWeightTotal>0.01)
		
		if (TempWeightTotal<=0.01){
			int Index=FavSecurNameList.size()-1;
			for (int i=0; i<FavSecurNameList.size();i++){
				double CurrentSecurPercentPortLevel=CurrentPortfolio.getSecurityAmount(CurrentAsset.getName(),FavSecurNameList.get

(i),CurrentDate)/TotalAmount;
				double TargetSecurPercentPortLevel=CurrentAsset.getTargetPercentage()/FavSecurNameList.size();/*Allocate Averagely :Equal 

Weight for each funds in a certain AssetClass*/
				if (CurrentSecurPercentPortLevel + MinimumBuyingPercentage< TargetSecurPercentPortLevel){
					Index=i;
					break;
				}
			}
			/*buy Securities*/
			SecurWeight[Index]=1.0;
				/*Testing*/printToLog("SecurWeight[Index]= "+SecurWeight[Index]+" ; "+" ClassWeight: = "+ClassWeightMap.get(AssetName));
			double Amount=AvailCapital*SecurWeight[Index]*ClassWeightMap.get(AssetName);
			if (Amount > TotalAmount*MinimumBuyingPercentage){
			CurrentPortfolio.buyAtNextOpen(CurrentAsset.getName(),FavSecurNameList.get(Index),Amount, CurrentDate,true);
			printToLog("BUY TYPE 2  "+CurrentAsset.getName()+"   "+FavSecurNameList.get(Index)+" "+Amount);
UseCapital+=Amount;
			} 
		} //  End if (TempWeightTotal<=0.01)
		
	}
}	// End of While

double Amount=AvailCapital-UseCapital;
if (Amount > 1) {
	iter = PresentativeAssetFundMap.entrySet().iterator(); 
    Map.Entry<String,List<String>> entry = (Map.Entry<String,List<String>>) iter.next(); 
    AssetName = entry.getKey(); 
    FavSecurNameList = entry.getValue(); 
	CurrentAsset=CurrentPortfolio.getAsset(AssetName);
	CurrentPortfolio.buyAtNextOpen(AssetName,getSecurity(FavSecurNameList.get(0)).getSymbol(), Amount, CurrentDate,true);
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