package com.lti.type;

import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

public class StringToMap implements java.io.Serializable, org.hibernate.usertype.UserType {

	private static final long serialVersionUID = 6580947600412151430L;

	private static final int[] TYPES = new int[] { Types.VARCHAR };

	public int[] sqlTypes() {

		return TYPES;
	}

	@SuppressWarnings("unchecked")
	public Class returnedClass() {
		return Map.class;
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object objx, Object objy) throws HibernateException {
		if (objx == null && objy == null)
			return true;

		if (objx == null || objy == null)
			return false;

		Map<String, String> m1 = (Map<String, String>) objx;
		Map<String, String> m2 = (Map<String, String>) objy;

		if (m1.size() != m2.size())
			return false;

		Iterator<String> keys = m1.keySet().iterator();
		while (keys.hasNext()) {
			String key = keys.next();
			if (!m1.get(key).equals(m2.get(key)))
				return false;
		}
		return true;
	}

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		String value = (String) Hibernate.STRING.nullSafeGet(rs, names[0]);
		if (value != null) {
			return parse(value);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		if (value != null) {
			String str = assemble((Map<String, String>) value);
			Hibernate.STRING.nullSafeSet(st, str, index);
		} else {
			Hibernate.STRING.nullSafeSet(st, value, index);
		}

	}

	@SuppressWarnings("unchecked")
	public Object deepCopy(Object value) throws HibernateException {
		Map<String, String> sourcehashtable = (Map<String, String>) value;
		Map<String, String> targethashtable = new HashMap<String, String>();
		try {
			if (value == null)
				return targethashtable;
			Iterator<String> iter = sourcehashtable.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				targethashtable.put(key, sourcehashtable.get(key));
			}
			return targethashtable;
		} catch (RuntimeException e) {
			return targethashtable;
		}

	}

	public boolean isMutable() {
		return false;
	}

	private String assemble(Map<String, String> bs) {
		try {
			StringWriter sw = new StringWriter();
			Marshaller marshaller = new Marshaller(sw);
//			Mapping mp=new Mapping();
//			URL u=this.getClass().getClassLoader().getResource("com/lti/type/string-to-map-mapping.xml");
//			mp.loadMapping(u);
//			marshaller.setMapping(mp);
//			if(bs==null||bs.size()==0)return "";
//			Iterator<String> keys=bs.keySet().iterator();
//			String[] strs=new String[bs.size()*2];
//			int count=0;
//			while(keys.hasNext()){
//				String key=keys.next();
//				strs[count++]=key;
//				strs[count++]=bs.get(key);
//			}
			marshaller.marshal(new HMap(bs));
			return sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	
	


	@SuppressWarnings("unchecked")
	private Map<String, String> parse(String value) {
		try {
			//long t1=System.currentTimeMillis();
			Unmarshaller unmarshaller = new Unmarshaller(HMap.class);
//			Mapping mp=new Mapping();
//			URL u=this.getClass().getClassLoader().getResource("com/lti/type/string-to-map-mapping.xml");
//			mp.loadMapping(u);
//			unmarshaller.setMapping(mp);
			unmarshaller.setWhitespacePreserve(true);
			HMap hinf = (HMap) unmarshaller.unmarshal(new StringReader(value));
			//long t2=System.currentTimeMillis();
			//System.out.println("load map: "+(t2-t1));
			return hinf.getMap();
		} catch (Exception e) {
			 //e.printStackTrace();
		}
		return null;
	}
	
	public  static void main(String[] args){
		Map<String,String> m1=new HashMap<String, String>();
		m1.put("k1", "v1");
		m1.put("k2", null);
		m1.put("k3", "v3");
		m1.put("k4", "v4");
		System.out.println(new StringToMap().assemble(m1));
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><HMap><map xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:java=\"http://java.sun.com\" xsi:type=\"java:org.exolab.castor.mapping.MapItem\"><key xsi:type=\"java:java.lang.String\">k3</key><value xsi:type=\"java:java.lang.String\">v3</value></map><map xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:java=\"http://java.sun.com\" xsi:type=\"java:org.exolab.castor.mapping.MapItem\"><key xsi:type=\"java:java.lang.String\">k4</key><value xsi:type=\"java:java.lang.String\">v4</value></map><map xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:java=\"http://java.sun.com\" xsi:type=\"java:org.exolab.castor.mapping.MapItem\"><key xsi:type=\"java:java.lang.String\">k1</key><value xsi:type=\"java:java.lang.String\">v1</value></map><map xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:java=\"http://java.sun.com\" xsi:type=\"java:org.exolab.castor.mapping.MapItem\"><key xsi:type=\"java:java.lang.String\">k2</key></map></HMap>";
		Map<String,String> map=new StringToMap().parse(xml);
		System.out.println(map.get("k1"));
	}

	public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
		return null;
	}

	public Serializable disassemble(Object arg0) throws HibernateException {
		return null;
	}

	public int hashCode(Object arg0) throws HibernateException {
		return 0;
	}

	public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
		return null;
	}

}