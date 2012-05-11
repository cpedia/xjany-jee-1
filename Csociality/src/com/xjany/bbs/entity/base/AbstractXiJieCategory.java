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

import com.xjany.bbs.entity.XiJieTemplate;


/**
 * AbstractAllUser entity provides the base persistence definition of the
 * AllUser entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractXiJieCategory implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//表字段
	private Integer id; //主键
	private String name;
	private Integer cmsDel = 0;
	private Set<XiJieTemplate> xiJieTemplates = new HashSet<XiJieTemplate>(0);

	// Constructors

	/** default constructor */
	public AbstractXiJieCategory() {
	}

	/** minimal constructor */
	public AbstractXiJieCategory(String name,int category) {
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
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "xiJieCategory")
	public Set<XiJieTemplate> getXiJieTemplates() {
		return xiJieTemplates;
	}
	
	public void setXiJieTemplates(Set<XiJieTemplate> xiJieTemplates) {
		this.xiJieTemplates = xiJieTemplates;
	}
}