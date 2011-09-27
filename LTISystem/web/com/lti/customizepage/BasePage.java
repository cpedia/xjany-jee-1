package com.lti.customizepage;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lti.service.AssetClassManager;
import com.lti.service.HolidayManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyClassManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.system.ContextHolder;
import com.lti.util.PersistentUtil;
import com.opensymphony.xwork2.ActionSupport;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class BasePage extends ActionSupport{

	public BasePage(){
		super();
		root = new HashMap();
	}
	
	public class ModuleTemplateMethodModel implements TemplateMethodModel  {
		public Object exec(List arguments) throws TemplateModelException {
			try {
				BasePage bp = PageCompiler.getPage((String) arguments.get(0));
				bp.init();
				bp.setModuleParameters(arguments);
				return bp.process();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "Module does not exist.";
		}
	}
	
	public class PersistentTemplateMethodModel implements TemplateMethodModel {
		public Object exec(List arguments) throws TemplateModelException {
			try {
				return PersistentUtil.readObject((String)arguments.get(0));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "Object did not exist.";
		}
	}

	protected AssetClassManager assetClassManager;
	protected PortfolioManager portfolioManager;
	protected SecurityManager securityManager;
	protected StrategyManager strategyManager;
	protected HolidayManager holidayManager;
	protected StrategyClassManager strategyClassManager;
	protected UserManager userManager;

	protected Map parameters;

	public String getParameter(String key) {
		if (parameters != null) {
			String[] v = (String[]) parameters.get(key);
			if (v != null && v.length >= 1)
				return v[0];
		}
		return null;
	}

	protected List moduleParameters;

	public String getModuleParameter(int i) {
		if (moduleParameters != null && i < moduleParameters.size()) {
			return (String) moduleParameters.get(i);
		}
		return null;
	}

	public static Configuration CustomizePageConf = new Configuration();
	protected Map root;


	public Map getRoot() {
		return root;
	}

	public void setRoot(Map root) {
		this.root = root;
	}

	public void init() throws Exception {
		CustomizePageConf.setDirectoryForTemplateLoading(new File(ContextHolder.getServletPath() + "/jsp/customizepage/"));
		// CustomizePageConf.setObjectWrapper(ObjectWrapper.BEANS_WRAPPER);

		assetClassManager = (AssetClassManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		portfolioManager = (PortfolioManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
		securityManager = (SecurityManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		strategyManager = (StrategyManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("strategyManager");
		holidayManager = (HolidayManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("holidayManager");
		strategyClassManager = (StrategyClassManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("strategyClassManager");
		userManager = (UserManager) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("userManager");

		root.put("module", new ModuleTemplateMethodModel());
		root.put("readObject", new PersistentTemplateMethodModel());

	}

	public String process() throws Exception {
		return "Customize Page";
	}
	public void executeUserCode() throws Exception {
	}

	public String getOutput() throws Exception {
		init();
		return process();
	}

	public String getStackTraceString(Exception e) {

		return com.lti.util.StringUtil.getStackTraceString(e);
	}

	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	public List getModuleParameters() {
		return moduleParameters;
	}

	public void setModuleParameters(List moduleParameters) {
		this.moduleParameters = moduleParameters;
	}
}
