package com.xjany.common.util;

import java.util.Iterator;


public interface XjanyMap<K,V> {
	
	public void put(K k,V v);
	public XjanyMapEntry<K, V> get(int i);
	public Iterator<XjanyMapEntry<K,V>> iterator();
	public void remove(int i);
	public void clear();
	public int size();
	
}
