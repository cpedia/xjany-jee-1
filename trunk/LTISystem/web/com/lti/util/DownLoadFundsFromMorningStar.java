/**
 * 
 */
package com.lti.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.lti.service.bo.AssetClass;
import com.lti.service.bo.CompanyFund;
import com.lti.system.ContextHolder;
import com.lti.service.SecurityManager;
import com.lti.util.html.ParseHtmlTable;

/**
 * @author Administrator
 *
 */
public class DownLoadFundsFromMorningStar {

	private SecurityManager sm;
	private List<CompanyFund> companyFundList;
	private String company;
	
	private final String separator="#";
	
	public DownLoadFundsFromMorningStar(String company){
		this.company = company;
		companyFundList = new ArrayList<CompanyFund>();
		sm = ContextHolder.getSecurityManager();
	}
	
	private boolean isBlank(char c) {
		if (c == '\t' || c == '\n' || c == ' ' || c == '\r')
			return true;
		else
			return false;
	}

	private boolean isTable(char[] a, int i) {
		try {
			String s=new String(a,i,5);
			if (s.equalsIgnoreCase("table"))
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	private boolean isTR(char[] a, int i) {
		try {
			String s=new String(a,i,2);
			if (s.equalsIgnoreCase("tr"))
				return true;
		} catch (Exception e) {
		}
		return false;
	}

	private boolean isTD(char[] a, int i) {
		try {
			String s=new String(a,i,2);
			if (s.equalsIgnoreCase("td"))
				return true;
		} catch (Exception e) {
		}
		return false;
	}
	
	private boolean isAhref(char[] a, int i){
		try {
			String s=new String(a,i,8);
			if (s.equalsIgnoreCase("a href=\'"))
				return true;
		} catch (Exception e) {
		}
		return false;
	}
		
	
	/**
	 * 行数从第3行开始考虑
	 * 列数只考虑第1,5列
	 * 以上都是从0开始考虑
	 * @param table
	 * @return
	 */
	private void getCompanyFundListFromTable(String table){
		char[] a = table.toCharArray();
		String link = "";
		StringBuffer col = new StringBuffer();
		List<String> dataList = new ArrayList<String>();
		int length = table.length();
		boolean flag = true;
		boolean inTable = false;
		boolean isTREnd = false;
		boolean isTDEnd = false;
		boolean isAhref = false;
		int tdNum = 0;
		int trNum = 0;
		for(int i = 0; i < length; ++i){
			if (a[i] == '<') {
				flag = false;
				while (i + 1 < length && isBlank(a[++i]))
					;
				if (isTable(a, i)) {
					inTable = true;
				}else if (isTD(a, i)) {
					++tdNum;
				}else if(isAhref(a, i)) {
					i+=7;
					isAhref = true;
				}else if (a[i] == '/' && isTable(a, i + 1)) {
					inTable = false;
				}else if (a[i] == '/' && isTR(a, i + 1)) {
					isTREnd=true;
				}else if (a[i] == '/' && isTD(a, i + 1)) {
					isTDEnd=true;
				}else if(a[i]  =='/' && a[i+1] == 'a') {
					isAhref = false;
				}
				continue;
			}
			if (a[i] == '>') {
				if(isAhref) isAhref = false;
				flag = true;
				continue;
			}
			if ( flag && inTable) {
				col.append(a[i]);
			}
			
			if(isAhref && inTable && tdNum > 5) {
				int linkStartIndex = i;
				while( i + 1 < length && a[++i] != '\'');
				int linkLength = i - linkStartIndex;
				link = new String(a, linkStartIndex, linkLength);
				if(!link.startsWith("http:")){
					if(!link.startsWith("/"))
						link = "http://quote.morningstar.com/" + link;
					else
						link = "http://quote.morningstar.com" + link;
				}
				isAhref = false;
			}
			if(isTDEnd){
				String str=col.toString().replaceAll("&nbsp;", "").trim();
				if(tdNum == 2 || tdNum == 6){
					//System.out.println(str);
					dataList.add(str);
				}
				col = new StringBuffer();
				isTDEnd=false;
			}
			if(isTREnd){
				if(trNum > 2){
					CompanyFund cf = new CompanyFund();
					if(dataList.size() == 2){
						cf.setTicker(dataList.get(0));
						cf.setMSName(dataList.get(1));
					}
					cf.setMSLink(link);
					cf.setCompany(company);
					companyFundList.add(cf);
				}
				isTREnd=false;
				trNum++;
				tdNum=0;
				link="";
				dataList.clear();
			}
		}
		//sm.saveAllCompanyFund(companyFundList);
	}
	
	private void getFundTable(String html){
		int index = html.indexOf("View All");
		if(index != -1) {
			int beginIndex = html.indexOf("<table", index);
			int endIndex = html.indexOf("</table>", beginIndex) + 8;
			String tableString = html.substring(beginIndex, endIndex);
			getCompanyFundListFromTable(tableString);
		}
	}
	
	private void  setCompanyFundCategoryAndChartLink(CompanyFund cf) {
		if(cf.getMSLink() == null || cf.getMSLink().equals("")){
			return;
		}
		String html = "html";
		int tryCount = 0;
		int beginIndex, endIndex;
		while(html.equals("html") && tryCount <= 30){
			html=ParseHtmlTable.getHtml(cf.getMSLink());
			++tryCount;
			if(html==null)html="html";
		}
		//TICKERNOTFOUND表示没有找到该FUND以及类似的
		//TICKERFOUND表示没有找到该FUND但找到类似的
		if(html.contains("TICKERFOUND") || html.contains("TICKERNOTFOUND"))
			return;
		beginIndex = html.indexOf("Name",0)+7;
		endIndex = html.indexOf("\"", beginIndex);
		//assetClassName.toCharArray();
		String hm = html.substring(beginIndex, endIndex);
		String category="";
		if(hm.startsWith("Miscellaneous")){
			category = "Miscellaneous";
		}else{
			char[] c = html.substring(beginIndex, endIndex).toCharArray();
			
			for(int i=0;i<c.length;++i){
				if(c[i]!='\\')
					category += c[i];
			}
			category.replace("\\", "");
		}
		if(category.equals("") || category.equals("ull,") || category.equals("rt"))
			cf.setCategory("");
		else
			cf.setCategory(category);
		char[] htmlStr = html.toCharArray();
		int length = html.length();
		int index = html.indexOf("<body>");
		index = html.indexOf("chart.aspx", index);
		String newLink = "";
		if(index != -1){
			//向前向后找直到找到"'"
			beginIndex = index;
			endIndex = index;
			while(beginIndex > 0 && htmlStr[beginIndex] != '\'')
				--beginIndex;
			while(endIndex < length && htmlStr[endIndex] != '\'')
				++endIndex;
			newLink = html.substring(beginIndex + 1, endIndex);
			if(!newLink.startsWith("http:")){
				if(!newLink.startsWith("/"))
					newLink = "http://quote.morningstar.com/" + newLink;
				else
					newLink = "http://quote.morningstar.com" + newLink;
			}
			cf.setMSLink(newLink);
		}
	}
	
	public void downLoad() throws IOException{
		//如果已经下载了这个company的数据，不再重复下载
//		if(sm.hasFundsForCompany(company)){
//			return;
//		}
		String strUrl = "http://quote.morningstar.com/TickerLookupResult.html?ticker=" + company + "&pageno=0&view=All&TLC=M#Result";
		String html = "html";
		int tryCount = 0;
		while(html.equals("html") && tryCount < 30){
			html = ParseHtmlTable.getHtml(strUrl);
			++tryCount;
			if(html == null) html = "html";
		}
		if(!html.equals("html")) 
			getFundTable(html);
		if(companyFundList != null && companyFundList.size() > 0){
			for(CompanyFund cf: companyFundList){
				try{
					setCompanyFundCategoryAndChartLink(cf);
					System.out.println(cf);
				}catch(Exception e){
					System.out.println(StringUtil.getStackTraceString(e));
				}
			}
		}
		sm.saveAllCompanyFund(companyFundList);
	}
	//一种只在MS上没有ticker的funds里面搜索
	//一种只在MS上有ticker的funds里面搜索
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		DownLoadFundsFromMorningStar df = new DownLoadFundsFromMorningStar("Fidelity");
		df.downLoad();
	}

}
