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
public class GS_Tactical_Mid_Month_Long_Short_Style1037 extends SimulateStrategy{
	public GS_Tactical_Mid_Month_Long_Short_Style1037(){
		super();
		StrategyID=1037L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String[] Funds;
	public void setFunds(String[] Funds){
		this.Funds=Funds;
	}
	
	public String[] getFunds(){
		return this.Funds;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		Funds=(String[])ParameterUtil.fetchParameter("String[]","Funds", "IWF, IWB, SPY, OEF, IWD, IWO, IWM, IWN, IWP, IWR, DVY, IWC ", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date startDate;
Date date;
String[] class1; //= {"SPY", "EFA", "IYR", "DBC", "TLT", "CASH", "CASH","CASH"};

	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("startDate: ");
		sb.append(startDate);
		sb.append("\n");
		sb.append("date: ");
		sb.append(date);
		sb.append("\n");
		sb.append("class1: ");
		sb.append(class1);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(startDate);
		stream.writeObject(date);
		stream.writeObject(class1);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		startDate=(Date)stream.readObject();;
		date=(Date)stream.readObject();;
		class1=(String[])stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		startDate=CurrentDate;

 date=LTIDate.getLastNYSETradingDayOfWeek(
              LTIDate.getNewNYSEWeek(LTIDate.getFirstNYSETradingDayOfMonth( LTIDate.getNewNYSEMonth(CurrentDate, 1)),3));

int c=Funds.length;
double[] score=new double[c];
for(int i=0; i<score.length; i++)
{
score[i]=0;
}


double a;
double bp1=0;
double bp2=0;
double bp3=0;
double bp4=0;

String[] newArray1=getTopSecurityArray(Funds,-12, CurrentDate, TimeUnit.WEEKLY, SortType.RETURN, true);
String[] newArray2=getTopSecurityArray(Funds, -6, CurrentDate, TimeUnit.WEEKLY, SortType.RETURN, true);
String[] newArray3=getTopSecurityArray(Funds, -3, CurrentDate, TimeUnit.WEEKLY, SortType.RETURN, true);
String[] newArray4=getTopSecurityArray(Funds, -1, CurrentDate, TimeUnit.WEEKLY, SortType.RETURN, true);


for(int i=0; i<=Funds.length-1; i++)
{
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(Funds[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
{
a =(getSecurity(Funds[i]).getReturn(CurrentDate, TimeUnit.WEEKLY, -12)
+ getSecurity(Funds[i]).getReturn(CurrentDate, TimeUnit.WEEKLY, -6)
+ getSecurity(Funds[i]).getReturn(CurrentDate, TimeUnit.WEEKLY, -3)
+ getSecurity(Funds[i]).getReturn(CurrentDate, TimeUnit.WEEKLY, -1 ))*100/4;

bp1=0;
bp2=0;
bp3=0;
bp4=0;

for(int j=0; j<=newArray1.length-1; j++)
{
if(Funds[i].equals(newArray1[j]))
    { 
     if(j<=14)
        bp1=1;
     else
        bp1=0;
    }
}


for(int j=0; j<=newArray2.length-1; j++)
{
    if(Funds[i].equals(newArray2[j]))
   { 
     if(j<=14)
        bp2=1;
     else
        bp2=0;
   }
}


for(int j=0; j<=newArray3.length-1; j++)
{
    if(Funds[i].equals(newArray3[j]))
    { 
     if(j<=14)
        bp3=1;
     else
        bp3=0;
    }
}


for(int j=0; j<=newArray4.length-1;j++)
{
 if(Funds[i].equals(newArray4[j]))
     { 
      if(j<=14)
        bp4=1;
      else
         bp4=0;
     }
}

score[i]=a+bp1+bp2+bp3+bp4;
}

else
score[i]=-100000;
}



double Maxscore;
int Maxscoreindex;

for(int i=score.length-1; i>=1; i--)
{
 
 Maxscore = score[i];
 Maxscoreindex =i;
  
   for(int j=i-1; j>=0; j--)
   { 
     double b;
     b= score[j];
     
     if(Maxscore <=b)
     {
      Maxscore =b;
      Maxscoreindex =j;
     }
   }
   
   if(Maxscoreindex !=i)
   {
     String tmp= Funds[Maxscoreindex];
     Funds [Maxscoreindex]= Funds[i];
     Funds [i]=tmp;
   
     double t=score[Maxscoreindex];
     score [Maxscoreindex]= score[i];
     score[i]=t;

   }
}


String log_m="After Ordering " + date.toString()+"\r\n";
for(int i=0;i<Funds.length;i++){
        log_m+=Funds[i] +" ";
        log_m+= score[i];
        log_m+="\r\n";
}
 printToLog(log_m);



int s=Funds.length-1;

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

/* long highest */
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("AssetLong");
CurrentAsset.setClassID(getAssetClassID(getSecurity (Funds[s]).getName()));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("AssetLong", getSecurity(Funds[s]).getName(), TotalAmount , CurrentDate);

/*short lowest */
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("AssetShort");
CurrentAsset.setClassID(getAssetClassID(getSecurity (Funds[0]).getName()));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.shortSell("AssetShort", getSecurity(Funds[0]).getName(), TotalAmount , CurrentDate);

/*
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset3");
CurrentAsset.setClassID(getAssetClassID(getSecurity (Funds[s-2]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset3", getSecurity(Funds[s-2]).getName(), TotalAmount * 0.33, CurrentDate);
*/

/*CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset4");
CurrentAsset.setClassID(getAssetClassID(getSecurity (Funds[s-3]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset4", getSecurity(Funds[s-3]).getName(), TotalAmount * 0.2, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset5");
CurrentAsset.setClassID(getAssetClassID(getSecurity (Funds[s-4]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset5", getSecurity(Funds[s-4]).getName(), TotalAmount * 0.2, CurrentDate);*/

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
	
		
		int c=Funds.length;
double[] score=new double[c];
for(int i=0; i<score.length; i++)
{
score[i]=0;
}


double a;
double bp1=0;
double bp2=0;
double bp3=0;
double bp4=0;

String[] newArray1=getTopSecurityArray(Funds,-12, CurrentDate, TimeUnit.WEEKLY, SortType.RETURN, true);
String[] newArray2=getTopSecurityArray(Funds, -6, CurrentDate, TimeUnit.WEEKLY, SortType.RETURN, true);
String[] newArray3=getTopSecurityArray(Funds, -3, CurrentDate, TimeUnit.WEEKLY, SortType.RETURN, true);
String[] newArray4=getTopSecurityArray(Funds, -1, CurrentDate, TimeUnit.WEEKLY, SortType.RETURN, true);



for(int i=0; i<=Funds.length-1; i++)
{
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(Funds[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
{
a =(getSecurity(Funds[i]).getReturn(CurrentDate, TimeUnit.WEEKLY, -12)
+ getSecurity(Funds[i]).getReturn(CurrentDate, TimeUnit.WEEKLY, -6)
+ getSecurity(Funds[i]).getReturn(CurrentDate, TimeUnit.WEEKLY, -3)
+ getSecurity(Funds[i]).getReturn(CurrentDate, TimeUnit.WEEKLY, -1 ))*100/4;

bp1=0;
bp2=0;
bp3=0;
bp4=0;

for(int j=0; j<=newArray1.length-1; j++)
{
if(Funds[i].equals(newArray1[j]))
    { 
     if(j<=14)
        bp1=1;
     else
        bp1=0;
    }
}


for(int j=0; j<=newArray2.length-1; j++)
{
    if(Funds[i].equals(newArray2[j]))
   { 
     if(j<=14)
        bp2=1;
     else
        bp2=0;
   }
}


for(int j=0; j<=newArray3.length-1; j++)
{
    if(Funds[i].equals(newArray3[j]))
    { 
     if(j<=14)
        bp3=1;
     else
        bp3=0;
    }
}


for(int j=0; j<=newArray4.length-1;j++)
{
 if(Funds[i].equals(newArray4[j]))
     { 
      if(j<=14)
        bp4=1;
      else
         bp4=0;
     }
}

score[i]=a+bp1+bp2+bp3+bp4;
}

else
score[i]=-100000;
}



double Maxscore;
int Maxscoreindex;

for(int i=score.length-1; i>=1; i--)
{
 
 Maxscore = score[i];
 Maxscoreindex =i;
  
   for(int j=i-1; j>=0; j--)
   { 
     double b;
     b= score[j];
     
     if(Maxscore <=b)
     {
      Maxscore =b;
      Maxscoreindex =j;
     }
   }
   
   if(Maxscoreindex !=i)
   {
     String tmp= Funds[Maxscoreindex];
     Funds [Maxscoreindex]= Funds[i];
     Funds [i]=tmp;

     double t=score[Maxscoreindex];
     score [Maxscoreindex]= score[i];
     score[i]=t;

   }
}


String log_m="After Ordering " + date.toString()+"\r\n";
for(int i=0;i<Funds.length;i++){
        log_m+=Funds[i] +" ";
        log_m+= score[i];
        log_m+="\r\n";
}
 printToLog(log_m);
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.equals(CurrentDate, date)) {
		   startDate=CurrentDate;
date=LTIDate.getLastNYSETradingDayOfWeek(
              LTIDate.getNewNYSEWeek(LTIDate.getFirstNYSETradingDayOfMonth( LTIDate.getNewNYSEMonth(CurrentDate, 1)),3));
printToLog("Next Month= "+date);


/* int c=Funds.length;
double[] score=new double[c];
for(int i=0; i<score.length; i++)
{
score[i]=0;
}


double a;
double bp1=0;
double bp2=0;
double bp3=0;
double bp4=0;

String[] newArray1=getTopSecurityArray(Funds,-12, CurrentDate, TimeUnit.WEEKLY, SortType.RETURN, true);
String[] newArray2=getTopSecurityArray(Funds, -6, CurrentDate, TimeUnit.WEEKLY, SortType.RETURN, true);
String[] newArray3=getTopSecurityArray(Funds, -3, CurrentDate, TimeUnit.WEEKLY, SortType.RETURN, true);
String[] newArray4=getTopSecurityArray(Funds, -1, CurrentDate, TimeUnit.WEEKLY, SortType.RETURN, true);



for(int i=0; i<=Funds.length-1; i++)
{
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(Funds[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
{
a =(getSecurity(Funds[i]).getReturn(CurrentDate, TimeUnit.WEEKLY, -12)
+ getSecurity(Funds[i]).getReturn(CurrentDate, TimeUnit.WEEKLY, -6)
+ getSecurity(Funds[i]).getReturn(CurrentDate, TimeUnit.WEEKLY, -3)
+ getSecurity(Funds[i]).getReturn(CurrentDate, TimeUnit.WEEKLY, -1 ))*100/4;


bp1=0;
bp2=0;
bp3=0;
bp4=0;

for(int j=0; j<=newArray1.length-1; j++)
{
if(Funds[i].equals(newArray1[j]))
    { 
     if(j<=14)
        bp1=1;
     else
        bp1=0;
    }
}


for(int j=0; j<=newArray2.length-1; j++)
{
    if(Funds[i].equals(newArray2[j]))
   { 
     if(j<=14)
        bp2=1;
     else
        bp2=0;
   }
}


for(int j=0; j<=newArray3.length-1; j++)
{
    if(Funds[i].equals(newArray3[j]))
    { 
     if(j<=14)
        bp3=1;
     else
        bp3=0;
    }
}


for(int j=0; j<=newArray4.length-1;j++)
{
 if(Funds[i].equals(newArray4[j]))
     { 
      if(j<=14)
        bp4=1;
      else
         bp4=0;
     }
}

score[i]=a+bp1+bp2+bp3+bp4;
}

else
score[i]=-100000;
}



double Maxscore;
int Maxscoreindex;

for(int i=score.length-1; i>=1; i--)
{
 
 Maxscore = score[i];
 Maxscoreindex =i;
  
   for(int j=i-1; j>=0; j--)
   { 
     double b;
     b= score[j];
     
     if(Maxscore <=b)
     {
      Maxscore =b;
      Maxscoreindex =j;
     }
   }
   
   if(Maxscoreindex !=i)
   {
     String tmp= Funds[Maxscoreindex];
     Funds [Maxscoreindex]= Funds[i];
     Funds [i]=tmp;

     double t=score[Maxscoreindex];
     score [Maxscoreindex]= score[i];
     score[i]=t;

   }
}


String log_m="After Ordering " + date.toString()+"\r\n";
for(int i=0;i<Funds.length;i++){
        log_m+=Funds[i] +" ";
        log_m+= score[i];
        log_m+="\r\n";
}
 printToLog(log_m);

*/

int s=Funds.length-1;

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

/* long highest */
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("AssetLong");
CurrentAsset.setClassID(getAssetClassID(getSecurity (Funds[s]).getName()));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("AssetLong", getSecurity(Funds[s]).getName(), TotalAmount , CurrentDate);

/*short lowest */
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("AssetShort");
CurrentAsset.setClassID(getAssetClassID(getSecurity (Funds[0]).getName()));
CurrentAsset.setTargetPercentage(0.5);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.shortSell("AssetShort", getSecurity(Funds[0]).getName(), TotalAmount , CurrentDate);

/*
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset1");
CurrentAsset.setClassID(getAssetClassID(getSecurity (Funds[s]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset1", getSecurity(Funds[s]).getName(), TotalAmount * 0.34, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity (Funds[s-1]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(Funds[s-1]).getName(), TotalAmount * 0.33, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset3");
CurrentAsset.setClassID(getAssetClassID(getSecurity (Funds[s-2]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset3", getSecurity(Funds[s-2]).getName(), TotalAmount * 0.33, CurrentDate);
*/
/*CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset4");
CurrentAsset.setClassID(getAssetClassID(getSecurity (Funds[s-3]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset4", getSecurity(Funds[s-3]).getName(), TotalAmount * 0.2, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset5");
CurrentAsset.setClassID(getAssetClassID(getSecurity (Funds[s-4]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset5", getSecurity(Funds[s-4]).getName(), TotalAmount * 0.2, CurrentDate);
*/
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