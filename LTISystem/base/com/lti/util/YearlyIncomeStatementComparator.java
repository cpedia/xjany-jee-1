package com.lti.util;

import com.lti.service.bo.YearlyIncomeStatement;
import java.util.Comparator;

public class YearlyIncomeStatementComparator implements Comparator{
	public int compare(Object o1,Object o2){
		YearlyIncomeStatement i1=(YearlyIncomeStatement)o1;
		YearlyIncomeStatement i2=(YearlyIncomeStatement)o2;
		return i1.getEndDate().compareTo(i2.getEndDate());
	}

}
