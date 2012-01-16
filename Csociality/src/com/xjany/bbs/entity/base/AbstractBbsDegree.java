package com.xjany.bbs.entity.base;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.xjany.bbs.entity.InterGeneric;

/**
 * AbstractBbsDegree entity provides the base persistence definition of the
 * BbsDegree entity. @author LiXiang
 */
@MappedSuperclass
public abstract class AbstractBbsDegree implements java.io.Serializable, InterGeneric {

	// Fields

	private Integer degreeId;
	private String degreeName;
	private Integer cmsDel = 0;

	// Constructors

	/** default constructor */
	public AbstractBbsDegree() {
	}

	/** full constructor */
	public AbstractBbsDegree(String degreeName, Integer cmsDel) {
		this.degreeName = degreeName;
		this.cmsDel = cmsDel;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "degreeId", unique = true, nullable = false)
	public Integer getDegreeId() {
		return this.degreeId;
	}

	public void setDegreeId(Integer degreeId) {
		this.degreeId = degreeId;
	}

	@Column(name = "degreeName", length = 50)
	public String getDegreeName() {
		return this.degreeName;
	}

	public void setDegreeName(String degreeName) {
		this.degreeName = degreeName;
	}

	@Column(name = "cms_del")
	public Integer getCmsDel() {
		return this.cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

}