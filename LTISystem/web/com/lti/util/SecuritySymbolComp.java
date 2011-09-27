package com.lti.util;

import java.util.Comparator;

import com.lti.service.bo.Security;

public class SecuritySymbolComp implements Comparator<Security> {

	@Override
	public int compare(Security s1, Security s2) {
		// TODO Auto-generated method stub
		return s1.getSymbol().compareTo(s2.getSymbol());
	}

}
