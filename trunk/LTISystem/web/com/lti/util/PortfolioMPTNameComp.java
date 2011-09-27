package com.lti.util;

import java.util.Comparator;

import com.lti.service.bo.PortfolioMPT;

public class PortfolioMPTNameComp implements Comparator<PortfolioMPT> {

	@Override
	public int compare(PortfolioMPT pm1, PortfolioMPT pm2) {
		// TODO Auto-generated method stub
		return pm1.getName().compareTo(pm2.getName());
	}

}
