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
public class John_Wasik_s_Nano_Investment_Portfolio262 extends SimulateStrategy{
	public John_Wasik_s_Nano_Investment_Portfolio262(){
		super();
		StrategyID=262L;
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
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("1");
CurrentAsset.setClassID(getAssetClassID("US Equity"));
CurrentAsset.setTargetPercentage(0.20);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VTI", TotalAmount * 0.20,
		CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("2");
CurrentAsset.setClassID(getAssetClassID("INTERNATIONAL Equity"));
CurrentAsset.setTargetPercentage(0.20);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGTSX", TotalAmount * 0.20,
		CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("3");
CurrentAsset.setClassID(getAssetClassID("US Equity"));
CurrentAsset.setTargetPercentage(0.20);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VNQ", TotalAmount * 0.20,
		CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("4");
CurrentAsset.setClassID(getAssetClassID("US MUNICIPAL BONDS"));
CurrentAsset.setTargetPercentage(0.20);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "TIP", TotalAmount * 0.20,
		CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("5");
CurrentAsset.setClassID(getAssetClassID("US MUNICIPAL BONDS"));
CurrentAsset.setTargetPercentage(0.20);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "AGG", TotalAmount * 0.20,
		CurrentDate);
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