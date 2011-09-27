/**
 * 
 */
package com.lti.action.mutualfund;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.lti.action.Action;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.type.TimeUnit;
import com.lti.util.LTIDate;

/**
 * @author Administrator
 *
 */
public class SetDefaultDateAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	
	private String symbol;
	
	private String resultString;
	
	private Integer interval;
	
	public String execute(){
		SecurityManager securityManager=(SecurityManager)ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		Security se = securityManager.get(symbol);
		Date eDate = se.getEndDate();
		Date sDate = LTIDate.getLastYear(eDate);
		Date earliestDate = se.getStartDate();
		resultString="";
		if(earliestDate==null)
			return Action.ERROR;
		earliestDate = LTIDate.getNewNYSETradingDay(earliestDate,interval+2);
		if(sDate.before(earliestDate))
			sDate = earliestDate;
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		String startDate = sdf.format(sDate);
		String endDate = sdf.format(eDate);
		resultString=startDate +"#" + endDate;
		return Action.SUCCESS;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public Integer getInterval() {
		return interval;
	}

	public void setInterval(Integer interval) {
		this.interval = interval;
	}


}