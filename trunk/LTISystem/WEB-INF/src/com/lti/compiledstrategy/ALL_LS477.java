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
public class ALL_LS477 extends SimulateStrategy{
	public ALL_LS477(){
		super();
		StrategyID=477L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String AdjustFrequency;
	public void setAdjustFrequency(String AdjustFrequency){
		this.AdjustFrequency=AdjustFrequency;
	}
	
	public String getAdjustFrequency(){
		return this.AdjustFrequency;
	}
	private String [] securitynameArray;
	public void setSecuritynameArray(String [] securitynameArray){
		this.securitynameArray=securitynameArray;
	}
	
	public String [] getSecuritynameArray(){
		return this.securitynameArray;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		AdjustFrequency=(String)ParameterUtil.fetchParameter("String","AdjustFrequency", "month", parameters);
		securitynameArray=(String [])ParameterUtil.fetchParameter("String []","securitynameArray", "VFINX, VGTSX, VGSIX, QRAAX, VUSTX", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	String[] assetnameArray; //={"Agriculture", "Gold", "Oil", "Euro","Japanese Yen"};

//String[] securitynameArray={"DBA", "GLD", "USO", "FXE", "FXY"};
//String[] securitynameArray={"SPY", "EFA", "IYR", "QRAAX", "TLT"};
//String[] securitynameArray={"VFINX", "VGTSX", "VGSIX", "QRAAX", "VUSTX"};

double[] percentageofall; 
//={0.20, 0.20, 0.20, 0.20, 0.20};

boolean[] position; //={false,false,false,false,false};
double originalTotalamount;
Integer interval;
Integer iTotalSecurities;

	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("assetnameArray: ");
		sb.append(assetnameArray);
		sb.append("\n");
		sb.append("percentageofall: ");
		sb.append(percentageofall);
		sb.append("\n");
		sb.append("position: ");
		sb.append(position);
		sb.append("\n");
		sb.append("originalTotalamount: ");
		sb.append(originalTotalamount);
		sb.append("\n");
		sb.append("interval: ");
		sb.append(interval);
		sb.append("\n");
		sb.append("iTotalSecurities: ");
		sb.append(iTotalSecurities);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(assetnameArray);
		stream.writeObject(percentageofall);
		stream.writeObject(position);
		stream.writeObject(originalTotalamount);
		stream.writeObject(interval);
		stream.writeObject(iTotalSecurities);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		assetnameArray=(String[])stream.readObject();;
		percentageofall=(double[])stream.readObject();;
		position=(boolean[])stream.readObject();;
		originalTotalamount=(Double)stream.readObject();;
		interval=(Integer)stream.readObject();;
		iTotalSecurities=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		Security S;
Asset CurrentAsset;
double TotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);

iTotalSecurities = securitynameArray.length;
percentageofall=new double[iTotalSecurities];
assetnameArray = new String[iTotalSecurities];
position = new boolean[iTotalSecurities]; 

for (int i=0; i<iTotalSecurities; i++) {
percentageofall[i] = 1.0/iTotalSecurities;
assetnameArray[i]="Asset"+i;
position[i]=false;
}
originalTotalamount=TotalAmount;
CurrentPortfolio.sellAssetCollection(CurrentDate);

for (int i=0; i<iTotalSecurities; i++) {
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(assetnameArray[i]);
CurrentAsset.setClassID(getAssetClassID("COMMODITIES"));
CurrentAsset.setTargetPercentage(percentageofall[i]);
CurrentPortfolio.addAsset(CurrentAsset); 
}
/*	
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(assetnameArray[1]);
CurrentAsset.setClassID(getAssetClassID("COMMODITIES"));
CurrentAsset.setTargetPercentage(percentageofall[1]);
CurrentPortfolio.addAsset(CurrentAsset);


CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(assetnameArray[2]);
CurrentAsset.setClassID(getAssetClassID("COMMODITIES"));
CurrentAsset.setTargetPercentage(percentageofall[2]);
CurrentPortfolio.addAsset(CurrentAsset);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(assetnameArray[3]);
CurrentAsset.setClassID(getAssetClassID("FIXED INCOME"));
CurrentAsset.setTargetPercentage(percentageofall[3]);
CurrentPortfolio.addAsset(CurrentAsset);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(assetnameArray[4]);
CurrentAsset.setClassID(getAssetClassID("FIXED INCOME"));
CurrentAsset.setTargetPercentage(percentageofall[4]);
CurrentPortfolio.addAsset(CurrentAsset);
*/

for(int i=0; i<iTotalSecurities; i++){
   double ema=0;

   S=getSecurity(securitynameArray[i]);
   ema = S.getEMA(CurrentDate, 7, TimeUnit.MONTHLY,false);

   if (S.getAdjClose(CurrentDate)>=ema) {
      CurrentPortfolio.buy(assetnameArray[i], securitynameArray[i], TotalAmount*percentageofall[i],
         CurrentDate);
      position[i]=true;
      printToLog(assetnameArray[i]+" is long");
    } else{
       CurrentPortfolio.shortSell(assetnameArray[i],securitynameArray[i],
           TotalAmount*percentageofall[i],CurrentDate);                  
       printToLog(assetnameArray[i]+" is short");
   }
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
	
		
		 double[] ema=new double[100];
double[] currentpriceinput=new double[100];
Security S;
//get basic data for later use
for(int i=0; i<iTotalSecurities;i++)
{
    S= getSecurity(securitynameArray[i]);
   currentpriceinput[i] = S.getAdjClose(CurrentDate);
   ema[i] = S.getEMA(CurrentDate, 7, TimeUnit.MONTHLY,false);
   if (currentpriceinput[i]>=ema[i]) {
      printToLog(assetnameArray[i]+" long");
   } else {
     printToLog(assetnameArray[i]+" short");
   }

   
}

		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) {
		   //double[] ema=new double[100];
//double[] currentpriceinput=new double[100];
double[] assetAmount=new double[100];
double[] targetAmount=new double[100];
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
//Security S;

//get basic data for later use
for(int i=0; i<iTotalSecurities;i++)
{
   printToLog("doing "+i);
   assetAmount[i]=CurrentPortfolio.getAssetAmount(assetnameArray[i],CurrentDate);
   targetAmount[i]=TotalAmount*percentageofall[i];

   S= getSecurity(securitynameArray[i]);
   currentpriceinput[i] = S.getAdjClose(CurrentDate);
   ema[i] = S.getEMA(CurrentDate, 7, TimeUnit.MONTHLY,false);
   
}

 //Judge by position determination to sell first
 for(int i=0;i<iTotalSecurities;i++) {
   //1.long to long with amount percentage larger than before
   if((position[i]==true)&&(currentpriceinput[i]>=ema[i])&&(assetAmount[i]>targetAmount[i])) {
       CurrentPortfolio.sell(assetnameArray[i],securitynameArray[i],
         assetAmount[i]-targetAmount[i],CurrentDate);
       printToLog(assetnameArray[i]+" goes long from long ");
       printToLog("position:"+position[i]);
       printToLog("currentpriceinput:"+currentpriceinput[i]);
       printToLog("ema:"+ema[i]);
       printToLog("assetAmount:"+assetAmount[i]);
       printToLog("targetAmount"+targetAmount[i]);
   }
 }


//2.long to short
for(int i=0;i<iTotalSecurities;i++) {
  if((position[i]==true)&&(currentpriceinput[i]<ema[i])) {
    CurrentPortfolio.sellAsset(assetnameArray[i], CurrentDate);
    //CurrentPortfolio.sell(assetnameArray[i],securitynameArray[i],assetAmount[i],CurrentDate);
    CurrentPortfolio.shortSell(assetnameArray[i],securitynameArray[i],targetAmount[i],CurrentDate); 
    printToLog(assetnameArray[i]+" goes short from long");
    printToLog("position:"+position[i]);
    printToLog("currentpriceinput:"+currentpriceinput[i]);
    printToLog("ema:"+ema[i]);
    printToLog("assetAmount:"+assetAmount[i]);
    printToLog("targetAmount"+targetAmount[i]);
  }
}

// short to smaller short
for(int i=0;i<iTotalSecurities;i++) {
   if((position[i]==false)&&(currentpriceinput[i]<ema[i])) {
     printToLog(assetnameArray[i]+" goes short from short");
     double bias=targetAmount[i]+assetAmount[i];
     if(bias>0) {
       CurrentPortfolio.shortSell(assetnameArray[i],securitynameArray[i],bias,CurrentDate);  
     }
     printToLog("bias:"+bias);
     printToLog("position:"+position[i]);
     printToLog("currentpriceinput:"+currentpriceinput[i]);
     printToLog("ema:"+ema[i]);
     printToLog("assetAmount:"+assetAmount[i]);
     printToLog("targetAmount"+targetAmount[i]);
  }
}

//3.long to long with amount percentage smaller than before to buy later
for(int i=0;i<iTotalSecurities;i++) {
  if((position[i]==true)&&(currentpriceinput[i]>=ema[i])&&(assetAmount[i]<targetAmount[i])) {
    CurrentPortfolio.buy(assetnameArray[i],securitynameArray[i],
        targetAmount[i]-assetAmount[i],CurrentDate);
    printToLog(assetnameArray[i]+" goes long from long");
    printToLog("position:"+position[i]);
    printToLog("currentpriceinput:"+currentpriceinput[i]);
    printToLog("ema:"+ema[i]);
    printToLog("assetAmount:"+assetAmount[i]);
    printToLog("targetAmount"+targetAmount[i]);
  }
}

//4.short to long 

for(int i=0;i<iTotalSecurities;i++) {
  if((position[i]==false)&&(currentpriceinput[i]>=ema[i])) {
    CurrentPortfolio.buy(assetnameArray[i],  
      securitynameArray[i],targetAmount[i]-assetAmount[i],CurrentDate);
    printToLog(assetnameArray[i]+" goes long from short");
    printToLog("position:"+position[i]);
    printToLog("currentpriceinput:"+currentpriceinput[i]);
    printToLog("ema:"+ema[i]);
    printToLog("assetAmount:"+assetAmount[i]);
    printToLog("targetAmount"+targetAmount[i]);
  }
}

//5.short to short 

for(int i=0;i<iTotalSecurities;i++) {
   if((position[i]==false)&&(currentpriceinput[i]<ema[i])) {
     printToLog(assetnameArray[i]+" goes short from short");
     double bias=targetAmount[i]+assetAmount[i];
     if(bias<0){
        CurrentPortfolio.buy(assetnameArray[i], securitynameArray[i], -bias,CurrentDate);
      }
     printToLog("bias:"+bias);
     printToLog("position:"+position[i]);
     printToLog("currentpriceinput:"+currentpriceinput[i]);
     printToLog("ema:"+ema[i]);
     printToLog("assetAmount:"+assetAmount[i]);
     printToLog("targetAmount"+targetAmount[i]);
  }
}

//update position for every sector
for(int i=0;i<iTotalSecurities;i++) {
  if(currentpriceinput[i]>=ema[i]) {
    position[i]=true;
  } else {
    position[i]=false;
  }
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