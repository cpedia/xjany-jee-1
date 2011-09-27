package com.lti.type;

import java.util.HashMap;
import java.util.Iterator;

import com.lti.util.StringUtil;

public class Counter {
	private java.util.Map<String,Integer> counter;
	public Counter(){
		counter=new HashMap<String, Integer>();
	}
	public Integer get(String key) {
		return counter.get(key);
	}
	public Integer put(String key, Integer value) {
		return counter.put(key, value);
	}
	public void add(String key) {
		this.add(key,1);
	}
	public void add(String key, Integer value) {
		Integer i=counter.get(key);
		if(i==null)i=value;
		else{
			i+=value;
		}
		counter.put(key, i);
	}
	public String output(){
		if(counter.size()==0)return "";
		Iterator<String> keyset=counter.keySet().iterator();
		StringBuffer sb=new StringBuffer();
		while(keyset.hasNext()){
			String key=keyset.next();
			Integer value=counter.get(key);
			sb.append(StringUtil.getOutput(key, value.toString()));
		}
		return sb.toString();
	}
}
