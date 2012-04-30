package com.xjany.xijie.action;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xjany.bbs.service.FileService;


@Controller
@RequestMapping("/")
public class IndexAction {
	@Autowired
	private FileService fileService;		

	@RequestMapping("/index.do")
	public String showIndex(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "/index";
	}		
}
