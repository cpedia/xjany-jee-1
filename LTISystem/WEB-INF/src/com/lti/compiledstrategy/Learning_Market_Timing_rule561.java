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
public class Learning_Market_Timing_rule561 extends SimulateStrategy{
	public Learning_Market_Timing_rule561(){
		super();
		StrategyID=561L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int EvaluatePeriod;
	public void setEvaluatePeriod(int EvaluatePeriod){
		this.EvaluatePeriod=EvaluatePeriod;
	}
	
	public int getEvaluatePeriod(){
		return this.EvaluatePeriod;
	}
	private int ReviewFrequency;
	public void setReviewFrequency(int ReviewFrequency){
		this.ReviewFrequency=ReviewFrequency;
	}
	
	public int getReviewFrequency(){
		return this.ReviewFrequency;
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
		EvaluatePeriod=(Integer)ParameterUtil.fetchParameter("int","EvaluatePeriod", "128", parameters);
		ReviewFrequency=(Integer)ParameterUtil.fetchParameter("int","ReviewFrequency", "128", parameters);
		PortfolioPoolString=(String[])ParameterUtil.fetchParameter("String[]","PortfolioPoolString", "P_4666, P_4662, P_4658, P_4680, P_4678, P_4652, P_4648,  P_4670,  P_4660,  P_4682,  P_4686, P_4688, P_4782, P_4910, P_4916, P_4918, P_4894, P_4896, P_4898, P_4900, P_4906, P_4908, P_4912, P_4914, P_4968, P_4954, P_4974, P_4972, P_4952, P_4990, P_4984, P_4980", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	/*String[] PortfolioPoolString = 
{P_4666, P_4662, P_4658, P_4680, P_4678, P_4652, P_4648,  P_4670,  P_4660,  P_4682,  P_4686, P_4688, P_4782, P_4910, P_4916, P_4918, P_4894, P_4896, P_4898, P_4900, P_4906, P_4908, P_4912, P_4914, P_4968, P_4954, P_4974, P_4972, P_4952, P_4990, P_4984, P_4980, P_5128, P_5126, P_5124, P_5122, P_5426, P_5422, P_5418, P_4932, P_5256, P_5254, P_5252, P_4930};*/

double[] PastReturn ;
double TotalAmount;
double MaxReturn;
int MaxReturnIndex;
int OldSize;
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
Portfolio ChosenPortfolio = null;
public List<Portfolio> getPortfolioPool() throws Exception {
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
		PastReturn = new double[PortfolioPoolString.length];
PortfolioPool= getPortfolioPool();
/*List<Portfolio>  PortfolioPool=getPortfolioList(PortfolioPoolString);
Portfolio ChosenPortfolio = null;*/

StartDate = CurrentDate;
Date EvaluateStartDate = LTIDate.getNewNYSETradingDay(CurrentDate,-EvaluatePeriod+1);
for(int i=0; i<=PortfolioPoolString.length-1; i++)
{   
    if(LTIDate.before(PortfolioPool.get(i).getStartingDate(), EvaluateStartDate))		
        PastReturn[i] = PortfolioPool.get(i).getAnnualizedReturn(EvaluateStartDate, CurrentDate);
    else
        PastReturn[i] = -10000;
}
MaxReturnIndex = 0;
MaxReturn = PastReturn[0];
for(int i=1; i<=PastReturn.length-1; i++)
{
    if(PastReturn[0]<PastReturn[i])
    {
        double TempReturn = PastReturn[0];
        PastReturn[0]=PastReturn[i];
        PastReturn[i]= TempReturn;
        MaxReturnIndex = i;
    }
}
ChosenPortfolio = PortfolioPool.get(MaxReturnIndex);
printToLog("The portfolio going to be followed: "+ ChosenPortfolio.getName());

CurrentPortfolio.sellAsset(curAsset, CurrentDate);
LastOperation = "SELL";
List<Transaction> TempTransactions = ChosenPortfolio.getTransactions(ChosenPortfolio.getStartingDate(),CurrentDate);
int Size = TempTransactions.size();
Transaction PlanTransaction = TempTransactions.get(Size-1);

if(PlanTransaction.getOperation().equalsIgnoreCase("BUY"))
{
    TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
    CurrentPortfolio.buy (curAsset, PlanTransaction.getSecurityName(), TotalAmount, CurrentDate);
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
	
		
		
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.isNYSETradingDay(CurrentDate)) {
		   Portfolio TempPortfolio = new Portfolio();
PortfolioPool=getPortfolioPool();
/*List<Portfolio>  PortfolioPool=getPortfolioList(PortfolioPoolString);
Portfolio ChosenPortfolio = null;*/

if(LTIDate.calculateInterval(StartDate, CurrentDate)>=ReviewFrequency)
{
    StartDate = CurrentDate;
    Date EvaluateStartDate = LTIDate.getNewNYSETradingDay(CurrentDate,-EvaluatePeriod+1);
    for(int i=0; i<=PortfolioPoolString.length-1; i++)
    {   
        if(LTIDate.before(PortfolioPool.get(i).getStartingDate(), EvaluateStartDate))		
            PastReturn[i] =PortfolioPool.get(i).getAnnualizedReturn(EvaluateStartDate, CurrentDate);
        else
            PastReturn[i] = -10000;
    }
    MaxReturnIndex = 0;
    MaxReturn = PastReturn[0];
    for(int i=1; i<=PastReturn.length-1; i++)
    {
        if(PastReturn[0]<PastReturn[i])
        {
            double TempReturn = PastReturn[0];
            PastReturn[0]=PastReturn[i];
            PastReturn[i]= TempReturn;
            MaxReturnIndex = i;
        }
    }
    TempPortfolio = ChosenPortfolio;
    ChosenPortfolio = PortfolioPool.get(MaxReturnIndex);
    printToLog("The portfolio going to be followed: "+ ChosenPortfolio.getName());        
}
else
    TempPortfolio = ChosenPortfolio;

List<Transaction> TempTransactions = ChosenPortfolio.getTransactions(ChosenPortfolio.getStartingDate(),CurrentDate);
int Size = TempTransactions.size();
if(TempPortfolio == ChosenPortfolio && Size == OldSize)
{	
}
else
{
OldSize = Size;
Transaction PlanTransaction = TempTransactions.get(Size-1);
boolean SecuritySame = true;
if(TempPortfolio != ChosenPortfolio)
{
if(TempPortfolio.getTransactions(TempPortfolio.getStartingDate(),CurrentDate).get(0).getSecurityName().equals(PlanTransaction.getSecurityName()))
    SecuritySame = true;
else
    SecuritySame = false;
}

if(TempPortfolio != ChosenPortfolio && !SecuritySame)
{
    if(LastOperation.equals("BUY"))
    {
        TotalAmount = CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);         
        CurrentPortfolio.sell(curAsset, TempPortfolio.getTransactions(TempPortfolio.getStartingDate(),CurrentDate).get(1).getSecurityName(),TotalAmount,CurrentDate);
        LastOperation = "SELL";
    }
    if(PlanTransaction.getOperation().equals("BUY"))
    {
        TotalAmount = CurrentPortfolio.getCash();
        CurrentPortfolio.buy (curAsset, PlanTransaction.getSecurityName(), TotalAmount, CurrentDate);
        LastOperation = "BUY";
    }
}
else
{
    if(LastOperation.equals("BUY") && PlanTransaction.getOperation().equals("SELL"))
    {
        TotalAmount = CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);         
        CurrentPortfolio.sell(curAsset, PlanTransaction.getSecurityName(),TotalAmount,CurrentDate);
        LastOperation = "SELL";       
    }
    if((LastOperation.equals("SELL")||LastOperation.equals("SELL_ASSET")) && PlanTransaction.getOperation().equals("BUY"))  
    {
         TotalAmount = CurrentPortfolio.getCash();
         CurrentPortfolio.buy (curAsset, PlanTransaction.getSecurityName(), TotalAmount, CurrentDate);
         LastOperation = "BUY";
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

//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Learning_Market_Timing_rule561.java:116: ~0&÷
//&÷ ¹Õ getAnnualizedReturn(java.util.Date,java.util.Date)
//Mn { com.lti.service.bo.Portfolio
//        PastReturn[i] = PortfolioPool.get(i).getAnnualizedReturn(EvaluateStartDate, CurrentDate);
//                                            ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Learning_Market_Timing_rule561.java:137: ~0&÷
//&÷ ¹Õ getTransactions(java.util.Date,java.util.Date)
//Mn { com.lti.service.bo.Portfolio
//List<Transaction> TempTransactions = ChosenPortfolio.getTransactions(ChosenPortfolio.getStartingDate(),CurrentDate);
//                                                    ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Learning_Market_Timing_rule561.java:185: ~0&÷
//&÷ ¹Õ getAnnualizedReturn(java.util.Date,java.util.Date)
//Mn { com.lti.service.bo.Portfolio
//            PastReturn[i] =PortfolioPool.get(i).getAnnualizedReturn(EvaluateStartDate, CurrentDate);
//                                               ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Learning_Market_Timing_rule561.java:208: ~0&÷
//&÷ ¹Õ getTransactions(java.util.Date,java.util.Date)
//Mn { com.lti.service.bo.Portfolio
//List<Transaction> TempTransactions = ChosenPortfolio.getTransactions(ChosenPortfolio.getStartingDate(),CurrentDate);
//                                                    ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Learning_Market_Timing_rule561.java:220: ~0&÷
//&÷ ¹Õ getTransactions(java.util.Date,java.util.Date)
//Mn { com.lti.service.bo.Portfolio
//if(TempPortfolio.getTransactions(TempPortfolio.getStartingDate(),CurrentDate).get(0).getSecurityName().equals(PlanTransaction.getSecurityName()))
//                ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\Learning_Market_Timing_rule561.java:231: ~0&÷
//&÷ ¹Õ getTransactions(java.util.Date,java.util.Date)
//Mn { com.lti.service.bo.Portfolio
//        CurrentPortfolio.sell(curAsset, TempPortfolio.getTransactions(TempPortfolio.getStartingDate(),CurrentDate).get(1).getSecurityName(),TotalAmount,CurrentDate);
//                                                     ^
//6 ï