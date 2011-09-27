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
public class SMA_Timing_Method_proposed_by_Faber305 extends SimulateStrategy{
	public SMA_Timing_Method_proposed_by_Faber305(){
		super();
		StrategyID=305L;
		StrategyClassID=2L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
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
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		UnderlyingSecurity=(String)ParameterUtil.fetchParameter("String","UnderlyingSecurity", "^DJI", parameters);
		Interval=(Integer)ParameterUtil.fetchParameter("int","Interval", "10", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	boolean buyposition;
double TotalAmount;
double CurrentPrice;
double SMAValue;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("buyposition: ");
		sb.append(buyposition);
		sb.append("\n");
		sb.append("TotalAmount: ");
		sb.append(TotalAmount);
		sb.append("\n");
		sb.append("CurrentPrice: ");
		sb.append(CurrentPrice);
		sb.append("\n");
		sb.append("SMAValue: ");
		sb.append(SMAValue);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(buyposition);
		stream.writeObject(TotalAmount);
		stream.writeObject(CurrentPrice);
		stream.writeObject(SMAValue);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		buyposition=(Boolean)stream.readObject();;
		TotalAmount=(Double)stream.readObject();;
		CurrentPrice=(Double)stream.readObject();;
		SMAValue=(Double)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		buyposition=false;


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
	
		
		if( LTIDate.isLastNYSETradingDayOfWeek(CurrentDate))
{  Security S;
  S=getSecurity(UnderlyingSecurity);
 CurrentPrice = S.getAdjClose(CurrentDate);
 SMAValue = S.getSMA(CurrentDate, Interval, TimeUnit.MONTHLY,false);
}
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (!buyposition&& (CurrentPrice > SMAValue)&&LTIDate.isLastNYSETradingDayOfWeek(CurrentDate)) {
		   //using cash to buy underlying security
TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset, UnderlyingSecurity, TotalAmount, CurrentDate);
buyposition=true;
printToLog("Enter Action 1");
		}
		else if (buyposition && (CurrentPrice<SMAValue)&&LTIDate.isLastNYSETradingDayOfWeek(CurrentDate)) {
		   //sell security to get cash
TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy (curAsset, "CASH", TotalAmount, CurrentDate);
buyposition=false;
printToLog("Enter Action 2");
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