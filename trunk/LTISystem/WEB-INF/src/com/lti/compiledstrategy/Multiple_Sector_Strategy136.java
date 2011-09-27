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
public class Multiple_Sector_Strategy136 extends SimulateStrategy{
	public Multiple_Sector_Strategy136(){
		super();
		StrategyID=136L;
		StrategyClassID=9L;
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
	Date date;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("date: ");
		sb.append(date);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(date);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		date=(Date)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		String[] nameArray = {"VISVX", "XLE", "DISVX", "XLU", "EFA", "XLF", "VGSLX", "FSRFX", "XLV", "XLP", "FNARX", "USAGX", "VIMSX", "PRFDX"};

double Maxreturn;
int Maxreturnindex;

for(int i=nameArray.length-1; i>=1; i--)
{
 Maxreturn=getSecurity(nameArray[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6);
 Maxreturnindex=i;
  
   for(int j=i-1; j>=0; j--)
   { 
     double b;
     b=getSecurity(nameArray[j]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6);
     
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


double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset1");
CurrentAsset.setClassID(getAssetClassID(getSecurity (nameArray[13]).getName()));
CurrentAsset.setTargetPercentage(0.250);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset1", getSecurity (nameArray[13]).getName(), TotalAmount * 0.25, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[12]).getName()));
CurrentAsset.setTargetPercentage(0.25);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(nameArray[12]).getName(), TotalAmount * 0.25, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset3");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[11]).getName()));
CurrentAsset.setTargetPercentage(0.1);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset3", getSecurity(nameArray[11]).getName(), TotalAmount * 0.1, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset4");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[10]).getName()));
CurrentAsset.setTargetPercentage(0.1);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset4", getSecurity(nameArray[10]).getName(), TotalAmount * 0.1, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset5");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[9]).getName()));
CurrentAsset.setTargetPercentage(0.1);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset5", getSecurity(nameArray[9]).getName(), TotalAmount * 0.1, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset6");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[8]).getName()));
CurrentAsset.setTargetPercentage(0.1);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset6", getSecurity(nameArray[8]).getName(), TotalAmount * 0.1, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset7");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[7]).getName()));
CurrentAsset.setTargetPercentage(0.1);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset7", getSecurity(nameArray[7]).getName(), TotalAmount * 0.1, CurrentDate);

date=LTIDate.getNewDate(CurrentDate, TimeUnit.MONTHLY, 6);

printToLog(date.toString());

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
		
		 
		else if (LTIDate.equals(date,CurrentDate)) {
		   String[] nameArray = {"VISVX", "XLE", "DISVX", "XLU", "EFA", "XLF", "VGSLX", "FSRFX", "XLV", "XLP", "FNARX", "USAGX", "VIMSX", "PRFDX"};

double Maxreturn;
int Maxreturnindex;

for(int i=nameArray.length-1; i>=1; i--)
{
 Maxreturn=getSecurity(nameArray[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6);
 Maxreturnindex=i;
  
   for(int j=i-1; j>=0; j--)
   { 
     double b;
     b=getSecurity(nameArray[j]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6);
     
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

String log_m=date.toString()+"\r\n";
for(int i=0;i<nameArray.length;i++){
        log_m+=nameArray[i] +" ";
        log_m+=getSecurity(nameArray[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6);
        log_m+="\r\n";
       
}
 printToLog(log_m);

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset1");
CurrentAsset.setClassID(getAssetClassID(getSecurity (nameArray[13]).getName()));
CurrentAsset.setTargetPercentage(0.250);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset1", getSecurity (nameArray[13]).getName(), TotalAmount * 0.25, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[12]).getName()));
CurrentAsset.setTargetPercentage(0.25);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(nameArray[12]).getName(), TotalAmount * 0.25, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset3");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[11]).getName()));
CurrentAsset.setTargetPercentage(0.1);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset3", getSecurity(nameArray[11]).getName(), TotalAmount * 0.1, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset4");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[10]).getName()));
CurrentAsset.setTargetPercentage(0.1);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset4", getSecurity(nameArray[10]).getName(), TotalAmount * 0.1, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset5");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[9]).getName()));
CurrentAsset.setTargetPercentage(0.1);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset5", getSecurity(nameArray[9]).getName(), TotalAmount * 0.1, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset6");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[8]).getName()));
CurrentAsset.setTargetPercentage(0.1);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset6", getSecurity(nameArray[8]).getName(), TotalAmount * 0.1, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset7");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[7]).getName()));
CurrentAsset.setTargetPercentage(0.1);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset7", getSecurity(nameArray[7]).getName(), TotalAmount * 0.1, CurrentDate);

date=LTIDate.getNewDate(CurrentDate, TimeUnit.MONTHLY, 6);
printToLog(date.toString());

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