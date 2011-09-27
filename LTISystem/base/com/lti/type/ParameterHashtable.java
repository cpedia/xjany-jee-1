package com.lti.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Hashtable;
import java.util.Iterator;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;


public class ParameterHashtable implements java.io.Serializable, org.hibernate.usertype.UserType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int[] TYPES = new int[] { Types.VARCHAR };

	public int[] sqlTypes() {

		return TYPES;
	}

	@SuppressWarnings("unchecked")
	public Class returnedClass() {
		// TODO Auto-generated method stub
		return Hashtable.class;
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object objx, Object objy) throws HibernateException {
		if (objx == null && objy == null)
			return true;
		String x = com.lti.util.ObjectXMLEncoder.objectToXML(objx);
		String y = com.lti.util.ObjectXMLEncoder.objectToXML(objy);
		if (x != null) {
			if (x.equals(y))
				return true;
			else
				return false;
		} else {
			if (y == null)
				return true;
			else
				return false;
		}
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
			String str = assemble((Hashtable) value);
			Hibernate.STRING.nullSafeSet(st, str, index);
		} else {
			Hibernate.STRING.nullSafeSet(st, value, index);
		}

	}

	@SuppressWarnings("unchecked")
	public Object deepCopy(Object value) throws HibernateException {
		Hashtable sourcehashtable = (Hashtable) value;
		Hashtable targethashtable = new Hashtable();
		if(value == null)return targethashtable;
		Iterator iter=sourcehashtable.keySet().iterator();
		while(iter.hasNext()){
			Object key=iter.next();
			targethashtable.put(key, sourcehashtable.get(key));
		}
		return targethashtable;
		
	}

	public boolean isMutable() {
		return false;
	}

	private String assemble(Hashtable bs) {
		return  com.lti.util.ObjectXMLEncoder.objectToXML(bs);
	}

	private Hashtable parse(String value) {
		try {
			Hashtable bs=(Hashtable) com.lti.util.ObjectXMLDecoder.XMLToObject(value);
			return bs;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(value);
			return null;
		}
	}

	public Object assemble(Serializable arg0, Object arg1) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public Serializable disassemble(Object arg0) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

	public int hashCode(Object arg0) throws HibernateException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Object replace(Object arg0, Object arg1, Object arg2) throws HibernateException {
		// TODO Auto-generated method stub
		return null;
	}

}