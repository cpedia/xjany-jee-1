package com.lti.type;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

public class HibernateDoubleArray implements java.io.Serializable, org.hibernate.usertype.UserType {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String SPLITTER = ",";
	private static final int[] TYPES = new int[] { Types.VARCHAR };

	public int[] sqlTypes() {

		return TYPES;
	}

	@SuppressWarnings("unchecked")
	public Class returnedClass() {
		// TODO Auto-generated method stub
		return Double[].class;
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object objx, Object objy) throws HibernateException {

		Double[] x = (Double[]) objx;
		Double[] y = (Double[]) objy;
		if (x == y) {
			return true;
		}

		if (x != null && y != null) {
			if (x.length != y.length){
				return false;
			}
			boolean flag = false;
			for (int i = 0; i < x.length; i++) {
				if (!x[i].equals(y[i])) {
					flag = true;
					break;
				}
				
			}
			if (flag)
				return false;
			else return true;
		}
		return false;
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
			String str = assemble((Double[]) value);
			Hibernate.STRING.nullSafeSet(st, str, index);
		} else {
			Hibernate.STRING.nullSafeSet(st, value, index);
		}

	}

	@SuppressWarnings("unchecked")
	public Object deepCopy(Object value) throws HibernateException {
		if(value==null)return null;
		Double[] source = (Double[]) value;
		Double[] target = new Double[source.length];

		for (int i = 0; i < source.length; i++) {
			target[i]=source[i];
		}

		return target;
	}

	public boolean isMutable() {
		return false;
	}

	private String assemble(Double[] strs) {
		StringBuffer strBuf = new StringBuffer();
		if (strs == null){
			return "";
		}
		
		for (int i = 0; i < strs.length; i++) {
			strBuf.append(strs[i]).append(SPLITTER);
		}

		return strBuf.toString();
	}

	private Double[] parse(String value) {
		String[] strs = ((String) value).split(SPLITTER);
		if(strs==null||strs.length==0)return null;
		Double[] array=new Double[strs.length];
		for(int i=0;i<array.length;i++){
			try {
				array[i]=Double.parseDouble(strs[i]);
			} catch (Exception e) {
				System.out.println(strs[i]);
				e.printStackTrace();
				array[i]=0.0;
			}
		}
		return array;
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