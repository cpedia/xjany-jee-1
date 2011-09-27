package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseJforumPost implements Serializable {
	private static final long serialVersionUID = 234131L;
	private Integer PostID;
	private Integer TopicID;
	private Integer ForumID;
	public Integer getForumID() {
		return ForumID;
	}
	public void setForumID(Integer forumID) {
		ForumID = forumID;
	}
	private Integer UserID;
	private Date PostTime;
	private String PosterIP;
	private Boolean EnableBBCode;
	private Boolean EnableHtml;
	private Boolean EnableSmilies;
	private Boolean EnableSig;
	private Date PostEditTime;
	private Integer PostEditCount;
	private Boolean Status;
	private Boolean Attach;
	public Boolean getAttach() {
		return Attach;
	}
	public void setAttach(Boolean attach) {
		Attach = attach;
	}
	private Boolean NeedModerate;
	private String PlanID;
	public Integer getPostID() {
		return PostID;
	}
	public void setPostID(Integer postID) {
		PostID = postID;
	}
	public Integer getTopicID() {
		return TopicID;
	}
	public void setTopicID(Integer topicID) {
		TopicID = topicID;
	}
	public Integer getUserID() {
		return UserID;
	}
	public void setUserID(Integer userID) {
		UserID = userID;
	}
	public Date getPostTime() {
		return PostTime;
	}
	public void setPostTime(Date postTime) {
		PostTime = postTime;
	}
	public String getPosterIP() {
		return PosterIP;
	}
	public void setPosterIP(String posterIP) {
		PosterIP = posterIP;
	}
	public Boolean getEnableBBCode() {
		return EnableBBCode;
	}
	public void setEnableBBCode(Boolean enableBBCode) {
		EnableBBCode = enableBBCode;
	}
	public Boolean getEnableHtml() {
		return EnableHtml;
	}
	public void setEnableHtml(Boolean enableHtml) {
		EnableHtml = enableHtml;
	}
	public Boolean getEnableSmilies() {
		return EnableSmilies;
	}
	public void setEnableSmilies(Boolean enableSmilies) {
		EnableSmilies = enableSmilies;
	}
	public Boolean getEnableSig() {
		return EnableSig;
	}
	public void setEnableSig(Boolean enableSig) {
		EnableSig = enableSig;
	}
	public Date getPostEditTime() {
		return PostEditTime;
	}
	public void setPostEditTime(Date postEditTime) {
		PostEditTime = postEditTime;
	}
	public Integer getPostEditCount() {
		return PostEditCount;
	}
	public void setPostEditCount(Integer postEditCount) {
		PostEditCount = postEditCount;
	}
	public Boolean getStatus() {
		return Status;
	}
	public void setStatus(Boolean status) {
		Status = status;
	}
	public Boolean getNeedModerate() {
		return NeedModerate;
	}
	public void setNeedModerate(Boolean needModerate) {
		NeedModerate = needModerate;
	}
	public String getPlanID() {
		return PlanID;
	}
	public void setPlanID(String planID) {
		PlanID = planID;
	}

}
