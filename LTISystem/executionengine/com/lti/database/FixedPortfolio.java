package com.lti.database;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


import com.lti.service.PortfolioHoldingManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.PortfolioState;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.finance.Asset;
import com.lti.type.finance.HoldingInf;
import com.lti.util.ChartUtil;
import com.lti.util.CurveChartUtil;
import com.lti.util.LTIDate;

public class FixedPortfolio {

	public static PortfolioManager portfolioManager;
	public static PortfolioHoldingManager portfolioHoldingManager;
	public static SecurityManager securityManager;
	
	public static PortfolioDailyData getPDD(Date date,List<PortfolioDailyData> pdds){
		for(PortfolioDailyData pdd:pdds){
			if(LTIDate.equals(pdd.getDate(), date))return pdd;
		}
		return null;
	}
	
	public static void print(Portfolio p,String mes){
		System.out.println(p.getID()+": "+mes);
	}
	
	public static void fixPortfolio(Portfolio p)throws Exception{
		PortfolioState ps=portfolioManager.getPortfolioState(p.getID());
		if(ps==null||ps.getState()==null||(ps.getState()!=4&&ps.getState()!=6)){
			print(p,"portfolio state is null");
			return;
		}
		
		List<PortfolioDailyData> pdds=portfolioManager.getDailydatas(p.getID());
		//List<HoldingItem> his=portfolioHoldingManager.getAllHoldingItemsAfter(p.getID(), null);

		if(pdds==null||pdds.size()<60)return;
		
		//修复的所有的holding item
//		for(HoldingItem hi:his){
//			double price=0.0;
//			try {
//				price = securityManager.getPrice(hi.getSecurityID(), hi.getDate());
//			} catch (Exception e) {
//				print(p,"no price, "+hi.getSymbol());
//				return;
//			}
//			hi.setPrice(price);
//			hi.setPercentage(hi.getShare()*price/getPDD(hi.getDate(), pdds).getAmount());
//			portfolioHoldingManager.updateHoldingItem(hi);
//		}
		
		//重建delay holding
//		Date delayDate=LTIDate.getHoldingDateMonthEnd(p.getEndDate());
//		List<HoldingItem> dhis=portfolioHoldingManager.getHoldingItems(p.getID(), delayDate);
//		if(dhis==null){
//			print(p,"lack of delay holding item");
//			return;
//		}
//		PortfolioInf pif=new PortfolioInf();
//		pif.setType(Configuration.PORTFOLIO_HOLDING_DELAY);
//		HoldingInf hif=new HoldingInf(p);
//		for(HoldingItem hi:dhis){
//			Asset a=hif.getAsset(hi.getAssetName());
//			if(a==null){
//				a=new Asset();
//				a.setName(hi.getAssetName());
//				hif.addAsset(a);
//			}
//			a.getHoldingItems().add(hi);
//		}
//		hif.setCash(0);
//		hif.setCurrentDate(delayDate);
//		hif.refreshAmounts();
//		pif.setHolding(hif);
//		pif.setPortfolioID(p.getID());
//		portfolioManager.savePortfolioInf(pif);
		
		PortfolioInf dpif=portfolioManager.getPortfolioInf(p.getID(), Configuration.PORTFOLIO_HOLDING_DELAY);
		PortfolioInf cpif=portfolioManager.getPortfolioInf(p.getID(), Configuration.PORTFOLIO_HOLDING_CURRENT);
		if(dpif==null){
			print(p,"lack of delay holding item");
			return;
		}
		
		
		//修复各种图
		plotCurveChart(pdds, ps, p);
		ps.setDelayPieChart(plotHoldingChart(dpif.getHolding(), false));
		ps.setRealtimePieChart(plotHoldingChart(cpif.getHolding(), true));
		
		portfolioManager.updatePortfolioState(ps);
	}
	private static void plotCurveChart(List<PortfolioDailyData> pdds, PortfolioState ps,Portfolio p) throws Exception {
		double[] values = new double[pdds.size()];
		Date[] dates = new Date[pdds.size()];

		// 计算欲显示的纵坐标
		Calendar ca = Calendar.getInstance();
		ca.setTime(pdds.get(0).getDate());
		Date delayDate = LTIDate.getHoldingDateMonthEnd(pdds.get(pdds.size() - 1).getDate());
		int year = ca.get(Calendar.YEAR);
		int len = 0;
		for (int i = 0; i < pdds.size(); i++) {
			if (i == 0) {
				dates[i] = pdds.get(i).getDate();
			} else if (i == pdds.size() - 1) {
				dates[i] = pdds.get(i).getDate();
			} else {
				ca.setTime(pdds.get(i).getDate());
				int nyear = ca.get(Calendar.YEAR);
				if (nyear != year) {
					year = nyear;
					dates[i - 1] = pdds.get(i - 1).getDate();
				}
			}
			if (!delayDate.before(pdds.get(i).getDate()))
				len = i + 1;
			values[i] = pdds.get(i).getAmount();
		}
		ps.setRealtimeChart(CurveChartUtil.drawChart(p.getName(), dates, values, values.length));
		if (len != 0)
			dates[len - 1] = pdds.get(len - 1).getDate();
		ps.setDelayChart(CurveChartUtil.drawChart(p.getName(), dates, values, len));

	}
	
	
	private static byte[] plotHoldingChart(HoldingInf hif, boolean realtime) throws Exception {
		Map<String, Double> sectors = new TreeMap<String, Double>();
		double total = 0.0;
		boolean shortSell = false;
		if (hif.getAssets() != null) {
			Iterator<Asset> iter_asset = hif.getAssets().iterator();
			while (iter_asset.hasNext()) {
				Asset a = iter_asset.next();
				if (a.getHoldingItems() != null) {
					Iterator<HoldingItem> iter_si = a.getHoldingItems().iterator();
					while (iter_si.hasNext()) {
						HoldingItem si = iter_si.next();
						String symbol = si.getSymbol();
						Double per = sectors.get(symbol);
						if (per == null)
							per = 0.0;
						if (si.getPercentage() > 0.0) {
							double np=si.getPercentage() + per;
							if(np>1)np=1;
							sectors.put(symbol, np);
							total += si.getPercentage();
						} else {
							shortSell = true;
						}
					}
				}
			}
			if (hif.getCash() != 0.0) {
				Double per = sectors.get("CASH");
				if (per == null)
					per = 0.0;
				double nper = hif.getCash() / hif.getAmount();
				if (nper + per < 0.0001) {
					sectors.remove("CASH");
				} else {
					sectors.put("CASH", nper + per);
				}

				total += nper;
			}
		}
		if (shortSell) {
			Iterator<String> iter = sectors.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				double p=sectors.get(key) / total;
				if(p>1)p=1;
				sectors.put(key, p);
			}
		}

		return ChartUtil.PlotPie(hif.getPortfolioName(), sectors);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		FixedPortfolio.portfolioHoldingManager=ContextHolder.getPortfolioHoldingManager();
		FixedPortfolio.portfolioManager=ContextHolder.getPortfolioManager();
		FixedPortfolio.securityManager=ContextHolder.getSecurityManager();
		
		List<Portfolio> ps=FixedPortfolio.portfolioManager.getSimplePortfolios(-1, -1);
		for(Portfolio p:ps){
			if(p.getID()>6469&&p.getID()<19773)continue;
			long t1=System.currentTimeMillis();
			fixPortfolio(p);
			long t2=System.currentTimeMillis();
			System.out.println(p.getID()+"."+p.getName()+" Time: "+(t2-t1));
		}
	
	}

}
