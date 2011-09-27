package com.lti.widgets;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONBuilder;

import com.lti.action.Action;
import com.lti.action.f401kaction;
import com.lti.jobscheduler.DailyExecutionJob;
import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.service.AssetClassManager;
import com.lti.service.JforumManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.Parse401KParameters;

public class createplanwidget extends framework {
	private String planname;
	private String data;
	private String postText;
	private String help;

	public String getHelp() {
		return help;
	}

	public void setHelp(String help) {
		this.help = help;
	}

	public String getPostText() {
		return postText;
	}

	public void setPostText(String postText) {
		this.postText = postText;
	}

	public String getPlanname() {
		return planname;
	}

	public void setPlanname(String planname) {
		this.planname = planname;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	private List<VariableFor401k> parseVariablesFor401k(String data) {
		String[] lines = data.split("\\.\\.");
		String[] headers = null;
		if (lines[0] != null && lines[0].length() > 0) {
			headers = lines[0].split(",,");
		} else {
			headers = lines[1].split(",,");
		}
		Map<String, Integer> index = new HashMap<String, Integer>();
		for (int i = 0; i < headers.length; i++) {
			if (headers[i] == null || headers[i].equals(""))
				continue;
			index.put(headers[i], i);
		}

		SecurityManager sm = ContextHolder.getSecurityManager();
		AssetClassManager acm = ContextHolder.getAssetClassManager();

		List<VariableFor401k> vs = new ArrayList<VariableFor401k>();
		for (int i = 2; i < lines.length; i++) {
			if (lines[i] == null || lines[i].equals(""))
				continue;
			String[] tds = lines[i].split(",,");
			boolean empty = true;
			for (int j = 0; j < tds.length; j++) {
				if (!tds[j].toLowerCase().equals("")) {
					empty = false;
					break;
				}
			}
			if (empty)
				continue;
			VariableFor401k v = new VariableFor401k();
			// ticker redemption roundtrip waitingperiod
			try {
				v.setSymbol(tds[index.get("ticker")]);
			} catch (Exception e1) {
				// e1.printStackTrace();
			}
			Security sec = null;
			AssetClass ac = null;
			if (v.getSymbol() != null) {
				sec = sm.get(v.getSymbol());
			}
			if (sec != null && sec.getClassID() != null) {
				ac = acm.get(sec.getClassID());
			}
			try {
				v.setAssetClassName(tds[index.get("assetclass")]);
			} catch (Exception e1) {
				// e1.printStackTrace();
			}
			if (v.getAssetClassName() == null
					|| v.getAssetClassName().equals("")) {
				if (ac != null) {
					v.setAssetClassName(ac.getName());
				} else {
					v.setAssetClassName("");
				}
			}

			try {
				v.setName(tds[index.get("name")]);
			} catch (Exception e1) {
				// e1.printStackTrace();
			}

			if (v.getName() == null || v.getName().equals("")) {
				if (sec != null) {
					v.setName(sec.getName());
				} else {
					v.setName("");
				}
			}

			try {
				v.setDescription(tds[index.get("description")]);
			} catch (Exception e1) {
				// e1.printStackTrace();
			}
			if (v.getDescription() == null || v.getDescription().equals("")) {
				if (sec != null) {
					v.setDescription(sec.getName());
				} else {
					v.setDescription("");
				}
			}

			try {
				v.setRedemption(Integer.parseInt(tds[index.get("redemption")]));
			} catch (Exception e) {
			}

			if (v.getRedemption() == null) {
				v.setRedemption(3);
			}

			try {
				v.setRoundtripLimit(Integer.parseInt(tds[index.get("roundtrip")]));
			} catch (Exception e) {
			}

			if (v.getRoundtripLimit() == null) {
				v.setRoundtripLimit(60);
			}

			try {
				v.setWaitingPeriod(Integer.parseInt(tds[index
						.get("waitingperiod")]));
			} catch (Exception e) {
			}

			if (v.getWaitingPeriod() == null) {
				v.setWaitingPeriod(0);
			}

			try {
				v.setMemo(tds[index.get("memo")]);
			} catch (Exception e) {
			}
			vs.add(v);
		}
		return vs;
	}

	private String tickers;
	private String description;

	public String getTickers() {
		return tickers;
	}

	public void setTickers(String tickers) {
		this.tickers = tickers;
	}

	public String process() {
//		String[] lines = tickers.split("\\.\\.");
//		AssetClassManager acm = ContextHolder.getAssetClassManager();
//		SecurityManager sm = ContextHolder.getSecurityManager();
		List<VariableFor401k> vars = new ArrayList<VariableFor401k>();
		Parse401KParameters pp = new Parse401KParameters();
		pp.setOriginalString(tickers.replace("$$", "\n"));
		try {
			pp.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}
		vars = pp.getVariables();
//		for (String symbol : lines) {
//			if (symbol.equals(""))
//				continue;
//			VariableFor401k v = new VariableFor401k();
//			Security sec = sm.get(symbol);
//			if (sec == null) {
//				v.setSymbol(symbol);
//				v.setMemo("n");
//			} else {
//				v.setSymbol(sec.getSymbol());
//				v.setDescription(sec.getName());
//				if (sec != null && sec.getClassID() != null) {
//					AssetClass ac = acm.get(sec.getClassID());
//					if (ac != null)
//						v.setAssetClassName(ac.getName());
//				} else {
//					v.setAssetClassName("unknown asset name");
//					v.setMemo("n");
//				}
//				v.setName(sec.getName());
//			}// end if
//			v.setRedemption(3);
//			vars.add(v);
//		}

		StringWriter sw = new StringWriter();
		JSONBuilder jb = new JSONBuilder(sw);

		planwidget._get_fund_table(jb, vars);

		json = sw.toString();
		return Action.JSON;
	}
	private String symbolsStr;
	public String getSymbolsStr() {
		return symbolsStr;
	}

	public void setSymbolsStr(String symbolsStr) {
		this.symbolsStr = symbolsStr;
	}
	
	
	public String getBenchMarksByAssertClass()
	{
		String message="";
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getAssetClassManager();
		SecurityManager securityManager = (SecurityManager) ContextHolder.getSecurityManager();
		String assetClassIDs[];
		assetClassIDs = symbolsStr.trim().split("\\.");
		Long assetClassID = Long.parseLong(assetClassIDs[0]);
		if (assetClassID == -1)
		{
			if (message == "" || message == null)
			{
				message = "NotFound" + "|";
			} else
			{
				message += "NotFound" + "|";
			}
			message += "NotFound" + "|";
			message += "NotFound" + "|";
			message += 3 + "|";
			message += "NotFound" + "|";
			message += "n" + "|";
		} else
		{
			AssetClass ac = assetClassManager.get(assetClassID);
			Security se = securityManager.get(ac.getBenchmarkID());
			if (message == "" || message == null)
			{
				message = ac.getName() + "|";
			} else
			{
				message += ac.getName() + "|";
			}
			message += se.getSymbol() + "|";
			message += se.getName() + "|";
			message += 3 + "|";
			message += se.getName() + "|";
			message += "p" + "|";
		}
		JSONObject o = new JSONObject();
		o.accumulate("msg",message);
		json = o.toString();
		return Action.JSON;
	}
	
	
	public String updatePlan(){
		return Action.JSON;
	}
	private String id;
	private String isCopy;
	private String isModer = "false";

	public String getIsModer() {
		return isModer;
	}

	public void setIsModer(String isModer) {
		this.isModer = isModer;
	}

	public String getIsCopy() {
		return isCopy;
	}

	public void setIsCopy(String isCopy) {
		this.isCopy = isCopy;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String createplan() {
		boolean isCreate=false;
		try {
			if(id.equals("undefined")){
				isCreate = true;
			}
			User user;
			boolean isAdmin = false;
			user = ContextHolder.getUserManager().getLoginUser();
			if (user.getID() == Configuration.SUPER_USER_ID) {
				isAdmin = true;
			}
			
			MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
			if (isCreate && !isAdmin) {
				int operationCode = mpm.canPlanCreate(user.getID(), true);
				if (operationCode == mpm.PERMISSION_LIMIT) {
					ServletActionContext.getResponse().setStatus(500);
					JSONObject o = new JSONObject();
					o.accumulate("msg", 
							"You need to subscribe as a Expert User or a Pro User to get the permission of creating plans.");
					json = o.toString();
					return Action.JSON;
//				} else if (isCreate && operationCode == 10) {
				} else if (operationCode == mpm.COUNT_LIMIT) {
					ServletActionContext.getResponse().setStatus(500);
					JSONObject o = new JSONObject();
					o.accumulate("msg", 
							"You have reached the maximum number of your own plans. To create a new plan, please delete one of your plans in the 'My Portfolio' page, or subscribe as a higher level user to obtain the permission for more own plans.");
					json = o.toString();
					return Action.JSON;
				}
			}
			
			StrategyManager strategyManager = ContextHolder
					.getStrategyManager();
			Strategy plan;
			if (!isCreate) {
				plan = strategyManager.get(Long.valueOf(id));
				if("true".equals(isCopy)){
					planname = plan.getName();
					Strategy template = new Strategy();// strategyManager.get(id);
					plan = template.clone();
					plan.setName(user.getUserName()+"_"+planname);
				}
			}else{
				if (planname == null || data == null) {
					JSONObject o = new JSONObject();
					o.accumulate("msg", "The plan name is empty or the data is empty.");
					json = o.toString();
					return Action.JSON;
				}
				plan = strategyManager.get(planname);
				if (!"true".equals(isModer)) {
					if (plan != null) {

						JSONObject o = new JSONObject();
						o.accumulate("msg", "The plan name has been used before, plaese input a new plan name.");
						json = o.toString();
						return Action.JSON;
					}
					Strategy template = new Strategy();// strategyManager.get(id);
					plan = template.clone();
					plan.setName(planname);
				}
			}
			plan.set401K(true);
			plan.setUserID(user.getID());
			plan.setUserName(user.getUserName());
			plan.setConsumerPlan(true);
			plan.setDescription(description);
			plan.setVariablesFor401k(parseVariablesFor401k(data));
			
			if(!"true".equals(isModer)){
				if(isCreate || "true".equals(isCopy)){
					strategyManager.add(plan);
					mpm.afterPlanCreate(user.getID(), plan.getID(), true);
				}else{
					plan.setID(Long.valueOf(id));
					strategyManager.update(plan);
				}
			}
			JforumManager jm=ContextHolder.getJforumManager();
			try {
				if(help!=null){
					jm.addComment(77,plan.getID(), user.getUserName(), plan.getName(), postText+help, ThirdParty);
				}else
					jm.addComment(77,plan.getID(), user.getUserName(), plan.getName(), postText, ThirdParty);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			JSONObject o = new JSONObject();
			o.accumulate("msg", "Created successfully.");
			o.accumulate("code", 400);
			o.accumulate("id", plan.getID());
			if("true".equals(isModer)){
				f401kaction fk = new f401kaction();
				User u = new User();
				u.setID(Configuration.SUPER_USER_ID);
				fk.setUser(u);
				fk.setID(plan.getID());
				try {
					fk._generate();
					o.accumulate("generate_code", 400);
				} catch (Exception e) {
					o.accumulate("generate_code", 500);
				}
				
				try{
					o.accumulate("makepublic_code", fk.makepublic());
				}catch(Exception e){
					o.accumulate("makepublic_code", 500);
				}
				

				List<Portfolio> portfolios = strategyManager
						.getModelPortfolios(plan.getID());
				StringBuffer sb = new StringBuffer();
				if (portfolios != null && portfolios.size() > 0) {
					for (Portfolio p : portfolios) {
						sb.append(p.getID()).append(",");
					}
				}
//				try{
//					fk.execute();
//					o.accumulate("monito_code", 400);
//				}catch(Exception e){
//					o.accumulate("monito_code", 500);
//				}
				

				String url = "http://localhost:8081/Dailystart?planID="
						+ plan.getID() + "&filter=PortfolioIDFilter&ids="
						+ sb.toString() + "&mode=1&forceMonitor=true";
				try {
					DailyExecutionJob.sendRequest(url);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			json = o.toString();
		} catch (Exception e) {
			JSONObject o = new JSONObject();
			o.accumulate("msg", "ERROR:-1;Fail to create plan");
			json = o.toString();
		}
		return Action.JSON;
	}

	private String ticker;
	private int size = 6;

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	

	public String ticker() {
		SecurityManager sm = ContextHolder.getSecurityManager();
		List<Security> sis = sm.getSecuritiesByName(ticker, size, 0).getItems();
		StringWriter w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		jb.object().key("size").value(sis.size());

		jb.key("tickers");
		JSONArray arr = new JSONArray();
		for (int i = 0; i < sis.size(); i++) {
			JSONObject o = new JSONObject();
			o.accumulate("ticker", sis.get(i).getSymbol());
			o.accumulate("description", sis.get(i).getName());
			arr.add(o);
		}
		jb.value(arr);
		jb.endObject();
		json = w.toString();
		return Action.JSON;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
