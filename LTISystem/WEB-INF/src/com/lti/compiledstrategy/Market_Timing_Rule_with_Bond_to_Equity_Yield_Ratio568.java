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
public class Market_Timing_Rule_with_Bond_to_Equity_Yield_Ratio568 extends SimulateStrategy{
	public Market_Timing_Rule_with_Bond_to_Equity_Yield_Ratio568(){
		super();
		StrategyID=568L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String BondString;
	public void setBondString(String BondString){
		this.BondString=BondString;
	}
	
	public String getBondString(){
		return this.BondString;
	}
	private String Threshold;
	public void setThreshold(String Threshold){
		this.Threshold=Threshold;
	}
	
	public String getThreshold(){
		return this.Threshold;
	}
	private String BuySecurity;
	public void setBuySecurity(String BuySecurity){
		this.BuySecurity=BuySecurity;
	}
	
	public String getBuySecurity(){
		return this.BuySecurity;
	}
	private int WaitingDay;
	public void setWaitingDay(int WaitingDay){
		this.WaitingDay=WaitingDay;
	}
	
	public int getWaitingDay(){
		return this.WaitingDay;
	}
	private int DelayDay;
	public void setDelayDay(int DelayDay){
		this.DelayDay=DelayDay;
	}
	
	public int getDelayDay(){
		return this.DelayDay;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		BondString=(String)ParameterUtil.fetchParameter("String","BondString", "^TNX", parameters);
		Threshold=(String)ParameterUtil.fetchParameter("String","Threshold", "SMA30", parameters);
		BuySecurity=(String)ParameterUtil.fetchParameter("String","BuySecurity", "^GSPC", parameters);
		WaitingDay=(Integer)ParameterUtil.fetchParameter("int","WaitingDay", "5", parameters);
		DelayDay=(Integer)ParameterUtil.fetchParameter("int","DelayDay", "5", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	int Position;
int[] SwitchSignal;
double ThresholdValue;
double PastThresholdValue;
Date LastSwitchDate;
double TotalAmount;
double Spread;

List<Date> days=new ArrayList<Date>();
List<Double> BondYieldList =new ArrayList<Double>();
List<Double> EquityYieldList =new ArrayList<Double>();
List<Double> RatioList =new ArrayList<Double>();
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
	@Override
  public void afterExecuting()   throws Exception {
    Date[] dates=new Date[days.size()];
    Double[][] values=new Double[days.size()][2];
    Double[] ratioValue=new Double[days.size()];
    Double[] currentValue = new Double[3];
    String xml1;
    String xml2;
    for(int i=0; i<days.size(); i++) {
       dates[i] = days.get(i);
       values[i][0]=BondYieldList.get(i);
       values[i][1]=EquityYieldList.get(i);
       ratioValue[i]=RatioList.get(i);
    }  
    currentValue[0] =  values[days.size()-1][0];
    currentValue[1] =  values[days.size()-1][1];
    currentValue[2] =  ratioValue[days.size()-1];

    xml1=buildXML(dates,values);
    xml2=buildXML(dates,ratioValue);
    com.lti.util.PersistentUtil.writeGlobalObject(xml1,"YieldData_"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(xml2,"BondEquityRatioList_"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(currentValue,"BondEquityCurrent_"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(dates[days.size()-1],"BondEquityDate_"+PortfolioID+".xml",StrategyID,PortfolioID);
  }
@Override
  public void afterUpdating()   throws Exception {
    Date[] dates=new Date[days.size()];
    Double[][] values=new Double[days.size()][2];
    Double[] ratioValue=new Double[days.size()];
    Double[] currentValue = new Double[3];
    String xml1;
    String xml2;
    for(int i=0; i<days.size(); i++) {
       dates[i] = days.get(i);
       values[i][0]=BondYieldList.get(i);
       values[i][1]=EquityYieldList.get(i);
       ratioValue[i]=RatioList.get(i);
    }  
    currentValue[0] =  values[days.size()-1][0];
    currentValue[1] =  values[days.size()-1][1];
    currentValue[2] =  ratioValue[days.size()-1];

    xml1=buildXML(dates,values);
    xml2=buildXML(dates,ratioValue);
    com.lti.util.PersistentUtil.writeGlobalObject(xml1,"YieldData_"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(xml2,"BondEquityRatioList_"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(currentValue,"BondEquityCurrent_"+PortfolioID+".xml",StrategyID,PortfolioID);
    com.lti.util.PersistentUtil.writeGlobalObject(dates[days.size()-1],"BondEquityDate_"+PortfolioID+".xml",StrategyID,PortfolioID);
  }

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		String S = BuySecurity;
Security Bond = getSecurity(BondString);
SwitchSignal = new int[DelayDay];

CurrentPortfolio.sellAsset(curAsset, CurrentDate);
TotalAmount = CurrentPortfolio.getCash();

double bondYield = Bond.getCurrentPrice(CurrentDate)/100;
double equityYield = 1/getLastYearPE(S,CurrentDate);
Spread = bondYield/equityYield;

days.add(CurrentDate);
BondYieldList.add(bondYield);
EquityYieldList.add(equityYield);
RatioList.add(Spread);
printToLog("BondYield = " + bondYield);
printToLog("EquityYield = " + equityYield);
printToLog("Bond To Equity Ratio = " + Spread);


//printToLog("The earliest available price date of the Security :"+getEarliestAvaliablePriceDate(getSecurity(S)));
//printToLog("The earliest available price date of the bond :"+getEarliestAvaliablePriceDate(Bond));

if(Threshold.charAt(0) == 'S')
{
    double SMA = Bond.getCurrentPrice(CurrentDate) * getLastYearPE(S,CurrentDate)/100;
    for(int i = 1; i<=Integer.parseInt(Threshold.substring(3)) -1; i++)
    {
        Date PastDate = LTIDate.getNewNYSETradingDay(CurrentDate,-i);
        SMA += Bond.getCurrentPrice(PastDate) * getLastYearPE(S,PastDate)/100;
    } 
    ThresholdValue = SMA/Integer.parseInt(Threshold.substring(3)) ;
}
else
    ThresholdValue= Double.parseDouble(Threshold.substring(0));     

if(Spread<ThresholdValue)
    SwitchSignal[DelayDay-1]=1;
else
    SwitchSignal[DelayDay-1]=0;

int j=1;
if(DelayDay>=2)
{
    PastThresholdValue = ThresholdValue;
    for(int i=0;i<=DelayDay-2;i++)
    {
        Date PastDate3 = LTIDate.getNewNYSETradingDay(CurrentDate,-i-1);
        double Spread3 = Bond.getCurrentPrice(PastDate3)*getLastYearPE(S,PastDate3)/100;
        if(Threshold.charAt(0) == 'S')
        {
            Date PastDate1 = LTIDate.getNewNYSETradingDay(CurrentDate,-i);
            double Spread1 = Bond.getCurrentPrice(PastDate1)*getLastYearPE(S,PastDate1)/100;
            Date PastDate2 = LTIDate.getNewNYSETradingDay(PastDate1, 1-Integer.parseInt(Threshold.substring(3)));   
            double Spread2 = Bond.getCurrentPrice(PastDate2)*getLastYearPE(S,PastDate2)/100;
            PastThresholdValue +=  (Spread2 - Spread1)/Integer.parseInt(Threshold.substring(3));        

            if(Spread3<PastThresholdValue)
                SwitchSignal[DelayDay-i-2]=1;
            else
                SwitchSignal[DelayDay-i-2]=0;
        }
        else
        {
            if(Spread3<ThresholdValue)
                SwitchSignal[DelayDay-i-2]=1;
            else
                SwitchSignal[DelayDay-i-2]=0;
        } 
        if(SwitchSignal[DelayDay-i-2]!=SwitchSignal[DelayDay-1]) 
            j=0;
    }
}

if(SwitchSignal[DelayDay-1]==1 && j==1)
{
    Position = 1;
    CurrentPortfolio.buy (curAsset, S, TotalAmount, CurrentDate);
    LastSwitchDate = CurrentDate;   
}
else
{
    Position = 0;
    LastSwitchDate = CurrentDate;  
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
	
		
		String S = BuySecurity;
Security Bond = getSecurity(BondString);	
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.isNYSETradingDay(CurrentDate)) {
		   double bondYield = Bond.getCurrentPrice(CurrentDate)/100;
double equityYield = 1/getLastYearPE(S,CurrentDate);
Spread = bondYield/equityYield;

days.add(CurrentDate);
BondYieldList.add(bondYield);
EquityYieldList.add(equityYield);
RatioList.add(Spread);
printToLog("BondYield = " + bondYield);
printToLog("EquityYield = " + equityYield);
printToLog("Bond To Equity Ratio = " + Spread);

if(Threshold.charAt(0) == 'S')
{
    Date PastDate = LTIDate.getNewNYSETradingDay(CurrentDate, 1-Integer.parseInt(Threshold.substring(3)));
    double Spread1= Bond.getCurrentPrice(PastDate)*getLastYearPE(S,PastDate)/100;
    ThresholdValue += (Spread - Spread1)/Integer.parseInt(Threshold.substring(3)) ;
    Date PastDate1 = LTIDate.getNewNYSETradingDay(PastDate, -1);
    if(ThresholdValue == Double.NaN && getLastYearPE(S,PastDate1) == Double.NaN  && getLastYearPE(S,PastDate) != Double.NaN)
    {
        double SMA = Bond.getCurrentPrice(CurrentDate) * getLastYearPE(S,CurrentDate)/100;
        for(int i = 1; i<=Integer.parseInt(Threshold.substring(3)) -1; i++)
        {
            Date PastDate2 = LTIDate.getNewNYSETradingDay(CurrentDate,-i);
            SMA += Bond.getCurrentPrice(PastDate2) * getLastYearPE(S,PastDate2)/100;
        } 
        ThresholdValue = SMA/Integer.parseInt(Threshold.substring(3)) ;
    }
}
printToLog("bond to equity = " + Spread);

if(DelayDay>=2)
{
    int temp;
    for(int i=0; i<=DelayDay-2;i++)
    {
        temp=SwitchSignal[i];
        SwitchSignal[i] = SwitchSignal[i+1];
        SwitchSignal[i+1] = temp;
    }
}

if(Spread<ThresholdValue)
    SwitchSignal[DelayDay-1]=1;
else
    SwitchSignal[DelayDay-1]=0;

if(LTIDate.calculateInterval(LastSwitchDate, CurrentDate)>=WaitingDay )
{

    if(Position != SwitchSignal[DelayDay-1] )
    {
        if(DelayDay>=2)
        {
             int j=1;
             for(int i=2;i<=DelayDay;i++)
             {
                 if(SwitchSignal[DelayDay-i]!=SwitchSignal[DelayDay-1]) 
                 {   
                     j=0;
                     break;
                 }          
             }
             if(j==1)
             {
                 Position = SwitchSignal[DelayDay-1];  
                 LastSwitchDate = CurrentDate;
             }    
         }
         else
         {
             Position = SwitchSignal[DelayDay-1];   
             LastSwitchDate = CurrentDate;
         }
     }
} 

if(LastSwitchDate == CurrentDate)
{

    if(Position == 1)
    {
        TotalAmount = CurrentPortfolio.getCash();
        CurrentPortfolio.buy(curAsset, S, TotalAmount, CurrentDate);
   }
    else
    {
        TotalAmount = CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);         
        CurrentPortfolio.sell(curAsset, S,TotalAmount,CurrentDate);
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