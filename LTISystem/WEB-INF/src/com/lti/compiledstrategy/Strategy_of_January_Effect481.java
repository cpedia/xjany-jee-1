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
public class Strategy_of_January_Effect481 extends SimulateStrategy{
	public Strategy_of_January_Effect481(){
		super();
		StrategyID=481L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private int startyear;
	public void setStartyear(int startyear){
		this.startyear=startyear;
	}
	
	public int getStartyear(){
		return this.startyear;
	}
	private String securitytolong;
	public void setSecuritytolong(String securitytolong){
		this.securitytolong=securitytolong;
	}
	
	public String getSecuritytolong(){
		return this.securitytolong;
	}
	private boolean shortsellbigcap;
	public void setShortsellbigcap(boolean shortsellbigcap){
		this.shortsellbigcap=shortsellbigcap;
	}
	
	public boolean getShortsellbigcap(){
		return this.shortsellbigcap;
	}
	private String securitytoshort;
	public void setSecuritytoshort(String securitytoshort){
		this.securitytoshort=securitytoshort;
	}
	
	public String getSecuritytoshort(){
		return this.securitytoshort;
	}
	private double percentageofshort;
	public void setPercentageofshort(double percentageofshort){
		this.percentageofshort=percentageofshort;
	}
	
	public double getPercentageofshort(){
		return this.percentageofshort;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		startyear=(Integer)ParameterUtil.fetchParameter("int","startyear", "2000", parameters);
		securitytolong=(String)ParameterUtil.fetchParameter("String","securitytolong", "IWM", parameters);
		shortsellbigcap=(Boolean)ParameterUtil.fetchParameter("boolean","shortsellbigcap", "true", parameters);
		securitytoshort=(String)ParameterUtil.fetchParameter("String","securitytoshort", "IWV", parameters);
		percentageofshort=(Double)ParameterUtil.fetchParameter("double","percentageofshort", "0.5", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	int rollingyear;
boolean position=false;
Date startdate,enddate;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("rollingyear: ");
		sb.append(rollingyear);
		sb.append("\n");
		sb.append("position: ");
		sb.append(position);
		sb.append("\n");
		sb.append("startdate: ");
		sb.append(startdate);
		sb.append("\n");
		sb.append("enddate: ");
		sb.append(enddate);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(rollingyear);
		stream.writeObject(position);
		stream.writeObject(startdate);
		stream.writeObject(enddate);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		rollingyear=(Integer)stream.readObject();;
		position=(Boolean)stream.readObject();;
		startdate=(Date)stream.readObject();;
		enddate=(Date)stream.readObject();;
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
	
		
		enddate=LTIDate.getRecentNYSETradingDay(LTIDate.getDate(rollingyear,1,31));

if(CurrentDate.equals(LTIDate.getRecentNYSETradingDay(LTIDate.getDate(rollingyear,12,1))))
{rollingyear++;
}


		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (!position&&LTIDate.isBeginning(TimeUnit.YEARLY,CurrentDate)&&!shortsellbigcap) {
		   printToLog("buy at the beginning of January");
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);

Asset CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset1");
CurrentAsset.setClassID(getAssetClassID(getSecurity (securitytolong).getName()));
CurrentAsset.setTargetPercentage(1.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy ("Asset1",securitytolong, TotalAmount, CurrentDate);

printToLog("buy small cap");
position=true;

		}
		else if (position&&CurrentDate.equals(enddate)&&!shortsellbigcap) {
		   double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);

Asset CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset1");
CurrentAsset.setClassID(getAssetClassID(getSecurity ("CASH").getName()));
CurrentAsset.setTargetPercentage(1.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy ("Asset1","CASH", TotalAmount, CurrentDate);
position=false;
		}
		else if (!position&&LTIDate.isBeginning(TimeUnit.YEARLY,CurrentDate)&&shortsellbigcap) {
		   printToLog("buy at the beginning of January");
double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);

Asset CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset1");
CurrentAsset.setClassID(getAssetClassID(getSecurity (securitytolong).getName()));
CurrentAsset.setTargetPercentage(1.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy ("Asset1",securitytolong, TotalAmount*(1.0), CurrentDate);

CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset2");
CurrentAsset.setClassID(getAssetClassID(getSecurity (securitytoshort).getName()));
CurrentAsset.setTargetPercentage(percentageofshort);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.shortSell("Asset2",securitytoshort, TotalAmount*percentageofshort, CurrentDate);
printToLog("buy small cap and shortsell big cap");

position=true;
		}
		else if (position&&CurrentDate.equals(enddate)&&shortsellbigcap) {
		   printToLog("sell at the end of January");

double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentPortfolio.sellAssetCollection(CurrentDate);

Asset CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset1");
CurrentAsset.setClassID(getAssetClassID(getSecurity ("CASH").getName()));
CurrentAsset.setTargetPercentage(1.0);
CurrentPortfolio.addAsset(CurrentAsset);
CurrentPortfolio.buy ("Asset1","CASH", TotalAmount, CurrentDate);
position=false;
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