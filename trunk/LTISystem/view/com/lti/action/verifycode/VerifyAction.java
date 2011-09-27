package com.lti.action.verifycode;

import javax.servlet.http.HttpServlet;  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  

import java.io.IOException;  
import java.io.PrintWriter;  

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import com.opensymphony.xwork2.ActionSupport;
import com.lti.action.Action;

public class VerifyAction extends ActionSupport implements ServletRequestAware,ServletResponseAware,Action{
	
	private static final long serialVersionUID = 1L;
	
	private HttpServletResponse response;
	
	private HttpServletRequest request;
	
	private String actionMessage;
	
	public String execute() throws Exception
	{
		response.setContentType("text/html;charset=utf-8");  
		String validateC = (String) request.getSession().getAttribute("validateCode");  
		String veryCode = request.getParameter("c");  
		PrintWriter out = response.getWriter();  
        if(veryCode==null||"".equals(veryCode)){  
            out.println("Please enter the correct Verification Code.");
            out.flush();    
            out.close(); 
            return Action.ERROR;
        }else{  
            if(validateC.equalsIgnoreCase(veryCode)){  
                out.println("Verification Code is correct."); 
                out.flush();    
                out.close(); 
                return Action.SUCCESS;
            }else{  
           	 out.println("Verification Code is incorrect.");
                out.flush();    
                out.close(); 
                return Action.ERROR;
           }  
        }
	}

	@Override
	public void setServletRequest(HttpServletRequest arg0) {
		// TODO Auto-generated method stub
		this.request=arg0;
		
	}

	@Override
	public void setServletResponse(HttpServletResponse arg0) {
		// TODO Auto-generated method stub
		this.response=arg0;
		
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getActionMessage() {
		return actionMessage;
	}

	public void setActionMessage(String actionMessage) {
		this.actionMessage = actionMessage;
	}

}
