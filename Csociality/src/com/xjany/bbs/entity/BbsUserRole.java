package com.xjany.bbs.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractBbsUserRole;

/**
 * BbsUserRole entity. @author LiXiang
 */
@Entity
@Table(name = "bbs_user_role", catalog = "cs_xjany")
public class BbsUserRole extends AbstractBbsUserRole implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public BbsUserRole() {
	}

	/** minimal constructor */
	public BbsUserRole(String roleName) {
		super(roleName);
	}

	/** full constructor */
	public BbsUserRole(String roleName, Integer cmsDel,
			Set<BbsUserProfile> bbsUserProfiles) {
		super(roleName, cmsDel, bbsUserProfiles);
	}
	
	@Override
	public void recycle(boolean isRecycle) {
		// TODO Auto-generated method stub
		if(isRecycle)
			this.setCmsDel(1);
		else 
			this.setCmsDel(0);
	}

}
