package com.xjany.bbs.action.front;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.xjany.bbs.entity.File;
import com.xjany.bbs.service.FileService;
import java.io.*;


@Controller
@RequestMapping("/file.do")
public class FileAction {
	@Autowired
	private FileService fileService;

	@RequestMapping(params = "method=add")
	public String addFile(HttpServletRequest request, ModelMap model, File file)
			throws Exception {
		int upId = 0;
		if (request.getParameter("upId") != null
				&& !"".equals(request.getParameter("upId")))
			upId = Integer.parseInt(request.getParameter("upId"));
		fileService.addFile(file, upId);
		List<File> list = fileService.findByUpId(upId);
		model.addAttribute("upId2", upId);// request保存这个对象
		model.addAttribute("id", upId);// request保存这个对象
		model.addAttribute("filelist", list);// request保存这个对象
		return "file";
	}

	@RequestMapping(params = "method=list")
	public String getList(HttpServletRequest request, ModelMap model, File file)
			throws Exception {
		int upId = 0;
		int id = 0;
		if (request.getParameter("upId") != null)
			upId = Integer.parseInt(request.getParameter("upId"));
		List<File> list = fileService.findByUpId(upId);
		try {
			id = fileService.findById(upId).getUpId(); // 上级的UPID
		} catch (Exception e) {
			// TODO: handle exception
		}
		model.addAttribute("upId2", upId);// request保存这个对象
		model.addAttribute("id", id);// request保存这个对象
		model.addAttribute("filelist", list);// request保存这个对象
		return "file";
	}

	/**
	 * 上传文件
	 * 
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST, params = "method=upload")
	public String upload(HttpServletRequest request, ModelMap model,
			File fileUpload) {

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		/** 构建图片保存的目录 **/
		String logoPathDir = "/uploadFiles";
		/** 得到图片保存目录的真实路径 **/
		String logoRealPathDir = request.getSession().getServletContext()
				.getRealPath(logoPathDir);
		/** 根据真实路径创建目录 **/
		java.io.File logoSaveFile = new java.io.File(logoRealPathDir);
		if (!logoSaveFile.exists())
			logoSaveFile.mkdirs();
		/** 页面控件的文件流 **/
		MultipartFile multipartFile = multipartRequest.getFile("file");

		// /** 获取文件的后缀 **/
		// String suffix =
		// multipartFile.getOriginalFilename().substring(multipartFile.getOriginalFilename().lastIndexOf("."));
		// /**使用UUID生成文件名称**/
		// String logImageName = UUID.randomUUID().toString()+ suffix;//构建文件名称

		String logImageName = "";
		try {
			logImageName = new String(multipartFile.getOriginalFilename()
					.getBytes("ISO8859-1"), "UTF-8"); // 处理字符问题
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		/** 拼成完整的文件保存路径加文件 **/
		String fileName = logoRealPathDir + java.io.File.separator
				+ logImageName;
		java.io.File file = new java.io.File(fileName);

		try {
			multipartFile.transferTo(file);
			int upId = 0;
			if (request.getParameter("upId") != null)
				upId = Integer.parseInt(request.getParameter("upId"));
			fileUpload.setName(logImageName);
			fileService.addFile(fileUpload, upId);
			List<File> list = fileService.findByUpId(upId);
			model.addAttribute("filelist", list);// request保存这个对象
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		model.put("fileName", fileName);
		return "file";

	}
}
