package com.lti.FinancialStatement;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.service.BaseFormulaManager;
import com.lti.service.FinancialStatementManager;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;
import com.lti.service.SecurityManager;
import com.lti.service.bo.BalanceStatement;
import com.lti.service.bo.CashFlow;
import com.lti.service.bo.Company;
import com.lti.service.bo.IncomeStatement;
import com.lti.service.bo.Security;
import com.lti.service.bo.YearlyBalanceStatement;
import com.lti.service.bo.YearlyCashFlow;
import com.lti.service.bo.YearlyIncomeStatement;
import com.lti.service.impl.DAOManagerImpl;
import com.lti.type.TimeUnit;

/**
 * @author Xie
 *
 */
public class FinancialStatementDatabaseImpl extends DAOManagerImpl implements FinancialStatement {

	FinancialStatementManager financialStatementManager = (FinancialStatementManager)ContextHolder.getInstance().getApplicationContext().getBean("financialStatementManager");
	/**
	 * get the revenue in the current quarter of the given date
	 * @param symbol
	 * @param date
	 * @throws 
	 */
	@Override
	public Double getAdjNetInc(String symbol, Date date) {
		return this.getAdjNetInc(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override     
	public Double getAdjNetInc(String symbol, int year, int quarter) {
	    return this.getCashFlow(symbol, year, quarter).getAdjNetInc();
	}

	@Override
	public Double getChangInCash(String symbol, Date date) {
		return this.getChangInCash(symbol,date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getChangInCash(String symbol, int year, int quarter) {
		com.lti.service.bo.CashFlow cf = this.getCashFlow(symbol,year,quarter);
		return this.getOperCash(symbol,year,quarter)+this.getInvCash(symbol,year,quarter)+this.getFinCash(symbol,year,quarter)+cf.getCurrencyAdj();
	}

	@Override
	public Double getCommonStock(String symbol, Date date) {
		return this.getCommonStock(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getCommonStock(String symbol, int year, int quarter) {
		return this.getBalanceStatement(symbol, year, quarter).getCommonStock();
	}

	@Override
	public Double getCurrencyAdj(String symbol, Date date) {
		return this.getCurrencyAdj(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getCurrencyAdj(String symbol, int year, int quarter) {
		return this.getCashFlow(symbol, year,quarter).getCurrencyAdj();
	}

	@Override
	public Double getDepr(String symbol, Date date) {
		return this.getDepr(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getDepr(String symbol, int year, int quarter) {
		return this.getCashFlow(symbol, year,quarter).getDeprAmort();
	}

	@Override
	public Double getIntExp(String symbol, Date date) {
		return this.getIntExp(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getIntExp(String symbol, int year, int quarter) {
		return this.getIncomeStatement(symbol,year,quarter).getIntExp();
	}

	@Override
	public Double getInvestment(String symbol, Date date) {
		return this.getInvestment(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getInvestment(String symbol, int year, int quarter) {
		return this.getCashFlow(symbol,year,quarter).getInvestment();
	}

	@Override
	public Double getNetBorrowing(String symbol, Date date) {
		return this.getNetBorrowing(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getNetBorrowing(String symbol, int year, int quarter) {
		return this.getCashFlow(symbol,year,quarter).getNetBorrow();
	}

	@Override
	public Double getNetRec(String symbol, Date date) {
		return this.getNetRec(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getNetRec(String symbol, int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getNetRec();
	}

	@Override
	public Double getNetTanAssets(String symbol, Date date) {
		return this.getNetTanAssets(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getNetTanAssets(String symbol, int year, int quarter) {
		com.lti.service.bo.BalanceStatement bs = this.getBalanceStatement(symbol,year,quarter);
		return bs.getAssets()-bs.getLiab()-bs.getIntangibles();
	}

	@Override
	public Double getOtherEquity(String symbol, Date date) {
		return this.getOtherEquity(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getOtherEquity(String symbol, int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getOtherEquity();
	}

	@Override
	public Double getPPNE(String symbol, Date date) {
		return this.getPPNE(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getPPNE(String symbol, int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getPPNE();
	}

	@Override
	public Double getPreferredStock(String symbol, Date date) {
		return this.getPreferredStock(symbol,date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getPreferredStock(String symbol, int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getPreferredStock();
	}

	@Override
	public Double getRetainedEarning(String symbol, Date date) {
		return this.getRetainedEarning(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getRetainedEarning(String symbol, int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getRetainedEarning();
	}

	@Override
	public Double getStockSalePur(String symbol, Date date) {
		return this.getStockSalePur(symbol,date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getStockSalePur(String symbol, int year, int quarter) {
		return this.getCashFlow(symbol,year,quarter).getStockSalePur();
	}

	@Override
	public Double getTotalCurAssets(String symbol, Date date) {
		return this.getTotalCurAssets(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getTotalCurAssets(String symbol, int year, int quarter) {
		return this.getBalanceStatement(symbol, year, quarter).getCurAssets();
	}

	@Override
	public Double getTotalCurLiab(String symbol, Date date) {
		return this.getTotalCurLiab(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getTotalCurLiab(String symbol, int year, int quarter) {
		com.lti.service.bo.BalanceStatement bs = this.getBalanceStatement(symbol,year,quarter);
			return this.getBalanceStatement(symbol,year,quarter).getCurLiab();
	}

	@Override
	public Double getTotalOperExp(String symbol, Date date) {
		return this.getTotalOperExp(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getTotalOperExp(String symbol, int year, int quarter) {
		com.lti.service.bo.IncomeStatement is = this.getIncomeStatement(symbol,year,quarter);
		return this.getIncomeStatement(symbol,year,quarter).getTotalOperExp();
	}

	@Override
	public Double getTotalOtherIncome(String symbol, Date date) {
		return this.getTotalOtherIncome(symbol,date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getTotalOtherIncome(String symbol, int year, int quarter) {
		com.lti.service.bo.IncomeStatement is = this.getIncomeStatement(symbol,year,quarter);
		return this.getIncomeStatement(symbol,year,quarter).getTotalOtherInc();
	}

	@Override
	public Double getTreasuryStock(String symbol, Date date) {
		return this.getTreasuryStock(symbol,date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getTreasuryStock(String symbol, int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getTreasuryStock();
	}

	@Override
	public Double getAcctChange(String symbol, Date date) {
		return this.getAcctChange(symbol,date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getAcctChange(String symbol, int year,int quarter) {
		return this.getIncomeStatement(symbol,year,quarter).getAcctChange();
	}


	@Override
	public Double getAcctPayable(String symbol, Date date) {
		return this.getAcctPayable(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getAcctPayable(String symbol, int year,int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getAcctPayable();
	}

	@Override
	public Double getCapExp(String symbol, Date date) {
		return this.getCapExp(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getCapExp(String symbol,int year, int quarter) {
		return this.getCashFlow(symbol,year,quarter).getCapExp();
	}

	@Override
	public Double getCashNEquiv(String symbol, Date date) {
		return this.getCashNEquiv(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getCashNEquiv(String symbol, int year,int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getCashNEquiv();
	}

	@Override
	public Double getCOGS(String symbol, Date date) {
		return this.getCOGS(symbol, date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getCOGS(String symbol,int year, int quarter) {
		return this.getIncomeStatement(symbol,year,quarter).getCOGS();
	}


	@Override
	public Double getDiscOper(String symbol, Date date) {
		return this.getDiscOper(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getDiscOper(String symbol,int year, int quarter) {
		return this.getIncomeStatement(symbol,year,quarter).getDiscOper();
	}

	@Override
	public Double getDividends(String symbol, Date date) {
		return this.getDividends(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getDividends(String symbol,int year, int quarter) {
		return this.getCashFlow(symbol,year,quarter).getDividend();
	}

	@Override
	public Double getEarningAfterTax(String symbol, Date date) {
		return this.getEarningAfterTax(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getEarningAfterTax(String symbol,int year, int quarter) {
		return this.getIncomeStatement(symbol,year,quarter).getAfterTaxInc();
	}

	@Override
	public Double getEarningBeforeInt(String symbol, Date date) {
		return this.getEarningBeforeInt(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getEarningBeforeInt(String symbol, int year, int quarter) {
		com.lti.service.bo.IncomeStatement is = this.getIncomeStatement(symbol,year,quarter);
		return this.getOperatingIncome(symbol, year, quarter)+is.getTotalOtherInc();
	}

	@Override
	public Double getEarningBeforeTax(String symbol, Date date) {
		return this.getEarningBeforeTax(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getEarningBeforeTax(String symbol,int year, int quarter) {
		return this.getIncomeStatement(symbol,year,quarter).getPreTaxInc();
	}

	@Override
	public Double getExtItem(String symbol, Date date) {
		return this.getExtItem(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getExtItem(String symbol,int year, int quarter) {
		return this.getIncomeStatement(symbol,year,quarter).getExtItem();
	}

	@Override
	public Double getFinCash(String symbol, Date date) {
		return this.getFinCash(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getFinCash(String symbol,int year, int quarter) {
		return this.getCashFlow(symbol,year,quarter).getFinCF();
	}

	@Override
	public Double getFreeCash(String symbol, Date date) {
		return this.getFreeCash(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getFreeCash(String symbol,int year, int quarter) {
		com.lti.service.bo.CashFlow cf = this.getCashFlow(symbol,year,quarter);
		return this.getOperCash(symbol,year,quarter)+cf.getCapExp();
	}

	@Override
	public Double getGrossProfit(String symbol, Date date) {
		return this.getGrossProfit(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getGrossProfit(String symbol,int year, int quarter) {
		com.lti.service.bo.IncomeStatement is = this.getIncomeStatement(symbol,year,quarter);
	    return this.getIncomeStatement(symbol,year,quarter).getGrossProfit();
	}

	@Override
	public Double getIncomeTax(String symbol, Date date) {
		return this.getIncomeTax(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getIncomeTax(String symbol,int year, int quarter) {
		return this.getIncomeStatement(symbol,year,quarter).getIncomeTax();
	}

	@Override
	public Double getIntangibles(String symbol, Date date) {
		return this.getIntangibles(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getIntangibles(String symbol,int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getIntangibles();
	}

	@Override
	public Double getInvCash(String symbol, Date date) {
		return this.getInvCash(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getInvCash(String symbol,int year, int quarter) {
		return this.getCashFlow(symbol,year,quarter).getInvCF();
	}

	@Override
	public Double getInventory(String symbol, Date date) {
		return this.getInventory(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getInventory(String symbol,int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getInventory();
	}

	@Override
	public Double getLTDebt(String symbol, Date date) {
		return this.getLTDebt(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getLTDebt(String symbol,int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getLongTermDebt();
	}

	@Override
	public Double getNetIncome(String symbol, Date date) {
		return this.getNetIncome(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getNetIncome(String symbol,int year, int quarter) {
	    return this.getIncomeStatement(symbol,year,quarter).getNetInc();
	}


	@Override
	public Double getOperatingIncome(String symbol, Date date) {
		return this.getOperatingIncome(symbol,date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getOperatingIncome(String symbol,int year, int quarter) {
		return this.getIncomeStatement(symbol,year,quarter).getOperInc();
	}

	@Override
	public Double getOperCash(String symbol, Date date) {
		return this.getOperCash(symbol,date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getOperCash(String symbol,int year, int quarter) {
		return this.getCashFlow(symbol,year,quarter).getOperCF();
	}

	@Override
	public Double getOtherCurAssets(String symbol, Date date) {
		return this.getOtherCurAssets(symbol,date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getOtherCurAssets(String symbol,int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getOtherCurAsset();
	}

	@Override
	public Double getOtherFinCash(String symbol, Date date) {
		return this.getOtherFinCash(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getOtherFinCash(String symbol,int year, int quarter) {
		return this.getCashFlow(symbol,year,quarter).getOtherFinCash();
	}

	@Override
	public Double getOtherInvCash(String symbol, Date date) {
		return this.getOtherInvCash(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getOtherInvCash(String symbol,int year, int quarter) {
		return this.getCashFlow(symbol,year,quarter).getOtherInvCash();
	}

	@Override
	public Double getOtherLTAssets(String symbol, Date date) {
		return this.getOtherLTAssets(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getOtherLTAssets(String symbol,int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getOtherLongTermAsset();
	}

	@Override
	public Double getLTInv(String symbol, Date date) {
		return this.getLTInv(symbol, date.getYear()+1900, LTIDate.getQuarter(date));
	}
	
	@Override
	public Double getLTInv(String symbol, int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getLongTermInv();
	}

	@Override
	public Double getOtherLTLiab(String symbol, Date date) {
		return this.getOtherLTLiab(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getOtherLTLiab(String symbol,int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getOtherLongTermLiab();
	}

	@Override
	public Double getOtherOperCash(String symbol, Date date) {
		return this.getOtherOperCash(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getOtherOperCash(String symbol,int year, int quarter) {
		return this.getCashFlow(symbol, year,quarter).getOtherOperCash();
	}

	@Override
	public Double getOtherOperExp(String symbol, Date date) {
		return this.getOtherOperExp(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getOtherOperExp(String symbol,int year, int quarter) {
		return this.getIncomeStatement(symbol,year,quarter).getOtherOperExp();
	}

	@Override
	public Double getOtherCurLiab(String symbol, Date date) {
		return this.getOtherCurLiab(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getOtherCurLiab(String symbol,int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getOtherCurLiab();
	}

	@Override
	public Double getRevenue(String symbol, Date date) {
		return this.getRevenue(symbol,date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getRevenue(String symbol,int year, int quarter) {
		return this.getIncomeStatement(symbol, year,quarter).getRevenue();
	}

	@Override
	public Double getRND(String symbol, Date date) {
		return this.getRND(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getRND(String symbol,int year, int quarter) {
		return this.getIncomeStatement(symbol,year,quarter).getRND();
	}

	@Override
	public Double getSGNA(String symbol, Date date) {
		return this.getSGNA(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getSGNA(String symbol,int year, int quarter) {
		return this.getIncomeStatement(symbol,year,quarter).getSGNA();
	}

	@Override
	public Double getCurDebt(String symbol, Date date) {
		return this.getCurDebt(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getCurDebt(String symbol,int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getShortTermDebt();
	}

	@Override
	public Double getCurInv(String symbol, Date date) {
		return this.getCurInv(symbol,date.getYear()+1900, LTIDate.getQuarter(date));
	}

	@Override
	public Double getCurInv(String symbol,int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getShortTermInv();
	}

	@Override
	public Double getTotalAssets(String symbol, Date date) {
		return this.getTotalAssets(symbol,date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getTotalAssets(String symbol,int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getAssets();
	}

	@Override
	public Double getTotalEquity(String symbol, Date date) {
		return this.getTotalEquity(symbol,date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getTotalEquity(String symbol,int year, int quarter) {
		return this.getTotalAssets(symbol,year,quarter) - this.getTotalLiab(symbol,year,quarter);
	}

	@Override
	public Double getTotalLiab(String symbol, Date date) {
		return this.getTotalLiab(symbol,date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getTotalLiab(String symbol,int year, int quarter) {
		return this.getBalanceStatement(symbol,year,quarter).getLiab();
	}

	@Override
	public Double getTotalLiabNEquiv(String symbol, Date date) {
		return this.getTotalLiabNEquiv(symbol,date.getYear()+1900,LTIDate.getQuarter(date));
	}

	@Override
	public Double getTotalLiabNEquiv(String symbol,int year, int quarter) {
		return this.getTotalEquity(symbol,year,quarter)+ this.getTotalLiab(symbol,year,quarter);
	}
	
	@Override
	public Double getAcctChange(String symbol, int year) {
		Double yearAC=0.0;
		for(int i=1;i<5;i++){
			yearAC += this.getAcctChange(symbol, year, i);
		}
		return yearAC;
	}

	@Override
	public Double getAcctPayable(String symbol, int year) {
		return this.getAcctChange(symbol, year, 4);
	}

	@Override
	public Double getAdjNetInc(String symbol, int year) {
		Double yearANI = 0.0;
		for(int i=1;i<5;i++){
			yearANI += this.getAdjNetInc(symbol, year, i);
		}
		return yearANI;
	}

	@Override
	public Double getCapExp(String symbol, int year) {
		Double yearCE = 0.0;
		for(int i=1;i<5;i++){
			yearCE +=this.getCapExp(symbol, year, i);
		}
		return yearCE;
	}

	@Override
	public Double getCashNEquiv(String symbol, int year) {
		return this.getCashNEquiv(symbol, year, 4);
	}

	@Override
	public Double getChangInCash(String symbol, int year) {
		Double yearCC = 0.0;
		for(int i=1;i<5;i++){
			yearCC += this.getChangInCash(symbol, year, i);
		}
		return yearCC;
	}

	@Override
	public Double getCOGS(String symbol, int year) {
		return this.getYearlyIncomeStatement(symbol, year).getCOGS();
	}

	@Override
	public Double getCommonStock(String symbol, int year) {
		return this.getCommonStock(symbol, year, 4);
	}

	@Override
	public Double getCurrencyAdj(String symbol, int year) {
		Double yearCA = 0.0;
		for(int i=1;i<5;i++){
			yearCA += this.getCurrencyAdj(symbol, year, i);
		}
		return yearCA;
	}

	@Override
	public Double getDepr(String symbol, int year) {
		Double yearD = 0.0;
		for(int i=1;i<5;i++){
			yearD += this.getDepr(symbol, year, i);
		}
		return yearD;
	}

	@Override
	public Double getDiscOper(String symbol, int year) {
		Double yearDO = 0.0;
		for(int i=1;i<5;i++){
			yearDO +=this.getDiscOper(symbol, year, i);
		}
		return yearDO;
	}

	@Override
	public Double getDividends(String symbol, int year) {
		Double yearD = 0.0;
		for(int i=1;i<5;i++){
			yearD += this.getDividends(symbol, year, i);
		}
		return yearD;
	}

	@Override
	public Double getEarningAfterTax(String symbol, int year) {
		return this.getYearlyIncomeStatement(symbol, year).getAfterTaxInc();
	}

	@Override
	public Double getEarningBeforeInt(String symbol, int year) {
		return this.getYearlyIncomeStatement(symbol, year).getPreTaxInc();
	}

	@Override
	public Double getEarningBeforeTax(String symbol, int year) {
		return this.getYearlyIncomeStatement(symbol, year).getPreTaxInc();
	}

	@Override
	public Double getExtItem(String symbol, int year) {
		Double yearEI = 0.0;
		for(int i=1;i<5;i++){
			yearEI += this.getExtItem(symbol, year, i);
		}
		return yearEI;
	}

	@Override
	public Double getFinCash(String symbol, int year) {
		return this.getYearlyCashFlow(symbol, year).getFinCF();
	}

	@Override
	public Double getFreeCash(String symbol, int year) {
		Double yearFC = 0.0;
		for(int i=1;i<5;i++){
			yearFC += this.getFreeCash(symbol, year, i);
		}
		return yearFC;
	}

	@Override
	public Double getGrossProfit(String symbol,int year) {
		return this.getYearlyIncomeStatement(symbol, year).getGrossProfit();
	}

	@Override
	public Double getIncomeTax(String symbol, int year) {
		return this.getYearlyIncomeStatement(symbol, year).getIncomeTax();
	}
                               
	@Override
	public Double getIntangibles(String symbol, int year) {
		return this.getIntangibles(symbol, year, 4);
	}

	@Override
	public Double getIntExp(String symbol, int year) {
		return this.getYearlyIncomeStatement(symbol, year).getIntExp();
	}

	@Override
	public Double getInvCash(String symbol, int year) {
		return this.getYearlyCashFlow(symbol, year).getInvCF();
	}

	@Override
	public Double getInventory(String symbol, int year) {
		return this.getYearlyBalanceStatement(symbol, year).getInventory();
	}

	@Override
	public Double getInvestment(String symbol, int year) {
		Double yearI = 0.0;
		for(int i=1;i<5;i++){
			yearI += this.getInvestment(symbol, year, i);
		}
		return yearI;
	}

	@Override
	public Double getLTDebt(String symbol, int year) {
		return this.getLTDebt(symbol, year, 4);
	}

	@Override
	public Double getLTInv(String symbol, int year) {
		return this.getLTInv(symbol, year, 4);
	}

	@Override
	public Double getNetBorrowing(String symbol, int year) {
		Double yearNB = 0.0;
		for(int i=1;i<5;i++){
			yearNB += this.getNetBorrowing(symbol, year,i);
		}
		return yearNB;
	}

	@Override
	public Double getNetIncome(String symbol, int year) {
		return this.getYearlyIncomeStatement(symbol, year).getNetInc();
	}

	@Override
	public Map<String, Double> getNetIncome(Date sDate, Date eDate) {
		return financialStatementManager.getNetIncome(sDate, eDate);
	}

	@Override
	public Double getNetRec(String symbol, int year) {
		return this.getNetRec(symbol, year, 4);
	}

	@Override
	public Double getNetTanAssets(String symbol, int year) {
		return this.getNetTanAssets(symbol, year, 4);
	}

	@Override
	public Double getOperatingIncome(String symbol, int year) {
		return this.getYearlyIncomeStatement(symbol, year).getOperInc();
	}

	@Override
	public Double getOperCash(String symbol, int year) {
		return this.getYearlyCashFlow(symbol, year).getOperCF();
	}

	@Override
	public Double getOtherCurAssets(String symbol, int year) {
		return this.getOtherCurAssets(symbol, year, 4);
	}

	@Override
	public Double getOtherEquity(String symbol, int year) {
		return this.getOtherEquity(symbol, year, 4);
	}

	@Override
	public Double getOtherFinCash(String symbol, int year) {
		Double yearOF = 0.0;
		for(int i=1;i<5;i++){
			yearOF += this.getOtherFinCash(symbol, year, i);
		}
		return yearOF;
	}

	@Override
	public Double getOtherInvCash(String symbol, int year) {
		Double yearOI = 0.0;
		for(int i=1;i<5;i++){
			yearOI += this.getOtherInvCash(symbol, year, i);
		}
		return yearOI;
	}

	@Override
	public Double getOtherLTAssets(String symbol, int year) {
		return this.getOtherLTAssets(symbol, year, 4);
	}

	@Override
	public Double getOtherLTLiab(String symbol, int year) {
		return this.getOtherLTLiab(symbol, year, 4);
	}

	@Override
	public Double getOtherOperCash(String symbol, int year) {
		Double yearOO = 0.0;
		for(int i=1;i<5;i++){
			yearOO += this.getOtherOperCash(symbol, year, i);
		}
		return yearOO;
	}

	@Override
	public Double getOtherOperExp(String symbol, int year) {
		Double yearOOE = 0.0;
		for(int i=1;i<5;i++){
			yearOOE += this.getOtherOperExp(symbol, year, i);
		}
		return yearOOE;
	}

	@Override
	public Double getOtherCurLiab(String symbol, int year) {
		return this.getOtherCurLiab(symbol, year, 4);
	}

	@Override
	public Double getPPNE(String symbol, int year) {
		return this.getPPNE(symbol, year, 4);
	}

	@Override
	public Double getPreferredStock(String symbol, int year) {
		return this.getPreferredStock(symbol, year, 4);
	}

	@Override
	public Double getRetainedEarning(String symbol, int year) {
		return this.getRetainedEarning(symbol, year, 4);
	}

	@Override
	public Double getRevenue(String symbol, int year) {
		return this.getYearlyIncomeStatement(symbol, year).getRevenue();
	}

	@Override
	public Double getRND(String symbol, int year) {
		return this.getYearlyIncomeStatement(symbol, year).getRD();
	}

	@Override
	public Double getSGNA(String symbol, int year) {
		Double yearSGNA = 0.0;
		for(int i=1;i<5;i++){
			yearSGNA += this.getSGNA(symbol, year, i);
		}
		return yearSGNA;
	}

	@Override
	public Double getCurDebt(String symbol, int year) {
		return this.getCurDebt(symbol, year, 4);
	}

	@Override
	public Double getCurInv(String symbol, int year) {
		return this.getCurInv(symbol, year, 4);
	}

	@Override
	public Double getStockSalePur(String symbol, int year) {
		Double yearSSP = 0.0;
		for(int i=1;i<5;i++){
			yearSSP += this.getStockSalePur(symbol, year, i);
		}
		return yearSSP;
	}

	@Override
	public Double getTotalAssets(String symbol, int year) {
		return this.getYearlyBalanceStatement(symbol, year).getAssets();
	}

	@Override
	public Double getTotalCurAssets(String symbol, int year) {
		return this.getYearlyBalanceStatement(symbol, year).getCurAssets();
	}

	@Override
	public Double getTotalCurLiab(String symbol, int year) {
		return this.getYearlyBalanceStatement(symbol, year).getCurLiab();
	}

	@Override
	public Double getTotalEquity(String symbol, int year) {
		return this.getTotalEquity(symbol, year, 4);
	}

	@Override
	public Double getTotalLiab(String symbol, int year) {
		return this.getYearlyBalanceStatement(symbol, year).getLiab();
	}

	@Override
	public Double getTotalLiabNEquiv(String symbol, int year) {
		return this.getTotalLiabNEquiv(symbol, year, 4);
	}

	@Override
	public Double getTotalOperExp(String symbol, int year) {
		return this.getYearlyIncomeStatement(symbol, year).getTotalOperExp();
	}

	@Override
	public Double getTotalOtherIncome(String symbol, int year) {
		Double yearTOI = 0.0;
		for(int i=1;i<5;i++){
			yearTOI += this.getTotalOtherIncome(symbol, year, i);
		}
		return yearTOI;
	}

	@Override
	public Double getTreasuryStock(String symbol, int year) {
		return this.getTreasuryStock(symbol, year, 4);
	}
	
	@Override
	public Double getShares(String symbol, int year) {
		if(this.getYearlyIncomeStatement(symbol, year).getShares()>=0.0){
		return this.getYearlyIncomeStatement(symbol, year).getShares();
		}
		else return null;
	}
	

	@Override
	public Double getFloats(String symbol, int year, int quarter) {
		return this.getIncomeStatement(symbol,year,quarter).getFloats();
	}

	@Override
	public Double getShares(String symbol, int year, int quarter) {
		return this.getIncomeStatement(symbol,year,quarter).getShares();
	}

	/*Valuation Ratios*/
	@Override
	public Double getAssetTurnover(String symbol, int year) {
		return this.getRevenueRate(symbol, year)/this.getTotalAssets(symbol, year);
	}

	@Override
	public Double getCOGSRate(String symbol, int year) {
		return this.getCOGSRate(symbol, year)/this.getRevenue(symbol, year);
	}

	@Override
	public Double getEBTMargin(String symbol, int year) {
		return this.getEarningBeforeTax(symbol, year)/this.getRevenue(symbol, year);
	}

	@Override
	public Double getFinancialLeverage(String symbol, int year) {
		return this.getTotalAssets(symbol, year)/this.getTotalEquity(symbol, year);
	}

	@Override
	public Double getGrossMargin(String symbol, int year) {
		return 1.0-this.getCOGSRate(symbol, year);
	}

	@Override
	public Double getIntExpRate(String symbol, int year) {
		return this.getIntExp(symbol, year)/this.getRevenue(symbol, year);
	}

	@Override
	public Double getNetMargin(String symbol, int year) {
		return this.getNetIncome(symbol, year)/this.getRevenue(symbol, year);
	}

	@Override
	public Double getOperMargin(String symbol, int year) {
		return this.getOperatingIncome(symbol, year)/this.getRevenue(symbol, year);
	}

	@Override
	public Double getReturnOnAsset(String symbol, int year) {
		return this.getNetIncome(symbol, year)/this.getTotalAssets(symbol, year);
	}

	@Override
	public Double getReturnOnEquity(String symbol, int year) {
		return this.getNetIncome(symbol, year)/this.getTotalEquity(symbol, year);
	}

	@Override
	public Double getRevenueRate(String symbol, int year) {
		return 1.0;
	}

	@Override
	public Double getRNDRate(String symbol, int year) {
		return this.getRND(symbol, year)/this.getRevenue(symbol, year);
	}

	@Override
	public Double getSGNARate(String symbol, int year) {
		return this.getSGNA(symbol, year)/this.getRevenue(symbol, year);
	}

	@Override
	public Double getTaxRate(String symbol, int year) {
		return this.getIncomeTax(symbol, year)/this.getEarningBeforeTax(symbol, year);
	}
	
	@Override
	public Double getFreeCashPerNetInc(String symbol, int year) {
		return this.getFreeCash(symbol, year)/this.getNetIncome(symbol, year);
	}

	@Override
	public Double getCapExpPerSales(String symbol, int year) {
		return this.getCapExp(symbol, year)/this.getRevenue(symbol, year);
	}

	@Override
	public Double getFreeCashPerSales(String symbol, int year) {
		return this.getFreeCash(symbol, year)/this.getRevenue(symbol, year);
	}

	@Override
	public Double getEPS(String symbol, int year) {
		try{
		return this.getNetIncome(symbol, year)/this.getShares(symbol,year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getEPS(String symbol, int year, int quarter) {
		com.lti.service.bo.IncomeStatement is = this.getIncomeStatement(symbol,year,quarter);
		try{
			if(is.getShares()>=0.0)
			return this.getNetIncome(symbol, year, quarter)/is.getShares();
			else return null;
		}catch(Exception e){
			return null;
		}
	}

	/*count:the number of report quarters*/
	@Override
	public Double getEPS(String symbol, int count, Date date) {
		List<IncomeStatement>IsList = this.getRecentIncomeStatement(symbol, count, date);
		if(IsList!=null){
		Double netInc = 0.0;
		for(int i=0;i<IsList.size();i++){
			netInc += this.getNetIncome(symbol, IsList.get(i).getYear(), IsList.get(i).getQuarter());
		}
		Double EPS = netInc/IsList.get(IsList.size()-1).getShares();
		return EPS;
		}
		else return null;
	}
    
	@Override
	public Double getTotLiabToTotAsset(String symbol, int year, int quarter) {
		try{
			BalanceStatement b=this.getBalanceStatement(symbol,year,quarter);
			return b.getLiab()/b.getAssets();
		}catch(Exception e){
			return null;
		}
	}

	//get the mean total liabilities to total assets ratio by the given symbol set 
	@Override
	public Double getTotLiabToTotAsset(List<String> symbols, int year, int quarter) {
		Double mean=0.0;
		int count=0;
		for(int i=0;i<symbols.size();i++){
			if(this.getTotLiabToTotAsset(symbols.get(i), year, quarter)!=null){
			mean+=this.getTotLiabToTotAsset(symbols.get(i), year, quarter);
			count+=1;
			}
		}		
		if(count>0){
			mean=mean/count;
			return mean;
			}
		else return 0.0;
	}

	@Override
	public Double getLongTermDebtToTotCap(String symbol, int year, int quarter) {
		com.lti.service.bo.BalanceStatement bs = financialStatementManager.getBalanceStatement(symbol, year, quarter);
		try{
			if((bs.getLongTermDebt()+bs.getPreferredStock()+bs.getCommonStock())!=0.0)
			return bs.getLongTermDebt()/(bs.getLongTermDebt()+bs.getPreferredStock()+bs.getCommonStock());
			else return 0.0;
		}catch(Exception e){
		return null;
		}
	}

	@Override
	public Double getLongTermDebtToTotCap(List<String> symbols, int year, int quarter) {
		Double mean=0.0;
		int count=0;
		for(int i=0;i<symbols.size();i++){
			if(this.getLongTermDebtToTotCap(symbols.get(i), year, quarter)!=null){
				mean+=this.getLongTermDebtToTotCap(symbols.get(i), year, quarter);
				count+=1;
			}
		}
			if(count>0){
				mean=mean/count;
				return mean;
				}
			else return 0.0;
	}

	@Override
	public Double getMarketCap(String symbol, int year, int quarter) {
		SecurityManager sm = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		com.lti.service.bo.IncomeStatement is = financialStatementManager.getIncomeStatement(symbol, year, quarter);
		Security se = sm.get(symbol);
		Date date=LTIDate.getLastDayOfQuarter(LTIDate.getDate(year, quarter*3, 1));
		try{
			//long t1=System.currentTimeMillis();
			Double price = sm.getPrice(se.getID(), date);
			//long t2=System.currentTimeMillis();
			//System.out.println("times:"+(t2-t1));
			return is.getShares()*price;
		}catch(Exception e){
			return 0.0;
		}
	}

	/*****function:get all stocks' market capital values of the quarter*****/
	@Override
	public Map<String, Double> getMarketCap(int year, int quarter) {
		return financialStatementManager.getMarketCap(year,quarter);
	}

	@Override
	public Double getMarketCap(String symbol, int year) {
		Date date = new Date();
		SecurityManager sm = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		com.lti.service.bo.IncomeStatement is = financialStatementManager.getIncomeStatement(symbol,date);
		Security se = sm.get(symbol);
		try{
			Double price = sm.getPrice(se.getID(),date);
			return is.getShares()*price;
		}catch(Exception e){
			return 0.0;
		}	
	}
	
	
	
	/*Growth Rate*/
	@Override
	public Double getYOYOperIncGrowth(String symbol, int year) {
		try{
			Double curYear = this.getOperatingIncome(symbol, year);
			Double lastYear = this.getOperatingIncome(symbol, year-1);
			return (curYear-lastYear)/lastYear;
		}catch (Exception e){
		return null;
		}
	}

	@Override
	public Double getYOYRevenueGrowth(String symbol, int year) {
		try{
			Double curYear = this.getRevenue(symbol, year);
			Double lastYear = this.getRevenue(symbol, year-1);
			return (curYear-lastYear)/lastYear;
		}catch (Exception e){
		return null;
		}
	}

	@Override
	public Double getYOYFreeCashGrowth(String symbol, int year) {
		try{
			Double curYear = this.getFreeCash(symbol, year);
			Double lastYear = this.getFreeCash(symbol, year-1);
			return (curYear-lastYear)/lastYear;
		}catch (Exception e){
		return null;
		}
	}

	@Override
	public Double getYOYOperCashGrowth(String symbol, int year) {
		try{
			Double curYear = this.getOperCash(symbol, year);
			Double lastYear = this.getOperCash(symbol, year-1);
			return (curYear-lastYear)/lastYear;
		}catch (Exception e){
		return null;
		}
	}

	/*Financial Health*/
	@Override
	public Double getCurrentRatio(String symbol, int year) {
		return this.getTotalCurAssets(symbol, year)/this.getTotalCurLiab(symbol, year);
	}

	@Override
	public Double getQuickRatio(String symbol, int year) {
		return (this.getCashNEquiv(symbol, year)+this.getNetRec(symbol, year))/this.getTotalCurLiab(symbol, year);
	}

	/*Financial Report Object*/
	@Override
	public BalanceStatement getBalanceStatement(String symbol, int year,
			int quarter) {
		try{
			return financialStatementManager.getBalanceStatement(symbol, year,quarter);
		}catch(Exception e){
		return null;
		}
	}

	@Override
	public CashFlow getCashFlow(String symbol, int year, int quarter) {
		try{
			return financialStatementManager.getCashFlow(symbol, year,quarter);
		}catch(Exception e){
		return null;
		}
	}

	@Override
	public IncomeStatement getIncomeStatement(String symbol, int year,
			int quarter) {
		try{
			return financialStatementManager.getIncomeStatement(symbol, year,quarter);
		}catch(Exception e){
			//e.printStackTrace();
			System.out.println(StringUtil.getStackTraceString(e));
		return null;
		}
	}

	@Override
	public BalanceStatement getRecentBalanceStatement(String symbol,Date date) {
		try{
			return financialStatementManager.getRecentBalanceStatement(symbol,date);
		}catch(Exception e){
		return null;
		}
	}

	@Override
	public CashFlow getRecentCashFlow(String symbol,Date date) {
		try{
			return financialStatementManager.getRecentCashFlow(symbol,date);
		}catch(Exception e){
		return null;
		}
	}

	@Override
	public IncomeStatement getRecentIncomeStatement(String symbol,Date date) {
		try{
			return financialStatementManager.getRecentIncomeStatement(symbol,date);
		}catch(Exception e){
		return null;
		}
	}
	
	@Override
	public List<BalanceStatement> getRecentBalanceStatement(String symbol,
			int count, Date date) {
		try{
			return financialStatementManager.getRecentBalanceStatement(symbol,count,date);
		}catch(Exception e){
		return null;
		}
	}

	@Override
	public List<CashFlow> getRecentCashFlow(String symbol, int count, Date date) {
		try{
			return financialStatementManager.getRecentCashFlow(symbol,count,date);
		}catch(Exception e){
		return null;
		}
	}

	@Override
	public List<IncomeStatement> getRecentIncomeStatement(String symbol,
			int count, Date date) {
		try{
			return financialStatementManager.getRecentIncomeStatement(symbol,count,date);
		}catch(Exception e){
		return null;
		}
	}


	@Override
	public Company getCompany(String symbol) {
		return financialStatementManager.getCompany(symbol);
	}

	@Override
	public List<Company> getIndustryIndex(String sector, String industry) {
		return financialStatementManager.getIndustrySymbols(sector,industry);
	}

	@Override
	public List<String> getIndustryIndex(String industry) {
		return financialStatementManager.getIndustrySymbols(industry);
		
	}

	@Override
	public YearlyBalanceStatement getYearlyBalanceStatement(String symbol,int year) {
		return financialStatementManager.getYearlyBalanceStatement(symbol,year);
	}

	@Override
	public YearlyCashFlow getYearlyCashFlow(String symbol, int year) {
		return financialStatementManager.getYearlyCashFlow(symbol, year);
	}

	@Override
	public YearlyIncomeStatement getYearlyIncomeStatement(String symbol,int year) {
		return financialStatementManager.getYearlyIncomeStatement(symbol, year);
	}

	
	@Override
	public List<YearlyBalanceStatement> getRecentYearlyBalanceStatement(String symbol, int count,Date CurDate) {
		return financialStatementManager.getRecentYearlyBalanceStatement(symbol, count, CurDate);
	}

	@Override
	public List<YearlyCashFlow> getRecentYearlyCashFlow(String symbol, int count,Date CurDate) {
		return financialStatementManager.getRecentYearlyCashFlow(symbol, count, CurDate);
	}

	@Override
	public List<YearlyIncomeStatement> getRecentYearlyIncomeStatement(String symbol,int count,Date CurDate) {
		return financialStatementManager.getRecentYearlyIncomeStatement(symbol, count, CurDate);
	}

	public static void main(String[]args){
		FinancialStatement fs = new FinancialStatementDatabaseImpl();
		
		long sTime = System.currentTimeMillis();
	    Double price = fs.getPrice("IBM", LTIDate.getDate(2008, 12, 9));
	    System.out.println(price);
		long tTime= System.currentTimeMillis();
		System.out.println("Time: "+(tTime-sTime));

        //Double eps = fs.getEPS("IBM", 6, date);
        //System.out.println(eps);

     
		//System.out.println(fs.getFreeCash("IBM", 2008));
		//System.out.println(fs.getNetIncome("IBM", 2008));
		/*System.out.println(fs.getRetainedEarning("IBM", d));
		System.out.println(fs.getTreasuryStock("IBM", d));
		System.out.println(fs.getOtherEquity("IBM", d));
		System.out.println(fs.getTotalEquity("IBM", d));
		System.out.println(fs.getNetTanAssets("IBM", d));
		System.out.println(fs.getCommonStock("IBM", d));*/

	}

	@Override
	public Map<String, Double[]> getLiabToAsset(int year, int quarter) {
		return financialStatementManager.getLiabToAsset(year, quarter);
	}

	@Override
	public Map<String, Double> getAnnualNetIncome(int year) {
		return financialStatementManager.getAnnualNetInc(year);
	}

	@Override
	public Map<String, String> getIndustryIndex() {
		return financialStatementManager.getIndustrySymbols();
	}

	@Override
	public Map<String, Double> getPrice(Date date) {
		return financialStatementManager.getPrice(date);
	}

	@Override
	public Map<String, Double[]> getBalanceCash(int year, int quarter) {
		return financialStatementManager.getBalanceCash(year,quarter);
	}

	/****  function: return all the stocks' quarterly 5 years eps growth ratios by the given date  ****/
	@Override
	public Map<String, Double> get5YearEPSGrowth(int year) {
		Map<String,Double> map = new HashMap<String, Double>();
		int fiveYearBef = year-5;//get the year that 5 year before;
		
	    Map<String,Double> map1 = this.getAnnualEPS(year);//store the symbol and the corresponding annual eps value by the given year
		Map<String,Double> map2 = this.getAnnualEPS(fiveYearBef);//store the symbol and the corresponding annual eps value 5 years before
		
		try{
		Iterator<String> it = map1.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			//check whether the same symbol has eps value 5 years before
			if(map2.get(key)!=null){
				BaseFormulaManager bfm = (BaseFormulaManager)ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
				Double[] v = new Double[2];
				v[0] = map1.get(key);//the stock eps of the date
				v[1] = map2.get(key);//the stock eps of the date 5 years before
			    Double growth = bfm.computeEPSGrowth(v[1],v[0],5);
				map.put(key, growth);
				System.out.println(key+":"+v[0]+";"+v[1]+";"+growth);
			}
		}
		}catch(Exception e){
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return map;
	}

	@Override
	public Map<String, Double> getEPS(Date date) {
		return financialStatementManager.getEPS(date);
	}

	@Override
	public Map<String, Double> getAnnualEPS(int year) {
		return financialStatementManager.getAnnualEPS(year);
	}

	@Override
	public Map<String, Double> getEPSGrowth(Date date) {
		Map<String,Double> map = new HashMap<String, Double>();
		Date date1 = LTIDate.getNewDate(date, TimeUnit.YEARLY, -1);
		Map<String,Double> map1 = this.getEPS(date);
		Map<String,Double> map2 = this.getEPS(date1);
		try{
			Iterator<String> it = map1.keySet().iterator();
			while(it.hasNext()){
				String key = it.next();
				if(map2.get(key)!=null){
					Double[] v = new Double[2];
					v[0] = map1.get(key);//the stock eps of the date
					v[1] = map2.get(key);//the stock eps of the date 5 years before
				    Double growth = (v[0]-v[1])/Math.abs(v[1]);
					map.put(key, growth);
					System.out.println(key+":"+v[0]+";"+v[1]+";"+growth);
				}
			}
			}catch(Exception e){
				System.out.println(StringUtil.getStackTraceString(e));
			}
		return map;
	}

	@Override
	public Map<String, Double> getFloats(Date date) {
		return financialStatementManager.getAllFloats(date);
	}

	@Override
	public Map<String, Double> getInstitutionHolder(Date date) {
		return financialStatementManager.getAllInstitutionHolders(date);
	}

	@Override
	public Map<String, Double> getRSGrade(Date date) {
		return financialStatementManager.getRSGrade(date);
	}

	@Override
	public Double getPrice(String symbol, Date date) {
		Map<String,Double>prices = this.getPrice(date);
		try{
			return prices.get(symbol);
		}catch(Exception e){
			
		}
		return null;
	}

	@Override
	public Double get5YearEPS(String symbol,int year) {
		int fiveYearBef = year-5;//get the year that 5 year before;
		Double eps1 = this.getEPS(symbol, year);
		Double eps2 = this.getEPS(symbol, fiveYearBef);
		BaseFormulaManager bfm = (BaseFormulaManager)ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
		try{
			Double growth = bfm.computeEPSGrowth(eps2, eps1, 5);
			return growth;
		}catch(Exception e){
		    return null;
		}
	}

	@Override
	public Double getInstitutionHolder(String symbol, int year, int quarter) {
		return this.getIncomeStatement(symbol, year, quarter).getInstitutionHolder();
	}

	@Override
	public Double getRSGrade(String symbol, Date date) {
		SecurityManager sm = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		return sm.getRSGrade(symbol, 5, date);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
