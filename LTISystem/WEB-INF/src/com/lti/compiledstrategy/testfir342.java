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
public class testfir342 extends SimulateStrategy{
	public testfir342(){
		super();
		StrategyID=342L;
		StrategyClassID=0L;
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
	double[] b0;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("b0: ");
		sb.append(b0);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(b0);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		b0=(double[])stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		b0[2]=getSecurity("VFINX").getCurrentPrice(CurrentDate);

b0[1]=getSecurity("VFINX").getCurrentPrice(LTIDate. getLastNTSETradingDay(1,TimeUnit.MONTHLY,CurrentDate));
b0[0]=getSecurity("VFINX").getCurrentPrice(LTIDate. getLastNTSETradingDay (2,TimeUnit.MONTHLY,CurrentDate));

printToLog("b0[1]");
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

//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\testfir342.java:68: ~0&÷
//&÷ ¹Õ getLastNTSETradingDay(int,com.lti.type.TimeUnit,java.util.Date)
//Mn { com.lti.util.LTIDate
//b0[1]=getSecurity("VFINX").getCurrentPrice(LTIDate. getLastNTSETradingDay(1,TimeUnit.MONTHLY,CurrentDate));
//                                                  ^
//D:\workspace\LTISystem\WEB-INF\src\com\lti\compiledstrategy\testfir342.java:69: ~0&÷
//&÷ ¹Õ getLastNTSETradingDay(int,com.lti.type.TimeUnit,java.util.Date)
//Mn { com.lti.util.LTIDate
//b0[0]=getSecurity("VFINX").getCurrentPrice(LTIDate. getLastNTSETradingDay (2,TimeUnit.MONTHLY,CurrentDate));
//                                                  ^
//2 ï