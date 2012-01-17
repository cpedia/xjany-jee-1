package com.xjany.bbs.entity.base;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


/**
 * AbstractAllUser entity provides the base persistence definition of the
 * AllUser entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractAllResLibrary implements java.io.Serializable {

	// Fields

	private int libId; //主键
	private int parentId;
	private String name;
	private int category;
	private int cmsDel = 0;

	// Constructors

	/** default constructor */
	public AbstractAllResLibrary() {
	}

	/** minimal constructor */
	public AbstractAllResLibrary(String name,int category) {
		this.name = name;
		this.category = category;
	}

	/** full constructor */
	public AbstractAllResLibrary(int libId, int parentId, String name, int category, int cmsDel) {
		this.libId = libId;
		this.parentId = parentId;
		this.name = name;
		this.category = category;
		this.cmsDel = cmsDel;
	}
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "libId", unique = true, nullable = false)
	public int getLibId() {
		return libId;
	}

	public void setLibId(int libId) {
		this.libId = libId;
	}
	@Column(name = "bbs_parentId")
	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	@Column(name = "bbs_name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "bbs_category", unique = true)
	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}
	@Column(name = "cmsDel")
	public int getCmsDel() {
		return cmsDel;
	}

	public void setCmsDel(int cmsDel) {
		this.cmsDel = cmsDel;
	}

}