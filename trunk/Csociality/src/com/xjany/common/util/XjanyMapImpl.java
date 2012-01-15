package com.xjany.common.util;

import java.util.ArrayList;
import java.util.List;


public class XjanyMapImpl<K,V> implements XjanyMap<K,V> {
	private List<XjanyMapEntry<K,V>> tentry;
	private int index = 0;
	private int number = 0;
	public XjanyMapImpl()
	{
		tentry = new ArrayList<XjanyMapEntry<K, V>>();
	}

	public void put(K key ,V value)
	{

		XjanyMapEntry<K,V> t2 = new XjanyMapEntry<K,V>();
		t2.setKey(key) ;
		t2.setValue(value);
		t2.setNumber(number);
		number ++ ;
		tentry.add(t2);
	}
	
	public XjanyMapEntry<K,V> get(int i) {
		
			return tentry.get(i);		
	}
	public int size()
	{
		
		return tentry.size();
	}
	public static void main(String[] args)
	{
		XjanyMapImpl<String,String> t = new XjanyMapImpl<String,String>();
		t.put("key1", "value1");
		t.put("key2", "value2");
		t.put("key3", "value3");
		t.put("key4", "value4");
		t.put("key5", "value5");

/*		for( int i = 0 ; i< t.size() ;i++ )
		{
			System.out.println(t.get(i).getKey() +"  --- "+ t.get(i).getValue());
		}*/
		
		for(XjanyMapEntry<String,String> e = t.next(); t.hasNext(); )
		{
			System.out.println( e.getKey() +" ------ "+ e.getValue());
		}
	}
	
	public void remove(int i)
	{		
		tentry.remove(i);
	}
	public void clear()
	{		
		tentry.clear();
	}
	public boolean hasNext()
	{
		System.out.println(index + " hasnext ");
		if(index >= tentry.size())
		{
			index = 0 ;
			return false;
		}
		index ++ ;
		return true;
	}
	
	public XjanyMapEntry<K,V> next()
	{
		System.out.println(index + " next ");
		return tentry.get(index);
	}

}
