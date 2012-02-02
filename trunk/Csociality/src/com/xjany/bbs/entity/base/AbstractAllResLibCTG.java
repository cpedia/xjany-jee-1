package com.xjany.bbs.entity.base;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import com.xjany.bbs.entity.AllResLibrary;


/**
 * AbstractAllUser entity provides the base persistence definition of the
 * AllUser entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractAllResLibCTG implements java.io.Serializable {

	// Fields

	//表字段
	private Integer id; //主键
	private String name;
	private Integer cmsDel = 0;
	private String cssName;
	private String cssId;
	private Set<AllResLibrary> allResLibrary = new HashSet<AllResLibrary>(0);

	// Constructors

	/** default constructor */
	public AbstractAllResLibCTG() {
	}

	/** minimal constructor */
	public AbstractAllResLibCTG(String name,int category) {
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "allResLibrary")
	public Set<AllResLibrary> getAllResLibrary() {
		return allResLibrary;
	}

	public void setAllResLibrary(Set<AllResLibrary> allResLibrary) {
		this.allResLibrary = allResLibrary;
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
}