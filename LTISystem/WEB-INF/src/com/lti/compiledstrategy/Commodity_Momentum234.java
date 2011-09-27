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
public class Commodity_Momentum234 extends SimulateStrategy{
	public Commodity_Momentum234(){
		super();
		StrategyID=234L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int PastPerformance;
	public void setPastPerformance(int PastPerformance){
		this.PastPerformance=PastPerformance;
	}
	
	public int getPastPerformance(){
		return this.PastPerformance;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		PastPerformance=(Integer)ParameterUtil.fetchParameter("int","PastPerformance", "1", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	int PastPerformanceMonths;
String[] nameArray = {"DBA", "DBB", "DBE", "DBP", "DBS", "DGL", "TLT", "CASH"};
double Maxreturn;
int Maxreturnindex;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("PastPerformanceMonths: ");
		sb.append(PastPerformanceMonths);
		sb.append("\n");
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
		stream.writeObject(PastPerformanceMonths);
		stream.writeObject(nameArray);
		stream.writeObject(Maxreturn);
		stream.writeObject(Maxreturnindex);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		PastPerformanceMonths=(Integer)stream.readObject();;
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
		PastPerformanceMonths=PastPerformance==1?6:3;
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
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[6]).getName()));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset1", getSecurity(nameArray[6]).getName(), TotalAmount *0.5, CurrentDate);


CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[5]).getName()));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(nameArray[5]).getName(), TotalAmount * 0.5, CurrentDate);

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
		
		 
		else if (isMonthEnd(CurrentDate)) {
		   PastPerformanceMonths=PastPerformance==1?6:3;
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
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[6]).getName()));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset1", getSecurity(nameArray[6]).getName(), TotalAmount *0.5, CurrentDate);


CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[5]).getName()));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(nameArray[5]).getName(), TotalAmount * 0.5, CurrentDate);
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