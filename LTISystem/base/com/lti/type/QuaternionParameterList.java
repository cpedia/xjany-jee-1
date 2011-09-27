package com.lti.type;


import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
   
 
public class QuaternionParameterList implements java.io.Serializable,org.hibernate.usertype.UserType{   
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private java.util.List<com.lti.type.Quaternion> calist;
	private static final String SPLITTER_OUTTER = "@";
	private static final String SPLITTER_INNER="#";
	private static final int[] TYPES = new int[] { Types.VARCHAR };

	public String getElement(String source){
		int first=source.indexOf("#");
		if(first==-1)first=source.indexOf("@");
		
		if(first==-1)return source;
		
		String element=source.substring(0,first);
		return element;
	}
	public String removeElement(String source){
		int first=source.indexOf("#");
		if(first==-1)first=source.indexOf("@");
		
		if(first==-1||first==source.length())return "";
		
		return source.substring(first+1,source.length());

	}
	
	
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
    	java.util.List<com.lti.type.Quaternion> x=(java.util.List<com.lti.type.Quaternion>)objx;
    	java.util.List<com.lti.type.Quaternion> y=(java.util.List<com.lti.type.Quaternion>)objy;
		if (x == y)
			return true;
		if (x != null && y != null) {
			if (x.size() != y.size())
				return false;

			for(int i=0;i<x.size();i++){
				com.lti.type.Quaternion xp=x.get(i);
				boolean flag=false;
				for(int j=0;j<y.size();j++){
					com.lti.type.Quaternion yp=y.get(j);
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
            String str = assemble((java.util.List<com.lti.type.Quaternion>) value);   
            Hibernate.STRING.nullSafeSet(st, str, index);   
        } else {   
            Hibernate.STRING.nullSafeSet(st, value, index);   
        }   
   
    }   
   
    @SuppressWarnings("unchecked")
	public Object deepCopy(Object value) throws HibernateException {   
    	java.util.List<com.lti.type.Quaternion> source=(java.util.List<com.lti.type.Quaternion>)value;
    	java.util.List<com.lti.type.Quaternion> target = new java.util.ArrayList<com.lti.type.Quaternion>();
		if(source == null || source.size()<=0)return target;
		
		for(int i=0;i<source.size();i++){
			target.add(source.get(i));
		}

		return target;
    }   
   
    public boolean isMutable() {           
        return false;   
    }   
   
    private String assemble(java.util.List<com.lti.type.Quaternion> ca) {   
		StringBuffer strBuf = new StringBuffer();
		
		if(ca==null)return "";
		
		for(int i=0;i<ca.size();i++){
			com.lti.type.Quaternion p=ca.get(i);
			strBuf.append(p.First).append(SPLITTER_INNER);
			strBuf.append(p.Second).append(SPLITTER_INNER);
			strBuf.append(p.Third).append(SPLITTER_INNER);
			strBuf.append(p.Fourth).append(SPLITTER_OUTTER);
		}

		return strBuf.toString(); 
    }   
   
    private java.util.List<com.lti.type.Quaternion> parse(String value) {   
    	
		
		java.util.List<com.lti.type.Quaternion> target = new java.util.ArrayList<com.lti.type.Quaternion>();
		String[] sources = value.split(SPLITTER_OUTTER);
		if(value==null||value.length()==0)return null;
		for (int i = 0; i < sources.length; i++) {
			String source = sources[i];
			com.lti.type.Quaternion q=new com.lti.type.Quaternion();
			q.First=this.getElement(source);
			source=this.removeElement(source);
			q.Second=this.getElement(source);
			source=this.removeElement(source);
			q.Third=this.getElement(source);
			source=this.removeElement(source);
			q.Fourth=this.getElement(source);
			source=this.removeElement(source);
			target.add(q);
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