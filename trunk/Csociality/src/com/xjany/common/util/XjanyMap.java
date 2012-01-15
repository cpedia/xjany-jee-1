package com.xjany.common.util;


public interface XjanyMap<K,V> {
	
	public void put(K k,V v);
	public XjanyMapEntry<K, V> get(int i);
}
