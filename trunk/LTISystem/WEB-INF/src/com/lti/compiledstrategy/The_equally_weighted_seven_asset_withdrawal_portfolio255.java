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
public class The_equally_weighted_seven_asset_withdrawal_portfolio255 extends SimulateStrategy{
	public The_equally_weighted_seven_asset_withdrawal_portfolio255(){
		super();
		StrategyID=255L;
		StrategyClassID=0L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private double fixedPercentage;
	public void setFixedPercentage(double fixedPercentage){
		this.fixedPercentage=fixedPercentage;
	}
	
	public double getFixedPercentage(){
		return this.fixedPercentage;
	}
	private double floor;
	public void setFloor(double floor){
		this.floor=floor;
	}
	
	public double getFloor(){
		return this.floor;
	}
	private double ceiling;
	public void setCeiling(double ceiling){
		this.ceiling=ceiling;
	}
	
	public double getCeiling(){
		return this.ceiling;
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
		fixedPercentage=(Double)ParameterUtil.fetchParameter("double","fixedPercentage", "0.04", parameters);
		floor=(Double)ParameterUtil.fetchParameter("double","floor", "0.036", parameters);
		ceiling=(Double)ParameterUtil.fetchParameter("double","ceiling", "0.05", parameters);
		Interval=(Integer)ParameterUtil.fetchParameter("int","Interval", "2", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double initialAmount;
double withdrawRate;
double adjustmentAmount;



	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("initialAmount: ");
		sb.append(initialAmount);
		sb.append("\n");
		sb.append("withdrawRate: ");
		sb.append(withdrawRate);
		sb.append("\n");
		sb.append("adjustmentAmount: ");
		sb.append(adjustmentAmount);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(initialAmount);
		stream.writeObject(withdrawRate);
		stream.writeObject(adjustmentAmount);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		initialAmount=(Double)stream.readObject();;
		withdrawRate=(Double)stream.readObject();;
		adjustmentAmount=(Double)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		Asset CurrentAsset;
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);

		
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("US large cap");
CurrentAsset.setClassID(getAssetClassID("US Equity"));
CurrentAsset.setTargetPercentage(0.1428);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "^GSPC", TotalAmount/7,
		CurrentDate);
	
		
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("IUS small cap");
CurrentAsset.setClassID(52l);
CurrentAsset.setTargetPercentage(0.1428);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "^RUT", TotalAmount /7,
		CurrentDate);
		
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("International Equity");
CurrentAsset.setClassID(9l);
CurrentAsset.setTargetPercentage(0.1428);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGTSX", TotalAmount  /7,CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Real  Estate");
CurrentAsset.setClassID(5l);
CurrentAsset.setTargetPercentage(0.1428);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGSIX",
		TotalAmount /7, CurrentDate);
		
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Commodity");
CurrentAsset.setClassID(getAssetClassID("Commodity"));
CurrentAsset.setTargetPercentage(0.1428);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "QRAAX",
		TotalAmount /7, CurrentDate);



		
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Cash");
CurrentAsset.setClassID(3l);
CurrentAsset.setTargetPercentage(0.1428);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "CASH",
		TotalAmount /7, CurrentDate);
		
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("US Bond");
CurrentAsset.setClassID(2l);
CurrentAsset.setTargetPercentage(0.1428);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VBMFX", TotalAmount /7,
		CurrentDate);

initialAmount=TotalAmount;
withdrawRate=0.05;
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
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (con1||con2||con3) {
		   double adjustmentAmount;
double withdrawalAmount;

adjustmentAmount= CurrentPortfolio.getTotalAmount(CurrentDate)*withdrawRate;

printToLog("adjustmentAmount"+adjustmentAmount);


Asset currentasset;
			Iterator<Asset> iter=CurrentPortfolio.getAssets().iterator();
			while(iter.hasNext()){
				currentasset=iter.next();
				HoldingItem si;
				Iterator<HoldingItem> iter_1=currentasset.getHoldingItems().iterator();
				while(iter_1.hasNext()){
					si=iter_1.next();
	Security currentsecurity=getSecurity(si.getSecurityID());
withdrawalAmount=adjustmentAmount*CurrentPortfolio.getTargetPercentage(currentasset.getName())*CurrentPortfolio.getSecurityAmount(currentasset.getName(),currentsecurity.getName(),CurrentDate)/CurrentPortfolio.getAssetAmount(currentasset.getName());

printToLog("SecurityAmount"+CurrentPortfolio.getSecurityAmount(currentasset.getName(),currentsecurity.getName(),CurrentDate));
printToLog("TotalAmount"+CurrentPortfolio.getTotalAmount(CurrentDate));
printToLog("AssetAmount"+CurrentPortfolio.getAssetAmount(currentasset,CurrentDate));

printToLog("AssetCollectionAmount"+CurrentPortfolio.getAssetCollectionAmount(CurrentDate));
printToLog("Cash"+CurrentPortfolio.getCash());
printToLog(currentsecurity.getName()+withdrawalAmount);

CurrentPortfolio.sell(currentasset.getName(),currentsecurity.getName(),withdrawalAmount,CurrentDate);
CurrentPortfolio.removeCash(withdrawalAmount,CurrentDate);	
			}
			}

CurrentPortfolio.balance(CurrentDate); 

withdrawRate=withdrawRate*1.03;
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