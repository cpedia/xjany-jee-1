package com.lti.action.news;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Strategy;
import com.lti.system.SearchConfiguration;
import com.lti.type.ApplicationKey;
import com.lti.type.SearchSite;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {
	private static final long serialVersionUID = 1L;
	
	static Log log = LogFactory.getLog(MainAction.class);
	
	private Long ID;	//record the id of portfolio or Strategy
	
	private PortfolioManager portfolioManager;
	private StrategyManager strategyManager;
	
	private String Keyword;
	private String AppKey;		//google application key
	private List<SearchSite> SearchSite;	//record the address of the Search Sites 
	private String searchContent;
	
	public String getAppKey() {
		return AppKey;
	}

	public void setAppKey(String appKey) {
		AppKey = appKey;
	}

	public List<SearchSite> getSearchSite() {
		return SearchSite;
	}

	public void setSearchSite(List<SearchSite> searchSite) {
		SearchSite = searchSite;
	}

	public String getSearchContent() {
		return searchContent;
	}

	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}

	public String getKeyword() {
		return Keyword;
	}

	public void setKeyword(String keyword) {
		Keyword = keyword;
	}


	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

	public void validate(){
		if (ID==null) ID = 0l;
	}
	
	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}
	
	private void removeTrashKeywords(List<String> keywordList, List<String> trash){
		for(int i = 0; i < trash.size(); i++){
			String w = trash.get(i);
			for(int j = 0; j < keywordList.size(); j++){
				String keyword = keywordList.get(j);
				if(w.equalsIgnoreCase(keyword)){
					keywordList.remove(keyword);
				}
			}
		}
		removeURL(keywordList);

	}
	
//	public List<String> generateKeyword(Portfolio p){
//		List<String> keywordList = p.getKeywords();
//		if(keywordList==null || keywordList.size() == 0){
//			List<SecurityItem> securities = p.getSecurities();
//			if(keywordList == null && securities != null)
//				keywordList = new ArrayList<String>();
//			for(int i = 0; i<securities.size(); i++){
//				//tring securityAndSymbol = securities.get(i).getSecurityName() + " " + securities.get(i).getSymbol();
//				String securityName = securities.get(i).getSecurityName();
//				if(!keywordList.contains(securityName))
//					keywordList.add(securities.get(i).getSecurityName());
//			}
//		}
//		else if(p.getIsOriginalPortfolio()==false){
//			List<SecurityItem> securities = p.getSecurities();
//			if(keywordList == null && securities != null)
//				keywordList = new ArrayList<String>();
//			for(int i = 0; i<securities.size(); i++){
//				//tring securityAndSymbol = securities.get(i).getSecurityName() + " " + securities.get(i).getSymbol();
//				String securityName = securities.get(i).getSecurityName();
//				if(!keywordList.contains(securityName))
//					keywordList.add(securities.get(i).getSecurityName());
//			}
//		}
//		List<String> trash = p.getTrashykeywords();
//		if(trash == null){
//			trash = new ArrayList<String>();
//		}
//		trash.add("cash");
//		trash.add("wikipedia");
//		trash.add("amazon");
//		removeTrashKeywords(keywordList, trash);
//		return keywordList;
//	}
//	
//	public List<String> generateKeyword(Strategy s){
//		List<String> keywordlist=s.getKeywords();
//		if(keywordlist==null||keywordlist.size()==0)
//		{
//			String description = s.getReference();
//			keywordlist = com.lti.util.TermExtraction.TermExtraction.getTags("LTISystem", description);
//		}
//		List<String> trash = s.getTrashykeywords();
//		if(trash == null){
//			trash = new ArrayList<String>();
//		}
//		trash.add("cash");
//		trash.add("wikipedia");
//		trash.add("amazon");
//		trash.add("nbsp");
//		removeTrashKeywords(keywordlist, trash);
//		return keywordlist;
//
//	}
	
	private boolean isUrl(String url){
		String regx = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$";
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(url);
		return m.find();
	}
	
	private void removeURL(List<String> keywordList){
		if(keywordList!=null && keywordList.size()!=0){
			for(int i = 0; i < keywordList.size(); i++){
				String keyword = keywordList.get(i);
				if(isUrl(keyword)==true){
					System.out.println("get a url:" + keyword);
					keywordList.remove(keyword);
				}
			}
		}
	}
	
	private String OrganiseKeywords(List<String> keywords, String Symbol){
		String keyword="";
		if(keywords.get(0)!=null && !keywords.get(0).equals(""))
			keyword = "\"" + keywords.get(0) + "\"";
		for(int i = 1; i<keywords.size(); i++){
			if(keywords.get(i)!=null && !keywords.get(i).equals(""))
				keyword += (Symbol + " \"" + keywords.get(i) + "\" ");
		}
		keyword = Replace(keyword);
		System.out.println("keyword string: " + keyword);
		return keyword;
	}
	
	private String Replace(String s){
		if(s.length()>0){
			String s1 = s.replaceAll("'", "\\\\'");
			return s1;
		}
		else
			return null;
	}

	private void init(){
	
		SearchConfiguration s=SearchConfiguration.getInstance();
		
		List<ApplicationKey> appkeylist = new ArrayList<ApplicationKey>();
		appkeylist = s.getApplicationKeys();
		if(appkeylist!=null && appkeylist.size()!=0){
			AppKey = appkeylist.get(0).getApplicationKey();
		}
		
		List<SearchSite> siteList = new ArrayList<SearchSite>();
		siteList = s.getSearchSites();
		if(siteList!=null && siteList.size()!=0){
			SearchSite = new ArrayList<SearchSite>();
			for(int i = 0; i < siteList.size(); i++){
				SearchSite.add(siteList.get(i));
			}
		}
	}
//	public String PortfolioNews() throws Exception{
//		init();
//		Portfolio portfolio;
//		try {
//			portfolio = portfolioManager.get(ID);
//			List<String> keywords = generateKeyword(portfolio);
//			if(keywords!=null)
//				Keyword = OrganiseKeywords(keywords, "OR");
//			else
//				Keyword = null;
//		} catch (Exception e) {
//			// TODO: handle exception
//			log.warn("couldn't get the portfolio");
//			addFieldError("portfolio", "No such portfolio!");
//			Keyword = null;
//		}
//		
//		return Action.SUCCESS;
//	}
	
//	public String StrategyNews() throws Exception{
//		init();
//		Strategy strategy;
//		try{
//			strategy=strategyManager.get(ID);
//			List<String> keywords=generateKeyword(strategy);
//			if(strategy.getName()!=null){
//				keywords.add(strategy.getName());
//			}
//			Keyword=OrganiseKeywords(keywords,"OR");
//		}catch (Exception e)
//		{
//			log.warn("could not get strategy");
//			this.addFieldError("strategy", "No such strategy!");
//			Keyword=null;
//		}
//		return Action.SUCCESS;
//	}
}
