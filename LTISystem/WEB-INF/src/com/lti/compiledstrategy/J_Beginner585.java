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
public class J_Beginner585 extends SimulateStrategy{
	public J_Beginner585(){
		super();
		StrategyID=585L;
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
		printToLog("Hello World");
     double mon;

    mon = 96.72;

    int tens;

    tens = (int)(mon/10);

    printToLog("     " + tens + " ten dollar bills");

    mon = (double)(mon - tens*10);
    printToLog("mon "+mon);
    int fives;

    fives = (int)(mon/5);

    printToLog("     " + fives + " five dollar bills");

    mon = (double)(mon - fives*5);
printToLog("mon "+mon);
    int ones;

    ones = (int)(mon/1);

    printToLog("     " + ones + " one dollar bills");

    mon = (double)(mon - ones*1);
printToLog("mon "+mon);
    int quarters;

    quarters = (int)(mon/0.25);

    printToLog("     " + quarters + " quarters");

    mon = (double)(mon - quarters*0.25);
printToLog("mon "+mon);
    int dimes;

    dimes = (int)(mon/0.10);

   printToLog("     " + dimes + " dimes");

    mon = (double)(mon - dimes*0.10);
printToLog("mon "+mon);
    int nickels;

    nickels = (int)(mon/0.05);

    printToLog("     " + nickels + " nickels");

    mon = (double)(mon - nickels*0.05);
printToLog("mon "+mon);

    int pennies;

    pennies = (int)(mon/0.01);

    printToLog("     " + pennies + " pennies");
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
	
		
		printToLog("Hello World");
     double mon;

    mon = 96.72;

    int tens;

    tens = (int)(mon/10);

    printToLog("     " + tens + " ten dollar bills");

    mon = (double)(mon - tens*10);

    int fives;

    fives = (int)(mon/5);

    printToLog("     " + fives + " five dollar bills");

    mon = (double)(mon - fives*5);

    int ones;

    ones = (int)(mon/1);

    printToLog("     " + ones + " one dollar bills");

    mon = (double)(mon - ones*1);

    int quarters;

    quarters = (int)(mon/0.25);

    printToLog("     " + quarters + " quarters");

    mon = (double)(mon - quarters*0.25);

    int dimes;

    dimes = (int)(mon/0.10);

   printToLog("     " + dimes + " dimes");

    mon = (double)(mon - dimes*0.10);

    int nickels;

    nickels = (int)(mon/0.05);

    printToLog("     " + nickels + " nickels");

    mon = (double)(mon - nickels*0.05);


    int pennies;

    pennies = (int)(mon/0.01);

    printToLog("     " + pennies + " pennies");

  
		
		
		
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