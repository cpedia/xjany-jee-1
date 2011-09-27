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
public class High_Yield_Bond_Alpha402 extends SimulateStrategy{
	public High_Yield_Bond_Alpha402(){
		super();
		StrategyID=402L;
		StrategyClassID=10L;
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
	private int AlphaLength;
	public void setAlphaLength(int AlphaLength){
		this.AlphaLength=AlphaLength;
	}
	
	public int getAlphaLength(){
		return this.AlphaLength;
	}
	private String Securityforswitching;
	public void setSecurityforswitching(String Securityforswitching){
		this.Securityforswitching=Securityforswitching;
	}
	
	public String getSecurityforswitching(){
		return this.Securityforswitching;
	}
	private String alphabenchmark;
	public void setAlphabenchmark(String alphabenchmark){
		this.alphabenchmark=alphabenchmark;
	}
	
	public String getAlphabenchmark(){
		return this.alphabenchmark;
	}
	private String SecurityForTrade;
	public void setSecurityForTrade(String SecurityForTrade){
		this.SecurityForTrade=SecurityForTrade;
	}
	
	public String getSecurityForTrade(){
		return this.SecurityForTrade;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		UnderlyingSecurity=(String)ParameterUtil.fetchParameter("String","UnderlyingSecurity", "VWEHX", parameters);
		AlphaLength=(Integer)ParameterUtil.fetchParameter("int","AlphaLength", "3", parameters);
		Securityforswitching=(String)ParameterUtil.fetchParameter("String","Securityforswitching", "CASH", parameters);
		alphabenchmark=(String)ParameterUtil.fetchParameter("String","alphabenchmark", "CASH", parameters);
		SecurityForTrade=(String)ParameterUtil.fetchParameter("String","SecurityForTrade", "VWEHX", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	boolean position;
double TotalAmount;
double AlphaValue;

	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("position: ");
		sb.append(position);
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
		stream.writeObject(position);
		stream.writeObject(TotalAmount);
		stream.writeObject(AlphaValue);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		position=(Boolean)stream.readObject();;
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
		position=false;      

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
S=getSecurity(UnderlyingSecurity);
AlphaValue = S.getAlpha(-AlphaLength*21, CurrentDate, TimeUnit.DAILY, false,alphabenchmark);
printToLog(AlphaValue);
//21 days in a month
//AlphaValue=S.getAlpha(LTIDate.getNewDate(-1, TimeUnit.YEARLY, CurrentDate),CurrentDate);

		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (!position&& (AlphaValue>0)) {
		   TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset, SecurityForTrade, TotalAmount, CurrentDate);
position=true;

		}
		else if (position && (AlphaValue<=0)) {
		   TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy (curAsset, Securityforswitching, TotalAmount, CurrentDate);
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