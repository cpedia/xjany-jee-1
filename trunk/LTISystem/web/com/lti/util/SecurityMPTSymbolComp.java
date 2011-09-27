package com.lti.util;

import java.util.Comparator;

import com.lti.service.bo.SecurityMPT;

public class SecurityMPTSymbolComp implements Comparator<SecurityMPT> {

	@Override
	public int compare(SecurityMPT s1, SecurityMPT s2) {
		// TODO Auto-generated method stub
		return s1.getSymbol().compareTo(s2.getSymbol());
	}

}