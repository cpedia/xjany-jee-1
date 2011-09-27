package com.lti.type;

import java.util.Map;

public class HMap{
	public HMap(){
		
	}
	public HMap(Map<String,String> m) {
		map=m;
	}
	private Map<String,String> map;

	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	
}