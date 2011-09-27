package com.lti.service;

import java.util.*;

import com.lti.service.bo.BalanceStatement;
import com.lti.service.bo.CashFlow;
import com.lti.service.bo.IncomeStatement;
import com.lti.service.bo.RelativeStrength;
import com.lti.type.PaginationSupport;

/**
 * @author Xie
 *
 */
public interface FinancialStatementManager {	
	
	public com.lti.service.bo.CashFlow getCashFlow(String symbol, Date date);
	
	public com.lti.service.bo.CashFlow getCashFlow(String symbol,int year,int quarter);

	public com.lti.service.bo.BalanceStatement getBalanceStatement(String symbol, Date date);
	
	public com.lti.service.bo.BalanceStatement getBalanceStatement(String symbol,int year,int quarter);

	public com.lti.service.bo.IncomeStatement getIncomeStatement(String symbol, Date date);
	
	public com.lti.service.bo.IncomeStatement getIncomeStatement(String symbol,int year,int quarter);

	public void updateCashFlow(com.lti.service.bo.CashFlow cashFlow);

	public void updateBalanceStatement(com.lti.service.bo.BalanceStatement balanceStatement);

	public void updateIncomeStatement(com.lti.service.bo.IncomeStatement incomeStatement);


	void removeCashFlow(long id);

	com.lti.service.bo.CashFlow getCashFlow(Long id);

	Long saveCashFlow(com.lti.service.bo.CashFlow g);

	void removeBalanceStatement(long id);

	com.lti.service.bo.BalanceStatement getBalanceStatement(Long id);

	Long saveBalanceStatement(com.lti.service.bo.BalanceStatement g);

	void removeIncomeStatement(long id);

	com.lti.service.bo.IncomeStatement getIncomeStatement(Long id);

	Long saveIncomeStatement(com.lti.service.bo.IncomeStatement g);

	void downloadCashFlow(String symbol, Date date);
	
	void downloadCashFlow(String symbol,int year,int quarter);

	void downloadBalanceStatement(String symbol, Date date);
	
	void downloadBalanceStatement(String symbol,int year,int quarter);

	void downloadIncomeStatement(String symbol, Date date);
	
	void downloadIncomeStatement(String symbol,int year,int quarter);
			
	void downloadFinancialStatement(String symbol,Date date);
	
	void downloadFinancialStatement(String symbol,int year,int quarter);
	
	void downloadFinancialStatement(String[]symbol,int year,int quarter);
	
	/*Company Sector and Industry*/
	
	public com.lti.service.bo.Company getCompany(String symbol);
		
	public void updateCompany(com.lti.service.bo.Company company);
	
	void removeCompany(long id);
	
	Long saveCompany(com.lti.service.bo.Company company);
	
	void downloadCompany(String symbol);
	
	/*get most recent financial statement*/
	
	public com.lti.service.bo.BalanceStatement getRecentBalanceStatement(String symbol,Date date); 
	
	public com.lti.service.bo.IncomeStatement getRecentIncomeStatement(String symbol,Date date);
	
	public com.lti.service.bo.CashFlow getRecentCashFlow(String symbol,Date date);
	
	/*get most recent count financial statement*/
	
	public List<com.lti.service.bo.BalanceStatement> getRecentBalanceStatement(String symbol,int count,Date date); 
	
	public List<com.lti.service.bo.IncomeStatement> getRecentIncomeStatement(String symbol,int count,Date date);
	
	public List<com.lti.service.bo.CashFlow> getRecentCashFlow(String symbol,int count,Date date);	
	
	public List<com.lti.service.bo.Company> getIndustrySymbols(String sector,String industry);
	
	public List<String> getIndustrySymbols(String industry);
	
	public Map<String,String>getIndustrySymbols();
	
	public RelativeStrength getRelativeStrength(String symbol);
	
	/**********************************************************************************/
	
	public PaginationSupport getIncomeStatement(int pageSize,int startIndex);
	
	public PaginationSupport getBalanceStatement(int pageSize,int startIndex);
	
	public PaginationSupport getCashFlow(int pageSize,int startIndex);
	
	public List<IncomeStatement> getIncomeStatement(String symbol);
	
	/***********************************************************************************/

	public com.lti.service.bo.YearlyCashFlow getYearlyCashFlow(String symbol,int year);
	
	public com.lti.service.bo.YearlyBalanceStatement getYearlyBalanceStatement(String symbol,int year);
	
	public com.lti.service.bo.YearlyIncomeStatement getYearlyIncomeStatement(String symbol,int year);
	
	public List<com.lti.service.bo.YearlyIncomeStatement> getRecentYearlyIncomeStatement(String symbol,int count,Date CurDate);
	
	public List<com.lti.service.bo.YearlyCashFlow> getRecentYearlyCashFlow(String symbol,int count,Date CurDate);
	
	public List<com.lti.service.bo.YearlyBalanceStatement> getRecentYearlyBalanceStatement(String symbol,int count,Date CurDate);

	public void updateYearlyCashFlow(com.lti.service.bo.YearlyCashFlow yearlyCashFlow);

	public void updateYearlyBalanceStatement(com.lti.service.bo.YearlyBalanceStatement yearlyBalanceStatement);

	public void updateYearlyIncomeStatement(com.lti.service.bo.YearlyIncomeStatement yearlyIncomeStatement);
	
	void removeYearlyCashFlow(long id);

	com.lti.service.bo.YearlyCashFlow getYearlyCashFlow(Long id);

	Long saveYearlyCashFlow(com.lti.service.bo.YearlyCashFlow g);

	void removeYearlyBalanceStatement(long id);

	com.lti.service.bo.YearlyBalanceStatement getYearlyBalanceStatement(Long id);

	Long saveYearlyBalanceStatement(com.lti.service.bo.YearlyBalanceStatement g);

	void removeYearlyIncomeStatement(long id);

	com.lti.service.bo.YearlyIncomeStatement getYearlyIncomeStatement(Long id);

	Long saveYearlyIncomeStatement(com.lti.service.bo.YearlyIncomeStatement g);
	
	void downloadYearlyIncomeStatement(String symbol,int year);
	
	void downloadYearlyBalanceStatement(String symbol,int year);
	
	void downloadYearlyCashFlow(String symbol,int year);
	
	/**********************************************************************************/
	void downloadIncomeStatement(String symbol);
	
	void downloadBalanceStatement(String symbol);
	
	void downloadCashFlow(String symbol);
	
	void downloadYearlyIncomeStatement(String symbol);
	
	void downloadYearlyBalanceStatement(String symbol);
	
	void downloadYearlyCashFlow(String symbol);
	
	void downloadFinancialStatement(String symbol);
	
	void downloadFinancialStatement(String[]symbols);
	
	void downloadYearlyFinancialStatement(String symbol);
	
	void downloadYearlyFinancialStatement(String[]symbols);
	
	void loadBalanceStatement(String symbol);
	
	void loadCashFlow(String symbol);
	
	void loadIncomeStatement(String symbol);
	
	void loadFinancialStatement(String symbol);
	
	void checkBICStatement(String symbol,String statementtype);
	
	void checkBICStatement(SecurityManager securityManager,List<String> list);
	
	String chkBICStatement(String symbol,String statementtype);
	
	String chkYBICStatement(String symbol,String statementtype);
	
	Set<String> chkBICStatement(SecurityManager securityManager,List<String> list);
	
	/*********************access the database using the sql statements******************************/
	
	public Map<String,Double[]> getLiabToAsset(int year,int quarter);
	
	public Map<String,Double> getNetIncome(Date sDate,Date eDate);
	
	public Map<String,Double> getAnnualNetInc(int year);
	
	public Map<String,Double> getMarketCap(int year,int quarter);
	
	public Map<String,Double> getPrice(Date date);
	
	public Map<String,Double[]> getBalanceCash(int year,int quarter);
	
	public Map<String,Double> getEPS(Date date);
	
	public Map<String,Double> getAllFloats(Date date);
	
	public Map<String,Double> getAllInstitutionHolders(Date date);
	
	public Map<String,Double> getRSGrade(Date date);
	
	public Map<String,Double> getAnnualEPS(int year);
}
