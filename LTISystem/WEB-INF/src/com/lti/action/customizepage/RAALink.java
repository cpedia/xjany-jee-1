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

public class RAALink extends CustomizePageAction{
	public RAALink(){super();}

	private static final long serialVersionUID = 2496394976004782444L;
public static String getLink(String symbol, int interval, String[] indexArray) {
		StringBuffer sb = new StringBuffer();
		sb.append("http://www.validfi.com:8080/LTISystem/jsp/mutualfund/Main.action?symbol=");
		sb.append(symbol);
		sb.append("&interval=");
		sb.append(interval);

		Date date = new Date();
		java.text.DateFormat df = new java.text.SimpleDateFormat("MM/dd/yyyy");
		sb.append("&endDate=");
		sb.append(df.format(date));

		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.MONTH, -6);
		sb.append("&startDate=");
		sb.append(df.format(ca.getTime()));

		sb.append("&allowNegative=");
		sb.append("false");

		sb.append("&indexArray=");
		for (int i = 0; i < indexArray.length; i++) {
			sb.append(indexArray[i]);
			if (i != indexArray.length - 1)
				sb.append(",%20");
		}

		return sb.toString();
	}	@SuppressWarnings("unchecked")
	public String process() throws Exception {

Template t = BasePage.CustomizePageConf.getTemplate("RAALink.ftl");ByteArrayOutputStream buf = new ByteArrayOutputStream();Writer out = new OutputStreamWriter(buf, "utf8");		executeUserCode();
		t.process(root, out);		return (buf.toString());
	}
	@SuppressWarnings("unchecked")
	public void executeUserCode() throws Exception {

		int interval=12;
		String symbol="DODBX";
		String[] indexArray={"VFINX","VGTSX","VGSIX","QRAAX","BEGBX","VBMFX","CASH"};

root.put("raalink", getLink(symbol, interval, indexArray));

String[] symbols={"DODBX","DODBX","DODBX","DODBX"};
List<String> links=new ArrayList<String>();
for(int i=0;i<symbols.length;i++){
	links.add(getLink(symbols[i], interval, indexArray));
}

root.put("links",links);

	}
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		init();
		int interval=12;
		String symbol="DODBX";
		String[] indexArray={"VFINX","VGTSX","VGSIX","QRAAX","BEGBX","VBMFX","CASH"};

root.put("raalink", getLink(symbol, interval, indexArray));

String[] symbols={"DODBX","DODBX","DODBX","DODBX"};
List<String> links=new ArrayList<String>();
for(int i=0;i<symbols.length;i++){
	links.add(getLink(symbols[i], interval, indexArray));
}

root.put("links",links);
		return Action.SUCCESS;
	}

}
