package com.xjany.bbs.action.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

public class BaseAction {
	
	/**
	 * 通用的拦截器  所有以  *.do的 都返回到 *.html页面
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/*.do")
	public String genricDo(HttpServletRequest request, ModelMap model)
	throws Exception {
		String   path   =   (String)   request.getRequestURI();
		path = path.substring(path.indexOf("/xjanyadmin")+12).replaceAll("\\.do", "");
		return path;
	}
	
	/**
	 * 通用的拦截器  所有以  *.jhtml的 都返回到 *.html页面
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/*.jhtml")
	public String genricJhtml(HttpServletRequest request, ModelMap model)
	throws Exception {
		String   path   =   (String)   request.getRequestURI();
		path = path.substring(path.indexOf("/xjanyadmin")+12).replaceAll("\\.jhtml", "");
		return path;
	}
}
