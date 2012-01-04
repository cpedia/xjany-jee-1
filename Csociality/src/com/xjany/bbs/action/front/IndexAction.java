package com.xjany.bbs.action.front;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xjany.bbs.service.FileService;
import java.io.*;


@Controller
public class IndexAction {
	@Autowired
	private FileService fileService;

	@RequestMapping("/index.html")
	public String showIndex(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "index";
	}
}