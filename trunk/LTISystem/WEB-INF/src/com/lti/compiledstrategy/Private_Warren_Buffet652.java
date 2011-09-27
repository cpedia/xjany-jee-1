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
public class Private_Warren_Buffet652 extends SimulateStrategy{
	public Private_Warren_Buffet652(){
		super();
		StrategyID=652L;
		StrategyClassID=9L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	List<String>stocks;
List<String> industries;
Map<String, String> company = new HashMap<String, String>();
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
	public Double getMeanLTDebtToEquity(String industry,int year,int quarter) throws Exception {
List<String>symbols = financialStatement.getIndustryIndex(industry);
Double mean=0d;
int count=0;
for(int i=0;i<symbols.size();i++){
Double debt=financialStatement.getLTDebt(symbols.get(i),year,quarter);
Double equity=financialStatement.getTotalEquity(symbols.get(i),year,quarter);
if(debt!=null&&equity!=null&&equity!=0.0){
mean+=debt/equity;
count+=1;
}
}
if(count>0)mean=mean/count;
return mean;
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		stocks=getAllStocks();

loadData();
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
		   CurrentPortfolio.sellAssetCollection(CurrentDate);

Boolean MarketCapFlg;
Boolean GrossOperInc0Flg;//last 12 months gross operating income
Boolean GrossOperInc1Flg;
Boolean ROE12mFlg;//ROE of the last 12 months
Boolean ROE1Flg;//ROE of the last fiscal year
Boolean DebtToEquityFlg;



List<Security> selected = new ArrayList<Security>();

Date lastQ1Date = LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -1);
Date lastQ2Date = LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -2);
Date lastQ3Date= LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -3);
Date lastQ4Date= LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -4);
Date lastQ5Date= LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -5);
Date lastY1Date = LTIDate.getNewDate(CurrentDate, TimeUnit.YEARLY, -1);
Date lastY2Date = LTIDate.getNewDate(CurrentDate, TimeUnit.YEARLY, -2);
Date lastY3Date = LTIDate.getNewDate(CurrentDate, TimeUnit.YEARLY, -3);
Date lastY4Date = LTIDate.getNewDate(CurrentDate, TimeUnit.YEARLY, -4);
Date lastY5Date = LTIDate.getNewDate(CurrentDate, TimeUnit.YEARLY, -5);
Date lastY6Date = LTIDate.getNewDate(CurrentDate, TimeUnit.YEARLY, -6);
Date lastY7Date = LTIDate.getNewDate(CurrentDate, TimeUnit.YEARLY, -7);
int quarter=LTIDate.getQuarter(CurrentDate);
int lastQ = LTIDate.getQuarter(lastQ1Date);
int year = LTIDate.getYear(lastQ1Date);

for (int i = 0; i < stocks.size(); i++) {

String symbol=stocks.get(i);
MarketCapFlg=false;
GrossOperInc0Flg=false;
GrossOperInc1Flg=false;
ROE12mFlg=false;
ROE1Flg=false;
DebtToEquityFlg=false;


// check whether the MarketCap is bigger than 1000M
Date lastQuarterEnd=LTIDate.getLastNYSETradingDayOfQuarter(LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY,-1));

Double marketCap=0d;
try{
marketCap = financialStatement.getPrice(symbol,lastQuarterEnd)* financialStatement.getShares(symbol,year,lastQ);
}
catch(Exception e){}
if(marketCap!=null&&marketCap> 1000000000)MarketCapFlg = true;
if (!MarketCapFlg)continue;

// check whether the gross operating income of the past 12 months is positive
Double total1=0d;
Double GrossOperInc1 = financialStatement.getOperatingIncome(symbol,year,lastQ);
Double GrossOperInc2 = financialStatement.getOperatingIncome(symbol,LTIDate.getYear(lastQ2Date),LTIDate.getQuarter(lastQ2Date));
Double GrossOperInc3 = financialStatement.getOperatingIncome(symbol,LTIDate.getYear(lastQ3Date),LTIDate.getQuarter(lastQ3Date));
Double GrossOperInc4 = financialStatement.getOperatingIncome(symbol,LTIDate.getYear(lastQ4Date),LTIDate.getQuarter(lastQ4Date));
if(GrossOperInc1!=null&&GrossOperInc2!=null&&GrossOperInc3!=null&&GrossOperInc4!=null) total1=GrossOperInc1+GrossOperInc2+GrossOperInc3+GrossOperInc4;
if(total1>0)GrossOperInc0Flg = true;
if (!GrossOperInc0Flg)continue;

// check whether the gross operating income of the last 1-7 fiscal year is positive
Double GrossOperIncY1=financialStatement.getOperatingIncome(symbol,LTIDate.getYear(lastY1Date));
if(GrossOperIncY1!=null&&GrossOperIncY1>0)GrossOperInc1Flg=true;
if(!GrossOperInc1Flg)continue;

//check whether the ROE of the last 12 months is larger than 15
Double total2=0d;
Double netIncQ1=financialStatement.getNetIncome(symbol,year,lastQ);
Double netIncQ2=financialStatement.getNetIncome(symbol,LTIDate.getYear(lastQ2Date),LTIDate.getQuarter(lastQ2Date));
Double netIncQ3=financialStatement.getNetIncome(symbol,LTIDate.getYear(lastQ3Date),LTIDate.getQuarter(lastQ2Date));
Double netIncQ4=financialStatement.getNetIncome(symbol,LTIDate.getYear(lastQ2Date),LTIDate.getQuarter(lastQ4Date));
Double totalEquityQ1=financialStatement.getTotalEquity(symbol,year, lastQ);
if(netIncQ1!=null&&netIncQ2!=null&&netIncQ3!=null&&netIncQ4!=null) total2=netIncQ1+netIncQ2+netIncQ3+netIncQ4;
if(totalEquityQ1!=null&&totalEquityQ1!=0.0){Double ROE=total2/totalEquityQ1;if(ROE>15)ROE12mFlg=true;}
if(!ROE12mFlg)continue;

//check whether the ROE for the last fiscal year is larger than 15
Double netIncY1=financialStatement.getNetIncome(symbol,LTIDate.getYear(lastY1Date));
Double totalEquityY1=financialStatement.getTotalEquity(symbol,LTIDate.getYear(lastY1Date));
if(netIncY1!=null&&totalEquityY1!=null&&totalEquityY1!=0.0){Double ROEY1=netIncY1/totalEquityY1;if(ROEY1>15)ROE1Flg=true;}
if(!ROE1Flg)continue;

// check the LT debt to equity ratio
Double mean1=getMeanLTDebtToEquity(getIndustry(symbol),year,lastQ);
Double LTdebt = financialStatement.getLTDebt(symbol, year, lastQ);
Double totalEquity=financialStatement.getTotalEquity(symbol,LTIDate.getYear(lastY1Date));
if(LTdebt!=null&&totalEquity!=null&&totalEquity!=0.0){
Double DTE=LTdebt/totalEquity;
if(DTE<mean1)DebtToEquityFlg = true;
}
if(!DebtToEquityFlg)continue;




if(MarketCapFlg && GrossOperInc0Flg && GrossOperInc1Flg&& ROE12mFlg &&ROE1Flg&& DebtToEquityFlg)selected.add(getSecurity(symbol));
}

if(selected.size()>0){
Asset CurrentAsset=new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset");
CurrentAsset.setTargetPercentage(1.0/selected.size());
CurrentPortfolio.addAsset(CurrentAsset);
Double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
System.out.println("current date is:"+CurrentDate+"TotalAmount is:"+TotalAmount);
System.out.println("current date is:"+CurrentDate+CurrentPortfolio.getInformation(CurrentDate)); 
for(int i=0;i<selected.size();i++){
System.out.println("current date is:"+CurrentDate+"selected stock is:"+selected.get(i));
printToLog("current date is:"+CurrentDate+"selected stock is:"+selected.get(i));
CurrentPortfolio.buy("Asset", selected.get(i).getSymbol(), TotalAmount*(1.0/selected.size()), CurrentDate);
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