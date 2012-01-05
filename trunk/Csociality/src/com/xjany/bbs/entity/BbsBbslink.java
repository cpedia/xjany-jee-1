package com.xjany.bbs.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractBbsBbslink;

/**
 * BbsBbslink entity. @author LiXiang
 */
@Entity
@Table(name = "bbs_bbslink", catalog = "cs_xjany")
public class BbsBbslink extends AbstractBbsBbslink implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public BbsBbslink() {
	}

	/** minimal constructor */
	public BbsBbslink(String linkName, String linkLogo, String linkUrl) {
		super(linkName, linkLogo, linkUrl);
	}

	/** full constructor */
	public BbsBbslink(String linkName, String linkLogo, String linkUrl,
			Integer cmsDel) {
		super(linkName, linkLogo, linkUrl, cmsDel);
	}

}
