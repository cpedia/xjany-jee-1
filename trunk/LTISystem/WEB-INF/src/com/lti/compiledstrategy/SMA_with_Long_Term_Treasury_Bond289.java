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
public class SMA_with_Long_Term_Treasury_Bond289 extends SimulateStrategy{
	public SMA_with_Long_Term_Treasury_Bond289(){
		super();
		StrategyID=289L;
		StrategyClassID=9L;
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
	private int NumberDays;
	public void setNumberDays(int NumberDays){
		this.NumberDays=NumberDays;
	}
	
	public int getNumberDays(){
		return this.NumberDays;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		UnderlyingSecurity=(String)ParameterUtil.fetchParameter("String","UnderlyingSecurity", "^DJI", parameters);
		NumberDays=(Integer)ParameterUtil.fetchParameter("int","NumberDays", "20", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	boolean position;
double TotalAmount, CurrentPrice, SMAValue;
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
		sb.append("SMAValue: ");
		sb.append(SMAValue);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(position);
		stream.writeObject(TotalAmount);
		stream.writeObject(CurrentPrice);
		stream.writeObject(SMAValue);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		position=(Boolean)stream.readObject();;
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
		Security S;
S=getSecurity(UnderlyingSecurity);
CurrentPrice = S.getAdjClose(CurrentDate);
SMAValue = S.getSMA(CurrentDate, NumberDays-1, TimeUnit.DAILY, false);
TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);

//printToLog("before init\n");
if (CurrentPrice> SMAValue) {
    CurrentPortfolio.buy(curAsset, UnderlyingSecurity, TotalAmount, CurrentDate);
    position=true;
} else {
    CurrentPortfolio.buy(curAsset, "VUSTX", TotalAmount, CurrentDate);
    position=false;
}

//printToLog("After init\n");
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
//printToLog("before Common\n");
S=getSecurity(UnderlyingSecurity);
CurrentPrice = S.getAdjClose(CurrentDate);
//printToLog("before Common getSMA\n");
SMAValue = S.getSMA(CurrentDate, NumberDays-1, TimeUnit.DAILY,false);
//printToLog("CommonAction, SMA: " + SMAValue +"Price: " + CurrentPrice+"\n");
TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
//printToLog("Current Price:"+CurrentPrice+"SMA:"+SMAValue+"Position:"+position);
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (!position&& (CurrentPrice > SMAValue)) {
		   CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset, UnderlyingSecurity, TotalAmount, CurrentDate);
position=true;

		}
		else if (position && (CurrentPrice<SMAValue)) {
		   CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy (curAsset, "VUSTX", TotalAmount, CurrentDate);
position=false;
//
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