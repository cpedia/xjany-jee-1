package com.lti.type;


import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
   
 
public class TripleParameterList implements java.io.Serializable,org.hibernate.usertype.UserType{   
   
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private java.util.List<com.lti.type.Triple> calist;
	private static final String SPLITTER_1 = "@";
	private static final String SPLITTER_2="#";
	private static final String SPLITTER_END="&";
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
    	java.util.List<com.lti.type.Triple> x=(java.util.List<com.lti.type.Triple>)objx;
    	java.util.List<com.lti.type.Triple> y=(java.util.List<com.lti.type.Triple>)objy;
		if (x == y)
			return true;
		if (x != null && y != null) {
			if (x.size() != y.size())
				return false;

			for(int i=0;i<x.size();i++){
				com.lti.type.Triple xp=x.get(i);
				boolean flag=false;
				for(int j=0;j<y.size();j++){
					com.lti.type.Triple yp=y.get(j);
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
            String str = assemble((java.util.List<com.lti.type.Triple>) value);   
            Hibernate.STRING.nullSafeSet(st, str, index);   
        } else {   
            Hibernate.STRING.nullSafeSet(st, value, index);   
        }   
   
    }   
   
    @SuppressWarnings("unchecked")
	public Object deepCopy(Object value) throws HibernateException {   
    	java.util.List<com.lti.type.Triple> source=(java.util.List<com.lti.type.Triple>)value;
    	java.util.List<com.lti.type.Triple> target = new java.util.ArrayList<com.lti.type.Triple>();
		if(source == null || source.size()<=0)return target;
		
		for(int i=0;i<source.size();i++){
			target.add(source.get(i));
		}

		return target;
    }   
   
    public boolean isMutable() {           
        return false;   
    }   
   
    private String assemble(java.util.List<com.lti.type.Triple> ca) {   
		StringBuffer strBuf = new StringBuffer();
		
		if(ca==null)return "";
		
		for(int i=0;i<ca.size();i++){
			com.lti.type.Triple p=ca.get(i);
			strBuf.append(p.First).append(SPLITTER_1);
			strBuf.append(p.Second).append(SPLITTER_2);
			strBuf.append(p.Third).append(SPLITTER_END);
		}

		return strBuf.toString(); 
    }   
   
    private java.util.List<com.lti.type.Triple> parse(String value) {   
		String[] strs = value.split(SPLITTER_END);
		java.util.List<com.lti.type.Triple> target = new java.util.ArrayList<com.lti.type.Triple>();
		
		for (int i = 0; i < strs.length; i++) {
			String[] ca = strs[i].split(SPLITTER_1);
			if(ca.length>=2){
				com.lti.type.Triple p=new com.lti.type.Triple();
				p.First=ca[0];
				String[] ca2=ca[1].split(SPLITTER_2);
				if(ca2.length>=2){
					p.Second=ca2[0];
					p.Third=ca2[1];
					target.add(p);
				}else if(ca2.length==1){
					p.Second=ca2[0];
					p.Third="";
					target.add(p);
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