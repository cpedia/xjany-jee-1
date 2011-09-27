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
public class S_and_P_Diversified_trends_indicator_strategy429 extends SimulateStrategy{
	public S_and_P_Diversified_trends_indicator_strategy429(){
		super();
		StrategyID=429L;
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
	String[] assetnameArray={"Energy","Industrial metal","Precious metal","Agriculture","Euro","Japanese Yen",
"British Pound","Swiss Franc","Australian Dollar","Canadian Dollar","U.S treasury bond","U.S treasury note"};

String[] securitynameArray={"DBE","DBB","DBP","DBA","FXE","FXY","FXB","FXF","FXC","FXA","AGG",
"SHY"};

double[] percentageofall={0.1875,0.05,0.0525,0.2,0.13,0.12,0.05,0.02,0.02,0.01,0.075,0.075};

boolean[] position={false,false,false,false,false,false,false,false,false,false,false,false};
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
		//if(intervalofEMA=="year")
//{interval=252;}
//if(intervalofEMA=="halfyear")
//{interval=126;}
//if(intervalofEMA=="quarter")
//{interval=63;}
//if(intervalofEMA=="month")
//{interval=21;}
//if(intervalofEMA=="halfmonth")
//{interval=10;}
//if(intervalofEMA=="week")
//{interval=5;}

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

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(assetnameArray[4]);
CurrentAsset.setClassID(getAssetClassID("FIXED INCOME"));
CurrentAsset.setTargetPercentage(percentageofall[4]);
CurrentPortfolio.addAsset(CurrentAsset);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(assetnameArray[5]);
CurrentAsset.setClassID(getAssetClassID("FIXED INCOME"));
CurrentAsset.setTargetPercentage(percentageofall[5]);
CurrentPortfolio.addAsset(CurrentAsset);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(assetnameArray[6]);
CurrentAsset.setClassID(getAssetClassID("FIXED INCOME"));
CurrentAsset.setTargetPercentage(percentageofall[6]);
CurrentPortfolio.addAsset(CurrentAsset);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(assetnameArray[7]);
CurrentAsset.setClassID(getAssetClassID("FIXED INCOME"));
CurrentAsset.setTargetPercentage(percentageofall[7]);
CurrentPortfolio.addAsset(CurrentAsset);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(assetnameArray[8]);
CurrentAsset.setClassID(getAssetClassID("INTERNATIONAL Equity"));
CurrentAsset.setTargetPercentage(percentageofall[8]);
CurrentPortfolio.addAsset(CurrentAsset);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(assetnameArray[9]);
CurrentAsset.setClassID(getAssetClassID("FIXED INCOME"));
CurrentAsset.setTargetPercentage(percentageofall[9]);
CurrentPortfolio.addAsset(CurrentAsset);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(assetnameArray[10]);
CurrentAsset.setClassID(getAssetClassID("FIXED INCOME"));
CurrentAsset.setTargetPercentage(percentageofall[10]);
CurrentPortfolio.addAsset(CurrentAsset);

CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName(assetnameArray[11]);
CurrentAsset.setClassID(getAssetClassID("FIXED INCOME"));
CurrentAsset.setTargetPercentage(percentageofall[11]);
CurrentPortfolio.addAsset(CurrentAsset);

for(int i=0; i<=11;i++){
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
//currentpriceinput[i]=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-1*interval,false);// //double prior2=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-2*interval,false);
//double prior3=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-3*interval,false);
//double prior4=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-4*interval,false);
//double prior5=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-5*interval,false);
//double prior6=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-6*interval,false);
//double prior7=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-7*interval,false);

prior1=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.MONTHLY,-1,false);
prior2=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.MONTHLY,-2,false);
prior3=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.MONTHLY,-3,false);
prior4=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.MONTHLY,-4,false);
prior5=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.MONTHLY,-5,false);
prior6=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.MONTHLY,-6,false);
prior7=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.MONTHLY,-7,false);

ema=prior7*0.0232+prior6*0.0371+prior5*0.0594+prior4*0.0951+prior3*0.1522
+prior2*0.2434+prior1*0.3895;
}

if(intervalofEMA.equals("halfmonth"))
{
//currentpriceinput[i]=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-1*interval,false);//double prior2=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-2*interval,false);
//double prior3=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-3*interval,false);
//double prior4=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-4*interval,false);
//double prior5=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-5*interval,false);
//double prior6=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-6*interval,false);
//double prior7=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-7*interval,false);

prior1=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-2,false);
prior2=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-4,false);
prior3=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-6,false);
prior4=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-8,false);
prior5=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-10,false);
prior6=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-12,false);
prior7=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-14,false);

ema=prior7*0.0232+prior6*0.0371+prior5*0.0594+prior4*0.0951+prior3*0.1522
+prior2*0.2434+prior1*0.3895;
}


if(intervalofEMA.equals("week"))
{
//double prior2=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-1*interval,false);//double prior2=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-2*interval,false);
//double prior3=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-3*interval,false);
//double prior4=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-4*interval,false);
//double prior5=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-5*interval,false);
//double prior6=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-6*interval,false);
//double prior7=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-7*interval,false);

prior1=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-1,false);
prior2=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-2,false);
prior3=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-3,false);
prior4=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-4,false);
prior5=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-5,false);
prior6=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-6,false);
prior7=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-7,false);

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
		
		 
		else if ((AdjustFrequency.equals("year") && isYearEnd(CurrentDate))
||(AdjustFrequency.equals("halfyear") &&LTIDate.isHalfYearEnd(CurrentDate))
||(AdjustFrequency.equals("quarter") && isQuarterEnd(CurrentDate)) 
||(AdjustFrequency.equals("month") && isMonthEnd(CurrentDate))) {
		   double[] ema=new double[12];
double[] currentpriceinput=new double[12];
double[] assetAmount=new double[12];
double[] targetAmount=new double[12];
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);


//get basic data for later use
for(int i=0; i<=11;i++)
{
assetAmount[i]=CurrentPortfolio.getAssetAmount(assetnameArray[i],CurrentDate);
targetAmount[i]=TotalAmount*percentageofall[i];

if(intervalofEMA.equals("month"))
{
//currentpriceinput[i]=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-1*interval,false);//double prior2=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-2*interval,false);
//double prior3=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-3*interval,false);
//double prior4=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-4*interval,false);
//double prior5=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-5*interval,false);
//double prior6=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-6*interval,false);
//double prior7=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-7*interval,false);

currentpriceinput[i]=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.MONTHLY,-1,false);
double prior2=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.MONTHLY,-2,false);
double prior3=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.MONTHLY,-3,false);
double prior4=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.MONTHLY,-4,false);
double prior5=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.MONTHLY,-5,false);
double prior6=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.MONTHLY,-6,false);
double prior7=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.MONTHLY,-7,false);


ema[i]=prior7*0.0232+prior6*0.0371+prior5*0.0594+prior4*0.0951+prior3*0.1522
+prior2*0.2434+currentpriceinput[i]*0.3895;
}

if(intervalofEMA.equals("halfmonth"))
{
//currentpriceinput[i]=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-1*interval,false);//double prior2=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-2*interval,false);
//double prior3=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-3*interval,false);
//double prior4=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-4*interval,false);
//double prior5=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-5*interval,false);
//double prior6=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-6*interval,false);
//double prior7=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-7*interval,false);

currentpriceinput[i]=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-2,false);
double prior2=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-4,false);
double prior3=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-6,false);
double prior4=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-8,false);
double prior5=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-10,false);
double prior6=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-12,false);
double prior7=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-14,false);

ema[i]=prior7*0.0232+prior6*0.0371+prior5*0.0594+prior4*0.0951+prior3*0.1522
+prior2*0.2434+currentpriceinput[i]*0.3895;
}


if(intervalofEMA.equals("week"))
{
//currentpriceinput[i]=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-1*interval,false);//double prior2=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-2*interval,false);
//double prior3=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-3*interval,false);
//double prior4=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-4*interval,false);
//double prior5=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-5*interval,false);
//double prior6=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-6*interval,false);
//double prior7=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.DAILY,-7*interval,false);

currentpriceinput[i]=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-1,false);
double prior2=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-2,false);
double prior3=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-3,false);
double prior4=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-4,false);
double prior5=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-5,false);
double prior6=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-6,false);
double prior7=getSecurity(securitynameArray[i]).getReturn(CurrentDate,TimeUnit.WEEKLY,-7,false);

ema[i]=prior7*0.0232+prior6*0.0371+prior5*0.0594+prior4*0.0951+prior3*0.1522
+prior2*0.2434+currentpriceinput[i]*0.3895;
}
}

//Judge by position determination to sell first
for(int i=0;i<=11;i++)
{
//1.long to long with amount percentage larger than before
if((position[i]==true)&&(currentpriceinput[i]>=ema[i])&&(assetAmount[i]>targetAmount[i]))
{
CurrentPortfolio.sell(assetnameArray[i],securitynameArray[i],assetAmount[i]-targetAmount[i],CurrentDate);
printToLog(assetnameArray[i]+" goes long from long ");
}
}

//2.long to short

//i=0 needs to be treated seperately
if((position[0]==true)&&(currentpriceinput[0]<ema[0]))
{
CurrentPortfolio.sell(assetnameArray[0],securitynameArray[0],assetAmount[0],CurrentDate);
CurrentPortfolio.buy(assetnameArray[0],"CASH",targetAmount[0],CurrentDate);
printToLog(assetnameArray[0]+" goes for CASH from long");
}

//normal cases
for(int i=1;i<=11;i++)
{
if((position[i]==true)&&(currentpriceinput[i]<ema[i]))
{
CurrentPortfolio.sell(assetnameArray[i],securitynameArray[i],assetAmount[i],CurrentDate);
CurrentPortfolio.shortSell(assetnameArray[i],securitynameArray[i],targetAmount[i],CurrentDate); 
printToLog(assetnameArray[i]+" goes short from long");
}
}

//3.long to long with amount percentage smaller than before to buy later
for(int i=0;i<=11;i++)
{
if((position[i]==true)&&(currentpriceinput[i]>=ema[i])&&(assetAmount[i]<targetAmount[i]))
{
CurrentPortfolio.buy(assetnameArray[i],securitynameArray[i],targetAmount[i]-assetAmount[i],CurrentDate);
printToLog(assetnameArray[i]+" goes long from long");
}
}

//4.short to long 

//i=0 needs to be treated seperately
if((position[0]==false)&&(currentpriceinput[0]>=ema[0]))
{
CurrentPortfolio.sell(assetnameArray[0],"CASH",assetAmount[0],CurrentDate);
CurrentPortfolio.buy(assetnameArray[0],securitynameArray[0],targetAmount[0],CurrentDate);
printToLog(assetnameArray[0]+" goes long from CASH");
}

//normal cases
for(int i=1;i<=11;i++)
{
if((position[i]==false)&&(currentpriceinput[i]>=ema[i]))
{
CurrentPortfolio.buy(assetnameArray[i], securitynameArray[i],targetAmount[i]-assetAmount[i],CurrentDate);
printToLog(assetnameArray[i]+" goes long from short");
}
}

//5.short to short 
//i=0 needs to be treated seperately
if((position[0]==false)&&(currentpriceinput[0]<ema[0]))
{
if(assetAmount[0]<targetAmount[0])
{CurrentPortfolio.buy(assetnameArray[0],securitynameArray[0],targetAmount[0]-assetAmount[0],CurrentDate);}
if(assetAmount[0]>targetAmount[0])
{CurrentPortfolio.sell(assetnameArray[0],"CASH",assetAmount[0]-targetAmount[0],CurrentDate);}
printToLog(assetnameArray[0]+" goes for CASH  from CASH");
}

//normal cases
for(int i=1;i<=11;i++)
{
if((position[i]==false)&&(currentpriceinput[i]<ema[i]))
{
printToLog(assetnameArray[i]+" goes short from short");
double bias=targetAmount[i]-(originalTotalamount*percentageofall[i]*2+assetAmount[i]);
if(bias<0){
CurrentPortfolio.buy(assetnameArray[i], securitynameArray[i], -bias,CurrentDate);
}
if(bias>0)
{CurrentPortfolio.shortSell(assetnameArray[i],securitynameArray[i],bias,CurrentDate);  
 }
}
}

//update position for every sector
for(int i=0;i<=11;i++)







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