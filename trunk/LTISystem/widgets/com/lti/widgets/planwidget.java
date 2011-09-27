package com.lti.widgets;

import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONBuilder;

import com.lti.action.Action;
import com.lti.action.f401kaction;
import com.lti.service.AssetClassManager;
import com.lti.service.JforumManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.JforumPost;
import com.lti.service.bo.JforumPostText;
import com.lti.service.bo.JforumTopics;
import com.lti.service.bo.PlanScore;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDownLoader;

public class planwidget extends framework {
	private String keyword;
	private int size = 100;

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String list() {
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		// 这里需要做权限控制
		List<Object[]> plans = null;
		try {
			plans = strategyManager.searchPlanByThirdParty(ThirdParty, keyword, size);
		} catch (Exception e) {
		}

		Writer w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		jb.object().key("size").value(plans.size()).key("keyword").value(keyword==null?"":keyword);
		
		jb.key("headers");
		jb.value(JSONArray.fromObject(new String[]{"id","name"}));
		
		
		jb.key("items");
		JSONArray arr = new JSONArray();
		for (int i = 0; i < plans.size(); i++) {
			Object[] plan = plans.get(i);
			JSONObject jo = new JSONObject();
			jo.accumulate("name", plan[1]);
			jo.accumulate("id", plan[0]);
			arr.add(jo);
		}
		jb.value(arr);
		jb.endObject();
		json = w.toString();
		return Action.JSON;
	}

	private long planID;

	public long getPlanID() {
		return planID;
	}

	public void setPlanID(long planID) {
		this.planID = planID;
	}
	@SuppressWarnings("unused")
	public static void _get_fund_table(JSONBuilder jb,List<VariableFor401k> vars){
		String quality = null;
		jb.object().key("size").value(vars.size());
		jb.key("headers");
		jb.value(JSONArray.fromObject(new String[]{"assetclass",//
				"ticker",//
				"name",//
				"rating",//
				"redemption",//
				"description",//
				"waitingperiod",//
				"memo",//
				"roundtrip",//
		}));
		jb.key("items");
		JSONArray arr = new JSONArray();
		String descrptionCopy;
		for (int i = 0; i <vars.size(); i++) {
			VariableFor401k v=vars.get(i);
			descrptionCopy = v.getDescription().replace("|null","");
			try {
				quality =String.valueOf(v.getQuality()*100);
				quality = quality.substring(0,quality.indexOf(".")+2)+"%";
			} catch (Exception e) {
			}
			if(v.getSymbol().equals("null|null")&&v.getAssetClassName().equals("null|null")){
				
				v = checkSymbol(v.getDescription().replace("|null",""));
				if(v==null){
					JSONObject jo = new JSONObject();
					jo.accumulate("assetclass",null);
					jo.accumulate("ticker", null);
					jo.accumulate("name", null);
					jo.accumulate("rating", null);
					jo.accumulate("description", descrptionCopy);
					arr.add(jo);
					continue;
				}
			}
			JSONObject jo = new JSONObject();
			
			String[] assetClassName = v.getAssetClassName().split("\\|");
			String[] ticker = v.getSymbol().split("\\|");
			String[] name = v.getName().split("\\|");
			String[] description = v.getDescription().split("\\|");
			String[] memo = v.getMemo().split("\\|");
			
			jo.accumulate("assetclass", assetClassName[0].equals("null")?assetClassName[1]:assetClassName[0]);
			jo.accumulate("ticker", ticker[0].equals("null")?ticker[1]:ticker[0]);
			jo.accumulate("name", name[0].equals("null")?name[1]:name[0]);
			jo.accumulate("rating", quality);
			jo.accumulate("redemption", v.getRedemption());
			jo.accumulate("description", description[0].equals("null")?description[1]:description[0]);
			jo.accumulate("waitingperiod", v.getWaitingPeriod());
			jo.accumulate("memo", memo[0].equals("null")?memo[1]:memo[0]);
			jo.accumulate("roundtrip", v.getRoundtripLimit());
			
			arr.add(jo);
		}
		jb.value(arr);
		jb.endObject();
	}
	private Map<String,List<VariableFor401k>> fundtableMap;
	public Map<String, List<VariableFor401k>> getFundtableMap() {
		return fundtableMap;
	}

	public void setFundtableMap(Map<String, List<VariableFor401k>> fundtableMap) {
		this.fundtableMap = fundtableMap;
	}

	public String basic_inf(){
		User user = ContextHolder.getUserManager().getLoginUser();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		//
		try{
			fundtableMap = strategyManager.getSixMajorAssetClassForPlan(planID);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//System.out.println(fundtableMap.toString());
		//
		Strategy plan=strategyManager.get(planID);
		int ranksize;
		PlanScore planScore = strategyManager.getPlanScoreByPlanID(planID);
		try {
			double score = planScore.getInvestmentScore() * 100;

			int count = 0;
			if (score == 0)
				count = 0;
			else if (score < 10)
				count = 1;
			else if (score < 35)
				count = 2;
			else if (score < 65)
				count = 3;
			else if (score < 85)
				count = 4;
			else
				count = 5;
			ranksize = count;
		} catch (Exception e) {
			ranksize = 0;
		}
		StringWriter sw=new StringWriter();
		JSONBuilder jb=new JSONBuilder(sw);
		
		
		
		
		jb.object().key("id").value(planID).key("name").value(plan.getName()).key("Rating").value(ranksize);
		String description = plan.getDescription();
//		if(!description.startsWith("<p>&nbsp;</p>\r\n<p>&nbsp;</p>")){
//			if(description.startsWith("<p>&nbsp;</p>")){
//				description = "<p>&nbsp;</p>\r\n"+description;
//			}else{
//				description = "<p>&nbsp;</p>\r\n<p>&nbsp;</p>"+description;
//			}			
//		}
		jb.key("description").value(description);
		jb.key("userName").value(user.getUserName());
		if(plan.getSimilarIssues()!=null && !plan.getSimilarIssues().equals("")){
			String similarIssue = plan.getSimilarIssues().replace("<p>", "").replace("</p>", "");
			jb.key("similarIssue").value(similarIssue.trim());
		}		
		try{
			jb.key("fundtableMapd");
			jb.object().key("items");
			jb.value(JSONObject.fromObject(fundtableMap));
			jb.endObject();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		jb.key("comments");
		_get_comments(jb);
		
		
//		jb.key("funds");
//		
//		if(plan.getVariablesFor401k()==null){
//			plan.setVariablesFor401k(new ArrayList<VariableFor401k>());
//		}
//		_get_fund_table(jb,plan.getVariablesFor401k());
		
		//modelportfolios
		jb.key("modelportfolios");
		_get_list_model_portfolios(jb);
		
		jb.endObject();
		json=sw.toString();
		System.out.println(this.json);
		return Action.JSON;
	}
	
	public static VariableFor401k checkSymbol(String symbolsStr) {
		VariableFor401k v = new VariableFor401k();
		LTIDownLoader ltiDownLoader = new LTIDownLoader();
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getAssetClassManager();
		Set<String> symbolSet = new HashSet<String>();
		symbolSet.add(symbolsStr);
		List<Security> securityList = ltiDownLoader.batchImportSecurity(symbolSet);
		if (securityList.size() != 0) {
			boolean found = false;
			for (Security se : securityList) {
				if (symbolsStr.equalsIgnoreCase(se.getSymbol())) {
					v.setAssetClassName(assetClassManager.get(se.getClassID()).getName());
					v.setSymbol(se.getSymbol());
					v.setName(se.getName());
					v.setDescription("");
					v.setMemo("");
					found = true;
				}
			}
			if (!found) {
				v = null;
			}
		}else 
			v = null;
		return v;
	}
	
	
	private CachePortfolioItem _getportfolio(List<Portfolio> ports, String name, StrategyManager strategyManager) {
		for (Portfolio p : ports) {
			if (p.getName().toLowerCase().contains(name.toLowerCase())) {

				CachePortfolioItem cpi = null;
				List<CachePortfolioItem> pitems = null;
				pitems = strategyManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + p.getID() + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_REALTIME_ID);
				// strategyManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID="
				// + p.getID() + " and cp.GroupID=0 and cp.RoleID=" +
				// Configuration.ROLE_PORTFOLIO_REALTIME_ID);
				if (pitems != null && pitems.size() > 0) {
					cpi = pitems.get(0);
				} else {
					cpi = new CachePortfolioItem();
					cpi.setPortfolioID(p.getID());
					cpi.setPortfolioName(p.getName());
				}
				cpi.setPortfolioName(name.split(" ")[3]);
				return cpi;
			}
		}
		return null;
	}
	
	private String postText;
	
	public String getPostText() {
		return postText;
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}
	

	public String add_comment(){
		User user;
		user = ContextHolder.getUserManager().getLoginUser();
		String userName = user.getUserName();
		StringWriter sw=new StringWriter();
		JSONBuilder jb=new JSONBuilder(sw);
		jb.object();
		try {
			JforumManager jm=ContextHolder.getJforumManager();
			Strategy plan=ContextHolder.getStrategyManager().get(planID);
			jm.addComment(75,planID, userName, plan.getName(), postText, ThirdParty);
			jb.key("code").value(400);
		} catch (Exception e) {
			jb.key("code").value(500);
		}
		
		jb.endObject();
		json=sw.toString();
		return Action.JSON;
	}
	
	public String list_model_portfolios(){
		
		StringWriter sw=new StringWriter();
		JSONBuilder jb=new JSONBuilder(sw);
		_get_list_model_portfolios(jb);
		json=sw.toString();
		return Action.JSON;
	}
	
	public void _get_comments(JSONBuilder jb){
		User user;
		user = ContextHolder.getUserManager().getLoginUser();
		String userName = user.getUserName();
		StrategyManager sm=ContextHolder.getStrategyManager();
		Strategy plan=sm.get(planID);
		JforumManager jm = ContextHolder.getJforumManager();
		JforumTopics jt=jm.getTopicByPlanId((int)planID);
		if(jt==null){
			jm.addComment(75,planID, userName, plan.getName(), plan.getDescription(), ThirdParty);
			jt=jm.getTopicByPlanId((int)planID);
		}
		List<JforumPostText> posts=jm.getPostTextsByTopicID(jt.getID());
		
		jb.object().key("size").value(posts.size());
		jb.key("planid").value(planID);
		jb.key("name").value(plan.getName());
		jb.key("headers");
		jb.value(JSONArray.fromObject(new String[]{
				"comment"//
		}));
		jb.key("items");
		JSONArray arr = new JSONArray();
		for (int i = 0; i <posts.size(); i++) {
			JforumPostText v=posts.get(i);
			JSONObject jo = new JSONObject();
			jo.accumulate("comment", v.getPostText());
			arr.add(jo);
		}
		jb.value(arr);
		jb.endObject();
		
	}
	
	private void _get_list_model_portfolios(JSONBuilder jb){
//		List<Portfolio> portfolios = ContextHolder.getStrategyManager().getModelPortfolios(planID);
//		jb.object().key("size").value(portfolios.size());
//		
//		jb.key("headers");
//		jb.value(JSONArray.fromObject(new String[]{"id","name","enddate"}));
		
//		DateFormat df=new SimpleDateFormat("MM/dd/yyyy");
//		
//		jb.key("items");
//		JSONArray arr = new JSONArray();
//		for (int i = 0; i < portfolios.size(); i++) {
//			Portfolio port = portfolios.get(i);
//			JSONObject jo = new JSONObject();
//			jo.accumulate("name", port.getName());
//			jo.accumulate("id", port.getID());
//			jo.accumulate("enddate", df.format(port.getEndDate()==null?port.getStartingDate():port.getEndDate()));
//			arr.add(jo);
//		}
//		jb.value(arr);
//		jb.endObject();
		
		
		List<Portfolio> ports = ContextHolder.getStrategyManager().getModelPortfolios(planID);

		//CachePortfolioItem s1 = _getportfolio(ports, "Strategic Asset Allocation Growth", strategyManager);
		CachePortfolioItem s2 = _getportfolio(ports, "Strategic Asset Allocation Moderate", ContextHolder.getStrategyManager());
		//CachePortfolioItem s3 = _getportfolio(ports, "Strategic Asset Allocation Conservative", strategyManager);
		List<CachePortfolioItem> SAAPortfolios = new ArrayList<CachePortfolioItem>();
		//if (s1 != null)
		//	SAAPortfolios.add(s1);
		if (s2 != null)
			SAAPortfolios.add(s2);
		
		jb.object().key("size").value(ports.size());
		jb.key("headers");
		jb.value(JSONArray.fromObject(new String[]{"id","name","enddate"}));
		
		DateFormat df=new SimpleDateFormat("MM/dd/yyyy");
		
		jb.key("items");
		JSONArray arr = new JSONArray();
		for (int i = 0; i < SAAPortfolios.size(); i++) {
			CachePortfolioItem port = SAAPortfolios.get(i);
			JSONObject jo = new JSONObject();
			jo.accumulate("name", port.getPortfolioName());
			jo.accumulate("id", port.getPortfolioID());
			arr.add(jo);
		}
		jb.value(arr);
		jb.endObject();
	}

	public static void main(String[] args){
		long pid=1479;
		planwidget p=new planwidget();
		p.setPlanID(pid);
		//p.set
		//p.list_model_portfolios();
		p.basic_inf();
		System.out.println(p.json);
		
	}
	
}
