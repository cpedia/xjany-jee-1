package com.xjany.bbs.action.front;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xjany.bbs.entity.AllUser;
import com.xjany.bbs.entity.BbsUserProfile;
import com.xjany.bbs.service.UserService;
import com.xjany.common.MD5Util;
import com.xjany.common.util.MD5UtilImpl;
import com.xjany.common.util.RequestUtils;
@Controller
public class UserAction {
	@Autowired
	private UserService userSerive;
	private MD5Util verification = new MD5UtilImpl();
	public UserService getUserSerive() {
		return userSerive;
	}

	public void setUserSerive(UserService userSerive) {
		this.userSerive = userSerive;
	}
	
	@RequestMapping("/login.do")
	public String showLogin(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "login";
	}
	@RequestMapping("/register.do")
	public String showRegisterDo(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "register";
	}
	@RequestMapping("/register.jhtml")
	public String showRegisterJhtml(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "register";
	}
	
	@RequestMapping("/checkUser.do")
	public String check(AllUser user, HttpServletRequest request, ModelMap model) {
		boolean result = userSerive.check(user);
		model.addAttribute("message", String.valueOf(result));
		return "../common/message";
	}
	@RequestMapping("/verification.do")
	public String verification(String codevalue, HttpServletRequest request, ModelMap model) {
		boolean result = (codevalue == verification.randFour());
		model.addAttribute("message",String.valueOf(result));
		return "../common/message";
	}
	@RequestMapping("/addBbsUserInfo.do")
	public String addBbsUserInfo(AllUser user,BbsUserProfile bbsUserProfile, HttpServletRequest request, ModelMap model) {
		user.setUserLoad(0);
		user.setUserRegIp(RequestUtils.getIpAddr(request));
		user.setUserRegTime(new java.sql.Timestamp(System.currentTimeMillis()));
		int result = userSerive.save(user, bbsUserProfile, null);
		model.addAttribute("message",String.valueOf(result));
		return "../common/message";
	}
	
}
