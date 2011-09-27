package com.lti.type.finance;

import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Map;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import com.lti.service.bo.Strategy;
import com.lti.system.Configuration;
import com.lti.type.executor.CodeInf;

public class GStrategy implements Serializable{

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
	
	protected java.lang.String Description;
	
	protected java.lang.String Reference;
	
	protected java.lang.Long UserID;
	
	protected Map<String, String> Attributes;
	
	protected java.lang.Long PostID;
	
	protected java.lang.Long ForumID;
	
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
	
	protected java.lang.Long MainStrategyID;
	
	public GStrategy(Long id2, String name2) {
		this.ID=id2;
		this.Name=name2;
	}
	public GStrategy() {
	}
	public GStrategy(Long id2, String name2, Long userID2) {
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
			this.Type ^= type;
	}
	
	/**
	 * @param type 如：PORTFOLIO_TYPE_MODEL
	 * @return
	 */
	public boolean isType(long type){
		return ((this.Type & type) != 0l);
	}
	
	public boolean is401K() {
		return isType(STRATEGY_TYPE_401K);
	}

	public void set401K(boolean is401k) {
		assignType(STRATEGY_TYPE_401K, is401k);
	}
	public boolean isNormal() {
		return isType(STRATEGY_TYPE_NORMAL);
	}

	public void setNormal(boolean is401k) {
		assignType(STRATEGY_TYPE_NORMAL, is401k);
	}
	
	public void setCenter(boolean center){
		assignType(STRATEGY_TYPE_401K_CENTER, center);
	}
	
	public void setFree(boolean free){
		assignType(STRATEGY_TYPE_401K_CENTER, free);
	}
	
	public final static long STRATEGY_TYPE_NORMAL=1;
	public final static long STRATEGY_TYPE_401K=1<<1;
	public final static long STRATEGY_TYPE_PRESERVED=1<<2;
	public final static long STRATEGY_TYPE_401K_CENTER=1<<3;
	public final static long STRATEGY_TYPE_FREE=1<<4;

	public String toXML() {
		try {
			StringWriter sw = new StringWriter();
			Marshaller marshaller = new Marshaller(sw);
			marshaller.marshal(this);
			return sw.toString();
		} catch (Exception e) {
			return "";
		}
	}
	
	public static GStrategy getInstance(Reader reader) {
		try {
			Unmarshaller unmarshaller = new Unmarshaller(GStrategy.class);
			unmarshaller.setWhitespacePreserve(true);
			GStrategy hinf = (GStrategy) unmarshaller.unmarshal(reader);
			return hinf;
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}
	
	public Strategy toStrategy(){
		Strategy ns=new Strategy();
		GStrategy s=this;
		ns.setID(s.getID());
		ns.setName(s.getName());
		ns.setUserName(s.getUserName());
		ns.setStrategyClassID(s.getStrategyClassID());
		ns.setDescription(s.getDescription());
		ns.setReference(s.getReference());
		ns.setUserID(s.getUserID());
		
		ns.setForumID(s.getForumID());
		ns.setCategories(s.getCategories());
		ns.setShortDescription(s.getShortDescription());
		ns.setSimilarIssues(s.getSimilarIssues());
		
		ns.setMainStrategyID(s.getMainStrategyID());
		
		ns.setType(s.getType());
		
		ns.setAttributes(s.getAttributes());
		ns.setPostID(s.getPostID());
		return ns;
	}
}