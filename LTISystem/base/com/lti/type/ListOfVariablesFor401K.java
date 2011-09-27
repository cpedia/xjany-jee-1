package com.lti.type;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;

import com.lti.service.bo.VariableFor401k;
import com.lti.util.ObjectXMLDecoder;
import com.lti.util.ObjectXMLEncoder;
import com.lti.util.ZipObject;

public class ListOfVariablesFor401K implements java.io.Serializable, org.hibernate.usertype.UserType {

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
		return List.class;
	}

	@SuppressWarnings("unchecked")
	public boolean equals(Object objx, Object objy) throws HibernateException {
		if (objx == null && objy == null)
			return true;
		String x = ObjectXMLEncoder.objectToXML((List) objx);
		String y = ObjectXMLEncoder.objectToXML((List) objy);
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
		Blob blob = rs.getBlob(names[0]);
		if (blob == null || blob.length() == 0)
			return null;
		byte[] bytes = blob.getBytes(1, (int) blob.length());
		return parse(bytes);
	}

	@SuppressWarnings("unchecked")
	public void nullSafeSet(PreparedStatement st, Object value, int index) throws HibernateException, SQLException {
		if(value==null)st.setBlob(index, (Blob)null);
		else{
			String str = assemble((List<VariableFor401k>) value);
			try {
				byte[] bytes= ZipObject.ObjectToZipBytes(str);
				st.setBlob(index, Hibernate.createBlob(bytes));
			} catch (Exception e) {
				st.setBlob(index, (Blob)null);
			}
		}

	}

	@SuppressWarnings("unchecked")
	public Object deepCopy(Object value) throws HibernateException {
		List<VariableFor401k> sourcelist = (List<VariableFor401k>) value;
		java.util.List<VariableFor401k> targetlist = new java.util.ArrayList<VariableFor401k>();
		if (value == null)
			return null;
		if (sourcelist.size() == 0)
			return targetlist;

		Iterator<VariableFor401k> iter = sourcelist.iterator();
		while (iter.hasNext()) {
			VariableFor401k key = iter.next();
			try {
				targetlist.add(key.clone());
			} catch (CloneNotSupportedException e) {
			}
		}
		return targetlist;

	}

	public boolean isMutable() {
		return false;
	}

	private String assemble(List<VariableFor401k> bs) {
		return ObjectXMLEncoder.objectToXML(bs);
	}

	private List<VariableFor401k> parse(byte[] bytes) {
		try {
			if(bytes==null||bytes.length==0)return null;
			String value=(String) ZipObject.ZipBytesToObject(bytes);
			List<VariableFor401k> bs = (List<VariableFor401k>)ObjectXMLDecoder.XMLToObject(value);
			return bs;
		} catch (Exception e) {
			//e.printStackTrace();
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