package com.lti.system;

import java.io.File;
import java.io.IOException;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lti.jobscheduler.Scheduler;
import com.lti.start.CompiledAllPages;
import com.lti.util.ProcessUtil;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author koko
 */
public class MyFilterDispatcher extends org.apache.struts2.dispatcher.FilterDispatcher {

	private static final long serialVersionUID = 1L;

	public MyFilterDispatcher() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		String uri = req.getRequestURI();

		String path = uri.substring(uri.indexOf("LTISystem") + 9);
		File f = new File(ContextHolder.getServletPath(), path);

		if (uri.indexOf(".html") > 0) {

			if (!f.exists()) {
				boolean includeHeader = true;
				if (req.getParameter("includeHeader") != null) {
					includeHeader = Boolean.parseBoolean(req.getParameter("includeHeader"));
				}
				res.setHeader("Cache-Control", "no-store");
				res.setDateHeader("Expires", 0);
				res.setHeader("Pragma", "no-cache");
				if (includeHeader) {
					res.sendRedirect(req.getContextPath() + "/jsp/news/nonews.html");
				} else {
					res.sendRedirect(req.getContextPath() + "/jsp/news/nonews.html?includeHeader=false");
				}
				return;
			}

			
		}
		request.setCharacterEncoding("utf8");
		HttpServletRequest q1 = (HttpServletRequest) request;
		long t1=System.currentTimeMillis();
		super.doFilter(request, response, filterChain);
		long t2=System.currentTimeMillis();
//		System.out.println((t2-t1)/1000+": "+q1.getRequestURL());
		q1.getContentType();
		// filterChain.doFilter(request, response);

	}

	public void init(FilterConfig filterConfig) throws ServletException {

		super.init(filterConfig);

		ServletContext sc = this.getServletContext();

		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(sc);
		ContextHolder.getInstance().setApplicationContext(ctx);

		ContextHolder.setServletPath(this.getServletContext().getRealPath("/"));
		if (!ProcessUtil.isRun()) {
			// ProcessUtil.start();
		}
		Scheduler.schedule();
		// try{
		// CompiledAllPages.compile();
		// }catch(Exception e){
		// e.printStackTrace();
		// }

	}

}
