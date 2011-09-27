/**
 * 
 */
package com.lti.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import com.lti.service.PortfolioManager;
import com.lti.service.bo.HoldingRecord;
import com.lti.service.bo.PortfolioInf;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.finance.HoldingInf;
import com.lti.service.bo.HoldingItem;
/**
 * @author CCD
 *
 */
public class InitialHoldingRecord {

	public void initialize(){
		PortfolioManager portfolioManager = (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		List<PortfolioInf> pifList = portfolioManager.getPortfolioInfs(Configuration.PORTFOLIO_HOLDING_CURRENT);
		if(pifList != null && pifList.size() > 0){
			int curCount = 1;
			for(PortfolioInf pif: pifList){
				System.out.println(curCount + "/" + pifList.size());
				curCount++;
				if(portfolioManager.hasHoldingRecord(pif.getPortfolioID()))//已有holding record记录的不考虑
					continue;
				HoldingInf hInf = pif.getHolding();
				if(hInf.getHoldingItems() != null){
					List<HoldingRecord> hrList = new ArrayList<HoldingRecord>();
					Date startDate = LTIDate.getNewNYSETradingDay(hInf.getCurrentDate(), -7);
					for(HoldingItem hi: hInf.getHoldingItems()){
						HoldingRecord  hr = new HoldingRecord();
						hr.setPortfolioID(pif.getPortfolioID());
						hr.setStartDate(startDate);
						hr.setSecurityID(hi.getSecurityID());
						hrList.add(hr);
					}
					portfolioManager.saveAll(hrList);
				}
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		InitialHoldingRecord ihr = new InitialHoldingRecord();
		ihr.initialize();
	}

}
