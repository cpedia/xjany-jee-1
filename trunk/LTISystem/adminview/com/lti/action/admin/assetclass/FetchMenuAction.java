package com.lti.action.admin.assetclass;



import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.AssetClassManager;
import com.lti.type.Menu;
import com.opensymphony.xwork2.ActionSupport;

public class FetchMenuAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(FetchMenuAction.class);
	

	private AssetClassManager assetClassManager;
	

	public void setAssetClassManager(AssetClassManager assetClassManager) {
		this.assetClassManager = assetClassManager;
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
		
		Menu menu=assetClassManager.getMenu(url,para);
		
		
        try {
        	JSONArray jsonObject = JSONArray.fromObject(menu);
        	resultString = jsonObject.toString();
        	System.out.println(resultString);
        } catch (Exception e) {
        	e.printStackTrace();
        	resultString = "ERROR";
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
