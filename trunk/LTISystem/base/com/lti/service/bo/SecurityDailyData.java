package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseSecurityDailyData;
import com.lti.system.Configuration;
import com.lti.util.DailyData;

public class SecurityDailyData extends BaseSecurityDailyData implements Serializable, DailyData{

	private static final long serialVersionUID = 1L;

	@Override
	public double getValue(int type){
		if(type == Configuration.PRICE_TYPE_ADJNAV)
			return getAdjNAV();
		else if(type == Configuration.PRICE_TYPE_ADJCLOSE)
			return getAdjClose();
		return 0;
	}
}