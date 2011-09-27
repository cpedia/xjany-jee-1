package com.lti.bean;


import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class Locales {
	
	private Locale current;

	public void setCurrent(Locale current) {
		this.current = current;
	}
	
	public Map<String, Locale> getLocales()
	{
		Map<String, Locale> locales = new Hashtable<String, Locale>();
		ResourceBundle bundle = ResourceBundle.getBundle("globalization", current);
		locales.put(bundle.getString("usen"), Locale.US);
		locales.put(bundle.getString("zhcn"), Locale.CHINA);
		return locales;
	}
}
