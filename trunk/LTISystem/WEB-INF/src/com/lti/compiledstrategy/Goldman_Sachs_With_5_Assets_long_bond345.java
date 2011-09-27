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
public class Goldman_Sachs_With_5_Assets_long_bond345 extends SimulateStrategy{
	public Goldman_Sachs_With_5_Assets_long_bond345(){
		super();
		StrategyID=345L;
		StrategyClassID=0L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int AdjustFrequency;
	public void setAdjustFrequency(int AdjustFrequency){
		this.AdjustFrequency=AdjustFrequency;
	}
	
	public int getAdjustFrequency(){
		return this.AdjustFrequency;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		AdjustFrequency=(Integer)ParameterUtil.fetchParameter("int","AdjustFrequency", "2", parameters);
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
		String[] nameArray = {"VFINX", "VGTSX", "VGSIX", "QRAAX", "VUSTX"};

printToLog("In Initial\n");
double Maxreturn;
int Maxreturnindex;

for(int i=nameArray.length-1; i>=1; i--)
{
 Maxreturn=getSecurity(nameArray[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12);
 Maxreturnindex=i;
  
   for(int j=i-1; j>=0; j--)
   { 
     double b;
     b=getSecurity(nameArray[j]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12);
     
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

String log_m="After Ordering " + CurrentDate.toString()+"\r\n";
for(int i=0;i<nameArray.length;i++){
        log_m+=nameArray[i] +" ";
        log_m+=getSecurity(nameArray[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12);
        log_m+="\r\n";
       
}
 printToLog(log_m);


double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset1");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[5]).getName()));
CurrentAsset.setTargetPercentage(1.0/3.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset1", getSecurity(nameArray[5]).getName(), TotalAmount * 1/3, CurrentDate);


CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[4]).getName()));
CurrentAsset.setTargetPercentage(1.0/3.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(nameArray[4]).getName(), TotalAmount * 1/3, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset3");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[3]).getName()));
CurrentAsset.setTargetPercentage(1.0/3.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset3", getSecurity(nameArray[3]).getName(), TotalAmount * 1/3, CurrentDate);


date=LTIDate.getNewNYSEMonth(CurrentDate, AdjustFrequency==2 ? 12: (AdjustFrequency == 1? 6: (AdjustFrequency==0)?3:1));

    while(!LTIDate.isNYSETradingDay(date))
    {
       date=LTIDate.add(date, 1);
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
		
		 
		else if (LTIDate.equals(date, CurrentDate)) {
		   String[] nameArray = {"VFINX", "VGTSX", "VGSIX", "QRAAX", "VBMFX", "VFISX"};


double Maxreturn;
int Maxreturnindex;

for(int i=nameArray.length-1; i>=1; i--)
{
 Maxreturn=getSecurity(nameArray[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12);
 Maxreturnindex=i;
  
   for(int j=i-1; j>=0; j--)
   { 
     double b;
     b=getSecurity(nameArray[j]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12);
     
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

String log_m="After Ordering " + CurrentDate.toString()+"\r\n";
for(int i=0;i<nameArray.length;i++){
        log_m+=nameArray[i] +" ";
        log_m+=getSecurity(nameArray[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12);
        log_m+="\r\n";
       
}
 printToLog(log_m);


double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset1");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[5]).getName()));
CurrentAsset.setTargetPercentage(1.0/3.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset1", getSecurity(nameArray[5]).getName(), TotalAmount * 1/3, CurrentDate);


CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[4]).getName()));
CurrentAsset.setTargetPercentage(1.0/3.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(nameArray[4]).getName(), TotalAmount * 1/3, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset3");
CurrentAsset.setClassID(getAssetClassID(getSecurity(nameArray[3]).getName()));
CurrentAsset.setTargetPercentage(1.0/3.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset3", getSecurity(nameArray[3]).getName(), TotalAmount * 1/3, CurrentDate);

date=LTIDate.getNewNYSEMonth(CurrentDate, AdjustFrequency==2 ? 12: (AdjustFrequency == 1? 6: (AdjustFrequency==0)?3:1));

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