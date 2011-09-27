package com.lti.executor;

import java.net.InetAddress;
import java.util.Date;

import com.lti.listener.Listener;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.lti.type.finance.ExecutorPortfolio;
import com.lti.util.EmailUtil;
import com.lti.util.IPUtil;
import com.lti.util.StringUtil;

public class ExecutorThread extends Thread {

	private Listener listener;

	private ExecutorPortfolio executorPortfolio;

	private Date stopDate;

	private int portfolioUpdate;
	
	private boolean forceMonitor;
	
	private boolean isCustomized = false;;
	

	public ExecutorThread(ExecutorPortfolio ep, Date d, int pu, boolean customized, Listener listener) {
		super(ep.getPortfolioName());
		portfolioUpdate = pu;
		forceMonitor = (portfolioUpdate == Configuration.PORTFOLIO_UPDATE_FORCEMONITOR ? true : false);
		stopDate = d;
		executorPortfolio = ep;
		executorPortfolio.setForceMonitor(forceMonitor);
		isCustomized = customized;
		this.listener=listener;
	}
	
	public ExecutorThread(ExecutorPortfolio ep, Date d, int pu, Listener listener) {
		super(ep.getPortfolioName());
		portfolioUpdate = pu;
		forceMonitor = (portfolioUpdate == Configuration.PORTFOLIO_UPDATE_FORCEMONITOR ? true : false);
		stopDate = d;
		executorPortfolio = ep;
		executorPortfolio.setForceMonitor(forceMonitor);
		this.listener=listener;
	}
	


	public void run() {
		if(ExecutorPool.getInstance().isRunning(executorPortfolio.getPortfolioID())){
			throw new RuntimeException("The portfolio is already running.");
		}
		try {
			ExecutorPool.getInstance().addExecutorPortfolio(executorPortfolio);
			ExecutorPool.getInstance().getThreads().put(executorPortfolio.getPortfolioID(), this);
			Executor e=new Executor();
			if(portfolioUpdate == Configuration.PORTFOLIO_UPDATE_RECONSTRUCT){
				e.construct(executorPortfolio.getPortfolioID(), stopDate, listener);
			}else{
				e.execute(executorPortfolio.getPortfolioID(), stopDate, forceMonitor, listener, isCustomized);
			}
			
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
			//sendEmail(e);
		} finally {
			ExecutorPool.getInstance().removeExecutorPortfolio(executorPortfolio);
		}

	}

	private void sendEmail(Throwable e){
		try{
			StringBuffer sb=new StringBuffer();
			sb.append("<a target='_blank' href='http://www.validfi.com/LTISystem/jsp/portfolio/ViewPortfolio.action?ID=" + executorPortfolio.getPortfolioID() + "'>");
			sb.append(executorPortfolio.getPortfolioName());
			sb.append("</a><br>\r\n");
			sb.append("<a target='_blank' href='http://www.validfi.com/LTISystem/jsp/portfolio/LogMain.action?portfolioID=" + executorPortfolio.getPortfolioID() +"'>");
			sb.append("logs");
			sb.append("</a><br>\r\n<br>\r\n");
			sb.append("Exception:<br>\r\n<pre>");
			sb.append(StringUtil.getStackTraceString(e));
			sb.append("</pre><br>\r\n<br>\r\n");
			sb.append("from "+InetAddress.getLocalHost().getHostName());
			
			if (IPUtil.getLocalIP().startsWith("70.38.112")){
				EmailUtil.sendMail(new String[]{"caixg@myplaniq.com","wyjfly@gmail.com","ccd1010@gmail.com"}, "error: " + executorPortfolio.getPortfolioName(), sb.toString());
			}
		}catch(Throwable t){
			
		}
	}
	public void stopExecution() {
		this.stop();
		ExecutorPool.getInstance().removeExecutorPortfolio(executorPortfolio);
	}


}