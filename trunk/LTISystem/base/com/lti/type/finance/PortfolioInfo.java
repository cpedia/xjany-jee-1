/**
 * 
 */
package com.lti.type.finance;

import java.io.Serializable;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.CacheStrategyItem;
import com.lti.service.bo.GlobalObject;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.HoldingRecord;
import com.lti.service.bo.Log;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.PortfolioState;
import com.lti.service.bo.Security;
import com.lti.service.bo.Transaction;

/**
 * @author CCD
 *
 */
public class PortfolioInfo implements Serializable{

	private static final long serialVersionUID = -6902325762005752124L;

	private HoldingInf simulateHolding;
	
	private HoldingInf simulateDelayHolding;
	
	private HoldingInf simulateExpectedHolding;
	
	private List<PortfolioDailyData> simulateDailyDatas;
	
	private List<Transaction> simulateTransactions;
	
	private List<HoldingItem> simulateHoldingItems;
	
	private List<HoldingRecord> simulateHoldingRecords;
	
	private List<PortfolioMPT> simulatePortfolioMPTs;
	
	private Portfolio portfolio;
	
	private Security security;
	
	private List<Log> simulateLogs;
	
	private PortfolioState portfolioState;
	
	private PortfolioInf currentPI;
	
	private PortfolioInf delayPI;
	
	private PortfolioInf expectedPI;
	
	private CachePortfolioItem delayCPI;
	
	private CachePortfolioItem realCPI;
	
	private List<CachePortfolioItem> CPIs;
	
	private List<CacheStrategyItem> CSIs;
	
	private List<GlobalObject> globalObjects;
	
	private boolean deleteExpectedHolding = false;
	
	private int mode;
	
	private int firstIndex;
	
	private boolean isCustomized = false;
	
	public PortfolioInfo(){
		
	}
	
	public HoldingInf getSimulateHolding() {
		return simulateHolding;
	}

	public void setSimulateHolding(HoldingInf simulateHolding) {
		this.simulateHolding = simulateHolding;
	}

	public HoldingInf getSimulateDelayHolding() {
		return simulateDelayHolding;
	}

	public void setSimulateDelayHolding(HoldingInf simulateDelayHolding) {
		this.simulateDelayHolding = simulateDelayHolding;
	}

	public List<PortfolioDailyData> getSimulateDailyDatas() {
		return simulateDailyDatas;
	}

	public void setSimulateDailyDatas(List<PortfolioDailyData> simulateDailyDatas) {
		this.simulateDailyDatas = simulateDailyDatas;
	}

	public List<Transaction> getSimulateTransactions() {
		return simulateTransactions;
	}

	public void setSimulateTransactions(List<Transaction> simulateTransactions) {
		this.simulateTransactions = simulateTransactions;
	}

	public List<HoldingItem> getSimulateHoldingItems() {
		return simulateHoldingItems;
	}

	public void setSimulateHoldingItems(List<HoldingItem> simulateHoldingItems) {
		this.simulateHoldingItems = simulateHoldingItems;
	}

	public List<HoldingRecord> getSimulateHoldingRecords() {
		return simulateHoldingRecords;
	}

	public void setSimulateHoldingRecords(List<HoldingRecord> simulateHoldingRecords) {
		this.simulateHoldingRecords = simulateHoldingRecords;
	}

	public List<PortfolioMPT> getSimulatePortfolioMPTs() {
		return simulatePortfolioMPTs;
	}

	public void setSimulatePortfolioMPTs(List<PortfolioMPT> simulatePortfolioMPTs) {
		this.simulatePortfolioMPTs = simulatePortfolioMPTs;
	}

	public Portfolio getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.portfolio = portfolio;
	}

	public Security getSecurity() {
		return security;
	}

	public void setSecurity(Security security) {
		this.security = security;
	}

	public List<Log> getSimulateLogs() {
		return simulateLogs;
	}

	public void setSimulateLogs(List<Log> simulateLogs) {
		this.simulateLogs = simulateLogs;
	}

	public PortfolioState getPortfolioState() {
		return portfolioState;
	}

	public void setPortfolioState(PortfolioState portfolioState) {
		this.portfolioState = portfolioState;
	}

	public PortfolioInf getCurrentPI() {
		return currentPI;
	}

	public void setCurrentPI(PortfolioInf currentPI) {
		this.currentPI = currentPI;
	}

	public PortfolioInf getDelayPI() {
		return delayPI;
	}

	public void setDelayPI(PortfolioInf delayPI) {
		this.delayPI = delayPI;
	}

	public PortfolioInf getExpectedPI() {
		return expectedPI;
	}

	public void setExpectedPI(PortfolioInf expectedPI) {
		this.expectedPI = expectedPI;
	}

	public CachePortfolioItem getDelayCPI() {
		return delayCPI;
	}

	public void setDelayCPI(CachePortfolioItem delayCPI) {
		this.delayCPI = delayCPI;
	}

	public CachePortfolioItem getRealCPI() {
		return realCPI;
	}

	public void setRealCPI(CachePortfolioItem realCPI) {
		this.realCPI = realCPI;
	}

	public List<CachePortfolioItem> getCPIs() {
		return CPIs;
	}

	public void setCPIs(List<CachePortfolioItem> is) {
		CPIs = is;
	}

	public List<CacheStrategyItem> getCSIs() {
		return CSIs;
	}

	public void setCSIs(List<CacheStrategyItem> is) {
		CSIs = is;
	}

	public List<GlobalObject> getGlobalObjects() {
		return globalObjects;
	}

	public void setGlobalObjects(List<GlobalObject> globalObjects) {
		this.globalObjects = globalObjects;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public int getFirstIndex() {
		return firstIndex;
	}

	public void setFirstIndex(int firstIndex) {
		this.firstIndex = firstIndex;
	}

	public HoldingInf getSimulateExpectedHolding() {
		return simulateExpectedHolding;
	}

	public void setSimulateExpectedHolding(HoldingInf simulateExpectedHolding) {
		this.simulateExpectedHolding = simulateExpectedHolding;
	}

	public boolean isDeleteExpectedHolding() {
		return deleteExpectedHolding;
	}

	public void setDeleteExpectedHolding(boolean deleteExpectedHolding) {
		this.deleteExpectedHolding = deleteExpectedHolding;
	}

	public boolean isCustomized() {
		return isCustomized;
	}

	public void setCustomized(boolean isCustomized) {
		this.isCustomized = isCustomized;
	}
	
}
