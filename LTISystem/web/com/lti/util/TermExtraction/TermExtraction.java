package com.lti.util.TermExtraction;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class TermExtraction {
	private static final String URL = "http://api.search.yahoo.com/ContentAnalysisService/V1/termExtraction";

	private static final Log log = LogFactory.getLog(TermExtraction.class);
	
	public static List<String> getTags(String appid,String context){

		try {
			HttpClient httpClient = new HttpClient();
			PostMethod postMethod = new PostMethod(URL);
			postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf8");
			NameValuePair[] data = { new NameValuePair("appid", appid), new NameValuePair("context", context), };
			postMethod.addParameters(data);
			int responseCode = httpClient.executeMethod(postMethod);
			if (responseCode != 200) {
				return null;
			}

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(true);
			factory.setIgnoringElementContentWhitespace(true);
			factory.setIgnoringComments(true);
			DocumentBuilder builder = factory.newDocumentBuilder();
			builder.setErrorHandler(new ErrorHandler() {
				public void warning(SAXParseException e) throws SAXException {
					log.warn(e);
					throw e;
				}

				public void error(SAXParseException e) throws SAXException {
					log.error(e);
					throw e;
				}

				public void fatalError(SAXParseException e) throws SAXException {
					log.fatal(e);
					throw e;
				}
			});

			List<String> tags = new ArrayList<String>();
			InputStream in = postMethod.getResponseBodyAsStream();
			Document doc = builder.parse(in);
			in.close();
			NodeList results = doc.getElementsByTagName("Result");
			if (results != null) {
				for (int i = 0; i < results.getLength(); i++) {
					Node node = results.item(i);
					String tag = getTextValue(node);
					if(tag!=null){
						tags.add(tag);
					}
				}
			}

			return tags;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public static String getTextValue(Node node) {
		if (node.hasChildNodes()) {
			return node.getFirstChild().getNodeValue();
		} else {
			return "";
		}
	}
}
