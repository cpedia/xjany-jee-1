package com.xjany.bbs.entity.base;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.bbs.entity.BbsUserProfile;

/**
 * AbstractAllUser entity provides the base persistence definition of the
 * AllUser entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractAllUser implements java.io.Serializable {

	// Fields

	private Integer userId;
	private AllUserGroup allUserGroup;
	private BbsUserProfile bbsUserProfile;
	private Integer cmsUserId;
	private String userName;
	private String userPsw;
	private Integer userSex;
	private String userRealName;
	private String userEmail;
	private Timestamp userRegTime;
	private String userRegIp;
	private String userLastIp;
	private Integer userLoad;
	private Timestamp userLastTime;
	private Integer userState;
	private Integer cmsDel;

	// Constructors

	/** default constructor */
	public AbstractAllUser() {
	}

	/** minimal constructor */
	public AbstractAllUser(String userName, String userPsw) {
		this.userName = userName;
		this.userPsw = userPsw;
	}

	/** full constructor */
	public AbstractAllUser(AllUserGroup allUserGroup,
			BbsUserProfile bbsUserProfile, Integer cmsUserId, String userName,
			String userPsw, Integer userSex, String userEmail,
			Timestamp userRegTime, String userIp, Integer userLoad,
			Timestamp userLastTime, Integer userState, Integer cmsDel) {
		this.allUserGroup = allUserGroup;
		this.bbsUserProfile = bbsUserProfile;
		this.cmsUserId = cmsUserId;
		this.userName = userName;
		this.userPsw = userPsw;
		this.userSex = userSex;
		this.userEmail = userEmail;
		this.userRegTime = userRegTime;
		this.userRegIp = userIp;
		this.userLoad = userLoad;
		this.userLastTime = userLastTime;
		this.userState = userState;
		this.cmsDel = cmsDel;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "userId", unique = true, nullable = false)
	public Integer getUserId() {
		return this.userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "groupId")
	public AllUserGroup getAllUserGroup() {
		return this.allUserGroup;
	}

	public void setAllUserGroup(AllUserGroup allUserGroup) {
		this.allUserGroup = allUserGroup;
	}

	@ManyToOne()
	@JoinColumn(name = "bbs_userId")
	public BbsUserProfile getBbsUserProfile() {
		return this.bbsUserProfile;
	}

	public void setBbsUserProfile(BbsUserProfile bbsUserProfile) {
		this.bbsUserProfile = bbsUserProfile;
	}

	@Column(name = "cms_userId")
	public Integer getCmsUserId() {
		return this.cmsUserId;
	}

	public void setCmsUserId(Integer cmsUserId) {
		this.cmsUserId = cmsUserId;
	}

	@Column(name = "userName", nullable = false, length = 30)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "userPsw", nullable = false, length = 100)
	public String getUserPsw() {
		return this.userPsw;
	}

	public void setUserPsw(String userPsw) {
		this.userPsw = userPsw;
	}

	@Column(name = "userSex")
	public Integer getUserSex() {
		return this.userSex;
	}

	public void setUserSex(Integer userSex) {
		this.userSex = userSex;
	}

	@Column(name = "userEmail", length = 50)
	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	@Column(name = "userRealName", length = 30)
	public String getUserRealName() {
		return this.userRealName;
	}
	
	public void setUserRealName(String userRealName) {
		this.userRealName = userRealName;
	}

	@Column(name = "userRegTime", length = 19)
	public Timestamp getUserRegTime() {
		return this.userRegTime;
	}

	public void setUserRegTime(Timestamp userRegTime) {
		this.userRegTime = userRegTime;
	}

	@Column(name = "userRegIp", length = 30)
	public String getUserRegIp() {
		return this.userRegIp;
	}

	public void setUserRegIp(String userRegIp) {
		this.userRegIp = userRegIp;
	}
	
	@Column(name = "userLastIp", length = 30)
	public String getUserLastIp() {
		return this.userLastIp;
	}
	
	public void setUserLastIp(String userLastIp) {
		this.userLastIp = userLastIp;
	}

	@Column(name = "userLoad")
	public Integer getUserLoad() {
		return this.userLoad;
	}

	public void setUserLoad(Integer userLoad) {
		this.userLoad = userLoad;
	}

	@Column(name = "userLastTime", length = 19)
	public Timestamp getUserLastTime() {
		return this.userLastTime;
	}

	public void setUserLastTime(Timestamp userLastTime) {
		this.userLastTime = userLastTime;
	}

	@Column(name = "userState")
	public Integer getUserState() {
		return this.userState;
	}

	public void setUserState(Integer userState) {
		this.userState = userState;
	}

	@Column(name = "cms_del")
	public Integer getCmsDel() {
		return this.cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

}