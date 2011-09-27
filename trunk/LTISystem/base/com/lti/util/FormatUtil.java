package com.lti.util;


import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;

import java.text.*;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
public class FormatUtil {

   

    private static DecimalFormat quantityDecimalFormat = new DecimalFormat("###0.###");

    private static DecimalFormat percentageDecimalFormat = new DecimalFormat("##0.##");


    public static String formatPercentage(Double percentage) {

        if (percentage == null) {

            return "NA";

        }else if(percentage.isNaN()){
        	return "NA";
        }
        else {

            return formatPercentage(percentage.doubleValue());

        }

    }

    public static String formatPercentage(double percentage) {

        return percentageDecimalFormat.format(percentage * 100);

    }

    public static String formatQuantity(Double quantity) {

        if (quantity == null) {
            return "NA";

        }else if(quantity.isNaN()){
        	return "NA";
        }
        else {

            return formatQuantity(quantity.doubleValue());

        }   

    }

    public static String formatQuantity(double quantity) {

    	String quantityStr = quantityDecimalFormat.format(quantity);
    	if(quantityStr.equals("-0"))
    		quantityStr = "0";
        return quantityStr;

    }
    
    public static String formatQuantityWithLen(Double quantity,int len) {

    	if (quantity == null)return "NA";
    	
    	String str = "###0.";
    	for(int i=0;i<len;i++)str+="#";
    	DecimalFormat tmpFormat = new DecimalFormat(str);
    	String quantityStr = tmpFormat.format(quantity);
    	if(quantityStr.equals("-0"))
    		quantityStr = "0";
        return quantityStr;

    }


    public static String date8to10(String date) {

        if (date == null || date.trim().length() == 0) {

            return "";

        } else {

            return date.substring(0, 4) + "-" + date.substring(4, 6) +

                    "-" + date.substring(6, 8);

        }

    }

    public final static double half(double d, int digit) {

        BigDecimal bd = new BigDecimal(d);

        bd = bd.setScale(digit,BigDecimal.ROUND_HALF_EVEN);

        return bd.doubleValue();

    }
    public static String formatQuantity(Double source,Integer precition) {
		
		if(precition==null||precition<1||precition>15)precition=3;
		
		String formatString="###0.";
		
		if(source==null)return "";
		
		for(int i=0;i<precition;i++)formatString+="#";
		
		DecimalFormat format = new DecimalFormat(formatString);
	
		return format.format(source);
	}

    public static void formatObject(List source,Integer precition) {
    	if(source!=null){
    		for(int i=0;i<source.size();i++){
    			formatObject(source.get(i),precition);
    		}
    	}
    }

	public static void formatObject(Object source,Integer precition) {
		
		if(precition==null||precition<1||precition>15)precition=3;
		
		String formatString="###0.";
		for(int i=0;i<precition;i++)formatString+="#";
		
		DecimalFormat format = new DecimalFormat(formatString);
	
		BeanInfo sourceBean=null;
		PropertyDescriptor[] propertyDescriptors=null;
		try {
			sourceBean = Introspector.getBeanInfo(source.getClass());
			propertyDescriptors = sourceBean.getPropertyDescriptors();
			
		} catch (Exception e2) {return;}

		
		for (int i = 0; i < propertyDescriptors.length; i++) {

			try {
				PropertyDescriptor pro = propertyDescriptors[i];
				Method rm = pro.getReadMethod();
				if (!Modifier.isPublic(rm.getDeclaringClass().getModifiers())) {
					rm.setAccessible(true);
				}			
				
				Method wm = pro.getWriteMethod();
				if (!Modifier.isPublic(wm.getDeclaringClass().getModifiers())) {
					wm.setAccessible(true);
				}
				
				if(pro.getPropertyType().getName().equals("java.lang.Double")){
					
					
					Double value = (java.lang.Double) rm.invoke(source, new Object[0]);
					
					wm.invoke((Object) source, new Object[] { Double.valueOf(format.format(value)) });
					
					
					
				}
			} catch (Exception e) {}

		}
	}
}