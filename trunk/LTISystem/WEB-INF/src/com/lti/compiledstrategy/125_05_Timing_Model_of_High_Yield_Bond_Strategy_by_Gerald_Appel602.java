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
public class 125_05_Timing_Model_of_High_Yield_Bond_Strategy_by_Gerald_Appel602 extends SimulateStrategy{
	public 125_05_Timing_Model_of_High_Yield_Bond_Strategy_by_Gerald_Appel602(){
		super();
		StrategyID=602L;
		StrategyClassID=20L;
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
		
		
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/125_05_Timing_Model_of_High_Yield_Bond_Strategy_by_Gerald_Appel602.java:22: <identifier> expected
//public class 125_05_Timing_Model_of_High_Yield_Bond_Strategy_by_Gerald_Appel602 extends SimulateStrategy{
//            ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/125_05_Timing_Model_of_High_Yield_Bond_Strategy_by_Gerald_Appel602.java:23: illegal start of type
//	public 125_05_Timing_Model_of_High_Yield_Bond_Strategy_by_Gerald_Appel602(){
//	       ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/125_05_Timing_Model_of_High_Yield_Bond_Strategy_by_Gerald_Appel602.java:23: invalid method declaration; return type required
//	public 125_05_Timing_Model_of_High_Yield_Bond_Strategy_by_Gerald_Appel602(){
//	          ^
//3 errors