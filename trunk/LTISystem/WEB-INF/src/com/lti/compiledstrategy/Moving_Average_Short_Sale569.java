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
public class Moving_Average_Short_Sale569 extends SimulateStrategy{
	public Moving_Average_Short_Sale569(){
		super();
		StrategyID=569L;
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
	private String LongSecurity;
	public void setLongSecurity(String LongSecurity){
		this.LongSecurity=LongSecurity;
	}
	
	public String getLongSecurity(){
		return this.LongSecurity;
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
	private String ShortSecurity;
	public void setShortSecurity(String ShortSecurity){
		this.ShortSecurity=ShortSecurity;
	}
	
	public String getShortSecurity(){
		return this.ShortSecurity;
	}
	private boolean ShortLong;
	public void setShortLong(boolean ShortLong){
		this.ShortLong=ShortLong;
	}
	
	public boolean getShortLong(){
		return this.ShortLong;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		SMA=(Boolean)ParameterUtil.fetchParameter("boolean","SMA", "true", parameters);
		LongSecurity=(String)ParameterUtil.fetchParameter("String","LongSecurity", "^DJI", parameters);
		Interval=(Integer)ParameterUtil.fetchParameter("int","Interval", "5", parameters);
		SignalSecurity=(String)ParameterUtil.fetchParameter("String","SignalSecurity", "^DJI", parameters);
		ShortSecurity=(String)ParameterUtil.fetchParameter("String","ShortSecurity", "^DJI", parameters);
		ShortLong=(Boolean)ParameterUtil.fetchParameter("boolean","ShortLong", "false", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	boolean position;
double TotalAmount, TotalAmountToCover,TotalCash;
double CurrentPrice;
double MAValue;
double preMAValue;
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
		position=false;
preMAValue = 0;
SShort = getSecurity(ShortSecurity);
/* buy the long */
TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
if (ShortLong) {
  CurrentPortfolio.shortSell(curAsset, LongSecurity, TotalAmount*0.5, CurrentDate);
} else {
   CurrentPortfolio.buy (curAsset, LongSecurity, TotalAmount*0.5, CurrentDate);
}
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
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (!position&& (CurrentPrice < MAValue)) {
		   TotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
ShortShares = (int)(TotalAmount*0.5/SShort.getCurrentPrice(CurrentDate));
CurrentPortfolio.shortSellByShareNumber(curAsset, ShortSecurity, ShortShares, CurrentDate);
position=true;
if (ShortLong) {
  CurrentPortfolio.shortSell(curAsset, LongSecurity, TotalAmount*0.5, CurrentDate);
} else {
   CurrentPortfolio.buy (curAsset, LongSecurity, TotalAmount*0.5, CurrentDate);
}
		}
		else if (position && (CurrentPrice > MAValue)) {
		   TotalAmountToCover = ShortShares*SShort.getCurrentPrice(CurrentDate);
TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
TotalCash = TotalAmount-CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
if (!ShortLong && TotalAmountToCover > TotalCash) {
   /* need to sell some long to cover the short */
   CurrentPortfolio.sell(curAsset, LongSecurity, TotalAmountToCover-TotalCash, CurrentDate);
} else if (!ShortLong && TotalAmountToCover < TotalCash) {
  /* buy more long */
   CurrentPortfolio.buy(curAsset, LongSecurity, TotalCash-TotalAmountToCover, CurrentDate);
}
 /* buy to close */
 CurrentPortfolio.buy(curAsset, ShortSecurity, TotalAmountToCover, CurrentDate);
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