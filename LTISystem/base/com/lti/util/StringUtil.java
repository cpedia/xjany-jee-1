package com.lti.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lti.service.AssetClassManager;
import com.lti.service.StrategyClassManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.StrategyClass;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class StringUtil {

	public static void main(String[] args){
		System.out.println("\"SPY\", ".replace("\"", "\\\""));
	}
	public static String toJSON(Map<String,String> map){
		StringBuffer sb=new StringBuffer();
		sb.append("{");
		Iterator<String> iter=map.keySet().iterator();
		while(iter.hasNext()){
			String key=iter.next();
			sb.append("\"");
			sb.append(key.replace("\"", "\\\""));
			sb.append("\"");
			sb.append(":");
			sb.append("\"");
			try {
				sb.append(map.get(key).replace("\"", "\\\"").replace("\r", "").replace("\n", ""));
			} catch (Exception e) {
				sb.append("");
			}
			sb.append("\"");
			sb.append(",");
		}
		sb.append("\"mapsize\":");
		sb.append(map.size());
		sb.append("}");
		return sb.toString();
	}
	public static String toJSON(Object o,Map<String, String> settings){
		StringBuffer sb=new StringBuffer();
		sb.append("{");
		try {
			BeanInfo sourceBean = Introspector.getBeanInfo(o.getClass());
			PropertyDescriptor[] propertyDescriptors = sourceBean.getPropertyDescriptors();
			for(int i=0;i<propertyDescriptors.length;i++){
				PropertyDescriptor pro=propertyDescriptors[i];
				Method rm = pro.getReadMethod();
				String name=settings.get(pro.getName().toLowerCase());
				if(name==null){
					
					continue;
					
				}
				if(pro.getPropertyType().getName().equals("java.util.List")){
					sb.append("\"");
					sb.append(name);
					sb.append("\":");
					sb.append("[");
					List list=(List) rm.invoke(o, new Object[0]);
					for(int j=0;j<list.size();j++){
						sb.append(toJSON(list.get(j),settings));
						if(j!=(list.size()-1)){
							sb.append(",");
						}
					}
					sb.append("]");
				}else{
					Object value=rm.invoke(o, new Object[0]);
					sb.append("\"");
					sb.append(name);
					sb.append("\":");
					sb.append("\"");
					if(value!=null)sb.append(String.valueOf(value).replace("\"", "\\\"").replace("\r", "").replace("\n", ""));
					sb.append("\"");
				}
				if(i!=(propertyDescriptors.length-1)){
					sb.append(",");
				}
			}
			sb.append("}");
		} catch (Exception e) {
			return "";
		}
		return sb.toString();
	}
	public static String assetClassManager() {
		// AssetClassManagerImpl assetClassManager=new AssetClassManagerImpl();
		AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		List<AssetClass> assetClassList = assetClassManager.getClasses();
		String assetClassArray = "";
		for (int i = 0; i < assetClassList.size(); i++) {
			AssetClass sc = assetClassList.get(i);
			String name = sc.getName();
			String id = sc.getID().toString();
			String par = "";
			if (sc != null && sc.getParentID() != null)
				par = sc.getParentID().toString();
			else
				par = "-1";
			assetClassArray += "assetClassArray[" + i + "]";
			assetClassArray += "=";
			assetClassArray += "new Array(" + id + "," + par + ",\"" + name + "\")";
			assetClassArray += ";\r\r\n";
		}
		return assetClassArray;
	}

	public static String strategyClassArray() {
		// StrategyClassManagerImpl strategyClassManager=new
		// StrategyClassManagerImpl();
		StrategyClassManager strategyClassManager = (StrategyClassManager) ContextHolder.getInstance().getApplicationContext().getBean("strategyClassManager");
		List<StrategyClass> strategyClassList = strategyClassManager.getClasses();
		String strategyClassArray = "";
		for (int i = 0; i < strategyClassList.size(); i++) {
			StrategyClass sc = strategyClassList.get(i);
			String name = sc.getName();
			String id = sc.getID().toString();
			String par = "";
			if (sc != null && sc.getParentID() != null)
				par = sc.getParentID().toString();
			else
				par = "-1";
			strategyClassArray += "strategyClassArray[" + i + "]";
			strategyClassArray += "=";
			strategyClassArray += "new Array(" + id + "," + par + ",\"" + name + "\")";
			strategyClassArray += ";\r\n";

		}
		return strategyClassArray;
	}

	public static String getString(String s, int i) {
		if (s == null)
			return s;
		if (s.length() >= i)
			return s.substring(0, i - 1);
		else {
			for (int j = s.length() - 1; j < i; j++) {
				s += " ";
			}
			return s;
		}
	}

	public static String getStackTraceString(Throwable e) {

		StackTraceElement[] ste = e.getStackTrace();

		StringBuffer sb = new StringBuffer();

		sb.append(e.getMessage() + "\n");

		for (int i = 0; i < ste.length; i++) {

			sb.append("\t" + ste[i].toString() + "\n");

		}

		Throwable tmp = e.getCause();
		while (tmp != null) {
			sb.append("Caused by: " + tmp.getMessage() + "\n");

			ste = tmp.getStackTrace();

			for (int i = 0; i < ste.length; i++) {

				sb.append("\t" + ste[i].toString() + "\n");

			}

			tmp = tmp.getCause();
		}

		return sb.toString();
	}
	
	public static String getStackTraceString(Throwable e,int depth) {
		StackTraceElement[] ste = e.getStackTrace();
		StringBuffer sb = new StringBuffer();
		sb.append(e.getMessage() + "\n");
		for (int i = 0; i < ste.length; i++) {
			if(i>=depth)break;
			sb.append("\t" + ste[i].toString() + "\n");
		}
		return sb.toString();
	}

	public static String[] sortStringArray(String[] strs) {
		Arrays.sort(strs);
		return strs;
	}

	/*
	 * A string uses ',' as the suffix and want to translate it into the int
	 * list
	 */

	public static List<Integer> stringToIntList(List<Integer> list, String s) {
		list = new ArrayList<Integer>();
		if (s != null) {
			String[] ss = s.split(",");
			for (int i = 0; i < ss.length; i++) {
				list.add(Integer.parseInt(ss[i]));
			}
			return list;
		} else
			return null;
	}

	public static String sortCategories(String categories) {
		if (categories == null)
			return null;
		String regx = ",|;|&| and ";
		Pattern p = Pattern.compile(regx);
		String[] c = p.split(categories);
		for (int i = 0; i < c.length; i++) {
			c[i] = c[i].trim();
		}
		Arrays.sort(c);
		if (c.length < 1)
			return null;
		String sortedCategories = "";
		for (int i = 0; i < c.length - 1; i++) {
			sortedCategories += (c[i] + ",");
		}
		sortedCategories += c[c.length - 1];
		return sortedCategories;
	}

	public static String[] splitCategories(String categories) {
		if (categories == null)
			return null;
		String[] c = categories.split(",");
		// List<String> categoryList = new ArrayList<String>();
		for (int i = 0; i < c.length; i++) {
			String s = c[i];
			c[i] = s.trim();
		}
		Arrays.sort(c);
		return c;
	}

	public static List<String> splitKeywords(String keywordStr) {
		if (keywordStr == null || keywordStr.equals(""))
			return null;
		String regx = "(\\s)+|(\\s)*&(\\s)*|(\\s)*,(\\s)*|(\\s)*;(\\s)*|(\\s)*and(\\s)*";
		Pattern p = Pattern.compile(regx);
		String[] keywords = p.split(keywordStr);
		List<String> keywordList = new ArrayList<String>();
		for (int i = 0; i < keywords.length; i++) {
			String s = keywords[i];
			s = s.trim();
			if (s.equals("") || s.length() == 0) {
				continue;
			}
			keywordList.add(s);
		}
		return keywordList;
	}

	public static String categoryString(String[] c) {
		String s = "";
		if (c == null || c.length == 0)
			return null;
		for (int i = 0; i < c.length - 1; i++) {
			s = s + "%" + c[i];
		}
		s = s + "%" + c[c.length - 1] + "%";
		System.out.println(s);
		return s;
	}

	public static Double percentageToDouble(String percentage) {
		Boolean isPercentage = false;
		if (percentage == null || percentage.endsWith("%") == true) {
			percentage = percentage.substring(0, percentage.length() - 1);
			isPercentage = true;
		}

		String regx = "^[-+]?\\d*\\.?\\d*$";
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(percentage);
		if (m.find() == false)
			return null;
		Double d = Double.parseDouble(percentage);
		if (isPercentage == true)
			return d / 100;
		else
			return d;
	}

	public static String getPath(String[] paths) {
		try {
			String seperator = "\\\\";
			if (Configuration.isLinux()) {
				seperator = "/";
			}
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < paths.length; i++) {
				sb.append(paths[i]);
				if (i != paths.length - 1)
					sb.append(seperator);
			}
			String regex = "(\\\\|/)+";
			return sb.toString().replaceAll(regex, seperator);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Boolean checkEmail(String email) {
		String regx = "^(([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+([;.](([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5}){1,25})+)*$";
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(email);
		if (m.find() == false)
			return false;
		else
			return true;
	}

	public static Boolean checkNumber(String s) {
		String regx = "^[-+]?[0-9]+[.]?[0-9]*$";
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(s);
		if (m.find() == false)
			return false;
		else
			return true;
	}

	public final static String SEPARATOR_INNER = "@";
	public final static String SEPARATOR_OUTTER = "#";

	public static String parseOutput(String key, String output) {
		if (output == null || key == null)
			return null;
		String[] items = output.split(SEPARATOR_OUTTER);
		for (int i = 0; i < items.length; i++) {
			String[] pair = items[i].split(SEPARATOR_INNER);
			if (pair.length == 2) {
				if (pair[0].equalsIgnoreCase(key))
					return pair[1];
			}
		}
		return null;
	}

	public static String getOutput(String key, String value) {
		return key + SEPARATOR_INNER + value + SEPARATOR_OUTTER;
	}


	public static String getValidName(String name) {
		return name.replace(" ", "_").replace("&", "_").replace('\\', '_').replace('/', '_').replace('&', '_').replace('-', '_').replace('.', '_').replace('%', '_').replace("'", "_").replace('"',	'_');
	}
	public static String getValidName2(String name) {
		return name.replace("'", "`").replace('\n',	' ').replace('\r',	' ').replace('"',	'`').trim();
	}
	public static List<String[]> parseArrayList(String s, int len) {
		if (s == null)
			return null;
		String[] strs = s.replace("\r", "").split("(\n)");
		List<String[]> list=new ArrayList<String[]>();
		for (int i = 0; i < strs.length; i++) {
			if(strs[i].equals(""))continue;
			String[] arr = strs[i].split("#");
			if (arr.length == len)
				list.add(arr);
			else{
				String[] newArray=new String[len];
				for(int j=0;j<len;j++){
					if(j<arr.length)newArray[j]=arr[j];
					else newArray[j]="";
				}
				list.add(newArray);
			}
		}
		return list;
	}
}
