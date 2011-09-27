package com.lti.service.bo;
import java.util.Date;

import com.lti.service.FinancialStatementManager;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;

public class Profitability {
	
	protected Double COGSRate;
	protected Double operMargin;
	protected Double RevenueRate = 1.0;
	
	public Double getRevenueRate(){
		return RevenueRate;
	}
	
	public Double calculateCOGSRate(String symbol,Date date){
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		IncomeStatement is= fsm.getIncomeStatement(symbol,date);
			COGSRate = is.getCOGS()/is.getRevenue();
			return COGSRate;
	}
	
	public Double calculateGrossMargin(String symbol,Date date){
		Double CrossMargin = 1-calculateCOGSRate(symbol,date);
		return CrossMargin;
	}
	
	public Double calculateSGNARate(String symbol,Date date){
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		IncomeStatement is= fsm.getIncomeStatement(symbol,date);
		Double SGNARate = is.getSGNA()/is.getRevenue();
		return SGNARate;
	}
	
	public Double calculateRNDRate(String symbol,Date date){
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		IncomeStatement is= fsm.getIncomeStatement(symbol,date);
	    Double RNDRate = is.getRND()/is.getRevenue();
	    return RNDRate;
	}
	
	public Double calculateOtherOperExpRate(String symbol,Date date){
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		IncomeStatement is= fsm.getIncomeStatement(symbol,date);
		Double OtherOperExpRate = is.getOtherOperExp()/is.getRevenue();
		return OtherOperExpRate;
	}
	
	public Double calculateOperMargin(String symbol,Date date){
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		IncomeStatement is= fsm.getIncomeStatement(symbol,date);
		Double operIncome = is.getRevenue()-is.getCOGS()-is.getRND()-is.getSGNA()-is.getOtherOperExp();
		operMargin = operIncome/is.getRevenue();
		return operMargin;
	}
	
	public Double calculateIntExpRate(String symbol,Date date){
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		IncomeStatement is= fsm.getIncomeStatement(symbol,date);
	    Double IntExpRate = is.getIntExp()/is.getRevenue();
		return IntExpRate;
	}

	public Double calculateEBTMargin(String symbol,Date date){
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		IncomeStatement is= fsm.getIncomeStatement(symbol,date);
		Double EBT = is.getRevenue()-is.getCOGS()-is.getRND()-is.getSGNA()-is.getOtherOperExp()+is.getTotalOtherInc()-is.getIntExp();
		Double EBTMargin = EBT/is.getRevenue();
		return EBTMargin;
	}
	
	public Double calculateTaxRate(String symbol,Date date){
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		IncomeStatement is= fsm.getIncomeStatement(symbol,date);
		Double EBT = is.getRevenue()-is.getCOGS()-is.getRND()-is.getSGNA()-is.getOtherOperExp()+is.getTotalOtherInc()-is.getIntExp();
		Double taxRate = is.getIncomeTax()/EBT;
		return taxRate;
	}
	
	public Double calculateNetMargin(String symbol,Date date){
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		IncomeStatement is= fsm.getIncomeStatement(symbol,date);
		Double EAT =  is.getRevenue()-is.getCOGS()-is.getRND()-is.getSGNA()-is.getOtherOperExp()+is.getTotalOtherInc()-is.getIntExp()-is.getIncomeTax();
		Double netMargin = EAT/is.getRevenue();
		return netMargin;
	}
	
	public Double calculateAssetTurnover(String symbol,Date date){
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		IncomeStatement is= fsm.getIncomeStatement(symbol,date);
		BalanceStatement bs = fsm.getBalanceStatement(symbol, date);
		Double totalAsset = bs.getCashNEquiv()+bs.getShortTermInv()+bs.getNetRec()+bs.getInventory()+bs.getOtherCurAsset()+bs.getLongTermInv()+bs.getPPNE()+bs.getIntangibles()+bs.getOtherLongTermAsset();
		Double assetTurnover = is.getRevenue()/totalAsset;
		return assetTurnover;

	}
	public Double calculateReturnOnAsset(String symbol,Date date){
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		IncomeStatement is= fsm.getIncomeStatement(symbol,date);
		BalanceStatement bs = fsm.getBalanceStatement(symbol, date);
		Double netIncome =  is.getRevenue()-is.getCOGS()-is.getRND()-is.getSGNA()-is.getOtherOperExp()+is.getTotalOtherInc()-is.getIntExp()-is.getIncomeTax()+is.getDiscOper()+is.getExtItem()+is.getAcctChange();
		Double totalAsset = bs.getCashNEquiv()+bs.getShortTermInv()+bs.getNetRec()+bs.getInventory()+bs.getOtherCurAsset()+bs.getLongTermInv()+bs.getPPNE()+bs.getIntangibles()+bs.getOtherLongTermAsset();
		Double returnOnAsset = netIncome/totalAsset;
        return returnOnAsset;	
	}
	public Double calculateFinancialLeverage(String symbol,Date date){
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		BalanceStatement bs = fsm.getBalanceStatement(symbol, date);
		Double totalAsset = bs.getCashNEquiv()+bs.getShortTermInv()+bs.getNetRec()+bs.getInventory()+bs.getOtherCurAsset()+bs.getLongTermInv()+bs.getPPNE()+bs.getIntangibles()+bs.getOtherLongTermAsset();
		Double totalEquity = bs.getPreferredStock()+bs.getCommonStock()+bs.getRetainedEarning()+bs.getTreasuryStock()+bs.getOtherEquity();
		return totalAsset/totalEquity;
	}
	public Double calculateReturnOnEquity(String symbol,Date date){
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		IncomeStatement is= fsm.getIncomeStatement(symbol,date);
		BalanceStatement bs = fsm.getBalanceStatement(symbol, date);
		Double netIncome =  is.getRevenue()-is.getCOGS()-is.getRND()-is.getSGNA()-is.getOtherOperExp()+is.getTotalOtherInc()-is.getIntExp()-is.getIncomeTax()+is.getDiscOper()+is.getExtItem()+is.getAcctChange();
		Double totalEquity = bs.getPreferredStock()+bs.getCommonStock()+bs.getRetainedEarning()+bs.getTreasuryStock()+bs.getOtherEquity();
		Double returnOnEquity = netIncome/totalEquity;
		return returnOnEquity;
	}

}
