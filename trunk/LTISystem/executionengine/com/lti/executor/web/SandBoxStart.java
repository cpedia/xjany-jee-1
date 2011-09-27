/**
 * 
 */
package com.lti.executor.web;

import java.io.File;
import java.util.Date;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;

import com.lti.cache.YearCache;
import com.lti.service.impl.SecurityManagerImplWithYearCache;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
/**
 * @author Administrator
 *
 */
public class SandBoxStart {
	public static void main(String[] args) throws Exception{
		int port = 8081;
		if (args.length >= 1)
			port = Integer.parseInt(args[0]);
		Server server = new Server(port);

		ResourceHandler resource_handler = new ResourceHandler();
		resource_handler.setWelcomeFiles(new String[] { "index.html" });
		resource_handler.setResourceBase(Start.class.getResource("html").getFile());
		System.out.println(new File(".").getAbsolutePath());

		//LTIExecutionEngineHandler executionEngineHandler=new LTIExecutionEngineHandler();
		
		FreeMarkerHandler freeMarkerHandler=new FreeMarkerHandler();
		
		SecurityManagerImplWithYearCache securityManager = (SecurityManagerImplWithYearCache)ContextHolder.getSecurityManager();
		securityManager.setSandBox(true);
		securityManager.setSandBoxEndDate(LTIDate.getNewNYSETradingDay(new Date(), 4));
		YearCache.getInstance().setSandBox(true);
		YearCache.getInstance().setSandBoxEndDate(LTIDate.getNewNYSETradingDay(new Date(), 4));
		
		System.out.println("Start Execution Engine!!!!!!!!");
		
		HandlerList handlers = new HandlerList();
		handlers.setHandlers(new Handler[] {freeMarkerHandler, resource_handler, new DefaultHandler()});
		server.setHandler(handlers);
		server.start();
		server.join();

	}
}
