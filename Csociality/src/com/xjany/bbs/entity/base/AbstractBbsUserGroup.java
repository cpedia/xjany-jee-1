package com.xjany.bbs.entity.base;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import com.xjany.bbs.entity.AllUser;
import com.xjany.bbs.entity.BbsUserProfile;
import com.xjany.bbs.entity.InterGeneric;

/**
 * AbstractBbsUserRole entity provides the base persistence definition of the
 * BbsUserRole entity. @author LiXiang
 */
@MappedSuperclass
public abstract class AbstractBbsUserGroup implements java.io.Serializable, InterGeneric {

	// Fields

	private Integer groupId;
	private String groupName;
	private Integer cmsDel = 0;
	private Set<AllUser> allUser = new HashSet<AllUser>(0);

	// Constructors

	/** default constructor */
	public AbstractBbsUserGroup() {
	}

	/** minimal constructor */
	public AbstractBbsUserGroup(String groupName) {
		this.groupName = groupName;
	}

	/** full constructor */
	public AbstractBbsUserGroup(String roleName, Integer cmsDel,
			Set<AllUser> allUser) {
		this.groupName = roleName;
		this.cmsDel = cmsDel;
		this.allUser = allUser;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "groupId", unique = true, nullable = false)
	public Integer getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	@Column(name = "groupName", nullable = false, length = 50)
	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	@Column(name = "cms_del")
	public Integer getCmsDel() {
		return this.cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bbsUserRole")
	public Set<AllUser> getAllUser() {
		return this.allUser;
	}

	public void setAllUser(Set<AllUser> allUser) {
		this.allUser = allUser;
	}

}