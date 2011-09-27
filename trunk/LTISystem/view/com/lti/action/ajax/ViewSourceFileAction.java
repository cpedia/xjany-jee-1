package com.lti.action.ajax;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;

import com.lti.action.Action;
import com.lti.action.portfolio.ajax.StrategyParameterAction;
import com.lti.executor.CompilerPreProcessor;
import com.lti.service.StrategyManager;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.StringUtil;
import com.lti.util.SystemEnvironment;
import com.opensymphony.xwork2.ActionSupport;

public class ViewSourceFileAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	private Long strategyID;

	private StrategyManager strategyManager;
	
	private String resultString;

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public String execute() throws Exception {
		String path = StringUtil.getPath(new String[] { ContextHolder.getServletPath(), (String) Configuration.get("COMPILED_STRATEGY_SRC_PATH") });
		name = CompilerPreProcessor.getValidateName(strategyManager.getLatestStrategyCode(strategyID).getCode());
		File f = new File(StringUtil.getPath(new String[] { path, name+".java" }));
		FileReader reader = new FileReader(f);
		BufferedReader br = new BufferedReader(reader);
		String s1 = null;
		StringBuffer sb = new StringBuffer();
		while ((s1 = br.readLine()) != null) {
			sb.append(s1).append("\r\n");
		}
		br.close();
		reader.close();
		resultString=sb.toString();
		return Action.SUCCESS;
	}

	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getStrategyID() {
		return strategyID;
	}

	public void setStrategyID(Long strategyID) {
		this.strategyID = strategyID;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

}
