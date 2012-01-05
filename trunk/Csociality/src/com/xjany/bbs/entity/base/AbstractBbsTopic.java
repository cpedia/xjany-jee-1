package com.xjany.bbs.entity.base;

import java.sql.Timestamp;
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

import com.xjany.bbs.entity.BbsReply;
import com.xjany.bbs.entity.BbsSubBoard;

/**
 * AbstractBbsTopic entity provides the base persistence definition of the
 * BbsTopic entity. @author LiXiang
 */
@MappedSuperclass
public abstract class AbstractBbsTopic implements java.io.Serializable {

	// Fields

	private Integer topicId;
	private BbsSubBoard bbsSubBoard;
	private String topicTitle;
	private String topicContent;
	private String topicUser;
	private String topicIp;
	private Timestamp topicTime;
	private Integer topicHits;
	private Integer topicReply;
	private Integer isNews;
	private Integer topicElite;
	private Integer topicTop;
	private String topicLastUser;
	private Timestamp topicLastTime;
	private Integer cmsDel;
	private Set<BbsReply> bbsReplies = new HashSet<BbsReply>(0);

	// Constructors

	/** default constructor */
	public AbstractBbsTopic() {
	}

	/** minimal constructor */
	public AbstractBbsTopic(BbsSubBoard bbsSubBoard, String topicTitle,
			String topicContent, Timestamp topicTime, Timestamp topicLastTime) {
		this.bbsSubBoard = bbsSubBoard;
		this.topicTitle = topicTitle;
		this.topicContent = topicContent;
		this.topicTime = topicTime;
		this.topicLastTime = topicLastTime;
	}

	/** full constructor */
	public AbstractBbsTopic(BbsSubBoard bbsSubBoard, String topicTitle,
			String topicContent, String topicUser, String topicIp,
			Timestamp topicTime, Integer topicHits, Integer topicReply,
			Integer isNews, Integer topicElite, Integer topicTop,
			String topicLastUser, Timestamp topicLastTime, Integer cmsDel,
			Set<BbsReply> bbsReplies) {
		this.bbsSubBoard = bbsSubBoard;
		this.topicTitle = topicTitle;
		this.topicContent = topicContent;
		this.topicUser = topicUser;
		this.topicIp = topicIp;
		this.topicTime = topicTime;
		this.topicHits = topicHits;
		this.topicReply = topicReply;
		this.isNews = isNews;
		this.topicElite = topicElite;
		this.topicTop = topicTop;
		this.topicLastUser = topicLastUser;
		this.topicLastTime = topicLastTime;
		this.cmsDel = cmsDel;
		this.bbsReplies = bbsReplies;
	}

	// Property accessors
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "topicId", unique = true, nullable = false)
	public Integer getTopicId() {
		return this.topicId;
	}

	public void setTopicId(Integer topicId) {
		this.topicId = topicId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "topicSubId", nullable = false)
	public BbsSubBoard getBbsSubBoard() {
		return this.bbsSubBoard;
	}

	public void setBbsSubBoard(BbsSubBoard bbsSubBoard) {
		this.bbsSubBoard = bbsSubBoard;
	}

	@Column(name = "topicTitle", nullable = false, length = 50)
	public String getTopicTitle() {
		return this.topicTitle;
	}

	public void setTopicTitle(String topicTitle) {
		this.topicTitle = topicTitle;
	}

	@Column(name = "topicContent", nullable = false, length = 4000)
	public String getTopicContent() {
		return this.topicContent;
	}

	public void setTopicContent(String topicContent) {
		this.topicContent = topicContent;
	}

	@Column(name = "topicUser", length = 30)
	public String getTopicUser() {
		return this.topicUser;
	}

	public void setTopicUser(String topicUser) {
		this.topicUser = topicUser;
	}

	@Column(name = "topicIp", length = 30)
	public String getTopicIp() {
		return this.topicIp;
	}

	public void setTopicIp(String topicIp) {
		this.topicIp = topicIp;
	}

	@Column(name = "topicTime", nullable = false, length = 19)
	public Timestamp getTopicTime() {
		return this.topicTime;
	}

	public void setTopicTime(Timestamp topicTime) {
		this.topicTime = topicTime;
	}

	@Column(name = "topicHits")
	public Integer getTopicHits() {
		return this.topicHits;
	}

	public void setTopicHits(Integer topicHits) {
		this.topicHits = topicHits;
	}

	@Column(name = "topicReply")
	public Integer getTopicReply() {
		return this.topicReply;
	}

	public void setTopicReply(Integer topicReply) {
		this.topicReply = topicReply;
	}

	@Column(name = "isNews")
	public Integer getIsNews() {
		return this.isNews;
	}

	public void setIsNews(Integer isNews) {
		this.isNews = isNews;
	}

	@Column(name = "topicElite")
	public Integer getTopicElite() {
		return this.topicElite;
	}

	public void setTopicElite(Integer topicElite) {
		this.topicElite = topicElite;
	}

	@Column(name = "topicTop")
	public Integer getTopicTop() {
		return this.topicTop;
	}

	public void setTopicTop(Integer topicTop) {
		this.topicTop = topicTop;
	}

	@Column(name = "topicLastUser", length = 30)
	public String getTopicLastUser() {
		return this.topicLastUser;
	}

	public void setTopicLastUser(String topicLastUser) {
		this.topicLastUser = topicLastUser;
	}

	@Column(name = "topicLastTime", nullable = false, length = 19)
	public Timestamp getTopicLastTime() {
		return this.topicLastTime;
	}

	public void setTopicLastTime(Timestamp topicLastTime) {
		this.topicLastTime = topicLastTime;
	}

	@Column(name = "cms_del")
	public Integer getCmsDel() {
		return this.cmsDel;
	}

	public void setCmsDel(Integer cmsDel) {
		this.cmsDel = cmsDel;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "bbsTopic")
	public Set<BbsReply> getBbsReplies() {
		return this.bbsReplies;
	}

	public void setBbsReplies(Set<BbsReply> bbsReplies) {
		this.bbsReplies = bbsReplies;
	}

}