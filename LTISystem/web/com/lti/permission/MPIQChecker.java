package com.lti.permission;

import com.lti.service.GroupManager;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class MPIQChecker {
	private boolean hasSubscred=false;
	public MPIQChecker(long userid){
		if(userid==Configuration.SUPER_USER_ID){
			hasSubscred=true;
			return;
		}
		GroupManager gm=ContextHolder.getGroupManager();
		Object[] groups=gm.getGroupIDs(userid);
		if(groups!=null&&groups.length>0){
			for(int i=0;i<groups.length;i++){
				if(groups[i].equals(Configuration.GROUP_MPIQ_B_ID) ||
						groups[i].equals(Configuration.GROUP_MPIQ_E_ID) ||
						groups[i].equals(Configuration.GROUP_MPIQ_P_ID)){
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
