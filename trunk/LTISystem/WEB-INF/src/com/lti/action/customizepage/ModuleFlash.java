package com.lti.action.customizepage;

import com.lti.service.bo.*;
import java.util.*;
import com.lti.type.*;
import com.lti.util.*;

import java.io.*;
import com.lti.bean.*;
import com.lti.system.*;
import com.lti.service.*;
import com.lti.action.customizepage.CustomizePageAction;
import com.lti.customizepage.BasePage;
import com.lti.action.Action;
import freemarker.template.Template;

public class ModuleFlash extends CustomizePageAction{
	public ModuleFlash(){super();}

	private static final long serialVersionUID = 2496394976004782444L;
	@SuppressWarnings("unchecked")
	public String process() throws Exception {

Template t = BasePage.CustomizePageConf.getTemplate("ModuleFlash.ftl");ByteArrayOutputStream buf = new ByteArrayOutputStream();Writer out = new OutputStreamWriter(buf, "utf8");		executeUserCode();
		t.process(root, out);		return (buf.toString());
	}
	@SuppressWarnings("unchecked")
	public void executeUserCode() throws Exception {

		//usage:
//in your customize template, invoke this module with ${module("ModuleFlash","portfolio1,portfolio2","2007-01-01","2008-02-02")}

String portfolios=getModuleParameter(1);
String startDate=getModuleParameter(2);
String endDate=getModuleParameter(3);

		String address = "www.validfi.com";
		root.put("address", address);

		String port = "80";
		root.put("port", port);

		String dataSource = "/LTISystem/jsp/ajax/CustomizePage.action?pageName=OutputXMLExample&portfolios="+portfolios +"&startDate="+startDate+"&endDate="+endDate;

		com.lti.action.flash.URLUTF8Encoder uRLUTF8Encoder = new com.lti.action.flash.URLUTF8Encoder();
		String url = uRLUTF8Encoder.encode(dataSource);
		root.put("url", url);

		String title = uRLUTF8Encoder.encode("Portfolios Compare");
		root.put("title", title);

		String nameArray = uRLUTF8Encoder.encode(portfolios);
		root.put("nameArray", nameArray);

		String flashSrc = "../mutualfund/main.swf";
		root.put("flashSrc", flashSrc);

	}
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		init();
		//usage:
//in your customize template, invoke this module with ${module("ModuleFlash","portfolio1,portfolio2","2007-01-01","2008-02-02")}

String portfolios=getModuleParameter(1);
String startDate=getModuleParameter(2);
String endDate=getModuleParameter(3);

		String address = "www.validfi.com";
		root.put("address", address);

		String port = "80";
		root.put("port", port);

		String dataSource = "/LTISystem/jsp/ajax/CustomizePage.action?pageName=OutputXMLExample&portfolios="+portfolios +"&startDate="+startDate+"&endDate="+endDate;

		com.lti.action.flash.URLUTF8Encoder uRLUTF8Encoder = new com.lti.action.flash.URLUTF8Encoder();
		String url = uRLUTF8Encoder.encode(dataSource);
		root.put("url", url);

		String title = uRLUTF8Encoder.encode("Portfolios Compare");
		root.put("title", title);

		String nameArray = uRLUTF8Encoder.encode(portfolios);
		root.put("nameArray", nameArray);

		String flashSrc = "../mutualfund/main.swf";
		root.put("flashSrc", flashSrc);
		return Action.SUCCESS;
	}

}
