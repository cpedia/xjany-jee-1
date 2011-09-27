package com.lti.executor.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.lang.reflect.Constructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

import com.lti.compiler.LTIClassLoader;

import freemarker.template.Configuration;
import freemarker.template.SimpleHash;
import freemarker.template.Template;

public abstract class BasePage {

	public static Configuration CustomizePageConf = new Configuration();
	protected static boolean isInitialized=false;
	protected SimpleHash root;
	
	protected String target;
	protected Request request;
	protected HttpServletRequest httpServletRequest;
	protected HttpServletResponse response;

	public void init() throws Exception {
		if(!isInitialized){
		CustomizePageConf.setDirectoryForTemplateLoading(new File(BasePage.class.getResource("action").getFile().replace("%20", " ")));
			//CustomizePageConf.setDirectoryForTemplateLoading(new File(BasePage.class.getResource("action").getFile()));
			//CustomizePageConf.setDirectoryForTemplateLoading(new File(new File(".").getCanonicalFile()+ "\\WEB-INF\\classes\\com\\lti\\executor\\web\\action"));
			isInitialized=true;
		}
		root = new SimpleHash();
	}

	public abstract String execute()throws Exception;
	
	public String process() throws Exception{
		Template t = BasePage.CustomizePageConf.getTemplate(execute());
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(buf, "utf8");
		root.put("output", this);
		t.process(root, out);
		return (buf.toString());
	}

	public String getOutput() throws Exception {
		init();
		return process();
	}
	
	public SimpleHash getRoot() {
		return root;
	}

	public void setRoot(SimpleHash root) {
		this.root = root;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}

	public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
		this.httpServletRequest = httpServletRequest;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	@SuppressWarnings("unchecked")
	public static BasePage getPage(String fileName) throws Exception {
		LTIClassLoader classLoader = new LTIClassLoader();
		//String jarURL = new File(".").getCanonicalFile()+"\\WEB-INF\\lib\\ltisystem_executorengine.jar";
		//String fullFileName = "com.lti.executor.web.action." + fileName;
		
		File classRoot=new File(BasePage.class.getResource("action").getFile(),fileName+".class");
		//System.out.print(classRoot.getAbsolutePath());
		Class cls =classLoader.loadUserClass(classRoot.getAbsolutePath(), "com.lti.executor.web.action."+ fileName, true);
		//System.out.println(jarURL);
		//System.out.println(fullFileName);
		//Class cls = classLoader.loadUserClassFromJAR(fullFileName, jarURL);
		
		//Class cls = Class.forName(fullFileName);
		Constructor constr = cls.getConstructor(new Class[] {});
		Object obj = constr.newInstance(new Object[] {});
		return (BasePage) obj;
	}	

//	public static void main(String[] args){
//		System.out.println(BasePage.class.getResource("action").getFile());
//	}

}
