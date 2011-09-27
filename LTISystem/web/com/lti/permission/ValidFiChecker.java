package com.lti.permission;

import com.lti.service.GroupManager;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class ValidFiChecker {
	private boolean hasSubscred=false;
	public ValidFiChecker(long userid){
		if(userid==Configuration.SUPER_USER_ID){
			hasSubscred=true;
			return;
		}
		GroupManager gm=ContextHolder.getGroupManager();
		Object[] groups=gm.getGroupIDs(userid);
		if(groups!=null&&groups.length>0){
			for(int i=0;i<groups.length;i++){
				if(Configuration.GROUP_VF_B_ID.equals(groups[i])){
					hasSubscred=true;
					break;
				}
			}
		}
	}
	public boolean hasSubscred(){
		return hasSubscred;
	}
}
