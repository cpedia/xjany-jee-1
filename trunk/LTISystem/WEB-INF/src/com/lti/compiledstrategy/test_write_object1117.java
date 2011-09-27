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
public class test_write_object1117 extends SimulateStrategy{
	public test_write_object1117(){
		super();
		StrategyID=1117L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String[] CandidateFund;
	public void setCandidateFund(String[] CandidateFund){
		this.CandidateFund=CandidateFund;
	}
	
	public String[] getCandidateFund(){
		return this.CandidateFund;
	}
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
		CandidateFund=(String[])ParameterUtil.fetchParameter("String[]","CandidateFund", "CHTVX,FOCPX, FUSVX, MSIVX, WFSVX, ^DWC, FSEMX, FIGRX, HAINX,FBNDX, VBMFX,CASH", parameters);
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
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		String[] TenFunds = new  String[10];
int[] TenScores = new int[10];
List<String> TenFundsList = new ArrayList<String>();

java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
String dateStr=df.format(CurrentDate);

for(int i = 0; i < 10; i++)
{
    TenFunds[i] = CandidateFund[i];
    TenFundsList.add(CandidateFund[i]);
    TenScores[i] = i+1;
}
Object[] Ob = new Object[]{TenFunds,TenFundsList,TenScores,dateStr};

com.lti.util.PersistentUtil.writeObject(Ob,"test write object "+dateStr);

/*
Object[] ReadObj = (Object[])com.lti.util.PersistentUtil.readObject("test write object " + dateStr);

String[] ReadTenFunds = (String[])ReadObj[0];
int[] ReadTenScores = (int[])ReadObj[2];
List<String> ReadTenFundsList = (ArrayList<String>)ReadObj[1];
String ReadDate = (String)ReadObj[3];

for(int i = 0; i < 10; i++)
{
    printToLog("Date : " + ReadDate + "   Fund " + i + "  : " + ReadTenFunds[i] + "  " + ReadTenFundsList.get(i) + "  " + ReadTenScores[i]);
}
*/

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
		   String[] TenFunds = new  String[10];
int[] TenScores = new int[10];
List<String> TenFundsList = new ArrayList<String>();

java.text.SimpleDateFormat df=new java.text.SimpleDateFormat("yyyy-MM-dd");
String dateStr=df.format(CurrentDate);

for(int i = 0; i < 10; i++)
{
    TenFunds[i] = CandidateFund[i];
    TenFundsList.add(CandidateFund[i]);
    TenScores[i] = i+1;
}
Object[] Ob = new Object[]{TenFunds,TenFundsList,TenScores,dateStr};

com.lti.util.PersistentUtil.writeObject(Ob,"test write object "+dateStr);

/*
Object[] ReadObj = (Object[])com.lti.util.PersistentUtil.readObject("test write object " + dateStr);

String[] ReadTenFunds = (String[])ReadObj[0];
int[] ReadTenScores = (int[])ReadObj[2];
List<String> ReadTenFundsList = (ArrayList<String>)ReadObj[1];
String ReadDate = (String)ReadObj[3];

for(int i = 0; i < 10; i++)
{
    printToLog("Date : " + ReadDate + "   Fund " + i + "  : " + ReadTenFunds[i] + "  " + ReadTenFundsList.get(i) + "  " + ReadTenScores[i]);
}
*/
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