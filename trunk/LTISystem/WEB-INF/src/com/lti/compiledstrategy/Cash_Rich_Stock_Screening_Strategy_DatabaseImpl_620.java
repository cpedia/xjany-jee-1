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
public class Cash_Rich_Stock_Screening_Strategy_DatabaseImpl_620 extends SimulateStrategy{
	public Cash_Rich_Stock_Screening_Strategy_DatabaseImpl_620(){
		super();
		StrategyID=620L;
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
	String[] sector = { "TECHNOLOGY", "BASIC MATERIALS", "CONGLOMERATES", "CONSUMER GOODS", "HEALTHCARE", "INDUSTRIAL GOODS", "SERVICES" };
List<String>stocks;
List<String> industries;
Map<String, String> company = new HashMap<String, String>();
Date lastQuarterDate=null;
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
	private Map<String,Double[]> means=new HashMap<String, Double[]>();

public Double[] getMean(String industry, int year, int quarter) throws Exception {
String key=industry+year+quarter;
Double[] value=means.get(key);
if(value!=null)return value;	
Map<String, Double[]> records = financialStatement.getLiabToAsset(year, quarter);
for (int p = 0; p < industries.size(); p++) {
double result1=0.0;
double result2=0.0;
String ind=industries.get(p);
List<String> companies = financialStatement.getIndustryIndex(ind);
			
double total1 = 0.0;
double total2 = 0.0;
int count = 0;
for (int i = 0; i < companies.size(); i++) {
String symbol = companies.get(i);
Double[] v = records.get(symbol);
if (v != null) {
total1 += v[0];
total2 +=v[1];
count++;
}
}
if (count != 0){
result1 = total1 / count;
result2 = total2 / count;
}				
means.put(ind+year+quarter, new Double[]{result1,result2});
}	
value=means.get(key);
return value;
}

private Map<String, Map<String,Double>> netIncs = new HashMap<String, Map<String,Double>>();

	public double getNetInc(String symbol, Date start, Date end)   throws Exception {
		String key = ""+LTIDate.getYear(end) + LTIDate.getQuarter(end);
		Map<String,Double> netInc=netIncs.get(key);
		if(netInc!=null){
			 Double d= netInc.get(symbol);
			 if(d!=null)return d;
			 else return 0.0;
		}
		Map<String, Double> records = financialStatement.getNetIncome(start, end);
		if(records!=null){
			netIncs.put(key, records);
			Double d= records.get(symbol);
			 if(d!=null)return d;
		}
		return 0.0;
	}

private Map<String,Map<String,Double>>annualNetIncs=new HashMap<String, Map<String,Double>>();

public double getAnnualNetInc(String symbol,int year)  throws Exception {
String key=""+year;
Map<String,Double>annualNetInc=annualNetIncs.get(key);
if(annualNetInc==null){
	annualNetInc=financialStatement.getAnnualNetIncome(year);
	if(annualNetInc!=null)annualNetIncs.put(key, annualNetInc);
}
if(annualNetInc!=null){
	Double d=annualNetInc.get(symbol);
	return d==null?0.0:d;
}
return 0.0;

}

private Map<String,Double> currentPrices=null;
public double getPrice(String symbol,Date date)  throws Exception {
Double result=0.0;
if(currentPrices==null){
	currentPrices=financialStatement.getPrice(date);
}
result=currentPrices.get(symbol);
return result==null?0.0:result;
}


private Map<String, Double> lastQuarterPrices = null;

	public double getLastQuarterPrice(String symbol)   throws Exception {
		Double result = 0.0;
		if (lastQuarterPrices == null) {
			lastQuarterPrices = financialStatement.getPrice(lastQuarterDate);
		}
		result = lastQuarterPrices.get(symbol);
		return result == null ? 0.0 : result;
	}


	private Map<String, Map<String, Double[]>> cashNShares = new HashMap<String, Map<String, Double[]>>();

	public Double[] getCashNShare(String symbol, int year, int quarter)  throws Exception {
		String key = ""+year + quarter;
		Map<String, Double[]> cashNShare = cashNShares.get(key);
		if(cashNShare==null){
			cashNShare= financialStatement.getBalanceCash(year, quarter);
			if(cashNShare!=null){
				cashNShares.put(key, cashNShare);
			}
		}
		if(cashNShare!=null){
			Double[] d=cashNShare.get(symbol);
			return d;
		}
		return null;
		
	}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		stocks = getStockBySector(sector);
industries = getIndustryBySector(sector);
company=financialStatement.getIndustryIndex();
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
	
		
		//this is very important

currentPrices=null;
//==============
Date d=LTIDate.getLastNYSETradingDayOfQuarter(LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -1));

if(!d.equals(lastQuarterDate)){
    lastQuarterPrices=null;
}
lastQuarterDate=d;

		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (LTIDate.isLastNYSETradingDayOfMonth(CurrentDate)) {
		   CurrentPortfolio.sellAssetCollection(CurrentDate);

Boolean EPSFlg1;// last 12 months eps
Boolean EPSFlg2;// last fiscal year eps
Boolean MarketCapFlg;
Boolean PriceFlg;
Boolean TLiabToTAssetFlg;
Boolean LTDebtToTCapFlg;
Boolean CashToPriceFlg;
Boolean CashPerShareFlg;
Boolean NetCashToPriceFlg;
Boolean NetCashPerShareFlg;

int EPS1Count=0;
int EPS2Count=0;
int MarketCapCount=0;
int PriceCount=0;
int LiabAssetCount=0;
int DebtCapCount=0;
int CashPriceCount=0;
int CashShareCount=0;
int NetCashPriceCount=0;
int NetCashShareCount=0;

List<Security> selected = new ArrayList<Security>();

Date lastQDate = LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -1);
int quarter=LTIDate.getQuarter(CurrentDate);
int lastQ = LTIDate.getQuarter(lastQDate);
int year = LTIDate.getYear(lastQDate);

System.out.println("Date is:" + CurrentDate);

for (int i = 0; i < stocks.size(); i++) {
EPSFlg1 = false;
EPSFlg2 = false;
MarketCapFlg = false;
PriceFlg = false;
TLiabToTAssetFlg = false;
LTDebtToTCapFlg = false;
CashToPriceFlg = false;
CashPerShareFlg = false;
NetCashToPriceFlg = false;
NetCashPerShareFlg = false;

// check whether the eps is positive.
Double netInc1 = getNetInc(stocks.get(i),LTIDate.getNewTradingDate(CurrentDate, TimeUnit.YEARLY, -1),CurrentDate);
System.out.println("last 12 months is:"+LTIDate.getNewTradingDate(CurrentDate, TimeUnit.YEARLY, -1));
if (netInc1 > 0)EPSFlg1 = true;
if (!EPSFlg1)continue;
EPS1Count++;

Double netInc2=getAnnualNetInc(stocks.get(i),year-1);
if (netInc2> 0)EPSFlg2 = true;
if (!EPSFlg2)continue;
EPS2Count++;

// check the total liabilities to total assets ratio
Double LiabRatio=financialStatement.getTotLiabToTotAsset(stocks.get(i), year, lastQ);
if (LiabRatio != null) {
Double meanLiabRatio = getMean(company.get(stocks.get(i)), year, lastQ)[0];
if (LiabRatio < meanLiabRatio)
TLiabToTAssetFlg = true;
}
if (!TLiabToTAssetFlg)continue;
LiabAssetCount++;

// check the long-term debt to total capital ratio
if (financialStatement.getLongTermDebtToTotCap(stocks.get(i), year, lastQ) != null) {
Double LongTermDebtRatio = financialStatement.getLongTermDebtToTotCap(stocks.get(i), year, lastQ);
Double meanLongTermDebtRatio = getMean(company.get(stocks.get(i)), year, lastQ)[1];
if (LongTermDebtRatio < meanLongTermDebtRatio)
LTDebtToTCapFlg = true;
}
if (!LTDebtToTCapFlg)continue;
DebtCapCount++;

// check whether the MarketCap is bigger than 50M
IncomeStatement is=financialStatement.getIncomeStatement(stocks.get(i), year, lastQ);
if ( is!= null) {
	Double MarketCap = getLastQuarterPrice(stocks.get(i))*is.getShares();
	if (MarketCap > 50000000)
		MarketCapFlg = true;
}
if (!MarketCapFlg)continue;
MarketCapCount++;


// check whether the stock current price is higher than $5
double price=getPrice(stocks.get(i),CurrentDate);
if (price> 5.0)
PriceFlg = true;
if (!PriceFlg)continue;
PriceCount++;

// check the cash to price ratio
Double[] cashs=getCashNShare(stocks.get(i),year,quarter);
Double cash=0.0;
if(cashs!=null&&cashs.length>0){
	cash=cashs[0];;
}
if (cash> 20 * price)CashToPriceFlg = true;
if (!CashToPriceFlg)continue;
CashPriceCount++;

// check the cash per shares to price ratio
Double shares=getCashNShare(stocks.get(i),year,quarter)[1];
if ((cash / shares) >= 0.2 * price)CashPerShareFlg = true;
if (!CashPerShareFlg)continue;
CashShareCount++;

// check the net cash to price ratio
Double netCash =getCashNShare(stocks.get(i),year,quarter)[2];
if (netCash > 20 * price)NetCashToPriceFlg = true;
if (!NetCashToPriceFlg)continue;
NetCashPriceCount++;

// check the net cash per shares to price ratio
if ((netCash / shares) >= 0.2 * price)NetCashPerShareFlg = true;
if (!NetCashPerShareFlg)continue;
NetCashShareCount++;

if (EPSFlg1 && EPSFlg2 && MarketCapFlg && PriceFlg && TLiabToTAssetFlg && LTDebtToTCapFlg && CashToPriceFlg && CashPerShareFlg && NetCashToPriceFlg && NetCashPerShareFlg)selected.add(getSecurity(stocks.get(i)));
}

printToLog("EPS1Count:"+EPS1Count);
printToLog("EPS2Count:"+EPS2Count);
printToLog("LiabAssetCount:"+LiabAssetCount);
printToLog("DebtCapCount:"+DebtCapCount);
printToLog("MarketCapCount:"+MarketCapCount);
printToLog("PriceCount:"+PriceCount);
printToLog("CashPriceCount:"+CashPriceCount);
printToLog("CashShareCount:"+CashShareCount);
printToLog("NetCashPriceCount:"+NetCashPriceCount);
printToLog("NetCashShareCount:"+NetCashShareCount);

if (selected.size() > 0) {
Asset CurrentAsset = new Asset();
CurrentAsset.setAssetStrategyID(getStrategyID("STATIC"));
CurrentAsset.setName("Asset");
CurrentAsset.setTargetPercentage(1.0 / selected.size());
CurrentPortfolio.addAsset(CurrentAsset);
Double TotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
for (int i = 0; i < selected.size(); i++) {
CurrentPortfolio.buy("Asset", selected.get(i).getSymbol(), TotalAmount * (1.0 / selected.size()), CurrentDate);
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