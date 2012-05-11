package com.xjany.bbs.entity.base;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import com.xjany.bbs.entity.XiJieTemplate;


/**
 * AbstractAllUser entity provides the base persistence definition of the
 * AllUser entity. @author MyEclipse Persistence Tools
 */
@MappedSuperclass
public abstract class AbstractXiJieStyle implements java.io.Serializable {

	// Fields

	//表字段
	private Integer id; //主键
	private String name;
	private String code;
	private String type;
	private String path;
	private XiJieTemplate xiJieTemplate;
	private Integer cmsDel = 0;

	// Constructors

	/** default constructor */
	public AbstractXiJieStyle() {
	}

	/** minimal constructor */
	public AbstractXiJieStyle(String name,int category) {
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
	@Column(name = "code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	@Column(name = "type")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	@Column(name = "path")
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	@OneToOne(mappedBy="xiJieStyle")
	public XiJieTemplate getXiJieTemplate() {
		return xiJieTemplate;
	}

	public void setXiJieTemplate(XiJieTemplate xiJieTemplate) {
		this.xiJieTemplate = xiJieTemplate;
	}
	
}