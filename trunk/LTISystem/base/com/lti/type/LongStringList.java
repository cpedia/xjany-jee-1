package com.lti.type;


import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
   
 
public class LongStringList implements java.io.Serializable,org.hibernate.usertype.UserType{   
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private java.util.List<com.lti.type.LongString> calist;
	private static final String SPLITTER = ",";
	private static final String SPLITTER_2=";";
	private static final int[] TYPES = new int[] { Types.VARCHAR };
   
    public int[] sqlTypes() {   
           
        return TYPES;   
    }   
    @SuppressWarnings("unchecked")
    public Class returnedClass() {   
        // TODO Auto-generated method stub   
    	return java.util.List.class;
    }   
    @SuppressWarnings("unchecked")
    public boolean equals(Object objx, Object objy) throws HibernateException {
    	java.util.List<com.lti.type.LongString> x=(java.util.List<com.lti.type.LongString>)objx;
    	java.util.List<com.lti.type.LongString> y=(java.util.List<com.lti.type.LongString>)objy;
		if (x == y)
			return true;
		if (x != null && y != null) {
			if (x.size() != y.size())
				return false;

			for(int i=0;i<x.size();i++){
				com.lti.type.LongString xp=x.get(i);
				boolean flag=false;
				for(int j=0;j<y.size();j++){
					com.lti.type.LongString yp=y.get(j);
					if(xp.equals(yp)){
						flag=true;
						break;
					}
				}
				if(flag==false)return false;
			}
			return true;
		}
		return false;
    }   
   
    public Object nullSafeGet(ResultSet rs, String[] names, Object owner)   
            throws HibernateException, SQLException {   
        String value = (String) Hibernate.STRING.nullSafeGet(rs, names[0]);   
        if (value != null) {   
            return parse(value);   
        } else {   
            return null;   
        }   
    }   
    @SuppressWarnings("unchecked")
    public void nullSafeSet(PreparedStatement st, Object value, int index)   
            throws HibernateException, SQLException {   
        if (value != null) {   
            String str = assemble((java.util.List<com.lti.type.LongString>) value);   
            Hibernate.STRING.nullSafeSet(st, str, index);   
        } else {   
            Hibernate.STRING.nullSafeSet(st, value, index);   
        }   
   
    }   
    @SuppressWarnings("unchecked")
    public Object deepCopy(Object value) throws HibernateException {   
    	java.util.List<com.lti.type.LongString> source=(java.util.List<com.lti.type.LongString>)value;
    	java.util.List<com.lti.type.LongString> target = new java.util.ArrayList<com.lti.type.LongString>();
		if(source == null || source.size()<=0)return target;
		
		for(int i=0;i<source.size();i++){
			target.add(source.get(i));
		}

		return target;
    }   
   
    public boolean isMutable() {           
        return false;   
    }   
   
    private String assemble(java.util.List<com.lti.type.LongString> ca) {   
		StringBuffer strBuf = new StringBuffer();
		
		if(ca==null)return "";
		
		for(int i=0;i<ca.size();i++){
			com.lti.type.LongString p=ca.get(i);
			strBuf.append(p.getID()).append(SPLITTER);
			strBuf.append(p.getName()).append(SPLITTER_2);
		}

		return strBuf.toString(); 
    }   
   
    private java.util.List<com.lti.type.LongString> parse(String value) {   
		String[] strs = ((String) value).split(SPLITTER_2);
		java.util.List<com.lti.type.LongString> target = new java.util.ArrayList<com.lti.type.LongString>();
		
		for (int i = 0; i < strs.length; i++) {
			String[] ca = strs[i].split(SPLITTER);
			if(ca.length>=2){
				com.lti.type.LongString p=new com.lti.type.LongString();
				try {
					p.setID(Long.parseLong(ca[0]));
					p.setName(ca[1]);
					target.add(p);
				} catch (NumberFormatException e) {
					//e.printStackTrace();
				}
			}
			
		}
		return target;
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