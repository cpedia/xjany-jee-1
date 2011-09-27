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
public class Beating_the_Dow_with_Bonds483 extends SimulateStrategy{
	public Beating_the_Dow_with_Bonds483(){
		super();
		StrategyID=483L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String AdjustFrequency;
	public void setAdjustFrequency(String AdjustFrequency){
		this.AdjustFrequency=AdjustFrequency;
	}
	
	public String getAdjustFrequency(){
		return this.AdjustFrequency;
	}
	private String shorttermbond;
	public void setShorttermbond(String shorttermbond){
		this.shorttermbond=shorttermbond;
	}
	
	public String getShorttermbond(){
		return this.shorttermbond;
	}
	private String longtermbond;
	public void setLongtermbond(String longtermbond){
		this.longtermbond=longtermbond;
	}
	
	public String getLongtermbond(){
		return this.longtermbond;
	}
	private String equitysecurity;
	public void setEquitysecurity(String equitysecurity){
		this.equitysecurity=equitysecurity;
	}
	
	public String getEquitysecurity(){
		return this.equitysecurity;
	}
	private String benchmarkofearningyield;
	public void setBenchmarkofearningyield(String benchmarkofearningyield){
		this.benchmarkofearningyield=benchmarkofearningyield;
	}
	
	public String getBenchmarkofearningyield(){
		return this.benchmarkofearningyield;
	}
	private String benchmarkofbondyield;
	public void setBenchmarkofbondyield(String benchmarkofbondyield){
		this.benchmarkofbondyield=benchmarkofbondyield;
	}
	
	public String getBenchmarkofbondyield(){
		return this.benchmarkofbondyield;
	}
	private String goldpricesecurity;
	public void setGoldpricesecurity(String goldpricesecurity){
		this.goldpricesecurity=goldpricesecurity;
	}
	
	public String getGoldpricesecurity(){
		return this.goldpricesecurity;
	}
	private int startyear;
	public void setStartyear(int startyear){
		this.startyear=startyear;
	}
	
	public int getStartyear(){
		return this.startyear;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		AdjustFrequency=(String)ParameterUtil.fetchParameter("String","AdjustFrequency", "month", parameters);
		shorttermbond=(String)ParameterUtil.fetchParameter("String","shorttermbond", "VFSTX", parameters);
		longtermbond=(String)ParameterUtil.fetchParameter("String","longtermbond", "VUSTX", parameters);
		equitysecurity=(String)ParameterUtil.fetchParameter("String","equitysecurity", "VFINX", parameters);
		benchmarkofearningyield=(String)ParameterUtil.fetchParameter("String","benchmarkofearningyield", "^GSPC", parameters);
		benchmarkofbondyield=(String)ParameterUtil.fetchParameter("String","benchmarkofbondyield", "VUSTX", parameters);
		goldpricesecurity=(String)ParameterUtil.fetchParameter("String","goldpricesecurity", "GLD", parameters);
		startyear=(Integer)ParameterUtil.fetchParameter("int","startyear", "", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	//holdingbond=true means holding bond,or else holding equity;
boolean holdingbond;
int rollingyear;
boolean isnewyear=false;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("holdingbond: ");
		sb.append(holdingbond);
		sb.append("\n");
		sb.append("rollingyear: ");
		sb.append(rollingyear);
		sb.append("\n");
		sb.append("isnewyear: ");
		sb.append(isnewyear);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(holdingbond);
		stream.writeObject(rollingyear);
		stream.writeObject(isnewyear);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		holdingbond=(Boolean)stream.readObject();;
		rollingyear=(Integer)stream.readObject();;
		isnewyear=(Boolean)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		rollingyear=startyear;

CurrentPortfolio.sellAssetCollection(CurrentDate);
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);

/*ranking*/
double equity,bond;

equity=1/(getLastYearPE(benchmarkofearningyield,CurrentDate));

//^TNX needs to be specially treated .
if(benchmarkofbondyield.equals("^TNX"))
{bond=getSecurity("^TNX").getCurrentPrice(CurrentDate)/100;}else
{
bond=getLastYearDividendYield(benchmarkofbondyield,CurrentDate);
}







//start to judge
if(equity>bond)
{printToLog("buy equity");
holdingbond=false;

Asset CurrentAsset ;
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2switch");
CurrentAsset.setClassID(getAssetClassID(getSecurity (equitysecurity).getName()));
CurrentAsset.setTargetPercentage(1.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2switch", equitysecurity, TotalAmount,CurrentDate);

}else

{
holdingbond=true;
printToLog("buy bond");

 /*judge on goldprice */
double priorgoldprice=getSecurity(goldpricesecurity)
.getCurrentPrice(LTIDate.getDate(rollingyear-1,1,1));
printToLog(priorgoldprice);

double goldprice=getSecurity(goldpricesecurity)
.getCurrentPrice(LTIDate.getDate(rollingyear,1,1));
printToLog(goldprice);

 if(goldprice>=priorgoldprice)  {
Asset CurrentAsset ;
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2switch");
CurrentAsset.setClassID(getAssetClassID(getSecurity (shorttermbond).getName()));
CurrentAsset.setTargetPercentage(1.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2switch", shorttermbond, TotalAmount,CurrentDate);
holdingbond=true;
         } else {

Asset CurrentAsset ;
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2switch");
CurrentAsset.setClassID(getAssetClassID(getSecurity (longtermbond).getName()));
CurrentAsset.setTargetPercentage(1.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2switch", longtermbond, TotalAmount,CurrentDate);
holdingbond=true;
  }


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
		
		 
		else if ((AdjustFrequency.equals("year") && LTIDate.isLastNYSETradingDayOfYear(CurrentDate))
||(AdjustFrequency.equals("quarter") && LTIDate.isLastNYSETradingDayOfQuarter(CurrentDate)) 
||(AdjustFrequency.equals("month") && LTIDate.isLastNYSETradingDayOfMonth(CurrentDate))) {
		   double equity,bond;
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);

/*ranking*/
equity=1/(getLastYearPE(benchmarkofearningyield,CurrentDate));

if(benchmarkofbondyield.equals("^TNX"))
{bond=getSecurity("^TNX").getCurrentPrice(CurrentDate)/100;
}else

{bond=getLastYearDividendYield(benchmarkofbondyield,CurrentDate);
}

printToLog("equity is "+equity);
printToLog("bond is "+bond);

//start to judge
    if((equity<bond)&&(holdingbond==false))     {
   printToLog("switch to bond");

/*judge on goldprice */

printToLog(rollingyear);

double priorgoldprice=getSecurity(goldpricesecurity).getCurrentPrice
(LTIDate.getDate(rollingyear-1,1,31));
double goldprice=getSecurity(goldpricesecurity).getCurrentPrice
(LTIDate.getDate(rollingyear,1,31));

             if(goldprice>=priorgoldprice)  {
CurrentPortfolio.sellAsset("Asset2switch", CurrentDate);
CurrentPortfolio.buy("Asset2switch", shorttermbond, TotalAmount,CurrentDate);
holdingbond=true;
         } else {
CurrentPortfolio.sellAsset("Asset2switch", CurrentDate);
CurrentPortfolio.buy("Asset2switch", longtermbond, TotalAmount,CurrentDate);
holdingbond=true;
     }

}


    if((equity>=bond)&&(holdingbond==true))    {

printToLog("switch to equity");
CurrentPortfolio.sellAsset("Asset2switch", CurrentDate);
CurrentPortfolio.buy("Asset2switch", equitysecurity, TotalAmount,CurrentDate);
holdingbond=false;
}

if(LTIDate.isLastNYSETradingDayOfYear(CurrentDate))   {

rollingyear++;
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