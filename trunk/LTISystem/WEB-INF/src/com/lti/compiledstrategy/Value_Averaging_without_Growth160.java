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
public class Value_Averaging_without_Growth160 extends SimulateStrategy{
	public Value_Averaging_without_Growth160(){
		super();
		StrategyID=160L;
		StrategyClassID=5L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private double targetvalue;
	public void setTargetvalue(double targetvalue){
		this.targetvalue=targetvalue;
	}
	
	public double getTargetvalue(){
		return this.targetvalue;
	}
	private int totalPeriod;
	public void setTotalPeriod(int totalPeriod){
		this.totalPeriod=totalPeriod;
	}
	
	public int getTotalPeriod(){
		return this.totalPeriod;
	}
	private int Interval;
	public void setInterval(int Interval){
		this.Interval=Interval;
	}
	
	public int getInterval(){
		return this.Interval;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		targetvalue=(Double)ParameterUtil.fetchParameter("double","targetvalue", "130000", parameters);
		totalPeriod=(Integer)ParameterUtil.fetchParameter("int","totalPeriod", "60", parameters);
		Interval=(Integer)ParameterUtil.fetchParameter("int","Interval", "1", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double value;
int i;
double accumulativeAmount;
double initialAmount;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("value: ");
		sb.append(value);
		sb.append("\n");
		sb.append("i: ");
		sb.append(i);
		sb.append("\n");
		sb.append("accumulativeAmount: ");
		sb.append(accumulativeAmount);
		sb.append("\n");
		sb.append("initialAmount: ");
		sb.append(initialAmount);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(value);
		stream.writeObject(i);
		stream.writeObject(accumulativeAmount);
		stream.writeObject(initialAmount);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		value=(Double)stream.readObject();;
		i=(Integer)stream.readObject();;
		accumulativeAmount=(Double)stream.readObject();;
		initialAmount=(Double)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		initialAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
value=(targetvalue-initialAmount)/totalPeriod;
printToLog("value"+value);
i=1;
accumulativeAmount=0;
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
	
		
		boolean con1=(Interval==0) && (LTIDate.isLastNYSETradingDayOfMonth(CurrentDate));
boolean con2=(Interval==1) && (LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate));
boolean con3=(Interval==2) && (LTIDate.isLastNYSETradingDayOfYear(CurrentDate));
boolean con4=(i<=totalPeriod);
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if ((con1||con2||con3)&&con4) {
		   int a;
int b;

double withdrawalAmount;
double adjustmentAmount;

adjustmentAmount=initialAmount+value*i-CurrentPortfolio.getTotalAmount(CurrentDate);
//the amount adjusted at the end of every period to reach the target value
accumulativeAmount=accumulativeAmount+adjustmentAmount;
//the total adjusted amount 

printToLog("adjustmentAmount:"+adjustmentAmount);
printToLog("accumulated amount contributed:"+accumulativeAmount);
printToLog("valuepath:"+(initialAmount+value*i));

if (adjustmentAmount>=0) {
          //add in more cash and buy in securities if the adjustment amount is positive.
          CurrentPortfolio.deposit(adjustmentAmount, CurrentDate);
          CurrentPortfolio.balance(CurrentDate);
}else
{
        Asset currentasset;
	Iterator<Asset> iter=CurrentPortfolio.getAssets().iterator();
	while(iter.hasNext()){
	currentasset=iter.next();
	HoldingItem si;
	Iterator<HoldingItem> iter_1=currentasset.getHoldingItems().iterator();
	while(iter_1.hasNext()){
             double secAmount;
	      si=iter_1.next();
	      Security currentsecurity=getSecurity(si.getSecurityID());   
              printToLog("Current Asset = "+currentasset.getName());
              secAmount=
CurrentPortfolio.getSecurityAmount(currentasset.getName(),currentsecurity.getName(),CurrentDate);
              withdrawalAmount=adjustmentAmount*secAmount/
                 CurrentPortfolio.getTotalAmount(CurrentDate); 

             printToLog("SecurityAmount"+secAmount);
             printToLog("TotalAmount"+CurrentPortfolio.getTotalAmount(CurrentDate));
             printToLog("AssetAmount"+CurrentPortfolio.getAssetAmount(currentasset.getName(),CurrentDate));
             printToLog("AssetCollectionAmount"+
                CurrentPortfolio.getAssetCollectionAmount(CurrentDate));
             printToLog("Cash"+CurrentPortfolio.getCash());
             printToLog(currentsecurity.getName()+withdrawalAmount);
             CurrentPortfolio.sell(currentasset.getName(),currentsecurity.getName(),
                   -withdrawalAmount,CurrentDate);
	}
	}
        /* round the number */
        int iWithdrawAmount = (int)(-adjustmentAmount);
        CurrentPortfolio.withdraw((double)iWithdrawAmount, CurrentDate);
}
i++;
//if the adjustment amount is negative, sell securities to get back to value path.

if (i== totalPeriod+1)
printToLog("Cash flow strategy is finished");

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