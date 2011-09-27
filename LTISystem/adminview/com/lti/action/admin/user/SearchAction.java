package com.lti.action.admin.user;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.type.PaginationSupport;
import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class SearchAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SearchAction.class);
	
	private PaginationSupport groups;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private String key;
	
	private GroupManager groupManager;


	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
		if(key==null||key.equals(""))addFieldError("key","Key word is not validate!");
		
	}

	@Override
	public String execute() throws Exception {
		
		groups=groupManager.getGroups(this.key,this.pageSize, this.startIndex);

		groups.addParameter("key", key);
		
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

	public String getKey() {
		
		return key;
	}

	
	public void setKey(String key) {
	
		this.key = key;
	}
	

}
