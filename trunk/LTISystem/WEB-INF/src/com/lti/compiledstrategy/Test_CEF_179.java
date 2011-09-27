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
public class Test_CEF_179 extends SimulateStrategy{
	public Test_CEF_179(){
		super();
		StrategyID=179L;
		StrategyClassID=2L;
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
		
		 
		else if (LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) {
		   String original;


original=CurrentPortfolio.getAssetSecurities(curAsset).get(0).getName();
printToLog(getSecurity(original).getName());

List <Security> CEF=getCEF( CurrentPortfolio.getAsset(curAsset).getAssetClassID());

printToLog(CEF.size());



printToLog("AssetClassID:"+CurrentPortfolio.getAsset(curAsset).getAssetClassID());

Security bond;
String name;

List <Security>Top=getTopSecurity(CEF,3,-1,CurrentDate,TimeUnit.YEARLY,SortType.SHARPE,true);

printToLog(Top.size());

/*
for(int i=0;i<10;i++){
bond = CEF.get(i);

printToLog(bond.getName());
printToLog(bond.getSharpeRatio(LTIDate.getNewNYSEYear(CurrentDate,-1),CurrentDate,TimeUnit.DAILY,true));
}
*/
//printToLog("Discount Rate"+bond.getDiscountRate(CurrentDate));


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