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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl;

public class ListDecoder {

	/**
	 * Anti-serialize XML to object
	 * 
	 * @param is
	 * @return
	 */
	public static Object XMLToList(InputStream is) throws Exception {
		Document doc = null;

		DocumentBuilderFactory dbf = new DocumentBuilderFactoryImpl();
		DocumentBuilder db = dbf.newDocumentBuilder();
		doc = db.parse(is);

		Element root = doc.getDocumentElement();

		return XMLToList(root);
	}

	/**
	 * Anti-serialize XML to object
	 * 
	 * @param str
	 * @return
	 */
	public static Object XMLToList(String str) {
		if (str == null || str.length() == 0)
			return null;
		try {
			StringBufferInputStream sis = new StringBufferInputStream(str);
			return XMLToList(sis);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
			return null;
		}

	}

	public static Object XMLToList(Element root) {
		List<Node> structures = getChildsByTag(root, "s");
		Integer length = Integer.parseInt(getChildsByTag((Element) structures.get(0), "l").get(0).getTextContent());
		String type = getChildsByTag((Element) structures.get(0), "t").get(0).getTextContent();
		Map<Integer, String> names = new HashMap<Integer, String>();
		Map<Integer, String> types = new HashMap<Integer, String>();

		for (Integer i = 0; i < length; i++) {
			List<Node> cis = getChildsByTag((Element) structures.get(0), "c" + i);
			if (cis == null || cis.size() == 0)
				continue;

			names.put(i, getChildsByTag((Element) cis.get(0), "n").get(0).getTextContent());
			types.put(i, getChildsByTag((Element) cis.get(0), "t").get(0).getTextContent());
		}

		List<Node> valueList = getChildsByTag(root, "v");
		if (valueList == null || valueList.size() < 1)
			return null;

		List list = new ArrayList();
		Class cls = null;
		try {
			cls = Class.forName(type);
		} catch (Exception e) {
			return null;
		}
		for (Integer i = 0; i < valueList.size(); i++) {
			
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

			for (int p = 0; p < length; p++) {
				List<Node> es = getChildsByTag((Element) valueList.get(i),"v"+i);
				if(es==null||es.size()==0)continue;

				String name=names.get(i);
				for (int k = 0; k < propertyDescriptors.length; k++) {
					PropertyDescriptor pro = propertyDescriptors[k];
					if (pro.getName().equals(name)) {
						Method wm = pro.getWriteMethod();
						if (wm != null) {
							if (!Modifier.isPublic(wm.getDeclaringClass().getModifiers())) {
								wm.setAccessible(true);
							}
							Object o=null;
							try {
								if (type.equalsIgnoreCase("java.lang.String") || type.equals("String")) {
									o=es.get(0).getTextContent();
								} else if (type.equalsIgnoreCase("java.lang.Long") || type.equalsIgnoreCase("Long")) {
									o=Long.parseLong(es.get(0).getTextContent());
								} else if (type.equalsIgnoreCase("java.lang.Double") || type.equalsIgnoreCase("Double")) {
									o=Double.parseDouble(es.get(0).getTextContent());
								} else if (type.equalsIgnoreCase("java.lang.Integer") || type.equalsIgnoreCase("int") || type.equalsIgnoreCase("Integer")) {
									o=Integer.parseInt(es.get(0).getTextContent());
								} else if (type.equalsIgnoreCase("java.lang.Float") || type.equalsIgnoreCase("Float")) {
									o=Float.parseFloat(es.get(0).getTextContent());
								} else if (type.equalsIgnoreCase("java.lang.Boolean") || type.equalsIgnoreCase("Boolean")) {
									o=Boolean.parseBoolean(es.get(0).getTextContent());
								}else{
								}
							} catch (Exception e) {
							}
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
				if(obj!=null)list.add(obj);
			}// end outter for
		}
		return list;
	}

	public static String covertNodeToString(Node node) throws TransformerFactoryConfigurationError, TransformerException {
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		DOMSource source = new DOMSource(node);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		transformer.transform(source, new StreamResult(baos));

		return String.valueOf(baos);
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
