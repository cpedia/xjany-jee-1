package com.lti.jobscheduler;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.edgar.EdgarUtil;
import com.lti.service.AssetClassManager;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.PortfolioState;
import com.lti.service.bo.Security;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.AssetClassCachingUtil;
import com.lti.util.LTIDate;
import com.lti.util.LTIDownLoader;
import com.lti.util.Parse401KParameters;
import com.lti.util.SecurityCachingUtil;

public class DailyProcessorJob extends TimerTask{
	static Log log = LogFactory.getLog(DailyProcessorJob.class);
	
	public void run() {
		
		try{
			System.out.println("start charge user group.");
			UserManager userManager=ContextHolder.getUserManager();
			userManager.changeUserGroup();
			System.out.println("end charge user group.");
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		PortfolioManager portfolioManager=ContextHolder.getPortfolioManager();
		
		//action 1, to clean temporally portfolios
		log.info("Start clearing portfolio ...");
		log.info("End clearing portfolio ...");
		
		//action 2, reset portfolio state
		try{
			String mode=(String)Configuration.get(Configuration.DAILY_EXECUTION_RESET_MODE);
			if(Configuration.DAILY_EXECUTION_RESET_MODE_WEEK_END.equals(mode)&&LTIDate.isWeekDay(new Date(),Calendar.SATURDAY)){
				log.info("[Reset Portfolio]Start to check portfolio state on week end.");
				resetPortfolioState();
				log.info("[Reset Portfolio]Complete check portfolio state on week end.");
			}else if(Configuration.DAILY_EXECUTION_RESET_MODE_MONTH_END.equals(mode)&&LTIDate.isMonthEnd(new Date())){
				log.info("[Reset Portfolio]Start to check portfolio state on Month end.");
				resetPortfolioState();
				log.info("[Reset Portfolio]Complete check portfolio state on Month end.");
				
			}
		}catch(Exception e){
			log.error("Reset Portfolio]",e);
		}
		
		log.info("start caching asset class...");
		//AssetClassManager acm=ContextHolder.getAssetClassManager();
		//List<AssetClass> acs=acm.getClasses();
		try {
			//AssetClassCachingUtil.index(acs);
			AssetClassCachingUtil.initialize();
		} catch (Exception e1) {
			log.info(e1);
		}
		
		log.info("start caching security...");
		//com.lti.service.SecurityManager sm=ContextHolder.getSecurityManager();
		//List<Security> ss=sm.getSecurities();
		try {
			//SecurityCachingUtil.index(ss);
			SecurityCachingUtil.intialize();
		} catch (Exception e1) {
			log.info(e1);
		}
		
		log.info("start caching...");
		Parse401KParameters.initCache();
		
		//action 3, update 13f index
		log.info("Start to update company index ...");
		try {
			EdgarUtil.updateCompanyIndexDateBase();
			log.info("End to update company index ...");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Update company index failed ...");
			log.error(e);
		}
		/**don't set it in validfi.com**/
		//action 4, update beta gain
//		com.lti.util.LTIDownLoader downloader=new com.lti.util.LTIDownLoader();
//		log.info("Start to update beta gain ...");
//		try{
//			downloader.updateDailyFactorBetaGain();
//			downloader.updateMonthlyHistoricalBetaGain();
//			log.info("End to update beta gain...");
//		}catch(Exception e){
//			e.printStackTrace();
//			log.info("Update beta gain failed...");
//			log.error(e);
//		}
		
		
	}

	private void resetPortfolioState(){
		PortfolioManager portfolioManager=ContextHolder.getPortfolioManager();
		
		Date lastUpdate=Configuration.getDailyExecutionResetDate();
		boolean update=true;
		if(lastUpdate!=null){
			Calendar ca=Calendar.getInstance();
			int w1=ca.get(Calendar.DAY_OF_YEAR);
			ca.setTime(lastUpdate);
			int w2=ca.get(Calendar.DAY_OF_YEAR);
			if(w1==w2)update=false;
		}
		if(update){
			Configuration.setDailyExecutionResetDate(new Date());
			List<PortfolioState> pses=portfolioManager.getPortfolioStates();
			for(int i=0;i<pses.size();i++){
				PortfolioState ps=pses.get(i);
				if(ps!=null){
					ps.setState(Configuration.PORTFOLIO_RUNNING_STATE_INACTIVE);
					portfolioManager.updatePortfolioState(ps);
				}
			}
			log.info("[Reset Portfolio]Reset portfolio state successfully");
		}else{
			log.info("[Reset Portfolio]Nothing to do.");
		}
	}

}
