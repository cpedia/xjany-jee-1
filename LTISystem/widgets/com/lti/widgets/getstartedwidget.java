package com.lti.widgets;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import org.acegisecurity.providers.encoding.Md5PasswordEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONBuilder;

import com.lti.action.Action;
import com.lti.action.register.registerAction;
import com.lti.bean.ProfileItem;
import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.permission.PublicMaker;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.service.bo.UserMarket;
import com.lti.service.bo.UserResource;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.CopyUtil;
import com.lti.util.EmailUtil;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionContext;

import freemarker.template.Template;

public class getstartedwidget extends framework {
	private String keyword;
	private String RegisterUserName;
	private String RegisterPassWord;
	private String Email;
	private String message;
	private String Address;
	public String getRegisterUserName() {
		return RegisterUserName;
	}

	public void setRegisterUserName(String registerUserName) {
		RegisterUserName = registerUserName;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String address) {
		Address = address;
	}

	public String getRegisterPassWord() {
		return RegisterPassWord;
	}

	public void setRegisterPassWord(String registerPassWord) {
		RegisterPassWord = registerPassWord;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

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
		jb.object().key("size").value(plans.size()).key("keyword")
				.value(keyword == null ? "" : keyword);

		jb.key("headers");
		jb.value(JSONArray.fromObject(new String[] {"name"}));

		jb.key("items");
		JSONArray arr = new JSONArray();
		for (int i = 0; i < plans.size(); i++) {
			Object[] plan = plans.get(i);
			JSONObject jo = new JSONObject();
			jo.accumulate("id", plan[0]);
			jo.accumulate("name", plan[1]);
			arr.add(jo);
		}
		jb.value(arr);
		jb.endObject();
		json = w.toString();
		return Action.JSON;
	}

	public String register() {
		User user = new User();
		UserManager userManager = ContextHolder.getUserManager();
		GroupManager groupManager = ContextHolder.getGroupManager();
		Writer w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		if (RegisterUserName != null
				&& userManager.get(RegisterUserName) != null) {
			message = "The username has been used, please use another one.";
			jb.object().key("message").value(message);
			jb.endObject();
			json=w.toString();
			return Action.JSON;
		}
		if (Email == null || Email.equals("")) {
			message = "The email can't be empty!";
			jb.object().key("message").value(message);
			jb.endObject();
			json=w.toString();
			return Action.JSON;
		}
		if (StringUtil.checkEmail(Email) == false) {
			message = "You email address is illegal, please enter again!";
			jb.object().key("message").value(message);
			jb.endObject();
			json=w.toString();
			return Action.JSON;
		}
		if (userManager.getUserByEmail(Email) != null) {
			message = "The email has been used, please use another one.";
			jb.object().key("message").value(message);
			jb.endObject();
			json=w.toString();
			return Action.JSON;
		}
		//对密码加密
		Md5PasswordEncoder md5 = new Md5PasswordEncoder();
		RegisterPassWord = md5.encodePassword(RegisterPassWord, null);
		try {
			user.setID(null);
			user.setEnabled(true);
			user.setEMail(Email);
			user.setAddress(Address);
			user.setUserName(RegisterUserName);
			user.setPassword(RegisterPassWord);
			user.setThirdParty(ThirdParty);
			userManager.add(user);
		} catch (Exception e) {
			message = "Fail to create user" + RegisterUserName;
			jb.object().key("message").value(message);
			jb.endObject();
			json=w.toString();
			return Action.JSON;
		}
		emailConfirm();
		HttpServletRequest request = (HttpServletRequest) ActionContext
				.getContext().get(ServletActionContext.HTTP_REQUEST);
		if (!isf401kdomain(request.getRequestURL().toString())) {
			groupManager.addGroupUser(Configuration.GROUP_MEMBER_ID,
					user.getID());
			groupManager
					.addGroupUser(Configuration.GROUP_MPIQ_ID, user.getID());
		} else {
			// groupManager.addGroupUser(Configuration.GROUP_F401K_ID,
			// user.getID());
			groupManager
					.addGroupUser(Configuration.GROUP_MPIQ_ID, user.getID());
		}
		try {
			MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
			mpm.createOneUserPermission(user.getID());
		} catch (Exception e) {
			
		}

		saveforUserMarket(user, request.getRequestURL().toString());
		message = "success!";
		jb.object().key("message").value(message);
		jb.endObject();
		json = w.toString();
		return Action.JSON;
	}

	/**
	 * save for send market emails for the user
	 * 
	 * @param user
	 * @param url
	 */
	public void saveforUserMarket(User user, String url) {
		UserManager userManager = ContextHolder.getUserManager();
		UserMarket userMarket = new UserMarket();
		String emailforUM = user.getEMail();
		// boolean hasEM = userManager.hasMarketEmail(emailforUM);
		userMarket.setUserEmail(emailforUM);
		userMarket.setUserFirstname(user.getFirstName() == null ? "" : user
				.getFirstName());
		userMarket.setUserLastname(user.getLastName() == null ? "" : user
				.getLastName());
		userMarket.setUserTelephone(user.getTelephone() == null ? "" : user
				.getTelephone());
		userMarket.setAddressCity(user.getAddressCity() == null ? "" : user
				.getAddressCity());
		userMarket.setAddressCountry(user.getAddressCountry() == null ? ""
				: user.getAddressCountry());
		userMarket.setAddressState(user.getAddressState() == null ? "" : user
				.getAddressState());
		userMarket.setUserAddress(user.getAddress() == null ? "" : user
				.getAddress());
		userMarket.setIsSend(true);
		if (isf401kdomain(url))
			userMarket.setGroupKey2("MPIQ");
		else
			userMarket.setGroupKey2("VF");
		userManager.saveMarketEmail(userMarket);
	}

	public boolean isf401kdomain(String url) {
		String[] strs = Configuration.get("f401kdomain").toString()
				.split("\\|");
		for (int i = 0; i < strs.length; i++) {
			if (url.toLowerCase().contains(strs[i].trim()))
				return true;
		}
		return false;
	}

	public Boolean emailConfirm() {
		String confirm = (String) Configuration.get("email.confirm");
		if (confirm.equals("false"))
			return true;
		else {
			String subject = (String) Configuration.get("email.subject");
			Boolean flag = true;
			if (Email != null && !Email.equals("")) {
				try {
					EmailUtil.sendMail(new String[] { Email }, subject,
							generateItem(Email));
				} catch (Exception e) {
					flag = false;
					e.printStackTrace();
				}
			}
			return flag;
		}
	}

	public static String generateItem(String user) {

		try {
			freemarker.template.Configuration conf = new freemarker.template.Configuration();
			try {
				String path = new registerAction().getClass()
						.getProtectionDomain().getCodeSource().getLocation()
						.getPath();
				if (path.indexOf("WEB-INF") > 0) {
					path = path.substring(0, path.indexOf("WEB-INF/classes"));
				} else {
					throw new IllegalAccessException("");
				}
				System.out.print(path + "jsp");
				conf.setDirectoryForTemplateLoading(new File(path + "jsp"));
			} catch (IOException e) {
				ContextHolder.addException(e);
			}
			Template itemplate;
			itemplate = conf.getTemplate("EmailItem.uftl");

			Map<String, Object> data = new HashMap<String, Object>();
			data.put("user", user);
			StringWriter sw = new StringWriter();
			itemplate.process(data, sw);
			return sw.toString();
		} catch (Exception e) {
			ContextHolder.addException(e);
			// e.printStackTrace();
		}
		return null;
	}
	
	public String myPlan(){
		UserManager userManager = ContextHolder.getUserManager();
		User user = userManager.getLoginUser();
		if (user.getID() == 1||user.getID() == 0) {
			user.setID((long) 2827);
		}
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		List<Strategy> strategies = strategyManager.getPrivateStrategies(user.getID());
		Writer w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		jb.object().key("myplan");
		
		JSONArray arr1 = new JSONArray();
		for(Strategy ls:strategies){
			JSONObject jo = new JSONObject();
			jo.accumulate("id",ls.getID());
			jo.accumulate("name", ls.getName());
			arr1.add(jo);
		}
		
		jb.value(arr1);
		jb.endObject();
		json = w.toString();
		return Action.JSON;
	}

	public String myportfolio() {
		String UserName = "null";
		UserManager userManager = ContextHolder.getUserManager();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		User user = userManager.getLoginUser();
		if (user.getUserName() != null) {
			UserName = user.getUserName();
		}
		if (user.getID() == 1||user.getID() == 0) {
			user.setID((long) 2827);
		}
		Writer w = new StringWriter();
		JSONBuilder jb = new JSONBuilder(w);
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		
		List<ProfileItem> profiles = new ArrayList<ProfileItem>();
		
		List<Long> followPortfolioIDList = new ArrayList<Long>();
		List<Long> followIDList = userManager.getResourceIDListByUserIDAndResourceType(user.getID(), Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
		List<Long> customizeIDList = userManager.getResourceIDListByUserIDAndResourceType(user.getID(), Configuration.USER_RESOURCE_PORTFOLIO_CUSTOMIZE);
		if(followIDList != null){
			for(Long pID : followIDList){
				if(customizeIDList != null && customizeIDList.contains(pID))
					continue;
				followPortfolioIDList.add(pID);
			}
		}
		for(Long pID : followPortfolioIDList) {
			Portfolio portfolio = portfolioManager.get(pID);
			UserResource userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(user.getID(), pID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
			String planIDStr = portfolio.getStrategies().getAssetAllocationStrategy().getParameter().get("PlanID");
			if(planIDStr != null) {//是taa/saa
				ProfileItem pi = new ProfileItem();
				pi.setPortfolioID(pID);
				if(userResource != null && userResource.getUpdateTime() != null)
					pi.setStartToFollowDate(LTIDate.parseDateToString(userResource.getUpdateTime()));
				try{
					Long planID = Long.parseLong(planIDStr);
					Strategy plan = strategyManager.get(planID);
					pi.setPlanID(planID);
					pi.setStrategyID(portfolio.getStrategies().getAssetAllocationStrategy().getID());
					pi.setStrategyName(portfolio.getStrategies().getAssetAllocationStrategy().getName());
					//pi.setStartToFollowDate(portfolioManager.getPortfolioLastFollowDate(portfolioID));
					pi.setPlanName(plan.getName());
					List<CachePortfolioItem> pitems = null;
					pitems = portfolioManager.findByHQL("from CachePortfolioItem cp where cp.PortfolioID=" + portfolio.getID() + " and cp.GroupID=0 and cp.RoleID=" + Configuration.ROLE_PORTFOLIO_REALTIME_ID);
					if (pitems != null && pitems.size() > 0) {
						CopyUtil.Translate(pitems.get(0), pi);
					}
//					Map<String,String> map = portfolio.getStrategies().getAssetAllocationStrategy().getParameter();
//					String fre=map.get("Frequency");
//					if(fre==null) fre=map.get("CheckFrequency");
//					if(fre==null) fre=map.get("RebalanceFrequency");
//					if(fre==null) fre="";
//					pi.setFrequency(fre);
//					String riskNumber = map.get("RiskProfile");
//					if(riskNumber != null)
//						pi.setRiskNumber(Double.parseDouble(riskNumber));
				}catch(Exception e){
					
				}
				PublicMaker publicMaker=new PublicMaker(pi);
				pi.setPublic(publicMaker.isPublic());
				profiles.add(pi);
			}
		}
		
		List<Portfolio> portfolio = portfolioManager.getPortfoliosByUser(
				user.getID(), Configuration.PORTFOLIO_TYPE_MODEL);

		
		
		jb.object().key("UserName").value(UserName)
				.key("myportfolio");
		JSONArray arr1 = new JSONArray();
		for (int i = 0; i < profiles.size(); i++) {
			JSONObject jo = new JSONObject();
			jo.accumulate("name", profiles.get(i).getPortfolioName());
			jo.accumulate("id", profiles.get(i).getPortfolioID());
			jo.accumulate("oyear", getAr(profiles.get(i).getPortfolioID()).get(0)
					.get(0));
			jo.accumulate("tyear", getAr(profiles.get(i).getPortfolioID()).get(0)
					.get(1));
			jo.accumulate("fyear", getAr(profiles.get(i).getPortfolioID()).get(0)
					.get(2));

			arr1.add(jo);
		}
		for (int i = 0; i < portfolio.size(); i++) {
			JSONObject jo = new JSONObject();
			jo.accumulate("name", portfolio.get(i).getName());
			jo.accumulate("id", portfolio.get(i).getID());
			jo.accumulate("oyear", getAr(portfolio.get(i).getID()).get(0)
					.get(0));
			jo.accumulate("tyear", getAr(portfolio.get(i).getID()).get(0)
					.get(1));
			jo.accumulate("fyear", getAr(portfolio.get(i).getID()).get(0)
					.get(2));

			arr1.add(jo);
		}
		jb.value(arr1);
		jb.endObject();
		json = w.toString();
		return Action.JSON;
	}

	public List<List<String>> getAr(Long portfolioID) {
		//
		List<List<String>> performance;
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		Portfolio portfolio = portfolioManager.get(portfolioID);
		portfolio = portfolioManager.get(portfolioID);
		List<CachePortfolioItem> pitems = null;
		pitems = ContextHolder.getPortfolioManager().findByHQL(
				"from CachePortfolioItem cp where cp.PortfolioID="
						+ portfolioID + " and cp.GroupID="
						+ Configuration.GROUP_MPIQ_ID + " and cp.RoleID="
						+ Configuration.ROLE_PORTFOLIO_DELAYED_ID);
		if (pitems == null || pitems.size() == 0) {
			CachePortfolioItem cpi = new CachePortfolioItem();
			PortfolioMPT p1 = portfolioManager.getPortfolioMPT(portfolioID,
					PortfolioMPT.DELAY_LAST_ONE_YEAR);
			PortfolioMPT p3 = portfolioManager.getPortfolioMPT(portfolioID,
					PortfolioMPT.DELAY_LAST_THREE_YEAR);
			PortfolioMPT p5 = portfolioManager.getPortfolioMPT(portfolioID,
					PortfolioMPT.DELAY_LAST_FIVE_YEAR);
			cpi.setPortfolioID(portfolioID);
			if (p1 != null) {
				cpi.setAR1(p1.getAR());
			}
			if (p3 != null) {
				cpi.setAR3(p3.getAR());
			}
			if (p5 != null) {
				cpi.setAR5(p5.getAR());
			}

			pitems.add(cpi);
		}

		performance = new ArrayList<List<String>>();
		List<String> mpts = new ArrayList<String>();

		if (pitems != null && pitems.size() > 0) {
			try {
				mpts.add((int) (pitems.get(0).getAR1() * 100 + 0.5) + "%");
			} catch (Exception e) {
				mpts.add("");
			}
			// mpts.add((int)(pitems.get(0).getSharpeRatio1()*100+0.5) + "%");
			try {
				mpts.add((int) (pitems.get(0).getAR3() * 100 + 0.5) + "%");
			} catch (Exception e) {
				mpts.add("");
			}
			// mpts.add((int)(pitems.get(0).getSharpeRatio3()*100+0.5) + "%");
			try {
				mpts.add((int) (pitems.get(0).getAR5() * 100 + 0.5) + "%");
			} catch (Exception e) {
				mpts.add("");
			}
			// mpts.add((int)(pitems.get(0).getSharpeRatio5()*100+0.5) + "%");
		} else {
			mpts.add("P_" + portfolioID);
			mpts.add("");
			mpts.add("");
			mpts.add("");
			// mpts.add("");
			// mpts.add("");
			// mpts.add("");
		}
		performance.add(mpts);

		//
		return performance;
	}

	// public String login(){
	/*
	 * User u = new User(); UserManagerImpl
	 */

	// }
//	public static void main(String[] args) {
//		myPlan();
//	}

}
