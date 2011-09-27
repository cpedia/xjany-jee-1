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
public class Copy_of_Guru_Asset_Allocation_Clone_All1007 extends SimulateStrategy{
	public Copy_of_Guru_Asset_Allocation_Clone_All1007(){
		super();
		StrategyID=1007L;
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
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		NumberOfGurus=(Integer)ParameterUtil.fetchParameter("int","NumberOfGurus", "2", parameters);
		index=(String [])ParameterUtil.fetchParameter("String []","index", "VFINX, VGTSX,  VGSIX, GLD, BEGBX,VFITX,VFICX,  CASH", parameters);
		bShort=(Boolean)ParameterUtil.fetchParameter("boolean","bShort", "false", parameters);
		AssetID=(Integer)ParameterUtil.fetchParameter("int","AssetID", "65", parameters);
		bAll=(Boolean)ParameterUtil.fetchParameter("boolean","bAll", "false", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date NextDate;
int interval=30;

Date startDate;
Date date;
List <Security > list1;
String[] class1; /*={"HSGFX", "HSTRX", "GGHEX","GBMFX", "LCORX", "PASDX","WASYX","VAAPX", "VWINX","GLRBX","FPACX",  "TFSMX", "SWHIX", "DHFCX", "GATEX", "WYASX", "JABAX"}; 
int s=class1.length-1;

*/
int s;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		return;
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		return;
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
		
		 
		else if (LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) {
		   //String[] index={"VFINX", "VGTSX",  "VGSIX", "GLD", "BEGBX","VFITX","VFICX",  "CASH" };
int c=class1.length;
double[] score=new double[c];
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
for(int i=0;i<class1.length;i++){
        log_m+=class1[i] +" ";
        log_m+= score[i];
        log_m+="\r\n";
}
// printToLog(log_m);

//String[] MF={class1[s],class1[s-1],class1[s-2], class1[s-3], class1[s-4]};
String[] MF = new String[class1.length];
for (int i=0; i<MF.length; i++) {
   MF[i]=class1[s-i];
}
int n=index.length;
String assetnameArray;

double totalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
//printToLog(NextDate);  



//get current portfolio's asset list 
List<Asset> list1=CurrentPortfolio.getCurrentAssetList();

Asset as=list1.get(0);
assetnameArray=as.getName();

CurrentPortfolio.sellAsset(assetnameArray,CurrentDate);

double[] newAmount=new double[n];
for(int j=0;j<NumberOfGurus;j++)
{
double[] temp=new double[n+1];
Security MutualFund = getSecurity(MF[j]);
temp= doRAA (MutualFund,index, interval,bShort,CurrentDate,TimeUnit.DAILY);
for(int k=0;k<n;k++)
   {  
    newAmount[k] += (1.0/NumberOfGurus)*temp[k]*totalAmount;
  }
}

for(int k=0;k<n;k++)
 {  
   CurrentPortfolio.buy(assetnameArray, index[k],newAmount[k] , CurrentDate);    
 }
NextDate=LTIDate.getNewNYSETradingDay(CurrentDate,interval);
//printToLog(NextDate);   
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