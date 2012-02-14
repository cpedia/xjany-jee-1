package com.xjany.bbs.action.front;

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
import com.xjany.common.web.session.SessionProvider;

@Controller
public class UserAction {
	@Autowired
	private UserService userService;
	
	@Autowired
	private SessionProvider sessionProvider;
	
	private MD5Util verification = new MD5UtilImpl();
	
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public SessionProvider getSessionProvider() {
		return sessionProvider;
	}
	public void setSessionProvider(SessionProvider sessionProvider) {
		this.sessionProvider = sessionProvider;
	}
	@RequestMapping("/user/index.do")
	public String showIndex(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "user/index";
	}
	
	@RequestMapping("/user/login.do")
	public String showLogin(HttpServletRequest request, ModelMap model)
			throws Exception {
		return "user/login";
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
	
	@RequestMapping("/user/check.do")
	public String check(AllUser user, HttpServletRequest request, ModelMap model) {
		AllUser result = userService.check(user);
		model.addAttribute("message", result);
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
        user.setUserRegIp(verification.getIp(request));
		user.setUserRegTime(new java.sql.Timestamp(System.currentTimeMillis()));
		AllUser result = userService.save(user, bbsUserProfile, null);
		model.addAttribute("message",result);
		if(result != null)
			return "user/index";
		return "../common/message";
		
		//PrintWriter out = response.getWriter();
		//out.println("<script language='JavaScript'>alert(\"success\");location.replace('/index.jsp');</script>");
	}
	
}
