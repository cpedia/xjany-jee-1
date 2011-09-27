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
public class Copy_of_Guru_Sector_Rotation_Clone1071 extends SimulateStrategy{
	public Copy_of_Guru_Sector_Rotation_Clone1071(){
		super();
		StrategyID=1071L;
		StrategyClassID=9L;
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
	private String[] class1;
	public void setClass1(String[] class1){
		this.class1=class1;
	}
	
	public String[] getClass1(){
		return this.class1;
	}
	private int NumberOfSectors;
	public void setNumberOfSectors(int NumberOfSectors){
		this.NumberOfSectors=NumberOfSectors;
	}
	
	public int getNumberOfSectors(){
		return this.NumberOfSectors;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		NumberOfGurus=(Integer)ParameterUtil.fetchParameter("int","NumberOfGurus", "2", parameters);
		index=(String [])ParameterUtil.fetchParameter("String []","index", "XLU, XLP, XLV, XLY, XLK, XLE, XLB, XLI, XLF", parameters);
		bShort=(Boolean)ParameterUtil.fetchParameter("boolean","bShort", "false", parameters);
		class1=(String[])ParameterUtil.fetchParameter("String[]","class1", "XRO, RYSRX, ETFOX, PYH, CGMFX, FUNDX, PDP, PYH, PIV", parameters);
		NumberOfSectors=(Integer)ParameterUtil.fetchParameter("int","NumberOfSectors", "4", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date NextDate;
int interval=30;

Date startDate;
Date date;

/*String[] class1={"EZU",  "FEZ", "VGK", "IEV", "PVLDX", "OAKIX", "PWV", "HFCVX", "AMSTX", "ELV", "PID", "VTV", "SSAIX", "EFV", "TWEIX", "VEIPX", "DGT","XLG", "PFM", "FEU", "MAVFX", "VTRIX", "VWNFX", "YACKX", "DGRIX", "FMIHX", "HAINX", "AAIPX", "FDGFX", "RYEUX", "SGRIX", "THPGX", "WVALX", "DODFX", "SSHFX", "IVE", "SWINX", "JENSX", "PIPDX", "EFA", "WIBCX", "DIA", "BJBIX", "DVY", "DTLVX", "FEQIX", "ICEUX", "IWD", "OAKMX", "PHJ", "IWW", "SWANX", "FSTKX", "TRVLX", "MEQFX", "PRDGX", "PRGFX", "SSGWX", "AAGPX", "SPY", "PRFDX", "TORYX", "GABBX", "SWDIX", "PRBLX", "UMINX", "HAVLX", "VQNPX", "DSEFX", "DREVX", "PWP", "SWOIX", "SWOGX", "VHGEX", "VFINX", "OPIEX", "OBDEX", "FEQTX", "FDIVX", "RIMEX", "GABEX", "JSVAX", "DGAGX", "CFIMX", "VWNDX", "SWHIX", "SNXFX", "FFIDX", "SMCDX", "SLASX", "IWB", "BIGRX", "OBFOX", "IWS", "FWWFX", "TRBCX", "GABAX", "TMW", "IYY", "PRGIX", "VTI", "IWV", "NBSRX", "PRSGX", "CAMOX", "UMEQX", "JAWWX", "SOPFX", "FDVLX", "MVALX", "RSP", "FEAFX", "CCEVX", "UMVEX", "PRITX", "BEQGX", "FLCSX", "IVW", "FDEQX", "NGUAX", "TIVFX", "VUG", "NBISX", "IWF", "JMCVX", "WPGLX", "AGROX", "JANSX", "WTMVX", "VALDX", "FMACX", "JAEIX", "ELG", "PREDX",  "FAIRX", "VWUSX", "IWZ", "FGRIX", "FBGRX", "SNIGX", "MRVEX", "ARGFX", "UMBIX", "PWC", "PWY", "NICSX", "IJJ", "FAMVX", "RYSTX", "JAGIX", "HSGFX", "TEQUX", "PENNX", "GABGX", "BRGRX", "MDY", "MLPIX", "DTLGX", "MGRIX", "NPRTX", "NBREX", "PMCDX", "MUHLX", "BERWX", "STRGX", "RYSRX","CGMFX", "LMVTX"}; 
int s=class1.length-1;

*/

int s;
class IndexClass {
      public double Amount;
      public String sName;
      
     public IndexClass (double A, String s) /*{*/  
{
             Amount = A;
             sName = s;
    }
    public void setAmount (double A) /*{*/  
{
            Amount = A;
   }
   public void setName(String s) /*{*/  
{
          sName=s;
   }
    public double getAmount() /*{*/  
{
            return Amount;
   }
   public String getName() /*{*/  
{
           return sName;
   }
 }
class IndexClassCompare implements java.util.Comparator<IndexClass> {
   public int compare(IndexClass ic1, IndexClass ic2)/*{*/  
{
      if (ic1.getAmount()<ic2.getAmount() ) {
          return 1;
     } else { return -1;}
   }
}
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
		com.lti.compiler.LTIClassLoader cl=new com.lti.compiler.LTIClassLoader();
Thread.currentThread().setContextClassLoader(cl);

s=class1.length-1;
startDate=CurrentDate;
date=CurrentDate;

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
String[] ValidIndex;
List <String> ValidIndexList = new ArrayList<String>();
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
// printToLog(log_m);

String[] MF={class1[s],class1[s-1],class1[s-2], class1[s-3], class1[s-4]};
String assetnameArray;

double totalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
//printToLog(NextDate);  

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
int n=ValidIndex.length;
//printToLog(n);
double[] newAmount=new double[n];
for(int j=0;j<NumberOfGurus;j++)
{
double[] temp=new double[n+1];
System.out.println("before RAA");
temp=RAA(interval,CurrentDate,TimeUnit.DAILY,MF[j],ValidIndex,bShort);
System.out.println("after RAA");
for(int k=0;k<n;k++)
   {  
    newAmount[k] += (1.0/NumberOfGurus)*temp[k]*totalAmount;
  }
}

IndexClass[] ICArray = new IndexClass[n];
for (int i=0; i<n; i++) {
     ICArray[i]= new IndexClass(newAmount[i], ValidIndex[i]);
     //printToLog("i "+i+" amount= "+ICArray[i].getAmount()+ " Name= "+ICArray[i].getName());
}
java.util.Comparator<IndexClass> iccompare = new IndexClassCompare();
java.util.Arrays.sort(ICArray, iccompare);
//printToLog("n= "+n+" NumberOfSectors= "+NumberOfSectors);
double totalTopAmount=0.0;
for (int k=0; k<NumberOfSectors; k++ ){
    totalTopAmount += ICArray[k].getAmount();
}
for(int k=0;k<NumberOfSectors;k++)
 {  
   printToLog("k="+k+" ICArray[k]="+ICArray[k].getAmount()+" totalAmount="+totalAmount+" totalTopAmount="+totalTopAmount);
   CurrentPortfolio.buy(assetnameArray, ICArray[k].getName(), ICArray[k].getAmount()* totalAmount/totalTopAmount, CurrentDate);    
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