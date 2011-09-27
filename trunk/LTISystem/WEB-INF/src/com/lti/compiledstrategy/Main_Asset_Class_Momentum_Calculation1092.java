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
public class Main_Asset_Class_Momentum_Calculation1092 extends SimulateStrategy{
	public Main_Asset_Class_Momentum_Calculation1092(){
		super();
		StrategyID=1092L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String[] MainAssetClass;
	public void setMainAssetClass(String[] MainAssetClass){
		this.MainAssetClass=MainAssetClass;
	}
	
	public String[] getMainAssetClass(){
		return this.MainAssetClass;
	}
	private double CashScoreWeight;
	public void setCashScoreWeight(double CashScoreWeight){
		this.CashScoreWeight=CashScoreWeight;
	}
	
	public double getCashScoreWeight(){
		return this.CashScoreWeight;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		MainAssetClass=(String[])ParameterUtil.fetchParameter("String[]","MainAssetClass", "US EQUITY, INTERNATIONAL EQUITY, FIXED INCOME, REAL ESTATE, COMMODITIES, Emerging Market, INTERNATIONAL BONDS, High Yield Bond, CASH, BALANCE FUND", parameters);
		CashScoreWeight=(Double)ParameterUtil.fetchParameter("double","CashScoreWeight", "1.3", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	boolean beenUpdated = false;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("beenUpdated: ");
		sb.append(beenUpdated);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(beenUpdated);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		beenUpdated=(Boolean)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	private java.text.DateFormat _df = new java.text.SimpleDateFormat("yyyy-MM-dd");

@Override
  public void afterExecuting()   throws Exception {
    // TODO Auto-generated method stub
    super.afterExecuting();
    String dateStr = _df.format(CurrentDate);
        com.lti.util.PersistentUtil.writeGlobalObject(MainAssetClass, "AssetMomentumRank_" + PortfolioID,StrategyID,PortfolioID);
        com.lti.util.PersistentUtil.writeGlobalObject(dateStr, "AssetMomentumRankDate_" + PortfolioID,StrategyID,PortfolioID);
  }
  @Override
  public void afterUpdating()   throws Exception {
    super.afterUpdating();
    String dateStr = _df.format(CurrentDate);
    if(beenUpdated)
        com.lti.util.PersistentUtil.writeGlobalObject(MainAssetClass, "AssetMomentumRank_" + PortfolioID,StrategyID,PortfolioID);
        com.lti.util.PersistentUtil.writeGlobalObject(dateStr, "AssetMomentumRankDate_" + PortfolioID,StrategyID,PortfolioID);
  }





public double[] getMomentumScore(String[] Securities,  double CashScoreWeight , Date CurrentDate) throws Exception {
double[] score=new double[Securities.length];
for(int i=0; i<score.length; i++)
{
score[i]=0;
}

double a;
double bp1=0;
double bp2=0;
double bp3=0;
double bp4=0;

String[] newArray1=getTopSecurityArray(Securities,-12, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray2=getTopSecurityArray(Securities, -6, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray3=getTopSecurityArray(Securities, -3, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);
String[] newArray4=getTopSecurityArray(Securities, -1, CurrentDate, TimeUnit.MONTHLY, SortType.RETURN, true);

Security Fund = null;
Security Bench = null;
double FundSD = 0;
double BenchSD = 0;

for(int i=0; i<=Securities.length-1; i++)
{
if(LTIDate.before(getEarliestAvaliablePriceDate(getSecurity(Securities[i])), LTIDate. getNewNYSEMonth(CurrentDate, -12)))
{
a =(getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12)
+ getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6)
+ getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -3)
+ getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -1) )*100/4;
  
  //printToLog(Securities[i] + "1 month return : " + (getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -1)*100));
  //printToLog(Securities[i] + "3 month return : " + (getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -3)*100));
  //printToLog(Securities[i] + "6 month return : " + (getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -6)*100));
  //printToLog(Securities[i] + "12 month return : " + (getSecurity(Securities[i]).getReturn(CurrentDate, TimeUnit.MONTHLY, -12)*100));
  
//printToLog("Original Score: "+Securities[i]+" : "+a);

/*Adj for Cash, * CashScoreWeight 2010.04.14*/
if(Securities[i].equals("CASH"))   a =a * CashScoreWeight;

bp1=0;
bp2=0;
bp3=0;
bp4=0;

for(int j=0; j<=newArray1.length-1; j++)
{
if(Securities[i].equals(newArray1[j]))
    { 
     if(j<=score.length/2-1)
        bp1=1;
     else
        bp1=0;
    }
}


for(int j=0; j<=newArray2.length-1; j++)
{
    if(Securities[i].equals(newArray2[j]))
   { 
     if(j<=score.length/2-1)
        bp2=1;
     else
        bp2=0;
   }
}


for(int j=0; j<=newArray3.length-1; j++)
{
    if(Securities[i].equals(newArray3[j]))
    { 
     if(j<=score.length/2-1)
        bp3=1;
     else
        bp3=0;
    }
}


for(int j=0; j<=newArray4.length-1;j++)
{
 if(Securities[i].equals(newArray4[j]))
     { 
      if(j<=score.length/2-1)
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
return score;
}
  
  
  public double[] getTAAMomentumScore(Security[] Securities, double CashScoreWeight, Date CurrentDate)  throws Exception {
    double[][] returns = new double[4][Securities.length];
    int tb = 0;

    int[][] ranks = new int[4][Securities.length];
    for (int i = 0; i < Securities.length; i++) {
      Security security = Securities[i];
      for (int j = 0; j < 4; j++) {
        int month = -12;
        if (j == 1)
          month = -6;
        else if (j == 2)
          month = -3;
        else if (j == 3)
          month = -1;
        double r=0.0;
        if(LTIDate.before(security.getStartDate(), LTIDate.getNewNYSEMonth(CurrentDate, -12))){
          r = security.getReturn(CurrentDate, TimeUnit.MONTHLY, month);
        }else{
          r = -100000.0;
          printToLog("no return for "+security.getSymbol());
        }

        returns[j][i] = r;

      }
    }
    // 排序有重复？
    for (int i = 0; i < Securities.length; i++) {
      for (int j = 0; j < 4; j++) {
        int count = 0;
        for (int ii = 0; ii < Securities.length; ii++) {
          if (returns[j][i] >= returns[j][ii] && i != ii)
            count++;
        }
        ranks[j][i] = Securities.length - count;
        //printToLog("rank ["+j +","+ i +"] = " + ranks[j][i]);
      }
    }

    double[] scores = new double[Securities.length];
    for (int i = 0; i <= Securities.length - 1; i++) {
      double score = -100000;
      Security security = Securities[i];
      if (LTIDate.before(security.getStartDate(), LTIDate.getNewNYSEMonth(CurrentDate, -12))) {
        double a = (returns[0][i] + returns[1][i] + returns[2][i] + returns[3][i]) * 100 / 4;

        //printToLog("Original Score: "+Securities[i].getSymbol()+" : "+a);

        /* Adj for Cash, * CashScoreWeight 2010.04.14 */
        if (Securities[i].equals("CASH"))
          a = a * CashScoreWeight;

        int acc = 0;
        for (int j = 0; j < 4; j++) {
          if (ranks[j][i] <= Securities.length / 2)
          {
            acc++; tb++;
            //printToLog(Securities[i] + " has bonus " + j);
          }
        }

        score = a + acc;
      }
      scores[i] = score;
      //printToLog("Finanl Score: "+Securities[i].getSymbol()+" : "+ score);
    }
    //printToLog("Total bonus = " + tb);
    return scores;
  }

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		
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
	
		
		beenUpdated = false;
		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.isNYSETradingDay(CurrentDate)) {
		   String[] Benchmarks = new String[MainAssetClass.length];
Security[] BenchmarkSecurity = new Security[MainAssetClass.length];
double[] MomentumScores = new double[MainAssetClass.length];

for(int i = 0; i < MainAssetClass.length; i++)
   Benchmarks[i] = getSecurity(getAssetClass(MainAssetClass[i]).getBenchmarkID()).getSymbol();
//MomentumScores = getMomentumScore(Benchmarks, CashScoreWeight, CurrentDate);

for(int i = 0; i < MainAssetClass.length; i++)
    BenchmarkSecurity[i] = getSecurity(getAssetClass(MainAssetClass[i]).getBenchmarkID());
MomentumScores = getTAAMomentumScore(BenchmarkSecurity, CashScoreWeight, CurrentDate);

beenUpdated = true;

double MaxScore;
int MaxScoreIndex;
for(int i=0; i< MainAssetClass.length; i++)
{
 
 MaxScore = MomentumScores[i];
 MaxScoreIndex =i;
  
   for(int j=i+1; j<=MomentumScores.length-1; j++)
   {     
     if(MaxScore <= MomentumScores[j])
     {
      MaxScore = MomentumScores[j];
      MaxScoreIndex = j;
     }
   }
   
   if(MaxScoreIndex !=i)
   {
     String tmp1= Benchmarks[MaxScoreIndex];
     Benchmarks[MaxScoreIndex]= Benchmarks[i];
     Benchmarks[i]=tmp1; 	 

     String tmp2= MainAssetClass[MaxScoreIndex];
     MainAssetClass[MaxScoreIndex]= MainAssetClass[i];
     MainAssetClass[i]=tmp2; 	 
   
     double t=MomentumScores[MaxScoreIndex];
     MomentumScores[MaxScoreIndex]=MomentumScores[i];
     MomentumScores[i]=t;
   }
}

printToLog("Momentum Score Ranking :   "  +  CurrentDate);
for(int i = 0; i < MainAssetClass.length; i++)
    printToLog(MainAssetClass[i] + "   ( Benchmark: " + Benchmarks[i] + " )     Score  =  " +   (double)Math.round(MomentumScores[i]*100) /100 );
printToLog(" " );

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