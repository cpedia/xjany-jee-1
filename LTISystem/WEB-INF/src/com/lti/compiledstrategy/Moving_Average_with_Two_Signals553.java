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
public class Moving_Average_with_Two_Signals553 extends SimulateStrategy{
	public Moving_Average_with_Two_Signals553(){
		super();
		StrategyID=553L;
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
	private String SignalSecurity2;
	public void setSignalSecurity2(String SignalSecurity2){
		this.SignalSecurity2=SignalSecurity2;
	}
	
	public String getSignalSecurity2(){
		return this.SignalSecurity2;
	}
	private int Interval2;
	public void setInterval2(int Interval2){
		this.Interval2=Interval2;
	}
	
	public int getInterval2(){
		return this.Interval2;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		SMA=(Boolean)ParameterUtil.fetchParameter("boolean","SMA", "true", parameters);
		UnderlyingSecurity=(String)ParameterUtil.fetchParameter("String","UnderlyingSecurity", "^DJI", parameters);
		Interval=(Integer)ParameterUtil.fetchParameter("int","Interval", "5", parameters);
		SignalSecurity=(String)ParameterUtil.fetchParameter("String","SignalSecurity", "^DJI", parameters);
		SignalSecurity2=(String)ParameterUtil.fetchParameter("String","SignalSecurity2", "^DJI", parameters);
		Interval2=(Integer)ParameterUtil.fetchParameter("int","Interval2", "5", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	boolean position;
double TotalAmount;
double CurrentPrice;
double MAValue;
double preMAValue;
double CurrentPrice2;
double MAValue2;
double preMAValue2;



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
		sb.append("CurrentPrice2: ");
		sb.append(CurrentPrice2);
		sb.append("\n");
		sb.append("MAValue2: ");
		sb.append(MAValue2);
		sb.append("\n");
		sb.append("preMAValue2: ");
		sb.append(preMAValue2);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(position);
		stream.writeObject(TotalAmount);
		stream.writeObject(CurrentPrice);
		stream.writeObject(MAValue);
		stream.writeObject(preMAValue);
		stream.writeObject(CurrentPrice2);
		stream.writeObject(MAValue2);
		stream.writeObject(preMAValue2);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		position=(Boolean)stream.readObject();;
		TotalAmount=(Double)stream.readObject();;
		CurrentPrice=(Double)stream.readObject();;
		MAValue=(Double)stream.readObject();;
		preMAValue=(Double)stream.readObject();;
		CurrentPrice2=(Double)stream.readObject();;
		MAValue2=(Double)stream.readObject();;
		preMAValue2=(Double)stream.readObject();;
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
preMAValue2 = 0;

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

S=getSecurity(SignalSecurity2);
CurrentPrice2 = S.getAdjClose(CurrentDate);
if(SMA)
MAValue2 = S.getSMA(CurrentDate, Interval2, TimeUnit.DAILY,false);
else{
if(preMAValue2==0){
MAValue2 = S.getEMA(CurrentDate,CurrentDate, Interval2, TimeUnit.DAILY,false);
preMAValue2=MAValue2;
}
else{
preMAValue2=MAValue2;
MAValue2 = (CurrentPrice2*2+preMAValue2*(Interval2-1))/(Interval2+1);
}
}
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (!position&& (CurrentPrice > MAValue) && (CurrentPrice2>MAValue2)) {
		   TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset, UnderlyingSecurity, TotalAmount, CurrentDate);
position=true;

		}
		else if (position &&((CurrentPrice<MAValue) || (CurrentPrice2<MAValue2))) {
		   TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
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