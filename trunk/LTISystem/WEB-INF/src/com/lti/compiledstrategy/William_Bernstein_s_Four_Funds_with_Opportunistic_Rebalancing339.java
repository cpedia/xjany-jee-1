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
public class William_Bernstein_s_Four_Funds_with_Opportunistic_Rebalancing339 extends SimulateStrategy{
	public William_Bernstein_s_Four_Funds_with_Opportunistic_Rebalancing339(){
		super();
		StrategyID=339L;
		StrategyClassID=4L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private double rebalanceband;
	public void setRebalanceband(double rebalanceband){
		this.rebalanceband=rebalanceband;
	}
	
	public double getRebalanceband(){
		return this.rebalanceband;
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
		rebalanceband=(Double)ParameterUtil.fetchParameter("double","rebalanceband", "0.2", parameters);
		interval=(Integer)ParameterUtil.fetchParameter("int","interval", "2", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date NextDate;
String[] assetnameArray= {"total", "small", "foreign", "bonds"};
String[] securitynameArray= {"VFINX", "NAESX", "VTMGX", "VBMFX"};
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("NextDate: ");
		sb.append(NextDate);
		sb.append("\n");
		sb.append("assetnameArray: ");
		sb.append(assetnameArray);
		sb.append("\n");
		sb.append("securitynameArray: ");
		sb.append(securitynameArray);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(NextDate);
		stream.writeObject(assetnameArray);
		stream.writeObject(securitynameArray);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		NextDate=(Date)stream.readObject();;
		assetnameArray=(String[])stream.readObject();;
		securitynameArray=(String[])stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		double originalAmount= CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;


CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("total");
CurrentAsset.setClassID(getAssetClassID(getSecurity (securitynameArray[0]).getName()));
CurrentAsset.setTargetPercentage(0.25);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(assetnameArray[0], getSecurity (securitynameArray[0]).getName(), originalAmount*0.25, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("small");
CurrentAsset.setClassID(getAssetClassID(getSecurity(securitynameArray[1]).getName()));
CurrentAsset.setTargetPercentage(0.25);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(assetnameArray[1], getSecurity(securitynameArray[1]).getName(), originalAmount*0.25, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("foreign");
CurrentAsset.setClassID(getAssetClassID(getSecurity(securitynameArray[2]).getName()));
CurrentAsset.setTargetPercentage(0.25);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(assetnameArray[2], getSecurity(securitynameArray[2]).getName(),originalAmount *0.25, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("bonds");
CurrentAsset.setClassID(getAssetClassID(getSecurity(securitynameArray[3]).getName()));
CurrentAsset.setTargetPercentage(0.25);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(assetnameArray[3], getSecurity(securitynameArray[3]).getName(),originalAmount *0.25, CurrentDate);

NextDate=LTIDate.getNewNYSEWeek(CurrentDate,interval);
printToLog(NextDate);   
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
		   double totalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
printToLog(NextDate);   

double[] originalPercentage=new double[4];
double[] currentPercentage=new double[4];
double[] bias=new double[4];
double[] assetAmountArray=new double[4];
double[] price=new double[4];
double[] amount=new double[4];
double[] ratio=new double[4];
double totalselling=0;

//calculate basic data  
for(int i=0; i<=securitynameArray.length-1; i++)
{
originalPercentage[i]=CurrentPortfolio.getTargetPercentage(assetnameArray[i]);
assetAmountArray[i]=CurrentPortfolio.getAssetAmount(assetnameArray[i], CurrentDate);
currentPercentage[i]=assetAmountArray[i]/totalAmount;
bias[i]= currentPercentage[i]-originalPercentage[i];
ratio[i]=bias[i]/originalPercentage[i];
printToLog(bias[i]);  
}

//to sell and record totalselling
for(int i=0; i<=securitynameArray.length-1; i++)
{
if(Math.abs(ratio[i])>=rebalanceband&& bias[i]>0)
      {
price[i]= getSecurity (securitynameArray[i]).getCurrentPrice(CurrentDate);
amount[i]=totalAmount*bias[i]/price[i];
totalselling=totalselling+totalAmount*bias[i];
CurrentPortfolio.sell(assetnameArray[i], getSecurity(securitynameArray[i]).getName(),amount[i],CurrentDate);
printToLog("sell");     
 }
}

printToLog("totalsell"+totalselling);     

double totalbias=0;
double buyamount;
double[] weight=new double[4];

if(totalselling!=0)
{

//calculate totalbias
for(int i=0; i<=securitynameArray.length-1; i++)
  {if(bias[i]<0)
   {totalbias=totalbias+Math.abs(bias[i]);}
  }

for(int i=0; i<=securitynameArray.length-1; i++)
      {if(bias[i]<0)
          {weight[i]=Math.abs(bias[i])/totalbias;  
    price[i]=getSecurity(securitynameArray[i]).getCurrentPrice(CurrentDate); 
    buyamount=totalselling*weight[i]/price[i];
    CurrentPortfolio.buy(assetnameArray[i],getSecurity(securitynameArray[i]).getName(),buyamount,CurrentDate);
     printToLog("buy");   
            }
      }

}
NextDate=LTIDate.getNewNYSEWeek(CurrentDate,interval);
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