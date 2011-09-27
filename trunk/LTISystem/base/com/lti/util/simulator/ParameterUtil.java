package com.lti.util.simulator;

import java.text.SimpleDateFormat;
import java.util.Map;

import com.lti.Exception.Strategy.ParameterException;

public class ParameterUtil {
	public static Object fetchParameter(String type,String name,Map<String,String> parameters)throws ParameterException{
		Object o=null;
		String value=parameters.get(name);
		try {
			if(value==null)o= null;
			else if (type.equalsIgnoreCase("java.lang.String") || type.equals("String")) {
				o= value;
			} else if (type.equalsIgnoreCase("java.lang.Long") || type.equalsIgnoreCase("long")) {
				o= Long.parseLong(value);
			} else if (type.equalsIgnoreCase("java.lang.Double") || type.equalsIgnoreCase("double")) {
				o= Double.parseDouble(value);

			} else if (type.equalsIgnoreCase("java.lang.Integer") || type.equalsIgnoreCase("int") || type.equalsIgnoreCase("Integer")) {
				o= Integer.parseInt(value);

			} else if (type.equalsIgnoreCase("java.lang.Float") || type.equalsIgnoreCase("float")) {
				o=  Float.parseFloat(value) ;

			} else if (type.equalsIgnoreCase("java.lang.Boolean") || type.equalsIgnoreCase("boolean")) {
				o=   Boolean.parseBoolean(value);
			} else if (type.equalsIgnoreCase("java.util.Date") || type.equalsIgnoreCase("java.sql.Timestamp") || type.equalsIgnoreCase("java.sql.Date")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				o=   sdf.parse(value) ;
			} else {
				if(!value.startsWith("{"))value="{"+value+"}";
				if(type.matches("^\\s*String\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						String[] items=new String[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=array[i].replaceAll("\\\"", "");
						}
						o= items;
					}
				}
				else if(type.matches("^\\s*Double\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						Double[] items=new Double[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Double.parseDouble(array[i]);
						}
						o= items;
					}
				}
				else if(type.matches("^\\s*Float\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						Float[] items=new Float[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Float.parseFloat(array[i]);
						}
						o= items;
					}
				}
				else if(type.matches("^\\s*Boolean\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						Boolean[] items=new Boolean[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Boolean.parseBoolean(array[i]);
						}
						o= items;
					}
				}
				else if(type.matches("^\\s*Integer\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						Integer[] items=new Integer[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Integer.parseInt(array[i]);
						}
						o= items;
					}
				}else if(type.matches("^\\s*double\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						double[] items=new double[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Double.parseDouble(array[i]);
						}
						o= items;
					}
				}
				else if(type.matches("^\\s*float\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						float[] items=new float[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Float.parseFloat(array[i]);
						}
						o= items;
					}
				}
				else if(type.matches("^\\s*boolean\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						boolean[] items=new boolean[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Boolean.parseBoolean(array[i]);
						}
						o= items;
					}
				}
				else if(type.matches("^\\s*int\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						int[] items=new int[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Integer.parseInt(array[i]);
						}
						o= items;
					}
				}else o=null;
				
			}
		} catch (Exception e) {
			ParameterException pe=new ParameterException("Cannot get the parameter of name: "+name,e);
			throw pe;
		}
		if(o==null){
			ParameterException pe=new ParameterException("Cannot get the parameter of name: "+name+", type:"+type+", value:"+value);
			throw pe;
		}else{
			return o;
		}
	}
	
	public static Object fetchParameter(String type , String name, String defaultValue, Map<String,String> parameters)throws ParameterException{
		Object o=null;
		String value=parameters.get(name);
		try {
			if(value == null)
				value = defaultValue;
			if(value==null)o= null;
			else if (type.equalsIgnoreCase("java.lang.String") || type.equals("String")) {
				o= value;
			} else if (type.equalsIgnoreCase("java.lang.Long") || type.equalsIgnoreCase("long")) {
				o= Long.parseLong(value);
			} else if (type.equalsIgnoreCase("java.lang.Double") || type.equalsIgnoreCase("double")) {
				o= Double.parseDouble(value);

			} else if (type.equalsIgnoreCase("java.lang.Integer") || type.equalsIgnoreCase("int") || type.equalsIgnoreCase("Integer")) {
				o= Integer.parseInt(value);

			} else if (type.equalsIgnoreCase("java.lang.Float") || type.equalsIgnoreCase("float")) {
				o=  Float.parseFloat(value) ;

			} else if (type.equalsIgnoreCase("java.lang.Boolean") || type.equalsIgnoreCase("boolean")) {
				o=   Boolean.parseBoolean(value);
			} else if (type.equalsIgnoreCase("java.util.Date") || type.equalsIgnoreCase("java.sql.Timestamp") || type.equalsIgnoreCase("java.sql.Date")) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				o=   sdf.parse(value) ;
			} else {
				if(!value.startsWith("{"))value="{"+value+"}";
				if(type.matches("^\\s*String\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						String[] items=new String[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=array[i].replaceAll("\\\"", "");
						}
						o= items;
					}
				}
				else if(type.matches("^\\s*Double\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						Double[] items=new Double[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Double.parseDouble(array[i]);
						}
						o= items;
					}
				}
				else if(type.matches("^\\s*Float\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						Float[] items=new Float[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Float.parseFloat(array[i]);
						}
						o= items;
					}
				}
				else if(type.matches("^\\s*Boolean\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						Boolean[] items=new Boolean[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Boolean.parseBoolean(array[i]);
						}
						o= items;
					}
				}
				else if(type.matches("^\\s*Integer\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						Integer[] items=new Integer[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Integer.parseInt(array[i]);
						}
						o= items;
					}
				}else if(type.matches("^\\s*double\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						double[] items=new double[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Double.parseDouble(array[i]);
						}
						o= items;
					}
				}
				else if(type.matches("^\\s*float\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						float[] items=new float[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Float.parseFloat(array[i]);
						}
						o= items;
					}
				}
				else if(type.matches("^\\s*boolean\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						boolean[] items=new boolean[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Boolean.parseBoolean(array[i]);
						}
						o= items;
					}
				}
				else if(type.matches("^\\s*int\\s*\\[\\s*\\]\\s*$")){
					String[] array=value.split("(\\{\\s*)|(\\s*,\\s*)|(\\s*\\})");
					if(array.length>1){
						int[] items=new int[array.length-1];
						for(int i=1;i<array.length;i++){
							items[i-1]=Integer.parseInt(array[i]);
						}
						o= items;
					}
				}else o=null;
				
			}
		} catch (Exception e) {
			ParameterException pe=new ParameterException("Cannot get the parameter of name: "+name,e);
			throw pe;
		}
		if(o==null){
			ParameterException pe=new ParameterException("Cannot get the parameter of name: "+name+", type:"+type+", value:"+value);
			throw pe;
		}else{
			return o;
		}
	}
}
