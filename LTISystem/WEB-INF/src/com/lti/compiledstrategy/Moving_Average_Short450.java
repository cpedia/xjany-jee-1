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
public class Moving_Average_Short450 extends SimulateStrategy{
	public Moving_Average_Short450(){
		super();
		StrategyID=450L;
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
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		SMA=(Boolean)ParameterUtil.fetchParameter("boolean","SMA", "true", parameters);
		LongSecurity=(String)ParameterUtil.fetchParameter("String","LongSecurity", "^DJI", parameters);
		Interval=(Integer)ParameterUtil.fetchParameter("int","Interval", "5", parameters);
		SignalSecurity=(String)ParameterUtil.fetchParameter("String","SignalSecurity", "^DJI", parameters);
		ShortSecurity=(String)ParameterUtil.fetchParameter("String","ShortSecurity", "^DJI", parameters);
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
CurrentPortfolio.buy (curAsset, LongSecurity, TotalAmount, CurrentDate);
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
		
		 
		else if (position&& (CurrentPrice > MAValue)) {
		   //try{
TotalAmountToCover = ShortShares*SShort.getCurrentPrice(CurrentDate);
TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);

//TotalCash = TotalAmount-CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
TotalCash =  CurrentPortfolio.getCash();
printToLog("Cash getten in the first time = " + TotalCash);
if (TotalAmountToCover > TotalCash) {
   printToLog("need to sell some long to cover the short = " + (TotalAmountToCover-TotalCash)); 
   CurrentPortfolio.sell(curAsset, LongSecurity, TotalAmountToCover-TotalCash, CurrentDate);
} else if (TotalAmountToCover < TotalCash) {
     printToLog("buy more long"); 
   CurrentPortfolio.buy(curAsset, LongSecurity, TotalCash-TotalAmountToCover, CurrentDate);
}

printToLog("Amount needed to close short position =  " + TotalAmountToCover);
//printToLog("Amount we have in the portfolio (calculated) =  " + TotalCash);
printToLog("Amount we have in the portfolio (gotten in the second time) =  " + CurrentPortfolio.getCash());

 /* buy to close */
 CurrentPortfolio.buy(curAsset, ShortSecurity, TotalAmountToCover, CurrentDate);
position=false;

//}catch(Exception e1){printToLog("action 1 error");}
		}
		else if (!position && (CurrentPrice<MAValue)) {
		   TotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
ShortShares = (int)(TotalAmount/SShort.getCurrentPrice(CurrentDate));
CurrentPortfolio.shortSellByShareNumber(curAsset, ShortSecurity, ShortShares, CurrentDate);
printToLog("Short sell share = " + ShortShares);
printToLog("Cash Amount after Short sell = " + CurrentPortfolio.getCash());
position=true;
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