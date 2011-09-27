package com.lti.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lti.service.bo.Transaction;
import com.lti.system.ContextHolder;

public class PortfolioUtil {
	public static DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	public static DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");

	public static String[] OPERATIONS = new String[] { "buy", "sell", "buy_at_open", "sell_at_open" };
	public static String[] PRICES = new String[] { "price", "pr" };
	public static String[] SHARES = new String[] { "share", "shares", "shr", "sh" };
	public static String[] AMOUNTS = new String[] { "amount", "amnt", "am", "amt" };

	public static String[] getOperAndSymbol(String line) {
		for (int i = 0; i < OPERATIONS.length; i++) {
			Pattern pa = Pattern.compile("^" + OPERATIONS[i] + "\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
			Matcher ma = pa.matcher(line);
			if (ma.find()) {
				return new String[] { OPERATIONS[i], ma.group(1) };
			}
			pa = Pattern.compile("\\s+" + OPERATIONS[i] + "\\s+(\\w+)", Pattern.CASE_INSENSITIVE);
			ma = pa.matcher(line);
			if (ma.find()) {
				return new String[] { OPERATIONS[i], ma.group(1) };
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(getDate("buy 324kklkak;07/04/2003"));
		System.out.println(getOperAndSymbol("ka;kd;jkja;buy 324kklkak;ljfjfprice 24.30k;adfkjk;af22")[1]);
		System.out.println(getOperAndSymbol("ka;kd;jkja; buy 324kklkak;ljfjfprice 24.30k;adfkjk;af22")[1]);
	}

	public static String dateregs[] = new String[] { "(01|02|03|04|05|06|07|08|09|10|11|12)(\\/)([0-3]?\\d)(\\/)(\\d{4})", "(\\d{4})-(01|02|03|04|05|06|07|08|09|10|11|12)-([0-3]?\\d)" };

	public static Date getDate(String line) throws Exception {
		for (String regex : dateregs) {
			Pattern pa = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher ma = pa.matcher(line);
			if (ma.find()) {
				try {
					return df1.parse(ma.group());
				} catch (Exception e) {
					return df2.parse(ma.group());
				}
			}
		}

		return null;
	}

	public static Double getPrice(String line) {
		for (int i = 0; i < PRICES.length; i++) {
			Double d = getAfterSomeWords(line, PRICES[i]);
			if (d != null)
				return d;
		}
		return null;
	}

	public static Double getAmount(String line) {
		for (int i = 0; i < AMOUNTS.length; i++) {
			Double d = getAfterSomeWords(line, AMOUNTS[i]);
			if (d != null)
				return d;
		}
		return null;
	}

	public static Double getShare(String line) {
		for (int i = 0; i < SHARES.length; i++) {
			Double d = getAfterSomeWords(line, SHARES[i]);
			if (d != null)
				return d;
		}
		return null;
	}

	public static Double getAfterSomeWords(String line, String w) {
		String regexs[] = new String[] { "^" + w + "\\s*((-?\\d+)(\\.\\d+)?)", "\\s+" + w + "\\s*((-?\\d+)(\\.\\d+)?)" };
		for (String regex : regexs) {
			Pattern pa = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher ma = pa.matcher(line);
			if (ma.find()) {
				try {
					return Double.parseDouble(ma.group(1));
				} catch (Exception e) {
				}
			}
		}

		return null;
	}

	public static List<Double> getNumbers(String line) {
		String str = line;
		for (String regex : dateregs) {
			str = str.replaceAll(regex, "");
		}
		String regex = "((-?\\d+)(\\.\\d+)?)";
		Pattern pa = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher ma = pa.matcher(str);
		List<Double> nums = new ArrayList<Double>();
		while (ma.find()) {
			try {
				nums.add(Double.parseDouble(ma.group(1)));
			} catch (Exception e) {
			}
		}

		return nums;
	}

	public static Object[] parseTransactions(String personalTransactions) {
		List<Transaction> perTrs = new ArrayList<Transaction>();
		String[] lines = personalTransactions.replaceAll("\r\n", "\n").split("\n");
		StringBuffer sb = new StringBuffer();
		// 获得页面每一行的数据
		for (int tt = 0; tt < lines.length; tt++) {
			String line = lines[tt];
			try {
				line = line.toLowerCase().trim();
				if (line.startsWith("operation")) {
					continue;
				}

				String[] oper_sym = getOperAndSymbol(line);
				if (oper_sym == null) {
					sb.append("#").append((tt + 1)).append(":").append(line).append("\n");
					sb.append("\tThe operation should be 'buy' or 'sell'.").append("\n\n");
					continue;
				}

				String oper = oper_sym[0];
				String symbol = oper_sym[1];

				Date date = getDate(line);
				if (date == null) {
					sb.append("#").append((tt + 1)).append(":").append(line).append("\n");
					sb.append("\tThe date is null.").append("\n\n");
					continue;
				}

				Transaction tr = new Transaction();
				tr.setOperation(oper);
				tr.setSymbol(symbol);
				tr.setDate(date);
				// rule 1
				Double share = getShare(line);
				Double amount = getAmount(line);
				Double price = getPrice(line);

				// rule 2
				if (amount == null && share == null && price==null) {
					List<Double> nums = getNumbers(line);
					if (nums.size() > 0) {
						share = nums.get(0);
					}
					if (nums.size() > 1) {
						price = nums.get(1);
					}
					if (nums.size() > 2) {
						amount = nums.get(2);
					}
				}

				// rule 3
				if (price == null) {
					try {
						price = ContextHolder.getSecurityManager().get(symbol).getClose(date);
					} catch (Exception e) {
					}
					
				}
				if (share == null && amount != null && price != null) {
					share = amount / price;
				}
				if (share != null && amount == null && price != null) {
					amount = price * share;
				}

				// check validation
				if (share == null || price == null || amount == null || (price != null && share != null && amount != null && price * share != amount)) {
					sb.append("#").append((tt + 1)).append(":").append(line).append("\n");
					if (price == null) {
						sb.append("\tCannot find the price of ").append(symbol).append(".\n\n");
					} else if (share == null) {
						sb.append("\tShare is null.").append("\n\n");
					} else if (amount == null) {
						sb.append("\tAmount is null.").append("\n\n");
					} else {
						sb.append("\tShare * Price != Amount.").append("\n\n");
					}
					continue;
				}

				tr.setShare(share);
				tr.setAmount(amount);
				tr.setPrice(price);

				perTrs.add(tr);
			} catch (Exception e) {
				sb.append("#").append((tt + 1)).append(":").append(line).append("\n");
				sb.append(e.getMessage()).append("\n\n");
				continue;
			}
		}

		return new Object[] { perTrs, sb.toString() };
	}

	public static String format(String str,int len){
		StringBuffer sb=new StringBuffer(str);
		if(str.length()<len){
			for(int i=0;i<len-str.length();i++){
				sb.append(" ");
			}
		}
		return sb.toString();
	}
	
	public static String formatTransactions(List<Transaction> perTrs) {
		StringBuffer sb = new StringBuffer();
		sb.append(format("Operation",12));
		sb.append(format("Symbol",10));
		sb.append(format("Date",12));

		sb.append(format("Shares",10));
		sb.append(format("Price",10));
		
		sb.append(format("Amount",10));
		sb.append("\r\n");
		if (perTrs != null && perTrs.size() > 0) {

			for (Transaction t : perTrs) {
				sb.append(format(t.getOperation(),12));
				sb.append(format(t.getSymbol(),10));
				sb.append(format(df2.format(t.getDate()),12));
				sb.append(format(t.getShare() == null ? "NA" : FormatUtil.formatQuantity(t.getShare()),10));
				sb.append(format(t.getPrice() == null ? "NA" : FormatUtil.formatQuantity(t.getPrice()),10));
				sb.append(format(t.getAmount() == null ? "NA" : FormatUtil.formatQuantity(t.getAmount()),10));
				sb.append("\r\n");
			}
		}

		return sb.toString();
	}
}
