package com.lti.util;

import java.util.Comparator;
import com.lti.service.bo.IncomeStatement;

public class IncomeStatementComparator implements Comparator{
	
	public int compare(Object o1,Object o2){
		IncomeStatement i1=(IncomeStatement)o1;
		IncomeStatement i2=(IncomeStatement)o2;
		return i1.getDate().compareTo(i2.getDate());
	}

}

