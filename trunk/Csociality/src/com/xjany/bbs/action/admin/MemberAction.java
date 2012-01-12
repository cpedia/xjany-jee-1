package com.xjany.bbs.action.admin;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xjany.bbs.entity.AllUser;
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
		return "member/list";
	}
	
	@RequestMapping("/member/v_add.do")
	public String v_add(Integer pageNo,HttpServletRequest request, ModelMap model) {
		List list = groupService.findGroupBySql();
		model.addAttribute("groupList", list);
		return "member/add";
	}
	
	@RequestMapping("member/v_edit.do")
	public String v_edit(HttpServletRequest request, ModelMap model) {
		return "member/edit";
	}
	
	@RequestMapping("/member/o_save.do")
	public String o_save(BbsUserProfile bbsUserProfile,AllUser allUser,HttpServletRequest request, ModelMap model) {
		allUser.setUserIp(RequestUtils.getIpAddr(request));
		allUser.setUserLastTime(new Timestamp(new Date().getTime()));
		userService.save(allUser,bbsUserProfile);
		return "redirect:v_list.do";
	}
	
	@RequestMapping("/member/o_delete.do")
	public String o_delete(String ids,HttpServletRequest request, ModelMap model) {
		userService.delete(Integer.valueOf(ids));
		return "redirect:v_list.do";
	}
}
