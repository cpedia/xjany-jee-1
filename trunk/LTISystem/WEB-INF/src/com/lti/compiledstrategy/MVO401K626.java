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
public class MVO401K626 extends SimulateStrategy{
	public MVO401K626(){
		super();
		StrategyID=626L;
		StrategyClassID=2L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int Months;
	public void setMonths(int Months){
		this.Months=Months;
	}
	
	public int getMonths(){
		return this.Months;
	}
	private String[] Funds;
	public void setFunds(String[] Funds){
		this.Funds=Funds;
	}
	
	public String[] getFunds(){
		return this.Funds;
	}
	private String[] ExtraFunds;
	public void setExtraFunds(String[] ExtraFunds){
		this.ExtraFunds=ExtraFunds;
	}
	
	public String[] getExtraFunds(){
		return this.ExtraFunds;
	}
	private double RAF;
	public void setRAF(double RAF){
		this.RAF=RAF;
	}
	
	public double getRAF(){
		return this.RAF;
	}
	private String RebalanceFrequency;
	public void setRebalanceFrequency(String RebalanceFrequency){
		this.RebalanceFrequency=RebalanceFrequency;
	}
	
	public String getRebalanceFrequency(){
		return this.RebalanceFrequency;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		Months=(Integer)ParameterUtil.fetchParameter("int","Months", "12", parameters);
		Funds=(String[])ParameterUtil.fetchParameter("String[]","Funds", "CHTVX, FOCPX, FUSVX, MSIVX, WFSVX, ^DWC, FSEMX, FIGRX, HAINX, FBNDX, VBMFX", parameters);
		ExtraFunds=(String[])ParameterUtil.fetchParameter("String[]","ExtraFunds", "CASH", parameters);
		RAF=(Double)ParameterUtil.fetchParameter("double","RAF", "4", parameters);
		RebalanceFrequency=(String)ParameterUtil.fetchParameter("String","RebalanceFrequency", "monthly", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	int iLength;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("iLength: ");
		sb.append(iLength);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(iLength);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		iLength=(Integer)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		printToLog("starting");
if (Months==0) { iLength=12; }else { iLength=Months;}
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
		
		 
		else if ((RebalanceFrequency.equals("monthly")&&LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) || (RebalanceFrequency.equals("quarterly")&&LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate))) {
		   Security Sec; 
double discountRate;
Date startDate,  endDate;  
int i;
double CashReturn=0.0, FundReturn;
List<String> securityList = new ArrayList<String>();
List<Double> lowerLimits = new ArrayList<Double>();
List<Double> upperLimits = new ArrayList<Double>();
List<Double> expectedExcessReturns = new ArrayList<Double>();
LTIMVOInterface li = new LTIMVOInterface();
printToLog("working on " + CurrentDate);

 for (i=0; i<ExtraFunds.length; i++) {
        securityList.add(ExtraFunds[i]);
        lowerLimits.add(0.0);
        upperLimits.add(1.0);
        if (Months==0) {
         expectedExcessReturns.add(0.0);
        } else {
         expectedExcessReturns.add(null);
        }
 }

/* get cheap funds */
for (i=0; i<Funds.length; i++) {
    Sec=getSecurity(Funds[i]);
    
     printToLog("Security= "+Funds[i]);
     printToLog("EarliestAvaliablePriceDate= " +getEarliestAvaliablePriceDate(Sec));
     printToLog("New Month= "+LTIDate.getNewNYSEMonth(CurrentDate, -iLength));
    if (Months==0) {
      /* get the score for the CASH */
     CashReturn =(getSecurity("CASH").getReturn(CurrentDate, TimeUnit.MONTHLY, -12)
      + getSecurity("CASH").getReturn(CurrentDate, TimeUnit.MONTHLY, -9)
      + getSecurity("CASH").getReturn(CurrentDate, TimeUnit.MONTHLY, -6)
      + getSecurity("CASH").getReturn(CurrentDate, TimeUnit.MONTHLY, -3)
      + getSecurity("CASH").getReturn(CurrentDate, TimeUnit.MONTHLY, -1) )/5.0;
    }

    if (LTIDate.before(getEarliestAvaliablePriceDate(Sec), LTIDate.getNewNYSEMonth(CurrentDate, -12))) {
       if (Months==0) {
            FundReturn = (Sec.getReturn(CurrentDate, TimeUnit.MONTHLY, -12)
      + Sec.getReturn(CurrentDate, TimeUnit.MONTHLY, -9)
      + Sec.getReturn(CurrentDate, TimeUnit.MONTHLY, -6)
      + Sec.getReturn(CurrentDate, TimeUnit.MONTHLY, -3)
      + Sec.getReturn(CurrentDate, TimeUnit.MONTHLY, -1) )/5.0;
            if (FundReturn>=CashReturn) {
                securityList.add(Funds[i]);
                lowerLimits.add(0.0);
                upperLimits.add(1.0);
                expectedExcessReturns.add(FundReturn-CashReturn);
            }
        } else {
            securityList.add(Funds[i]);
             lowerLimits.add(0.0);
             upperLimits.add(1.0);
              expectedExcessReturns.add(null);
        }
    }
}

startDate = LTIDate.getNewNYSEMonth(CurrentDate, -iLength);
endDate = CurrentDate;

/* for debug purpose */
printToLog("securityList size: " + securityList.size() + " lowerLimits size " + lowerLimits.size() + " upperLimits size " + upperLimits.size() + " expectedExcessReturns size: " + expectedExcessReturns.size() + " RAF: " +RAF + " startDate: "+ startDate);
boolean bNonCash=false;
for(i=0; i<securityList.size(); i++ ) {
   if (!securityList.get(i).equals("CASH")) bNonCash=true;
   printToLog("securityList["+i+"]= "+securityList.get(i));
   printToLog("lowerLimits["+i+"]= "+lowerLimits.get(i));
   printToLog("upperLimits["+i+"]= "+upperLimits.get(i));
   printToLog("expectedExcessReturns["+i+"]="+expectedExcessReturns.get(i));
}

double amount = CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset, CurrentDate);

/* only when there is noncash security */
if (bNonCash) {
   li.createModel(securityList, lowerLimits, upperLimits, expectedExcessReturns, RAF, startDate,  endDate, TimeUnit.DAILY, true); /* false means using NAV instead of price */

 List<Double> weights=null;
  try{
   weights = li.getMVOWeightsWithLimits();
}catch(Exception e){
   printToLog(StringUtil.getStackTraceString(e));
printToLog(startDate);
printToLog(endDate);
printToLog(RAF);
  throw e;
}
   /* for debug purpose */
   List<Double> returns = li.checkExpectedReturns();
   for (i = 0; i < returns.size(); i++) {
    printToLog(securityList.get(i) + ": " + returns.get(i));
   }

   for (i=0;i<weights.size();++i) {
    printToLog(weights.get(i));
  }

  /* now for the buy and sell */
  double amountBought=0.0;

  /* last one should be left to make it to be full amount */
  for (i=0; i<securityList.size()-1; i++) {
      if (weights.get(i)>=0.01) {
        if ((amountBought+amount*(weights.get(i)))<=amount) {
          printToLog("weights.get("+i+") "+weights.get(i));
         CurrentPortfolio.buy(curAsset, securityList.get(i), amount*weights.get(i), CurrentDate);
         amountBought += amount*weights.get(i);
       } else {
        break;
      }
    }
  }
  if (amount-amountBought >0) 
  CurrentPortfolio.buy(curAsset, securityList.get(securityList.size()-1), amount-amountBought,  CurrentDate);
} else {
  /* just buy CASH */
 CurrentPortfolio.buy(curAsset, "CASH",  amount, CurrentDate); 
}

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