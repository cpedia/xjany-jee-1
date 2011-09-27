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
public class Guru_Allocator_RAA_Fund_Clone_Parameter555 extends SimulateStrategy{
	public Guru_Allocator_RAA_Fund_Clone_Parameter555(){
		super();
		StrategyID=555L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String FundToClone;
	public void setFundToClone(String FundToClone){
		this.FundToClone=FundToClone;
	}
	
	public String getFundToClone(){
		return this.FundToClone;
	}
	private String[] indices;
	public void setIndices(String[] indices){
		this.indices=indices;
	}
	
	public String[] getIndices(){
		return this.indices;
	}
	private String AdjustFrequency;
	public void setAdjustFrequency(String AdjustFrequency){
		this.AdjustFrequency=AdjustFrequency;
	}
	
	public String getAdjustFrequency(){
		return this.AdjustFrequency;
	}
	private int interval;
	public void setInterval(int interval){
		this.interval=interval;
	}
	
	public int getInterval(){
		return this.interval;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		FundToClone=(String)ParameterUtil.fetchParameter("String","FundToClone", "HSGFX", parameters);
		indices=(String[])ParameterUtil.fetchParameter("String[]","indices", "VFINX, VGTSX, VGSIX, QRAAX, BEGBX,VBMFX, CASH", parameters);
		AdjustFrequency=(String)ParameterUtil.fetchParameter("String","AdjustFrequency", "month", parameters);
		interval=(Integer)ParameterUtil.fetchParameter("int","interval", "30", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date NextDate;
int NumberOfGurus=1;
Date startDate;
Date date;

String[] class1={"HSGFX"};
/* {"HSGFX", "HSTRX", "GGHEX","LCORX", "PASDX","WASYX","VAAPX", "VWINX","GLRBX","FPACX",  "TFSMX", "SWHIX", "DHFCX", "GATEX"};  */
int s=class1.length-1;


	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("NextDate: ");
		sb.append(NextDate);
		sb.append("\n");
		sb.append("NumberOfGurus: ");
		sb.append(NumberOfGurus);
		sb.append("\n");
		sb.append("startDate: ");
		sb.append(startDate);
		sb.append("\n");
		sb.append("date: ");
		sb.append(date);
		sb.append("\n");
		sb.append("class1: ");
		sb.append(class1);
		sb.append("\n");
		sb.append("s: ");
		sb.append(s);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(NextDate);
		stream.writeObject(NumberOfGurus);
		stream.writeObject(startDate);
		stream.writeObject(date);
		stream.writeObject(class1);
		stream.writeObject(s);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		NextDate=(Date)stream.readObject();;
		NumberOfGurus=(Integer)stream.readObject();;
		startDate=(Date)stream.readObject();;
		date=(Date)stream.readObject();;
		class1=(String[])stream.readObject();;
		s=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		startDate=CurrentDate;
date=CurrentDate;

CurrentPortfolio.sellAssetCollection(CurrentDate);

NextDate=AdjustFrequency.equals("month")? LTIDate.getNewNYSETradingDay(CurrentDate,22):
LTIDate.getNewNYSETradingDay(CurrentDate,5);

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
Asset CurrentAsset;


CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Fund_Clone");
CurrentAsset.setClassID(getAssetClassID("HYBRID ASSETS"));
CurrentAsset.setTargetPercentage(1.0);
CurrentPortfolio.addAsset(CurrentAsset);
for(int i=0; i<indices.length; i++) 
CurrentPortfolio.buy(CurrentAsset.getName(), indices[i], TotalAmount * 1.0/indices.length,CurrentDate);
/*
CurrentPortfolio.buy(CurrentAsset.getName(), "VGTSX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGSIX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "QRAAX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "BEGBX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VBMFX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "CASH", TotalAmount * 0.4,CurrentDate);
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
		
		 
		else if (NextDate.equals(CurrentDate)) {
		   String[] MF={FundToClone};

int iNumIndex=indices.length;
String FirstAssetName;

double totalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
printToLog(NextDate);  

//get current portfolio's asset list 
List<Asset> list1=CurrentPortfolio.getCurrentAssetList();

Asset as=list1.get(0);
FirstAssetName=as.getName();

CurrentPortfolio.sellAsset(FirstAssetName,CurrentDate);

double[] newAmount=new double[iNumIndex];
double[] temp=new double[iNumIndex];
for(int j=0;j<NumberOfGurus;j++)
{
   printToLog("Doing RAA for " + MF[j]);
    temp=RAA(interval,CurrentDate,TimeUnit.DAILY,MF[j],indices,false);
    for(int k=0;k<iNumIndex;k++)
   {  
       newAmount[k] += (1.0/NumberOfGurus)*temp[k]*totalAmount;
   }
}

for(int k=0;k<iNumIndex;k++)
 {  
   CurrentPortfolio.buy(FirstAssetName, indices[k],newAmount[k] , CurrentDate);    
 }

NextDate=AdjustFrequency.equals("month")? LTIDate.getNewNYSETradingDay(CurrentDate,22):
LTIDate.getNewNYSETradingDay(CurrentDate,5);

printToLog(NextDate);   
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