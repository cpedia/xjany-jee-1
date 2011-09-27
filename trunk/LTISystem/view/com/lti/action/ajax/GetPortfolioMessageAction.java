package com.lti.action.ajax;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.PortfolioState;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class GetPortfolioMessageAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	PortfolioManager portfolioManager;

	private Long id;

	private String resultString;

	private Boolean runInLocal;

	private Boolean runInJob;

	public Boolean getRunInJob() {
		return runInJob;
	}

	public void setRunInJob(Boolean runInJob) {
		this.runInJob = runInJob;
	}

	public Boolean getRunInLocal() {
		return runInLocal;
	}

	public void setRunInLocal(Boolean runInLocal) {
		this.runInLocal = runInLocal;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void validate() {

	}

	@Override
	public String execute() throws Exception {
		resultString = "";

		try {
			Long PortfolioID = id;

			if (id == null) {
				resultString = "Wrong operation.";
				return Action.SUCCESS;
			}

			PortfolioState ps = (PortfolioState) ActionContext.getContext().getSession().get("PortfolioState" + id);

			if (ps == null) {
				ps = portfolioManager.getPortfolioState(PortfolioID);
			}
			
			if(ps!=null)resultString=ps.getState()+"";
			else resultString = "Wrong operation.";

		} catch (Exception e) {
			resultString = "Wrong operation.";
		}

		return Action.SUCCESS;

	}

}
