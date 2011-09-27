package com.lti.system;


public class MessageCenter {
	private static MessageCenter instance=null;

	public static synchronized MessageCenter getInstance() {
		if(instance==null)return new MessageCenter();
		else return instance;
	}
	
	private MessageCenter() {

	}
	
	
	
	
	
}