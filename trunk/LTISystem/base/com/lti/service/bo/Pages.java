package com.lti.service.bo;

import com.lti.service.bo.base.BasePages;

public class Pages extends BasePages {

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Pages p2 = (Pages) obj;
		if(this.ID == p2.getID() && this.PageName.equals(p2.getPageName()) && this.Symbol.equals(p2.getSymbol()))
			return true;
		else
			return false;
	}
	

}
