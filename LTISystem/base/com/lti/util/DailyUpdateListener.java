/**
 * 
 */
package com.lti.util;

/**
 * @author CCD
 *
 */
public class DailyUpdateListener{
	private SecurityManagerListener securityManagerListener;
	
	public void setSecurityManagerListener(
			SecurityManagerListener securityManagerListener) {
		this.securityManagerListener = securityManagerListener;
	}
	
	public void copy_total(int totalSize)
	{
		securityManagerListener.init(totalSize);
	}
	
	public void copy_current(int currentPos)
	{
		securityManagerListener.copy_num(currentPos);
	}
	
	public void copy_state(String state)
	{
		securityManagerListener.copy_string(state);
	}
}
