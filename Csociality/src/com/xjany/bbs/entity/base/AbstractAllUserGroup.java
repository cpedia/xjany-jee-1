package com.xjany.bbs.entity.base;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import com.xjany.bbs.entity.AllUser;

/**
 * AbstractAllUserGroup entity provides the base persistence definition of the
 * AllUserGroup entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractAllUserGroup implements java.io.Serializable {

	// Fields

	private Integer groupId;
	private String groupName;
	private String groupIntro;
	private Integer cmsDel = 0;
	private Set<AllUser> allUsers = new HashSet<AllUser>(0);

	// Constructors

	/** default constructor */
	public AbstractAllUserGroup() {
	}

	/** minimal constructor */
	public AbstractAllUserGroup(String groupName) {
		this.groupName = groupName;
	}

	/** full constructor */
	public AbstractAllUserGroup(String groupName, Integer cmsDel,
			Set<AllUser> allUsers) {
		this.groupName = groupName;
		this.cmsDel = cmsDel;
		this.allUsers = allUsers;
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

	@Column(name = "groupName", length = 50)
	public String getGroupName() {
		return this.groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	@Column(name = "groupIntro", length = 400)
	public String getGroupIntro() {
		return this.groupIntro;
	}
	
	public void setGroupIntro(String groupIntro) {
		this.groupIntro = groupIntro;
	}

	@Column(name = "cms_del")
	public Integer getCmsDel() {
		return this.cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "allUserGroup")
	public Set<AllUser> getAllUsers() {
		return this.allUsers;
	}

	public void setAllUsers(Set<AllUser> allUsers) {
		this.allUsers = allUsers;
	}

}