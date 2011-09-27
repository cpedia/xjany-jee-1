package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.lti.system.Configuration;
import com.lti.type.executor.StrategyInf;

public abstract class BasePortfolio implements Serializable{

	private static final long serialVersionUID = -3195588552392183234L;

	protected java.lang.Long ID;

	protected java.lang.String Name;
	protected java.lang.String Description;
	protected java.lang.Long UserID;
	protected java.lang.Long MainStrategyID;
	protected java.util.Date EndDate;
	protected java.lang.Long OriginalPortfolioID;
	protected java.util.Date StartingDate;
	protected java.lang.String Categories;
	protected java.util.Date LastTransactionDate;
	protected java.lang.String UserName;
	protected java.util.Date DelayLastTransactionDate;
	protected long Type=0l;
	protected Map<String, String> Attributes;
	protected StrategyInf Strategies;
	protected Integer State=Configuration.PORTFOLIO_STATE_STATIC;
	
	protected Date CreatedDate;
	
	
	
	public Date getCreatedDate() {
		return CreatedDate;
	}

	public void setCreatedDate(Date createdDate) {
		CreatedDate = createdDate;
	}

	public Integer getState() {
		return State;
	}

	public void setState(Integer state) {
		State = state;
	}

	@Deprecated
	public BasePortfolio(Long id2, String name2, Date endDate2, Long userID2, Integer state) {
		this.ID=id2;
		this.Name=name2;
		this.EndDate=endDate2;
		this.UserID=userID2;
		this.Type^=state.longValue();
	}
	
	public BasePortfolio() {
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
	public java.lang.Long getUserID() {
		return UserID;
	}
	public void setUserID(java.lang.Long userID) {
		UserID = userID;
	}
	public java.lang.Long getMainStrategyID() {
		return MainStrategyID;
	}
	public void setMainStrategyID(java.lang.Long mainStrategyID) {
		MainStrategyID = mainStrategyID;
	}
	public java.util.Date getEndDate() {
		return EndDate;
	}
	public void setEndDate(java.util.Date endDate) {
		EndDate = endDate;
	}
	public java.lang.Long getOriginalPortfolioID() {
		return OriginalPortfolioID;
	}
	public void setOriginalPortfolioID(java.lang.Long originalPortfolioID) {
		OriginalPortfolioID = originalPortfolioID;
	}
	public java.util.Date getStartingDate() {
		return StartingDate;
	}
	public void setStartingDate(java.util.Date startingDate) {
		StartingDate = startingDate;
	}
	public java.lang.String getCategories() {
		return Categories;
	}
	public void setCategories(java.lang.String categories) {
		Categories = categories;
	}
	public java.util.Date getLastTransactionDate() {
		return LastTransactionDate;
	}
	public void setLastTransactionDate(java.util.Date lastTransactionDate) {
		LastTransactionDate = lastTransactionDate;
	}
	public java.lang.String getUserName() {
		return UserName;
	}
	public void setUserName(java.lang.String userName) {
		UserName = userName;
	}
	@Deprecated
	public java.util.Date getDelayLastTransactionDate() {
		return DelayLastTransactionDate;
	}
	@Deprecated
	public void setDelayLastTransactionDate(java.util.Date delayLastTransactionDate) {
		DelayLastTransactionDate = delayLastTransactionDate;
	}
	public Map<String, String> getAttributes() {
		return Attributes;
	}
	public void setAttributes(Map<String, String> attributes) {
		Attributes = attributes;
	}
	public long getType() {
		return Type;
	}
	public void setType(long type) {
		Type = type;
	}

	public StrategyInf getStrategies() {
		if(Strategies==null){
			Strategies=new StrategyInf();
		}
		return Strategies;
	}

	public void setStrategies(StrategyInf strategies) {
		Strategies = strategies;
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
	
	/**
	 * 注意此为true时，Main strategy id 不为空
	 */
	public boolean isModel() {
		return isType(Configuration.PORTFOLIO_TYPE_MODEL);
	}

	public void setModel(boolean isModel) {
		assignType(Configuration.PORTFOLIO_TYPE_MODEL, isModel);
	}
	
	public boolean isProduction(){
		return isType(Configuration.PORTFOLIO_TYPE_PRODUCTION);
	}
	
	public void setProduction(boolean isProduction){
		assignType(Configuration.PORTFOLIO_TYPE_PRODUCTION, isProduction);
	}
	
	public boolean isData(){
		return isType(Configuration.PORTFOLIO_TYPE_DATA);
	}
	
	public void setData(boolean isProduction){
		assignType(Configuration.PORTFOLIO_TYPE_DATA, isProduction);
	}

	public boolean isPersonal(){
		return isType(Configuration.PORTFOLIO_TYPE_PERSONAL);
	}
	
	public void setPersonal(boolean isPersonal){
		assignType(Configuration.PORTFOLIO_TYPE_PERSONAL, isPersonal);
	}
	
	public boolean isFullyPublic(){
		return isType(Configuration.PORTFOLIO_TYPE_FULLYPUBLIC);
	}
	
	public void setFullyPublic(boolean isFullyPublic){
		assignType(Configuration.PORTFOLIO_TYPE_FULLYPUBLIC,isFullyPublic);
	}

}