package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public abstract class BaseCompanyFund implements Serializable{

	protected Long ID;
	
	protected String Ticker="";
	
	protected String MSName="";
	
	protected String MSLink="";
	
	protected String Category="";
	
	protected String Company;
	
	protected String AssetName="";
	
	protected String MSFullName="";
	
	protected String TickerName="";
	
	protected Date StartDate;

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String getTicker() {
		return Ticker;
	}

	public void setTicker(String ticker) {
		Ticker = ticker;
	}

	public String getMSName() {
		return MSName;
	}

	public void setMSName(String name) {
		MSName = name;
	}

	public String getMSLink() {
		return MSLink;
	}

	public void setMSLink(String link) {
		MSLink = link;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}

	public String getCompany() {
		return Company;
	}

	public void setCompany(String company) {
		Company = company;
	}

	public String getAssetName() {
		return AssetName;
	}

	public void setAssetName(String assetName) {
		AssetName = assetName;
	}

	public String getMSFullName() {
		return MSFullName;
	}

	public void setMSFullName(String fullName) {
		MSFullName = fullName;
	}

	public String getTickerName() {
		return TickerName;
	}

	public void setTickerName(String tickerName) {
		TickerName = tickerName;
	}

	public Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}

	
}