package com.lti.system;

import java.util.List;

import javax.servlet.jsp.JspException;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.taglibs.authz.AuthorizeTag;
import org.apache.commons.lang.StringUtils;

import com.lti.service.UserManager;
import com.lti.service.bo.User;
import com.lti.type.EmailLoginDetails;
/**
 * Extend the acegi's tags.
 * @author SuPing
 * 2010-09-09
 */
public class LTIAuthorizeTag  extends AuthorizeTag{

	/**
	  * 扩展acegi标签，实现指定方法名称自动根据名称生成对应的权限字符序列传递给acegi对应的标签
	  */
	private static final long serialVersionUID = 1L;
	private String ifAllGranted = "";
    private String ifAnyGranted = "";
    private String ifNotGranted = "";
	private UserManager userManager;
	
	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public String getIfAllGranted() {
		return ifAllGranted;
	}

	public void setIfAllGranted(String ifAllGranted) {
		this.ifAllGranted = ifAllGranted;
	}

	public String getIfAnyGranted() {
		return ifAnyGranted;
	}

	public void setIfAnyGranted(String ifAnyGranted) {
		this.ifAnyGranted = ifAnyGranted;
	}

	public String getIfNotGranted() {
		return ifNotGranted;
	}

	public void setIfNotGranted(String ifNotGranted) {
		this.ifNotGranted = ifNotGranted;
	}
	
	@Override
	public int doStartTag() throws JspException {
		//如果设置的为空则显示标签体
	    if(StringUtils.isBlank(getIfAllGranted()) && StringUtils.isBlank(getIfAnyGranted()) && StringUtils.isBlank(getIfNotGranted())){
	    	return EVAL_BODY_INCLUDE;
	    }
	    userManager = ContextHolder.getUserManager();
	    User us = userManager.getLoginUser();
		String username = us.getUserName();
		EmailLoginServiceImpl userDetail = (EmailLoginServiceImpl) com.lti.system.ContextHolder.getInstance().getApplicationContext().getBean("emailLoginServiceImpl");
	    EmailLoginDetails userDetails = (EmailLoginDetails)userDetail.loadUserByUsername(username);
	    GrantedAuthority[] authorities = userDetails.getAuthorities();
	    String grantedString = null;
	    boolean isAnyGranted = false;
	    boolean isAllGranted = false;
	    boolean isNotGranted = false;
	    if(authorities!=null && authorities.length>0){
	    	if(!StringUtils.isBlank(getIfAnyGranted())){
	    		String[] Granted = getIfAnyGranted().split(",");
	    		for(int i=0;i<Granted.length;i++){
	    			String granted = Granted[i];
	    			for(int j=0;j<authorities.length;j++){
	    				grantedString = ((String)authorities[j].getAuthority()).trim();
	    				if(grantedString.equals(granted)){
	    					isAnyGranted = true;
	    					break;
	    				}
	    			}
	    		}
	    	}
	    	else if(!StringUtils.isBlank(getIfAllGranted())){
	    		String[] Granted = getIfAllGranted().split(",");
	    		int num=0;
	    		for(int i=0;i<Granted.length;i++){
	    			String granted = Granted[i];
	    			for(int j=0;j<authorities.length;j++){
	    				grantedString = ((String)authorities[j].getAuthority()).trim();
	    				if(grantedString.equals(granted)){
	    					num++;continue;
	    				}
	    			}
	    		}
	    		if(num==Granted.length)  isAllGranted=true;
	    	}
	    	else if(!StringUtils.isBlank(getIfNotGranted())){
	    		String[] Granted = getIfNotGranted().split(",");
	    		int num=0;
	    		for(int i=0;i<Granted.length;i++){
	    			String granted = Granted[i];
	    			for(int j=0;j<authorities.length;j++){
	    				grantedString = ((String)authorities[j].getAuthority()).trim();
	    				if(grantedString.equals(granted)){
	    					num++;//isNotGranted = true;
	    				}
	    			}
	    		}
	    		if(num==0) isNotGranted = true;
	    	}
	    }
	    //如果设置的funcString没有在acegi的权限控制范围内则显示标签体
	    if(isAnyGranted || isAllGranted || isNotGranted){
	    	return EVAL_BODY_INCLUDE;
	    }    
	    //如果在acegi的权限控制范围则取出该资源相对应的权限设置到setIfAnyGranted()方法中
	    String roles="";
	    if(authorities!=null && authorities.length>0){
	    	for(int i=0;i<authorities.length;i++){
	    		roles += authorities[i].getAuthority() + "\t"; 
	    	}
	    	roles = roles.substring(0,roles.length()-1); 
	    }
	    else
	    	roles = "";
	    if(!StringUtils.isBlank(getIfAnyGranted())) this.setIfAnyGranted(roles);
	    else if(!StringUtils.isBlank(getIfAllGranted())) this.setIfAllGranted(roles);
	    else if(!StringUtils.isBlank(getIfNotGranted())) this.setIfNotGranted(roles);
	    return super.doStartTag();
	}	
}
