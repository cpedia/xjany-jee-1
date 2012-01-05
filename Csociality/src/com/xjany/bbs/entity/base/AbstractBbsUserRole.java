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

import com.xjany.bbs.entity.BbsUserProfile;

/**
 * AbstractBbsUserRole entity provides the base persistence definition of the
 * BbsUserRole entity. @author LiXiang
 */
@MappedSuperclass
public abstract class AbstractBbsUserRole implements java.io.Serializable {

	// Fields

	private Integer roleId;
	private String roleName;
	private Integer cmsDel;
	private Set<BbsUserProfile> bbsUserProfiles = new HashSet<BbsUserProfile>(0);

	// Constructors

	/** default constructor */
	public AbstractBbsUserRole() {
	}

	/** minimal constructor */
	public AbstractBbsUserRole(String roleName) {
		this.roleName = roleName;
	}

	/** full constructor */
	public AbstractBbsUserRole(String roleName, Integer cmsDel,
			Set<BbsUserProfile> bbsUserProfiles) {
		this.roleName = roleName;
		this.cmsDel = cmsDel;
		this.bbsUserProfiles = bbsUserProfiles;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "roleId", unique = true, nullable = false)
	public Integer getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	@Column(name = "roleName", nullable = false, length = 50)
	public String getRoleName() {
		return this.roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Column(name = "cms_del")
	public Integer getCmsDel() {
		return this.cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bbsUserRole")
	public Set<BbsUserProfile> getBbsUserProfiles() {
		return this.bbsUserProfiles;
	}

	public void setBbsUserProfiles(Set<BbsUserProfile> bbsUserProfiles) {
		this.bbsUserProfiles = bbsUserProfiles;
	}

}