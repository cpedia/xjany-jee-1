package com.lti.action.ajax;

import java.util.List;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.StrategyClassManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Role;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;



public class GetStrategySuggestTxtAction  extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	StrategyManager strategyManager;

	StrategyClassManager strategyClassManager;
	




	public String getResultString() {
		return resultString;
	}


	public void setResultString(String resultString) {
		this.resultString = resultString;
	}


	public String getQ() {
		return q;
	}


	public void setQ(String q) {
		this.q = q;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	
	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}
	
	private Long[] parseIDs(String s) {
		String[] ss = s.split(",");
		Long[] ids = new Long[ss.length];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = Long.parseLong(ss[i]);
		}
		return ids;

	}
	private String resultString;
	
	private String q;
	
	private String type;

	public String execute() throws Exception {
		
		GroupManager groupManager = (GroupManager) ContextHolder.getInstance().getApplicationContext().getBean("groupManager");
		UserManager userManager = (UserManager) ContextHolder.getInstance().getApplicationContext().getBean("userManager");

		User user = userManager.getLoginUser();
		Long userID;
		String groupIDs="";
		if (user == null) {
			userID = -1l;
		} else
			userID = user.getID();

		boolean isAdmin = false;
		if (userID.equals(Configuration.SUPER_USER_ID)) {
			isAdmin = true;
		}

		boolean isAnonymous = true;
		if (!userID.equals(Configuration.USER_ANONYMOUS)) {
			isAnonymous = false;
		}

		if (groupIDs == null || groupIDs.equals("")) {
			if (isAnonymous) {
				groupIDs = groupManager.get(Configuration.GROUP_ANONYMOUS).getID() + "";
			} else {
				groupIDs = groupManager.get(Configuration.GROUP_ANONYMOUS).getID() + ",";
				groupIDs += groupManager.get(Configuration.GROUP_MEMBER).getID();
			}
		}

		Object[] groups = groupManager.getGroupIDs(userID);
		Long[] ids = parseIDs(groupIDs);
		boolean readable;
		int resultCount=0;
		
		resultString="";
		resultString=new String(resultString.getBytes("ISO-8859-1"), "UTF-8");
    	try{
			if(q!=null&&!q.equals("")){
				q=new String(q.getBytes("ISO-8859-1"), "UTF-8");
				//DetachedCriteria detachedCriteria = DetachedCriteria.forClass(com.lti.service.bo.Strategy.class);
				//detachedCriteria.add(Restrictions.eq("StrategyClassID", strategyClassManager.get(type).getID()));
				//detachedCriteria.add(Restrictions.like("Name","%"+q+"%"));
				Role role_strategy_read = groupManager.getRoleByName(Configuration.ROLE_STRATEGY_READ);
				Long classid = strategyClassManager.get(type).getID();
				List<Strategy> resultList=strategyManager.searchStrategies(classid, q);
				if(type.equalsIgnoreCase(Configuration.ASSET_ALLOCATION_STRATEGY_CLASS_NAME)){
					Long assetStrategyID = strategyClassManager.get(Configuration.ASSET_STRATEGY_CLASS_NAME).getID();
					List<Strategy> assetStrategies = strategyManager.searchStrategies(assetStrategyID, q);
					if(assetStrategies!=null && assetStrategies.size()>0)
					{
						for(int i=0;i<assetStrategies.size();++i)
						{
							readable = groupManager.hasrole(role_strategy_read.getID(), ids, assetStrategies.get(i).getID(),Configuration.RESOURCE_TYPE_STRATEGY);
							if(readable||assetStrategies.get(i).getUserID().equals(userID))
							{
								resultList.add(assetStrategies.get(i));
								//we just need 10 at most
								if(++resultCount>10)
									break;	
							}
						}
					}
				}
				resultString+="STATIC";
				resultString+="\n";
				for(int i=0;i<resultList.size();i++){
					if(resultList.get(i).getName().equalsIgnoreCase(q)){
						String[] tmp = resultString.split("STATIC\n");
						resultString = "STATIC"+"\n";
						resultString += q;
						resultString +="\n";
						resultString += tmp[1];
					}
					else{
						resultString+=resultList.get(i).getName();
						resultString+="\n";
					}
				}
			}
		}catch(Exception ex){
		}
		return Action.SUCCESS;
	}


	public void setStrategyClassManager(StrategyClassManager strategyClassManager) {
		this.strategyClassManager = strategyClassManager;
	}
}
