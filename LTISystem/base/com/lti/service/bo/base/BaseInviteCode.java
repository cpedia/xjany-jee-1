/**
 * 
 */
package com.lti.service.bo.base;

import java.io.Serializable;

/**
 * @author CCD
 *
 */
public abstract class BaseInviteCode implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private java.lang.Long ID;
	private java.lang.String InviteCode;
	private java.lang.String MiddleMan;
	private java.util.Date date;
	private java.lang.Double PriceItem;
	private java.lang.String Type;
	private java.lang.String Url;
	private java.lang.String InviteName;
	public java.lang.String getUrl() {
		return Url;
	}
	public void setUrl(java.lang.String url) {
		Url = url;
	}
	public java.lang.String getInviteName() {
		return InviteName;
	}
	public void setInviteName(java.lang.String inviteName) {
		InviteName = inviteName;
	}
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long iD) {
		ID = iD;
	}
	public java.lang.String getInviteCode() {
		return InviteCode;
	}
	public void setInviteCode(java.lang.String inviteCode) {
		InviteCode = inviteCode;
	}
	public java.lang.String getMiddleMan() {
		return MiddleMan;
	}
	public void setMiddleMan(java.lang.String middleMan) {
		MiddleMan = middleMan;
	}
	public java.util.Date getDate() {
		return date;
	}
	public void setDate(java.util.Date date) {
		this.date = date;
	}
	public java.lang.Double getPriceItem() {
		return PriceItem;
	}
	public void setPriceItem(java.lang.Double priceItem) {
		PriceItem = priceItem;
	}
	public java.lang.String getType() {
		return Type;
	}
	public void setType(java.lang.String type) {
		Type = type;
	}
	
	
	
	
	
	
}
