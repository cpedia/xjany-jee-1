package com.lti.action.customizepage;

import java.util.Enumeration;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.customizepage.BasePage;
import com.lti.customizepage.PageCompiler;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.sun.tools.internal.ws.processor.model.Request;

/**
 * @author Michael Cai
 * 
 */
public class CustomizePageAction extends BasePage implements Action {

	private static final long serialVersionUID = 1L;
	private  String pageContent;

	public String getPageContent() {
		return pageContent;
	}

	public void setPageContent(String pageContent) {
		this.pageContent = pageContent;
	}

	@Override
	public String execute() throws Exception {
		String url=ServletActionContext.getRequest().getRequestURL().toString();
		int i=url.lastIndexOf('/')+1;
		String action=url.substring(i,url.length()-7);
		BasePage b=PageCompiler.getPage(action);
		b.init();
		b.setParameters(ActionContext.getContext().getParameters());
		b.executeUserCode();
		root=b.getRoot();
		Enumeration e=ServletActionContext.getRequest().getParameterNames();
		while(e.hasMoreElements()){
			String key=(String)e.nextElement();
			root.put(key, ServletActionContext.getRequest().getParameter(key));
		}
		
		return Action.SUCCESS;

	}

	public static void main(String[] args) {
		CustomizePageAction a = new CustomizePageAction();
		if (a instanceof ActionSupport) {
			System.out.println("OK");
		}
	}

}
