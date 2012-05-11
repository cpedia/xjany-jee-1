package com.xjany.bbs.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractXiJieStyle;

/**
 * AllUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "xijie_style", catalog = "cs_xjany")
public class XiJieStyle extends AbstractXiJieStyle implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructors

	/** default constructor */
	public XiJieStyle() {
	}

	/** minimal constructor */
	public XiJieStyle(String name,int category) {
		super(name, category);
	}

}
