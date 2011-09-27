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
public class Cash_Rich_Stock_Screening_Strategy_MemoryImpl_605 extends SimulateStrategy{
	public Cash_Rich_Stock_Screening_Strategy_MemoryImpl_605(){
		super();
		StrategyID=605L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private boolean userList;
	public void setUserList(boolean userList){
		this.userList=userList;
	}
	
	public boolean getUserList(){
		return this.userList;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		userList=(Boolean)ParameterUtil.fetchParameter("boolean","userList", "false", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	String[] sector = { "TECHNOLOGY", "BASIC MATERIALS", "CONGLOMERATES", "CONSUMER GOODS", "HEALTHCARE", "INDUSTRIAL GOODS", "SERVICES" };
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
	public Double getMeanLiabToAssets(String industry,int year,int quarter) throws Exception {
List<String>symbols = financialStatement.getIndustryIndex(industry);
Double mean=0d;
int count=0;
for(int i=0;i<symbols.size();i++){
Double liab=financialStatement.getTotalLiab(symbols.get(i),year,quarter);
Double assets=financialStatement.getTotalAssets(symbols.get(i),year,quarter);
if(liab!=null&&assets!=null&&assets!=0.0){
mean+=liab/assets;
count+=1;
}
}
if(count>0)mean=mean/count;
return mean;
}

public Double getMeanLTDebtToCap(String industry,int year,int quarter) throws Exception {
List<String>symbols = financialStatement.getIndustryIndex(industry);
Double mean=0d;
int count=0;
for(int i=0;i<symbols.size();i++){
try{
Double LTDebt=financialStatement.getLTDebt(symbols.get(i),year,quarter);
Double Cap=financialStatement.getLTDebt(symbols.get(i),year,quarter)+financialStatement.getPreferredStock(symbols.get(i),year,quarter)+financialStatement.getCommonStock(symbols.get(i),year,quarter);
if(LTDebt!=null&&Cap!=null&&Cap!=0.0){
mean+=LTDebt/Cap;
count+=1;
}
}catch(Exception e){}
}
if(count>0)mean=mean/count;
return mean;
}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
		stocks = getStockBySector(sector);
industries = getIndustryBySector(sector);
company=financialStatement.getIndustryIndex();

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

Date lastQ1Date = LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -1);
Date lastQ2Date = LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -2);
Date lastQ3Date= LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -3);
Date lastQ4Date= LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -4);
int quarter=LTIDate.getQuarter(CurrentDate);
int lastQ = LTIDate.getQuarter(lastQ1Date);
int year = LTIDate.getYear(lastQ1Date);

for (int i = 0; i < stocks.size(); i++) {
String symbol=stocks.get(i);

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
Double total1=0d;
Double netInc1 = financialStatement.getNetIncome(symbol,year,lastQ);
Double netInc2 = financialStatement.getNetIncome(symbol,LTIDate.getYear(lastQ2Date),LTIDate.getQuarter(lastQ2Date));
Double netInc3 = financialStatement.getNetIncome(symbol,LTIDate.getYear(lastQ3Date),LTIDate.getQuarter(lastQ3Date));
Double netInc4 = financialStatement.getNetIncome(symbol,LTIDate.getYear(lastQ4Date),LTIDate.getQuarter(lastQ4Date));
if(netInc1!=null&&netInc2!=null&&netInc3!=null&&netInc4!=null)total1=netInc1+netInc2+netInc3+netInc4;
if(total1>0)EPSFlg1 = true;
if (!EPSFlg1)continue;
EPS1Count++;

Double netInc5=financialStatement.getNetIncome(symbol,year);
if(netInc5!=null&&netInc5>0)EPSFlg2 = true;
if (!EPSFlg2)continue;
EPS2Count++;

// check the total liabilities to total assets ratio
Double mean1=getMeanLiabToAssets(getIndustry(symbol),year,lastQ);
Double liab = financialStatement.getTotalLiab(symbol,year,lastQ);
Double assets = financialStatement.getTotalAssets(symbol,year,lastQ);
if(liab!=null&&assets!=null&&assets!=0.0){
Double ratio=liab/assets;
if(ratio<mean1)TLiabToTAssetFlg = true;
}
if(!TLiabToTAssetFlg)continue;
LiabAssetCount++;

// check the long-term debt to total capital ratio
Double mean2=getMeanLTDebtToCap(getIndustry(symbol),year,lastQ);
try{
Double LTDebt=financialStatement.getLTDebt(symbol,year,lastQ);
Double Cap=financialStatement.getLTDebt(symbol,year,lastQ)+financialStatement.getPreferredStock(symbol,year,lastQ)+financialStatement.getCommonStock(symbol,year,lastQ);
if(LTDebt!=null&&Cap!=null&&Cap!=0.0){
Double ratio=LTDebt/Cap;
if(ratio<mean2)LTDebtToTCapFlg=true;
}
}catch(Exception e){}
if(!LTDebtToTCapFlg)continue;
DebtCapCount++;

// check whether the MarketCap is bigger than 50M
Date lastQuarterEnd = LTIDate.getLastNYSETradingDayOfQuarter(LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -1));
Double marketCap=0d;
try{
marketCap = financialStatement.getPrice(symbol,lastQuarterEnd)* financialStatement.getShares(symbol,year,lastQ);
}
catch(Exception e){}
if(marketCap!=null&&marketCap> 50000000)MarketCapFlg = true;
if (!MarketCapFlg)continue;
MarketCapCount++;

// check whether the stock current price is higher than $5
double price=0.0;
try{
price=financialStatement.getPrice(symbol,CurrentDate);
}catch(Exception e){}
if(price>5.0)PriceFlg = true;
if(!PriceFlg)continue;
PriceCount++;

// check the cash to price ratio
Double cash=financialStatement.getCashNEquiv(symbol,year,lastQ);
if(cash!=null&&cash> 20 * price)CashToPriceFlg = true;
if(!CashToPriceFlg)continue;
CashPriceCount++;

// check the cash per shares to price ratio
Double shares=financialStatement.getShares(symbol,year,lastQ);
if (shares!=null&&(cash / shares) >= 0.2 * price)CashPerShareFlg = true;
if (CashPerShareFlg == false)continue;
CashShareCount++;

// check the net cash to price ratio
Double netCash=cash-financialStatement.getTotalCurLiab(symbol,year,lastQ);
if(netCash!=null&&netCash > 20 * price)NetCashToPriceFlg = true;
if(!NetCashToPriceFlg)continue;
NetCashPriceCount++;

// check the net cash per shares to price ratio
if ((netCash / shares) >= 0.2 * price)NetCashPerShareFlg = true;
if(!NetCashPerShareFlg)continue;
NetCashShareCount++;

if (EPSFlg1&& EPSFlg2&&MarketCapFlg && PriceFlg && TLiabToTAssetFlg && LTDebtToTCapFlg && CashToPriceFlg && CashPerShareFlg && NetCashToPriceFlg && NetCashPerShareFlg)selected.add(getSecurity(symbol));
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