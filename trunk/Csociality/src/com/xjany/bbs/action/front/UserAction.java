package com.xjany.bbs.action.front;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xjany.bbs.service.FileService;
@Controller
public class UserAction {
	@Autowired
	private FileService fileService;

	@RequestMapping("/login.jhtml")
	public String showIndex(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "login";
	}
}
