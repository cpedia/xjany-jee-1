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
public class SP_500_Stock_Momentum_Based_on_FundX_Scoring535 extends SimulateStrategy{
	public SP_500_Stock_Momentum_Based_on_FundX_Scoring535(){
		super();
		StrategyID=535L;
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
/* S&P 500 from OX, need some fix as two deletions. Suspect this is not updated ones.  based on 3/20/2009 */
String[] class1= {"A", 
"AA", 
"AAPL", 
"ABC", 
"ABK", 
"ABT", 
"ACE", 
"ACS", 
"ACV", 
"ADBE", 
"ADCT", 
"ADI", 
"ADM", 
"ADP", 
"ADSK", 
"AEE", 
"AEP", 
"AES", 
"AET", 
"AFL", 
"AGN", 
"AHC", 
"AIG", 
"AIV", 
"ALL", 
"ALTR", 
"AMAT", 
"AMD", 
"AMGN", 
"AMP", 
"AMZN", 
"AN", 
"AOC", 
"APA", 
"APC", 
//"APCC", 
"APD", 
"APOL", 
"ASH", 
"ASO", 
"ATI", 
"AVP", 
"AVY", 
"AXP", 
"AYE", 
"AZO", 
"BA", 
"BAC", 
"BAX", 
"BBBY", 
"BBT", 
"BBY", 
"BC", 
"BCR", 
"BDK", 
"BDX", 
"BEN", 
"BF/B", 
"BHI", 
"BIG", 
"BIIB", 
"BJS", 
"BK", 
"BLL", 
"BMC", 
"BMS", 
"BMY", 
"BNI", 
"BR", 
"BRCM", 
"BSC", 
"BSX", 
"BXP", 
"C", 
"CA", 
"CAG", 
"CAH", 
"CAT", 
"CB", 
"CBE", 
"CBS", 
"CCE", 
"CCL", 
"CCU", 
"CEG", 
"CHK", 
"CI", 
"CIEN", 
"CINF", 
"CIT", 
"CL", 
"CLX", 
"CMA", 
"CMCSA", 
"CME", 
"CMI", 
"CMS", 
"CMVT", 
"CNP", 
"CNX", 
"COF", 
"COH", 
"COL", 
"COP", 
"COST", 
"CPB", 
"CPN", 
"CPWR", 
"CSC", 
"CSCO", 
"CSX", 
"CTAS", 
"CTL", 
"CTX", 
"CTXS", 
"CVG", 
"CVH", 
"CVS", 
"CVX", 
"D", 
"DAL", 
"DD", 
"DDS", 
"DE", 
"DELL", 
"DF", 
"DGX", 
"DHI", 
"DHR", 
"DIS", 
"DOV", 
"DOW", 
"DRI", 
"DTE", 
"DUK", 
"DVN", 
"DYN", 
"EBAY", 
"ECL", 
"ED", 
"EFX", 
"EIX", 
"EK", 
"EL", 
"EMC", 
"EMN", 
"EMR", 
"EOG", 
"EP", 
"EQ", 
"EQR", 
"ERTS", 
"ESRX", 
"ETN", 
"ETR", 
"EXC", 
"F", 
"FCX", 
"FDO", 
"FDX", 
"FE", 
"FHN", 
"FII", 
"FISV", 
"FITB", 
"FLR", 
"FNM", 
"FO", 
"FPL", 
"FRE", 
"FRX", 
"G", 
"GAS", 
"GCI", 
"GD", 
"GE", 
"GENZ", 
"GILD", 
"GIS", 
"GLW", 
"GM", 
"GNW", 
"GOOG", 
"GPC", 
"GPS", 
"GR", 
"GS", 
"GT", 
"GWW", 
"HAL", 
"HAR", 
"HAS", 
"HBAN", 
"HD", 
"HES", 
"HIG", 
"HMA", 
"HNZ", 
"HOG", 
"HON", 
"HOT", 
"HPQ", 
"HRB", 
"HSP", 
"HSY", 
"HUM", 
"IBM", 
"IFF", 
"IGT", 
"INTC", 
"INTU", 
"IP", 
"IPG", 
"IR", 
"ITT", 
"ITW", 
"JBL", 
"JCI", 
"JCP", 
"JDSU", 
"JNJ", 
"JNPR", 
"JNS", 
"JNY", 
"JPM", 
"JWN", 
"K", 
"KBH", 
"KEY", 
"KG", 
"KIM", 
"KLAC", 
"KMB", 
"KO", 
"KR", 
"KSS", 
"LEG", 
"LEN", 
"LH", 
"LIZ", 
"LLL", 
"LLTC", 
"LLY", 
"LM", 
"LMT", 
"LNC", 
"LOW", 
"LPX", 
"LSI", 
"LTD", 
"LUV", 
"LXK", 
"MAR", 
"MAS", 
"MAT", 
"MAY", 
"MBI", 
"MCD", 
"MCK", 
"MCO", 
"MDP", 
"MDT", 
"MET", 
"MHP", 
"MHS", 
"MI", 
"MIL", 
"MKC", 
"MMC", 
"MMM", 
"MO", 
"MOLX", 
"MON", 
"MOT", 
"MRK", 
"MRO", 
"MS", 
"MSFT", 
"MTB", 
"MTG", 
"MU", 
"MUR", 
"MWV", 
"MXIM", 
"MYL", 
"NAV", 
"NBR", 
"NCC", 
"NCR", 
"NE", 
"NEM", 
"NI", 
"NKE", 
"NOC", 
"NOV", 
"NOVL", 
"NSC", 
"NSM", 
"NTAP", 
"NTRS", 
"NUE", 
"NVDA", 
"NVLS", 
"NWL", 
"NWS", 
"NYT", 
"ODP", 
"OMC", 
"OMX", 
"ORCL", 
"OXY", 
"PAYX", 
"PBG", 
"PBI", 
"PCAR", 
"PCG", 
"PCL", 
"PDCO", 
"PEG", 
"PEP", 
"PFE", 
"PFG", 
"PG", 
"PGN", 
"PGR", 
"PH", 
"PHM", 
"PKI", 
"PLD", 
"PLL", 
"PMCS", 
"PMTC", 
"PNC", 
"PNW", 
"PPG", 
"PPL", 
"PRU", 
"PSA", 
"PSFT", 
"PTV", 
"PX", 
"Q", 
"QCOM", 
"QLGC", 
"R", 
"RAI", 
"RDC", 
"RF", 
"RHI", 
"RIG", 
"ROH", 
"ROK", 
"RRD", 
"RSH", 
"RTN", 
"RX", 
"S", 
"SANM", 
"SBL", 
"SBUX", 
"SCHW", 
"SEE", 
"SFA", 
"SGP", 
"SHLD", 
"SHW", 
"SIAL", 
"SLB", 
"SLE", 
"SLM", 
"SNA", 
"SNDK", 
"SNV", 
"SO", 
//"SOTR", 
"SOV", 
"SPG", 
"SPLS", 
"SRE", 
"SSP", 
"STI", 
"STJ", 
"STT", 
"STZ", 
"SUN", 
"SVU", 
"SWK", 
"SWY", 
"SYK", 
"SYMC", 
"SYY", 
"T", 
"TAP", 
"TE", 
"TER", 
"TGT", 
"THC", 
"TIF", 
"TIN", 
"TJX", 
"TLAB", 
"TMK", 
"TMO", 
"TROW", 
"TSN", 
"TWX", 
"TXN", 
"TXT", 
"TYC", 
"UIS", 
"UNH", 
"UNM", 
"UNP", 
"UPS", 
"USB", 
"UTX", 
"VFC", 
"VIA/B", 
"VLO", 
"VMC", 
"VNO", 
"VRSN", 
"VRTS", 
"VZ", 
"WAG", 
"WAT", 
"WB", 
"WEN", 
"WFC", 
"WFMI", 
"WFT", 
"WHR", 
"WIN", 
"WLP", 
"WMB", 
"WMI", 
"WMT", 
"WPI", 
"WY", 
"WYE", 
"WYN", 
"X", 
"XEL", 
"XL", 
"XLNX", 
"XOM", 
"XRX", 
"XTO", 
"YHOO", 
"YUM", 
"ZION", 
"ZMH"};

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
//printToLog(class1[i]);
score[i]=-100000;
try {
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
} else
score[i]=-100000;
}catch(Exception e){}
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
score[i]=-100000;
try {
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
}catch(Exception e){}
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