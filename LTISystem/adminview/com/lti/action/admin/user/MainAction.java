package com.lti.action.admin.user;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.type.PaginationSupport;
import com.lti.action.Action;
import com.lti.service.UserManager;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);
	
	private PaginationSupport users;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private UserManager userManager;
	
	
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
	}

	@Override
	public String execute() throws Exception {
		
		users=userManager.getUsers(this.pageSize, this.startIndex);
		
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




	
	public PaginationSupport getUsers() {
	
		return users;
	}

	
	public void setUsers(PaginationSupport users) {
	
		this.users = users;
	}
	

	
	public void setStartIndex(Integer startIndex) {
	
		this.startIndex = startIndex;
	}


	
	public void setPageSize(Integer pageSize) {
	
		this.pageSize = pageSize;
	}


	

}
