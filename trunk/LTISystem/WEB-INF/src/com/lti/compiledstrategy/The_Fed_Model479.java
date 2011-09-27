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
public class The_Fed_Model479 extends SimulateStrategy{
	public The_Fed_Model479(){
		super();
		StrategyID=479L;
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
	private String equitysecurity;
	public void setEquitysecurity(String equitysecurity){
		this.equitysecurity=equitysecurity;
	}
	
	public String getEquitysecurity(){
		return this.equitysecurity;
	}
	private String bondsecurity;
	public void setBondsecurity(String bondsecurity){
		this.bondsecurity=bondsecurity;
	}
	
	public String getBondsecurity(){
		return this.bondsecurity;
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
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		AdjustFrequency=(String)ParameterUtil.fetchParameter("String","AdjustFrequency", "month", parameters);
		equitysecurity=(String)ParameterUtil.fetchParameter("String","equitysecurity", "VFINX", parameters);
		bondsecurity=(String)ParameterUtil.fetchParameter("String","bondsecurity", "VBMFX", parameters);
		benchmarkofearningyield=(String)ParameterUtil.fetchParameter("String","benchmarkofearningyield", "^GSPC", parameters);
		benchmarkofbondyield=(String)ParameterUtil.fetchParameter("String","benchmarkofbondyield", "VBMFX", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	//holdingbond=true means holding bond,or else holding equity;
boolean holdingbond;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("holdingbond: ");
		sb.append(holdingbond);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(holdingbond);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		holdingbond=(Boolean)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
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

printToLog("DY"+bond);






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

Asset CurrentAsset ;
CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2switch");
CurrentAsset.setClassID(getAssetClassID(getSecurity (equitysecurity).getName()));
CurrentAsset.setTargetPercentage(1.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy("Asset2switch", bondsecurity, TotalAmount,CurrentDate);
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

printToLog("DY"+bond);


printToLog("equity is "+equity);
printToLog("bond is "+bond);

//start to judge
if((equity<bond)&&(holdingbond==false))
{
printToLog("switch to bond");
CurrentPortfolio.sellAsset("Asset2switch", CurrentDate);
CurrentPortfolio.buy("Asset2switch", bondsecurity, TotalAmount,CurrentDate);
holdingbond=true;
}

if((equity>=bond)&&(holdingbond==true))
{
printToLog("switch to equity");
CurrentPortfolio.sellAsset("Asset2switch", CurrentDate);
CurrentPortfolio.buy("Asset2switch", equitysecurity, TotalAmount,CurrentDate);
holdingbond=false;
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