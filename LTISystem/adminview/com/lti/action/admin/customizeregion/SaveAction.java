package com.lti.action.admin.customizeregion;

import java.util.ArrayList;
import java.util.List;

import com.lti.action.Action;
import com.lti.service.CustomizePageManager;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.GroupManager;
import com.lti.service.bo.CustomizePage;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.Group;
import com.lti.service.bo.Pages;
import com.lti.util.CustomizeUtil;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	
	private Long ID;
	
	private CustomizeRegion CRPage;
	
	private CustomizeRegionManager customizeRegionManager;
	
	private CustomizePageManager customizePageManager;
	
	private String action;
	
	private GroupManager groupManager;
	
	private List<Pages> pages;
	
	private List<Group> groups;
	
	private List<CustomizePage> cps;
	
	private List<CustomizePage> centerCPs;
	
	public List<CustomizePage> getCps() {
		return cps;
	}

	public void setCps(List<CustomizePage> cps) {
		this.cps = cps;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public void validate(){
		groups = groupManager.getGroups();
		cps = new ArrayList<CustomizePage>();
		centerCPs = new ArrayList<CustomizePage>();
		CustomizePage cp = new CustomizePage();
		cp.setID(null);
		cp.setName("No Content");
		cp.setCode(null);
		cp.setTemplate(null);
		cps.add(cp);
		cp = new CustomizePage();
		cp.setID(0l);
		cp.setName("Fixed");
		cp.setCode(null);
		cp.setTemplate(null);
		cps.add(cp);
		cps.addAll(customizePageManager.getCustomizePages());
		centerCPs.add(cp);
		centerCPs.addAll(customizePageManager.getCustomizePages());
		if(action != null && !action.equals("create") && ID == null){
			addFieldError("ID", "There is no such customize page!");
			return;
		}
	}
	
	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public CustomizeRegion getCRPage() {
		return CRPage;
	}

	public void setCRPage(CustomizeRegion page) {
		CRPage = page;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public void setCustomizePageManager(CustomizePageManager customizePageManager) {
		this.customizePageManager = customizePageManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}
	
	private void setRegionID(CustomizeRegion cr){
		if(cr.getEastRegionName()!=null && !cr.getEastRegionName().equalsIgnoreCase("No Content") && !cr.getEastRegionName().equalsIgnoreCase("Fixed")){
			cr.setEast(customizePageManager.get(cr.getEastRegionName()).getID());
		}
		else if(cr.getEastRegionName() != null && cr.getEastRegionName().equalsIgnoreCase("Fixed")){
			cr.setEast(0l);
		}
		else
		{
			cr.setEast(null);
		}
		if(cr.getWestRegionName()!=null && !cr.getWestRegionName().equalsIgnoreCase("No Content") && !cr.getWestRegionName().equalsIgnoreCase("Fixed")){
			cr.setWest(customizePageManager.get(cr.getWestRegionName()).getID());
		}
		else if(cr.getWestRegionName() != null && cr.getWestRegionName().equalsIgnoreCase("Fixed")){
			cr.setWest(0l);
		}
		else
		{
			cr.setWest(null);
		}
		if(cr.getSouthRegionName()!=null && !cr.getSouthRegionName().equalsIgnoreCase("No Content") && !cr.getSouthRegionName().equalsIgnoreCase("Fixed")){
			cr.setSouth(customizePageManager.get(cr.getSouthRegionName()).getID());
		}
		else if(cr.getSouthRegionName() != null && cr.getSouthRegionName().equalsIgnoreCase("Fixed")){
			cr.setSouth(0l);
		}
		else
		{
			cr.setSouth(null);
		}
		if(cr.getCenterRegionName()!=null && !cr.getCenterRegionName().equalsIgnoreCase("Fixed")){
			cr.setCenter(customizePageManager.get(cr.getCenterRegionName()).getID());
		}
		else
		{
			cr.setCenter(0l);
		}	
	}
	
	private void setRegionWidthContent(CustomizeRegion cr){
		Double d = cr.getCenterWidth();
		if(d != null && d != 0.0){
			cr.setCenterWidthStr(d * 100 + "%");
		}
		else
		{
			cr.setCenterWidthStr("0");
		}
		d = cr.getEastWidth();
		if(d != null && d != 0.0){
			cr.setEastWidthStr(d * 100 + "%");
		}
		else
		{
			cr.setEastWidthStr("0");
		}
		d = cr.getWestWidth();
		if(d != null && d != 0.0){
			cr.setWestWidthStr(d * 100 + "%");
		}
		else
		{
			cr.setWestWidthStr("0");
		}
		d = cr.getSouthHeight();
		if(d != null && d != 0.0){
			cr.setSouthHeightStr(d * 100 + "%");
		}
		else
		{
			cr.setSouthHeightStr("0");
		}
		
	}
	
	private void setRegionWidth(CustomizeRegion cr){
		Double d;
		//set center region width
		String widthStr = cr.getCenterWidthStr();
		if(widthStr != null && !widthStr.equals("")){
			d = StringUtil.percentageToDouble(widthStr);
			cr.setCenterWidth(d);
		}
		else
			cr.setCenterWidth(0.0);
		//set east region width
		widthStr = cr.getEastWidthStr();
		if(widthStr != null && !widthStr.equals("")){
			d = StringUtil.percentageToDouble(widthStr);
			cr.setEastWidth(d);
		}
		else
			cr.setEastWidth(0.0);
		//set south region width
		widthStr = cr.getSouthHeightStr();
		if(widthStr != null && !widthStr.equals("")){
			d = StringUtil.percentageToDouble(widthStr);
			cr.setSouthHeight(d);
		}
		else
			cr.setSouthHeight(0.0);
		//set west region width
		widthStr = cr.getWestWidthStr();
		if(widthStr != null && !widthStr.equals("")){
			d = StringUtil.percentageToDouble(widthStr);
			cr.setWestWidth(d);
		}
		else
			cr.setWestWidth(0.0);
	}

	public String View(){
		CRPage = customizeRegionManager.get(ID);
		CustomizeUtil.setRegionContentName(CRPage);
		setRegionWidthContent(CRPage);
		if(ID == 0){
			pages = customizeRegionManager.getPages();
			List<CustomizeRegion> crs = customizeRegionManager.getRegionCustomizes();
			for(int i = 0; i < crs.size(); i++){
				String pageName = crs.get(i).getPageName();
				//Pages P = customizeRegionManager.getPage(pageName);
				if(pages != null && !pageName.equals("New Page")){
					for(int j = 0; j < pages.size(); j++){
						Pages p = pages.get(j);
						if(p.getPageName().equals(pageName)){
							pages.remove(p);
							break;
						}
					}
				}
			}
		}
		return Action.SUCCESS;
	}
	
	public String Save(){
		setRegionID(CRPage);
		setRegionWidth(CRPage);
		CRPage.setID(ID);
		Boolean success = customizeRegionManager.update(CRPage);
		if(success == true)
			return Action.SUCCESS;
		else
			return Action.INPUT;
	}
	
	public String Create(){
		setRegionID(CRPage);
		setRegionWidth(CRPage);
		CRPage.setID(null);
		if(CRPage.getPageID() == null){
			action = null;
			return Action.INPUT;
		}
		CRPage.setPageName(customizeRegionManager.getPage(CRPage.getPageID()).getPageName());
		Long pageID = customizeRegionManager.add(CRPage);
		if(pageID > 0)
			return Action.SUCCESS;
		else
		{
			addFieldError("page name", "The " + CRPage.getPageName() + " has already existed.");
			addActionMessage("The " + CRPage.getPageName() + " has already existed.");
			action = null;
			return Action.INPUT;
		}
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<Pages> getPages() {
		return pages;
	}

	public void setPages(List<Pages> pages) {
		this.pages = pages;
	}

	public List<CustomizePage> getCenterCPs() {
		return centerCPs;
	}

	public void setCenterCPs(List<CustomizePage> centerCPs) {
		this.centerCPs = centerCPs;
	}
}
