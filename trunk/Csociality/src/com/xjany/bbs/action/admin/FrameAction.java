package com.xjany.bbs.action.admin;


import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xjany.bbs.service.FileService;
import java.io.*;


@Controller
public class FrameAction {
	@Autowired
	private FileService fileService;

	@RequestMapping("/index.jhtml")
	public String showIndex(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "index";
	}
}
