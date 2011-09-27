package com.lti.action.ajax;

import com.lti.action.Action;
import com.lti.cache.LTICache;
import com.opensymphony.xwork2.ActionSupport;

public class PutNewsAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	LTICache newsCache=LTICache.getNewsCache();

	String news;

	String resultString;
	
	Long portfolioID;

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public String getNews() {
		return news;
	}

	public void setNews(String news) {
		this.news = news;
	}

	public String execute() throws Exception {

		try {
			newsCache.put(portfolioID, news);
			resultString = "0";
			return Action.SUCCESS;
		} catch (Exception ex) {
			resultString = "-1";
			return Action.SUCCESS;
		}
	}
}
