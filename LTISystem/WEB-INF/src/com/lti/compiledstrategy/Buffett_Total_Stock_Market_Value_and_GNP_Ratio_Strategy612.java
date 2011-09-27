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
public class Buffett_Total_Stock_Market_Value_and_GNP_Ratio_Strategy612 extends SimulateStrategy{
	public Buffett_Total_Stock_Market_Value_and_GNP_Ratio_Strategy612(){
		super();
		StrategyID=612L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String AffectedBond;
	public void setAffectedBond(String AffectedBond){
		this.AffectedBond=AffectedBond;
	}
	
	public String getAffectedBond(){
		return this.AffectedBond;
	}
	private String NonAffectedBond;
	public void setNonAffectedBond(String NonAffectedBond){
		this.NonAffectedBond=NonAffectedBond;
	}
	
	public String getNonAffectedBond(){
		return this.NonAffectedBond;
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
	private String BuySecurity;
	public void setBuySecurity(String BuySecurity){
		this.BuySecurity=BuySecurity;
	}
	
	public String getBuySecurity(){
		return this.BuySecurity;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		AffectedBond=(String)ParameterUtil.fetchParameter("String","AffectedBond", "DGS5", parameters);
		NonAffectedBond=(String)ParameterUtil.fetchParameter("String","NonAffectedBond", "DFII5", parameters);
		Threshold=(String)ParameterUtil.fetchParameter("String","Threshold", "SMA30", parameters);
		WaitingDay=(Integer)ParameterUtil.fetchParameter("int","WaitingDay", "5", parameters);
		DelayDay=(Integer)ParameterUtil.fetchParameter("int","DelayDay", "5", parameters);
		BuySecurity=(String)ParameterUtil.fetchParameter("String","BuySecurity", "^GSPC", parameters);
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
double ExpectInflation;
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
		sb.append("ExpectInflation: ");
		sb.append(ExpectInflation);
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
		stream.writeObject(ExpectInflation);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		Position=(Integer)stream.readObject();;
		SwitchSignal=(int[])stream.readObject();;
		ThresholdValue=(Double)stream.readObject();;
		PastThresholdValue=(Double)stream.readObject();;
		LastSwitchDate=(Date)stream.readObject();;
		TotalAmount=(Double)stream.readObject();;
		ExpectInflation=(Double)stream.readObject();;
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
Indicator AB = getIndicator(AffectedBond);
Indicator NAB = getIndicator(NonAffectedBond);
SwitchSignal = new int[DelayDay];

CurrentPortfolio.sellAsset(curAsset, CurrentDate);
TotalAmount = CurrentPortfolio.getCash();

printToLog("The earliest available price date of the Security :"+getEarliestAvaliablePriceDate(S));

if(Threshold.charAt(0) == 'S')
{
    double ABSMA = AB.getValue(CurrentDate);
    double NABSMA = NAB.getValue(CurrentDate);
    for(int i = 1; i<=Integer.parseInt(Threshold.substring(3)) -1; i++)
    {
        Date PastDate = LTIDate.getNewNYSETradingDay(CurrentDate,-i);
        ABSMA += AB.getValue(PastDate);
        NABSMA += NAB.getValue(PastDate);
    } 
    ABSMA = ABSMA/Integer.parseInt(Threshold.substring(3)) ;
    NABSMA = NABSMA/Integer.parseInt(Threshold.substring(3)) ;
    ThresholdValue = (ABSMA - NABSMA)/100;
}
else
    ThresholdValue= Double.parseDouble(Threshold.substring(0));    

ExpectInflation= (AB.getValue(CurrentDate) - NAB.getValue(CurrentDate))/100;
if(ExpectInflation<ThresholdValue)
    SwitchSignal[DelayDay-1]=1;
else
    SwitchSignal[DelayDay-1]=0;

int j=1;
if(DelayDay>=2)
{
    PastThresholdValue = ThresholdValue;
    for(int i=0;i<=DelayDay -2;i++)
    {
        Date PastDate3 = LTIDate.getNewNYSETradingDay(CurrentDate,-i-1);
        double PastExpectInflation3 = (AB.getValue(PastDate3) - NAB.getValue(PastDate3))/100;  
        if(Threshold.charAt(0) == 'S')
        {
            Date PastDate1 = LTIDate.getNewNYSETradingDay(CurrentDate,-i);
            double PastExpectInflation1 = (AB.getValue(PastDate1) - NAB.getValue(PastDate1))/100; 
            Date PastDate2 = LTIDate.getNewNYSETradingDay(PastDate1, 1-Integer.parseInt(Threshold.substring(3)));   
            double PastExpectInflation2 = (AB.getValue(PastDate2) - NAB.getValue(PastDate2))/100;   
            PastThresholdValue +=  (PastExpectInflation2 - PastExpectInflation1)/Integer.parseInt(Threshold.substring(3));        

            if(PastExpectInflation3<PastThresholdValue)
                SwitchSignal[DelayDay-i-2]=1;
            else
                SwitchSignal[DelayDay-i-2]=0;
        }
        else
        {
            if(PastExpectInflation3<ThresholdValue)
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
    CurrentPortfolio.buy (curAsset, S.getName(), TotalAmount, CurrentDate);
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
	
		
		Security S = getSecurity(BuySecurity);
Indicator AB = getIndicator(AffectedBond);
Indicator NAB = getIndicator(NonAffectedBond);
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.isNYSETradingDay(CurrentDate)) {
		   ExpectInflation = (AB.getValue(CurrentDate) - NAB.getValue(CurrentDate))/100;
if(Threshold.charAt(0) == 'S')
{
    Date PastDate = LTIDate.getNewNYSETradingDay(CurrentDate, 1-Integer.parseInt(Threshold.substring(3)));
    double PastExpectInflation = (AB.getValue(PastDate) - NAB.getValue(PastDate))/100;
    ThresholdValue += (ExpectInflation - PastExpectInflation)/Integer.parseInt(Threshold.substring(3)) ;
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

if(ExpectInflation<ThresholdValue)
    SwitchSignal[DelayDay-1]=1;
else
    SwitchSignal[DelayDay-1]=0;

if(LTIDate.calculateInterval(LastSwitchDate, CurrentDate)>= WaitingDay )
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
        CurrentPortfolio.buy(curAsset, S.getName(), TotalAmount, CurrentDate);
   }
    else
    {
        TotalAmount = CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);         
        CurrentPortfolio.sell(curAsset, S.getName(),TotalAmount,CurrentDate);
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