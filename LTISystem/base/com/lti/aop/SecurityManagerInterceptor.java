package com.lti.aop;

import java.util.HashMap;
import java.util.Map;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class SecurityManagerInterceptor implements MethodInterceptor {

	public static Map<String, Long> statMap1=new HashMap<String, Long>();
	public static Map<String, Long> statMap2=new HashMap<String, Long>();
	
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		long startTime = System.currentTimeMillis();
		Object result = null;
		result = methodInvocation.proceed();
		long endTime = System.currentTimeMillis();
		String key=getKey(methodInvocation.getMethod());
		Long count=statMap1.get(key);
		if(count==null){
			statMap1.put(key, 0L);
		}
		else {
			count++;
			statMap1.put(key, count);
		}
		Long time=statMap2.get(key);
		if(time==null){
			time=(endTime - startTime);
		}else{
			time+=(endTime - startTime);
		}
		statMap2.put(key, time);

		return result;
	}
	
	private String getKey(java.lang.reflect.Method method){
		StringBuffer sb=new StringBuffer();
		sb.append(method.getName());
		sb.append("(");
		Class[] paras=method.getParameterTypes();
		
		if(paras!=null){
			for(int i=0;i<paras.length;i++){
				sb.append(paras[i].getName());
				if(i!=paras.length-1)sb.append(",");
			}
		}
		sb.append(")");
		return sb.toString();
	}
}
