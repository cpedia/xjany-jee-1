package com.lti.action;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lti.service.PortfolioManager;
import com.lti.service.ProfileManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Profile;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.service.bo.UserTransaction;
import com.lti.system.ContextHolder;
import com.lti.util.FileOperator;

public class statisticaction {

	
	private List<Strategy> strategies;
	private List<Portfolio> portfolios;
	private List<User> users;
	private List<UserTransaction> transactions;
	
	private Date date;
	
	private String datestr;
	
	
	public String overall()throws Exception{
		Calendar ca=Calendar.getInstance();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		DateFormat df1=new SimpleDateFormat("yyyy-MM-dd");
		if(datestr!=null){
			date=df1.parse(datestr);
			ca.setTime(date);
			ca.add(Calendar.DAY_OF_YEAR, 1);
			date=ca.getTime();
		}else{
			ca.add(Calendar.DAY_OF_YEAR, 1);
			date=ca.getTime();
		}
		
		ca.add(Calendar.DAY_OF_YEAR, -1);
		Date predate=ca.getTime();
		
		PortfolioManager portfolioManager=ContextHolder.getPortfolioManager();
		UserManager userManager=ContextHolder.getUserManager();
		ProfileManager profileManager=(ProfileManager) ContextHolder.getInstance().getApplicationContext().getBean("profileManager");
		
		
		String datestr=" p.createdDate<'"+df.format(date)+"' and p.createdDate>= '"+df.format(predate)+"'";
		
		strategies=portfolioManager.findByHQL("select new Strategy(p.ID,p.Name,p.UserID,p.PlanType,p.Status,p.createdDate) from Strategy p where "+datestr);
		if(strategies!=null&&strategies.size()>0){
			for(Strategy str:strategies){
				str.setUserName(userManager.get(str.getUserID()).getUserName());
			}
		}
		
		portfolios=portfolioManager.findByHQL("select new Portfolio(p.ID,p.Name,p.UserID,p.Type,p.State,p.EndDate,p.createdDate) from Portfolio p where "+datestr);
		if(portfolios!=null&&portfolios.size()>0){
			for(Portfolio p:portfolios){
				try {
					p.setUserName(userManager.get(p.getUserID()).getUserName());
				} catch (Exception e) {
					p.setUserName(""+p.getUserID());
				}
				Map<String,String> attrs=new HashMap<String, String>();
				List<Profile> pros=profileManager.getProfilesByPortfolioID(p.getID());
				if(pros!=null&&pros.size()>0){
					attrs.put("planName", pros.get(0).getPlanName());
					attrs.put("planID", pros.get(0).getPlanID()+"");
				}
				p.setAttributes(attrs);
			}
		}
		users=portfolioManager.findByHQL("from User p where "+datestr);
		transactions=portfolioManager.findByHQL("from UserTransaction p where "+" p.timeCreated<'"+df.format(date)+"' and p.timeCreated>= '"+df.format(predate)+"'");
		if(transactions!=null&&transactions.size()>0){
			for(UserTransaction ut:transactions){
				ut.setUserName(userManager.get(ut.getUserID()).getUserName());
			}
		}
		
		return Action.SUCCESS;
		
	}

	public String getDatestr() {
		return datestr;
	}

	public void setDatestr(String datestr) {
		this.datestr = datestr;
	}

	public List<Strategy> getStrategies() {
		return strategies;
	}

	public void setStrategies(List<Strategy> strategies) {
		this.strategies = strategies;
	}

	public List<Portfolio> getPortfolios() {
		return portfolios;
	}

	public void setPortfolios(List<Portfolio> portfolios) {
		this.portfolios = portfolios;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<UserTransaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<UserTransaction> transactions) {
		this.transactions = transactions;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
	
}
