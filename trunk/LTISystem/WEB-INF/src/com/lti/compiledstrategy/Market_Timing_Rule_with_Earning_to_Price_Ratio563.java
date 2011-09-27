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
public class Market_Timing_Rule_with_Earning_to_Price_Ratio563 extends SimulateStrategy{
	public Market_Timing_Rule_with_Earning_to_Price_Ratio563(){
		super();
		StrategyID=563L;
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
	private double Threshold;
	public void setThreshold(double Threshold){
		this.Threshold=Threshold;
	}
	
	public double getThreshold(){
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
		Threshold=(Double)ParameterUtil.fetchParameter("double","Threshold", "0.05", parameters);
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
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(Position);
		stream.writeObject(SwitchSignal);
		stream.writeObject(ThresholdValue);
		stream.writeObject(PastThresholdValue);
		stream.writeObject(LastSwitchDate);
		stream.writeObject(TotalAmount);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		Position=(Integer)stream.readObject();;
		SwitchSignal=(int[])stream.readObject();;
		ThresholdValue=(Double)stream.readObject();;
		PastThresholdValue=(Double)stream.readObject();;
		LastSwitchDate=(Date)stream.readObject();;
		TotalAmount=(Double)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		String S = BuySecurity;
SwitchSignal = new int[DelayDay];

CurrentPortfolio.sellAsset(curAsset, CurrentDate);
TotalAmount = CurrentPortfolio.getCash();

if(!LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(S)),LTIDate.getNewNYSETradingDay(CurrentDate, -DelayDay)))
    printToLog("There is no available price at the beginning of the delay days. Please assign a later start date of the portfolio. ");

if(Threshold<=1)
    ThresholdValue= Threshold;
if(Threshold>1)
{
    double SMA = 1/getLastYearPE(S,CurrentDate);
    for(int i = 1; i<=Threshold-1; i++)
    {
        Date PastDate = LTIDate.getNewNYSETradingDay(CurrentDate,-i);
        SMA += 1/getLastYearPE(S,PastDate);
    } 
    ThresholdValue = SMA/Threshold;
}


if(1/getLastYearPE(S,CurrentDate)>ThresholdValue)
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
        double EP3 = 1/getLastYearPE(S,PastDate3);
        if(Threshold>1)
        {
            Date PastDate1 = LTIDate.getNewNYSETradingDay(CurrentDate,-i);
            double EP1 = 1/getLastYearPE(S,PastDate1);
            Date PastDate2 = LTIDate.getNewNYSETradingDay(PastDate1,1-(int)Threshold);   
            double EP2 = 1/getLastYearPE(S,PastDate2);
            PastThresholdValue +=  (EP2 -EP1)/Threshold;
            if(EP3>PastThresholdValue)
                SwitchSignal[DelayDay-i-2]=1;
            else
                SwitchSignal[DelayDay-i-2]=0;
        }
        else
        {
            if(EP3>ThresholdValue)
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
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.isNYSETradingDay(CurrentDate)) {
		   double EP = 1/getLastYearPE(S,CurrentDate);
if(Threshold>1)
{
    Date PastDate1 = LTIDate.getNewNYSETradingDay(CurrentDate, 1-(int)Threshold);
    double PastEP1 = 1/getLastYearPE(S,PastDate1);
    ThresholdValue += (EP - PastEP1)/Threshold;
    Date PastDate2 = LTIDate.getNewNYSETradingDay(CurrentDate, -(int)Threshold);    
    if(ThresholdValue  == Double.NaN && 1/getLastYearPE(S,PastDate2) == Double.NaN && PastEP1 != Double.NaN )
    {
        double SMA = EP;
        for(int i = 1; i<=Threshold-1; i++)
        {
            Date PastDate = LTIDate.getNewNYSETradingDay(CurrentDate,-i);
            SMA += 1/getLastYearPE(S,PastDate);
        } 
        ThresholdValue = SMA/Threshold;      
    }
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

if(EP>ThresholdValue)
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