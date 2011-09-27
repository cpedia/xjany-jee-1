package com.lti.action.security;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.action.Action;
import com.lti.service.AssetClassManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.system.Configuration;
import com.lti.type.PaginationSupport;
import com.opensymphony.xwork2.ActionSupport;


public class SearchAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SearchAction.class);
	
	private PaginationSupport securities;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private SecurityManager securityManager;
	
	private AssetClassManager assetClassManager;
	
	private List<String> Classes;
	
	private List<String> Types;
	
	private String key;
	
	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
	}
	
	@Override
	public String execute() throws Exception {
		DetachedCriteria detachedCriteria=DetachedCriteria.forClass(Security.class);
		
		detachedCriteria.add(Restrictions.like("Symbol", "%"+key+"%"));
		
		securities=securityManager.findSecurities(detachedCriteria, pageSize, startIndex);
		
		securities.addParameter("key", key);
		
		Classes=new ArrayList<String>();
		
		Types=new ArrayList<String>();
		
		if(securities!=null&&securities.getItems()!=null){
			for(int i=0;i<securities.getItems().size();i++){
				Security s=(Security) securities.getItems().get(i);
				//s.setStartDate(securityManager.getStartDate(s.getID()));
				//s.setEndDate(securityManager.getEndDate(s.getID()));
				if(s.getClassID() != null && assetClassManager.get(s.getClassID())!=null)
					Classes.add(assetClassManager.get(s.getClassID()).getName());
				else
					Classes.add("No Class");
				if(s.getSecurityType()!=null&&securityManager.getSecurityTypeByID(s.getSecurityType())!=null)
					Types.add(securityManager.getSecurityTypeByID(s.getSecurityType()).getName());
				else
					Types.add("No Type");
			}
		}
		
		return Action.SUCCESS;

	}

	public PaginationSupport getSecurities() {
		return securities;
	}

	public void setSecurities(PaginationSupport securities) {
		this.securities = securities;
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

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getClasses() {
		return Classes;
	}

	public void setClasses(List<String> classes) {
		Classes = classes;
	}

	public List<String> getTypes() {
		return Types;
	}

	public void setTypes(List<String> types) {
		Types = types;
	}

	public void setAssetClassManager(AssetClassManager assetClassManager) {
		this.assetClassManager = assetClassManager;
	}

}
