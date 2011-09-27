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
public class Test_Read_Object608 extends SimulateStrategy{
	public Test_Read_Object608(){
		super();
		StrategyID=608L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String CheckFrequency;
	public void setCheckFrequency(String CheckFrequency){
		this.CheckFrequency=CheckFrequency;
	}
	
	public String getCheckFrequency(){
		return this.CheckFrequency;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		CheckFrequency=(String)ParameterUtil.fetchParameter("String","CheckFrequency", "monthly", parameters);
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
	/*
	 only run once after executing 

	public void afterExecuting(Portfolio portfolio,Date CurrentDate)  throws Exception {
                updateTable();
	}

	  only run once after updating 

	public void afterUpdating(Portfolio portfolio,Date CurrentDate)  throws Exception {
                updateTable();
	}
        public void updateTable() throws Exception {
		String[][] table=new String[][]{
                           {"Header1","Header2"},
                           {"1","2"},
                           {"a","b"}
                };
                String id=StrategyID+".table";
		PersistentUtil.writeObject(table,id);		               
        }
*/

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
String dateStr=df.format(CurrentDate);

Object[] ReadObj = (Object[])com.lti.util.PersistentUtil.readObject("test write object " + dateStr);

String[] ReadTenFunds = (String[])ReadObj[0];
int[] ReadTenScores = (int[])ReadObj[2];
List<String> ReadTenFundsList = (ArrayList<String>)ReadObj[1];
String ReadDate = (String)ReadObj[3];

for(int i = 0; i < 10; i++)
{
    printToLog("Date : " + ReadDate + "   Fund " + i + "  : " + ReadTenFunds[i] + "  " + ReadTenFundsList.get(i) + "  " + ReadTenScores[i]);
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
		
		 
		else if ((CheckFrequency.equals("monthly")&&LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) || (CheckFrequency.equals("quarterly")&&LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate)) || 
(CheckFrequency.equals("yearly")&&LTIDate.isLastNYSETradingDayOfYear(CurrentDate))) {
		   java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
String dateStr=df.format(CurrentDate);

Object[] ReadObj = (Object[])com.lti.util.PersistentUtil.readObject("test write object " + dateStr);

String[] ReadTenFunds = (String[])ReadObj[0];
int[] ReadTenScores = (int[])ReadObj[2];
List<String> ReadTenFundsList = (ArrayList<String>)ReadObj[1];
String ReadDate = (String)ReadObj[3];

for(int i = 0; i < 10; i++)
{
    printToLog("Date : " + ReadDate + "   Fund " + i + "  : " + ReadTenFunds[i] + "  " + ReadTenFundsList.get(i) + "  " + ReadTenScores[i]);
}
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