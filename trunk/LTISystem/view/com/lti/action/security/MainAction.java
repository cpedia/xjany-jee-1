package com.lti.action.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.AssetClassManager;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.SecurityManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Security;
import com.lti.system.Configuration;
import com.lti.type.PaginationSupport;
import com.lti.util.CustomizeUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;


public class MainAction extends ActionSupport implements Action {

	static Log log = LogFactory.getLog(MainAction.class);
	
	private static final long serialVersionUID = 1L;
	
	private PaginationSupport securities;

	private Integer startIndex;
	
	private Integer pageSize;
	
	private SecurityManager securityManager;
	
	private AssetClassManager assetClassManager;
	
	private UserManager userManager;
	
	private List<String> Classes;
	
	private List<String> Types;
	
	private CustomizeRegionManager customizeRegionManager;
	
	private CustomizeRegion customizeRegion;
	

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
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

	public SecurityManager getSecurityManager() {
		return securityManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)
			this.pageSize=Configuration.DefaultPageSize;
		
	}
	
	@Override
	public String execute() throws Exception {
		Classes=new ArrayList<String>();
		Types=new ArrayList<String>();
		securities=securityManager.getPureSecurities(this.pageSize, this.startIndex);
		
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
		
		customizeRegion = customizeRegionManager.get(CustomizeUtil.SECURITY_MAIN);
		
		Long userID = userManager.getLoginUser().getID();
		
		CustomizeUtil.setRegion(customizeRegion, userID);
		
		return Action.SUCCESS;

	}

	public AssetClassManager getAssetClassManager() {
		return assetClassManager;
	}

	public void setAssetClassManager(AssetClassManager assetClassManager) {
		this.assetClassManager = assetClassManager;
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


}
