package com.lti.util;

import java.util.Comparator;

import com.lti.service.bo.BalanceStatement;

public class BalanceStatementComparator implements Comparator{
	
	public int compare(Object o1,Object o2){
		BalanceStatement b1=(BalanceStatement)o1;
		BalanceStatement b2=(BalanceStatement)o2;
		return b1.getDate().compareTo(b2.getDate());
	}

}