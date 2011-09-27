package com.lti.action.clonecenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.ProfileManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Profile;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.Transaction;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 不再使用
 * @author Administrator
 *
 */
@Deprecated
public class ViewSchHoldingTransactionAction extends ActionSupport implements Action{

	private Long portfolioID;
	private String message;
	private boolean stateType;
	private boolean tranType;
	private Portfolio sportfolio;
	private List<Transaction> scheduletransactions;
	private PortfolioManager portfolioManager;
	private SecurityManager securityManager;
	private double total;
	
	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isStateType() {
		return stateType;
	}

	public void setStateType(boolean stateType) {
		this.stateType = stateType;
	}
	
	public boolean isTranType() {
		return tranType;
	}

	public void setTranType(boolean tranType) {
		this.tranType = tranType;
	}
	
	public Portfolio getSportfolio() {
		return sportfolio;
	}

	public void setSportfolio(Portfolio sportfolio) {
		this.sportfolio = sportfolio;
	}

	public List<Transaction> getScheduletransactions() {
		return scheduletransactions;
	}

	public void setScheduletransactions(List<Transaction> scheduletransactions) {
		this.scheduletransactions = scheduletransactions;
	}
	
	public PortfolioManager getPortfolioManager() {
		return portfolioManager;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}
	@Deprecated
	public String execute(){
		portfolioManager = ContextHolder.getPortfolioManager();
		securityManager = ContextHolder.getSecurityManager();
		Portfolio portfolio = portfolioManager.get(portfolioID);
		//TODO: hereeeeeeeeeeeeeeeeeeeeeeeeeeeeee
		//sportfolio = portfolio.calculateScheduleHolding();
		scheduletransactions = portfolioManager.getTransactions(portfolioID, Configuration.TRANSACTION_TYPE_SCHEDULE);//schedule
		if(sportfolio==null||(scheduletransactions==null&&scheduletransactions.size()<1)){
			message = "failure";
			return Action.SUCCESS;
		}
		else{
			scheduletransactions = useTransactionDescription(portfolioManager, securityManager, scheduletransactions);
			predictScheduleTransDate(scheduletransactions);
			//List assets=new ArrayList(sportfolio.getAssets());//
			//sportfolio.setAssetList(assets);//
			useDescription(sportfolio);
			message = "succes";
			return Action.SUCCESS;
		}
	}
	
	/**
	 * in the 401k plan portfolio, replace the security(portfolio-assets-securitylist) of holding for their plan's descriptions
	 * @param portfolio
	 * @return
	 * @author SuPing
	 * 2010/05/05
	 */
	public void useDescription(Portfolio portfolio){
//		com.lti.service.SecurityManager securityManager = ContextHolder.getSecurityManager();
//		StrategyManager strategyManager = ContextHolder.getStrategyManager();
//		SecurityManager sm=ContextHolder.getSecurityManager();
//		Strategy strategy = strategyManager.get(portfolio.getMainStrategyID());
//		ProfileManager profileManager = (ProfileManager) ContextHolder.getInstance().getApplicationContext().getBean("profileManager");
//		List<Profile> profiles = profileManager.getProfilesByPortfolioID(portfolio.getID());
//		if(strategy!=null){
//			searchFundDescription(strategy,strategyManager,portfolio);
//		}
//		else if(profiles!=null&&profiles.size()>0){
//			Long planID = profiles.get(0).getPlanID();
//			Strategy proStrategy = strategyManager.get(planID);
//			if(proStrategy!=null){
//				searchFundDescription(proStrategy,strategyManager,portfolio);
//			}
//		}
//		//show the SecurityName
//		Set<Asset> as=portfolio.getAssets();
//		Iterator ias = as.iterator();
//		if(portfolio.getCash()!=null)
//			total = portfolio.getCash();
//		else
//			total = 0.0;
//		Date date = com.lti.util.LTIDate.clearHMSM(portfolio.getEndDate());
//		while(ias.hasNext()){
//			Asset sas = (Asset)ias.next();
//			Set<SecurityItem> si=sas.getSecurityItems();
//			Iterator sit = si.iterator();
//			while(sit.hasNext()){
//				SecurityItem sis=(SecurityItem)sit.next();
//				if(sis.getSecurityName()==null||sis.getSecurityName().trim().equals("")){
//					Security security=securityManager.getBySymbol(sis.getSymbol());
//					sis.setSecurityName(security.getName());
//					//System.out.println(sis.getSecurityName()+""+sis.getSymbol()+"//"+sis.getSecurityType()+"//"+sis.getPercentage());
//					try {
//						double price = sm.getPrice(security.getID(), date);
//						double shares = sis.getShares();
//						double amount = shares * price;
//						total = total + amount;
//						sis.setTotalAmount(amount);
//						sis.setPrice(price);
//					} catch (Exception e1) {
//						sis.setTotalAmount(0.0);
//						sis.setPrice(0.0);
//						total = total + 0.0;
//						continue;
//					}
//				}
//				else continue;
//			}	
//		}
	}
	
	/**
	 * get the search part a function for strategy and prostrategy
	 * @param strategy
	 * @param strategyManager
	 * @param portfolio
	 * @author SuPing
	 * 2010/05/05
	 */
	public void searchFundDescription(Strategy strategy,StrategyManager strategyManager,Portfolio portfolio){
//		if(strategy.getType().equals(Configuration.STRATEGY_401K_TYPE)||strategy.getType()==1){//two ways to find the 401ks portfolio strategys
//			stateType=true;
//			List<Asset> assets=portfolio.getAssetList();
//			List<VariableFor401k> vk=strategyManager.getVariable401KByStrategyID(strategy.getID());
//			for(int i=0;i<assets.size();i++){
//				List<SecurityItem> sis=assets.get(i).getSecurityList();
//				for(int j=0;j<sis.size();j++){
//					for(int k=0;k<vk.size();k++){
//						if(vk.get(k).getSymbol()!=null){
//							if(vk.get(k).getSymbol().equals(sis.get(j).getSymbol())){
//								if(vk.get(k).getDescription()!=null&&!vk.get(k).getDescription().equals(""))
//									sis.get(j).setDescription401k(vk.get(k).getDescription());//replace the symbol of description
//								else
//									sis.get(j).setDescription401k(sis.get(j).getSymbol());
//								break;   
//							}//end if
//						}
//						else
//							sis.get(j).setDescription401k(sis.get(j).getSymbol());
//					}//end for
//				}//end for
//				assets.get(i).setSecurityList(sis);
//			}
//			portfolio.setAssetList(assets);
//		}
//		else
//			stateType=false;
	}
	
	/**
	 * in the 401k transactions, replace the symbols for their descriptions
	 * @param transactions
	 * @author SuPing
	 * 2010/02/01
	 */
	public List<Transaction> useTransactionDescription(PortfolioManager portfolioManager,com.lti.service.SecurityManager securityManager,List<Transaction> transactions){
		//PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		List<Transaction> nt=new ArrayList<Transaction>();
		StrategyManager strategyManager = ContextHolder.getStrategyManager();
		ProfileManager profileManager = (ProfileManager) ContextHolder.getInstance().getApplicationContext().getBean("profileManager");
		for(int i=0; i<transactions.size(); i++) {
			Transaction t = transactions.get(i);
			Portfolio portfolio = portfolioManager.get(t.getPortfolioID());	
			
			//show the SecurityName
			if(t.getSecurityName()==null||t.getSecurityName().trim().equals("")){
				if(t.getSymbol()!=null&&!t.getSymbol().trim().equals("")){
					Security security=securityManager.getBySymbol(t.getSymbol());
					t.setSecurityName(security.getName());
				}
				else if(t.getSecurityID()!=null){
					Security security=securityManager.get(t.getSecurityID());
					t.setSecurityName(security.getName());
					if(t.getSymbol()==null)
						t.setSymbol(security.getSymbol());
				}
				else
					t.setSecurityName(t.getSymbol());	
			}
			
			Strategy strategy = strategyManager.get(portfolio.getMainStrategyID());//get the transaction's strategy
			List<Profile> profiles = profileManager.getProfilesByPortfolioID(portfolio.getID());
			if(strategy!=null){
				if(strategy.getType().equals(Configuration.STRATEGY_401K_TYPE)||strategy.getType()==1){//two ways to find the 401ks portfolio strategys
					tranType=true;
					List<VariableFor401k> vk=strategyManager.getVariable401KByStrategyID(strategy.getID());
					for(int k=0;k<vk.size();k++){
						if(vk.get(k).getSymbol()!=null){
							if(vk.get(k).getSymbol().equals(t.getSymbol())){
								if(vk.get(k).getDescription()!=null&&!vk.get(k).getDescription().equals(""))
									t.setDescription401k(vk.get(k).getDescription());//replace the symbol of description
								else
									t.setDescription401k(t.getSymbol());
								break;
							}//end if
						}
						else
							t.setDescription401k(t.getSymbol());
					}//end k_for  
					nt.add(t);
				}//end strategy_if
				else{
					tranType=false;
					nt.add(t);
				}
			}
			else if(profiles!=null&&profiles.size()>0){
				Long planID = profiles.get(0).getPlanID();
				Strategy proStrategy = strategyManager.get(planID);
				if(proStrategy!=null){
					if(proStrategy.getType().equals(Configuration.STRATEGY_401K_TYPE)||proStrategy.getType()==1){//two ways to find the 401ks portfolio strategys
						tranType=true;
						List<VariableFor401k> vk=strategyManager.getVariable401KByStrategyID(proStrategy.getID());
						for(int k=0;k<vk.size();k++){
							if(vk.get(k).getSymbol()!=null){
								if(vk.get(k).getSymbol().equals(t.getSymbol())){
									if(vk.get(k).getDescription()!=null&&!vk.get(k).getDescription().equals(""))
										t.setDescription401k(vk.get(k).getDescription());//replace the symbol of description
									else
										t.setDescription401k(t.getSymbol());
									break;
								}//end if
							}
							else
								t.setDescription401k(t.getSymbol());
						}//end k_for  
						nt.add(t);
					}//end strategy_if
					else{
						tranType=false;
						nt.add(t);
					}
				}
			}
			else
				nt.add(t);
		}
		return nt;
	}
	
	/**
	 * @author SuPing
	 * change the scheduledTransactions' date to tomorrow.
	 * @param transactions
	 */
	public static void predictScheduleTransDate(List<Transaction> transactions){
		if(transactions==null)return;
		for(int i=0; i<transactions.size(); i++){
			Transaction t = transactions.get(i);
			Date currentDate = t.getDate();
			Date futureDate = LTIDate.getNewNYSETradingDay(currentDate,1);
			t.setDate(futureDate);
		}
	}

	
}
