/**
 * 
 */
package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseVAFund;

/**
 * @author CCD
 *
 */
public class VAFund extends BaseVAFund implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private char SearchType = 'A';
	private String MSLink = "";
	@Override
	public String toString(){
		return 	"Ticker: " + this.Ticker + 
				"\nAssetName: " + this.AssetName + 
				"\nFundName: " + this.FundName + 
				"\nMSVAName: " + this.MSVAName + 
				"\nBarronName: " + this.BarronName +
				"\nFullName: " + this.FullName + "\n";
	}
	public char getSearchType() {
		return SearchType;
	}
	public void setSearchType(char searchType) {
		SearchType = searchType;
	}
	public String getMSLink() {
		return MSLink;
	}
	public void setMSLink(String link) {
		MSLink = link;
	}
}
