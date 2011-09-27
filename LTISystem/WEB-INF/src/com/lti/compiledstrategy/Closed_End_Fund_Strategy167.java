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
public class Closed_End_Fund_Strategy167 extends SimulateStrategy{
	public Closed_End_Fund_Strategy167(){
		super();
		StrategyID=167L;
		StrategyClassID=2L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int sharpeyear;
	public void setSharpeyear(int sharpeyear){
		this.sharpeyear=sharpeyear;
	}
	
	public int getSharpeyear(){
		return this.sharpeyear;
	}
	private int discountyear;
	public void setDiscountyear(int discountyear){
		this.discountyear=discountyear;
	}
	
	public int getDiscountyear(){
		return this.discountyear;
	}
	private double sellstop;
	public void setSellstop(double sellstop){
		this.sellstop=sellstop;
	}
	
	public double getSellstop(){
		return this.sellstop;
	}
	private double buystop;
	public void setBuystop(double buystop){
		this.buystop=buystop;
	}
	
	public double getBuystop(){
		return this.buystop;
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
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		sharpeyear=(Integer)ParameterUtil.fetchParameter("int","sharpeyear", "3", parameters);
		discountyear=(Integer)ParameterUtil.fetchParameter("int","discountyear", "3", parameters);
		sellstop=(Double)ParameterUtil.fetchParameter("double","sellstop", "0.2", parameters);
		buystop=(Double)ParameterUtil.fetchParameter("double","buystop", "0.2", parameters);
		Funds=(String[])ParameterUtil.fetchParameter("String[]","Funds", " ERH, GLU, GUT, MFD, MGU, UTG", parameters);
		ReferenceFund=(String)ParameterUtil.fetchParameter("String","ReferenceFund", "XLU", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	String position;
String original;
boolean flag;
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
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(position);
		stream.writeObject(original);
		stream.writeObject(flag);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		position=(String)stream.readObject();;
		original=(String)stream.readObject();;
		flag=(Boolean)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		flag=true; 
original = ReferenceFund;

//original=CurrentPortfolio.getAssetSecurities(curAsset).get(0).getName();
printToLog(original);
position="";

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
		
		 
		else if (LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)&&(flag==true)) {
		   //List <Security > CEF= getCEF( CurrentPortfolio.getAsset(curAsset).getAssetClassID());
//List <Security > TopSecurityList=null;
String[] TopSecurityArray;
Security bond;
double sharpe;
double originalsharpe;


/*bond=getTopSecurity(CEF,3,-sharpeyear,CurrentDate,TimeUnit.YEARLY,SortType.SHARPE,true).get(0);*/

//THE ONE WITH THE BEST SECURITY
TopSecurityArray = 
getTopSecurityArray(Funds,-sharpeyear, CurrentDate, TimeUnit.YEARLY, SortType.SHARPE, true);


for(int i=0;i<TopSecurityArray.length;i++)
{
if(TopSecurityArray[i]==null) break;
printToLog(TopSecurityArray[i]);
bond = getSecurity(TopSecurityArray[i]);
sharpe=bond.getSharpeRatio(LTIDate.getNewNYSEYear(CurrentDate,-sharpeyear),CurrentDate,TimeUnit.DAILY,true);
originalsharpe=getSecurity(original).getSharpeRatio(LTIDate.getNewNYSEYear(CurrentDate,-sharpeyear),CurrentDate,TimeUnit.DAILY,false);

printToLog("Discount Rate"+bond.getDiscountRate(CurrentDate));
printToLog("Average Discount Rate:"+bond.getAverageDiscountRate(-discountyear, TimeUnit.YEARLY, CurrentDate)*(1+buystop));
printToLog("sharperatio:"+sharpe);
printToLog("original sharperatio:"+originalsharpe);

if((bond.getDiscountRate(CurrentDate)<bond.getAverageDiscountRate(-discountyear,TimeUnit.YEARLY,CurrentDate)*(1+buystop))&&(sharpe>originalsharpe))
//to judge if the best fund has a low discount rate compared with its average discount rate. 


{double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
position= bond.getName();
//take down the name of the fund held
CurrentPortfolio.buy (curAsset, position, amount, CurrentDate);
// substitute current position for the fund which has a good sharpe ratio and a low discount rate.
printToLog("Buy in CEF");
   flag=false;
i=1000;
}

}
/* end of none null TopSecurityList. If it is null, do nothing, still uses the existing one */
		}
		else if (LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)&&(flag==false)) {
		   printToLog(position+"Discount Rate"+getSecurity(position).getDiscountRate(CurrentDate));
printToLog(position+"Average DiscountRate"+getSecurity(position).getAverageDiscountRate(-discountyear, TimeUnit.YEARLY, CurrentDate)*(1-sellstop));


if(getSecurity(position).getDiscountRate(CurrentDate)<getSecurity(position).getAverageDiscountRate(-discountyear, TimeUnit.YEARLY, CurrentDate)*(1-sellstop))
//judge if the discount rate of the fund falls below its average by a certain percentage.


{double amount=CurrentPortfolio.getAssetAmount(curAsset, CurrentDate);
CurrentPortfolio.sellAsset(curAsset,CurrentDate);
CurrentPortfolio.buy (curAsset, original , amount, CurrentDate);
//sell the fund if its discount rate rises above average by a certain percentage and get back cash.

printToLog("Change back to original position");
   flag=true;
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