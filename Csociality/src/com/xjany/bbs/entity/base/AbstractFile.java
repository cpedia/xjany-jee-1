package com.xjany.bbs.entity.base;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * AbstractFile entity provides the base persistence definition of the File
 * entity. @author LiXiang
 */
@MappedSuperclass
public abstract class AbstractFile implements java.io.Serializable, AbstractGeneric {

	// Fields

	private Integer id;
	private Integer downId;
	private String name;
	private String path;
	private Integer upId;
	private Integer cmsDel;

	// Constructors

	/** default constructor */
	public AbstractFile() {
	}

	/** full constructor */
	public AbstractFile(Integer downId, String name, String path, Integer upId,
			Integer cmsDel) {
		this.downId = downId;
		this.name = name;
		this.path = path;
		this.upId = upId;
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

	@Column(name = "downId")
	public Integer getDownId() {
		return this.downId;
	}

	public void setDownId(Integer downId) {
		this.downId = downId;
	}

	@Column(name = "name", length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "path", length = 10)
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Column(name = "upId")
	public Integer getUpId() {
		return this.upId;
	}

	public void setUpId(Integer upId) {
		this.upId = upId;
	}

	@Column(name = "cms_del")
	public Integer getCmsDel() {
		return this.cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

}