package com.lti.action.admin.security;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.type.PaginationSupport;
import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.SecurityManager;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

import com.lti.service.bo.Security;

public class SearchAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SearchAction.class);
	
	private PaginationSupport securities;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private SecurityManager securityManager;
	
	private String key;
	

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
	}

	@Override
	public String execute() throws Exception {

		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Security.class);
		
		detachedCriteria.add(Restrictions.like("Symbol", "%"+key+"%"));
		
		securities=securityManager.findSecurities(detachedCriteria, pageSize, startIndex);
		
		if(securities!=null&&securities.getItems()!=null){
			for(int i=0;i<securities.getItems().size();i++){
				Security s=(Security) securities.getItems().get(i);
				//s.setStartDate(securityManager.getStartDate(s.getID()));
				//s.setEndDate(securityManager.getEndDate(s.getID()));
			}
		}
		
		securities.addParameter("key", key);
		
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




	
	public void setStartIndex(Integer startIndex) {
	
		this.startIndex = startIndex;
	}


	
	public void setPageSize(Integer pageSize) {
	
		this.pageSize = pageSize;
	}

	public PaginationSupport getSecurities() {
		return securities;
	}

	public void setSecurities(PaginationSupport securities) {
		this.securities = securities;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}


	

}
