package com.lti.executor.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.TimeZone;

import javax.swing.JOptionPane;

import org.apache.commons.configuration.PropertiesConfiguration;

import com.lti.system.ContextHolder;

import freemarker.template.Template;

public class LocalExecStart {


	public static void init() throws Exception{
		com.lti.executor.Compiler.setFormatSource(false);
		//com.lti.executor.Executor.setReCompile(true);
		String timezoneid = ContextHolder.getHolidayManager().getTimeZoneID();
		//String timezoneid = TimeZone.getDefault().getID();
		TimeZone.setDefault(TimeZone.getTimeZone(timezoneid));
		String ip = JOptionPane.showInputDialog("Please input the server IP");
		resetIP(ip);
	}
	
	public static void resetIP(String ip)throws Exception{
		freemarker.template.Configuration conf=new freemarker.template.Configuration();
		conf.setDirectoryForTemplateLoading(new File(ContextHolder.getServletPath()+"/"+com.lti.system.Configuration.get("CLASS_ROOT_PATH")));
		Template t = conf.getTemplate("applicationContext.ftl");
		FileOutputStream buf = new FileOutputStream(new File(ContextHolder.getServletPath()+"/"+com.lti.system.Configuration.get("CLASS_ROOT_PATH")+"/applicationContext.xml"));
		Writer out = new OutputStreamWriter(buf, "utf8");
		HashMap<String,String> map=new HashMap<String,String>();
		map.put("ip", ip);
		t.process(map, out);
		out.flush();
		out.close();
		
		PropertiesConfiguration config = new PropertiesConfiguration(ContextHolder.getServletPath()+"/"+com.lti.system.Configuration.get("CLASS_ROOT_PATH")+"/system-config.properties");
		config.save();
	}

	/**
	 * Launch the application
	 * 
	 * @param args
	 */
	public static void main(final String[] args) {
		int port = 8081;
		if (args.length >= 1)
			port = Integer.parseInt(args[0]);

		ServerSocket s = null;
		try {
			s = new ServerSocket(port);
		} catch (Exception ex) {
			System.exit(0);
		}
		try {
			s.close();
			init();
		} catch (Exception ex) {
			System.out.println("error");
		}

		new Thread(){
			public void run(){
				try {
					Start.main(args);
				} catch (final Exception e) {
				}
			}
		}.start();
		
		LocalExecStart les=new LocalExecStart();
	}

	public static boolean isCreatedContents = false;


}
