package com.lti.util.validate;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TableMap implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String title;
	private String symbol;
	private String source;
	private Map<String, Map<String, String>> rows = new HashMap<String, Map<String, String>>();

	public void clear() {
		rows.clear();
	}

	public boolean containsKey(String key) {
		if (rows.containsKey(key))
			return true;
		else {
			Iterator<String> iter = rows.keySet().iterator();
			while (iter.hasNext()) {
				Map col = rows.get(iter.next());
				if (col.containsKey(key))
					return true;
			}
		}
		return false;
	}

	public boolean containsValue(String value) {
		Iterator<String> iter = rows.keySet().iterator();
		while (iter.hasNext()) {
			Map col = rows.get(iter.next());
			if (col.containsValue(value))
				return true;
		}
		return false;
	}

	public String get(String rowKey,String colKey) {
		Map<String,String> m=rows.get(trim(rowKey));
		if(m!=null){
			return m.get(trim(colKey));
		}
		return null;
	}
	
	public Double getDouble(String rowKey,String colKey) {
		Map<String,String> m=rows.get(trim(rowKey));
		if(m!=null){
			try {
				return Double.parseDouble(m.get(trim(colKey)));
			} catch (RuntimeException e) {
			}
		}
		return null;
	}

	public boolean isEmpty() {
		return rows.isEmpty();
	}

	public Set rowKeySet() {
		return rows.keySet();
	}
	public Set colKeySet() {
		if(!rows.isEmpty()){
			Map m=rows.get(rows.keySet().iterator().next());
			if(m!=null)return m.keySet();
		}
		return null;
	}

	public void put(String rowKey, String colKey,String value) {
		if(value==null||value.equals(""))return;
		Map m=rows.get(trim(rowKey));
		if(m==null){
			m=new HashMap();
		}
		m.put(trim(colKey), value);
		rows.put(rowKey, m);
	}


	public void remove(String rowKey, String colKey) {
		Map m=rows.get(rowKey);
		if(m==null){
			return;
		}
		m.remove(colKey);
		rows.put(rowKey, m);
	}

	public int size() {
		int size=rows.size();
		if(size==1){
			Map m=rows.get(rows.keySet().iterator().next());
			if(m!=null)return m.size();
		}
		return size;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public void putAll(TableMap tm){
		if(tm!=null&&tm.size()!=0){
			rows.putAll(tm.getRows());
		}
	}

	public Map<String, Map<String, String>> getRows() {
		return rows;
	}

	public void setRows(Map<String, Map<String, String>> rows) {
		this.rows = rows;
	}
	public static String trim(String s){
		if(s==null)return null;
		char[] a=s.toCharArray();
		int start=0;
		while(isBlank(a[start]))start++;
		int end=a.length-1;
		while(isBlank(a[end]))end--;
		StringBuffer sb=new StringBuffer();
		for(int i=start;i<=end;i++){
			sb.append(a[i]);
		}
		return sb.toString();
	}
	public static boolean isBlank(char c){
		if(c=='\t'||c=='\n'||c==' '||c=='\r')return true;
		else return false;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
}
