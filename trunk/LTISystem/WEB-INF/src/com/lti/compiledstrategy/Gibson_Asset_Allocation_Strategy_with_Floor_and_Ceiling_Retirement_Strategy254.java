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
public class Gibson_Asset_Allocation_Strategy_with_Floor_and_Ceiling_Retirement_Strategy254 extends SimulateStrategy{
	public Gibson_Asset_Allocation_Strategy_with_Floor_and_Ceiling_Retirement_Strategy254(){
		super();
		StrategyID=254L;
		StrategyClassID=5L;
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
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		fixedPercentage=(Double)ParameterUtil.fetchParameter("double","fixedPercentage", "0.04", parameters);
		floor=(Double)ParameterUtil.fetchParameter("double","floor", "0.036", parameters);
		ceiling=(Double)ParameterUtil.fetchParameter("double","ceiling", "0.05", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double initialAmount;
double accumulatedAmount;
double PreviousCPI;
double inflation;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("initialAmount: ");
		sb.append(initialAmount);
		sb.append("\n");
		sb.append("accumulatedAmount: ");
		sb.append(accumulatedAmount);
		sb.append("\n");
		sb.append("PreviousCPI: ");
		sb.append(PreviousCPI);
		sb.append("\n");
		sb.append("inflation: ");
		sb.append(inflation);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(initialAmount);
		stream.writeObject(accumulatedAmount);
		stream.writeObject(PreviousCPI);
		stream.writeObject(inflation);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		initialAmount=(Double)stream.readObject();;
		accumulatedAmount=(Double)stream.readObject();;
		PreviousCPI=(Double)stream.readObject();;
		inflation=(Double)stream.readObject();;
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
CurrentAsset.setName("US Equity");
CurrentAsset.setClassID(getAssetClassID("US Equity"));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VFINX", TotalAmount * 0.2,
		CurrentDate);
	
		
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("International Equity");
CurrentAsset.setClassID(getAssetClassID("International Equity"));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGTSX", TotalAmount * 0.2,
		CurrentDate);
		
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("REIT");
CurrentAsset.setClassID(getAssetClassID("REIT"));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGSIX", TotalAmount * 0.2,
		CurrentDate);
		
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Commodity");
CurrentAsset.setClassID(getAssetClassID("Commodity"));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "QRAAX",
		TotalAmount * 0.2, CurrentDate);
		
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("International Bond");
CurrentAsset.setClassID(getAssetClassID("International Bonds"));
CurrentAsset.setTargetPercentage(0.06);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "BEGBX",
		TotalAmount * 0.06, CurrentDate);
		
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("US Bond");
CurrentAsset.setClassID(getAssetClassID("US Bond"));
CurrentAsset.setTargetPercentage(0.14);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VBMFX", TotalAmount * 0.14,
		CurrentDate);

initialAmount=TotalAmount;

accumulatedAmount=0;
inflation=0;
PreviousCPI=getIndicator("CPIAUCNS").getValue(LTIDate.getNewNYSEYear(CurrentDate,-1));
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
	
		
		
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.isLastNYSETradingDayOfYear(CurrentDate)) {
		   double floorAmount;
double ceilingAmount;
double adjustmentAmount;
double withdrawalAmount;

inflation=(getIndicator("CPIAUCSL").getValue(CurrentDate)-PreviousCPI)/PreviousCPI;
printToLog("inflation"+inflation);

floorAmount= initialAmount *floor*(1+inflation);
ceilingAmount= initialAmount *ceiling*(1+inflation);
adjustmentAmount= CurrentPortfolio.getTotalAmount(CurrentDate)*fixedPercentage*(1+inflation);

printToLog("Floor"+floorAmount+"AdjustmentAmount"+adjustmentAmount+"Ceiling"+ceilingAmount);

if(adjustmentAmount<floorAmount)
adjustmentAmount=floorAmount;
else if (adjustmentAmount>ceilingAmount)
adjustmentAmount=ceilingAmount;

printToLog("withdrawal:"+adjustmentAmount);

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

accumulatedAmount=accumulatedAmount+adjustmentAmount;
printToLog("AccumulatedAmount"+accumulatedAmount);

PreviousCPI=getIndicator("CPIAUCNS").getValue(CurrentDate);



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