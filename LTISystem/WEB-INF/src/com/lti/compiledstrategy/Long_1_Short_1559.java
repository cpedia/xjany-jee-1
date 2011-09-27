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
public class Long_1_Short_1559 extends SimulateStrategy{
	public Long_1_Short_1559(){
		super();
		StrategyID=559L;
		StrategyClassID=2L;
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
	private double ShortPercent;
	public void setShortPercent(double ShortPercent){
		this.ShortPercent=ShortPercent;
	}
	
	public double getShortPercent(){
		return this.ShortPercent;
	}
	private String AdjustFrequency;
	public void setAdjustFrequency(String AdjustFrequency){
		this.AdjustFrequency=AdjustFrequency;
	}
	
	public String getAdjustFrequency(){
		return this.AdjustFrequency;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		LongSecurity=(String)ParameterUtil.fetchParameter("String","LongSecurity", "IYR", parameters);
		ShortSecurity=(String)ParameterUtil.fetchParameter("String","ShortSecurity", "URE", parameters);
		ShortPercent=(Double)ParameterUtil.fetchParameter("double","ShortPercent", "0.5", parameters);
		AdjustFrequency=(String)ParameterUtil.fetchParameter("String","AdjustFrequency", "month", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double TotalAmount;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("TotalAmount: ");
		sb.append(TotalAmount);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(TotalAmount);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
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
		
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.buy (curAsset, LongSecurity, TotalAmount, CurrentDate);
CurrentPortfolio.shortSell(curAsset, ShortSecurity, TotalAmount*ShortPercent, CurrentDate);
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
		
		 
		else if ((AdjustFrequency.equals("year") && LTIDate.isLastNYSETradingDayOfYear(CurrentDate))
||(AdjustFrequency.equals("quarter") && LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate)) 
||(AdjustFrequency.equals("month") && LTIDate.isLastNYSETradingDayOfMonth(CurrentDate))) {
		   
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.buy (curAsset, LongSecurity, TotalAmount, CurrentDate);
CurrentPortfolio.shortSell(curAsset, ShortSecurity, TotalAmount*ShortPercent, CurrentDate);
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