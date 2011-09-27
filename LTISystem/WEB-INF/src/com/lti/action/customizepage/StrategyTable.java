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

public class StrategyTable extends CustomizePageAction{
	public StrategyTable(){super();}

	private static final long serialVersionUID = 2496394976004782444L;
	@SuppressWarnings("unchecked")
	public String process() throws Exception {

Template t = BasePage.CustomizePageConf.getTemplate("StrategyTable.ftl");ByteArrayOutputStream buf = new ByteArrayOutputStream();Writer out = new OutputStreamWriter(buf, "utf8");		executeUserCode();
		t.process(root, out);		return (buf.toString());
	}
	@SuppressWarnings("unchecked")
	public void executeUserCode() throws Exception {

		Object[] id={new Long(1)};

List<Strategy> Strategies=strategyManager.getStrategiesByClass(id);

root.put("strategies",Strategies);

	}
	@SuppressWarnings("unchecked")
	public String execute() throws Exception {

		init();
		Object[] id={new Long(1)};

List<Strategy> Strategies=strategyManager.getStrategiesByClass(id);

root.put("strategies",Strategies);
		return Action.SUCCESS;
	}

}
