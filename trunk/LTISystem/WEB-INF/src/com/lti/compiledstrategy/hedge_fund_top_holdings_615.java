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
public class hedge_fund_top_holdings_615 extends SimulateStrategy{
	public hedge_fund_top_holdings_615(){
		super();
		StrategyID=615L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String[] CandidateFundString;
	public void setCandidateFundString(String[] CandidateFundString){
		this.CandidateFundString=CandidateFundString;
	}
	
	public String[] getCandidateFundString(){
		return this.CandidateFundString;
	}
	private int TopStart;
	public void setTopStart(int TopStart){
		this.TopStart=TopStart;
	}
	
	public int getTopStart(){
		return this.TopStart;
	}
	private String TopType;
	public void setTopType(String TopType){
		this.TopType=TopType;
	}
	
	public String getTopType(){
		return this.TopType;
	}
	private String Categary;
	public void setCategary(String Categary){
		this.Categary=Categary;
	}
	
	public String getCategary(){
		return this.Categary;
	}
	private boolean UseCategary;
	public void setUseCategary(boolean UseCategary){
		this.UseCategary=UseCategary;
	}
	
	public boolean getUseCategary(){
		return this.UseCategary;
	}
	private int TopEnd;
	public void setTopEnd(int TopEnd){
		this.TopEnd=TopEnd;
	}
	
	public int getTopEnd(){
		return this.TopEnd;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		CandidateFundString=(String[])ParameterUtil.fetchParameter("String[]","CandidateFundString", "P_5486, P_5785, P_5795, P_5827, P_5841, P_5867, P_6029, P_6073, P_6085, P_6099, P_6101, P_6117", parameters);
		TopStart=(Integer)ParameterUtil.fetchParameter("int","TopStart", "1", parameters);
		TopType=(String)ParameterUtil.fetchParameter("String","TopType", "popular", parameters);
		Categary=(String)ParameterUtil.fetchParameter("String","Categary", "value", parameters);
		UseCategary=(Boolean)ParameterUtil.fetchParameter("boolean","UseCategary", "true", parameters);
		TopEnd=(Integer)ParameterUtil.fetchParameter("int","TopEnd", "5", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
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
		List<Portfolio> CandidateFunds = null;
if(UseCategary == true)
    CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
else
    CandidateFunds = getPortfolioList(CandidateFundString);
List<Security> AvailableFunds = new ArrayList<Security>();
List <HoldingItem> TopHoldings = null;

Date LastMonthEnd = LTIDate.getMonthEnd(LTIDate.getLastMonthDate(CurrentDate));
LastMonthEnd = LTIDate.getRecentNYSETradingDay(LastMonthEnd);

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
TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, PortfolioHoldingUtil.SORT_BY_POPULAR, TopStart - 1, TopEnd -1);

if(TopType.equals("total percentage"))
TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, 2, TopStart - 1, TopEnd -1);


CurrentPortfolio.sellAsset(curAsset,CurrentDate);
double amount=CurrentPortfolio.getCash();

for(int i=0;i<=TopHoldings.size()-1;i++)
{
    try{ 
    CurrentPortfolio.buy(curAsset, TopHoldings.get(i).getSymbol() ,amount/TopHoldings.size(), CurrentDate);

    if(TopType.equals("popular"))
    printToLog( TopHoldings.get(i).getSymbol() + "'s popular  =  " + TopHoldings.get(i).getPopular());
    if(TopType.equals("total percentage"))
    printToLog( TopHoldings.get(i).getSymbol() + "'s total percentage  =  " + TopHoldings.get(i).getPercentage());    
    }catch(Exception e){
    printToLog("miss  " + TopHoldings.get(i).getSymbol() );
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
	
		
		Calendar calendar=Calendar.getInstance();
    calendar.setTime(CurrentDate);
    int month=calendar.get(Calendar.MONTH)+1;

Date MonthEnd = LTIDate.getMonthEnd(CurrentDate);
MonthEnd = LTIDate.getRecentNYSETradingDay(MonthEnd);
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if ((month == 2 || month == 5 || month == 8|| month == 11) && LTIDate.equals(CurrentDate , MonthEnd) ) {
		   List<Portfolio> CandidateFunds = null;
if(UseCategary == true)
    CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
else
    CandidateFunds = getPortfolioList(CandidateFundString);
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

/*need an API that can get the top holdings after inputting the fund list */

Long[] AvailableFundsLong = getIDs(AvailableFunds);

if(TopType.equals("popular"))
TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  CurrentDate, PortfolioHoldingUtil.SORT_BY_POPULAR, TopStart - 1, TopEnd -1);

if(TopType.equals("total percentage"))
TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  CurrentDate, 2, TopStart - 1, TopEnd -1);

CurrentPortfolio.sellAsset(curAsset,CurrentDate);
double amount=CurrentPortfolio.getCash();

for(int i=0;i<=TopHoldings.size()-1;i++)
{
    try{ 
    CurrentPortfolio.buy(curAsset, TopHoldings.get(i).getSymbol() ,amount/TopHoldings.size(), CurrentDate);

    if(TopType.equals("popular"))
    printToLog( TopHoldings.get(i).getSymbol() + "'s popular  =  " + TopHoldings.get(i).getPopular());
    if(TopType.equals("total percentage"))
    printToLog( TopHoldings.get(i).getSymbol() + "'s total percentage  =  " + TopHoldings.get(i).getPercentage());    
    }catch(Exception e){
    printToLog("miss  " + TopHoldings.get(i).getSymbol() );
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

//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\hedge_fund_top_holdings_615.java:123: ~0&÷
//&÷ ¹Õ get13FPortfolios(java.lang.String,<nulltype>)
//Mn { com.lti.service.CloningCenterManager
//    CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
//                                                         ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\hedge_fund_top_holdings_615.java:146: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.hedge_fund_top_holdings_615
//TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, PortfolioHoldingUtil.SORT_BY_POPULAR, TopStart - 1, TopEnd -1);
//                                                                                     ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\hedge_fund_top_holdings_615.java:146: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.hedge_fund_top_holdings_615
//TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, PortfolioHoldingUtil.SORT_BY_POPULAR, TopStart - 1, TopEnd -1);
//              ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\hedge_fund_top_holdings_615.java:149: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.hedge_fund_top_holdings_615
//TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  LastMonthEnd, 2, TopStart - 1, TopEnd -1);
//              ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\hedge_fund_top_holdings_615.java:161: ~0&÷
//&÷ ¹Õ getPopular()
//Mn { com.lti.service.bo.HoldingItem
//    printToLog( TopHoldings.get(i).getSymbol() + "'s popular  =  " + TopHoldings.get(i).getPopular());
//                                                                                       ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\hedge_fund_top_holdings_615.java:203: ~0&÷
//&÷ ¹Õ get13FPortfolios(java.lang.String,<nulltype>)
//Mn { com.lti.service.CloningCenterManager
//    CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
//                                                         ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\hedge_fund_top_holdings_615.java:225: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.hedge_fund_top_holdings_615
//TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  CurrentDate, PortfolioHoldingUtil.SORT_BY_POPULAR, TopStart - 1, TopEnd -1);
//                                                                                    ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\hedge_fund_top_holdings_615.java:225: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.hedge_fund_top_holdings_615
//TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  CurrentDate, PortfolioHoldingUtil.SORT_BY_POPULAR, TopStart - 1, TopEnd -1);
//              ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\hedge_fund_top_holdings_615.java:228: ~0&÷
//&÷ ØÏ PortfolioHoldingUtil
//Mn { com.lti.compiledstrategy.hedge_fund_top_holdings_615
//TopHoldings = PortfolioHoldingUtil.getTopHoldings(AvailableFundsLong,  CurrentDate, 2, TopStart - 1, TopEnd -1);
//              ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\hedge_fund_top_holdings_615.java:239: ~0&÷
//&÷ ¹Õ getPopular()
//Mn { com.lti.service.bo.HoldingItem
//    printToLog( TopHoldings.get(i).getSymbol() + "'s popular  =  " + TopHoldings.get(i).getPopular());
//                                                                                       ^
//10 ï