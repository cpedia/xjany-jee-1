package com.lti.service.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.text.DateFormatSymbols;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import sun.tools.tree.FinallyStatement;

import com.lti.FinancialStatement.FinancialStatement;
import com.lti.FinancialStatement.FinancialStatementDatabaseImpl;
import com.lti.service.BaseFormulaManager;
import com.lti.service.FinancialStatementManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.CashFlow;
import com.lti.service.bo.Company;
import com.lti.service.bo.Group;
import com.lti.service.bo.IncomeStatement;
import com.lti.service.bo.BalanceStatement;
import com.lti.service.bo.RelativeStrength;
import com.lti.service.bo.Security;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.lti.util.html.*;
import com.lti.util.IncomeStatementComparator;
import com.lti.util.CashFlowComparator;
import com.lti.util.BalanceStatementComparator;
import com.lti.util.StringUtil;
import com.lti.util.YearlyBalanceStatementComparator;
import com.lti.util.YearlyCashFlowComparator;
import com.lti.util.YearlyIncomeStatementComparator;
import com.lti.type.*;
import com.lti.system.Configuration;
import com.lti.service.bo.YearlyBalanceStatement;
import com.lti.service.bo.YearlyCashFlow;
import com.lti.service.bo.YearlyIncomeStatement;

import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class FinancialStatementManagerImpl extends DAOManagerImpl implements com.lti.service.FinancialStatementManager, Serializable {

	private static final long serialVersionUID = 1L;
	
	@Override
	public void removeCashFlow(long id){
		Object obj = getHibernateTemplate().get(CashFlow.class, id);
		getHibernateTemplate().delete(obj);
	}

	@Override
	public CashFlow getCashFlow(Long id) {
		return (CashFlow) getHibernateTemplate().get(CashFlow.class, id);
	}
	
	@Override
	public Long saveCashFlow(CashFlow cf) {
		getHibernateTemplate().save(cf);
		return cf.getID();
	}
	
	@Override
	public void updateCashFlow(CashFlow cf) {
		getHibernateTemplate().update(cf);
	}	

	@Override
	public void removeBalanceStatement(long id){
		Object obj = getHibernateTemplate().get(BalanceStatement.class, id);
		getHibernateTemplate().delete(obj);
	}

	@Override
	public BalanceStatement getBalanceStatement(Long id) {
		return (BalanceStatement) getHibernateTemplate().get(BalanceStatement.class, id);
	}
	
	@Override
	public Long saveBalanceStatement(BalanceStatement bs) {
		getHibernateTemplate().save(bs);
		return bs.getID();
	}
	
	@Override
	public void updateBalanceStatement(BalanceStatement bs) {
		getHibernateTemplate().update(bs);
	}
		
	@Override
	public void removeIncomeStatement(long id){
		Object obj = getHibernateTemplate().get(IncomeStatement.class, id);
		getHibernateTemplate().delete(obj);
	}

	@Override
	public IncomeStatement getIncomeStatement(Long id) {
		return (IncomeStatement) getHibernateTemplate().get(IncomeStatement.class, id);
	}

	@Override
	public Long saveIncomeStatement(IncomeStatement is) {
		getHibernateTemplate().save(is);
		return is.getID();
	}

	@Override
	public void updateIncomeStatement(IncomeStatement is) {
		getHibernateTemplate().update(is);	
	}	
	
	@Override
	public com.lti.service.bo.CashFlow getCashFlow(String symbol, Date date) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CashFlow.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.eq("Date", LTIDate.getLastDayOfQuarter(date)));
		List<CashFlow> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	public com.lti.service.bo.CashFlow getCashFlow(String symbol,int year,int quarter) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CashFlow.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.eq("Year", year));
		detachedCriteria.add(Restrictions.eq("Quarter", quarter));
		List<CashFlow> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}
	@Override
	public com.lti.service.bo.BalanceStatement getBalanceStatement(String symbol, Date date) {
		try{
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BalanceStatement.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.eq("Date", LTIDate.getLastDayOfQuarter(date)));
		List<BalanceStatement> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public com.lti.service.bo.BalanceStatement getBalanceStatement(String symbol,int year,int quarter) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BalanceStatement.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.eq("Year", year));
		detachedCriteria.add(Restrictions.eq("Quarter", quarter));
		List<BalanceStatement> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}
	@Override
	public com.lti.service.bo.IncomeStatement getIncomeStatement(String symbol, Date date) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IncomeStatement.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.eq("Date", LTIDate.getLastDayOfQuarter(date)));
		List<IncomeStatement> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	public com.lti.service.bo.IncomeStatement getIncomeStatement(String symbol,int year,int quarter){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IncomeStatement.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.eq("Year", year));
		detachedCriteria.add(Restrictions.eq("Quarter",quarter));
		List<IncomeStatement> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}
	
	@Override
	public void downloadCashFlow(String symbol, Date date) {
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		Date logDate = new Date();
		CashFlow cf = fsm.getCashFlow(symbol,LTIDate.getYear(date),LTIDate.getQuarter(date));
		if(cf==null){
		String html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/cf?s="+symbol);
		String content = trim(com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1));
		if(this.MatchQuarter(date, content)){
			try{
			HtmlToCSV(content,symbol,date,"cashflow",5,false);
			String[] cfData = ReadCSV(symbol,date,"cashflow");
			
			Double deprAmort = Double.parseDouble(cfData[5])*1000;
			Double adjNetInc = Double.parseDouble(cfData[7])*1000;
			Double otherOperCash = Double.parseDouble(cfData[9])*1000+Double.parseDouble(cfData[11])*1000+Double.parseDouble(cfData[13])*1000+Double.parseDouble(cfData[15])*1000;
			Double capExp = Double.parseDouble(cfData[19])*1000;
			Double investment = Double.parseDouble(cfData[21])*1000;
			Double otherInvCash = Double.parseDouble(cfData[23])*1000;
			Double dividend = Double.parseDouble(cfData[27])*1000;
			Double stockSalePur = Double.parseDouble(cfData[29])*1000;
			Double netBorrow = Double.parseDouble(cfData[31])*1000;
			Double otherFinCash = Double.parseDouble(cfData[33])*1000;
			Double currencyAdj = Double.parseDouble(cfData[37])*1000;
			Double FinCF = Double.parseDouble(cfData[35])*1000;
			Double InvCF = Double.parseDouble(cfData[25])*1000;
			Double OperCF = Double.parseDouble(cfData[17])*1000;
			
			CashFlow cf1 = new CashFlow();
			cf1.setSymbol(symbol);
			cf1.setDate(this.DateFormat(cfData[1]));
			cf1.setYear(LTIDate.getYear(date));
			cf1.setQuarter(LTIDate.getQuarter(date));
			cf1.setDeprAmort(deprAmort);
		    cf1.setAdjNetInc(adjNetInc);
		    cf1.setOtherOperCash(otherOperCash);
		    cf1.setCapExp(capExp);
		    cf1.setInvestment(investment);
		    cf1.setOtherInvCash(otherInvCash);
		    cf1.setDividend(dividend);
		    cf1.setStockSalePur(stockSalePur);
		    cf1.setNetBorrow(netBorrow);
		    cf1.setOtherFinCash(otherFinCash);
		    cf1.setCurrencyAdj(currencyAdj);
		    cf1.setFinCF(FinCF);
		    cf1.setInvCF(InvCF);
		    cf1.setOperCF(OperCF);
		    
		    this.saveCashFlow(cf1);
		    Configuration.writeFinancialLog(symbol, logDate, date.getYear()+1900, LTIDate.getQuarter(date),"CashFlow done.");
		}catch(Exception e){
		}		
		}else return;
		}else return;
	}

	@Override
	public void downloadBalanceStatement(String symbol, Date date) {
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		Date logDate = new Date();
		BalanceStatement bs = fsm.getBalanceStatement(symbol,LTIDate.getYear(date),LTIDate.getQuarter(date));
		if(bs==null){
		String html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/bs?s="+symbol);
		String content = trim(com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1));
		if(this.MatchQuarter(date, content)){
			try{
			HtmlToCSV(content,symbol,date,"balancestatement",5,false);
			String[] bsData = ReadCSV(symbol,date,"balancestatement");
			
			Double cashNEquiv = Double.parseDouble(bsData[3])*1000;
			Double shortTermInv = Double.parseDouble(bsData[5])*1000;
			Double netRec = Double.parseDouble(bsData[7])*1000;
			Double inventory = Double.parseDouble(bsData[9])*1000;
			Double otherCurAsset = Double.parseDouble(bsData[11])*1000;
			Double longTermInv = Double.parseDouble(bsData[15])*1000;
			Double ppne = Double.parseDouble(bsData[17])*1000;
			Double intangibles = (Double.parseDouble(bsData[19])+Double.parseDouble(bsData[21]))*1000;
			Double otherLTAsset = (Double.parseDouble(bsData[23])+Double.parseDouble(bsData[25])+Double.parseDouble(bsData[27]))*1000;
			Double acctPayable = Double.parseDouble(bsData[31])*1000;
			Double shortTermDebt = Double.parseDouble(bsData[33])*1000;
			Double otherCurLiab = Double.parseDouble(bsData[35])*1000;
			Double longTermDebt = Double.parseDouble(bsData[39])*1000;
			Double otherLTLiab = (Double.parseDouble(bsData[41])+Double.parseDouble(bsData[43])+Double.parseDouble(bsData[45])+Double.parseDouble(bsData[47]))*1000;
			Double preferredStock = Double.parseDouble(bsData[55])*1000;
			Double commonStock = Double.parseDouble(bsData[57])*1000;
			Double retainedEarning = Double.parseDouble(bsData[59])*1000;
			Double treasuryStock = Double.parseDouble(bsData[61])*1000;
			Double otherEquity = (Double.parseDouble(bsData[51])+Double.parseDouble(bsData[53])+Double.parseDouble(bsData[63])+Double.parseDouble(bsData[65]))*1000;
			Double curAssets = Double.parseDouble(bsData[13])*1000;
			Double assets = Double.parseDouble(bsData[29])*1000;
			Double curLiab = Double.parseDouble(bsData[37])*1000;
			Double liab = Double.parseDouble(bsData[49])*1000;
			
			//set value;
			BalanceStatement bs1 = new BalanceStatement();
			bs1.setSymbol(symbol);
			bs1.setDate(this.DateFormat(bsData[1]));
			bs1.setYear(LTIDate.getYear(date));
			bs1.setQuarter(LTIDate.getQuarter(date));
			bs1.setCashNEquiv(cashNEquiv);
			bs1.setShortTermInv(shortTermInv);
			bs1.setNetRec(netRec);
			bs1.setInventory(inventory);
			bs1.setOtherCurAsset(otherCurAsset);
			bs1.setLongTermInv(longTermInv);
			bs1.setPPNE(ppne);
			bs1.setIntangibles(intangibles);
			bs1.setOtherLongTermAsset(otherLTAsset);
			bs1.setAcctPayable(acctPayable);
			bs1.setShortTermDebt(shortTermDebt);
			bs1.setOtherCurLiab(otherCurLiab);
			bs1.setLongTermDebt(longTermDebt);
			bs1.setOtherLongTermLiab(otherLTLiab);
			bs1.setPreferredStock(preferredStock);
			bs1.setCommonStock(commonStock);
			bs1.setRetainedEarning(retainedEarning);
			bs1.setTreasuryStock(treasuryStock);
			bs1.setOtherEquity(otherEquity);
			bs1.setCurAssets(curAssets);
			bs1.setAssets(assets);
			bs1.setCurLiab(curLiab);
			bs1.setLiab(liab);

			this.saveBalanceStatement(bs1);
			Configuration.writeFinancialLog(symbol, logDate,LTIDate.getYear(date), LTIDate.getQuarter(date),"BalanceSheet done.");
		}catch(Exception e){			
		}
		}
		else return;
		}else return;
	}
		
	@Override
	public void downloadIncomeStatement(String symbol, Date date) {
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		Date logDate = new Date();
		IncomeStatement is = fsm.getIncomeStatement(symbol,LTIDate.getYear(date),LTIDate.getQuarter(date)); 
	    if(is==null){
		String html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/is?s="+symbol);
		String content = trim(com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1));
		if(this.MatchQuarter(date, content)){
			try{
			HtmlToCSV(content,symbol,date,"incomestatement",5,false);
			String[] isData = ReadCSV(symbol,date,"incomestatement");	
		
			Double revenue = Double.parseDouble(isData[3])*1000;
			Double cogs = Double.parseDouble(isData[5])*1000;
			Double rnd = Double.parseDouble(isData[9])*1000;
			Double sgna = Double.parseDouble(isData[11])*1000;
			Double otherOperExp = Double.parseDouble(isData[13])*1000+Double.parseDouble(isData[15])*1000;
			Double totalOtherInc = Double.parseDouble(isData[21])*1000;
			Double intExp = Double.parseDouble(isData[25])*1000;
			Double incomeTax = Double.parseDouble(isData[29])*1000+Double.parseDouble(isData[31])*1000;
			Double discOper = Double.parseDouble(isData[35])*1000;
			Double extItem = Double.parseDouble(isData[37])*1000+Double.parseDouble(isData[41])*1000;
			Double acctChange = Double.parseDouble(isData[39])*1000;
			Double shares = this.getShares(symbol,LTIDate.getYear(date),LTIDate.getQuarter(date))[0];
			Double floats = this.getShares(symbol,LTIDate.getYear(date),LTIDate.getQuarter(date))[1];
			Double institutionHolder = this.getInstitutionHolder(symbol, LTIDate.getYear(date),LTIDate.getQuarter(date));
			Double grossProfit = Double.parseDouble(isData[7])*1000;
			Double totalOperExp = rnd+sgna+otherOperExp;
			Double operInc = Double.parseDouble(isData[19])*1000;
			Double preTaxInc = Double.parseDouble(isData[27])*1000;
			Double afterTaxInc = preTaxInc-incomeTax;
			Double netInc = Double.parseDouble(isData[43])*1000;
			
			//set value
			IncomeStatement is1 = new IncomeStatement();
			is1.setSymbol(symbol);
			is1.setDate(this.DateFormat(isData[1]));
			is1.setYear(LTIDate.getYear(date));
			is1.setQuarter(LTIDate.getQuarter(date));
			is1.setRevenue(revenue);
			is1.setCOGS(cogs);
			is1.setRND(rnd);
			is1.setSGNA(sgna);
			is1.setOtherOperExp(otherOperExp);
			is1.setTotalOtherInc(totalOtherInc);
			is1.setIntExp(intExp);
			is1.setIncomeTax(incomeTax);
			is1.setDiscOper(discOper);
			is1.setExtItem(extItem);
			is1.setAcctChange(acctChange);
			is1.setShares(shares);
			is1.setFloats(floats);
			is1.setInstitutionHolder(institutionHolder);
			is1.setGrossProfit(grossProfit);
			is1.setTotalOperExp(totalOperExp);
			is1.setOperInc(operInc);
			is1.setPreTaxInc(preTaxInc);
			is1.setAfterTaxInc(afterTaxInc);
			is1.setNetInc(netInc);
			
			this.saveIncomeStatement(is1);
			Configuration.writeFinancialLog(symbol, logDate, LTIDate.getYear(date), LTIDate.getQuarter(date),"IncomeStatement done.");
		}catch(Exception e){			
		}
		}
			else return;
	}else return;
	}
	
	@Override
	public void downloadFinancialStatement(String symbol, Date date) {   
		this.downloadIncomeStatement(symbol, date);
		this.downloadCashFlow(symbol, date);
		this.downloadBalanceStatement(symbol, date);	
	}

	@Override
	public void downloadBalanceStatement(String symbol, int year, int quarter) {
		Date tmp = LTIDate.getDate(year, quarter*3, 1);
		this.downloadBalanceStatement(symbol, tmp);
	}

	@Override
	public void downloadCashFlow(String symbol, int year, int quarter) {
		Date tmp = LTIDate.getDate(year, quarter*3, 1);
		this.downloadCashFlow(symbol, tmp);	
	}

	@Override
	public void downloadIncomeStatement(String symbol, int year, int quarter) {
		Date tmp = LTIDate.getDate(year, quarter*3, 1);
		this.downloadIncomeStatement(symbol, tmp);
	}

	@Override
	public void downloadFinancialStatement(String symbol, int year, int quarter) {
		Date logDate = new Date();
		Date tmp = LTIDate.getDate(year, quarter*3, 1);
		this.downloadFinancialStatement(symbol, tmp);
		//Configuration.writeFinancialLog(symbol, logDate, year, quarter,"FinancialStatement done.");
	}

	@Override
	public void downloadFinancialStatement(String[] symbol, int year,int quarter) {
		for(int i=0;i<symbol.length;i++){
			this.downloadFinancialStatement(symbol[i], year, quarter);
		}
	}
	
	public static boolean MatchQuarter(Date date,String content){
		String tmp = date.toString();
		if(LTIDate.getQuarter(date)==4&&(content.contains("Dec"+" "+tmp.substring(26,28))||content.contains("Nov"+" "+tmp.substring(26,28))||content.contains("Oct"+" "+tmp.substring(26,28))))return true;
		else if(LTIDate.getQuarter(date)==3&&(content.contains("Sep"+" "+tmp.substring(26,28))||content.contains("Aug"+" "+tmp.substring(26,28))||content.contains("Jul"+" "+tmp.substring(26,28))))return true;
		else if(LTIDate.getQuarter(date)==2&&(content.contains("Jun"+" "+tmp.substring(26,28))||content.contains("May"+" "+tmp.substring(26,28))||content.contains("Apr"+" "+tmp.substring(26,28))))return true;
		else if(LTIDate.getQuarter(date)==1&&(content.contains("Mar"+" "+tmp.substring(26,28))||content.contains("Feb"+" "+tmp.substring(26,28))||content.contains("Jan"+" "+tmp.substring(26,28))))return true;
		else return false;
	}
	public static boolean MatchYear(int year,String content){	
		String tmp = String.valueOf(year);
		if(content.contains(" "+tmp.substring(2, 4)))return true;
		else return false;
	}
	
	public static String trim(String content){
		char[]a=content.toCharArray();
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<a.length;i++){
			if(i>100&&a[i]=='-'){a[i]='0';sb.append(a[i]);}
			if(a[i]=='(')a[i]='-';
			if(!(a[i]==','||a[i]=='$'||a[i]==')'))sb.append(a[i]);
		}
		return sb.toString();	
	}
	
	public static Date DateFormat(String d){
		Date date = null;
		String[] tmp = d.split(" ");
		String tmp1;
		SimpleDateFormat f = new SimpleDateFormat("yy/MM/d");
		try{
		if(d.contains("Jan")){
			tmp1 = tmp[2]+"/"+"01"+"/"+tmp[1];
			date = f.parse(tmp1);
		}
		else if(d.contains("Feb")){
			tmp1 = tmp[2]+"/"+"02"+"/"+tmp[1];
			date = f.parse(tmp1);
		}
		else if(d.contains("Mar")){
			tmp1 = tmp[2]+"/"+"03"+"/"+tmp[1];
			date = f.parse(tmp1);
		}
		else if(d.contains("Apr")){
			tmp1 = tmp[2]+"/"+"04"+"/"+tmp[1];
			date = f.parse(tmp1);
		}
		else if(d.contains("May")){
			tmp1 = tmp[2]+"/"+"05"+"/"+tmp[1];
			date = f.parse(tmp1);
		}
		else if(d.contains("Jun")){
			tmp1 = tmp[2]+"/"+"06"+"/"+tmp[1];
			date = f.parse(tmp1);
		}
		else if(d.contains("Jul")){
			tmp1 = tmp[2]+"/"+"07"+"/"+tmp[1];
			date = f.parse(tmp1);
		}
		else if(d.contains("Aug")){
			tmp1 = tmp[2]+"/"+"08"+"/"+tmp[1];
			date = f.parse(tmp1);
		}
		else if(d.contains("Sep")){
			tmp1 = tmp[2]+"/"+"09"+"/"+tmp[1];
			date = f.parse(tmp1);
		}
		else if(d.contains("Oct")){
			tmp1 = tmp[2]+"/"+"10"+"/"+tmp[1];
			date = f.parse(tmp1);
		}
		else if(d.contains("Nov")){
			tmp1 = tmp[2]+"/"+"11"+"/"+tmp[1];
			date = f.parse(tmp1);
		}
		else{
			tmp1 = tmp[2]+"/"+"12"+"/"+tmp[1];
			date = f.parse(tmp1);
		}
		}catch(Exception e){
			
		}
		return date;
	}
	public static String getPath(String symbol,Date date,String statementType){
		String str = System.getProperty("os.name").toUpperCase();
		String sysPath;
		if (str.indexOf("WINDOWS") != -1) 
			sysPath=System.getenv("windir")+"\\temp\\";	
			else 
				sysPath = "/var/tmp/finanacialstatment/";	
		File f = new File(sysPath);
		if(!f.exists()){
		    f.mkdirs();           
		}
		int year = date.getYear()+1900;
		int quarter = LTIDate.getQuarter(date);
		String filePath = sysPath+year+"-"+quarter+"-"+symbol+"-"+statementType+".csv";
		return filePath;
	}
	
	/**
	 * find certain CSV file in the directory 
	 * @author SuPing
	 * @param symbol
	 * @param statementType
	 * @return
	 * 2009/10/22
	 */
	public static List<String> getRexPath(String symbol,String statementType){
		String sysPath;
		String str = System.getProperty("os.name").toUpperCase();
		ArrayList<String> files = new ArrayList<String>();
		ArrayList<String> tfiles = new ArrayList<String>();
		if (str.indexOf("WINDOWS") != -1) 
			sysPath=System.getenv("windir")+"\\temp\\";	
			else 
				sysPath = "/var/tmp/finanacialstatment/";	
		filelist(files,sysPath);
		try{
			for(int i=0;i<files.size();i++){
				String[] tmp = files.get(i).split("-");
				if(tmp.length!=4) continue;
				if(tmp[2].equals(symbol)&&tmp[3].equals(statementType+".csv")){
					//System.out.println("done"+files.get(i));
					tfiles.add(files.get(i));
			}
		}
		}catch(Exception e){e.printStackTrace();}
		return tfiles;
	}
	
	/**
	 * @author SuPing
	 * get the file directory list
	 * 2009/10/22
	 */
	public static void filelist(List<String> lst,String path){   
		File f = new File(path);
		if(!f.exists()) f.mkdirs();           
		if(f.isDirectory()){   
			lst.add(f.getAbsolutePath()+"\\");   
			String dirs[] = f.list();   
			for(int i=0;dirs!=null&&i<dirs.length;i++){   
				filelist(lst,f.getAbsolutePath()+"\\"+dirs[i]);   
			}   
		}   
		if(f.isFile())lst.add(f.getAbsolutePath());   
	}
	
	public static Date HtmlToCSV(String content,String symbol,Date date,String statementType,int count,Boolean latest){
		String tmpContent = content;
		int dateAt=0;
		
 		if(tmpContent.equals(""))return null;
		
 		try{
		//trim the html text
		String[]tmp1=tmpContent.split("\n");
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<tmp1.length;i++){
			sb.append(tmp1[i]);
		}
		
		String[]tmp = sb.toString().split("#");	
		int startAt=0;
		
		for(int i=0;i<tmp.length;i++){
			if(tmp[i].equals("PERIOD ENDING"))startAt = i;
		}
		
		ArrayList list = new ArrayList();
		for(int i=startAt;i<tmp.length;i++){
			//if(tmp[i].startsWith("(")&&tmp[i].endsWith(")"))tmp[i]="-"+tmp.
			if(!tmp[i].equals(""))list.add(tmp[i]);
		}
       
		for(int i=0;i<count;i++){
			//if count equals 5,the page is a quarterly financial statement page;if count equals 4,the page is a annual financial statement page
			if((count==5&&MatchQuarter(date,list.get(i).toString()))||(count==4&&MatchYear(LTIDate.getYear(date),list.get(i).toString()))){
			dateAt=i;
			}
        }
		String filePath;
		System.out.println(DateFormat(tmp[startAt+1]));
		if(latest)filePath = getPath(symbol,DateFormat(tmp[startAt+1]),statementType);
		else filePath = getPath(symbol,date,statementType);
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try{
			fos = new FileOutputStream(filePath);
			osw = new OutputStreamWriter(fos,"utf-8");
			bw = new BufferedWriter(osw);
			if(!tmp.toString().isEmpty())
			{
				for(int i=0;i<list.size();i++){
					if(latest){
						if(i%count==0)bw.write(list.get(i)+",");
						if((i+count-1)%count==0)bw.write(list.get(i)+",");
					}
					else{
					if(i%count==0)bw.write(list.get(i)+",");
					if((i+count-dateAt)%count==0)bw.write(list.get(i)+",");
					}
				}
			}
			    bw.flush();
				bw.close();
				osw.close();
				fos.close();	
		}catch(IOException e){
			e.printStackTrace();
		}
		return DateFormat(tmp[startAt+1]);
 		}catch(Exception e){
 		    return null;
 		}
	}
	
	/**
	 * modify the HtimlToCSV to copy the all updates from Yahoo 
	 * 2009/10/22
	 * @author SuPing
	 * @param content
	 * @param symbol
	 * @param date
	 * @param statementType
	 * @param count
	 * @return
	 */
	public static Date[] HtmlToCSV(String content,String symbol,Date date,String statementType,int count){
		String tmpContent = content;
		int dateAt=0;
		
 		if(tmpContent.equals(""))return null;
		
 		try{
 			//trim the html text
 			String[]tmp1=tmpContent.split("\n");
 			StringBuffer sb = new StringBuffer();
 			for(int i=0;i<tmp1.length;i++){
 				sb.append(tmp1[i]);
 			}
		
 			String[]tmp = sb.toString().split("#");	
 			int startAt=0;
		
 			for(int i=0;i<tmp.length;i++){
 				if(tmp[i].equalsIgnoreCase("Period Ending"))
 					startAt = i;
 				break;
 			}
		
 			ArrayList list = new ArrayList();
 			for(int i=startAt;i<tmp.length;i++){
 				//if(tmp[i].startsWith("(")&&tmp[i].endsWith(")"))tmp[i]="-"+tmp.
 				if(!tmp[i].equals(""))list.add(tmp[i]);
 			}
       
 			for(int i=0;i<count;i++){
 				//if count equals 5,the page is a quarterly financial statement page;if count equals 4,the page is a annual financial statement page
 				if((count==5&&MatchQuarter(date,list.get(i).toString()))||(count==4&&MatchYear(LTIDate.getYear(date),list.get(i).toString()))){
 					dateAt=i;
 				}
 			}
 			String[] filePath=new String[count-1];
 			Date[] update=new Date[count-1];
 			int updateAt=startAt+1;
 			
 			for(int j=0;j<count-1;j++){
 				update[j]=DateFormat(tmp[updateAt]);
 				filePath[j] = getPath(symbol,DateFormat(tmp[updateAt]),statementType);
 				FileOutputStream fos = null;
 				OutputStreamWriter osw = null;
 				BufferedWriter bw = null;
 				try{
 					fos = new FileOutputStream(filePath[j]);
 					osw = new OutputStreamWriter(fos,"utf-8");
 					bw = new BufferedWriter(osw);
 					if(!tmp.toString().isEmpty())
 					{
 						for(int i=0;i<list.size();i++){
 							if(i%count==0)bw.write(list.get(i)+",");
 							if((i+count-1)%count==0)bw.write(list.get(i+j)+",");
 						}
 					}
 					bw.flush();
 					bw.close();
 					osw.close();
					fos.close();	
 				}catch(IOException e){
 					e.printStackTrace();
 				}
 				updateAt++;
 			}
 			return update;
 			}catch(Exception e){
 		    return null;
 		}
	}
	
	
	public static String[] ReadCSV(String symbol,Date date,String statementType){
		StringBuffer sb = new StringBuffer();    
		try{
			String filePath = getPath(symbol,date,statementType);
			File f = new File(filePath);
		    BufferedReader br = new BufferedReader(new FileReader(f)); 
		    String stemp; 
		    while((stemp=br.readLine())!=null){
		    	sb.append(stemp);
		    }
		    br.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){		
		}
		
		String[] data = sb.toString().split(",");
		return data;
	}
	
	/**
	 * Read the CSVs which had download
	 * 2009/10/22
	 * @author SuPing
	 * @param symbol
	 * @param filePath
	 * @param statementType
	 * @return
	 */
	public static String[] ReadCSV(String symbol,String filePath,String statementType){
		StringBuffer sb = new StringBuffer();    
		try{
			File f = new File(filePath);
		    BufferedReader br = new BufferedReader(new FileReader(f)); 
		    String stemp; 
		    while((stemp=br.readLine())!=null){
		    	sb.append(stemp);
		    }
		    br.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){		
		}
		String[] data = sb.toString().split(",");
		return data;
	}

	/**
	 * get share outstanding from the most recent report
	 * modify by SuPing
	 * 2009/10/27
	 */
	public static Double[] getShares(String symbol,int year,int quarter){
		Double[] values = {0.0,0.0};
		
		String html="html";
		String result="result";
		int num=0;
		try{
			while(html.equals("html")&&num<=30){
				html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/ks?s="+symbol);
				num++;
					if(html==null) html="html";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		html=com.lti.util.html.ParseHtmlTable.cleanHtml(html);
		int c=com.lti.util.html.ParseHtmlTable.countTable(html);
		String tmp=null;
		int targetAt=0;
		for(int i=0;i<c;i++){
			tmp=com.lti.util.html.ParseHtmlTable.getTable(html,i);
			if(tmp!=null&&tmp.contains("SHARE STATISTICS")){
				targetAt=i;
				break;
			}
		}
		String targetTable = com.lti.util.html.ParseHtmlTable.getTable(html,targetAt);
		if(targetTable!=null){
			while(result.equals("result")){
				result=com.lti.util.html.ParseHtmlTable.extractTableContent(targetTable.toCharArray(), 0, targetTable.length()-1,100,2,100);
				if(result==null) result="null page";
			}
		}else{
			values[0]=-998000.0; values[1]=-997000.0;
			return  values;
		}
		if(result.equals("null page")) {
			values[0]=-998000.0; values[1]=-997000.0;
			return  values;
		}else{
			String[] elements = result.split("#");
			//set shares
			for(int i=0;i<elements.length;i++)
				if(elements[i].equals("N/")||elements[i].equals("N/A")) elements[i]="-9990";
			try{
				if(elements[5].endsWith("B"))values[0]=Double.parseDouble(elements[5].substring(0, elements[5].length()-1))*1000000000;
				else if(elements[5].endsWith("M"))values[0]=Double.parseDouble(elements[5].substring(0, elements[5].length()-1))*1000000;
				else values[0]=Double.parseDouble(elements[5].substring(0, elements[5].length()-1))*1000;
				//set float shares
				if(elements[7].endsWith("B"))values[1]=Double.parseDouble(elements[7].substring(0, elements[7].length()-1))*1000000000;
				else if(elements[7].endsWith("M"))values[1]=Double.parseDouble(elements[7].substring(0, elements[7].length()-1))*1000000;
				else values[1]=Double.parseDouble(elements[7].substring(0, elements[7].length()-1))*1000;
			}catch(Exception e){
				e.printStackTrace();
			}
			return values;
		}
	}
	
	//get Institution Holders 
	public static Double getInstitutionHolder(String symbol,int year,int quarter){
		Double holders = 0d;
		String html="html";
		int num=0;
		try{
			while(html.equals("html")&&num<=30){
				html=ParseHtmlTable.getHtml("http://finance.yahoo.com/q/mh?s="+symbol);
				num++;
				if(html==null) html="html";
			}
			html=ParseHtmlTable.cleanHtml(html);
			int c = ParseHtmlTable.countTable(html);
			int flag = 0;
			for(int i=0;i<c;i++){
				String tmp = ParseHtmlTable.getTable(html, i);
				if(tmp!=null&&tmp.contains("NUMBER OF INSTITUTIONS HOLDING SHARES")){flag=i;break;
				}
			}		
			String targetTable=ParseHtmlTable.getTable(html, flag);
			String result=ParseHtmlTable.extractTableContent(targetTable.toCharArray(), 0, targetTable.length()-1,200,1,200);
			String[] elements = result.split("#");
			for(int i=0;i<elements.length;i++){
				if(elements[i].contains("NUMBER OF INSTITUTIONS HOLDING SHARES")&&!elements[i+1].equals(""))
					holders = Double.parseDouble(elements[i+1]);
				if(holders==null) holders=-9999.0;
			}
			System.out.println(holders);
		}catch(Exception e){}
		return  holders;
    }
	
	
	//test
	public static void main(String[] args){
		FinancialStatementManagerImpl fsm = (FinancialStatementManagerImpl)ContextHolder.getInstance().getApplicationContext().getBean("financialStatementManager");
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");

		long t1 = System.currentTimeMillis(); 
		 
		//String[] a = {"APXR","APHTQ","APIA","APOG","APRB","ATCS","AGT","APOL","AINV","ASOE","APLL","APAB","APPS","AAPL","ARCI","APDN","AERG","AIT","AMAT","AMCC","APNT","APNS","APSG","APSO","ATHYQ","AOPL","AREX","ATR","WTR","AQCRE","AQAS","AQUA","AQUM","AQRO","AQNM","AQIS","ARSD","ARA","ARDM","ADYE","ARBX","ARB","ARBE","ABR","ARCW","ABIO","ACDQ","KAD","ARCAY","MT","ACGL","ARJ","ACI","ADM","ARHN","ARST","ACAT","RDEA","ARDNA","ARDTQ","ADNT","ARNA","ARD","ARCC","ARET","AGX","AGXM","AGII","STST","AGNT","ARGL","ARIS","ARAH","ARIA","ARBA","ADSPQ","AWYI","RAMS","AISIQ","ARINA","ARTL","ARKR","AKDS","ABFS","HOST","ARMH","AMNF","AWI","ARNI","ARTX","ARQL","ARRY","ARYC","HRT","ARRS","ARW","AROW","ARWD","ARWR","ARTG","ARTEQ","ARTNA","ARTI","ARTC","AJG","ALIF","ARTD","ARTW","ARUN","ARM","ARYX","ASAL","ASA","ASNL","ASTTY","ASBN","ABG","ASCQ","ASDS","ASCBQ","ASCMA","ASTI","ASCD","ASFJ","AHT","ASH","ASIT","AKRK","AEPW","AFBR","ASGXF","AAGH","AWRCF","ATVG","CIO","GRR","ATYM","ASIA","AAMA","AADG","CLCE","ASMI","ASML","ASPM","ASPN","ASRPF","AHL","AZPN","APPY","ASPO","APIT","APY","AACC","ALC","ASBC","AEC","ASMH","ASAM","AIZ","ASDI","AGO","APHY","ASFI","ATEA","ASTE","AF","ASTR","ATTG","AZN","ALOT","ATCCQ","ATRO","ASTC","ASYTQ","T","ATAI","AHNA","ATAC","AHNCQ","ATGCQ","ATHN","AGIXQ","ATHR","ATHX","AAME","ATBC","ATBA","ACFC","AGLFQ","APBL","ASFN","ATNI","AWIN","ATPL","ATNO","AXG","AAWW","ATLS","ATLR","ATN","ALMI","ATOG","AHD","APL","ALSI","ATYG","ATML","ATMI","ATO","AGWTQ","ATGU","ATOC","ATPG","ATRC","ATRI","ATSI","ATSX","ATTD","ATTUF","AWMM","ATW","AUO","ABBB","AUBN","AUDC","AUIO","VOXX","AZC","AGDI","ARAU","AULO","ARGA","AZK","AOGS","ASMFQ","ACKO","AUSPF","ANZBY","AOGC","AUCAF","AUTH","ADAT","AUZR","ADNW","ADWT","AUGR","ABTL","ADSK","AIMM","AUTO","ALV","ADP","AN","AZO","AUXO","AUXL","CITY","AWX","AOGN","AVRX","AVB","AVIT","AVNR","AAIR","AVTO","AVTR","AVSM","AVSO","AVATQ","AVXT","AVNY","AVRNQ","AVNT","AVNU","AVRO","AVY","AVSS","AVII","AVGE","AVCEE","AVID","AVGN","AVMC","ASGMF","CAR","AVA","AVSR","AVTI","AVIX","AVZA","AVT","AVCT","AVP","AVPI","AVTMQ","AVVH","AVX","AWRE","AXA","ACLS","AXSI","AFT","AGIJ","AXIH","AXPW","AXS","AXMP","AXSO","AXYS","AXTI","AXTC","AZGS","AZUL","AZUEF","AZZ","BHO","BGS","BFTC","BHIT","BOSC","BVRSF","BTWO","BTDG","BABB","FLY","BWEBF","BMI","BPMI","BIDU","BKBO","BHI","BKRS","BCPC","BEEI","BEZ","BWINB","BLD","BLL","BTN","BLDP","BRSI","BYI","BLQN","BLSV","BANF","BCIS","BBV","BBD","BCH","BLX","BMA","STD","SAN","CIB","BKJ","BARI","TBBK","BXS","BCV","BCFT","BTFG","TBHS","BKMU","BAC","BONC","BOCH","BKEAY","BOFL","GRAN","BOH","IRE","BKYF","BMRC","BOMK","BMO","BK","BNS","BKSC","BCAR","OZRK","BOVA","BBX","BSTR","BFIN","BGVF","RATE","BNX","BKUNQ","BANR","BAA","BNYN","BHNN","BHB","BCLR","BCS","BARE","BKPG","BKS","B","BRN","BBSI","ABX","TPUT","BOGA","BASFY","BSIC","BAS","BWTR","BSET","BATY","BTIOQ","BAX","BAYK","BAYN","BAYRY","BYHL","BYLK","BYCX","BTE","BAYW","BHLLF","BBT","BBMO","BBNG","BBVUF","BFR","BCBP","BCE","BCSB","BEAV","BESIY","BFNB","BCOEE","BFED","BCON","BCND","BECN","BRCO","BGPTQ","BBGI","BZH","BEBE","BEC","BDX","BBBY","BESE","BEIC","BJGL","BELFB","BDC","BLLI","BELM","BVIG","BLUS","BLC","BGCC","BMS","BFFI","BMBN","BHE","BPMA","BNCL","BNGPY","BNHNA","BEVFF","BKLYY","BERK","BRK.A","BHLB","BERL","CHBD","BRY","BBY","BEYS","BWAV","BHBC","BNV","BXLC","BYOC","BFCF","BRGYY","BGCP","BGII","BHP","BBL","BDGV","BIDZ","BGFV","BBUCQ","BCTE","BIG","BRBMF","BSKO","BBND","BGST","BSGC","BBG","BMNM","BNGOF","BISU","BGES","BITI","BKYI","BMSN","BPTH","BIO","BRLI","BASI","BAZH","BCRX","BIOD","BDSI","BZEC","BFNH","BFRM","BIOF","BFLSE","BIIB","BIFC","BHRT","BJCT","BLGO","BLTI","BLFS","BMRN","BMSPF","BMR","BMTL","BMRA","BSYT","BMTI","BNET","BONU","BNVI","BPAC","BIPH","BPUR","BPAX","BIOS","BSHF","BSTC","BSMD","BIOHF","BBH","BMCS","BTEL","BTIM","BTRNQ","BVF","BVTI","BRBH","BMJ","BBBI","BDMS","BITS","BZCN","BJS","BJRI","BJ","BKFG","BDK","BBOX","BKH","BKTK","BLKB","BBBB","BHWF","BQR","BKT","BKCC","BTA","BCF","BLK","BX","ADRA","ADRD","ADRE","ADRU","BSAL","BLKL","BBI","BDR","BLT","BLU","BCSI","BDCO","BESN","BLHI","NILE","BLRGZ","BRBI","BSI","BVBC","BLZN","BFRE","BFLY","BGAT","BXG","BXC","BPHX","BLPT","BLUG","BTH","KAZ","BMC","BJGP","BMXD","BNCN","BNCC","BNSSA","BWP","BOWFF","BOBE","BBCZ","BA","BOFI","BOGN","BZ","BOKF","BOLDQ","BOLV","BOLL","BOLT","BONT","BONL","BONZ","BGOIE","BNLB","BDCG","BNFR","BNSO","BOTX","BAMM","BKTC","BMER","WEL","BGP","BOREF","BWA","BORL","BSHI","SAM","BPFH","BXP","BSX","BORT","EPAY","BBTC","BDLSQ","BOVD","BVX","BWL.A","BWTL","BNE","BYD","BP","BPT","BPIGF","HAXS","BPW","BPZ","BRC","BCLI","BRHI","BRCI","BNDT","BDN","BBDC","BRP","BTM","BAK","BRVO","BZOFF","BOBS","BRZM","BRE","BWAY","BWLRF","BZC","BRZVE","BBEP","BFDI","BDTE","BRER","BRZZQ","BDGE","BBNK","BRDG","BLSW","BPI","BRDCY","BGTH","BRID","BGG","BEXP","CELL","BTSR","BCAHY","BDLN","BCO","CFL","EAT","BNXR","BMY","BRS","BBLKF","BAIRY","BTI","BSY","BKBK","BCST","BCASE","BRCM","BDLF","BPSG","BR","BVII","BVSN","BYFC","BWBA","BWEN","BRCD","BRNC","BKD","BRKC","BXXXQ","BAM","BHS","BIP","BPO","BRKL","BCKE","BFSB","BMXI","BRKS","BKSD","BRO","BWS","BF.B","BWMG","BRT","BRKR","BRBW","BC","BVYH","BW","BMTC","BRYN","BSDM","BSKT","BSML","BSQR","BT","BTUI","BTXN","BGH","BPL","BKI","BKE","BUCY","BWLD","BBW","BLDR","BLGM","BULM","BLRVQ","BLVT","BG","BZLFY","BKC","BMLS","BNI","BRST","BZYR","BVNI","BUTL","BUKS","BZTG","BVFL","BWY","BWIH","CHP","CFFI","CDDT","CFON","CHRW","BCR","COBT","CAGI","CA","CAB","CVC","CBT","CCMP","COG","CACH","CACI","CBY","CADD","CDNS","CADE","CADX","CDZI","KDUS","CGT","CGL.A","CAP","DVR","CALM","CCAA","CAAUF","CLMS","CHY","CAMP","CVGW","CALVF","CCC","CALC","CFNB","CLGL","CAMD","COGC","CPKI","CWT","CALP","CLNW","ELY","CALD","CLSP","CPE","CLWY","CALL","CPN","CTON","CLMT","CLYW","CBMCE","CBEX","CBM","CCHI","CNGG","CAMH","CDGD","CAFI","CAELU","CAC","CPT","CCJ","CAML","CPFR","CAM","CMSF","CBLRF","CPB","CAMT","CCRE","CM","CNI","CNQ","CP","CSIQ","SNG","CZICF","COWP","CNND","CANR","CTHP","CLZR","CVRX","CNDL","CANM","CBIS","BIKEQ","CFW","CAJ","CMN","CITI","CPHC","CYBA","CGYNQ","CBNJ","CAPE","CYSG","CPLA","CBKN","CBEV","CCBG","CETG","CCOWQ","CGLD","CGSYE","CMKT","COF","CPLP","CPTP","CRSVF","CRRA","CSU","CSWC","CT","CSE","CAPB","CLA","CBC","CFFN","LSE","CAPI","CPSO","CMO","CPST","CPD","CSAR","CBZFF","CRR","CABN","CICS","CDVTE","CDY","CSCX","CRDC","CADM","CDBK","CFNL","CAH","CDIC","CGCP","CRME","BEAT","CVBT","CSII","CXM","CDOM","CATM","CRE","CADV","CECO","CRGO","CAHR","CGAQ","CBOU","CSL","KMX","CKEC","CCL","CUK","CLBH","CART","CRPL","CRS","CSV","CRZO","CRRB","TAST","CRI","CARV","CASM","CACB","CAE","CASB","CSCD","HOO","CSEF","CWST","CASY","CSH","CTQN","CSSV","CASS","ROX","CAGU","CHOD","MOAT","CMRG","CALA","CHSI","CYSU","CPRX","CATT","CTHH","CAT","CATY","CTR","CTTY","CAV","CVCO","CVAT","CAVM","CBG","CBEY","CBZ","CBL","CBS","CCMO","CAW","CCFH","CCFN","CDWR","CHINA","CDI","CDSI","CWDW","CFK","CEAH","CEC","CECB","CECE","CECS","FUN","CDR","CVM","CLDN","CE","CRA","CLTY","CLS","CLXX","CELG","CEGE","CRII","CTIC","CLWL","CTIX","CEL","CCYG","CLDX","CLMI","CLPTQ","CLTK","CLST","CLSN","CSUH","CX","CNC","CYCL","CHLE","CNBC","CLFC","CFWH","CNP","CSCC","CSFL","CTX","CEBK","CEDC","CETV","CFBK","CEF","CENT","GTU","CJBK","CENMF","CTNR","CPF","CET","CVCY","CV","CVBK","CWIR","TRUE","CGHI","CENX","CNBKA","CNTY","CYPE","CTL","CVO","CEPH","CEHC","CPHD","CRDN","CGXP","CRNT","CERP","CERN","CERS","CEVA","CF","CFIM","CFOS","CITZ","CGV","GIB","CGXEF","CHG","CHAD","CCCFF","CHQGF","CCMS","CHB","CHMP","CREBQ","CSBR","CHAG","CAON","CIHI","CYOU","CPEU","CACAU","CTHR","CRL","SCHW","CHIC","CHRS","GTLS","CHTRQ","CHFN","CHRTD","CWLT","CCF","CPKA","CHWD","CHTT","CHDO","CHKP","CKP","CAKE","CHEL","CHTP","CHRX","CEMI","CHE","CXSP","CHFC","CTKI","CEMJQ","CHMG","CQP","LNG","CHKJ","CHKE","CSKEQ","CHK","CPK","CHEUY","CHEV","CVX","CBI","CVR","CHS","CBNK","CFCM","CITC","PLCE","CH","CIM","CHCG","CADC","CHBU","CAGC","CHAS","CAGTF","CAAH","CAXG","CAEI","CNAM","CALG","CAAS","CBAK","CBEH","CHHB","CHBOE","CHBP","CBTT","CBBD","CCCI","CABL","CHGI","CCGY","CCTR","CHDA","CHID","CDGT","STV","CDII","CADUE","CDYT","DL","CDSG","CEA","CEUA","CHGY","CGYV","CSHEF","CGRP","CXTI","JRJC","CHFI","CFSG","CHIF","CHFY","CHFR","CHN","CFQUF","CGWY","CHGS","CGA","CAGM","GRRF","CGDI","CNHL","CHHE","CNHC","CHRI","CRRWF","HOL","CHHL","CHLN","CHRN","CIWT","CPBY","CIIC","CHIO","CIVS","CKGT","LFC","CHLO","CMFO","CMM","CMDA","CMED","CHME","CHL","CHMO","CHNG","CHNR","NPD","CNEH","CNOA","SNP","CPHI","CPSL","CPLY","CPDV","CREG","CRTP","CRJI","CSR","SHZ","KUN","CSGJ","CSKI","ZNH","CSOF","CSGH","CSUN","CSXB","CNTF","CTDC","CTGLF","CHTL","CHA","CTFO","CHU","CVVT","CVDT","CHWG","CWSI","CWLC","CXDC","CYIG","CYID","CYIP","CYXI","CYXN","CHYU","CYD","CHBT","CAST","CEDU","CHWE","CHDX","IMOS","CMG","CQB","CIEC","CCYS","CHH","COFS","CHRD","CBK","CDXC","CRC","CB","CHT","CHD","CHDN","CHYR","CTMCY","CSBHY","CBR","CICN","CIEN","CI","XEC","CIMT","CMXX","CBB","CINF","CNK","CRWFF","CTAS","CNCN","CPCIQ","CIR","CCTYQ","CSYI","CRUS","CIRC","CSCO","CNWT","CIT","CTDB","CTRN","C","CZNC","CZBT","CZBS","CZWI","CZYB","CIWV","CFIN","CZFS","CTZN","CZFC","CIZN","CRBC","CSBC","CIA","CTXS","CTBK","CTCC","CDEVY","CHCO","CYLN","CYN","CSNY","CTEL","CTVWF","CKR","CKFB","CKRU","CKXE","CKX","IONN","CLAI","CLSI","CLC","CLRN","CLRT","CLAR","GLA","CKFC","CLRS","TCGI","CGR","GOF","CWEI","CDTI","CLNE","CLH","CPWE","CLTH","CCO","CSKH","CLRA","CLFD","CCBEF","CLRO","CPBR","CSYS","CLW","CLWR","CNL","CBLI","CKEI","CKSW","CLF","CSBK","CLDA","CLX","CLPHY","CLHI","CLXM","CLYVE","CME","CMSB","CMS","CNA","SUR","CNBZ","CFNA","CCNE","CNH","CISG","CEO","CNSO","CXG","CTTD","CIGIQ","COH","COHM","CRV","CBCO","COCBF","COTE","CLSC","COBZ","COBR","COKE","KO","CCE","KOF","CCH","CCON","CODL","CDMA","CVLY","CDE","JVA","CSA","CCOI","COGT","CGNX","CTSH","COGO","CNS","INB","COHR","COHU","CSTR","CWTR","CCIX","CFX","CL","CCNG","PSS","CLCT","CGEG","CLXS","CGDF","CNB","COBK","CCOM","CLP","CBAN","CIIG","CGFIA","CBBO","COLB","CBRX","COLM","BUS","CGSE","CMCO","CMRO","CBMX","CRXX","CMCA","CMCSA","CMDZQ","CDCO","CMA","CFS","FIX","CCBP","CCNI","MOC","CBSH","CMNR","CGCO","CMFB","CPLT","CMOH","ZCCCF","CMC","CEFC","CNAF","CVGI","CRZBY","CXIA","COES","CCLNQ","CWBS","CBTE","CTV","CTCH","CICI","JCS","CBON","CBIN","CBU","BTC","ALBY","CPBK","CCBD","CFFC","CFIS","CMFP","CYH","CIBN","CMTI","CPBC","CSHB","CTBI","CVLL","CWBC","CBSO","CVLT","CBD","ABV","SBS","CIG","ELP","SID","CCU","BVN","COGE","CODI","CMP","CML","CTT","CCMI","CPX","COPI","CPTC","CHCR","CMHS","CMZ","CCRT","CGEN","CMPD","CHZS","CLCXQ","CPSI","CSC","CSVI","CSWI","CTGX","COIB","CPWR","CIX","SCOR","CHCI","CRK","CITP","CMTL","CMTX","COMV","CMVT","CNW","CAG","CDIR","CPTS","CXO","CNCG","LENS","CCVR","CGAM","CCSG","CNQR","CCUR","CONC","CNXT","CFRI","CNRB","CGMC","CNMD","CMHM","CONN","CNCM","CTBC","CTWS","COP","CNLG","CNRD","CNO","CNX","CNSL","ED","CFWEQ","CGX","CSLMF","CWCO","CTO","CNSV","CPYE","CTCT","CDDDQ","STZ","CEG","CEP","CSLR","XLY","CPSS","XLP","CBKM","CNSF","CSGI","MCF","CTTAY","CAL","CPPXF","CNFU","CISC","CUO","KMKCF","CLR","CNU","CNVR","CVRG","CVG","CVNS","CVTL","COIN","CGRTQ","COO","CBE","CTB","COOP","CPA","CPNO","CPRT","CNIC","COYN","COPY","CLHRF","CORT","CBAI","CDXP","CORG","CLB","CMT","CORE","CREL","CFCC","CONX","CRGIY","CMNFY","COCO","CPO","CRN","CSBQ","CFP","CRTX","CWRL","GLW","CNIG","COWI","BCA","CDURQ","EXBD","OFC","CUSRF","CXW","ETQ","CPROF","COR","CLDB","CORS","CJR","CRVL","CZZ","COSI","COSN","CSMO","CPWM","CSGP","COST","COT","CGRB","CPAH","CMFI","CRRC","CUZ","CVD","CVA","CVTI","CVH","COVR","CVST","COWN","CWLZ","CPCF","CPEX","CPL","CVU","CPY","CPII","CPSH","CRAI","CBRL","HOOK","CRAF","CRFT","CR","CRD.B","CRAY","CRZY","CBSJ","CRTV","CREAF","BAP","CACC","COFI","CIK","CS","DHY","CRMZ","CRED","CREE","CSNT","CRFN","CRESY","CRUJF","CRH","CXPO","CRPI","CRMH","CROX","COFF","CHBH","CCRN","CRT","CXZ","CRDS","XTXI","XTEX","CRWTF","CRWG","CRAN","CCI","CRWS","CRWE","CFGI","CCK","CRWN","CWNH","CWOIE","CRXL","CKGRQ","CCEL","CRY","CYRX","CRYP","CYTGF","CYRV","KRY","CSBB","CSGS","CSPI","CSS","CSX","CTCM","CTDH","CTIG","CTIB","CTRP","CTS","CUB","QBC","CBST","CUDO","FZN","CFR","CFI","CMI","CMLS","CRGN","CUTC","CRIS","CRNM","CW","SRV","CUTR","CVBF","CVV","CNVT","CVI","CVS","CXTO","CYLU","CYAN","CYDM","CYBD","CYDE","CYRD","CYKN","CYBL","CBMH","CYBX","CYBE","CYBS","CYBV","CYBI","CYRP","CYDI","CYCC","ATC","CYLO","CYDS","CYIO","CYMI","CYNEA","CYNO","CPBM","CYPB","CY","CYT","CYOE","CYGX","CYTK","GTF","CYTX","CYTR","CYTC","DECC","DHI","DAAT","DJCO","DAI","DLYT","DAKT","DJRT","DLOV","DFCO","DAN","DHR","DAC","DANKY","DANOY","DNBK","DARA","DRI","DAR","DASTY","DCLT","DDUP","DAIO","RACE","DTLK","DLGI","DMAR","DTMG","DRAM","DATA","DWCH","DAVN","DVA","DAWKQ","DWSN","DXR","DBRM","DSTI","DSUPQ","DCBR","DCAP","DCBF","DCIU","DPM","DCT","DDIC","DDSU","DLAD","TRAK","DF","DEAR","DRSV","DBTB","DECK","DCGN","DECO","DII","DCZI","DPDW","DPFD","DWOG","DVLY","DE","DFR","DFNSE","DFSH","DFTS","DGMA","DEIX","DEJ","DGTC","DLM","DCTH","DK","DEG","DLIA","DELL","DLPX","DPHIQ","DFG","DLSC","DSIIQ","DAL","DLA","DELTY","DGAS","DOIG","DPTR","DGEN","DDDC","PROJ","DEL","DLX","DMAN","DMAT","DCMG","DNRR","DNR","DNDN","DNN","DMKB","DENN","DPAT","XRAY","DEPO","DSCI","DYSC","DSGX","DWRI","DEST","DSNY","DSWL","DTRX","DMCD","DB","DLAKY","DT","DEVC","DDR","DVN","DV","DEWY","DXCM","DXXFF","DGIT","DGC","DHNA","DHT","DEO","DLPC","DCAI","DIAAF","DMDD","DDEQ","DMND","DHIL","DHTT","DHMS","DTPI","DO","DHCC","DRH","DIA","DSX","DIAC","DSHL","DYXC","DTCT","DHX","DKS","DBD","DDRX","DGII","DMRC","DRAD","DGLY","DIGA","DCDC","DLFG","DIGL","DPW","DLR","DRIV","DVID","DGFX","DGI","DGLP","DGTW","DIGI","DGNG","DJJI","DDS","DCOM","DIMC","DIN","DWIS","DIOD","DIOMQ","DNEX","DIRI","DRCXQ","DTV","DSBO","DCSR","DFS","DISCA","DSCO","DISH","DTEK","DDVS","DESCQ","DITC","DTTY","HIRD","DFRH","DISS","DVTS","DVNTF","DIVX","DXYN","DMEI","DNAG","DNBF","DCNMQ","DNP","DMC","DCPT","DM","DLB","DLLR","DTG","DLTR","DOMR","DMNM","DOM","D","DPZ","UFS","DCI","DGICA","DNKYQ","DORB","DEGY","DRL","DMLP","DIIB","DORM","HILL","DTVI","DEGH","DBLE","DBTK","DEI","DOVP","DOV","DDE","DVD","DOVR","DPO","DOW","DWNFQ","DPAC","DPL","DPS","RDY","DRGV","DRGG","DRUG","DVCO","DROOY","DRJ","DWA","DBRN","DRC","DW","DMF","LEO","DSM","TBUS","DRQ","DKAM","DKIN","DSCM","DCU","DRYS","DSPG","DST","DSW","DTIIQ","DTE","DTSI","DSTR","DUCK","DCO","DUF","DUK","DRE","DNB","DEP","DNE","DUNE","DFT","DRRX","DUSA","DGRI","DVIXQ","DVLN","DXPE","DYAI","DYAX","DY","DBGC","DGIX","DYII","DDMX","DYMC","DYLI","BOOM","DRGP","DRCO","DYMTF","DYSL","DYNT","DVAX","DCP","DYN","DX","DYNE","EMDF","ETFC","EPEO","EJ","ESIMF","ESMT","ESYG","EDIG","DD","EONGY","SSP","EACO","EBMT","EGBN","EAGB","EGLE","EGXP","EFSI","EXP","EROC","EBOF","ESCI","ESSE","EBLC","EFTI","ELNK","EWKS","ECDV","ETFS","EWBC","NGT","EML","EESC","EGDD","EIHI","ELC","EVBS","EGP","EMN","EK","ESYE","ESIC","EYRDQ","JOES","ETN","EV","EOI","EVV","EVN","EVY","ETJ","ETB","EAUI","EBDC","EBAY","EBIX","ECBE","ECRO","ECCE","ECMN","ELON","ECHR","ECTE","ECHTA","SATS","ECET","ECLP","ECOG","EOPI","ECL","ECOS","EEI","ECOC","ECEC","EC","ESYM","ECTX","EDAC","EDAP","EDCI","EDDH","EBHI","EDEN","EDNE","EDN","EDFY","EDGR","EPEX","EDGW","DIET","EIX","EDWY","EDPFY","EDR","EDUC","EW","EFJI","EFTB","EFUT","EGAN","EGAM","EGHDQ","EGML","EFIR","EGXF","EHTH","BAGL","ESALY","ECPN","EP","EE","EPB","ELAMF","ELN","ELAN","EMITF","ESLT","EVSNF","ELCO","EWTLQ","ELDO","EGO","ESYS","ELGT","EMCO","ELRC","ESIO","MELA","ELSE","EMLIF","EGLS","ELUXY","ERTS","EKCS","EGMI","ESNR","ELST","ETCIA","EFII","EILL","ETGF","ETAK","LLY","EEGI","ELURQ","ELOG","ELTP","ETCHE","EGT","RDEN","EMYCF","ECF","ESBK","LONG","ELOY","ELRA","ELRN","ELTK","ELXS","EYSM","EMAN","EMAK","EQ","AKO.A","ERJ","EMBR","EMC","EMCI","ECIN","EMCF","EME","EMKR","EMDY","EMS","EBS","LZR","EMDH","ISEE","ESHG","ESC","EMR","MSN","EMIS","EMMS","EDE","EEGC","NYNY","ERSO","EWCR","EPRC","EPYSQ","EIG","EOC","ICA","EMPY","ERI","ELMG","ETEC","ELX","ENPT","ENGO","ENAB","EIPC","EEQ","EEP","ENB","ECA","ECIA","ECGA","ECMH","EAC","EBTX","ECPG","ENP","WIRE","ENCO","EDVP","END","EXK","EDVC","ENDP","ENDO","ELGX","EVSC","ENH","ENWV","HEV","EGN","ENR","ENHD","ENCC","ENER","EFOI","EIIN","ENKG","ERPLQ","EQST","ERII","XLE","ESA","ETE","ETP","EWST","EXXI","ECNG","ES","ENRJ","ENLU","ENOC","ENYNF","ERF","ENI","ENS","ETCK","ENCZQ","ENGA","ENGEF","EGX","ENG","ENHT","E","DVINQ","ENLGE","EBF","ENOT","ENA","NPO","ESV","ENSG","ESGR","EPTI","ENSL","ENTG","ETM","ECNI","ETR","ETRM","EST","EBTC","EFSC","EPE","EINF","EPD","ENT","ENCR","EIUS","EPR","ETHT","ENTN","ESAN","ETAD","EVC","EGI","ENMD","EPCN","ENTR","ETOP","ENTU","ENTX","ENUI","ENVG","EVTN","ENVK","EVEH","EVCC","EECPQ","EESV","EPG","EVSP","ETCC","ECGP","ECGI","ENWN","EXNT","ENZ","ENZN","EOG","EONC","EOSI","EPGL","EPAZ","EPCYY","EPOR","EPCC","EPCT","EPIC","EPCG","EPIQ","EPIX","EPXR","EPTG","PLUS","EPHC","EPLN","EQT","ENET","EQPI","EFX","EQUI","EQIX","EQFC","ELS","EMDAQ","EQY","EQR","EQUUS","EQS","ERUC","ERES","ERFW","ERGN","ERGB","ERHE","ERIE","ERMS","ESBS","ASAT","ESBF","ESCL","ESCA","ESMC","ESE","ESREF","ESNI","ESFT","ESPI","ESP","EPRTQ","ESSA","ESIV","ESS","ERNT","EL","ESL","ETLC","ETLO","EERG","ETLT","ETH","EHTE","ETEV","ETLS","TSER","ETWC","EUSI","EUOK","EUPH","EURX","EUENF","CLWT","EUBK","EUGS","EEFT","EPAR","EMCC","ESEA","EUOT","EVEP","EVTP","EVVV","ESCC","EVBN","EVCI","EVK","EVRC","EVR","RE","EEE","EBI","ESLR","EVGG","EVIS","EPM","EVLVQ","EVOGF","EVOL","EXAS","EXAC","EXAR","EXCL","EXM","XCO","EXCS","EXEL","EXC","XRA","EXFO","XIDE","EXLS","EXBX","EXOU","EXPE","EXPD","EXDL","EXGI","EXPO","ESRX","XPO","XJT","EXTI","EENI","EXH","EXLP","EXR","EXTR","EXEXA","XOM","EYII","EZCH","EZPW","EZEN","FMBM","FNB","FFIV","FACT","FCTOA","FTUSQ","FDS","FIC","FCHDQ","FCS","FFH","FRP","FNGC","FCNRE","FALC","FDO","FMYR","FMRX","DAVE","FDOG","FNM","FELI","FEEC","FVSTA","FARM","FMCB","FMAO","FFKT","FMNB","FARO","FHHIQ","FSCXQ","FAST","FFFC","FBSS","FFG","FBCM","FCSX","FECOF","AGM","FRT","FSCR","FSS","FDTRE","FDML","FII","FDX","FFCO","FEEL","FEIC","FCH","FMLP","FLWE","FHC","FETM","FWIN","FROI","FGP","FOE","FFDF","FFPM","XXFPL","FFWC","FGXI","FIATY","FBCE","FTGX","FTWR","FDEI","FSBI","FDBC","ONEQ","FNF","FIS","LION","FPP","FSC","FITB","FTMC","FIF","FISI","FNGP","XLF","FCON","FNLH","FNCMQ","FNSR","FINL","FMST","FNLY","FTGIE","FNVG","FFGI","FPNQE","FAC","FABK","FADV","FAF","FASC","FAVS","FBNC","FBPI","FNLC","FBP","FBMS","FBSI","FIRT","SUFB","BUSE","FBIZ","FCAL","FCVA","FCPN","FCAP","FCFS","FCEC","FCZA","FCBN","FCNCA","FCLF","FCF","FCBC","FCFL","FCCO","FDEF","FIME","FFBH","FFSX","FFNM","FFBC","FFIN","THFF","FFCH","FFNW","FFKY","FFHS","FHRT","FHN","FFSL","FR","FKYS","FKFS","FLFL","FRST","FMFC","FMWC","FMD","FMAR","FRME","FMR","FMBH","FMBI","FMFKE","FNSC","FBAK","FNCB","FXNC","FNAT","FNFG","FNFI","FNRN","FLIC","FF","FOTB","FPAFY","FPTB","FPFC","FPO","FREVS","FRGB","FSRL","FRFC","FSFG","FSGI","FSLR","FSBK","FSNM","FSTF","FMNG","FUNC","FWV","FBMI","FCFC","FE","FFED","FFLT","FGOCE","FIRM","FMER","FPFX","FSRV","FSTW","FIMG","FISV","FSCI","FVE","FGHH","FBC","FSR","FLML","FAME","FLDR","BDL","FLTB","FLTWQ","FSI","FLXI","FLXT","FLXS","FLEX","FLTS","FLTT","FLIR","FLRP","FRAE","FLRB","FGMG","FPU","FTK","FCIN","FLOW","FLO","FLS","FLR","FFIC","FMC","FTI","FNBG","FNBN","FNHM","FDTC","FNXMF","FMXLQ","FONE","FCSE","FMCN","FCCG","FMX","FONR","FNXCE","VIFL","FL","FBLQ","FTRSQ","FTAR","FMTI","FORC","FRPT","F","FFHN","FCE.A","FRX","FST","FTRM","FOR","FVRG","ASUR","FLSW","FORM","FORTY","FORR","FDI","FORBQ","FFDH","FIGI","FIG","FO","FCRI","FFSY","FFI","FNET","FMNLF","FWRD","FORD","FOSL","FWLT","FCL","FPWB","FCRS","FOFN","FRBE","FXCB","FXPT","FXBY","FPBI","FPBF","FPIC","FPL","FGNT","FTE","FFTIQ","FBTXQ","FC","FCMCE","FELE","FEP","FRAF","FMNJ","BEN","FSP","FRTW","FT","FKWL","FRWL","FRED","FRE","FCBI","FOH","FESI","FFGR","FDMF","FGOVF","FCX","FREE","RAIL","FMNTQ","FEIM","FRSC","FMS","FRES","FDP","FRHV","FWTC","FREZ","FGHLQ","FRD","FBR","FYAD","FRIZQ","FRS","FRMO","FRG","FRNTQ","FTR","FRGY","FTBK","FTO","FRO","FFEX","FSBC","FSII","FSON","FCN","FLIP","FUFWE","FUGO","FSYS","FTEK","FCEL","FLNA","FUJI","FJTSY","FLL","FMBV","FLCR","FULO","FPLYQ","FULT","FNDM","FNDT","FUQI","FRM","FBN","FUWAY","FSIN","FUSN","FSN","FCRZ","FUTR","VITK","FTFL","FMDAY","FUTOE","FFHL","FXEN","FXRE","GKSR","GIII","WILC","GAEX","GWLK","GFA","GAIA","GAN","GAXIQ","GXYF","HIST","GBL","GAIMF","GME","GMTC","GMZN","GPIC","GMPM","GCAN","GRS","GMTN","GCI","GPS","GARB","GDI","GOYL","GRMN","IT","GSX","GST","GWIR","GHSE","GNRG","GWAY","GMT","GET","GOCH","GEER","GSAC","GLTC","GXPI","GPRO","GENR","GNK","GENC","GY","GNLK","DNA","GNXE","GNRL","BGC","GNMP","GNCMA","GNIZQ","GNRD","GD","GE","JOB","GEVI","GFN","GGWPQ","GKIN","GMGCQ","GMR","GNMT","GIS","GMO","GMGMQ","GSI","GNBT","GCO","GWR","GEL","GGHO","GLS","GENI","GTHR","GENE","GNVN","GENX","GNOI","GTOP","GNPR","GENM","GHDX","GXDX","GNYS","G","GNTA","GETI","GNTX","GENT","GTIV","GNTO","GPC","GNVC","GNW","GENZ","GEO","GNNC","GBOE","GEOY","GGR","GOK","GMET","GPR","GORX","GEOI","GFME","GTWN","GECR","GGC","GPW","GSPH","GOVX","GWRX","GSVE","GRB","GNA","GGB","GABC","GERN","GFGU","GTY","GVHR","GFIG","GFRP","GHQ","GGLT","GA","GMOS","ROCK","GIGA","GIGM","GILT","GIL","GILD","GTAX","GRSR","GIVN","GBCI","GWSV","GLAD","GOOD","GAIN","GLMA","GSK","GLBZ","GLRP","GLG","GLIAQ","GRT","GBLE","GACFQ","GAXC","GBVS","GQN","GCPL","GCA","GBCS","GCEH","GHC","GLBC","GDRS","GDIV","GEEG","GEYH","GNH","GEYI","GNTP","GAMT","GEPT","GLGT","GBGD","GGRN","GHI","GIMU","GIFD","GLBL","GINV","GISV","GBTI","GBMT","GBMR","GLOB","GOLX","GLP","GPTX","GPN","GPPL","GBLP","GRLY","GBRC","GRDB","GSFD","GSOL","GTLL","GTNOQ","GNET","GWTR","GWRC","GAI","GLBT","GLOI","GSB","GSAT","GCOM","GTVCF","GBTR","GBIR","GLOW","GLUU","GLGSQ","GMKT","GMXR","GNCP","GOL","GLNG","GCMN","GFI","GHII","GRZ","GORO","GRPS","GG","GARA","MYNG","GOEG","GLDC","GGTHF","GPTC","GPXM","GPH","GORV","GSPT","GSS","GVDI","GWBC","GV","GFSI","GS","GMEX","GRMC","GSPG","TEEE","GTA","GOLF","GTIM","GR","GDP","GT","GOOG","GNCN","GRC","GTAP","GOTTQ","GOVB","GPX","GPSN","GGG","GTI","GHM","GKK","GTE","GATT","LOPE","GDTGF","GRRB","GVGDF","GCFB","GVA","GNGI","GLIF","GPK","GTIX","GOJO","GRVY","GTN","GRMH","GTPS","GAFL","GAP","GBG","GEBD","GFLB","GLUX","GLDD","GNI","GXP","GSBC","WOLF","GB","GAFC","GCMEE","GRNB","GBH","GMCR","GRNO","GPRE","GSTYE","GSRE","GBTG","GBX","GCBC","GRYE","GHL","GRH","GRLC","GLRE","GLTV","GMTI","GERS","GREN","GVFF","GRBS","GEF","GSTN","GLGI","GRIF","GFF","GLLC","GNBA","GPI","GLDI","GWDCQ","GBE","GMK","OMAB","PAC","ASR","SAB","GGAL","GMBXF","RC","SIM","TV","TMM","GRYO","GSEN","GSLA","GGA","GNV","GVP","GSIC","GSIG","GSIT","GSVI","GTTLQ","SOLR","GTCBD","GTCC","GTSI","GTXO","GTXI","GSH","GZGT","GBNK","GFED","GFG","GDTI","GES","GUID","GTHP","GUPR","GNXP","TELI","GCOG","GIFI","GFRE","GLFE","GWPC","GLF","GPOR","GIA","JAHI","SORT","GU","GPAX","GVSS","GWSC","GYMB","GYRO","HEES","HQH","HQL","HRB","FUL","HNZ","HABC","HCKT","AIP","HAE","HGUE","HAIN","HJHO","HX","HPCO","HAL","HALL","HWG","HALO","HMAUF","HABK","HBNK","HAMP","HCNP","HMPR","HNAB","HKFI","HBHC","HDLM","HBI","HGR","HAFC","HNFSA","THG","HNSN","HANS","HRBN","HBTC","HDNG","HOG","HGIC","HNBC","HARL","HAR","HLIT","HMY","HRLSQ","HWFG","TINY","HRS","HPOL","HSTX","HWD","HSC","HRCT","HHS","HIG","HTMXQ","HVLL","HBIO","HAVA","VGENQ","HTE","HNR","HRVEQ","HAS","HAST","HTS","HAUP","HAVSF","HVT","HE","HA","HWK","HWKN","HWBK","HYDN","HAYZQ","HAYN","HCNV","HCC","HCP","HCFB","HDB","HEDYY","HW","HLOI","HCN","XLV","HDVY","FIT","HGRD","HMA","HNT","HESG","HSSO","HR","HCSG","HETC","HGAT","HLS","HSPO","HS","HSTM","HTRN","HWAY","HFFI","HTV","HTLD","HTLF","HTOG","HTLLQ","HPY","HTLJ","HTSF","HTWR","EAR","HHDG","HEK","HL","HLYS","HEII","HEI","HSII","HMYRQ","HINKY","HELE","HLCS","HLXC","HMNA","HXBM","HLX","HLXW","OTE","HCGI","HP","HAHI","HEMA","HMGN","HEB","HWEG","HMBT","HDRX","HENL","HNNA","HBE","HSIC","HNYA","HPLF","HLF","HBRM","HERO","HTGC","HRUA","HBKS","HTBK","HFWA","HBOS","HEOP","HCCI","HRLY","MLHR","HT","HSY","HTZ","HSKA","HES","HEW","HPQ","HXL","HFFC","HFBA","HF","HGG","HSCR","HIRI","HSR","HITK","HIBB","HIBE","HICKA","HTCO","TOH","HCBC","HEVEE","HVAE","HYI","HYP","HBRF","HCD","HIA","HBKA","HIGPQ","HSVLY","HIHO","HIW","HPGP","HLND","HIL","HRC","HI","HBIA","HTH","HIMX","HIFS","HPSO","HRSH","HIRU","HTVNQ","HIT","HITT","HKN","HOLI","HLTH","HMG","HMNF","HMSY","HNI","HOKU","HEPH","HOC","HEP","HOLL","HOOB","HOLX","HBCP","HOMB","HCFL","HD","HDIX","HFBL","HOME","HWEN","HMIN","HLFN","HME","HSOA","HSYT","HOFD","HGFNQ","HOMS","HTWC","HXM","HMC","HON","HOKCY","HPJ","HOFT","HH","HFBC","HMN","HBNC","HZNB","HRZB","HGPI","HRZ","HRL","HOS","HNIN","ZINC","HSP","HPT","HST","HOTT","HWBI","HMSM","HOUM","HTGT","HOTJ","HUSA","HITD","HWCC","HOV","HQS","HRP","HBC","HSNI","HSTC","HSWI","HNP","HUBG","HUB.B","HCBK","HHGP","HDHL","HDSN","HUVL","HUGH","HTC","HGT","HFGB","HHGM","HBSY","HGSI","EROX","HUM","HUMT","HCOIQ","HBAN","HUN","HURC","HURN","HRAY","HUSKF","HTCH","HTX","HBPI","HYLKF","HBDY","HYDGQ","HYEG","HYHY","HYDP","HYGS","HYDI","HTECE","HYC","HDY","HYPRQ","HDII","HYPF","HYSNY","HYTM","ICABY","IFLO","IMNY","ISAC","IDSY","IISLF","IOMG","ITWO","ITUI","ITOO","IAO","IACI","IAG","IASCA","IBNW","IBAS","IBKC","IBPM","IBIS","IBIN","ICAD","ICGN","IEP","ICCW","IWEB","ICFO","ICFI","IBN","ICOG","ICOC","ICLR","ICON","ICOP","ICPR","ICTG","ICTSF","ICUI","ICXT","IDCO","IDA","IDCX","IDARQ","IDI","IDIX","IDPIQ","IDRA","IEX","IDXX","IDMI","IDOI","IDT","IECE","IEHC","IELE","IENT","IFSH","IGTE","IGNE","IG","IGAI","IGPG","IGOI","IHS","IIVI","IKAN","IKGI","IKNX","ILNC","ITW","ILMN","ILXRQ","IMR","DISK","ISNS","ISOL","IMTL","IMCX","IMAG","IAGX","IWSY","IMGM","IMLEE","IMDS","IMGG","IMGI","IMRX","IMN","IMAX","IMCC","ICRP","IMED","IIG","IMKI","IMMR","IMMP","ICCC","BLUD","IMMFF","IMMCQ","IMUC","IMGN","IMMU","IMMB","IMPM","IMSU","IMXC","IMMG","IBLTZ","IPXL","IMPC","ICII","IGPFF","IPII","IMO","IPMN","IPSU","ITYBY","IMSCE","ZCOM","RX","IDGI","ICNSQ","IOT","ICNT","SAAS","MAIL","INCY","IFSB","IHC","IBFL","INDB","IBCP","IDEV","IXOG","IFN","IGC","INCB","IDGG","IEAM","IDSM","IDSA","XLI","IBA","IDMCQ","NRGP","NRGY","IFRX","IFNNY","INFN","IMCI","IFNY","IMGR","INFI","IPCC","IFCC","INFS","IUSA","IFNX","IFLG","INOW","IWWI","INFA","IAIC","IACH","III","IOSA","IFMX","IFMC","ISHM","IFSG","IFON","INFY","INHI","IGD","ING","IID","IGNT","IR","IMKTA","IM","INHX","IHBT","INTO","INKS","IRC","IMDD","INWK","INCM","INOD","IMEN","IPHS","IOSP","INOC","IPUR","MPEG","INVCE","ICSN","IVDN","IGCA","INIV","ISSC","INVX","IHT","INNU","INO","NPLA","IRBL","NSIT","ISGT","ISIM","INSGY","ISIG","INSV","INSU","INSM","ISPH","IIIN","ISTC","PODD","NSUR","INSW","INSQ","ILAS","ITGB","ITAC","IBNK","IART","INGA","ISYS","ITKG","INVI","INMD","INBP","IBSS","ITDD","IDTI","IESC","IEVM","IHSVQ","IHCH","IISX","INMG","INTP","IZZI","ISSI","ISSM","ITYCQ","IMFD","TEG","INTC","FILM","IGXT","IDN","ILVC","INS","ITLI","ITIG","ITLN","IPAR","IAN","IHGP","IBDI","IBKR","IDC","ININ","ISWI","IAQGU","IAGM","ICLK","IHG","ICE","IDCC","IREP","IFSIA","INTG","INRSA","ILI","IBI","IRCE","LINK","IUSN","IN","IMMM","IMTO","IMOT","IMCB","ITMN","INAP","IAX","IARO","IAAC","IBAL","IBOC","IBM","ICO","ICBU","ICTL","INCC","ICSM","IDND","IENI","IFCIQ","IFLI","IFF","IFDG","IFUE","IGT","IGRU","INIS","ITSI","IP","IPWG","IPRPY","IPMG","IRF","ROY","ISH","ISLV","ISSG","ISCA","ILST","ISCO","ITXN","ITWG","GEEK","INET","ICGE","ICCXQ","IGLD","ITNF","IIJI","INAR","IOC","IPAH","INPH","IPLY","IPG","INTX","ISIL","ISPD","IHRI","ITP","IILG","IBCA","IVBK","ITWR","INTT","IVAC","IORG","ITPC","IGLB","IPI","ITRP","IIN","INGNQ","INTZ","INTU","ISRG","IVC","VTIV","SNAK","IMA","IVZ","INVT","IVCO","ITG","ISBC","ICH","IRET","ITIC","IVIT","INSA","IHO","IVRO","IVOB","INXI","IYXI","IZON","IO","IFTI","IWA","IWDM","IPT","IPAS","IPCR","IPCM","IPCS","IPGP","IPIXQ","IPZI","IRBS","IRIX","IRIDQ","IRSB","IRIS","IRBT","IRM","IRS","IRSN","IFC","ISAT","ISOO","ISBL","ISEC","SHY","TLT","IEF","AGG","TIP","ICF","IAU","DVY","IYT","IYY","ITA","IYM","IAI","IYC","IYK","IYE","IYG","IYF","IHF","IYH","ITB","IYJ","IAK","IHI","IEO","IEZ","IHE","IYR","IAT","IYW","IYZ","IDU","FXI","LQD","KLD","JKD","JKE","JKF","JKG","JKH","JKI","JKJ","JKK","JKL","EWA","EWO","EWK","EWZ","EWC","EFG","EFA","EFV","EEM","EZU","EWQ","EWG","EWH","EWI","EWJ","EWM","EWW","EWN","EPP","EWS","EZA","EWY","EWP","EWD","EWL","EWT","EWU","IBB","NY","NYC","IWF","IWB","IWD","IWO","IWM","IWN","IWZ","IWV","IWW","IWC","IWP","IWR","IWS","OEF","ISI","IVW","IVV","IVE","IEV","IOO","RXI","KXI","IXC","IXG","IXJ","EXI","MXI","IXN","IXP","JXI","GSG","ILF","IJK","IJH","IJJ","IGE","IGW","IGN","IGV","IGM","IJT","IJR","IJS","WPS","ITF","SLV","ISLN","ISIS","ISLE","ISHI","ILE","IOMT","ISON","ISR","ISRL","ISDR","ISTA","SFI","ITGL","ITCD","ITC","ITCC","ITMUQ","ITXFF","ITI","ITEX","ITHH","ITNS","ITRI","ITRO","ITT","ESI","ITRN","IVAN","IVN","IVNM","IVD","IVII","IVVI","IVOT","IVOI","IWIHF","IWTT","IXEH","XXIA","IXYS","JSAIY","JJSF","JAX","JCG","JBHT","JCP","SJM","MAYS","JCOM","JASO","JBL","JCDA","JKHY","JACK","JTX","JAXB","JXSB","JCLY","JACO","JEC","JFGI","JADA","JAGH","JGPK","JAG","JAKK","JMBA","JHX","JRCC","JLWT","JNS","JAH","JVDT","JAV","JAVO","JYHW","JAZZ","JDAS","JDSU","JNTX","JEDOF","JEF","JFBI","JFBC","JEN","JERT","DELI","JLI","JBLU","JCTCF","JFC","JGBO","JNGW","JST","JKAK","JLMC","JMAR","JMGE","JMP","JAS","JOEZ","JBSS","JBT","JDOG","JNJ","JCI","JELCY","JOUT","JNY","JLL","JSDA","JOSB","JRN","JRCOQ","JOYG","JPEI","JPM","JPST","JUMT","JNIP","JNPR","JMIH","JYRA","KFED","KSP","KSWS","KTLI","KTII","KV.A","LRN","KANP","KAI","KAHL","KALU","KGHI","KALG","KHGT","KLDO","KAMN","KANA","KNDI","KCLI","KSU","KPPC","KRAT","KATX","KATY","KWHIY","KDN","KED","KYE","KYN","KB","KBH","KHA","KBR","KBW","KRNY","KGN","KWPW","KEI","K","KELLQ","KELYA","KLYG","KEMEE","KNDL","KNXA","KENS","KMT","KWIC","KCP","KNSY","KENT","KNTH","KTYB","KFFB","KINV","KYUS","KERA","KMNG","KERX","KSSH","KEQU","KEG","KTEC","KTCC","KEY","KEYN","KEYO","KYCN","KFRC","KHD","KDCE","KVIL","KKTI","KLIB","KRC","KBALB","KBX","KMB","KIM","KMP","KMR","KND","KCI","KG","KING","KNGS","KSYT","KFS","KGC","KEX","KNBWY","KIRK","KDGL","KTCHE","KRG","KFN","KLAC","KLBAY","KLGG","KLMK","KLMR","KMGB","NITE","KNECQ","KNX","VLCCF","KBAS","KNL","KNOL","KNOT","KNUR","KARE","KBSTY","KDKN","KOG","KSS","KCAP","KKOEF","KOLR","KMTUY","KMDO","KONA","KNM","KONG","AHONY","RDSMY","KKPNY","PHG","KOPN","KOP","KEP","KFY","KOSS","KOW","KQIPQ","KFT","KBLB","KTOS","KAUSQ","KRSL","KFTG","KKD","KR","KNOS","KRO","KSBI","KSW","KTC","KUB","KUHM","KLIC","KURU","KPCG","KLOC","KVHI","KWGBF","KYO","KBPH","KYZN","LLFH","ID","LLL","FSTR","SCX","LCTZ","LJPC","LZB","LB","DDSS","LH","LAB","LG","BOOT","LTS","LDSH","LFRGY","LSBK","LVCA","LBAI","LKFN","LAKE","LACO","LAIC","LRCX","LAMR","LFL","LNBO","LANC","LNCE","LANZ","LFG","LDR","LNDC","LARK","LLND","LNY","LSTR","GAIT","LCI","LTRX","LPAD","LPSB","LGOV","LRRA","LVFHF","LVS","LSAL","LHO","LMTI","LCRD","LLTI","LRST","LMLG","LTTV","LTTC","LSCC","LRRS","LAWE","LAWS","LWSN","LAYN","LAZ","LKI","LCAV","CRCUF","LCCI","LCNB","LDHL","LDK","LPTC","LBIX","LDIS","LNWH","LEAP","LF","LEA","LTRE","XPRT","LCRY","LECT","LEDP","LEE","LPHM","LFBG","LEGC","LGYH","LGCY","LRCI","LGDI","LEGE","LGBS","LM","LEG","LEHMQ","LTCR","LMAT","LNCM","LPS","EGTK","LEN","LII","LNVGY","LENXQ","LFMI","LCAR","LUK","LVLT","LXRA","LXRX","LXUN","LEXI","LEXPQ","LXP","LXK","LEXO","LPL","LGAH","LGL","LHCG","LYBI","LBTE","LIA","LBCP","LBBB","LCPM","LBTYA","LIBHA","LMDIA","LCAPA","LINTA","LRY","LBSU","LICT","LFXG","LPHI","LSR","LIFE","LTM","LPNT","LFPI","LQWC","LFTC","LCUT","LFVN","LWAY","LGND","LSCG","LROD","LPTH","LTSC","LWLG","LIHR","LIMC","LIME","LLNW","LTD","TVL","LNCC","LNCR","LINC","LECO","LNC","LPBC","LNN","LLTC","LNKE","LTON","LWLL","LINE","LCHL","LIOX","LGF","LPET","LIPD","LPVO","LIQWF","LQDT","LQMT","LTGLQ","LTWV","LAD","LTHU","LFUS","LTFD","LIVC","LYV","LIVE","LPSN","LVWR","LVWD","LIZ","JADE","LKAI","LKQX","LRT","LYG","LMIA","LMLP","LNBB","LOCM","LPHC","LBAS","LMT","LNET","LGN","L","LOAX","LOGN","LOGC","LGVN","LGTY","LGSL","LOGI","LOJN","LNST","LOGE","LPTI","LFT","LOOK","LOOP","LORL","LO","LRUSF","LWSL","LTUS","LTEC","LABC","LPX","LOW","LYLP","LSBX","LSBI","LXU","LSI","LYTS","LTC","LTVCQ","LTWC","LTXC","LZ","LUB","LEI","LUCY","LDVK","LUFK","LULU","LL","LUMCQ","LMNX","LUNL","LUNA","LGDOF","LUNMF","URX","LUX","LVMUY","LDL","LYRI","MFW","MFBP","MTB","MWIS","MDC","MHO","MBCI","TUC","MCBC","MCIM","MACC","MACE","MAC","CLI","MFNC","MIC","MCVE","MVSN","M","MCZ","MAEI","MAD","MADGQ","MDEX","MMRSF","MVG","MAGS","MGLN","MGG","MMP","MPET","GMLI","MGIC","LAVA","MECAQ","MGA","MAGAA","MNGA","MAG","MAGY","MDORE","MPG","MGYR","MTA","MTE","MHLD","MFB","MAIN","MAM","MNLU","MSFG","MREE","MSFN","COOL","MJOG","MJRC","MMUS","MKTAY","MAKO","MBEW","MLVF","MAMM","MHJ","TMNG","MNCSQ","MGOF","MANH","MNHN","LOAN","MHAN","MHTX","MNTX","MTW","MTEX","MNKD","MAN","MNSF","MANT","MVTG","MFC","MAPP","MRO","MCHX","MCBN","MCS","MRGO","MEXP","MARPS","MPX","HZO","ME","MHDG","MKL","MTRE","LEDR","EVX","GDX","MKTX","MWWC","MWE","MRLN","MMIO","MXBTF","MAR","MMC","MI","MSHL","MHLI","MATK","MRTN","MSO","MLM","MMLP","MVL","MRVL","MVNP","MAS","MASI","MMGW","MEE","MTZ","MHH","MDBS","MA","MTCH","MASC","XLB","MATH","MLKIQ","MTRX","MTXX","MXXH","MAT","MATW","MTSN","MLP","MVRM","MAVO","MXGL","MAXC","MXT","MAXI","MXIM","MMS","MRTI","MXUS","MXWL","MXWF","MAXW","MXM","MAXY","MFLR","MBFI","MBI","MBTF","MCIF","MFE","MNI","MKC","MSSR","MDR","MCD","MCGC","MGRC","MHP","GLFN","MCK","MMR","MRINA","MDCA","MDII","MRNA","MDZ","MDTV","MDU","MJN","MEAD","MIG","MWV","MEAS","MKTY","MTL","MDIN","MWDSQ","TAXI","MEDX","MSBT","MDAS","MDTH","MCLN","MHS","EMEQE","MDFI","TVH","MDEA","MEG","MSII","MDAW","MBAY","MCCC","MDGC","MDLK","MEDG","MEDT","MDCI","MCTH","MISJ","MDLH","MDNU","MPW","MRIIQ","MSMT","MSNW","MSSI","MCVI","MDCO","MNOV","MRX","MCUJ","MED","MDTL","MEDS","SCEP","MDVN","MEDW","MZEI","MDJT","MLKNA","MD","MEDQ","MDRA","MTOX","MDT","MDWV","MGIN","MMDA","MGTC","MGWSF","MGOA","MPEL","BPRGY","MLNX","MLOBF","MLTC","MMBW","MTNX","WFR","MEMS","MW","MENB","MNTEF","MENT","MTSL","MRPI","MELI","MBR","MBWM","MIGP","MERC","MBVT","MRK","MRCY","MCY","MDP","MRGE","MRGN","VIVO","EBSB","TMR","MSEL","MMSI","MTH","MHGU","MERX","MRM","MERR","MMPIQ","MESA","MLAB","MTR","MSB","MCCK","MPR","CASH","MBRX","MTBR","MBLX","MTSXY","MEA","MTLK","MMG","MTATQ","MV","MTWVQ","MEOH","MEI","MET","MCOMQ","MGMA","MTRO","INFO","MCBI","MGS","PCS","MDF","MXCYY","MTD","MTWD","MXC","CASA","MXOM","MF","MFA","MFRI","MGEE","MTG","MGM","MGEG","MGPI","MGT","MDH","MIM","BKR","MCRL","MCTI","MMTC","MLAR","MCHP","MFI","MFLU","MIIS","MLOG","MMTIF","MITI","MENV","MU","NOIZ","MPAD","MCRS","MSCC","MSFT","MSTR","TUNE","MVIS","MFCO","MEND","MPB","MAA","MDS","MDY","MCFI","MBRK","MBRG","MIDD","MBCN","MSEX","MDLL","MGXX","MSL","MWYGQ","MDW","MBHI","MOFG","MGIFF","MIIX","MKRS","MZIAQ","MMRK","MLSS","MNHG","MENA","MBTGE","MCELQ","SMCG","MILR","MLR","MILL","MICC","MLDS","MIL","MDXG","MGOL","MGH","MNDO","MR","MSPD","MSA","MFN","MNEAF","MTX","MGN","MIOI","MLES","MIPS","MSOL","MRDG","MIR","MRVT","MREO","MIGL","MSON","MISS","MSW","MITD","MIND","MITK","MTPH","MTU","MITSY","MIVI","MIVA","MFG","MKSI","CMKG","NNUT","MTP","MMCE","MMRF","MOBI","MBIC","MINI","MBT","MOBL","MBLV","MOCO","MODY","MPAC","MDVX","MODM","MODG","MOD","MODT","MLNK","MMOG","MGUY","MHK","MOJO","MLRIQ","MIPI","MOLX","MOH","MLER","TAP","MNTA","MMBF","MCOAQ","MNKB","MCRI","MCEM","MCBF","MNRK","MAHI","MCAM","MGI","MLXG","MNRTA","MGRM","MPWR","TYPE","MNRO","MROE","MON","MWW","MTVO","PSTA","MRH","MNMN","MCO","MOG.A","MHCO","MCKE","MS","RNE","EDD","ICB","MRFD","MHGC","MORN","MBDE","MBKR","MRT","MOS","MOSY","MOVT","MPAA","MOT","MVAC","MNBT","MDM","MOV","MOVE","MVTS","MVGR","MPCCQ","XDSL","MPML","MPS","UNCLQ","MRVC","MSBF","MSM","MSCS","MXB","MSGI","MSHI","MUCPQ","MTICQ","MTMC","MNTG","MTSI","MTSC","MLI","MWA","MSOF","MULT","LABL","MFLX","MMTS","MBND","MCET","MGAM","MMAB","MUBM","MUR","MFLI","MUZE","MFDB","MLRMF","MFSF","MVO","MVBF","MVC","MVPN","MWIV","MYHA","MYE","MYL","MYMX","MYRG","MYGN","MYST","MWEB","MZTH","NVIC","NECO","NABI","NBR","NC","NCEN","NLC","NTE","NSLT","NNBP","NGEN","NANO","NANX","NNSR","NSPH","NSSC","NAITF","NARA","NASB","NDAQ","NAFC","NSHA","NPSNY","NTG","NATH","NEGY","NABZY","NASV","NBOH","NBG","NKSH","FIZZ","NCMI","NCOC","NDCP","NADX","NEGI","NEGS","NFP","NFG","NGG","NHI","NHC","NHCR","NHLD","NATI","NATL","NPDV","NPBC","NPK","NQCI","NRVHQ","NRCI","NNN","NSCT","NSEC","NSM","NSTLQ","NTSC","NWLI","NOV","NHRX","NHP","NRDCQ","NAII","NGS","NAXG","BHIP","NTNI","NRP","NADVF","NLIA","NNANE","NUBY","NRVN","NAWL","BABY","NTZ","NVSL","NLS","NAVR","NVDF","NCI","NAVG","NNA.U","NM","NMM","NAVI","NAV","NBTF","NBII","NBTB","NTY","NCS","NCIT","NCOA","NCR","NCTI","NPWS","NNMIF","NDBKY","NENA","NP","NEFB","NKTR","NNI","NEOG","NGNM","NHYT","NMGC","NEOME","NEON","NEOL","NEOP","NTEC","NBS","NESS","NSTC","NEST","NETC","UEPS","NTAP","NTES","NZ","NFLX","NTGR","NLST","NETL","NTME","NETO","NTPL","NTCT","NTWK","N","NTTL","NETTQ","NASC","NWCN","TNCXQ","NENG","NWK","NSSI","NHS","CUR","NHPI","NTII","NBIX","NRGN","NGSX","NRGX","NURO","NSR","TNDM","NGLPF","UWN","NGHI","NSU","NANA","NWBA","NCBC","NCNC","NCEYQ","GBR","NDVR","NWD","NENE","NEBS","NEN","NOOF","NGBF","NGHO","NGD","NGRN","NHTB","NEWH","NJMC","NJR","NWMD","NMEN","NMXC","NWMO","NNRG","EDU","NOEC","NTYN","NULM","NWBD","NWY","NYB","BBAL","NYMT","NYT","NAL","NWXJ","NBBC","NWCI","NCT","NWL","NFX","NKCIF","NEU","NMKT","NEM","NR","NFSB","NEWP","NWCM","NWSA","NEWS","NEWT","NEXC","NXY","NXGNF","NXPE","NXHD","NEXQQ","NXTY","NEXM","NXPC","NXPS","NXST","NGMC","NXTI","NXRA","NXMR","WAVE","NXGIF","NEXS","NFES","NFSE","NGAS","NGPC","EGOV","NCRG","NICE","NICK","GAS","NJ","NHWK","NIHK","NIHD","NKE","NILA","NLTX","NMLE","NINE","NTT","NISGY","NI","NSANY","NICH","NTRO","NIUS","NIV","NL","NMTI","NNBR","NNRI","NED","NLCI","NOBH","NE","NBL","NOBV","NOBLQ","NROM","NNUP","NFTI","NOK","NMR","NIMU","NRDS","NAT","NDSN","JWN","NSC","NSATF","NHYDY","NSYS","NTL","NRTLQ","NOA","NAEY","NGA","NAGM","PAL","NASMQ","NAMC","NHR","FFFD","NRT","NORH","NPBP","NSBC","NOVB","NTLNF","NBN","NECB","GSMI","NIDB","NU","NRLB","NAK","NXPN","NOG","NSFC","NTIC","NTRS"};
		
		//test BalanceStatement
		//String[] a={"AMEH.OB","LTTV.OB","PGID.OB","MOS","CPSL","NSHA","NEOG","KOOL","HSR","TWIN","AMN","AOS","AZZ","SKY","KBH","CTR","COST","NUHC","CRAI","ADAT","CREL","MERX","TRT","CPRT","GNV","PSA"};
		//test Cashflow
		//String[] a={"AMEH.OB","CTCJ.OB","CIIC","HIA","IAN","LTTV.OB","LPHI","PGID","SOC","CPSL","RMCF","MKC","SNT","GNBT","TPI","ROM","MXB","VOXX","MCS","BWL-A","MDRX","MEAD","TRT","FFDF","FIF"};
		//test IncomeStatement
		//String[] a={"AMEH.OB","BSOI.OB","CTCJ.OB","DEGH.OB","GHQ","GLFO.OB","HIA","IAN","IAQG.OB","LTTV.OB","MEJ","SOC","STQN.OB","TCXB.OB","TMI","UHFI.OB","ZXSI.OB","TTY","BQI","BF-B","OPMR","CPB","GLDC","MKC","SENEA","STKL","CEGE","HNAB","GNBT","KV-A","CHTT","TPI","WNI","XTNT","MOG-A","PCR","AOS","CTR","CXR","AM","DCU","MDCA","NYER","JW-A","CBRL","VOXW","TIBX","ANTP","PKE","MFE","KMX","GCBC","GNV","INTG","TTO","ZBB"};
		//String[] a={"CIIC","CPSL"};
		
		List<String> a=securityManager.getAllStocks();
		//update the FS
		for(int i=0;i<a.size();i++)
			fsm.downloadFinancialStatement(a.get(i));
		System.out.println("Checking time starting...");
		Set<String> symbolSet = new HashSet<String>();
		symbolSet=fsm.chkBICStatement(securityManager, a);
		String[] symList = (String[]) symbolSet.toArray(new String[symbolSet.size()]);
		for(int i=0;i<symList.length;i++)
	        fsm.downloadFinancialStatement(symList[i]);
		//load the CSV which had download
		/*for(int i=0;i<a.size();i++)
				//fsm.loadBalanceStatement(a.get(i));
		for(int i=0;i<a.size();i++)
				fsm.loadCashFlow(a.get(i));
		for(int i=0;i<a.size();i++)
				fsm.loadIncomeStatement(a.get(i));*/
		
		//check the BIC sutiation
		//fsm.checkBICStatement(securityManager, a);
		long t2 = System.currentTimeMillis();
		System.out.println("All done.time is:"+(t2-t1));
		//fsm.importHistoricalCF();
        //fsm.downloadFinancialStatement(str, 2009, 1);
        //RelativeStrength rs = fsm.getRelativeStrength("abtt");
        //System.out.println(rs.getID());
		}
	/**************************************************************************************/
	/*                       Company sector and industry                                 */
	/**************************************************************************************/
	
	@Override
	public void downloadCompany(String symbol) {
		if(this.getCompany(symbol)==null){
		String html = com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/pr?s="+symbol);
		html=com.lti.util.html.ParseHtmlTable.cleanHtml(html);
		int c = com.lti.util.html.ParseHtmlTable.countTable(html);
		int flag = 0;
		for(int i=0;i<c;i++){
			String tmp = com.lti.util.html.ParseHtmlTable.getTable(html, i);
			if(tmp!=null&&tmp.contains("SECTOR:")&&tmp.contains("INDUSTRY:"))flag=i;
		}
	      String targetTable=com.lti.util.html.ParseHtmlTable.getTable(html, flag);
	      if(targetTable!=null){
		  String result=com.lti.util.html.ParseHtmlTable.extractTableContent(targetTable.toCharArray(), 0, targetTable.length()-1,200,1,200);
		  String[]info = result.split("#");

		  Company co = new Company();
		  co.setSymbol(symbol);
		  co.setSector(info[3]);
		  co.setIndustry(info[5]);
		  
		  this.saveCompany(co);
		}
		}
		else System.out.println(symbol+" existed");
	}

	@Override
	public Company getCompany(String symbol) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Company.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		List<Company> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size()>= 1)
			return bolist.get(0);
		else
			return null;
	}

	@Override
	public void removeCompany(long id) {
		Object obj = getHibernateTemplate().get(Company.class, id);
		getHibernateTemplate().delete(obj);
	}

	@Override
	public Long saveCompany(Company company) {
		getHibernateTemplate().save(company);
		return company.getID();
	}

	@Override
	public void updateCompany(Company company) {
		getHibernateTemplate().update(company);
	}

	@Override
	public com.lti.service.bo.IncomeStatement getRecentIncomeStatement(String symbol,Date date){
    try{
		return this.getRecentIncomeStatement(symbol,1, date).get(0);
    }catch(Exception e){
    	return null;
    }
	}
	
	@Override
	public com.lti.service.bo.CashFlow getRecentCashFlow(String symbol,Date date){
		try{
		return this.getRecentCashFlow(symbol, 1, date).get(0);
		}catch(Exception e){
			return null;
		}
	}
	@Override
	public com.lti.service.bo.BalanceStatement getRecentBalanceStatement(String symbol,Date date){
		try{
		return this.getRecentBalanceStatement(symbol, 1, date).get(0);
		}catch(Exception e){
			return null;
		}
	}

	@Override
	public List<BalanceStatement> getRecentBalanceStatement(String symbol, int count,Date date) {
		FinancialStatementManager fsm = (FinancialStatementManager)ContextHolder.getInstance().getApplicationContext().getBean("financialStatementManager");
		List<BalanceStatement> list=new ArrayList();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BalanceStatement.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.le("Date", date));
		List<BalanceStatement> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= count){
			java.util.Collections.sort(bolist, new BalanceStatementComparator());
			for(int i=0;i<count;i++){
				list.add(bolist.get(bolist.size()-1-i));
			}	
			return list;
		}
		else return null;
	}

	@Override
	public List<CashFlow> getRecentCashFlow(String symbol, int count,Date date) {
		FinancialStatementManager fsm = (FinancialStatementManager)ContextHolder.getInstance().getApplicationContext().getBean("financialStatementManager");
		List<CashFlow> list=new ArrayList();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CashFlow.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.le("Date", date));
		List<CashFlow> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= count){
			java.util.Collections.sort(bolist, new CashFlowComparator());
			for(int i=0;i<count;i++){
				list.add(bolist.get(bolist.size()-1-i));
			}	
			return list;
		}
		else return null;
	}

	@Override
	public List<IncomeStatement> getRecentIncomeStatement(String symbol, int count,Date date) {
		FinancialStatementManager fsm = (FinancialStatementManager)ContextHolder.getInstance().getApplicationContext().getBean("financialStatementManager");
		List<IncomeStatement> list=new ArrayList();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IncomeStatement.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.le("Date", date));
		List<IncomeStatement> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= count){
			java.util.Collections.sort(bolist, new IncomeStatementComparator());
			for(int i=0;i<count;i++){
				list.add(bolist.get(bolist.size()-1-i));
			}	
			return list;
		}
		else return null;
	}
	
	@Override
	public List<YearlyBalanceStatement> getRecentYearlyBalanceStatement(String symbol, int count, Date CurDate) {
		List<YearlyBalanceStatement> list=new ArrayList<YearlyBalanceStatement>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(YearlyBalanceStatement.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.le("EndDate", CurDate));
		List<YearlyBalanceStatement> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= count){
			java.util.Collections.sort(bolist, new YearlyBalanceStatementComparator());
			for(int i=0;i<count;i++){
				list.add(bolist.get(bolist.size()-1-i));
			}	
			return list;
		}
		else return null;
	}

	@Override
	public List<YearlyCashFlow> getRecentYearlyCashFlow(String symbol, int count,Date CurDate) {
		List<YearlyCashFlow> list=new ArrayList<YearlyCashFlow>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(YearlyCashFlow.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.le("EndDate", CurDate));
		List<YearlyCashFlow> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= count){
			java.util.Collections.sort(bolist, new YearlyCashFlowComparator());
			for(int i=0;i<count;i++){
				list.add(bolist.get(bolist.size()-1-i));
			}	
			return list;
		}
		else return null;
	}

	@Override
	public List<YearlyIncomeStatement> getRecentYearlyIncomeStatement(String symbol,int count, Date CurDate) {
		List<YearlyIncomeStatement> list=new ArrayList<YearlyIncomeStatement>();
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(YearlyIncomeStatement.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.le("EndDate", CurDate));
		List<YearlyIncomeStatement> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= count){
			java.util.Collections.sort(bolist, new YearlyIncomeStatementComparator());
			for(int i=0;i<count;i++){
				list.add(bolist.get(bolist.size()-1-i));
			}	
			return list;
		}
		else return null;
	}

	@Override
	public List<Company> getIndustrySymbols(String sector, String industry) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(Company.class);
		detachedCriteria.add(Restrictions.eq("Sector", sector));
		detachedCriteria.add(Restrictions.eq("Industry", industry));
		List<Company> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist;
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getIndustrySymbols(String industry) {
		String sql = "SELECT symbol FROM `ltisystem`.`ltisystem_company`where industry='"+industry+"'";
		List<String>symbols = new ArrayList<String>();
		try{
		List list = super.findBySQL(sql);
		if (list!=null&&list.size()!=0){
			for(int i=0;i<list.size();i++){
				Object obj=(Object)list.get(i);
				String symbol=(String)obj;
				symbols.add(symbol);
				System.out.println(symbol);
			}
		}
		}catch(Exception e){}		
		return symbols;
	}

	@Override
	public Map<String, String> getIndustrySymbols() {
		String sql="SELECT symbol,industry FROM ltisystem_company";
		Map<String,String> map =new HashMap<String, String>();
		try {
			List list=super.findBySQL(sql);			
			if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
					Object[] objs=(Object[])list.get(i);
					String symbol=(String)objs[0];
					System.out.println(symbol);
					String industry=(String)objs[1];
					System.out.println(industry);
					if(industry!=null&&symbol!=null)map.put(symbol,industry);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return map;
	}

	public RelativeStrength getRelativeStrength(String symbol) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(RelativeStrength.class);
		detachedCriteria.add(Restrictions.eq("Symbol",symbol));
		List<RelativeStrength> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size()>= 1)
			return bolist.get(0);
		else
			return null;
	}
	

	/***********************************************************************/
	public PaginationSupport getIncomeStatement(int pageSize,int startIndex){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IncomeStatement.class);
		return this.findPageByCriteria(detachedCriteria, pageSize, startIndex);
	}
	
	public PaginationSupport getBalanceStatement(int pageSize,int startIndex){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(BalanceStatement.class);
		return this.findPageByCriteria(detachedCriteria, pageSize, startIndex);
	}
	
	public PaginationSupport getCashFlow(int pageSize,int startIndex){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(CashFlow.class);
		return this.findPageByCriteria(detachedCriteria, pageSize, startIndex);
	}
	
	public List<IncomeStatement> getIncomeStatement(String symbol){
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(IncomeStatement.class);
		detachedCriteria.add(Restrictions.like("Symbol", "%" + symbol + "%"));
		return this.findByCriteria(detachedCriteria);
	}
	
	public void reSet(String symbol, Date date) {
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		Date QEnd = LTIDate.getLastDayOfQuarter(date);
		Date logDate = new Date();
		IncomeStatement is = fsm.getIncomeStatement(symbol,LTIDate.getYear(date),LTIDate.getQuarter(date));
		if(is!=null){
		String html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/is?s="+symbol);
		String content = trim(com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1));
		if(this.MatchQuarter(date, content)){
			try{
			HtmlToCSV(content,symbol,date,"incomestatement",5,false);
			String[] isData = ReadCSV(symbol,date,"incomestatement");

			Double rnd = Double.parseDouble(isData[9])*1000;
			Double sgna = Double.parseDouble(isData[11])*1000;
			Double otherOperExp = Double.parseDouble(isData[13])*1000+Double.parseDouble(isData[15])*1000;
			Double incomeTax = Double.parseDouble(isData[29])*1000+Double.parseDouble(isData[31])*1000;
			Double grossProfit = Double.parseDouble(isData[7])*1000;
			Double totalOperExp = rnd+sgna+otherOperExp;
			Double operInc = Double.parseDouble(isData[19])*1000;
			Double preTaxInc = Double.parseDouble(isData[27])*1000;
			Double afterTaxInc = preTaxInc-incomeTax;
			Double netInc = Double.parseDouble(isData[43])*1000;		
			//set value;
			
			is.setIncomeTax(incomeTax);
			is.setGrossProfit(grossProfit);
			is.setTotalOperExp(totalOperExp);
			is.setOperInc(operInc);
			is.setPreTaxInc(preTaxInc);
			is.setAfterTaxInc(afterTaxInc);
			is.setNetInc(netInc);

			this.updateIncomeStatement(is);
			System.out.println(symbol+" is done");
			Configuration.writeFinancialLog(symbol, logDate, date.getYear()+1900, LTIDate.getQuarter(date),"IncomeStatement done.");
		}catch(Exception e){
			
		}
		}
		else return;
		}else return;
	}
	
	/****************************** Import Historical Data from CSV Files*******************************************************/
	
	/**
	 * caixg: Please add three parameters, filePath,datePath,sharesPath to this API, or remove it.
	 */ 
	public void importHistoricalIS(){
		BufferedReader br1 =  null;
		CsvListReader cr1 = null;
		BufferedReader br2 =  null;
		CsvListReader cr2 = null;
		BufferedReader br3 =  null;
		CsvListReader cr3 = null;
		String filePath = "H:\\LTISystem Program\\Glossary files\\AI\\AI\\INCOME_STMT_QUARTER.CSV";
		String datePath = "H:\\LTISystem Program\\Glossary files\\AI\\AI\\DATES AND PERIODS.CSV";
		String sharesPath = "H:\\LTISystem Program\\Glossary files\\AI\\AI\\SHARES STATS.CSV";
		try{
			br1 = new BufferedReader(new FileReader(filePath));
			cr1 = new CsvListReader(br1,CsvPreference.STANDARD_PREFERENCE);
			br2 = new BufferedReader(new FileReader(datePath));
			cr2 = new CsvListReader(br2,CsvPreference.STANDARD_PREFERENCE);
			br3 = new BufferedReader(new FileReader(sharesPath));
			cr3 = new CsvListReader(br3,CsvPreference.STANDARD_PREFERENCE);
			
			List<String>list1 = cr1.read();
			List<String>list2 = cr2.read();
			List<String>list3 = cr3.read();

			while(list1!=null&&list2!=null&&list3!=null){
				for(int i=0;i<8;i++){
				if(list1==null||list2==null||list3==null)break;
				
				String symbol = list1.get(1);
				String revenue = list1.get(5+i);
				String COGS = list1.get(13+i);
				String RD = list1.get(29+i);
				String interest = list1.get(45+i);
				String GrossProfit = list1.get(21+i);
				String TotOperExp = list1.get(61+i);
				String OperInc = list1.get(69+i);
				String NetInc = list1.get(141+i);
				String PreTaxInc = list1.get(93+i);
				String AfterTaxInc = list1.get(109+i);
				
				String floats = list3.get(21);
				String shares = list3.get(44+i);
				String institutionHolder = list3.get(31);
				
				String q1 = list2.get(0+i);
					
				Date d;
				if(!q1.equals("/  /")){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				d = LTIDate.clearHMSM(sdf.parse(q1));
				}
				else d = null;
				
				if(d==null)continue;
				else{
					
					if(this.getIncomeStatement(symbol, LTIDate.getYear(d), LTIDate.getQuarter(d))==null){
					IncomeStatement is = new IncomeStatement();
					is.setDate(d);
					is.setYear(LTIDate.getYear(d));
					is.setQuarter(LTIDate.getQuarter(d));
					is.setSymbol(symbol);
					is.setRevenue(Double.parseDouble(revenue)*1000000);
					is.setCOGS(Double.parseDouble(COGS)*1000000);
					is.setRND(Double.parseDouble(RD)*1000000);
					is.setIntExp(Double.parseDouble(interest)*1000000);
					is.setGrossProfit(Double.parseDouble(GrossProfit)*1000000);
					is.setTotalOperExp(Double.parseDouble(TotOperExp)*1000000);
					is.setNetInc(Double.parseDouble(NetInc)*1000000);
					is.setPreTaxInc(Double.parseDouble(PreTaxInc)*1000000);
					is.setAfterTaxInc(Double.parseDouble(AfterTaxInc)*1000000);
					is.setOperInc(Double.parseDouble(OperInc)*1000000);
					is.setFloats(Double.parseDouble(floats)*1000000);
					is.setShares(Double.parseDouble(shares)*1000000);
					if(!(Double.parseDouble(institutionHolder)<0))is.setInstitutionHolder(Double.parseDouble(institutionHolder));
					
					this.saveIncomeStatement(is);
					System.out.println("Date:"+d+";"+"Symbol:"+symbol+";"+"Revenue:"+revenue+";"+"COGS:"+COGS+";"+"RD:"+RD+";"+"Interest:"+interest+";"+"GrossProfit:"+GrossProfit+";"+"TotOperExp:"+TotOperExp+";"+"OperInc:"+OperInc+";"+"NetInc:"+NetInc+";"+"PreTaxInc:"+PreTaxInc+";");
				}
				}
			}
				list1= cr1.read();
				list2= cr2.read();
				list3 = cr3.read();
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cr1!=null){
				try{
					cr1.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void importHistoricalBS(){
		BufferedReader br1 =  null;
		CsvListReader cr1 = null;
		BufferedReader br2 =  null;
		CsvListReader cr2 = null;
		
		String filePath = "H:\\LTISystem Program\\Glossary files\\AI\\AI\\BALANCE_SHEET_QUARTER.CSV";
		String datePath = "H:\\LTISystem Program\\Glossary files\\AI\\AI\\DATES AND PERIODS.CSV";
		
		try{
			br1 = new BufferedReader(new FileReader(filePath));
			cr1 = new CsvListReader(br1,CsvPreference.STANDARD_PREFERENCE);
			br2 = new BufferedReader(new FileReader(datePath));
			cr2 = new CsvListReader(br2,CsvPreference.STANDARD_PREFERENCE);
		
			List<String>list1 = cr1.read();
			List<String>list2 = cr2.read();

			while(list1!=null&&list2!=null){
				for(int i=0;i<8;i++){
				if(list1==null||list2==null)break;
				
				String symbol = list1.get(1);
				String cashEquiv = list1.get(5+i);
				String stInv = list1.get(13+i);
				String netRec = list1.get(21+i);
				String inventory = list1.get(29+i);
				String otherCurAsset = list1.get(37+i);
				String curAsset = list1.get(45+i);
				String PPE = list1.get(53+i);
				String ltInv = list1.get(61+i);
				String intangibles = list1.get(69+i);
				String otherLtAsset = list1.get(77+i);
				String asset = list1.get(85+i);
				String acctPayable = list1.get(93+i);
				String stDebt = list1.get(101+i);
				String otherCurLiab = list1.get(109+i);
				String curLiab = list1.get(117+i);
				String ltDebt = list1.get(125+i);
				String otherLtLiab = list1.get(133+i);
				String liab = list1.get(141+i);
				String preferredStock = list1.get(149+i);
				String commonStock = list1.get(157+i);			
				
				String q1 = list2.get(0+i);
				
				Date d;
				if(!q1.equals("/  /")){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				d = LTIDate.clearHMSM(sdf.parse(q1));
				}
				else d = null;				
				if(d==null)continue;
				else{			
					if(this.getBalanceStatement(symbol, LTIDate.getYear(d), LTIDate.getQuarter(d))==null){
					BalanceStatement bs = new BalanceStatement();
					bs.setDate(d);
					bs.setYear(LTIDate.getYear(d));
					bs.setQuarter(LTIDate.getQuarter(d));
					bs.setSymbol(symbol);
					bs.setCashNEquiv(Double.parseDouble(cashEquiv)*1000000);
					bs.setShortTermInv(Double.parseDouble(stInv)*1000000);
					bs.setNetRec(Double.parseDouble(netRec)*1000000);
					bs.setInventory(Double.parseDouble(inventory)*1000000);
					bs.setOtherCurAsset(Double.parseDouble(otherCurAsset)*1000000);
					bs.setCurAssets(Double.parseDouble(curAsset)*1000000);
					bs.setPPNE(Double.parseDouble(PPE)*1000000);
					bs.setLongTermInv(Double.parseDouble(ltInv)*1000000);
					bs.setIntangibles(Double.parseDouble(intangibles)*1000000);
					bs.setOtherLongTermAsset(Double.parseDouble(otherLtAsset)*1000000);
					bs.setAssets(Double.parseDouble(asset)*1000000);
					bs.setAcctPayable(Double.parseDouble(acctPayable)*1000000);
					bs.setShortTermDebt(Double.parseDouble(stDebt)*1000000);
					bs.setOtherCurLiab(Double.parseDouble(otherCurLiab)*1000000);
					bs.setCurLiab(Double.parseDouble(curLiab)*1000000);
					bs.setLongTermDebt(Double.parseDouble(ltDebt)*1000000);
					bs.setOtherLongTermLiab(Double.parseDouble(otherLtLiab)*1000000);
					bs.setLiab(Double.parseDouble(liab)*1000000);
					bs.setPreferredStock(Double.parseDouble(preferredStock)*1000000);
					bs.setCommonStock(Double.parseDouble(commonStock)*1000000);
					this.saveBalanceStatement(bs);
					System.out.println("Date:"+d+";"+"Symbol:"+symbol);
					}
				}
				}
				list1= cr1.read();
				list2= cr2.read();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cr1!=null||cr2!=null){
				try{
					cr1.close();
					cr2.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void importHistoricalCF(){
		BufferedReader br1 =  null;
		CsvListReader cr1 = null;
		BufferedReader br2 =  null;
		CsvListReader cr2 = null;
		
		String filePath = "H:\\LTISystem Program\\Glossary files\\AI\\CASH FLOW\\CASH FLOW QUARTER.CSV";
		String datePath = "H:\\LTISystem Program\\Glossary files\\AI\\AI\\DATES AND PERIODS.CSV";
		
		try{
			br1 = new BufferedReader(new FileReader(filePath));
			cr1 = new CsvListReader(br1,CsvPreference.STANDARD_PREFERENCE);
			br2 = new BufferedReader(new FileReader(datePath));
			cr2 = new CsvListReader(br2,CsvPreference.STANDARD_PREFERENCE);
		
			List<String>list1 = cr1.read();
			List<String>list2 = cr2.read();

			while(list1!=null&&list2!=null){
				for(int i=0;i<8;i++){
				if(list1==null||list2==null)break;
				
				String symbol = list1.get(1);
				String CapExp = list1.get(4+i);
				String CurrencyAdj = list1.get(64);				
				String TCF = list1.get(28+i);
				String TCI = list1.get(36+i);
				String TCO = list1.get(44+i);
				
				String q1 = list2.get(0+i);
	
				Date d;
				if(!q1.equals("/  /")){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				d = LTIDate.clearHMSM(sdf.parse(q1));
				}
				else d = null;				
				if(d==null)continue;
				else{			
					if(this.getCashFlow(symbol, LTIDate.getYear(d), LTIDate.getQuarter(d))==null){
					CashFlow cf = new CashFlow();
					cf.setDate(d);
					cf.setYear(LTIDate.getYear(d));
					cf.setQuarter(LTIDate.getQuarter(d));
					cf.setSymbol(symbol);
					cf.setCapExp(Double.parseDouble(CapExp)*1000000);
					cf.setCurrencyAdj(Double.parseDouble(CurrencyAdj)*1000000);
					cf.setFinCF(Double.parseDouble(TCF)*1000000);
					cf.setInvCF(Double.parseDouble(TCI)*1000000);
					cf.setOperCF(Double.parseDouble(TCO)*1000000);
					
					this.saveCashFlow(cf);
					System.out.println("Date:"+d+";"+"Symbol:"+symbol);
					}
				}
				}
				list1= cr1.read();
				list2= cr2.read();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cr1!=null||cr2!=null){
				try{
					cr1.close();
					cr2.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	
	public void ImportYearlyIS(){
		BufferedReader br1 =  null;
		CsvListReader cr1 = null;
		BufferedReader br2 =  null;
		CsvListReader cr2 = null;
		BufferedReader br3 =  null;
		CsvListReader cr3 = null;
		String filePath = "H:\\LTISystem Program\\Glossary files\\AI\\AI\\INCOME_STMT_ANNUAL.CSV";
		String datePath = "H:\\LTISystem Program\\Glossary files\\AI\\AI\\DATES AND PERIODS.CSV";
		String sharesPath = "H:\\LTISystem Program\\Glossary files\\AI\\AI\\SHARES STATS.CSV";
		try{
			br1 = new BufferedReader(new FileReader(filePath));
			cr1 = new CsvListReader(br1,CsvPreference.STANDARD_PREFERENCE);
			br2 = new BufferedReader(new FileReader(datePath));
			cr2 = new CsvListReader(br2,CsvPreference.STANDARD_PREFERENCE);
			br3 = new BufferedReader(new FileReader(sharesPath));
			cr3 = new CsvListReader(br3,CsvPreference.STANDARD_PREFERENCE);
			
			List<String>list1 = cr1.read();
			List<String>list2 = cr2.read();
			List<String>list3 = cr3.read();

			while(list1!=null&&list2!=null&&list3!=null){
				for(int i=0;i<7;i++){
				if(list1==null||list2==null||list3==null)break;
				
				String symbol = list1.get(1);
				String revenue = list1.get(6+i);
				String COGS = list1.get(14+i);
				String RD = list1.get(30+i);
				String IntExp = list1.get(46+i);
				String IncomeTax = list1.get(102+i);
				String GrossProfit = list1.get(22+i);
				String TotOperExp = list1.get(62+i);
				String OperInc = list1.get(70+i);
				String PreTaxInc = list1.get(94+i);
				String AfterTaxInc = list1.get(110+i);
				String NetInc = list1.get(142+i);
				
				
				String floats = list3.get(21);
				String shares = list3.get(52+i);
				String institutionHolder = list3.get(31);
				
				String q1 = list2.get(8+i);
					
				Date d;
				if(!q1.equals("/  /")){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				d = LTIDate.clearHMSM(sdf.parse(q1));
				}
				else d = null;
				
				if(d==null)continue;
				else{
					
					if(this.getYearlyIncomeStatement(symbol, LTIDate.getYear(d))==null){
					YearlyIncomeStatement yis = new YearlyIncomeStatement();
					yis.setEndDate(d);
					yis.setYear(LTIDate.getYear(d));
					yis.setSymbol(symbol);
					yis.setRevenue(Double.parseDouble(revenue)*1000000);
					yis.setCOGS(Double.parseDouble(COGS)*1000000);
					yis.setRD(Double.parseDouble(RD)*1000000);
					yis.setIntExp(Double.parseDouble(IntExp)*1000000);
					yis.setIncomeTax(Double.parseDouble(IncomeTax)*1000000);
					yis.setGrossProfit(Double.parseDouble(GrossProfit)*1000000);
					yis.setTotalOperExp(Double.parseDouble(TotOperExp)*1000000);
					yis.setOperInc(Double.parseDouble(OperInc)*1000000);
					yis.setPreTaxInc(Double.parseDouble(PreTaxInc)*1000000);
					yis.setAfterTaxInc(Double.parseDouble(AfterTaxInc)*1000000);
					yis.setNetInc(Double.parseDouble(NetInc)*1000000);
					
					yis.setFloats(Double.parseDouble(floats)*1000000);
					yis.setShares(Double.parseDouble(shares)*1000000);
					if(!(Double.parseDouble(institutionHolder)<0))yis.setInstitutionHolder(Double.parseDouble(institutionHolder));
					
					this.saveYearlyIncomeStatement(yis);
					System.out.println("Date:"+d+";"+"Symbol:"+symbol+";"+"Revenue:"+revenue+";"+"COGS:"+COGS+";"+"RD:"+RD+";"+"Interest:"+IntExp+";"+"GrossProfit:"+GrossProfit+";"+"TotOperExp:"+TotOperExp+";"+"OperInc:"+OperInc+";"+"NetInc:"+NetInc+";"+"PreTaxInc:"+PreTaxInc+";");
				}
				}
			}
				list1= cr1.read();
				list2= cr2.read();
				list3 = cr3.read();
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cr1!=null){
				try{
					cr1.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public void ImportYearlyBS(){
		BufferedReader br1 =  null;
		CsvListReader cr1 = null;
		BufferedReader br2 =  null;
		CsvListReader cr2 = null;
		
		String filePath = "H:\\LTISystem Program\\Glossary files\\AI\\AI\\BALANCE_SHEET_ANNUAL.CSV";
		String datePath = "H:\\LTISystem Program\\Glossary files\\AI\\AI\\DATES AND PERIODS.CSV";
		
		try{
			br1 = new BufferedReader(new FileReader(filePath));
			cr1 = new CsvListReader(br1,CsvPreference.STANDARD_PREFERENCE);
			br2 = new BufferedReader(new FileReader(datePath));
			cr2 = new CsvListReader(br2,CsvPreference.STANDARD_PREFERENCE);
		
			List<String>list1 = cr1.read();
			List<String>list2 = cr2.read();

			while(list1!=null&&list2!=null){
				for(int i=0;i<7;i++){
				if(list1==null||list2==null)break;		
				String symbol = list1.get(1);
				String Cash = list1.get(5+i);
				String stInv = list1.get(12+i);
				String acctRec = list1.get(19+i);
				String inventory = list1.get(26+i);
				String otherCurAsset = list1.get(33+i);
				String curAsset = list1.get(40+i);
				String PPE = list1.get(47+i);
				String ltInv = list1.get(54+i);
				String intangibles = list1.get(61+i);
				String otherLtAsset = list1.get(68+i);
				String asset = list1.get(75+i);
				String acctPayable = list1.get(82+i);
				String stDebt = list1.get(89+i);
				String otherCurLiab = list1.get(96+i);
				String curLiab = list1.get(103+i);
				String ltDebt = list1.get(110+i);
				String otherLtLiab = list1.get(117+i);
				String liab = list1.get(124+i);
				String preferredStock = list1.get(131+i);
				String TotalEquity = list1.get(138+i);			
				
				String q1 = list2.get(8+i);
				
				Date d;
				if(!q1.equals("/  /")){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				d = LTIDate.clearHMSM(sdf.parse(q1));
				}
				else d = null;				
				if(d==null)continue;
				else{			
					if(this.getYearlyBalanceStatement(symbol, LTIDate.getYear(d))==null){
					YearlyBalanceStatement ybs = new YearlyBalanceStatement();
					ybs.setEndDate(d);
					ybs.setYear(LTIDate.getYear(d));
					ybs.setSymbol(symbol);
					ybs.setCash(Double.parseDouble(Cash)*1000000);
					ybs.setShortTermInv(Double.parseDouble(stInv)*1000000);
					ybs.setAcctRec(Double.parseDouble(acctRec)*1000000);
					ybs.setInventory(Double.parseDouble(inventory)*1000000);
					ybs.setOtherCurAssets(Double.parseDouble(otherCurAsset)*1000000);
					ybs.setCurAssets(Double.parseDouble(curAsset)*1000000);
					ybs.setPPE(Double.parseDouble(PPE)*1000000);
					ybs.setLongTermInv(Double.parseDouble(ltInv)*1000000);
					ybs.setIntangibles(Double.parseDouble(intangibles)*1000000);
					ybs.setOtherLTAssets(Double.parseDouble(otherLtAsset)*1000000);
					ybs.setAssets(Double.parseDouble(asset)*1000000);
					ybs.setAcctPayable(Double.parseDouble(acctPayable)*1000000);
					ybs.setShortTermDebt(Double.parseDouble(stDebt)*1000000);
					ybs.setOtherCurLiab(Double.parseDouble(otherCurLiab)*1000000);
					ybs.setCurLiab(Double.parseDouble(curLiab)*1000000);
					ybs.setLongTermDebt(Double.parseDouble(ltDebt)*1000000);
					ybs.setOtherLTLiab(Double.parseDouble(otherLtLiab)*1000000);
					ybs.setLiab(Double.parseDouble(liab)*1000000);
					ybs.setPreferredStock(Double.parseDouble(preferredStock)*1000000);
					ybs.setTotalEquity(Double.parseDouble(TotalEquity)*1000000);
					this.saveYearlyBalanceStatement(ybs);
					System.out.println("Date:"+d+";"+"Symbol:"+symbol+"Cash"+Cash+"stInv"+stInv+"acctRec"+acctRec+"inventory"+inventory+"otherCurAsset"+otherCurAsset+"curAsset"+curAsset+"PPE"+PPE+"ltInv"+ltInv+"intangibles"+intangibles+"otherLtAsset"+otherLtAsset+"asset"+asset+"acctPayable"+acctPayable+"stDebt"+stDebt+"otherCurLiab"+otherCurLiab+"curLiab"+curLiab+"ltDebt"+ltDebt+"otherLtLiab"+otherLtLiab+"liab"+liab+"preferredStock"+preferredStock+"TotalEquity"+TotalEquity);
					}
				}
				}
				list1= cr1.read();
				list2= cr2.read();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cr1!=null||cr2!=null){
				try{
					cr1.close();
					cr2.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	public void ImportYearlyCF(){
		BufferedReader br1 =  null;
		CsvListReader cr1 = null;
		BufferedReader br2 =  null;
		CsvListReader cr2 = null;
		
		String filePath = "H:\\LTISystem Program\\Glossary files\\AI\\CASH FLOW\\CASH FLOW ANNUAL.CSV";
		String datePath = "H:\\LTISystem Program\\Glossary files\\AI\\AI\\DATES AND PERIODS.CSV";
		
		try{
			br1 = new BufferedReader(new FileReader(filePath));
			cr1 = new CsvListReader(br1,CsvPreference.STANDARD_PREFERENCE);
			br2 = new BufferedReader(new FileReader(datePath));
			cr2 = new CsvListReader(br2,CsvPreference.STANDARD_PREFERENCE);
		
			List<String>list1 = cr1.read();
			List<String>list2 = cr2.read();

			while(list1!=null&&list2!=null){
				for(int i=0;i<7;i++){
				if(list1==null||list2==null)break;
				
				String symbol = list1.get(1);
				String CapExp = list1.get(5+i);			
				String TCF = list1.get(29+i);
				String TCI = list1.get(37+i);
				String TCO = list1.get(45+i);
				
				String q1 = list2.get(8+i);
	
				Date d;
				if(!q1.equals("/  /")){
				SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
				d = LTIDate.clearHMSM(sdf.parse(q1));
				}
				else d = null;				
				if(d==null)continue;
				else{			
					if(this.getYearlyCashFlow(symbol, LTIDate.getYear(d))==null){
					YearlyCashFlow ycf = new YearlyCashFlow();
					ycf.setEndDate(d);
					ycf.setYear(LTIDate.getYear(d));
					ycf.setSymbol(symbol);
					ycf.setCapExp(Double.parseDouble(CapExp)*1000000);
					ycf.setFinCF(Double.parseDouble(TCF)*1000000);
					ycf.setInvCF(Double.parseDouble(TCI)*1000000);
					ycf.setOperCF(Double.parseDouble(TCO)*1000000);
					
					this.saveYearlyCashFlow(ycf);
					System.out.println("Date:"+d+";"+"Symbol:"+symbol+"CapExp"+CapExp+"TCF"+TCF+"TCI"+TCI+"TCO"+TCO);
					}
				}
				}
				list1= cr1.read();
				list2= cr2.read();
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cr1!=null||cr2!=null){
				try{
					cr1.close();
					cr2.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
		
	}

	/***********************************************************************/     
	/*                       Annual  Financial Statements                 */
	/*********************************************************************/
	
	@Override
	public YearlyBalanceStatement getYearlyBalanceStatement(String symbol,int year) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(YearlyBalanceStatement.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.eq("Year", year));
		List<YearlyBalanceStatement> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	@Override
	public YearlyCashFlow getYearlyCashFlow(String symbol, int year) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(YearlyCashFlow.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.eq("Year", year));
		List<YearlyCashFlow> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	@Override
	public YearlyIncomeStatement getYearlyIncomeStatement(String symbol,int year) {
		DetachedCriteria detachedCriteria = DetachedCriteria.forClass(YearlyIncomeStatement.class);
		detachedCriteria.add(Restrictions.eq("Symbol", symbol));
		detachedCriteria.add(Restrictions.eq("Year", year));
		List<YearlyIncomeStatement> bolist = super.findByCriteria(detachedCriteria);
		if (bolist.size() >= 1)
			return bolist.get(0);
		else
			return null;
	}

	@Override
	public void updateYearlyBalanceStatement(YearlyBalanceStatement ybs) {
		getHibernateTemplate().update(ybs);
	}

	@Override
	public void updateYearlyCashFlow(YearlyCashFlow ycf) {
		getHibernateTemplate().update(ycf);
	}

	@Override
	public void updateYearlyIncomeStatement(YearlyIncomeStatement yis) {
		getHibernateTemplate().update(yis);
	}

	@Override
	public YearlyBalanceStatement getYearlyBalanceStatement(Long id) {
		return (YearlyBalanceStatement) getHibernateTemplate().get(YearlyBalanceStatement.class, id);
	}

	@Override
	public YearlyCashFlow getYearlyCashFlow(Long id) {
		return (YearlyCashFlow) getHibernateTemplate().get(YearlyCashFlow.class, id);
	}

	@Override
	public YearlyIncomeStatement getYearlyIncomeStatement(Long id) {
		return (YearlyIncomeStatement) getHibernateTemplate().get(YearlyIncomeStatement.class, id);
	}

	@Override
	public void removeYearlyBalanceStatement(long id) {
		Object obj = getHibernateTemplate().get(YearlyBalanceStatement.class, id);
		getHibernateTemplate().delete(obj);
		
	}

	@Override
	public void removeYearlyCashFlow(long id) {
		Object obj = getHibernateTemplate().get(YearlyCashFlow.class, id);
		getHibernateTemplate().delete(obj);	
	}

	@Override
	public void removeYearlyIncomeStatement(long id) {
		Object obj = getHibernateTemplate().get(YearlyIncomeStatement.class, id);
		getHibernateTemplate().delete(obj);	
	}

	@Override
	public Long saveYearlyBalanceStatement(YearlyBalanceStatement ybs) {
		getHibernateTemplate().save(ybs);
		return ybs.getID();
	}

	@Override
	public Long saveYearlyCashFlow(YearlyCashFlow ycf) {
		getHibernateTemplate().save(ycf);
		return ycf.getID();
	}

	@Override
	public Long saveYearlyIncomeStatement(YearlyIncomeStatement yis) {
		getHibernateTemplate().save(yis);
		return yis.getID();
	}
	
	public void updateHistoricalIS(){
		BufferedReader br1 =  null;
		CsvListReader cr1 = null;
		BufferedReader br2 =  null;
		CsvListReader cr2 = null;
		String filePath = "H:\\LTISystem Program\\Glossary files\\AI\\AI\\INCOME_STMT_QUARTER.CSV";
		String datePath = "H:\\LTISystem Program\\Glossary files\\AI\\AI\\DATES AND PERIODS.CSV";
		String[]arr={"AFL","AIZ","AMIC","AOC","CNO","EIHI","FPIC","GTS","PICO","SFG","UNM","AMG","AMP","BEN","BX","CLMS","CNS","ACAS","AINV","ALD","ARCC","AACC","ACF","ADS","AEA","AXP","BKCC","CACC","CCRT","SBP","AFSI","AJG","BRO","CHSI","CRVL","DCAP","EHTH","MMC","NFP","UAHC","WSH","AMPL","AMTD","BGCP","COWN","ETFC","IBKR","JMP","KBW","LTS","MF","MKTX","OXPS","FNM","HCM","HF","HTS","IOT","MFA","NLY","NYMT","RWT","SNFCA","UPFC","ACAP","ACE","ACGL","AFFM","AFG","AGII","AHL","AIG","ALL","AMPH","AMSF","ASI","AWH","AXS","ADC","AKR","ALX","ARL","CBG","CHLN","CIM","CT","CXW","DFT","FOR","GBE","GKK","ABCB","ABVA","ACBA","AMNB","ANCX","ANNB","ASFN","BAYN","BBT","BCAR","BKOR","BKSC","BNCN","BOFL","CAPE","CBAN","CBKN","CCBG","CFFC","CFFI","CHCO","CLBH","AMFI","ASBC","BUSE","CBC","CHEV","CHFC","CITZ","CMA","CNAF","CORS","CRBC","CZWI","DEAR","FBIZ","FBMI","FFBC","FFNM","FITB","FMBI","FMER","FPFC","FRME","GABC","HBAN","IBCP","ALLB","ALNC","AROW","ASRV","ATLO","BARI","BCBP","BDGE","BERK","BHB","BMTC","BNCL","BNV","BPFH","CAC","CBNJ","CBU","CCBP","CCNE","CEBK","CIZN","CJBK","CMSB","CNBC","CNBKA","AANB","AMRB","AWBC","BBNK","BMRC","BOCH","BOH","BSRR","CACB","CASB","CATY","CBBO","CBON","CCBD","CFNB","CPF","CVBF","CVCY","CVLL","CWBC","CWLZ","CYN","DNBF","EWBC","ACFC","APAB","AUBN","BFNB","BKBK","BTFG","BXS","CADE","CFNL","CNB","CSFL","CSNT","CTBI","CZFC","EVBS","FABK","FBMS","FBSS","FFKT","FFKY","FHN","FMFC","FNB","BANF","BOKF","CASS","CBSH","CFR","COBZ","CSHB","EBTX","EFSC","FCCO","FFIN","FFNW","FFSX","FSNM","GFED","GSBC","HTLF","IBOC","LARK","LBCP","MCBI","MFNC","MOFG","OSBK","ABBC","ABCW","ABNJ","AF","ASBI","ATBC","BANR","BBX","BCSB","BFBC","BFED","BFIN","BFSB","BHLB","BKMU","ABK","FAF","FNF","ITIC","MBI","MTG","ORI","PMI","RDN","STC","SUR","TGIC","AEE","ALE","AVA","CHG","CMS","CNP","DPL","ED","EXC","FPU","LNT","MGEE","MIR","NI","NU","NVE","NWE","PCG","PEG","PNM","ACPW","AEP","AES","AYE","BCON","BKH","CEG","CNL","CPN","CWCO","AGL","APU","ATO","CLNE","CPK","DGAS","EGN","EPB","EQT","EWST","GAS","LG","NFG","NJR","NWN","OKE","ARTNA","AWK","AWR","BWTR","CTWS","CWT","MSEX","PNNW","SJW","SWWC","WTR","YORW","BPSG","DHIL","FBR","GBL","GFIG","GHL","ITG","JEF","LAB","LM","NITE","OPY","PLCC","RJF","SF","SIEB","WDR","AAME","AEL","AGO","CIA","DFG","FAC","FFG","GNW","IHC","IPCR","KCLI","BAC","BK","RODM","SCHW","BLK","C","JPM","KEY","AFN","ANH","ATAX","BRT","CMO","CRE","DFR","DRL","DX","MHLD","ELC","AXC","HDS","ALUS","AMV","AYA","AWSR.OB","MABAA.OB","AMTC","PEX","APRB.OB","AMEH.OB","ASCQ.OB","ASPO.OB","AXG","AUCAF.OB","BHCG.OB","BHWF.OB","BPW","CAEL.OB","CMKT.OB","CLA","CYSU.OB","CLNH.OB","HOL","CIIC","CNSJ.OB","CLXS.OB","COBK","BUS","CWBS","BTC","CODI","CCVR.OB","CVA","CRT","CRWE.OB","DCDC.OB","DOM","DMLP","DEGH.OB","EBIG.OB","NGT","EDRG.OB","ESA","ESGI.OB","EST","FCIC.OB","FCVA","FSTF","FMNG.OB","FIGI","GFN","GCNV.OB","GGHO.OB","GHQ","GQN","GHC","GISV.OB","GPH","GEYO.OB","GGA","GLFO.OB","HHDG.OB","HEK","TOH","HBRF.OB","HIA","HGT","HYDQ.OB","CTCJ.OB","IDI","IAN","IAQG.OB","ICH","IVOI.OB","JKAK.OB","KHA","LPTC.OB","LTHO.OB","LIA","LTVL.OB","LCHL.OB","LIXT.OB","LRT","MARPS","MAQC.OB","MHLI.OB","MBH","TVH","MRHD.OB","MCMV.OB","NDAQ","NVDF.OB","NBMF.OB","NEWT","NSAQ.OB","NAQ","NYX","OOLN.OB","OKN","NLX","PGID.OB","PBT","PIP","PGRI.OB","PAX","RAME","RTRO.OB","RBCF.OB","SBR","SFE","STJC.OB","SJT","SFEF.OB","MEJ","SHIP","SOSV.OB","SACQ.OB","SVGP.OB","OOO","TNF","TCXB.OB","TELOZ","TPL","THLM.OB","TIRTZ.OB","TTSP.OB","TGY","TISG.OB","TUX","TRBD.OB","UHFI.OB","UBNK","UWBK","VTG","VRY","VPFG","VYTC.OB","WAL","WSMO.OB","WTU","XDRC.OB","ZAP","TWFH.OB","TTY","CBIN","EUBK","FBP","BPOP","IBM","AVD","CF","CGA","CMP","COIN","EDEN","POT","TRA","TNH","AA","CENX","KALU","APD","ASH","CE","DOW","WIRE","FCX","AGT","GSS","NEM","RGLD","VGZ","WGW","AXAS","AEZ","APC","APA","APAGF","ATPG","AOG","BRN","BRY","BDCO","KAZ","BEXP","COG","CPE","CRZO","CHK","XEC","CKX","CWEI","CRK","MCF","XTEX","DPTR","DNR","DVN","DBLE","EPEX","EEQ","EAC","END","EPD","EOG","EPM","XCO","FPP","FST","GSX","GPR","GEOI","GMXR","GDP","GTE","GPOR","HKN","HUSA","ISRL","LINE","LEI","MPET","MMLP","MMR","TMR","MXC","NFX","NBL","OXY","PHX","PLLL","PVA","HK","PQ","PXD","PXP","PNRG","PDO","STR","KWK","RRC","ROSE","ROYL","SWN","SM","SGY","SFY","TRGL","TXCO","UPL","EGY","WMZ","XTO","ATI","ARLP","ANR","ACI","SHZ","CNX","BOOM","FCL","ZINC","ICO","JRCC","MEE","NANX","NCOC","NRP","PZG","PCX","BTU","PVG","PVR","RTI","SWC","TLR","TIE","URRE","USEG","USU","WLT","WLB","CVX","XOM","AHGP","CHBT","AREX","ARD","ATLS","ATN","AHD","ATW","BBG","BPZ","BBEP","BRNC","CFW","LNG","CXG","CXO","CLR","QBC","DO","DNE","ENP","ESV","EVEP","FXEN","GST","GMET","GLRP","GIFI","HP","HERO","HPGP","HLND","HDY","KEG","KOG","LGCY","ME","MWE","MVO","NBR","NTG","GBR","NGAS","NE","NOG","PKD","PTEN","PRC","PINN","PDC","PSE","PDE","QRCP","REXX","RDC","SD","STXX","TGC","TEC","RIG","TIV","UDRL","UNT","VQ","WTI","WLL","ALY","BHI","BAS","BJS","BOLT","WEL","DVR","CAM","CRR","CPX","CLB","DWSN","DRQ","EXXI","EXH","EXLP","FTI","GOK","GLBL","GLF","HAL","HLX","NOV","NGS","NR","OII","OIS","OMNI","RES","SLB","SII","SPN","SWSI","TESO","TTI","TGE","TRMA","WRES","WFT","WG","APL","BGH","BPL","CPNO","DPM","DEP","EP","EEP","ETE","ETP","HEP","KMP","MGG","MMP","NS","NSH","OKS","PAA","RGNC","SE","SEP","SXL","NGLS","TCLP","TPP","TLP","WES","WMB","ALJ","ARSD","BPT","CLMT","XTXI","CVI","DK","EROC","EPE","FTO","HES","HOC","IMO","PETD","SUN","SYNM","TSO","VLO","WNR","CDE","HL","ADES","ALTI","APFC","BIOF","CBT","CYAN","CYT","FOE","FSI","FTK","IOSP","KRO","LZ","MACE","NOEC","NEU","OMG","PEIX","KWR","SXT","SDTH","SIAL","SOA","SYMX","VRNM","GRA","WLK","WPZ","AKS","CRS","CPSL","CLF","FRD","GSI","ROCK","GNI","HSC","HAYN","MEA","NWPX","ZEUS","STLD","SUTR","USAP","ALB","ARJ","CCC","CDTI","GGC","HUN","ICOC","IPHS","IFF","LDL","NLC","NL","OLN","POL","PX","ROC","TORM","VHI","ASFI","SMG","CBE","CR","GE","PPG","RTK","UTX","MMM","HDSN","LII","WHR","CVR","F","GM","SORL","AXL","ARGN","ARM","ALV","BWA","CAAS","CVGI","CSLR","BW","ATC","DAN","FDML","FSYS","GETI","GNTX","JCI","MLR","MOD","MPAA","NOBL","PLI","SMP","SRI","BQI","TEN","TWI","WBC","WMCO","WATG","HOOK","CQP","DPS","HANS","JSDA","MGN","REED","GPRE","ROX","CEDC","PENX","WVVI","PPO","CATM","WDFC","CSTR","TPUT","DBD","EFOI","FEP","DSUP","IIIN","HYC","NUE","KBALB","KNL","SYNL","LYTS","PTC","PBI","MTX","XRX","MO","CHKE","DHR","PM","RAI","TXT","STSI","ARCI","VGR","HELE","CLX","IRBT","NPK","ECL","SPW","OBCI","SCL","DORM","HAYZ","IPSU","LEA","DF","STRT","LWAY","SUP","SYUT","TRW","AVID","CSCD","SAM","COBR","DTSI","TAP","MSN","COKE","HAR","CCE","ICOP","COT","KOSS","PBG","RWC","PAS","ROFO","KO","STZ","UEIC","ALCO","HNI","ANDE","ADM","SCS","BG","VIRC","CQB","CHD","DAR","KFT","LANC","HSY","RMCF","ETH","TR","TOF","FLXS","FO","FBN","LEG","FDP","TPX","USHS","XNN","BLT","BSET","JAH","HOFT","LBY","LCUT","NWL","BCPC","SCSS","CGL-A","HOGS","SEB","ABD","TSN","ACU","ATX","EBF","OMX","TFCO","SR","BLL","BMS","BWY","CCK","MWV","MPAC","NTIC","OI","PKG","PTV","SEE","BEST","SLGN","TUP","UFPT","AVY","BZ","BKI","CSAR","CVO","UFS","IAX","IP","KAI","KPPC","MERC","NSHA","NP","TIS","GLT","PCH","RKT","SWM","SON","TIN","WPP","ACV","AVP","BARE","CAW","CL","RDEN","EL","IPAR","KMB","BHIP","PARL","FACE","PG","REV","TSC","SUMR","UG","CHYR","EK","IKNX","IMAX","NRVN","XRIT","FEED","AIPC","CPO","FARM","GLDC","GMCR","JJSF","JBSS","K","KTEC","LNCE","MLP","MGPI","PSTA","NWD","OME","OFI","PEET","PEP","RAH","FRZ","SLE","SENEA","SMBL","STKL","TSTY","HAIN","SNAK","THS","FOOD","FUQI","MOV","ORNG","SMD","LVB","ACAT","HOG","MPX","PII","ABL","AMTY","ATR","CSL","CTB","CMT","CTIB","FHC","FORD","GT","MBLX","MYE","QUIX","ROG","TG","ADGF","ALDA","ELY","CYBI","ESCA","JOUT","NLS","POOL","RGR","APP","CRI","COLM","CRWS","DLA","EBHI","EVK","GIII","HBI","JOEZ","MFB","PERY","SPOR","RL","SGC","TAGS","TRLG","UA","VFC","CROX","COH","DECK","HLYS","ICON","KSWS","KCP","BOOT","PXG","DFZ","RCKY","SKX","SHOO","TBAC","TLF","TBL","VLCM","WEYS","WWW","AOI","UVV","APII","FNET","GPIC","HAS","JAKK","LF","MCZ","MAT","RCRC","RUS","FSS","FRPT","KNDI","OSK","PCAR","SPAR","STS","WNC","ASTM","ABII","ACAD","ACHN","ACOR","AFFY","ALNY","ALTU","AOB","AMGN","FOLD","AMLN","ANSV","ANIK","AGEN","ABIO","ARIA","ATHX","BCRX","BDSI","BIIB","BMRN","BPAX","BSTC","CBM","CXM","CELG","CEGE","CTIC","CERS","CRL","CBMX","CRXX","CRTX","CRGN","CRIS","CVTX","CYCC","CYPB","CYTK","CYTX","CYTR","DNDN","DSCO","DYAX","EBS","ENZN","EPIX","EXAS","GENR","GNVC","GENZ","GILD","GTCB","GTXI","HALO","HEB","HSKA","HGSI","IDIX","IDRA","IDMI","IG","ILMN","IMM","IMGN","INCY","INHX","INSM","ITMN","JAZZ","LXRX","LIFE","LGND","MNKD","MRNA","MEDX","MRX","MBRX","MITI","MIPI","MNTA","NTII","NBIX","NVAX","NPSP","NXXI","OMPI","ENMD","ONTY","ONXX","OPTR","MDVN","OSIP","OSIR","PDLI","PGNX","QLTI","QCOR","REGN","RGEN","RPRX","RNN","SGMO","SVNT","SGEN","SNT","SQNM","STEM","SNSS","TRGT","TGEN","TECH","THRX","THLD","TSPT","TRMS","TRBN","VICL","VPHM","XNPT","XOMA","ZGEN","ABAX","AKRX","AMAG","ABMC","APPY","AVRX","CLDX","EPCT","ICGN","IDXX","ICCC","IMMU","INFI","ILI","ISTA","VIVO","MGRM","MYGN","NEOG","ORCH","ORXE","PTN","PBIO","QDEL","RGDX","DDD","SRDX","SNTA","TPTX","ACUR","ALKS","CBRX","EMIS","HSP","ISV","KV-A","MTXX","NKTR","NVD","NOVN","PPCO","PETS","QGLY","ULU","ABT","ACET","APPA","AVGN","BMY","LLY","GORX","JAV","JNJ","KERX","MRK","OSCI","PFE","SGP","SPPI","WYE","ADLR","ALXN","AGN","RDEA","ARNA","ARQL","ARYX","AVNR","AVII","CEPH","CHTT","CBST","DEPO","DRRX","DUSA","DVAX","ELI","ENDP","FRX","GERN","ISPH","ISIS","KNDL","KG","LCI","MSHL","MDCO","MBRK","NRGN","NEXM","NTMD","NBY","OPK","OREX","PTIE","VRUS","POZN","RGN","RELV","SNTS","SCLN","SEPR","SIGA","SCMP","SUPG","UTHR","VRX","VRTX","WCRX","AUXL","KUN","CSKI","MTEX","MDNU","NAII","NTY","NUTR","PRGO","PBH","SLXP","TBV","USNA","CPD","HLCS","ILE","MYL","PRX","SRLS","WPI","AET","AGP","CNC","CI","CVH","HNT","HS","HUM","MGLN","MDF","MOH","UNH","UAM","WCG","WLP","AHCI","AFAM","KAD","CHE","GTIV","LHCG","LNCR","PHC","AMSG","CYH","DYII","HMA","LPNT","MDTH","RHB","SSY","THC","UHS","ADK","AVCA","ALC","BKD","CSU","ESC","FVE","KND","NHC","ODSY","SKH","SUNH","SRZ","ENSG","ARAY","ALGN","AHPI","APT","AMAC","AMMD","HRT","ASPM","ATSI","BFRM","BSDM","CLZR","CLSN","CDIC","CHIO","CNMD","CRY","CUTR","CYNO","DRAD","DYNT","EW","LZR","ENDO","ESMC","EXAC","FONR","GB","HNSN","HTRN","HOLX","ISRG","IVC","IRIX","IVVI","KCI","LAKE","GAIT","MAKO","MASI","MDCI","MEND","MSA","MSON","BABY","NXTM","OFIX","PMTI","PHMD","RMD","RMTI","RTIX","SIRO","SLTM","SNCI","SPAN","STRC","STJ","STXS","STE","SMA","SPNC","TGX","KOOL","TOMO","ULGX","VAR","VSCI","VNUS","VOLC","WHRT","WMGI","ZMH","ZOLL","ABMD","ATEC","ADL","ANGO","AIS","ARTL","ATRC","ATRI","BAX","BDX","BLTI","BMTI","BSMD","BSX","BVX","CSCX","CRDC","BEAT","CASM","CPTS","COV","BCR","DXR","XRAY","DXCM","ELGX","EVVV","HAE","HRC","HDIX","IFLO","ICUI","INO","PODD","IART","ISR","IVD","KNSY","LMAT","LMNX","MMSI","MLAB","NSPH","NURO","NMTI","NCST","NUVA","TEAR","OCLS","OSUR","VITA","OSTE","PKI","PMII","PDEX","RVP","ROCM","SENO","SONO","SDIX","SYK","THOR","TSON","UPI","UTMD","VASC","VVUS","YDNT","AIQ","ARRY","BITI","ERES","GHDX","GXDX","LH","MTOX","NADX","PPDI","PMD","DGX","RDNT","SPEX","INMD","LCAV","PZZ","TRCR","ALLI","ASGR","ANCI","AMS","BIOS","CNU","DVA","DCAI","GRMH","HGR","HLS","EAR","HH","HYTM","IPCM","MD","NBS","NHWK","VETS","PRSC","PSYS","TLCV","USPH","VRAD","BA","RTN","AIM","ATK","ADG","ATRO","SPAB","BEAV","BZC","CVU","CW","DCO","EDAC","GD","GR","HWK","HXL","HSR","HON","KAMN","KRSL","LMIA","LMT","MOG-A","NOC","ORB","COL","SIF","SPR","TASR","TDY","TOD","TDG","TGI","EXP","RMX","RMIX","ADEP","AP","BLD","BGG","BWEN","BC","CFX","CMI","CVV","DOV","DRC","ETC","FLDR","FLS","GDI","GRC","GGG","IEX","ITW","IR","IIN","ITT","MNTX","MIDD","PRST","SHS","TFX","TNC","TWIN","WUHN","AG","ALG","ASTE","BUCY","CAT","CMCO","GENC","MTW","NC","PTG","TEX","AAON","ACO","APOG","AWI","CUO","DW","FAST","GFF","HW","IPII","LXU","MLM","MDU","PGTI","QEPC","NX","RSOL","TECUA","USLM","USG","CBI","CAEI","FIX","EME","IESC","KSW","PEC","PWR","SPWRA","AGX","FLR","FWLT","GV","GVA","GLDD","IGC","INSU","MTZ","MDR","MYRG","OMGI","PCR","PLPC","STRL","ABAT","AIMC","AETI","AME","AOS","ARTX","AZZ","BEZ","BDC","CPST","CBAK","CCIX","ETN","HEV","ENR","ENS","XIDE","FELE","FSIN","BGC","GTI","HRBN","LFUS","MAG","PLUG","POWL","RZ","RBC","ROK","TRCI","TNB","TIII","TLGD","ULBI","VLNC","WGOV","ZRBA","ZOLT","ASEI","B","CRDN","CCF","CFSG","CIR","EGT","EMR","NPO","HEES","MWA","NGA","OFLX","PH","PNR","ROP","SXI","SNHY","TTES","TRS","AFP","WTS","WXCO","AERT","DEL","KOP","LUK","LPX","MAS","PATK","POPE","TWP","UFPI","WY","HDNG","KDN","KMT","NNBR","ROLL","SWK","THMD","TKR","CAV","CVCO","CHB","PHHM","GTLS","GHM","LDSH","MATW","MINI","MLI","PKOH","PCP","RS","X","VMI","CECE","FTEK","MFRI","AVTR","BZH","BHS","CTX","CHCI","DHI","MHO","MDC","MTH","NVR","OHB","PHM","RYL","SPF","AERO","EML","LECO","PFIN","SSD","SNA","BDK","AIN","DII","HWG","IFSIA","MHK","DXYN","UFI","XRM","ECOL","AWX","CLH","GNH","HCCI","PESI","RSG","SRCL","WCN","WMI","WSII","WCAA","IPG","MWW","OMC","VCLK","AIRT","ATSG","CHRW","EXPD","HUBG","UPS","VEXP","AIRM","AAWW","LIMC","PHII","BKRS","CACH","CHIC","CBK","CTRN","DEST","FINL","LTD","SCVL","SMRT","SYMS","TLB","ORLY","PBY","PRTS","CRV","GPC","LKQX","AE","CAS","GLP","HWKN","FSTR","MIC","RVEP","FUEL","INT","BBGI","CXR","CMLS","EMMS","ETM","RGCI","SGA","SALM","SIRI","SBSA","BLC","CBS","CETV","CTCM","EVC","FSCI","GTN","HTV","NXST","NTN","PLA","SBGI","WPTE","AKNS","BECN","BXC","IBI","AAC","CIDM","AM","AMIN","APEI","ARP","APAC","ARBX","ATAC","ATHN","BNE","BR","CDZI","CBZ","CHMP","CLCT","CPSI","SCOR","CGX","CSGP","CSS","CYBS","DLX","DGIT","DCP","ES","ENOC","EEFT","EXLS","FIC","FIS","FADV","VIFL","GKSR","HCSG","HPY","HMSY","HQS","IMNY","IAO","ICTG","INWK","INOC","NPLA","INSW","ICE","INET","IRM","LIME","LLNW","LPSN","LMLP","MFW","MA","MELI","MGI","LABL","UEPS","ONT","OWW","ORS","PDII","PFSW","PFWD","PRAA","POWR","PRGX","RRD","REIS","REFR","DINE","ROL","SVVS","SDBT","SGRP","SYKE","SNX","TTGT","TTEC","TSS","USAT","UTK","VCGH","PGV","VVI","V","VPRT","WINA","ZANE","AMZN","BIDZ","BFLY","DLIA","EBAY","GAIA","GSIC","IACI","NSIT","LIVE","OSTK","PCCC","MALL","RBI","STMP","SYX","VVTV","CVC","CMCSA","CRWN","DISH","LBTYA","LCAPA","LNET","MCCC","SPDE","TWC","VMED","AVSR","ENPT","GTSI","IM","INXI","NAVR","PRLS","SCSC","WSTG","DCU","EMS","FGXI","TUC","MNRO","NTRI","RURL","SFLY","LOV","STAN","KDE","DUCK","FRED","TUES","NDN","CVS","MHS","OCR","PMC","RAD","WAG","ABC","BJGP","CAH","HLF","MCK","NUS","EPAX","CPLA","CECO","CAST","COCO","DV","GPX","ESI","LRN","LTRE","LINC","MMS","NLCI","REVU","STRA","UTI","BBY","HGG","RSH","RSC","AEY","AXE","ARW","VOXX","AVT","CELL","CRFT","HWCC","IFON","ISIG","JACO","NUHC","ORBT","TAIT","TESS","UPG","GWW","WSO","WCC","CKXE","GET","NWSA","TWX","DIS","DIT","CORE","NAFC","SPTN","SYY","BYI","CPHC","CHDN","DDE","LACO","MNTG","MGAM","PNK","PTEK","UBET","FUN","LYV","OUTD","PRXI","RCL","SIX","ORCD","TIXC","WMG","WWE","ARDNA","DDRX","IMKTA","PTRY","RDK","SWY","SUSS","GAP","WMK","BBBY","DWRI","HVT","JEN","KIRK","PIR","BLDR","LL","ARG","AIT","DXPE","HRSH","LAWS","SNEN","NILE","DGC","CHH","MAR","MHGC","OEH","RLH","SNSTA","HOT","WYN","CAL","DAL","UAUA","LCC","ADPI","BDMS","CDII","CFS","EXBD","DTPI","DUF","ELOY","EXPO","ESRX","FCN","IT","G","GLOI","HSII","HEW","HIL","HURN","ICFI","IDSA","III","VTIV","TMNG","BKR","NOVA","RMKR","HCKT","TGIS","TRR","WW","CCO","CTCT","HHS","HOLL","IUSA","LAMR","MCHX","MDCA","CMKG","NCMI","SGK","SORC","VCI","AHII","CHDX","HSIC","MWIV","NYER","OMI","PSSI","STAA","CKEC","CNK","DWA","DISK","LGF","MVL","NOOF","PTSX","RDI","RGC","RENT","HAST","NFLX","TWMC","AMIE","CSV","CPY","FIT","LGBT","PPD","RGS","RSCR","ROAC","SCI","STNR","STON","WOOF","WTW","WU","CRRC","MHP","AHC","DJCO","GCI","JRN","LEE","MEG","SSP","MNI","NYT","WPO","DM","MSO","MDP","PRM","PRVT","SPRO","ARII","BNI","CSX","RAIL","GWR","KSU","NSC","PRPX","PWX","TRN","UNP","WAB","AAI","ALK","ALGT","XJT","GIA","HA","JBLU","MESA","PNCL","RJET","SKYW","LUV","RNT","ACY","AYR","UHAL","CAR","CAP","DTG","GMT","HTZ","MRLN","MGRC","MIND","RCII","RRR","R","TAL","URI","WSC","WLFC","ACTG","ABCO","AMRI","ARB","ARWR","BASI","CBLI","CBTE","CVD","GTF","DCGN","ENCO","FORR","GPRO","LDR","XPRT","LUNA","MAXY","NRCI","NTSC","OMEX","PRXL","SNMX","SPIR","SMMX","ASCA","BYD","CNTY","NYNY","FLL","WOLF","LVS","LGN","MGM","MCRI","PENN","RIV","WYNN","ARKR","EAT","BWLD","BKC","CPKI","CEC","CMG","COSI","DENN","DIN","DPZ","DAVE","FRS","GTIM","GCFB","JAX","KONA","KKD","LNY","MSSR","MCD","CASA","MRT","NATH","PZZA","PFCB","PZZI","RICK","RUBO","RUTH","STRZ","TXRH","WEN","WEST","YUM","BCO","CKP","CSR","MOC","CRN","DMC","GEO","HBE","ID","LOJN","NSSC","PONE","TRIS","AXB","ACLI","EGLE","GNK","GMR","HRZ","HOS","ISH","KSP","KEX","OSP","OSG","RLOG","CKH","TBSI","TDW","TRBR","CBOU","PNRA","SBUX","THI","ACMR","BAMM","HIST","GMTN","NRGP","NRGY","IPT","JAS","HZO","MED","MDS","ODP","PERF","SBH","BID","SGU","SPH","TITN","TSCO","TA","WMAR","FLWS","BWL-A","DVD","GEE","LTM","TRK","CLUB","BGFV","CAB","DOVR","DRJ","GOLF","ASF","AHS","BBSI","CDI","CITP","CCRN","DHX","JOB","GVHR","HHGP","KELYA","KFRC","MAN","MPS","NCI","ASGN","RCMT","RHI","SFN","SRT","TSTF","TBI","VSCP","ACM","CAI","ENG","FRM","JEC","KBR","KTOS","TTEK","URS","VSR","VSEC","WYY","WLDN","BBW","ABFS","CLDN","CNW","CVTI","XPO","FWRD","FFEX","HTLD","JBHT","KNX","LSTR","MRTN","ODFL","PTSI","PATR","QLTY","SAIA","UACL","USAK","WERN","YRCW","CENT","CTHR","EDUC","LKI","MHJ","USTR","ACTU","ADBE","ADVS","ATEA","BITS","BMC","BORL","EPAY","BLSW","CA","CALL","CPBY","CTFO","CVLT","CPWR","TRAK","PROJ","EPIC","FNDT","GUID","MSFT","TYPE","NTWK","NUAN","PVSW","PTEC","PRO","QADI","RHT","RNOW","SLRY","SCIL","SMSI","SOAP","SOFO","SPSS","SXCI","SY","SNCR","TLEO","TIGR","VOXW","WZE","ACCL","AMCS","ANLY","ADP","BLKB","CALD","CATT","COGT","CTSH","CVG","CSGS","CSPI","DTLK","DWCH","DMAN","DRCO","EBIX","EPIQ","PLUS","FALC","FISV","GFSI","GVP","HMNA","ITWO","RX","INFA","ISSC","ISYS","IDN","ININ","JKHY","JDAS","KNXA","MVSN","MANT","MSTR","NTCT","N","NVTL","OMTR","ONSM","PEGA","PRFT","PKT","QDHC","QSFT","RADS","SAPE","SBN","SLH","SNIC","SFSF","SUMT","SYNA","TSYS","TRXI","UNCA","VTAL","RNIN","WSTM","APKT","ADTN","ATGN","ARRS","AWRE","BBND","CBEY","CLFD","CLRO","CMRO","CTV","JCS","GLW","DTV","TBUS","DSPG","SATS","EFJI","ELMG","ENWV","HLIT","HRS","HSTX","IDSY","IKAN","INFN","JDSU","KVHI","LORL","MRM","MOT","NTGR","NEXS","NMRX","OPTC","PIII","PWAV","QCOM","SHOR","SONS","SCON","SYMM","TLAB","TNS","VRAZ","CHIP","VSAT","WSTL","ZHNE","ZOOM","EGHT","ADPT","CCUR","OMCL","SCKT","XATA","ACFN","AVCT","EFII","ELX","ESCC","HAUP","ICAD","IGOI","IMMR","INFS","IN","INPH","KTCC","LTRX","LXK","MSII","MRCY","MTSI","OPNT","PLNR","RSYS","RIMG","SCMM","SSYS","TACT","PANL","VIDE","WAVX","ZBRA","ALAN","CML","DDUP","HILL","EMC","HTCH","IMN","ISLN","LCRD","QTM","STX","STEC","WDC","PAR","AMT","CCOI","CCI","ELNK","EQ","GEOY","GLBC","HUGH","LVLT","MDLK","NSR","WAVE","ORBC","PAET","PGI","SDXC","SVR","TSTC","TKO","TWER","VG","XFN","CRAY","IGT","NYFX","RACK","SGMS","JAVA","TDC","AEIS","AMSC","ATCO","APH","AVZA","AVX","CHP","COGO","CPII","CTS","DPW","DLB","ESIO","ENA","ESLR","HOKU","IVAC","KOPN","MXWL","NOIZ","MVIS","MOLX","NSYS","OESX","PWER","QBAK","SLI","SPA","TNL","TEL","TYC","VICR","VSH","WGA","WMS","MDRX","APY","CERN","ECLP","ETWC","HLTH","MDAS","MEDW","MEDQ","MRGE","MGT","QSII","STRM","ANSW","DATA","DST","DNB","GNET","IDC","WEBM","WXS","ACXM","ACS","AGYS","CBR","CSC","CTGX","EDGW","HX","IGTE","IFLG","MTMC","NCIT","NCR","NSTC","PER","SRX","SXE","SYNT","TSCC","TEAM","TIER","UIS","VRTU","ADAM","AKAM","ABTL","RATE","BNX","EDGR","DIET","EXPE","GOOG","HGRD","HSTM","HSWI","INSP","LOCM","LOOK","LEDR","MIVA","MOVE","NWMO","PCLN","QPSA","SOHU","KNOT","TSCM","TRAC","TZOO","TCX","UNTD","UDW","WWWW","WBMD","YHOO","GSB","ARBA","ARTG","ADAT","BVSN","CTXS","DRIV","DSCM","FFIV","HPOL","IBAS","IIG","INOD","INAP","ICGE","IPAS","JCOM","KEYN","LIOX","LQDT","EGOV","ORCC","ONVI","OTEX","OPWV","RAX","RNWK","SONE","SLTC","SNWL","SPRT","SYMC","ULTI","VRSN","VIGN","VHC","VOCS","WBSN","ZIXI","GNCMA","SAAS","ATVI","BBBB","CNVR","DIVX","ERTS","GLUU","MMUS","RLRN","THQI","TDSC","BBOX","DGII","DIGA","ELON","EXTR","JNPR","NWK","OCNW","RVBD","STAR","SMCI","VPF","AAPL","BHE","DDIC","FLEX","MFLX","PKE","PTIX","PLXS","SANM","SMTX","TTMI","BDR","EDCI","GCOM","ITI","LVWR","PLT","PLCM","SEAC","SNR","SYNX","TKLC","VII","ATRM","AFFX","AMOT","AERG","STST","AXYS","BMI","BEC","BIO","BRKR","CALP","CPHD","CLRT","CGNX","COHR","COMV","CUB","CYBE","DAIO","DGLY","DNEX","ELSE","ESE","FARO","FEIC","FLIR","GRMN","GIGA","GSIG","HBIO","ICXT","IIVI","IMA","IO","IRIS","ITRI","KTII","KEI","LLL","LB","LCRY","MEAD","MEAS","MKTY","MTD","MIL","MOCO","MTSC","NANO","NEWP","OICO","OYOG","PRCP","PSDV","RAE","RSTI","RTEC","SIRF","STKR","STRN","SYPR","TIK","LGL","TMO","TLX","TRNS","TRMB","VARI","WAT","WEX","WTT","ZIGO","ACTI","ASIA","DBTK","ENTU","MFE","NENG","NOVL","FIRE","VDSI","AMD","ATML","CAVM","CY","ENTR","FORM","GSIT","IDTI","INTC","ISIL","MXIM","MLNX","ONNN","OPXT","SIMG","TXN","WEDC","ZILG","API","AMKR","ANAD","AMCC","ATHR","AVNX","AXTI","BRCM","CAMD","CNXT","DIOD","EMKR","EXAR","FCS","HITT","ITLN","IRF","INTT","IPGP","IRSN","LDIS","WFR","MCRL","MSCC","TUNE","MOSY","OPLK","PSEM","PXLW","PLXT","PMCS","POWI","QLGC","QUIK","RFMD","SATC","SWKS","SMSC","SUPX","TO","TXCC","TQNT","VLTR","AATI","AFOP","ALTR","ASTI","AUTH","BKHM","CEVA","CRUS","ENER","ENTN","FSLR","IXYS","LSCC","LLTC","LOGC","LSI","MEMS","MCHP","MPWR","NVEC","RBCN","TRID","UCTT","VIRL","XLNX","ZRAN","ASYS","ASYT","ATMI","ACLS","BRKS","BTUI","CCMP","COHU","CREE","CYMI","ENTG","XXIA","KLAC","KLIC","LRCX","LPTH","MTSN","MSPD","MKSI","NVLS","OSIS","SMTL","SRSL","TWLL","TGAL","TER","TSRA","TRT","UTEK","VSEA","VECO","ISSI","LGVN","MIPS","NLST","NETL","RMBS","RMTR","SNDK","SSTI","SPSN","ACIW","ANSS","BSQR","CDNS","CHRD","CNQR","EVOL","ISNS","INS","MANH","MCRS","MSCS","NATI","OPTV","PMTC","PDFS","PNS","TYL","VMW","ALSK","T","CTL","CBB","CNSL","DECC","EQIX","FRP","FTR","HTCO","IWA","Q","SHEN","SURW","TWTC","VZ","WWVY","WIN","ATNI","MBND","TMRK","AIRV","ANEN","ARCW","AFT","CLWR","FTGX","FTWR","FSN","GSAT","IDCC","IPCS","KNOL","LEAP","PCS","TNDM","NIHD","NTLS","PCTI","PRXM","PRPL","RCNI","SBAC","S","WRLS","USM","USMO","UTSI","VM"};
		

		try{
			br1 = new BufferedReader(new FileReader(filePath));
			cr1 = new CsvListReader(br1,CsvPreference.STANDARD_PREFERENCE);
			br2 = new BufferedReader(new FileReader(datePath));
			cr2 = new CsvListReader(br2,CsvPreference.STANDARD_PREFERENCE);
			
			List<String>list1 = cr1.read();
			List<String>list2 = cr2.read();

			while(list1!=null&&list2!=null){
				Boolean isAt=false;
				int at = 8;
				Date d=null;
				if(list1==null||list2==null)break;
				String symbol = list1.get(1);
				for(int i=0;i<arr.length;i++){
					if(symbol.equals(arr[i]))isAt=true;
				}
				if(!isAt){
					list1= cr1.read();
					list2= cr2.read();
					continue;
				}
				else{
				for(int i=0;i<8;i++){
					String q1 = list2.get(0+i);			
					if(!q1.equals("/  /")){
					SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
					d = LTIDate.clearHMSM(sdf.parse(q1));
					}
					else d = null;
					if(LTIDate.getYear(d)==2008&&LTIDate.getQuarter(d)==1){
						at=i;
						break;
					}
				}		

				if(d==null||at==8)continue;
				
				else{		
					IncomeStatement is = this.getIncomeStatement(symbol, LTIDate.getYear(d), LTIDate.getQuarter(d));
					if(is!=null){
					String incTax = list1.get(101+at);
					String netInc = list1.get(141+at);
					is.setIncomeTax(Double.parseDouble(incTax)*1000000);
					is.setNetInc(Double.parseDouble(netInc)*1000000);	
					System.out.println(symbol+incTax+";"+netInc);
					this.updateIncomeStatement(is);
				}
			}
				}
				list1= cr1.read();
				list2= cr2.read();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	/**************************************************************************/
    /*     download annual financial report from yahoo by the given year      */
	/**************************************************************************/

	@Override
	public void downloadYearlyBalanceStatement(String symbol,int year) {
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		Date logDate = new Date();
		Date YEnd = LTIDate.getDate(year, 12, 31);
		YearlyBalanceStatement ybs = fsm.getYearlyBalanceStatement(symbol,year);
		if(ybs==null){
		String html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/bs?s="+symbol+"&annual");
		String content = trim(com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1));
		if(this.MatchYear(year, content)){
			try{
			HtmlToCSV(content,symbol,YEnd,"annualbalancestatement",4,false);
			String[] bsData = ReadCSV(symbol,YEnd,"annualbalancestatement");
		
			Double cash = Double.parseDouble(bsData[3])*1000;
			Double shortTermInv = Double.parseDouble(bsData[5])*1000;
			Double acctRec = Double.parseDouble(bsData[7])*1000;
			Double inventory = Double.parseDouble(bsData[9])*1000;
			Double otherCurAsset = Double.parseDouble(bsData[11])*1000;
			Double longTermInv = Double.parseDouble(bsData[15])*1000;
			Double ppe = Double.parseDouble(bsData[17])*1000;
			Double intangibles = (Double.parseDouble(bsData[19])+Double.parseDouble(bsData[21]))*1000;
			Double otherLTAsset = (Double.parseDouble(bsData[23])+Double.parseDouble(bsData[25])+Double.parseDouble(bsData[27]))*1000;
			Double acctPayable = Double.parseDouble(bsData[31])*1000;
			Double shortTermDebt = Double.parseDouble(bsData[33])*1000;
			Double otherCurLiab = Double.parseDouble(bsData[35])*1000;
			Double longTermDebt = Double.parseDouble(bsData[39])*1000;
			Double otherLTLiab = (Double.parseDouble(bsData[41])+Double.parseDouble(bsData[43])+Double.parseDouble(bsData[45])+Double.parseDouble(bsData[47]))*1000;
			Double preferredStock = Double.parseDouble(bsData[55])*1000;
			Double totalEquity = Double.parseDouble(bsData[57])*1000;
			Double curAssets = Double.parseDouble(bsData[13])*1000;
			Double assets = Double.parseDouble(bsData[29])*1000;
			Double curLiab = Double.parseDouble(bsData[37])*1000;
			Double liab = Double.parseDouble(bsData[49])*1000;
			
			//set value;
			YearlyBalanceStatement ybs1 = new YearlyBalanceStatement();
			ybs1.setSymbol(symbol);
			ybs1.setEndDate(this.DateFormat(bsData[1]));
			/*
			if(LTIDate.getQuarter(this.DateFormat(bsData[1]))<4)ybs1.setYear(year-1);
			else ybs1.setYear(year);
			*/
			ybs1.setYear(year);
			ybs1.setCash(cash);
			ybs1.setShortTermInv(shortTermInv);
			ybs1.setAcctRec(acctRec);
			ybs1.setInventory(inventory);
			ybs1.setOtherCurAssets(otherCurAsset);
			ybs1.setLongTermInv(longTermInv);
			ybs1.setPPE(ppe);
			ybs1.setIntangibles(intangibles);
			ybs1.setOtherLTAssets(otherLTAsset);
			ybs1.setAcctPayable(acctPayable);
			ybs1.setShortTermDebt(shortTermDebt);
			ybs1.setOtherCurLiab(otherCurLiab);
			ybs1.setLongTermDebt(longTermDebt);
			ybs1.setOtherLTLiab(otherLTLiab);
			ybs1.setPreferredStock(preferredStock);
			ybs1.setTotalEquity(totalEquity);
			ybs1.setCurAssets(curAssets);
			ybs1.setAssets(assets);
			ybs1.setCurLiab(curLiab);
			ybs1.setLiab(liab);

			this.saveYearlyBalanceStatement(ybs1);
			Configuration.writeFinancialLog(symbol, logDate, year,4,"YearlyBalanceSheet done.");
		}catch(Exception e){
			
		}
		}
		else return;
		}else return;
	}

	@Override
	public void downloadYearlyCashFlow(String symbol,int year) {
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		Date logDate = new Date();
		Date YEnd = LTIDate.getDate(year, 12, 31);
		YearlyCashFlow ycf = fsm.getYearlyCashFlow(symbol,year);
		if(ycf==null){
		String html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/cf?s="+symbol+"&annual");
		String content = trim(com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1));
		if(this.MatchYear(year, content)){
			try{
			HtmlToCSV(content,symbol,YEnd,"annualcashflow",4,false);
			String[] cfData = ReadCSV(symbol,YEnd,"annualcashflow");

			Double capExp = Double.parseDouble(cfData[19])*1000;
			Double FinCF = Double.parseDouble(cfData[35])*1000;
			Double InvCF = Double.parseDouble(cfData[25])*1000;
			Double OperCF = Double.parseDouble(cfData[17])*1000;
			
			YearlyCashFlow ycf1 = new YearlyCashFlow();
			ycf1.setSymbol(symbol);
			ycf1.setEndDate(this.DateFormat(cfData[1]));
			/*
			if(LTIDate.getQuarter(this.DateFormat(cfData[1]))<4)ycf1.setYear(year-1);
			else ycf1.setYear(year);
			*/
			ycf1.setYear(year);
		    ycf1.setCapExp(capExp);
		    ycf1.setFinCF(FinCF);
		    ycf1.setInvCF(InvCF);
		    ycf1.setOperCF(OperCF);
		    
		    this.saveYearlyCashFlow(ycf1);
		    Configuration.writeFinancialLog(symbol, logDate, year, 4,"YearlyCashFlow done.");
		}catch(Exception e){
		}		
		}else return;
		}else return;
	}

	@Override
	public void downloadYearlyIncomeStatement(String symbol,int year) {
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();	
		Date logDate = new Date();
		Date YEnd = LTIDate.getDate(year, 12, 31);
		YearlyIncomeStatement yis = fsm.getYearlyIncomeStatement(symbol,year); 
	    if(yis==null){
		String html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/is?s="+symbol+"&annual");
		String content = trim(com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1));
		if(this.MatchYear(year, content)){
			try{
			HtmlToCSV(content,symbol,YEnd,"annualincomestatement",4,false);
			String[] isData = ReadCSV(symbol,YEnd,"annualincomestatement");	
			
			Double revenue = Double.parseDouble(isData[3])*1000;
			Double cogs = Double.parseDouble(isData[5])*1000;
			Double rnd = Double.parseDouble(isData[9])*1000;
			Double sgna = Double.parseDouble(isData[11])*1000;
			Double otherOperExp = Double.parseDouble(isData[13])*1000+Double.parseDouble(isData[15])*1000;
			Double intExp = Double.parseDouble(isData[25])*1000;
			Double incomeTax = Double.parseDouble(isData[29])*1000+Double.parseDouble(isData[31])*1000;
			Double shares = this.getShares(symbol,year,4)[0];
			Double floats = this.getShares(symbol,year,4)[1];
			Double institutionHolder = this.getInstitutionHolder(symbol, year,4);
			Double grossProfit = Double.parseDouble(isData[7])*1000;
			Double totalOperExp = rnd+sgna+otherOperExp;
			Double operInc = Double.parseDouble(isData[19])*1000;
			Double preTaxInc = Double.parseDouble(isData[27])*1000;
			Double afterTaxInc = preTaxInc-incomeTax;
			Double netInc = Double.parseDouble(isData[43])*1000;
			
			//set value
			YearlyIncomeStatement yis1 = new YearlyIncomeStatement();
			yis1.setSymbol(symbol);
			yis1.setEndDate(this.DateFormat(isData[1]));
			/*
			if(LTIDate.getQuarter(this.DateFormat(isData[1]))<4)yis1.setYear(year-1);
			else yis1.setYear(year);
			*/
			yis1.setYear(year);
			yis1.setRevenue(revenue);
			yis1.setCOGS(cogs);
			yis1.setRD(rnd);
			yis1.setIntExp(intExp);
			yis1.setIncomeTax(incomeTax);
			yis1.setShares(shares);
			yis1.setFloats(floats);
			yis1.setInstitutionHolder(institutionHolder);
			yis1.setGrossProfit(grossProfit);
			yis1.setTotalOperExp(totalOperExp);
			yis1.setOperInc(operInc);
			yis1.setPreTaxInc(preTaxInc);
			yis1.setAfterTaxInc(afterTaxInc);
			yis1.setNetInc(netInc);
			
			this.saveYearlyIncomeStatement(yis1);
			Configuration.writeFinancialLog(symbol, logDate, year, 4,"YearlyIncomeStatement done.");
		}catch(Exception e){			
		}
		}
			else return;
	}else return;
	}
	
	/******************************************************************************************/
	/***                  load the latest 4 quaters financial reports from CSV              ***/
	/******************************************************************************************/
	/**
	 * @author SuPing
	 * add on 2009/10/24
	 */
	@Override
	public void loadBalanceStatement(String symbol){
		Date logDate = new Date();
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		List<String> filePath = getRexPath(symbol,"balancestatement");
		for(int i=0;i<filePath.size();i++){
			String[] tmp = filePath.get(i).split("-");
			System.out.println("do:"+filePath.get(i));
			String[] bsData = ReadCSV(symbol,filePath.get(i),"balancestatement"); 
			for(int j=0;j<bsData.length;j++)
				if(bsData[j].equals("-"))bsData[j]="0";
			BalanceStatement bs = fsm.getBalanceStatement(symbol,Integer.parseInt(tmp[0].substring(16)),Integer.parseInt(tmp[1]));
			if(bs==null){
				try{	
					Date pDate = this.DateFormat(bsData[1]);
					Double cashNEquiv = Double.parseDouble(bsData[3])*1000;
					Double shortTermInv = Double.parseDouble(bsData[5])*1000;
					Double netRec = Double.parseDouble(bsData[7])*1000;
					Double inventory = Double.parseDouble(bsData[9])*1000;
					Double otherCurAsset = Double.parseDouble(bsData[11])*1000;
					Double longTermInv = Double.parseDouble(bsData[15])*1000;
					Double ppne = Double.parseDouble(bsData[17])*1000;
					Double intangibles = (Double.parseDouble(bsData[19])+Double.parseDouble(bsData[21]))*1000;
					Double otherLTAsset = (Double.parseDouble(bsData[23])+Double.parseDouble(bsData[25])+Double.parseDouble(bsData[27]))*1000;
					Double acctPayable = Double.parseDouble(bsData[31])*1000;
					Double shortTermDebt = Double.parseDouble(bsData[33])*1000;
					Double otherCurLiab = Double.parseDouble(bsData[35])*1000;
					Double longTermDebt = Double.parseDouble(bsData[39])*1000;
					Double otherLTLiab = (Double.parseDouble(bsData[41])+Double.parseDouble(bsData[43])+Double.parseDouble(bsData[45])+Double.parseDouble(bsData[47]))*1000;
					Double preferredStock = Double.parseDouble(bsData[55])*1000;
					Double commonStock = Double.parseDouble(bsData[57])*1000;
					Double retainedEarning = Double.parseDouble(bsData[59])*1000;
					Double treasuryStock = Double.parseDouble(bsData[61])*1000;
					Double otherEquity = (Double.parseDouble(bsData[51])+Double.parseDouble(bsData[53])+Double.parseDouble(bsData[63])+Double.parseDouble(bsData[65]))*1000;
					Double curAssets = Double.parseDouble(bsData[13])*1000;
					Double assets = Double.parseDouble(bsData[29])*1000;
					Double curLiab = Double.parseDouble(bsData[37])*1000;
					Double liab = Double.parseDouble(bsData[49])*1000;
			
					//set value;
					BalanceStatement bs1 = new BalanceStatement();
					bs1.setSymbol(symbol);
					bs1.setDate(pDate);
					bs1.setYear(Integer.parseInt(tmp[0].substring(16)));
					bs1.setQuarter(Integer.parseInt(tmp[1]));
					bs1.setCashNEquiv(cashNEquiv);
					bs1.setShortTermInv(shortTermInv);
					bs1.setNetRec(netRec);
					bs1.setInventory(inventory);
					bs1.setOtherCurAsset(otherCurAsset);
					bs1.setLongTermInv(longTermInv);
					bs1.setPPNE(ppne);
					bs1.setIntangibles(intangibles);
					bs1.setOtherLongTermAsset(otherLTAsset);
					bs1.setAcctPayable(acctPayable);
					bs1.setShortTermDebt(shortTermDebt);
					bs1.setOtherCurLiab(otherCurLiab);
					bs1.setLongTermDebt(longTermDebt);
					bs1.setOtherLongTermLiab(otherLTLiab);
					bs1.setPreferredStock(preferredStock);
					bs1.setCommonStock(commonStock);
					bs1.setRetainedEarning(retainedEarning);
					bs1.setTreasuryStock(treasuryStock);
					bs1.setOtherEquity(otherEquity);
					bs1.setCurAssets(curAssets);
					bs1.setAssets(assets);
					bs1.setCurLiab(curLiab);
					bs1.setLiab(liab);

					this.saveBalanceStatement(bs1);
					System.out.println(symbol+"BalanceSheet made up.");
					Configuration.writeFinancialLog(symbol,logDate, Integer.parseInt(tmp[0].substring(16)),Integer.parseInt(tmp[1])," BalanceSheet made up.");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * @author SuPing
	 * add on 2009/10/24
	 */
	@Override
	public void loadCashFlow(String symbol){
		Date logDate = new Date();
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		List<String> filePath = getRexPath(symbol,"cashflow");
		for(int i=0;i<filePath.size();i++){
			String[] tmp = filePath.get(i).split("-");
			System.out.println("do:"+filePath.get(i));
			String[] cfData = ReadCSV(symbol,filePath.get(i),"cashflow"); 
			for(int j=0;j<cfData.length;j++)
				if(cfData[j].equals("-"))cfData[j]="0";
			CashFlow cf = fsm.getCashFlow(symbol,Integer.parseInt(tmp[0].substring(16)),Integer.parseInt(tmp[1]));
			if(cf==null){			
				try{
					Double deprAmort = Double.parseDouble(cfData[5])*1000;
					Double adjNetInc = Double.parseDouble(cfData[7])*1000;
					Double otherOperCash = Double.parseDouble(cfData[9])*1000+Double.parseDouble(cfData[11])*1000+Double.parseDouble(cfData[13])*1000+Double.parseDouble(cfData[15])*1000;
					Double capExp = Double.parseDouble(cfData[19])*1000;
					Double investment = Double.parseDouble(cfData[21])*1000;
					Double otherInvCash = Double.parseDouble(cfData[23])*1000;
					Double dividend = Double.parseDouble(cfData[27])*1000;
					Double stockSalePur = Double.parseDouble(cfData[29])*1000;
					Double netBorrow = Double.parseDouble(cfData[31])*1000;
					Double otherFinCash = Double.parseDouble(cfData[33])*1000;
					Double currencyAdj = Double.parseDouble(cfData[37])*1000;
					Double FinCF = Double.parseDouble(cfData[35])*1000;
					Double InvCF = Double.parseDouble(cfData[25])*1000;
					Double OperCF = Double.parseDouble(cfData[17])*1000;
			
					CashFlow cf1 = new CashFlow();
					cf1.setSymbol(symbol);
					cf1.setDate(this.DateFormat(cfData[1]));
					cf1.setYear(Integer.parseInt(tmp[0].substring(16)));
					cf1.setQuarter(Integer.parseInt(tmp[1]));
					cf1.setDeprAmort(deprAmort);
					cf1.setAdjNetInc(adjNetInc);
					cf1.setOtherOperCash(otherOperCash);
					cf1.setCapExp(capExp);
					cf1.setInvestment(investment);
					cf1.setOtherInvCash(otherInvCash);
					cf1.setDividend(dividend);
					cf1.setStockSalePur(stockSalePur);
					cf1.setNetBorrow(netBorrow);
					cf1.setOtherFinCash(otherFinCash);
					cf1.setCurrencyAdj(currencyAdj);
					cf1.setFinCF(FinCF);
					cf1.setInvCF(InvCF);
					cf1.setOperCF(OperCF);
		    
					this.saveCashFlow(cf1);
					System.out.println(symbol+"CashFlow made up.");
					Configuration.writeFinancialLog(symbol,logDate,Integer.parseInt(tmp[0].substring(16)),Integer.parseInt(tmp[1])," CashFlow made up.");
				}catch(Exception e){
					e.printStackTrace();
				}		
			}else continue;
		}
	}
	
	/**
	 * @author SuPing
	 * add on 2009/10/24
	 */
	@Override
	public void loadIncomeStatement(String symbol){
		Date logDate = new Date();
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		List<String> filePath = getRexPath(symbol,"incomestatement");
		for(int i=0;i<filePath.size();i++){
			String[] tmp = filePath.get(i).split("-");
			System.out.println("do:"+filePath.get(i));
			String[] isData = ReadCSV(symbol,filePath.get(i),"incomestatement"); 
			for(int j=0;j<isData.length;j++)
				if(isData[j].equals("-"))isData[j]="0";
			IncomeStatement is = fsm.getIncomeStatement(symbol,Integer.parseInt(tmp[0].substring(16)),Integer.parseInt(tmp[1])); 
			if(is==null){	
				try{
					Double revenue = Double.parseDouble(isData[3])*1000;
					Double cogs = Double.parseDouble(isData[5])*1000;
					Double rnd = Double.parseDouble(isData[9])*1000;
					Double sgna = Double.parseDouble(isData[11])*1000;
					Double otherOperExp = Double.parseDouble(isData[13])*1000+Double.parseDouble(isData[15])*1000;
					Double totalOtherInc = Double.parseDouble(isData[21])*1000;
					Double intExp = Double.parseDouble(isData[25])*1000;
					Double incomeTax = Double.parseDouble(isData[29])*1000+Double.parseDouble(isData[31])*1000;
					Double discOper = Double.parseDouble(isData[35])*1000;
					Double extItem = Double.parseDouble(isData[37])*1000+Double.parseDouble(isData[41])*1000;
					Double acctChange = Double.parseDouble(isData[39])*1000;
					Double shares = this.getShares(symbol,Integer.parseInt(tmp[0].substring(16)),Integer.parseInt(tmp[1]))[0];
					Double floats = this.getShares(symbol,Integer.parseInt(tmp[0].substring(16)),Integer.parseInt(tmp[1]))[1];
					Double institutionHolder = this.getInstitutionHolder(symbol, Integer.parseInt(tmp[0].substring(16)),Integer.parseInt(tmp[1]));
					Double grossProfit = Double.parseDouble(isData[7])*1000;
					Double totalOperExp = rnd+sgna+otherOperExp;
					Double operInc = Double.parseDouble(isData[19])*1000;
					Double preTaxInc = Double.parseDouble(isData[27])*1000;
					Double afterTaxInc = preTaxInc-incomeTax;
					Double netInc = Double.parseDouble(isData[43])*1000;
			
					//set value
					IncomeStatement is1 = new IncomeStatement();
					is1.setSymbol(symbol);
					is1.setDate(this.DateFormat(isData[1]));
					is1.setYear(Integer.parseInt(tmp[0].substring(16)));
					is1.setQuarter(Integer.parseInt(tmp[1]));
					is1.setRevenue(revenue);
					is1.setCOGS(cogs);
					is1.setRND(rnd);
					is1.setSGNA(sgna);
					is1.setOtherOperExp(otherOperExp);
					is1.setTotalOtherInc(totalOtherInc);
					is1.setIntExp(intExp);
					is1.setIncomeTax(incomeTax);
					is1.setDiscOper(discOper);
					is1.setExtItem(extItem);
					is1.setAcctChange(acctChange);
					if(!shares.equals(-999000.0)&&!shares.equals(-998000.0)) is1.setShares(shares);
					if(!floats.equals(-999000.0)&&!shares.equals(-997000.0)) is1.setFloats(floats);
					is1.setInstitutionHolder(institutionHolder);
					is1.setGrossProfit(grossProfit);
					is1.setTotalOperExp(totalOperExp);
					is1.setOperInc(operInc);
					is1.setPreTaxInc(preTaxInc);
					is1.setAfterTaxInc(afterTaxInc);
					is1.setNetInc(netInc);
			
					this.saveIncomeStatement(is1);
					System.out.println(symbol+"IncomeStatement made up.");
					Configuration.writeFinancialLog(symbol, logDate, Integer.parseInt(tmp[0].substring(16)), Integer.parseInt(tmp[1])," IncomeStatement made up.");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
			
	}
	
	/******************************************************************************************/
	/***                  check the  financial reports from the database                    ***/
	/******************************************************************************************/
	/**
	 * @author SuPing
	 * add on 2009/10/26
	 */
	@Override
	public void checkBICStatement(String symbol,String statementtype){
		Date logDate = new Date();
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		String bsql = "SELECT date FROM `ltisystem_balancestatement`where symbol='"+symbol+"';";
		String csql = "SELECT date FROM `ltisystem_cashflow`where symbol='"+symbol+"';";
		String isql = "SELECT date FROM `ltisystem_incomestatement`where symbol='"+symbol+"';";
		try{
		List blist = super.findBySQL(bsql);
		List clist = super.findBySQL(csql);
		List ilist = super.findBySQL(isql);
		HashSet dateSet = new HashSet();
		dateSet.addAll(blist);
		dateSet.addAll(clist);
		dateSet.addAll(ilist);
		List<String> bslog= new ArrayList<String>();
		List<String> cflog= new ArrayList<String>();
		List<String> islog= new ArrayList<String>();
		Date[] date = (Date[]) dateSet.toArray(new Date[dateSet.size()]);
		for(int i=0;i<dateSet.size();i++){
			BalanceStatement bs = fsm.getBalanceStatement(symbol,LTIDate.getYear(date[i]),LTIDate.getQuarter(date[i]));
			CashFlow cf = fsm.getCashFlow(symbol,LTIDate.getYear(date[i]),LTIDate.getQuarter(date[i]));
			IncomeStatement is = fsm.getIncomeStatement(symbol,LTIDate.getYear(date[i]),LTIDate.getQuarter(date[i])); 
			
			if(bs!=null&&cf!=null&&is!=null) continue;
			else if(bs==null&&statementtype.equals("balancestatement"))
				bslog.add(symbol+" "+date[i]+" BalanceStatement lost.");	
			else if(cf==null&&statementtype.equals("cashflow"))
				cflog.add(symbol+" "+date[i]+" CashFlow lost.");
			else if(is==null&&statementtype.equals("incomestatement"))
				islog.add(symbol+" "+date[i]+" IncomeStatement lost.");
			
		}
		System.out.println(symbol+": has checked!");
		if(statementtype.equals("balancestatement"))
			for(int i=0;i<bslog.size();i++)
				Configuration.writeFinancialChkLog(bslog.get(i), logDate);
		if(statementtype.equals("cashflow"))
			for(int i=0;i<cflog.size();i++)
				Configuration.writeFinancialChkLog(cflog.get(i), logDate);
		if(statementtype.equals("incomestatement"))
			for(int i=0;i<islog.size();i++)
				Configuration.writeFinancialChkLog(islog.get(i), logDate);
		}catch(Exception e){
			System.out.println(StringUtil.getStackTraceString(e));
		}
	}
	
	@Override
	public String chkBICStatement(String symbol,String statementtype){
		Date logDate = new Date();
		boolean bicFlg=false;
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		String bsql = "SELECT date FROM `ltisystem_balancestatement`where symbol='"+symbol+"';";
		String csql = "SELECT date FROM `ltisystem_cashflow`where symbol='"+symbol+"';";
		String isql = "SELECT date FROM `ltisystem_incomestatement`where symbol='"+symbol+"';";
		try{
		List blist = super.findBySQL(bsql);
		List clist = super.findBySQL(csql);
		List ilist = super.findBySQL(isql);
		HashSet dateSet = new HashSet();
		dateSet.addAll(blist);
		dateSet.addAll(clist);
		dateSet.addAll(ilist);
		List<String> bslog= new ArrayList<String>();
		List<String> cflog= new ArrayList<String>();
		List<String> islog= new ArrayList<String>();
		Date[] date = (Date[]) dateSet.toArray(new Date[dateSet.size()]);
		for(int i=0;i<dateSet.size();i++){
			BalanceStatement bs = fsm.getBalanceStatement(symbol,LTIDate.getYear(date[i]),LTIDate.getQuarter(date[i]));
			CashFlow cf = fsm.getCashFlow(symbol,LTIDate.getYear(date[i]),LTIDate.getQuarter(date[i]));
			IncomeStatement is = fsm.getIncomeStatement(symbol,LTIDate.getYear(date[i]),LTIDate.getQuarter(date[i])); 
			
			if(bs!=null&&cf!=null&&is!=null) continue;
			else if(bs==null&&statementtype.equals("balancestatement"))
				bslog.add(symbol+" "+date[i]+" BalanceStatement lost.");	
			else if(cf==null&&statementtype.equals("cashflow"))
				cflog.add(symbol+" "+date[i]+" CashFlow lost.");
			else if(is==null&&statementtype.equals("incomestatement"))
				islog.add(symbol+" "+date[i]+" IncomeStatement lost.");
			if(bs==null||cf==null||is==null) bicFlg=true;
		}
		if(statementtype.equals("balancestatement"))
			for(int i=0;i<bslog.size();i++)
				Configuration.writeFinancialChkLog(bslog.get(i), logDate);
		if(statementtype.equals("cashflow"))
			for(int i=0;i<cflog.size();i++)
				Configuration.writeFinancialChkLog(cflog.get(i), logDate);
		if(statementtype.equals("incomestatement"))
			for(int i=0;i<islog.size();i++)
				Configuration.writeFinancialChkLog(islog.get(i), logDate);
		}catch(Exception e){
			System.out.println(StringUtil.getStackTraceString(e));
		}
		if(bicFlg) return symbol;
		else return null;
	}
	
	/**
	 * @author SuPing
	 * add on 2009/10/26
	 */
	@Override
	public void checkBICStatement(SecurityManager securityManager,List<String> list){
		String[] type={"balancestatement","cashflow","incomestatement"};
		for(int i=0;i<type.length;i++)
			for(int j=0;j<list.size();j++){
				this.checkBICStatement(list.get(j),type[i]);
			}
	}
	
	@Override
	public Set<String> chkBICStatement(SecurityManager securityManager,List<String> list){
		String symbol=null;
		Set<String> symbolSet = new HashSet<String>();
		String[] type={"balancestatement","cashflow","incomestatement"};
		for(int i=0;i<type.length;i++)
			for(int j=0;j<list.size();j++){
				symbol=this.chkBICStatement(list.get(j),type[i]);
				if(symbol!=null){
					symbolSet.add(symbol);
					System.out.println(symbol+": has checked!");	
				}
				else continue;
			}
		return symbolSet;
	}
	
	/**
	 * @author SuPing
	 * 2009-01-04
	 */
	@Override
	public String chkYBICStatement(String symbol,String statementtype){
		Date logDate = new Date();
		boolean bicFlg=false;
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		String bsql = "SELECT enddate FROM `ltisystem_balancestatement_yearly`where symbol='"+symbol+"';";
		String csql = "SELECT enddate FROM `ltisystem_cashflow_yearly`where symbol='"+symbol+"';";
		String isql = "SELECT enddate FROM `ltisystem_incomestatement_yearly`where symbol='"+symbol+"';";
		try{
		List blist = super.findBySQL(bsql);
		List clist = super.findBySQL(csql);
		List ilist = super.findBySQL(isql);
		HashSet dateSet = new HashSet();
		dateSet.addAll(blist);
		dateSet.addAll(clist);
		dateSet.addAll(ilist);
		List<String> bslog= new ArrayList<String>();
		List<String> cflog= new ArrayList<String>();
		List<String> islog= new ArrayList<String>();
		Date[] date = (Date[]) dateSet.toArray(new Date[dateSet.size()]);
		for(int i=0;i<dateSet.size();i++){
			YearlyBalanceStatement bs = fsm.getYearlyBalanceStatement(symbol,LTIDate.getYear(date[i]));
			YearlyCashFlow cf = fsm.getYearlyCashFlow(symbol,LTIDate.getYear(date[i]));
			YearlyIncomeStatement is = fsm.getYearlyIncomeStatement(symbol,LTIDate.getYear(date[i])); 
			
			if(bs!=null&&cf!=null&&is!=null) continue;
			else if(bs==null&&statementtype.equals("balancestatement_yearly"))
				bslog.add(symbol+" "+date[i]+" BalanceStatement Yearly lost.");	
			else if(cf==null&&statementtype.equals("cashflow_yearly"))
				cflog.add(symbol+" "+date[i]+" CashFlow Yearly lost.");
			else if(is==null&&statementtype.equals("incomestatement_yearly"))
				islog.add(symbol+" "+date[i]+" IncomeStatement Yearly lost.");
			if(bs==null||cf==null||is==null) bicFlg=true;
		}
		if(statementtype.equals("balancestatement_yearly"))
			for(int i=0;i<bslog.size();i++)
				Configuration.writeFinancialChkLog(bslog.get(i), logDate);
		if(statementtype.equals("cashflow_yearly"))
			for(int i=0;i<cflog.size();i++)
				Configuration.writeFinancialChkLog(cflog.get(i), logDate);
		if(statementtype.equals("incomestatement_yearly"))
			for(int i=0;i<islog.size();i++)
				Configuration.writeFinancialChkLog(islog.get(i), logDate);
		}catch(Exception e){
			System.out.println(StringUtil.getStackTraceString(e));
		}
		if(bicFlg) return symbol;
		else return null;
	}
	/******************************************************************************************/
	/***                  download the latest financial reports from Yahoo                   ***/
	/******************************************************************************************/
	/**
	 * modify by SuPing
	 * 2009/10/20
	 */
	@Override
	public void downloadBalanceStatement(String symbol) {
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		Date logDate = new Date();
		
		String html="html";
		int num=0;
		try{
			while(html.equals("html")&&num<=30){
					html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/bs?s="+symbol);
					num++;
					if(html==null) html="html";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		String content = trim(com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1));
		Date date[] = HtmlToCSV(content,symbol,logDate,"balancestatement",5);
		if(date!=null){
			for(int i=0;i<4;i++){
				try{
					if(date[i]==null)continue;
				}catch(Exception e){e.printStackTrace();}
				String[] bsData = ReadCSV(symbol,date[i],"balancestatement"); 
				for(int j=0;j<bsData.length;j++)
					if(bsData[j].equals("-"))bsData[j]="0";
				BalanceStatement bs = fsm.getBalanceStatement(symbol,LTIDate.getYear(date[i]),LTIDate.getQuarter(date[i]));
				if(bs==null){
					try{	
						Date pDate = this.DateFormat(bsData[1]);
						Double cashNEquiv = Double.parseDouble(bsData[3])*1000;
						Double shortTermInv = Double.parseDouble(bsData[5])*1000;
						Double netRec = Double.parseDouble(bsData[7])*1000;
						Double inventory = Double.parseDouble(bsData[9])*1000;
						Double otherCurAsset = Double.parseDouble(bsData[11])*1000;
						Double longTermInv = Double.parseDouble(bsData[15])*1000;
						Double ppne = Double.parseDouble(bsData[17])*1000;
						Double intangibles = (Double.parseDouble(bsData[19])+Double.parseDouble(bsData[21]))*1000;
						Double otherLTAsset = (Double.parseDouble(bsData[23])+Double.parseDouble(bsData[25])+Double.parseDouble(bsData[27]))*1000;
						Double acctPayable = Double.parseDouble(bsData[31])*1000;
						Double shortTermDebt = Double.parseDouble(bsData[33])*1000;
						Double otherCurLiab = Double.parseDouble(bsData[35])*1000;
						Double longTermDebt = Double.parseDouble(bsData[39])*1000;
						Double otherLTLiab = (Double.parseDouble(bsData[41])+Double.parseDouble(bsData[43])+Double.parseDouble(bsData[45])+Double.parseDouble(bsData[47]))*1000;
						Double preferredStock = Double.parseDouble(bsData[55])*1000;
						Double commonStock = Double.parseDouble(bsData[57])*1000;
						Double retainedEarning = Double.parseDouble(bsData[59])*1000;
						Double treasuryStock = Double.parseDouble(bsData[61])*1000;
						Double otherEquity = (Double.parseDouble(bsData[51])+Double.parseDouble(bsData[53])+Double.parseDouble(bsData[63])+Double.parseDouble(bsData[65]))*1000;
						Double curAssets = Double.parseDouble(bsData[13])*1000;
						Double assets = Double.parseDouble(bsData[29])*1000;
						Double curLiab = Double.parseDouble(bsData[37])*1000;
						Double liab = Double.parseDouble(bsData[49])*1000;
			
						//set value;
						BalanceStatement bs1 = new BalanceStatement();
						bs1.setSymbol(symbol);
						bs1.setDate(pDate);
						bs1.setYear(date[i].getYear()+1900);
						bs1.setQuarter(LTIDate.getQuarter(date[i]));
						bs1.setCashNEquiv(cashNEquiv);
						bs1.setShortTermInv(shortTermInv);
						bs1.setNetRec(netRec);
						bs1.setInventory(inventory);
						bs1.setOtherCurAsset(otherCurAsset);
						bs1.setLongTermInv(longTermInv);
						bs1.setPPNE(ppne);
						bs1.setIntangibles(intangibles);
						bs1.setOtherLongTermAsset(otherLTAsset);
						bs1.setAcctPayable(acctPayable);
						bs1.setShortTermDebt(shortTermDebt);
						bs1.setOtherCurLiab(otherCurLiab);
						bs1.setLongTermDebt(longTermDebt);
						bs1.setOtherLongTermLiab(otherLTLiab);
						bs1.setPreferredStock(preferredStock);
						bs1.setCommonStock(commonStock);
						bs1.setRetainedEarning(retainedEarning);
						bs1.setTreasuryStock(treasuryStock);
						bs1.setOtherEquity(otherEquity);
						bs1.setCurAssets(curAssets);
						bs1.setAssets(assets);
						bs1.setCurLiab(curLiab);
						bs1.setLiab(liab);

						this.saveBalanceStatement(bs1);
						Configuration.writeFinancialLog(symbol,logDate, date[i].getYear()+1900, LTIDate.getQuarter(date[i])," BalanceSheet update on");
					}catch(Exception e){
					e.printStackTrace();
					}
				}else continue;
			}
		}
	}
	
	/**
	 * modify by SuPing
	 * 2009/10/20
	 */
	@Override
	public void downloadCashFlow(String symbol) {
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		Date logDate = new Date();
		
		String html="html";
		int num=0;
		try{
			while(html.equals("html")&&num<=30){
					html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/cf?s="+symbol);
					num++;
					if(html==null) html="html";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		String content = trim(com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1));
		Date[] date = HtmlToCSV(content,symbol,logDate,"cashflow",5);
		if(date!=null){
			for(int i=0;i<4;i++){
				try{
					if(date[i]==null)continue;
				}catch(Exception e){e.printStackTrace();}
				String[] cfData = ReadCSV(symbol,date[i],"cashflow");
				for(int j=0;j<cfData.length;j++)
					if(cfData[j].equals("-"))cfData[j]="0";
				CashFlow cf = fsm.getCashFlow(symbol,LTIDate.getYear(date[i]),LTIDate.getQuarter(date[i]));
				if(cf==null){			
					try{
						Double deprAmort = Double.parseDouble(cfData[5])*1000;
						Double adjNetInc = Double.parseDouble(cfData[7])*1000;
						Double otherOperCash = Double.parseDouble(cfData[9])*1000+Double.parseDouble(cfData[11])*1000+Double.parseDouble(cfData[13])*1000+Double.parseDouble(cfData[15])*1000;
						Double capExp = Double.parseDouble(cfData[19])*1000;
						Double investment = Double.parseDouble(cfData[21])*1000;
						Double otherInvCash = Double.parseDouble(cfData[23])*1000;
						Double dividend = Double.parseDouble(cfData[27])*1000;
						Double stockSalePur = Double.parseDouble(cfData[29])*1000;
						Double netBorrow = Double.parseDouble(cfData[31])*1000;
						Double otherFinCash = Double.parseDouble(cfData[33])*1000;
						Double currencyAdj = Double.parseDouble(cfData[37])*1000;
						Double FinCF = Double.parseDouble(cfData[35])*1000;
						Double InvCF = Double.parseDouble(cfData[25])*1000;
						Double OperCF = Double.parseDouble(cfData[17])*1000;
			
						CashFlow cf1 = new CashFlow();
						cf1.setSymbol(symbol);
						cf1.setDate(this.DateFormat(cfData[1]));
						cf1.setYear(date[i].getYear()+1900);
						cf1.setQuarter(LTIDate.getQuarter(date[i]));
						cf1.setDeprAmort(deprAmort);
						cf1.setAdjNetInc(adjNetInc);
						cf1.setOtherOperCash(otherOperCash);
						cf1.setCapExp(capExp);
						cf1.setInvestment(investment);
						cf1.setOtherInvCash(otherInvCash);
						cf1.setDividend(dividend);
						cf1.setStockSalePur(stockSalePur);
						cf1.setNetBorrow(netBorrow);
						cf1.setOtherFinCash(otherFinCash);
						cf1.setCurrencyAdj(currencyAdj);
						cf1.setFinCF(FinCF);
						cf1.setInvCF(InvCF);
						cf1.setOperCF(OperCF);
		    
						this.saveCashFlow(cf1);
						Configuration.writeFinancialLog(symbol, logDate,LTIDate.getYear(date[i]), LTIDate.getQuarter(date[i])," CashFlowSheet update on");
					}catch(Exception e){
						e.printStackTrace();
					}		
				}else continue;
			}
		}
	}
	
	/**
	 * modify by SuPing
	 * 2009/10/20
	 */
	@Override
	public void downloadIncomeStatement(String symbol) {
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		Date logDate = new Date();

		String html="html";
		int num=0;
		try{
			while(html.equals("html")&&num<=30){
					html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/is?s="+symbol);
					num++;
					if(html==null) html="html";
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		String content = trim(com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1));
		Date[] date = HtmlToCSV(content,symbol,logDate,"incomestatement",5);
		if(date!=null){
			for(int i=0;i<4;i++){
				try{
					if(date[i]==null)continue;
				}catch(Exception e){}
				String[] isData = ReadCSV(symbol,date[i],"incomestatement");
				for(int j=0;j<isData.length;j++)
					if(isData[j].equals("-"))isData[j]="0";
				IncomeStatement is = fsm.getIncomeStatement(symbol,LTIDate.getYear(date[i]),LTIDate.getQuarter(date[i])); 
				if(is==null){	
					try{
						Double revenue = Double.parseDouble(isData[3])*1000;
						Double cogs = Double.parseDouble(isData[5])*1000;
						Double rnd = Double.parseDouble(isData[9])*1000;
						Double sgna = Double.parseDouble(isData[11])*1000;
						Double otherOperExp = Double.parseDouble(isData[13])*1000+Double.parseDouble(isData[15])*1000;
						Double totalOtherInc = Double.parseDouble(isData[21])*1000;
						Double intExp = Double.parseDouble(isData[25])*1000;
						Double incomeTax = Double.parseDouble(isData[29])*1000+Double.parseDouble(isData[31])*1000;
						Double discOper = Double.parseDouble(isData[35])*1000;
						Double extItem = Double.parseDouble(isData[37])*1000+Double.parseDouble(isData[41])*1000;
						Double acctChange = Double.parseDouble(isData[39])*1000;
						Double shares = this.getShares(symbol,LTIDate.getYear(date[i]),LTIDate.getQuarter(date[i]))[0];
						Double floats = this.getShares(symbol,LTIDate.getYear(date[i]),LTIDate.getQuarter(date[i]))[1];
						Double institutionHolder = this.getInstitutionHolder(symbol, LTIDate.getYear(date[i]),LTIDate.getQuarter(date[i]));
						Double grossProfit = Double.parseDouble(isData[7])*1000;
						Double totalOperExp = rnd+sgna+otherOperExp;
						Double operInc = Double.parseDouble(isData[19])*1000;
						Double preTaxInc = Double.parseDouble(isData[27])*1000;
						Double afterTaxInc = preTaxInc-incomeTax;
						Double netInc = Double.parseDouble(isData[43])*1000;
			
						//set value
						IncomeStatement is1 = new IncomeStatement();
						is1.setSymbol(symbol);
						is1.setDate(this.DateFormat(isData[1]));
						is1.setYear(date[i].getYear()+1900);
						is1.setQuarter(LTIDate.getQuarter(date[i]));
						is1.setRevenue(revenue);
						is1.setCOGS(cogs);
						is1.setRND(rnd);
						is1.setSGNA(sgna);
						is1.setOtherOperExp(otherOperExp);
						is1.setTotalOtherInc(totalOtherInc);
						is1.setIntExp(intExp);
						is1.setIncomeTax(incomeTax);
						is1.setDiscOper(discOper);
						is1.setExtItem(extItem);
						is1.setAcctChange(acctChange);
						if(!shares.equals(-999000.0)&&!shares.equals(-998000.0)) is1.setShares(shares);
						if(!floats.equals(-999000.0)&&!floats.equals(-997000.0)) is1.setFloats(floats);
						if(!institutionHolder.equals(-9999.0))is1.setInstitutionHolder(institutionHolder);
						is1.setGrossProfit(grossProfit);
						is1.setTotalOperExp(totalOperExp);
						is1.setOperInc(operInc);
						is1.setPreTaxInc(preTaxInc);
						is1.setAfterTaxInc(afterTaxInc);
						is1.setNetInc(netInc);
			
						this.saveIncomeStatement(is1);
						Configuration.writeFinancialLog(symbol, logDate, date[i].getYear()+1900, LTIDate.getQuarter(date[i])," IncomeStatementSheet update on");
					}catch(Exception e){
						e.printStackTrace();
					}
				}else continue;
			}
		}
	}

	@Override
	public void downloadYearlyBalanceStatement(String symbol) {
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		Date logDate = new Date();
		
		String html="html";
		int i=1,num=0;
		try{
			while(html.equals("html")&&num<=30){
				html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/bs?s="+symbol+"&annual");
				num++;
				if(html==null) {html="html";i++;}
				if(i%10==0) Thread.sleep(10*1000);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		String content = trim(com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1));
		Date date = HtmlToCSV(content,symbol,logDate,"annualbalancestatement",4,true);
		if(date==null)return;
		String[] bsData = ReadCSV(symbol,date,"annualbalancestatement");
		for(int j=0;j<bsData.length;j++)
			if(bsData[j].equals("-"))bsData[j]="0";
		YearlyBalanceStatement ybs = fsm.getYearlyBalanceStatement(symbol,LTIDate.getYear(this.DateFormat(bsData[1])));	
		if(ybs==null){
			try{			
			Double cash = Double.parseDouble(bsData[3])*1000;
			Double shortTermInv = Double.parseDouble(bsData[5])*1000;
			Double acctRec = Double.parseDouble(bsData[7])*1000;
			Double inventory = Double.parseDouble(bsData[9])*1000;
			Double otherCurAsset = Double.parseDouble(bsData[11])*1000;
			Double longTermInv = Double.parseDouble(bsData[15])*1000;
			Double ppe = Double.parseDouble(bsData[17])*1000;
			Double intangibles = (Double.parseDouble(bsData[19])+Double.parseDouble(bsData[21]))*1000;
			Double otherLTAsset = (Double.parseDouble(bsData[23])+Double.parseDouble(bsData[25])+Double.parseDouble(bsData[27]))*1000;
			Double acctPayable = Double.parseDouble(bsData[31])*1000;
			Double shortTermDebt = Double.parseDouble(bsData[33])*1000;
			Double otherCurLiab = Double.parseDouble(bsData[35])*1000;
			Double longTermDebt = Double.parseDouble(bsData[39])*1000;
			Double otherLTLiab = (Double.parseDouble(bsData[41])+Double.parseDouble(bsData[43])+Double.parseDouble(bsData[45])+Double.parseDouble(bsData[47]))*1000;
			Double preferredStock = Double.parseDouble(bsData[55])*1000;
			Double totalEquity = Double.parseDouble(bsData[57])*1000;
			Double curAssets = Double.parseDouble(bsData[13])*1000;
			Double assets = Double.parseDouble(bsData[29])*1000;
			Double curLiab = Double.parseDouble(bsData[37])*1000;
			Double liab = Double.parseDouble(bsData[49])*1000;
			
			//set value;
			YearlyBalanceStatement ybs1 = new YearlyBalanceStatement();
			ybs1.setSymbol(symbol);
			ybs1.setEndDate(this.DateFormat(bsData[1]));
			/*
			if(LTIDate.getQuarter(this.DateFormat(bsData[1]))<4)ybs1.setYear(LTIDate.getYear(this.DateFormat(bsData[1]))-1);
			else ybs1.setYear(LTIDate.getYear(this.DateFormat(bsData[1])));
			*/
			ybs1.setYear(LTIDate.getYear(this.DateFormat(bsData[1])));
			ybs1.setCash(cash);
			ybs1.setShortTermInv(shortTermInv);
			ybs1.setAcctRec(acctRec);
			ybs1.setInventory(inventory);
			ybs1.setOtherCurAssets(otherCurAsset);
			ybs1.setLongTermInv(longTermInv);
			ybs1.setPPE(ppe);
			ybs1.setIntangibles(intangibles);
			ybs1.setOtherLTAssets(otherLTAsset);
			ybs1.setAcctPayable(acctPayable);
			ybs1.setShortTermDebt(shortTermDebt);
			ybs1.setOtherCurLiab(otherCurLiab);
			ybs1.setLongTermDebt(longTermDebt);
			ybs1.setOtherLTLiab(otherLTLiab);
			ybs1.setPreferredStock(preferredStock);
			ybs1.setTotalEquity(totalEquity);
			ybs1.setCurAssets(curAssets);
			ybs1.setAssets(assets);
			ybs1.setCurLiab(curLiab);
			ybs1.setLiab(liab);

			this.saveYearlyBalanceStatement(ybs1);
			Configuration.writeYFinancialLog(symbol, logDate,LTIDate.getYear(date),"YearlyBalanceSheet done.");
			
		}catch(Exception e){		
		}
		}else return;
	}

	@Override
	public void downloadYearlyCashFlow(String symbol) {
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		Date logDate = new Date();
		
		String html="html";
		int i=1,num=0;
		try{
			while(html.equals("html")&&num<=30){
				html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/cf?s="+symbol+"&annual");
				num++;
				if(html==null) {html="html";i++;}
				if(i%10==0) Thread.sleep(10*1000);
			}
		}catch(Exception e){
				e.printStackTrace();
		}
		String content = trim(com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1));
		Date date = HtmlToCSV(content,symbol,logDate,"annualcashflow",4,true);
		if(date==null)return;
		String[] cfData = ReadCSV(symbol,date,"annualcashflow");
		for(int j=0;j<cfData.length;j++)
			if(cfData[j].equals("-"))cfData[j]="0";
		YearlyCashFlow ycf = fsm.getYearlyCashFlow(symbol,LTIDate.getYear(this.DateFormat(cfData[1])));
		if(ycf==null){
			try{						
			Double capExp = Double.parseDouble(cfData[19])*1000;
			Double FinCF = Double.parseDouble(cfData[35])*1000;
			Double InvCF = Double.parseDouble(cfData[25])*1000;
			Double OperCF = Double.parseDouble(cfData[17])*1000;
			
			YearlyCashFlow ycf1 = new YearlyCashFlow();
			ycf1.setSymbol(symbol);
			ycf1.setEndDate(this.DateFormat(cfData[1]));
			/*
			if(LTIDate.getQuarter(this.DateFormat(cfData[1]))<4)ycf1.setYear(year-1);
			else ycf1.setYear(year);*/
			ycf1.setYear(LTIDate.getYear(this.DateFormat(cfData[1])));
		    ycf1.setCapExp(capExp);
		    ycf1.setFinCF(FinCF);
		    ycf1.setInvCF(InvCF);
		    ycf1.setOperCF(OperCF);
		    
		    this.saveYearlyCashFlow(ycf1);
		    Configuration.writeYFinancialLog(symbol, logDate, LTIDate.getYear(date),"YearlyCashFlow done.");
		}catch(Exception e){
		}		
		}else return;
	}

	@Override
	public void downloadYearlyIncomeStatement(String symbol) {
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();	
		Date logDate = new Date();
	    
		String html="html";
		int i=1,num=0;
		try{
			while(html.equals("html")&&num<=30){
				html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/is?s="+symbol+"&annual");
				num++;
				if(html==null) {html="html";i++;}
				if(i%10==0) Thread.sleep(10*1000);
			}
		}catch(Exception e){
				e.printStackTrace();
		}
		String content = trim(com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1));
		Date date = HtmlToCSV(content,symbol,logDate,"annualincomestatement",4,true);
		if(date==null)return;
		String[] isData = ReadCSV(symbol,date,"annualincomestatement");
		for(int j=0;j<isData.length;j++)
			if(isData[j].equals("-"))isData[j]="0";
		YearlyIncomeStatement yis = fsm.getYearlyIncomeStatement(symbol,LTIDate.getYear(this.DateFormat(isData[1]))); 
	    if(yis==null){
			try{
        	Double revenue = Double.parseDouble(isData[3])*1000;
			Double cogs = Double.parseDouble(isData[5])*1000;
			Double rnd = Double.parseDouble(isData[9])*1000;
			Double sgna = Double.parseDouble(isData[11])*1000;
			Double otherOperExp = Double.parseDouble(isData[13])*1000+Double.parseDouble(isData[15])*1000;
			Double intExp = Double.parseDouble(isData[25])*1000;
			Double incomeTax = Double.parseDouble(isData[29])*1000+Double.parseDouble(isData[31])*1000;
			Double shares = this.getShares(symbol,LTIDate.getYear(this.DateFormat(isData[1])),4)[0];
			Double floats = this.getShares(symbol,LTIDate.getYear(this.DateFormat(isData[1])),4)[1];
			Double institutionHolder = this.getInstitutionHolder(symbol, LTIDate.getYear(this.DateFormat(isData[1])),4);
			Double grossProfit = Double.parseDouble(isData[7])*1000;
			Double totalOperExp = rnd+sgna+otherOperExp;
			Double operInc = Double.parseDouble(isData[19])*1000;
			Double preTaxInc = Double.parseDouble(isData[27])*1000;
			Double afterTaxInc = preTaxInc-incomeTax;
			Double netInc = Double.parseDouble(isData[43])*1000;
			
			//set value
			YearlyIncomeStatement yis1 = new YearlyIncomeStatement();
			yis1.setSymbol(symbol);
			yis1.setEndDate(this.DateFormat(isData[1]));
			/*if(LTIDate.getQuarter(this.DateFormat(isData[1]))<4)yis1.setYear(LTIDate.getYear(this.DateFormat(isData[1]))-1);
			else yis1.setYear(LTIDate.getYear(this.DateFormat(isData[1])));*/
			yis1.setYear(LTIDate.getYear(this.DateFormat(isData[1])));
			yis1.setRevenue(revenue);
			yis1.setCOGS(cogs);
			yis1.setRD(rnd);
			yis1.setIntExp(intExp);
			yis1.setIncomeTax(incomeTax);
			yis1.setShares(shares);
			yis1.setFloats(floats);
			yis1.setInstitutionHolder(institutionHolder);
			yis1.setGrossProfit(grossProfit);
			yis1.setTotalOperExp(totalOperExp);
			yis1.setOperInc(operInc);
			yis1.setPreTaxInc(preTaxInc);
			yis1.setAfterTaxInc(afterTaxInc);
			yis1.setNetInc(netInc);
			
			this.saveYearlyIncomeStatement(yis1);
			Configuration.writeYFinancialLog(symbol, logDate, LTIDate.getYear(date),"YealyIncomeStatement done.");
		}catch(Exception e){			
		}
	}else return;
	}

	/**
	 * modify by SuPing
	 */
	@Override
	public void downloadFinancialStatement(String symbol) {
		Date logDate = new Date();
		LTIDate ltidate = new LTIDate();
		boolean bicFlg=false;
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		String bsql = "SELECT max(date) FROM `ltisystem_balancestatement`where symbol='"+symbol+"';";
		String csql = "SELECT max(date) FROM `ltisystem_cashflow`where symbol='"+symbol+"';";
		String isql = "SELECT max(date) FROM `ltisystem_incomestatement`where symbol='"+symbol+"';";
		try{
			List blist = super.findBySQL(bsql);
			List clist = super.findBySQL(csql);
			List ilist = super.findBySQL(isql);
			Date[] bdate = (Date[]) blist.toArray(new Date[blist.size()]);
			Date[] cdate = (Date[]) clist.toArray(new Date[clist.size()]);
			Date[] idate = (Date[]) ilist.toArray(new Date[ilist.size()]);
			if(bdate[0]!=null&&ltidate.calculateInterval(bdate[0], logDate)>=90){
				this.downloadBalanceStatement(symbol);
			}
			if(cdate[0]!=null&&ltidate.calculateInterval(cdate[0], logDate)>=90){
				this.downloadCashFlow(symbol);
			}
			if(idate[0]!=null&&ltidate.calculateInterval(idate[0], logDate)>=90){
				this.downloadIncomeStatement(symbol);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void loadFinancialStatement(String symbol) {
		this.loadBalanceStatement(symbol);
		this.loadCashFlow(symbol);
		this.loadIncomeStatement(symbol);
	}

	@Override
	public void downloadFinancialStatement(String[] symbols) {
		for(int i=0;i<symbols.length;i++){
			this.downloadFinancialStatement(symbols[i]);
		}
	}

	/**
	 * Modify by SuPing for independent yearly FinancialStatement
	 * 2010-01-04
	 */
	@Override
	public void downloadYearlyFinancialStatement(String symbol) {
//		this.downloadYearlyBalanceStatement(symbol);
//		this.downloadYearlyCashFlow(symbol);
//		this.downloadYearlyIncomeStatement(symbol);
		Date logDate = new Date();
		LTIDate ltidate = new LTIDate();
		boolean bicFlg=false;
		FinancialStatementManager fsm=ContextHolder.getFinancialStatementManager();
		String bsql = "SELECT max(enddate) FROM `ltisystem_balancestatement_yearly`where symbol='"+symbol+"';";
		String csql = "SELECT max(enddate) FROM `ltisystem_cashflow_yearly`where symbol='"+symbol+"';";
		String isql = "SELECT max(enddate) FROM `ltisystem_incomestatement_yearly`where symbol='"+symbol+"';";
		try{
			List blist = super.findBySQL(bsql);
			List clist = super.findBySQL(csql);
			List ilist = super.findBySQL(isql);
			Date[] bdate = (Date[]) blist.toArray(new Date[blist.size()]);
			Date[] cdate = (Date[]) clist.toArray(new Date[clist.size()]);
			Date[] idate = (Date[]) ilist.toArray(new Date[ilist.size()]);
			if(bdate[0]!=null&&ltidate.calculateInterval(bdate[0], logDate)>=30){
				this.downloadYearlyBalanceStatement(symbol);
			}
			if(cdate[0]!=null&&ltidate.calculateInterval(cdate[0], logDate)>=30){
				this.downloadYearlyCashFlow(symbol);
			}
			if(idate[0]!=null&&ltidate.calculateInterval(idate[0], logDate)>=30){
				this.downloadYearlyIncomeStatement(symbol);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public void downloadYearlyFinancialStatement(String[] symbols) {
		for(int i=0;i<symbols.length;i++){
			this.downloadYearlyFinancialStatement(symbols[i]);
		}
	}
	@Override
	public Map<String, Double[]> getLiabToAsset(int year, int quarter) {
		String sql="SELECT symbol,liab/assets as value1,LongTermDebt/(LongTermDebt+PreferredStock+CommonStock) as value2 FROM ltisystem_balancestatement l where year="+year+" and quarter="+quarter;
		Map<String,Double[]> map =new HashMap<String, Double[]>();
		try {
			List list=super.findBySQL(sql);
			
			if(list!=null){
				for(int i=0;i<list.size();i++){
					Object[] objs=(Object[])list.get(i);
					String symbol=(String)objs[0];
					Double v1=(Double)objs[1];
					Double v2=(Double)objs[2];
					if(v1!=null&&symbol!=null&v2!=null){
						Double[] ds=new Double[2];
						ds[0]=v1;
						ds[1]=v2;
						map.put(symbol,ds);
					}
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return map;
	}

	/**** function:get all stocks' sum of net incomes between sDate and eDate***/
	@Override
	public Map<String, Double> getNetIncome(Date sDate, Date eDate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String start = sdf.format(sDate);
		String end = sdf.format(eDate);
		String sql="SELECT symbol,sum(netinc) FROM ltisystem_incomestatement l where Date > '"+start+"' and date <'"+end
		+"' group by symbol";
		Map<String,Double> map =new HashMap<String, Double>();
		try {
			List list=super.findBySQL(sql);
			
			if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
					Object[] objs=(Object[])list.get(i);
					String symbol=(String)objs[0];
					Double v=(Double)objs[1];
					if(v!=null&&symbol!=null)map.put(symbol,v);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return map;
	}

	/***  function:get all the stocks' annual net income values  ***/
	@Override
	public Map<String, Double> getAnnualNetInc(int year) {
		String sql="SELECT symbol,netInc FROM ltisystem_incomestatement_yearly where year="+year;
		Map<String,Double> map =new HashMap<String, Double>();
		try {
			List list=super.findBySQL(sql);
			
			if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
					Object[] objs=(Object[])list.get(i);
					String symbol=(String)objs[0];
					Double v=(Double)objs[1];
					if(v!=null&&symbol!=null)map.put(symbol,v);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return map;
	}

	/***  function:get all the stocks' market capital values  ***/
	@Override
	public Map<String, Double> getMarketCap(int year, int quarter) {
		Date date=LTIDate.getLastDayOfQuarter(LTIDate.getDate(year, quarter*3, 1));
		String sql1="SELECT se.symbol,s.close FROM ltisystem_securitydailydata s right join ltisystem_security se on s.securityid=se.id  right join ltisystem_company c on se.symbol=c.symbol where date='"+date+"'";
		String sql2="";
		Map<String,Double>map = new HashMap<String, Double>();
		/*
		try{
			List list1=super.findBySQL(sql1);
			List list2=super.findBySQL(sql2);
			if(list1!=null&&list1.size()!=0&&list2!=null&&list2.size()!=0){
			
			}
		}catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		*/
		return map;
	}

	@Override
	public Map<String, Double> getPrice(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String d = sdf.format(date);
		String sql = "SELECT b.symbol,a.close FROM `ltisystem_securitydailydata` a left join `ltisystem_security` b on a.securityid=b.id where a.date='"+d+"'";
		Map<String,Double> map =new HashMap<String, Double>();
		try {
			List list=super.findBySQL(sql);
			
			if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
					Object[] objs=(Object[])list.get(i);
					String symbol=(String)objs[0];
					Double v=(Double)objs[1];
					if(v!=null&&symbol!=null)map.put(symbol,v);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return map;
	}

	/****              function: specify data access for Cash Rich Strategy                 ****/
	/****  return symbol,cash and net cash in balance sheet and shares in incomestatement   ****/
	@Override
	public Map<String, Double[]> getBalanceCash(int year, int quarter) {
		String sql = "SELECT a.symbol,a.cashnequiv,a.cashnequiv-a.curliab, b.shares FROM `ltisystem_balancestatement`a join `ltisystem_incomestatement`b on a.symbol=b.symbol and a.year=b.year and a.quarter=b.quarter where a.year='"+year+"' and a.quarter='"+quarter+"' order by a.symbol desc;";
		Map<String,Double[]> map =new HashMap<String, Double[]>();
		try {
			List list=super.findBySQL(sql);	
			if(list!=null){
				for(int i=0;i<list.size();i++){
					Object[] objs=(Object[])list.get(i);
					String symbol=(String)objs[0];
					Double v1=(Double)objs[1];
					Double v2=(Double)objs[2];
					Double v3=(Double)objs[3];
					if(v1!=null&&symbol!=null&&v2!=null&&v3!=null){
						Double[] ds=new Double[3];
						ds[0]=v1;
						ds[1]=v2;
						ds[2]=v3;
						map.put(symbol,ds);
					}
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return map;
	}

	/***** function: return all stocks' eps of the current quarter by the given date  ****/
	@Override
	public Map<String, Double> getEPS(Date date) {
		int year = LTIDate.getYear(date);
		int quarter = LTIDate.getQuarter(date);
		String sql = "SELECT symbol,netinc/shares FROM `ltisystem_incomestatement`where year="+year+" and quarter="+quarter+" and shares>=0;";
		Map<String,Double> map =new HashMap<String, Double>();
		try {
			List list=super.findBySQL(sql);
			
			if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
					Object[] objs=(Object[])list.get(i);
					String symbol=(String)objs[0];
					Double v=(Double)objs[1];
					if(v!=null&&symbol!=null)map.put(symbol,v);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return map;
	}

	/****  function: get all the stocks' floats value by the given date   *****/
	@Override
	public Map<String, Double> getAllFloats(Date date) {
		int year = LTIDate.getYear(date);
		int quarter = LTIDate.getQuarter(date);
		String sql = "SELECT symbol,floats FROM `ltisystem_incomestatement`where year="+year+" and quarter="+quarter+";";
		Map<String,Double> map =new HashMap<String, Double>();
		try {
			List list=super.findBySQL(sql);
			
			if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
					Object[] objs=(Object[])list.get(i);
					String symbol=(String)objs[0];
					System.out.println(symbol);
					Double v=(Double)objs[1];
					System.out.println(v);
					if(v!=null&&symbol!=null)map.put(symbol,v);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return map;
	}

	@Override
	public Map<String, Double> getAllInstitutionHolders(Date date) {
		int year = LTIDate.getYear(date);
		int quarter = LTIDate.getQuarter(date);
		String sql ="SELECT symbol,institutionholder FROM `ltisystem_incomestatement`where year="+year+" and quarter="+quarter+";";
		Map<String,Double> map =new HashMap<String, Double>();
		try {
			List list=super.findBySQL(sql);
			if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
					Object[] objs=(Object[])list.get(i);
					String symbol=(String)objs[0];
					System.out.println(symbol);
					Double v = (Double)objs[1];
					System.out.println(v);
					if(v!=null&&symbol!=null)map.put(symbol,v);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return map;
	}

	@Override
	public Map<String, Double> getRSGrade(Date date) {
		int year = LTIDate.getYear(date);
		int month = date.getMonth()+1;
		String sql = "SELECT symbol,rank FROM `ltisystem_relativestrength`where year(date)="+year+" and month(date)="+month+";";
		String sql1 = "SELECT count(symbol) FROM `ltisystem_relativestrength`where year(date)="+year+" and month(date)="+month+";";
		BigInteger size = BigInteger.ZERO;
		
		try{
		List list1 = super.findBySQL(sql1);
		Object obj = (Object)list1.get(0);
		size = (BigInteger)obj;
		}catch(Exception e){
			System.out.println(StringUtil.getStackTraceString(e));
		}
		
		Map<String,Double> map =new HashMap<String, Double>();
		try {
			List list=super.findBySQL(sql);
			
			if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
					Object[] objs=(Object[])list.get(i);
					String symbol=(String)objs[0];
					BigInteger v=(BigInteger)objs[1];
					Double grade = 100.0-100.0*v.doubleValue()/size.doubleValue();
					if(v!=null&&symbol!=null)map.put(symbol,grade);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return map;
	}

	@Override
	public Map<String, Double> getAnnualEPS(int year) {
		String sql = "SELECT symbol,netinc/shares FROM `ltisystem_incomestatement_yearly`where year="+year+" and shares>=0;";
		Map<String,Double> map =new HashMap<String, Double>();
		try {
			List list=super.findBySQL(sql);
			
			if(list!=null&&list.size()!=0){
				for(int i=0;i<list.size();i++){
					Object[] objs=(Object[])list.get(i);
					String symbol=(String)objs[0];
					Double v=(Double)objs[1];
					if(v!=null&&symbol!=null)map.put(symbol,v);
				}
			}
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
		}
		return map;
	}

	
	
	
	
}
