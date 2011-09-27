package com.lti.action;

import java.util.Date;

import org.apache.struts2.ServletActionContext;

public class testaction {

	public String test(){
		System.out.println(new Date());
		return Action.MESSAGE;
	}
	public String test2(){
		System.out.print(new Date());
		System.out.println(ServletActionContext.getRequest().getRemoteHost());
		return Action.MESSAGE;
	}
	public String test3(){
		System.out.print(new Date());
		System.out.println(ServletActionContext.getRequest().getRemoteHost());
		return Action.MESSAGE;
	}
	public String test4(){
		System.out.print(new Date());
		System.out.println(ServletActionContext.getRequest().getRemoteHost());
		return Action.MESSAGE;
	}
	public String test5(){
		System.out.print(new Date());
		System.out.println(ServletActionContext.getRequest().getRemoteHost());
		return Action.MESSAGE;
	}
}
