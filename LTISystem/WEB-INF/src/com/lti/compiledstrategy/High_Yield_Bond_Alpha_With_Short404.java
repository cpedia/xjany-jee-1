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
public class High_Yield_Bond_Alpha_With_Short404 extends SimulateStrategy{
	public High_Yield_Bond_Alpha_With_Short404(){
		super();
		StrategyID=404L;
		StrategyClassID=10L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String SignalSecurity;
	public void setSignalSecurity(String SignalSecurity){
		this.SignalSecurity=SignalSecurity;
	}
	
	public String getSignalSecurity(){
		return this.SignalSecurity;
	}
	private int AlphaLength;
	public void setAlphaLength(int AlphaLength){
		this.AlphaLength=AlphaLength;
	}
	
	public int getAlphaLength(){
		return this.AlphaLength;
	}
	private String AlphaBenchmark;
	public void setAlphaBenchmark(String AlphaBenchmark){
		this.AlphaBenchmark=AlphaBenchmark;
	}
	
	public String getAlphaBenchmark(){
		return this.AlphaBenchmark;
	}
	private String SecurityForShort;
	public void setSecurityForShort(String SecurityForShort){
		this.SecurityForShort=SecurityForShort;
	}
	
	public String getSecurityForShort(){
		return this.SecurityForShort;
	}
	private String SecurityForLong;
	public void setSecurityForLong(String SecurityForLong){
		this.SecurityForLong=SecurityForLong;
	}
	
	public String getSecurityForLong(){
		return this.SecurityForLong;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		SignalSecurity=(String)ParameterUtil.fetchParameter("String","SignalSecurity", "VWEHX", parameters);
		AlphaLength=(Integer)ParameterUtil.fetchParameter("int","AlphaLength", "1", parameters);
		AlphaBenchmark=(String)ParameterUtil.fetchParameter("String","AlphaBenchmark", "VBMFX", parameters);
		SecurityForShort=(String)ParameterUtil.fetchParameter("String","SecurityForShort", "VWEHX", parameters);
		SecurityForLong=(String)ParameterUtil.fetchParameter("String","SecurityForLong", "VWEHX", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	boolean  shortposition;
int ShortShares;
double TotalAmount;
double AlphaValue;

	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("shortposition: ");
		sb.append(shortposition);
		sb.append("\n");
		sb.append("ShortShares: ");
		sb.append(ShortShares);
		sb.append("\n");
		sb.append("TotalAmount: ");
		sb.append(TotalAmount);
		sb.append("\n");
		sb.append("AlphaValue: ");
		sb.append(AlphaValue);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(shortposition);
		stream.writeObject(ShortShares);
		stream.writeObject(TotalAmount);
		stream.writeObject(AlphaValue);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		shortposition=(Boolean)stream.readObject();;
		ShortShares=(Integer)stream.readObject();;
		TotalAmount=(Double)stream.readObject();;
		AlphaValue=(Double)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		shortposition=false; 
TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset, "CASH", TotalAmount, CurrentDate);

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
S=getSecurity(SignalSecurity);
SShort=getSecurity(SecurityForShort);
AlphaValue = S.getAlpha(-AlphaLength*21, CurrentDate, TimeUnit.DAILY, false, AlphaBenchmark);
//printToLog(AlphaValue);
//21 days in a month
//AlphaValue=S.getAlpha(LTIDate.getNewDate(-1, TimeUnit.YEARLY, CurrentDate),CurrentDate);

		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (shortposition && (AlphaValue >0) ) {
		   /* cover short */
CurrentPortfolio.sellAsset(curAsset, CurrentDate);


/* go long here */
TotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.buy (curAsset, SecurityForLong, TotalAmount, CurrentDate);

shortposition=false;
		}
		else if ((!shortposition) && (AlphaValue <=0)) {
		   TotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
//ShortShares = (int)(TotalAmount/SShort.getCurrentPrice(CurrentDate));
printToLog("Total amount = "+TotalAmount);
CurrentPortfolio.shortSell(curAsset, SecurityForShort, TotalAmount, CurrentDate);
shortposition=true;

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