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
public class Calendar_Based_Trading_2491 extends SimulateStrategy{
	public Calendar_Based_Trading_2491(){
		super();
		StrategyID=491L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String UserSecurity;
	public void setUserSecurity(String UserSecurity){
		this.UserSecurity=UserSecurity;
	}
	
	public String getUserSecurity(){
		return this.UserSecurity;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		UserSecurity=(String)ParameterUtil.fetchParameter("String","UserSecurity", "^DWC", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	boolean bRule1;
boolean bRule2;
boolean bRule3;
boolean InterSellRule1;
boolean InterSellRule2;
int ruleNameRecord;      // "1" records the rule1, while "2" records the rule2.
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
		bRule1=false;
bRule2=false;
bRule3=false;
InterSellRule1=false;
InterSellRule2=false;
ruleNameRecord=0;
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
		
		 
		else if (!bRule3&&LTIDate.isLastNYSETradingDayOfMonth(LTIDate.getNewNYSETradingDay(CurrentDate,2))) {
		   bRule1=true;
if (!bRule2) { 
if(LTIDate.isNYSEHoliday(LTIDate.getNewWeekDay(CurrentDate,3))) bRule2=true;
double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
confidenceCheckStart("Rule1",amount,CurrentDate);
printToLog("Buy Rule1 is triggered! Amount:  "+amount);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy (curAsset, UserSecurity, amount, CurrentDate);
ruleNameRecord=1;
}
		}
		else if (LTIDate.isNYSEHoliday(LTIDate.getNewWeekDay(CurrentDate,3))) {
		   bRule2 = true;
if (!bRule1) { 
double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
confidenceCheckStart("Rule2",amount,CurrentDate);
printToLog("Buy Rule2 is triggered! Amount:  "+amount);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy (curAsset, UserSecurity, amount, CurrentDate);
ruleNameRecord=2;
}
		}
		else if (bRule1&&!bRule2&&LTIDate.isFirstNYSETradingDayofMonth (LTIDate.getNewNYSETradingDay(CurrentDate, -4))) {
		   bRule1 = false;
double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
confidenceCheckEnd("Rule1",amount,CurrentDate);
printToLog("Sell Rule1 is triggered! Amount:  "+amount);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy (curAsset, "CASH", amount, CurrentDate);
		}
		else if (!bRule1&&bRule2&&LTIDate.isNYSEHoliday(LTIDate.getNewWeekDay(CurrentDate,1))) {
		   if(!LTIDate.isLastNYSETradingDayOfWeek(LTIDate.getNewWeekDay(CurrentDate,4))&&!LTIDate.isLastNYSETradingDayOfWeek(LTIDate.getNewWeekDay(CurrentDate,2))&&
!LTIDate.isThirdFridayofMonth(LTIDate.getNewNYSETradingDay(CurrentDate,-1))){
bRule2=false;
double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
confidenceCheckEnd("Rule2",amount,CurrentDate);
printToLog("Sell Rule2 is triggered! Amount:  "+amount);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy (curAsset, "CASH", amount, CurrentDate);
}
else{
	bRule2=false;
	bRule3=true;
}
		}
		else if (bRule1&&bRule2) {
		   if(LTIDate.isFirstNYSETradingDayofMonth (LTIDate.getNewNYSETradingDay(CurrentDate, -4))){
	InterSellRule1=true;
	if(InterSellRule2){
		double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
if(ruleNameRecord==1){
confidenceCheckEnd("Rule1",amount,CurrentDate);
printToLog("Sell Rule1 is triggered! Amount:  "+amount);
ruleNameRecord=0;
}
else if(ruleNameRecord==2){
confidenceCheckEnd("Rule2",amount,CurrentDate);
printToLog("Sell Rule2 is triggered! Amount:  "+amount);
ruleNameRecord=0;
}
		CurrentPortfolio.sellAsset(curAsset,CurrentDate);
		CurrentPortfolio.buy (curAsset, "CASH", amount, CurrentDate);
		bRule1 = false; 
		bRule2 = false;
	  InterSellRule1=false;
		InterSellRule2=false;
	}
}
if (LTIDate.isNYSEHoliday(LTIDate.getNewWeekDay(CurrentDate,1))){
	InterSellRule2=true;
	if(InterSellRule1){
		if(!LTIDate.isLastNYSETradingDayOfWeek(LTIDate.getNewWeekDay(CurrentDate,2))&&!LTIDate.isLastNYSETradingDayOfWeek(LTIDate.getNewWeekDay(CurrentDate,4))&&!LTIDate.isThirdFridayofMonth(LTIDate. getNewWeekDay (CurrentDate,-1))){
		double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
if(ruleNameRecord==1){
confidenceCheckEnd("Rule1",amount,CurrentDate);
printToLog("Sell Rule1 is triggered! Amount:  "+amount);
ruleNameRecord=0;
}
else if(ruleNameRecord==2){
confidenceCheckEnd("Rule2",amount,CurrentDate);
printToLog("Sell Rule2 is triggered! Amount:  "+amount);
ruleNameRecord=0;
}
		CurrentPortfolio.sellAsset(curAsset,CurrentDate);
		CurrentPortfolio.buy (curAsset, "CASH", amount, CurrentDate);
		}
		else{
		bRule3=true;
		}
		bRule1 = false; 
		bRule2 = false;
		InterSellRule1=false;
		InterSellRule2=false;
	}
}
		}
		else if (bRule3&&LTIDate.isNYSEHoliday(LTIDate. getNewWeekDay (CurrentDate, -1))) {
		   bRule3=false;
double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
if(ruleNameRecord==1){
confidenceCheckEnd("Rule1",amount,CurrentDate);
printToLog("Sell Rule1 is triggered! Amount:  "+amount);
ruleNameRecord=0;
}
else if(ruleNameRecord==2){
confidenceCheckEnd("Rule2",amount,CurrentDate);
printToLog("Sell Rule2 is triggered! Amount:  "+amount);
ruleNameRecord=0;
}
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy (curAsset, "CASH", amount, CurrentDate);
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