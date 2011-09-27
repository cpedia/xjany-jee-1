package com.lti.action.ajax;

import java.util.List;


import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.opensymphony.xwork2.ActionSupport;



/**
 * The clients must get the complete names of user/strategy/portfolio.
 * This action help clients to get complete name list with the first typed letters.
 * @author koko
 *
 */
public class GetResourceSuggestTxtAction  extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	StrategyManager strategyManager;
	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}
	
	PortfolioManager portfolioManager;
	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	UserManager userManager;
	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	private String q;
	
	private String type;
	
	private String resultString;
	@Override
	public String execute() throws Exception {
    	try{
    		//q: query name
    		//type: portfolio/strategy/user
    		
    		resultString="";
    		resultString=new String(resultString.getBytes("ISO-8859-1"), "UTF-8");
    		if(q!=null&&!q.equals("")){
    			q=new String(q.getBytes("ISO-8859-1"), "UTF-8");
				if(type==null){
					//process error when the type parameter is wrong
				}
				//user
				else if(type.equalsIgnoreCase("user")){
					DetachedCriteria detachedCriteria = DetachedCriteria.forClass(com.lti.service.bo.User.class);
					detachedCriteria.add(Restrictions.like("UserName","%"+q+"%"));
					
					List<User> resultList=userManager.getUsers(detachedCriteria);

					for(int i=0;i<resultList.size();i++){
						resultString+=resultList.get(i).getUserName();
						resultString+="\n";
					}
				}
				
				//strategy
				else if(type.equalsIgnoreCase("strategy")){
					DetachedCriteria detachedCriteria = DetachedCriteria.forClass(com.lti.service.bo.Strategy.class);
					detachedCriteria.add(Restrictions.like("Name","%"+q+"%"));
					
					List<Strategy> resultList=strategyManager.getStrategies(detachedCriteria);

					for(int i=0;i<resultList.size();i++){
						resultString+=resultList.get(i).getName();
						resultString+="\n";
					}
				}			
				else if(type.equalsIgnoreCase("portfolio")){
					DetachedCriteria detachedCriteria = DetachedCriteria.forClass(com.lti.service.bo.Portfolio.class);
					detachedCriteria.add(Restrictions.like("Name","%"+q+"%"));
					detachedCriteria.add(Restrictions.eq("IsModelPortfolio",false));
					detachedCriteria.add(Restrictions.eq("IsOriginalPortfolio",false));
					
					List<Portfolio> resultList=portfolioManager.getPortfolios(detachedCriteria);
					for(int i=0;i<resultList.size();i++){
						resultString+="id:";
						resultString+=resultList.get(i).getID();
						resultString+=",name:";
						resultString+=resultList.get(i).getName();
						resultString+="\n";
					}
				}else{
					
				}
				
			}
		}catch(Exception ex){
			resultString="Error";
		}
		
		return Action.SUCCESS;

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
	public String getResultString() {
		return resultString;
	}
	public void setResultString(String resultString) {
		this.resultString = resultString;
	}





}
