/**
 * 
 */
package com.lti.util;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.lti.Exception.SecurityException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.Transaction;
import com.lti.system.ContextHolder;
import com.lti.system.Configuration;
import com.lti.type.finance.HoldingInf;
/**
 * @author CCD
 *
 */
public class PortfolioAdjustAmountUtil {
	public static final int COMMISSION_TYPE_EACHPAY = 1;
	public static final int COMMISSION_TYPE_RATE = 2;
	/**
	 * 
	 * @param portfolioID
	 * @param commissionType
	 * @param commissionAmountEachTransaction
	 * @param commissionRate
	 * @param taxOnReturn
	 * @param withDrawAmount
	 * @param taxOnWithdraw
	 * @param depositAmount
	 * @param ignoreCash
	 * @throws NoPriceException 
	 * @throws SecurityException 
	 */
	private static List<PortfolioDailyData> adjustPortfolioAmount(Long portfolioID, int commissionType, Double commissionAmountEachTransaction, Double commissionRate, Double taxRateOnLongReturn, Double taxRateOnShortReturn, Double withDrawAmount, Double taxOnWithdraw, Double depositAmount, boolean ignoreCash, int interval) throws SecurityException, NoPriceException {
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		List<PortfolioDailyData> pddList = portfolioManager.getDailydatas(portfolioID);
		List<Transaction> transactionList = portfolioManager.getRealAndReinvestTransactions(portfolioID);
		List<PortfolioDailyData> adjustedAmountList = new ArrayList<PortfolioDailyData>();
		List<PortfolioDailyData> adjustedAmountForMPTList = new ArrayList<PortfolioDailyData>();
		HashMap<Long, List<Transaction>> transactionMap = new HashMap<Long, List<Transaction>>();
		HashMap<Long, Integer> transactionCurIndexMap = new HashMap<Long, Integer>();
		//用来累计长期收益的，如果年末时为负，则其值保存到下年，否则扣完税后其值清0
		Double longTermAmount = 0.0;
		//用来累计短期收益的，如果年末时为负，则其值保存到下年，否则扣完税后其值清0
		Double shortTermAmount = 0.0;
		//年末时长期收益额
		Double longTermTax = 0.0;
		//年末时短期收益额
		Double shortTermTax = 0.0;
		int transactionIndex = 0;
		if(pddList != null && pddList.size() > 0) {
			longTermTax = 0.0;
			shortTermTax = 0.0;
			Double adjustedRate = 1.0;
			Double oneAdjustedRate = 1.0;
			boolean first = true;
			Double preOriginalAmount = 0.0;
			for(PortfolioDailyData pdd : pddList) {
				Date curDate = pdd.getDate();
				boolean adjusted =  false;
				Double curOriginalAmount = pdd.getAmount();
				Double adjustedAmount = curOriginalAmount;
				Double adjustedAmountForMPT = curOriginalAmount;
				if(first){
					preOriginalAmount = curOriginalAmount;
					first = false;
				}
				oneAdjustedRate = curOriginalAmount / preOriginalAmount;
				if(adjustedAmountList.size() > 0)
					adjustedAmount = adjustedAmountList.get(adjustedAmountList.size() - 1).getAmount() * oneAdjustedRate;
				if(adjustedAmountForMPTList.size() > 0)
					adjustedAmountForMPT = adjustedAmountForMPTList.get(adjustedAmountForMPTList.size() - 1).getAmount() * oneAdjustedRate;
				
				//首先考虑commission
				Double tradingAmount = 0.0;
				int tradingCount = 0;
				for(int i=transactionIndex;i<transactionList.size();++i){
					Transaction t = transactionList.get(i);
					if(LTIDate.equals(t.getDate(), curDate)){
						if(t.getTransactionType() == Configuration.TRANSACTION_TYPE_REAL){
							//如果是买或分红则把交易记录加入到transactionMap
							if(t.getOperation().equals(Configuration.TRANSACTION_BUY) || t.getOperation().equals(Configuration.TRANSACTION_BUY_AT_OPEN) || t.getOperation().equals(Configuration.TRANSACTION_REINVEST))
							{
								List<Transaction> tList = transactionMap.get(t.getSecurityID());
								if(tList == null){
									tList = new ArrayList<Transaction>();
									transactionCurIndexMap.put(t.getSecurityID(), 0);
								}
								tList.add(t);
								transactionMap.put(t.getSecurityID(), tList);
							}
							if(ignoreCash && t.getID() == Configuration.CASHID)
								continue;
							if(commissionType == COMMISSION_TYPE_EACHPAY) {//按笔收取手续费
								++tradingCount;
							}else{//按比例收取手续费
								tradingAmount += t.getAmount() ;
							}
							adjusted = true;
							//如果是卖的话，则必须计算长短期利益
							if(t.getOperation().equals(Configuration.TRANSACTION_SELL_AT_OPEN) || t.getOperation().equals(Configuration.TRANSACTION_SELL)) {
								Double curLongAmount = 0.0;
								Double curShortAmount = 0.0;
								Date midTermStartDate = LTIDate.getNewNYSEMonth(curDate, -interval);
								List<Transaction> tList = transactionMap.get(t.getSecurityID());
								int curIndex = transactionCurIndexMap.get(t.getSecurityID());
								Double sellShare = t.getShare();
								Double curPrice = securityManager.getAdjPrice(t.getSecurityID(), t.getDate());
								boolean ok = false;
								for(int j = curIndex; j < tList.size(); ++j){
									Transaction buyTransaction = tList.get(j);
									Double buyShare = buyTransaction.getShare();
									Double buyPrice = securityManager.getAdjPrice(t.getSecurityID(), buyTransaction.getDate());
									Double tAmount = 0.0;
									if(buyShare < sellShare) {
										tAmount = buyShare * (curPrice - buyPrice);
										buyTransaction.setShare(0.0);
									}else{
										tAmount = sellShare * (curPrice - buyPrice);
										buyTransaction.setAmount(buyShare - sellShare);
										sellShare = 0.0;
										curIndex = j;
										transactionCurIndexMap.put(t.getSecurityID(), curIndex);
										ok = true;
									}
									if(LTIDate.before(buyTransaction.getDate(), midTermStartDate)){
										 curLongAmount += tAmount * adjustedRate;
									}else 
										 curShortAmount += tAmount * adjustedRate;
									if(ok)
										break;
								}
								Double longTransactionCost = curLongAmount / (curLongAmount + curShortAmount);
								curLongAmount -= longTransactionCost * commissionAmountEachTransaction;
								curShortAmount -= (1 - longTransactionCost) * commissionAmountEachTransaction;
								longTermAmount += curLongAmount;
								shortTermAmount += curShortAmount;
							}
						}else
							continue;
					}else{
						transactionIndex = i;
						break;
					}
				}
				//交易费用
				Double taxAmountOnTransaction = 0.0;
				if(commissionType == COMMISSION_TYPE_EACHPAY) //按笔收取手续费
					taxAmountOnTransaction = tradingCount * commissionAmountEachTransaction;
				else//按比例收取手续费
					taxAmountOnTransaction = tradingAmount * commissionRate * adjustedRate;
					
				//取款费用
				Double curWithDrawAmount = 0.0;
				Double curDepositAmount = 0.0;
				Double taxAmountOnWithdraw = 0.0;
				if(LTIDate.isLastNYSETradingDayOfMonth(curDate)){
					curWithDrawAmount = withDrawAmount;
					curDepositAmount = depositAmount;
					adjusted = true;
				}
				taxAmountOnWithdraw = withDrawAmount * taxOnWithdraw;
				//年税收
				Double taxAmountOnReturn = 0.0;
				if(LTIDate.isLastNYSETradingDayOfYear(curDate)) {
					//如果是每年最后一个交易日，则继续判断是否之前已有一个交易日
					if((longTermAmount < 0 && shortTermAmount > 0)||(longTermAmount > 0 && shortTermAmount < 0) ){
						if(Math.abs(longTermAmount) > Math.abs(shortTermAmount)){
							longTermAmount += shortTermAmount;
							shortTermAmount = 0.0;
						}else{
							shortTermAmount += longTermAmount;
							longTermAmount = 0.0;
						}
					}
					if(longTermAmount > 0){
						longTermTax = longTermAmount;
						longTermAmount = 0.0;
					}
					if(shortTermAmount > 0){
						shortTermTax = shortTermAmount;
						shortTermAmount = 0.0;
					}
					taxAmountOnReturn = longTermTax * taxRateOnLongReturn + shortTermTax * taxRateOnShortReturn;
					adjusted = true;
				}
					
				//adjust amount for mpt
				adjustedAmountForMPT = adjustedAmountForMPT - (taxAmountOnTransaction + taxAmountOnWithdraw + taxAmountOnReturn);
				//adjust amount
				adjustedAmount = adjustedAmount - (taxAmountOnTransaction + taxAmountOnWithdraw + curWithDrawAmount + taxAmountOnReturn) + curDepositAmount;
				//调整下次交易日的交易比例
				adjustedRate = adjustedAmount / curOriginalAmount;
				preOriginalAmount = curOriginalAmount; 
					
				PortfolioDailyData pdd1 = new PortfolioDailyData();
				pdd1.setPortfolioID(portfolioID);
				pdd1.setDate(curDate);
				pdd1.setAmount(adjustedAmountForMPT);
				adjustedAmountForMPTList.add(pdd1);
					
					
				PortfolioDailyData pdd2 = new PortfolioDailyData();
				pdd2.setPortfolioID(portfolioID);
				pdd2.setDate(curDate);
				pdd2.setAmount(adjustedAmount);
				adjustedAmountList.add(pdd2);
				
			}
		}
		return adjustedAmountForMPTList;
	}
	
	
	
	private static List<PortfolioMPT> calculateMPT(final Portfolio portfolio, List<PortfolioDailyData> pddList){
		BaseFormulaUtil bfu = new BaseFormulaUtil();
		SecurityManager securityManager = ContextHolder.getSecurityManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		HoldingInf simulateHolding = portfolioManager.getHolding(portfolio.getID(), Configuration.PORTFOLIO_HOLDING_CURRENT);
		Date startDate = pddList.get(0).getDate();
		Date endDate = pddList.get(pddList.size() - 1).getDate();
		long benchmarkID = simulateHolding.getBenchmarkID();
		long cashID = securityManager.get("CASH").getID();
		List<SecurityDailyData> sddList = securityManager.getDailyDatas(benchmarkID, LTIDate.getNewWeekDay(startDate, -1), LTIDate.getNewWeekDay(endDate, 1));
		List<SecurityDailyData> riskFreeList = securityManager.getDailyDatas(cashID, startDate, endDate);
		return bfu.computePortfolioMPTConsiderTax(portfolio, pddList, sddList, riskFreeList);
	}
	
	public static List<PortfolioMPT> calculatePerformance(Long portfolioID, int commissionType, Double commissionAmountEachTransaction, Double commissionRate, Double taxRateOnLongReturn, Double taxRateOnShortReturn, Double withDrawAmount, Double taxOnWithdraw, Double depositAmount, boolean ignoreCash, int interval) throws SecurityException, NoPriceException {
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		Portfolio portfolio = portfolioManager.get(portfolioID);
		List<PortfolioDailyData> pddList = PortfolioAdjustAmountUtil.adjustPortfolioAmount(portfolioID, commissionType, commissionAmountEachTransaction, commissionRate, taxRateOnLongReturn, taxRateOnShortReturn, withDrawAmount, taxOnWithdraw, depositAmount, ignoreCash, interval);
		List<PortfolioMPT> mptList = PortfolioAdjustAmountUtil.calculateMPT(portfolio, pddList);
		return mptList;
	}
	
	public static void main(String[] args) throws SecurityException, NoPriceException{
		List<PortfolioMPT> mptList = PortfolioAdjustAmountUtil.calculatePerformance(10361L, COMMISSION_TYPE_EACHPAY, 7.0, 0.1, 0.1, 0.3, 100.0, 0.0, 0.0, true, 12);
		for(PortfolioMPT pmpt : mptList){
			System.out.println( pmpt.getYear() + "," + pmpt.getAR() + "," + pmpt.getSharpeRatio());
		}
		return;
	}
}
