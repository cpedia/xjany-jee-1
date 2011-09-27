package com.lti.customizepage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lti.action.Action;
import com.lti.compiler.LTIClassLoader;
import com.lti.service.bo.CustomizePage;
import com.lti.system.ContextHolder;
import com.sun.tools.javac.Main;

public class PageCompiler {

	public static String formateText(String source, String append) {
		String resultString = com.lti.executor.CompilerPreProcessor.str_replace(source, "\r", "");
		resultString = com.lti.executor.CompilerPreProcessor.str_replace(resultString, "\n", "\r\n" + append);
		return append + resultString;
	}

	public static String generateImportHead() {
		StringBuilder code = new StringBuilder();
		code.append("package com.lti.action.customizepage;\r\n\r\n");

		code.append("import com.lti.service.bo.*;\r\n");
		code.append("import java.util.*;\r\n");
		code.append("import com.lti.type.*;\r\n");
		code.append("import com.lti.util.*;\r\n\r\n");
		code.append("import java.io.*;\r\n");
		code.append("import com.lti.bean.*;\r\n");
		code.append("import com.lti.system.*;\r\n");
		code.append("import com.lti.service.*;\r\n");
		code.append("import com.lti.action.customizepage.CustomizePageAction;\r\n");
		code.append("import com.lti.customizepage.BasePage;\r\n");
		code.append("import com.lti.action.Action;\r\n");
		code.append("import freemarker.template.Template;\r\n\r\n");
		return code.toString();
	}

	public static void GenerateTemplate(String name, String path, String template) throws Exception {
		String TemplatePath = path + "/jsp/customizepage/";

		String filename = name + ".ftl";
		File file = new File(TemplatePath + filename);
		// if(!file.exists())file.createNewFile();
		PrintWriter pw = new PrintWriter(new FileOutputStream(file));
		pw.print(template);
		pw.flush();
		pw.close();
	}

	public static void DeleteTemplate(String fileName, String path) throws Exception {
		String TemplatePath = path + "/jsp/customizepage/";
		File sFile = new File(TemplatePath + fileName + ".ftl");
		sFile.delete();

	}

	public static String Complie(CustomizePage cp, String path) throws Exception {
		String name = cp.getName();
		String userCode = cp.getCode();
		String functions = cp.getFunctions();
		String ClassPath;
		if (ContextHolder.isLinux()) {
			ClassPath = path + "/WEB-INF/classes/:" + path + "/WEB-INF/lib/freemarker.jar:" + path + "/WEB-INF/lib/hibernate3.jar:" + path + "/WEB-INF/lib/ltisystem_base.jar:" + path + "/WEB-INF/lib/xwork-2.0.4.jar";
		} else {
			ClassPath = path + "/WEB-INF/classes/;" + path + "/WEB-INF/lib/freemarker.jar;" + path + "/WEB-INF/lib/hibernate3.jar;" + path + "/WEB-INF/lib/ltisystem_base.jar;" + path + "/WEB-INF/lib/xwork-2.0.4.jar";
		}

		String DestinationPath = path + "/WEB-INF/classes/";
		String SourcePath = path + "/WEB-INF/src/com/lti/action/customizepage/";
		String classFileName = name;

		StringBuilder code = new StringBuilder();

		code.append(PageCompiler.generateImportHead());

		// class begin
		code.append("public class " + classFileName + " extends CustomizePageAction{\r\n");
		// constructor
		code.append("\tpublic " + classFileName + "(){super();}\r\n\r\n");

		code.append("\tprivate static final long serialVersionUID = 2496394976004782444L;\r\n");
		if (functions != null)
			code.append(functions);

		// process function
		code.append("\t@SuppressWarnings(\"unchecked\")\r\n");
		code.append("\tpublic String process() throws Exception {\r\n");
		code.append("\r\n");

		code.append("Template t = BasePage.CustomizePageConf.getTemplate(\"" + name + ".ftl\");");
		code.append("ByteArrayOutputStream buf = new ByteArrayOutputStream();");
		code.append("Writer out = new OutputStreamWriter(buf, \"utf8\");");

		code.append("\t\texecuteUserCode();\r\n");

		code.append("\t\t" + "t.process(root, out);");
		code.append("\t\t" + "return (buf.toString());");
		code.append("\r\n\t}");
		code.append("\r\n");
		
		// usercode function
		code.append("\t@SuppressWarnings(\"unchecked\")\r\n");
		code.append("\tpublic void executeUserCode() throws Exception {\r\n");
		code.append("\r\n");


		code.append("\t\t" + userCode + "\r\n");

		code.append("\r\n\t}");
		code.append("\r\n");		

		// execute function
		code.append("\t@SuppressWarnings(\"unchecked\")\r\n");
		code.append("\tpublic String execute() throws Exception {\r\n");
		code.append("\r\n");
		code.append("\t\tinit();\r\n");
		code.append("\t\t" + userCode + "\r\n");

		code.append("\t\t" + "return Action.SUCCESS;");
		code.append("\r\n\t}");
		code.append("\r\n");

		// class end
		code.append("\r\n}\r\n");// close the class;

		String filename = classFileName + ".java";
		File file = new File(SourcePath + filename);
		// if(!file.exists())file.createNewFile();
		PrintWriter pw = new PrintWriter(new FileOutputStream(file));
		pw.print(code.toString());
		pw.flush();
		pw.close();

		String[] arg = new String[] { "-classpath", ClassPath, "-d", DestinationPath, SourcePath + filename.replace(' ', '_').replace('\'', '_') };
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintWriter out = new PrintWriter(baos);
		Main.compile(arg, out);
		out.flush();

		if (!file.exists() || file.isDirectory())
			return "File does not exist!";
		ByteArrayInputStream fis = new ByteArrayInputStream(baos.toByteArray());
		byte[] buf = new byte[1024];
		StringBuffer sb = new StringBuffer();
		while ((fis.read(buf)) != -1) {
			sb.append(new String(buf));
			buf = new byte[1024];
		}
		if (!sb.toString().equals("")) {
			System.out.println(sb.toString());
			System.out.println("ClassPath: " + ClassPath);
			// Delete(classFileName,path);
			// throw new Exception("Failed to Compile!");
		}
		return sb.toString();

	}

	public static void Delete(String fileName, String path) {
		String ClassPath = path + "/WEB-INF/classes/";
		String SourcePath = path + "/WEB-INF/src/com/lti/action/customizepage/";
		File sFile = new File(SourcePath + fileName.replace(' ', '_').replace('\'', '_') + ".java");
		sFile.delete();
		File bFile = new File(ClassPath + fileName.replace(' ', '_').replace('\'', '_') + ".class");
		bFile.delete();

	}

	private static Map<String, Object[]> cachedClasses = new HashMap<String, Object[]>();

	private static Class findClass(String fileName) throws Exception {
		File f = new File(ContextHolder.getServletPath() + "/WEB-INF/classes/com/lti/action/customizepage/" + fileName + ".class");
		long lastmodified = f.lastModified();
		Object[] pair = cachedClasses.get(fileName);
		if (pair != null) {
			long timestamp = (Long) pair[0];
			if (lastmodified == timestamp)
				return (Class) pair[1];
		}
		LTIClassLoader classLoader = new LTIClassLoader();
		Class cls = classLoader.loadUserClass(ContextHolder.getServletPath() + "/WEB-INF/classes/com/lti/action/customizepage/" + fileName + ".class", "com.lti.action.customizepage." + fileName, true);
		cachedClasses.put(fileName, new Object[]{lastmodified,cls});
		return cls;
	}

	@SuppressWarnings("unchecked")
	public static BasePage getPage(String fileName) throws Exception {
		Class cls = findClass(fileName);
		Constructor constr = cls.getConstructor(new Class[] {});
		Object obj = constr.newInstance(new Object[] {});
		return (BasePage) obj;
	}
	
	public static void main(String[] args){
		for(int i=0;i<1000000;i++){
			if(i%10000==0)System.out.println(i);
			try {
				PageCompiler.getPage("AboutUs");
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	}

}
