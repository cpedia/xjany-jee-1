package com.lti.action.example;
import com.lti.action.Action;
import com.opensymphony.xwork2.ActionSupport;


public class TestAction extends ActionSupport implements Action {
	
	org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(TestAction.class); 
	
	private String test;
	
	
	public String getTest() {
		return test;
	}


	public void setTest(String test) {
		this.test = test;
	}


	@Override
	public String execute() throws Exception {
		test="Hello world!";
		return Action.SUCCESS;
	}

}
