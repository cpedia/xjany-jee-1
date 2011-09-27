package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import com.lti.system.Configuration;

public abstract class BaseStrategy implements Serializable{

	private static final long serialVersionUID = -7207552240379514182L;

	protected java.lang.Long ID;

	protected java.lang.String Name;
	
	protected java.lang.String UserName;
	
	public java.lang.String getUserName() {
		return UserName;
	}
	public void setUserName(java.lang.String userName) {
		UserName = userName;
	}
	protected java.lang.Long StrategyClassID;
	protected java.lang.String Ticker;
	
	protected java.lang.String Description;
	
	protected java.lang.String Reference;
	
	protected java.lang.Long UserID;
	
	protected Map<String, String> Attributes;
	
	protected java.lang.Long PostID;
	
	protected java.lang.Long ForumID;
	
	
	protected Date CreatedDate;
	
	protected String UserContent;
	
	
	protected String ThirdParty;
	
	
	
	
	public String getThirdParty() {
		return ThirdParty;
	}
	public void setThirdParty(String thirdParty) {
		ThirdParty = thirdParty;
	}
	public String getUserContent() {
		return UserContent;
	}
	public void setUserContent(String userContent) {
		UserContent = userContent;
	}
	public Date getCreatedDate() {
		return CreatedDate;
	}
	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
	}
	public java.lang.Long getPostID() {
		return PostID;
	}
	public void setPostID(java.lang.Long postID) {
		PostID = postID;
	}
	public java.lang.Long getForumID() {
		return ForumID;
	}
	public void setForumID(java.lang.Long forumID) {
		ForumID = forumID;
	}
	protected java.lang.String Categories;
	
	protected java.lang.String ShortDescription;
	
	protected java.lang.String SimilarIssues;
	
	protected long Type;
	
	protected int PlanType = 0;
	
	protected int Status = 0;
	
	protected java.lang.Long MainStrategyID;
	
	public BaseStrategy(Long id2, String name2) {
		this.ID=id2;
		this.Name=name2;
	}
	public BaseStrategy() {
	}
	public BaseStrategy(Long id2, String name2, Long userID2) {
		this.ID=id2;
		this.Name=name2;
		this.UserID=userID2;
		
	}
	public java.lang.Long getID() {
		return ID;
	}
	public void setID(java.lang.Long iD) {
		ID = iD;
	}
	public java.lang.String getName() {
		return Name;
	}
	public void setName(java.lang.String name) {
		Name = name;
	}
	public java.lang.String getDescription() {
		return Description;
	}
	public void setDescription(java.lang.String description) {
		Description = description;
	}
	public java.lang.String getReference() {
		return Reference;
	}
	public void setReference(java.lang.String reference) {
		Reference = reference;
	}
	public java.lang.Long getUserID() {
		return UserID;
	}
	public void setUserID(java.lang.Long userID) {
		UserID = userID;
	}
	public Map<String, String> getAttributes() {
		return Attributes;
	}
	public void setAttributes(Map<String, String> attributes) {
		Attributes = attributes;
	}
	public java.lang.String getCategories() {
		return Categories;
	}
	public void setCategories(java.lang.String categories) {
		Categories = categories;
	}
	public java.lang.String getShortDescription() {
		return ShortDescription;
	}
	public void setShortDescription(java.lang.String shortDescription) {
		ShortDescription = shortDescription;
	}
	public java.lang.String getSimilarIssues() {
		return SimilarIssues;
	}
	public void setSimilarIssues(java.lang.String similarIssues) {
		SimilarIssues = similarIssues;
	}
	public java.lang.Long getStrategyClassID() {
		return StrategyClassID;
	}
	public void setStrategyClassID(java.lang.Long strategyClassID) {
		StrategyClassID = strategyClassID;
	}
	public java.lang.Long getType() {
		return Type;
	}
	public void setType(java.lang.Long type) {
		Type = type;
	}
	public java.lang.Long getMainStrategyID() {
		return MainStrategyID;
	}
	public void setMainStrategyID(java.lang.Long mainStrategyID) {
		MainStrategyID = mainStrategyID;
	}
	
	/**
	 * @param type 如：PORTFOLIO_TYPE_MODEL
	 * @param value
	 */
	public void assignType(long type,boolean value){
		if(value)
			this.Type |= type;
		else
			//this.Type ^= type;
			this.Type &= (~type);
	}
	
	/**
	 * @param type 如：PORTFOLIO_TYPE_MODEL
	 * @return
	 */
	public boolean isType(long type){
		return ((this.Type & type) == type);
	}
	
	public boolean isIndexed(){
		return isType(Configuration.STRATEGY_TYPE_INDEXED);
	}
	
	public void setIndexed(boolean indexed){
		assignType(Configuration.STRATEGY_TYPE_INDEXED, indexed);
	}
	
	public boolean is401K() {
		return isType(Configuration.STRATEGY_TYPE_401K);
	}

	public void set401K(boolean is401k) {
		assignType(Configuration.STRATEGY_TYPE_401K, is401k);
	}
	
	public boolean isConsumerPlan() {
		return isType(Configuration.STRATEGY_TYPE_CONSUMER);
	}

	public void setConsumerPlan(boolean c) {
		assignType(Configuration.STRATEGY_TYPE_CONSUMER, c);
	}
	public boolean isNormal() {
		return isType(Configuration.STRATEGY_TYPE_NORMAL);
	}

	public void setNormal(boolean is401k) {
		assignType(Configuration.STRATEGY_TYPE_NORMAL, is401k);
	}
	public java.lang.String getTicker() {
		return Ticker;
	}
	public void setTicker(java.lang.String ticker) {
		Ticker = ticker;
	}
	public int getPlanType() {
		return PlanType;
	}
	public void setPlanType(int planType) {
		this.PlanType = planType;
	}
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		this.Status = status;
	}
	
}