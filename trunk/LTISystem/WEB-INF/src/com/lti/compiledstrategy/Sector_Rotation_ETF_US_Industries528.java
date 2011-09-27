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
public class Sector_Rotation_ETF_US_Industries528 extends SimulateStrategy{
	public Sector_Rotation_ETF_US_Industries528(){
		super();
		StrategyID=528L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int Frequency;
	public void setFrequency(int Frequency){
		this.Frequency=Frequency;
	}
	
	public int getFrequency(){
		return this.Frequency;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		Frequency=(Integer)ParameterUtil.fetchParameter("int","Frequency", "1", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date startDate;
Date date;
/* all Fidelity Select Funds representing stock industries */
String[] class1= {"XLF", "PJB", "KIE", "IAI", "IYG", "IYR", "IBB", "PPH", "XLV", "XLY", "XLP", "XLI", "IYT", "XHB", "MOO",  "PPA", "RTH", "TAN", "XLE", "OIH", "XLB", "KOL", "NLR", "SLX", "GEX", "PHO", "XME", "GDX", "QQQQ", "SMH", "XLU", "IYZ"};
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
date=CurrentDate;


int c=class1.length;
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

String[] newArray1=getTopSecurityArray(class1,-12, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray2=getTopSecurityArray(class1, -6, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray3=getTopSecurityArray(class1, -3, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray4=getTopSecurityArray(class1, -1, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);


for(int i=0; i<=class1.length-1; i++)
{
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(class1[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
{
a =(getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -3)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -1) )*100/4;


for(int j=0; j<=newArray1.length-1; j++)
{
if(class1[i].equals(newArray1[j]))
    { 
     if(j<=14)
        bp1=1;
     else
        bp1=0;
    }
}


for(int j=0; j<=newArray2.length-1; j++)
{
    if(class1[i].equals(newArray2[j]))
   { 
     if(j<=14)
        bp2=1;
     else
        bp2=0;
   }
}


for(int j=0; j<=newArray3.length-1; j++)
{
    if(class1[i].equals(newArray3[j]))
    { 
     if(j<=14)
        bp3=1;
     else
        bp3=0;
    }
}


for(int j=0; j<=newArray4.length-1;j++)
{
 if(class1[i].equals(newArray4[j]))
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
     String tmp= class1[Maxscoreindex];
     class1 [Maxscoreindex]= class1[i];
     class1 [i]=tmp;
   
     double t=score[Maxscoreindex];
     score [Maxscoreindex]= score[i];
     score[i]=t;

   }
}


String log_m="After Ordering " + date.toString()+"\r\n";
for(int i=0;i<class1.length;i++){
        log_m+=class1[i] +" ";
        log_m+= score[i];
        log_m+="\r\n";
}
 printToLog(log_m);



int s=class1.length-1;

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset1");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s]).getName()));
CurrentAsset.setTargetPercentage(0.34);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset1", getSecurity(class1[s]).getName(), TotalAmount * 0.34, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-1]).getName()));
CurrentAsset.setTargetPercentage(0.3);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(class1[s-1]).getName(), TotalAmount * 0.33, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset3");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-2]).getName()));
CurrentAsset.setTargetPercentage(0.3);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset3", getSecurity(class1[s-2]).getName(), TotalAmount * 0.33, CurrentDate);
/*
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset4");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-3]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset4", getSecurity(class1[s-3]).getName(), TotalAmount * 0.2, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset5");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-4]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset5", getSecurity(class1[s-4]).getName(), TotalAmount * 0.2, CurrentDate);
*/
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
		
		 
		else if (((Frequency == 1)&& (LTIDate.isNYSETradingDay(CurrentDate) && LTIDate.calculateInterval(startDate, CurrentDate)>=90))|| ((Frequency == 0)&& (LTIDate.isNYSETradingDay(CurrentDate) && LTIDate.calculateInterval(startDate, CurrentDate)>=30))) {
		   startDate=CurrentDate;


int c=class1.length;
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

String[] newArray1=getTopSecurityArray(class1,-12,CurrentDate, TimeUnit.MONTHLY, SortType.RETURN ,true);
String[] newArray2=getTopSecurityArray(class1, -6, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray3=getTopSecurityArray(class1, -3, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray4=getTopSecurityArray(class1, -1, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);



for(int i=0; i<=class1.length-1; i++)
{
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(class1[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
{
a =(getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -3)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -1) )*100/4;


for(int j=0; j<=newArray1.length-1; j++)
{
if(class1[i].equals(newArray1[j]))
    { 
     if(j<=14)
        bp1=1;
     else
        bp1=0;
    }
}


for(int j=0; j<=newArray2.length-1; j++)
{
    if(class1[i].equals(newArray2[j]))
   { 
     if(j<=14)
        bp2=1;
     else
        bp2=0;
   }
}


for(int j=0; j<=newArray3.length-1; j++)
{
    if(class1[i].equals(newArray3[j]))
    { 
     if(j<=14)
        bp3=1;
     else
        bp3=0;
    }
}


for(int j=0; j<=newArray4.length-1;j++)
{
 if(class1[i].equals(newArray4[j]))
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
     String tmp= class1[Maxscoreindex];
     class1 [Maxscoreindex]= class1[i];
     class1 [i]=tmp;

     double t=score[Maxscoreindex];
     score [Maxscoreindex]= score[i];
     score[i]=t;

   }
}


String log_m="After Ordering " + date.toString()+"\r\n";
for(int i=0;i<class1.length;i++){
        log_m+=class1[i] +" ";
        log_m+= score[i];
        log_m+="\r\n";
}
 printToLog(log_m);



int s=class1.length-1;

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset1");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s]).getName()));
CurrentAsset.setTargetPercentage(0.34);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset1", getSecurity(class1[s]).getName(), TotalAmount * 0.34, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-1]).getName()));
CurrentAsset.setTargetPercentage(0.33);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(class1[s-1]).getName(), TotalAmount * 0.33, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset3");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-2]).getName()));
CurrentAsset.setTargetPercentage(0.33);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset3", getSecurity(class1[s-2]).getName(), TotalAmount * 0.33, CurrentDate);
/*
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset4");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-3]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset4", getSecurity(class1[s-3]).getName(), TotalAmount * 0.2, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset5");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-4]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset5", getSecurity(class1[s-4]).getName(), TotalAmount * 0.2, CurrentDate);
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