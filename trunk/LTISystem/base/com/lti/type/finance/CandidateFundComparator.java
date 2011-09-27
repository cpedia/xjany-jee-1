package com.lti.type.finance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CandidateFundComparator implements Comparator<CandidateFundType> {

	@Override
	public int compare(CandidateFundType o1, CandidateFundType o2) {
		if(o1.getScore()>o2.getScore())return -1;
		else if(o1.getScore()==o2.getScore())return 0;
		else return 1;
	}
	
	public static void main(String[] args){
		CandidateFundType cf1=new CandidateFundType();
		cf1.setScore(0.1);
		CandidateFundType cf2=new CandidateFundType();
		cf2.setScore(0.2);
		CandidateFundType cf3=new CandidateFundType();
		cf3.setScore(0.4);
		CandidateFundType cf4=new CandidateFundType();
		cf4.setScore(0.3);
		List<CandidateFundType> cfs=new ArrayList<CandidateFundType>();
		cfs.add(cf1);
		cfs.add(cf2);
		cfs.add(cf3);
		cfs.add(cf4);
		Collections.sort(cfs, new CandidateFundComparator());
		for(CandidateFundType cfa:cfs){
			System.out.println(cfa.getScore());
		}
	}
}
