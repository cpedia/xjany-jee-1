package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BasePortfolioDailyData;
import com.lti.system.Configuration;
import com.lti.util.DailyData;



public  class PortfolioDailyData extends BasePortfolioDailyData implements Serializable, DailyData{

	private static final long serialVersionUID = 1L;
	@Override
	public double getValue(int type) {
			return getAmount();
	}
}