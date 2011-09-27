package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.Date;

public class BasePlanArticle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3205551994844734486L;
	protected Long ID;
	protected Long PlanID;
	protected String ArticleTitle;
	protected String Link;
	protected Date Date;
	protected String Content;
	protected Boolean Display;
	protected String Symbols;

	public Boolean getDisplay() {
		return Display;
	}

	public void setDisplay(Boolean display) {
		Display = display;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public Long getPlanID() {
		return PlanID;
	}

	public void setPlanID(Long planID) {
		PlanID = planID;
	}



	public String getArticleTitle() {
		return ArticleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		ArticleTitle = articleTitle;
	}

	public String getLink() {
		return Link;
	}

	public void setLink(String link) {
		Link = link;
	}

	public Date getDate() {
		return Date;
	}

	public void setDate(Date date) {
		Date = date;
	}

	public String getSymbols() {
		return Symbols;
	}

	public void setSymbols(String symbols) {
		Symbols = symbols;
	}

}
