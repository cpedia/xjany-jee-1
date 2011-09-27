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
public class Global_Tactical_Asset_Allocation_Value_Based_Test_610 extends SimulateStrategy{
	public Global_Tactical_Asset_Allocation_Value_Based_Test_610(){
		super();
		StrategyID=610L;
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
	Date startDate;
String[] nameArray = {"VFINX","VBMFX","VFICX","CASH","VWEHX","VUSTX", "BEGBX","VGSIX"};
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("startDate: ");
		sb.append(startDate);
		sb.append("\n");
		sb.append("nameArray: ");
		sb.append(nameArray);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(startDate);
		stream.writeObject(nameArray);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		startDate=(Date)stream.readObject();;
		nameArray=(String[])stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		printToLog("modify");
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
		
		 
		else if (LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) {
		   //LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)
startDate=CurrentDate;
CurrentPortfolio.sellAssetCollection(CurrentDate);
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);

/*ranking*/
Double[] score=new Double[nameArray.length];
for(int i=0;i<nameArray.length;i++)
{
 if(nameArray[i].equals("VFINX")){
 score[i]=1/(getLastYearPE("^GSPC",CurrentDate));
 } else

 if(nameArray[i].equals("VBMFX")){
 score[i]=getLastYearDividendYield(nameArray[i],CurrentDate)-0.01;
 } else 

 if(nameArray[i].equals("VFICX")){
 score[i]=getLastYearDividendYield(nameArray[i],CurrentDate)-0.02;
 } else

 if(nameArray[i].equals("CASH")){
 score[i]=getLastYearDividendYield(nameArray[i],CurrentDate);
 } else 

 if(nameArray[i].equals("VWEHX")){
 score[i]=getLastYearDividendYield(nameArray[i],CurrentDate)-0.06;
 } else

 if(nameArray[i].equals("VUSTX")){
 score[i]=getLastYearDividendYield(nameArray[i],CurrentDate)-0.06;
 } else

if(nameArray[i].equals("BEGBX")){
 score[i]=getLastYearDividendYield(nameArray[i],CurrentDate)-0.01;
 }else

if(nameArray[i].equals("VGSIX")){
 score[i]=getLastYearDividendYield(nameArray[i],CurrentDate)-0.02;
 }
 }

Double Maxscore;
int Maxscoreindex;
for(int i=nameArray.length-1; i>=0; i--)
{
if(score[i].isNaN())score[i]=Double.NEGATIVE_INFINITY;
 Maxscore = score[i];
 Maxscoreindex =i;  
   for(int j=i-1; j>=0; j--)
   { 
     Double b;
     b= score[j];
     
     if(Maxscore <=b)
     {
      Maxscore =b;
      Maxscoreindex =j;
     }
   }
   
   if(Maxscoreindex !=i)
   {
     String tmp= nameArray[Maxscoreindex];
     nameArray [Maxscoreindex]= nameArray[i];
     nameArray[i]=tmp;
   
     Double t=score[Maxscoreindex];
     score [Maxscoreindex]= score[i];
     score[i]=t;
   }
}

printToLog("VFINX:"+1/getLastYearPE("^GSPC",CurrentDate));
printToLog("VBMFX:"+getLastYearDividendYield("VBMFX",CurrentDate));
printToLog("VFICX:"+getLastYearDividendYield("VFICX",CurrentDate));
printToLog("CASH:"+getLastYearDividendYield("CASH",CurrentDate));
printToLog("VWEHX:"+getLastYearDividendYield("VWEHX",CurrentDate));
printToLog("VUSTX:"+getLastYearDividendYield("VUSTX",CurrentDate));
printToLog("BEGBX:"+getLastYearDividendYield("BEGBX",CurrentDate));
printToLog("VGSIX:"+getLastYearDividendYield("VGSIX",CurrentDate));

for(int i=0;i<nameArray.length;i++){
printToLog("nameArray element is:"+i+nameArray[i]+";score is:"+score[i]);
}

Asset CurrentAsset ;
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset1");
CurrentAsset.setClassID(getAssetClassID(getSecurity (nameArray[nameArray.length-1]).getName()));
printToLog("selected security is:"+getSecurity(nameArray[7]).getSymbol());
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), nameArray[nameArray.length-1], TotalAmount * 0.5,CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity (nameArray[nameArray.length-2]).getName()));
printToLog("selected security is:"+getSecurity(nameArray[6]).getSymbol());
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), nameArray[nameArray.length-2], TotalAmount * 0.5,CurrentDate);
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