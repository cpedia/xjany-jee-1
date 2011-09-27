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
public class Momentum_Scoring_on_the_hedge_fund_holdings650 extends SimulateStrategy{
	public Momentum_Scoring_on_the_hedge_fund_holdings650(){
		super();
		StrategyID=650L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String AdjustFrequency;
	public void setAdjustFrequency(String AdjustFrequency){
		this.AdjustFrequency=AdjustFrequency;
	}
	
	public String getAdjustFrequency(){
		return this.AdjustFrequency;
	}
	private String[] Securities;
	public void setSecurities(String[] Securities){
		this.Securities=Securities;
	}
	
	public String[] getSecurities(){
		return this.Securities;
	}
	private int MinInvested;
	public void setMinInvested(int MinInvested){
		this.MinInvested=MinInvested;
	}
	
	public int getMinInvested(){
		return this.MinInvested;
	}
	private int MaxInvested;
	public void setMaxInvested(int MaxInvested){
		this.MaxInvested=MaxInvested;
	}
	
	public int getMaxInvested(){
		return this.MaxInvested;
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
		AdjustFrequency=(String)ParameterUtil.fetchParameter("String","AdjustFrequency", "month", parameters);
		Securities=(String[])ParameterUtil.fetchParameter("String[]","Securities", "SNP, CHA ,YZC, HNP, ACH, LFC, CEO, BIDU, PTR, CHL, CHU, NTES, SNDA, GSH ,SHI , MR, STP, EDU, CTRP, SOHU, GA, ZNH, SINA, LFT, FMCN, SMI, YGE, CEA, LDK, ASIA, EJ, JASO, VISN, CMED", parameters);
		MinInvested=(Integer)ParameterUtil.fetchParameter("int","MinInvested", "0", parameters);
		MaxInvested=(Integer)ParameterUtil.fetchParameter("int","MaxInvested", "100", parameters);
		InvestHolding=(Boolean)ParameterUtil.fetchParameter("boolean","InvestHolding", "false", parameters);
		UseCategary=(Boolean)ParameterUtil.fetchParameter("boolean","UseCategary", "true", parameters);
		Categary=(String)ParameterUtil.fetchParameter("String","Categary", "value", parameters);
		TopType=(String)ParameterUtil.fetchParameter("String","TopType", "popular", parameters);
		TopN=(Integer)ParameterUtil.fetchParameter("int","TopN", "50", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date startDate;
Date date;
/* Dow 30 stocks based on 3/20/2009 */
String[] class1;
List<Security> SecurityPool;
String[] SecurityPoolString;
/* = {"AA", "AXP", "BA", "BAC", "C", "CAT", "CVX", "DD", "DIS", "GE", "GM", "HD", "HPQ", "IBM", "INTC", "JNJ", "JPM", "KFT", "KO", "MCD", "MMM", "MRK", "MSFT", "PFE", "PG", "T", "UTX", "VZ", "WMT", "XOM"};*/

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
                List<Transaction> trs=portfolio.getTransactions();
                com.lti.util.PersistentUtil.writeObject(com.lti.util.TransactionUtil.compareTransactions(trs,com.lti.util.TransactionUtil.MergeTransactions(trs)),portfolio.getID()+".transactions");
}

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
		startDate=CurrentDate;
date=CurrentDate;
printToLog("Securities Length: " + Securities.length);


if(InvestHolding)
{
    Date LastMonthEnd = LTIDate.getMonthEnd(LTIDate.getLastMonthDate(CurrentDate));
    LastMonthEnd = LTIDate.getRecentNYSETradingDay(LastMonthEnd);
    List<Portfolio> CandidateFunds = null;
    if(UseCategary == true)
        CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
    else
        CandidateFunds = getPortfolioList(Securities);
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

    SecurityPoolString = new String[TopHoldings.size()];
    
    for(int i = 0; i<=TopHoldings.size()-1; i++)
    {
         printToLog("holding  " +TopHoldings.get(i).getSymbol());
        SecurityPoolString[i] = TopHoldings.get(i).getSymbol();
    }
}
else 
{
    SecurityPoolString = Securities;
}

String[] class1 = new String[SecurityPoolString.length];
for (int i=0; i<SecurityPoolString.length; i++) {
class1[i] = SecurityPoolString[i];
printToLog("security "+ i+" "+class1[i]);
}


int c=class1.length;
double[] score=new double[c];
for(int i=0; i<score.length; i++)
{
score[i]=0;
}


double a;
double bp1=0;
double bp2=0;
double bp3=0;
double bp4=0;

String[] newArray1=getTopSecurityArray(class1,-12, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray2=getTopSecurityArray(class1, -6, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray3=getTopSecurityArray(class1, -3, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray4=getTopSecurityArray(class1, -1, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);


for(int i=0; i<=class1.length-1; i++)
{
printToLog("doing " +i);
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(class1[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
{
a =(getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -3)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -1) )*100/4;


for(int j=0; j<=newArray1.length-1; j++)
{
if(class1[i].equals(newArray1[j]))
    { 
     if(j<=c/2)
        bp1=1;
     else
        bp1=0;
    }
}


for(int j=0; j<=newArray2.length-1; j++)
{
    if(class1[i].equals(newArray2[j]))
   { 
     if(j<=c/2)
        bp2=1;
     else
        bp2=0;
   }
}


for(int j=0; j<=newArray3.length-1; j++)
{
    if(class1[i].equals(newArray3[j]))
    { 
     if(j<=c/2)
        bp3=1;
     else
        bp3=0;
    }
}


for(int j=0; j<=newArray4.length-1;j++)
{
 if(class1[i].equals(newArray4[j]))
     { 
      if(j<=c/2)
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



double Maxscore;
int Maxscoreindex;

for(int i=score.length-1; i>=1; i--)
{
 
 Maxscore = score[i];
 Maxscoreindex =i;
  
   for(int j=i-1; j>=0; j--)
   { 
     double b;
     b= score[j];
     
     if(Maxscore <=b)
     {
      Maxscore =b;
      Maxscoreindex =j;
     }
   }
   
   if(Maxscoreindex !=i)
   {
     String tmp= class1[Maxscoreindex];
     class1 [Maxscoreindex]= class1[i];
     class1 [i]=tmp;
   
     double t=score[Maxscoreindex];
     score [Maxscoreindex]= score[i];
     score[i]=t;

   }
}


String log_m="After Ordering " + date.toString()+"\r\n";
for(int i=0;i<class1.length;i++){
        log_m+=class1[i] +" ";
        log_m+= score[i];
        log_m+="\r\n";
}
 printToLog(log_m);



int s=class1.length-1;
//int iTotalHoldings = (s+1)/10>3?(s+1)/10 : 3;
int iTotalHoldings = (s+1)/10>MinInvested ? (s+1)/10:MinInvested;
iTotalHoldings = iTotalHoldings>MaxInvested ? MaxInvested : iTotalHoldings;

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

for (int i=0; i<iTotalHoldings; i++) {
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset"+i);
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-i]).getName()));
CurrentAsset.setTargetPercentage(1.0/iTotalHoldings);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset"+i, getSecurity(class1[s-i]).getName(), TotalAmount * (1.0/iTotalHoldings), CurrentDate);
}
/* CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-1]).getName()));
CurrentAsset.setTargetPercentage(0.3);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(class1[s-1]).getName(), TotalAmount * 0.33, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset3");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-2]).getName()));
CurrentAsset.setTargetPercentage(0.3);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset3", getSecurity(class1[s-2]).getName(), TotalAmount * 0.33, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset4");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-3]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset4", getSecurity(class1[s-3]).getName(), TotalAmount * 0.2, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset5");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-4]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset5", getSecurity(class1[s-4]).getName(), TotalAmount * 0.2, CurrentDate);
*/
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
		
		 
		else if ((AdjustFrequency.equals("year") && LTIDate.isLastNYSETradingDayOfYear(CurrentDate))
||(AdjustFrequency.equals("quarter") && LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate)) 
||(AdjustFrequency.equals("month") && LTIDate.isLastNYSETradingDayOfMonth(CurrentDate))) {
		   startDate=CurrentDate;

if(InvestHolding)
{
    Date LastMonthEnd = LTIDate.getMonthEnd(LTIDate.getLastMonthDate(CurrentDate));
    LastMonthEnd = LTIDate.getRecentNYSETradingDay(LastMonthEnd);
    List<Portfolio> CandidateFunds = null;
    if(UseCategary == true)
        CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
    else
        CandidateFunds = getPortfolioList(Securities);
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

    SecurityPoolString = new String[TopHoldings.size()];
    
    for(int i = 0; i<=TopHoldings.size()-1; i++)
    {
         printToLog("holding  " +TopHoldings.get(i).getSymbol());
        SecurityPoolString[i] = TopHoldings.get(i).getSymbol();
    }
}
else 
{
    SecurityPoolString = Securities;
}

String[] class1 = new String[SecurityPoolString.length];
for (int i=0; i<SecurityPoolString.length; i++) {
class1[i] = SecurityPoolString[i];
printToLog("security "+ i+" "+class1[i]);
}


int c=class1.length;
double[] score=new double[c];
for(int i=0; i<score.length; i++)
{
score[i]=0;
}

double a;
double bp1=0;
double bp2=0;
double bp3=0;
double bp4=0;

String[] newArray1=getTopSecurityArray(class1,-12,CurrentDate, TimeUnit.MONTHLY, SortType.RETURN ,true);
String[] newArray2=getTopSecurityArray(class1, -6, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray3=getTopSecurityArray(class1, -3, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray4=getTopSecurityArray(class1, -1, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);



for(int i=0; i<=class1.length-1; i++)
{
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(class1[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
{
a =(getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -3)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -1) )*100/4;


for(int j=0; j<=newArray1.length-1; j++)
{
if(class1[i].equals(newArray1[j]))
    { 
     if(j<=c/2)
        bp1=1;
     else
        bp1=0;
    }
}


for(int j=0; j<=newArray2.length-1; j++)
{
    if(class1[i].equals(newArray2[j]))
   { 
     if(j<=c/2)
        bp2=1;
     else
        bp2=0;
   }
}


for(int j=0; j<=newArray3.length-1; j++)
{
    if(class1[i].equals(newArray3[j]))
    { 
     if(j<=c/2)
        bp3=1;
     else
        bp3=0;
    }
}


for(int j=0; j<=newArray4.length-1;j++)
{
 if(class1[i].equals(newArray4[j]))
     { 
      if(j<=c/2)
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



double Maxscore;
int Maxscoreindex;

for(int i=score.length-1; i>=1; i--)
{
 
 Maxscore = score[i];
 Maxscoreindex =i;
  
   for(int j=i-1; j>=0; j--)
   { 
     double b;
     b= score[j];
     
     if(Maxscore <=b)
     {
      Maxscore =b;
      Maxscoreindex =j;
     }
   }
   
   if(Maxscoreindex !=i)
   {
     String tmp= class1[Maxscoreindex];
     class1 [Maxscoreindex]= class1[i];
     class1 [i]=tmp;

     double t=score[Maxscoreindex];
     score [Maxscoreindex]= score[i];
     score[i]=t;

   }
}


String log_m="After Ordering " + date.toString()+"\r\n";
for(int i=0;i<class1.length;i++){
        log_m+=class1[i] +" ";
        log_m+= score[i];
        log_m+="\r\n";
}
 printToLog(log_m);

int s=class1.length-1;
//int iTotalHoldings = (s+1)/10>3?(s+1)/10 : 3;
int iTotalHoldings = (s+1)/10>MinInvested ? (s+1)/10:MinInvested;
iTotalHoldings = iTotalHoldings>MaxInvested ? MaxInvested : iTotalHoldings;


double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

for (int i=0; i<iTotalHoldings; i++) {
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset"+i);
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-i]).getName()));
CurrentAsset.setTargetPercentage(1.0/iTotalHoldings);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset"+i, getSecurity(class1[s-i]).getName(), TotalAmount * (1.0/iTotalHoldings), CurrentDate);
}

/*
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-1]).getName()));
CurrentAsset.setTargetPercentage(0.33);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(class1[s-1]).getName(), TotalAmount * 0.33, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset3");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-2]).getName()));
CurrentAsset.setTargetPercentage(0.33);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset3", getSecurity(class1[s-2]).getName(), TotalAmount * 0.33, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset4");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-3]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset4", getSecurity(class1[s-3]).getName(), TotalAmount * 0.2, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset5");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-4]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset5", getSecurity(class1[s-4]).getName(), TotalAmount * 0.2, CurrentDate);
*/
		}
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Momentum_Scoring_on_the_hedge_fund_holdings650.java:147: ~0&÷
//&÷ ¹Õ getTransactions()
//Mn { com.lti.service.bo.Portfolio
//                List<Transaction> trs=portfolio.getTransactions();
//                                               ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Momentum_Scoring_on_the_hedge_fund_holdings650.java:175: ~0&÷
//&÷ ¹Õ get13FPortfolios(java.lang.String,<nulltype>)
//Mn { com.lti.service.CloningCenterManager
//        CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
//                                                             ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Momentum_Scoring_on_the_hedge_fund_holdings650.java:194: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.Momentum_Scoring_on_the_hedge_fund_holdings650
//    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, PortfolioHoldingUtil.SORT_BY_POPULAR, 0, TopN-1);
//                                                                                         ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Momentum_Scoring_on_the_hedge_fund_holdings650.java:194: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.Momentum_Scoring_on_the_hedge_fund_holdings650
//    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, PortfolioHoldingUtil.SORT_BY_POPULAR, 0, TopN-1);
//                  ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Momentum_Scoring_on_the_hedge_fund_holdings650.java:196: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.Momentum_Scoring_on_the_hedge_fund_holdings650
//    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, 2, 0, TopN -1);
//                  ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Momentum_Scoring_on_the_hedge_fund_holdings650.java:436: ~0&÷
//&÷ ¹Õ get13FPortfolios(java.lang.String,<nulltype>)
//Mn { com.lti.service.CloningCenterManager
//        CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
//                                                             ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Momentum_Scoring_on_the_hedge_fund_holdings650.java:455: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.Momentum_Scoring_on_the_hedge_fund_holdings650
//    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, PortfolioHoldingUtil.SORT_BY_POPULAR, 0, TopN-1);
//                                                                                         ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Momentum_Scoring_on_the_hedge_fund_holdings650.java:455: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.Momentum_Scoring_on_the_hedge_fund_holdings650
//    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, PortfolioHoldingUtil.SORT_BY_POPULAR, 0, TopN-1);
//                  ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Momentum_Scoring_on_the_hedge_fund_holdings650.java:457: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.Momentum_Scoring_on_the_hedge_fund_holdings650
//    TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, 2, 0, TopN -1);
//                  ^
//9 ï