package com.lti.util;

import java.util.Comparator;
import java.util.List;

import com.lti.service.bo.SecurityMPT;

public class SecurityMPTExtraComp implements Comparator<SecurityMPT> {

	@Override
	public int compare(SecurityMPT s1, SecurityMPT s2) {
		// TODO Auto-generated method stub
		List<String> extra1 = s1.getExtras();
		List<String> extra2 = s2.getExtras();
		if(extra1 == null && extra2 == null)
			return 0;
		else if(extra1 == null && extra2 != null)
			return -1;
		else if(extra1 != null && extra2 ==null)
			return 1;
		else if(extra1.size() < extra2.size())
			return -1;
		else if(extra1.size() > extra2.size())
			return 1;
		String e1 = extra1.get(extra1.size() - 1);
		String e2 = extra2.get(extra2.size() - 1);
		if(e1 == null && e2==null)
			return 0;
		else if(e1 == null && e2 != null)
			return -1;
		else if(e1 != null && e2 == null)
			return 1;
		else{
			Double d1 = Double.parseDouble(e1);
			Double d2 = Double.parseDouble(e2);
			return d1.compareTo(d2);
		}
	}

}
