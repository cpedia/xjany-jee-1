package com.lti.action.ajax;

import com.lti.action.Action;
import com.opensymphony.xwork2.ActionSupport;

public class StopExecutionAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	
	private Long portfolioID;
	
	private String resultString;

	public String getResultString() {
		return resultString;
	}


	public void setResultString(String resultString) {
		this.resultString = resultString;
	}


	
	public Long getPortfolioID() {
		return portfolioID;
	}


	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}


	public String execute(){
		resultString="";
		try {
			//Executor e=ExecutorPool.getInstance().getExecutor(portfolioID);
			//if(e!=null)e.stop();
			resultString="<script>alert('Please wait, it start to stop now!');</script>";
		} catch (Exception e) {
			// TODO: handle exception
			resultString = null;
		}
		return Action.SUCCESS;
	}

}
