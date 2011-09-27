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

public class Update extends CustomizePageAction{
	public Update(){super();}

	private static final long serialVersionUID = 2496394976004782444L;
	@SuppressWarnings("unchecked")
	public String process() throws Exception {

Template t = BasePage.CustomizePageConf.getTemplate("Update.ftl");ByteArrayOutputStream buf = new ByteArrayOutputStream();Writer out = new OutputStreamWriter(buf, "utf8");		executeUserCode();
		t.process(root, out);		return (buf.toString());
	}
	@SuppressWarnings("unchecked")
	public void executeUserCode() throws Exception {

		Date date = new Date();
java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy");
	
Calendar ca = Calendar.getInstance();
ca.add(Calendar.MONTH, -6);

root.put("EndDate", df.format(date));
root.put("StartDate", df.format(ca.getTime()));
root.put("HomeURL", "CustomizePage.action?pageName=");

	}
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		init();
		Date date = new Date();
java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy");
	
Calendar ca = Calendar.getInstance();
ca.add(Calendar.MONTH, -6);

root.put("EndDate", df.format(date));
root.put("StartDate", df.format(ca.getTime()));
root.put("HomeURL", "CustomizePage.action?pageName=");
		return Action.SUCCESS;
	}

}
