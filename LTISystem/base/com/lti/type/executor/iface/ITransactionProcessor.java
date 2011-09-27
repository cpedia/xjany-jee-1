package com.lti.type.executor.iface;

import java.util.Date;

public interface ITransactionProcessor {
	public void addTransaction(long portfolioID, Long securityID, long strategyID, String symbol, String assetname, Date date, String operation, double amount, double share, double percentage, int transactionType);
	public void addLog(long portfolioID,Date date,String message);
}
 