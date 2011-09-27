package com.lti.action.admin.strategyclass;


import java.util.List;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.StrategyClassManager;
import com.lti.service.bo.StrategyClass;
import com.lti.type.Menu;
import com.opensymphony.xwork2.ActionSupport;

public class FetchMenuAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(FetchMenuAction.class);
	

	private StrategyClassManager strategyClassManager;
	

	public void setStrategyClassManager(StrategyClassManager strategyClassManager) {
		this.strategyClassManager = strategyClassManager;
	}
	
	private String resultString;

	private String url;
	
	private String para;
	
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPara() {
		return para;
	}

	public void setPara(String para) {
		this.para = para;
	}

	@Override
	public String execute() throws Exception {
		
		Menu menu=strategyClassManager.getMenu(url,para);
		
		JSONArray jsonObject = JSONArray.fromObject(menu);
        try {
        	resultString = jsonObject.toString();
        } catch (Exception e) {
        	resultString = "ss";
        }
		
		return Action.SUCCESS;

	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}


}
