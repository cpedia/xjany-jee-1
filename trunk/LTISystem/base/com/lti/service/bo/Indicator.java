package com.lti.service.bo;

import java.io.Serializable;
import java.util.Date;

import com.lti.service.IndicatorManager;
import com.lti.service.bo.base.BaseIndicator;
import com.lti.system.ContextHolder;

public class Indicator extends BaseIndicator implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public java.lang.Double getValue(Date date){
		
		IndicatorManager indicatorManager=(IndicatorManager) ContextHolder.getInstance().getApplicationContext().getBean("indicatorManager");
		
		return indicatorManager.getDailydata(this.getID(), date).getValue();
	}
	
	public Double getFirstValue(){
		
		IndicatorManager indicatorManager=(IndicatorManager) ContextHolder.getInstance().getApplicationContext().getBean("indicatorManager");
		
		IndicatorDailyData idd = indicatorManager.getFirstDailyData(this.getID());
		
		if(idd == null || idd.getValue() == null)return 0.0;
		
		return idd.getValue();
	}
	
	public Double getLastValue(){
		
		IndicatorManager indicatorManager=(IndicatorManager) ContextHolder.getInstance().getApplicationContext().getBean("indicatorManager");
		
		IndicatorDailyData idd = indicatorManager.getLastDailyData(this.getID());
		
		if(idd == null || idd.getValue() == null)return 0.0;
		
		return idd.getValue();
	}
		
}