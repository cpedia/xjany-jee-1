package com.lti.type;

import java.io.Serializable;

public class Quaternion implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String First;
	public String Second;
	public String Third;
	public String Fourth;
	
	public String getFourth() {
		return Fourth;
	}

	public void setFourth(String fourth) {
		Fourth = fourth;
	}

	public Quaternion(){
		this.First=new String();
		this.Second=new String();
		this.Third=new String();
		this.Fourth=new String();
	}
	
	public Quaternion(String first,String second, String third,String fourth){
		this.First=first;
		this.Second=second;
		this.Third=third;
		this.Fourth=fourth;
	}
	
	public boolean equals(Quaternion p){
		if(this.First.equals(p.First)&&
				this.Second.equals(p.Second)&&
				this.Third.equals(p.Third)&&
				this.Fourth.equals(p.Fourth))
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
