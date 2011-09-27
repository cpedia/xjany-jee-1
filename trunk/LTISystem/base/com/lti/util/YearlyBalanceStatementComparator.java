package com.lti.util;

import com.lti.service.bo.YearlyBalanceStatement;
import java.util.Comparator;

public class YearlyBalanceStatementComparator implements Comparator{
	public int compare(Object o1,Object o2){
		YearlyBalanceStatement i1=(YearlyBalanceStatement)o1;
		YearlyBalanceStatement i2=(YearlyBalanceStatement)o2;
		return i1.getEndDate().compareTo(i2.getEndDate());
	}

}
