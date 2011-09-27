package com.lti.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ListElementMap implements Map {

	private java.util.HashMap map;

	public ListElementMap() {
		super();
		map = new HashMap();
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		map.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		// TODO Auto-generated method stub
		return map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		// TODO Auto-generated method stub
		java.util.Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			java.util.List list = (List) map.get(iter.next());
			if (list.contains(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set entrySet() {
		// TODO Auto-generated method stub
		return map.entrySet();
	}

	@Override
	public Object get(Object key) {
		// TODO Auto-generated method stub
		List list = new ArrayList();
		java.util.Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String k = (String) iter.next();
			if (k.startsWith((String) key)) {
				List l = (List) map.get(k);
				list.addAll(l);
			}

		}
		return list;
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return map.isEmpty();
	}

	@Override
	public Set keySet() {
		// TODO Auto-generated method stub
		return map.keySet();
	}

	@Override
	public Object put(Object key, Object value) {
		// TODO Auto-generated method stub
		List l = (List) map.get(key);
		if (l == null)
			l = new ArrayList();
		if(value instanceof java.util.List){
			l.addAll((List)value);
		}else{
			l.add(value);
		}
		map.put(key, l);
		return null;
	}

	@Override
	public void putAll(Map m) {
		// TODO Auto-generated method stub
		if(m!=null){
			java.util.Iterator iter=m.keySet().iterator();
			if(iter.hasNext()){
				String key=(String) iter.next();
				map.put(key, m.get(key));
			}
		}
	}

	@Override
	public Object remove(Object key) {
		// TODO Auto-generated method stub
		Object obj=get(key);
		if(obj!=null){
			java.util.Iterator iter = map.keySet().iterator();
			while (iter.hasNext()) {
				String k = (String) iter.next();
				if (k.startsWith((String) key)) {
					map.remove(key);
				}

			}
		}
		return obj;
			
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		int size=0;
		java.util.Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String k = (String) iter.next();
			List l=((List)map.get(k));
			if(l!=null)size+=l.size();
		}
		return size;
	}

	@Override
	public Collection values() {
		// TODO Auto-generated method stub
		List list=new ArrayList();
		java.util.Iterator iter = map.keySet().iterator();
		while (iter.hasNext()) {
			String k = (String) iter.next();
			list.addAll((List)map.get(k));

		}
		return list;
	}

}
