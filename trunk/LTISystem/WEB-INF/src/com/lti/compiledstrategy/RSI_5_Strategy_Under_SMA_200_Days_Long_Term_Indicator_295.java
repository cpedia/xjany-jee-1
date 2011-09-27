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
public class RSI_5_Strategy_Under_SMA_200_Days_Long_Term_Indicator_295 extends SimulateStrategy{
	public RSI_5_Strategy_Under_SMA_200_Days_Long_Term_Indicator_295(){
		super();
		StrategyID=295L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String userSecurity;
	public void setUserSecurity(String userSecurity){
		this.userSecurity=userSecurity;
	}
	
	public String getUserSecurity(){
		return this.userSecurity;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		userSecurity=(String)ParameterUtil.fetchParameter("String","userSecurity", "", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date startDate;
boolean position;
double RSI5;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("startDate: ");
		sb.append(startDate);
		sb.append("\n");
		sb.append("position: ");
		sb.append(position);
		sb.append("\n");
		sb.append("RSI5: ");
		sb.append(RSI5);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(startDate);
		stream.writeObject(position);
		stream.writeObject(RSI5);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		startDate=(Date)stream.readObject();;
		position=(Boolean)stream.readObject();;
		RSI5=(Double)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		startDate=LTIDate.getNewNYSETradingDay(CurrentDate,-5);
Security S=getSecurity(userSecurity);
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
	
		
		Security S=getSecurity(userSecurity);
//RSI5=S.getRSI(startDate,CurrentDate);
//startDate=LTIDate.getNewNYSETradingDay(startDate,1);

RSI5=S.getRSI(LTIDate.getNewNYSETradingDay(CurrentDate, -5), CurrentDate,false);
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (RSI5<30&&position==false) {
		   if (S.getAdjClose(CurrentDate) > S.getSMA(CurrentDate, 199, TimeUnit.DAILY, false)) {
double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset, userSecurity, amount, CurrentDate);
position=true;
}
		}
		else if (RSI5>50&&position==true) {
		   double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy (curAsset, "CASH", amount, CurrentDate);
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