package com.lti.type;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;

public class EmailLoginDetails implements UserDetails{

	
	private static final long serialVersionUID = 1234L;
	private String username;   
    private String password;   
    private boolean enabled;   
    private GrantedAuthority[] authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public GrantedAuthority[] getAuthorities() {
		return authorities;
	}
	public void setAuthorities(GrantedAuthority[] authorities) {
		this.authorities = authorities;
	} 
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
        sb.append(super.toString()).append(": ");
        sb.append("Username: ").append(username).append("; ");
        sb.append("Password: [PROTECTED]; ");
        sb.append("Enabled: ").append(enabled).append("; ");
        sb.append("AccountNonExpired: ").append(accountNonExpired).append("; ");
        sb.append("credentialsNonExpired: ").append(credentialsNonExpired).append("; ");
        sb.append("AccountNonLocked: ").append(accountNonLocked).append("; ");
        if(getAuthorities() != null)
        {
            sb.append("Granted Authorities: ");
            for(int i = 0; i < getAuthorities().length; i++)
            {
                if(i > 0)
                    sb.append(", ");
                sb.append(getAuthorities()[i].toString());
            }

        } else
        {
            sb.append("Not granted any authorities");
        }
        return sb.toString();
	}
}
