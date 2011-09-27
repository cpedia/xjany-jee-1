/**
 * 
 */
package com.lti.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.lti.service.bo.KeyValuePair;
import com.lti.service.bo.SecurityMPT;

/**
 * @author CCD
 *
 */
public class NumberUtil {
	public static int getDifferentSize(List<KeyValuePair> kvpList){
		HashSet<Double> valueSet = new HashSet<Double>();
		int size=0;
		for(int i=0;i<kvpList.size();++i){
			Double value= kvpList.get(i).getValue();
			if(!valueSet.contains(value)){
				++size;
				valueSet.add(value);
			}
		}
		return size;
	}
	
	public static void getRank(List<KeyValuePair> kvpList, HashMap<Long, Double> scoreMap){
		Double delta = 1.0/ kvpList.size();
		Double curScore = 1.0;
		for(int i=0;i<kvpList.size();++i){
			scoreMap.put(kvpList.get(i).getKey(), curScore);
			curScore-=delta;
		}
	}
}
