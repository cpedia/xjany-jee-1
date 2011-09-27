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
public class Seven_Sector_Strategy133 extends SimulateStrategy{
	public Seven_Sector_Strategy133(){
		super();
		StrategyID=133L;
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
		double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Real estate");
CurrentAsset.setClassID(getAssetClassID("US equity"));
CurrentAsset.setTargetPercentage(1.0/7.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "FRESX", TotalAmount * 1.0/7.0, CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Food and agriculture");
CurrentAsset.setClassID(getAssetClassID("US equity"));
CurrentAsset.setTargetPercentage(1.0/7.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "FDFAX", TotalAmount * 1.0/7.0, CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Financials");
CurrentAsset.setClassID(getAssetClassID("US equity"));
CurrentAsset.setTargetPercentage(1.0/7.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "FSPCX", TotalAmount * 1.0/7.0, CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Utilities");
CurrentAsset.setClassID(getAssetClassID("US equity"));
CurrentAsset.setTargetPercentage(1.0/7.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "FKUTX", TotalAmount * 1.0/7.0, CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Energy");
CurrentAsset.setClassID(getAssetClassID("US equity"));
CurrentAsset.setTargetPercentage(1.0/7.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGENX", TotalAmount * 1.0/7.0, CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Health");
CurrentAsset.setClassID(getAssetClassID("US equity"));
CurrentAsset.setTargetPercentage(1.0/7.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGHCX", TotalAmount * 1.0/7.0, CurrentDate);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Leisure");
CurrentAsset.setClassID(getAssetClassID("US equity"));
CurrentAsset.setTargetPercentage(1.0/7.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "FLISX", TotalAmount * 1.0/7.0, CurrentDate);

date=LTIDate.getNewNYSEMonth(CurrentDate, 6);

    while(!LTIDate.isNYSETradingDay(date))
    {
       date=LTIDate.add(date, 1);
    }


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
		   String[] nameArray = {"FRESX", "FDFAX", "FSPCX", "FKUTX", "VGENX", "VGHCX", "FLISX"};


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

String log_m="After Ordering " + date.toString()+"\r\n";
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
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[6]).getName()));
CurrentAsset.setTargetPercentage(2.0/7.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset1", getSecurity(nameArray[6]).getName(), TotalAmount * 2/7, CurrentDate);


CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[5]).getName()));
CurrentAsset.setTargetPercentage(2.0/7.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(nameArray[5]).getName(), TotalAmount * 2/7, CurrentDate);


CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset3");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[4]).getName()));
CurrentAsset.setTargetPercentage(1.0/7.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset3", getSecurity(nameArray[4]).getName(), TotalAmount * 1/7, CurrentDate);


CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset4");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[3]).getName()));
CurrentAsset.setTargetPercentage(1.0/7.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset4", getSecurity(nameArray[3]).getName(), TotalAmount * 1/7, CurrentDate);


CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset5");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[2]).getName()));
CurrentAsset.setTargetPercentage(1.0/7.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset5", getSecurity(nameArray[2]).getName(), TotalAmount * 1/7, CurrentDate);

date=LTIDate.getNewNYSEMonth(CurrentDate, 6);

    while(!LTIDate.isNYSETradingDay(date))
    {
       date=LTIDate.add(date, 1);
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