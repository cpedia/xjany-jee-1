package com.xjany.bbs.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractXiJieCategory;

/**
 * AllUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "xijie_category", catalog = "cs_xjany")
public class XiJieCategory extends AbstractXiJieCategory implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructors

	/** default constructor */
	public XiJieCategory() {
	}

	/** minimal constructor */
	public XiJieCategory(String name,int category) {
		super(name, category);
	}

}
