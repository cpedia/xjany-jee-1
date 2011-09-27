package com.lti.util.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Timer;

public class HttpTest extends java.util.TimerTask{
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
	public static void main(String[] args){
		Timer dailyRequest = new Timer();
		dailyRequest.schedule(new HttpTest(), new Date(),3600000);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.print("["+new Date()+"]*********************************************************");
		System.out.print(getHtml("http://www.validfi.com/LTISystem/jsp/ajax/CustomizePage.action?pageName=HomePageTable"));
	}
}
