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
public class Long_Short_Index551 extends SimulateStrategy{
	public Long_Short_Index551(){
		super();
		StrategyID=551L;
		StrategyClassID=16L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String LongSecurity;
	public void setLongSecurity(String LongSecurity){
		this.LongSecurity=LongSecurity;
	}
	
	public String getLongSecurity(){
		return this.LongSecurity;
	}
	private String ShortSecurity;
	public void setShortSecurity(String ShortSecurity){
		this.ShortSecurity=ShortSecurity;
	}
	
	public String getShortSecurity(){
		return this.ShortSecurity;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		LongSecurity=(String)ParameterUtil.fetchParameter("String","LongSecurity", "VIVAX", parameters);
		ShortSecurity=(String)ParameterUtil.fetchParameter("String","ShortSecurity", "VIGRX", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double TotalAmount;
int ShortShares;
Security SShort;

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
		SShort = getSecurity(ShortSecurity);
/* buy the long */
TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset, LongSecurity, TotalAmount, CurrentDate);
/* short */
//ShortShares = (int)(TotalAmount/SShort.getCurrentPrice(CurrentDate));
//CurrentPortfolio.shortSellByShareNumber(curAsset, ShortSecurity, ShortShares, CurrentDate);
CurrentPortfolio.shortSell(curAsset, ShortSecurity, TotalAmount, CurrentDate);
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
		
		 
		else if (LTIDate.isLastNYSETradingDayOfWeek(CurrentDate)) {
		   SShort = getSecurity(ShortSecurity);
/* buy the long */
TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
//printToLog("Total Amount "+TotalAmount);
CurrentPortfolio.buy (curAsset, LongSecurity, TotalAmount, CurrentDate);
//printToLog("Buying "+TotalAmount);
/* short */
//ShortShares = (int)(TotalAmount/SShort.getCurrentPrice(CurrentDate));
//CurrentPortfolio.shortSellByShareNumber(curAsset, ShortSecurity, ShortShares, CurrentDate);
CurrentPortfolio.shortSell(curAsset, ShortSecurity, TotalAmount, CurrentDate);
//printToLog("Shorting "+TotalAmount);
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