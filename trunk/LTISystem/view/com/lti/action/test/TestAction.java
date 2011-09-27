package com.lti.action.test;

import org.apache.struts2.config.NullResult;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.views.freemarker.FreemarkerResult;

import com.lti.action.Action;
import com.opensymphony.xwork2.ActionSupport;

/**
 * @author Michael Chua
 *
 */
public class TestAction extends ActionSupport implements Action{
	private static final long serialVersionUID = 1L;
	@Override
	public void validate() {
		
	}
	@Results({   
	    @Result(name="success", type=FreemarkerResult.class, value = "/jsp/fundcenter.uftl", params = {}),   
	})
	@Override
	public String execute(){
		return "success";
	}
}
