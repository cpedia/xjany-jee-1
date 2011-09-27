package com.lti.cache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ShowCacheStatus {

	public static List<Long> getKeys(){
		List<Long> list=new ArrayList<Long>();
		Iterator<Long> iter=YearCache.getInstance().caches.keySet().iterator();
		while(iter.hasNext()){
			list.add(iter.next());
		}
		return list;
	}
	
	public static void main(String[] args){
		Arrays.toString(com.lti.cache.ShowCacheStatus.getKeys().toArray());
	}

}
