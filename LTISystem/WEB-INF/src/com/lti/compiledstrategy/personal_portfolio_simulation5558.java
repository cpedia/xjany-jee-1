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
public class personal_portfolio_simulation5558 extends SimulateStrategy{
	public personal_portfolio_simulation5558(){
		super();
		StrategyID=5558L;
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

    CurrentAsset = new Asset();
    CurrentAsset.setName("PersonalAsset");
    CurrentAsset.setAssetClassID(0l);
    CurrentAsset.setAssetClassName("PersonalAsset");
    CurrentAsset.setTargetPercentage(1.0);
    addAsset(CurrentAsset);
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
	
		
				
		
		List<Transaction> trs = this.SimulateHolding.getPersonalTransactions();
		if(trs!=null&&trs.size()>0){
			for(Transaction tr:trs){
				if(tr.getDate()!=null&&LTIDate.equals(CurrentDate, tr.getDate())){
					double share=tr.getShare();
					double price=securityManager.get(tr.getSymbol()).getClose(CurrentDate);
					double amount=share*price;
					if (com.lti.system.Configuration.TRANSACTION_BUY.equalsIgnoreCase(tr.getOperation())) {
						buy("PersonalAsset", tr.getSymbol(), amount, CurrentDate);
					}
					if (com.lti.system.Configuration.TRANSACTION_SELL.equalsIgnoreCase(tr.getOperation())) {
						sell("PersonalAsset", tr.getSymbol(), amount, CurrentDate);
					}				}
			}
		}
		
		
		
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