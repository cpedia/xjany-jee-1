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

import com.xjany.bbs.entity.BbsTopic;

/**
 * AbstractBbsReply entity provides the base persistence definition of the
 * BbsReply entity. @author LiXiang
 */
@MappedSuperclass
public abstract class AbstractBbsReply implements java.io.Serializable, AbstractGeneric {

	// Fields

	private Integer repId;
	private BbsTopic bbsTopic;
	private String repUser;
	private String repContent;
	private Timestamp repTime;
	private String repIp;
	private Integer cmsDel;

	// Constructors

	/** default constructor */
	public AbstractBbsReply() {
	}

	/** minimal constructor */
	public AbstractBbsReply(BbsTopic bbsTopic, String repUser,
			String repContent, Timestamp repTime, String repIp) {
		this.bbsTopic = bbsTopic;
		this.repUser = repUser;
		this.repContent = repContent;
		this.repTime = repTime;
		this.repIp = repIp;
	}

	/** full constructor */
	public AbstractBbsReply(BbsTopic bbsTopic, String repUser,
			String repContent, Timestamp repTime, String repIp, Integer cmsDel) {
		this.bbsTopic = bbsTopic;
		this.repUser = repUser;
		this.repContent = repContent;
		this.repTime = repTime;
		this.repIp = repIp;
		this.cmsDel = cmsDel;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "repId", unique = true, nullable = false)
	public Integer getRepId() {
		return this.repId;
	}

	public void setRepId(Integer repId) {
		this.repId = repId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "repTopicId", nullable = false)
	public BbsTopic getBbsTopic() {
		return this.bbsTopic;
	}

	public void setBbsTopic(BbsTopic bbsTopic) {
		this.bbsTopic = bbsTopic;
	}

	@Column(name = "repUser", nullable = false, length = 30)
	public String getRepUser() {
		return this.repUser;
	}

	public void setRepUser(String repUser) {
		this.repUser = repUser;
	}

	@Column(name = "repContent", nullable = false, length = 4000)
	public String getRepContent() {
		return this.repContent;
	}

	public void setRepContent(String repContent) {
		this.repContent = repContent;
	}

	@Column(name = "repTime", nullable = false, length = 19)
	public Timestamp getRepTime() {
		return this.repTime;
	}

	public void setRepTime(Timestamp repTime) {
		this.repTime = repTime;
	}

	@Column(name = "repIp", nullable = false, length = 30)
	public String getRepIp() {
		return this.repIp;
	}

	public void setRepIp(String repIp) {
		this.repIp = repIp;
	}

	@Column(name = "cms_del")
	public Integer getCmsDel() {
		return this.cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

}