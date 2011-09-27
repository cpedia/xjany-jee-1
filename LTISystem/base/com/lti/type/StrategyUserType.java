package com.lti.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

import com.lti.type.executor.CompiledStrategy;

public class StrategyUserType implements java.io.Serializable, org.hibernate.usertype.UserType {

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
		return com.lti.type.executor.CompiledStrategy.class;
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
			String str = assemble((CompiledStrategy) value);
			Hibernate.STRING.nullSafeSet(st, str, index);
		} else {
			Hibernate.STRING.nullSafeSet(st, value, index);
		}

	}

	@SuppressWarnings("unchecked")
	public Object deepCopy(Object value) throws HibernateException {
		if (value == null)return null;
		String x = com.lti.util.ObjectXMLEncoder.objectToXML(value);
		if(x==null){
			return null;
		}
		else{
			try {
				CompiledStrategy bs=(CompiledStrategy) com.lti.util.ObjectXMLDecoder.objectXmlDecoder(x);
				return bs;
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
		
	}

	public boolean isMutable() {
		return false;
	}

	private String assemble(CompiledStrategy bs) {
		return  com.lti.util.ObjectXMLEncoder.objectToXML(bs);
	}

	private CompiledStrategy parse(String value) {
		try {
			CompiledStrategy bs=(CompiledStrategy) com.lti.util.ObjectXMLDecoder.objectXmlDecoder(value);
			return bs;
		} catch (Exception e) {
			e.printStackTrace();
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