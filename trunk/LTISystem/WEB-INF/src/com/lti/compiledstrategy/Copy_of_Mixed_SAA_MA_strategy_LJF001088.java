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
public class Copy_of_Mixed_SAA_MA_strategy_LJF001088 extends SimulateStrategy{
	public Copy_of_Mixed_SAA_MA_strategy_LJF001088(){
		super();
		StrategyID=1088L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private double MAEpsilo;
	public void setMAEpsilo(double MAEpsilo){
		this.MAEpsilo=MAEpsilo;
	}
	
	public double getMAEpsilo(){
		return this.MAEpsilo;
	}
	private double FixEpsilo;
	public void setFixEpsilo(double FixEpsilo){
		this.FixEpsilo=FixEpsilo;
	}
	
	public double getFixEpsilo(){
		return this.FixEpsilo;
	}
	private int Interval;
	public void setInterval(int Interval){
		this.Interval=Interval;
	}
	
	public int getInterval(){
		return this.Interval;
	}
	private String UnderlyingSecurity;
	public void setUnderlyingSecurity(String UnderlyingSecurity){
		this.UnderlyingSecurity=UnderlyingSecurity;
	}
	
	public String getUnderlyingSecurity(){
		return this.UnderlyingSecurity;
	}
	private String CheckFrequency;
	public void setCheckFrequency(String CheckFrequency){
		this.CheckFrequency=CheckFrequency;
	}
	
	public String getCheckFrequency(){
		return this.CheckFrequency;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		MAEpsilo=(Double)ParameterUtil.fetchParameter("double","MAEpsilo", "0", parameters);
		FixEpsilo=(Double)ParameterUtil.fetchParameter("double","FixEpsilo", "0.4", parameters);
		Interval=(Integer)ParameterUtil.fetchParameter("int","Interval", "3", parameters);
		UnderlyingSecurity=(String)ParameterUtil.fetchParameter("String","UnderlyingSecurity", "SPY", parameters);
		CheckFrequency=(String)ParameterUtil.fetchParameter("String","CheckFrequency", "Weekly", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	//Variables
//double MAEpsilo;//0 0.3 0.5 0.7 1
//double FixEpsilo;//0 0.30 0.50 0.70 1.00
//int Interval; //1 3 7 months(30_day,1_quarter,200_day,respectively)
//String UnderlyingSecurity;// ^DJI,SPY, VIVAX,  VWUSX, VISVX, VISGX

double RebalancePercent=0.05; //5%
double RiskyAllocation;
double MASignal=0;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("RebalancePercent: ");
		sb.append(RebalancePercent);
		sb.append("\n");
		sb.append("RiskyAllocation: ");
		sb.append(RiskyAllocation);
		sb.append("\n");
		sb.append("MASignal: ");
		sb.append(MASignal);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(RebalancePercent);
		stream.writeObject(RiskyAllocation);
		stream.writeObject(MASignal);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		RebalancePercent=(Double)stream.readObject();;
		RiskyAllocation=(Double)stream.readObject();;
		MASignal=(Double)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		//Pure Moving Average Strategy(PMA) : FixEpsilo=0;
//General Moving Average Strategy(GMA) : FixEpsilo!=0;
//Fixed allocation Strategy(Fixed): MAEpsilo=0;
//!!!Startdate <>200 days

/*Create Assets*/
Asset CurrentAsset;

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("US EQUITY");
CurrentAsset.setClassID(getAssetClassID("US EQUITY"));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("CASH");
CurrentAsset.setClassID(getAssetClassID("CASH"));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);

CurrentPortfolio.buy("US EQUITY",UnderlyingSecurity, 1, CurrentDate);
CurrentPortfolio.buy("CASH","CASH", 1, CurrentDate);
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
	
		
		//Common Action
if((LTIDate.isLastNYSETradingDayOfWeek(CurrentDate) && CheckFrequency.equalsIgnoreCase("Weekly")) ||(LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)&& CheckFrequency.equalsIgnoreCase("Monthly")))
{
	/*Get MASignal*/
	Security S;
	S=getSecurity(UnderlyingSecurity);
	double CurrentPrice = S.getAdjClose(CurrentDate);
	double SMAValue = S.getSMA(CurrentDate, Interval, TimeUnit.MONTHLY,false);
	if (CurrentPrice > SMAValue) {MASignal = 1;}
		else {MASignal =0;}
	
	/*Buy action*/
	RiskyAllocation=FixEpsilo+MAEpsilo*MASignal;
	double TotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
	//CurrentPortfolio.sellAssetCollection(CurrentDate);
	CurrentPortfolio.sell("US EQUITY", UnderlyingSecurity, CurrentPortfolio.getSecurityAmount("US EQUITY",UnderlyingSecurity,CurrentDate),CurrentDate);
	CurrentPortfolio.sell("CASH", "CASH", CurrentPortfolio.getSecurityAmount("CASH","CASH",CurrentDate),CurrentDate);
	CurrentPortfolio.buy("US EQUITY",UnderlyingSecurity,TotalAmount*RiskyAllocation,CurrentDate);
	CurrentPortfolio.buy("CASH","CASH",TotalAmount*(1-RiskyAllocation),CurrentDate);
	
	/*
	double TotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
	double RiskyAmount=CurrentPortfolio.getAssetAmount((getSecurity(UnderlyingSecurity).getAssetClass()));
	if (RiskyAmount/TotalAmount < RiskyAllocation*(1-RebalancePercent)) ||(RiskyAmount/TotalAmount > RiskyAllocation*(1+RebalancePercent)	// Rebalance
	{
		CurrentPortfolio.sell("US EQUITY", UnderlyingSecurity, CurrentPortfolio.getSecurityAmount("US EQUITY",UnderlyingSecurity,CurrentDate),CurrentDate);
		CurrentPortfolio.sell("CASH", "CASH", CurrentPortfolio.getSecurityAmount("CASH","CASH",CurrentDate),CurrentDate);
		CurrentPortfolio.buy("US EQUITY",UnderlyingSecurity,TotalAmount*RiskyAllocation,CurrentDate);
		CurrentPortfolio.buy("CASH","CASH",TotalAmount*(1-RiskyAllocation),CurrentDate);
		printToLog("Rebalanced!");
	}
	*/
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