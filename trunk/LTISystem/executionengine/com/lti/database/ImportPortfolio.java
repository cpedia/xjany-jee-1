package com.lti.database;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.lti.service.PortfolioManager;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.Security;
import com.lti.service.impl.DAOManagerImpl;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.executor.StrategyInf;
import com.lti.type.finance.Asset;
import com.lti.type.finance.GPortfolio;
import com.lti.type.finance.HoldingInf;
import com.lti.util.CSVReader;

public class ImportPortfolio {

	
	private static Long getLong(String s){
		if(s==null)return null;
		else{
			try {
				return Long.parseLong(s);
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}
	private static Integer getInt(String s){
		if(s==null)return null;
		else{
			try {
				return Integer.parseInt(s);
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}
	private static DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	private static java.util.Date getDate(String s){
		
		if(s==null)return null;
		else{
			try {
				return df.parse(s);
			} catch (Exception e) {
				return null;
			}
		}
	}
	
	public void savebasicportfolio(Portfolio np,GPortfolio gp){
		np.setID(gp.getID());
		np.setName(gp.getName());
		np.setDescription(gp.getDescription());
		np.setUserID(gp.getUserID());
		np.setMainStrategyID(gp.getMainStrategyID());
		np.setEndDate(gp.getEndDate());
		np.setOriginalPortfolioID(gp.getOriginalPortfolioID());
		np.setStartingDate(gp.getStartingDate());
		np.setCategories(gp.getCategories());
		np.setLastTransactionDate(gp.getLastTransactionDate());
		np.setUserName(gp.getUserName());
		np.setDelayLastTransactionDate(gp.getDelayLastTransactionDate());
		np.setType(gp.getType());
		np.setAttributes(gp.getAttributes());
		np.setStrategies(gp.getStrategies());
		np.setState(gp.getState());
		pm.update(np);
	}
	
	public static PortfolioManager pm=ContextHolder.getPortfolioManager();
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		System.out.println("Ed's IRA #1  Vanguard ETFs Tactical Asset Allocation Conservative".replace("'", "\\'"));
		
		List<Portfolio> ps=pm.getSimplePortfolios(-1,-1);
		for(Portfolio p:ps){
			if(p.getOriginalPortfolioID()==null){
				//pm.remove(p.getID());
				List<String> sqls = new ArrayList<String>();
				sqls.add("delete from " + Configuration.TABLE_PORTFOLIO + " where id=" + p.getID());
				((DAOManagerImpl)pm).executeSQL(sqls);
				System.out.println(p.getID());
			}
		}
		
		
		List<Portfolio> portfolios=pm.getSimplePortfolios(-1,-1);
		
		for(Portfolio p:portfolios){
//			if(p==null){
//				DAOManagerImpl dao=(DAOManagerImpl) pm;
//				String sql="insert into ltisystem_portfolio(id,name,userid,type,state) values ("+portfolioid+",'"+line.get(1).replace("'", "\\'")+"',1,0,0)";
//				System.out.println(sql);
//				dao.executeSQL(sql);
//				p=pm.get(portfolioid);
//			}
			long portfolioid=p.getID();
			
			
			
			StrategyInf sif=null;
			try {
				sif = StrategyInf.getInstance(new FileReader(new File("f:/portfolios/p2/P_STRATEGIES_"+portfolioid+".xml")));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			if(sif==null){
				sif=new StrategyInf();
				System.out.println("strategy error"+portfolioid);
			}
			p.setStrategies(sif);
			
			com.lti.service.SecurityManager securityManager=ContextHolder.getSecurityManager();
			
			HoldingInf holdingInf=HoldingInf.getInstance(new FileReader(new File("f:/portfolios/p2/P_O_HOLDING_"+portfolioid+".xml")));
			if(holdingInf==null){
				holdingInf=new HoldingInf(portfolioid, 10000.0, p.getStartingDate());
				System.out.println("holding error"+portfolioid);
			}else{
				holdingInf.setBenchmarkSymbol(securityManager.get(holdingInf.getBenchmarkID()).getSymbol());
				
				if(holdingInf.getAssets()!=null&&holdingInf.getAssets().size()!=0){
					for(Asset ab:holdingInf.getAssets()){
						if(ab.getHoldingItems()!=null&&ab.getHoldingItems().size()!=0){
							for(HoldingItem hi:ab.getHoldingItems()){
								
								Security s=securityManager.get(hi.getSecurityID());
								if(s==null){
									s=securityManager.get("spy");
									System.out.println("	security");
								}
								hi.setSecurityID(s.getID());
								hi.setSymbol(s.getSymbol());
								double price;
								try {
									price = securityManager.getPrice(s.getID(), p.getStartingDate());
									hi.setPrice(price);
								} catch (Exception e) {
									s=securityManager.get("spy");
									hi.setSecurityID(s.getID());
									hi.setSymbol(s.getSymbol());
									price = securityManager.getPrice(s.getID(), p.getStartingDate());
									hi.setPrice(price);
								}
							}
						}
					}
				}
				
				holdingInf.refreshAmounts();
			}
			
			PortfolioInf pif=new PortfolioInf();
			pif.setHolding(holdingInf);
			pif.setPortfolioID(portfolioid);
			pif.setType(Configuration.PORTFOLIO_HOLDING_ORIGINAL);
			pm.update(p);
			pm.savePortfolioInf(pif);
			System.out.println("done..."+p.getName()+"..."+p.getID());
		}
	}

}
