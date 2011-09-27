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
public class Turtle_Strategy330 extends SimulateStrategy{
	public Turtle_Strategy330(){
		super();
		StrategyID=330L;
		StrategyClassID=14L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private boolean SystemType;
	public void setSystemType(boolean SystemType){
		this.SystemType=SystemType;
	}
	
	public boolean getSystemType(){
		return this.SystemType;
	}
	private String[] nameArray;
	public void setNameArray(String[] nameArray){
		this.nameArray=nameArray;
	}
	
	public String[] getNameArray(){
		return this.nameArray;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		SystemType=(Boolean)ParameterUtil.fetchParameter("boolean","SystemType", "true", parameters);
		nameArray=(String[])ParameterUtil.fetchParameter("String[]","nameArray", "DBB,DBP,DBA,FXA,FXB,FXC", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	//Variable:
//String[] nameArray={"DBB","DBP","DBA","FXA","FXB","FXC"};

int n=40;                      //Maximum number of securities
int []countUnit=new int[n];     //When we have estabished a long position and the price break out, we need to increase the trading unit.
int []incUnit=new int[n];
double TradeAmount;           //The total amount the user is allowed to trade, it varies with the gains and loses.
double RemainCash;
double []Amount=new double[n];       //Trade amount for each (future) market.
double []LastClose=new double[n];
double []CurrentPrice=new double[n];
double []EntryPrice=new double[n];
double []CurrentHigh=new double[n];
double []CurrentLow=new double[n];
double []Last10DaysHigh=new double[n];
double []Last10DaysLow=new double[n];
double []Last20DaysHigh=new double[n];
double []Last20DaysLow=new double[n];
double []Last55DaysHigh=new double[n];
double []Last55DaysLow=new double[n];
double []StopPrice=new double[n];
double []Volatility=new double[n];
double []EntryVolatility=new double[n];
double []Unit=new double[n];
boolean yearBegin;
boolean []Established=new boolean[n];
boolean []Position=new boolean[n];     //"true" for a long position; while "false" for a short position.
boolean []LastEarned=new boolean[n];   //If we earned the profit in the latest transaction, it is set "true", else "false".
int []PositionLimit=new int[n];        //The position limit for a certain market.
String[] Fund=new String[n];
Date LastDate;
Date StartDate;
Date Start10Days;
Date Start20Days;
Date Start55Days;
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
	//Functions:
//function1: find the maximum.
public double findMax(double a,double b, double c) throws Exception {
  double temp;
  if(a>=b){
    if(a>=c)
    temp=a;
    else
    temp=c;
  }
  else{
    if(b>=c)
    temp=b;
    else
    temp=c;
  }
  return temp;
}

//function2: calculate the volatility
public void calculateVolatility() throws Exception {
  double []TR=new double[n];
  for(int j=0;j<n;j++){
    LastClose[j]=getSecurity(Fund[j]).getCurrentPrice(LastDate);
    CurrentHigh[j]=getSecurity(Fund[j]).getHigh(CurrentDate);
    CurrentLow[j]=getSecurity(Fund[j]).getLow(CurrentDate);
    TR[j]=findMax(CurrentHigh[j]-CurrentLow[j],CurrentHigh[j]-LastClose[j],LastClose[j]-CurrentLow[j]);
    Volatility[j]=(19*Volatility[j]+TR[j])/20;
  }
}

//function3: calculate the unit value.
public void calculateUnit() throws Exception {
  for(int k=0;k<n;k++)
  Unit[k]=(0.01*TradeAmount)/Volatility[k];
}

//function4: Adjust the user amount. It operates at the beginning of each year if necessary. 
public void adjustAccount() throws Exception {
  double []sellAmount=new double[n];
  double withDrawCash;
  for(int i=0;i<n;i++){
    Amount[i]=CurrentPortfolio.getSecurityAmount(curAsset,nameArray[i],CurrentDate);
    sellAmount[i]=0.2*Amount[i];
    CurrentPortfolio.sell(curAsset,nameArray[i],sellAmount[i],CurrentDate);
    Amount[i]=0.8*Amount[i];
  }
  RemainCash=0.8*RemainCash;
  withDrawCash=0.2*TradeAmount;
  TradeAmount=0.8*TradeAmount;
  CurrentPortfolio.withdraw(withDrawCash,CurrentDate);
}

//fuction5: Estabish the long position.
public void longPosition(boolean SysType,int i) throws Exception {
  //double []incAmount=new double[n];
  if(SysType){
    if(CurrentPrice[i]>Last20DaysHigh[i]){
      if(!LastEarned[i]){               //we have not earned profit at the last trade.
        if(RemainCash>Unit[i]){
          if(Unit[i]>=CurrentPrice[i])
          Amount[i]=Unit[i];
          else
          Amount[i]=CurrentPrice[i];
          RemainCash=RemainCash-Amount[i];
          CurrentPortfolio.buy(curAsset,nameArray[i], Amount[i], CurrentDate);
          EntryPrice[i]=CurrentPrice[i];
          EntryVolatility[i]=Volatility[i];
          Established[i]=true;
          Position[i]=true;
        }
      }
      else
      //LastEarned[i]=false;
      if(CurrentPrice[i]>Last55DaysHigh[i]){
        if(RemainCash>Unit[i]){
          if(Unit[i]>=CurrentPrice[i])
          Amount[i]=Unit[i];
          else
          Amount[i]=CurrentPrice[i];
          RemainCash=RemainCash-Amount[i];
          CurrentPortfolio.buy (curAsset,nameArray[i], Amount[i], CurrentDate);
          EntryPrice[i]=CurrentPrice[i];
          EntryVolatility[i]=Volatility[i];
          Established[i]=true;
          Position[i]=true;
        }
      }
    }
  }
  else{
    if(CurrentPrice[i]>Last55DaysHigh[i]){
      if(RemainCash>Unit[i]){
        if(Unit[i]>=CurrentPrice[i])
        Amount[i]=Unit[i];
        else
        Amount[i]=CurrentPrice[i];
        RemainCash=RemainCash-Amount[i];
        CurrentPortfolio.buy (curAsset,nameArray[i], Amount[i], CurrentDate);
        EntryPrice[i]=CurrentPrice[i];
        EntryVolatility[i]=Volatility[i];
        Established[i]=true;
        Position[i]=true;
      }
    }
  }
}
//function6: Establish the short position.
public void shortPosition(boolean SysType, int i) throws Exception {
  //double []incAmount=new double[n];
  if(SysType){
    if(CurrentPrice[i]<Last20DaysLow[i]){
      if(!LastEarned[i]){               //we have not earned profit at the last trade.
        if(Unit[i]>=CurrentPrice[i])
        Amount[i]=-Unit[i];
        else
        Amount[i]=-CurrentPrice[i];
        RemainCash=RemainCash-Amount[i];
        CurrentPortfolio.shortSell(curAsset,nameArray[i],-Amount[i], CurrentDate);
        EntryPrice[i]=CurrentPrice[i];
        EntryVolatility[i]=Volatility[i];
        Established[i]=true;
        Position[i]=false;
      }
      else{
        if(CurrentPrice[i]<Last55DaysLow[i]){
          if(Unit[i]>=CurrentPrice[i])
          Amount[i]=-Unit[i];
          else
          Amount[i]=-CurrentPrice[i];
          RemainCash=RemainCash-Amount[i];
          CurrentPortfolio.shortSell(curAsset,nameArray[i],-Amount[i], CurrentDate);
          EntryPrice[i]=CurrentPrice[i];
          EntryVolatility[i]=Volatility[i];
          Established[i]=true;
          Position[i]=false;
        }
      }
    }
  }
  else{
    if(CurrentPrice[i]<Last55DaysLow[i]){
      if(Unit[i]>=CurrentPrice[i])
      Amount[i]=-Unit[i];
      else
      Amount[i]=-CurrentPrice[i];
      RemainCash=RemainCash-Amount[i];
      CurrentPortfolio.shortSell(curAsset,nameArray[i],-Amount[i], CurrentDate);
      EntryPrice[i]=CurrentPrice[i];
      EntryVolatility[i]=Volatility[i];
      Established[i]=true;
      Position[i]=false;
    }
  }
}

//function7: Conbination of the long and short position.
public void establishPosition(boolean SysType,int j) throws Exception {
    if(CurrentPrice[j]>Last20DaysHigh[j]||CurrentPrice[j]>Last55DaysHigh[j])
      longPosition(SysType,j);
    else if(CurrentPrice[j]<Last20DaysLow[j]||CurrentPrice[j]<Last55DaysLow[j])
      shortPosition(SysType,j);
}


public void longAddUnits(boolean SysType,int i) throws Exception {
  int j=0;
  int allowGap=PositionLimit[i]-countUnit[i];
  double incAmount;
  double N=EntryVolatility[i];
  double TriggerLowPrice=0.0;
  double TriggerHighPrice=0.0;
  double UpLimitPrice=EntryPrice[i]+(PositionLimit[i]-1)*N/2;
  for(j=0;j<PositionLimit[i];j++){
    TriggerLowPrice=EntryPrice[i]+(j-1)*N/2;
    TriggerHighPrice=EntryPrice[i]+j*N/2;
    if(CurrentPrice[i]<=EntryPrice[i])
    break;
    else if(CurrentPrice[i]>TriggerLowPrice&&CurrentPrice[i]<=TriggerHighPrice)
    break;
  }
  if(CurrentPrice[i]>UpLimitPrice){
    j=PositionLimit[i];
  }
  if(j>=allowGap)                     //Check out how many Units are needed to increase;
  incUnit[i]=allowGap;
  else
  incUnit[i]=j;             
  if(incUnit[i]>0){
    incAmount=incUnit[i]*Unit[i];
    if(incAmount>0&&RemainCash>incAmount){
      Amount[i]=Amount[i]+incAmount;
      RemainCash=RemainCash-incAmount;
      CurrentPortfolio.buy (curAsset,nameArray[i],incAmount, CurrentDate);
      countUnit[i]=countUnit[i]+incUnit[i];
      EntryPrice[i]=CurrentPrice[i];
      EntryVolatility[i]=Volatility[i];
      printToLog("Adding Long Position");
    }
  }
}

//function9:Calculate the increasing units for short position.
public void shortAddUnits(boolean SysType,int i) throws Exception {
  int j=0;
  int addUnit;
  int allowGap=PositionLimit[i]-countUnit[i];
  double incAmount;
  double N=EntryVolatility[i];
  double TriggerLowPrice=0.0;
  double TriggerHighPrice=0.0;
  double LowLimitPrice=EntryPrice[i]-(PositionLimit[i]-1)*N/2;
  for(j=0;j<PositionLimit[i];j++){
    TriggerLowPrice=EntryPrice[i]-j*N/2;
    TriggerHighPrice=EntryPrice[i]-(j-1)*N/2;
    if(CurrentPrice[i]>=EntryPrice[i])
    break;
    else if(CurrentPrice[i]>=TriggerLowPrice&&CurrentPrice[i]<TriggerHighPrice)
    break;
  }
  if(CurrentPrice[i]<LowLimitPrice){
    j=PositionLimit[i];
  }
  if(j>=allowGap)                     //Check out how many Units are needed to increase;
  incUnit[i]=allowGap;
  else
  incUnit[i]=j;  
  if(incUnit[i]>0){
    incAmount=incUnit[i]*Unit[i];
    if(incAmount>0){
      Amount[i]=Amount[i]-incAmount;
      RemainCash=RemainCash+incAmount;
      CurrentPortfolio.shortSell(curAsset,nameArray[i],incAmount, CurrentDate);
      countUnit[i]=countUnit[i]+incUnit[i];
      EntryPrice[i]=CurrentPrice[i];
      EntryVolatility[i]=Volatility[i];
      printToLog("Adding Short Position Unit");
    }
  }
}

//funtion10: Deal with the routine check.
public void routineCheck(boolean SysType) throws Exception {
  for(int i=0;i<n;i++){
    if(!Established[i])
    establishPosition(SysType,i);
    else {
      if(Position[i])
      longAddUnits(SysType,i);
      else
      shortAddUnits(SysType,i);
    }
  }
}
//funtion11: caculate the Stop Price.
public void longStop(boolean SysType,int t) throws Exception {
  if(incUnit[t]>=0){
    if(incUnit[t]!=PositionLimit[t]){
      StopPrice[t]=EntryPrice[t]-2*EntryVolatility[t];
      for(int j=1;j<=incUnit[t];j++)
      StopPrice[t]=StopPrice[t]+0.5*EntryVolatility[t];
    }
    else{
      double UpPrice=EntryPrice[t]+(incUnit[t]-1)*EntryVolatility[t]/2;
      double Stop1=EntryPrice[t]-2*EntryVolatility[t]+(incUnit[t]-1)*EntryVolatility[t]/2;
      StopPrice[t]=Stop1+(CurrentPrice[t]-UpPrice);
    }  
  }
}


public void shortStop(boolean SysType,int t) throws Exception {
  if(incUnit[t]>=0){
    if(incUnit[t]!=PositionLimit[t]){
      StopPrice[t]=EntryPrice[t]+2*EntryVolatility[t];
      for(int j=1;j<=incUnit[t];j++)
      StopPrice[t]=StopPrice[t]-0.5*EntryVolatility[t];
    }
    else{
      double UpPrice=EntryPrice[t]-(incUnit[t]-1)*EntryVolatility[t]/2;
      double Stop1=EntryPrice[t]+2*EntryVolatility[t]-(incUnit[t]-1)*EntryVolatility[t]/2;
      StopPrice[t]=Stop1+(CurrentPrice[t]-UpPrice);
    }  
  }
}


public void Stop(boolean SysType,int t) throws Exception {
  if(Position[t])
  longStop(SysType,t);
  else
  shortStop(SysType,t);
}
//function9: Exit the market.
public void Exit(boolean SysType,int t) throws Exception {
  double SecAmount;
  if(Established[t]){  
    if(SysType){                  //If we are in system 1.
      if(Position[t]){               //We are in a long position.
        if(CurrentPrice[t]<Last10DaysLow[t]||CurrentPrice[t]<StopPrice[t]){
          SecAmount=CurrentPortfolio.getSecurityAmount(curAsset,nameArray[t],CurrentDate);
          CurrentPortfolio.sell(curAsset,nameArray[t],SecAmount,CurrentDate);
          RemainCash=RemainCash+SecAmount;
          if(SecAmount>Amount[t])     //Check that whether we have earned profit at the last trade.
          LastEarned[t]=true;
          else 
          LastEarned[t]=false;
          Amount[t]=0;
          incUnit[t]=0;
          countUnit[t]=0;
          Established[t]=false;
          Position[t]=false;
          if(CurrentPrice[t]<Last10DaysLow[t])
          printToLog(CurrentDate+"10 days' low");
          else
          printToLog(CurrentDate+"Stop Price comes out");
        }
      }
      else{                      //We are in a short position.
        if(CurrentPrice[t]>Last10DaysHigh[t]||CurrentPrice[t]>StopPrice[t]){
          Date dd = LTIDate.getDate(2008,2,13);
          if(LTIDate.equals(dd,CurrentDate))
          {
            int abc = 1;
            abc++;
          }
          SecAmount=-CurrentPortfolio.getSecurityAmount(curAsset,nameArray[t],CurrentDate);
          if(RemainCash>SecAmount){
            CurrentPortfolio.buy(curAsset,nameArray[t],SecAmount,CurrentDate);
            RemainCash=RemainCash-SecAmount;
            printToLog("shortSell exit  "+SecAmount+"VS"+Amount[t]);
            if(SecAmount>Amount[t])     //Check that whether we have earned profit at the last trade.
            LastEarned[t]=true;
            else 
            LastEarned[t]=false;
            Amount[t]=0;
            incUnit[t]=0;
            countUnit[t]=0;
            Established[t]=false;
            //printToLog("shortSell exit  "+SecAmount);
          }
        }
      }
    }
    else{                            //If we are in system 2.
      if(Position[t]){               //We are in a long position.
        if(CurrentPrice[t]<Last20DaysLow[t]||CurrentPrice[t]<StopPrice[t]){
          SecAmount=CurrentPortfolio.getSecurityAmount(curAsset,nameArray[t],CurrentDate);
          CurrentPortfolio.sell(curAsset,nameArray[t],SecAmount,CurrentDate);
          RemainCash=RemainCash+SecAmount;
          if(SecAmount>Amount[t])     //Check that whether we have earned profit at the last trade.
          LastEarned[t]=true;
          else 
          LastEarned[t]=false;
          Amount[t]=0;
          incUnit[t]=0;
          countUnit[t]=0;
          Established[t]=false;
          Position[t]=false;
          if(CurrentPrice[t]<Last10DaysLow[t])
          printToLog(CurrentDate+"20 days' low");
          else
          printToLog(CurrentDate+"Stop Price comes out");
        }
      }
      else{                      //We are in a short position.
        if(CurrentPrice[t]>Last20DaysHigh[t]||CurrentPrice[t]>StopPrice[t]){
          SecAmount=-CurrentPortfolio.getSecurityAmount(curAsset,nameArray[t],CurrentDate);
          if(RemainCash>SecAmount){
            CurrentPortfolio.buy(curAsset,nameArray[t],SecAmount,CurrentDate);
            RemainCash=RemainCash-SecAmount;
            if(SecAmount>Amount[t])     //Check that whether we have earned profit at the last trade.
            LastEarned[t]=true;
            else 
            LastEarned[t]=false;
            Amount[t]=0;
            incUnit[t]=0;
            countUnit[t]=0;
            Established[t]=false;
            printToLog("shortSell exit  "+SecAmount);
          }
        }
      }
    }
  }
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		//Initial:
n = nameArray.length; // reset n to be the real number of funds
TradeAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
RemainCash=TradeAmount;
CurrentPortfolio.sellAsset(curAsset, CurrentDate);
for(int i=0;i<n;i++){
  Fund[i]=nameArray[i];
  CurrentPrice[i]=getSecurity(Fund[i]).getCurrentPrice(CurrentDate);
  Established[i]=false;
  LastEarned[i]=false;
  countUnit[i]=0;
  incUnit[i]=0;
  Position[i]=true;
  PositionLimit[i]=4;
}
double []initTR=new double[20];
double []sumTR=new double [n];
for(int i=0;i<n;i++){
  sumTR[i]=0.0;
}
for(int j=0;j<n;j++){                          //Caculating the initial Volotility of 20 days moving average TR.
  StartDate=LTIDate.getNewNYSETradingDay(CurrentDate,-19);
  for(int k=0;k<20;k++){
    LastClose[j]=getSecurity(Fund[j]).getCurrentPrice(StartDate);
    CurrentHigh[j]=getSecurity(Fund[j]).getHigh(StartDate);
    CurrentLow[j]=getSecurity(Fund[j]).getLow(StartDate);
    initTR[k]=findMax(CurrentHigh[j]-CurrentLow[j],CurrentHigh[j]-LastClose[j],LastClose[j]-CurrentLow[j]);
    StartDate=LTIDate.getNewNYSETradingDay(StartDate,1);;
  }
  for(int l=0;l<20;l++){
    sumTR[j]=sumTR[j]+initTR[l];
  }
  Volatility[j]=sumTR[j]/20;
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
	
		
		
LastDate=LTIDate.getNewNYSETradingDay(CurrentDate,-1);
Start10Days=LTIDate.getNewNYSETradingDay(CurrentDate,-10);
Start20Days=LTIDate.getNewNYSETradingDay(CurrentDate,-20);
Start55Days=LTIDate.getNewNYSETradingDay(CurrentDate,-55);
for(int i=0;i<n;i++)
{
printToLog("dealing the fund : " + Fund[i]);
CurrentPrice[i]=getSecurity(Fund[i]).getCurrentPrice(CurrentDate);
}
for(int k=0;k<n;k++){
	Last10DaysHigh[k]=getSecurity(Fund[k]).getHighestPrice(Start10Days,LastDate);
	Last10DaysLow[k]=getSecurity(Fund[k]).getLowestPrice(Start10Days,LastDate);
	Last20DaysHigh[k]=getSecurity(Fund[k]).getHighestPrice(Start20Days,LastDate);
	Last20DaysLow[k]=getSecurity(Fund[k]).getLowestPrice(Start20Days,LastDate);
	Last55DaysHigh[k]=getSecurity(Fund[k]).getHighestPrice(Start55Days,LastDate);
	Last55DaysLow[k]=getSecurity(Fund[k]).getLowestPrice(Start55Days,LastDate);
}
calculateVolatility();
calculateUnit();
routineCheck(SystemType);
for(int j=0;j<n;j++){
	Stop(SystemType,j);
	Exit(SystemType,j);
}
yearBegin=LTIDate.isBeginning(TimeUnit.YEARLY,CurrentDate);

		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (yearBegin) {
		   
double remainAssetAmount;
double remainAmount;
remainAssetAmount=CurrentPortfolio.getAssetAmount(curAsset,CurrentDate);
remainAmount=remainAssetAmount+RemainCash;
if(remainAmount<0.9*TradeAmount){
	adjustAccount();
	printToLog("Adjusting Size sucessfully");
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