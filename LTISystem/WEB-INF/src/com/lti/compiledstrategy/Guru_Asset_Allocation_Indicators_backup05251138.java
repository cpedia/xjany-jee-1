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
public class Guru_Asset_Allocation_Indicators_backup05251138 extends SimulateStrategy{
	public Guru_Asset_Allocation_Indicators_backup05251138(){
		super();
		StrategyID=1138L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int NumberOfGurus;
	public void setNumberOfGurus(int NumberOfGurus){
		this.NumberOfGurus=NumberOfGurus;
	}
	
	public int getNumberOfGurus(){
		return this.NumberOfGurus;
	}
	private String [] index;
	public void setIndex(String [] index){
		this.index=index;
	}
	
	public String [] getIndex(){
		return this.index;
	}
	private boolean bShort;
	public void setBShort(boolean bShort){
		this.bShort=bShort;
	}
	
	public boolean getBShort(){
		return this.bShort;
	}
	private int AssetID;
	public void setAssetID(int AssetID){
		this.AssetID=AssetID;
	}
	
	public int getAssetID(){
		return this.AssetID;
	}
	private boolean bAll;
	public void setBAll(boolean bAll){
		this.bAll=bAll;
	}
	
	public boolean getBAll(){
		return this.bAll;
	}
	private String Frequency;
	public void setFrequency(String Frequency){
		this.Frequency=Frequency;
	}
	
	public String getFrequency(){
		return this.Frequency;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		NumberOfGurus=(Integer)ParameterUtil.fetchParameter("int","NumberOfGurus", "10", parameters);
		index=(String [])ParameterUtil.fetchParameter("String []","index", "VFINX, VGTSX,  VGSIX, GLD, BEGBX,VFITX,VFICX,  CASH", parameters);
		bShort=(Boolean)ParameterUtil.fetchParameter("boolean","bShort", "false", parameters);
		AssetID=(Integer)ParameterUtil.fetchParameter("int","AssetID", "65", parameters);
		bAll=(Boolean)ParameterUtil.fetchParameter("boolean","bAll", "false", parameters);
		Frequency=(String)ParameterUtil.fetchParameter("String","Frequency", "week", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date NextDate;
int interval=30;

Date startDate;
Date date;
String[] class1; 
int s;

Date[] weeks=new Date[520];
Double[][] allocs=new Double[520][2];
int iWeeks=0;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("NextDate: ");
		sb.append(NextDate);
		sb.append("\n");
		sb.append("interval: ");
		sb.append(interval);
		sb.append("\n");
		sb.append("startDate: ");
		sb.append(startDate);
		sb.append("\n");
		sb.append("date: ");
		sb.append(date);
		sb.append("\n");
		sb.append("class1: ");
		sb.append(class1);
		sb.append("\n");
		sb.append("s: ");
		sb.append(s);
		sb.append("\n");
		sb.append("weeks: ");
		sb.append(weeks);
		sb.append("\n");
		sb.append("allocs: ");
		sb.append(allocs);
		sb.append("\n");
		sb.append("iWeeks: ");
		sb.append(iWeeks);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(NextDate);
		stream.writeObject(interval);
		stream.writeObject(startDate);
		stream.writeObject(date);
		stream.writeObject(class1);
		stream.writeObject(s);
		stream.writeObject(weeks);
		stream.writeObject(allocs);
		stream.writeObject(iWeeks);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		NextDate=(Date)stream.readObject();;
		interval=(Integer)stream.readObject();;
		startDate=(Date)stream.readObject();;
		date=(Date)stream.readObject();;
		class1=(String[])stream.readObject();;
		s=(Integer)stream.readObject();;
		weeks=(Date[])stream.readObject();;
		allocs=(Double[][])stream.readObject();;
		iWeeks=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	Date[] dates=new Date[53];
Double[][] values=new Double[53][2];
@Override
	public void afterExecuting(Portfolio portfolio, Date CurrentDate)   throws Exception {
		super.afterExecuting(portfolio, CurrentDate);
                int i=0;
                String xml;
                for (i=0; i<52; i++) {
                    dates[i] = weeks[iWeeks-52+i];
                     values[i][0]=allocs[iWeeks-52+i][0];
                     values[i][1]=allocs[iWeeks-52+i][1];
               }
               dates[52] = weeks[iWeeks-1];
               values[52][0]=allocs[iWeeks-1][0];
               values[52][1]=allocs[iWeeks-1][1];            
               printToLog("Date " + dates[52]+" values[0]: "+values[52][0]+" values[1]: "+values[52][1]); 
                xml=buildXML(dates,values);
		com.lti.util.PersistentUtil.writeObject(xml,"Moderate"+NumberOfGurus+".xml");
	}
@Override
	public void afterUpdating(Portfolio portfolio, Date CurrentDate)   throws Exception {
		super.afterExecuting(portfolio, CurrentDate);
                int i=0;
                String xml;
                for (i=0; i<52; i++) {
                    dates[i] = weeks[iWeeks-52+i];
                     values[i][0]=allocs[iWeeks-52+i][0];
                     values[i][1]=allocs[iWeeks-52+i][1];
               }
               dates[52] = weeks[iWeeks-1];
               values[52][0]=allocs[iWeeks-1][0];
               values[52][1]=allocs[iWeeks-1][1];            
               printToLog("Date " + dates[52]+" values[0]: "+values[52][0]+" values[1]: "+values[52][1]); 
                xml=buildXML(dates,values);
		com.lti.util.PersistentUtil.writeObject(xml,"Moderate"+NumberOfGurus+".xml");
	}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		List <Security > list1;
startDate=CurrentDate;
date=CurrentDate;
if (bAll) {
int i, j;
List<Security> listModerate, listConservative, listWorldAlloc;
listModerate=getSecurityByClass(65);
listConservative=getSecurityByClass(66);
listWorldAlloc=getSecurityByClass(99);
class1=new String[listModerate.size()+listConservative.size()+listWorldAlloc.size()];
s=class1.length-1;
for (i=0; i<listModerate.size(); i++) {
  class1[i]=listModerate.get(i).getSymbol();
}
j=listModerate.size();
for (i=0;i<listConservative.size();i++) {
   class1[i+j] = listConservative.get(i).getSymbol();
}
j=j+listConservative.size();
for (i=0;i<listWorldAlloc.size();i++) {
   class1[i+j] = listWorldAlloc.get(i).getSymbol();
}
} else {
list1=getSecurityByClass(AssetID);
class1 = new String[list1.size()];
s=class1.length-1;
for (int i=0; i<list1.size(); i++) {
  class1[i]=list1.get(i).getSymbol();
}
}

printToLog("Total Funds: "+class1.length);
CurrentPortfolio.sellAssetCollection(CurrentDate);

NextDate=LTIDate.getNewNYSETradingDay(CurrentDate,interval);
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
Asset CurrentAsset;


CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Fund_Clone");
CurrentAsset.setClassID(getAssetClassID("HYBRID ASSETS"));
CurrentAsset.setTargetPercentage(1.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy(CurrentAsset.getName(), "VFINX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGTSX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VGSIX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "QRAAX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "BEGBX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "VBMFX", TotalAmount * 0.1,CurrentDate);
CurrentPortfolio.buy(CurrentAsset.getName(), "CASH", TotalAmount * 0.4,CurrentDate);
	 				

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
		
		 
		else if ((Frequency.equals("month")&&LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) || (Frequency.equals("week")&&LTIDate.isLastNYSETradingDayOfWeek(CurrentDate))) {
		   //String[] index={"VFINX", "VGTSX",  "VGSIX", "GLD", "BEGBX","VFITX","VFICX",  "CASH" };
String[] ValidIndex;
List <String> ValidIndexList = new ArrayList<String>();
int c=class1.length;
double[] score=new double[c];
if (NumberOfGurus != 0) {
for(int i=0; i<=score.length-1; i++)
{
score[i]=0;
}

double a;
double bp1=0;
double bp2=0;
double bp3=0;
double bp4=0;


for(int i=0; i<=class1.length-1; i++)
{
try {
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(class1[i])), LTIDate. getNewNYSEMonth(CurrentDate, -

12)))
{
a =(getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -3)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -1) )*100/4;
score[i] = a;
/*****************************************************************************************
String[] newArray1=getTopSecurityArray(class1,-12, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, persistable);

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

String[] newArray2=getTopSecurityArray(class1, -6, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, persistable);

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

String[] newArray3=getTopSecurityArray(class1, -3, CurrentDate,  TimeUnit.MONTHLY, SortType.RETURN, persistable);

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

String[] newArray4=getTopSecurityArray(class1, -1, CurrentDate,  TimeUnit.MONTHLY, SortType.RETURN, persistable);

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
**************************************************************************************************************/
}

else
score[i]=-100000;
}  catch (Exception e) {
score[i]=-100000;
}
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
for(int i=class1.length-NumberOfGurus;i<class1.length;i++){
        log_m+=class1[i] +" ";
        log_m+= score[i];
        log_m+="\r\n";
}
 printToLog(log_m);
}

//String[] MF={class1[s],class1[s-1],class1[s-2], class1[s-3], class1[s-4]};
String[] MF = new String[class1.length];
for (int i=0; i<MF.length; i++) {
   MF[i]=class1[s-i];
}
int n=index.length;
String assetnameArray;

double totalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);

//get current portfolio's asset list 
List<Asset> list1=CurrentPortfolio.getCurrentAssetList();

Asset as=list1.get(0);
assetnameArray=as.getName();

CurrentPortfolio.sellAsset(assetnameArray,CurrentDate);

/* filter out non valid Index and then form ValidIndx array */
for (int i=0; i<index.length; i++) {
    if (LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(index[i])), LTIDate.getRecentNYSETradingDay(LTIDate.getNewDate(CurrentDate, TimeUnit.DAILY, -interval-20)))) {
      ValidIndexList.add(index[i]);
    }
}
ValidIndex = new String[ValidIndexList.size()];
for (int i=0; i<ValidIndexList.size(); i++) {
      ValidIndex[i]=ValidIndexList.get(i);
}
n=ValidIndex.length;

int iNumFunds;
iNumFunds = NumberOfGurus==0? MF.length:NumberOfGurus;

double[] newAmount=new double[n];
for(int j=0;j<iNumFunds;j++)
{
double[] temp=new double[n+1];
temp=RAA(interval,CurrentDate,TimeUnit.DAILY,MF[j],ValidIndex,bShort);
for(int k=0;k<n;k++)
   {  
    newAmount[k] += (1.0/iNumFunds)*temp[k]*totalAmount;
  }
}

weeks[iWeeks]=CurrentDate;
allocs[iWeeks][0]=newAmount[0]/totalAmount;
allocs[iWeeks][1]=newAmount[1]/totalAmount;
iWeeks++;
printToLog("iWeeks: "+iWeeks);
for(int k=0;k<n;k++)
 {  
   CurrentPortfolio.buy(assetnameArray, ValidIndex[k],newAmount[k] , CurrentDate);    
   printToLog("Allocation on "+ValidIndex[k]+" "+newAmount[k]/totalAmount);
 }
NextDate=LTIDate.getNewNYSETradingDay(CurrentDate,interval);

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