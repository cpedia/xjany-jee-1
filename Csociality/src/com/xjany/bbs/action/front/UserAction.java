package com.xjany.bbs.action.front;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xjany.bbs.entity.AllUser;
import com.xjany.bbs.service.UserService;
@Controller
public class UserAction {
	@Autowired
	private UserService userSerive;

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
}
