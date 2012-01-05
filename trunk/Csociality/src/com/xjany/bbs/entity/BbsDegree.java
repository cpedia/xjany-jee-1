package com.xjany.bbs.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractBbsDegree;

/**
 * BbsDegree entity. @author LiXiang
 */
@Entity
@Table(name = "bbs_degree", catalog = "cs_xjany")
public class BbsDegree extends AbstractBbsDegree implements
		java.io.Serializable {

	// Constructors

	/** default constructor */
	public BbsDegree() {
	}

	/** full constructor */
	public BbsDegree(String degreeName, Integer cmsDel) {
		super(degreeName, cmsDel);
	}

}
