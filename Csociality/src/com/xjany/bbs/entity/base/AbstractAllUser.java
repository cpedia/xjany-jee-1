package com.xjany.bbs.entity.base;

import java.sql.Timestamp;
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

import com.xjany.bbs.entity.AllUserGroup;
import com.xjany.bbs.entity.BbsUserProfile;

/**
 * AbstractAllUser entity provides the base persistence definition of the
 * AllUser entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractAllUser implements java.io.Serializable {

	// Fields
//	   userId               int not null auto_increment comment '用户ID',
//	   cms_userId           int,
//	   bbs_userId           int,
//	   userName             varchar(30) not null comment '用户名',
//	   userPsw              varchar(30) not null comment '密 码',
//	   groupId              int comment '组ID',
//	   userSex              int default 1 comment '用户性别(0、男 1、女)',
//	   userEmail            varchar(50) comment '用户E-mail',
//	   userRegTime          datetime comment '注册时间',
//	   userIp               varchar(30) comment '注册IP',
//	   userLoad             int default 0 comment '用户登陆次数',
//	   userLastTime         datetime comment '最后登陆时间',
//	   userState            int default 1 comment '用户状态 0、离线 1、在线',
//	   cms_del              int(2) default 0,
//	   primary key (userId)
	
	
	private Integer userId;
	private AllUserGroup allUserGroup;
	private BbsUserProfile bbsUserProfile;
	private Integer cmsUserId;
	private String userName;
	private String userPsw;
	private Integer userSex;
	private String userEmail;
	private Timestamp userRegTime;
	private String userIp;
	private Integer userLoad;
	private Timestamp userLastTime;
	private Integer userState;
	private Integer cmsDel;
	private Set<BbsUserProfile> bbsUserProfiles = new HashSet<BbsUserProfile>(0);

	// Constructors

	/** default constructor */
	public AbstractAllUser() {
	}

	/** minimal constructor */
	public AbstractAllUser(String userName, String userPsw, Integer userSex,
			String userEmail, Timestamp userRegTime, String userIp,
			Integer userLoad, Timestamp userLastTime, Integer userState) {
		this.userName = userName;
		this.userPsw = userPsw;
		this.userSex = userSex;
		this.userEmail = userEmail;
		this.userRegTime = userRegTime;
		this.userIp = userIp;
		this.userLoad = userLoad;
		this.userLastTime = userLastTime;
		this.userState = userState;
	}

	/** full constructor */
	public AbstractAllUser(AllUserGroup allUserGroup,
			BbsUserProfile bbsUserProfile, Integer cmsUserId, String userName,
			String userPsw, Integer userSex, String userEmail,
			Timestamp userRegTime, String userIp, Integer userLoad,
			Timestamp userLastTime, Integer userState, Integer cmsDel,
			Set<BbsUserProfile> bbsUserProfiles) {
		this.allUserGroup = allUserGroup;
		this.bbsUserProfile = bbsUserProfile;
		this.cmsUserId = cmsUserId;
		this.userName = userName;
		this.userPsw = userPsw;
		this.userSex = userSex;
		this.userEmail = userEmail;
		this.userRegTime = userRegTime;
		this.userIp = userIp;
		this.userLoad = userLoad;
		this.userLastTime = userLastTime;
		this.userState = userState;
		this.cmsDel = cmsDel;
		this.bbsUserProfiles = bbsUserProfiles;
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

	@ManyToOne(fetch = FetchType.LAZY)
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

	@Column(name = "userPsw", nullable = false, length = 30)
	public String getUserPsw() {
		return this.userPsw;
	}

	public void setUserPsw(String userPsw) {
		this.userPsw = userPsw;
	}

	@Column(name = "userSex", nullable = false)
	public Integer getUserSex() {
		return this.userSex;
	}

	public void setUserSex(Integer userSex) {
		this.userSex = userSex;
	}

	@Column(name = "userEmail", nullable = false, length = 50)
	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	@Column(name = "userRegTime", nullable = false, length = 19)
	public Timestamp getUserRegTime() {
		return this.userRegTime;
	}

	public void setUserRegTime(Timestamp userRegTime) {
		this.userRegTime = userRegTime;
	}

	@Column(name = "userIp", nullable = false, length = 30)
	public String getUserIp() {
		return this.userIp;
	}

	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}

	@Column(name = "userLoad", nullable = false)
	public Integer getUserLoad() {
		return this.userLoad;
	}

	public void setUserLoad(Integer userLoad) {
		this.userLoad = userLoad;
	}

	@Column(name = "userLastTime", nullable = false, length = 19)
	public Timestamp getUserLastTime() {
		return this.userLastTime;
	}

	public void setUserLastTime(Timestamp userLastTime) {
		this.userLastTime = userLastTime;
	}

	@Column(name = "userState", nullable = false)
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

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "allUser")
	public Set<BbsUserProfile> getBbsUserProfiles() {
		return this.bbsUserProfiles;
	}

	public void setBbsUserProfiles(Set<BbsUserProfile> bbsUserProfiles) {
		this.bbsUserProfiles = bbsUserProfiles;
	}

}