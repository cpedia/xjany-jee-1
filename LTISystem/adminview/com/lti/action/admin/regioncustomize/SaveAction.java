package com.lti.action.admin.regioncustomize;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

import com.lti.type.PaginationSupport;
import com.lti.action.Action;
import com.lti.service.RegionCustomizeManager;
import com.lti.service.CustomizePageManager;
import com.lti.service.GroupManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.RegionCustomize;
import com.lti.service.bo.CustomizePage;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action{
	private static final long serialVersionUID = 1L;
	private long ID;
	private String action;
	private boolean isCreate;
	private RegionCustomize regionCustomize;
	private RegionCustomizeManager regionCustomizeManager;
	private List<CustomizePage> cps;
	private List<String> positions;
	private List<Group> groups;
	private CustomizePageManager customizePageManager; 
	private GroupManager groupManager;
	public void validate()
	{
		groups=groupManager.getGroups();
		cps=customizePageManager.getCustomizePages();
		positions = new ArrayList<String>();
		positions.add("east");
		positions.add("west");
		positions.add("center");
		positions.add("south");
	}
	
	public String View(){
		regionCustomize=regionCustomizeManager.get(ID);
		return Action.SUCCESS;
	}
	public String Create(){
		isCreate=true;
		return Action.INPUT;
	}
	public String Save(){
		regionCustomize.setID(null);
		regionCustomizeManager.add(regionCustomize);
		if(regionCustomize.getID()!=null)
			return Action.SUCCESS;
		else
			return Action.INPUT;
	}
	public String Delete(){
		regionCustomizeManager.remove(ID);
		return Action.SUCCESS;
	}
	public String Update()
	{
		regionCustomize.setID(ID);
		regionCustomizeManager.update(regionCustomize);
		return Action.SUCCESS;
	}

	public long getID() {
		return ID;
	}

	public void setID(long id) {
		ID = id;
	}

	public RegionCustomize getRegionCustomize() {
		return regionCustomize;
	}

	public void setRegionCustomize(RegionCustomize regionCustomize) {
		this.regionCustomize = regionCustomize;
	}

	public RegionCustomizeManager getRegionCustomizeManager() {
		return regionCustomizeManager;
	}

	public void setRegionCustomizeManager(
			RegionCustomizeManager regionCustomizeManager) {
		this.regionCustomizeManager = regionCustomizeManager;
	}

	public List<CustomizePage> getCps() {
		return cps;
	}

	public void setCps(List<CustomizePage> cps) {
		this.cps = cps;
	}

	public CustomizePageManager getCustomizePageManager() {
		return customizePageManager;
	}

	public void setCustomizePageManager(CustomizePageManager customizePageManager) {
		this.customizePageManager = customizePageManager;
	}

	public boolean isCreate() {
		return isCreate;
	}

	public void setCreate(boolean isCreate) {
		this.isCreate = isCreate;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public List<String> getPositions() {
		return positions;
	}

	public void setPositions(List<String> positions) {
		this.positions = positions;
	}

	
}