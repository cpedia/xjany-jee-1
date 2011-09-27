package com.lti.type;

import java.io.Serializable;

public class Triple implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String First;
	public String Second;
	public String Third;
	
	public Triple(){
		this.First=new String();
		this.Second=new String();
		this.Third=new String();
	}
	
	public Triple(String first,String second, String third){
		this.First=first;
		this.Second=second;
		this.Third=third;
	}
	
	public boolean equals(Triple p){
		if(this.First.equals(p.First)&&this.Second.equals(p.Second)&&this.Third.equals(p.Third))
			return true;
		else return false;
	}

	public String getFirst() {
		return First;
	}

	public void setFirst(String first) {
		First = first;
	}

	public String getSecond() {
		return Second;
	}

	public void setSecond(String second) {
		Second = second;
	}

	public String getThird() {
		return Third;
	}

	public void setThird(String third) {
		Third = third;
	}

}
