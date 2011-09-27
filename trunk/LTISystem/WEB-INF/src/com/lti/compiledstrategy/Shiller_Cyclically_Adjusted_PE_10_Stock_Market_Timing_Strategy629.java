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
public class Shiller_Cyclically_Adjusted_PE_10_Stock_Market_Timing_Strategy629 extends SimulateStrategy{
	public Shiller_Cyclically_Adjusted_PE_10_Stock_Market_Timing_Strategy629(){
		super();
		StrategyID=629L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String EquityFund;
	public void setEquityFund(String EquityFund){
		this.EquityFund=EquityFund;
	}
	
	public String getEquityFund(){
		return this.EquityFund;
	}
	private double SignificantOvervalued;
	public void setSignificantOvervalued(double SignificantOvervalued){
		this.SignificantOvervalued=SignificantOvervalued;
	}
	
	public double getSignificantOvervalued(){
		return this.SignificantOvervalued;
	}
	private double ModestOvervalued;
	public void setModestOvervalued(double ModestOvervalued){
		this.ModestOvervalued=ModestOvervalued;
	}
	
	public double getModestOvervalued(){
		return this.ModestOvervalued;
	}
	private double ModestUndervalued;
	public void setModestUndervalued(double ModestUndervalued){
		this.ModestUndervalued=ModestUndervalued;
	}
	
	public double getModestUndervalued(){
		return this.ModestUndervalued;
	}
	private double SignificantUndervalued;
	public void setSignificantUndervalued(double SignificantUndervalued){
		this.SignificantUndervalued=SignificantUndervalued;
	}
	
	public double getSignificantUndervalued(){
		return this.SignificantUndervalued;
	}
	private String Frequency;
	public void setFrequency(String Frequency){
		this.Frequency=Frequency;
	}
	
	public String getFrequency(){
		return this.Frequency;
	}
	private String CashFund;
	public void setCashFund(String CashFund){
		this.CashFund=CashFund;
	}
	
	public String getCashFund(){
		return this.CashFund;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		EquityFund=(String)ParameterUtil.fetchParameter("String","EquityFund", "^DWC", parameters);
		SignificantOvervalued=(Double)ParameterUtil.fetchParameter("double","SignificantOvervalued", "1.5", parameters);
		ModestOvervalued=(Double)ParameterUtil.fetchParameter("double","ModestOvervalued", "1.17", parameters);
		ModestUndervalued=(Double)ParameterUtil.fetchParameter("double","ModestUndervalued", "0.83", parameters);
		SignificantUndervalued=(Double)ParameterUtil.fetchParameter("double","SignificantUndervalued", "0.67", parameters);
		Frequency=(String)ParameterUtil.fetchParameter("String","Frequency", "weekly", parameters);
		CashFund=(String)ParameterUtil.fetchParameter("String","CashFund", "CASH", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double Ratio,Position, Percent, TotalAmount;

List<Date> weeks=new ArrayList<Date>();
List<Double> CurrentPE10List =new ArrayList<Double>();
List<Double> LongTermPE10List =new ArrayList<Double>();
List<Double> RatioList =new ArrayList<Double>();
int iWeeks=0;
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
	private boolean needToUpdateData = false;
@Override
  public void afterExecuting()   throws Exception {
    Date[] dates=new Date[weeks.size()];
    Double[][] values=new Double[weeks.size()][2];
    Double[] ratioValue=new Double[weeks.size()];
    Double[] currentValue = new Double[3];
    String valueState = null;
    String xml1;
    String xml2;
    for(int i=0; i<weeks.size(); i++) {
       dates[i] = weeks.get(i);
       values[i][0]=CurrentPE10List.get(i);
       values[i][1]=LongTermPE10List.get(i);
       ratioValue[i]=RatioList.get(i);
    }  
    currentValue[0] =  values[weeks.size()-1][0];
    currentValue[1] =  values[weeks.size()-1][1];
    currentValue[2] =  ratioValue[weeks.size()-1];
    
    if(currentValue[2] > 1) valueState = (int)java.lang.Math.floor((currentValue[2]*100-100)+0.5) + "% Over Value";
    if(currentValue[2] < 1) valueState = (int)java.lang.Math.floor((100-currentValue[2]*100)+0.5) +"% Under Value";

    xml1=buildXML(dates,values);
    xml2=buildXML(dates,ratioValue);
    com.lti.util.PersistentUtil.writeGlobalObject(xml1,"PE10Data_"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(xml2,"ShillerRatioList_"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(currentValue,"ShillerRatioDataValue_"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(dates[weeks.size()-1],"ShillerRatioDate_"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(valueState,"ShillerRatioState_"+PortfolioID+".xml",StrategyID,PortfolioID);
  }
@Override
  public void afterUpdating()   throws Exception {
    printToLog("RatioList :" + RatioList);
    if(needToUpdateData)
    {
    Date[] dates=new Date[weeks.size()];
    Double[][] values=new Double[weeks.size()][2];
    Double[] ratioValue=new Double[weeks.size()];
    Double[] currentValue = new Double[3];
    String valueState = null;
    String xml1;
    String xml2;
    for(int i=0; i<weeks.size(); i++) {
       dates[i] = weeks.get(i);
       values[i][0]=CurrentPE10List.get(i);
       values[i][1]=LongTermPE10List.get(i);
       ratioValue[i]=RatioList.get(i);
    }  
    currentValue[0] =  values[weeks.size()-1][0];
    currentValue[1] =  values[weeks.size()-1][1];
    currentValue[2] =  ratioValue[weeks.size()-1];
    
    if(currentValue[2] > 1) valueState = (int)java.lang.Math.floor((currentValue[2]*100-100)+0.5) + "% Over Value";
    if(currentValue[2] < 1) valueState = (int)java.lang.Math.floor((100-currentValue[2]*100)+0.5) +"% Under Value";

    printToLog("Ratio Series: " +  ratioValue);
    
    xml1=buildXML(dates,values);
    xml2=buildXML(dates,ratioValue);
    com.lti.util.PersistentUtil.writeGlobalObject(xml1,"PE10Data_"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(xml2,"ShillerRatioList_"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(currentValue,"ShillerRatioDataValue_"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(dates[weeks.size()-1],"ShillerRatioDate_"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(valueState,"ShillerRatioState_"+PortfolioID+".xml",StrategyID,PortfolioID);
    }
  }

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		Asset CurrentAsset;
/* decide position here */
Security S = getSecurity("^GSPC");
double price = S.getAdjClose(CurrentDate);
ShillerSP500 shillerSP500 = null;
try{
    shillerSP500 = securityManager.getShillerSP500Data(CurrentDate);
}catch(Exception eShiller){
    shillerSP500 = securityManager.getShillerSP500Data(LTIDate.getNewNYSEMonth(CurrentDate, -1));
}
double CurrentPE10 = (price*shillerSP500.getCPIRatio())/shillerSP500.getAvgRealEarnings10();
double LongTermPE10 = shillerSP500.getAvgPriceEarnings();

Ratio = CurrentPE10/LongTermPE10;

 weeks.add(CurrentDate);  
 CurrentPE10List.add(CurrentPE10);  
 LongTermPE10List.add(LongTermPE10);
 RatioList.add(Ratio);
 iWeeks++;  
printToLog("iWeeks: "+iWeeks + "   LongTermPE10 = " + LongTermPE10);
printToLog("iWeeks: "+iWeeks + "   CurrentPE10 = " + CurrentPE10);
printToLog("iWeeks: "+iWeeks + "   ShillerRatio = " + Ratio);

if ((Ratio >= SignificantOvervalued)) {
    Position=0.0;
} else if ( (Ratio < SignificantUndervalued)) {
    Position=4.0;
} else if ((Ratio < ModestOvervalued) && (Ratio >=  ModestUndervalued)) {
    Position=2.0;
} else if ((Ratio < ModestUndervalued) && (Ratio >= SignificantUndervalued)) {
    Position=3.0;
} else Position=1.0;
printToLog("Price= "+price+ " AvgEarnings10= "+ shillerSP500.getAvgEarnings10()+ " AvgPriceEarnings= "+shillerSP500.getAvgPriceEarnings() +" Ratio= "+Ratio+" Position= "+Position);
TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Risky");
//CurrentAsset.setClassID(getAssetClassID(getSecurity ("SPY")));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Risky", EquityFund, TotalAmount * Position*0.25, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Safe");
//CurrentAsset.setClassID(getAssetClassID(getSecurity ("CASH")));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Safe",CashFund, TotalAmount*(1.0-Position*0.25), CurrentDate);




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
	
		
		
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if ((Frequency.equals("weekly") && LTIDate.isLastNYSETradingDayOfWeek(CurrentDate)) || (Frequency.equals("monthly") && LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) ) {
		   //try{
Security S = getSecurity("^GSPC");
double price = S.getAdjClose(CurrentDate);
printToLog("S&P Price : " + price);
ShillerSP500 shillerSP500 = null;
try{
    shillerSP500 = securityManager.getShillerSP500Data(CurrentDate);
}catch(Exception eShiller){
    shillerSP500 = securityManager.getShillerSP500Data(LTIDate.getNewNYSEMonth(CurrentDate, -1));
}
printToLog("CPIRatio : " + shillerSP500.getCPIRatio());
printToLog("AvgRealEarnings10 : " + shillerSP500.getAvgRealEarnings10());
printToLog("AvgPriceEarnings: " + shillerSP500.getAvgPriceEarnings());
double CurrentPE10 = (price*shillerSP500.getCPIRatio())/shillerSP500.getAvgRealEarnings10();
double LongTermPE10 = shillerSP500.getAvgPriceEarnings();
Ratio = CurrentPE10/LongTermPE10;

 weeks.add(CurrentDate);  
 CurrentPE10List.add(CurrentPE10);  
 LongTermPE10List.add(LongTermPE10);
 RatioList.add(Ratio);
 iWeeks++;  
needToUpdateData = true;

printToLog(" Position= "+Position);
printToLog("iWeeks: "+iWeeks + "   Long Term PE10  Avg = " + LongTermPE10);
printToLog("iWeeks: "+iWeeks + "   CurrentPE10  = " + CurrentPE10);
printToLog("iWeeks: "+iWeeks + "   ShillerRatio = " + Ratio);


if ((Ratio >= SignificantOvervalued)) {
    if (Position !=0.0) {
    TotalAmount = CurrentPortfolio.getAssetAmount("Risky", CurrentDate);
    CurrentPortfolio.sellAsset("Risky", CurrentDate);  
    CurrentPortfolio.buy("Safe", CashFund, TotalAmount, CurrentDate);
     Position = 0.0;
   }
} else if ( (Ratio < SignificantUndervalued)) {
    if (Position != 4.0) {
    TotalAmount = CurrentPortfolio.getAssetAmount("Safe",CurrentDate);         
     CurrentPortfolio.sellAsset("Safe", CurrentDate);
     CurrentPortfolio.buy("Risky", EquityFund, TotalAmount, CurrentDate);
    Position = 4.0;
   }
} else if ((ModestOvervalued>0.0)&&(Ratio < SignificantOvervalued) && (Ratio >= ModestOvervalued)) {
    Percent=(Position-1.0)*0.25; // each slot is 25%
   Position = 1.0; 
    if (Percent != 0.0) {
      /* sell Percent Risky */
      TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
      CurrentPortfolio.sellAsset("Safe", CurrentDate);
      CurrentPortfolio.sellAsset("Risky", CurrentDate);
      CurrentPortfolio.buy("Risky", EquityFund, TotalAmount*Position*0.25, CurrentDate);
      CurrentPortfolio.buy("Safe", CashFund, TotalAmount*(1-Position*0.25), CurrentDate);
   } else {
     /* do nothing */
  }
} else if ((ModestOvervalued>0.0)&&(Ratio < ModestOvervalued) && (Ratio >=  ModestUndervalued)) {
    Percent=(Position-2.0)*0.25; // each slot is 25%
   Position=2.0;
    if (Percent !=0.0) {
      /* sell Percent Risky */
      TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
      CurrentPortfolio.sellAsset("Safe", CurrentDate);
      CurrentPortfolio.sellAsset("Risky", CurrentDate);
      CurrentPortfolio.buy("Risky", EquityFund, TotalAmount*Position*0.25, CurrentDate);
      CurrentPortfolio.buy("Safe", CashFund, TotalAmount*(1-Position*0.25), CurrentDate);
   } else {
     /* do nothing */
  }
} else if ((ModestOvervalued>0.0)&&(Ratio < ModestUndervalued) && (Ratio >= SignificantUndervalued)) {
    Percent=(Position-3.0)*0.25; // each slot is 25%
   Position = 3.0;
    if (Percent != 0.0) {
      /* sell Percent Risky */
      TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
      CurrentPortfolio.sellAsset("Safe", CurrentDate);
      CurrentPortfolio.sellAsset("Risky", CurrentDate);
      CurrentPortfolio.buy("Risky", EquityFund, TotalAmount*Position*0.25, CurrentDate);
      CurrentPortfolio.buy("Safe", CashFund, TotalAmount*(1-Position*0.25), CurrentDate);
   } else {
     /* do nothing */
  }
}

//}catch(Exception e1){
///    printToLog("Action Error");
//}
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