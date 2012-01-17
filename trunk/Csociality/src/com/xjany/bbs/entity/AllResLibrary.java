package com.xjany.bbs.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractAllResLibrary;

/**
 * AllUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "all_res_lib", catalog = "cs_xjany")
public class AllResLibrary extends AbstractAllResLibrary implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructors

	/** default constructor */
	public AllResLibrary() {
	}

	/** minimal constructor */
	public AllResLibrary(String name,int category) {
		super(name, category);
	}

	/** full constructor */
	public AllResLibrary(int libId, int parentId, String name, int category, int cmsDel) {
		super(libId, parentId, name, category, cmsDel);
	}

}
