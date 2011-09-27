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
public class Dynamic_Value_Averaging171 extends SimulateStrategy{
	public Dynamic_Value_Averaging171(){
		super();
		StrategyID=171L;
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
	private double totalPeriod;
	public void setTotalPeriod(double totalPeriod){
		this.totalPeriod=totalPeriod;
	}
	
	public double getTotalPeriod(){
		return this.totalPeriod;
	}
	private int Interval;
	public void setInterval(int Interval){
		this.Interval=Interval;
	}
	
	public int getInterval(){
		return this.Interval;
	}
	private double cGrowthRate;
	public void setCGrowthRate(double cGrowthRate){
		this.cGrowthRate=cGrowthRate;
	}
	
	public double getCGrowthRate(){
		return this.cGrowthRate;
	}
	private double vGrowthRate;
	public void setVGrowthRate(double vGrowthRate){
		this.vGrowthRate=vGrowthRate;
	}
	
	public double getVGrowthRate(){
		return this.vGrowthRate;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		targetvalue=(Double)ParameterUtil.fetchParameter("double","targetvalue", "100000", parameters);
		totalPeriod=(Double)ParameterUtil.fetchParameter("double","totalPeriod", "60", parameters);
		Interval=(Integer)ParameterUtil.fetchParameter("int","Interval", "1", parameters);
		cGrowthRate=(Double)ParameterUtil.fetchParameter("double","cGrowthRate", "0.01", parameters);
		vGrowthRate=(Double)ParameterUtil.fetchParameter("double","vGrowthRate", "0.005", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double value;
int i;
double accumulativeAmount;
double finalTimeIndex;
double currentTimeIndex;
double c;

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
		sb.append("finalTimeIndex: ");
		sb.append(finalTimeIndex);
		sb.append("\n");
		sb.append("currentTimeIndex: ");
		sb.append(currentTimeIndex);
		sb.append("\n");
		sb.append("c: ");
		sb.append(c);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(value);
		stream.writeObject(i);
		stream.writeObject(accumulativeAmount);
		stream.writeObject(finalTimeIndex);
		stream.writeObject(currentTimeIndex);
		stream.writeObject(c);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		value=(Double)stream.readObject();;
		i=(Integer)stream.readObject();;
		accumulativeAmount=(Double)stream.readObject();;
		finalTimeIndex=(Double)stream.readObject();;
		currentTimeIndex=(Double)stream.readObject();;
		c=(Double)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		finalTimeIndex=totalPeriod/(1-Math.pow((1+cGrowthRate/2+vGrowthRate/2),totalPeriod)* CurrentPortfolio.getTotalAmount(CurrentDate)/targetvalue);
//calculate the final time index
currentTimeIndex=finalTimeIndex-totalPeriod;
//Calculate current time index
c=targetvalue/(finalTimeIndex*Math.pow((1+cGrowthRate/2+vGrowthRate/2),finalTimeIndex));
//calculate the initial contribution should investment begin from the hypothetical first period
value=c*currentTimeIndex*Math.pow((1+cGrowthRate/2+vGrowthRate/2),currentTimeIndex);
//calculate the initial value path
accumulativeAmount=0;
i=1;

printToLog("finalTimeIndex:"+finalTimeIndex);
printToLog("currentTimeIndex:"+currentTimeIndex);
printToLog("c:"+c);
printToLog("value:"+value);
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
		   currentTimeIndex=currentTimeIndex+1;
////calculate the time index at the end of each period
value=c*currentTimeIndex*Math.pow((1+cGrowthRate/2+vGrowthRate/2),currentTimeIndex);
//calculate the value path at the end of each period

printToLog("currentTimeIndex:"+currentTimeIndex);
printToLog("value:"+value);


int a;
int b;
double withdrawalAmount;
double adjustmentAmount;

adjustmentAmount=value-CurrentPortfolio.getTotalAmount(CurrentDate);
//the amount adjusted at the end of every period to reach the target value
accumulativeAmount=accumulativeAmount+adjustmentAmount;
//the total adjusted amount 

printToLog("adjustmentAmount:"+adjustmentAmount);
printToLog("accumulativeAmount:"+accumulativeAmount);

if (adjustmentAmount>=0)
{CurrentPortfolio.deposit(adjustmentAmount,CurrentDate);
CurrentPortfolio.balance(CurrentDate);}
//add in more cash and buy in securities if the adjustment amount is positive.

else
{Asset currentasset;
			Iterator<Asset> iter=CurrentPortfolio.getAssets().iterator();
			while(iter.hasNext())
{
				currentasset=iter.next();
				HoldingItem si;
				Iterator<HoldingItem> iter_1=currentasset.getHoldingItems().iterator();
				while(iter_1.hasNext()){
					si=iter_1.next();
	Security currentsecurity=getSecurity(si.getSecurityID());
double securityamount;
securityamount=CurrentPortfolio.getSecurityAmount(currentasset.getName(),currentsecurity.getName(),CurrentDate);
double assetamount;
assetamount=CurrentPortfolio.getAssetAmount(currentasset.getName(),CurrentDate);
withdrawalAmount=(-adjustmentAmount*CurrentPortfolio.getTargetPercentage(currentasset.getName())*securityamount)/assetamount;

printToLog("SecurityAmount"+securityamount);
printToLog("TargetPercentage:"+CurrentPortfolio.getTargetPercentage(currentasset.getName()));
printToLog("TotalAmount"+CurrentPortfolio.getTotalAmount(CurrentDate));
printToLog("AssetAmount"+assetamount);
printToLog("current transaction:"+currentsecurity.getName()+withdrawalAmount);

CurrentPortfolio.sell(currentasset.getName(),currentsecurity.getName(),withdrawalAmount,CurrentDate);
CurrentPortfolio.withdraw(withdrawalAmount,CurrentDate);
				}
			}


}
i++;
//if the adjustment amount is negative, sell securities to get back to value path.

if (i>totalPeriod)
printToLog("Cash flow strategy is over.");

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