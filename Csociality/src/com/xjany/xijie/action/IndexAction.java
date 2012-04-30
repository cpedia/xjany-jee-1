package com.xjany.xijie.action;


import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class IndexAction {

	@RequestMapping("/index.jhtml")
	public String showIndex(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "index";
	}
}
