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
public class High_Yield_Bond_Timing_Strategy187 extends SimulateStrategy{
	public High_Yield_Bond_Timing_Strategy187(){
		super();
		StrategyID=187L;
		StrategyClassID=20L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private double buystop;
	public void setBuystop(double buystop){
		this.buystop=buystop;
	}
	
	public double getBuystop(){
		return this.buystop;
	}
	private double sellstop;
	public void setSellstop(double sellstop){
		this.sellstop=sellstop;
	}
	
	public double getSellstop(){
		return this.sellstop;
	}
	private String bond;
	public void setBond(String bond){
		this.bond=bond;
	}
	
	public String getBond(){
		return this.bond;
	}
	private String Securityforswitching;
	public void setSecurityforswitching(String Securityforswitching){
		this.Securityforswitching=Securityforswitching;
	}
	
	public String getSecurityforswitching(){
		return this.Securityforswitching;
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
		buystop=(Double)ParameterUtil.fetchParameter("double","buystop", "0.025", parameters);
		sellstop=(Double)ParameterUtil.fetchParameter("double","sellstop", "0.025", parameters);
		bond=(String)ParameterUtil.fetchParameter("String","bond", "VWEHX", parameters);
		Securityforswitching=(String)ParameterUtil.fetchParameter("String","Securityforswitching", "CASH", parameters);
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
		printToLog("init 0");
double amount=CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);
printToLog("init 1");
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
printToLog("init 2");
CurrentPortfolio.buy(curAsset, "CASH", amount, CurrentDate);
printToLog("init 3");
// Liquidate current asset and buy in short-term bond fund which is similar to money market fund. 

startDate=CurrentDate;
position=false;
printToLog("init 4");

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
	
		
		Security hy;
hy=getSecurity(signal);
printToLog(CurrentDate.toString());
double currentPrice=hy. getAdjClose(CurrentDate);

double lowPrice=hy.getLowestAdjustPrice(startDate,CurrentDate);
double highPrice=hy.getHighestAdjustPrice(startDate, CurrentDate);
boolean flag=false;

/* also do the sell stop on the underlying security */

hy=getSecurity(bond);
double currentUnderlyingPrice=hy. getAdjClose(CurrentDate);
double highUnderlyingPrice=hy.getHighestAdjustPrice(startDate, CurrentDate);
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (position==false&&((currentPrice- lowPrice)/lowPrice)>buystop&&flag==false) {
		   double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
//Sell short-term bond fund and buy in the assigned high yield bond

CurrentPortfolio.buy (curAsset, bond, amount, CurrentDate);
startDate= CurrentDate;
position=true;
flag=true;

		}
		else if (position==true&&(((highPrice-currentPrice)/highPrice)>sellstop || (highUnderlyingPrice-currentUnderlyingPrice)/highUnderlyingPrice>sellstop)&&flag==false) {
		   double amount=CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy(curAsset, "CASH", amount, CurrentDate);
// Sell high yield bond fund and get back"FSHBX"

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