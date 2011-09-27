package com.lti.executor;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.Exception.Executor.NotAllowRunningException;
import com.lti.executor.web.PortfoliosFilter;
import com.lti.executor.web.action.Action;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.base.BasePortfolio;
import com.lti.system.ContextHolder;
import com.lti.type.finance.ExecutorPortfolio;
import com.lti.util.EmailUtil;
import com.lti.util.IPUtil;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

public class BatchExecutorThread extends Thread {

	protected String title;

	protected PortfoliosFilter filter;
	
	protected PriorityQueue<ExecutorPortfolio> portfolioQueue;
	
	protected Set<Long> planIDSet;
	
	protected List<ExecutorPortfolio> portfolios;
	
	
	public synchronized void addPlanPortfolios(Long planID, List<ExecutorPortfolio> executorPortfolioList){
		if(!planIDSet.contains(planID)){
			portfolioQueue.addAll(executorPortfolioList);
			portfolios.addAll(executorPortfolioList);
			updateSize += executorPortfolioList.size();
			this.planIDSet.add(planID);
		}
	}
	
	public void addPortfolios(List<ExecutorPortfolio> executorPortfolioList){
		portfolioQueue.addAll(executorPortfolioList);
		portfolios.addAll(portfolioQueue);
		updateSize = executorPortfolioList.size();
	}
	
	public synchronized boolean containPlanID(Long planID){
		if(planIDSet != null)
			return planIDSet.contains(planID);
		return false;
	}
	
	public synchronized void stopPlanPortfolios(Long planID, List<Long> portfolioIDList){
		if(planIDSet.contains(planID)){
			if(portfolioIDList != null && portfolioIDList.size() > 0){
				for(Long portfolioID : portfolioIDList){
					for(ExecutorPortfolio ep: portfolioQueue){
						if(ep.getPortfolioID().equals(portfolioID)){
							portfolioQueue.remove(ep);
							--updateSize;
							for(ExecutorPortfolio eep: portfolios){
								if(eep.getPortfolioID().equals(portfolioID)){
									portfolios.remove(eep);
									break;
								}
							}
							break;
						}
					}
					
				}
			}
			planIDSet.remove(planID);
		}
		
	}
	
	public BatchExecutorThread(String string) {
		super(string);
		portfolioQueue = new PriorityQueue<ExecutorPortfolio>(100, new Comparator<ExecutorPortfolio>() {
	          public int compare(ExecutorPortfolio ep1, ExecutorPortfolio ep2) {
	        	  if(ep1.getPriority() < ep2.getPriority())
	        		  return 1;
	        	  else if(ep1.getPriority() > ep2.getPriority())
	        		  return -1;
	        	  else 
	        		  return ep1.getPortfolioID() < ep2.getPortfolioID() ? 1 : -1;
	            }
	          }  
		);
		planIDSet = new HashSet<Long>();
		portfolios = new ArrayList<ExecutorPortfolio>();
		currentPointer = 1;
	}

	public PortfoliosFilter getFilter() {
		return filter;
	}

	public void setFilter(PortfoliosFilter filter) {
		this.filter = filter;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date startDate=new Date();

	public int updateSize = 0;
	
	public int currentPointer = 0;
	
	public long currentPortfolioID = 0l;
	
	public String currentPortfolioName = "Not ready yet.";
	
	public Date currentStartDate = new Date();
	
	private int mode=0;
	
	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}
	
	private boolean stop=false;

	public boolean isStop() {
		return stop;
	}

	public void setStop(boolean stop) {
		this.stop = stop;
	}

	boolean forceMonitor=false;
	
	public boolean isForceMonitor() {
		return forceMonitor;
	}

	public void setForceMonitor(boolean forceMonitor) {
		this.forceMonitor = forceMonitor;
	}
	
	public void run() {
		boolean start=false;
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long timestamp=System.currentTimeMillis();
		CsvListWriter clw=null;
		File file=null;
		try {
			file=new File(title+""+ LTIDate.parseDateToString(new Date(timestamp))+".csv");
			clw=new CsvListWriter(new FileWriter(file), CsvPreference.STANDARD_PREFERENCE);
			Action.timestamphistory.add(timestamp);
			Action.history.put(timestamp+".startingdate", new Date());
			Action.history.put(timestamp+".title", title);
			Action.history.put(timestamp+".forceMonitor", forceMonitor+"");
			if(mode==0){
				if(Action.setDailyUpdate(true)){
					start = true;
				}else{
					return;
				}
				List<Portfolio> portfolioList = filter.getPortfolios(forceMonitor);
				List<ExecutorPortfolio> executorPortfolioList = new ArrayList<ExecutorPortfolio>();
				for(Portfolio p : portfolioList){
					ExecutorPortfolio ep = new ExecutorPortfolio();
					ep.setPriority(3);
					ep.setForceMonitor(forceMonitor);
					ep.setEndDate(p.getEndDate());
					ep.setPortfolioID(p.getID());
					ep.setPortfolioName(p.getName());
					executorPortfolioList.add(ep);
				}
				addPortfolios(executorPortfolioList);
				Action.history.put(timestamp+".mode", "daily_update");
			}else if(mode==1){
				start = true;
				Action.setBatchUpdate(true);
				Action.history.put(timestamp+".mode", "batch_update");
			}
			//portfolios=filter.getPortfolios(forceMonitor);
			//updateSize = portfolios.size();
			Action.history.put(timestamp+".size", updateSize);
			List<String> list = new ArrayList<String>();
			list.add(portfolioQueue.size() + "");
			clw.write(list);
			while(portfolioQueue.size() > 0){
				long t1=System.currentTimeMillis();
				ExecutorPortfolio ep = portfolioQueue.poll();
				++currentPointer;
				currentPortfolioID = ep.getPortfolioID();
				currentPortfolioName = ep.getPortfolioName();
				currentStartDate = new Date();
				try {
					ExecutorPool.getInstance().addExecutorPortfolio(ep);
				} catch (NotAllowRunningException e) {
					continue;
				}
				//portfolioID, portfolioName, endDate|N/A, success|fail, beginTime, endTime, cost Time, success | getStackTraceString
				Executor executor = new Executor();
				Object newPortfolio = executor.execute(ep.getPortfolioID(), null, ep.getForceMonitor(), Action.listener);
				ExecutorPool.getInstance().removeExecutorPortfolio(ep);
				long t2 = System.currentTimeMillis();
				list = new ArrayList<String>();
				list.add(ep.getPortfolioID() + "");
				list.add(ep.getPortfolioName());
				Date endDate = null;
				if( newPortfolio!= null && newPortfolio instanceof Portfolio) endDate = ((BasePortfolio) newPortfolio).getEndDate();
				if( endDate == null)
					endDate = ep.getEndDate();
				if( endDate != null)
					list.add(df.format(endDate));
				else
					list.add("N/A");
				if (endDate != null) {
					Calendar ca = Calendar.getInstance();
					ca.setTimeInMillis(System.currentTimeMillis());
					if (ca.get(Calendar.HOUR_OF_DAY) < 16) {
						ca.add(Calendar.DAY_OF_YEAR, -1);
					}
					Date tdate = LTIDate.getRecentNYSETradingDay(ca.getTime());

					if (LTIDate.equals(tdate, endDate)) {
						list.add("success");
					}else
						list.add("fail");
				}else
					list.add("fail");
				list.add(df.format(new Date(t1)));
				list.add(df.format(new Date(t2)));
				list.add((t2 - t1) * 1.0 / 1000 + "secs");
				if( newPortfolio != null && newPortfolio instanceof Throwable)
					list.add(StringUtil.getStackTraceString((Throwable) newPortfolio));
				else
					list.add("success.");
				clw.write(list);
				//if(stop)break;
			}
		}catch(Exception e){
			ContextHolder.addException(e);
			System.out.println(StringUtil.getStackTraceString(e));
		}finally {
			if(start){
				if(mode==0){
					Action.setDailyUpdate(false);
					EmailUtil.sendEmails(false);
				}
				if(mode==1)
					Action.setBatchUpdate(false);
			}
			if(clw!=null){
				try {
					clw.close();
					EmailUtil.sendAttachment(new String[]{"wyjfly@gmail.com","caixg@ltisystem.com","jzhong@gmail.com"}, "["+df.format(new Date())+"]Execution Summarize", "From "+IPUtil.getLocalIP(), "report-"+df.format(new Date())+".csv", file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			Action.history.put(timestamp+".enddate", new Date());
		}
	}

	public PriorityQueue<ExecutorPortfolio> getPortfolioQueue() {
		return portfolioQueue;
	}

	public void setPortfolioQueue(PriorityQueue<ExecutorPortfolio> portfolioQueue) {
		this.portfolioQueue = portfolioQueue;
	}

	public Set<Long> getPlanIDSet() {
		return planIDSet;
	}

	public void setPlanIDSet(Set<Long> planIDSet) {
		this.planIDSet = planIDSet;
	}

	public List<ExecutorPortfolio> getPortfolios() {
		return portfolios;
	}
	
}