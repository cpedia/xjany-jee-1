package com.lti.system;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.acegisecurity.Authentication;
import org.acegisecurity.ui.rememberme.TokenBasedRememberMeServices;
import org.acegisecurity.userdetails.UserDetails;

import com.lti.service.UserManager;
import com.lti.service.bo.User;
import com.lti.type.EmailLoginDetails;

public class MyAcegiRememberMe extends TokenBasedRememberMeServices{
	
	/*overwrite acegi for adding cookies when login
	 * @author Tomara*/
	
	private UserManager userManager;
	
	public void loginSuccess(HttpServletRequest request, HttpServletResponse response, Authentication successfulAuthentication)
	{
		super.loginSuccess(request, response, successfulAuthentication);
 		String username=null;
		Object obj = successfulAuthentication.getPrincipal();

		if (obj instanceof UserDetails || obj instanceof EmailLoginDetails) {
			username = ((UserDetails) obj).getUsername();
		} else {
			username = obj.toString();
		}
		User user = userManager.get(username);
		Long userID;
		if(user != null){
			MyAcegiFilter.addAdditionalInfor(request,response,user,31536000);
		}
		else
		{
			userID = Configuration.USER_ANONYMOUS;
		}
		
	}

	@Override
	public Authentication autoLogin(HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		Authentication authentication = super.autoLogin(request, response);
		if(authentication == null)
			return authentication;
		Object obj = authentication.getPrincipal();
		String username=null;
		if (obj instanceof UserDetails || obj instanceof EmailLoginDetails) {
			username = ((UserDetails) obj).getUsername();
		} else {
			username = obj.toString();
		}
		User user = userManager.get(username);
		Long userID;
		if(user != null){
			MyAcegiFilter.addAdditionalInfor(request,response,user,Integer.MIN_VALUE);
		}
		else
		{
			userID = Configuration.USER_ANONYMOUS;
		}
		return authentication;
	}



	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
} 