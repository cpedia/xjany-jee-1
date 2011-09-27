package com.lti.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.XMLDecoder;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.lti.service.bo.Portfolio;
import com.lti.system.ContextHolder;
import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;

public class MergeUtil {
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

	public static void main(String[] args){
		Portfolio p1=ContextHolder.getPortfolioManager().get(408L);
		String s=objectToXML(p1);
		Portfolio p2=(Portfolio) XMLToObject(s);
		System.out.println(CopyUtil.objecToString(p1));
		System.out.println("================================");
		System.out.println(CopyUtil.objecToString(p2));
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
		} else if (obj instanceof java.util.Hashtable) {
			Hashtable ht = (Hashtable) obj;
			Iterator iter = ht.keySet().iterator();
			while (iter.hasNext()) {
				String key = (String) iter.next();
				String value = (String) ht.get(key);
				sb.append("<pair>");
				sb.append("<key>");
				sb.append(key);
				sb.append("</key>");
				sb.append("<value>" +value+ "</value>");
				sb.append("</pair>");
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

		sb.append("</object>");
		return sb.toString();
	}
	
	/**
	 * Anti-serialize XML to object
	 * 
	 * @param is
	 * @return
	 */
	public static Object XMLToObject(InputStream is) throws Exception {
		Document doc = null;

		DocumentBuilderFactory dbf =new DocumentBuilderFactoryImpl();
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(is);

		Element root = doc.getDocumentElement();

		return XMLToObject(root);
	}

	/**
	 * Anti-serialize XML to object
	 * 
	 * @param str
	 * @return
	 */
	public static Object XMLToObject(String str) {
		if (str == null || str.length() == 0)
			return null;
		try {
			StringBufferInputStream sis = new StringBufferInputStream(str);
			return XMLToObject(sis);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			//e1.printStackTrace();
			return null;
		}

	}

	public static Object XMLToObject(Element root) {

		List<Node> typeList = getChildsByTag(root, "type");
		if (typeList == null || typeList.size() < 1)
			return null;
		String type = typeList.get(0).getTextContent();

		Object obj = null;
		if (type.equalsIgnoreCase("java.lang.String") || type.equals("String")) {

			List<Node> valueList = getChildsByTag(root, "value");
			;
			if (valueList == null || valueList.size() < 1)
				return null;
			String value = valueList.get(0).getTextContent();
			obj = new String(value);

		} else if (type.equalsIgnoreCase("java.lang.Long") || type.equalsIgnoreCase("Long")) {

			List<Node> valueList = getChildsByTag(root, "value");
			if (valueList == null || valueList.size() < 1)
				return null;
			String value = valueList.get(0).getTextContent();
			try {
				obj = Long.parseLong(value);
			} catch (NumberFormatException e) {
				return null;
			}

		} else if (type.equalsIgnoreCase("java.lang.Double") || type.equalsIgnoreCase("Double")) {

			List<Node> valueList = getChildsByTag(root, "value");
			if (valueList == null || valueList.size() < 1)
				return null;
			String value = valueList.get(0).getTextContent();
			try {
				obj = Double.parseDouble(value);
			} catch (NumberFormatException e) {
				return null;
			}

		} else if (type.equalsIgnoreCase("java.lang.Integer") || type.equalsIgnoreCase("int") || type.equalsIgnoreCase("Integer")) {

			List<Node> valueList = getChildsByTag(root, "value");
			if (valueList == null || valueList.size() < 1)
				return null;
			String value = valueList.get(0).getTextContent();
			try {
				obj = Integer.parseInt(value);
			} catch (NumberFormatException e) {
				return null;
			}

		} else if (type.equalsIgnoreCase("java.lang.Float") || type.equalsIgnoreCase("Float")) {

			List<Node> valueList = getChildsByTag(root, "value");
			if (valueList == null || valueList.size() < 1)
				return null;
			String value = valueList.get(0).getTextContent();
			try {
				obj = Float.parseFloat(value);
			} catch (NumberFormatException e) {
				return null;
			}

		} else if (type.equalsIgnoreCase("java.lang.Boolean") || type.equalsIgnoreCase("Boolean")) {

			List<Node> valueList = getChildsByTag(root, "value");
			if (valueList == null || valueList.size() < 1)
				return null;
			String value = valueList.get(0).getTextContent();
			try {
				obj = Boolean.parseBoolean(value);
			} catch (Exception e) {
				return null;
			}

		} else if (type.equalsIgnoreCase("java.util.Date") || type.equalsIgnoreCase("java.sql.Timestamp") || type.equalsIgnoreCase("java.sql.Date")) {

			List<Node> valueList = getChildsByTag(root, "value");
			if (valueList == null || valueList.size() < 1)
				return null;
			String value = valueList.get(0).getTextContent();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				obj = sdf.parse(value);
			} catch (ParseException e) {
				return null;
			}

		} else if (type.equalsIgnoreCase("java.util.Hashtable")) {

			List<Node> pairList = getChildsByTag(root, "pair");
			if (pairList == null)
				return null;
			Hashtable<String, String> ht = new Hashtable<String, String>();
			for (int i = 0; i < pairList.size(); i++) {
				try {

					Element pair = (Element) pairList.get(i);
					String key = getChildsByTag(pair, "key").get(0).getTextContent();
					String value;
					Element vE = (Element) getChildsByTag(pair, "value").get(0);
					if (vE.getElementsByTagName("object").getLength() > 0) {
						value = covertNodeToString(getChildsByTag(vE, "object").get(0));
						// System.out.println(value);
					} else {
						value = getChildsByTag(pair, "value").get(0).getTextContent();
						// System.out.println(value);
					}

					ht.put(key, value);
				} catch (Exception e) {
					e.printStackTrace();
					continue;
				}
			}
			obj = ht;
		} else if (type.equalsIgnoreCase("java.util.Set") || type.equalsIgnoreCase("java.util.List")) {
			Collection col = null;
			if (type.equalsIgnoreCase("java.util.Set")) {
				col = new TreeSet();
			} else {
				col = new ArrayList();
			}
			List<Node> items = getChildsByTag(root, "object");

			if (items == null || items.size() < 1)
				return null;
			for (int j = 0; j < items.size(); j++) {
				Element item = (Element) items.get(j);
				Object o = XMLToObject(item);
				if (o != null)
					col.add(o);
			}
			obj = col;
		} else {
			Class cls = null;
			try {
				cls = Class.forName(type);
				obj = cls.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			List<Node> objectList = getChildsByTag(root, "object");
			if (objectList == null || objectList.size() < 1)
				return null;
			BeanInfo beanInfo = null;
			PropertyDescriptor[] propertyDescriptors = null;
			try {
				beanInfo = Introspector.getBeanInfo(cls);
				propertyDescriptors = beanInfo.getPropertyDescriptors();
			} catch (Exception e2) {
				e2.printStackTrace();
				return null;
			}

			for (int i = 0; i < objectList.size(); i++) {
				Element e = (Element) objectList.get(i);

				String name = null;
				try {
					name = getChildsByTag(e, "name").get(0).getTextContent();
				} catch (DOMException e2) {
					e2.printStackTrace();
					continue;
				}

				for (int j = 0; j < propertyDescriptors.length; j++) {
					PropertyDescriptor pro = propertyDescriptors[j];
					if (pro.getName().equals(name)) {
						Method wm = pro.getWriteMethod();
						if (wm != null) {
							if (!Modifier.isPublic(wm.getDeclaringClass().getModifiers())) {
								wm.setAccessible(true);
							}
							Object o = XMLToObject(e);
							if (o != null) {
								try {
									wm.invoke((Object) obj, new Object[] { o });
								} catch (Exception e1) {
									e1.printStackTrace();
									continue;
								}
							}
						}
					}// end if
				}// end inner for
			}// end outter for
		}

		return obj;
	}

	public static String covertNodeToString(Node node) throws TransformerFactoryConfigurationError, TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		DOMSource source = new DOMSource(node);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		transformer.transform(source, new StreamResult(baos));

		return String.valueOf(baos);
	}

	public static void ObjectXMLDecoder(Object obj, String XMLStr) {
		if (XMLStr == null)
			return;

		Document doc = null;

		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			StringBufferInputStream sis = new StringBufferInputStream(XMLStr);
			doc = db.parse(sis);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}

		Element root = doc.getDocumentElement();

		List<Node> objectList = getChildsByTag(root, "object");
		if (objectList == null || objectList.size() < 1)
			return;
		BeanInfo beanInfo = null;
		PropertyDescriptor[] propertyDescriptors = null;
		try {
			beanInfo = Introspector.getBeanInfo(obj.getClass());
			propertyDescriptors = beanInfo.getPropertyDescriptors();
		} catch (Exception e2) {
			e2.printStackTrace();
			return;
		}

		for (int i = 0; i < objectList.size(); i++) {
			Element e = (Element) objectList.get(i);

			String name = null;
			try {
				name = getChildsByTag(e, "name").get(0).getTextContent();
			} catch (DOMException e2) {
				e2.printStackTrace();
				continue;
			}

			for (int j = 0; j < propertyDescriptors.length; j++) {
				PropertyDescriptor pro = propertyDescriptors[j];
				if (pro.getName().equals(name)) {
					Method wm = pro.getWriteMethod();
					if (wm != null) {
						if (!Modifier.isPublic(wm.getDeclaringClass().getModifiers())) {
							wm.setAccessible(true);
						}
						Object o = XMLToObject(e);
						if (o != null) {
							try {
								wm.invoke((Object) obj, new Object[] { o });
							} catch (Exception e1) {
								e1.printStackTrace();
								continue;
							}
						}
					}
				}// end if
			}// end inner for
		}// end outter for
	}

	public static List<Node> getChildsByTag(Element parentNode, String tagName) {
		List<Node> childs = new ArrayList<Node>();
		try {
			NodeList allChilds = parentNode.getElementsByTagName(tagName);
			Node e;
			for (int i = 0; i < allChilds.getLength(); i++) {
				e = allChilds.item(i);
				if (e.getNodeType() == Node.ELEMENT_NODE && e.getParentNode().equals(parentNode)) {
					childs.add(e);
				}
			}// for
		} catch (Exception ie) {
			ie.printStackTrace();
		}

		return childs;
	}

	/**
	 * Decoder supplied by java
	 * 
	 * @param objString
	 * @return
	 * @throws Exception
	 */
	public static List objectXmlDecoder(String objString) throws Exception {
		List objList = new ArrayList();
		StringBufferInputStream fis = new StringBufferInputStream(objString);
		XMLDecoder decoder = new XMLDecoder(fis);
		Object obj = null;
		try {
			while ((obj = decoder.readObject()) != null) {
				objList.add(obj);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
		fis.close();
		decoder.close();
		return objList;
	}
}
