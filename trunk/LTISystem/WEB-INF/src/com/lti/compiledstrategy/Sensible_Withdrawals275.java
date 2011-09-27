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
public class Sensible_Withdrawals275 extends SimulateStrategy{
	public Sensible_Withdrawals275(){
		super();
		StrategyID=275L;
		StrategyClassID=5L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private double floor;
	public void setFloor(double floor){
		this.floor=floor;
	}
	
	public double getFloor(){
		return this.floor;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		floor=(Double)ParameterUtil.fetchParameter("double","floor", "0.03", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double lastPeriodAmount;
double cumulativeAmount;
double PreviousCPI;
double inflation;

	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("lastPeriodAmount: ");
		sb.append(lastPeriodAmount);
		sb.append("\n");
		sb.append("cumulativeAmount: ");
		sb.append(cumulativeAmount);
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
		stream.writeObject(lastPeriodAmount);
		stream.writeObject(cumulativeAmount);
		stream.writeObject(PreviousCPI);
		stream.writeObject(inflation);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		lastPeriodAmount=(Double)stream.readObject();;
		cumulativeAmount=(Double)stream.readObject();;
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
		lastPeriodAmount= CurrentPortfolio.getTotalAmount(CurrentDate);
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
		   double adjustmentAmount;
double extraAmount;
double initialAmount;
double withdrawalAmount;

inflation=(getIndicator("CPIAUCSL").getValue(CurrentDate)-PreviousCPI)/PreviousCPI;
printToLog("inflation"+inflation);

initialAmount=CurrentPortfolio.getTotalAmount(CurrentDate)*floor*(1+inflation);



if ((CurrentPortfolio.getTotalAmount(CurrentDate)-initialAmount)>lastPeriodAmount*(1+inflation))
{extraAmount= (CurrentPortfolio.getTotalAmount(CurrentDate)- initialAmount- lastPeriodAmount*(1+inflation))*0.5;
}
else extraAmount=0;

adjustmentAmount= initialAmount+ extraAmount;

printToLog("adjustmentAmount"+adjustmentAmount);
printToLog("extraAmount"+extraAmount);

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

lastPeriodAmount= CurrentPortfolio.getTotalAmount(CurrentDate);
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