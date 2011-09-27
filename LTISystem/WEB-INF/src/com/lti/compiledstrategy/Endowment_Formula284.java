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
public class Endowment_Formula284 extends SimulateStrategy{
	public Endowment_Formula284(){
		super();
		StrategyID=284L;
		StrategyClassID=5L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private double initialPercentage;
	public void setInitialPercentage(double initialPercentage){
		this.initialPercentage=initialPercentage;
	}
	
	public double getInitialPercentage(){
		return this.initialPercentage;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		initialPercentage=(Double)ParameterUtil.fetchParameter("double","initialPercentage", "0.04", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double cumulativeAmount;
double PreviousCPI;
double inflation;
double adjustmentAmount;
boolean flag;
double originalAmount;

	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("cumulativeAmount: ");
		sb.append(cumulativeAmount);
		sb.append("\n");
		sb.append("PreviousCPI: ");
		sb.append(PreviousCPI);
		sb.append("\n");
		sb.append("inflation: ");
		sb.append(inflation);
		sb.append("\n");
		sb.append("adjustmentAmount: ");
		sb.append(adjustmentAmount);
		sb.append("\n");
		sb.append("flag: ");
		sb.append(flag);
		sb.append("\n");
		sb.append("originalAmount: ");
		sb.append(originalAmount);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(cumulativeAmount);
		stream.writeObject(PreviousCPI);
		stream.writeObject(inflation);
		stream.writeObject(adjustmentAmount);
		stream.writeObject(flag);
		stream.writeObject(originalAmount);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		cumulativeAmount=(Double)stream.readObject();;
		PreviousCPI=(Double)stream.readObject();;
		inflation=(Double)stream.readObject();;
		adjustmentAmount=(Double)stream.readObject();;
		flag=(Boolean)stream.readObject();;
		originalAmount=(Double)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		PreviousCPI=getIndicator("CPIAUCNS").getValue(LTIDate.getNewNYSEYear(CurrentDate,-1));
originalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
flag=true;

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
		
		 
		else if (LTIDate.isLastNYSETradingDayOfYear(CurrentDate)&&flag==true) {
		   double withdrawalAmount;

adjustmentAmount= initialPercentage* originalAmount;

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

cumulativeAmount= cumulativeAmount + adjustmentAmount;
printToLog("cumulativeAmount"+cumulativeAmount);

PreviousCPI=getIndicator("CPIAUCNS").getValue(CurrentDate);
flag=false;

		}
		else if (LTIDate.isLastNYSETradingDayOfYear(CurrentDate)&&flag==false) {
		   double withdrawalAmount;
double FixedPercenatageAmount;
double FixedDollarAmount;

inflation=(getIndicator("CPIAUCSL").getValue(CurrentDate)-PreviousCPI)/PreviousCPI;
FixedDollarAmount= adjustmentAmount *(1+inflation)*0.7;
FixedPercenatageAmount=CurrentPortfolio.getTotalAmount(CurrentDate)*0.04*0.3;

adjustmentAmount= FixedPercenatageAmount + FixedDollarAmount;

printToLog("inflation"+inflation);
printToLog("FixedPercenatageAmount"+FixedPercenatageAmount);
printToLog("FixedDollarAmount"+FixedDollarAmount);
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

cumulativeAmount= cumulativeAmount + adjustmentAmount;
printToLog("cumulativeAmount"+cumulativeAmount);

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