package com.lti.system;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.transaction.Synchronization;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.AbstractRefreshableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.supercsv.io.CsvListWriter;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.lti.service.AssetClassManager;
import com.lti.service.BaseFormulaManager;
import com.lti.service.CompanyIndexManager;
import com.lti.service.DataManager;
import com.lti.service.FinancialStatementManager;
import com.lti.service.GroupManager;
import com.lti.service.HolidayManager;
import com.lti.service.IndicatorManager;
import com.lti.service.InviteManager;
import com.lti.service.JforumManager;
import com.lti.service.MutualFundManager;
import com.lti.service.PortfolioHoldingManager;
import com.lti.service.PortfolioManager;
import com.lti.service.ProfileManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyClassManager;
import com.lti.service.StrategyManager;
import com.lti.service.UserManager;
import com.lti.service.WidgetUserManager;
import com.lti.util.FileOperator;
import com.lti.util.StringUtil;
import com.lti.Exception.Executor.SimulateException;
import com.lti.FinancialStatement.FinancialStatement;

public class ContextHolder {
	private final static ContextHolder instance = new ContextHolder();

	public static ContextHolder getInstance() {
		return instance;
	}

	private ApplicationContext ac = null;

	private String filePath = "applicationContext.xml";

	private static String servletPath;

	public static String getServletPath() {

		if (servletPath == null)
			servletPath = System.getProperty("user.dir");

		return servletPath;
	}
	
	public static String getImagePath(){

		return "/images/";
	}

	public static synchronized void setServletPath(String s) {
		servletPath = s;
	}

	private ContextHolder() {
	}

	private ContextHolder(String file) {
		filePath = file;
	}

	public ApplicationContext getApplicationContext() {
		if (ac == null) {
			String[] paths = { filePath };
			ac = new ClassPathXmlApplicationContext(paths);
		}
		return ac;
	}

	public static void RefreshContext() {
		AbstractRefreshableApplicationContext arac = ((AbstractRefreshableApplicationContext) getInstance().getApplicationContext());
		arac.refresh();
		System.out.println("refresh ok..");
	}
	public static String getDailyExecutionCornExp(){
		return getCornExpression("executeDailyStrategyTrigger");
	}
	public static void setDailyExecutionCornExp(String cornExpression)throws Exception{
		backup();
		try {
			setCornExpression("executeDailyStrategyTrigger",cornExpression);
			RefreshContext();
		} catch (Exception e) {
			e.printStackTrace();
			restore();
		}
	}
	
	public static String getClearPortfolioCornExp(){
		return getCornExpression("clearTempPortfolioTrigger");
	}
	
	public static void setClearPortfolioCornExp(String cornExpression)throws Exception{
		backup();
		try {
			setCornExpression("clearTempPortfolioTrigger",cornExpression);
			RefreshContext();
		} catch (Exception e) {
			e.printStackTrace();
			restore();
		}
	}
	public static String getUpdateDatabaseCornExp(){
		return getCornExpression("updateDailyDataTrigger");
	}
	
	public static void setUpdateDatabaseCornExp(String cornExpression)throws Exception{
		backup();
		try {
			setCornExpression("updateDailyDataTrigger",cornExpression);
			RefreshContext();
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			restore();
		}
	}
	
	public static void setCornExpression(String executeDailyStrategyTrigger,String clearTempPortfolioTrigger,String updateDailyDataTrigger)throws Exception{
		backup();
		try {
			String resource = ContextHolder.getServletPath() + "/WEB-INF/applicationContext.xml";
			
			SAXReader reader=new SAXReader();
			File outFile = new File(resource);
			reader.setEntityResolver(new EntityResolver(){
				public InputSource resolveEntity(java.lang.String publicId, java.lang.String systemId) throws SAXException,
				java.io.IOException	{
					if (systemId.equals("http://www.springframework.org/dtd/spring-beans.dtd"))
						return new InputSource(new ByteArrayInputStream("".getBytes()));
					else
						return null;
				}
			});
			
			org.dom4j.Document  doc=reader.read(outFile);
			org.dom4j.Element root = doc.getRootElement();

			List beans = root.selectNodes("bean");
			for (int i = 0; i < beans.size(); i++) {
				org.dom4j.Element e = (org.dom4j.Element) beans.get(i);
				String id_x=e.attributeValue("id");
				if (id_x!=null&&id_x.equals("executeDailyStrategyTrigger")) {
					List props = e.selectNodes("property");
					for (int j = 0; j < props.size(); j++) {
						org.dom4j.Element prop = (org.dom4j.Element) props.get(j);
						String name = prop.attributeValue("name");
						if (name!=null&&name.equals("cronExpression")) {
							// prop.setAttribute("value", "test");
							prop.setAttributeValue("value", executeDailyStrategyTrigger);
						}
					}
				}
				if (id_x!=null&&id_x.equals("clearTempPortfolioTrigger")) {
					List props = e.selectNodes("property");
					for (int j = 0; j < props.size(); j++) {
						org.dom4j.Element prop = (org.dom4j.Element) props.get(j);
						String name = prop.attributeValue("name");
						if (name!=null&&name.equals("cronExpression")) {
							// prop.setAttribute("value", "test");
							prop.setAttributeValue("value", clearTempPortfolioTrigger);
						}
					}
				}
				
				if (id_x!=null&&id_x.equals("updateDailyDataTrigger")) {
					List props = e.selectNodes("property");
					for (int j = 0; j < props.size(); j++) {
						org.dom4j.Element prop = (org.dom4j.Element) props.get(j);
						String name = prop.attributeValue("name");
						if (name!=null&&name.equals("cronExpression")) {
							// prop.setAttribute("value", "test");
							prop.setAttributeValue("value", updateDailyDataTrigger);
						}
					}
				}
			}
			XMLWriter writer = new XMLWriter(new FileWriter(resource));
			writer.write(doc);
			writer.close();
			
			RefreshContext();
		} catch (Exception e) {
			e.printStackTrace();
			restore();
		}
	}
	
	public static void setSQLString(String value)throws Exception{
		String path1=ContextHolder.getServletPath() + "/WEB-INF/applicationContext.xml";
		String path2=ContextHolder.getServletPath() + "/WEB-INF/classes/applicationContext.xml";
		setSQLString(value,path1);
		setSQLString(value,path2);
		
		PropertiesConfiguration pc=new PropertiesConfiguration(ContextHolder.getServletPath() + "/WEB-INF/classes/log4j.properties");
		pc.setProperty("log4j.appender.DATABASE.URL", value);
		pc.save();
	}
	
	public static void setLanguage(String value)throws Exception{
		PropertiesConfiguration pc=new PropertiesConfiguration(ContextHolder.getServletPath() + "/WEB-INF/classes/struts.properties");
		pc.setProperty("struts.locale", value);
		pc.save();
		
		PropertiesConfiguration pc2=new PropertiesConfiguration(ContextHolder.getServletPath() + "/WEB-INF/classes/system-config.properties");
		pc2.setProperty("nation", value);
		pc2.save();
	}
	
	public static void setConfigFile(String value)throws Exception{
		PropertiesConfiguration pc2=new PropertiesConfiguration(ContextHolder.getServletPath() + "/WEB-INF/classes/system-config.properties");
		pc2.setProperty("config_file", value);
		pc2.save();
	}
	
	public static void setSQLString(String value,String path)throws Exception{
		String resource =path;
		
		SAXReader reader=new SAXReader();
		File outFile = new File(resource);
		reader.setEntityResolver(new EntityResolver(){
			public InputSource resolveEntity(java.lang.String publicId, java.lang.String systemId) throws SAXException,
			java.io.IOException	{
				if (systemId.equals("http://www.springframework.org/dtd/spring-beans.dtd"))
					return new InputSource(new ByteArrayInputStream("".getBytes()));
				else
					return null;
			}
		});
		
		org.dom4j.Document  doc=reader.read(outFile);
		org.dom4j.Element root = doc.getRootElement();

		List beans = root.selectNodes("bean");
		for (int i = 0; i < beans.size(); i++) {
			org.dom4j.Element e = (org.dom4j.Element) beans.get(i);
			String id_x=e.attributeValue("id");
			if (id_x!=null&&id_x.equals("dataSource")) {
				List props = e.selectNodes("property");
				for (int j = 0; j < props.size(); j++) {
					org.dom4j.Element prop = (org.dom4j.Element) props.get(j);
					String name = prop.attributeValue("name");
					if (name!=null&&name.equals("url")) {
						// prop.setAttribute("value", "test");
						prop.setAttributeValue("value", value);
					}
				}
			}
		}
		XMLWriter writer = new XMLWriter(new FileWriter(resource));
		writer.write(doc);
		writer.close();
	}
	
	private static void setCornExpression(String id,String value)throws Exception{
		String resource = ContextHolder.getServletPath() + "/WEB-INF/applicationContext.xml";
		
		SAXReader reader=new SAXReader();
		File outFile = new File(resource);
		reader.setEntityResolver(new EntityResolver(){
			public InputSource resolveEntity(java.lang.String publicId, java.lang.String systemId) throws SAXException,
			java.io.IOException	{
				if (systemId.equals("http://www.springframework.org/dtd/spring-beans.dtd"))
					return new InputSource(new ByteArrayInputStream("".getBytes()));
				else
					return null;
			}
		});
		
		org.dom4j.Document  doc=reader.read(outFile);
		org.dom4j.Element root = doc.getRootElement();

		List beans = root.selectNodes("bean");
		for (int i = 0; i < beans.size(); i++) {
			org.dom4j.Element e = (org.dom4j.Element) beans.get(i);
			String id_x=e.attributeValue("id");
			if (id_x!=null&&id_x.equals(id)) {
				List props = e.selectNodes("property");
				for (int j = 0; j < props.size(); j++) {
					org.dom4j.Element prop = (org.dom4j.Element) props.get(j);
					String name = prop.attributeValue("name");
					if (name!=null&&name.equals("cronExpression")) {
						// prop.setAttribute("value", "test");
						prop.setAttributeValue("value", value);
					}
				}
			}
		}
		XMLWriter writer = new XMLWriter(new FileWriter(resource));
		writer.write(doc);
		writer.close();
	}
	
	private static String getCornExpression(String id){
		try {
			String resource = ContextHolder.getServletPath() + "/WEB-INF/applicationContext.xml";
			
			SAXReader reader=new SAXReader();
			File outFile = new File(resource);
			reader.setEntityResolver(new EntityResolver(){
				public InputSource resolveEntity(java.lang.String publicId, java.lang.String systemId) throws SAXException,
				java.io.IOException	{
					if (systemId.equals("http://www.springframework.org/dtd/spring-beans.dtd"))
						return new InputSource(new ByteArrayInputStream("".getBytes()));
					else
						return null;
				}
			});
			
			org.dom4j.Document  doc=reader.read(outFile);
			org.dom4j.Element root = doc.getRootElement();

			List beans = root.selectNodes("bean");
			for (int i = 0; i < beans.size(); i++) {
				org.dom4j.Element e = (org.dom4j.Element) beans.get(i);
				String id_x=e.attributeValue("id");
				if (id_x!=null&&id_x.equals(id)) {
					List props = e.selectNodes("property");
					for (int j = 0; j < props.size(); j++) {
						org.dom4j.Element prop = (org.dom4j.Element) props.get(j);
						String name = prop.attributeValue("name");
						if (name!=null&&name.equals("cronExpression")) {
							// prop.setAttribute("value", "test");
							return prop.attributeValue("value");
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}

	public static boolean backup(){
		String o = ContextHolder.getServletPath() + "/WEB-INF/applicationContext.xml";
		String backup = ContextHolder.getServletPath() + "/WEB-INF/applicationContext.xml_backup";
		return FileOperator.copy(o, backup);
	}
	public static boolean restore(){
		String o = ContextHolder.getServletPath() + "/WEB-INF/applicationContext.xml_backup";
		String resotre =ContextHolder.getServletPath() + "/WEB-INF/applicationContext.xml";
		return FileOperator.copy(o, resotre);
	}
	
	public String getFilePath() {
		return filePath;
	}

	public synchronized void setApplicationContext(ApplicationContext ac) {
		this.ac = ac;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public static AssetClassManager getAssetClassManager() {
		return (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
	}

	public static BaseFormulaManager getBaseFormulaManager() {
		return (BaseFormulaManager) ContextHolder.getInstance().getApplicationContext().getBean("baseFormulaManager");
	}


	public static DataManager getDataManager() {
		return (DataManager) ContextHolder.getInstance().getApplicationContext().getBean("dataManager");
	}

	public static GroupManager getGroupManager() {
		return (GroupManager) ContextHolder.getInstance().getApplicationContext().getBean("groupManager");
	}

	public static HolidayManager getHolidayManager() {
		return (HolidayManager) ContextHolder.getInstance().getApplicationContext().getBean("holidayManager");
	}

	public static IndicatorManager getIndicatorManager() {
		return (IndicatorManager) ContextHolder.getInstance().getApplicationContext().getBean("indicatorManager");
	}

	public static PortfolioManager getPortfolioManager() {
		return (PortfolioManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioManager");
	}
	public static CompanyIndexManager getCompanyIndexManager() {
		return (CompanyIndexManager) ContextHolder.getInstance().getApplicationContext().getBean("companyIndexManager");
	}
	public static PortfolioHoldingManager getPortfolioHoldingManager() {
		return (PortfolioHoldingManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioHoldingManager");
	}
	public static SecurityManager getSecurityManager() {
		return (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
	}


	public static StrategyClassManager getStrategyClassManager() {
		return (StrategyClassManager) ContextHolder.getInstance().getApplicationContext().getBean("strategyClassManager");
	}

	public static StrategyManager getStrategyManager() {
		return (StrategyManager) ContextHolder.getInstance().getApplicationContext().getBean("strategyManager");
	}

	public static UserManager getUserManager() {
		return (UserManager) ContextHolder.getInstance().getApplicationContext().getBean("userManager");
	}
	public static InviteManager getinvIteManager() {
		return (InviteManager) ContextHolder.getInstance().getApplicationContext().getBean("inviteManage");
	}
	public static MutualFundManager getMutualFundManager() {
		return (MutualFundManager) ContextHolder.getInstance().getApplicationContext().getBean("mutualFundManager");
	}
	
	public static FinancialStatementManager getFinancialStatementManager() {
		return (FinancialStatementManager) ContextHolder.getInstance().getApplicationContext().getBean("financialStatementManager");
	}
	
	public static JforumManager getJforumManager() {
		return (JforumManager) ContextHolder.getInstance().getApplicationContext().getBean("JforumManager");
	}
	
	public static ProfileManager getProfileManager() {
		return (ProfileManager) ContextHolder.getInstance().getApplicationContext().getBean("profileManager");
	}
	
	public static WidgetUserManager getWidgetUserManager() {
		return (WidgetUserManager) ContextHolder.getInstance().getApplicationContext().getBean("widgetUserManager");
	}
	
	/*public static FinancialStatement getFinancialStatement(){
		return (FinancialStatement) ContextHolder.getInstance().getApplicationContext().getBean("financialStatement");
	}*/

	public static boolean isLinux() {
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1) {
			return true;
		}
		return false;
	}
	public static DateFormat df=new SimpleDateFormat("yyyy_MM_dd_hh_MM_ss");
	public static String log_name="exception_log_"+df.format(new Date())+".txt";
	public synchronized static void addException(Throwable e) {
		StringBuffer sb=new StringBuffer();
		if(e instanceof SimulateException){
			SimulateException se=(SimulateException) e;
			sb.append("portfolio id: "+se.getPortfolioID());
			sb.append("\r\n");
			sb.append("portfolio name: ");
			sb.append(se.getPortfolioName());
			sb.append("\r\n\r\n");
		}
		sb.append(StringUtil.getStackTraceString(e));
		sb.append("\r\n\r\n\r\n\r\n");
		FileOperator.appendMethodA(log_name, sb.toString());
	}
}