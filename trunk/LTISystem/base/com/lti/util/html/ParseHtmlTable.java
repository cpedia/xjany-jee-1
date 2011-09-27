package com.lti.util.html;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.lti.util.LTIDate;
import com.lti.util.validate.TableMap;

public class ParseHtmlTable {
	
	private static String[] specialChars={"&amp;","&"};

	private static boolean isBlank(char c) {
		if (c == '\t' || c == '\n' || c == ' ' || c == '\r')
			return true;
		else
			return false;
	}

	private static boolean isTable(char[] a, int i) {
		try {
			String s=new String(a,i,5);
			if (s.equalsIgnoreCase("table"))
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	private static boolean isTR(char[] a, int i) {
		try {
			String s=new String(a,i,2);
			if (s.equalsIgnoreCase("tr"))
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	private static boolean isTD(char[] a, int i) {
		try {
			String s=new String(a,i,2);
			if (s.equalsIgnoreCase("td"))
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	//no test
	public static String getContent(String s,int table,int row,int col){
		char[] a=s.toUpperCase().toCharArray();
		
		int table_c=0;
		int row_c=0;
		int col_c=0;
		int length=a.length;
		int start=0;
		int end=0;
		for (int i=0; i <= length; i++) {
			if (a[i] == '<') {
				end = i;
				while (i + 1 < length && isBlank(a[++i]))
					;
				if (isTable(a, i)) {
					table_c++;
					row_c=0;
					col_c=0;
					while (a[++i] != '>')
						;
				} else if (isTR(a, i)) {
					row_c++;
					col_c=0;
					while (a[++i] != '>')
						;
				} else if (isTD(a, i)) {
					col_c++;
					while (a[++i] != '>')
						;
				} else if (a[i] == '/' && isTD(a, ++i)) {
					while (a[++i] != '>')
						;
					if (table==table_c&&row_c==row&&col_c==col) {
						return new String(a,start+1,end-start-1);
					}
				}
			}
		}
		
		return null;
	}
	
	public static String getTable(String s,int count){
		char[] a=s.toUpperCase().toCharArray();
		
		int table_c=0;
		int length=a.length;
		int start=0;
		int end=0;
		for (int i=0; i < length; i++) {
			if (a[i] == '<') {
				
				end=i;
				while (i + 1 < length && isBlank(a[++i]))
					;
				if (isTable(a, i)) {
					
					table_c++;
					while (a[++i] != '>')
						;
					start=i;
				}  else if (a[i] == '/' && isTable(a, ++i)) {
					while (a[++i] != '>')
						;
					if (count==table_c) {
						String t=new String(a,start+1,end-start-1);
						return "<TABLE>"+t+"</TABLE>";
					}
				}
			}
		}
		
		return null;
	}
	
	public static String removeColumn(String table,int count){
		char[] a=table.toUpperCase().toCharArray();
		
		StringBuffer sb=new StringBuffer();
		int col_c=0;
		int length=a.length;
		boolean flag=false;
		for (int i=0; i < length; i++) {
			
			if (a[i] == '<') {
				while (i + 1 < length && isBlank(a[++i])){
				}
				
				if (isTR(a, i)) {
					col_c=0;
					while (a[++i] != '>'){
					}
					sb.append("<TR");
				} else if (isTD(a, i)) {
					col_c++;
					if(col_c==count)flag=true;
					while (a[++i] != '>'){
					}
					if(!flag)sb.append("<TD");
				} else if (a[i] == '/' && isTD(a, ++i)) {
					while (a[++i] != '>'){
					}
					if(!flag)sb.append("</TD");
					flag=false;
					continue;
				}else{
					sb.append("<");
				}
			}
			if(!flag)sb.append(a[i]);
		}
		
		return sb.toString();
	}
	
	private TableMap CurTableMap;
	private List<String> ColKeys;

	public TableMap parseTR(char[] a, int i, int j) {
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

	public void parseTD(char[] a, int i, int j, int trcount) {
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
			//if (a[i] == '&') {
			//	while (i + 1 < j && !isBlank(a[i + 1]))
			//		i = i + 1;
			//}
			if (flag == true) {
				sb.append(a[i]);
			}
		}
		return sb.toString();
	}
	public static String getHtml(String urlString) {
		try {
			StringBuffer html = new StringBuffer();
			URL url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			InputStreamReader isr = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			String temp;
			while ((temp = br.readLine()) != null) {
				html.append(temp).append("\n");
			}
			br.close();
			isr.close();
			return html.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String separator="#";
	public static int MaxLen=50;
	public static int MinCols=4;
	public static int MaxCols=50;
	
	public static String extractTableContent(char[] a,int i,int j){
		StringBuffer page = new StringBuffer();
		StringBuffer line = new StringBuffer();
		StringBuffer col =new StringBuffer();
		boolean flag = true;
		int tableCount =0;
		int tdCount=0;
		boolean isTREnd=false;
		boolean isTDEnd=false;
		for (; i <= j; i++) {
			if (a[i] == '<') {
				flag = false;
				while (i + 1 < a.length && isBlank(a[++i]))
					;
				if (isTable(a, i)) {
					tableCount++;
				}else if (isTD(a, i)) {
					tdCount++;
				}else if (a[i] == '/' && isTable(a, i + 1)) {
					tableCount--;
				}else if (a[i] == '/' && isTR(a, i + 1)) {
					isTREnd=true;
				}
				else if (a[i] == '/' && isTD(a, i + 1)) {
					isTDEnd=true;
				}
				continue;
			}
			if (a[i] == '>') {
				flag = true;
				continue;
			}
			if (flag == true&&tableCount>0) {
				col.append(a[i]);
			}
			
			if(isTDEnd){
				String str=col.toString().replaceAll("&nbsp;", "").trim();
				int len=str.length();
				if(len<1||len>MaxLen){
					
				}else{
					line.append(str);
				}
				line.append(separator);
				col=new StringBuffer();
				isTDEnd=false;
			}
			if(isTREnd){
				if(tdCount>MaxCols||tdCount<MinCols){
					//page.append("\n");
				}
				else{
					line.append("\n");
					page.append(line);
				}
				line=new StringBuffer();
				isTREnd=false;
				tdCount=0;
				
			}
		}
		return page.toString();
	}

	public static String extractTableContent(char[] a,int i,int j,int MaxLen,int MinCols,int MaxCols){
		StringBuffer page = new StringBuffer();
		StringBuffer line = new StringBuffer();
		StringBuffer col =new StringBuffer();
		boolean flag = true;
		int tableCount =0;
		int tdCount=0;
		boolean isTREnd=false;
		boolean isTDEnd=false;
		for (; i <= j; i++) {
			if (a[i] == '<') {
				flag = false;
				while (i + 1 < a.length && isBlank(a[++i]))
					;
				if (isTable(a, i)) {
					tableCount++;
				}else if (isTD(a, i)) {
					tdCount++;
				}else if (a[i] == '/' && isTable(a, i + 1)) {
					tableCount--;
				}else if (a[i] == '/' && isTR(a, i + 1)) {
					isTREnd=true;
				}
				else if (a[i] == '/' && isTD(a, i + 1)) {
					isTDEnd=true;
				}
				continue;
			}
			if (a[i] == '>') {
				flag = true;
				continue;
			}
			if (flag == true&&tableCount>0) {
				col.append(a[i]);
			}
			
			if(isTDEnd){
				String str=col.toString().replaceAll("&nbsp;", "").trim();
				int len=str.length();
				if(len<1||len>MaxLen){
					
				}else{
					line.append(str);
				}
				line.append(separator);
				col=new StringBuffer();
				isTDEnd=false;
			}
			if(isTREnd){
				if(tdCount>MaxCols||tdCount<MinCols){
					//page.append("\n");
				}
				else{
					line.append("\n");
					page.append(line);
				}
				line=new StringBuffer();
				isTREnd=false;
				tdCount=0;
				
			}
		}
		return page.toString();
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
			//if (a[i] == '&') {
			//	while (i + 1 < a.length && !isBlank(a[i + 1]))
			//		i = i + 1;
			//}
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
		//String retstr=sb.toString().replaceAll("<td>\\s*</td>", "");
		//retstr=retstr.replaceAll("<tr>\\s*</tr>", "");
		//retstr=retstr.replaceAll("<table>\\s*</table>", "");
		return sb.toString();
	}

	public static boolean isASCII(char c) {
		if (c >= 32 && c <= 127)
			return true;
		else
			return false;
	}

	public TableMap parseTable(String htmlStr) {
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

	public static int countTable(String html){
		int count = 0;
		char[]a = html.toCharArray();
		for(int i=0;i<a.length-5;i++){
			if(a[i] == '/' && isTable(a, i + 1)){
				count+=1;
			}
		}
		return count;
	}

	public static void main(String[] args){
		Double floats = 0.0;
		String html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/ks?s=IBM");
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
		String result=com.lti.util.html.ParseHtmlTable.extractTableContent(targetTable.toCharArray(), 0, targetTable.length()-1,100,2,100);
		String[] elements = result.split("#");
		System.out.println(elements[7]);
		if(elements[7].endsWith("B"))floats=Double.parseDouble(elements[7].substring(0, elements[7].length()-1))*1000000000;
		else if(elements[7].endsWith("M"))floats=Double.parseDouble(elements[7].substring(0, elements[7].length()-1))*1000000;
		else floats=Double.parseDouble(elements[7].substring(0, elements[7].length()-1))*1000;
		System.out.println(floats);
			
}
}
