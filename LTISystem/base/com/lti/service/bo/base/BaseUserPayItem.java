package com.lti.service.bo.base;

import java.io.Serializable;

public class BaseUserPayItem implements Serializable{

	protected java.lang.Long ID;

	protected java.lang.String itemName;
	
	protected java.lang.Long GroupID;

	public java.lang.Long getID() {
		return ID;
	}

	public void setID(java.lang.Long id) {
		ID = id;
	}

	public java.lang.String getItemName() {
		return itemName;
	}

	public void setItemName(java.lang.String itemName) {
		this.itemName = itemName;
	}

	public java.lang.Long getGroupID() {
		return GroupID;
	}

	public void setGroupID(java.lang.Long groupID) {
		GroupID = groupID;
	}
}
