package com.xjany.bbs.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.xjany.bbs.entity.base.AbstractResourceLibrary;

/**
 * AllUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "resource_lib", catalog = "cs_xjany")
public class ResourceLibrary extends AbstractResourceLibrary implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Constructors

	/** default constructor */
	public ResourceLibrary() {
	}

	/** minimal constructor */
	public ResourceLibrary(String name,int category) {
		super(name, category);
	}

	/** full constructor */
	public ResourceLibrary(int libId, int parentId, String name, int category, int cmsDel) {
		super(libId, parentId, name, category, cmsDel);
	}

}
