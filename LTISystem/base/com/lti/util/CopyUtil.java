package com.lti.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.type.LongString;
import com.lti.type.finance.Asset;

public class CopyUtil {
	static Log log = LogFactory.getLog(com.lti.util.CopyUtil.class);

	@Deprecated
	@SuppressWarnings( { "unchecked", "unused" })
	private static void Translate1(com.lti.service.bo.Portfolio p1, com.lti.service.bo.Portfolio p2) {
//		if (p1 == null || p2 == null)
//			return;
//
//		p2.setLogs(p1.getLogs());
//		p2.setTransactions(p1.getTransactions());
//		p2.setDailydatas(p1.getDailydatas());
//		p2.setMpts(p1.getMpts());
//
//		p2.setAssetAllocationParameter(copyHashtable(p1.getAssetAllocationParameter()));
//		p2.setCashFlowParameter(copyHashtable(p1.getCashFlowParameter()));
//		p2.setRebalancingParameter(copyHashtable(p1.getRebalancingParameter()));
//		p2.setAssetAllocationStrategyID(p1.getAssetAllocationStrategyID());
//		p2.setAssets(copyAssets(p1.getAssets()));
//
//		p2.setBenchmarkID(p1.getBenchmarkID());
//
//		p2.setCash(p1.getCash());
//
//		p2.setCashFlowStrategyID(p1.getCashFlowStrategyID());
//
//		p2.setCurrentStrategyID(p1.getCurrentStrategyID());
//
//		p2.setDescription(p1.getDescription());
//
//		p2.setID(p1.getID());
//
//		p2.setConfidence(p1.getConfidence());
//
//		p2.setIsModelPortfolio(p1.getIsModelPortfolio());
//
//		p2.setIsOriginalPortfolio(p1.getIsOriginalPortfolio());
//
//		p2.setMainStrategyID(p1.getMainStrategyID());
//
//		p2.setName(p1.getName());
//
//		p2.setOriginalAmount(p1.getOriginalAmount());
//
//		p2.setPortfolioID(p1.getPortfolioID());
//
//		p2.setRebalancingStrategyID(p1.getRebalancingStrategyID());
//
//		p2.setStartingDate(p1.getStartingDate());
//
//		p2.setState(p1.getState());
//
//		p2.setUserID(p1.getUserID());
//
//		p2.setEndDate(p1.getEndDate());
//
//		p2.setPermissions(copyLongString(p1.getPermissions()));
//
//		p2.setKeywords(copyString(p1.getKeywords()));
//
//		p2.setTrashykeywords(copyString(p1.getTrashykeywords()));
//
//		p2.setMarginRate(p1.getMarginRate());

	}

	public static List<LongString> copyLongString(List<LongString> ls) {
		if (ls == null)
			return null;
		List<LongString> l = new ArrayList<LongString>();
		for (int i = 0; i < ls.size(); i++) {
			l.add(new LongString(new Long(ls.get(i).getID()), new String(ls.get(i).getName())));
		}
		return l;
	}

	public static List<String> copyString(List<String> ls) {
		if (ls == null)
			return null;
		List<String> l = new ArrayList<String>();
		for (int i = 0; i < ls.size(); i++) {
			if (ls.get(i) != null)
				l.add(new String(ls.get(i)));
		}
		return l;
	}

	public static Hashtable<String, String> copyHashtable(Hashtable<String, String> ht) {
		if (ht == null)
			return null;
		Hashtable<String, String> cp = new Hashtable<String, String>();
		Iterator<String> iter = ht.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next().toString();
			cp.put(key, ht.get(key).toString());
		}
		return cp;
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	private static Set<Asset> copyAssets(Set<Asset> assets) {
//		if (assets == null)
//			return null;
//		TreeSet<com.lti.service.bo.Asset> cps = new TreeSet<com.lti.service.bo.Asset>();
//		Iterator<com.lti.service.bo.Asset> iter = assets.iterator();
//		while (iter.hasNext()) {
//			com.lti.service.bo.Asset a1 = iter.next();
//			com.lti.service.bo.Asset cp1 = new com.lti.service.bo.Asset();
//
//			cp1.setName(a1.getName());
//			cp1.setAssetStrategyID(a1.getAssetStrategyID());
//			cp1.setAssetStrategyParameter(copyHashtable(a1.getAssetStrategyParameter()));
//			cp1.setClassID(a1.getClassID());
//			cp1.setTargetPercentage(a1.getTargetPercentage());
//			cp1.setStartingDate(a1.getStartingDate());
//
//			cp1.setPortfolioID(null);
//			cp1.setID(null);
//
//			Set<com.lti.service.bo.SecurityItem> sicps = new TreeSet<com.lti.service.bo.SecurityItem>();
//			Set<com.lti.service.bo.SecurityItem> sis = a1.getSecurityItems();
//
//			if (sis != null) {
//				Iterator<SecurityItem> sisIter = sis.iterator();
//				while (sisIter.hasNext()) {
//					com.lti.service.bo.SecurityItem sicp1 = new com.lti.service.bo.SecurityItem();
//					com.lti.service.bo.SecurityItem si1 = sisIter.next();
//
//					sicp1.setReinvesting(si1.getReinvesting());
//					sicp1.setSecurityID(si1.getSecurityID());
//					sicp1.setShares(si1.getShares());
//
//					sicp1.setAssetID(null);
//					sicp1.setID(null);
//
//					sicps.add(sicp1);
//				}
//			}
//			cp1.setSecurityItems(sicps);
//			cps.add(cp1);
//		}
//		return cps;
		
		return null;
	}

	public static String getMethodParamType(Method wm) {

		String innerClassName = "";
		Type[] paramTypeList = wm.getGenericParameterTypes();
		for (Type paramType : paramTypeList) {
			if (paramType instanceof ParameterizedType) {
				Type[] types = ((ParameterizedType) paramType).getActualTypeArguments();
				for (Type type : types) {

					innerClassName = type.toString().substring(6);
				}
			}
		}
		return innerClassName;
	}

	@SuppressWarnings("unchecked")
	public static void Translate(Object source, Object target) {

		BeanInfo sourceBean = null;
		BeanInfo targetbean = null;
		PropertyDescriptor[] propertyDescriptors = null;
		try {
			sourceBean = Introspector.getBeanInfo(source.getClass());
			targetbean = Introspector.getBeanInfo(target.getClass());
			propertyDescriptors = targetbean.getPropertyDescriptors();

		} catch (Exception e2) {
			// TODO Auto-generated catch block
			log.warn("Translate Error! can not get BeanInfo from the source object or the target object!");
			return;
		}

		for (int i = 0; i < propertyDescriptors.length; i++) {

			PropertyDescriptor pro = propertyDescriptors[i];
			Method wm = pro.getWriteMethod();

			if (wm != null) {

				PropertyDescriptor[] sourcepds = sourceBean.getPropertyDescriptors();
				//
				String innerClassName = getMethodParamType(wm);

				for (int j = 0; j < sourcepds.length; j++) {
					if (sourcepds[j].getName().equals(pro.getName())) {
						try {
							Method rm = sourcepds[j].getReadMethod();

							if (!Modifier.isPublic(rm.getDeclaringClass().getModifiers())) {
								rm.setAccessible(true);
							}
							if (!Modifier.isPublic(wm.getDeclaringClass().getModifiers())) {
								wm.setAccessible(true);
							}
							//
							if (pro.getPropertyType().getName().equals("java.util.Set") || pro.getPropertyType().getName().equals("java.util.List")) {

								java.util.Collection value = (java.util.Collection) rm.invoke(source, new Object[0]);

								Iterator iter = value.iterator();
								if (pro.getPropertyType().getName().equals("java.util.Set")) {
									Set newSet = new TreeSet();
									while (iter.hasNext()) {
										Object s = iter.next();
										Object t;
										Class cls = Class.forName(innerClassName);

										Constructor constr = cls.getConstructor(new Class[] {});
										t = constr.newInstance(new Object[] {});
										Translate(s, t);
										newSet.add(t);

									}
									wm.invoke((Object) target, new Object[] { newSet });
								}// if(pro.getPropertyType().getName().equals("java.util.Set"))
								// end
								if (pro.getPropertyType().getName().equals("java.util.List")) {
									List newList = new ArrayList();
									while (iter.hasNext()) {
										Object s = iter.next();
										Object t;
										Class cls = Class.forName(innerClassName);
										Constructor constr = cls.getConstructor(new Class[] {});
										t = constr.newInstance(new Object[] {});
										Translate(s, t);
										newList.add(t);

									}
									wm.invoke((Object) target, new Object[] { newList });
								}// if(pro.getPropertyType().getName().equals("java.util.Set"))
								// end

							} else {
								Object value = rm.invoke(source, new Object[0]);
								wm.invoke((Object) target, new Object[] { value });
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							if (pro.getName().equals("assets") || pro.getName().equals("modelPortfolios")) {
								// e.printStackTrace();
							}
							log.info("Translate Error ! can not get translate the property '" + pro.getName() + "' or it is null");
						}

						break;
					}// if (sourcepds[j].getName().equals(pro.getName()))
				}// for (int j = 0; j < sourcepds.length; j++)
			}
		}

	}

	public static void printObject(Object source) {
		System.out.print(objecToString(source, ""));
	}

	@SuppressWarnings("unchecked")
	public static String objecToString(Object source) {
		return objecToString(source, "");
	}

	@SuppressWarnings("unchecked")
	public static String objecToString(Object source, String pre) {

		StringBuffer sb = new StringBuffer();

		if (source instanceof java.lang.String) {
			sb.append(pre + source);
			sb.append("\r\n");
			return sb.toString();
		}

		BeanInfo sourceBean = null;
		PropertyDescriptor[] propertyDescriptors = null;
		try {
			sourceBean = Introspector.getBeanInfo(source.getClass());
			propertyDescriptors = sourceBean.getPropertyDescriptors();

		} catch (Exception e2) {
			return "";
		}

		for (int i = 0; i < propertyDescriptors.length; i++) {

			try {
				PropertyDescriptor pro = propertyDescriptors[i];
				Method rm = pro.getReadMethod();
				if (!Modifier.isPublic(rm.getDeclaringClass().getModifiers())) {
					rm.setAccessible(true);
				}

				if (pro.getPropertyType().getName().equals("java.util.Set") || pro.getPropertyType().getName().equals("java.util.List")) {

					sb.append(pre + pro.getName() + ": ");
					sb.append("\r\n");

					java.util.Collection value = (java.util.Collection) rm.invoke(source, new Object[0]);

					Iterator iter = value.iterator();
					while (iter.hasNext()) {
						try {
							Object s = iter.next();
							sb.append(objecToString(s, pre + "\t"));
						} catch (Exception e) {
						}
					}

				} else if (pro.getPropertyType().getName().equals("java.util.Hashtable")) {
					java.util.Hashtable<String, String> ht = (Hashtable) rm.invoke(source, new Object[0]);
					Iterator<String> iter = ht.keySet().iterator();
					sb.append(pre + pro.getName() + ": ");
					sb.append("\r\n");
					while (iter.hasNext()) {
						String key = iter.next();
						sb.append(pre + "\t" + key + " " + ht.get(key));
					}
				} else {
					Object value = rm.invoke(source, new Object[0]);
					sb.append(pre + pro.getName() + ": " + value);
					sb.append("\r\n");
				}
			} catch (Exception e) {
			}

		}
		return sb.toString();
	}

	// does not support list for now
	@SuppressWarnings("unchecked")
	public static String autoFillInFields(Class cls, String name, Vector<String> cols) {
		StringBuffer sb = new StringBuffer();

		String type = cls.getName();
		if (type.equalsIgnoreCase("java.lang.String")) {
			sb.append(type);
			sb.append(" ");
			sb.append(name);
			sb.append("=");
			sb.append("\"myplaniq.com\";\n");
		} else if (type.equalsIgnoreCase("java.lang.Long")) {
			sb.append(type);
			sb.append(" ");
			sb.append(name);
			sb.append("=");
			sb.append("1L;\n");
		} else if (type.equalsIgnoreCase("java.lang.Double")) {
			sb.append(type);
			sb.append(" ");
			sb.append(name);
			sb.append("=");
			sb.append("0.001;\n");
		} else if (type.equalsIgnoreCase("java.lang.Integer")) {
			sb.append(type);
			sb.append(" ");
			sb.append(name);
			sb.append("=");
			sb.append("1;\n");
		} else if (type.equalsIgnoreCase("java.lang.Float")) {
			sb.append(type);
			sb.append(" ");
			sb.append(name);
			sb.append("=");
			sb.append("0.1;\n");

		} else if (type.equalsIgnoreCase("java.lang.Boolean")) {
			sb.append(type);
			sb.append(" ");
			sb.append(name);
			sb.append("=");
			sb.append("true;\n");
		} else if (type.equalsIgnoreCase("java.util.Date") || type.equalsIgnoreCase("java.sql.Timestamp") || type.equalsIgnoreCase("java.sql.Date")) {
			sb.append(type);
			sb.append(" ");
			sb.append(name);
			sb.append("=");
			sb.append("new java.util.Date();\n");
		} else if (cls.isEnum()) {
			Object[] objs = cls.getEnumConstants();
			sb.append(type);
			sb.append(" ");
			sb.append(name);
			sb.append("=");
			sb.append(objs[0]);
			sb.append(";\n");
		} else {
			sb.append(cls.getName());
			sb.append(" ");
			sb.append(name);
			sb.append("= new ");
			sb.append(cls.getName());
			sb.append("();\n");

			Method[] methods = cls.getMethods();
			if (methods != null) {
				for (int i = 0; i < methods.length; i++) {
					Method method = methods[i];
					if (method.getName().startsWith("set")) {
						Class[] parameters = method.getParameterTypes();
						if (parameters.length == 1) {
							Class parameter = parameters[0];
							if (cols != null && !cols.contains(parameter.getSimpleName().toLowerCase()))
								continue;
							if (parameter.isArray()) {
								String innerType = parameter.getComponentType().getSimpleName();
								sb.append(innerType);
								sb.append("[] ");
								sb.append(name + "_" + method.getName().substring(3) + "s");
								sb.append("=new ");
								sb.append(innerType);
								sb.append("[1];\n");

								sb.append(autoFillInFields(parameter.getComponentType(), name + "_" + method.getName().substring(3) + "s0", cols));

								sb.append(name + "_" + method.getName().substring(3) + "s[0]=");
								sb.append(name + "_" + method.getName().substring(3) + "s0;\n");

								sb.append(name);
								sb.append(".");
								sb.append(method.getName());
								sb.append("(");
								sb.append(name + "_" + method.getName().substring(3) + "s");
								sb.append(");\n");
							} else {
								sb.append(autoFillInFields(parameter, name + "_" + method.getName().substring(3), cols));

								sb.append(name);
								sb.append(".");
								sb.append(method.getName());
								sb.append("(");
								sb.append(name + "_" + method.getName().substring(3));
								sb.append(");\n");
							}
						}
					}
				}
			}
		}

		return sb.toString();

	}

	public static void main(String[] args) {
		System.out.println(autoFillInFields(Asset.class, "asset", null));

	}

	public static void setAttributes(PropertyDescriptor pro, Method wm, Object object, Object value) {

		try {
			if (pro.getPropertyType().getName().equals("java.lang.String") || pro.getPropertyType().getName().equals("String")) {

				if (value != null)
					wm.invoke((Object) object, new Object[] { String.valueOf(value) });

			} else if (pro.getPropertyType().getName().equals("java.lang.Double") || pro.getPropertyType().getName().equals("double")) {
				if (value != null)
					wm.invoke((Object) object, new Object[] { Double.parseDouble(String.valueOf(value)) });
			} else if (pro.getPropertyType().getName().equals("java.lang.Integer") || pro.getPropertyType().getName().equals("int")) {
				if (value != null)
					wm.invoke((Object) object, new Object[] { Integer.parseInt(String.valueOf(value)) });
			} else if (pro.getPropertyType().getName().equals("java.lang.Long") || pro.getPropertyType().getName().equals("long")) {
				if (value != null)
					wm.invoke((Object) object, new Object[] { Long.parseLong(String.valueOf(value)) });
			} else if (pro.getPropertyType().getName().equals("java.lang.Float") || pro.getPropertyType().getName().equals("float")) {
				if (value != null)
					wm.invoke((Object) object, new Object[] { Float.parseFloat(String.valueOf(value)) });
			}else if (pro.getPropertyType().getName().equals("java.lang.Boolean") || pro.getPropertyType().getName().equals("boolean")) {
				if (value != null)
					wm.invoke((Object) object, new Object[] { Boolean.parseBoolean(String.valueOf(value)) });
			} else if (pro.getPropertyType().getName().equals("java.util.Date")) {
				if (value != null)
					wm.invoke((Object) object, new Object[] { (java.util.Date) value });
			} else {

			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	public static void TranslateToObject(Map<String, String> request, Object o) {
		BeanInfo targetbean = null;
		PropertyDescriptor[] propertyDescriptors = null;
		try {
			targetbean = Introspector.getBeanInfo(o.getClass());
			propertyDescriptors = targetbean.getPropertyDescriptors();
		} catch (Exception ew) {
			ew.printStackTrace();
			return;
		}
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor pd = propertyDescriptors[i];
			Method wm = pd.getWriteMethod();
			if (wm != null) {
				setAttributes(pd, wm, o, request.get(pd.getName()));
			}
		}
	}

}
