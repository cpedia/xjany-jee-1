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
public class Average_Down_Portfolio_Management376 extends SimulateStrategy{
	public Average_Down_Portfolio_Management376(){
		super();
		StrategyID=376L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int TN;
	public void setTN(int TN){
		this.TN=TN;
	}
	
	public int getTN(){
		return this.TN;
	}
	private String Index;
	public void setIndex(String Index){
		this.Index=Index;
	}
	
	public String getIndex(){
		return this.Index;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		TN=(Integer)ParameterUtil.fetchParameter("int","TN", "5", parameters);
		Index=(String)ParameterUtil.fetchParameter("String","Index", "^GSPC", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	int LN; /* number of units left */
double PreviousPrice, PreviousBuyPrice, PreviousSellPrice, CurrentPrice, Percent, CashAmount;
Security Sec;
String RiskyName, CashName;
Asset RiskyAsset, CashAsset;
Date startDate;
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
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		LN =TN;
Sec = getSecurity(Index);
PreviousPrice = Sec.getAdjClose(CurrentDate);
startDate=CurrentDate;
Percent = 0.5/TN;
CashAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
RiskyName="Risky";
CashName="Cash";

Asset CurrentAsset;

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(RiskyName);
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
RiskyAsset=CurrentAsset;

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(CashName);
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CashName, "CASH", CashAmount, CurrentDate);
CashAsset=CurrentAsset;
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
	
		
		CurrentPrice=Sec. getAdjClose(CurrentDate);
double lowPrice=Sec.getLowestAdjustPrice(startDate,CurrentDate);
double highPrice=Sec.getHighestAdjustPrice(startDate, CurrentDate);

		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if ((LN!=0) && (CurrentPrice<highPrice*(1-Percent))) {
		   /* buy 1/LN of CashAmount RiskyAsset */
double amount=CurrentPortfolio.getAssetAmount(CashAsset, CurrentDate);
CurrentPortfolio.sell(CashName, "CASH", amount*(1.0/LN), CurrentDate);
CurrentPortfolio.buy(RiskyAsset, Index, amount*(1.0/LN), CurrentDate);
LN--;
PreviousPrice=CurrentPrice;
startDate=CurrentDate;
printToLog("Buy Risky: LN="+LN+" PreviousPrice= "+PreviousPrice);
		}
		else if ((LN!=TN)&&(CurrentPrice>PreviousPrice*(1+3*Percent))) {
		   /* sell 1/(TN-LN) RiskyAsset */
double amount=CurrentPortfolio.getAssetAmount(RiskyAsset,CurrentDate);
CurrentPortfolio.sell(RiskyName, Index, amount*(1.0/(TN-LN)), CurrentDate);
CurrentPortfolio.buy(CashAsset, "CASH", amount*(1.0/(TN-LN)), CurrentDate);
LN++;
PreviousPrice=CurrentPrice;
startDate=CurrentDate;
printToLog("Sell Risky: LN="+LN+" PreviousPrice= "+PreviousPrice);
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