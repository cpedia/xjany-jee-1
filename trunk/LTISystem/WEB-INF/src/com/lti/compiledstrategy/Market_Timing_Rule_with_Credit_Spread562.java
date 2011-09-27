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
public class Market_Timing_Rule_with_Credit_Spread562 extends SimulateStrategy{
	public Market_Timing_Rule_with_Credit_Spread562(){
		super();
		StrategyID=562L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String HighCreditBond;
	public void setHighCreditBond(String HighCreditBond){
		this.HighCreditBond=HighCreditBond;
	}
	
	public String getHighCreditBond(){
		return this.HighCreditBond;
	}
	private String LowCreditBond;
	public void setLowCreditBond(String LowCreditBond){
		this.LowCreditBond=LowCreditBond;
	}
	
	public String getLowCreditBond(){
		return this.LowCreditBond;
	}
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
		HighCreditBond=(String)ParameterUtil.fetchParameter("String","HighCreditBond", "VWESX", parameters);
		LowCreditBond=(String)ParameterUtil.fetchParameter("String","LowCreditBond", "VWEHX", parameters);
		BuySecurity=(String)ParameterUtil.fetchParameter("String","BuySecurity", "^GSPC", parameters);
		Threshold=(Double)ParameterUtil.fetchParameter("double","Threshold", "30", parameters);
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
	public double getSpread(Security Secu1,Security Secu2,Date CurDate,int n)
 throws Exception {
    double Spread = (Secu1.getCurrentPrice(CurDate) / Secu1.getCurrentPrice(LTIDate.getNewNYSETradingDay(CurDate, -n))  -  Secu2.getCurrentPrice(CurDate) / Secu2.getCurrentPrice(LTIDate.getNewNYSETradingDay(CurDate, -n))) / n ;
    return Spread; 
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		Security S = getSecurity(BuySecurity);
Security HighCredit = getSecurity(HighCreditBond);
Security LowCredit = getSecurity(LowCreditBond);
SwitchSignal = new int[DelayDay];

CurrentPortfolio.sellAsset(curAsset, CurrentDate);
TotalAmount = CurrentPortfolio.getCash();

printToLog("The earliest available price date of the Security :"+getEarliestAvaliablePriceDate(S));
printToLog("The earliest available price date of the HighCredit :"+getEarliestAvaliablePriceDate(HighCredit));
printToLog("The earliest available price date of the LowCredit bond :"+getEarliestAvaliablePriceDate(LowCredit));

if(Threshold<=1)
    ThresholdValue=Threshold;
if(Threshold>1)
   ThresholdValue= getSpread(HighCredit, LowCredit, CurrentDate, (int)Threshold);

Spread= getSpread(HighCredit, LowCredit, CurrentDate, 1) ;


if(Spread>ThresholdValue)
    SwitchSignal[DelayDay-1]=1;
else
    SwitchSignal[DelayDay-1]=0;

int j=1;
if(DelayDay>=2)
{
    for(int i=2;i<=DelayDay;i++)
    {
        Date PastDate = LTIDate.getNewNYSETradingDay(CurrentDate,-i+1);
        Spread= getSpread(HighCredit, LowCredit, PastDate, 1) ;
        if(Threshold>1)
        {
            PastThresholdValue = getSpread(HighCredit, LowCredit, PastDate, (int)Threshold);
            if(Spread>PastThresholdValue)
                SwitchSignal[DelayDay-i]=1;
            else
                SwitchSignal[DelayDay-i]=0;
        }
        else
        {
            if(Spread>ThresholdValue)
                SwitchSignal[DelayDay-i]=1;
            else
                SwitchSignal[DelayDay-i]=0;
        } 
        if(SwitchSignal[DelayDay-i]!=SwitchSignal[DelayDay-1]) 
            j=0;
    }
}

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
    CurrentPortfolio.buy(curAsset, "CASH", TotalAmount, CurrentDate);
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
Security HighCredit = getSecurity(HighCreditBond);
Security LowCredit = getSecurity(LowCreditBond);
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.isNYSETradingDay(CurrentDate)) {
		   if(Threshold>1)
   ThresholdValue= getSpread(HighCredit, LowCredit, CurrentDate, (int)Threshold);

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

Spread= getSpread(HighCredit, LowCredit, CurrentDate,1);
/*printToLog("spread = " + Spread);*/

if(Spread>ThresholdValue)
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
        TotalAmount = CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);    
        CurrentPortfolio.sell(curAsset, "CASH", TotalAmount, CurrentDate);
        CurrentPortfolio.buy(curAsset, S.getName(), TotalAmount, CurrentDate);
   }
    else
    {
        TotalAmount = CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);         
        CurrentPortfolio.sell(curAsset, S.getName(),TotalAmount,CurrentDate);
        CurrentPortfolio.buy(curAsset, "CASH", TotalAmount, CurrentDate);
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