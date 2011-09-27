package com.lti.action.ajax;

import com.lti.action.Action;
import com.lti.customizepage.BasePage;
import com.lti.customizepage.PageCompiler;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Michael Cai
 * 1. consider to remove this file
 *
 */
public class CustomizePageAction extends ActionSupport implements Action  {

	private static final long serialVersionUID = 1L;
	
	private String pageName;
	
	private String resultString;
	
	private boolean includeHeader;
	
	public boolean isIncludeHeader() {
		return includeHeader;
	}

	public void setIncludeHeader(boolean includeHeader) {
		this.includeHeader = includeHeader;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public void validate(){
	}

	
	@Override
	public String execute() throws Exception {
//		if(includeHeader)return "header";
// 		resultString="";
//		//String name=servletRequest.getParameter("name");
//		if(pageName==null||(pageName.equals("")))resultString="Error Request!";
//		else {
//			try {
//				java.util.Map parameters=ActionContext.getContext().getParameters();
//				BasePage bp=PageCompiler.getPage(pageName);
//				bp.setParameters(parameters);
//				resultString=bp.getOutput();
//			} catch (Exception e) {
//				// TODO: handle exception
//				resultString = StringUtil.getStackTraceString(e);
//			}
//			
//		}
		resultString="<script>window.location.href='/LTISystem/jsp/customizepage/"+pageName+".action';</script>";
		
		 return Action.SUCCESS;

	}

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}	
	

}
