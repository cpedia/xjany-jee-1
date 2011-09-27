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
public class The_125_05_Timing_Model_of_High_Yield_Bond_Strategy_by_Gerald_Appel119 extends SimulateStrategy{
	public The_125_05_Timing_Model_of_High_Yield_Bond_Strategy_by_Gerald_Appel119(){
		super();
		StrategyID=119L;
		StrategyClassID=20L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String bond;
	public void setBond(String bond){
		this.bond=bond;
	}
	
	public String getBond(){
		return this.bond;
	}
	private String CashSecurity;
	public void setCashSecurity(String CashSecurity){
		this.CashSecurity=CashSecurity;
	}
	
	public String getCashSecurity(){
		return this.CashSecurity;
	}
	private String signal;
	public void setSignal(String signal){
		this.signal=signal;
	}
	
	public String getSignal(){
		return this.signal;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		bond=(String)ParameterUtil.fetchParameter("String","bond", "MHITX", parameters);
		CashSecurity=(String)ParameterUtil.fetchParameter("String","CashSecurity", "CASH", parameters);
		signal=(String)ParameterUtil.fetchParameter("String","signal", "VWEHX", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	boolean position;
Date startDate;

	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("position: ");
		sb.append(position);
		sb.append("\n");
		sb.append("startDate: ");
		sb.append(startDate);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(position);
		stream.writeObject(startDate);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		position=(Boolean)stream.readObject();;
		startDate=(Date)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		 double amount=CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy(curAsset, CashSecurity, amount, CurrentDate);

startDate=CurrentDate;
position=false;

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
	
		
		printToLog(bond);
Security hy=getSecurity(signal);
printToLog(hy.toString());
double currentPrice=hy.getAdjClose(CurrentDate);
double lowPrice=hy.getLowestAdjustPrice(startDate,CurrentDate);
double highPrice=hy.getHighestAdjustPrice(startDate, CurrentDate);
boolean flag=false;

		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (position==false&&((currentPrice- lowPrice)/lowPrice)>0.0125&&flag==false) {
		   double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);

CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy (curAsset, bond, amount, CurrentDate);

startDate= CurrentDate;
position=true;
flag=true;

		}
		else if (position==true&&((highPrice-currentPrice)/highPrice)>0.005&&flag==false) {
		   double amount=CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy(curAsset,CashSecurity, amount, CurrentDate);

startDate= CurrentDate;
position=false;
flag=true;

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