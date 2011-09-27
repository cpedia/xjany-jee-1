package com.lti.util;

import java.util.Comparator;
import com.lti.service.bo.PortfolioDailyData;

public class PortfolioIDComp implements Comparator {

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 * use to sort the portfolio daily date in the order of portfolio ID
	 */
	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		PortfolioDailyData p1 = (PortfolioDailyData) o1;
		PortfolioDailyData p2 = (PortfolioDailyData) o2;
		if(p1.getPortfolioID() > p2.getPortfolioID())
			return 0;
		else
			return 1;
	}

}
