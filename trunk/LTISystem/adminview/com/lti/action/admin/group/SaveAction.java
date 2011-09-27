package com.lti.action.admin.group;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.GroupRole;
import com.lti.service.bo.Role;
import com.opensymphony.xwork2.ActionSupport;

public class SaveAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(SaveAction.class);

	private String action;

	private Long ID;

	private String name;

	private String description;

	private String title;

	private TreeMap<String, Boolean> roles;

	private GroupManager groupManager;

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	public void validate() {

		if (this.action == null || this.action.equals(""))
			this.action = ACTION_CREATE;

		if (this.action.equalsIgnoreCase(ACTION_SAVE)) {
			if (name == null || name.equals("")) {
				addFieldError("name", "Name is not validate!");
				return;
			} else {
				Group g = groupManager.get(name);

				if (g != null) {
					addFieldError("name", "Name is not validate!");
					return;
				}

			}
		}

		else if (this.action.equalsIgnoreCase(ACTION_UPDATE)) {

			if (ID == null) {
				addFieldError("ID", "ID is not validate!");
			} else {
				Group g = groupManager.get(ID);

				if (g == null)
					addFieldError("ID", "ID is not validate!");
			}

			if (name == null || name.equals(""))
				addFieldError("name", "Name is not validate!");

		}

		else if (this.action.equalsIgnoreCase(ACTION_DELETE)) {

			if (ID == null) {
				addFieldError("ID", "ID is not validate!");
			} else {
				Group g = groupManager.get(ID);

				if (g == null)
					addFieldError("ID", "ID is not validate!");
			}

		}

		else if (this.action.equalsIgnoreCase(ACTION_VIEW)) {

			if (ID == null) {
				addFieldError("ID", "ID is not validate!");
			} else {
				Group g = groupManager.get(ID);

				if (g == null)
					addFieldError("ID", "ID is not validate!");
			}

		} else if (this.action.equalsIgnoreCase(ACTION_CREATE)) {

		}

		else {

			addFieldError("action", "Request is not validate!");

		}

	}

	@Override
	public String execute() throws Exception {

		Group group = new Group();

		group.setID(ID);

		group.setName(name);

		group.setDescription(description);

		List<Role> roleList = groupManager.getAllRoles();

		if (this.action.equalsIgnoreCase(ACTION_SAVE)) {

			group.setID(null);

			groupManager.save(group);

			ID = group.getID();

			title = "Group: " + name;

			action = ACTION_UPDATE;

			if (roleList != null && roles != null) {
				for (int i = 0; i < roleList.size(); i++) {
					Role r = roleList.get(i);
					Object obj = roles.get(r.getName());
					Boolean b = Boolean.valueOf(obj.toString());
					if (b != null && b == true) {
						groupManager.addGroupRole(ID, r.getID());
					}
				}

			}

			addActionMessage("Create Successfully!");

			return Action.INPUT;

		}

		else if (this.action.equalsIgnoreCase(ACTION_UPDATE)) {

			groupManager.update(group);

			action = ACTION_UPDATE;

			title = "Group: " + name;

			if (roleList != null && roles != null) {
				for (int i = 0; i < roleList.size(); i++) {
					Role r = roleList.get(i);
					Object obj = roles.get(r.getName());
					Boolean b = Boolean.valueOf(obj.toString());
					if (b != null && b == false) {
						groupManager.deleteGroupRole(ID, r.getID());
					}
					if (b != null && b == true) {
						groupManager.deleteGroupRole(ID, r.getID(), 0);
						groupManager.addGroupRole(ID, r.getID());
					}
				}
			}

			roles = new TreeMap<String, Boolean>();

			if (roleList != null && roles != null) {
				for (int i = 0; i < roleList.size(); i++) {
					Role r = roleList.get(i);
					roles.put(r.getName(), new Boolean(false));
				}
			}

			List<Role> existsRoles = groupManager.getRolesByGroupID(ID);

			if (existsRoles != null && roles != null) {
				for (int i = 0; i < existsRoles.size(); i++) {
					Role r = existsRoles.get(i);
					roles.put(r.getName(), true);
				}
			}
			addActionMessage("Update Successfully!");

			return Action.INPUT;

		}

		else if (this.action.equalsIgnoreCase(ACTION_DELETE)) {

			groupManager.delete(ID);

			action = ACTION_CREATE;

			if (roleList != null) {
				for (int i = 0; i < roleList.size(); i++) {
					Role r = roleList.get(i);
					groupManager.deleteGroupRole(ID, r.getID());
				}
			}

			addActionMessage("Delete Successfully!");

			return Action.SUCCESS;

		}

		else if (this.action.equalsIgnoreCase(ACTION_VIEW)) {

			group = groupManager.get(ID);

			ID = group.getID();

			name = group.getName();

			description = group.getDescription();

			roles = new TreeMap<String, Boolean>();

			if (roleList != null && roles != null) {
				for (int i = 0; i < roleList.size(); i++) {
					Role r = roleList.get(i);
					roles.put(r.getName(), new Boolean(false));
				}
			}

			List<Role> existingRoles = groupManager.getRolesByGroupID(ID);

			if (existingRoles != null && roles != null) {
				for (int i = 0; i < existingRoles.size(); i++) {
					Role r = existingRoles.get(i);
					roles.put(r.getName(), true);
				}
			}

			title = "Group: " + name;

			action = ACTION_UPDATE;

			return Action.INPUT;

		} else if (this.action.equalsIgnoreCase(ACTION_CREATE)) {

			action = ACTION_SAVE;

			title = "New Group";

			roles = new TreeMap<String, Boolean>();

			if (roleList != null && roles != null) {
				for (int i = 0; i < roleList.size(); i++) {
					Role r = roleList.get(i);
					roles.put(r.getName(), new Boolean(false));
				}
			}

			return Action.INPUT;
		}

		return Action.ERROR;

	}

	public String getAction() {

		return action;
	}

	public void setAction(String action) {

		this.action = action;
	}

	public Long getID() {

		return ID;
	}

	public void setID(Long id) {

		ID = id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public String getDescription() {

		return description;
	}

	public void setDescription(String description) {

		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public TreeMap<String, Boolean> getRoles() {
		return roles;
	}

	public void setRoles(TreeMap<String, Boolean> roles) {
		this.roles = roles;
	}
}
