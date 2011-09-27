package com.lti.type;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

public class ByteArrayUserType implements java.io.Serializable, org.hibernate.usertype.UserType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int[] TYPES = new int[] { Types.BLOB };

	public int[] sqlTypes() {

		return TYPES;
	}

	@SuppressWarnings("unchecked")
	public Class returnedClass() {
		// TODO Auto-generated method stub
		return byte[].class;
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object x, Object y) throws HibernateException {
		return (x == y) || (x != null && y != null && java.util.Arrays.equals((byte[]) x, (byte[]) y));

	}

	public Object nullSafeGet(ResultSet rs, String[] names, Object owner) throws HibernateException, SQLException {
		Blob blob = rs.getBlob(names[0]);
		if(blob==null)return null;
		return blob.getBytes(1, (int) blob.length());
	}

	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		if(value==null)st.setBlob(index, (Blob)null);
		else {
			//st.setBlob(index, Hibernate.createBlob((byte[]) value));
			//st.setBlob(index, new ByteArrayInputStream((byte[]) value));
			st.setBytes(index, (byte[]) value);
		}
	}

	public Object deepCopy(Object value) {
		if (value == null)
			return null;
		byte[] bytes = (byte[]) value;
		byte[] result = new byte[bytes.length];
		System.arraycopy(bytes, 0, result, 0, bytes.length);
		return result;
	}

	public boolean isMutable() {
		return true;
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