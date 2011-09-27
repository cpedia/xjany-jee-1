package com.lti.action.fundcenter;

import org.apache.struts2.config.Namespace;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.views.freemarker.FreemarkerResult;


/**
 * @author Michael Chua
 * 
 */
@ParentPackage("struts-default")
@Namespace("/jsp/test/")
@Results({
	@Result(name="2", type=FreemarkerResult.class, value = "/jsp/info.jsp", params = {})   
})
public class TestAction{
	
	public String execute(){
		return "2";
	}
}
