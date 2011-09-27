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
public class Momentum_Hedge505 extends SimulateStrategy{
	public Momentum_Hedge505(){
		super();
		StrategyID=505L;
		StrategyClassID=9L;
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
	private String SecurityForShort;
	public void setSecurityForShort(String SecurityForShort){
		this.SecurityForShort=SecurityForShort;
	}
	
	public String getSecurityForShort(){
		return this.SecurityForShort;
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
	private String CoreLongSecurity;
	public void setCoreLongSecurity(String CoreLongSecurity){
		this.CoreLongSecurity=CoreLongSecurity;
	}
	
	public String getCoreLongSecurity(){
		return this.CoreLongSecurity;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		SMA=(Boolean)ParameterUtil.fetchParameter("boolean","SMA", "true", parameters);
		SecurityForShort=(String)ParameterUtil.fetchParameter("String","SecurityForShort", "SPY", parameters);
		Interval=(Integer)ParameterUtil.fetchParameter("int","Interval", "200", parameters);
		SignalSecurity=(String)ParameterUtil.fetchParameter("String","SignalSecurity", "SPY", parameters);
		CoreLongSecurity=(String)ParameterUtil.fetchParameter("String","CoreLongSecurity", "P_3724", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	boolean ShortPosition;
double TotalAmount;
double CurrentPrice;
double MAValue;
double preMAValue;
double ShortShares;
Asset ShortAsset;

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
		ShortPosition=false;
preMAValue = 0;

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("CoreLong");
CurrentAsset.setClassID(getAssetClassID("US Equity"));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("CoreLong", CoreLongSecurity, TotalAmount , CurrentDate);

/* for short sell asset */
ShortAsset=new Asset();
ShortAsset.setAssetStrategyID(getStrategyID("STATIC"));
ShortAsset.setName("Short");
ShortAsset.setClassID(getAssetClassID("US Equity"));
ShortAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(ShortAsset);

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
	
		
		Security S,SShort;

SShort=getSecurity(SecurityForShort);

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
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (!ShortPosition&& (CurrentPrice < MAValue)) {
		   TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
ShortShares = (int)(TotalAmount/SShort.getCurrentPrice(CurrentDate));

CurrentPortfolio.shortSellByShareNumber("Short", SShort.getID(), ShortShares, CurrentDate);
ShortPosition=true;


		}
		else if (ShortPosition && (CurrentPrice>MAValue)) {
		   TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
double TotalLongAmount=CurrentPortfolio.getAssetAmount("CoreLong", CurrentDate);
if (TotalLongAmount>TotalAmount) {
/*need to sell some long to buy back short */
CurrentPortfolio.sell("CoreLong", CoreLongSecurity, TotalLongAmount-TotalAmount, CurrentDate);
}
/* cover short  and might buy more*/
CurrentPortfolio.buy("Short", SecurityForShort, -CurrentPortfolio.getAssetAmount("Short", CurrentDate), CurrentDate);
ShortPosition=false;
/* buy more CoreLong */
if (TotalAmount>TotalLongAmount)
CurrentPortfolio.buy("CoreLong", CoreLongSecurity, TotalAmount-TotalLongAmount, CurrentDate);

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