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

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private List<StrategyClass> strategyClasses;

	
	private StrategyClassManager strategyClassManager;
	

	public List<StrategyClass> getStrategyClasses() {
		return strategyClasses;
	}

	public void setStrategyClasses(List<StrategyClass> strategyClasses) {
		this.strategyClasses = strategyClasses;
	}

	public void setStrategyClassManager(StrategyClassManager strategyClassManager) {
		this.strategyClassManager = strategyClassManager;
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
