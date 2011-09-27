package com.lti.type;

import java.io.Serializable;
import java.io.StringReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

import com.lti.type.finance.HoldingInf;

public class StringToHoldingInf implements java.io.Serializable, org.hibernate.usertype.UserType {

	private static final long serialVersionUID = 6580947600412151430L;

	private static final int[] TYPES = new int[] { Types.VARCHAR };

	public int[] sqlTypes() {

		return TYPES;
	}

	@SuppressWarnings("unchecked")
	public Class returnedClass() {
		return HoldingInf.class;
	}

	public boolean equals(Object objx, Object objy) throws HibernateException {
		if (objx == null && objy == null)
			return true;

		if (objx == null || objy == null)
			return false;

		return objx.equals(objy);
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
			String str = assemble((HoldingInf) value);
			Hibernate.STRING.nullSafeSet(st, str, index);
		} else {
			Hibernate.STRING.nullSafeSet(st, value, index);
		}

	}

	@SuppressWarnings("unchecked")
	public Object deepCopy(Object value) throws HibernateException {
		if(value==null)return null;
		HoldingInf sinf=(HoldingInf) value;
		return HoldingInf.getInstance(new StringReader(sinf.toXML()));

	}

	public boolean isMutable() {
		return false;
	}

	private String assemble(HoldingInf bs) {
		return bs.toXML();
	}
	
	


	@SuppressWarnings("unchecked")
	private HoldingInf parse(String value) {
		return HoldingInf.getInstance(new StringReader(value));
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