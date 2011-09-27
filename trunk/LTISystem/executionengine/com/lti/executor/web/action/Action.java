package com.lti.executor.web.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lti.executor.BatchExecutorThread;
import com.lti.executor.web.ExecutorListenerForWeb;



public class Action {
	private static boolean isDailyUpdate=false;
	private static boolean isBatchUpdate=false;
	public static boolean isDailyUpdate() {
		
		return isDailyUpdate;
	}
	
	public static boolean isBatchUpdate() {
		
		return isBatchUpdate;
	}
	public synchronized static boolean setDailyUpdate(boolean id) {
		if(id==true){
			if(Action.isDailyUpdate==true){
				return false;
			}
		}
		Action.isDailyUpdate = id;
		return true;
	}
	public synchronized static boolean setBatchUpdate(boolean ib) {
		if(ib==true){
			if(Action.isBatchUpdate==true){
				return false;
			}
		}
		Action.isBatchUpdate = ib;
		return true;
	}
	
	public static BatchExecutorThread dailyUpdate;
	public static BatchExecutorThread batchUpdate;
	
	public static Map<String,Object> session=new HashMap<String, Object>();
	
	public static ExecutorListenerForWeb listener=new ExecutorListenerForWeb();
	
	public static Map<String, Object> history=new HashMap<String, Object>();
	public static List<Long> timestamphistory=new ArrayList<Long>();
}
