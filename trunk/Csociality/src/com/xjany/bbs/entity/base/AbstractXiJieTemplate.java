package com.xjany.bbs.entity.base;

import static javax.persistence.GenerationType.IDENTITY;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import com.xjany.bbs.entity.XiJieCategory;
import com.xjany.bbs.entity.XiJieStyle;


/**
 * AbstractAllUser entity provides the base persistence definition of the
 * AllUser entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractXiJieTemplate implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//表字段
	private Integer id; //主键
	private String name;
	private XiJieCategory xiJieCategory;
	private XiJieStyle xiJieStyle;
	private Integer cmsDel = 0;

	// Constructors

	/** default constructor */
	public AbstractXiJieTemplate() {
	}

	/** minimal constructor */
	public AbstractXiJieTemplate(String name,int category) {
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryId")
	public XiJieCategory getXiJieCategory() {
		return xiJieCategory;
	}

	public void setXiJieCategory(XiJieCategory xiJieCategory) {
		this.xiJieCategory = xiJieCategory;
	}
	
	
	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="styleId")
	public XiJieStyle getXiJieStyle() {
		return xiJieStyle;
	}

	public void setXiJieStyle(XiJieStyle xiJieStyle) {
		this.xiJieStyle = xiJieStyle;
	}

}