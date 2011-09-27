package com.lti.bean;

import java.util.ArrayList;
import java.util.List;

import com.lti.type.BLItem;

public class BLCovarienceBean {
	
	List<BLRowBean> covarience;

	public List<BLRowBean> getCovarience() {
		return covarience;
	}

	public void setCovarience(List<BLRowBean> covarience) {
		this.covarience = covarience;
	}
	
	public BLCovarienceBean(){		
		covarience = new ArrayList<BLRowBean>();
	}
}
