package com.lti.action.admin.regioncustomize;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.type.PaginationSupport;
import com.lti.service.RegionCustomizeManager;
import com.lti.service.bo.RegionCustomize;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action{
	private static final long serialVersionUID = 1L;
	
	private Integer pageSize;
	private Integer startIndex;
	private PaginationSupport rcs;
	private RegionCustomizeManager regionCustomizeManager;
	
	public void validate(){
		
		if(this.startIndex==null)this.startIndex=0;
		
		if(this.pageSize==null||this.pageSize<=0)this.pageSize=Configuration.DefaultPageSize;
		
	}
	
	public String execute() throws Exception{
		rcs=regionCustomizeManager.getRegionCustomize(this.pageSize, this.startIndex);
		return Action.SUCCESS;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public RegionCustomizeManager getRegionCustomizeManager() {
		return regionCustomizeManager;
	}
	public void setRegionCustomizeManager(
			RegionCustomizeManager regionCustomizeManager) {
		this.regionCustomizeManager = regionCustomizeManager;
	}

	public PaginationSupport getRcs() {
		return rcs;
	}

	public void setRcs(PaginationSupport rcs) {
		this.rcs = rcs;
	}
	
	
	
}