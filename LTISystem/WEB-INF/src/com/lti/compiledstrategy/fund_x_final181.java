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
public class fund_x_final181 extends SimulateStrategy{
	public fund_x_final181(){
		super();
		StrategyID=181L;
		StrategyClassID=0L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date startDate;
Date date;

	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("startDate: ");
		sb.append(startDate);
		sb.append("\n");
		sb.append("date: ");
		sb.append(date);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(startDate);
		stream.writeObject(date);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		startDate=(Date)stream.readObject();;
		date=(Date)stream.readObject();;
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

String[] class1= { "EZU", "OLCVX", "FEZ", "VGK", "IEV", "PVLDX", "OAKIX", "PWV"};

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
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(class1[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
{
a =(getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -3)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -1) )*100/4;

String[] newArray1=getTopSecurityArray(class1,-12, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN);

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

String[] newArray2=getTopSecurityArray(class1, -6, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN);

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

String[] newArray3=getTopSecurityArray(class1, -3, CurrentDate,  TimeUnit.MONTHLY, SortType.RETURN);

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

String[] newArray4=getTopSecurityArray(class1, -1, CurrentDate,  TimeUnit.MONTHLY, SortType.RETURN);

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


if(i==class1.length-1)
{
String log_f="After Ordering 12-month return" + date.toString()+"\r\n";
for(int k=0;k<newArray1.length;k++){
   log_f+=newArray1[k];
   log_f+=getSecurity(newArray1[k]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12);
  log_f+="\r\n";
}
printToLog(log_f);

String log_g="After Ordering 6-month return" + date.toString()+"\r\n";
for(int k=0;k<newArray2.length;k++){
   log_g+=newArray2[k];
   log_g+=getSecurity(newArray2[k]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6);
  log_g+="\r\n";
}
printToLog(log_g);

String log_h="After Ordering 3-month return" + date.toString()+"\r\n";
for(int k=0;k<newArray3.length;k++){
   log_h+=newArray3[k];
   log_h+=getSecurity(newArray3[k]).getReturn(CurrentDate, TimeUnit.MONTHLY, -3);
  log_h+="\r\n";
}
printToLog(log_h);

String log_t="After Ordering 1-month return" + date.toString()+"\r\n";
for(int k=0;k<newArray4.length;k++){
   log_t+=newArray4[k];
   log_t+=getSecurity(newArray4[k]).getReturn(CurrentDate, TimeUnit.MONTHLY, -1);
  log_t+="\r\n";
}
printToLog(log_t);
}
	

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
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset1", getSecurity(class1[s]).getName(), TotalAmount * 0.2, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-1]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(class1[s-1]).getName(), TotalAmount * 0.2, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset3");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-2]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset3", getSecurity(class1[s-2]).getName(), TotalAmount * 0.2, CurrentDate);

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
		
		 
		else if (LTIDate.isNYSETradingDay(CurrentDate) && LTIDate.calculateInterval(startDate, CurrentDate)>=90) {
		   startDate=CurrentDate;
date=CurrentDate;

String[] class1= { "EZU", "OLCVX", "FEZ", "VGK", "IEV", "PVLDX", "OAKIX", "PWV"};

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
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(class1[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
{
a =(getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -3)
+ getSecurity(class1[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -1) )*100/4;

String[] newArray1=getTopSecurityArray(class1,-12, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN);

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

String[] newArray2=getTopSecurityArray(class1, -6, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN);

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

String[] newArray3=getTopSecurityArray(class1, -3, CurrentDate,  TimeUnit.MONTHLY, SortType.RETURN);

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

String[] newArray4=getTopSecurityArray(class1, -1, CurrentDate,  TimeUnit.MONTHLY, SortType.RETURN);

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


if(i==class1.length-1)
{
String log_f="After Ordering 12-month return" + date.toString()+"\r\n";
for(int k=0;k<newArray1.length;k++){
   log_f+=newArray1[k];
   log_f+=getSecurity(newArray1[k]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12);
  log_f+="\r\n";
}
printToLog(log_f);

String log_g="After Ordering 6-month return" + date.toString()+"\r\n";
for(int k=0;k<newArray2.length;k++){
   log_g+=newArray2[k];
   log_g+=getSecurity(newArray2[k]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6);
  log_g+="\r\n";
}
printToLog(log_g);

String log_h="After Ordering 3-month return" + date.toString()+"\r\n";
for(int k=0;k<newArray3.length;k++){
   log_h+=newArray3[k];
   log_h+=getSecurity(newArray3[k]).getReturn(CurrentDate, TimeUnit.MONTHLY, -3);
  log_h+="\r\n";
}
printToLog(log_h);

String log_t="After Ordering 1-month return" + date.toString()+"\r\n";
for(int k=0;k<newArray4.length;k++){
   log_t+=newArray4[k];
   log_t+=getSecurity(newArray4[k]).getReturn(CurrentDate, TimeUnit.MONTHLY, -1);
  log_t+="\r\n";
}
printToLog(log_t);
}
	

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
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset1", getSecurity(class1[s]).getName(), TotalAmount * 0.2, CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-1]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2", getSecurity(class1[s-1]).getName(), TotalAmount * 0.2, CurrentDate);



CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset3");
CurrentAsset.setClassID(getAssetClassID(getSecurity (class1[s-2]).getName()));
CurrentAsset.setTargetPercentage(0.2);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset3", getSecurity(class1[s-2]).getName(), TotalAmount * 0.2, CurrentDate);

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


		}
		
		
		else{
		
			;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/fund_x_final181.java:101: cannot find symbol
//symbol  : method getTopSecurityArray(java.lang.String[],int,java.util.Date,com.lti.type.TimeUnit,com.lti.type.SortType)
//location: class com.lti.compiledstrategy.fund_x_final181
//String[] newArray1=getTopSecurityArray(class1,-12, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN);
//                   ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/fund_x_final181.java:114: cannot find symbol
//symbol  : method getTopSecurityArray(java.lang.String[],int,java.util.Date,com.lti.type.TimeUnit,com.lti.type.SortType)
//location: class com.lti.compiledstrategy.fund_x_final181
//String[] newArray2=getTopSecurityArray(class1, -6, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN);
//                   ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/fund_x_final181.java:127: cannot find symbol
//symbol  : method getTopSecurityArray(java.lang.String[],int,java.util.Date,com.lti.type.TimeUnit,com.lti.type.SortType)
//location: class com.lti.compiledstrategy.fund_x_final181
//String[] newArray3=getTopSecurityArray(class1, -3, CurrentDate,  TimeUnit.MONTHLY, SortType.RETURN);
//                   ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/fund_x_final181.java:140: cannot find symbol
//symbol  : method getTopSecurityArray(java.lang.String[],int,java.util.Date,com.lti.type.TimeUnit,com.lti.type.SortType)
//location: class com.lti.compiledstrategy.fund_x_final181
//String[] newArray4=getTopSecurityArray(class1, -1, CurrentDate,  TimeUnit.MONTHLY, SortType.RETURN);
//                   ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/fund_x_final181.java:346: cannot find symbol
//symbol  : method getTopSecurityArray(java.lang.String[],int,java.util.Date,com.lti.type.TimeUnit,com.lti.type.SortType)
//location: class com.lti.compiledstrategy.fund_x_final181
//String[] newArray1=getTopSecurityArray(class1,-12, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN);
//                   ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/fund_x_final181.java:359: cannot find symbol
//symbol  : method getTopSecurityArray(java.lang.String[],int,java.util.Date,com.lti.type.TimeUnit,com.lti.type.SortType)
//location: class com.lti.compiledstrategy.fund_x_final181
//String[] newArray2=getTopSecurityArray(class1, -6, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN);
//                   ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/fund_x_final181.java:372: cannot find symbol
//symbol  : method getTopSecurityArray(java.lang.String[],int,java.util.Date,com.lti.type.TimeUnit,com.lti.type.SortType)
//location: class com.lti.compiledstrategy.fund_x_final181
//String[] newArray3=getTopSecurityArray(class1, -3, CurrentDate,  TimeUnit.MONTHLY, SortType.RETURN);
//                   ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/fund_x_final181.java:385: cannot find symbol
//symbol  : method getTopSecurityArray(java.lang.String[],int,java.util.Date,com.lti.type.TimeUnit,com.lti.type.SortType)
//location: class com.lti.compiledstrategy.fund_x_final181
//String[] newArray4=getTopSecurityArray(class1, -1, CurrentDate,  TimeUnit.MONTHLY, SortType.RETURN);
//                   ^
//8 errors