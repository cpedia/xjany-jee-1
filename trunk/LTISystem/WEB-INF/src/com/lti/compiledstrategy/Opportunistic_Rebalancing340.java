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
public class Opportunistic_Rebalancing340 extends SimulateStrategy{
	public Opportunistic_Rebalancing340(){
		super();
		StrategyID=340L;
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
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("NextDate: ");
		sb.append(NextDate);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(NextDate);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		NextDate=(Date)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		NextDate=LTIDate.getNewNYSEWeek(CurrentDate,interval);
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
		   String[] assetnameArray=new String[10];
String[] securitynameArray=new String[10];
double totalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
printToLog(NextDate);   
 
int num=0;

//get current portfolio's asset list and security list
List<Asset> list1=CurrentPortfolio.getCurrentAssetList();

for(int i=0; i<=list1.size()-1; i++)
   {
Asset as=list1.get(i);
assetnameArray[i]=as.getName();
List<HoldingItem> list2=as.getHoldingItems();
securitynameArray[i]=getSecurity(list2.get(0).getSecurityID()).getSymbol();
num++;
}

double[] originalPercentage=new double[num];
double[] currentPercentage=new double[num];
double[] bias=new double[num];
double[] assetAmountArray=new double[num];
double[] price=new double[num];
double[] amount=new double[num];
double[] ratio=new double[num];
double totalselling=0;

//calculate basic data  
for(int i=0; i<=num-1; i++)
{
originalPercentage[i]=CurrentPortfolio.getTargetPercentage(assetnameArray[i]);
assetAmountArray[i]=CurrentPortfolio.getAssetAmount(assetnameArray[i], CurrentDate);
currentPercentage[i]=assetAmountArray[i]/totalAmount;
bias[i]= currentPercentage[i]-originalPercentage[i];
ratio[i]=bias[i]/originalPercentage[i];
printToLog(bias[i]);  
}

//to sell and record totalselling
for(int i=0; i<=num-1; i++)
{
if(Math.abs(ratio[i])>=rebalanceband&& bias[i]>0)
      {
price[i]=getSecurity (securitynameArray[i]).getCurrentPrice(CurrentDate);
amount[i]=totalAmount*bias[i];
totalselling=totalselling+totalAmount*bias[i];
CurrentPortfolio.sell(assetnameArray[i], getSecurity(securitynameArray[i]).getName(),amount[i],CurrentDate);
printToLog("sell "+securitynameArray[i]+" "+amount[i]);     
 }
}

printToLog("totalsell"+totalselling);     

double totalbias=0;
double buyamount;
double[] weight=new double[num];

if(totalselling!=0)
{

//calculate totalbias
for(int i=0; i<=num-1; i++)
  {if(bias[i]<0)
   {totalbias=totalbias+Math.abs(bias[i]);}
  }

for(int i=0; i<=num-1; i++)
      {if(bias[i]<0)
          {weight[i]=Math.abs(bias[i])/totalbias;  
    price[i]=getSecurity(securitynameArray[i]).getCurrentPrice(CurrentDate); 
    buyamount=totalselling*weight[i];
    CurrentPortfolio.buy(assetnameArray[i],getSecurity(securitynameArray[i]).getName(),buyamount,CurrentDate);
     printToLog("buy "+securitynameArray[i]+" "+amount[i]);   
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