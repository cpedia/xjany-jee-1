package com.lti.action.portfolio.ajax;

import com.opensymphony.xwork2.ActionSupport;
import com.lti.action.Action;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import java.util.List;
import java.util.ArrayList;

public class SecuritySuggestAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	
	private SecurityManager securityManager;
	
	private String keyWord;
	
	private String result;
	
	private List<Security> securityList;
	public SecurityManager getSecurityManager() {
		return securityManager;
	}
	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}
	
	public String getKeyWord() {
		return keyWord;
	}
	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}
	public String execute() throws Exception{
    	try{
			if(keyWord!=null&&!keyWord.equals("")){
				securityList=securityManager.getSecuritiesByName(keyWord);
				for(int i=0;i<securityList.size();i++){
					result+=securityList.get(i).getSymbol();
					result+="#";
					result+=securityList.get(i).getName();
					result+="\n";
				}
			}
			return Action.SUCCESS;
		}catch(Exception ex){
			return Action.ERROR;
		}
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
}
