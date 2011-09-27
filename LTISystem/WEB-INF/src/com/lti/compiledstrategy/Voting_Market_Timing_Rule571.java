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
public class Voting_Market_Timing_Rule571 extends SimulateStrategy{
	public Voting_Market_Timing_Rule571(){
		super();
		StrategyID=571L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int ReviewFrequency;
	public void setReviewFrequency(int ReviewFrequency){
		this.ReviewFrequency=ReviewFrequency;
	}
	
	public int getReviewFrequency(){
		return this.ReviewFrequency;
	}
	private double SwithProportion;
	public void setSwithProportion(double SwithProportion){
		this.SwithProportion=SwithProportion;
	}
	
	public double getSwithProportion(){
		return this.SwithProportion;
	}
	private String BuySecurity;
	public void setBuySecurity(String BuySecurity){
		this.BuySecurity=BuySecurity;
	}
	
	public String getBuySecurity(){
		return this.BuySecurity;
	}
	private String[] PortfolioPoolString;
	public void setPortfolioPoolString(String[] PortfolioPoolString){
		this.PortfolioPoolString=PortfolioPoolString;
	}
	
	public String[] getPortfolioPoolString(){
		return this.PortfolioPoolString;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		ReviewFrequency=(Integer)ParameterUtil.fetchParameter("int","ReviewFrequency", "1", parameters);
		SwithProportion=(Double)ParameterUtil.fetchParameter("double","SwithProportion", "0.5", parameters);
		BuySecurity=(String)ParameterUtil.fetchParameter("String","BuySecurity", "^GSPC", parameters);
		PortfolioPoolString=(String[])ParameterUtil.fetchParameter("String[]","PortfolioPoolString", "P_4666, P_4662, P_4658, P_4680, P_4678, P_4652, P_4648,  P_4670,  P_4660,  P_4682,  P_4686, P_4688, P_4782, P_4910, P_4916, P_4918, P_4894, P_4896, P_4898, P_4900, P_4906, P_4908, P_4912, P_4914, P_4968, P_4954, P_4974, P_4972, P_4952, P_4990, P_4984, P_4980, P_5128, P_5126, P_5124, P_5122, P_5426, P_5422, P_5418, P_4932, P_5256, P_5254, P_5252, P_4930", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	/*String[] PortfolioPoolString = 
{P_4666, P_4662, P_4658, P_4680, P_4678, P_4652, P_4648,  P_4670,  P_4660,  P_4682,  P_4686, P_4688, P_4782, P_4910, P_4916, P_4918, P_4894, P_4896, P_4898, P_4900, P_4906, P_4908, P_4912, P_4914, P_4968, P_4954, P_4974, P_4972, P_4952, P_4990, P_4984, P_4980, P_5128, P_5126, P_5124, P_5122, P_5426, P_5422, P_5418, P_4932, P_5256, P_5254, P_5252, P_4930};*/


double TotalAmount;
Date StartDate;
String LastOperation = new String();
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
	List<Portfolio> PortfolioPool = null;

public List<Portfolio> getPortfolioPool()  throws Exception {
       if(PortfolioPool==null){
              PortfolioPool=getPortfolioList(PortfolioPoolString);
       }
       return PortfolioPool;
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		Security S = getSecurity(BuySecurity);
StartDate = CurrentDate;
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
LastOperation = "SELL";
List<Portfolio> AvailablePortfolio = new ArrayList<Portfolio>();
PortfolioPool=getPortfolioPool() ;


for(int i = 0; i<=PortfolioPoolString.length-1;i++)
{
    if(LTIDate.before(getPortfolioPool().get(i).getStartingDate(), CurrentDate))
        AvailablePortfolio.add(getPortfolioPool().get(i));
}
printToLog(AvailablePortfolio.size());
int j=0;
for(int i=0;i<= AvailablePortfolio.size()-1;i++)
{
    List<Transaction> TempTransactions =  AvailablePortfolio.get(i).getTransactions( AvailablePortfolio.get(i).getStartingDate(),CurrentDate);
    int Size = TempTransactions.size();
    if(Size >=1)
    {
        if(TempTransactions.get(Size-1).getOperation().equals("BUY"))
            j +=1;
    }
}
printToLog("j = "+j+" size = "+ AvailablePortfolio.size() +"buy proportion = " +((double) j/AvailablePortfolio.size()));
if(( (double) j / AvailablePortfolio.size())>= SwithProportion)
{
    TotalAmount = CurrentPortfolio.getCash();
    CurrentPortfolio.buy(curAsset, S.getName(), TotalAmount, CurrentDate);
    LastOperation = "BUY";
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
	
		
		Security S = getSecurity(BuySecurity);

		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.calculateInterval(StartDate, CurrentDate)>= ReviewFrequency*30  && LTIDate.isNYSETradingDay(CurrentDate)) {
		   StartDate = CurrentDate;
List<Portfolio> AvailablePortfolio = new ArrayList<Portfolio>();
PortfolioPool=getPortfolioPool() ;

for(int i = 0; i<=PortfolioPoolString.length-1;i++)
{
    if(LTIDate.before(getPortfolioPool().get(i).getStartingDate(), CurrentDate))
        AvailablePortfolio.add(getPortfolioPool().get(i));
}

int j=0;
for(int i=0; i<= AvailablePortfolio.size()-1;i++)
{
    List<Transaction> TempTransactions =  AvailablePortfolio.get(i).getTransactions( AvailablePortfolio.get(i).getStartingDate(),CurrentDate);
    int Size = TempTransactions.size();
    if(Size >=1)
    {
        if(TempTransactions.get(Size-1).getOperation().equals("BUY"))
            j +=1;
    }
}

printToLog("j = "+j+" size = "+ AvailablePortfolio.size() +"buy proportion = " +((double) j/AvailablePortfolio.size()));

if(LastOperation.equals("SELL") && ((double) j / AvailablePortfolio.size()) >= SwithProportion)
{
    TotalAmount = CurrentPortfolio.getCash();
    CurrentPortfolio.buy(curAsset, S.getName(), TotalAmount, CurrentDate);
    LastOperation = "BUY";
}
else if(LastOperation.equals("BUY") &&  ((double) j / AvailablePortfolio.size()) <= (1-SwithProportion))
{
    TotalAmount = CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);   
    CurrentPortfolio.sell(curAsset, S.getName(), TotalAmount, CurrentDate);
    LastOperation = "SELL";    
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

//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Voting_Market_Timing_Rule571.java:129: ~0&÷
//&÷ ¹Õ getTransactions(java.util.Date,java.util.Date)
//Mn { com.lti.service.bo.Portfolio
//    List<Transaction> TempTransactions =  AvailablePortfolio.get(i).getTransactions( AvailablePortfolio.get(i).getStartingDate(),CurrentDate);
//                                                                   ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Voting_Market_Timing_Rule571.java:184: ~0&÷
//&÷ ¹Õ getTransactions(java.util.Date,java.util.Date)
//Mn { com.lti.service.bo.Portfolio
//    List<Transaction> TempTransactions =  AvailablePortfolio.get(i).getTransactions( AvailablePortfolio.get(i).getStartingDate(),CurrentDate);
//                                                                   ^
//2 ï