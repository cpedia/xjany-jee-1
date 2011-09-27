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
public class MACD_Strategy311 extends SimulateStrategy{
	public MACD_Strategy311(){
		super();
		StrategyID=311L;
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
		UserSecurity=(String)ParameterUtil.fetchParameter("String","UserSecurity", "^DJI", parameters);
		SignalSecurity=(String)ParameterUtil.fetchParameter("String","SignalSecurity", "^DJI", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	boolean position;
double CurrentPrice;
double EMA1;
double EMA2;
double preEMA1;
double preEMA2;
double MACDValue;
double preMACDValue;
double DEAValue;
double TotalAmount;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("position: ");
		sb.append(position);
		sb.append("\n");
		sb.append("CurrentPrice: ");
		sb.append(CurrentPrice);
		sb.append("\n");
		sb.append("EMA1: ");
		sb.append(EMA1);
		sb.append("\n");
		sb.append("EMA2: ");
		sb.append(EMA2);
		sb.append("\n");
		sb.append("preEMA1: ");
		sb.append(preEMA1);
		sb.append("\n");
		sb.append("preEMA2: ");
		sb.append(preEMA2);
		sb.append("\n");
		sb.append("MACDValue: ");
		sb.append(MACDValue);
		sb.append("\n");
		sb.append("preMACDValue: ");
		sb.append(preMACDValue);
		sb.append("\n");
		sb.append("DEAValue: ");
		sb.append(DEAValue);
		sb.append("\n");
		sb.append("TotalAmount: ");
		sb.append(TotalAmount);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(position);
		stream.writeObject(CurrentPrice);
		stream.writeObject(EMA1);
		stream.writeObject(EMA2);
		stream.writeObject(preEMA1);
		stream.writeObject(preEMA2);
		stream.writeObject(MACDValue);
		stream.writeObject(preMACDValue);
		stream.writeObject(DEAValue);
		stream.writeObject(TotalAmount);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		position=(Boolean)stream.readObject();;
		CurrentPrice=(Double)stream.readObject();;
		EMA1=(Double)stream.readObject();;
		EMA2=(Double)stream.readObject();;
		preEMA1=(Double)stream.readObject();;
		preEMA2=(Double)stream.readObject();;
		MACDValue=(Double)stream.readObject();;
		preMACDValue=(Double)stream.readObject();;
		DEAValue=(Double)stream.readObject();;
		TotalAmount=(Double)stream.readObject();;
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
Security S;
S=getSecurity(SignalSecurity);
EMA1 = S.getEMA(CurrentDate,CurrentDate,12,TimeUnit.DAILY,false);
EMA2 = S.getEMA(CurrentDate,CurrentDate,26,TimeUnit.DAILY,false);
preMACDValue=EMA1-EMA2;
MACDValue=0;
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
CurrentPrice=S.getAdjClose(CurrentDate);
if(MACDValue==0)
MACDValue=preMACDValue;
else{
	preEMA1=EMA1;
	preEMA2=EMA2;
	preMACDValue=MACDValue;
	EMA1=(CurrentPrice*2+preEMA1*11)/13;
	EMA2=(CurrentPrice*2+preEMA2*25)/27;
	MACDValue=EMA1-EMA2;
	DEAValue=(MACDValue*2+preMACDValue*8)/10;
}
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (!position&&(DEAValue>0)&&(MACDValue>DEAValue)) {
		   TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset,UserSecurity, TotalAmount, CurrentDate);
position=true;
		}
		else if (position&&(DEAValue<0)&&(MACDValue<DEAValue)) {
		   TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset,"CASH", TotalAmount, CurrentDate);
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