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
public class Guru_Allocator_RAA_Fixed_Income_Wide538 extends SimulateStrategy{
	public Guru_Allocator_RAA_Fixed_Income_Wide538(){
		super();
		StrategyID=538L;
		StrategyClassID=10L;
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
		NumberOfGurus=(Integer)ParameterUtil.fetchParameter("int","NumberOfGurus", "2", parameters);
		Frequency=(Integer)ParameterUtil.fetchParameter("int","Frequency", "0", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date NextDate;
int interval=30;

Date startDate;
Date date;
/* including morningstar managers of the year: PTTRX, TGLMX, WATFX, MWTRX, LSBDX,  DODIX, FPNIX */
String[] class1={"LSBDX", "INCMX", "LBSIX", "OSTIX", "STICX", "LCMAX", "FSRIX", "PTTRX","DODIX", "TGBAX", "MWTRX", "FPNIX", "TGLMX", "WATFX"}; 
int s=class1.length-1;


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
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(NextDate);
		stream.writeObject(interval);
		stream.writeObject(startDate);
		stream.writeObject(date);
		stream.writeObject(class1);
		stream.writeObject(s);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		NextDate=(Date)stream.readObject();;
		interval=(Integer)stream.readObject();;
		startDate=(Date)stream.readObject();;
		date=(Date)stream.readObject();;
		class1=(String[])stream.readObject();;
		s=(Integer)stream.readObject();;
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
interval = Frequency==1?90:30;


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
		
		 
		else if (NextDate.equals(CurrentDate)) {
		   String[] index={ "VWEHX",  "VUSTX",  "VFISX", "VFIIX", "BEGBX" };
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

String[] MF={class1[s],class1[s-1],class1[s-2], class1[s-3], class1[s-4]};
int n=index.length;
String assetnameArray;

double totalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
printToLog(NextDate);  



//get current portfolio's asset list 
List<Asset> list1=CurrentPortfolio.getCurrentAssetList();

Asset as=list1.get(0);
assetnameArray=as.getName();

CurrentPortfolio.sellAsset(assetnameArray,CurrentDate);

double[] newAmount=new double[n];
for(int j=0;j<NumberOfGurus;j++)
{
double[] temp=new double[n+1];
temp=RAA(interval,CurrentDate,TimeUnit.DAILY,MF[j],index,false);
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