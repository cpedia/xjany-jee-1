package com.lti.action.admin.group.strategy;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.bo.Group;
import com.lti.system.Configuration;
import com.lti.type.PaginationSupport;
import com.opensymphony.xwork2.ActionSupport;


public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private PaginationSupport strategies;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private Long groupID;
	
	private GroupManager groupManager;
	
	private String title;
	
	
	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
		if(groupID==null){
			
			addFieldError("groupID","Group ID is not validate!");
			
		}else{
			
			Group g=groupManager.get(groupID);
			
			if(g==null){
				addFieldError("ID","ID is not validate!");
				return;
			}
			title="Group: "+g.getName();
		}
		
	}
	@Override
	public String execute() throws Exception {
		
		strategies=groupManager.getStrategies(groupID, -1, pageSize, startIndex);
		
		return Action.SUCCESS;

	}

	public PaginationSupport getStrategies() {
		return strategies;
	}

	public void setStrategies(PaginationSupport strategies) {
		this.strategies = strategies;
	}

	public Integer getStartIndex() {
		return startIndex;
	}


	public void setStartIndex(Integer startIndex) {
		this.startIndex = startIndex;
	}


	public Integer getPageSize() {
		return pageSize;
	}


	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}


	public Long getGroupID() {
		return groupID;
	}


	public void setGroupID(Long groupID) {
		this.groupID = groupID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
