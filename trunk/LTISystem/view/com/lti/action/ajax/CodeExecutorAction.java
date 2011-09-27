package com.lti.action.ajax;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import com.lti.SimpleExecutor.SimpleExecutor;
import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Transaction;
import com.opensymphony.xwork2.ActionSupport;



public class CodeExecutorAction  extends ActionSupport implements Action{

	private static final long serialVersionUID = 1L;

	
	public String getResultString() {
		return resultString;
	}



	public void setResultString(String resultString) {
		this.resultString = resultString;
	}


	private String resultString;
	
	private String code;

	public String getCode() {
		return code;
	}



	public void setCode(String code) {
		this.code = code;
	}



	public String execute() throws Exception {
		resultString="";
		try{
			resultString+="<table width=100%>";
			String path = ServletActionContext.getServletContext().getRealPath("/");
			
			String fileName=SimpleExecutor.Complie(path, code);
			List<String> messages=SimpleExecutor.execute(fileName);
			SimpleExecutor.Delete(fileName, path);
			if(messages!=null){
				for(int i=0;i<messages.size();i++){
					resultString+="<tr><td>";
					resultString+=messages.get(i)+"\r\n";
					resultString+="</td></tr>";
				}
			}
			resultString+="</table>";
			return Action.SUCCESS;
		}catch(Exception ex){
			String resultString="<table width=\"100%\">";
			resultString+="<tr><td>There was no transaction!</td></tr>";
			resultString+="</table>";
			return Action.SUCCESS;
		}
	}
}
