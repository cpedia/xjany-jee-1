package com.xjany.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xjany.entity.District;
import com.xjany.entity.File;
import com.xjany.service.FileService;

@Controller
public class FileAction {
	@Autowired
	private FileService fileService;

	@RequestMapping("fileAdd.do")
	public String addFile(HttpServletRequest request, ModelMap model, File file)
			throws Exception {
		int upId = 0;
		if (request.getParameter("upId") != null)
			upId = Integer.parseInt(request.getParameter("upId"));
		fileService.addFile(file, upId);
		List<File> list = fileService.findByUpId(upId);
		model.addAttribute("filelist", list);// request保存这个对象
		return "file";
	}

	@RequestMapping("file.do")
	public String getList(HttpServletRequest request, ModelMap model, File file)
			throws Exception {
		int upId = 0;
		if (request.getParameter("upId") != null)
			upId = Integer.parseInt(request.getParameter("upId"));
		List<File> list = fileService.findByUpId(upId);
		model.addAttribute("filelist", list);// request保存这个对象
		return "file";
	}
}
