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
public class CANSLIM26653 extends SimulateStrategy{
	public CANSLIM26653(){
		super();
		StrategyID=653L;
		StrategyClassID=3L;
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
//List<Security> seList=new ArrayList<Security>();
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
		stocks=getAllStocks();
//seList = getSecuritiesByType(5);
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

Boolean EPSGrowth1Flg;
Boolean EPSGrowth2Flg;
Boolean EPSGrowth3Flg;
Boolean EPSGrowth4Flg;
Boolean EPSFlg;
Boolean priceFlg;
Boolean floatFlg;
Boolean RSFlg;
Boolean HolderFlg;

Boolean EPSMYFlg;
Boolean EPS12YFlg;
Boolean EPS23YFlg;
Boolean EPS34YFlg;
Boolean EPS45YFlg;
Boolean RSNFlg;
Boolean RS70Flg;

int EPSCount1=0;
int EPSCount2=0;
int EPSCount3=0;
int EPSCount4=0;
int EPSCount5=0;
int priceCount=0;
int floatCount=0;
int RSCount=0;
int HolderCount=0;

List<String>selected=new ArrayList<String>();
//List<String> stocks=new ArrayList<String>();

Date last1QDate = LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -1);
Date last2QDate= LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -2);
Date last3QDate=LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -3);
Date last4QDate=LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -4);
Date last5QDate=LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -5);
Date last6QDate=LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -6);
Date last1YDate=LTIDate.getNewDate(CurrentDate, TimeUnit.YEARLY, -1);
Date last2YDate=LTIDate.getNewDate(CurrentDate, TimeUnit.YEARLY, -2);
Date last3YDate=LTIDate.getNewDate(CurrentDate, TimeUnit.YEARLY, -3);
Date last4YDate=LTIDate.getNewDate(CurrentDate, TimeUnit.YEARLY, -4);
Date last5YDate=LTIDate.getNewDate(CurrentDate, TimeUnit.YEARLY, -5);

int year1Q=LTIDate.getYear(last1QDate);
int quarter1Q=LTIDate.getQuarter(last1QDate);
int year2Q=LTIDate.getYear(last2QDate);
int quarter2Q=LTIDate.getQuarter(last2QDate);
int year3Q=LTIDate.getYear(last3QDate);
int quarter3Q=LTIDate.getQuarter(last3QDate);
int year4Q=LTIDate.getYear(last4QDate);
int quarter4Q=LTIDate.getQuarter(last4QDate);
int year5Q=LTIDate.getYear(last5QDate);
int quarter5Q=LTIDate.getQuarter(last5QDate);
int year6Q=LTIDate.getYear(last6QDate);
int quarter6Q=LTIDate.getQuarter(last6QDate);
int year1Y=LTIDate.getYear(last1YDate);
int quarter1Y=LTIDate.getQuarter(last1YDate);
int year2Y=LTIDate.getYear(last2YDate);
int quarter2Y=LTIDate.getQuarter(last2YDate);
int year3Y=LTIDate.getYear(last3YDate);
int quarter3Y=LTIDate.getQuarter(last3YDate);
int year4Y=LTIDate.getYear(last4YDate);
int quarter4Y=LTIDate.getQuarter(last4YDate);
int year5Y=LTIDate.getYear(last5YDate);
int quarter5Y=LTIDate.getQuarter(last5YDate);

//for(int j=0;j<seList.size();j++)
//stocks.add(seList.get(i).getSymbol());

for(int i=0;i<stocks.size();i++){

String symbol=stocks.get(i);
EPSGrowth1Flg=false;
EPSGrowth2Flg=false;
EPSGrowth3Flg=false;
EPSGrowth4Flg=false;
EPSFlg=false;
priceFlg=false;
floatFlg=false;
RSFlg=false;
HolderFlg=false;

EPSMYFlg=false;
EPS12YFlg=false;
EPS23YFlg=false;
EPS34YFlg=false;
EPS45YFlg=false;
RSNFlg=false;
RS70Flg=false;

Double eps1Q=financialStatement.getEPS(symbol,year1Q,quarter1Q);
Double eps2Q=financialStatement.getEPS(symbol,year2Q,quarter2Q);
Double eps3Q=financialStatement.getEPS(symbol,year3Q,quarter3Q);
Double eps4Q=financialStatement.getEPS(symbol,year4Q,quarter4Q);
Double eps5Q=financialStatement.getEPS(symbol,year5Q,quarter5Q);
Double eps6Q=financialStatement.getEPS(symbol,year6Q,quarter6Q);
Double eps1Y=financialStatement.getEPS(symbol,year1Y);
Double eps2Y=financialStatement.getEPS(symbol,year2Y);
Double eps3Y=financialStatement.getEPS(symbol,year3Y);
Double eps4Y=financialStatement.getEPS(symbol,year4Y);
Double eps5Y=financialStatement.getEPS(symbol,year5Y);
Double netInc1Q=financialStatement.getNetIncome(symbol,year1Q,quarter1Q);
Double netInc2Q=financialStatement.getNetIncome(symbol,year2Q,quarter2Q);
Double netInc3Q=financialStatement.getNetIncome(symbol,year3Q,quarter3Q);
Double netInc4Q=financialStatement.getNetIncome(symbol,year4Q,quarter4Q);
Double share1Q=financialStatement.getShares(symbol,year1Q,quarter1Q);

//check whether eps growth increases in 5 years and 12 month
if(netInc1Q!=null&&netInc2Q!=null&&netInc3Q!=null&&netInc4Q!=null&&share1Q!=null&&share1Q!=0d&&eps1Y!=null&&eps2Y!=null&&eps3Y!=null&&eps4Y!=null&&eps5Y!=null){
Double eps12m=(netInc1Q+netInc2Q+netInc3Q+netInc4Q)/share1Q;
try{
if(eps12m>eps1Y) EPSMYFlg=true;
if(eps1Y>eps2Y) EPS12YFlg=true;
if(eps2Y>eps3Y) EPS23YFlg=true;
if(eps3Y>eps4Y) EPS34YFlg=true;
if(eps4Y>eps5Y) EPS45YFlg=true;
//printToLog(symbol+" eps12m:"+eps12m+" eps1Y:"+eps1Y+" eps2Y:"+eps2Y+" eps3Y:"+eps3Y+" eps4Y"+eps4Y+" eps5Y:"+eps5Y);
}catch(Exception e){}
if(eps12m>eps1Y&&eps1Y>eps2Y&&eps2Y>eps3Y&&eps3Y>eps4Y&&eps4Y>eps5Y)
EPSGrowth1Flg=true;
}

//printToLog(symbol+" eps12m>eps1:"+EPSMYFlg+" eps1Y>eps2Y:"+EPS12YFlg+" eps2Y>eps3Y:"+EPS23YFlg+" eps3Y>eps4Y:"+EPS34YFlg+" eps4Y>eps5Y:"+EPS45YFlg);
//printToLog(symbol+"'s check whether eps growth increases in 5 years and 12 month:"+EPSGrowth1Flg);
if(!EPSGrowth1Flg)continue;
EPSCount1++;

//check whether the YOY EPS Growth is bigger than20%
Double growth1=null;
if(eps1Q!=null&&eps5Q!=null&&eps5Q!=0d){
growth1=(eps1Q-eps5Q)/eps5Q;
//if(growth1>0.2)EPSGrowth2Flg=true;
}
//printToLog(symbol+"'s YOY eps growth:"+EPSGrowth2Flg);
//if(!EPSGrowth2Flg)continue;
//EPSCount2++;

//check whether the Q1-Q5 EPS Growth is bigger than the Q2-Q6 EPS Growth
Double growth2;
if(growth1!=null&&eps2Q!=null&&eps6Q!=null&&eps6Q!=0d){
growth2=(eps2Q-eps6Q)/eps6Q;
if(growth1>growth2)EPSGrowth3Flg=true;
}
//printToLog(symbol+"'s check whether the Q1-Q5 EPS Growth is bigger than the Q2-Q6 EPS Growth:"+EPSGrowth3Flg);
if(!EPSGrowth3Flg)continue;
EPSCount3++;

//check whether EPS is positive in the recent two quarters
if(eps1Q>0.0&&eps2Q>0.0)EPSFlg=true;
//printToLog(symbol+"'s check whether EPS is positive in the recent two quarters:"+EPSFlg);
if(!EPSFlg)continue;
EPSCount4++;

//check whether the 5 Year EPS Growth is bigger than 25%
Double growth4=financialStatement.get5YearEPS(symbol,year1Y);
if(growth4!=null&&growth4>0.25)EPSGrowth4Flg=true;
if(!EPSGrowth4Flg)continue;
//printToLog(symbol+"'s check whether the 5 Year EPS Growth is bigger than 25%:"+EPSGrowth4Flg);
EPSCount5++;

//check the institution holder
Double holder=financialStatement.getInstitutionHolder(symbol,year1Q,quarter1Q);
if(holder!=null&&holder>5.0)HolderFlg=true;
//printToLog(symbol+"'s check the institution holder:"+HolderFlg);
if(!HolderFlg)continue;
HolderCount++;

//check whether the price is within 10% of its 52-week high 
try{
double high=getSecurity(symbol).getHighestPrice(LTIDate.getNewNYSETradingDay(CurrentDate,-52),CurrentDate);
double price=financialStatement.getPrice(symbol,CurrentDate);
if(price>=0.9*high)priceFlg=true;
}catch(Exception e){}
//printToLog(symbol+"'s within 10% of its 52-week high :"+priceFlg);
if(!priceFlg)continue;
priceCount++;

//check the 52 week relative strength
Double rs=financialStatement.getRSGrade(symbol,CurrentDate);
try{
//printToLog(symbol+" RSGrade:"+rs);
if(rs!=null) RSNFlg=true;
if(rs>70.0) RS70Flg=true;
}catch(Exception e){
printToLog(e);
}
if(rs!=null&&rs>70.0)RSFlg=true;
//printToLog(symbol+"'s rs!=null"+RSNFlg+" rs>70.0"+ RS70Flg);
//printToLog(symbol+"'s check the 52 week relative strength:"+RSFlg);
if(!RSFlg)continue;
RSCount++;



if(EPSGrowth1Flg&&EPSFlg&&EPSGrowth3Flg&&EPSGrowth4Flg&&priceFlg&&RSFlg&&HolderFlg)
selected.add(stocks.get(i));
}

printToLog("EPSCount1:"+EPSCount1);
printToLog("EPSCount2:"+EPSCount2);
printToLog("EPSCount3:"+EPSCount3);
printToLog("EPSCount4:"+EPSCount4);
printToLog("EPSCount5:"+EPSCount5);
printToLog("priceCount:"+priceCount);
printToLog("floatCount:"+floatCount);
printToLog("RSCount:"+RSCount);
printToLog("HolderCount:"+HolderCount);

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
CurrentPortfolio.buy("Asset", selected.get(i), TotalAmount*(1.0/selected.size()), CurrentDate);
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