package com.lti.action.admin.stock;

public class TestAction {
	private String a;
	private String b;
	private String c;
	public String execute(){
		c=Integer.parseInt(a)+Integer.parseInt(b)+"";
		return "success";
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getB() {
		return b;
	}
	public void setB(String b) {
		this.b = b;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}

}
