package com.lti.executor.web;

import java.util.Date;

import com.lti.executor.Simulator;
import com.lti.executor.web.action.Action;
import com.lti.listener.Listener;
import com.lti.service.bo.Portfolio;
import com.lti.util.StringUtil;

public class ExecutorListenerForWeb implements Listener{

	@Override
	public void afterAction(Simulator sim, Date date) {
		Portfolio p=sim.getSimulatePortfolio();
		Integer state=20;
		double per=(date.getTime()-p.getStartingDate().getTime())*1.0/(System.currentTimeMillis()-p.getStartingDate().getTime());
		state=(int)(per*70)+20;
		Action.session.put(p.getID()+".state", state);
		Action.session.put(p.getID()+".timestamp", new Long(System.currentTimeMillis()));
	
	}

	@Override
	public void afterAction(Simulator sim) {
		Portfolio p=sim.getSimulatePortfolio();
		Action.session.put(p.getID()+".state", new Integer(80));
		Action.session.put(p.getID()+".timestamp", new Long(System.currentTimeMillis()));
	}

	@Override
	public void afterComputeMPT(Simulator sim) {
		Portfolio p=sim.getSimulatePortfolio();
		Action.session.put(p.getID()+".state", new Integer(90));
		Action.session.put(p.getID()+".timestamp", new Long(System.currentTimeMillis()));
		
	}

	@Override
	public void afterFail(Portfolio p, Throwable ex) {
		Action.session.put(p.getID()+".state", new Integer(-1));
		Action.session.put(p.getID()+".timestamp", new Long(System.currentTimeMillis()));
		Action.session.put(p.getID()+".error", StringUtil.getStackTraceString(ex));
	}

	@Override
	public void afterFinish(Simulator sim) {
		Portfolio p=sim.getSimulatePortfolio();
		Action.session.put(p.getID()+".state", new Integer(100));
		Action.session.put(p.getID()+".timestamp", new Long(System.currentTimeMillis()));
		Action.session.put(p.getID()+".error", "No error message");
		
	}

	@Override
	public void afterInit(Simulator sim) {
		Portfolio p=sim.getSimulatePortfolio();
		Action.session.put(p.getID()+".state", new Integer(10));
		Action.session.put(p.getID()+".timestamp", new Long(System.currentTimeMillis()));
		
	}

	@Override
	public void afterReInit(Simulator sim) {
		Portfolio p=sim.getSimulatePortfolio();
		Action.session.put(p.getID()+".state", new Integer(10));
		Action.session.put(p.getID()+".timestamp", new Long(System.currentTimeMillis()));
		
	}

	@Override
	public void afterResume(Simulator sim) {
		Portfolio p=sim.getSimulatePortfolio();
		Action.session.put(p.getID()+".state", new Integer(100));
		Action.session.put(p.getID()+".timestamp", new Long(System.currentTimeMillis()));
		Action.session.put(p.getID()+".error", "No error message");
	}

	@Override
	public void beforeResume(Simulator sim) {
		Portfolio p=sim.getSimulatePortfolio();
		Action.session.put(p.getID()+".state", new Integer(5));
		Action.session.put(p.getID()+".timestamp", new Long(System.currentTimeMillis()));
		
	}

	@Override
	public void beforeStart(Simulator sim) {
		Portfolio p=sim.getSimulatePortfolio();
		Action.session.put(p.getID()+".state", new Integer(5));
		Action.session.put(p.getID()+".timestamp", new Long(System.currentTimeMillis()));
	}

}
