package com.xjany.bbs.action.admin;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.bbs.service.GroupService;
import com.xjany.common.exception.DaoException;
import com.xjany.common.exception.ServiceException;
import com.xjany.common.page.Pagination;
import com.xjany.common.page.SimplePage;
import com.xjany.common.util.CookieUtils;

@Controller
public class GroupAction {

	@Autowired
	private GroupService groupService;

	@RequestMapping("/group/v_list.do")
	public String v_list(Integer pageNo, HttpServletRequest request, ModelMap model) {
		Pagination pagination = groupService.getPage(SimplePage.cpn(pageNo), CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		return "group/list";
	}

	@RequestMapping("/group/v_add.do")
	public String v_add(HttpServletRequest request, ModelMap model) {
		return "group/add";
	}

	@RequestMapping("/group/v_edit.do")
	public String v_edit(Integer id,HttpServletRequest request, ModelMap model) {
		AllUserGroup allUserGroup = groupService.findById(id);
		model.addAttribute("allUserGroup",allUserGroup);
		return "group/edit";
	}
	
	@RequestMapping("/group/o_save.do")
	public String o_save(AllUserGroup allUserGroup, HttpServletRequest request, ModelMap model) {
		groupService.save(allUserGroup);
		return "redirect:v_list.do";
	}

	@RequestMapping("/group/o_edit.do")
	public String o_edit(Integer id,HttpServletRequest request, ModelMap model) {
		AllUserGroup allUserGroup = groupService.findById(id);
		model.addAttribute("allUserGroup",allUserGroup);
		return "group/edit";
	}
	
	@RequestMapping("/group/o_delete.do")
	public String o_delete(Integer id,HttpServletRequest request, ModelMap model) {
		try {
			groupService.deleteById(id);
		} catch (DaoException e) {
		}
		return "redirect:v_list.do";
	}
}
