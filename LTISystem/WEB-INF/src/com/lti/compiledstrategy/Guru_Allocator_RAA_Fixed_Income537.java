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
public class Guru_Allocator_RAA_Fixed_Income537 extends SimulateStrategy{
	public Guru_Allocator_RAA_Fixed_Income537(){
		super();
		StrategyID=537L;
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
	private String[] GuruFunds;
	public void setGuruFunds(String[] GuruFunds){
		this.GuruFunds=GuruFunds;
	}
	
	public String[] getGuruFunds(){
		return this.GuruFunds;
	}
	private String[] index;
	public void setIndex(String[] index){
		this.index=index;
	}
	
	public String[] getIndex(){
		return this.index;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		NumberOfGurus=(Integer)ParameterUtil.fetchParameter("int","NumberOfGurus", "2", parameters);
		Frequency=(Integer)ParameterUtil.fetchParameter("int","Frequency", "0", parameters);
		GuruFunds=(String[])ParameterUtil.fetchParameter("String[]","GuruFunds", "OSTIX, INCMX,OSIYX, STICX,IOSIX, RPSIX,FSICX, IOSSX, FKSAX, FSRIX, LSIZX, KSTSX, LSBDX, VCSBX, NEZYX, JESNX, MFIIX, PDVYX,  HLIZX, HSNIX, JHSBX, JHSTX, JISBX, LBDFX, LBSTX, LCMAX, RSFRX, RSTRX, SEEAX, SMARX, STIZX, YQFIX", parameters);
		index=(String[])ParameterUtil.fetchParameter("String[]","index", "VFSTX, VFICX, VWESX, VWEHX, VFISX,  VFITX, VUSTX,  VFIIX, BEGBX, VIPSX", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date NextDate;
int interval=30;

Date startDate;
Date date;

//String[] class1={"LSBDX", "INCMX", "LBSIX", "OSTIX", "STICX", "LCMAX", "FSRIX"}; 
int s;

//String[] index={"VFSTX", "VFICX", "VWESX", "VWEHX", "VFISX",  "VFITX", "VUSTX",  "VFIIX", "BEGBX", "VIPSX" };
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
		s=GuruFunds.length-1;
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
int c=index.length;
for (int i=0; i<c; i++) {
      CurrentPortfolio.buy(CurrentAsset.getName(), index[i], TotalAmount *1.0/c,CurrentDate);
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
		
		 
		else if (NextDate.equals(CurrentDate)) {
		   /* String[] index={"VFSTX", "VFICX", "VWESX", "VWEHX", "VFISX",  "VFITX", "VUSTX",  "VFIIX", "BEGBX", "VIPSX" }; */
int c=GuruFunds.length;
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


for(int i=0; i<=GuruFunds.length-1; i++)
{
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(GuruFunds[i])), LTIDate. getNewNYSEMonth(CurrentDate, -

12)))
{
a =(getSecurity(GuruFunds[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12)
+ getSecurity(GuruFunds[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6)
+ getSecurity(GuruFunds[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -3)
+ getSecurity(GuruFunds[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -1) )*100/4;

String[] newArray1=getTopSecurityArray(GuruFunds,-12, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, persistable);

for(int j=0; j<=newArray1.length-1; j++)
{
if(GuruFunds[i].equals(newArray1[j]))
    { 
     if(j<=14)
        bp1=1;
     else
        bp1=0;
    }
}

String[] newArray2=getTopSecurityArray(GuruFunds, -6, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, persistable);

for(int j=0; j<=newArray2.length-1; j++)
{
    if(GuruFunds[i].equals(newArray2[j]))
   { 
     if(j<=14)
        bp2=1;
     else
        bp2=0;
   }
}

String[] newArray3=getTopSecurityArray(GuruFunds, -3, CurrentDate,  TimeUnit.MONTHLY, SortType.RETURN, persistable);

for(int j=0; j<=newArray3.length-1; j++)
{
    if(GuruFunds[i].equals(newArray3[j]))
    { 
     if(j<=14)
        bp3=1;
     else
        bp3=0;
    }
}

String[] newArray4=getTopSecurityArray(GuruFunds, -1, CurrentDate,  TimeUnit.MONTHLY, SortType.RETURN, persistable);

for(int j=0; j<=newArray4.length-1;j++)
{
 if(GuruFunds[i].equals(newArray4[j]))
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
     String tmp= GuruFunds[Maxscoreindex];
     GuruFunds [Maxscoreindex]= GuruFunds[i];
     GuruFunds [i]=tmp;

     double t=score[Maxscoreindex];
     score [Maxscoreindex]= score[i];
     score[i]=t;

   }
}


String log_m="After Ordering " + date.toString()+"\r\n";
for(int i=0;i<GuruFunds.length;i++){
        log_m+=GuruFunds[i] +" ";
        log_m+= score[i];
        log_m+="\r\n";
}
 printToLog(log_m);

//String[] MF={GuruFunds[s],GuruFunds[s-1],GuruFunds[s-2], GuruFunds[s-3], GuruFunds[s-4]};
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
temp=RAA(interval,CurrentDate,TimeUnit.DAILY,GuruFunds[s-j],index,false);
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