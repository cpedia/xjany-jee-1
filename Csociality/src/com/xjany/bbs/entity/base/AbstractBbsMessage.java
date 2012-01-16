package com.xjany.bbs.entity.base;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.xjany.bbs.entity.InterGeneric;

/**
 * AbstractBbsMessage entity provides the base persistence definition of the
 * BbsMessage entity. @author LiXiang
 */
@MappedSuperclass
public abstract class AbstractBbsMessage implements java.io.Serializable, InterGeneric {

	// Fields

	private Integer mesId;
	private String mesOwn;
	private String mesTitle;
	private String mesContent;
	private String mesPost;
	private Timestamp mesTime;
	private Integer mesIsRead;
	private Integer cmsDel = 0;

	// Constructors

	/** default constructor */
	public AbstractBbsMessage() {
	}

	/** minimal constructor */
	public AbstractBbsMessage(String mesOwn, String mesTitle,
			String mesContent, String mesPost, Timestamp mesTime,
			Integer mesIsRead) {
		this.mesOwn = mesOwn;
		this.mesTitle = mesTitle;
		this.mesContent = mesContent;
		this.mesPost = mesPost;
		this.mesTime = mesTime;
		this.mesIsRead = mesIsRead;
	}

	/** full constructor */
	public AbstractBbsMessage(String mesOwn, String mesTitle,
			String mesContent, String mesPost, Timestamp mesTime,
			Integer mesIsRead, Integer cmsDel) {
		this.mesOwn = mesOwn;
		this.mesTitle = mesTitle;
		this.mesContent = mesContent;
		this.mesPost = mesPost;
		this.mesTime = mesTime;
		this.mesIsRead = mesIsRead;
		this.cmsDel = cmsDel;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "mesId", unique = true, nullable = false)
	public Integer getMesId() {
		return this.mesId;
	}

	public void setMesId(Integer mesId) {
		this.mesId = mesId;
	}

	@Column(name = "mesOwn", nullable = false, length = 30)
	public String getMesOwn() {
		return this.mesOwn;
	}

	public void setMesOwn(String mesOwn) {
		this.mesOwn = mesOwn;
	}

	@Column(name = "mesTitle", nullable = false, length = 100)
	public String getMesTitle() {
		return this.mesTitle;
	}

	public void setMesTitle(String mesTitle) {
		this.mesTitle = mesTitle;
	}

	@Column(name = "mesContent", nullable = false, length = 500)
	public String getMesContent() {
		return this.mesContent;
	}

	public void setMesContent(String mesContent) {
		this.mesContent = mesContent;
	}

	@Column(name = "mesPost", nullable = false, length = 30)
	public String getMesPost() {
		return this.mesPost;
	}

	public void setMesPost(String mesPost) {
		this.mesPost = mesPost;
	}

	@Column(name = "mesTime", nullable = false, length = 19)
	public Timestamp getMesTime() {
		return this.mesTime;
	}

	public void setMesTime(Timestamp mesTime) {
		this.mesTime = mesTime;
	}

	@Column(name = "mesIsRead", nullable = false)
	public Integer getMesIsRead() {
		return this.mesIsRead;
	}

	public void setMesIsRead(Integer mesIsRead) {
		this.mesIsRead = mesIsRead;
	}

	@Column(name = "cms_del")
	public Integer getCmsDel() {
		return this.cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

}