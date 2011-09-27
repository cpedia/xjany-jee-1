package com.lti.action.admin.group;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.type.PaginationSupport;
import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private PaginationSupport groups;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private GroupManager groupManager;
	
	
	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
	}

	@Override
	public String execute() throws Exception {
		
		groups=groupManager.getGroups(this.pageSize, this.startIndex);
		
		return Action.SUCCESS;

	}

	
	public int getStartIndex() {
	
		return startIndex;
	}


	
	public void setStartIndex(int startIndex) {
	
		this.startIndex = startIndex;
	}


	
	public int getPageSize() {
	
		return pageSize;
	}


	
	public void setPageSize(int pageSize) {
	
		this.pageSize = pageSize;
	}




	
	public PaginationSupport getGroups() {
	
		return groups;
	}

	
	public void setGroups(PaginationSupport groups) {
	
		this.groups = groups;
	}
	

	
	public void setStartIndex(Integer startIndex) {
	
		this.startIndex = startIndex;
	}


	
	public void setPageSize(Integer pageSize) {
	
		this.pageSize = pageSize;
	}


	

}
