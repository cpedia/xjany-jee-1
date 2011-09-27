package com.lti.database;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.lti.Exception.Security.NoPriceException;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.Security;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.finance.Asset;
import com.lti.type.finance.GPortfolio;
import com.lti.type.finance.HoldingInf;

public class SyncPortfolio {

	private final static String ROOT="/home/sshadmin/p";

	
	/**
	 * 获取相应的Holding
	 * @param p
	 * @param type Holding的类型
	 * @return
	 * @throws FileNotFoundException
	 * @throws NoPriceException
	 */
	public static PortfolioInf getPortfolioInf(Portfolio p, long type) throws FileNotFoundException, NoPriceException {
		long portfolioid = p.getID();
		com.lti.service.SecurityManager securityManager = ContextHolder.getSecurityManager();
		String filename = "P_O_HOLDING_" + portfolioid + ".xml";
		
		Date pricedate=p.getStartingDate();
		if (type == Configuration.PORTFOLIO_HOLDING_CURRENT) {
			filename = "P_C_HOLDING_" + portfolioid + ".xml";
			pricedate=p.getEndDate();
		} else if (type == Configuration.PORTFOLIO_HOLDING_EXPECTED) {
			filename = "P_S_HOLDING_" + portfolioid + ".xml";
			pricedate=p.getEndDate();
		}else if (type == Configuration.PORTFOLIO_HOLDING_DELAY) {
			filename = "P_D_HOLDING_" + portfolioid + ".xml";
			pricedate=p.getEndDate();
		}
		HoldingInf holdingInf = null;
		holdingInf = HoldingInf.getInstance(new FileReader(new File(ROOT,filename)));
		//如果为空的话
		if (holdingInf == null) {
			holdingInf = new HoldingInf(portfolioid, 10000.0, p.getStartingDate());
			System.out.println(p.getID()+" holding error" + portfolioid);
		} else {
			holdingInf.setBenchmarkSymbol(securityManager.get(holdingInf.getBenchmarkID()).getSymbol());
			boolean noprice=false;
			if (holdingInf.getAssets() != null && holdingInf.getAssets().size() != 0) {
				for (Asset ab : holdingInf.getAssets()) {
					if (ab.getHoldingItems() != null && ab.getHoldingItems().size() != 0) {
						for (HoldingItem hi : ab.getHoldingItems()) {

							Security s = securityManager.get(hi.getSecurityID());
							if (s == null) {
								System.out.println(p.getID()+" Error:no security.");
								hi.setPrice(0.0);
								continue;
							}
							hi.setSecurityID(s.getID());
							hi.setSymbol(s.getSymbol());
							double price;
							try {
								if(noprice){
									hi.setPrice(0.0);
								}else{
									price = securityManager.getPrice(s.getID(), pricedate);
									hi.setPrice(price);
								}
								
							} catch (Exception e) {
								System.out.println("Portfolio: "+p.getID()+"----"+s.getSymbol()+": "+p.getStartingDate());
								hi.setPrice(0.0);
								noprice=true;
							}
						}
					}
				}
			}

			holdingInf.refreshAmounts();
		}

		PortfolioInf pif = new PortfolioInf();
		pif.setHolding(holdingInf);
		pif.setPortfolioID(p.getID());
		pif.setType(type);

		return pif;
	}
	
	public static void syncportfolio(Portfolio p)throws Exception{
		GPortfolio gp = GPortfolio.getInstance(new FileReader(new File(ROOT, "P_"+p.getID() + ".xml")));
		if(gp==null){
			System.out.println("Null: "+p.getID());
			return;
		}
		Portfolio np = gp.toPortfolio();
		p.setType(np.getType());
		p.setAttributes(np.getAttributes());
		p.setOriginalPortfolioID(p.getOriginalPortfolioID());
		p.setStrategies(gp.getStrategies());
		pm.update(p);
		
		
		PortfolioInf opif = getPortfolioInf(p, Configuration.PORTFOLIO_HOLDING_ORIGINAL);
		pm.savePortfolioInf(opif);

		PortfolioInf cpif =null;
		try {
			cpif = getPortfolioInf(p, Configuration.PORTFOLIO_HOLDING_CURRENT);
			pm.savePortfolioInf(cpif);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}catch(Exception e){
			if(cpif!=null)System.out.println(cpif.getHolding().toXML());
			throw e;
		}
		
		PortfolioInf dpif =null;
		try {
			dpif = getPortfolioInf(p, Configuration.PORTFOLIO_HOLDING_DELAY);
			pm.savePortfolioInf(dpif);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}catch(Exception e){
			if(dpif!=null)System.out.println(dpif.getHolding().toXML());
			throw e;
		}
		
		try {
			PortfolioInf spif = getPortfolioInf(p, Configuration.PORTFOLIO_HOLDING_EXPECTED);
			pm.savePortfolioInf(spif);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void sync()throws Exception{
		pm = ContextHolder.getPortfolioManager();
		List<Portfolio> ps = pm.getPortfolios(-1, -1);
		for (Portfolio p : ps) {
			if (p.getOriginalPortfolioID() == null&&p.getID()!=0l) {
				try {
					pm.remove(p.getID());
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("ID:"+p.getID());
				System.out.println("O ID:"+p.getOriginalPortfolioID());
			}
		}

		System.out.println("delete ok.");
		ps = pm.getPortfolios();
		for (Portfolio p : ps) {
			if(p.getID()==0l)continue;
			long t1=System.currentTimeMillis();
			syncportfolio(p);
			long t2=System.currentTimeMillis();
			System.out.println("Time: "+(t2-t1)+"-"+p.getID());
			
		}
	}
	
	
	static PortfolioManager pm;
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		pm = ContextHolder.getPortfolioManager();
		sync();
	}

}
