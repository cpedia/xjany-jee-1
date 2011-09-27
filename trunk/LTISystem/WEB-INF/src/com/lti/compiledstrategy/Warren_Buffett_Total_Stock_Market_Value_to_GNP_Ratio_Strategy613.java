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
public class Warren_Buffett_Total_Stock_Market_Value_to_GNP_Ratio_Strategy613 extends SimulateStrategy{
	public Warren_Buffett_Total_Stock_Market_Value_to_GNP_Ratio_Strategy613(){
		super();
		StrategyID=613L;
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
	private String GNPorGDP;
	public void setGNPorGDP(String GNPorGDP){
		this.GNPorGDP=GNPorGDP;
	}
	
	public String getGNPorGDP(){
		return this.GNPorGDP;
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
		GNPorGDP=(String)ParameterUtil.fetchParameter("String","GNPorGDP", "GNP", parameters);
		SignificantOvervalued=(Double)ParameterUtil.fetchParameter("double","SignificantOvervalued", "1.15", parameters);
		ModestOvervalued=(Double)ParameterUtil.fetchParameter("double","ModestOvervalued", "0.90", parameters);
		ModestUndervalued=(Double)ParameterUtil.fetchParameter("double","ModestUndervalued", "0.75", parameters);
		SignificantUndervalued=(Double)ParameterUtil.fetchParameter("double","SignificantUndervalued", "0.50", parameters);
		Frequency=(String)ParameterUtil.fetchParameter("String","Frequency", "weekly", parameters);
		CashFund=(String)ParameterUtil.fetchParameter("String","CashFund", "CASH", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double Ratio, GPValue, TotalCap, Position, Percent, TotalAmount;

List<Date> weeks=new ArrayList<Date>();
List<Double> CapToGP =new ArrayList<Double>();
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
    Double[] values=new Double[weeks.size()];
    String valueState = null;
    String xml;
    for(int i=0; i<weeks.size(); i++) {
       dates[i] = weeks.get(i);
       values[i]=CapToGP.get(i);
    }     
    if(values[weeks.size()-1] < 0.5) valueState = "Significantly Undervalued";
    else if(values[weeks.size()-1] < 0.75) valueState = "Modestly Undervalued";
    else if(values[weeks.size()-1] < 0.9) valueState = "Fairly Valued";
    else if(values[weeks.size()-1] < 1.15) valueState = "Modestly Overvalued";
    else valueState = "Significantly Overvalued";

    xml=buildXML(dates,values);
    com.lti.util.PersistentUtil.writeGlobalObject(xml,"CapToGPList"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject((Double)values[weeks.size()-1],"CapToGPValue"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(dates[weeks.size()-1],"CapToGPDate"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(valueState,"CapToGPState"+PortfolioID+".xml",StrategyID,PortfolioID);
  }
@Override
  public void afterUpdating()   throws Exception {
    if(needToUpdateData)
    {
    Date[] dates=new Date[weeks.size()];
    Double[] values=new Double[weeks.size()];
    String valueState = null;
    String xml;
    for(int i=0; i<weeks.size(); i++) {
       dates[i] = weeks.get(i);
       values[i]=CapToGP.get(i);
    }     
    if(values[weeks.size()-1] < 0.5) valueState = "Significantly Undervalued";
    else if(values[weeks.size()-1] < 0.75) valueState = "Modestly Undervalued";
    else if(values[weeks.size()-1] < 0.9) valueState = "Fairly Valued";
    else if(values[weeks.size()-1] < 1.15) valueState = "Modestly Overvalued";
    else valueState = "Significantly Overvalued";

    xml=buildXML(dates,values);
    com.lti.util.PersistentUtil.writeGlobalObject(xml,"CapToGPList"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject((Double)values[weeks.size()-1],"CapToGPValue"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(dates[weeks.size()-1],"CapToGPDate"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(valueState,"CapToGPState"+PortfolioID+".xml",StrategyID,PortfolioID);
    }
  }

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		Asset CurrentAsset;
/* decide position here */
Security S = getSecurity("^DWC");
Indicator GP = getIndicator(GNPorGDP);
GPValue = GP.getValue(CurrentDate);
TotalCap = S.getAdjClose(CurrentDate);
Ratio = TotalCap/GPValue;

 weeks.add(CurrentDate);  
 CapToGP.add(Ratio);  
 iWeeks++;  
printToLog("iWeeks: "+iWeeks + "   the ratio of the total stock market capitalization to GNP = " + Ratio);

if ((Ratio >= SignificantOvervalued)) {
    Position=0.0;
} else if ( (Ratio < SignificantUndervalued)) {
    Position=4.0;
} else if ((Ratio < ModestOvervalued) && (Ratio >=  ModestUndervalued)) {
    Position=2.0;
} else if ((Ratio < ModestUndervalued) && (Ratio >= SignificantUndervalued)) {
    Position=3.0;
} else Position=1.0;

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
		   Security S = getSecurity("^DWC");
Indicator GP = getIndicator(GNPorGDP);
GPValue = GP.getValue(CurrentDate);
TotalCap = S.getAdjClose(CurrentDate);
Ratio = TotalCap/GPValue;


 weeks.add(CurrentDate);  
 CapToGP.add(Ratio);  
 iWeeks++;  
needToUpdateData = true;
printToLog("iWeeks: "+iWeeks + "   the ratio of the total stock market capitalization to GNP = " + Ratio);

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
  Position = 2.0;
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