package com.lti.type;

import java.util.Date;

import com.lti.util.LTIDate;

public class Quarter {
	public int year;
	public int quarter;
	public Date date;
	public Quarter(int year,int quarter){
		date=LTIDate.getLastNYSETradingDayOfMonth(LTIDate.getDate(year, quarter*3, 1));
	}
	public int getYear() {
		return year;
	}
	public int getQuarter() {
		return quarter;
	}
	public Date getDate() {
		return date;
	}
	//test
	public static void main(String[]args){
		java.util.Date d=LTIDate.getDate(2008, 12, 30);
		d=LTIDate.getLastDayOfQuarter(d);
		String s=d.toString();
		//System.out.println(d);
		//String tmp = s.substring(8, 10)+"-"+s.substring(4,7)+"-"+s.substring(26,28);
		//System.out.println(s.substring(4,7)+"-"+s.substring(8, 10)+"-"+s.substring(26,28));
		String html=com.lti.util.html.ParseHtmlTable.getHtml("http://finance.yahoo.com/q/is?s=IBM");
		String content = com.lti.util.html.ParseHtmlTable.extractTableContent(html.toCharArray(),0, html.length()-1);
		
	    
		
	}
}

