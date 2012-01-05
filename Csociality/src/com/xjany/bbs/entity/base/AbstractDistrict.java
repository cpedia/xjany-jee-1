package com.xjany.bbs.entity.base;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractDistrict entity provides the base persistence definition of the
 * District entity. @author LiXiang
 */
@MappedSuperclass
public abstract class AbstractDistrict implements java.io.Serializable, AbstractGeneric {

	// Fields

	private Integer id;
	private String name;
	private String province;
	private Integer cmsDel;

	// Constructors

	/** default constructor */
	public AbstractDistrict() {
	}

	/** full constructor */
	public AbstractDistrict(String name, String province, Integer cmsDel) {
		this.name = name;
		this.province = province;
		this.cmsDel = cmsDel;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "name", length = 10)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "province", length = 10)
	public String getProvince() {
		return this.province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "cms_del")
	public Integer getCmsDel() {
		return this.cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

}