package com.lti.compiler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import com.lti.system.ContextHolder;
import com.lti.util.FileOperator;
import com.lti.util.StringUtil;
import com.sun.tools.javac.Main;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class LTICompiler {

	private Configuration conf;
	private String templateDirectory;
	private String templateName;
	@SuppressWarnings("unchecked")
	private Map codemap;
	
	private String classpath;
	private String srcpath;
	private String destpath;
	private String srcname;
	private String destname;
	
	@SuppressWarnings("unused")
	private String name;
	private boolean isFormatSource=true;
	
	@SuppressWarnings("unchecked")
	public String complile(String name,String templatename,Map codemap)throws Exception{
		this.setName(name);
		this.codemap=codemap;
		this.templateName=templatename;
		try {
			this.clean(name);
		} catch (Exception e) {
			System.out.println(StringUtil.getStackTraceString(e));
			//e.printStackTrace();
		}
		this.generateSourceCode();
		return this.generateClassCode();
		
	}
	
	public LTICompiler(String templateDirectory, String[] classpath, String srcpath, String destpath) {
		super();
		init(templateDirectory,  classpath, srcpath, destpath);
	}
	
	public void init(String templateDirectory, String[] classpath, String srcpath, String destpath) {
		this.templateDirectory = templateDirectory;
		this.classpath = getClassPath(classpath);
		this.srcpath = srcpath;
		this.destpath = destpath;
		conf = new Configuration();
	}

	public void setName(String name){
		this.name=name;
		this.srcname=name+".java";
		this.destname=name+".class";
	}
	
	public String getClassPath(String[] classpath){
		String seperator=":";
		if(!ContextHolder.isLinux()){
			seperator=";";
		}
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<classpath.length;i++){
			sb.append(classpath[i]);
			if(i!=classpath.length-1)sb.append(seperator);
		}
		return sb.toString();
	}
	
	public Template getTemplate() throws Exception {
		conf.setDirectoryForTemplateLoading(new File(templateDirectory));
		Template t = conf.getTemplate(templateName);
		return t;

	}


	@SuppressWarnings("unchecked")
	public void generateSourceCode() throws Exception{
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		Writer out = new OutputStreamWriter(buf, "utf8");
		getTemplate().process(codemap, out);
		File file = new File(StringUtil.getPath(new String[]{srcpath,srcname}));
		PrintWriter pw = new PrintWriter(new FileOutputStream(file));
		pw.print(buf.toString());
		pw.flush();
		pw.close();
		//if(isFormatSource)JavaFormatter.format(file);
	}

	public synchronized static void compile(String args[],PrintWriter out){
		Main.compile(args, out);
	}
	
	public String generateClassCode()throws Exception{
		String[] arg = new String[] { "-classpath", classpath, "-d", destpath, StringUtil.getPath(new String[]{srcpath,srcname})
				//,"-Xlint","deprecation","-Xlint","unchecked"
		
		};
		Writer sw=new StringWriter();
		PrintWriter out = new PrintWriter(sw);
		LTICompiler.compile(arg, out);
		//Main.compile(arg, out);
		out.flush();
		String[] outputs=sw.toString().split("\n");
		if(outputs!=null&&outputs.length>=1){
			StringBuffer lines=new StringBuffer();
			for (int i = 0; i < outputs.length; i++) {
				lines.append("\n").append("//").append(outputs[i]);
			}
			FileOperator.appendMethodA(StringUtil.getPath(new String[]{srcpath,srcname}), lines.toString());
		}
		String string=sw.toString();
		//if(string!=null&&string.length()>3)System.out.println(string);
		return string;
	}
	

	public void clean(String name) {
		this.setName(name);
		File sFile = new File(StringUtil.getPath(new String[]{destpath,"/com/lti/compiledstrategy",destname}));
		sFile.delete();
		File bFile = new File(StringUtil.getPath(new String[]{srcpath,srcname}));
		bFile.delete();

	}

	public boolean isFormatSource() {
		return isFormatSource;
	}

	public void setFormatSource(boolean isFormatSource) {
		this.isFormatSource = isFormatSource;
	}
}
