package com.lti.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.io.CsvListReader;
import org.supercsv.prefs.CsvPreference;

public class CSVDecoder {
	
	public static DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
	
	public static Object parseValue(String str,Class cls){
		Object o=null;
		try {
			String type=cls.getName();
			if (type.equalsIgnoreCase("java.lang.String") || type.equals("String")) {
				o=str;
			} else if (type.equalsIgnoreCase("java.lang.Long") || type.equalsIgnoreCase("Long")) {
				o=Long.parseLong(str);
			} else if (type.equalsIgnoreCase("java.lang.Double") || type.equalsIgnoreCase("Double")) {
				o=Double.parseDouble(str);
			} else if (type.equalsIgnoreCase("java.lang.Integer") || type.equalsIgnoreCase("int") || type.equalsIgnoreCase("Integer")) {
				o=Integer.parseInt(str);
			} else if (type.equalsIgnoreCase("java.lang.Float") || type.equalsIgnoreCase("Float")) {
				o=Float.parseFloat(str);
			} else if (type.equalsIgnoreCase("java.lang.Boolean") || type.equalsIgnoreCase("Boolean")) {
				o=Boolean.parseBoolean(str);
			}else if (type.equalsIgnoreCase("java.util.Date") ) {
				o=df.parseObject(str);
			}else{
			}
		} catch (Exception e) {
		}
		return o;
	}
	
	public static List CSVToList(String str,Class cls){
		try {
			if(str==null)return null;
			List olist=new ArrayList();
			StringReader sr=new StringReader(str);
			CsvListReader clr=new CsvListReader(sr,CsvPreference.STANDARD_PREFERENCE);
			if(CSVEncoder.isBasicType(cls.getName())){
				List<String> list=null;
				while((list=clr.read())!=null){
					Object o=parseValue(list.get(0),cls);
					if(o!=null)olist.add(o);
				}
			}else{
				List<String> hs=clr.read();
				List<String> headers = new ArrayList<String>();
				headers.addAll(hs);
				List<String> list=null;
				while((list=clr.read())!=null){
					BeanInfo beanInfo = null;
					PropertyDescriptor[] propertyDescriptors = null;
					Object obj =null;
					try {
						obj = cls.newInstance();
						beanInfo = Introspector.getBeanInfo(cls);
						propertyDescriptors = beanInfo.getPropertyDescriptors();
					} catch (Exception e2) {
						return null;
					}
					for(int i=0;i<list.size();i++){
						if(i+1>headers.size())break;
						String name=headers.get(i);
						for (int k = 0; k < propertyDescriptors.length; k++) {
							PropertyDescriptor pro = propertyDescriptors[k];
							if (pro.getName().equals(name)) {
								Method wm = pro.getWriteMethod();
								if (wm != null) {
									if (!Modifier.isPublic(wm.getDeclaringClass().getModifiers())) {
										wm.setAccessible(true);
									}
									Object o=parseValue(list.get(i), pro.getPropertyType());
									
									if (o != null) {
										try {
											wm.invoke((Object) obj, new Object[] { o });
										} catch (Exception e1) {
											continue;
										}
									}
								}
							}// end if
						}// end inner for
					}
					olist.add(obj);
				}
			}
			return olist;
		} catch (Exception e) {
			return null;
		} 
	}

}
