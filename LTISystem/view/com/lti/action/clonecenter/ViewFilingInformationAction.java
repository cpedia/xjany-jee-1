package com.lti.action.clonecenter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class ViewFilingInformationAction {
	private Long ID;
	
	private PortfolioManager portfolioManager;
	private UserManager userManager;
	public static SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd/yyyy");
	private String lastPeriod;
	private String firstPeriod;
	
	private String totalCounts;
	private String totalReturn;
	private String totalAmount;
	private String lastValidDate;

	@Deprecated
	public String execute(){
//		portfolioManager=ContextHolder.getPortfolioManager();
//		try {
//			String sql1="select date from "+Configuration.TABLE_TRANACTION+" where portfolioid="+ID+" order by date desc limit 0,1";
//			List<Date> last=portfolioManager.findBySQL(sql1);
//			String sql2="select date from "+Configuration.TABLE_TRANACTION+" where portfolioid="+ID+" order by date asc limit 0,1";
//			List<Date> first=portfolioManager.findBySQL(sql2);
//			if(last!=null&&last.size()>0){
//				lastPeriod=sdf2.format(last.get(0));
//			}
//			if(first!=null&&first.size()>0){
//				firstPeriod=sdf2.format(first.get(0));
//			}
//			Portfolio portfolio=portfolioManager.get(ID);
//			totalCounts=portfolio.getSecurities().size()+"";
//			totalReturn=portfolioManager.computeTotalReturn(portfolio.getID())+"";
//			totalAmount=portfolio.getTotalAmount(portfolio.getEndDate())+"";
//			lastValidDate=sdf2.format(portfolio.getEndDate());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		return "success";
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long id) {
		ID = id;
	}

	public String getLastPeriod() {
		return lastPeriod;
	}

	public void setLastPeriod(String lastPeriod) {
		this.lastPeriod = lastPeriod;
	}

	public String getFirstPeriod() {
		return firstPeriod;
	}

	public void setFirstPeriod(String firstPeriod) {
		this.firstPeriod = firstPeriod;
	}

	public String getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(String totalCounts) {
		this.totalCounts = totalCounts;
	}

	public String getTotalReturn() {
		return totalReturn;
	}

	public void setTotalReturn(String totalReturn) {
		this.totalReturn = totalReturn;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getLastValidDate() {
		return lastValidDate;
	}

	public void setLastValidDate(String lastValidDate) {
		this.lastValidDate = lastValidDate;
	}


}
