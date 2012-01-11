package com.xjany.bbs.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractAllUserGroup;

/**
 * AllUserGroup entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "all_user_group", catalog = "cs_xjany")
public class AllUserGroup extends AbstractAllUserGroup implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public AllUserGroup() {
	}

	/** minimal constructor */
	public AllUserGroup(String groupName) {
		super(groupName);
	}

	/** full constructor */
	public AllUserGroup(String groupName, Integer cmsDel, Set<AllUser> allUsers) {
		super(groupName, cmsDel, allUsers);
	}

}
