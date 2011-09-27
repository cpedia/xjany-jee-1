package com.lti.util;

import java.util.Comparator;
import com.lti.service.bo.YearlyCashFlow;

public class YearlyCashFlowComparator implements Comparator{

	public int compare(Object o1,Object o2){
		YearlyCashFlow i1=(YearlyCashFlow)o1;
		YearlyCashFlow i2=(YearlyCashFlow)o2;
		return i1.getEndDate().compareTo(i2.getEndDate());
	}
}
