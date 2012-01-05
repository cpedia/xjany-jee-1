package com.xjany.bbs.entity.base;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractBbsBbslink entity provides the base persistence definition of the
 * BbsBbslink entity. @author LiXiang
 */
@MappedSuperclass
public abstract class AbstractBbsBbslink implements java.io.Serializable {

	// Fields

	private Integer linkId;
	private String linkName;
	private String linkLogo;
	private String linkUrl;
	private Integer cmsDel;

	// Constructors

	/** default constructor */
	public AbstractBbsBbslink() {
	}

	/** minimal constructor */
	public AbstractBbsBbslink(String linkName, String linkLogo, String linkUrl) {
		this.linkName = linkName;
		this.linkLogo = linkLogo;
		this.linkUrl = linkUrl;
	}

	/** full constructor */
	public AbstractBbsBbslink(String linkName, String linkLogo, String linkUrl,
			Integer cmsDel) {
		this.linkName = linkName;
		this.linkLogo = linkLogo;
		this.linkUrl = linkUrl;
		this.cmsDel = cmsDel;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "linkId", unique = true, nullable = false)
	public Integer getLinkId() {
		return this.linkId;
	}

	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}

	@Column(name = "linkName", nullable = false, length = 50)
	public String getLinkName() {
		return this.linkName;
	}

	public void setLinkName(String linkName) {
		this.linkName = linkName;
	}

	@Column(name = "linkLogo", nullable = false, length = 50)
	public String getLinkLogo() {
		return this.linkLogo;
	}

	public void setLinkLogo(String linkLogo) {
		this.linkLogo = linkLogo;
	}

	@Column(name = "linkUrl", nullable = false, length = 50)
	public String getLinkUrl() {
		return this.linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	@Column(name = "cms_del")
	public Integer getCmsDel() {
		return this.cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

}