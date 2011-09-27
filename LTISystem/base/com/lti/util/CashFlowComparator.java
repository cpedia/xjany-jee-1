package com.lti.util;

import java.util.Comparator;

import com.lti.service.bo.CashFlow;;

public class CashFlowComparator implements Comparator{
	
	public int compare(Object o1,Object o2){
		CashFlow c1=(CashFlow)o1;
		CashFlow c2=(CashFlow)o2;
		return c1.getDate().compareTo(c2.getDate());
	}

}
