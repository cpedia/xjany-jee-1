package com.lti.action;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.context.SecurityContext;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;

import com.lti.service.UserManager;
import com.lti.service.bo.User;
import com.lti.system.ContextHolder;
import com.lti.system.EmailLoginServiceImpl;

public class fbaction {
	
	private String fbEmail;
	private String message;
	public String facebook() throws NoSuchAlgorithmException{
		UserManager userManager = ContextHolder.getUserManager();
		User user = userManager.getUserByEmail(fbEmail);		
		message = "";
		if(user ==null){
			String [] ss = fbEmail.split("@");
			String name = ss[0];
			User us = userManager.get(name);
			int n=1;
			String username=name;
			while(us!=null){
				username=name+n;
				n++;
				us = userManager.get(username);
			}
			
			String password="validfijohnc403";
			User nUser = new User();		
			nUser.setUserName(username);
			nUser.setEMail(fbEmail);
			nUser.setPassword(password);
			nUser.setEnabled(true);
			userManager.add(nUser);
			EmailLoginServiceImpl userDetail = (EmailLoginServiceImpl) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("emailLoginServiceImpl");
			List<String> authNames = userDetail.AuthoritiesByUsernameQuery(nUser);
			int authN = authNames.size();
	        GrantedAuthority[] authorities = new GrantedAuthority[authN];             
	        for(int i=0; i<authN; i++) {   
	            authorities[i] = new GrantedAuthorityImpl(authNames.get(i).toString());   
	        }
			UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(username,password,authorities);
			result.setDetails(userDetail);
			SecurityContext sc = SecurityContextHolder.getContext();
			sc.setAuthentication(result);
			message="Thanks for logging in with Facebook and creating an MyPlanIQ account! Please set your password at \" My Acount\" page.";
			
		}else{
			String username = user.getUserName();
			String password = user.getPassword();
			EmailLoginServiceImpl userDetail = (EmailLoginServiceImpl) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("emailLoginServiceImpl");
			List<String> authNames = userDetail.AuthoritiesByUsernameQuery(user);
			int authN = authNames.size();
	        GrantedAuthority[] authorities = new GrantedAuthority[authN];             
	        for(int i=0; i<authN; i++) {   
	            authorities[i] = new GrantedAuthorityImpl(authNames.get(i).toString());   
	        }   
			UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(username,password,authorities);
			result.setDetails(userDetail);
			SecurityContext sc = SecurityContextHolder.getContext();
			sc.setAuthentication(result);
			
		}
		return Action.MESSAGE;
	}
	public String getFbEmail() {
		return fbEmail;
	}
	public void setFbEmail(String fbEmail) {
		this.fbEmail = fbEmail;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
