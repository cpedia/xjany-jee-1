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
public class Single_Security576 extends SimulateStrategy{
	public Single_Security576(){
		super();
		StrategyID=576L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String name;
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return this.name;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		name=(String)ParameterUtil.fetchParameter("String","name", "", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	int count=0;
int count2=-50;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("count: ");
		sb.append(count);
		sb.append("\n");
		sb.append("count2: ");
		sb.append(count2);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(count);
		stream.writeObject(count2);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		count=(Integer)stream.readObject();;
		count2=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		count=0;
count2=-40;
Asset CurrentAsset;



CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset");
CurrentAsset.setClassID(getAssetClassID("COMMODITIES"));
CurrentAsset.setTargetPercentage(1.0);
CurrentPortfolio.addAsset(CurrentAsset);

double totalamount=CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.buy("Asset", getSecurity(name).getID(), totalamount, CurrentDate,true);
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
	
		
		count++;
count2--;
printToLog("count"+count);
printToLog("count2"+count2);
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		
		else{
		
			if(isMonthEnd(CurrentDate))sendMail();;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

//