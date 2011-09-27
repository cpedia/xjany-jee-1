package com.lti.action.admin.stock;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.jobscheduler.DailyUpdateControler;
import com.lti.jobscheduler.FinanStateControler;
import com.lti.jobscheduler.FinanYearlyStateControler;
import com.lti.service.PortfolioManager;
import com.lti.system.ContextHolder;
import com.lti.type.LongString;
import com.lti.type.Pair;
import com.lti.type.Triple;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ActionContext;

public class ExecutionFinanAction extends ActionSupport implements Action{
	
	private static final long serialVersionUID = 1L;
	
	static Log log = LogFactory.getLog(ExecutionFinanAction.class);
	
	private List<LongString> executors;

	private String updateDBPeriod;
	private String updateDBStartDate;
	private Boolean isUpdating;
	private String startDate;
	private int stateSize=3;
	private int currentCount;
	private String[] stateList=null;
	private int totalCount;
	private String[] processState=null;
	private String state;
	
	/****For Yearly FinanceSatement************/
	private List<LongString> executorYs;
	private String updateYDBPeriod;
	private String updateYDBStartDate;
	private Boolean isYUpdating;
	private String startYDate;
	private int stateYSize=3;
	private int currentYCount;
	private String[] stateYList=null;
	private int totalYCount;
	private String[] processYState=null;
	private String stateY;
	
	public List<LongString> getExecutors() {
		return executors;
	}

	public void setExecutors(List<LongString> executors) {
		this.executors = executors;
	}
	
	@Override
	public String execute() throws Exception {
		
		HttpServletRequest request = ServletActionContext.getRequest();
		stateList = new String[this.stateSize];
		processState = new String[this.stateSize];
		for(int i=0;i<this.stateSize;++i)
			processState[i]="UnBegin";
		stateList[0]="Update Stocks Financial Statement";
		stateList[1]="Check Stocks Financial Statement";
		stateList[2]="Make up Stocks Financial Statement";
		request.setAttribute("stateList",stateList);
		
		/****For Yearly FinanceSatement************/
		stateYList = new String[this.stateYSize];
		processYState = new String[this.stateYSize];
		for(int i=0;i<this.stateYSize;++i)
			processYState[i]="UnBegin";
		stateYList[0]="Update Stocks Yearly Financial Statement";
		stateYList[1]="Check Stocks Yearly Financial Statement";
		stateYList[2]="Make up Stocks Yearly Financial Statement";
		
		isUpdating = FinanStateControler.isFSUpdating;
		isYUpdating = FinanYearlyStateControler.isYFSUpdating;
		this.setIsUpdating(isUpdating);
		this.setIsYUpdating(isYUpdating);
		Date stDate = new Date();
		if(!isUpdating){
			state="Nothing Process";
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd aaa hh:mm:ss",Locale.US);
			updateDBStartDate= df.format(stDate)+"(Financial Statement does not Update)";
			updateDBPeriod="0.0";
			currentCount=0;
			totalCount=0;
		}
		if (isUpdating) {
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd aaa hh:mm:ss",Locale.US);
				updateDBStartDate = df.format(FinanStateControler.startDate)+"(Financial Statement Data Updating)";
				updateDBPeriod = String.valueOf(((new Date()).getTime() - FinanStateControler.startDate.getTime()) * 1.0 / 1000 / 60);
				currentCount=FinanStateControler.current_count;
				totalCount=FinanStateControler.total_count;
				state=FinanStateControler.state;
				ActionContext.getContext().put("currentCount", currentCount);
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
				ActionContext.getContext().put("processState", processState);
				this.setProcessState(processState);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if(!isYUpdating){
			stateY="Nothing Process";
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd aaa hh:mm:ss",Locale.US);
			updateYDBStartDate= df.format(stDate)+"(Yearly Financial Statement does not Update)";
			updateYDBPeriod="0.0";
			currentYCount=0;
			totalYCount=0;
		}
		if (isYUpdating) {
			try {
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd aaa hh:mm:ss",Locale.US);
				updateYDBStartDate = df.format(FinanYearlyStateControler.startYDate)+"(Financial Statement Data Updating)";
				updateYDBPeriod = String.valueOf(((new Date()).getTime() - FinanYearlyStateControler.startYDate.getTime()) * 1.0 / 1000 / 60);
				currentYCount=FinanYearlyStateControler.current_count;
				totalYCount=FinanYearlyStateControler.total_count;
				stateY=FinanYearlyStateControler.state;
				ActionContext.getContext().put("currentYCount", currentYCount);
				int currentY=0;
				int i;
				for(i=this.stateYSize-1;i>=0;--i){
					if(this.stateY.equals(stateYList[i]))
					{
						currentY=i;
						processYState[i]="Processing";
						break;
					}
				}
				for(i=0;i<currentY;++i)
					processYState[i]="Finish";
				for(i=currentY+1;i<this.stateYSize;++i)
					processYState[i]="UnBegin";
				ActionContext.getContext().put("processYState", processYState);
				this.setProcessYState(processYState);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Action.SUCCESS;
	}

	public Boolean getIsUpdating() {
		return isUpdating;
	}

	public void setIsUpdating(Boolean isUpdating) {
		this.isUpdating = isUpdating;
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

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
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

	public String[] getProcessState() {
		return processState;
	}

	public void setProcessState(String[] processState) {
		this.processState = processState;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<LongString> getExecutorYs() {
		return executorYs;
	}

	public void setExecutorYs(List<LongString> executorYs) {
		this.executorYs = executorYs;
	}

	public String getUpdateYDBPeriod() {
		return updateYDBPeriod;
	}

	public void setUpdateYDBPeriod(String updateYDBPeriod) {
		this.updateYDBPeriod = updateYDBPeriod;
	}

	public String getUpdateYDBStartDate() {
		return updateYDBStartDate;
	}

	public void setUpdateYDBStartDate(String updateYDBStartDate) {
		this.updateYDBStartDate = updateYDBStartDate;
	}

	public Boolean getIsYUpdating() {
		return isYUpdating;
	}

	public void setIsYUpdating(Boolean isYUpdating) {
		this.isYUpdating = isYUpdating;
	}

	public String getStartYDate() {
		return startYDate;
	}

	public void setStartYDate(String startYDate) {
		this.startYDate = startYDate;
	}

	public int getCurrentYCount() {
		return currentYCount;
	}

	public void setCurrentYCount(int currentYCount) {
		this.currentYCount = currentYCount;
	}

	public String[] getStateYList() {
		return stateYList;
	}

	public void setStateYList(String[] stateYList) {
		this.stateYList = stateYList;
	}

	public int getTotalYCount() {
		return totalYCount;
	}

	public void setTotalYCount(int totalYCount) {
		this.totalYCount = totalYCount;
	}

	public String[] getProcessYState() {
		return processYState;
	}

	public void setProcessYState(String[] processYState) {
		this.processYState = processYState;
	}

	public String getStateY() {
		return stateY;
	}

	public void setStateY(String stateY) {
		this.stateY = stateY;
	}
	
	

}
