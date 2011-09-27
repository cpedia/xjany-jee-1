/**
 * 
 */
package com.lti.util;

import java.util.Comparator;

import com.lti.service.bo.KeyValuePair;

/**
 * @author CCD
 *
 */
public class KeyValuePairComparator implements Comparator{
	@Override
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		KeyValuePair kvp1 = (KeyValuePair) o1;
		KeyValuePair kvp2 = (KeyValuePair) o2;
		if(kvp1.getValue() < kvp2.getValue())
			return 1;
		else
			return 0;
	}
}
