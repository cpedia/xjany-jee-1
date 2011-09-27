package com.lti.type.finance;

import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.lti.type.executor.StrategyInf;

public class GPortfolio implements Serializable{

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
	public Integer getState() {
		return State;
	}
	public static GPortfolio getInstance(Reader reader) {
		try {
			Unmarshaller unmarshaller = new Unmarshaller(GPortfolio.class);
			unmarshaller.setWhitespacePreserve(true);
			GPortfolio hinf = (GPortfolio) unmarshaller.unmarshal(reader);
			return hinf;
		} catch (Exception e) {
		}
		return null;
	}
	public void setState(Integer state) {
		State = state;
	}

	@Deprecated
	public GPortfolio(Long id2, String name2, Date endDate2, Long userID2, Integer state) {
		this.ID=id2;
		this.Name=name2;
		this.EndDate=endDate2;
		this.UserID=userID2;
		this.Type^=state.longValue();
	}
	
	public GPortfolio() {
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
	public java.util.Date getDelayLastTransactionDate() {
		return DelayLastTransactionDate;
	}
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
			this.Type ^= type;
	}
	
	/**
	 * @param type 如：PORTFOLIO_TYPE_MODEL
	 * @return
	 */
	public boolean isType(long type){
		return ((this.Type & type) != 0l);
	}
	
	/**
	 * 注意此为true时，Main strategy id 不为�?
	 */
	public boolean isModel() {
		return isType(PORTFOLIO_TYPE_MODEL);
	}

	public void setModel(boolean isModel) {
		assignType(PORTFOLIO_TYPE_MODEL, isModel);
	}
	
	public boolean isProduction(){
		return isType(PORTFOLIO_TYPE_PRODUCTION);
	}
	
	public void setProduction(boolean isProduction){
		assignType(PORTFOLIO_TYPE_PRODUCTION, isProduction);
	}

	public boolean isPersonal(){
		return isType(PORTFOLIO_TYPE_PERSONAL);
	}
	
	public void setPersonal(boolean isPersonal){
		assignType(PORTFOLIO_TYPE_PERSONAL, isPersonal);
	}
	
	public boolean isData(){
		return isType(PORTFOLIO_TYPE_DATA);
	}
	
	public void setData(boolean isProduction){
		assignType(PORTFOLIO_TYPE_DATA, isProduction);
	}
	public final static long PORTFOLIO_TYPE_DATA=1<<4;
	public final static long PORTFOLIO_TYPE_MODEL=1;
	public final static long PORTFOLIO_TYPE_FREE=1<<1;
	public final static long PORTFOLIO_TYPE_PRODUCTION=1<<2;
	public final static long PORTFOLIO_TYPE_PERSONAL=1<<3;
	public String toXML() {
		try {
			StringWriter sw = new StringWriter();
			Marshaller marshaller = new Marshaller(sw);
			marshaller.marshal(this);
			return sw.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	public Portfolio toPortfolio(){
		Portfolio np=new Portfolio();
		GPortfolio p=this;
		np.setID(p.getID());
		np.setName(p.getName());
		np.setDescription(p.getDescription());
		np.setUserID(p.getUserID());
		np.setMainStrategyID(p.getMainStrategyID());
		np.setEndDate(p.getEndDate());
		np.setOriginalPortfolioID(p.getOriginalPortfolioID());
		np.setStartingDate(p.getStartingDate());
		np.setCategories(p.getCategories());
		np.setLastTransactionDate(p.getLastTransactionDate());
		np.setUserName(p.getUserName());
		np.setDelayLastTransactionDate(p.getDelayLastTransactionDate());
		
		np.setState(p.getState());

		np.setType(p.getType());
		
		np.setAttributes(p.getAttributes());
		
		np.setStrategies(p.getStrategies());
		return np;
	}
}