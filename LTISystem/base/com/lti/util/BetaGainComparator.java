package com.lti.util;
import java.util.Comparator;

import com.lti.service.bo.SecurityRanking;

public class BetaGainComparator implements Comparator{
	
	public int compare(Object o1,Object o2) {
		SecurityRanking p1=(SecurityRanking)o1;
		SecurityRanking p2=(SecurityRanking)o2; 
		if(p1.getBetaGain()<p2.getBetaGain())
			return 1;
		return 0;
	}
}
