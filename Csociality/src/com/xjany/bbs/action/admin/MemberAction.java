package com.xjany.bbs.action.admin;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xjany.bbs.service.FileService;
import com.xjany.bbs.service.GroupService;
import com.xjany.bbs.service.UserService;
import com.xjany.common.page.Pagination;
import com.xjany.common.page.SimplePage;
import com.xjany.common.util.CookieUtils;

import java.util.List;
import java.util.Properties;


@Controller
public class MemberAction{
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private GroupService groupService;

	@RequestMapping("/member/v_list.do")
	public String v_list(Integer pageNo,HttpServletRequest request, ModelMap model) {
		Pagination pagination = userService.getPage(null, null,
				null, null, false, false, null, SimplePage.cpn(pageNo),
				CookieUtils.getPageSize(request));
		model.addAttribute("pagination", pagination);
		return "/member/list";
	}
	
	@RequestMapping("/member/v_add.do")
	public String v_add(Integer pageNo,HttpServletRequest request, ModelMap model) {
		List list = groupService.findGroupBySql();
		model.addAttribute("groupList", list);
		return "/member/add";
	}
	
	@RequestMapping("/member/v_edit.do")
	public String v_edit(Integer pageNo,HttpServletRequest request, ModelMap model) {
		return "/member/edit";
	}
}
