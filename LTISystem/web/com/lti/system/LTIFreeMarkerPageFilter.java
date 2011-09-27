package com.lti.system;

import java.io.File;
import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.struts2.sitemesh.FreeMarkerPageFilter;

import com.opensymphony.module.sitemesh.Decorator;
import com.opensymphony.module.sitemesh.Page;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionEventListener;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.interceptor.PreResultListener;
import com.opensymphony.xwork2.util.OgnlValueStack;

public class LTIFreeMarkerPageFilter extends FreeMarkerPageFilter {
	private FilterConfig filterConfig;

	public void init(FilterConfig filterConfig) {
		super.init(filterConfig);
		this.filterConfig = filterConfig;
	}

	protected void applyDecorator(Page page, Decorator decorator, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		ServletContext servletContext = filterConfig.getServletContext();
		ActionContext ctx = ServletActionContext.getActionContext(req);
		boolean isHtml=false;
		String uri = req.getRequestURI();
		
		String path = uri.substring(uri.indexOf("LTISystem") + 9);
		File f = new File(ContextHolder.getServletPath(), path);
		
		if (uri.indexOf(".html") > 0) {
			
			if (!f.exists()) {
				boolean includeHeader = true;
				if (req.getParameter("includeHeader") != null) {
					includeHeader = Boolean.parseBoolean(req.getParameter("includeHeader"));
				}
				if (includeHeader) {
					res.sendRedirect(req.getContextPath() + "/jsp/news/nonews.html");
				} else {
					res.sendRedirect(req.getContextPath() + "/jsp/news/nonews.html?includeHeader=false");
				}
				return;
			}
			
			isHtml=true;
			res.setHeader("Cache-Control", "no-store");
			res.setDateHeader("Expires", 0);
			res.setHeader("Pragma", "no-cache");
		}
		if (ctx == null) {
			// ok, one isn't associated with the request, so let's get a
			// ThreadLocal one (which will create one if needed)
			OgnlValueStack vs = new OgnlValueStack();
			if(!isHtml)vs.getContext().putAll(Dispatcher.getInstance().createContextMap(req, res, null, servletContext));
			ctx = new ActionContext(vs.getContext());
			if (ctx.getActionInvocation() == null) {
				// put in a dummy ActionSupport so basic functionality still
				// works
				ActionSupport action = new ActionSupport();
				vs.push(action);
				ctx.setActionInvocation(new DummyActionInvocation(action));
			}
		}

		// delegate to the actual page decorator
		applyDecorator(page, decorator, req, res, servletContext, ctx);
	}

	static class DummyActionInvocation implements ActionInvocation {

		private static final long serialVersionUID = -4808072199157363028L;

		ActionSupport action;

		public DummyActionInvocation(ActionSupport action) {
			this.action = action;
		}

		public Object getAction() {
			return action;
		}

		public boolean isExecuted() {
			return false;
		}

		public ActionContext getInvocationContext() {
			return null;
		}

		public ActionProxy getProxy() {
			return null;
		}

		public Result getResult() throws Exception {
			return null;
		}

		public String getResultCode() {
			return null;
		}

		public void setResultCode(String resultCode) {
		}

		public OgnlValueStack getStack() {
			return null;
		}

		public void addPreResultListener(PreResultListener listener) {
		}

		public String invoke() throws Exception {
			return null;
		}

		public String invokeActionOnly() throws Exception {
			return null;
		}

		public void setActionEventListener(ActionEventListener listener) {
		}
	}
	
	
}
