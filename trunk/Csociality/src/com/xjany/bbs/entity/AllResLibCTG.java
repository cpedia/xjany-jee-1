package com.xjany.bbs.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractAllResLibCTG;

/**
 * AllUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "all_res_lib_ctg", catalog = "cs_xjany")
public class AllResLibCTG extends AbstractAllResLibCTG implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructors

	/** default constructor */
	public AllResLibCTG() {
	}

	/** minimal constructor */
	public AllResLibCTG(String name,int category) {
		super(name, category);
	}

}
