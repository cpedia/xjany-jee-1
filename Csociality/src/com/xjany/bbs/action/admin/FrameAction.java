package com.xjany.bbs.action.admin;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xjany.bbs.service.FileService;
import com.xjany.bbs.service.UserService;
import com.xjany.common.page.Pagination;
import com.xjany.common.page.SimplePage;
import com.xjany.common.util.CookieUtils;

import java.util.Properties;


@Controller
public class FrameAction extends BaseAction{
	
	@Autowired
	private UserService userService;

	@RequestMapping("/right.do")
	public String right(HttpServletRequest request, ModelMap model) {
		Properties props = System.getProperties();
		Runtime runtime = Runtime.getRuntime();
		long freeMemoery = runtime.freeMemory();
		long totalMemory = runtime.totalMemory();
		long usedMemory = totalMemory - freeMemoery;
		long maxMemory = runtime.maxMemory();
		long useableMemory = maxMemory - totalMemory + freeMemoery;
		model.addAttribute("props", props);
		model.addAttribute("freeMemoery", freeMemoery);
		model.addAttribute("totalMemory", totalMemory);
		model.addAttribute("usedMemory", usedMemory);
		model.addAttribute("maxMemory", maxMemory);
		model.addAttribute("useableMemory", useableMemory);
		return "right";
	}
	
	@RequestMapping("/frame/user_main.do")
	public String user_main(HttpServletRequest request, ModelMap model) {
		return "/frame/user_main";
	}
	
	@RequestMapping("/frame/user_left.do")
	public String user_left(HttpServletRequest request, ModelMap model) {
		return "/frame/user_left";
	}
	
	@RequestMapping("/frame/user_right.do")
	public String user_right(HttpServletRequest request, ModelMap model) {
		return "/frame/user_right";
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
		//path = path.replaceAll("\\/xjanyadmin\\/", "").replaceAll("\\.jhtml", "");
		path = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
		return path;
	}
	
	/**
	 * 通用的拦截器 
	 * @param request
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/*.*")
	public String genricSuffix(HttpServletRequest request, ModelMap model)
			throws Exception {
		String path = (String)request.getRequestURI();
		path = path.substring(path.lastIndexOf("/") + 1, path.lastIndexOf("."));
//		if(path.endsWith(".jhtml"))
//			path = path.replaceAll("\\/xjanyadmin\\/", "").replaceAll("\\.jhtml", "");
//		else if(path.endsWith(".do"))
//			path = path.replaceAll("\\/xjanyadmin\\/", "").replaceAll("\\.do", "");
		return path;
	}
}
