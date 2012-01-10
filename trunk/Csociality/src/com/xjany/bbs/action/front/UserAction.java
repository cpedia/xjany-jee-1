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
	
	private AllUser user;
	
	@RequestMapping("/login.jhtml")
	public String showLogin(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "login";
	}
	@RequestMapping("/register.jhtml")
	public String showRegister(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "register";
	}
}
