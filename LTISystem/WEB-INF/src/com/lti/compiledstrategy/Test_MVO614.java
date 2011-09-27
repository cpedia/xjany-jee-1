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
public class Test_MVO614 extends SimulateStrategy{
	public Test_MVO614(){
		super();
		StrategyID=614L;
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
		LTIMVOInterface li = new LTIMVOInterface();
List<String> securityList = new ArrayList<String>();
securityList.add("CASH");
securityList.add("UTF");
securityList.add("ERH");
Date start = LTIDate.getDate(2004, 4, 29);
Date end = LTIDate.getDate(2005, 4, 29);
double RAF = 4;
TimeUnit timeUnit = TimeUnit.DAILY;
List<Double> lowerLimits = new ArrayList<Double>();
lowerLimits.add(0.0);
lowerLimits.add(0.0);
lowerLimits.add(0.0);
List<Double> upperLimits = new ArrayList<Double>();
upperLimits.add(1.0);
upperLimits.add(1.0);
upperLimits.add(1.0);
List<Double> exceptedExcessReturns = new ArrayList<Double>();
exceptedExcessReturns.add(null);
exceptedExcessReturns.add(null);
exceptedExcessReturns.add(null);
li.createModel(securityList, lowerLimits, upperLimits, exceptedExcessReturns, RAF, start, end, timeUnit,false);
List<Double> returns = li.checkExpectedReturns();
for (int i = 0; i < returns.size(); i++) {
    printToLog(securityList.get(i) + ": " + returns.get(i));
    printToLog(lowerLimits.get(i) + "  " + upperLimits.get(i));
}
 List<Double> weights=null;
 
 weights = li.getMVOWeightsWithLimits();

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