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
public class Fix_Transactions606 extends SimulateStrategy{
	public Fix_Transactions606(){
		super();
		StrategyID=606L;
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
		com.lti.service.PortfolioManager pm = com.lti.system.ContextHolder.getPortfolioManager();
    java.util.List<com.lti.service.bo.Portfolio> portfolios = pm.getMonitorPortfolios();
    printToLog("total:"+portfolios.size());
    for(int i=0;i<portfolios.size();i++){
      com.lti.service.bo.Portfolio p=portfolios.get(i);
      java.util.List<com.lti.service.bo.Transaction> transactions=pm.getTransactions(p.getID());
      if(transactions!=null){
        double cachedTotal=0.0;
        java.util.Date lastDate=null;
        for(int j=0;j<transactions.size();j++){
          com.lti.service.bo.Transaction t=transactions.get(j);
          java.util.Date d=t.getDate();
          double total=0.0;
          if(lastDate!=null&&lastDate.equals(d)){
            total=cachedTotal;
          }else{
            try {
              total=pm.getDailydata(p.getID(), d).getAmount();
              cachedTotal=total;
              lastDate=d;
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          if(total!=0.0){
            t.setPercentage(t.getAmount()/total);
            pm.updateTransaction(t);
          }
          
        }
        printToLog("["+i+"]fixed "+p.getID()+"."+p.getName()+", counts: "+transactions.size());
      }
    }
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

///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/Fix_Transactions606.java:62: cannot find symbol
//symbol  : method getMonitorPortfolios()
//location: interface com.lti.service.PortfolioManager
//    java.util.List<com.lti.service.bo.Portfolio> portfolios = pm.getMonitorPortfolios();
//                                                                ^
//1 error