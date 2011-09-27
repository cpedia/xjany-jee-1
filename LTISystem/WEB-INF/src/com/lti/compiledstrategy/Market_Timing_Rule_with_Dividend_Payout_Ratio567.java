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
public class Market_Timing_Rule_with_Dividend_Payout_Ratio567 extends SimulateStrategy{
	public Market_Timing_Rule_with_Dividend_Payout_Ratio567(){
		super();
		StrategyID=567L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String BuySecurity;
	public void setBuySecurity(String BuySecurity){
		this.BuySecurity=BuySecurity;
	}
	
	public String getBuySecurity(){
		return this.BuySecurity;
	}
	private String Threshold;
	public void setThreshold(String Threshold){
		this.Threshold=Threshold;
	}
	
	public String getThreshold(){
		return this.Threshold;
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
		BuySecurity=(String)ParameterUtil.fetchParameter("String","BuySecurity", "^GSPC", parameters);
		Threshold=(String)ParameterUtil.fetchParameter("String","Threshold", "SMA30", parameters);
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
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("Position: ");
		sb.append(Position);
		sb.append("\n");
		sb.append("SwitchSignal: ");
		sb.append(SwitchSignal);
		sb.append("\n");
		sb.append("ThresholdValue: ");
		sb.append(ThresholdValue);
		sb.append("\n");
		sb.append("PastThresholdValue: ");
		sb.append(PastThresholdValue);
		sb.append("\n");
		sb.append("LastSwitchDate: ");
		sb.append(LastSwitchDate);
		sb.append("\n");
		sb.append("TotalAmount: ");
		sb.append(TotalAmount);
		sb.append("\n");
		sb.append("Spread: ");
		sb.append(Spread);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(Position);
		stream.writeObject(SwitchSignal);
		stream.writeObject(ThresholdValue);
		stream.writeObject(PastThresholdValue);
		stream.writeObject(LastSwitchDate);
		stream.writeObject(TotalAmount);
		stream.writeObject(Spread);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		Position=(Integer)stream.readObject();;
		SwitchSignal=(int[])stream.readObject();;
		ThresholdValue=(Double)stream.readObject();;
		PastThresholdValue=(Double)stream.readObject();;
		LastSwitchDate=(Date)stream.readObject();;
		TotalAmount=(Double)stream.readObject();;
		Spread=(Double)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	public double dividendYield(String Secu,Indicator Indic,Date CurDate)
 throws Exception {
    double YearDividend = Indic.getValue(CurrentDate) + Indic.getValue(LTIDate.getNewNYSEQuarter(CurDate, -1)) + Indic.getValue(LTIDate.getNewNYSEQuarter(CurDate, -2)) + Indic.getValue(LTIDate.getNewNYSEQuarter(CurDate, -3));
    double DividendYield = YearDividend / getSecurity(Secu).getCurrentPrice(CurrentDate);
    return DividendYield; 
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		String S = BuySecurity;
Indicator SPDividend = getIndicator("SPDIVIDEND");
SwitchSignal = new int[DelayDay];

CurrentPortfolio.sellAsset(curAsset, CurrentDate);
TotalAmount = CurrentPortfolio.getCash();

printToLog("The earliest available price date of the Security :"+getEarliestAvaliablePriceDate(getSecurity(S)));

if(S == "^GSPC")
    Spread = dividendYield(S, SPDividend,CurrentDate) * getLastYearPE(S,CurrentDate);
else
    Spread = getLastYearDividendYield(S,CurrentDate) * getLastYearPE(S,CurrentDate);

if(Threshold.charAt(0) == 'S')
{
    double SMA = Spread;;
    for(int i = 1; i<=Integer.parseInt(Threshold.substring(3)) -1; i++)
    {
        Date PastDate = LTIDate.getNewNYSETradingDay(CurrentDate,-i);
        if(S == "^GSPC")
            SMA += dividendYield(S, SPDividend,PastDate) *  getLastYearPE(S,PastDate);
        else
            SMA += getLastYearDividendYield(S,PastDate) * getLastYearPE(S,PastDate);
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
        double Spread3;
        Date PastDate3 = LTIDate.getNewNYSETradingDay(CurrentDate,-i-1);
        if(S == "^GSPC")
            Spread3 = dividendYield(S, SPDividend,PastDate3)  *  getLastYearPE(S,PastDate3);
        else        
            Spread3 = getLastYearDividendYield(S,PastDate3) * getLastYearPE(S,PastDate3);
        if(Threshold.charAt(0) == 'S')
        {
            double Spread1;
            double Spread2;
            Date PastDate1 = LTIDate.getNewNYSETradingDay(CurrentDate,-i);
            if(S == "^GSPC")
                Spread1 = dividendYield(S, SPDividend,PastDate1)  *  getLastYearPE(S,PastDate1);
            else        
                Spread1 = getLastYearDividendYield(S,PastDate1) * getLastYearPE(S,PastDate1);
            Date PastDate2 = LTIDate.getNewNYSETradingDay(PastDate1, 1-Integer.parseInt(Threshold.substring(3)));   
            if(S == "^GSPC")
                Spread2 = dividendYield(S, SPDividend,PastDate2)  *  getLastYearPE(S,PastDate2);
            else        
                Spread2 = getLastYearDividendYield(S,PastDate2) * getLastYearPE(S,PastDate2);
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
Indicator SPDividend = getIndicator("SPDIVIDEND");
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.isNYSETradingDay(CurrentDate)) {
		   if(S == "^GSPC")
    Spread = dividendYield(S, SPDividend,CurrentDate) * getLastYearPE(S,CurrentDate);
else
    Spread = getLastYearDividendYield(S,CurrentDate) * getLastYearPE(S,CurrentDate);

if(Threshold.charAt(0) == 'S')
{
    Date PastDate = LTIDate.getNewNYSETradingDay(CurrentDate, 1-Integer.parseInt(Threshold.substring(3)));
    double Spread1;
    if(S == "^GSPC")
        Spread1 = dividendYield(S, SPDividend,PastDate) * getLastYearPE(S,PastDate);
    else
        Spread1 = getLastYearDividendYield(S,PastDate) * getLastYearPE(S,PastDate);
    ThresholdValue += (Spread - Spread1)/Integer.parseInt(Threshold.substring(3)) ;
}


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