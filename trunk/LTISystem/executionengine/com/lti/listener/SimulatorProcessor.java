package com.lti.listener;

import com.lti.executor.Simulator;
import com.lti.service.bo.Portfolio;

public interface SimulatorProcessor {
	public void success(Simulator sim);
	public void error(Portfolio p,Throwable e);
}
