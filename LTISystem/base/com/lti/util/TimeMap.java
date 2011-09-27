package com.lti.util;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;



public class TimeMap<K,V>  {
	
	private class DateElement{
		private long time;
		private V value;
		public long getTime() {
			return time;
		}
		public void setTime(long time) {
			this.time = time;
		}
		public V getValue() {
			return value;
		}
		public void setValue(V value) {
			this.value = value;
		}
		public DateElement(long time, V value) {
			super();
			this.time = time;
			this.value = value;
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((value == null) ? 0 : value.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final DateElement other = (DateElement) obj;
			if (value == null) {
				if (other.value != null)
					return false;
			} else if (!value.equals(other.value))
				return false;
			return true;
		}
		
	}
	
	private Queue<K> keyQueue = new LinkedList<K>();

	private Map<K,DateElement> map;
	public TimeMap(long timeOut,int size) {
		super();
		map=new HashMap<K,DateElement>();
		this.timeOut=timeOut;
		this.maxSize=size;
	}
	
	public TimeMap(){
		super();
		map=new HashMap<K,DateElement>();
	}

	private long timeOut=2*60*60*1000;
	private int maxSize=30000;
	private boolean fastMode=false;

	public void setTimeOut(long to){
		timeOut=to;
	}
	public void setMaxSize(int size){
		maxSize=size;
	}
	public void setFastMode(boolean f){
		fastMode=f;
	}
	
	public V get(K key) {
		DateElement de=map.get(key);
		if(de==null)return null;
		else if(System.currentTimeMillis()-de.time>timeOut){
			map.remove(key);
			if(fastMode)return de.value;
			return null;
		}
		else return de.value;
	}
	
	public V remove(K key){
		synchronized (this) {
			DateElement de=map.get(key);
			if(de==null)return null;
			map.remove(key);
			return de.value;
		}
	}

	public V put(K key, V value) {
		synchronized (this) {
			if(value!=null){
				map.put(key, new DateElement(System.currentTimeMillis(),value));
				keyQueue.add(key);
				if(map.size()>maxSize)clear();
			}
			return value;
		}
	}

	public void clear(){
		synchronized (this) {
			while (!keyQueue.isEmpty()) {
				K key = keyQueue.peek();
				DateElement de = map.get(key);
				if (System.currentTimeMillis() - de.time > timeOut) {
					map.remove(keyQueue.poll());
				} else
					break;
			}
			while (keyQueue.size() > maxSize) {
				map.remove(keyQueue.poll());
			}
		}
	}
	
	public static void main(String[] args)throws Exception{
		TimeMap<Long, Date> map=new TimeMap<Long, Date>();
		for(long i=0;i<60000;i++){
			map.put(i, new Date());
		}
		System.currentTimeMillis();
		
	}


}
