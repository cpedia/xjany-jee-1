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
public class Test_chart_object630 extends SimulateStrategy{
	public Test_chart_object630(){
		super();
		StrategyID=630L;
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
	Date[] dates=new Date[100];
Double[] values=new Double[100];
Double[][] valueses=new Double[100][2];
int i=0;
@Override
	public void afterExecuting(Portfolio portfolio, Date CurrentDate)   throws Exception {
		super.afterExecuting(portfolio, CurrentDate);
		String xml=buildXML(dates,values);
		com.lti.util.PersistentUtil.writeObject(xml,"chart.xml");
                xml=buildXML(dates,valueses);
		com.lti.util.PersistentUtil.writeObject(xml,"charts.xml");
	}

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
	
		
		if(i<100){
       dates[i]=CurrentDate;
       values[i]=CurrentPortfolio.getTotalAmount(CurrentDate);
       valueses[i][0]=  values[i];
  valueses[i][1]=  values[i]+(Math.random()-0.5)*values[i]*0.2;
       i++;
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