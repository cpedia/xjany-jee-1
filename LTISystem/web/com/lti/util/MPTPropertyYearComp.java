package com.lti.util;

import java.util.Comparator;

import com.lti.bean.MPTProperty;

public class MPTPropertyYearComp implements Comparator<MPTProperty> {

	@Override
	public int compare(MPTProperty m1, MPTProperty m2) {
		// TODO Auto-generated method stub
		Long year1 = m1.getYear();
		Long year2 = m2.getYear();
		return year1.compareTo(year2);
	}

}
