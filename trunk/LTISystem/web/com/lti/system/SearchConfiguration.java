package com.lti.system;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import com.lti.type.ApplicationKey;
import com.lti.type.SearchSite;

public class SearchConfiguration {

	private List<ApplicationKey> applicationKeys;
	private List<SearchSite> searchSites;

	private static SearchConfiguration instance;
	
	public static SearchConfiguration getInstance() {
		if(instance==null)load();

		return instance;
	}

	public List<ApplicationKey> getApplicationKeys() {
		return applicationKeys;
	}

	public void setApplicationKeys(List<ApplicationKey> applicationKeys) {
		this.applicationKeys = applicationKeys;
	}

	public List<SearchSite> getSearchSites() {
		return searchSites;
	}

	public void setSearchSites(List<SearchSite> searchSites) {
		this.searchSites = searchSites;
	}

	public SearchConfiguration() {
		super();
		
	}

	public static void load() {
		try {
			Digester digester = new Digester();

			digester.setValidating(false);

			digester.addObjectCreate("searchconfiguration", SearchConfiguration.class);

			digester.addObjectCreate("searchconfiguration/applicationkey", ApplicationKey.class);
			digester.addBeanPropertySetter("searchconfiguration/applicationkey/applicationKey", "applicationKey");
			digester.addBeanPropertySetter("searchconfiguration/applicationkey/site", "site");
			digester.addSetNext("searchconfiguration/applicationkey", "addApplicationKey");

			
			digester.addObjectCreate("searchconfiguration/searchsite", SearchSite.class);
			digester.addBeanPropertySetter("searchconfiguration/searchsite/name", "name");
			digester.addBeanPropertySetter("searchconfiguration/searchsite/address", "address");
			digester.addSetNext("searchconfiguration/searchsite", "addSearchSite");
			
			
			File input = new File(ContextHolder.getServletPath()+"/search-configuration.xml");
			instance = (SearchConfiguration) digester.parse(input);

			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addApplicationKey(ApplicationKey ap) {
		if(applicationKeys==null)applicationKeys = new ArrayList<ApplicationKey>();
		
		applicationKeys.add(ap);
	}

	public void addSearchSite(SearchSite ss) {
		if(searchSites==null)searchSites = new ArrayList<SearchSite>();
		searchSites.add(ss);
	}
}
