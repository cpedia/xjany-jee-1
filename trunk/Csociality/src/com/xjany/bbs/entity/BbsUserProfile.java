package com.xjany.bbs.entity;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractBbsUserProfile;

/**
 * BbsUserProfile entity. @author LiXiang
 */
@Entity
@Table(name = "bbs_user_profile", catalog = "cs_xjany")
public class BbsUserProfile extends AbstractBbsUserProfile implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public BbsUserProfile() {
	}

	/** full constructor */
	public BbsUserProfile(AllUser allUser, BbsUserRole bbsUserRole,
			Integer userRoleId, String userBirthDay, String userFace,
			String userQicq, String userIntro, String userAddr,
			String userCareer, Integer userWealth, Integer userDegree,
			Integer userTopic, Integer userReply, Integer userDelTopic,
			Integer userEliteTopic, Set<AllUser> allUsers) {
		super(allUser, bbsUserRole, userRoleId, userBirthDay, userFace,
				userQicq, userIntro, userAddr, userCareer, userWealth,
				userDegree, userTopic, userReply, userDelTopic, userEliteTopic,
				allUsers);
	}

}
