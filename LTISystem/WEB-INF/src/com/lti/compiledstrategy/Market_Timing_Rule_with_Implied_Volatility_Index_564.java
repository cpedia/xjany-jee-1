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
public class Market_Timing_Rule_with_Implied_Volatility_Index_564 extends SimulateStrategy{
	public Market_Timing_Rule_with_Implied_Volatility_Index_564(){
		super();
		StrategyID=564L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
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
	private String BuySecurity;
	public void setBuySecurity(String BuySecurity){
		this.BuySecurity=BuySecurity;
	}
	
	public String getBuySecurity(){
		return this.BuySecurity;
	}
	private int SwitchRule;
	public void setSwitchRule(int SwitchRule){
		this.SwitchRule=SwitchRule;
	}
	
	public int getSwitchRule(){
		return this.SwitchRule;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		Threshold=(String)ParameterUtil.fetchParameter("String","Threshold", "SMA30", parameters);
		WaitingDay=(Integer)ParameterUtil.fetchParameter("int","WaitingDay", "5", parameters);
		DelayDay=(Integer)ParameterUtil.fetchParameter("int","DelayDay", "5", parameters);
		BuySecurity=(String)ParameterUtil.fetchParameter("String","BuySecurity", "^GSPC", parameters);
		SwitchRule=(Integer)ParameterUtil.fetchParameter("int","SwitchRule", "0", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	int Position;
int[] SwitchSignal;
double ThresholdValue;
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
		stream.writeObject(LastSwitchDate);
		stream.writeObject(TotalAmount);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		Position=(Integer)stream.readObject();;
		SwitchSignal=(int[])stream.readObject();;
		ThresholdValue=(Double)stream.readObject();;
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
		Security S = getSecurity(BuySecurity);
Security VIX = getSecurity("^VIX");
SwitchSignal = new int[DelayDay];

CurrentPortfolio.sellAsset(curAsset, CurrentDate);
TotalAmount = CurrentPortfolio.getCash();

if(!LTIDate.before(getEarliestAvaliablePriceDate(S),LTIDate.getNewNYSETradingDay(CurrentDate, -DelayDay)))
    printToLog("There is no available price at the beginning of the delay days. Please assign a later start date of the portfolio. ");

if(Threshold.charAt(0) == 'S')
   ThresholdValue=VIX.getSMA(CurrentDate, Integer.parseInt(Threshold.substring(3)) ,TimeUnit.DAILY);
else
    ThresholdValue= Double.parseDouble(Threshold.substring(0));    


if(VIX.getCurrentPrice(CurrentDate)<ThresholdValue)
    SwitchSignal[DelayDay-1]=1;
else
    SwitchSignal[DelayDay-1]=0;

int j=1;
if(DelayDay>=2)
{
    for(int i=2;i<=DelayDay;i++)
    {
        Date PastDate = LTIDate.getNewNYSETradingDay(CurrentDate,-i+1);
        if(Threshold.charAt(0) == 'S')
        {
            if(VIX.getCurrentPrice(PastDate)<VIX.getSMA(PastDate, Integer.parseInt(Threshold.substring(3)),TimeUnit.DAILY))
                SwitchSignal[DelayDay-i]=1;
            else
                SwitchSignal[DelayDay-i]=0;
        }
        else
        {
            if(VIX.getCurrentPrice(PastDate)<ThresholdValue)
                SwitchSignal[DelayDay-i]=1;
            else
                SwitchSignal[DelayDay-i]=0;
        } 
        if(SwitchSignal[DelayDay-i]!=SwitchSignal[DelayDay-1]) 
            j=0;
    }
}

if(SwitchRule == 0)
{
    if(SwitchSignal[DelayDay-1]==1 && j==1)
    {
        Position = 1;
        CurrentPortfolio.buy (curAsset, S.getName(), TotalAmount, CurrentDate);
        LastSwitchDate = CurrentDate;   
    }
    else
    {
        Position = 0;
        LastSwitchDate = CurrentDate;  
    }
}
else
{
    if(SwitchSignal[DelayDay-1]==0 && j==1)
    {
        Position = 0;
        CurrentPortfolio.buy (curAsset, S.getName(), TotalAmount, CurrentDate);
        LastSwitchDate = CurrentDate;   
    }
    else
    {
        Position = 1;
        LastSwitchDate = CurrentDate;  
    }
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
	
		
			    Security S = getSecurity(BuySecurity);
	    Security VIX = getSecurity("^VIX");	
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.isNYSETradingDay(CurrentDate)) {
		   if(Threshold.charAt(0) == 'S')
   ThresholdValue=VIX.getSMA(CurrentDate, Integer.parseInt(Threshold.substring(3)) ,TimeUnit.DAILY);

printToLog("VIX = "+VIX.getCurrentPrice(CurrentDate)+"   Threshold  =  "+ThresholdValue);

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

if(VIX.getCurrentPrice(CurrentDate)<ThresholdValue)
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
    if(SwitchRule == 0)
    {
        if(Position == 1)
        {
            TotalAmount = CurrentPortfolio.getCash();
            CurrentPortfolio.buy(curAsset, S.getName(), TotalAmount, CurrentDate);
       }
        else
        {
            TotalAmount = CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);         
            CurrentPortfolio.sell(curAsset, S.getName(), TotalAmount,CurrentDate);
        }
    }
    else
    {
        if(Position == 0)
        {
            TotalAmount = CurrentPortfolio.getCash();
            CurrentPortfolio.buy(curAsset, S.getName(),  TotalAmount, CurrentDate);
       }
        else
        {
            TotalAmount = CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);         
            CurrentPortfolio.sell(curAsset, S.getName(), TotalAmount,CurrentDate);
        }
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