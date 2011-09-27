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
public class S_and_P_Sector_Rotation_Past_Winners214 extends SimulateStrategy{
	public S_and_P_Sector_Rotation_Past_Winners214(){
		super();
		StrategyID=214L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int PastPerformanceMonths;
	public void setPastPerformanceMonths(int PastPerformanceMonths){
		this.PastPerformanceMonths=PastPerformanceMonths;
	}
	
	public int getPastPerformanceMonths(){
		return this.PastPerformanceMonths;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		PastPerformanceMonths=(Integer)ParameterUtil.fetchParameter("int","PastPerformanceMonths", "3", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	String[] nameArray = {"XLP", "XLV", "XLE", "XLK", "XLI", "XLU", "XLB", "XLY", "CASH", "CASH"};
double Maxreturn;
int Maxreturnindex;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("nameArray: ");
		sb.append(nameArray);
		sb.append("\n");
		sb.append("Maxreturn: ");
		sb.append(Maxreturn);
		sb.append("\n");
		sb.append("Maxreturnindex: ");
		sb.append(Maxreturnindex);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(nameArray);
		stream.writeObject(Maxreturn);
		stream.writeObject(Maxreturnindex);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		nameArray=(String[])stream.readObject();;
		Maxreturn=(Double)stream.readObject();;
		Maxreturnindex=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		printToLog("initial starts\n");
for(int i=nameArray.length-1; i>=1; i--)
{
 Maxreturn=getSecurity(nameArray[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, 
                      -PastPerformanceMonths);
 Maxreturnindex=i;
  printToLog("loop "+i+"\n");
   for(int j=i-1; j>=0; j--)
   { 
     double b;
     b=getSecurity(nameArray[j]).getReturn(CurrentDate, TimeUnit.MONTHLY,  
                      -PastPerformanceMonths);
     printToLog("loop "+j+"\n");
     if(Maxreturn<b)
     {

      Maxreturn=b;
      Maxreturnindex=j;
     }
   }
   printToLog("loop "+Maxreturnindex+"\n");
   if(Maxreturnindex !=i)
   {
    
	 String tmp=nameArray[Maxreturnindex];
     nameArray[Maxreturnindex]=nameArray[i];
     nameArray[i]=tmp;
   }
}

String log_m="After Ordering " +"\r\n";
for(int i=0;i<nameArray.length;i++){
        log_m+=nameArray[i] +" ";
        log_m+=getSecurity(nameArray[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -PastPerformanceMonths);
        log_m+="\r\n";
       
}
 printToLog(log_m);

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset1");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[nameArray.length-1]).getName()));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset1", getSecurity(nameArray[nameArray.length-1]).getName(), TotalAmount *0.5, CurrentDate);


CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[nameArray.length-2]).getName()));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(nameArray[nameArray.length-2]).getName(), TotalAmount * 0.5, CurrentDate);

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
		   for(int i=nameArray.length-1; i>=1; i--)
{
 Maxreturn=getSecurity(nameArray[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, 
                      -PastPerformanceMonths);
 Maxreturnindex=i;
  
   for(int j=i-1; j>=0; j--)
   { 
     double b;
     b=getSecurity(nameArray[j]).getReturn(CurrentDate, TimeUnit.MONTHLY,  
                      -PastPerformanceMonths);
     
     if(Maxreturn<b)
     {

      Maxreturn=b;
      Maxreturnindex=j;
     }
   }
   
   if(Maxreturnindex !=i)
   {
    
	 String tmp=nameArray[Maxreturnindex];
     nameArray[Maxreturnindex]=nameArray[i];
     nameArray[i]=tmp;
   }
}

String log_m="After Ordering " +"\r\n";
for(int i=0;i<nameArray.length;i++){
        log_m+=nameArray[i] +" ";
        log_m+=getSecurity(nameArray[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -PastPerformanceMonths);
        log_m+="\r\n";
       
}
 printToLog(log_m);

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset1");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[nameArray.length-1]).getName()));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset1", getSecurity(nameArray[nameArray.length-1]).getName(), TotalAmount *0.5, CurrentDate);


CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[nameArray.length-2]).getName()));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(nameArray[nameArray.length-2]).getName(), TotalAmount * 0.5, CurrentDate);
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