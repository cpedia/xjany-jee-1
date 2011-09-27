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
public class S_and_P_Commodity_Trend_Indicators476 extends SimulateStrategy{
	public S_and_P_Commodity_Trend_Indicators476(){
		super();
		StrategyID=476L;
		StrategyClassID=14L;
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
	private String intervalofEMA;
	public void setIntervalofEMA(String intervalofEMA){
		this.intervalofEMA=intervalofEMA;
	}
	
	public String getIntervalofEMA(){
		return this.intervalofEMA;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		AdjustFrequency=(String)ParameterUtil.fetchParameter("String","AdjustFrequency", "month", parameters);
		intervalofEMA=(String)ParameterUtil.fetchParameter("String","intervalofEMA", "month", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	String[] assetnameArray={"Energy", "Industrial metal", "Precious metal",  "Agriculture"};

String[] securitynameArray={"DBE","DBB","DBP","DBA"};

double[] percentageofall={0.375, 0.1, 0.105, 0.42};

boolean[] position={false,false,false,false};
double originalTotalamount;
Integer interval;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("assetnameArray: ");
		sb.append(assetnameArray);
		sb.append("\n");
		sb.append("securitynameArray: ");
		sb.append(securitynameArray);
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
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(assetnameArray);
		stream.writeObject(securitynameArray);
		stream.writeObject(percentageofall);
		stream.writeObject(position);
		stream.writeObject(originalTotalamount);
		stream.writeObject(interval);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		assetnameArray=(String[])stream.readObject();;
		securitynameArray=(String[])stream.readObject();;
		percentageofall=(double[])stream.readObject();;
		position=(boolean[])stream.readObject();;
		originalTotalamount=(Double)stream.readObject();;
		interval=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		Asset CurrentAsset;
double TotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
originalTotalamount=TotalAmount;
CurrentPortfolio.sellAssetCollection(CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(assetnameArray[0]);
CurrentAsset.setClassID(getAssetClassID("COMMODITIES"));
CurrentAsset.setTargetPercentage(percentageofall[0]);
CurrentPortfolio.addAsset(CurrentAsset);
		
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
CurrentAsset.setClassID(getAssetClassID("COMMODITIES"));
CurrentAsset.setTargetPercentage(percentageofall[3]);
CurrentPortfolio.addAsset(CurrentAsset);

for(int i=0; i<=3; i++){
double prior1=0;
double prior2;
double prior3;
double prior4;
double prior5;
double prior6;
double prior7;
double ema=0;

if(intervalofEMA.equals("month"))
{
prior1=getSecurity(securitynameArray[i]).getAdjClose(CurrentDate);
prior2=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.MONTHLY,-1));
prior3=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.MONTHLY,-2));
prior4=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.MONTHLY,-3));
prior5=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.MONTHLY,-4));
prior6=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.MONTHLY,-5));
prior7=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.MONTHLY,-6));

ema=prior7*0.0232+prior6*0.0371+prior5*0.0594+prior4*0.0951+prior3*0.1522
+prior2*0.2434+prior1*0.3895;
}

if(intervalofEMA.equals("halfmonth"))
{
prior1=getSecurity(securitynameArray[i]).getAdjClose(CurrentDate);
prior2=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-2));
prior3=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-4));
prior4=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-6));
prior5=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-8));
prior6=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-10));
prior7=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-12));

ema=prior7*0.0232+prior6*0.0371+prior5*0.0594+prior4*0.0951+prior3*0.1522
+prior2*0.2434+prior1*0.3895;
}

if(intervalofEMA.equals("week"))
{
prior1=getSecurity(securitynameArray[i]).getAdjClose(CurrentDate);
prior2=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-1));
prior3=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-2));
prior4=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-3));
prior5=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-4));
prior6=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-5));
prior7=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-6));

ema=prior7*0.0232+prior6*0.0371+prior5*0.0594+prior4*0.0951+prior3*0.1522
+prior2*0.2434+prior1*0.3895;
}

if (prior1>=ema)
{
CurrentPortfolio.buy(assetnameArray[i], securitynameArray[i], TotalAmount*percentageofall[i],
CurrentDate);
position[i]=true;
printToLog(assetnameArray[i]+" is long");
}

else{

if(i==0){
CurrentPortfolio.buy(assetnameArray[i],"CASH",TotalAmount*percentageofall[i],CurrentDate);
position[i]=false;
printToLog(assetnameArray[i]+" goes to CASH");
}
else{
CurrentPortfolio.shortSell(assetnameArray[i],securitynameArray[i],TotalAmount*percentageofall[i],CurrentDate);                  
printToLog(assetnameArray[i]+" is short");
}

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
	
		
		
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if ((AdjustFrequency.equals("year") && LTIDate.isLastNYSETradingDayOfYear(CurrentDate))
||(AdjustFrequency.equals("quarter") && LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate)) 
||(AdjustFrequency.equals("month") && LTIDate.isLastNYSETradingDayOfMonth(CurrentDate))) {
		   double[] ema=new double[4];
double[] currentpriceinput=new double[4];
double[] assetAmount=new double[4];
double[] targetAmount=new double[4];
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
double prior1, prior2, prior3, prior4, prior5, prior6, prior7;

//get basic data for later use
for(int i=0; i<=3; i++)
{
assetAmount[i]=CurrentPortfolio.getAssetAmount(assetnameArray[i],CurrentDate);
targetAmount[i]=TotalAmount*percentageofall[i];

if(intervalofEMA.equals("month"))
{
prior1=getSecurity(securitynameArray[i]).getAdjClose(CurrentDate);
prior2=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.MONTHLY,-1));
prior3=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.MONTHLY,-2));
prior4=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.MONTHLY,-3));
prior5=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.MONTHLY,-4));
prior6=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.MONTHLY,-5));
prior7=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.MONTHLY,-6));

ema[i]=prior7*0.0232+prior6*0.0371+prior5*0.0594+prior4*0.0951+prior3*0.1522
+prior2*0.2434+prior1*0.3895;
currentpriceinput[i] = prior1;
}

if(intervalofEMA.equals("halfmonth"))
{
prior1=getSecurity(securitynameArray[i]).getAdjClose(CurrentDate);
prior2=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-2));
prior3=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-4));
prior4=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-6));
prior5=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-8));
prior6=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-10));
prior7=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-12));

ema[i]=prior7*0.0232+prior6*0.0371+prior5*0.0594+prior4*0.0951+prior3*0.1522
+prior2*0.2434+prior1*0.3895;
currentpriceinput[i] = prior1;
}

if(intervalofEMA.equals("week"))
{
prior1=getSecurity(securitynameArray[i]).getAdjClose(CurrentDate);
prior2=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-1));
prior3=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-2));
prior4=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-3));
prior5=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-4));
prior6=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-5));
prior7=getSecurity(securitynameArray[i]).getAdjClose(LTIDate.getNewTradingDate(CurrentDate,TimeUnit.WEEKLY,-6));

ema[i]=prior7*0.0232+prior6*0.0371+prior5*0.0594+prior4*0.0951+prior3*0.1522
+prior2*0.2434+prior1*0.3895;
currentpriceinput[i] = prior1;
}
}

//Judge by position determination to sell first: SELLs first and then BUYs
/* 6 cases
L to SL (long to small long)
L to S (long to short)
S to BS (short to big short)
L to BL (long to big long)
S to L (short to long)
S to SS (short to small short)
*/
//1. L to SL
for(int i=0;i<=3; i++)
{
//1.long to long with amount percentage larger than before
if((position[i]==true)&&(currentpriceinput[i]>=ema[i])&&(assetAmount[i]>targetAmount[i]))
{
CurrentPortfolio.sell(assetnameArray[i],securitynameArray[i],assetAmount[i]-targetAmount[i],CurrentDate);
printToLog(assetnameArray[i]+" goes long from long ");
printToLog("position:"+position[i]);
printToLog("currentpriceinput:"+currentpriceinput[i]);
printToLog("ema:"+ema[i]);
printToLog("assetAmount:"+assetAmount[i]);
printToLog("targetAmount"+targetAmount[i]);
}
}

//2.L to S

//Asset 0 long to cash
if((position[0]==true)&&(currentpriceinput[0]<ema[0]))
{
CurrentPortfolio.sellAsset(assetnameArray[0], CurrentDate);
//CurrentPortfolio.sell(assetnameArray[0],securitynameArray[0],assetAmount[0],CurrentDate);
/* buy later CurrentPortfolio.buy(assetnameArray[0],"CASH",targetAmount[0],CurrentDate);
printToLog(assetnameArray[0]+" goes for CASH from long"); */
printToLog("position:"+position[0]);
printToLog("currentpriceinput:"+currentpriceinput[0]);
printToLog("ema:"+ema[0]);
printToLog("assetAmount:"+assetAmount[0]);
printToLog("targetAmount"+targetAmount[0]);
}

//normal cases
for(int i=1;i<=3; i++)
{
if((position[i]==true)&&(currentpriceinput[i]<ema[i]))
{
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

//3. S to BS short to bigger short 
for(int i=1;i<=3; i++)
{
if((position[i]==false)&&(currentpriceinput[i]<ema[i]))
{
printToLog(assetnameArray[i]+" goes short from short");
//double bias=targetAmount[i]-(originalTotalamount*percentageofall[i]*2+assetAmount[i]);
double bias=targetAmount[i]+assetAmount[i];
if(bias>0)
{CurrentPortfolio.shortSell(assetnameArray[i],securitynameArray[i],bias,CurrentDate);  
 }
printToLog("bias:"+bias);
printToLog("position:"+position[i]);
printToLog("currentpriceinput:"+currentpriceinput[i]);
printToLog("ema:"+ema[i]);
printToLog("assetAmount:"+assetAmount[i]);
printToLog("targetAmount"+targetAmount[i]);
}
}


//4. L to BL
for(int i=0;i<=3; i++)
{
if((position[i]==true)&&(currentpriceinput[i]>=ema[i])&&(assetAmount[i]<targetAmount[i]))
{
CurrentPortfolio.buy(assetnameArray[i],securitynameArray[i],targetAmount[i]-assetAmount[i],CurrentDate);
printToLog(assetnameArray[i]+" goes long from long");
printToLog("position:"+position[i]);
printToLog("currentpriceinput:"+currentpriceinput[i]);
printToLog("ema:"+ema[i]);
printToLog("assetAmount:"+assetAmount[i]);
printToLog("targetAmount"+targetAmount[i]);
}
}

//5.S to L short to long 

//i=0 needs to be treated seperately
if((position[0]==false)&&(currentpriceinput[0]>=ema[0]))
{
// already sold before CurrentPortfolio.sellAsset(assetnameArray[0], CurrentDate);
//CurrentPortfolio.sell(assetnameArray[0],"CASH",assetAmount[0],CurrentDate);
CurrentPortfolio.buy(assetnameArray[0],securitynameArray[0],targetAmount[0],CurrentDate);
printToLog(assetnameArray[0]+" goes long from CASH");
printToLog("position:"+position[0]);
printToLog("currentpriceinput:"+currentpriceinput[0]);
printToLog("ema:"+ema[0]);
printToLog("assetAmount:"+assetAmount[0]);
printToLog("targetAmount"+targetAmount[0]);
}

//normal cases
for(int i=1;i<=3; i++)
{
if((position[i]==false)&&(currentpriceinput[i]>=ema[i]))
{
CurrentPortfolio.buy(assetnameArray[i], securitynameArray[i],targetAmount[i]-assetAmount[i],CurrentDate);
printToLog(assetnameArray[i]+" goes long from short");
printToLog("position:"+position[i]);
printToLog("currentpriceinput:"+currentpriceinput[i]);
printToLog("ema:"+ema[i]);
printToLog("assetAmount:"+assetAmount[i]);
printToLog("targetAmount"+targetAmount[i]);
}
}

//6.S to SS short to smaller short 

//normal cases
for(int i=1;i<=3; i++)
{
if((position[i]==false)&&(currentpriceinput[i]<ema[i]))
{
printToLog(assetnameArray[i]+" goes short from short");
//double bias=targetAmount[i]-(originalTotalamount*percentageofall[i]*2+assetAmount[i]);
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
for(int i=0;i<=3; i++)
{
if(currentpriceinput[i]>=ema[i])
{position[i]=true;}
else{position[i]=false;}
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