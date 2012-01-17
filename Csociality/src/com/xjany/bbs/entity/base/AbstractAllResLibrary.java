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

	private Integer libId; //主键
	private Integer parentId;
	private Integer isNote=0;//是否有叶子,.0没有,1有
	private String name;
	private Integer category;
	private Integer cmsDel = 0;

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
	public Integer getLibId() {
		return libId;
	}

	public void setLibId(Integer libId) {
		this.libId = libId;
	}
	
	@Column(name = "parentId")
	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Column(name = "isNote")
	public Integer getIsNote() {
		return isNote;
	}

	public void setIsNote(Integer isNote) {
		this.isNote = isNote;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "category")
	public Integer getCategory() {
		return category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	@Column(name = "cmsDel")
	public Integer getCmsDel() {
		return cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}
}