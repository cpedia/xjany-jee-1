package com.xjany.action.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xjany.common.page.Pagination;
import com.xjany.common.page.SimplePage;
import com.xjany.common.web.CookieUtils;

@Controller
public class CreateStyleAction {

//	@Autowired
//	private CreatestyleService createstyleService;
//
//	@RequestMapping("/createstyle/v_list.do")
//	public String v_list(Integer pageNo, HttpServletRequest request, ModelMap model) {
//		Pagination pagination = createstyleService.getPage(SimplePage.cpn(pageNo), CookieUtils.getPageSize(request));
//		model.addAttribute("pagination", pagination);
//		return "createstyle/list";
//	}
//
//	@RequestMapping("/createstyle/v_add.do")
//	public String v_add(HttpServletRequest request, ModelMap model) {
//		return "createstyle/add";
//	}
//
//	@RequestMapping("/createstyle/v_edit.do")
//	public String v_edit(Integer id,HttpServletRequest request, ModelMap model) {
//		Bbscreatestyle bbscreatestyle = createstyleService.findById(id);
//		model.addAttribute("bbscreatestyle",bbscreatestyle);
//		return "createstyle/edit";
//	}
//	
//	@RequestMapping("/createstyle/o_save.do")
//	public String o_save(Bbscreatestyle bbscreatestyle, HttpServletRequest request, ModelMap model) {
//		createstyleService.save(bbscreatestyle);
//		return "redirect:v_list.do";
//	}
//
//	@RequestMapping("/createstyle/o_update.do")
//	public String o_update(Bbscreatestyle bbscreatestyle,HttpServletRequest request, ModelMap model) {
//		createstyleService.update(bbscreatestyle);
//		return "redirect:v_list.do";
//	}
//	
//	@RequestMapping("/createstyle/o_delete.do")
//	public String o_delete(Integer id,HttpServletRequest request, ModelMap model) {
//		createstyleService.deleteById(id);
//		return "redirect:v_list.do";
//	}
}
