package com.lti.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.XMLEncoder;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import com.lti.system.ContextHolder;

public class ObjectXMLEncoder {

	public static void main(String[] args){
		int a=0;
		Object o=a;
		if(o instanceof Integer){
			System.out.println("a");
		}
	}
	/**
	 * Serialize object to XML
	 * @param obj
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String objectToXML(Object obj) {
		if(obj==null)return null;
		return objectToXML(obj, "");
	}

	
	
	@SuppressWarnings("unchecked")
	public static String objectToXML(Object obj, String name) {

		StringBuffer sb = new StringBuffer();
		String className=obj.getClass().getName();
		if(className.equals("java.sql.Timestamp"))className="java.util.Date";
		sb.append("<object>");
		sb.append("<name>");
		sb.append(name);
		sb.append("</name>");
		sb.append("<type>");
		sb.append(className);
		sb.append("</type>");
		if (obj instanceof java.lang.String) {
			sb.append("<value><![CDATA[" + obj + "]]></value>");
		} else if (obj instanceof java.lang.Long) {
			sb.append("<value>" + String.valueOf(obj) + "</value>");
		} else if (obj instanceof java.lang.Double) {
			sb.append("<value>" + String.valueOf(obj) + "</value>");
		} else if (obj instanceof java.lang.Integer) {
			sb.append("<value>" + String.valueOf(obj) + "</value>");
		} else if (obj instanceof java.lang.Float) {
			sb.append("<value>" + String.valueOf(obj) + "</value>");
		} else if (obj instanceof java.lang.Boolean) {
			sb.append("<value>" + String.valueOf(obj) + "</value>");
		} else if (obj instanceof java.util.Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sb.append("<value>" + sdf.format(obj) + "</value>");
		} else if ( obj instanceof java.util.Map) {
			Map ht = (Map) obj;
			Iterator iter = ht.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				String value = (String) ht.get(key);
				sb.append("<pair>");
				sb.append("<key>");
				sb.append(key);
				sb.append("</key>");
				sb.append("<value><![CDATA[" +value+ "]]></value>");
				sb.append("</pair>");
			}
		} else if (obj instanceof java.util.Collection) {
			java.util.Collection ht = (java.util.Collection) obj;
			Iterator iter = ht.iterator();
			while (iter.hasNext()) {
				Object key = (Object) iter.next();
				sb.append(objectToXML(key, ""));
			}
		} else {
			BeanInfo sourceBean = null;
			PropertyDescriptor[] propertyDescriptors = null;
			try {
				sourceBean = Introspector.getBeanInfo(obj.getClass());
				propertyDescriptors = sourceBean.getPropertyDescriptors();
				if (propertyDescriptors == null || propertyDescriptors.length == 0) {
					return "<object><name>" + name + "</name></object>";
				}
			} catch (Exception e2) {
				return "<object><name>" + name + "</name></object>";
			}

			for (int i = 0; i < propertyDescriptors.length; i++) {

				try {
					StringBuffer attr = new StringBuffer();
					PropertyDescriptor pro = propertyDescriptors[i];

					if (pro.getWriteMethod() == null)
						continue;

					Method rm = pro.getReadMethod();
					if (rm == null)
						continue;
					if (!Modifier.isPublic(rm.getDeclaringClass().getModifiers())) {
						rm.setAccessible(true);
					}

					if (pro.getPropertyType().getName().equals("java.util.Set") || pro.getPropertyType().getName().equals("java.util.List")) {

						java.util.Collection value = (java.util.Collection) rm.invoke(obj, new Object[0]);
						if (value == null || value.size() == 0)
							continue;
						attr.append("<object>");
						attr.append("<type>");
						attr.append(pro.getPropertyType().getName());
						attr.append("</type>");
						attr.append("<name>");
						attr.append(pro.getName());
						attr.append("</name>");
						Iterator iter = value.iterator();
						while (iter.hasNext()) {
							try {
								Object s = iter.next();
								attr.append(objectToXML(s, ""));
							} catch (Exception e) {
							}
						}
						attr.append("</object>");

					} else {
						Object value = rm.invoke(obj, new Object[0]);
						if (value != null)
							attr.append(objectToXML(value, pro.getName()));

					}
					sb.append(attr);
				} catch (Exception e) {
				}

			}
		}

		sb.append("</object>\r\n");
		return sb.toString();
	}
	
	/**
	 * Encode supplied by java
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static String objectXmlEncoder(Object obj) throws Exception {

		java.io.ByteArrayOutputStream fos = new java.io.ByteArrayOutputStream();
		XMLEncoder encoder = new XMLEncoder(fos);
		encoder.writeObject(obj);
		encoder.flush();
		encoder.close();
		fos.close();
		return fos.toString();
	}
}
