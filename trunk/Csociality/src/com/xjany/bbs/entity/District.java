package com.xjany.bbs.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractDistrict;

/**
 * District entity. @author LiXiang
 */
@Entity
@Table(name = "district", catalog = "cs_xjany")
public class District extends AbstractDistrict implements java.io.Serializable {

	// Constructors

	/** default constructor */
	public District() {
	}

	/** full constructor */
	public District(String name, String province, Integer cmsDel) {
		super(name, province, cmsDel);
	}

}
