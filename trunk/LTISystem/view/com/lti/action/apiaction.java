package com.lti.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONObject;

import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.service.UserManager;
import com.lti.service.WidgetUserManager;
import com.lti.service.bo.User;
import com.lti.service.bo.WidgetUser;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class apiaction {

	private String reqparam;
	private String email;
	private String website;
	private String message;
	WidgetUserManager wingetUserManager = ContextHolder.getWidgetUserManager();
	public String getEmail()
	{
		return email;
	}

	public String getMessage()
	{
		return message;
	}


	public void setMessage(String message)
	{
		this.message = message;
	}


	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getWebsite()
	{
		return website;
	}

	public void setWebsite(String website)
	{
		this.website = website;
	}

	public String getReqparam() {
		return reqparam;
	}

	public void setReqparam(String reqparam) {
		this.reqparam = reqparam;
	}

	public List<apiunit> getWidgets() {
		return widgets;
	}

	public void setWidgets(List<apiunit> widgets) {
		this.widgets = widgets;
	}

	private List<apiunit> widgets;

	private boolean hasTable = false;
	private boolean hasRss = false;
	
	private String json;

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String widget() {
		String[] units = reqparam.split("#");
		widgets = new ArrayList<apiunit>();
		for (String unit : units) {
			if (unit.equals(""))
				continue;
			String[] params = unit.split("\\|");
			apiunit au = new apiunit();
			au.setId(params[0]);
			au.setUrl(params[1]);
			int type = Integer.parseInt(params[2]);
			au.setType(type);
			if (type == apiunit.TYPE_RSS) {
				hasRss = true;
			} else if (type == apiunit.TYPE_TABLE) {
				hasTable = true;
			}
			au.setWidth(params[3]);
			au.setHeight(params[4]);
			try {
				au.setTitle(params[5]);
			} catch (Exception e) {
			}
			widgets.add(au);
		}
		return Action.SUCCESS;
	}

	public String widgetUser()
	{
		WidgetUser widgetUser = new WidgetUser();
		widgetUser.setEmail(email);
		widgetUser.setWebsite(website);
		wingetUserManager.save(widgetUser);
		message = "Thank you!";
		return Action.MESSAGE;
	}
	private String jsoncallback;
	public String getJsoncallback() {
		return jsoncallback;
	}

	public void setJsoncallback(String jsoncallback) {
		this.jsoncallback = jsoncallback;
	}

	public String checkLogin(){
		UserManager userManager = ContextHolder.getUserManager();
		User user = userManager.getLoginUser();
		JSONObject o = new JSONObject();
		if(user!=null&&user.getID()!=0){
			message = "success";
		}else message = "fail";
		o.accumulate("data", message);
		json = o.toString();
		return Action.JSON;
	}
	
	public String canCreatePlan(){
		boolean isAdmin = false;
		User user = ContextHolder.getUserManager().getLoginUser();
		if (user.getID() == Configuration.SUPER_USER_ID) {
			isAdmin = true;
		}
		
		MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
		if (!isAdmin) {
			int operationCode = mpm.canPlanCreate(user.getID(), true);
			if (operationCode == mpm.PERMISSION_LIMIT) {
//				ServletActionContext.getResponse().setStatus(500);
//				o.accumulate("msg", "You need to subscribe as a Expert User or a Pro User to get the permission of creating plans.");
				message = "1";
			} else if (operationCode == mpm.COUNT_LIMIT) {
//				ServletActionContext.getResponse().setStatus(500);
//				o.accumulate("msg", "You have reached the maximum number of your own plans. To create a new plan, please delete one of your plans in the 'My Portfolio' page, or subscribe as a higher level user to obtain the permission for more own plans.");
				message = "2";
			} else
				message = "0";
		}else{
			message = "0";
		}
		return Action.MESSAGE;
	}
	
	public boolean isHasTable() {
		return hasTable;
	}

	public void setHasTable(boolean hasTable) {
		this.hasTable = hasTable;
	}

	public boolean isHasRss() {
		return hasRss;
	}

	public void setHasRss(boolean hasRss) {
		this.hasRss = hasRss;
	}

}
