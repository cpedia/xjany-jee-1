package com.lti.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class CSVEncoder {

	public static void main(String[] args) {
	}

	/**
	 * Serialize object to XML
	 * 
	 * @param list
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String listToCSV(List list) {
		try {
			if (list == null || list.size() == 0)
				return null;
			StringWriter sw = new StringWriter();
			CsvListWriter clw = new CsvListWriter(sw, CsvPreference.STANDARD_PREFERENCE);

			List<String> headers = getHeaders(list.get(0).getClass());
			if(headers!=null)clw.write(headers);
			
			for (int i = 0; i < list.size(); i++) {
				List<String> row=getRow(list.get(i));
				if(row!=null)clw.write(row);
			}
			return sw.toString();
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String getValue(Object obj){
		if (obj instanceof java.lang.String) {
			return (String) obj;
		} else if (obj instanceof java.lang.Long) {
			return (String.valueOf(obj));
		} else if (obj instanceof java.lang.Double) {
			return (String.valueOf(obj));
		} else if (obj instanceof java.lang.Integer) {
			return (String.valueOf(obj));
		} else if (obj instanceof java.lang.Float) {
			return (String.valueOf(obj));
		} else if (obj instanceof java.lang.Boolean) {
			return (String.valueOf(obj));
		} else if (obj instanceof java.util.Date) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return ( sdf.format(obj));
		}else{
			return "";
		}
	}
	
	public static List<String> getRow(Object obj) {
		if (obj == null)
			return null;

		List<String> row=new ArrayList<String>();

		if (isBasicType(obj)) {
			row.add(getValue(obj));
		} else {
			BeanInfo sourceBean = null;
			PropertyDescriptor[] propertyDescriptors = null;
			try {
				sourceBean = Introspector.getBeanInfo(obj.getClass());
				propertyDescriptors = sourceBean.getPropertyDescriptors();
				if (propertyDescriptors == null || propertyDescriptors.length == 0) {
					return null;
				}
			} catch (Exception e2) {
				return null;
			}
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
					if (value != null) {
						row.add(getValue(value));
					}else{
						row.add("");
					}
				} catch (Exception e) {
				}
			}
		}
		return row;
	}

	public static List<String> getHeaders(Class cls) {

		String className = cls.getName();
		if (className.equals("java.sql.Timestamp"))
			className = "java.util.Date";

		
		if (isBasicType(className)) {
			return null;
		}

		List<String> headers = new ArrayList<String>();
		BeanInfo sourceBean = null;
		PropertyDescriptor[] propertyDescriptors = null;
		try {
			sourceBean = Introspector.getBeanInfo(cls);
			propertyDescriptors = sourceBean.getPropertyDescriptors();
			if (propertyDescriptors == null || propertyDescriptors.length == 0) {
				return null;
			}
		} catch (Exception e2) {
			return null;
		}

		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor pro = propertyDescriptors[i];
			if (!isBasicType(pro.getPropertyType().getName()))
				continue;
			headers.add(pro.getName());
		}
		return headers;
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
		} else if (type.equalsIgnoreCase("java.util.Date")) {
		} else {
			return false;
		}
		return true;
	}


}
