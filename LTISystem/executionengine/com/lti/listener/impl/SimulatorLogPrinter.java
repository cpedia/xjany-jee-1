package com.lti.listener.impl;

import java.util.ArrayList;
import java.util.List;

import com.lti.listener.PrintLogProcessor;
import com.lti.service.bo.Log;

public class SimulatorLogPrinter implements PrintLogProcessor{
	
	List<Log> logs=new ArrayList<Log>();

	public SimulatorLogPrinter() {
		logs=new ArrayList<Log>();
	}
	
	@Override
	public void printToLog(Log log) {
		logs.add(log);
	}

	public List<Log> getLogs() {
		return logs;
	}

	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}
	
	public void clear(){
		logs=new ArrayList<Log>();
	}
	

}
