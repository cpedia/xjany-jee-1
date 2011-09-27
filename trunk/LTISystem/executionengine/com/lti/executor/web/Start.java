package com.lti.executor.web;

import java.util.TimeZone;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import com.lti.system.ContextHolder;

public class Start {


	
	public static void main(String[] args) throws Exception{
		int port = 8081;
		if (args.length >= 1)
			port = Integer.parseInt(args[0]);
		Server server = new Server(port);
		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setWelcomeFiles(new String[] { "index.html" });
		//resource_handler.setResourceBase(new File(".").getCanonicalFile()+"\\WEB-INF\\classes\\com\\lti\\executor\\web\\html");
		resource_handler.setResourceBase(Start.class.getResource("html").getFile());
		String timezoneid = ContextHolder.getHolidayManager().getTimeZoneID();
		//String timezoneid = TimeZone.getDefault().getID();
		TimeZone.setDefault(TimeZone.getTimeZone(timezoneid));
		//LTIExecutionEngineHandler executionEngineHandler=new LTIExecutionEngineHandler();
		
		FreeMarkerHandler freeMarkerHandler=new FreeMarkerHandler();
		
		System.out.println("Start Execution Engine!!!!!!!!");
		
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] {freeMarkerHandler, resource_handler, new DefaultHandler()});
		server.setHandler(handlers);
		server.start();
		server.join();

	}

}
