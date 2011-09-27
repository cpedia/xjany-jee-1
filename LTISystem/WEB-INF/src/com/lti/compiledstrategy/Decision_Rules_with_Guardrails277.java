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
public class Decision_Rules_with_Guardrails277 extends SimulateStrategy{
	public Decision_Rules_with_Guardrails277(){
		super();
		StrategyID=277L;
		StrategyClassID=5L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private double initialwithdrawalRate;
	public void setInitialwithdrawalRate(double initialwithdrawalRate){
		this.initialwithdrawalRate=initialwithdrawalRate;
	}
	
	public double getInitialwithdrawalRate(){
		return this.initialwithdrawalRate;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		initialwithdrawalRate=(Double)ParameterUtil.fetchParameter("double","initialwithdrawalRate", "0.05", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	double lastPeriodAmount;
double cumulativeAmount;
double PreviousCPI;
double inflation;
double withdrawalRate;

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
		sb.append("withdrawalRate: ");
		sb.append(withdrawalRate);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(lastPeriodAmount);
		stream.writeObject(cumulativeAmount);
		stream.writeObject(PreviousCPI);
		stream.writeObject(inflation);
		stream.writeObject(withdrawalRate);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		lastPeriodAmount=(Double)stream.readObject();;
		cumulativeAmount=(Double)stream.readObject();;
		PreviousCPI=(Double)stream.readObject();;
		inflation=(Double)stream.readObject();;
		withdrawalRate=(Double)stream.readObject();;
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
cumulativeAmount=0;
withdrawalRate=initialwithdrawalRate;
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

if ((CurrentPortfolio.getTotalAmount(CurrentDate)>=lastPeriodAmount)||(withdrawalRate<=initialwithdrawalRate))
{
  if (withdrawalRate>1.2*initialwithdrawalRate)
    {withdrawalRate=withdrawalRate*0.9;

adjustmentAmount=CurrentPortfolio.getTotalAmount(CurrentDate)*withdrawalRate;
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

CurrentPortfolio.sell(currentasset.getName(),currentsecurity.getName(),withdrawalAmount,CurrentDate);
CurrentPortfolio.removeCash(withdrawalAmount,CurrentDate);	
			}
			}

cumulativeAmount= cumulativeAmount + adjustmentAmount;
printToLog("cumulativeAmount"+cumulativeAmount);

inflation=(getIndicator("CPIAUCSL").getValue(CurrentDate)-PreviousCPI)/PreviousCPI;
if (inflation>0.06)
inflation=0.06;
withdrawalRate=withdrawalRate*(1+inflation);
    }
        else if (withdrawalRate<0.8*initialwithdrawalRate)
                    {
                       withdrawalRate=withdrawalRate*1.1;

adjustmentAmount=CurrentPortfolio.getTotalAmount(CurrentDate)*withdrawalRate;
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


CurrentPortfolio.sell(currentasset.getName(),currentsecurity.getName(),withdrawalAmount,CurrentDate);
CurrentPortfolio.removeCash(withdrawalAmount,CurrentDate);	
			}
		}
cumulativeAmount= cumulativeAmount + adjustmentAmount;
printToLog("cumulativeAmount"+cumulativeAmount);

inflation=(getIndicator("CPIAUCSL").getValue(CurrentDate)-PreviousCPI)/PreviousCPI;
withdrawalRate=withdrawalRate*(1+inflation);
                   }
                   else
                             {
                           adjustmentAmount=CurrentPortfolio.getTotalAmount(CurrentDate)*withdrawalRate;
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

CurrentPortfolio.sell(currentasset.getName(),currentsecurity.getName(),withdrawalAmount,CurrentDate);
CurrentPortfolio.removeCash(withdrawalAmount,CurrentDate);	
			}
			}

cumulativeAmount= cumulativeAmount + adjustmentAmount;
printToLog("cumulativeAmount"+cumulativeAmount);

inflation=(getIndicator("CPIAUCSL").getValue(CurrentDate)-PreviousCPI)/PreviousCPI;
if (inflation>0.06)
inflation=0.06;
withdrawalRate=withdrawalRate*(1+inflation);

                             }
}

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