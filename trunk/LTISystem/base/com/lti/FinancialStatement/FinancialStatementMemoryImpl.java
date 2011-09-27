package com.lti.FinancialStatement;

import java.util.ArrayList;
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
import com.lti.type.TimeUnit;
import com.lti.service.impl.DAOManagerImpl;
import com.lti.service.impl.FinancialStatementManagerImpl;

/**
 * @author Xie
 * 
 */

/**
 * @author Xie
 *
 */
public class FinancialStatementMemoryImpl extends
		FinancialStatementDatabaseImpl {


	/** ******************************************************************************************** */
	/* store Financial Statement data */
	/** ******************************************************************************************* */

	private Map<String, QuarterCache> qCache = new HashMap<String, QuarterCache>();
	private Map<String, YearCache> yCache = new HashMap<String, YearCache>();

	private class QuarterCache {
		public List<Double> values = null;
		public String symbol;
		public List<Integer> years = null;
		public List<Integer> quarters = null;

		public Double getValue(int year, int quarter) {
			if (getIndex(year, quarter) >= 0)
				return values.get(getIndex(year, quarter));
			else
				return null;
		}

		public int getIndex(int year, int quarter) {
			for (int i = 0; i < years.size(); i++) {
				if (years.get(i) == year && quarters.get(i) == quarter)
					return i;
			}
			return -1;
		}
	}

	private class YearCache {
		public List<Double> values = null;
		public String symbol;
		public List<Integer> years = null;

		public Double getValue(int year) {
			if (getIndex(year) >= 0)
				return values.get(getIndex(year));
			else
				return null;
		}

		public int getIndex(int year) {
			for (int i = 0; i < years.size(); i++) {
				if (years.get(i) == year)
					return i;
			}
			return -1;
		}
	}

	private Map<String, Double> priceCache = new HashMap<String, Double>();
	private Date priceDate;

	/** ************************************************************************************** */
	/* load Financial Statement data into Memory */
	/** ************************************************************************************** */

	private void loadAttribute(String attr) {
		String table = "";
		String sql = "";

		FinancialStatementManagerImpl fsmi = (FinancialStatementManagerImpl) ContextHolder
				.getInstance().getApplicationContext().getBean(
						"financialStatementManager");

		// choose the database table by the attribute
		if (attr.equalsIgnoreCase("Revenue") || attr.equalsIgnoreCase("COGS")
				|| attr.equalsIgnoreCase("SGNA")
				|| attr.equalsIgnoreCase("RND")
				|| attr.equalsIgnoreCase("OtherOperExp")
				|| attr.equalsIgnoreCase("IntExp")
				|| attr.equalsIgnoreCase("AcctChange")
				|| attr.equalsIgnoreCase("DiscOper")
				|| attr.equalsIgnoreCase("ExtItem")
				|| attr.equalsIgnoreCase("IncomeTax")
				|| attr.equalsIgnoreCase("TotalOtherInc")
				|| attr.equalsIgnoreCase("Shares")
				|| attr.equalsIgnoreCase("Floats")
				|| attr.equalsIgnoreCase("InstitutionHolder")
				|| attr.equalsIgnoreCase("GrossProfit")
				|| attr.equalsIgnoreCase("TotalOperExp")
				|| attr.equalsIgnoreCase("OperInc")
				|| attr.equalsIgnoreCase("PreTaxInc")
				|| attr.equalsIgnoreCase("AfterTaxInc")
				|| attr.equalsIgnoreCase("NetInc"))
			table = "ltisystem_incomestatement";
		if (attr.equalsIgnoreCase("CashNEquiv")
				|| attr.equalsIgnoreCase("ShortTermInv")
				|| attr.equalsIgnoreCase("NetRec")
				|| attr.equalsIgnoreCase("Inventory")
				|| attr.equalsIgnoreCase("OtherCurAsset")
				|| attr.equalsIgnoreCase("PPNE")
				|| attr.equalsIgnoreCase("Intangibles")
				|| attr.equalsIgnoreCase("OtherLongTermAsset")
				|| attr.equalsIgnoreCase("AcctPayable")
				|| attr.equalsIgnoreCase("ShortTermDebt")
				|| attr.equalsIgnoreCase("OtherCurLiab")
				|| attr.equalsIgnoreCase("LongTermDebt")
				|| attr.equalsIgnoreCase("OtherLongTermLiab")
				|| attr.equalsIgnoreCase("PreferredStock")
				|| attr.equalsIgnoreCase("CommonStock")
				|| attr.equalsIgnoreCase("RetainedEarning")
				|| attr.equalsIgnoreCase("TreasuryStock")
				|| attr.equalsIgnoreCase("OtherEquity")
				|| attr.equalsIgnoreCase("LongTermInv")
				|| attr.equalsIgnoreCase("CurAssets")
				|| attr.equalsIgnoreCase("Assets")
				|| attr.equalsIgnoreCase("CurLiab")
				|| attr.equalsIgnoreCase("Liab"))
			table = "ltisystem_balancestatement";
		if (attr.equalsIgnoreCase("DeprAmort")
				|| attr.equalsIgnoreCase("OtherOperCash")
				|| attr.equalsIgnoreCase("CapExp")
				|| attr.equalsIgnoreCase("OtherInvCash")
				|| attr.equalsIgnoreCase("OtherFinCash")
				|| attr.equalsIgnoreCase("Dividend")
				|| attr.equalsIgnoreCase("AdjNetInc")
				|| attr.equalsIgnoreCase("Investment")
				|| attr.equalsIgnoreCase("StockSalePur")
				|| attr.equalsIgnoreCase("NetBorrow")
				|| attr.equalsIgnoreCase("CurrencyAdj")
				|| attr.equalsIgnoreCase("OperCF")
				|| attr.equalsIgnoreCase("FinCF")
				|| attr.equalsIgnoreCase("InvCF"))
			table = "ltisystem_cashflow";
		if (attr.startsWith("Annu")) {
			attr = attr.substring(4);
			if (attr.equalsIgnoreCase("Revenue")
					|| attr.equalsIgnoreCase("COGS")
					|| attr.equalsIgnoreCase("RD")
					|| attr.equalsIgnoreCase("IntExp")
					|| attr.equalsIgnoreCase("IncomeTax")
					|| attr.equalsIgnoreCase("PreTaxInc")
					|| attr.equalsIgnoreCase("AfterTaxInc")
					|| attr.equalsIgnoreCase("GrossProfit")
					|| attr.equalsIgnoreCase("TotalOperExp")
					|| attr.equalsIgnoreCase("OperInc")
					|| attr.equalsIgnoreCase("NetInc")
					|| attr.equalsIgnoreCase("Floats")
					|| attr.equalsIgnoreCase("Shares")
					|| attr.equalsIgnoreCase("InstitutionHolder"))
				table = "ltisystem_incomestatement_yearly";
			if (attr.equalsIgnoreCase("Cash")
					|| attr.equalsIgnoreCase("ShortTermInv")
					|| attr.equalsIgnoreCase("AcctRec")
					|| attr.equalsIgnoreCase("Inventory")
					|| attr.equalsIgnoreCase("OtherCurAssets")
					|| attr.equalsIgnoreCase("PPE")
					|| attr.equalsIgnoreCase("LongTermInv")
					|| attr.equalsIgnoreCase("Intangibles")
					|| attr.equalsIgnoreCase("OtherLTAssets")
					|| attr.equalsIgnoreCase("AcctPayable")
					|| attr.equalsIgnoreCase("ShortTermDebt")
					|| attr.equalsIgnoreCase("LongTermDebt")
					|| attr.equalsIgnoreCase("OtherCurLiab")
					|| attr.equalsIgnoreCase("OtherLTLiab")
					|| attr.equalsIgnoreCase("PreferredStock")
					|| attr.equalsIgnoreCase("TotalEquity")
					|| attr.equalsIgnoreCase("CurAssets")
					|| attr.equalsIgnoreCase("Assets")
					|| attr.equalsIgnoreCase("CurLiab")
					|| attr.equalsIgnoreCase("Liab"))
				table = "ltisystem_balancestatement_yearly";
			if (attr.equalsIgnoreCase("CapExp")
					|| attr.equalsIgnoreCase("FinCF")
					|| attr.equalsIgnoreCase("InvCF")
					|| attr.equalsIgnoreCase("OperCF"))
				table = "ltisystem_cashflow_yearly";
		}

		// execute the sql statement
		if (table.equalsIgnoreCase("ltisystem_incomestatement")
				|| table.equalsIgnoreCase("ltisystem_balancestatement")
				|| table.equalsIgnoreCase("ltisystem_cashflow")) {
			sql = "SELECT symbol," + attr + ",year,quarter FROM " + table
					+ " group by symbol,date";
			try {
				List list = fsmi.findBySQL(sql);
				String previousSymbol = "A";// the first stock symbol in
											// database is "A"

				if (list != null && list.size() != 0) {
					QuarterCache qc = new QuarterCache();
					qc.years = new ArrayList<Integer>();
					qc.quarters = new ArrayList<Integer>();
					qc.values = new ArrayList<Double>();

					for (int i = 0; i < list.size(); i++) {
						Object[] objs = (Object[]) list.get(i);// read the
																// records from
																// database
						String symbol = (String) objs[0];

						// compare the symbol with the previous symbol.Store the
						// value and period into the previous QuarterCache if
						// they are the same.
						if (previousSymbol.equalsIgnoreCase(symbol)) {
							qc.values.add((Double) objs[1]);
							qc.years.add((Integer) objs[2]);
							qc.quarters.add((Integer) objs[3]);
						} else {
							qCache.put(attr + "." + previousSymbol, qc);
							qc = new QuarterCache();
							qc.years = new ArrayList<Integer>();
							qc.quarters = new ArrayList<Integer>();
							qc.values = new ArrayList<Double>();
							qc.values.add((Double) objs[1]);
							qc.years.add((Integer) objs[2]);
							qc.quarters.add((Integer) objs[3]);
						}

						previousSymbol = symbol;
					}

				}
			} catch (Exception e) {
				System.out.println(StringUtil.getStackTraceString(e));
			}
		}
		// if the table is IncomeStatement_Yearly,BalanceStatement_Yearly or
		// CashFlow_Yearly
		else {
			sql = "SELECT symbol," + attr + ",year FROM " + table
					+ " group by symbol,enddate";
			try {
				List list = fsmi.findBySQL(sql);
				String previousSymbol = "A";// the first stock symbol in
											// database is "A"

				if (list != null && list.size() != 0) {
					YearCache yc = new YearCache();
					yc.years = new ArrayList<Integer>();
					yc.values = new ArrayList<Double>();

					for (int i = 0; i < list.size(); i++) {
						Object[] objs = (Object[]) list.get(i);// read the
																// records from
																// database
						String symbol = (String) objs[0];

						// compare the symbol with the previous symbol.Store the
						// value and period into the previous QuarterCache if
						// they are the same.
						if (previousSymbol.equalsIgnoreCase(symbol)) {
							yc.values.add((Double) objs[1]);
							yc.years.add((Integer) objs[2]);
						} else {
							yCache.put(attr + "." + previousSymbol, yc);
							yc = new YearCache();
							yc.years = new ArrayList<Integer>();
							yc.values = new ArrayList<Double>();
							yc.values.add((Double) objs[1]);
							yc.years.add((Integer) objs[2]);
						}
						previousSymbol = symbol;
					}

				}
			} catch (Exception e) {
				System.out.println(StringUtil.getStackTraceString(e));
			}
		}

	}

	/** ************************************************************************************** */
	/* load price to Cache by the given date */
	/** ************************************************************************************** */
	public void loadPrice(Date date) {
		priceDate = date;
		priceCache = super.getPrice(date);
	}

	/** ************************************************************************************** */
	/* fetch Financial Statement data from Cache */
	/** ************************************************************************************** */

	private QuarterCache getQuarterCache(String symbol, String attr) {
		QuarterCache qc = qCache.get(attr + "." + symbol);
		if (qc == null) {
			if(qCache.get(attr + "." + "IBM")==null){
		    //IBM is a benchmark here to judge whether values of the attribute of the stocks is loaded
			System.out.println(symbol+" start to load quarter cache:"+attr);
			loadAttribute(attr);
			qc = qCache.get(attr + "." + symbol);
			}
		}
		return qc;
	}

	private YearCache getYearCache(String symbol, String attr) {
		YearCache yc = yCache.get(attr.substring(4) + "." + symbol);
		if (yc == null) {
			if(yCache.get(attr.substring(4) + "." + "IBM")==null){
			//IBM is a benchmark here to judge whether values of the attribute of the stocks is loaded.
			System.out.println(symbol+" start to load year cache:"+attr);
			loadAttribute(attr);
			yc = yCache.get(attr.substring(4) + "." + symbol);
		   }
		}
		return yc;
	}

	/** *************************************************************************************** */
	/* get attributes of Financial Statement */
	/** *************************************************************************************** */

	@Override
	public Double getRevenue(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "Revenue").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getCOGS(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "COGS").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getSGNA(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "SGNA").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getRND(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "RND").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOtherOperExp(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "OtherOperExp").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getIntExp(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "IntExp").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getAcctChange(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "AcctChange").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getDiscOper(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "DiscOper").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getExtItem(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "ExtItem").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getIncomeTax(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "IncomeTax").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getTotalOtherIncome(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "TotalOtherInc").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getShares(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "Shares").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getFloats(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "Floats").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getInstitutionHolder(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "InstitutionHolder").getValue(year,quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getGrossProfit(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "GrossProfit").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	public Double getOperExp(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "TotalOperExp").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOperatingIncome(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "OperInc").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getEarningBeforeTax(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "PreTaxInc").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getEarningAfterTax(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "AfterTaxInc").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getNetIncome(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "NetInc").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getCashNEquiv(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "CashNEquiv").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getCurInv(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "ShortTermInv").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getNetRec(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "NetRec").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getInventory(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "Inventory").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOtherCurAssets(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "OtherCurAsset").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getPPNE(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "PPNE").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getIntangibles(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "Intangibles").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOtherLTAssets(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "OtherLongTermAsset").getValue(year,quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getAcctPayable(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "AcctPayable").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getCurDebt(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "ShortTermDebt").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOtherCurLiab(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "OtherCurLiab").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getLTDebt(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "LongTermDebt").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOtherLTLiab(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "OtherLongTermLiab").getValue(year,quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getPreferredStock(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "PreferredStock").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getCommonStock(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "CommonStock").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getRetainedEarning(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "RetainedEarning").getValue(year,quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getTreasuryStock(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "TreasuryStock").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOtherEquity(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "OtherEquity").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getLTInv(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "LongTermInv").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getTotalCurAssets(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "CurAssets").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getTotalAssets(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "Assets").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getTotalCurLiab(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "CurLiab").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getTotalLiab(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "Liab").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public Double getTotalEquity(String symbol, int year, int quarter) {
		try{
		return this.getTotalAssets(symbol,year,quarter) - this.getTotalLiab(symbol,year,quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getDepr(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "DeprAmort").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOtherOperCash(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "OtherOperCash").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getCapExp(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "CapExp").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOtherInvCash(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "OtherInvCash").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOtherFinCash(String symbol, int year, int quarter) {
		try{
			return this.getOperCash(symbol, year, quarter)+this.getCapExp(symbol, year, quarter);
		}catch(Exception e){
			return null;
		}
	}
	
	@Override
	public Double getFreeCash(String symbol, int year, int quarter) {
		try{
		return this.getOperCash(symbol,year,quarter)+this.getCapExp(symbol,year,quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getDividends(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "Dividend").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getAdjNetInc(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "AdjNetInc").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getInvestment(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "Investment").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getStockSalePur(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "StockSalePur").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getNetBorrowing(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "NetBorrow").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getCurrencyAdj(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "CurrencyAdj").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOperCash(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "OperCF").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getFinCash(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "FinCF").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getInvCash(String symbol, int year, int quarter) {
		try{
			return getQuarterCache(symbol, "InvCF").getValue(year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getRevenue(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuRevenue").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getCOGS(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuCOGS").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getRND(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuRD").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getIntExp(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuIntExp").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getIncomeTax(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuIncomeTax").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getEarningBeforeTax(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuPreTaxInc").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getEarningAfterTax(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuAfterTaxInc").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getGrossProfit(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuGrossProfit").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	public Double getOperExp(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuTotalOperExp").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOperatingIncome(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuOperInc").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getNetIncome(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuNetInc").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	public Double getFloats(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuFloats").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getShares(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuShares").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	public Double getInstitutionHolder(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuInstitutionHolder").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getCashNEquiv(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuCash").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getCurInv(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuShortTermInv").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	public Double getAcctRec(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuAcctRec").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getInventory(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuInventory").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOtherCurAssets(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuOtherCurAssets").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getPPNE(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuPPE").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getLTInv(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuLongTermInv").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getIntangibles(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuIntangibles").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOtherLTAssets(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuOtherLTAssets").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getAcctPayable(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuAcctPayable").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getCurDebt(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuShortTermDebt").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getLTDebt(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuLongTermDebt").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOtherCurLiab(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuOtherCurLiab").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOtherLTLiab(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuOtherLTLiab").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getPreferredStock(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuPreferredStock").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getTotalEquity(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuTotalEquity").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getTotalCurAssets(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuCurAssets").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getTotalAssets(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuAssets").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getTotalCurLiab(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuCurLiab").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getTotalLiab(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuLiab").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getCapExp(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuCapExp").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getFinCash(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuFinCF").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getInvCash(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuInvCF").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getOperCash(String symbol, int year) {
		try{
			return getYearCache(symbol, "AnnuOperCF").getValue(year);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getFreeCash(String symbol, int year) {
		try{
			return this.getOperCash(symbol, year)+this.getCapExp(symbol, year);
		}catch(Exception e){
		return null;
		}
	}

	@Override
	public Map<String, String> getIndustryIndex() {
		try{
			return super.getIndustryIndex();
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<String> getIndustryIndex(String industry) {
		try{
			return super.getIndustryIndex(industry);
		}catch(Exception e){
			return null;
		}
	}
	
	/***********************************************************************************/
	/*                       fetch the Financial Statement ratio                       */
	/***********************************************************************************/
	@Override
	public Double getEPS(String symbol, int year, int quarter) {
		try{
			return this.getNetIncome(symbol, year, quarter)/this.getShares(symbol, year, quarter);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public Double getEPS(String symbol, int year) {
		try{
			return this.getNetIncome(symbol, year)/this.getShares(symbol, year);
		}catch(Exception e){
			return null;
		}
	}

	/** ************************************************************************************************* */
	/* get stock's price from priceCache by the given date */
	/** ************************************************************************************************* */
	public Double getPrice(String symbol, Date date) {
		/*
		double price = 0d;
		if (priceCache == null ||priceDate==null||!priceDate.equals(date)) {
			System.out.println("start to load price");
			loadPrice(date);
		}
		try {
			price = priceCache.get(symbol);
			return price;
		} catch (Exception e) {
			return null;
		}
		*/
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
        try{
		return securityManager.getPrice(securityManager.get(symbol).getID(), date);
        }catch(Exception e){
        	return null;
        }
	}

	
	public static void main(String[] args) {
		FinancialStatementMemoryImpl fsmi = new FinancialStatementMemoryImpl();
		long time1 = System.currentTimeMillis();
		/*
		Double v1 = fsmi.getNetIncome("IBM", 2008);
		Double v2 = fsmi.getNetIncome("^DWC", 2008);
		Double v3 = fsmi.getNetIncome("AAGPX",2008);
		Double v4 = fsmi.getNetIncome("IEF", 2008);
		Double v5 = fsmi.getNetIncome("AGC", 2008);
		Double v7 = fsmi.getNetIncome("BHD", 2008);
		Double v8 = fsmi.getNetIncome("BHV", 2008);
		System.out.println(v1+","+v2+","+v3+","+v4+","+v5+","+v7+","+v8);
		*/
		Date CurrentDate=LTIDate.getDate(2008, 6, 30);
		Date lastQ1Date = LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -1);
		Date lastQ2Date = LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -2);
		Date lastQ3Date= LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -3);
		Date lastQ4Date= LTIDate.getNewDate(CurrentDate, TimeUnit.QUARTERLY, -4);
		Double v4=fsmi.getNetIncome("ARGN", LTIDate.getYear(lastQ4Date),LTIDate.getQuarter(lastQ4Date));
		System.out.println(v4);
		long time2 = System.currentTimeMillis();
		System.out.println("time is:" + (time2 - time1));
	}

	@Override
	public Double get5YearEPS(String symbol, int year) {
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
	public Double getRSGrade(String symbol, Date date) {
		return super.getRSGrade(symbol, date);
	}


}
