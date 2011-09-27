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
public class Yale_Individual_Investor_s_Portfolio_3123 extends SimulateStrategy{
	public Yale_Individual_Investor_s_Portfolio_3123(){
		super();
		StrategyID=123L;
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
	int i;
double j;
int sum;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("i: ");
		sb.append(i);
		sb.append("\n");
		sb.append("j: ");
		sb.append(j);
		sb.append("\n");
		sb.append("sum: ");
		sb.append(sum);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(i);
		stream.writeObject(j);
		stream.writeObject(sum);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		i=(Integer)stream.readObject();;
		j=(Double)stream.readObject();;
		sum=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		printToLog("Hello World");
i=1;
j= i+1.0;
printToLog("One String " + "Another String");
printToLog("i: "+i);
printToLog("j: "+j);

//sum from 1 to 1000
sum=0;
for(i=0; i<1000;i++) {
  sum = sum+i;
}

printToLog("sum = "+sum);





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
		
		 
		else if (false) {
		   printToLog("foo");
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