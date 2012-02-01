package com.xjany.common.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class GrenricUtil {
	/**
	 * 
	 * @param objs 参数
	 * @param greneriPath 生成文件路径
	 * @param templetPath 模板路径
	 * @param inFileName 模板文件名
	 * @param outFileName 生成文件的文件名
	 * @return
	 */
	public static void _get_code(HttpServletRequest request,Map<String,Object> objs,String greneriPath,String templetPath,String inFileName,String outFileName){
		StringWriter sw = null;
		try {
			sw=new StringWriter();
			Configuration conf=new Configuration();
			conf.setServletContextForTemplateLoading(request.getSession().getServletContext(),templetPath);
//			conf.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir"),templetPath));
			conf.setTagSyntax(0);
			conf.setDefaultEncoding("utf-8");
			Template t = conf.getTemplate(inFileName);
			t.process(objs, sw);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		
		File file = new File(GrenricUtil.class.getClassLoader().getResource("")+greneriPath+outFileName);
		if (!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] b = new byte[1024];
			b = sw.toString().getBytes();
			try {
					fos.write(b);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
