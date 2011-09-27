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
public class BetaGain_Ranking_Strategy635 extends SimulateStrategy{
	public BetaGain_Ranking_Strategy635(){
		super();
		StrategyID=635L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String assetclassname;
	public void setAssetclassname(String assetclassname){
		this.assetclassname=assetclassname;
	}
	
	public String getAssetclassname(){
		return this.assetclassname;
	}
	private int ranking;
	public void setRanking(int ranking){
		this.ranking=ranking;
	}
	
	public int getRanking(){
		return this.ranking;
	}
	private int interval;
	public void setInterval(int interval){
		this.interval=interval;
	}
	
	public int getInterval(){
		return this.interval;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		assetclassname=(String)ParameterUtil.fetchParameter("String","assetclassname", "Moderate Allocation", parameters);
		ranking=(Integer)ParameterUtil.fetchParameter("int","ranking", "5", parameters);
		interval=(Integer)ParameterUtil.fetchParameter("int","interval", "3", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
	

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		Date date = CurrentDate;
date = LTIDate.getFirstDateOfMonth(date);
date = LTIDate.clearHMSM(date);
Asset CurrentAsset=new Asset();
double TotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("BG_RANKING_"+ranking);
CurrentAsset.setClassID(getAssetClassID(assetclassname));
CurrentAsset.setTargetPercentage(1.0);
CurrentPortfolio.addAsset(CurrentAsset);

List<Long> securityIDList = portfolioManager.createStarPortfolio(assetclassname, ranking, interval, date);
int size = securityIDList.size();
double buyAmount = TotalAmount / size;
for(int i=0;i<size;++i)
{
     printToLog(securityIDList.get(i));
      try{
      CurrentPortfolio.buy(CurrentAsset.getName(), securityIDList.get(i), buyAmount,CurrentDate);
      }catch(Exception e){
            continue;
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
		
		 
		else if (LTIDate.isFirstNYSETradingDayofMonth(CurrentDate)) {
		   Date date = CurrentDate;
date = LTIDate.getFirstDateOfMonth(CurrentDate);
date = LTIDate.clearHMSM(date);
List<Asset> assetList=CurrentPortfolio.getCurrentAssetList();
Asset asset= assetList.get(0);
CurrentPortfolio.sellAsset(asset.getName(),CurrentDate);
double TotalAmount=CurrentPortfolio.getTotalAmount(CurrentDate);
List<Long> securityIDList = portfolioManager.createStarPortfolio(assetclassname, ranking, interval, date);
int size = securityIDList.size();
double buyAmount = TotalAmount / size;
Date nextMonthDate = LTIDate.getNextMonthFirstDay(CurrentDate);
nextMonthDate = LTIDate.getFirstNYSETradingDayOfMonth(nextMonthDate);
for(int i=0;i<size;++i)
{
      printToLog(securityIDList.get(i));
      try{
      Security se = securityManager.get(securityIDList.get(i));
     if(se.getEndDate().after(nextMonthDate))
               CurrentPortfolio.buy(asset.getName(), securityIDList.get(i), buyAmount,CurrentDate);
      }catch(Exception e){
            continue;
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