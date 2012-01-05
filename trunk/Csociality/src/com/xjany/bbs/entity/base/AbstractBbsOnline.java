package com.xjany.bbs.entity.base;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.xjany.bbs.entity.InterGeneric;

/**
 * AbstractBbsOnline entity provides the base persistence definition of the
 * BbsOnline entity. @author LiXiang
 */
@MappedSuperclass
public abstract class AbstractBbsOnline implements java.io.Serializable, InterGeneric {

	// Fields

	private Integer onId;
	private String onName;
	private String onRole;
	private String onIp;
	private String onBrowser;
	private String onLocate;
	private Timestamp onTime;
	private Integer cmsDel;

	// Constructors

	/** default constructor */
	public AbstractBbsOnline() {
	}

	/** minimal constructor */
	public AbstractBbsOnline(String onName, String onRole, String onIp,
			String onBrowser, String onLocate, Timestamp onTime) {
		this.onName = onName;
		this.onRole = onRole;
		this.onIp = onIp;
		this.onBrowser = onBrowser;
		this.onLocate = onLocate;
		this.onTime = onTime;
	}

	/** full constructor */
	public AbstractBbsOnline(String onName, String onRole, String onIp,
			String onBrowser, String onLocate, Timestamp onTime, Integer cmsDel) {
		this.onName = onName;
		this.onRole = onRole;
		this.onIp = onIp;
		this.onBrowser = onBrowser;
		this.onLocate = onLocate;
		this.onTime = onTime;
		this.cmsDel = cmsDel;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "onId", unique = true, nullable = false)
	public Integer getOnId() {
		return this.onId;
	}

	public void setOnId(Integer onId) {
		this.onId = onId;
	}

	@Column(name = "onName", nullable = false, length = 30)
	public String getOnName() {
		return this.onName;
	}

	public void setOnName(String onName) {
		this.onName = onName;
	}

	@Column(name = "onRole", nullable = false, length = 50)
	public String getOnRole() {
		return this.onRole;
	}

	public void setOnRole(String onRole) {
		this.onRole = onRole;
	}

	@Column(name = "onIp", nullable = false, length = 30)
	public String getOnIp() {
		return this.onIp;
	}

	public void setOnIp(String onIp) {
		this.onIp = onIp;
	}

	@Column(name = "onBrowser", nullable = false, length = 30)
	public String getOnBrowser() {
		return this.onBrowser;
	}

	public void setOnBrowser(String onBrowser) {
		this.onBrowser = onBrowser;
	}

	@Column(name = "onLocate", nullable = false, length = 100)
	public String getOnLocate() {
		return this.onLocate;
	}

	public void setOnLocate(String onLocate) {
		this.onLocate = onLocate;
	}

	@Column(name = "onTime", nullable = false, length = 19)
	public Timestamp getOnTime() {
		return this.onTime;
	}

	public void setOnTime(Timestamp onTime) {
		this.onTime = onTime;
	}

	@Column(name = "cms_del")
	public Integer getCmsDel() {
		return this.cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

}