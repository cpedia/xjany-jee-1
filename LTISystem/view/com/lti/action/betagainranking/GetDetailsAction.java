/**
 * 
 */
package com.lti.action.betagainranking;

import java.util.Enumeration;

import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author CCD
 *
 */
public class GetDetailsAction extends ActionSupport implements Action{
	private String symbol;
	
	private String url;
	
	public String execute()
	{
		return Action.SUCCESS;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}


	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
