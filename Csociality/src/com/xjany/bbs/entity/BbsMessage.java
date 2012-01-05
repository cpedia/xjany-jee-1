package com.xjany.bbs.entity;

import java.sql.Timestamp;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractBbsMessage;

/**
 * BbsMessage entity. @author LiXiang
 */
@Entity
@Table(name = "bbs_message", catalog = "cs_xjany")
public class BbsMessage extends AbstractBbsMessage implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public BbsMessage() {
	}

	/** minimal constructor */
	public BbsMessage(String mesOwn, String mesTitle, String mesContent,
			String mesPost, Timestamp mesTime, Integer mesIsRead) {
		super(mesOwn, mesTitle, mesContent, mesPost, mesTime, mesIsRead);
	}

	/** full constructor */
	public BbsMessage(String mesOwn, String mesTitle, String mesContent,
			String mesPost, Timestamp mesTime, Integer mesIsRead, Integer cmsDel) {
		super(mesOwn, mesTitle, mesContent, mesPost, mesTime, mesIsRead, cmsDel);
	}

}
