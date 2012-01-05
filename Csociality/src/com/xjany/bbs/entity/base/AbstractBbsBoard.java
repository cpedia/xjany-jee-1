package com.xjany.bbs.entity.base;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import com.xjany.bbs.entity.BbsSubBoard;

/**
 * AbstractBbsBoard entity provides the base persistence definition of the
 * BbsBoard entity. @author LiXiang
 */
@MappedSuperclass
public abstract class AbstractBbsBoard implements java.io.Serializable {

	// Fields

	private Integer boaId;
	private String boaName;
	private String boaMaster;
	private Integer cmsDel;
	private Set<BbsSubBoard> bbsSubBoards = new HashSet<BbsSubBoard>(0);

	// Constructors

	/** default constructor */
	public AbstractBbsBoard() {
	}

	/** minimal constructor */
	public AbstractBbsBoard(String boaName) {
		this.boaName = boaName;
	}

	/** full constructor */
	public AbstractBbsBoard(String boaName, String boaMaster, Integer cmsDel,
			Set<BbsSubBoard> bbsSubBoards) {
		this.boaName = boaName;
		this.boaMaster = boaMaster;
		this.cmsDel = cmsDel;
		this.bbsSubBoards = bbsSubBoards;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "boaId", unique = true, nullable = false)
	public Integer getBoaId() {
		return this.boaId;
	}

	public void setBoaId(Integer boaId) {
		this.boaId = boaId;
	}

	@Column(name = "boaName", nullable = false, length = 30)
	public String getBoaName() {
		return this.boaName;
	}

	public void setBoaName(String boaName) {
		this.boaName = boaName;
	}

	@Column(name = "boaMaster", length = 20)
	public String getBoaMaster() {
		return this.boaMaster;
	}

	public void setBoaMaster(String boaMaster) {
		this.boaMaster = boaMaster;
	}

	@Column(name = "cms_del")
	public Integer getCmsDel() {
		return this.cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bbsBoard")
	public Set<BbsSubBoard> getBbsSubBoards() {
		return this.bbsSubBoards;
	}

	public void setBbsSubBoards(Set<BbsSubBoard> bbsSubBoards) {
		this.bbsSubBoards = bbsSubBoards;
	}

}