/**
 * 
 */
package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseSecurityRanking;

/**
 * @author CCD
 *
 */
public class SecurityRanking extends BaseSecurityRanking implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String toString()
	{
		return "securityID: "+this.SecurityID+" symbol: "+this.Symbol+" betagain: "+this.BetaGain+" ranking: "+ " date: "+ this.EndDate.getYear()+1900+"-"+this.EndDate.getMonth();
	}
}
