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
public class Hedge_Fund_Group_Top_n_Index_599 extends SimulateStrategy{
	public Hedge_Fund_Group_Top_n_Index_599(){
		super();
		StrategyID=599L;
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
	private int TopN;
	public void setTopN(int TopN){
		this.TopN=TopN;
	}
	
	public int getTopN(){
		return this.TopN;
	}
	private String RankType;
	public void setRankType(String RankType){
		this.RankType=RankType;
	}
	
	public String getRankType(){
		return this.RankType;
	}
	private int EvaluatePeriod;
	public void setEvaluatePeriod(int EvaluatePeriod){
		this.EvaluatePeriod=EvaluatePeriod;
	}
	
	public int getEvaluatePeriod(){
		return this.EvaluatePeriod;
	}
	private int Frequency;
	public void setFrequency(int Frequency){
		this.Frequency=Frequency;
	}
	
	public int getFrequency(){
		return this.Frequency;
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
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		CandidateFundString=(String[])ParameterUtil.fetchParameter("String[]","CandidateFundString", "P_5486, P_5785, P_5795, P_5827, P_5841, P_5867, P_6029, P_6073, P_6085, P_6099, P_6101, P_6117", parameters);
		TopN=(Integer)ParameterUtil.fetchParameter("int","TopN", "5", parameters);
		RankType=(String)ParameterUtil.fetchParameter("String","RankType", "performance", parameters);
		EvaluatePeriod=(Integer)ParameterUtil.fetchParameter("int","EvaluatePeriod", "1", parameters);
		Frequency=(Integer)ParameterUtil.fetchParameter("int","Frequency", "1", parameters);
		Categary=(String)ParameterUtil.fetchParameter("String","Categary", "value", parameters);
		UseCategary=(Boolean)ParameterUtil.fetchParameter("boolean","UseCategary", "true", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date StartDate; 
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("StartDate: ");
		sb.append(StartDate);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(StartDate);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		StartDate=(Date)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	/**
	 * only run once after executing 
	 */
	public void afterExecuting(Portfolio portfolio,Date CurrentDate)  throws Exception {
               //FileOperator.appendMethodA("Start","/home/sshadmin/transactions.txt");   
               List<Transaction> trs=portfolio.getTransactions();
               for(int i=0;i<trs.size();i++){
                             FileOperator.appendMethodA("/home/sshadmin/transactions.txt",TransactionUtil.outputTransaction(trs.get(i))+"\r\n");    
               }
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
List <Security> TopFunds = new ArrayList<Security>();

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

if(AvailableFunds.size() < TopN)
   { printToLog("The candidate funds are less than Top N.");}


if(RankType.equals("performance"))
{
TopFunds =getTopSecurity(AvailableFunds, TopN , EvaluatePeriod, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, false); 
/*printToLog("no error when getting Top return");*/
}

if(RankType.equals("sharpe"))
{
TopFunds =getTopSecurity(AvailableFunds, TopN , EvaluatePeriod, CurrentDate, TimeUnit.MONTHLY, SortType.SHARPE, false); 
/*printToLog("no error when getting Top SHARPE");*/
}



if(TopFunds !=null)
{
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
printToLog("sell asset " );
double amount=CurrentPortfolio.getCash();

for(int i=0;i<=TopFunds.size()-1;i++)
    CurrentPortfolio.buy(curAsset, TopFunds.get(i).getName(),amount/TopFunds.size(), CurrentDate);
}

StartDate = CurrentDate;
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
		
		 
		else if (LTIDate.calculateInterval(StartDate, CurrentDate) >=(30*Frequency)) {
		   StartDate = CurrentDate;
List<Portfolio> CandidateFunds = null;
if(UseCategary == true)
    CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
else
    CandidateFunds = getPortfolioList(CandidateFundString);
List<Security> AvailableFunds = new ArrayList<Security>();
List <Security> TopFunds = new ArrayList<Security>();

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

if(AvailableFunds.size() < TopN)
   { printToLog("The candidate funds are less than Top N.");}

if(RankType.equals("performance"))
{
TopFunds =getTopSecurity(AvailableFunds, TopN , EvaluatePeriod, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, false); 
/*printToLog("no error when getting Top return");*/
}

if(RankType.equals("sharpe"))
{
TopFunds =getTopSecurity(AvailableFunds, TopN , EvaluatePeriod, CurrentDate, TimeUnit.MONTHLY, SortType.SHARPE, false); 
/*printToLog("no error when getting Top SHARPE");*/
}


if(TopFunds !=null)
{
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
printToLog("sell asset " );
double amount=CurrentPortfolio.getCash();

for(int i=0;i<=TopFunds.size()-1;i++)
    {
    printToLog("buy " + TopFunds.get(i).getName());
    CurrentPortfolio.buy(curAsset, TopFunds.get(i).getName(),amount/TopFunds.size(), CurrentDate);
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

//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Hedge_Fund_Group_Top_n_Index_599.java:127: ~0&÷
//&÷ ¹Õ getTransactions()
//Mn { com.lti.service.bo.Portfolio
//               List<Transaction> trs=portfolio.getTransactions();
//                                              ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Hedge_Fund_Group_Top_n_Index_599.java:140: ~0&÷
//&÷ ¹Õ get13FPortfolios(java.lang.String,<nulltype>)
//Mn { com.lti.service.CloningCenterManager
//    CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
//                                                         ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Hedge_Fund_Group_Top_n_Index_599.java:215: ~0&÷
//&÷ ¹Õ get13FPortfolios(java.lang.String,<nulltype>)
//Mn { com.lti.service.CloningCenterManager
//    CandidateFunds = com.lti.service.CloningCenterManager.get13FPortfolios( Categary, null);
//                                                         ^
//3 ï