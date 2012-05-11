package com.xjany.bbs.entity.base;

import static javax.persistence.GenerationType.IDENTITY;


import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import com.xjany.bbs.entity.AllResLibCTG;

/**
 * AbstractAllUser entity provides the base persistence definition of the
 * AllUser entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractAllResLibrary implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//表字段
	private Integer libId; //主键
	private Integer parentId;
	private Integer isNote=0;//是否有叶子,.0没有,1有
	private String name;
	private Integer cmsDel = 0;
	private String cssName;
	private String cssId;
	private AllResLibCTG allResCtg;

	// Constructors

	/** default constructor */
	public AbstractAllResLibrary() {
	}

	/** minimal constructor */
	public AbstractAllResLibrary(String name) {
		this.name = name;
	}

	/** full constructor */
	public AbstractAllResLibrary(int libId, int parentId, String name, int cmsDel) {
		this.libId = libId;
		this.parentId = parentId;
		this.name = name;
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

	@Column(name = "cmsDel")
	public Integer getCmsDel() {
		return cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

	public String getCssName() {
		return cssName;
	}

	public void setCssName(String cssName) {
		this.cssName = cssName;
	}

	public String getCssId() {
		return cssId;
	}

	public void setCssId(String cssId) {
		this.cssId = cssId;
	}
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryId")
	public AllResLibCTG getAllResCtg() {
		return allResCtg;
	}

	public void setAllResCtg(AllResLibCTG allResCtg) {
		this.allResCtg = allResCtg;
	}

}