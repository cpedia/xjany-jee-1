package com.lti.system;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.ui.AbstractProcessingFilter;
import org.acegisecurity.ui.savedrequest.SavedRequest;
import org.acegisecurity.ui.webapp.AuthenticationProcessingFilter;
import org.acegisecurity.Authentication;

import javax.print.attribute.standard.Severity;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.acegisecurity.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.lti.permission.MPIQChecker;
import com.lti.permission.ValidFiChecker;
import com.lti.service.GroupManager;
import com.lti.service.InviteManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.InviteCode;
import com.lti.service.bo.User;
import com.lti.type.EmailLoginDetails;
import com.lti.util.EryptUtil;
import com.lti.util.EscapeUnescapeUtil;
import com.opensymphony.xwork2.ActionContext;

public class MyAcegiFilter extends AuthenticationProcessingFilter {
	/*
	 * overwrite acegi for adding cookies when login
	 * 
	 * @author Tomara
	 */
	private UserManager userManager;
	private InviteManager inviteManager;


	public static void addCookies(HttpServletResponse response, long userID, String username, int max) {
		Cookie cookie = new Cookie("jforumSSOCookie", new EryptUtil().encryptData2(username));
		cookie.setMaxAge(max);
		cookie.setPath("/jforum");
		response.addCookie(cookie);
		GroupManager gm = ContextHolder.getGroupManager();
		List<Group> groups = gm.getUserGroups(userID);
		if (groups != null) {
			boolean flag = false;
			for (int i = 0; i < groups.size(); i++) {
				Group g = groups.get(i);
				if (g.getID().equals(Configuration.GROUP_MPIQ_ID)) {
					Cookie cookiegroup = new Cookie("jforumSSOGroupCookie", new EryptUtil().encryptData2("F401K"));
					cookiegroup.setMaxAge(max);
					cookiegroup.setPath("/jforum");
					response.addCookie(cookiegroup);
					flag = true;
					break;
				}
			}
			if (!flag) {
				Cookie cookiegroup = new Cookie("jforumSSOGroupCookie", new EryptUtil().encryptData2("VALIDFI"));
				cookiegroup.setMaxAge(max);
				cookiegroup.setPath("/jforum");
				response.addCookie(cookiegroup);
			}
		}
	}

	public static void addAdditionalInfor(HttpServletRequest request, HttpServletResponse response, User user,int max) {
		Long userID = null;
		String username=user.getUserName();

		if (user != null) {
			userID = user.getID();
			if(max!=Integer.MIN_VALUE)addCookies(response, userID, username, max);
			List<Group> groups = ContextHolder.getUserManager().getGroupsByUser(userID);
			StringBuffer sb = new StringBuffer();
			sb.append(Configuration.GROUP_ANONYMOUS_ID);
			if (groups != null && groups.size() != 0) {

				for (int i = 0; i < groups.size(); i++) {
					if(groups.get(i)!=null&&groups.get(i).getID()!=null){
						sb.append(",");
						sb.append(groups.get(i).getID());
					}
				}
			}
			request.getSession().setAttribute("user", user);
			request.getSession().setAttribute("groups", sb.toString());
			request.getSession().setAttribute("username", username);
			ValidFiChecker vc=new ValidFiChecker(userID);
			MPIQChecker fc=new MPIQChecker(userID);
			request.getSession().setAttribute("validfiChecker", vc);
			request.getSession().setAttribute("mpiqChecker", fc);
		} else {
			userID = Configuration.USER_ANONYMOUS;
		}
	}

	protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
		super.onSuccessfulAuthentication(request, response, authResult);
		String username = null;
		Object obj = authResult.getPrincipal();

		if (obj instanceof UserDetails || obj instanceof EmailLoginDetails) {
			username = ((UserDetails) obj).getUsername();
			User user = userManager.get(username);
			addAdditionalInfor(request,response,user,Integer.MIN_VALUE);
		} else {
			username = obj.toString();
		}

		// request.getSession().
	}

	@Override
	public String getDefaultTargetUrl() {
		// TODO Auto-generated method stub
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = (HttpServletRequest) requestAttributes.getRequest();
		//给已注册的用户添加邀请码
		InviteCode inviteCode = new InviteCode();
		inviteCode = inviteManager.getInviteId(request.getParameter("inviteCode"));
		String reqInviteCode = request.getParameter("inviteCode");
		if(reqInviteCode!=null&&!reqInviteCode.equals("")){
			if(inviteCode!=null){
				User user = new User();
				user = userManager.get(request.getParameter("j_username"));
				user.setInviteCodeID((long)inviteManager.getInviteId(request.getParameter("inviteCode")).getID());
				System.out.print("inviteCodeID:"+inviteManager.getInviteId(request.getParameter("inviteCode")).getID());
				userManager.update(user);
				
				return "/invitsuccess.jsp";
			}else{
				return "/invitfail.jsp";
			}
			
		}
		SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute(AbstractProcessingFilter.ACEGI_SAVED_REQUEST_KEY);
		if (savedRequest != null) {
			System.out.println(savedRequest.getFullRequestUrl());
			return savedRequest.getFullRequestUrl();
		}
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			String url = "";
			if(request.getParameter("tolanding") !=null ) return "/f401k__landingma.action";
			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (c.getName().equalsIgnoreCase("requestpage")) {
					url = EscapeUnescapeUtil.unescape(c.getValue());
					System.out.println(url);
				}
			}
			if (url != null && !url.equals(""))
				return url;
		}
		return super.getDefaultTargetUrl();
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	public void setInviteManager(InviteManager inviteManager) {
		this.inviteManager = inviteManager;
	}

}