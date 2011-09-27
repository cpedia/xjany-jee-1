package com.lti.executor.web;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import com.lti.util.StringUtil;

public class FreeMarkerHandler extends AbstractHandler {

	public void handle(String target, Request request, HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException, ServletException {
		response.setHeader("Cache-Control","no-store");
		response.setDateHeader("Expires", 0);
		response.setHeader("Pragma","no-cache"); 
		String action = request.getRequestURI().substring(request.getRequestURI().lastIndexOf("/") + 1);
		if (!action.contains(".")) {
			BasePage bp = null;
			try {
				bp = BasePage.getPage(action.substring(0, 1).toUpperCase() + action.substring(1, action.length()));
				bp.setTarget(target);
				bp.setRequest(request);
				bp.setResponse(response);
				bp.setHttpServletRequest(httpServletRequest);
				request.setHandled(true);
			} catch (Exception e) {
				return;
			}
			String out;
			try {
				out = bp.getOutput();
			} catch (Exception e) {
				out = "<pre>" + StringUtil.getStackTraceString(e) + "</pre>";
			}
			
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println(out);
			
		}

	}

}
