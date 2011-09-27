package com.lti.FinancialStatement;

import java.util.Date;
import java.util.Map;

import java.util.*;

import com.lti.util.LTIDate;


/**
 * @author Xie
 *
 */
public interface FinancialStatement {
	
/*Income Statement fields*/
	/**
	     * get the revenue of the given symbol and the quarter of the given date
		 * @param symbol
		 * @param date
		 * @return Revenue
	 */
	public java.lang.Double getRevenue(String symbol,Date date) ;
	
/**
	 * get the revenue of the given symbol and quarter
	 * @param symbol
	 * @param year
     * @param quarter
	 * @return revenue
 */
	public java.lang.Double getRevenue(String symbol,int year,int quarter) ;
	
    /**
     * get the revenue by the given symbol and year
     * @param symbol
	 * @param year
	 * @return revenue
     */
	public java.lang.Double getRevenue(String symbol,int year);
	
	/**
	 * get the Cost of Goods Sold of the given symbol and the quarter of the given date
	 * @param symbol
	 * @param date
	 * @return COGS
	 */
	public java.lang.Double getCOGS(String symbol,Date date) ;

	/**
		 * get the Cost of Goods Sold of the given symbol and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return COGS
	 */
	public java.lang.Double getCOGS(String symbol,int year,int quarter) ;
	/**
	 * get the Cost of Goods Sold by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return COGS
    */
	public java.lang.Double getCOGS(String symbol,int year) ;
	/**
		 * get the Selling General and Administrative of the given symbol and the quarter of the given date
		 * @param symbol
		 * @param date
		 * @return SGNA
	 */
	public java.lang.Double getSGNA(String symbol,Date date) ;

	/**
		 * get the Selling General and Administrative of the given symbol and date
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return SGNA
	 */
	public java.lang.Double getSGNA(String symbol,int year,int quarter) ;
	/**
	 * get the Selling General and Administrative by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return SGNA
     */
	public java.lang.Double getSGNA(String symbol,int year) ;
	/**
		 * get the value of Research Development by the given symbol and date
		 * @param symbol
		 * @param date
		 * @return RND
	 */
	public java.lang.Double getRND(String symbol,Date date) ;

	/**
		 * get the value of Research Development by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return RND
	 */
	public java.lang.Double getRND(String symbol,int year,int quarter) ;
	/**
	 * get the value of Research Development by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return RND
     */	
	public java.lang.Double getRND(String symbol,int year) ;
	
	/**
		 * get the value of Other Operating Expenses by the given symbol and date 
		 * @param symbol
		 * @param date
		 * @return OtherOperExp
	 */
	public java.lang.Double getOtherOperExp(String symbol,Date date) ;

	/**
		 * get the value of Other Operating Expenses by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return OtherOperExp
	 */
	public java.lang.Double getOtherOperExp(String symbol,int year,int quarter) ;
	/**
	 * get the value of Other Operating Expenses by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return OtherOperExp
     */	
	public java.lang.Double getOtherOperExp(String symbol,int year) ;
	/**
		 * get the value of Total Other Income by the given symbol and date 
		 * @param symbol
	     * @param date
		 * @return TotalOtherInc
	 */
	public java.lang.Double getTotalOtherIncome(String symbol,Date date);
	
	/**
		 * get the value of Total Other Income by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return TotalOtherInc
	 */
	public java.lang.Double getTotalOtherIncome(String symbol,int year,int quarter);
	/**
	 * get the value of Total Other Income by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return TotalOtherInc
     */
	public java.lang.Double getTotalOtherIncome(String symbol,int year);
	/**
		 * get the value of Interest Expense by the given symbol and date 
		 * @param symbol
		 * @param date
		 * @return IntExp
	 */
	public java.lang.Double getIntExp(String symbol,Date date) ;

	/**
		 * get the value of Interest Expense by the given symbol,year and quarter
		 * @param symbol
		 * @param year
		 * @return IntExp
	 */
	public java.lang.Double getIntExp(String symbol,int year,int quarter) ;
	/**
	 * get the value of Interest Expense by the given symbol and year
	 * @param symbol
	 * @param year
     * @param quarter
	 * @return IntExp
     */	
	public java.lang.Double getIntExp(String symbol,int year);
	/**
		 * get the value of Income Tax by the given symbol and date 
		 * @param symbol
	     * @param date
		 * @return IncomeTax
	 */
	public java.lang.Double getIncomeTax(String symbol,Date date) ;

	/**
		 * get the value of Income Tax by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return IncomeTax
	 */
	public java.lang.Double getIncomeTax(String symbol,int year,int quarter) ;
	/**
	 * get the value of Income Tax by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return IncomeTax
     */
	public java.lang.Double getIncomeTax(String symbol,int year) ;
	/**
		 * get the value of Discontinued Operations by the given symbol and date 
		 * @param symbol
	     * @param date
		 * @return DisOper
	 */
	public java.lang.Double getDiscOper(String symbol,Date date) ;

	/**
		 * get the value of Discontinued Operations by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return DisOper
	 */
	public java.lang.Double getDiscOper(String symbol,int year,int quarter) ;
	/**
	 * get the value of Discontinued Operations by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return DisOper
 */
	public java.lang.Double getDiscOper(String symbol,int year) ;
	/**
		 * get the value of Extra Items by the given symbol and date 
		 * @param symbol
	     * @param date
		 * @return ExtItem
	 */
	public java.lang.Double getExtItem(String symbol,Date date) ;

	/**
		 * get the value of Extra Items by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return ExtItem
	 */
	public java.lang.Double getExtItem(String symbol,int year,int quarter) ;
	/**
	 * get the value of Extra Items by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return ExtItem
     */	
	public java.lang.Double getExtItem(String symbol,int year) ;
	/**
		 * get the value of Accounting Changes by the given symbol and date 
		 * @param symbol
	     * @param date
		 * @return AcctChange
	 */
	public java.lang.Double getAcctChange(String symbol,Date date) ;

	/**
		 * get the value of Accounting Changes by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return AcctChange
	 */
	public java.lang.Double getAcctChange(String symbol,int year,int quarter) ;
	/**
	 * get the value of Accounting Changes by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return AcctChange
     */
	public java.lang.Double getAcctChange(String symbol,int year) ;
	
	public java.lang.Double getShares(String symbol,int year);
	
	public java.lang.Double getShares(String symbol,int year,int quarter);
	
	public java.lang.Double getFloats(String symbol,int year,int quarter);
	
	/*Income Statement calculations*/
	
	/**
	     * get the value of Gross Profit by the given symbol and date 
		 * @param symbol
	     * @param date
	     * @return GrossProfit
	 */
	public java.lang.Double getGrossProfit(String symbol,Date date);
	
	/**
		 * get the value of Gross Profit by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return GrossProfit
	 */
	public java.lang.Double getGrossProfit(String symbol,int year,int quarter);
	/**
	 * get the value of Gross Profit by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return GrossProfit
     */	
	public java.lang.Double getGrossProfit(String symbol,int year);
	/**
		 * get the value of Total Operating Expenses by the given symbol and date 
		 * @param symbol
	     * @param date
		 * @return TotalOperExp
	 */
	
	public java.lang.Double getTotalOperExp(String symbol,Date date);
	
	/**
		 * get the value of Total Operating Expenses by the given symbol,year and quarter 
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return TotalOperExp
	 */
	public java.lang.Double getTotalOperExp(String symbol,int year,int quarter);
	/**
	 * get the value of Total Operating Expenses by the given symbol and year 
	 * @param symbol
	 * @param year
	 * @return TotalOperExp
     */
	public java.lang.Double getTotalOperExp(String symbol,int year);
	
	/**
		 * get the value of Operating Income by the given symbol and date 
		 * @param symbol
	     * @param date
		 * @return OperInc
	 */
	public java.lang.Double getOperatingIncome(String symbol, Date date);
	/**
		 * get the value of Operating Income by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return OperInc
	 */
	public java.lang.Double getOperatingIncome(String symbol, int year,int quarter);
	/**
	 * get the value of Operating Income by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return OperInc
     */	
	public java.lang.Double getOperatingIncome(String symbol, int year);
	
	/**
		 * get the value of Earnings Before Interest And Taxes by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return EarningBeforeInt
	 */
	public java.lang.Double getEarningBeforeInt(String symbol, Date date);

	/**
		 * get the value of Earnings Before Interest And Taxes by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return EarningBeforeInt
	 */
	public java.lang.Double getEarningBeforeInt(String symbol, int year,int quarter);
	/**
	 * get the value of Earnings Before Interest And Taxes by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return EarningBeforeInt
     */
	public java.lang.Double getEarningBeforeInt(String symbol, int year);
	
	/**
		 * get the value of Earnings Before Tax by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return EarningBeforeTax
	 */
	public java.lang.Double getEarningBeforeTax(String symbol, Date date);

	/**
		 * get the value of Earnings Before Tax by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return EarningBeforeTax
	 */
	public java.lang.Double getEarningBeforeTax(String symbol, int year,int quarter);
	/**
	 * get the value of Earnings Before Tax by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return EarningBeforeTax
     */
	public java.lang.Double getEarningBeforeTax(String symbol, int year);
	
	/**
		 * get the value of Earnings After Tax by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return EarningAfterTax
	 */
	public java.lang.Double getEarningAfterTax(String symbol, Date date);

	/**
		 * get the value of Earnings After Tax by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return EarningAfterTax
	 */
	public java.lang.Double getEarningAfterTax(String symbol, int year,int quarter);
	/**
	 * get the value of Earnings After Tax by the given symbol and year
	 * @param symbol
	 * @param year
     * @param quarter
	 * @return EarningAfterTax
     */	
	public java.lang.Double getEarningAfterTax(String symbol, int year);
	
	/**
		 * get the value of Net Income by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return NetIncome
	 */
	public java.lang.Double getNetIncome(String symbol, Date date);

	/**
		 * get the value of Net Income by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return NetIncome
	 */
	public java.lang.Double getNetIncome(String symbol, int year,int quarter);
	/**
	 * get the value of Net Income by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return NetIncome
     */
	public java.lang.Double getNetIncome(String symbol, int year);
	
	public Map<String,Double>getNetIncome(Date sDate,Date eDate);
	
	public Map<String,Double>getAnnualNetIncome(int year);
	
	/*CashFlow fields*/
	
	/**
	     * get the value of Depreciation by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return Depr
	 */
	public java.lang.Double getDepr(String symbol,Date date);

	/**
		 * get the value of Depreciation by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return Depr
	 */
	public java.lang.Double getDepr(String symbol,int year,int quarter);
	/**
	 * get the value of Depreciation by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return Depr
     */
	public java.lang.Double getDepr(String symbol,int year);
	/**
		 * get the value of Adjustments To Net Income by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return AdjNetInc
	 */
	public java.lang.Double getAdjNetInc(String symbol,Date date);

	/**
		 * get the value of Adjustments To Net Income by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return AdjNetInc
	 */
	public java.lang.Double getAdjNetInc(String symbol,int year,int quarter);
	/**
	 * get the value of Adjustments To Net Income by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return AdjNetInc
     */
	public java.lang.Double getAdjNetInc(String symbol,int year);
	/**
		 * get the value of Other Operating Cash by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return OtherOperCash
	 */ 
	public java.lang.Double getOtherOperCash(String symbol,Date date) ;

	/**
		 * get the value of Other Operating Cash by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return OtherOperCash
	 */
	public java.lang.Double getOtherOperCash(String symbol,int year,int quarter) ;
	/**
	 * get the value of Other Operating Cash by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return OtherOperCash
     */
	public java.lang.Double getOtherOperCash(String symbol,int year) ;
	/**
		 * get the value of Capital Expenditures by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return CapExp
	 */
	public java.lang.Double getCapExp(String symbol,Date date) ;

	/**
		 * get the value of Capital Expenditures by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return CapExp
	 */
	public java.lang.Double getCapExp(String symbol,int year,int quarter) ;
	/**
	 * get the value of Capital Expenditures by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return CapExp
     */
	public java.lang.Double getCapExp(String symbol,int year) ;
	/**
		 * get the value of Investment by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return Investment
	 */
	public java.lang.Double getInvestment(String symbol,Date date) ;

	/**
		 * get the value of Investment by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return Investment
	 */
	public java.lang.Double getInvestment(String symbol,int year,int quarter) ;
	/**
	 * get the value of Investment by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return Investment
     */
	public java.lang.Double getInvestment(String symbol,int year) ;
	/**
		 * get the value of Other Investing Cash by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return OtherInvCash
	 */
	public java.lang.Double getOtherInvCash(String symbol,Date date) ;

	/**
		 * get the value of Other Investing Cash by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return OtherInvCash
	 */
	public java.lang.Double getOtherInvCash(String symbol,int year,int quarter) ;
	/**
	 * get the value of Other Investing Cash by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return OtherInvCash
     */	
	public java.lang.Double getOtherInvCash(String symbol,int year) ;
	/**
		 * get the value of Dividends by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return Dividends
	 */
	public java.lang.Double getDividends(String symbol,Date date) ;

	/**
		 * get the value of Dividends by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return Dividends
	 */
	public java.lang.Double getDividends(String symbol,int year,int quarter) ;
	/**
	 * get the value of Dividends by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return Dividends
     */
	public java.lang.Double getDividends(String symbol,int year) ;
	/**
		 * get the value of Sale Purchase of Stock by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return StockSalePur
	 */
	public java.lang.Double getStockSalePur(String symbol,Date date) ;

	/**
		 * get the value of Sale Purchase of Stock by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return StockSalePur
	 */
	public java.lang.Double getStockSalePur(String symbol,int year,int quarter) ;
	/**
	 * get the value of Sale Purchase of Stock by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return StockSalePur
     */
	public java.lang.Double getStockSalePur(String symbol,int year) ;
	/**
		 * get the value of Net Borrowings by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return NetBorrowing
	 */
	public java.lang.Double getNetBorrowing(String symbol,Date date);

	/**
		 * get the value of Net Borrowings by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return NetBorrowing
	 */
	public java.lang.Double getNetBorrowing(String symbol,int year,int quarter);
	/**
	 * get the value of Net Borrowings by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return NetBorrowing
     */
	public java.lang.Double getNetBorrowing(String symbol,int year);
	/**
		 * get the value of Other Financing Cash by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return OtherFinCash
	 */
	public java.lang.Double getOtherFinCash(String symbol,Date date) ;

	/**
		 * get the value of Other Financing Cash by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return OtherFinCash
	 */
	public java.lang.Double getOtherFinCash(String symbol,int year,int quarter) ;
	/**
	 * get the value of Other Financing Cash by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return OtherFinCash
     */
	public java.lang.Double getOtherFinCash(String symbol,int year) ;
	/**
		 * get the value of Currency Adjustment by the given symbol and date
		 * @param symbol
	     * @param date
		 * @return CurrencyAdj
	 */
	public java.lang.Double getCurrencyAdj(String symbol,Date date);
	
	/**
		 * get the value of Currency Adjustment by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return CurrencyAdj
	 */
	public java.lang.Double getCurrencyAdj(String symbol,int year,int quarter);
	/**
	 * get the value of Currency Adjustment by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return CurrencyAdj
     */
	public java.lang.Double getCurrencyAdj(String symbol,int year);
	
	/*Cash Flow calculation*/
	
	/**
	    * get the value of Operating Cash by the given symbol and date
	    * @param symbol
        * @param date
	    * @return OperCash
    */
	public java.lang.Double getOperCash(String symbol, Date date);

	/**
		 * get the value of Operating Cash by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return OperCash
	 */
	public java.lang.Double getOperCash(String symbol, int year,int quarter);
	/**
	 * get the value of Operating Cash by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return OperCash
     */
	public java.lang.Double getOperCash(String symbol, int year);
	/**
		 * get the value of Investing Cash by the given symbol and date
		 * @param symbol
         * @param date
		 * @return InvCash
	 */
	public java.lang.Double getInvCash(String symbol, Date date);

	/**
		 * get the value of Investing Cash by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return InvCash
	 */
	public java.lang.Double getInvCash(String symbol, int year,int quarter);
	/**
	 * get the value of Investing Cash by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return InvCash
     */
	public java.lang.Double getInvCash(String symbol, int year);
	/**
		 * get the value of Financing Cash by the given symbol and date
		 * @param symbol
         * @param date
		 * @return FinCash
	 */
	public java.lang.Double getFinCash(String symbol, Date date);

	/**
		 * get the value of Financing Cash by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return FinCash
	 */
	public java.lang.Double getFinCash(String symbol, int year,int quarter);
	/**
	 * get the value of Financing Cash by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return FinCash
     */
	public java.lang.Double getFinCash(String symbol, int year);
	/**
		 * get the value of Free Cash by the given symbol and date
		 * @param symbol
         * @param date
		 * @return FreeCash
	 */
	public java.lang.Double getFreeCash(String symbol, Date date);

	/**
		 * get the value of Free Cash by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return FreeCash
	 */
	public java.lang.Double getFreeCash(String symbol,int year,int quarter);
	/**
	 * get the value of Free Cash by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return FreeCash
     */
	public java.lang.Double getFreeCash(String symbol,int year);
	/**
		 * get the value of Change In Cash by the given symbol and date
		 * @param symbol
         * @param date
		 * @return ChangInCash
	 */
	public java.lang.Double getChangInCash(String symbol, Date date);
	
	/**
		 * get the value of Change In Cash by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return ChangInCash
	 */
	public java.lang.Double getChangInCash(String symbol,int year,int quarter);
	/**
	 * get the value of Change In Cash by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return ChangInCash
     */
	public java.lang.Double getChangInCash(String symbol,int year);
	
	/*Balance Statement fields*/
	/**
	 * get the value of Cash And Cash Equivalents by the given symbol and date
	 * @param symbol
     * @param date
	 * @return CashNEquiv
    */	
	public java.lang.Double getCashNEquiv(String symbol,Date date) ;

	/**
		 * get the value of Cash And Cash Equivalents by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return CashNEquiv
	 */
	public java.lang.Double getCashNEquiv(String symbol,int year,int quarter) ;
	/**
	 * get the value of Cash And Cash Equivalents by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return CashNEquiv
     */	
	public java.lang.Double getCashNEquiv(String symbol,int year) ;
	
	/**
		 * get the value of Short Term Investments by the given symbol and date
		 * @param symbol
         * @param date
		 * @return ShortTermInv
	 */
	public java.lang.Double getCurInv(String symbol,Date date) ;

	/**
		 * get the value of Short Term Investments by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return ShortTermInv
	 */
	public java.lang.Double getCurInv(String symbol,int year,int quarter) ;
	/**
	 * get the value of Short Term Investments by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return ShortTermInv
     */
	public java.lang.Double getCurInv(String symbol,int year) ;
	
	/**
		 * get the value of Net Receivables by the given symbol and date
		 * @param symbol
         * @param date
		 * @return NetRec
	 */
	public java.lang.Double getNetRec(String symbol,Date date) ;

	/**
		 * get the value of Net Receivables by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return NetRec
	 */ 
	public java.lang.Double getNetRec(String symbol,int year,int quarter) ;
	/**
	 * get the value of Net Receivables by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return NetRec
     */ 
	public java.lang.Double getNetRec(String symbol,int year) ;
	
	/**
		 * get the value of Inventory by the given symbol and date
		 * @param symbol
         * @param date
		 * @return Inventory
	 */
	public java.lang.Double getInventory(String symbol,Date date) ;
	
	/**
		 * get the value of Inventory by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return Inventory
	 */
	public java.lang.Double getInventory(String symbol,int year,int quarter) ;
	/**
	 * get the value of Inventory by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return Inventory
     */
	public java.lang.Double getInventory(String symbol,int year);
	
	/**
		 * get the value of Other Current Assets by the given symbol and date
		 * @param symbol
         * @param date
		 * @return OtherCurAssets
	 */
	public java.lang.Double getOtherCurAssets(String symbol,Date date) ;

	/**
		 * get the value of OtherCurAssets by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return OtherCurAssets
	 */
	public java.lang.Double getOtherCurAssets(String symbol,int year,int quarter) ;
	/**
	 * get the value of OtherCurAssets by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return OtherCurAssets
     */
	public java.lang.Double getOtherCurAssets(String symbol,int year) ;
	/**
		 * get the value of Long Term Investment by the given symbol and date
		 * @param symbol
         * @param date
		 * @return LongTermInv
	 */
	public java.lang.Double getLTInv(String symbol,Date date);
	
	/**
		 * get the value of Long Term Investment by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return LongTermInv
	 */
	public java.lang.Double getLTInv(String symbol,int year,int quarter);
	/**
	 * get the value of Long Term Investment by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return LongTermInv
     */
	public java.lang.Double getLTInv(String symbol,int year);
	/**
		 * get the value of Property Plant and Equipment by the given symbol and date
		 * @param symbol
         * @param date
		 * @return PPNE
	 */
	public java.lang.Double getPPNE(String symbol,Date date) ;

	/**
		 * get the value of Property Plant and Equipment by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return PPNE
	 */
	public java.lang.Double getPPNE(String symbol,int year,int quarter) ;
	/**
	 * get the value of Property Plant and Equipment by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return PPNE
     */
	public java.lang.Double getPPNE(String symbol,int year) ;
	/**
		 * get the value of Intangibles Assets by the given symbol and date
		 * @param symbol
         * @param date
		 * @return Intangibles
	 */
	public java.lang.Double getIntangibles(String symbol,Date date) ;
	
	/**
		 * get the value of Intangibles Assets by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return Intangibles
	 */
	public java.lang.Double getIntangibles(String symbol,int year,int quarter) ;
	/**
	 * get the value of Intangibles Assets by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return Intangibles
     */
	public java.lang.Double getIntangibles(String symbol,int year) ;
	/**
		 * get the value of Other LongTerm Assets by the given symbol and date
		 * @param symbol
         * @param date
		 * @return OtherLongTermAssets
	 */
	public java.lang.Double getOtherLTAssets(String symbol,Date date) ;

	/**
		 * get the value of Other LongTerm Assets by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return OtherLongTermAssets
	 */
	public java.lang.Double getOtherLTAssets(String symbol,int year,int quarter) ;
	/**
	 * get the value of Other LongTerm Assets by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return OtherLongTermAssets
     */
	public java.lang.Double getOtherLTAssets(String symbol,int year) ;
	
	/**
		 * get the value of Accounts Payable by the given symbol and date
		 * @param symbol
         * @param date
		 * @return AcctPayable
	 */
	public java.lang.Double getAcctPayable(String symbol,Date date) ;
	/**
		 * get the value of Accounts Payable by the given symbol,year and quarter
		 * @param symbol
         * @param year
         * @param quarter
		 * @return AcctPayable
	 */
	public java.lang.Double getAcctPayable(String symbol,int year,int quarter) ;
	/**
	 * get the value of Accounts Payable by the given symbol and year
	 * @param symbol
     * @param year
	 * @return AcctPayable
     */
	public java.lang.Double getAcctPayable(String symbol,int year) ;
	
	
	/**
		 * get the value of ShortTerm Debt by the given symbol and date
		 * @param symbol
         * @param date
		 * @return ShortTermDebt
	 */
	public java.lang.Double getCurDebt(String symbol,Date date) ;
	
	/**
		 * get the value of ShortTerm Debt by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return ShortTermDebt
	 */
	public java.lang.Double getCurDebt(String symbol,int year,int quarter) ;
	/**
	 * get the value of ShortTerm Debt by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return ShortTermDebt
     */
	public java.lang.Double getCurDebt(String symbol,int year) ;
	
	/**
		 * get the value of Other Current Liabilities by the given symbol and date
		 * @param symbol
         * @param date
		 * @return OtherShortTermLiab
	 */
	public java.lang.Double getOtherCurLiab(String symbol,Date date) ;
	
	/**
		 * get the value of Other Current Liabilities by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return OtherShortTermLiab
	 */
	public java.lang.Double getOtherCurLiab(String symbol,int year,int quarter) ;
	/**
	 * get the value of Other Current Liabilities by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return OtherShortTermLiab
     */
	public java.lang.Double getOtherCurLiab(String symbol,int year) ;
	/**
		 * get the value of LongTerm Debt by the given symbol and date
		 * @param symbol
         * @param date
		 * @return LongTermDebt
	 */
	public java.lang.Double getLTDebt(String symbol,Date date) ;
	
	/**
		 * get the value of LongTerm Debt by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return LongTermDebt
	 */
	public java.lang.Double getLTDebt(String symbol,int year,int quarter) ;
	/**
	 * get the value of LongTerm Debt by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return LongTermDebt
     */
	public java.lang.Double getLTDebt(String symbol,int year) ;
	
	/**
		 * get the value of Other LongTerm Liabilities by the given symbol and date
		 * @param symbol
         * @param date
		 * @return OtherLongTermLiab
	 */
	public java.lang.Double getOtherLTLiab(String symbol,Date date) ;
	
	/**
		 * get the value of Other LongTerm Liabilities by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return OtherLongTermLiab
	 */
	public java.lang.Double getOtherLTLiab(String symbol,int year,int quarter) ;
	/**
	 * get the value of Other LongTerm Liabilities by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return OtherLongTermLiab
     */
	public java.lang.Double getOtherLTLiab(String symbol,int year) ;
	
	/**
		 * get the value of Preferred Stock by the given symbol and date
		 * @param symbol
         * @param date
		 * @return PreferredStock
	 */
	public java.lang.Double getPreferredStock(String symbol,Date date);
	
	/**
		 * get the value of Preferred Stock by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return PreferredStock
	 */
	public java.lang.Double getPreferredStock(String symbol,int year,int quarter);
	/**
	 * get the value of Preferred Stock by the given symbol and year
	 * @param symbol
	 * @param year
 	 * @return PreferredStock
     */
	public java.lang.Double getPreferredStock(String symbol,int year);
	
	/**
		 * get the value of Common Stock by the given symbol and date
		 * @param symbol
         * @param date
		 * @return CommonStock
	 */
	public java.lang.Double getCommonStock(String symbol,Date date);
	
	/**
		 * get the value of Common Stock by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return CommonStock
	 */
	public java.lang.Double getCommonStock(String symbol,int year,int quarter);
	/**
	 * get the value of Common Stock by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return CommonStock
     */
	public java.lang.Double getCommonStock(String symbol,int year);
	/**
		 * get the value of Retained Earning by the given symbol and date
		 * @param symbol
         * @param date
		 * @return RetainedEarning
	 */
	public java.lang.Double getRetainedEarning(String symbol,Date date);
	
	/**
		 * get the value of Retained Earning by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return RetainedEarning
	 */
	public java.lang.Double getRetainedEarning(String symbol,int year,int quarter);
	/**
	 * get the value of Retained Earning by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return RetainedEarning
     */
	public java.lang.Double getRetainedEarning(String symbol,int year);
	/**
		 * get the value of Treasury Stock by the given symbol and date
		 * @param symbol
         * @param date
		 * @return TreasuryStock
	 */
	public java.lang.Double getTreasuryStock(String symbol,Date date);
	
	/**
		 * get the value of Treasury Stock by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return TreasuryStock
	 */
	public java.lang.Double getTreasuryStock(String symbol,int year,int quarter);
	/**
	 * get the value of Treasury Stock by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return TreasuryStock
     */
	public java.lang.Double getTreasuryStock(String symbol,int year);
	
	/**
		 * get the value of Other Equity by the given symbol and date
		 * @param symbol
         * @param date
		 * @return OtherEquity
	 */
	public java.lang.Double getOtherEquity(String symbol,Date date);
	
	/**
		 * get the value of Other Equity by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return OtherEquity
	 */
	public java.lang.Double getOtherEquity(String symbol,int year,int quarter);
	/**
	 * get the value of Other Equity by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return OtherEquity
     */
	public java.lang.Double getOtherEquity(String symbol,int year);
	
	/*Balance Statement calculation*/
	/**
	 * get the value of Total Current Assets by the given symbol and date
	 * @param symbol
     * @param date
	 * @return TotalCurAssets
 */	
	public java.lang.Double getTotalCurAssets(String symbol, Date date);

	/**
		 * get the value of Total Current Assets by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return TotalCurAssets
	 */
	public java.lang.Double getTotalCurAssets(String symbol, int year,int quarter);
	/**
	 * get the value of Total Current Assets by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return TotalCurAssets
     */
	public java.lang.Double getTotalCurAssets(String symbol, int year);
	
	/**
		 * get the value of Total Assets by the given symbol and date
	     * @param symbol
         * @param date
		 * @return TotalAssets
	 */
	public java.lang.Double getTotalAssets(String symbol, Date date);

	/**
		 * get the value of Total Assets by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return TotalAssets
	 */
	public java.lang.Double getTotalAssets(String symbol, int year,int quarter);
	/**
	 * get the value of Total Assets by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return TotalAssets
     */
	public java.lang.Double getTotalAssets(String symbol, int year);
	/**
		 * get the value of Total Current Liabilities by the given symbol and date
	     * @param symbol
         * @param date
		 * @return TotalCurLiab
	 */
	public java.lang.Double getTotalCurLiab(String symbol, Date date);

	/**
		 * get the value of Total Current Liabilities by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return TotalCurLiab
	 */
	public java.lang.Double getTotalCurLiab(String symbol, int year,int quarter);
	/**
	 * get the value of Total Current Liabilities by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return TotalCurLiab
     */
	public java.lang.Double getTotalCurLiab(String symbol, int year);
	
	/**
		 * get the value of Total Liabilities by the given symbol and date
	     * @param symbol
         * @param date
		 * @return TotalLiab
	 */
	public java.lang.Double getTotalLiab(String symbol, Date date);

	/**
		 * get the value of Total Liabilities by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return TotalLiab
	 */
	public java.lang.Double getTotalLiab(String symbol,int year, int quarter);
	/**
	 * get the value of Total Liabilities by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return TotalLiab
     */
	public java.lang.Double getTotalLiab(String symbol,int year);
	
	/**
		 * get the value of Total Stockholder Equity by the given symbol and date
	     * @param symbol
         * @param date
		 * @return TotalEquity
	 */
	public java.lang.Double getTotalEquity(String symbol, Date date);

	/**
		 * get the value of Stockholder Equity by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return TotalEquity
	 */
	public java.lang.Double getTotalEquity(String symbol,int year, int quarter);
	/**
	 * get the value of Stockholder Equity by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return TotalEquity
     */
	public java.lang.Double getTotalEquity(String symbol,int year);
	
	/**
		 * get the value of Net Tangible Assets by the given symbol and date
	     * @param symbol
         * @param date
		 * @return NetTanAssets
	 */
	public java.lang.Double getNetTanAssets(String symbol, Date date);
	
	/**
		 * get the value of Net Tangible Assets by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return NetTanAssets
	 */
	public java.lang.Double getNetTanAssets(String symbol,int year, int quarter);
	/**
	 * get the value of Net Tangible Assets by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return NetTanAssets
     */
	public java.lang.Double getNetTanAssets(String symbol,int year);
	
	/**
		 * get the value of Total Liabilities and Equivalents by the given symbol and date
	     * @param symbol
         * @param date
         * @return TotalLiabNEquiv
	 */
	public java.lang.Double getTotalLiabNEquiv(String symbol, Date date);

	/**
		 * get the value of Total Liabilities and Equivalents by the given symbol,year and quarter
		 * @param symbol
		 * @param year
	     * @param quarter
		 * @return TotalLiabNEquiv
	 */
	public java.lang.Double getTotalLiabNEquiv(String symbol, int year,int quarter);
	/**
	 * get the value of Total Liabilities and Equivalents by the given symbol and year
	 * @param symbol
	 * @param year
	 * @return TotalLiabNEquiv
     */
	public java.lang.Double getTotalLiabNEquiv(String symbol, int year);
	
	
	/*Valuation Ratio*/
	
	public java.lang.Double getRevenueRate(String symbol,int year);
	
	public java.lang.Double getCOGSRate(String symbol,int year);
	
	public java.lang.Double getGrossMargin(String symbol,int year);
	
	public java.lang.Double getSGNARate(String symbol,int year);
	
	public java.lang.Double getRNDRate(String symbol,int year);
	
	public java.lang.Double getOperMargin(String symbol,int year);
	
	public java.lang.Double getIntExpRate(String symbol,int year);
	
	public java.lang.Double getEBTMargin(String symbol,int year);
	
	public java.lang.Double getTaxRate(String symbol,int year);
	
	public java.lang.Double getNetMargin(String symbol,int year);
	
	public java.lang.Double getAssetTurnover(String symbol,int year);
	
	public java.lang.Double getReturnOnAsset(String symbol,int year);
	
	public java.lang.Double getFinancialLeverage(String symbol,int year);
	
	public java.lang.Double getReturnOnEquity(String symbol,int year);
	
	public java.lang.Double getFreeCashPerNetInc(String symbol,int year);
	
	public java.lang.Double getFreeCashPerSales(String symbol,int year);
	
	public java.lang.Double getCapExpPerSales(String symbol,int year);
	
	public java.lang.Double getEPS(String symbol,int year);
	
	public java.lang.Double getEPS(String symbol,int year,int quarter);
	
	public java.lang.Double getEPS(String symbol,int count,Date date);
	
	public java.lang.Double getMarketCap(String symbol,int year);
	
	public java.lang.Double getMarketCap(String symbol,int year,int quarter);
	
	public Map<String,Double> getMarketCap(int year,int quarter);
	
	public java.lang.Double getTotLiabToTotAsset(String symbol,int year,int quarter);
	
	public java.lang.Double getTotLiabToTotAsset(List<String> symbols,int year,int quarter);
	
	public Map<String,Double[]> getLiabToAsset(int year,int quarter);
	
	public java.lang.Double getLongTermDebtToTotCap(String symbol,int year,int quarter);
	
	public java.lang.Double getLongTermDebtToTotCap(List<String> symbols,int year,int quarter);
	
	/*Growth Rate*/
	
	public java.lang.Double getYOYRevenueGrowth(String symbol,int year);
	
	public java.lang.Double getYOYOperIncGrowth(String symbol,int year);
	
	public java.lang.Double getYOYOperCashGrowth(String symbol,int year);
	
	public java.lang.Double getYOYFreeCashGrowth(String symbol,int year);
	
	//public java.lang.Double getEPSGrowth(String symbol,int year);
	
	/*Financial Health*/
	public java.lang.Double getCurrentRatio(String symbol,int year);
	
	public java.lang.Double getQuickRatio(String symbol,int year);
	
	/*get financial statement object*/
	
	public com.lti.service.bo.IncomeStatement getIncomeStatement(String symbol,int year,int quarter);
	
	public com.lti.service.bo.CashFlow getCashFlow(String symbol,int year,int quarter);
	
	public com.lti.service.bo.BalanceStatement getBalanceStatement(String symbol,int year,int quarter);
	
    public com.lti.service.bo.IncomeStatement getRecentIncomeStatement(String symbol,Date date);
	
	public com.lti.service.bo.CashFlow getRecentCashFlow(String symbol,Date date);
	
	public com.lti.service.bo.BalanceStatement getRecentBalanceStatement(String symbol,Date date);
	
	public List<com.lti.service.bo.IncomeStatement> getRecentIncomeStatement(String symbol,int count,Date date);
	
	public List<com.lti.service.bo.CashFlow> getRecentCashFlow(String symbol,int count,Date date);
	
	public List<com.lti.service.bo.BalanceStatement> getRecentBalanceStatement(String symbol,int count,Date date);
	
	public com.lti.service.bo.Company getCompany(String symbol);
	
	public List<com.lti.service.bo.Company> getIndustryIndex(String sector,String industry);
	
	public List<String> getIndustryIndex(String industry);
	
	public Map<String,String>getIndustryIndex();
	
	public com.lti.service.bo.YearlyIncomeStatement getYearlyIncomeStatement(String symbol,int year);
	
	public com.lti.service.bo.YearlyCashFlow getYearlyCashFlow(String symbol,int year);
	
	public com.lti.service.bo.YearlyBalanceStatement getYearlyBalanceStatement(String symbol,int year);
	
	public List<com.lti.service.bo.YearlyIncomeStatement> getRecentYearlyIncomeStatement(String symbol,int count,Date CurDate);
	
	public List<com.lti.service.bo.YearlyCashFlow> getRecentYearlyCashFlow(String symbol,int count,Date CurDate);
	
	public List<com.lti.service.bo.YearlyBalanceStatement> getRecentYearlyBalanceStatement(String symbol,int count,Date CurDate);

	public Map<String,Double> getPrice(Date date);
	
	public Map<String,Double[]> getBalanceCash(int year,int quarter);
	
	public Map<String,Double> getEPS(Date date);
	
	public Map<String,Double> getAnnualEPS(int year);
	
	public Map<String,Double> get5YearEPSGrowth(int year);
	
	public Map<String,Double> getEPSGrowth(Date date);
	
	public Map<String,Double> getFloats(Date date);
	
	public Map<String,Double> getInstitutionHolder(Date date);
	
	public Map<String,Double> getRSGrade(Date date);
	
	public Double getPrice(String symbol,Date date);
	
	public Double get5YearEPS(String symbol,int year);
	
	public Double getInstitutionHolder(String symbol,int year,int quarter);
	
	public Double getRSGrade(String symbol,Date date);
}