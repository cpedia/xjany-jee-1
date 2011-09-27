package com.lti.util.validate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;

import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityMPT;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class TotalReturnValidator {

	private java.lang.Long securityID;
	// private String logFile;

	// http://quicktake.morningstar.com/FundNet/TotalReturns.aspx?Country=USA&Symbol=VWEHX
	public static String URL = "http://quicktake.morningstar.com/FundNet/TotalReturns.aspx";
	// http://quicktake.morningstar.com/etfnet/TotalReturns.aspx?Country=USA&Symbol=AGG
	public static String Country = "USA";
	public static String testURL = "http://202.116.76.163:8000/LTISystem";

	public TotalReturnValidator(){
		
	}
	public static String getTotalReturnHTML(String url, String country, String symbol) throws Exception {
		if (url == null)
			url = URL;
		if (country == null)
			country = Country;
		if (symbol == null)
			return null;
		String connectUrl = url + "?Country=" + country + "&Symbol=" + symbol;
		HttpClient client = new HttpClient();
		HttpMethod method = new GetMethod(connectUrl);
		String r = null;
		try {
			client.executeMethod(method);
			r = method.getResponseBodyAsString();
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				method.releaseConnection();
			} catch (Exception e) {
			}
		}
		return r;
	}

	private static boolean isBlank(char c) {
		if (c == '\t' || c == '\n' || c == ' ' || c == '\r')
			return true;
		else
			return false;
	}

	private static boolean isTable(char[] a, int i) {
		try {
			if (a[i] == 'T' && a[i + 1] == 'A' && a[i + 2] == 'B' && a[i + 3] == 'L' && a[i + 4] == 'E')
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	private static boolean isTR(char[] a, int i) {
		try {
			if (a[i] == 'T' && a[i + 1] == 'R')
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	private static boolean isTD(char[] a, int i) {
		try {
			if (a[i] == 'T' && a[i + 1] == 'D')
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	private static TableMap CurTableMap;
	private static List<String> ColKeys;

	public static TableMap parseTR(char[] a, int i, int j) {
		boolean isTR = false;
		int trcount = 0;
		int start = 0;
		int length = a.length;
		CurTableMap = new TableMap();
		ColKeys = new ArrayList<String>();
		for (; i <= j; i++) {
			if (a[i] == '<') {
				int end = i;
				while (i + 1 < length && isBlank(a[++i]))
					;
				if (isTR(a, i)) {
					isTR = true;
					trcount++;
					while (a[++i] != '>')
						;
					start = i;
				} else if (a[i] == '/' && isTR(a, ++i)) {
					while (a[++i] != '>')
						;
					if (isTR) {
						parseTD(a, start + 1, end - 1, trcount);
					}
					isTR = false;
				}
				continue;
			}
			if (a[i] == '>') {
				continue;
			}
		}
		TableMap tm = CurTableMap;
		CurTableMap = null;
		ColKeys = null;
		return tm;
	}

	public static void parseTD(char[] a, int i, int j, int trcount) {
		boolean isTD = false;
		int start = 0;
		int tdcount = 0;
		int length = a.length;
		String rowKey = null;
		for (; i <= j; i++) {
			if (a[i] == '<') {
				int end = i;
				while (i + 1 < length && isBlank(a[++i]))
					;
				if (isTD(a, i)) {
					isTD = true;
					tdcount++;
					while (a[++i] != '>')
						;
					start = i;
				} else if (a[i] == '/' && isTD(a, ++i)) {
					while (a[++i] != '>')
						;
					if (isTD) {
						if (trcount == 1) {
							if (tdcount == 1)
								continue;
							else {
								ColKeys.add(htmlToString(a, start + 1, end - 1));
							}
						} else {
							if (tdcount == 1) {
								rowKey = htmlToString(a, start + 1, end - 1);
							} else
								try {
									CurTableMap.put(rowKey, ColKeys.get(tdcount - 2), htmlToString(a, start + 1, end - 1));
								} catch (Exception e) {
									// e.printStackTrace();
								}
						}
					}
					isTD = false;
				}
				continue;
			}
			if (a[i] == '>') {
				continue;
			}
		}
	}

	public static String htmlToString(char[] a, int i, int j) {
		StringBuffer sb = new StringBuffer();
		boolean flag = true;
		for (; i <= j; i++) {
			if (a[i] == '<') {
				flag = false;
				continue;
			}
			if (a[i] == '>') {
				flag = true;
				continue;
			}
			if (a[i] == '&') {
				while (i + 1 < j && !isBlank(a[i + 1]))
					i = i + 1;
			}
			if (flag == true) {
				sb.append(a[i]);
			}
		}
		return sb.toString();
	}

	public static String cleanHtml(String s) {
		StringBuffer sb = new StringBuffer();
		boolean flag = false;
		boolean flag_td = false;
		char[] a = s.toUpperCase().toCharArray();
		for (int i = 0; i < a.length; i++) {
			if (a[i] == '<') {
				flag = false;
				while (i + 1 < a.length && isBlank(a[++i]))
					;
				if (isTable(a, i)) {
					while (a[++i] != '>')
						;
					sb.append("<table>");
				} else if (a[i] == '/' && isTable(a, i + 1)) {
					while (a[++i] != '>')
						;
					sb.append("</table>");
				} else if (isTR(a, i)) {
					while (a[++i] != '>')
						;
					sb.append("<tr>");
				} else if (a[i] == '/' && isTR(a, i + 1)) {
					while (a[++i] != '>')
						;
					sb.append("</tr>");
				} else if (isTD(a, i)) {
					while (a[++i] != '>')
						;
					sb.append("<td>");
					flag_td = true;
				} else if (a[i] == '/' && isTD(a, i + 1)) {
					while (a[++i] != '>')
						;
					sb.append("</td>");
					flag_td = false;
				}
			}
			if (a[i] == '>') {
				flag = true;
				continue;
			}
			if (a[i] == '&') {
				while (i + 1 < a.length && !isBlank(a[i + 1]))
					i = i + 1;
			}
			if (flag_td && flag) {
				if (isBlank(a[i])) {
					if (isBlank(sb.charAt(sb.length() - 1)))
						continue;
					else
						sb.append(' ');
				} else if (isASCII(a[i]))
					sb.append(a[i]);
			}
		}
		return sb.toString();
	}

	public static boolean isASCII(char c) {
		if (c >= 33 && c <= 127)
			return true;
		else
			return false;
	}

	public static TableMap parseTable(String htmlStr) {
		boolean isTable = false;
		int start = 0;
		if (htmlStr == null) {
			return null;
		}
		TableMap tms = new TableMap();
		tms.setSource(htmlStr);
		htmlStr = htmlStr.toUpperCase();
		char[] a = htmlStr.toCharArray();
		int length = a.length;
		for (int i = 0; i < length; i++) {
			if (a[i] == '<') {
				int end = i;
				while (i + 1 < length && isBlank(a[++i]))
					;
				if (isTable(a, i)) {
					isTable = true;
					while (a[++i] != '>')
						;
					start = i;
				} else if (a[i] == '/' && isTable(a, ++i)) {
					while (a[++i] != '>')
						;
					if (isTable) {
						TableMap tm = parseTR(a, start + 1, end - 1);
						if (tm.size() != 0)
							tms.putAll(tm);
					}
					isTable = false;
				}
				continue;
			}
			if (a[i] == '>') {
				continue;
			}
		}
		return tms;
	}

	public static TableMap getTotalReturn(String url, String country, String symbol) throws Exception {
		return parseTable(cleanHtml(getTotalReturnHTML(url, country, symbol)));
	}

	public static void downloadSecurityMpt() throws Exception {
		SecurityManager securityManager = ContextHolder.getSecurityManager();
//		 List<Security> slist = securityManager.getSecurities(new String[]{"AGG","SPY"});

		List<Security> slist = securityManager.getSecurities();

		List<TableMap> tms = new ArrayList<TableMap>();
		for (int i = 0; i < slist.size(); i++) {
			Security security = slist.get(i);

			if (security.getSymbol().indexOf(".OF") > 0)
				continue;
			if (security.getSymbol().indexOf(".CEF") > 0)
				continue;
			if (security.getSecurityType() == 6)
				continue;

			TableMap tm = null;
			try {
				tm = TotalReturnValidator.getTotalReturn(null, null, security.getSymbol());
				tm.setSymbol(security.getSymbol());
				if (tm != null) {
					tms.add(tm);
					System.out.println("Download " + security.getSymbol() + "...");
				}
			} catch (Exception e) {
				continue;
			}
			if(i>15)break;
		}
		
		CheckMpt cm = new CheckMpt();
		String path = Configuration.getTempDir() + "SecurityMPT";
		FileOutputStream fo = new FileOutputStream(new File(path));
		
		ObjectOutputStream stream = new ObjectOutputStream(fo);

		stream.writeInt(tms.size());
		for (int i = 0; i < tms.size(); i++) {
			stream.writeObject(tms.get(i));
			System.out.println("Persist " + tms.get(i).getSymbol() + "...");

		}
		stream.flush();
		stream.close();
		fo.flush();
		fo.close();

	}

	public static List<TableMap> read() throws Exception {
		CheckMpt cm = new CheckMpt();
		String path;
          path =Configuration.getTempDir() + "SecurityMPT";
		FileInputStream io = new FileInputStream(new File(path));
		ObjectInputStream stream = new ObjectInputStream(io);
		int size = stream.readInt();
		List<TableMap> tms = new ArrayList<TableMap>();
		for (int i = 0; i < size; i++) {
			TableMap tm = null;
			try {
				tm = (TableMap) stream.readObject();
			} catch (RuntimeException e) {
				e.printStackTrace();
			}
			if (tm != null)
				tms.add(tm);
		}
		stream.read();
		stream.close();
		io.close();
		return tms;
	}

	/**
	 * check all SecurityMpt, compare with morningStar TotalReturn
	 * real difference : 0.2
	 */
	public void checkSecurityMpt(Double difference) throws Exception {

		List<TableMap> tms = read();
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		List<Security> slist = securityManager.getSecurities();
		CheckMpt cm = new CheckMpt();
		Date cur = new Date();
		cm.writeLog(cur + "===checkSecurityMpt===");
		for (int i = 0; i < slist.size(); i++) {
			securityID = slist.get(i).getID();
			if (securityID != null) {

				List<SecurityMPT> smpt = securityManager.getSecurityMPTS(securityID);
				if (smpt != null) {
					for (int j = 0; j < smpt.size(); j++) {

						Long year = smpt.get(j).getYear();
						Double sar = smpt.get(j).getAR();
						String ss = smpt.get(j).getSymbol();
//						if (year > 2000 && year < 2009) {

							for (int k = 0; k < tms.size(); k++) {

								String ts = tms.get(k).getSymbol();
								Double tar = tms.get(k).getDouble("TOTAL RETURN %", year.toString());

								try {

									if (ss.equalsIgnoreCase(ts)) {

										if (sar != null && tar != null && Math.abs(sar - tar*0.01) > difference) {

											cm.writeLog(ss + " big difference [" + year.toString() + "] securityAR: " + sar + "   TotalReturn: " + tar*0.01 + "  [" + Math.abs(sar - tar*0.01)+ "]");
											System.out.println(ss + " " + year.toString() + " big difference, securityAR:" + sar + " TotalReturn:" + tar*0.01 + "  [" + Math.abs(sar - tar*0.01)+ "]");

										}

									}
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
//						}

					}
				}
			}
		}
		cm.writeLog(cur + "===finish checkSecurityMpt===");

	}

	/**
	 * check Single Security PortfolioMpt
	 * real difference : 0.002
	 * @param portfolioID
	 * @param symbol
	 * @throws Exception
	 */
	public void checkSingleSecurityMpt(Long portfolioID, String symbol,Double difference) throws Exception {

		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		List<PortfolioMPT> pmpt = portfolioManager.getEveryYearsMPT(portfolioID);
		String ps = symbol;
		CheckMpt cm = new CheckMpt();
		List<TableMap> tms = read();
		// SingleSecurityPortfolioMpt compare with morningStar TotalReturn
		Date cur = new Date();
		cm.writeLog(cur + "===CheckSingleSecurity ===");
		if (pmpt != null) {
			for (int j = 0; j < pmpt.size(); j++) {

				Integer year = pmpt.get(j).getYear();
				Double par = pmpt.get(j).getAR();

//				if (year > 2000&& year < 2009) {

					for (int k = 0; k < tms.size(); k++) {

						String ts = tms.get(k).getSymbol();
						Double tar = tms.get(k).getDouble("TOTAL RETURN %", year.toString());

						try {

							if (ps.equalsIgnoreCase(ts)) {

								if (par != null && tar != null && Math.abs(par - tar*0.01) > difference) {

									cm.writeLog(ps + " big difference [" + year.toString() + "] portfolioAR: " + par + "   TotalReturn: " + tar*0.01 + "  [" + Math.abs(par - tar*0.01)+ "]");
									System.out.println(ps + " " + year.toString() + " big difference, portfolioAR:" + par + " TotalReturn:" + tar*0.01 + "  [" + Math.abs(par - tar*0.01)+ "]");

								}

							}
						} catch (Exception e) {
							e.printStackTrace();
						}

					}
//				}

			}
		}

		// SingleSecurityPortfolioMpt compare with SecurityMpt
//		cm.writeLog(cur + "===CheckSingleSecurityPortfolioMpt,compare with SecurityMpt===");
		SecurityManager securityManager = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Security s=securityManager.get(symbol);
		Long securityID= s.getID();
		List<SecurityMPT> smpt = securityManager.getSecurityMPTS(securityID);
		if (smpt != null && pmpt != null) {

			for (int i = 0; i < smpt.size(); i++) {

				Long syear = smpt.get(i).getYear();
				Double sar = smpt.get(i).getAR();

				Integer pyear;
				Double psar;
				for (int p = 0; p < pmpt.size(); p++) {
					pyear = pmpt.get(p).getYear();
					psar = pmpt.get(p).getAR();

					try {
						if ((long) pyear == syear) {
							if (sar != null && psar != null && Math.abs(psar - sar) > difference) {
								cm.writeLog(ps + " big difference [" + pyear.toString() + "] portfolioAR: " + psar + "   securityAR: " + sar + " [" + Math.abs(psar - sar) + "]");
								System.out.println(ps + " " + pyear.toString() + " big difference, portfolioAR:" + psar + " securityAR:" + sar+ " [" + Math.abs(psar - sar) + "]");
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}
		cm.writeLog(cur + "===finish ===");
	}
	public static void main(String[] args) throws Exception {
		TotalReturnValidator tv = new TotalReturnValidator();
//		tv.downloadSecurityMpt();
//		tv.checkSecurityMpt(0.1);
		
//		portfolioID = (long) 3310;
//		securityID = (long) 889;
//		symbol = "TAHYX";
		
		Double difference= 0.002;
        Long portfolioID = 3314L;
		String symbol = "FHYTX";
		tv.checkSingleSecurityMpt(portfolioID,symbol,difference);
	}
}
