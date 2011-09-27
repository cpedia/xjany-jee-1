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
public class Dollar_Cost_Averaging159 extends SimulateStrategy{
	public Dollar_Cost_Averaging159(){
		super();
		StrategyID=159L;
		StrategyClassID=5L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private double targetvalue;
	public void setTargetvalue(double targetvalue){
		this.targetvalue=targetvalue;
	}
	
	public double getTargetvalue(){
		return this.targetvalue;
	}
	private int totalPeriod;
	public void setTotalPeriod(int totalPeriod){
		this.totalPeriod=totalPeriod;
	}
	
	public int getTotalPeriod(){
		return this.totalPeriod;
	}
	private int FixedInterval;
	public void setFixedInterval(int FixedInterval){
		this.FixedInterval=FixedInterval;
	}
	
	public int getFixedInterval(){
		return this.FixedInterval;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		targetvalue=(Double)ParameterUtil.fetchParameter("double","targetvalue", "100000.0", parameters);
		totalPeriod=(Integer)ParameterUtil.fetchParameter("int","totalPeriod", "40", parameters);
		FixedInterval=(Integer)ParameterUtil.fetchParameter("int","FixedInterval", "1", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double deposit;
double accumulatedAmount;
int remainingPeriod;

	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("deposit: ");
		sb.append(deposit);
		sb.append("\n");
		sb.append("accumulatedAmount: ");
		sb.append(accumulatedAmount);
		sb.append("\n");
		sb.append("remainingPeriod: ");
		sb.append(remainingPeriod);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(deposit);
		stream.writeObject(accumulatedAmount);
		stream.writeObject(remainingPeriod);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		deposit=(Double)stream.readObject();;
		accumulatedAmount=(Double)stream.readObject();;
		remainingPeriod=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		printToLog(CurrentPortfolio.getTotalAmount(CurrentDate));
deposit=(targetvalue-CurrentPortfolio.getTotalAmount(CurrentDate))/totalPeriod;
remainingPeriod=totalPeriod;
accumulatedAmount=0;
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
		
		 
		else if (((((FixedInterval==0) && (LTIDate.isLastNYSETradingDayOfMonth(CurrentDate))) || ((FixedInterval==1) && (LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate))) || ((FixedInterval==2)) && (LTIDate.isLastNYSETradingDayOfYear(CurrentDate))))&&(remainingPeriod>0)) {
		   CurrentPortfolio.deposit(deposit,CurrentDate);
CurrentPortfolio.balance(CurrentDate);
accumulatedAmount=accumulatedAmount+deposit;
//take down the accumulated amount contributed to the portfolio

printToLog(remainingPeriod);
printToLog(accumulatedAmount);

remainingPeriod--;
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