package com.lti.action.admin.portfolio;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.jobscheduler.DailyExecutionJob;
import com.lti.jobscheduler.DailyUpdateJob;
import com.lti.jobscheduler.DailyUpdateControler;
import com.lti.jobscheduler.FastDailyUpdateControler;
import com.lti.service.PortfolioManager;
import com.lti.system.ContextHolder;
import com.lti.type.LongString;
import com.lti.type.Pair;
import com.lti.type.Triple;
import com.opensymphony.xwork2.ActionSupport;

public class ExecutionStateAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(ExecutionStateAction.class);

	private List<LongString> executors;

	private List<Triple> executeThreads;

	private List<Pair> logs;

	private PortfolioManager portfolioManager;

	private Boolean isUpdatingDB;
	
	private Boolean isFastUpdating;
	
	private String updateDBPeriod;
	private String updateDBStartDate;

	private Boolean isUpdating;
	private Integer totalSize;
	private Double process;
	private String startDate;
	private String runPeriod;

	private String curPortfolioName;
	private Long curPortfolioID;
	private String curStartDate;
	private String curRunPeriod;
	
	private int stateSize=16;
	private int currentCount;
	private String[] stateList=null;
	private int totalCount;
	private String[] processState=null;
	private String state;

	public List<LongString> getExecutors() {
		return executors;
	}

	public void setExecutors(List<LongString> executors) {
		this.executors = executors;
	}
	public void initialState()
	{
		stateList=new String[this.stateSize];
		processState=new String[this.stateSize];
		for(int i=0;i<this.stateSize;++i)
			processState[i]="UnBegin";
		stateList[0]="Update CEF&Dividend";
		stateList[1]="Upload Dividend";
		stateList[2]="Update Daily Price";
		stateList[3]="Upload Daily Price";
		stateList[4]="Upload Nav";
		stateList[5]="Update AdjNav";
		stateList[6]="Update Cash";
		stateList[7]="Update FamaBenchMark";
		stateList[8]="Update CN Fund";
		stateList[9]="Update CN Cash";
		stateList[10]="Check Split";
		stateList[11]="Update Indicator";
		stateList[12]="Update S&P Earning&PE";
		stateList[13]="Update Stocks Financial Statement";
		stateList[14]="Update Relative Strength Data";
		stateList[15]="Update MPT";
	}
	/**
	 * 
	 * 1 stands for ok
	 * 2 stands for processing
	 * 3 stands for unbegin
	 * **/
	public void checkProcess()
	{
		int current=0;
		int i;
		for(i=this.stateSize-1;i>=0;--i){
			if(this.state.equals(stateList[i]))
			{
					current=i;
					processState[i]="Processing";
					break;
			}
		}
		for(i=0;i<current;++i)
			processState[i]="Finish";
		for(i=current+1;i<this.stateSize;++i)
			processState[i]="UnBegin";
	}
	@Override
	public String execute() throws Exception {

		/*List<Long> ids =null;//= ExecutorPool.getInstance().getWorkingPortfolio();

		executors = new ArrayList<LongString>();

		if (ids != null && ids.size() >= 1) {
			for (int i = 0; i < ids.size(); i++) {
				executors.add(new LongString(ids.get(i), portfolioManager.get(ids.get(i)).getName()));
			}
		}

		executeThreads = new ArrayList<Triple>();

		isUpdating = DailyExecutionControler.isUpdating;
		if (isUpdating) {
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
				startDate = df.format(DailyExecutionControler.startDate);
				runPeriod = String.valueOf(((new Date()).getTime() - DailyExecutionControler.startDate.getTime()) * 1.0 / 1000 / 60);
				totalSize = DailyExecutionControler.updateSize;
				process = DailyExecutionControler.currentPointer * 100.0 / DailyExecutionControler.updateSize;
				curPortfolioName = portfolioManager.get(DailyExecutionControler.currentPortfolioID).getName();
				curPortfolioID = DailyExecutionControler.currentPortfolioID;
				curStartDate = df.format(DailyExecutionControler.currentStartDate);
				curRunPeriod = String.valueOf(((new Date()).getTime() - DailyExecutionControler.currentStartDate.getTime()) * 1.0 / 1000 / 60);
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
			}
		} else {
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-DD hh:mm:ss");
				startDate = df.format(DailyExecutionControler.startDate);
				runPeriod = String.valueOf((DailyExecutionControler.endDate.getTime() - DailyExecutionControler.startDate.getTime()) * 1.0 / 1000 / 60);
				totalSize = DailyExecutionControler.updateSize;
				// process=DailyExecution.currentPointer*1.0/DailyExecution.updateSize;
				// curPortfolioName=portfolioManager.get(DailyExecution.currentPortfolioID).getName();
				// curPortfolioID=DailyExecution.currentPortfolioID;
				// curStartDate=df.format(DailyExecution.currentStartDate);
				// curRunPeriod=String.valueOf(((new
				// Date()).getTime()-DailyExecution.currentStartDate.getTime())*1.0/1000/60);
			} catch (RuntimeException e) {
				// TODO Auto-generated catch block
			}
		}*/
		initialState();
		isUpdatingDB = DailyUpdateControler.isUpdating;
		
		isFastUpdating = FastDailyUpdateControler.isUpdating;
		
		this.setIsUpdating(isUpdatingDB || isFastUpdating);

		if (isUpdatingDB) {
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				updateDBStartDate = df.format(DailyUpdateControler.startDate)+"(Historical Daily Data Updating)";
				updateDBPeriod = String.valueOf(((new Date()).getTime() - DailyUpdateControler.startDate.getTime()) * 1.0 / 1000 / 60);
				currentCount=DailyUpdateControler.current_count;
				totalCount=DailyUpdateControler.total_count;
				state=DailyUpdateControler.state;
				checkProcess();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}else if(isFastUpdating){
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				updateDBStartDate = df.format(FastDailyUpdateControler.startDate)+"(End Of Date Daily Data Updating)";
				updateDBPeriod = String.valueOf(((new Date()).getTime() - FastDailyUpdateControler.startDate.getTime()) * 1.0 / 1000 / 60);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		else {
			try {
				updateDBPeriod = String.valueOf((DailyUpdateControler.endDate.getTime() - DailyUpdateControler.startDate.getTime()) * 1.0 / 1000 / 60);
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				updateDBStartDate = df.format(new Date())+"(server start time or last update time)";
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}

		File root = null;
		String path = ContextHolder.getServletPath()+"/ExecutorEngine";

		root = new File(path);

		File[] files = root.listFiles();
		logs = new ArrayList<Pair>();
		if(files!=null){
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				if (file.isFile() && file.getName().contains("dailyexecution")) {
					Pair p = new Pair(file.getName(), file.getName());
					logs.add(p);
				}
			}
		}
		

		return Action.SUCCESS;

	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public List<Triple> getExecuteThreads() {
		return executeThreads;
	}

	public void setExecuteThreads(List<Triple> executeThreads) {
		this.executeThreads = executeThreads;
	}

	public void setIsUpdating(Boolean isUpdating) {
		this.isUpdating = isUpdating;
	}

	public PortfolioManager getPortfolioManager() {
		return portfolioManager;
	}

	public Integer getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	public Double getProcess() {
		return process;
	}

	public void setProcess(Double process) {
		this.process = process;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getRunPeriod() {
		return runPeriod;
	}

	public void setRunPeriod(String runPeriod) {
		this.runPeriod = runPeriod;
	}

	public String getCurPortfolioName() {
		return curPortfolioName;
	}

	public void setCurPortfolioName(String curPortfolioName) {
		this.curPortfolioName = curPortfolioName;
	}

	public String getCurStartDate() {
		return curStartDate;
	}

	public void setCurStartDate(String curStartDate) {
		this.curStartDate = curStartDate;
	}

	public String getCurRunPeriod() {
		return curRunPeriod;
	}

	public void setCurRunPeriod(String curRunPeriod) {
		this.curRunPeriod = curRunPeriod;
	}

	public Boolean getIsUpdating() {
		return isUpdating;
	}

	public Long getCurPortfolioID() {
		return curPortfolioID;
	}

	public void setCurPortfolioID(Long curPortfolioID) {
		this.curPortfolioID = curPortfolioID;
	}

	public Boolean getIsUpdatingDB() {
		return isUpdatingDB;
	}

	public void setIsUpdatingDB(Boolean isUpdatingDB) {
		this.isUpdatingDB = isUpdatingDB;
	}

	public String getUpdateDBPeriod() {
		return updateDBPeriod;
	}

	public void setUpdateDBPeriod(String updateDBPeriod) {
		this.updateDBPeriod = updateDBPeriod;
	}

	public String getUpdateDBStartDate() {
		return updateDBStartDate;
	}

	public void setUpdateDBStartDate(String updateDBStartDate) {
		this.updateDBStartDate = updateDBStartDate;
	}

	public List<Pair> getLogs() {
		return logs;
	}

	public void setLogs(List<Pair> logs) {
		this.logs = logs;
	}

	public Boolean getIsFastUpdating() {
		return isFastUpdating;
	}

	public void setIsFastUpdating(Boolean isFastUpdating) {
		this.isFastUpdating = isFastUpdating;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String[] getStateList() {
		return stateList;
	}

	public void setStateList(String[] stateList) {
		this.stateList = stateList;
	}

	public String[] getProcessState() {
		return processState;
	}

	public void setProcessState(String[] processState) {
		this.processState = processState;
	}
}
