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
public class Momentum_Scoring548 extends SimulateStrategy{
	public Momentum_Scoring548(){
		super();
		StrategyID=548L;
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
	private String[] Securities;
	public void setSecurities(String[] Securities){
		this.Securities=Securities;
	}
	
	public String[] getSecurities(){
		return this.Securities;
	}
	private int MinInvested;
	public void setMinInvested(int MinInvested){
		this.MinInvested=MinInvested;
	}
	
	public int getMinInvested(){
		return this.MinInvested;
	}
	private int MaxInvested;
	public void setMaxInvested(int MaxInvested){
		this.MaxInvested=MaxInvested;
	}
	
	public int getMaxInvested(){
		return this.MaxInvested;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		AdjustFrequency=(String)ParameterUtil.fetchParameter("String","AdjustFrequency", "month", parameters);
		Securities=(String[])ParameterUtil.fetchParameter("String[]","Securities", "SNP, CHA ,YZC, HNP, ACH, LFC, CEO, BIDU, PTR, CHL, CHU, NTES, SNDA, GSH ,SHI , MR, STP, EDU, CTRP, SOHU, GA, ZNH, SINA, LFT, FMCN, SMI, YGE, CEA, LDK, ASIA, EJ, JASO, VISN, CMED", parameters);
		MinInvested=(Integer)ParameterUtil.fetchParameter("int","MinInvested", "0", parameters);
		MaxInvested=(Integer)ParameterUtil.fetchParameter("int","MaxInvested", "100", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date startDate;
Date date;
/* Dow 30 stocks based on 3/20/2009 */
String[] class1;
/* = {"AA", "AXP", "BA", "BAC", "C", "CAT", "CVX", "DD", "DIS", "GE", "GM", "HD", "HPQ", "IBM", "INTC", "JNJ", "JPM", "KFT", "KO", "MCD", "MMM", "MRK", "MSFT", "PFE", "PG", "T", "UTX", "VZ", "WMT", "XOM"};*/

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
	/*
@Override
  public void afterExecuting(Portfolio portfolio, Date CurrentDate)   throws Exception {
    super.afterExecuting(portfolio, CurrentDate);
                List<Transaction> trs=portfolio.getTransactions();
                com.lti.util.PersistentUtil.writeObject(com.lti.util.TransactionUtil.compareTransactions(trs,com.lti.util.TransactionUtil.MergeTransactions(trs)),portfolio.getID()+".transactions");
}
*/

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		startDate=CurrentDate;
date=CurrentDate;
printToLog("Securities Length: " + Securities.length);
class1 = new String[Securities.length];


for (int i=0; i<Securities.length; i++) {
class1[i] = Securities[i];
printToLog("security "+ i+" "+class1[i]);
}


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
printToLog("doing " +i);
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
//int iTotalHoldings = (s+1)/10>3?(s+1)/10 : 3;
int iTotalHoldings = (s+1)/10>MinInvested ? (s+1)/10:MinInvested;
iTotalHoldings = iTotalHoldings>MaxInvested ? MaxInvested : iTotalHoldings;

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

for (int i=0; i<iTotalHoldings; i++) {
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset"+i);
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-i]).getName()));
CurrentAsset.setTargetPercentage(1.0/iTotalHoldings);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset"+i, class1[s-i], TotalAmount * (1.0/iTotalHoldings), CurrentDate);
}
/* CurrentAsset=new Asset();
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
		
		 
		else if ((AdjustFrequency.equals("year") && LTIDate.isLastNYSETradingDayOfYear(CurrentDate)) ||(AdjustFrequency.equals("quarter") && LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate))  ||(AdjustFrequency.equals("month") && LTIDate.isLastNYSETradingDayOfMonth(CurrentDate))) {
		   startDate=CurrentDate;

for (int i=0; i<Securities.length; i++) {
class1[i] = Securities[i];
printToLog("security "+ i+" "+class1[i]);
}


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
if(!getSecurity(class1[i]).getIsClosed() && LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(class1[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
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
//int iTotalHoldings = (s+1)/10>3?(s+1)/10 : 3;
int iTotalHoldings = (s+1)/10>MinInvested ? (s+1)/10:MinInvested;
iTotalHoldings = iTotalHoldings>MaxInvested ? MaxInvested : iTotalHoldings;


double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);
Asset CurrentAsset;

for (int i=0; i<iTotalHoldings; i++) {
CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset"+i);
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-i]).getName()));
CurrentAsset.setTargetPercentage(1.0/iTotalHoldings);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset"+i, class1[s-i], TotalAmount * (1.0/iTotalHoldings), CurrentDate);
}

/*
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