package com.xjany.action.admin;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xjany.bbs.entity.AllResLibCTG;
import com.xjany.bbs.entity.AllResLibrary;
import com.xjany.bbs.service.ResourceService;

@Controller
public class ResourceAction {
	@Autowired
	private ResourceService resourceService;


	@RequestMapping("/resource/v_list.do")
	public String v_list(HttpServletRequest request, ModelMap model, AllResLibrary allResLibrary)
			throws Exception {
		int parentId = 0;
		int id = 0;
		if (request.getParameter("parentId") != null)
			parentId = Integer.parseInt(request.getParameter("parentId"));
		List<AllResLibrary> list = resourceService.findByParentId(parentId);
		try {
			id = resourceService.findById(parentId).getParentId(); // 上级的UPID
		} catch (Exception e) {
		}
		model.addAttribute("parentId2", parentId);// request保存这个对象
		model.addAttribute("id", id);// request保存这个对象
		model.addAttribute("list", list);// request保存这个对象
		return "/resource/list";
	}
	
	@RequestMapping("/resource/o_add.do")
	public String o_add(HttpServletRequest request, ModelMap model, AllResLibrary allResLibrary)
			throws Exception {
		int parentId = 0;
		if (request.getParameter("parentId") != null
				&& !"".equals(request.getParameter("parentId")))
			parentId = Integer.parseInt(request.getParameter("parentId"));
		resourceService.save(allResLibrary, parentId);
		return "redirect:v_list.do";
	}
	
	@RequestMapping("/resource/o_delete.do")
	public String o_delete(HttpServletRequest request, ModelMap model, Integer id) throws Exception {
		resourceService.deleteById(id);
		return "redirect:v_list.do";
	}
	
//	@RequestMapping("/resource/o_generic.do")
//	public String o_generic(HttpServletRequest request, ModelMap model, Integer id) throws Exception {
//		List<AllResLibrary> list = resourceService.findByParentId(0);  //所有根(职业大类)
//		for(int i=0;i<list.size();i++){
//			list.get(i).setAllResLibrary(new HashSet<AllResLibrary>(resourceService.findByParentId(list.get(i).getLibId())));
//		}
//		
//		Map<String,Object> objs=new HashMap<String, Object>();
//		objs.put("resList", list);
//		GrenricUtil._get_code(request,objs,XjanyConstants.GRENERIPATH,XjanyConstants.TEMPLETPATH, "zhiye.html","zhiye.js");
//		return "redirect:v_list.do";
//	}
	
	/*
	 * 以下是ctg的action
	 */

	@RequestMapping("/resource/v_list_ctg.do")
	public String vListCtg(HttpServletRequest request, ModelMap model, AllResLibCTG allResLibCTG)
	throws Exception {
		List<AllResLibCTG> list = resourceService.findAllCTG();
		model.addAttribute("list", list);
		return "/rescatgory/list";
	}
}
