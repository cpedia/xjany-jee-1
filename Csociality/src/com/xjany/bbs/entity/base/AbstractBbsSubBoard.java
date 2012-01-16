package com.xjany.bbs.entity.base;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

import com.xjany.bbs.entity.BbsBoard;
import com.xjany.bbs.entity.BbsTopic;
import com.xjany.bbs.entity.InterGeneric;

/**
 * AbstractBbsSubBoard entity provides the base persistence definition of the
 * BbsSubBoard entity. @author LiXiang
 */
@MappedSuperclass
public abstract class AbstractBbsSubBoard implements java.io.Serializable, InterGeneric {

	// Fields

	private Integer subId;
	private BbsBoard bbsBoard;
	private String subName;
	private String subImg;
	private String subInfo;
	private String subMaster;
	private Integer subTopicNum;
	private Integer subReNum;
	private String subLastUser;
	private String subLastTopic;
	private Integer cmsDel = 0;
	private Set<BbsTopic> bbsTopics = new HashSet<BbsTopic>(0);

	// Constructors

	/** default constructor */
	public AbstractBbsSubBoard() {
	}

	/** minimal constructor */
	public AbstractBbsSubBoard(String subName) {
		this.subName = subName;
	}

	/** full constructor */
	public AbstractBbsSubBoard(BbsBoard bbsBoard, String subName,
			String subImg, String subInfo, String subMaster,
			Integer subTopicNum, Integer subReNum, String subLastUser,
			String subLastTopic, Integer cmsDel, Set<BbsTopic> bbsTopics) {
		this.bbsBoard = bbsBoard;
		this.subName = subName;
		this.subImg = subImg;
		this.subInfo = subInfo;
		this.subMaster = subMaster;
		this.subTopicNum = subTopicNum;
		this.subReNum = subReNum;
		this.subLastUser = subLastUser;
		this.subLastTopic = subLastTopic;
		this.cmsDel = cmsDel;
		this.bbsTopics = bbsTopics;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "subId", unique = true, nullable = false)
	public Integer getSubId() {
		return this.subId;
	}

	public void setSubId(Integer subId) {
		this.subId = subId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "subBoaId")
	public BbsBoard getBbsBoard() {
		return this.bbsBoard;
	}

	public void setBbsBoard(BbsBoard bbsBoard) {
		this.bbsBoard = bbsBoard;
	}

	@Column(name = "subName", nullable = false, length = 30)
	public String getSubName() {
		return this.subName;
	}

	public void setSubName(String subName) {
		this.subName = subName;
	}

	@Column(name = "subImg", length = 20)
	public String getSubImg() {
		return this.subImg;
	}

	public void setSubImg(String subImg) {
		this.subImg = subImg;
	}

	@Column(name = "subInfo", length = 200)
	public String getSubInfo() {
		return this.subInfo;
	}

	public void setSubInfo(String subInfo) {
		this.subInfo = subInfo;
	}

	@Column(name = "subMaster", length = 20)
	public String getSubMaster() {
		return this.subMaster;
	}

	public void setSubMaster(String subMaster) {
		this.subMaster = subMaster;
	}

	@Column(name = "subTopicNum")
	public Integer getSubTopicNum() {
		return this.subTopicNum;
	}

	public void setSubTopicNum(Integer subTopicNum) {
		this.subTopicNum = subTopicNum;
	}

	@Column(name = "subReNum")
	public Integer getSubReNum() {
		return this.subReNum;
	}

	public void setSubReNum(Integer subReNum) {
		this.subReNum = subReNum;
	}

	@Column(name = "subLastUser", length = 20)
	public String getSubLastUser() {
		return this.subLastUser;
	}

	public void setSubLastUser(String subLastUser) {
		this.subLastUser = subLastUser;
	}

	@Column(name = "subLastTopic", length = 50)
	public String getSubLastTopic() {
		return this.subLastTopic;
	}

	public void setSubLastTopic(String subLastTopic) {
		this.subLastTopic = subLastTopic;
	}

	@Column(name = "cms_del")
	public Integer getCmsDel() {
		return this.cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bbsSubBoard")
	public Set<BbsTopic> getBbsTopics() {
		return this.bbsTopics;
	}

	public void setBbsTopics(Set<BbsTopic> bbsTopics) {
		this.bbsTopics = bbsTopics;
	}

}