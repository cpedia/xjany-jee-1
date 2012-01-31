package com.xjany.bbs.action.admin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xjany.bbs.service.GroupService;
import com.xjany.common.page.Pagination;
import com.xjany.common.page.SimplePage;
import com.xjany.common.util.CookieUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Controller
public class TempleteAction {
	public String _get_code(Map<String,Object> objs,String filename){
		StringWriter sw = null;
		try {
			sw=new StringWriter();
			Configuration conf=new Configuration();
			conf.setDirectoryForTemplateLoading(new File(System.getProperty("user.dir"),"WebRoot/include/"));
			Template t = conf.getTemplate(filename);
			t.process(objs, sw);
			//StringWriter jsw=new StringWriter();
			//JSONBuilder js=new JSONBuilder(jsw);
			//js.object().key("html").value(sw.toString()).endObject();
		} catch (IOException e) {
			//e.printStackTrace();
			//e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		
		File file = new File(System.getProperty("user.dir"),"WebRoot/common/templete/test.txt");
		if (!file.exists())
		{
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public static void main(String[] args){
		Map<String,Object> objs=new HashMap<String, Object>();
		objs.put("serverName", "");
		new TempleteAction()._get_code(objs, "pony.js");
	}
}
