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
public class Moving_Average_with_Signal449 extends SimulateStrategy{
	public Moving_Average_with_Signal449(){
		super();
		StrategyID=449L;
		StrategyClassID=16L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private boolean SMA;
	public void setSMA(boolean SMA){
		this.SMA=SMA;
	}
	
	public boolean getSMA(){
		return this.SMA;
	}
	private String UnderlyingSecurity;
	public void setUnderlyingSecurity(String UnderlyingSecurity){
		this.UnderlyingSecurity=UnderlyingSecurity;
	}
	
	public String getUnderlyingSecurity(){
		return this.UnderlyingSecurity;
	}
	private int Interval;
	public void setInterval(int Interval){
		this.Interval=Interval;
	}
	
	public int getInterval(){
		return this.Interval;
	}
	private String SignalSecurity;
	public void setSignalSecurity(String SignalSecurity){
		this.SignalSecurity=SignalSecurity;
	}
	
	public String getSignalSecurity(){
		return this.SignalSecurity;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		SMA=(Boolean)ParameterUtil.fetchParameter("boolean","SMA", "true", parameters);
		UnderlyingSecurity=(String)ParameterUtil.fetchParameter("String","UnderlyingSecurity", "^DJI", parameters);
		Interval=(Integer)ParameterUtil.fetchParameter("int","Interval", "5", parameters);
		SignalSecurity=(String)ParameterUtil.fetchParameter("String","SignalSecurity", "^DJI", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	boolean position;
double TotalAmount;
double CurrentPrice;
double MAValue;
double preMAValue;


	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("position: ");
		sb.append(position);
		sb.append("\n");
		sb.append("TotalAmount: ");
		sb.append(TotalAmount);
		sb.append("\n");
		sb.append("CurrentPrice: ");
		sb.append(CurrentPrice);
		sb.append("\n");
		sb.append("MAValue: ");
		sb.append(MAValue);
		sb.append("\n");
		sb.append("preMAValue: ");
		sb.append(preMAValue);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(position);
		stream.writeObject(TotalAmount);
		stream.writeObject(CurrentPrice);
		stream.writeObject(MAValue);
		stream.writeObject(preMAValue);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		position=(Boolean)stream.readObject();;
		TotalAmount=(Double)stream.readObject();;
		CurrentPrice=(Double)stream.readObject();;
		MAValue=(Double)stream.readObject();;
		preMAValue=(Double)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		position=false;
preMAValue = 0;

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
	
		
		Security S;
S=getSecurity(SignalSecurity);
CurrentPrice = S.getAdjClose(CurrentDate);
if(SMA)
MAValue = S.getSMA(CurrentDate, Interval, TimeUnit.DAILY,false);
else{
if(preMAValue==0){
MAValue = S.getEMA(CurrentDate,CurrentDate, Interval, TimeUnit.DAILY,false);
preMAValue=MAValue;
}
else{
preMAValue=MAValue;
MAValue = (CurrentPrice*2+preMAValue*(Interval-1))/(Interval+1);
}
}
printToLog("MAValue "+MAValue+ " CurrentPrice "+CurrentPrice);
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (!position&& (CurrentPrice > MAValue)) {
		   CurrentPortfolio.sellAsset(curAsset, CurrentDate);
TotalAmount = CurrentPortfolio.getTotalAmount( CurrentDate);
CurrentPortfolio.buy (curAsset, UnderlyingSecurity, TotalAmount, CurrentDate);
position=true;

		}
		else if (position && (CurrentPrice<MAValue)) {
		   CurrentPortfolio.sellAsset(curAsset,CurrentDate);
TotalAmount = CurrentPortfolio.getTotalAmount( CurrentDate);
CurrentPortfolio.buy (curAsset, "CASH", TotalAmount, CurrentDate);
position=false;
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