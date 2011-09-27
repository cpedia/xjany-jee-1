package com.lti.type;

import java.util.Comparator;


public class ReturnComparator implements Comparator{
	@Override
	public int compare(Object arg0, Object arg1) {
		double sv1 = (Double)arg0;
		double sv2 = (Double)arg1;
		
		if(sv1 > sv2)
			return 1;
		else
			return 0;
	}
}