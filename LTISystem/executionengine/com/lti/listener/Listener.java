package com.lti.listener;

import java.util.Date;

import com.lti.executor.Simulator;
import com.lti.service.bo.Portfolio;

public interface Listener {
	public void afterFail(Portfolio p, Throwable ex);

	public void beforeStart(Simulator sim);

	public void beforeResume(Simulator sim);

	public void afterInit(Simulator sim);
	
	public void afterReInit(Simulator sim);

	public void afterAction(Simulator sim, Date date);

	public void afterAction(Simulator sim);

	public void afterComputeMPT(Simulator sim);

	public void afterFinish(Simulator sim);

	public void afterResume(Simulator sim);

}
