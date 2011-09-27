package com.lti.action.example;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.rosuda.JRI.REXP;

import com.lti.action.Action;
import com.lti.action.main.MainAction;
import com.opensymphony.xwork2.ActionSupport;

import com.lti.util.LTIRInterface;


public class TestValidationAction  extends ActionSupport implements Action{

	private static final long serialVersionUID = 1L;

	private String name;
	
	private Date date;
	
	private Double a;

	static Log log = LogFactory.getLog(TestValidationAction.class);
	@Override
	public String execute() throws Exception {
		log.info("LogFactory");
	
		System.out.println("111111");
		
		String[] a = new String[2];
		
		REXP[] b = new REXP[2];
		
		String[] bb = new String[2];
		
		a[0] = "LTI_return1=1+1";
		
		a[1] = "LTI_return2=2^3";
		
		System.out.println("222222");
		
		LTIRInterface li = LTIRInterface.getInstance();
		
		System.out.println("3333333");
		
		b = li.RCall2(a, 2);
		
		System.out.println("4444444");
		
		for(int i=0;i<b.length;i++)
		{
			this.a = li.parseDouble(b[i]);
			
		}
		
		//li.closeEngine();
		
		return Action.SUCCESS;

	}


	public void validate(){
		if(name==null||name.equals("")){
			//addFieldError("name","Name should not be null!");
		}
		if(date==null){
			//addFieldError("date","Date format wrong!");
		}
	}
	
	public String getName() {
	
		return name;
	}


	
	public void setName(String name) {
	
		this.name = name;
	}


	
	public Date getDate() {
	
		return date;
	}


	
	public void setDate(Date date) {
	
		this.date = date;
	}

	public void setA(Double a)
	{
		this.a = a;
	}
	
	public Double getA()
	{
		return this.a;
	}
	
}
