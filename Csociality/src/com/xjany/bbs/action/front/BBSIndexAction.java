package com.xjany.bbs.action.front;


import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("bbs/front")
public class BBSIndexAction {

	@RequestMapping("/index.do")
	public String showIndex(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "/index";
	}
}
