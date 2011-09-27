package com.lti.action.ajax;

import java.util.List;

import com.lti.action.Action;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.opensymphony.xwork2.ActionSupport;

public class GetSecuritySuggestTxtAction  extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	SecurityManager securityManager;

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}
	private String resultString;
	
	private String q;
	
	private int considerLength = 0;
	@Override
	public String execute() throws Exception {
		resultString="";
		resultString=new String(resultString.getBytes("ISO-8859-1"), "UTF-8");
    	try{
			if(q!=null&&!q.equals("")){
				q=new String(q.getBytes("ISO-8859-1"), "UTF-8");
				q=q.replace("_", "\\_");
				//List<Security> resultList=securityManager.getSecuritiesByName(q);
				List<Security> resultList=securityManager.getSecuritiesByNameConsiderLength(q, considerLength);
				for(int i=0;i<resultList.size();i++){
					resultString+=resultList.get(i).getSymbol();
					resultString+="#";
					resultString+=resultList.get(i).getName();
					resultString+="\n";
					if(i>=10)break;
				}
			}
		}catch(Exception ex){
		}
		return Action.SUCCESS;

	}
	public String getResultString() {
		return resultString;
	}
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}
	public String getQ() {
		return q;
	}
	public void setQ(String q) {
		this.q = q;
	}
	public int getConsiderLength() {
		return considerLength;
	}
	public void setConsiderLength(int considerLength) {
		this.considerLength = considerLength;
	}

}
