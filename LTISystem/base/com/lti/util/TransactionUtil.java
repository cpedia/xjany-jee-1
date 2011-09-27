package com.lti.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Transaction;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class TransactionUtil {

	public static List<Transaction> MergeTransactions(List<Transaction> trs) {
		if(trs==null)return null;
		List<Transaction> transactions = new ArrayList<Transaction>();
		
		for (int i = 0; i < trs.size(); i++) {
			Transaction t = trs.get(i).clone();
			boolean find = false;
			for (int j = 0; j < transactions.size(); j++) {
				Transaction transaction = transactions.get(j);
				String transactionOperation = transaction.getOperation();
				String tOperation = t.getOperation();
				if (t.getDate() != null && t.getDate().equals(transaction.getDate()) && t.getTransactionType().equals(transaction.getTransactionType())) {
					if (t.getSecurityID() == null) {
						boolean mergable = false;
						mergable = (tOperation.equals(Configuration.DEPOSIT) && transactionOperation.equals(Configuration.WITHDRAW)) || (tOperation.equals(Configuration.WITHDRAW) && transactionOperation.equals(Configuration.DEPOSIT));
						if (tOperation.equals(transactionOperation)) {
							if (tOperation.equals(Configuration.WITHDRAW)) {
								t.setAmount(t.getAmount() + transaction.getAmount());
								t.setPercentage(t.getPercentage()+transaction.getPercentage());
								transactions.remove(transaction);
								transactions.add(t);
								find = true;
								break;
							} else if (tOperation.equals(Configuration.DEPOSIT)) {
								transaction.setAmount(t.getAmount() + transaction.getAmount());
								transaction.setPercentage(t.getPercentage() + transaction.getPercentage());
								find = true;
								break;
							}
						}// if(tOperation.equals(transactionOperation))
						else if (mergable) {

							double amount = transaction.getAmount() - t.getAmount();
							double percentage = transaction.getPercentage() - t.getPercentage();
							if (amount > 0) {
								transaction.setAmount(amount);
								transaction.setPercentage(percentage);
							} else if (amount == 0.0) {
								transactions.remove(transaction);
							} else {
								t.setAmount(-amount);
								t.setPercentage(-percentage);
								transactions.remove(transaction);
								transactions.add(t);
							}
							find = true;
							break;
						}

					} else {
						if (t.getSecurityID().equals(transaction.getSecurityID()) && t.getAssetName().equals(transaction.getAssetName())) {
							boolean mergable = false;
							mergable = (tOperation.equals(Configuration.TRANSACTION_BUY) && transactionOperation.equals(Configuration.TRANSACTION_SELL)) || (tOperation.equals(Configuration.TRANSACTION_BUY_AT_OPEN) && transactionOperation.equals(Configuration.TRANSACTION_SELL_AT_OPEN)) || (tOperation.equals(Configuration.TRANSACTION_SELL) && transactionOperation.equals(Configuration.TRANSACTION_BUY)) || (tOperation.equals(Configuration.TRANSACTION_SELL_AT_OPEN) && transactionOperation.equals(Configuration.TRANSACTION_BUY_AT_OPEN))
							;

							if (tOperation.equals(transactionOperation)) {
								if (tOperation.equals(Configuration.TRANSACTION_BUY) || tOperation.equals(Configuration.TRANSACTION_BUY_AT_OPEN)) {
									t.setAmount(t.getAmount() + transaction.getAmount());
									t.setPercentage(t.getPercentage() + transaction.getPercentage());
									t.setShare(t.getShare() + transaction.getShare());
									transactions.remove(transaction);
									transactions.add(t);
									find = true;
									break;
								} else if (tOperation.equals(Configuration.TRANSACTION_SELL) || tOperation.equals(Configuration.TRANSACTION_SELL_AT_OPEN) || tOperation.equals(Configuration.TRANSACTION_SHORT_SELL) || tOperation.equals(Configuration.TRANSACTION_SHORT_SELL_AT_OPEN)) {
									transaction.setAmount(t.getAmount() + transaction.getAmount());
									transaction.setPercentage(t.getPercentage() + transaction.getPercentage());
									transaction.setShare(t.getShare() + transaction.getShare());
									find = true;
									break;
								}else if(tOperation.equals(Configuration.TRANSACTION_REINVEST)){
									transaction.setAmount(t.getAmount() + transaction.getAmount());
									transaction.setPercentage(t.getPercentage() + transaction.getPercentage());
									transaction.setShare(t.getShare() + transaction.getShare());
									find = true;
									break;
								}
							} else if (mergable) {
								double amount = transaction.getAmount() - t.getAmount();
								double percentage = transaction.getPercentage() - t.getPercentage();
								double share = transaction.getShare() - t.getShare();
								if (amount > 0) {
									transaction.setAmount(amount);
									transaction.setPercentage(percentage);
									transaction.setShare(share);
								} else if (amount == 0.0) {
									transactions.remove(transaction);
								} else {
									t.setAmount(-amount);
									t.setPercentage(-percentage);
									t.setShare(-share);
									transactions.remove(transaction);
									transactions.add(t);
								}
								find = true;
								break;
							}
						}// if(t.getSecurityID().equals(transaction.getSecurityID())){
					}// if(t.getSecurityID()==null){
				}// if (t.getDate().equals(transaction.getDate())) {
			}// for j
			if (!find) {
				transactions.add(t);
			}
		}
		Iterator<Transaction> iter=transactions.iterator();
		while(iter.hasNext()){
			Transaction t=iter.next();
			if(t.getAmount().equals(0.0)){
				iter.remove();
			}
		}

		return transactions;
	}

	public static String outputTransaction(Transaction t) {
		StringBuffer sb = new StringBuffer();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		sb.append("<td>");
		sb.append(df.format(t.getDate()));
		sb.append("</td>");
		sb.append("<td>");
		sb.append(t.getOperation());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(t.getSecurityID());
		sb.append("</td>");
		sb.append("<td>");
		sb.append(t.getAmount());
		sb.append("</td>");
		return sb.toString();
	}

	public static String compareTransactions(List<Transaction> trs1, List<Transaction> trs2) {
		int i = 0, j = 0;
		Date lastDate = null;
		StringBuffer sb=new StringBuffer();
		sb.append("<table>");
		sb.append("\n");
		while (i < trs1.size() && j < trs2.size()) {
			Transaction t1 = trs1.get(i);

			if (lastDate != null && !t1.getDate().equals(lastDate)) {
				while (j < trs2.size()) {
					Transaction t2 = trs2.get(j);
					if (!t2.equals(lastDate)) {
						break;
					}
					sb.append("<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>");
					sb.append(outputTransaction(t2) + "</tr>");
					sb.append("\n");
					j++;
				}
			}
			Transaction t2 = trs2.get(j);
			sb.append("<tr>" + outputTransaction(t1));
			if (t1.getDate().equals(t2.getDate())) {
				sb.append(outputTransaction(t2) + "</tr>");
				j++;
			} else {
				sb.append("<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
			}
			i++;
			lastDate = t1.getDate();
			sb.append("\n");
			
		}

		while (i < trs1.size()) {
			Transaction t1 = trs1.get(i);
			sb.append("<tr>" + outputTransaction(t1));
			sb.append("<td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td></tr>");
			sb.append("\n");
			i++;
		}
		while (j < trs2.size()) {
			Transaction t2 = trs2.get(j);
			sb.append("<tr><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>");
			sb.append(outputTransaction(t2) + "</tr>");
			sb.append("\n");
			j++;
		}
		sb.append("</table>");
		return sb.toString();
	}

	public static void main1(String[] args){
		PortfolioManager pm = ContextHolder.getPortfolioManager();
		List<Portfolio> portfolios = pm.getCurrentPortfolios();
		for (int i = portfolios.size() - 1; i >=0; i--) {
			List<Transaction> transactions = pm.getTransactions(portfolios.get(i).getID());
			if (transactions != null && transactions.size() > 0) {
				try {
					File f=new File(transactions.get(0).getPortfolioID()+".html");
					BufferedWriter bw=new BufferedWriter(new FileWriter(f));
					List<Transaction> mTransactions = MergeTransactions(transactions);
					bw.write(compareTransactions(transactions, mTransactions));
					bw.flush();
					bw.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}
	public static void main(String[] args){
		List<Transaction> trs=new ArrayList<Transaction>();
		trs.add(new Transaction());
		trs.add(new Transaction());
		trs.get(0).setAmount(500.0);
		trs.get(0).setSecurityID(9l);
		trs.get(0).setOperation(Configuration.TRANSACTION_BUY_AT_OPEN);
		trs.get(0).setTransactionType(2);
		
		
		trs.get(1).setAmount(500.0);
		trs.get(1).setSecurityID(9l);
		trs.get(0).setOperation(Configuration.TRANSACTION_BUY_AT_OPEN);
		MergeTransactions(trs);
		
		for(Transaction t:trs){
			System.out.println(t.getAmount());
		}
		//fixPercentage();
	}
	
}
