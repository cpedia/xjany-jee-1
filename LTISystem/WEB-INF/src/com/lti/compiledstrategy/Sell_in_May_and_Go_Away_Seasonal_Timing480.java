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
public class Sell_in_May_and_Go_Away_Seasonal_Timing480 extends SimulateStrategy{
	public Sell_in_May_and_Go_Away_Seasonal_Timing480(){
		super();
		StrategyID=480L;
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
	private String SignalSecurity;
	public void setSignalSecurity(String SignalSecurity){
		this.SignalSecurity=SignalSecurity;
	}
	
	public String getSignalSecurity(){
		return this.SignalSecurity;
	}
	private int startyear;
	public void setStartyear(int startyear){
		this.startyear=startyear;
	}
	
	public int getStartyear(){
		return this.startyear;
	}
	private boolean bNoMACD;
	public void setBNoMACD(boolean bNoMACD){
		this.bNoMACD=bNoMACD;
	}
	
	public boolean getBNoMACD(){
		return this.bNoMACD;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		UnderlyingSecurity=(String)ParameterUtil.fetchParameter("String","UnderlyingSecurity", "^GSPC", parameters);
		SignalSecurity=(String)ParameterUtil.fetchParameter("String","SignalSecurity", "^GSPC", parameters);
		startyear=(Integer)ParameterUtil.fetchParameter("int","startyear", "", parameters);
		bNoMACD=(Boolean)ParameterUtil.fetchParameter("boolean","bNoMACD", "false", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	boolean positionofequity=false,dateofoct=false,dateofapril=false;
double CurrentPrice;
double EMA1;
double EMA2;
double preEMA1;
double preEMA2;
double MACDValue;
double preMACDValue;
double DEAValue;
double TotalAmount;
int rollingyear;
Date octdate;
Date aprildate;

	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("positionofequity: ");
		sb.append(positionofequity);
		sb.append("\n");
		sb.append("dateofoct: ");
		sb.append(dateofoct);
		sb.append("\n");
		sb.append("dateofapril: ");
		sb.append(dateofapril);
		sb.append("\n");
		sb.append("CurrentPrice: ");
		sb.append(CurrentPrice);
		sb.append("\n");
		sb.append("EMA1: ");
		sb.append(EMA1);
		sb.append("\n");
		sb.append("EMA2: ");
		sb.append(EMA2);
		sb.append("\n");
		sb.append("preEMA1: ");
		sb.append(preEMA1);
		sb.append("\n");
		sb.append("preEMA2: ");
		sb.append(preEMA2);
		sb.append("\n");
		sb.append("MACDValue: ");
		sb.append(MACDValue);
		sb.append("\n");
		sb.append("preMACDValue: ");
		sb.append(preMACDValue);
		sb.append("\n");
		sb.append("DEAValue: ");
		sb.append(DEAValue);
		sb.append("\n");
		sb.append("TotalAmount: ");
		sb.append(TotalAmount);
		sb.append("\n");
		sb.append("rollingyear: ");
		sb.append(rollingyear);
		sb.append("\n");
		sb.append("octdate: ");
		sb.append(octdate);
		sb.append("\n");
		sb.append("aprildate: ");
		sb.append(aprildate);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(positionofequity);
		stream.writeObject(dateofoct);
		stream.writeObject(dateofapril);
		stream.writeObject(CurrentPrice);
		stream.writeObject(EMA1);
		stream.writeObject(EMA2);
		stream.writeObject(preEMA1);
		stream.writeObject(preEMA2);
		stream.writeObject(MACDValue);
		stream.writeObject(preMACDValue);
		stream.writeObject(DEAValue);
		stream.writeObject(TotalAmount);
		stream.writeObject(rollingyear);
		stream.writeObject(octdate);
		stream.writeObject(aprildate);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		positionofequity=(Boolean)stream.readObject();;
		dateofoct=(Boolean)stream.readObject();;
		dateofapril=(Boolean)stream.readObject();;
		CurrentPrice=(Double)stream.readObject();;
		EMA1=(Double)stream.readObject();;
		EMA2=(Double)stream.readObject();;
		preEMA1=(Double)stream.readObject();;
		preEMA2=(Double)stream.readObject();;
		MACDValue=(Double)stream.readObject();;
		preMACDValue=(Double)stream.readObject();;
		DEAValue=(Double)stream.readObject();;
		TotalAmount=(Double)stream.readObject();;
		rollingyear=(Integer)stream.readObject();;
		octdate=(Date)stream.readObject();;
		aprildate=(Date)stream.readObject();;
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
S=getSecurity(SignalSecurity);
EMA1 = S.getEMA(CurrentDate,CurrentDate,12,TimeUnit.DAILY,false);
EMA2 = S.getEMA(CurrentDate,CurrentDate,26,TimeUnit.DAILY,false);
preMACDValue=EMA1-EMA2;
MACDValue=0;
rollingyear=startyear;
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
	
		
		octdate=LTIDate.getRecentNYSETradingDay(LTIDate.getDate(rollingyear,10,16));

aprildate=LTIDate.getRecentNYSETradingDay(LTIDate.getDate(rollingyear,4,26));

printToLog(octdate);
printToLog(aprildate);
//if(CurrentDate.equals()LTIDate. isNYSETradingDate(CurrentDate,-1,10,-1,-1,16,-1))\
//if(LTIDate. isNYSETradingDate(CurrentDate,underlyingyear,10,-1,-1,16,-1))


if(CurrentDate.equals(octdate))
{printToLog("oct arrived");
rollingyear++; dateofoct=true;dateofapril=false;

}



if(CurrentDate.equals(aprildate))
{
printToLog("april arrived");
dateofapril=true;dateofoct=false;}




Security S;
S=getSecurity(SignalSecurity);
CurrentPrice=S.getAdjClose(CurrentDate);
if(MACDValue==0)
MACDValue=preMACDValue;
else{
	preEMA1=EMA1;
	preEMA2=EMA2;
	preMACDValue=MACDValue;
	EMA1=(CurrentPrice*2+preEMA1*11)/13;
	EMA2=(CurrentPrice*2+preEMA2*25)/27;
	MACDValue=EMA1-EMA2;
	DEAValue=(MACDValue*2+preMACDValue*8)/10;
}
printToLog("DEAValue is "+DEAValue);
printToLog("MACDValue is "+MACDValue);
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if ((!bNoMACD && !positionofequity&&(DEAValue>0)&&(MACDValue>DEAValue)&&dateofoct) || (bNoMACD&&dateofoct)) {
		   printToLog("on a buy signal");
TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);


CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset,UnderlyingSecurity, TotalAmount, CurrentDate);
printToLog("buy UnderlyingSecurity");
positionofequity=true;
		}
		else if ((!bNoMACD&&positionofequity&&(DEAValue<0)&&(MACDValue<DEAValue)&&dateofapril) || (bNoMACD&&dateofapril)) {
		   printToLog("on a sell signal");
TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);

CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset,"CASH", TotalAmount, CurrentDate);
printToLog("buy cash");
positionofequity=false;

//CurrentDate.equals(deadlinedateofoct)
		}
		else if ((!bNoMACD&&!positionofequity&&LTIDate.isLastNYSETradingDayOfYear(CurrentDate)&&dateofoct)) {
		   printToLog("deadline of oct arrived");
TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);

CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset,UnderlyingSecurity, TotalAmount, CurrentDate);
printToLog("buy UnderlyingSecurity");
positionofequity=true;
		}
		else if ((!bNoMACD&&positionofequity&&LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate)&&dateofapril)) {
		   printToLog("dead line of april arrived");
TotalAmount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);

CurrentPortfolio.sellAsset(curAsset, CurrentDate);
CurrentPortfolio.buy (curAsset,"CASH", TotalAmount, CurrentDate);
printToLog("buy cash");
positionofequity=false;
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