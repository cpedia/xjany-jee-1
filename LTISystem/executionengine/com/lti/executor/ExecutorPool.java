package com.lti.executor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lti.Exception.Executor.NotAllowRunningException;
import com.lti.service.bo.Portfolio;
import com.lti.type.finance.ExecutorPortfolio;


public class ExecutorPool {


	private static ExecutorPool instance = null;

	public static synchronized ExecutorPool getInstance() {
		if (instance == null) {
			try {
				instance = new ExecutorPool();
			} catch (Exception e) {
				System.out.println(e.getMessage());
				System.out.println(e.getMessage());
			}
		}
		return instance;
	}
	
	private Map<Long, ExecutorPortfolio> pool = new HashMap<Long, ExecutorPortfolio>();
	
	private Map<Long,Thread> threads=new HashMap<Long, Thread>();
	
	/**
	 * 如果该ExecutorPortfolio已经在执行，将会抛出异常
	 * @param ep
	 * @throws NotAllowRunningException
	 */
	public synchronized void addExecutorPortfolio(ExecutorPortfolio ep) throws NotAllowRunningException{
		if(pool.get(ep.getPortfolioID())!=null){
			throw new NotAllowRunningException("The portfolio["+ep.getPortfolioName()+"]["+ep.getPortfolioID()+"] is running.",ep.getPortfolioID(), ep.getPortfolioName());
		}else{
			pool.put(ep.getPortfolioID(), ep);
		}
	}
	
	public synchronized void removeExecutorPortfolio(ExecutorPortfolio ep){
		pool.remove(ep.getPortfolioID());
		threads.remove(ep.getPortfolioID());
	}
	
	public boolean isRunning(Long portfolioID){
		if( pool.get(portfolioID) != null)
			return true;
		return false;
	}

	public Map<Long, ExecutorPortfolio> getPool() {
		return pool;
	}

	public Map<Long, Thread> getThreads() {
		return threads;
	}

	public void setThreads(Map<Long, Thread> threads) {
		this.threads = threads;
	}

	public void setPool(Map<Long, ExecutorPortfolio> pool) {
		this.pool = pool;
	}

}
