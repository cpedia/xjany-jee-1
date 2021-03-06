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
public class Gibson_Asset_Allocation_plus_withdrawal_strategy296 extends SimulateStrategy{
	public Gibson_Asset_Allocation_plus_withdrawal_strategy296(){
		super();
		StrategyID=296L;
		StrategyClassID=5L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private double withdrawalRate;
	public void setWithdrawalRate(double withdrawalRate){
		this.withdrawalRate=withdrawalRate;
	}
	
	public double getWithdrawalRate(){
		return this.withdrawalRate;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		withdrawalRate=(Double)ParameterUtil.fetchParameter("double","withdrawalRate", "0.05", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double AccumulatedAmount;
double PreviousCPI;
double inflation;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("AccumulatedAmount: ");
		sb.append(AccumulatedAmount);
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
		stream.writeObject(AccumulatedAmount);
		stream.writeObject(PreviousCPI);
		stream.writeObject(inflation);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		AccumulatedAmount=(Double)stream.readObject();;
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



AccumulatedAmount=0;
inflation=0;
PreviousCPI=getIndicator("CPIAUCNS").getValue(CurrentDate);
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
		   double adjustmentAmount;
double withdrawalAmount;

inflation=(getIndicator("CPIAUCSL").getValue(CurrentDate)-PreviousCPI)/PreviousCPI;
printToLog("inflation:"+inflation);

withdrawalRate=withdrawalRate*(1+inflation);
adjustmentAmount= CurrentPortfolio.getTotalAmount(CurrentDate)*withdrawalRate;


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



AccumulatedAmount=AccumulatedAmount+adjustmentAmount;
printToLog("AccumulatedAmount"+AccumulatedAmount);

PreviousCPI=getIndicator("CPIAUCNS").getValue(CurrentDate);

CurrentPortfolio.balance(CurrentDate);
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