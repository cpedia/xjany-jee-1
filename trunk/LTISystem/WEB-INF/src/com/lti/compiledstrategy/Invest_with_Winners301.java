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
public class Invest_with_Winners301 extends SimulateStrategy{
	public Invest_with_Winners301(){
		super();
		StrategyID=301L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private double NumberInvest;
	public void setNumberInvest(double NumberInvest){
		this.NumberInvest=NumberInvest;
	}
	
	public double getNumberInvest(){
		return this.NumberInvest;
	}
	private int MonthOfNegativeReturn;
	public void setMonthOfNegativeReturn(int MonthOfNegativeReturn){
		this.MonthOfNegativeReturn=MonthOfNegativeReturn;
	}
	
	public int getMonthOfNegativeReturn(){
		return this.MonthOfNegativeReturn;
	}
	private double PercentageBelowRecentHigh;
	public void setPercentageBelowRecentHigh(double PercentageBelowRecentHigh){
		this.PercentageBelowRecentHigh=PercentageBelowRecentHigh;
	}
	
	public double getPercentageBelowRecentHigh(){
		return this.PercentageBelowRecentHigh;
	}
	private int MonthRecentHigh;
	public void setMonthRecentHigh(int MonthRecentHigh){
		this.MonthRecentHigh=MonthRecentHigh;
	}
	
	public int getMonthRecentHigh(){
		return this.MonthRecentHigh;
	}
	private double PercentageInRank;
	public void setPercentageInRank(double PercentageInRank){
		this.PercentageInRank=PercentageInRank;
	}
	
	public double getPercentageInRank(){
		return this.PercentageInRank;
	}
	private int BuyFrequency;
	public void setBuyFrequency(int BuyFrequency){
		this.BuyFrequency=BuyFrequency;
	}
	
	public int getBuyFrequency(){
		return this.BuyFrequency;
	}
	private String[] SecurityPoolString1;
	public void setSecurityPoolString1(String[] SecurityPoolString1){
		this.SecurityPoolString1=SecurityPoolString1;
	}
	
	public String[] getSecurityPoolString1(){
		return this.SecurityPoolString1;
	}
	private boolean InvestHolding;
	public void setInvestHolding(boolean InvestHolding){
		this.InvestHolding=InvestHolding;
	}
	
	public boolean getInvestHolding(){
		return this.InvestHolding;
	}
	private boolean UseCategary;
	public void setUseCategary(boolean UseCategary){
		this.UseCategary=UseCategary;
	}
	
	public boolean getUseCategary(){
		return this.UseCategary;
	}
	private String Categary;
	public void setCategary(String Categary){
		this.Categary=Categary;
	}
	
	public String getCategary(){
		return this.Categary;
	}
	private String TopType;
	public void setTopType(String TopType){
		this.TopType=TopType;
	}
	
	public String getTopType(){
		return this.TopType;
	}
	private int TopN;
	public void setTopN(int TopN){
		this.TopN=TopN;
	}
	
	public int getTopN(){
		return this.TopN;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		NumberInvest=(Double)ParameterUtil.fetchParameter("double","NumberInvest", "2", parameters);
		MonthOfNegativeReturn=(Integer)ParameterUtil.fetchParameter("int","MonthOfNegativeReturn", "1", parameters);
		PercentageBelowRecentHigh=(Double)ParameterUtil.fetchParameter("double","PercentageBelowRecentHigh", "5", parameters);
		MonthRecentHigh=(Integer)ParameterUtil.fetchParameter("int","MonthRecentHigh", "1", parameters);
		PercentageInRank=(Double)ParameterUtil.fetchParameter("double","PercentageInRank", "40", parameters);
		BuyFrequency=(Integer)ParameterUtil.fetchParameter("int","BuyFrequency", "1", parameters);
		SecurityPoolString1=(String[])ParameterUtil.fetchParameter("String[]","SecurityPoolString1", "SPY, MDY, IWM, IWN, IWC, DIA,OEF, QQQQ, XLP, XLF, XLY, XLV, XLI, XLU, XLK, XLB, XLE, SMH, BBH, GDX, RKH, RTH, OIH, PPH, XHB, EFA, EEM,  EWJ, EWY, FXI, EWZ, IYR, DBC, DBA, GLD, DBB, TLT, TIP, HYG", parameters);
		InvestHolding=(Boolean)ParameterUtil.fetchParameter("boolean","InvestHolding", "false", parameters);
		UseCategary=(Boolean)ParameterUtil.fetchParameter("boolean","UseCategary", "true", parameters);
		Categary=(String)ParameterUtil.fetchParameter("String","Categary", "value", parameters);
		TopType=(String)ParameterUtil.fetchParameter("String","TopType", "popular", parameters);
		TopN=(Integer)ParameterUtil.fetchParameter("int","TopN", "50", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	/*String[] SecurityPoolString = {"SPY", "MDY", "IWM", "IWN", "IWC", "DIA", "OEF", "QQQQ", "XLP", "XLF", "XLY", "XLV", "XLI", "XLU", "XLK", "XLB", "XLE", "SMH", "BBH", "GDX", "RKH", "RTH", "OIH", "PPH", "XHB", "EFA", "EEM",  "EWJ", "EWY", "FXI", "EWZ", "IYR", "DBC", "DBA", "GLD", "DBB", "TLT", "TIP", "HYG"}; */
List<Security> SecurityPool;
List<Security> SecurityBuy = new ArrayList<Security>();
double[] PastPerformanceScore;
String[] SecurityPoolString;
int[] IndexOfSellSecurity;
double MaxScore;
int MaxScoreIndex;
int BuyNumber = 0;
double AcceptableLowestRank;
Date StartDate; 
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
	public static Long[] getIDs(List<Security> symbols) throws Exception {
       Long[] ids=new Long[symbols.size()];
       for(int i=0;i<ids.length;i++){
           ids[i]=Long.parseLong(symbols.get(i).getSymbol().substring(2));
       }
       return ids;
    }

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		StartDate = CurrentDate;
SecurityPool = new ArrayList<Security>();

if(InvestHolding)
{
    Date LastMonthEnd = LTIDate.getMonthEnd(LTIDate.getLastMonthDate(CurrentDate));
    LastMonthEnd = LTIDate.getRecentNYSETradingDay(LastMonthEnd);
    List<Portfolio> CandidateFunds = null;
    if(UseCategary == true)
        CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
    else
        CandidateFunds = getPortfolioList(SecurityPoolString);
    List<Security> AvailableFunds = new ArrayList<Security>();
    List <HoldingItem> TopHoldings = null;

    for(int i = 0; i<=CandidateFunds.size()-1;i++)
    {
        if(LTIDate.before(CandidateFunds.get(i).getStartingDate(),LastMonthEnd))
             try{
            Security sfund=getSecurity(CandidateFunds.get(i).getSymbol());
            sfund.getClose(LastMonthEnd);
            AvailableFunds.add(sfund);
            }catch(Exception e){
            }
    }

    Long[] AvailableFundsLong = getIDs(AvailableFunds);
    if(TopType.equals("popular"))
    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, PortfolioHoldingUtil.SORT_BY_POPULAR, 0, TopN-1);
    if(TopType.equals("weighted"))
    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, 2, 0, TopN -1);

    PastPerformanceScore = new double[TopHoldings.size()];
    SecurityPoolString = new String[TopHoldings.size()];
    
    for(int i = 0; i<=TopHoldings.size()-1; i++)
    {
         printToLog("holding  " +TopHoldings.get(i).getSymbol());
        SecurityPool.add(getSecurity(TopHoldings.get(i).getSymbol()));
        SecurityPoolString[i] = TopHoldings.get(i).getSymbol();
    }
}
else 
{
    SecurityPoolString = SecurityPoolString1;
    SecurityPool = getSecurityList(SecurityPoolString);
    PastPerformanceScore = new double[SecurityPoolString.length];
}
printToLog( "SecurityPool size = " + SecurityPool.size() +  "SecurityPoolString size  = "  + SecurityPoolString.length);


/*Calculate the average of the securities' 1, 3, 6, 9, 12 month returns*/ 
int k = 0;
for(int i=0; i<=SecurityPool.size()-1; i++)
{
  if(LTIDate.before(getEarliestAvaliablePriceDate(SecurityPool.get(i)), LTIDate.getNewNYSEMonth(CurrentDate, -12)))
    PastPerformanceScore[i] = (SecurityPool.get(i).getReturn(CurrentDate, TimeUnit.MONTHLY, -1)+SecurityPool.get(i).getReturn(CurrentDate, TimeUnit.MONTHLY, -3)+SecurityPool.get(i).getReturn(CurrentDate, TimeUnit.MONTHLY, -6)+SecurityPool.get(i).getReturn(CurrentDate, TimeUnit.MONTHLY, -9)+SecurityPool.get(i).getReturn(CurrentDate, TimeUnit.MONTHLY, -12))*100/5;
  else
  {  
    PastPerformanceScore[i] = -10000;
    k+=1;
  }
}
/*Sort the securities by PastPerformanceScore*/
AcceptableLowestRank = java.lang.Math.floor((PastPerformanceScore.length-k)*PercentageInRank/100); 
for(int i=0; i<=AcceptableLowestRank-1; i++)
{
 
 MaxScore = PastPerformanceScore[i];
 MaxScoreIndex =i;
  
   for(int j=i+1; j<=PastPerformanceScore.length-1; j++)
   {     
     if(MaxScore <= PastPerformanceScore[j])
     {
      MaxScore = PastPerformanceScore[j];
      MaxScoreIndex = j;
     }
   }
   
   if(MaxScoreIndex !=i)
   {
     String tmp= SecurityPoolString[MaxScoreIndex];
     SecurityPoolString[MaxScoreIndex]= SecurityPoolString[i];
     SecurityPoolString[i]=tmp;   
   
     double t=PastPerformanceScore[MaxScoreIndex];
     PastPerformanceScore[MaxScoreIndex]=PastPerformanceScore[i];
     PastPerformanceScore[i]=t;
   }
}
SecurityPool = getSecurityList(SecurityPoolString);

String log_m="After Ordering (the top "+PercentageInRank+"%)" +"\r\n";
for(int i=0;i<=AcceptableLowestRank-1;i++)
{
        log_m+=SecurityPoolString[i] +" ";
        log_m+=PastPerformanceScore[i];
        log_m+="\r\n";      
}
printToLog(log_m);

/*buy securities according to the buy rules*/
if(AcceptableLowestRank>0.5)
{ printToLog("a");
for(int i=0; i<=AcceptableLowestRank-1; i++)
{  printToLog("b" + i);
  if(SecurityPool.get(i).getReturn(LTIDate.getNewNYSEMonth(CurrentDate, -MonthOfNegativeReturn),CurrentDate)>=0)
  { printToLog("c" + i);
    if((1-PercentageBelowRecentHigh/100)*SecurityPool.get(i).getHighestPrice(LTIDate.getNewDate(CurrentDate,TimeUnit.MONTHLY, -MonthRecentHigh),CurrentDate)<SecurityPool.get(i).getCurrentPrice(CurrentDate))
    {
      SecurityBuy.add(SecurityPool.get(i));
      BuyNumber+=1;  printToLog("buynumber =  " + BuyNumber);
    }
  }
  if(BuyNumber==NumberInvest) break;
}
}
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;
for(int i=1; i<=NumberInvest;i++)
{
  if(i<=BuyNumber)
  {
    CurrentAsset=new Asset();
    CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
    CurrentAsset.setName("Asset"+i);
    CurrentAsset.setClassID(SecurityBuy.get(i-1).getAssetClassID());
    CurrentAsset.setTargetPercentage(1/NumberInvest);
    CurrentPortfolio.addAsset(CurrentAsset);
    CurrentPortfolio.buy("Asset"+i, SecurityBuy.get(i-1).getName(), TotalAmount/NumberInvest, CurrentDate); 
    printToLog("buy  " + SecurityBuy.get(i-1).getName());
  }
  else
  {
    CurrentAsset=new Asset();
    CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
    CurrentAsset.setName("Asset"+i);    
    CurrentAsset.setTargetPercentage(1/NumberInvest);
    CurrentPortfolio.addAsset(CurrentAsset);
  }
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
	
		
		
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.isNYSETradingDay(CurrentDate)) {
		   printToLog( "SecurityPool size = " + SecurityPool.size() +  "SecurityPoolString size  = "  + SecurityPoolString.length);
if(InvestHolding)
{
    Calendar calendar=Calendar.getInstance();
    calendar.setTime(CurrentDate);
    int month=calendar.get(Calendar.MONTH)+1;
    Date MonthEnd = LTIDate.getMonthEnd(CurrentDate);
    MonthEnd = LTIDate.getRecentNYSETradingDay(MonthEnd);
    if((month == 2 || month == 5 || month == 8|| month == 11) && LTIDate.equals(CurrentDate , MonthEnd))
    {
    SecurityPool = new ArrayList<Security>();
    List<Portfolio> CandidateFunds = null;
    if(UseCategary == true)
        CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
    else
        CandidateFunds = getPortfolioList(SecurityPoolString);
    List<Security> AvailableFunds = new ArrayList<Security>();
    List <HoldingItem> TopHoldings = null;

    for(int i = 0; i<=CandidateFunds.size()-1;i++)
    {
        if(LTIDate.before(CandidateFunds.get(i).getStartingDate(),CurrentDate))
             try{
            Security sfund=getSecurity(CandidateFunds.get(i).getSymbol());
            sfund.getClose(CurrentDate);
            AvailableFunds.add(sfund);
            }catch(Exception e){
            }
    }

    Long[] AvailableFundsLong = getIDs(AvailableFunds);
    if(TopType.equals("popular"))
    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  CurrentDate, PortfolioHoldingUtil.SORT_BY_POPULAR, 0, TopN-1);
    if(TopType.equals("weighted"))
    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  CurrentDate, 2, 0, TopN -1);
    PastPerformanceScore = new double[TopHoldings.size()];
    SecurityPoolString = new String[TopHoldings.size()];
    
    for(int i = 0; i<=TopHoldings.size()-1; i++)
    {
        SecurityPool.add(getSecurity(TopHoldings.get(i).getSymbol()));
        SecurityPoolString[i] = TopHoldings.get(i).getSymbol();
    }
} 
}

/*Check the sell rules of negative returns and recent high  */			
int n = 0;
IndexOfSellSecurity = new int[(int)NumberInvest];
if(BuyNumber>0)
{
  int j = 0;
  for(int i=0;i<NumberInvest;i++)
  {
	  if(SecurityBuy.get(i)!=null)
	  {

		  if(SecurityBuy.get(i).getReturn(CurrentDate,TimeUnit.MONTHLY,-MonthOfNegativeReturn)<0 || (1-PercentageBelowRecentHigh/100)*SecurityPool.get(i).getHighestPrice(LTIDate.getNewDate(CurrentDate, TimeUnit.MONTHLY, -MonthRecentHigh),CurrentDate)>SecurityPool.get(i).getCurrentPrice(CurrentDate))
		  {
		  	  IndexOfSellSecurity[i]=1;
		      n=+1;
		  }
			  j+=1;
	  } 
		  if(j==BuyNumber)
			 break;
  }
}   

if(n>0)
{
				
  for(int i=0;i<=NumberInvest-1;i++)
  {
    if(IndexOfSellSecurity[i]==1)
    {
      double AssetAmount = CurrentPortfolio.getAssetAmount("Asset"+(i+1),CurrentDate);
      CurrentPortfolio.sell("Asset"+(i+1),SecurityBuy.get(i).getName(),AssetAmount,CurrentDate);
      BuyNumber-=1;
      SecurityBuy.set(i,null);
      IndexOfSellSecurity[i]=0;
       printToLog("   left  " + BuyNumber);
    }
  } 
}

/*Rank the past performance scores every "BuyFrequency" months and check for the sell rule of ranks and the buy rules */

if(LTIDate.calculateInterval(StartDate, CurrentDate)>=(30*BuyFrequency))
{
	StartDate = CurrentDate;
	/* Calculate the average of the securities' 1, 3, 6, 9, 12 month returns */ 
	int k = 0;
printToLog( "SecurityPool size = " + SecurityPool.size() +  "SecurityPoolString size  = "  + SecurityPoolString.length);
	for(int i=0; i<=SecurityPool.size()-1; i++)
	{
	  if(LTIDate.before(getEarliestAvaliablePriceDate(SecurityPool.get(i)), LTIDate.getNewNYSEMonth(CurrentDate, -12)))
	    PastPerformanceScore[i] = (SecurityPool.get(i).getReturn(CurrentDate, TimeUnit.MONTHLY, -1)+SecurityPool.get(i).getReturn(CurrentDate, TimeUnit.MONTHLY, -3)+SecurityPool.get(i).getReturn(CurrentDate, TimeUnit.MONTHLY, -6)+SecurityPool.get(i).getReturn(CurrentDate, TimeUnit.MONTHLY, -9)+SecurityPool.get(i).getReturn(CurrentDate, TimeUnit.MONTHLY, -12))*100/5;
	  else
	  {  
	    PastPerformanceScore[i] = -10000;
	    k+=1;
	  }
	}
	/* Sort the securities by PastPerformanceScore */
	AcceptableLowestRank = java.lang.Math.floor((PastPerformanceScore.length-k)*PercentageInRank/100); 
	for(int i=0; i<=AcceptableLowestRank-1; i++)
	{
	 
	 MaxScore = PastPerformanceScore[i];
	 MaxScoreIndex =i;
	  
	   for(int j=i+1; j<=PastPerformanceScore.length-1; j++)
	   {     
	     if(MaxScore <= PastPerformanceScore[j])
	     {
	      MaxScore = PastPerformanceScore[j];
	      MaxScoreIndex = j;
	     }
	   }
	   
	   if(MaxScoreIndex !=i)
	   {
	     String tmp= SecurityPoolString[MaxScoreIndex];
	     SecurityPoolString[MaxScoreIndex]= SecurityPoolString[i];
	     SecurityPoolString[i]=tmp;	 
	   
	     double t=PastPerformanceScore[MaxScoreIndex];
	     PastPerformanceScore[MaxScoreIndex]=PastPerformanceScore[i];
	     PastPerformanceScore[i]=t;
	   }
	}
	SecurityPool = getSecurityList(SecurityPoolString);

	String log_m="After Ordering (the top "+PercentageInRank+"%)" +"\r\n";
	for(int i=0;i<=AcceptableLowestRank-1;i++)
	{
	        log_m+=SecurityPoolString[i] +" ";
	        log_m+=PastPerformanceScore[i];
	        log_m+="\r\n";      
	}
                      


                     /* sell currentasset ranked below AcceptableLowestRank */
	int j = BuyNumber;
	if(BuyNumber>0)
	{
	for(int i=0;i<NumberInvest-1;i++)
	{
	  if(SecurityPool.indexOf(SecurityBuy.get(i))>(AcceptableLowestRank-1))
	  {
	    double AssetAmount = CurrentPortfolio.getAssetAmount("Asset"+(i+1),CurrentDate);
	    CurrentPortfolio.sell("Asset"+(i+1),SecurityBuy.get(i).getName(),AssetAmount,CurrentDate);
	    SecurityBuy.set(i,null);
	    BuyNumber-=1;

	  }
	  if(SecurityBuy.get(i)!=null)
	    j-=1;
	  if(j==0)
		break;
	}
	}

	/* buy securities according to the buy rules */
	double TotalAmount;
	double CashAmount;

	if(BuyNumber<NumberInvest)
	{ 
	  if(AcceptableLowestRank>0.5)
	  {
	  for(int i=0; i<=AcceptableLowestRank-1; i++)
	  { 
	    if(!SecurityBuy.contains(SecurityPool.get(i)))   
	    {
	      if(SecurityPool.get(i).getReturn(LTIDate.getNewNYSEMonth(CurrentDate, -MonthOfNegativeReturn),CurrentDate)>=0)
	      {
	        if((1-PercentageBelowRecentHigh/100)*SecurityPool.get(i).getHighestPrice(LTIDate.getNewDate(CurrentDate, TimeUnit.MONTHLY, -MonthRecentHigh),CurrentDate)<SecurityPool.get(i).getCurrentPrice(CurrentDate)) 
	        {
	          int m = SecurityBuy.indexOf(null);
	          if(m==-1)
	          {
	        	  m=BuyNumber;
	        	  SecurityBuy.add(SecurityPool.get(i));
	          }
	          else
	            SecurityBuy.set(m,SecurityPool.get(i));
	          
	          TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
	          CashAmount = CurrentPortfolio.getCash();
	          if(CashAmount<TotalAmount/NumberInvest)
	            CurrentPortfolio.balance(CurrentDate); 
	          
	          CurrentPortfolio.getAsset("Asset"+(m+1)).setAssetStrategyID(getStrategyID("STATIC"));
	          CurrentPortfolio.getAsset("Asset"+(m+1)).setClassID(SecurityBuy.get(m).getAssetClassID());
	          CurrentPortfolio.buy("Asset"+(m+1), SecurityBuy.get(m).getName(), TotalAmount/NumberInvest, CurrentDate); 
	          BuyNumber+=1;
	        }
	      }
	    }  
	    if(BuyNumber==NumberInvest) break;
	  }
	  }
	}	
}
		}
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Invest_with_Winners301.java:196: ~0&÷
//&÷ ¹Õ get13FPortfolios(java.lang.String,<nulltype>)
//Mn { com.lti.service.CloningCenterManager
//        CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
//                                                             ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Invest_with_Winners301.java:215: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.Invest_with_Winners301
//    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, PortfolioHoldingUtil.SORT_BY_POPULAR, 0, TopN-1);
//                                                                                         ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Invest_with_Winners301.java:215: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.Invest_with_Winners301
//    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, PortfolioHoldingUtil.SORT_BY_POPULAR, 0, TopN-1);
//                  ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Invest_with_Winners301.java:217: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.Invest_with_Winners301
//    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, 2, 0, TopN -1);
//                  ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Invest_with_Winners301.java:315: ~0&÷
//&÷ ¹Õ getAssetClassID()
//Mn { com.lti.service.bo.Security
//    CurrentAsset.setClassID(SecurityBuy.get(i-1).getAssetClassID());
//                                                ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Invest_with_Winners301.java:369: ~0&÷
//&÷ ¹Õ get13FPortfolios(java.lang.String,<nulltype>)
//Mn { com.lti.service.CloningCenterManager
//        CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
//                                                             ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Invest_with_Winners301.java:388: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.Invest_with_Winners301
//    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  CurrentDate, PortfolioHoldingUtil.SORT_BY_POPULAR, 0, TopN-1);
//                                                                                        ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Invest_with_Winners301.java:388: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.Invest_with_Winners301
//    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  CurrentDate, PortfolioHoldingUtil.SORT_BY_POPULAR, 0, TopN-1);
//                  ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Invest_with_Winners301.java:390: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.Invest_with_Winners301
//    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  CurrentDate, 2, 0, TopN -1);
//                  ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Invest_with_Winners301.java:552: ~0&÷
//&÷ ¹Õ getAssetClassID()
//Mn { com.lti.service.bo.Security
//	          CurrentPortfolio.getAsset("Asset"+(m+1)).setClassID(SecurityBuy.get(m).getAssetClassID());
//	                                                                                ^
//10 ï