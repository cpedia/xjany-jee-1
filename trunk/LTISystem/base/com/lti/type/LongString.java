package com.lti.type;

public class LongString {
	private Long ID;
	private String name;
	
	public LongString(){
		
	}
	
	public LongString(Long id,String n){
		ID=id;
		name=n;
	}
	
	public Long getID() {
		return ID;
	}
	public void setID(Long id) {
		ID = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
