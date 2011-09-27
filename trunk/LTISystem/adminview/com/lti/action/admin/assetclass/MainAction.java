package com.lti.action.admin.assetclass;


import java.util.List;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.AssetClassManager;
import com.lti.service.bo.AssetClass;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private List<AssetClass> assetClasses;

	
	private AssetClassManager assetClassManager;
	

	public List<AssetClass> getAssetClasses() {
		return assetClasses;
	}

	public void setAssetClasses(List<AssetClass> assetClasses) {
		this.assetClasses = assetClasses;
	}

	public void setAssetClassManager(AssetClassManager assetClassManager) {
		this.assetClassManager = assetClassManager;
	}
	
	private String menuString;

	@Override
	public String execute() throws Exception {
		
		return Action.SUCCESS;

	}

	public String getMenuString() {
		return menuString;
	}

	public void setMenuString(String menuString) {
		this.menuString = menuString;
	}
}
