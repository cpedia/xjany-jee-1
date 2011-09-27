package com.lti.SimpleExecutor;

import java.util.ArrayList;
import java.util.List;

import com.lti.service.AssetClassManager;
import com.lti.service.HolidayManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;

public class BaseCode {
	
	protected AssetClassManager assetClassManager;
	protected PortfolioManager portfolioManager;
	protected SecurityManager securityManager;
	protected StrategyManager strategyManager;
	protected HolidayManager holidayManager;
	
	
	List<String> messages;

	public BaseCode() {
		super();
		messages=new ArrayList<String>();
		assetClassManager=(AssetClassManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		portfolioManager=(PortfolioManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		securityManager=(SecurityManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		strategyManager=(StrategyManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("strategyManager");
		holidayManager=(HolidayManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("holidayManager");
		
	}
	
	public List<String> getMessages(){
		return messages;
	}
	
	public void print(String message){
		messages.add(message);
	}
	public void print(Object message){
		if(message==null)return;
		messages.add(String.valueOf(message));
	}
	public void print(Object[] message){
		if(message==null)return;
		for(int i=0;i<message.length;i++){
			messages.add(String.valueOf(message[i]));
		}
	}
	public void print(double[] message){
		if(message==null)return;
		for(int i=0;i<message.length;i++){
			messages.add(String.valueOf(message[i]));
		}
	}
	public void print(String[] message){
		if(message==null)return;
		for(int i=0;i<message.length;i++){
			messages.add(String.valueOf(message[i]));
		}
	}
	public void print(int[] message){
		if(message==null)return;
		for(int i=0;i<message.length;i++){
			messages.add(String.valueOf(message[i]));
		}
	}
	public void print(String name,Object[] message){
		if(message==null)return;
		for(int i=0;i<message.length;i++){
			messages.add(name+"["+i+"]"+String.valueOf(message[i]));
		}
	}
	public void print(String name,double[] message){
		if(message==null)return;
		for(int i=0;i<message.length;i++){
			messages.add(name+"["+i+"]"+String.valueOf(message[i]));
		}
	}
	public void print(String name,int[] message){
		if(message==null)return;
		for(int i=0;i<message.length;i++){
			messages.add(name+"["+i+"]"+String.valueOf(message[i]));
		}
	}
	public void print(String name,String[] message){
		if(message==null)return;
		for(int i=0;i<message.length;i++){
			messages.add(name+"["+i+"]"+String.valueOf(message[i]));
		}
	}
	public String getStackTraceString(Exception e) {
		
		return com.lti.util.StringUtil.getStackTraceString(e);
	}
}
