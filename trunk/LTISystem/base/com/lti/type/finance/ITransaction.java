/**
 * 
 */
package com.lti.type.finance;

import java.util.Date;

/**
 * @author ccd
 *
 */
public interface ITransaction {
	Long getPortfolioID();
	Long getSecurityID();
	Long getStrategyID();
	String getSymbol();
	String getAssetName();
	Date getDate();
	String getOperation();
	Double getAmount();
	Double getShare();
	Double getPercentage();
	Integer getTransactionType();
	Boolean getReInvest();
	Boolean getIsIgnore();
}
