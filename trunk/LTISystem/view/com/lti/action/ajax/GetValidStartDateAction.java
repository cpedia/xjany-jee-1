package com.lti.action.ajax;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.lti.action.Action;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionSupport;

public class GetValidStartDateAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;
	
	private SecurityManager securityManager;
	
	private String resultString;
	
	private String benchmarkArray;

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public static DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
	
	public String execute(){
		String[] benchmarks=benchmarkArray.replace(" ", "").split(",");
		Date d=LTIDate.getDate(1800, 1, 1);
		String symbol="SECURITY";
		for(int i=0;i<benchmarks.length;i++){
			Security s=securityManager.get(benchmarks[i]);
			if(s!=null){
				Date startdate=securityManager.getStartDate(s.getID());
				if(startdate!=null&&startdate.after(d)){
					d=startdate;
					symbol=s.getSymbol();
				}
			}
		}
		//resultString=symbol+"'s start date is: "+df.format(d);
		resultString="The earliest start date is: "+df.format(d);
		return Action.SUCCESS;
	}

	public String getBenchmarkArray() {
		return benchmarkArray;
	}

	public void setBenchmarkArray(String benchmarkArray) {
		this.benchmarkArray = benchmarkArray;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}
}
