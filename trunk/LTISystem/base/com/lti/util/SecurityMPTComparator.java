/**
 * 
 */
package com.lti.util;

import java.util.Comparator;

import com.lti.service.bo.SecurityMPT;
import com.lti.type.SortType;

/**
 * @author CCD
 *
 */
public class SecurityMPTComparator implements Comparator{

	public SortType st;
	public SortType getSt() {
		return st;
	}
	public void setSt(SortType st) {
		this.st = st;
	}
	public SecurityMPTComparator(SortType st){
		this.st=st;
	}
	@Override
	public int compare(Object o1, Object o2) {
		SecurityMPT s1 = (SecurityMPT) o1;
		SecurityMPT s2 = (SecurityMPT) o2;

		// TODO Auto-generated method stub
		if(st== SortType.ALPHA){
			if(s1.getAlpha() < s2.getAlpha())
				return 1;
			return 0;
		}
		if(st== SortType.TREYNOR){
			if(s1.getTreynorRatio() < s2.getTreynorRatio())
				return 1;
			return 0;
		}
		if(st== SortType.ANNULIZEDRETURN){
			if(s1.getAR() < s2.getAR())
				return 1;
			return 0;
		}
		return 0;
	}
	
}
