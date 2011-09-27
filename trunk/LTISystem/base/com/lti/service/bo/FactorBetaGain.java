/**
 * 
 */
package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseFactorBetaGain;


/**
 * @author Administrator
 *
 */
public class FactorBetaGain extends BaseFactorBetaGain implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public String toString()
	{
		return "symbol: "+Symbol+" factor: "+Factor+" beta "+Beta+" 1 month: "+OneMonth+" 3 months: "+ThreeMonth+" 6 months: "+HalfYear+" 1 year: "+OneYear;
	}
}
