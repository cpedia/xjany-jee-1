package com.lti.action;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

public class DoubleConvertor extends StrutsTypeConverter {
	@Override
	public Object convertFromString(Map context, String[] values, Class toClass) {
		if (Double.class == toClass) {
			String doubleStr = values[0];
			Double d = Double.parseDouble(doubleStr);
			return d;
		}
		return 0;
	}

	@Override
	public String convertToString(Map context, Object o) {
		if (o != null)
			return o.toString();
		else
			return "";
	}

}
