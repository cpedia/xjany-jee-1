package com.lti.SimpleExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import com.sun.tools.javac.Main;

public class SimpleExecutor {

	public static String formateText(String source,String append){
		String resultString=com.lti.executor.CompilerPreProcessor.str_replace(source, "\r", "");
		resultString=com.lti.executor.CompilerPreProcessor.str_replace(resultString, "\n", "\r\n"+append);
		return append+resultString;
	}

	public static String generateImportHead(){
		StringBuilder code = new StringBuilder();
		code.append("package com.lti.SimpleExecutor.code;\r\n\r\n");
		
		code.append("import com.lti.service.bo.*;\r\n");
		code.append("import java.util.*;\r\n");
		code.append("import com.lti.type.*;\r\n");
		code.append("import com.lti.util.*;\r\n\r\n");
		return code.toString();
	}
	
	public static synchronized String Complie(String path,String userCode) throws Exception {
		String ClassPath=path+"/WEB-INF/classes";
		String SourcePath=path+"/WEB-INF/src/com/lti/SimpleExecutor/code/";
		String classFileName="code"+(new Date()).getTime();
		
		StringBuilder code = new StringBuilder();
		
		code.append(SimpleExecutor.generateImportHead());
		
		//class begin
		code.append("public class " + classFileName + " extends com.lti.SimpleExecutor.BaseCode{\r\n");
		//constructor
		code.append("\tpublic " + classFileName + "(){super();}\r\n\r\n");
				
		//execute function 
		code.append("\tpublic void execute() throws Exception{\r\n");
		code.append("\r\n");
		
		code.append("\t\ttry{\r\n");
		code.append("\t\t\t"+userCode+"\r\n");
		
		code.append("\r\n\t\t}catch(Exception e){");
		code.append("\r\n\t\t\tprint(getStackTraceString(e));");
		code.append("\r\n\t\t\tthrow e;");
		code.append("\r\n\t\t}");
		
		code.append("\r\n\t}");
		code.append("\r\n");
		
				
		//class end
		code.append("\r\n}\r\n");// close the class;
		
		String filename=classFileName+".java";
		File file = new File(SourcePath+filename);
		//if(!file.exists())file.createNewFile();
		PrintWriter pw = new PrintWriter(new FileOutputStream(file));
		pw.print(code.toString());
		pw.flush();
		pw.close();

		String tmpFileName=classFileName+".output";
		String[] arg = new String[] { "-classpath", ClassPath,"-d", ClassPath, SourcePath+filename.replace(' ', '_').replace('\'', '_') };
		PrintWriter out=new PrintWriter(new File(tmpFileName));
		Main.compile(arg,out);
		out.flush();
		
		
		File outputFile=new File(tmpFileName);
		if(!file.exists()||file.isDirectory())
		    return "Can not find the message output file!";
		FileInputStream fis=new FileInputStream(outputFile);
		byte[] buf = new byte[1024];
		StringBuffer sb=new StringBuffer();
		while((fis.read(buf))!=-1){
		    sb.append(new String(buf));    
		    buf=new byte[1024];
		}
		outputFile.delete();
		if(!sb.toString().equals("")){
			Delete(classFileName,path);
			throw new Exception("Failed to Compile!");
		}
		return classFileName;
		
	}
	public static void Delete(String fileName,String path) {
		String ClassPath=path+"/WEB-INF/classes/";
		String SourcePath=path+"/WEB-INF/src/com/lti/SimpleExecutor/code/";
		File sFile = new File(SourcePath+fileName.replace(' ', '_').replace('\'', '_') + ".java");
		sFile.delete();
		File bFile = new File(ClassPath+fileName.replace(' ', '_').replace('\'', '_') + ".class");
		bFile.delete();

	}
	
	@SuppressWarnings("unchecked")
	public static List<String> execute(String fileName) throws Exception {
		Class cls = Class.forName("com.lti.SimpleExecutor.code."+ fileName);
		Constructor constr = cls.getConstructor(new Class[] {});
		Object obj = constr.newInstance(new Object[] {});

		Method mth = cls.getMethod("execute");
		mth.invoke(obj);
		
		Method getM = cls.getMethod("getMessages");
		return (List<String>) getM.invoke(obj);

	}	
	
}
