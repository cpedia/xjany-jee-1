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
public class Closed_End_Fund_Discount_Strategy600 extends SimulateStrategy{
	public Closed_End_Fund_Discount_Strategy600(){
		super();
		StrategyID=600L;
		StrategyClassID=2L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int SharpeYear;
	public void setSharpeYear(int SharpeYear){
		this.SharpeYear=SharpeYear;
	}
	
	public int getSharpeYear(){
		return this.SharpeYear;
	}
	private int DiscountYear;
	public void setDiscountYear(int DiscountYear){
		this.DiscountYear=DiscountYear;
	}
	
	public int getDiscountYear(){
		return this.DiscountYear;
	}
	private String[] Funds;
	public void setFunds(String[] Funds){
		this.Funds=Funds;
	}
	
	public String[] getFunds(){
		return this.Funds;
	}
	private String ReferenceFund;
	public void setReferenceFund(String ReferenceFund){
		this.ReferenceFund=ReferenceFund;
	}
	
	public String getReferenceFund(){
		return this.ReferenceFund;
	}
	private double BuyStop;
	public void setBuyStop(double BuyStop){
		this.BuyStop=BuyStop;
	}
	
	public double getBuyStop(){
		return this.BuyStop;
	}
	private double discountMin;
	public void setDiscountMin(double discountMin){
		this.discountMin=discountMin;
	}
	
	public double getDiscountMin(){
		return this.discountMin;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		SharpeYear=(Integer)ParameterUtil.fetchParameter("int","SharpeYear", "1", parameters);
		DiscountYear=(Integer)ParameterUtil.fetchParameter("int","DiscountYear", "1", parameters);
		Funds=(String[])ParameterUtil.fetchParameter("String[]","Funds", "UTF,  ERH, GLU, GUT, MFD, MGU, UTG", parameters);
		ReferenceFund=(String)ParameterUtil.fetchParameter("String","ReferenceFund", "XLU", parameters);
		BuyStop=(Double)ParameterUtil.fetchParameter("double","BuyStop", "0.10", parameters);
		discountMin=(Double)ParameterUtil.fetchParameter("double","discountMin", "0.0", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	String position;
String original;
boolean flag;
String sCurSec;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("position: ");
		sb.append(position);
		sb.append("\n");
		sb.append("original: ");
		sb.append(original);
		sb.append("\n");
		sb.append("flag: ");
		sb.append(flag);
		sb.append("\n");
		sb.append("sCurSec: ");
		sb.append(sCurSec);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(position);
		stream.writeObject(original);
		stream.writeObject(flag);
		stream.writeObject(sCurSec);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		position=(String)stream.readObject();;
		original=(String)stream.readObject();;
		flag=(Boolean)stream.readObject();;
		sCurSec=(String)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		sCurSec=ReferenceFund;
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
		   String[] TopSecurityArray;
Security Sec; 
double sharpe, discountRate;
double originalsharpe;
int i;


/*bond=getTopSecurity(CEF,3,-sharpeyear,CurrentDate,TimeUnit.YEARLY,SortType.SHARPE,true).get(0);*/

//THE ONE WITH THE BEST SECURITY
TopSecurityArray = 
   getTopSecurityArray(Funds,-SharpeYear*252, CurrentDate, TimeUnit.DAILY, 
         SortType.SHARPE, true);

originalsharpe=getSecurity(ReferenceFund).getSharpeRatio(
     -SharpeYear*252, CurrentDate, TimeUnit.DAILY, false);
double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);     

for(i=0;i<TopSecurityArray.length;i++) {
    if(TopSecurityArray[i]==null) break;
    printToLog(TopSecurityArray[i]);
    Sec = getSecurity(TopSecurityArray[i]);
    sharpe=Sec.getSharpeRatio(-SharpeYear*252,CurrentDate, TimeUnit.DAILY,true);

    printToLog("Discount Rate"+Sec.getDiscountRate(CurrentDate));
    printToLog("Average Discount Rate:"+
      Sec.getAverageDiscountRate(-DiscountYear,TimeUnit.YEARLY, CurrentDate));
    printToLog("sharperatio:"+sharpe);
    printToLog("original sharperatio:"+originalsharpe);
    if ((sharpe < originalsharpe)) break;
    discountRate = Sec.getDiscountRate(CurrentDate);
    if (((discountMin<=0) && (discountRate<=discountMin))||
         ((discountMin>0)&&(discountRate<=0)&&(discountRate < 
       Sec.getAverageDiscountRate(-DiscountYear,TimeUnit.YEARLY,CurrentDate)*(1+BuyStop)))) {
       if (!sCurSec.equals(Sec.getName())) {
       CurrentPortfolio.sellAsset(curAsset,CurrentDate);       
       CurrentPortfolio.buy (curAsset, Sec.getName(), amount, CurrentDate);
       sCurSec=Sec.getName();
      }
       printToLog("Buy in CEF");
       i=10000;
       break;
   }
}
/* if there is nothing to buy, buy the original when the current holding is no longer a bargain*/
if (i!=10000) {
   sharpe = getSecurity(sCurSec).getSharpeRatio(-SharpeYear*252,CurrentDate, TimeUnit.DAILY,true);
   if (!sCurSec.equals(ReferenceFund) && 
       ((sharpe <originalsharpe)||(getSecurity(sCurSec).getDiscountRate(CurrentDate)>0))) {
       CurrentPortfolio.sellAsset(curAsset,CurrentDate);       
       CurrentPortfolio.buy (curAsset, ReferenceFund, amount, CurrentDate);
       sCurSec=ReferenceFund;
       printToLog("Buy in ReferenceFund "+ ReferenceFund);
  }
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