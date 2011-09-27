package com.lti.jobscheduler;


import java.net.URLEncoder;
import java.util.TimerTask;

import org.apache.commons.httpclient.HttpClient;


public class DailyExecutionJob extends TimerTask{

	private String title;
	private String filter;
	private String interval;
	private String priorityArray;
	private String keyWords;
	private String updateMode;
	public String getKeyWords() {
		return keyWords;
	}
	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}
	public String getUpdateMode() {
		return updateMode;
	}
	public void setUpdateMode(String updateMode) {
		this.updateMode = updateMode;
	}
	public String getInterval() {
		return interval;
	}
	public String getPriorityArray() {
		return priorityArray;
	}
	public void setPriorityArray(String priorityArray) {
		this.priorityArray = priorityArray;
	}
	public void setInterval(String interval) {
		this.interval = interval;
	}
	public String getFilter() {
		return filter;
	}
	public void setFilter(String filter) {
		this.filter = filter;
	}
	public synchronized void run() {

		try {
			String url="http://127.0.0.1:8081/dailystart?portfolioID=0";
			if(title!=null){
				
				url+="&title="+URLEncoder.encode(title,"utf8");
			}
			if(filter!=null){
				url+="&filter="+URLEncoder.encode(filter,"utf8");
			}
			if(interval!=null){
				url+="&interval="+URLEncoder.encode(interval, "utf8");
			}
			if(priorityArray!=null){
				url+="&priorityArray="+URLEncoder.encode(priorityArray, "utf8");
			}
			if(updateMode!=null)
				url+="&updateMode="+URLEncoder.encode(updateMode, "utf8");
			if(keyWords!=null)
				url+="&keyWords="+URLEncoder.encode(keyWords, "utf8");
			sendRequest(url);
			System.out.println("Start daily execution.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	public static void sendRequest(String url) throws Exception {
		HttpClient client = new HttpClient();
		org.apache.commons.httpclient.HttpMethod method = new org.apache.commons.httpclient.methods.GetMethod(url);
		client.executeMethod(method);
		method.releaseConnection();
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}
