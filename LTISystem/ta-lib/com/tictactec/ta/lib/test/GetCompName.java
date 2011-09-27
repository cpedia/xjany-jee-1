package com.tictactec.ta.lib.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;

import com.lti.util.UrlUtil;

public class GetCompName {

	public static String getUrl(long code) {
		String url = "http://www.sse.com.cn/sseportal/webapp/datapresent/SSEQueryListCmpAct?reportName=QueryListCmpRpt&REPORTTYPE=GSZC&COMPANY_CODE=" + code + "&PRODUCTID=" + code;
		return url;
	}

	public static String cut(String str1) {

		String str = "";
		str = str1;

		str = str.replaceAll("</?[^>]+>", ""); // 剔出了<html>的标签
		str = str.replace("&nbsp;", " ");
		str = str.replace("&amp;", "&");
		return str;

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

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		long[] codes = new long[] { 600000, 600019, 600031, 600050, 600111, 600362, 600519, 600837, 601088, 601166, 601288, 601390, 601601, 601688, 601818, 601899, 601958, 600015, 600028, 600036, 600089, 600188, 600383, 600547, 600900, 601111, 601168, 601318, 601398, 601628, 601699, 601857, 601919, 601989, 600016, 600030, 600048, 600104, 600348, 600489, 600585, 601006, 601118, 601169, 601328, 601600, 601668, 601766, 601898, 601939 };
		for (long code : codes) {
			String html = UrlUtil.getHtml(getUrl(code));
			String[] lines = html.split("\n");
			System.out.print(code);
			System.out.print(",");
			for (int i = 0; i < lines.length; i++) {
				String line = lines[i];
				if (line.indexOf("公司简称") != -1) {
					String[] names=cut(lines[i+1]).split("/");
					System.out.print(names[0]);
					System.out.print(",");
					System.out.print(names[0]);
					System.out.print(",");
				}
				if (line.indexOf("公司全称") != -1) {
					System.out.print(cut(lines[i+1]));
					System.out.print(",");
					System.out.print(cut(lines[i+2]));
					System.out.print(",");
				}
			}
			System.out.println();
			Thread.sleep(1000);
		}

	}

}
