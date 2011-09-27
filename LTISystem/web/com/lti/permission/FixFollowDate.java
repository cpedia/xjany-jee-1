package com.lti.permission;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.PortfolioFollowDate;
import com.lti.service.bo.UserResource;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class FixFollowDate {
	
	public void fix(){
		UserManager userManager = ContextHolder.getUserManager();
		PortfolioManager portfolioManager = ContextHolder.getPortfolioManager();
		List<PortfolioFollowDate> portfolioFollowDateList = portfolioManager.getPortfolioFollowDates();
		if(portfolioFollowDateList != null){
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			for(PortfolioFollowDate portfolioFollowDate : portfolioFollowDateList){
				Long portfolioID = portfolioFollowDate.getPortfolioID();
				String datesStr = portfolioFollowDate.getDateString();
				String[] dates = datesStr.split("#");
				try {
					Date lastFollowDate = df.parse(dates[dates.length - 1]);
					UserResource userResource = userManager.getCustomizePortfolioResourceByPortfolioID(portfolioID);
					if(userResource != null) {//是这个用户创建的
						userResource = userManager.getUserResourceByUserIDAndResourceIDAndResourceType(userResource.getUserID(), portfolioID, Configuration.USER_RESOURCE_PORTFOLIO_FOLLOW);
						if(userResource != null){
							userResource.setUpdateTime(lastFollowDate);
							userManager.updateUserResourse(userResource);
						}
					}
				} catch (ParseException e) {
					System.out.println("Follow, Date," + portfolioFollowDate.getPortfolioID());
				}
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FixFollowDate ffd = new FixFollowDate();
		ffd.fix();
	}

}
