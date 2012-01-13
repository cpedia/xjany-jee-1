package com.xjany.bbs.action.admin;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xjany.bbs.entity.AllUser;
import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.bbs.entity.BbsUserProfile;
import com.xjany.bbs.service.GroupService;
import com.xjany.bbs.service.UserService;
import com.xjany.common.page.Pagination;
import com.xjany.common.page.SimplePage;
import com.xjany.common.util.CookieUtils;
import com.xjany.common.util.RequestUtils;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


@Controller
public class GroupAction{
	
	@Autowired
	private GroupService groupService;
	
	@RequestMapping("/group/testAjax.do")
	public String testAjax(HttpServletRequest request, ModelMap model) {
		model.addAttribute("message", "test");
		return "../common/message";
	}
	
	@RequestMapping("/group/v_list.do")
	public String v_list(Integer id,HttpServletRequest request, ModelMap model) {
		List<AllUserGroup> list = groupService.findById(id);
		model.addAttribute("groupList", list);
		return "group/list";
	}
}
