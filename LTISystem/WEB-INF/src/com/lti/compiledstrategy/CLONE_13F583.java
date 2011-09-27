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
public class CLONE_13F583 extends SimulateStrategy{
	public CLONE_13F583(){
		super();
		StrategyID=583L;
		StrategyClassID=3L;
	}
	//----------------------------------------------------
	//parameters
	//----------------------------------------------------
	private String name;
	public void setName(String name){
		this.name=name;
	}
	
	public String getName(){
		return this.name;
	}
	@SuppressWarnings("deprecation")
	public void fetchParameters()throws ParameterException{
		Map<String,String> parameters=SimulateParameters;
		//curAsset=parameters.get("curAsset");
		name=(String)ParameterUtil.fetchParameter("String","name", "spy", parameters);
	}
	//----------------------------------------------------
	//variables
	//----------------------------------------------------
	Date lastUpdateDate=null;
Date nextUpdateDate=null;
	public String getVariables(){
		StringBuffer sb=new StringBuffer();
		sb.append("lastUpdateDate: ");
		sb.append(lastUpdateDate);
		sb.append("\n");
		sb.append("nextUpdateDate: ");
		sb.append(nextUpdateDate);
		sb.append("\n");
		return sb.toString();
	}
	
	public void writeObject(ObjectOutputStream stream) throws IOException{
		stream.writeObject(lastUpdateDate);
		stream.writeObject(nextUpdateDate);
	}
	
	public void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException{
		lastUpdateDate=(Date)stream.readObject();;
		nextUpdateDate=(Date)stream.readObject();;
	}
	
	//----------------------------------------------------
	//user defined functions
	//----------------------------------------------------	
		// if it's true, the strategy will read the ofx information by report
	// date, or by quarter end
	boolean useFiledDate = true;

	// name of the asset holding positions
	String ASSET = "CLONE_13F";

	// handle precision problems
	double margin_error = 10;

	// the rate to extend the total amount
	double extendRate = 1.02;

	// debug variables
	// output debugging informations to log
	boolean isDebugging = true;
	// if you want to read all positions from 13f, please set it to zero, only
	// for debugging
	int maxSize = 100;
	boolean ignoreVFSymbol = true;
	boolean ignoreNotInDatabaseSymbol = true;


	public Map<String, Double> getNewPercentage(List<HoldingItem> newPositions)  throws Exception {
		double newTotalAmount = 0.0;
		double newCash = 0.0;

		int size = (maxSize == 0 ? newPositions.size() : maxSize);

		StringBuffer sb = new StringBuffer();
		if (isDebugging) {
			sb.append("13F information:\n");
			sb.append("SYMBOL");
			sb.append("\t");
			sb.append("PRICE");
			sb.append("\t");
			sb.append("SHARES");
			sb.append("\n");
		}
		int size_c = 0;
		for (int i = 0; i < newPositions.size(); i++) {
			if (size_c >= size)
				break;
			HoldingItem curPosition = newPositions.get(i);
			curPosition.setTotalAmount(curPosition.getPrice() * curPosition.getShares());
			boolean validate = true;
			if (ignoreVFSymbol) {
				if (curPosition.getSymbol().toLowerCase().startsWith("vf_"))
					validate = false;
			}
			if (ignoreNotInDatabaseSymbol) {
				try {
					Security s = getSecurity(curPosition.getSymbol());
					s.getClose(CurrentDate);
				} catch (Exception e) {
					validate = false;
				}
			}
			if (curPosition.getSymbol().toLowerCase().equals("cash") || !validate) {
				newCash += curPosition.getTotalAmount();
			} else {
				if (isDebugging) {
					sb.append(curPosition.getSymbol());
					sb.append("\t");
					sb.append(curPosition.getPrice());
					sb.append("\t");
					sb.append(curPosition.getShares());
					sb.append("\n");
				}
				size_c++;
				newTotalAmount += curPosition.getTotalAmount();
			}
		}
		if (isDebugging)
			printToLog(sb.toString());

		// extend the total amount, for precision problem
		newTotalAmount *= extendRate;
		Map<String, Double> newPercentage = new HashMap<String, Double>();
		size_c = 0;
		for (int i = 0; i < newPositions.size(); i++) {
			if (size_c >= size)
				break;
			HoldingItem curPosition = newPositions.get(i);
			boolean validate = true;
			if (ignoreVFSymbol) {
				if (curPosition.getSymbol().toLowerCase().startsWith("vf_"))
					validate = false;
			}
			if (ignoreNotInDatabaseSymbol) {
				try {
					Security s = getSecurity(curPosition.getSymbol());
					s.getClose(CurrentDate);
				} catch (Exception e) {
					validate = false;
				}
			}
			if (!curPosition.getSymbol().toLowerCase().equals("cash") && validate) {
                                size_c++;
				Double exists=newPercentage.get(curPosition.getSymbol());
                                Double np=curPosition.getTotalAmount() / newTotalAmount;
				if(exists==null){
					
					newPercentage.put(curPosition.getSymbol(), np);
				}else{
					newPercentage.put(curPosition.getSymbol(), np+exists);
				}
				
			}
		}
		// newPercentage.put("CASH", cash/totalamount);
		return newPercentage;
	}

	public Map<String, Double> getOldPercentage()   throws Exception {
		List<HoldingItem> oldPositions = new ArrayList<HoldingItem>();
		Set<HoldingItem> siset = CurrentPortfolio.getAsset(ASSET).getHoldingItems();
		if (siset != null) {
			oldPositions.addAll(siset);
		}
		double oldTotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);
		double oldCash = CurrentPortfolio.getCash();
		Map<String, Double> oldPercentage = new HashMap<String, Double>();
		for (int i = 0; i < oldPositions.size(); i++) {
			HoldingItem curPosition = oldPositions.get(i);
			Security s = getSecurity(curPosition.getSecurityID());
			curPosition.setSymbol(s.getSymbol());
			curPosition.setTotalAmount(s.getClose(CurrentDate) * curPosition.getShares());
			if (!curPosition.getSymbol().toLowerCase().equals("cash")) {
				oldPercentage.put(curPosition.getSymbol(), curPosition.getTotalAmount() / oldTotalAmount);
			}
		}
		// oldPercentage.put("CASH", cash/totalamount);
		return oldPercentage;
	}
	
	public void rebalance(List<HoldingItem> newPositions)  throws Exception {
		if(isDebugging)printToLog("===============================================================");
		if(isDebugging)printToLog("Portfolio Structure before adjusting\n" + CurrentPortfolio.getInformation(CurrentDate));
		Map<String, Double> newPercentage = getNewPercentage(newPositions);
		Map<String, Double> oldPercentage = getOldPercentage();

		if (isDebugging)
			printToLog("Old percentages:", oldPercentage);
		if (isDebugging)
			printToLog("New percentages:", newPercentage);

		double oldTotalAmount = CurrentPortfolio.getTotalAmount(CurrentDate);

		// action1, sell the holdings which are not hold by current
		// quarter
		Iterator<String> iter1 = oldPercentage.keySet().iterator();
		while (iter1.hasNext()) {
			String symbol = iter1.next();
			if (newPercentage.get(symbol) == null || newPercentage.get(symbol).equals(0.0)) {
				CurrentPortfolio.sell(ASSET, symbol, oldTotalAmount * oldPercentage.get(symbol), CurrentDate);
                                 newPercentage.remove(symbol);
			}
		}

		// action2, sell the holdings which percentage decrease
		Iterator<String> iter2 = newPercentage.keySet().iterator();
		while (iter2.hasNext()) {
			String symbol = iter2.next();
			double newP = (newPercentage.get(symbol) == null ? 0.0 : newPercentage.get(symbol));
			double oldP = (oldPercentage.get(symbol) == null ? 0.0 : oldPercentage.get(symbol));
			if (oldPercentage.get(symbol) != null && newP < oldP) {
				double tradeamount = (oldP - newP) * oldTotalAmount;
				if (tradeamount < margin_error)
					continue;
				CurrentPortfolio.sell(ASSET, symbol, tradeamount, CurrentDate);
			}
		}

		// action3, buy the holdings which are not hold by previous
		// quarter
		Iterator<String> iter3 = newPercentage.keySet().iterator();
		while (iter3.hasNext()) {
			String symbol = iter3.next();
			double newP = (newPercentage.get(symbol) == null ? 0.0 : newPercentage.get(symbol));
			double oldP = (oldPercentage.get(symbol) == null ? 0.0 : oldPercentage.get(symbol));
			if (newP > oldP) {
				double tradeamount = oldTotalAmount * (newP - oldP);
				if (tradeamount < margin_error)
					continue;
				CurrentPortfolio.buy(ASSET, symbol, tradeamount, CurrentDate);
			}
		}
		if (isDebugging)
			printToLog("Portfolio Structure after adjusting\n" + CurrentPortfolio.getInformation(CurrentDate));
		if (isDebugging)
			printToLog("===============================================================");
	}


private void updateBuySell(Portfolio portfolio) {
		List<Transaction> trs = portfolio.getTransactions(null, null);
		Counter c = new Counter();
		if (trs.size() > 0) {
			Date d = trs.get(trs.size() - 1).getDate();
			c.add(trs.get(trs.size() - 1).getOperation());
			for (int i = trs.size() - 2; i >= 0; i--) {
				Transaction t = trs.get(i);
				if (t.getDate().before(d))
					break;
				c.add(t.getOperation());
			}
		}
		String output = portfolio.getOutput() == null ? "" : portfolio.getOutput();
		portfolio.setOutput(output + c.output());
	}

	
	private void drawPieChart(Portfolio portfolio) throws Exception {
		Map<String, Double> sectors;
		sectors = portfolio.getSectors();
		Iterator<String> keys = sectors.keySet().iterator();
		Double v = Double.MIN_VALUE;
		String k = null;
		
		while (keys.hasNext()) {
			String key = keys.next();
			Double d = sectors.get(key);
			if (v < d) {
				v = d;
				k = key;
			}
		}
		if (k != null) {
String output = portfolio.getOutput() == null ? "" : portfolio.getOutput();
			output += StringUtil.getOutput(com.lti.system.Configuration.SECTOR_TOP_SECTOR, k);
			output += StringUtil.getOutput(com.lti.system.Configuration.CLONE_CENTER_TOP_10, FormatUtil.formatPercentage(portfolio.getTop10())+"%");
			output += StringUtil.getOutput(com.lti.system.Configuration.CLONE_CENTER_TOTAL_POSITION, portfolio.getTotalPosition()+"");
			
			portfolio.setOutput(output);
		}
		name = portfolio.getName();
		String root=com.lti.system.ContextHolder.getServletPath();
		root=root.substring(0,root.indexOf("LTISystem")+9);
		java.io.File f=new java.io.File(root,com.lti.system.ContextHolder.getImagePath() + "clonecenter/");
		if(!f.exists()){
			f.mkdirs();
		}
		ChartUtil.PlotPie(name, sectors, f.getAbsolutePath()+"/" + StringUtil.getValidName(name) + ".jpg");


	}

	private void setTopSector(Portfolio portfolio) {

	}

	private void afterExecution(Portfolio portfolio) throws Exception{
		updateBuySell(portfolio);
		drawPieChart(portfolio);
		setTopSector(portfolio);
	}

	@Override
	public void afterExecuting(Portfolio portfolio, Date CurrentDate)     throws Exception {
		super.afterExecuting(portfolio, CurrentDate);
		afterExecution(portfolio);
	}

	@Override
	public void afterUpdating(Portfolio portfolio, Date CurrentDate)     throws Exception {
		super.afterUpdating(portfolio, CurrentDate);
		afterExecution(portfolio);
	}

	//----------------------------------------------------
	//initialize code
	//----------------------------------------------------
	@SuppressWarnings({ "deprecation", "unchecked" })
	public void init() throws Exception{
					Asset asset = new Asset();
			asset.setName(ASSET);
			asset.setTargetPercentage(1.0);
			asset.setAssetStrategyID(0L);
			CurrentPortfolio.addAsset(asset);
			com.lti.service.CompanyIndexManager cim = com.lti.system.ContextHolder.getCompanyIndexManager();
			List<CompanyIndex> cis = cim.get(name, CompanyIndex.Type_13F_HR);
			com.lti.edgar.EdgarUtil.convertToOFX(name, cis, false);

			
			List<HoldingItem> newPositions = OFXUtil.getHoldingItems(name, CurrentDate, lastUpdateDate,useFiledDate);

			if(isDebugging)printToLog("Read vfofx file");

			if (newPositions != null && newPositions.size() > 0 ){

				rebalance(newPositions);
                                lastUpdateDate=CurrentDate;
                               nextUpdateDate=OFXUtil.getNextUpdateDate(name, CurrentDate,  useFiledDate);
                               if(isDebugging)printToLog("Next update date: "+nextUpdateDate);

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
	
		
		List<HoldingItem> newPositions =null;

if(nextUpdateDate==null||(nextUpdateDate!=null&&CurrentDate.getTime()>=nextUpdateDate.getTime())){
        if(nextUpdateDate==null&&RunningMode==RUNNING_MODE_UPDATE){
                          com.lti.service.CompanyIndexManager cim =  com.lti.system.ContextHolder.getCompanyIndexManager();
			List<CompanyIndex> cis = cim.get(name, CompanyIndex.Type_13F_HR);
			com.lti.edgar.EdgarUtil.convertToOFX(name, cis, false);
                      if(isDebugging) printToLog("Update vfofx file.");
        }
        newPositions = OFXUtil.getHoldingItems(name, CurrentDate, lastUpdateDate, useFiledDate);
        if(isDebugging)printToLog("Read vfofx file");
}


		
		
		
		if(new Boolean(true).equals(false)){
			
		}
		
		 
		else if (ok[poki[) {
		   hjgkjhg
		}
		else if (kjhgk) {
		   kgjk
		}
		
		
		else{
		
			jgk;
		}
		
	}
	
	public double getVersion(){
		return version;
	}
	
}

///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/CLONE_13F583.java:409: illegal start of expression
//		else if (ok[poki[) {
//		                 ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/CLONE_13F583.java:410: not a statement
//		   hjgkjhg
//		   ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/CLONE_13F583.java:410: ';' expected
//		   hjgkjhg
//		          ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/CLONE_13F583.java:413: not a statement
//		   kgjk
//		   ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/CLONE_13F583.java:413: ';' expected
//		   kgjk
//		       ^
///usr/apache-tomcat-6.0.16/webapps/LTISystem/WEB-INF/src/com/lti/compiledstrategy/CLONE_13F583.java:419: not a statement
//			jgk;
//			^
//6 errors