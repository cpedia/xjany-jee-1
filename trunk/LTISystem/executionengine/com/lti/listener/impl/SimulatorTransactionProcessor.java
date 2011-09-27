package com.lti.listener.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.util.LTIDate;
import com.lti.service.bo.Transaction;
import com.lti.type.executor.iface.ITransactionProcessor;
import com.lti.system.Configuration;

public class SimulatorTransactionProcessor implements ITransactionProcessor, Serializable{

	private static final long serialVersionUID = 6648805669479395057L;
	List<Transaction> transactions;
	List<Transaction> scheduleTransactions;
	public SimulatorTransactionProcessor(){
		transactions=new ArrayList<Transaction>();
		scheduleTransactions = new ArrayList<Transaction>();
	}
	
	@Override
	public void addLog(long portfolioID, Date date, String message) {
		// TODO Auto-generated method stub
		
	}
	
	private Transaction createTransaction(long portfolioID, Long securityID, long strategyID, String symbol, String assetname, Date date, String operation, double amount, double share, double percentage, int transactionType) {
		Transaction tr=new Transaction();
		tr.setPortfolioID(portfolioID);
		tr.setDate(date);
		tr.setSecurityID(securityID);
		tr.setSymbol(symbol);
		tr.setAssetName(assetname);
		tr.setStrategyID(strategyID);
		tr.setOperation(operation);
		tr.setShare(share);
		tr.setAmount(amount);
		tr.setPercentage(percentage);
		tr.setTransactionType(transactionType);
		return tr;
	}
	
	@Override
	public void addTransaction(long portfolioID, Long securityID, long strategyID, String symbol, String assetname, Date date, String operation, double amount, double share, double percentage, int transactionType) {
		Transaction tr = this.createTransaction(portfolioID, securityID, strategyID, symbol, assetname, date, operation, amount, share, percentage, transactionType);
		if(transactionType == Configuration.TRANSACTION_TYPE_SCHEDULE)
			scheduleTransactions.add(tr);
		else
			transactions.add(tr);
	}
	
	private double get(double amount){
		if(!Double.isNaN(amount)){
			return amount;
		}
		return 0.0;
	}

	public void clear(Date date){
		transactions=new ArrayList<Transaction>();
		for(int i=0;i<scheduleTransactions.size();++i){
			Transaction t = scheduleTransactions.get(i);
			if(LTIDate.equals(t.getDate(), date)){
				scheduleTransactions = scheduleTransactions.subList(i, scheduleTransactions.size());
				return;
			}
		}
		scheduleTransactions = new ArrayList<Transaction>();
	}
	
	public void clearAll(){
		transactions=new ArrayList<Transaction>();
		scheduleTransactions = new ArrayList<Transaction>();
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

	public List<Transaction> getScheduleTransactions() {
		return scheduleTransactions;
	}

	public void setScheduleTransactions(List<Transaction> scheduleTransactions) {
		this.scheduleTransactions = scheduleTransactions;
	}

	

}
