package com.lti.permission;

import java.util.Date;

public interface PermissionChecker {
	
	public final static int REAL_TIME=0;
	public final static int DELAY_TIME=1;
	public final static int NO_VIEW=2;
	public final static int READ_CODE=3;
	public final static int BASIC_READ=4;
	
	public boolean isOwner();
	public boolean isAdmin();
	public boolean isAnonymous();
	
	public boolean hasRealtimeRole();
	public boolean hasDelaytimeRole();
	public Date getLastLegalDate();
	
	public boolean hasOperationRole();
	
	
	public boolean hasViewRole();
	
	public boolean hasViewCodeRole();
	
	public int getReadMode();
	
	public boolean hasSubscred();
	
	public boolean isAdvancedUser();
}
