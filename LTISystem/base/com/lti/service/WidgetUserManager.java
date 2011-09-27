package com.lti.service;

import java.util.List;

import com.lti.service.bo.WidgetUser;

public interface WidgetUserManager {
	
	public void save(WidgetUser widgetUser);

	public void saveOrUpdate(WidgetUser widgetUser);

	public void update(WidgetUser widgetUser);

	public void remove(long id);
	
	public List<WidgetUser> getWidgetUserList();
	
	public WidgetUser getWidgetUser(long id);
	
	public Integer checkWidgetUser(String email);

}
