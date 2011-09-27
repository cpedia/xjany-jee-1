package com.lti.action.admin.fundlist;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.lti.action.Action;
import com.lti.jobscheduler.FundStateControler;
import com.lti.jobscheduler.TickerSearchControler;
import com.lti.type.LongString;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class ExecutionFundTableAction extends ActionSupport implements Action{
	
	private List<LongString> executors;

	private String updateDBPeriod;
	private String updateDBStartDate;
	private Boolean isUpdating;
	private String startDate;
	private int stateSize=1;
	private int currentCount;
	private String[] stateList=null;
	private int totalCount;
	private String[] processState=null;
	private String state;
	
	/*******Ticker Search****************/
	private String updateDBTPeriod;
	private String updateDBTStartDate;
	private Boolean isTUpdating;
	private String startTDate;
	private int stateTSize=3;
	private int currentTCount;
	private String[] stateTList=null;
	private int totalTCount;
	private String[] processTState=null;
	private String stateT;
	
	@Override
	public String execute() throws Exception {
		stateList = new String[this.stateSize];
		processState = new String[this.stateSize];
		for(int i=0;i<this.stateSize;++i)
			processState[i]="UnBegin";
		stateList[0]="Execute FundTable List";
		
		/*******Ticker Search****************/
		stateTList = new String[this.stateTSize];
		processTState = new String[this.stateTSize];
		for(int i=0;i<this.stateTSize;++i)
			processTState[i]="UnBegin";
		stateTList[0]="Execute Ticker Search";
		stateTList[1]="Update the plan's description by the tickers";
		stateTList[2]="Write the Ticker Search Report";
		
		isUpdating = FundStateControler.isFSUpdating;
		this.setIsUpdating(isUpdating);
		isTUpdating = TickerSearchControler.isTSUpdating;
		this.setIsTUpdating(isTUpdating);
		Date stDate = new Date();
		if(!isUpdating){
			state="Nothing Process";
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd aaa hh:mm:ss",Locale.US);
			updateDBStartDate= df.format(stDate)+"(Fundtable List Statement does not execute)";
			updateDBPeriod="0.0";
			currentCount=0;
			totalCount=0;
		}
		if (isUpdating) {
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd aaa hh:mm:ss",Locale.US);
				updateDBStartDate = df.format(FundStateControler.startDate)+"(Fundtable List Statement Executing)";
				updateDBPeriod = String.valueOf(((new Date()).getTime() - FundStateControler.startDate.getTime()) * 1.0 / 1000 / 60);
				currentCount=FundStateControler.current_count;
				totalCount=FundStateControler.total_count;
				state=FundStateControler.state;
				if(state.trim().equals(""))state="Nothing Process";
				//ActionContext.getContext().put("currentCount", currentCount);
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
				//ActionContext.getContext().put("processState", processState);
				this.setProcessState(processState);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if(!isTUpdating){
			stateT="Nothing Process";
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd aaa hh:mm:ss",Locale.US);
			updateDBTStartDate= df.format(stDate)+"(Ticker Search does not execute)";
			updateDBTPeriod="0.0";
			currentTCount=0;
			totalTCount=0;
		}
		if (isTUpdating) {
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd aaa hh:mm:ss",Locale.US);
				updateDBTStartDate = df.format(TickerSearchControler.startTDate)+"(Ticker Search Statement Executing)";
				updateDBTPeriod = String.valueOf(((new Date()).getTime() - TickerSearchControler.startTDate.getTime()) * 1.0 / 1000 / 60);
				currentTCount=TickerSearchControler.current_count;
				totalTCount=TickerSearchControler.total_count;
				stateT=TickerSearchControler.state;
				if(stateT.trim().equals(""))stateT="Nothing Process";
				//ActionContext.getContext().put("currentTCount", currentTCount);
				int current=0;
				int i;
				for(i=this.stateTSize-1;i>=0;--i){
					if(this.stateT.equals(stateTList[i]))
					{
						current=i;
						processTState[i]="Processing";
						break;
					}
				}
				for(i=0;i<current;++i)
					processTState[i]="Finish";
				for(i=current+1;i<this.stateTSize;++i)
					processTState[i]="UnBegin";
				//ActionContext.getContext().put("processTState", processTState);
				this.setProcessTState(processTState);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Action.SUCCESS;
	}

	public List<LongString> getExecutors() {
		return executors;
	}

	public void setExecutors(List<LongString> executors) {
		this.executors = executors;
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

	public Boolean getIsUpdating() {
		return isUpdating;
	}

	public void setIsUpdating(Boolean isUpdating) {
		this.isUpdating = isUpdating;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public int getStateSize() {
		return stateSize;
	}

	public void setStateSize(int stateSize) {
		this.stateSize = stateSize;
	}

	public int getCurrentCount() {
		return currentCount;
	}

	public void setCurrentCount(int currentCount) {
		this.currentCount = currentCount;
	}

	public String[] getStateList() {
		return stateList;
	}

	public void setStateList(String[] stateList) {
		this.stateList = stateList;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public String[] getProcessState() {
		return processState;
	}

	public void setProcessState(String[] processState) {
		this.processState = processState;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUpdateDBTPeriod() {
		return updateDBTPeriod;
	}

	public void setUpdateDBTPeriod(String updateDBTPeriod) {
		this.updateDBTPeriod = updateDBTPeriod;
	}

	public String getUpdateDBTStartDate() {
		return updateDBTStartDate;
	}

	public void setUpdateDBTStartDate(String updateDBTStartDate) {
		this.updateDBTStartDate = updateDBTStartDate;
	}

	public Boolean getIsTUpdating() {
		return isTUpdating;
	}

	public void setIsTUpdating(Boolean isTUpdating) {
		this.isTUpdating = isTUpdating;
	}

	public String getStartTDate() {
		return startTDate;
	}

	public void setStartTDate(String startTDate) {
		this.startTDate = startTDate;
	}

	public int getStateTSize() {
		return stateTSize;
	}

	public void setStateTSize(int stateTSize) {
		this.stateTSize = stateTSize;
	}

	public int getCurrentTCount() {
		return currentTCount;
	}

	public void setCurrentTCount(int currentTCount) {
		this.currentTCount = currentTCount;
	}

	public String[] getStateTList() {
		return stateTList;
	}

	public void setStateTList(String[] stateTList) {
		this.stateTList = stateTList;
	}

	public int getTotalTCount() {
		return totalTCount;
	}

	public void setTotalTCount(int totalTCount) {
		this.totalTCount = totalTCount;
	}

	public String[] getProcessTState() {
		return processTState;
	}

	public void setProcessTState(String[] processTState) {
		this.processTState = processTState;
	}

	public String getStateT() {
		return stateT;
	}

	public void setStateT(String stateT) {
		this.stateT = stateT;
	}

}
