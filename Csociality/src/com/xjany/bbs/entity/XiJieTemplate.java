package com.xjany.bbs.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractXiJieTemplate;

/**
 * AllUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "xijie_template", catalog = "cs_xjany")
public class XiJieTemplate extends AbstractXiJieTemplate implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructors

	/** default constructor */
	public XiJieTemplate() {
	}

	/** minimal constructor */
	public XiJieTemplate(String name,int category) {
		super(name, category);
	}

}
