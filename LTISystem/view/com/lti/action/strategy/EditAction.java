package com.lti.action.strategy;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.executor.Compiler;
import com.lti.permission.MYPLANIQPermissionManager;
import com.lti.permission.PermissionChecker;
import com.lti.permission.StrategyPermissionChecker;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.ProfileManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Profile;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.StrategyCode;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.Pair;
import com.lti.type.executor.CodeInf;
import com.lti.util.StringUtil;


public class EditAction {
	private Strategy strategy;
	private Long ID;
	private StrategyManager strategyManager;
	private String message;

	
	private long userid;
	private String username;
	private boolean isAnonymous=true;
	private boolean isAdmin=false;
	public EditAction(){
		User user=ContextHolder.getUserManager().getLoginUser();
		userid=user.getID();
		username=user.getUserName();
		if(userid!=Configuration.USER_ANONYMOUS)isAnonymous=false;
		if(userid==Configuration.SUPER_USER_ID)isAdmin=true;
	}
	
	private List<Pair> attributes=null;
	private boolean isOwner;
	public boolean isAnonymous() {
		return isAnonymous;
	}


	public void setAnonymous(boolean isAnonymous) {
		this.isAnonymous = isAnonymous;
	}


	public boolean isAdmin() {
		return isAdmin;
	}


	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}


	public boolean isOwner() {
		return isOwner;
	}


	public void setOwner(boolean isOwner) {
		this.isOwner = isOwner;
	}


	public String edit() {
		//判断是否为anonymous
		if(isAnonymous){
			message = "Please login before editing a strategy.";
			return Action.MESSAGE;
		}
		strategyManager = ContextHolder.getStrategyManager();
		strategy = strategyManager.get(ID);
		if (strategy == null) {
			message = "No such startegy or plan.";
			return Action.MESSAGE;
		}
		
		PermissionChecker pc=new StrategyPermissionChecker(strategy, ServletActionContext.getRequest());
		isOwner=pc.isOwner();
		isAdmin=pc.isAdmin();
		if(!pc.hasViewRole()){
			message = "This strategy is set to be a private one, please go back to the home page.";
			return Action.MESSAGE;
		}
		
		if(strategy.getAttributes()!=null){
			java.util.Iterator<String> iter=strategy.getAttributes().keySet().iterator();
			attributes=new ArrayList<Pair>();
			while(iter.hasNext()){
				String key=iter.next();
				attributes.add(new Pair(key, strategy.getAttributes().get(key)));
			}
		}
		
		if(isAdmin){
			fetchgroups();
		}
		
		return Action.SUCCESS;
	}
	private String readGroup;
	
	public String getReadGroup() {
		return readGroup;
	}


	public void setReadGroup(String readGroup) {
		this.readGroup = readGroup;
	}


	private void fetchgroups(){
		GroupManager groupManager=ContextHolder.getGroupManager();
		String[] grs=groupManager.getGroupRoleNameArray(ID, Configuration.ROLE_STRATEGY_READ_ID);
		readGroup="";
		if(grs!=null&&grs.length!=0){
			boolean first=true;
			for(String gr:grs){
				if(!first){
					readGroup+=",";
				}
				first=false;
				readGroup+=gr;
			}
		}
	}
	private String shortDescription;
	public String getShortDescription() {
		return shortDescription;
	}


	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	private String name=null;
	public String view(){
		strategyManager = ContextHolder.getStrategyManager();
		if(ID!=null){
			strategy=strategyManager.get(ID);
		}else{
			strategy=strategyManager.get(name);
			
		}
		if(strategy.getDescription().contains("<style")&&strategy.getDescription().indexOf("<style")<181){
			
			//shortDescription = filterStyle(strategy.getDescription());
			shortDescription = strategy.getDescription().replaceAll("<\\s*style\\s+([^>]*)\\s*>([^>]*)\\s*style>", "");
			shortDescription = filterHtml(shortDescription);
		}
		else shortDescription = filterHtml(strategy.getDescription());
		if(shortDescription.length()>180){
			Character s;
			int count=0;
			for(int i = 0;i<shortDescription.length();i++){
				s=shortDescription.charAt(i);
				if(s.toString().equals(" ")) {
					count++;
					if(count==30){
						shortDescription=shortDescription.substring(0, i);
						break;
					}else continue;
				}
				if(i==shortDescription.length()+1){
					shortDescription=shortDescription.substring(0, shortDescription.length());
				}
			}
			
		}else shortDescription=shortDescription.substring(0, shortDescription.length());
		
		PermissionChecker pc=new StrategyPermissionChecker(strategy, ServletActionContext.getRequest());
		isOwner=pc.isOwner();
		isAdmin=pc.isAdmin();
		if(!pc.hasViewRole()){
			message = "This strategy is set to be a private one, please go back to the home page.";
			return Action.MESSAGE;
		}
		
		ID=strategy.getID();
		return Action.SUCCESS;
	}
	
	//过滤html <> 标签
	private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签
	public String filterHtml(String str) {
		Pattern pattern = Pattern.compile(regxpForHtml);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		boolean result1 = matcher.find();
		while (result1) {
		matcher.appendReplacement(sb, "");
		result1 = matcher.find();
		}
		matcher.appendTail(sb);
		return sb.toString();
	}
	
	private String operation;
	
	public String getOperation() {
		return operation;
	}


	public void setOperation(String operation) {
		this.operation = operation;
	}

	List<Portfolio> portfolios;

	public List<Portfolio> getPortfolios() {
		return portfolios;
	}


	public void setPortfolios(List<Portfolio> portfolios) {
		this.portfolios = portfolios;
	}


	public String save() {
		
		if(isAnonymous){
			message = "Please login before saving a startegy.";
			return Action.MESSAGE;
		}
		strategyManager = ContextHolder.getStrategyManager();
		Strategy dbstrategy=strategyManager.get(ID);
		if (ID == 0l) {
			strategy.setID(null);
			strategy.setNormal(true);
			strategy.setUserID(userid);
			strategy.setUserName(username);
			strategyManager.add(strategy);
			ID = strategy.getID();
		} else{
			
			PermissionChecker pc=new StrategyPermissionChecker(dbstrategy, ServletActionContext.getRequest());
			isOwner=pc.isOwner();
			isAdmin=pc.isAdmin();
			
			
			if(operation!=null&&operation.equals(Action.ACTION_SAVEAS)){
				if(!pc.hasViewRole()){
					message = "You can not save a strategy which you didnot have the read permission as a new one.";
					return Action.MESSAGE;
				}
				dbstrategy.setID(null);
				//dbstrategy.setID(strategy.getID());
				dbstrategy.setName(strategy.getName());
				//dbstrategy.setStrategyClassID(strategy.getStrategyClassID());
				//dbstrategy.setDescription(strategy.getDescription());
				//dbstrategy.setReference(strategy.getReference());
				dbstrategy.setUserID(userid);
				dbstrategy.setUserName(username);
				//dbstrategy.setAttributes(strategy.getAttributes());
				//dbstrategy.setPostID(strategy.getPostID());
				//dbstrategy.setForumID(strategy.getForumID());
				//newstrategy.setCategories(strategy.getCategories());
				//newstrategy.setShortDescription(strategy.getShortDescription());
				//newstrategy.setSimilarIssues(strategy.getSimilarIssues());
				//dbstrategy.setNormal(true);
				//dbstrategy.setMainStrategyID(strategy.getMainStrategyID());
				
				StrategyCode sc=null;
				if(isAdmin){
					sc=strategyManager.getLatestStrategyCode(ID);
					
				}
				if(sc==null){
					sc=new StrategyCode();
				}
				sc.setID(null);
				sc.setStrategyID(null);
				codeInf=sc.getCode();
				
				if(codeInf==null){
					codeInf=new CodeInf();
					sc.setCode(codeInf);
				}
				
				strategyManager.add(dbstrategy);
				ID = dbstrategy.getID();
				codeInf.setName(strategy.getName());
				codeInf.setID(ID);
				sc.setStrategyID(ID);
				
				strategyManager.saveStrategyCode(sc);
			}else if(operation!=null&&operation.equals(Action.ACTION_PREDELETE)){
				if(!isOwner&&!isAdmin){
					message = "You are not the owner of this strategy, please go back to the home page.";
					return Action.MESSAGE;
				}
				ProfileManager pm=(ProfileManager) ContextHolder.getInstance().getApplicationContext().getBean("profileManager");
				portfolios=strategyManager.getModelPortfolios(ID);
				if(portfolios==null){
					portfolios=new ArrayList<Portfolio>();
				}
				List<Profile> profiles=pm.getProfilesByPlanID(ID);
				if(profiles!=null&&profiles.size()>0){
					for(Profile p:profiles){
						portfolios.add(new Portfolio(p.getPortfolioID(), p.getPortfolioName(), 0l, 0, null));
					}
				}
				return "predelete";
			}else if(operation!=null&&operation.equals(Action.ACTION_DELETE)){
				if(!isOwner&&!isAdmin){
					message = "You are not the owner of this strategy, please go back to the home page.";
					return Action.MESSAGE;
				}
//				ProfileManager pm=(ProfileManager) ContextHolder.getInstance().getApplicationContext().getBean("profileManager");
//				portfolios=strategyManager.getModelPortfolios(ID);
//				if(portfolios==null){
//					portfolios=new ArrayList<Portfolio>();
//				}
//				List<Profile> profiles=pm.getProfilesByPlanID(ID);
//				if(profiles!=null&&profiles.size()>0){
//					for(Profile p:profiles){
//						portfolios.add(new Portfolio(p.getPortfolioID(), p.getPortfolioName(), 0l, 0, null));
//					}
//				}
//				try {
//					PortfolioManager portm=ContextHolder.getPortfolioManager();
//					for(Portfolio p:portfolios){
//						portm.remove(p.getID());
//					}
//					strategyManager.remove(ID);
//					if(!isAdmin){
//						User loginUser = ContextHolder.getUserManager().getLoginUser();
//						Long userID = loginUser.getID();
//						MYPLANIQPermissionManager mpm = new MYPLANIQPermissionManager();
//						mpm.afterPlanDelete(userID, ID);
//					}
//					
				try{
					strategyManager.remove(ID);
				} catch (Exception e) {
					System.out.println(StringUtil.getStackTraceString(e));
					message = "Erorr when deleting the strategy of ID: "+ID;
					return Action.MESSAGE;
				}
			}else {
				if(!isOwner&&!isAdmin){
					message = "You are not the owner of this strategy, please go back to the home page.";
					return Action.MESSAGE;
				}
				
				//dbstrategy.setID(strategy.getID());
				dbstrategy.setName(strategy.getName());
				dbstrategy.setStrategyClassID(strategy.getStrategyClassID());
				dbstrategy.setDescription(strategy.getDescription());
				dbstrategy.setReference(strategy.getReference());
				//dbstrategy.setUserID(strategy.getUserID());
				dbstrategy.setAttributes(strategy.getAttributes());
				//dbstrategy.setPostID(strategy.getPostID());
				//dbstrategy.setForumID(strategy.getForumID());
				dbstrategy.setCategories(strategy.getCategories());
				dbstrategy.setShortDescription(strategy.getShortDescription());
				dbstrategy.setSimilarIssues(strategy.getSimilarIssues());
				//dbstrategy.setType(strategy.getType());
				//dbstrategy.setMainStrategyID(strategy.getMainStrategyID());
				
				if(attributes==null){
					dbstrategy.setAttributes(null);
				}else{
					Map<String,String> map=new HashMap<String, String>();
					for(Pair p:attributes){
						if(p.getPre()!=null&&!p.getPre().equals("")){
							map.put(p.getPre(),p.getPost());
						}
					}
					dbstrategy.setAttributes(map);
				}
				
				strategyManager.update(dbstrategy);
			}
			
		}
		
		return Action.SUCCESS;
	}
	private String boole;
	public String getBoole() {
		return boole;
	}
	public void setBoole(String boole) {
		this.boole = boole;
	}
	//ETF&True
	public String newAttributes(){
		strategyManager = ContextHolder.getStrategyManager();
		strategy = strategyManager.get(ID);
		Strategy dbstrategy=strategyManager.get(ID);
		
		Map<String,String> map=new HashMap<String, String>();
		if(strategy.getAttributes()!=null){
			map.putAll(strategy.getAttributes());
		}
		try{
			boole = strategy.getAttributes().get(name);
			if(boole==null||boole.equals("false")){
				boole = "true";
			}else boole = "false";
		}catch(Exception e){
			boole = "true";
		}
		map.put(name,boole);
		dbstrategy.setAttributes(map);
		strategyManager.update(dbstrategy);
		return Action.SUCCESS;
	}
	
	public String loadAttributes()
	{
		strategyManager = ContextHolder.getStrategyManager();
		strategy = strategyManager.get(ID);
		
		if(strategy.getAttributes()!=null){
			message = strategy.getAttributes().get(name);
		}
		if(message == null){
			message = "false";
		}
		
		return Action.MESSAGE;
	}
	

	public List<Pair> getAttributes() {
		return attributes;
	}


	public void setAttributes(List<Pair> attributes) {
		this.attributes = attributes;
	}


	public String list() {
		return Action.SUCCESS;
	}

	public String delete() {
		return Action.SUCCESS;
	}

	private CodeInf codeInf; 
	public String editcode() {
		if(!isAdmin){
			message = "Only administrator can edit the code of strategy, please go back to homepage.";
			return Action.MESSAGE;
		}
		strategyManager = ContextHolder.getStrategyManager();
		strategy = strategyManager.get(ID);
		if(strategy==null){
			message="The strategy doesn't exist.";
			return Action.MESSAGE;
		}
		StrategyCode sc=strategyManager.getLatestStrategyCode(ID);
		if(sc==null){
			codeInf=new CodeInf();
			codeInf.setID(ID);
			codeInf.setName(strategy.getName());
		}else{
			codeInf=sc.getCode();
		}
		return Action.SUCCESS;
	}

	public String savecode() {
		if(!isAdmin){
			message = "Only administrator can edit the code of strategy, please go back to homepage.";
			return Action.MESSAGE;
		}
		strategyManager = ContextHolder.getStrategyManager();
		if (ID == 0l) {
			throw new RuntimeException("You cannot save the code of the static strategy.");
		} else {
			Strategy dbstrategy=strategyManager.get(ID);
			if(dbstrategy==null){
				throw new RuntimeException("The strategy doesn't exist.");
			}
			codeInf.setName(dbstrategy.getName());
			StrategyCode sc=new StrategyCode();
			sc.setStrategyID(dbstrategy.getID());
			sc.setDate(new Date());
			sc.setCode(codeInf);
			strategyManager.saveStrategyCode(sc);
		}
		return Action.SUCCESS;
	}
	
	public String compile(){
		if(!isAdmin){
			message = "Only administrator can edit the code of strategy, please go back to homepage.";
			return Action.MESSAGE;
		}
		strategyManager = ContextHolder.getStrategyManager();
		strategy = strategyManager.get(ID);
		if(strategy==null){
			message="The strategy doesn't exist.";
			return Action.MESSAGE;
		}
		StrategyCode sc=strategyManager.getLatestStrategyCode(ID);
		if(sc==null){
			codeInf=new CodeInf();
			codeInf.setID(ID);
			codeInf.setName(strategy.getName());
		}else{
			codeInf=sc.getCode();
		}
		try {
			message=Compiler.complie(codeInf, strategy.getStrategyClassID());
		} catch (Exception e) {
			message="<pre>"+StringUtil.getStackTraceString(e)+"</pre>";
		}
		return Action.MESSAGE;
	}

	public Strategy getStrategy() {
		return strategy;
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public CodeInf getCodeInf() {
		return codeInf;
	}

	public void setCodeInf(CodeInf codeInf) {
		this.codeInf = codeInf;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	public String ajaxsavegroups(){
		if(isAnonymous){
			message = "Please login before changing group information.";
			return Action.MESSAGE;
		}
		if(!isAdmin){
			message = "Only adminstrator has the permission to change the group information of strategy.";
			return Action.MESSAGE;
		}
		StrategyManager strategyManager=ContextHolder.getStrategyManager();
		GroupManager groupManager=ContextHolder.getGroupManager();
		
		Strategy dbstrategy = strategyManager.get(ID);
		if(dbstrategy==null){
			message = "No such strategy.";
			return Action.MESSAGE;
		}
			
		if(readGroup!=null){
			String[] dgps=readGroup.split(",");
			groupManager.changeGroupRoles(ID, dgps, Configuration.ROLE_STRATEGY_READ_ID);
		}
		
		fetchgroups();
		message="Read Groups: "+readGroup;
		return Action.MESSAGE;
	}
}
