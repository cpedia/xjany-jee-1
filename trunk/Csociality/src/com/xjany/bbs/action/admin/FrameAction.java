package com.xjany.bbs.action.admin;



import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Properties;


@Controller
public class FrameAction extends BaseAction{
	
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
	
}
