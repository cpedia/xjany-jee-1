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
public class test_At_Next_Open977 extends SimulateStrategy{
	public test_At_Next_Open977(){
		super();
		StrategyID=977L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private double Threshold;
	public void setThreshold(double Threshold){
		this.Threshold=Threshold;
	}
	
	public double getThreshold(){
		return this.Threshold;
	}
	private String BuySecurity;
	public void setBuySecurity(String BuySecurity){
		this.BuySecurity=BuySecurity;
	}
	
	public String getBuySecurity(){
		return this.BuySecurity;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		Threshold=(Double)ParameterUtil.fetchParameter("double","Threshold", "30", parameters);
		BuySecurity=(String)ParameterUtil.fetchParameter("String","BuySecurity", "^GSPC", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	int Position;
double ThresholdValue;
double TotalAmount;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("Position: ");
		sb.append(Position);
		sb.append("\n");
		sb.append("ThresholdValue: ");
		sb.append(ThresholdValue);
		sb.append("\n");
		sb.append("TotalAmount: ");
		sb.append(TotalAmount);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(Position);
		stream.writeObject(ThresholdValue);
		stream.writeObject(TotalAmount);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		Position=(Integer)stream.readObject();;
		ThresholdValue=(Double)stream.readObject();;
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
		Security S = getSecurity(BuySecurity);

CurrentPortfolio.sellAsset(curAsset, CurrentDate);
TotalAmount = CurrentPortfolio.getCash();

ThresholdValue=S.getSMA(CurrentDate,(int)Threshold,TimeUnit.DAILY);

if(S.getCurrentPrice(CurrentDate)>ThresholdValue)
    Position = 1;
else
    Position = 0;

if(Position == 1)
    CurrentPortfolio.buy(curAsset,S.getName(), TotalAmount, CurrentDate,true);

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
	
		
		Security S = getSecurity(BuySecurity);
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) {
		   ThresholdValue=S.getSMA(CurrentDate,(int)Threshold,TimeUnit.DAILY);

if(Position == 1)
    if(S.getCurrentPrice(CurrentDate)<ThresholdValue)
    {
        TotalAmount = CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);         
        CurrentPortfolio.sell(curAsset.getName(), S.getName(),TotalAmount,CurrentDate);
        Position = 0;
    }
if(Position == 0)
    if(S.getCurrentPrice(CurrentDate)>ThresholdValue)
    {
        TotalAmount = CurrentPortfolio.getCash();
        CurrentPortfolio.buy(curAsset.getName(), S.getName(), TotalAmount, CurrentDate,true);
        Position = 1;
    }
		}
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\test_At_Next_Open977.java:109: ~0&÷
//&÷ ¹Õ buy(java.lang.String,java.lang.String,double,java.util.Date,boolean)
//Mn { com.lti.type.executor.SimulateStrategy
//    CurrentPortfolio.buy(curAsset,S.getName(), TotalAmount, CurrentDate,true);
//                    ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\test_At_Next_Open977.java:143: ~0&÷
//&÷ ¹Õ getName()
//Mn { java.lang.String
//        CurrentPortfolio.sell(curAsset.getName(), S.getName(),TotalAmount,CurrentDate);
//                                      ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\test_At_Next_Open977.java:150: ~0&÷
//&÷ ¹Õ getName()
//Mn { java.lang.String
//        CurrentPortfolio.buy(curAsset.getName(), S.getName(), TotalAmount, CurrentDate,true);
//                                     ^
//3 ï