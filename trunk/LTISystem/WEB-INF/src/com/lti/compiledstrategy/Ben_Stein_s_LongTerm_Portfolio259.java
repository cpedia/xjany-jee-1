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
public class Ben_Stein_s_LongTerm_Portfolio259 extends SimulateStrategy{
	public Ben_Stein_s_LongTerm_Portfolio259(){
		super();
		StrategyID=259L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
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
CurrentPortfolio.sellAssetCollection(CurrentDate);

CurrentAsset = new Asset();
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("1");
CurrentAsset.setClassID(getAssetClassID("INTERNATIONAL Equity"));
CurrentAsset.setTargetPercentage(0.30);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "FSTMX", TotalAmount * 0.30,
		CurrentDate);



CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("2");
CurrentAsset.setClassID(getAssetClassID("INTERNATIONAL Equity"));
CurrentAsset.setTargetPercentage(0.15);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "EFA", TotalAmount * 0.15,
		CurrentDate);

		
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("3");
CurrentAsset.setClassID(getAssetClassID("INTERNATIONAL Equity"));
CurrentAsset.setTargetPercentage(0.10);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "EEM", TotalAmount * 0.10,
		CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("4");
CurrentAsset.setClassID(9l);
CurrentAsset.setTargetPercentage(0.10);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "ICF", TotalAmount * 0.10,CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("5");
CurrentAsset.setClassID(getAssetClassID("INTERNATIONAL Equity"));
CurrentAsset.setTargetPercentage(0.10);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "IWN", TotalAmount * 0.10,CurrentDate);	


CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("6");
CurrentAsset.setClassID(getAssetClassID("US Equity"));
CurrentAsset.setTargetPercentage(0.10);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "ADRE", TotalAmount * 0.10,CurrentDate);	


CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("7");
CurrentAsset.setClassID(3l);
CurrentAsset.setTargetPercentage(0.15);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(),"CASH", TotalAmount * 0.15,CurrentDate);

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
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

//