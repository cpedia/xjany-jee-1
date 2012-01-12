package com.xjany.bbs.entity.base;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import com.xjany.bbs.entity.AllUser;
import com.xjany.bbs.entity.BbsUserRole;

/**
 * AbstractBbsUserProfile entity provides the base persistence definition of the
 * BbsUserProfile entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractBbsUserProfile implements java.io.Serializable {

	// Fields

	private Integer bbsUserId;
	private BbsUserRole bbsUserRole;
	private Integer userRoleId;
	private String userBirthDay;
	private String userFace;
	private String userQicq;
	private String userIntro;
	private String userAddr;
	private String userCareer;
	private Integer userWealth;
	private Integer userDegree;
	private Integer userTopic;
	private Integer userReply;
	private Integer userDelTopic;
	private Integer userEliteTopic;
	private Set<AllUser> allUsers = new HashSet<AllUser>(0);

	// Constructors

	/** default constructor */
	public AbstractBbsUserProfile() {
	}

	/** full constructor */
	public AbstractBbsUserProfile(BbsUserRole bbsUserRole, Integer userRoleId,
			String userBirthDay, String userFace, String userQicq,
			String userIntro, String userAddr, String userCareer,
			Integer userWealth, Integer userDegree, Integer userTopic,
			Integer userReply, Integer userDelTopic, Integer userEliteTopic,
			Set<AllUser> allUsers) {
		this.bbsUserRole = bbsUserRole;
		this.userRoleId = userRoleId;
		this.userBirthDay = userBirthDay;
		this.userFace = userFace;
		this.userQicq = userQicq;
		this.userIntro = userIntro;
		this.userAddr = userAddr;
		this.userCareer = userCareer;
		this.userWealth = userWealth;
		this.userDegree = userDegree;
		this.userTopic = userTopic;
		this.userReply = userReply;
		this.userDelTopic = userDelTopic;
		this.userEliteTopic = userEliteTopic;
		this.allUsers = allUsers;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "bbs_userId", unique = true, nullable = false)
	public Integer getBbsUserId() {
		return this.bbsUserId;
	}

	public void setBbsUserId(Integer bbsUserId) {
		this.bbsUserId = bbsUserId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "roleId")
	public BbsUserRole getBbsUserRole() {
		return this.bbsUserRole;
	}

	public void setBbsUserRole(BbsUserRole bbsUserRole) {
		this.bbsUserRole = bbsUserRole;
	}

	@Column(name = "userRoleId")
	public Integer getUserRoleId() {
		return this.userRoleId;
	}

	public void setUserRoleId(Integer userRoleId) {
		this.userRoleId = userRoleId;
	}

	@Column(name = "userBirthDay", length = 10)
	public String getUserBirthDay() {
		return this.userBirthDay;
	}

	public void setUserBirthDay(String userBirthDay) {
		this.userBirthDay = userBirthDay;
	}

	@Column(name = "userFace", length = 30)
	public String getUserFace() {
		return this.userFace;
	}

	public void setUserFace(String userFace) {
		this.userFace = userFace;
	}

	@Column(name = "userQicq", length = 10)
	public String getUserQicq() {
		return this.userQicq;
	}

	public void setUserQicq(String userQicq) {
		this.userQicq = userQicq;
	}

	@Column(name = "userIntro", length = 500)
	public String getUserIntro() {
		return this.userIntro;
	}

	public void setUserIntro(String userIntro) {
		this.userIntro = userIntro;
	}

	@Column(name = "userAddr", length = 30)
	public String getUserAddr() {
		return this.userAddr;
	}

	public void setUserAddr(String userAddr) {
		this.userAddr = userAddr;
	}

	@Column(name = "userCareer", length = 30)
	public String getUserCareer() {
		return this.userCareer;
	}

	public void setUserCareer(String userCareer) {
		this.userCareer = userCareer;
	}

	@Column(name = "userWealth")
	public Integer getUserWealth() {
		return this.userWealth;
	}

	public void setUserWealth(Integer userWealth) {
		this.userWealth = userWealth;
	}

	@Column(name = "userDegree")
	public Integer getUserDegree() {
		return this.userDegree;
	}

	public void setUserDegree(Integer userDegree) {
		this.userDegree = userDegree;
	}

	@Column(name = "userTopic")
	public Integer getUserTopic() {
		return this.userTopic;
	}

	public void setUserTopic(Integer userTopic) {
		this.userTopic = userTopic;
	}

	@Column(name = "userReply")
	public Integer getUserReply() {
		return this.userReply;
	}

	public void setUserReply(Integer userReply) {
		this.userReply = userReply;
	}

	@Column(name = "userDelTopic")
	public Integer getUserDelTopic() {
		return this.userDelTopic;
	}

	public void setUserDelTopic(Integer userDelTopic) {
		this.userDelTopic = userDelTopic;
	}

	@Column(name = "userEliteTopic")
	public Integer getUserEliteTopic() {
		return this.userEliteTopic;
	}

	public void setUserEliteTopic(Integer userEliteTopic) {
		this.userEliteTopic = userEliteTopic;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bbsUserProfile")
	public Set<AllUser> getAllUsers() {
		return this.allUsers;
	}

	public void setAllUsers(Set<AllUser> allUsers) {
		this.allUsers = allUsers;
	}

}