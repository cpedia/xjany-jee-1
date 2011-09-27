package com.lti.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Strategy;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.StringUtil;

public class CloningCenterManager {

//	public final static String Clone_13F_Strategy="CLONE_13F";
//	
//	/**
//	 * if the fundName exists, only update the category and description
//	 * @param fundName
//	 * @param _13fName
//	 * @param startingDate
//	 * @param category
//	 * @param description
//	 * @throws Exception
//	 */
//	public static void create13FPortfolio(String fundName,String _13fName,Date startingDate,String category,String description)throws Exception{
//		PortfolioManager pm=ContextHolder.getPortfolioManager();
//		StrategyManager sm=ContextHolder.getStrategyManager();
//		
//		Portfolio p=pm.getOriginalPortfolio(fundName);
//		if(p==null){
//			Portfolio clone13F=pm.get(0L).clone();
//			clone13F.setID(null);
//			if(startingDate!=null){
//				clone13F.setStartingDate(startingDate);
//			}else{
//				List<Date> dlist=pm.findBySQL("SELECT datefiled FROM "+Configuration.TABLE_COMPANY_INDEX+" l where companyname ='"+_13fName+"' and formtype='13f-hr' order by datefiled asc limit 0,1;");
//				if(dlist!=null&&dlist.size()==1){
//					SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
//					Date d=sdf.parse(sdf.format(dlist.get(0)));
//					clone13F.setStartingDate(d);
//				}
//			}
//			
//			clone13F.setName(fundName);
//			clone13F.setCash(clone13F.getOriginalAmount());
//			clone13F.setState(Configuration.PORTFOLIO_STATE_ALIVE);
//			
//			Strategy clone13FStrategy=sm.get(Clone_13F_Strategy);
//			clone13F.setAssetAllocationStrategyID(clone13FStrategy.getID());
//			Hashtable<String, String> parameters=new Hashtable<String, String>();
//			parameters.put("name", _13fName.trim());
//			clone13F.setAssetAllocationParameter(parameters);
//			
//			clone13F.setIsModelPortfolio(true);
//			clone13F.setIsOriginalPortfolio(true);
//			clone13F.setMainStrategyID(clone13FStrategy.getID());
//			clone13F.setUserID(clone13FStrategy.getUserID());
//			clone13F.setCategories(category);
//			clone13F.setDescription(description);
//			
//			pm.add(clone13F);
//		}else{
//			p.setCategories(StringUtil.sortCategories(category));
//			p.setDescription(description);
//			
//			Hashtable<String, String> parameters=p.getAssetAllocationParameter();
//			if(parameters==null)parameters=new Hashtable<String, String>();
//			parameters.put("name", _13fName.trim());
//			p.setAssetAllocationParameter(parameters);
//			
//			Strategy clone13FStrategy=sm.get(Clone_13F_Strategy);
//			if(!clone13FStrategy.getID().equals(p.getAssetAllocationStrategyID())){
//				throw new Exception("You are changing a portfolio not created by clone center.");
//			}
//			
//			p.setState(Configuration.PORTFOLIO_STATE_ALIVE);
//			
//			pm.update(p);
//			Portfolio c=pm.get(p.getPortfolioID());
//			c.setCategories(StringUtil.sortCategories(category));
//			c.setDescription(description);
//			
//			Hashtable<String, String> oparameters=c.getAssetAllocationParameter();
//			if(oparameters==null)oparameters=new Hashtable<String, String>();
//			oparameters.put("name", _13fName.trim());
//			c.setAssetAllocationParameter(oparameters);
//			
//			c.setState(Configuration.PORTFOLIO_STATE_ALIVE);
//			
//			pm.update(c);
//		}
//	}
//	
//	public static List<String[]> get13FName(String _name){
//		PortfolioManager pm=ContextHolder.getPortfolioManager();
//		
//		try {
//			_name=_name.replace("\\", "\\\\").replace("'", "\\'");
//			
//			List<String[]> olist=new ArrayList<String[]>();
//			List<String> list=pm.findBySQL("SELECT distinct companyname FROM "+Configuration.TABLE_COMPANY_INDEX+" l where companyname like '"+_name+"%' and formtype='13f-hr';");
//			if(list!=null){
//				for(int i=0;i<list.size();i++){
//					String name=list.get(i).replace("\\", "\\\\").replace("'", "\\'");
//					String[] strs=new String[2];
//					strs[0]=list.get(i);
//					List<Object> dlist=pm.findBySQL("SELECT datefiled FROM "+Configuration.TABLE_COMPANY_INDEX+" l where companyname ='"+name+"' and formtype='13f-hr' order by datefiled asc limit 0,1;");
//					if(dlist!=null&&dlist.size()==1){
//						strs[1]=dlist.get(0).toString();
//					}
//					olist.add(strs);
//				}
//			}
//			return olist;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//	public static void executeCloning13F(String[] nameArray){
//		
//	}
//	
//	public static List<Portfolio> get13FPortfolios(){
//		StrategyManager sm=ContextHolder.getStrategyManager();
//		Strategy clone13FStrategy=sm.get(Clone_13F_Strategy);
//		return sm.getModelPortfolios(clone13FStrategy.getID());
//	}
//	
//	public static Strategy get13FStrategy(){
//		StrategyManager sm=ContextHolder.getStrategyManager();
//		Strategy clone13FStrategy=sm.get(Clone_13F_Strategy);
//		return clone13FStrategy;
//	}
//	public static List<Portfolio> get13FPortfolios(String categories,String keyword){
//		StrategyManager sm=ContextHolder.getStrategyManager();
//		Strategy clone13FStrategy=sm.get(Clone_13F_Strategy);
//		PortfolioManager pm=ContextHolder.getPortfolioManager();
//		return pm.getPortfoliosByCategory(categories, keyword,clone13FStrategy.getID(), null, null);
//	}	
//	
//	public static Portfolio get13FPortfolio(String fundName){
//		PortfolioManager pm=ContextHolder.getPortfolioManager();
//		return pm.get(fundName);
//	}
//	
//	public static void main(String[] args){
//		try {
//			List<Portfolio> ports=CloningCenterManager.get13FPortfolios();
//			PortfolioManager pm=ContextHolder.getPortfolioManager();
//			String[] categories={"1.Free","1.Indexes","1.Value","1.Sector Picks","1.Cap Picks","1.Low Turnover" ,"1.High Concentration" ,"1.Endowments","1.International" };
//			String[] Cap_Picks={"2.Less than $500 million","2.$500 million to $1 billion","2.$1 billion to $5 billion","2.$5 billion to $10 billion","2.$10 billion to $20 billion","2.$20 billion to $50 billion","2.Greater than $50 billion"};
//			String[] Sector_Picks={"2.Basic Materials","2.Capital Goods","2.Conglomerates"};
//			for(int i=0;i<ports.size();i++){
//				Portfolio p=ports.get(i);
//				String category=categories[i%categories.length];
//				if(category.equals("1.Sector Picks")){
//					category+=","+Sector_Picks[i%Sector_Picks.length];
//				}
//				if(category.equals("1.Cap Picks")){
//					category+=","+Cap_Picks[i%Cap_Picks.length];
//				}
//				Hashtable<String, String> parameters=new Hashtable<String, String>();
//				parameters.put("name", p.getName().substring(6));
//				p.setAssetAllocationParameter(parameters);
//				p.setCategories(StringUtil.sortCategories(category));
//				pm.update(p);
//				Portfolio o=pm.getOriginalPortfolio(p.getID());
//				o.setCategories(category);
//				o.setAssetAllocationParameter(parameters);
//				pm.update(o);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
}
