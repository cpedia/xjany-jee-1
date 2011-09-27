package com.lti.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.List;

public class ListEncoder {

	public static void main(String[] args) {
	}

	/**
	 * Serialize object to XML
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String listToXML(List list) {
		if (list == null || list.size() == 0)
			return null;
		StringBuffer sb = new StringBuffer();
		sb.append("<list>");
		sb.append(getInnerStructure(list.get(0)));
		for(int i=0;i<list.size();i++){
			sb.append(getXML(list.get(i)));
		}
		sb.append("</list>");
		return sb.toString();
	}

	public static boolean isBasicType(Object obj) {
		if (obj instanceof java.lang.String) {
		} else if (obj instanceof java.lang.Long) {
		} else if (obj instanceof java.lang.Double) {
		} else if (obj instanceof java.lang.Integer) {
		} else if (obj instanceof java.lang.Float) {
		} else if (obj instanceof java.lang.Boolean) {
		} else if (obj instanceof java.util.Date) {
		} else {
			return false;
		}
		return true;
	}

	public static boolean isBasicType(String type) {
		if (type.equalsIgnoreCase("java.lang.String") || type.equals("String")) {
		} else if (type.equalsIgnoreCase("java.lang.Long") || type.equalsIgnoreCase("Long")) {
		} else if (type.equalsIgnoreCase("java.lang.Double") || type.equalsIgnoreCase("Double")) {
		} else if (type.equalsIgnoreCase("java.lang.Integer") || type.equalsIgnoreCase("int") || type.equalsIgnoreCase("Integer")) {
		} else if (type.equalsIgnoreCase("java.lang.Float") || type.equalsIgnoreCase("Float")) {
		} else if (type.equalsIgnoreCase("java.lang.Boolean") || type.equalsIgnoreCase("Boolean")) {
		}else{
			return false;
		}
		return true;
	}

	public static String getInnerStructure(Object obj) {
		if (obj == null)
			return null;
		StringBuffer sb = new StringBuffer();
		String className = obj.getClass().getName();
		if (className.equals("java.sql.Timestamp"))
			className = "java.util.Date";
		sb.append("<s>");
		sb.append("<t>");
		sb.append(className);
		sb.append("</t>");

		if (obj instanceof java.lang.String) {
		} else if (obj instanceof java.lang.Long) {
		} else if (obj instanceof java.lang.Double) {
		} else if (obj instanceof java.lang.Integer) {
		} else if (obj instanceof java.lang.Float) {
		} else if (obj instanceof java.lang.Boolean) {
		} else if (obj instanceof java.util.Date) {
		} else {
			BeanInfo sourceBean = null;
			PropertyDescriptor[] propertyDescriptors = null;
			try {
				sourceBean = Introspector.getBeanInfo(obj.getClass());
				propertyDescriptors = sourceBean.getPropertyDescriptors();
				if (propertyDescriptors == null || propertyDescriptors.length == 0) {
					return "<s><t>" + className + "</t></s>";
				}
			} catch (Exception e2) {
				return "<s><t>" + className + "</t></s>";
			}

			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor pro = propertyDescriptors[i];
				if (!isBasicType(pro.getPropertyType().getName()))
					continue;
				sb.append("<c");
				sb.append(i);
				sb.append(">");
				sb.append("<n>");
				sb.append(pro.getName());
				sb.append("</n>");
				sb.append("<t>");
				sb.append(pro.getPropertyType().getName());
				sb.append("</t>");
				sb.append("</c");
				sb.append(i);
				sb.append(">");
			}
			sb.append("<l>");
			sb.append(propertyDescriptors.length);
			sb.append("</l>");
		}

		sb.append("</s>\r\n");
		return sb.toString();
	}

	@SuppressWarnings("unchecked")
	public static String getXML(Object obj) {
		if(obj==null)return "";
		StringBuffer sb = new StringBuffer();
		
		String className = obj.getClass().getName();
		if (obj instanceof java.lang.String) {
			sb.append("<v><![CDATA[" + obj + "]]></v>");
		} else if (obj instanceof java.lang.Long) {
			sb.append("<v>" + String.valueOf(obj) + "</v>");
		} else if (obj instanceof java.lang.Double) {
			sb.append("<v>" + String.valueOf(obj) + "</v>");
		} else if (obj instanceof java.lang.Integer) {
			sb.append("<v>" + String.valueOf(obj) + "</v>");
		} else if (obj instanceof java.lang.Float) {
			sb.append("<v>" + String.valueOf(obj) + "</v>");
		} else if (obj instanceof java.lang.Boolean) {
			sb.append("<v>" + String.valueOf(obj) + "</v>");
		} else if (obj instanceof java.util.Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sb.append("<v>" + sdf.format(obj) + "</v>");
		} else {
			
			BeanInfo sourceBean = null;
			PropertyDescriptor[] propertyDescriptors = null;
			try {
				sourceBean = Introspector.getBeanInfo(obj.getClass());
				propertyDescriptors = sourceBean.getPropertyDescriptors();
				if (propertyDescriptors == null || propertyDescriptors.length == 0) {
					return "<v></v>";
				}
			} catch (Exception e2) {
				return "<v></v>";
			}
			sb.append("<v>");
			for (int i = 0; i < propertyDescriptors.length; i++) {

				try {
					PropertyDescriptor pro = propertyDescriptors[i];

					if (!isBasicType(pro.getPropertyType().getName()))
						continue;

					if (pro.getWriteMethod() == null)
						continue;

					Method rm = pro.getReadMethod();
					if (rm == null)
						continue;
					if (!Modifier.isPublic(rm.getDeclaringClass().getModifiers())) {
						rm.setAccessible(true);
					}

					Object value = rm.invoke(obj, new Object[0]);
					if (value != null){
						sb.append("<v");
						sb.append(i);
						sb.append(">");
						sb.append(value);
						sb.append("</v");
						sb.append(i);
						sb.append(">");
					}
				} catch (Exception e) {
				}
			}
			sb.append("</v>");
		}
		sb.append("\r\n");
		return sb.toString();
	}

}
