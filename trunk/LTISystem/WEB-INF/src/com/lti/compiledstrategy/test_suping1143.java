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
public class test_suping1143 extends SimulateStrategy{
	public test_suping1143(){
		super();
		StrategyID=1143L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String Security;
	public void setSecurity(String Security){
		this.Security=Security;
	}
	
	public String getSecurity(){
		return this.Security;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		Security=(String)ParameterUtil.fetchParameter("String","Security", "IYR", parameters);
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
	/*
public void afterExecuting(Portfolio portfolio,Date CurrentDate)  throws Exception {
    for(Transaction t:portfolio.getTransactions()){
                                                         printToLog(t.getDate()+":"+t.getOperation()+t.getTransactionType()+":"+t.getAmount()+"=="+t.getSecurityID());
                                           }
                                         
List<Transaction> trs=portfolio.getTransactions();
    portfolio.setTransactions(new ArrayList<Transaction>());
    portfolio.saveScheduleTransaction(portfolio.getEndDate());
    for(Transaction t:portfolio.getTransactions()){
                                                         printToLog("========="+t.getDate()+":"+t.getOperation()+t.getTransactionType()+":"+t.getAmount()+"=="+t.getSecurityID());
                                           }
    
printToLog("size:"+portfolio.getTransactions().size());
portfolio.getTransactions().addAll(trs);
printToLog("size:"+portfolio.getTransactions().size());
List<Transaction> tr2=TransactionUtil.MergeTransactions(portfolio.getTransactions());
for(Transaction t:tr2){
                                                         printToLog("===m======"+t.getDate()+":"+t.getOperation()+t.getTransactionType()+":"+t.getAmount()+"=="+t.getSecurityID());
                                           }

portfolio.setTransactions(trs);
}
    */

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		Asset CurrentAsset;

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("CASH");
CurrentAsset.setClassID(getAssetClassID("CASH"));
CurrentAsset.setTargetPercentage(0.0);
CurrentPortfolio.addAsset(CurrentAsset);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset");
CurrentAsset.setClassID(getAssetClassID("CASH"));
CurrentAsset.setTargetPercentage(0.0);
CurrentPortfolio.addAsset(CurrentAsset);
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
	
		
		double cash = CurrentPortfolio.getCash();
printToLog("cash = " + cash);
double buyamount = (double)501;

double CashAmount = CurrentPortfolio.getSecurityAmount("CASH", "CASH", CurrentDate);
printToLog("CASH = " + CashAmount);

if(CashAmount > 0)
    CurrentPortfolio.sellAtNextOpen("CASH", "CASH", CashAmount, CurrentDate);

if(CashAmount+cash > buyamount)
{
CurrentPortfolio.buyAtNextOpen("Asset", Security, buyamount, CurrentDate,true);
printToLog("Buy Amount = " + buyamount);
}


		
		
		
		if(new Boolean(true).equals(false)){
			
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