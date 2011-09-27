package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseSymbol implements Serializable{
	private int id;
	
	private String symbol;
	
	private int topicId;
	
	private String title;
	
	private java.sql.Timestamp time;
	
	private boolean needModerate;
	
	public boolean isNeedModerate() {
		return needModerate;
	}

	public void setNeedModerate(boolean needModerate) {
		this.needModerate = needModerate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public java.sql.Timestamp getTime() {
		return time;
	}

	public void setTime(java.sql.Timestamp time) {
		this.time = time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	
	
}
