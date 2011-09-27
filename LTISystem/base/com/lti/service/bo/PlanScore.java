/**
 * 
 */
package com.lti.service.bo;

import java.io.Serializable;
import com.lti.service.bo.base.BasePlanScore;
/**
 * @author CCD
 *
 */
public class PlanScore extends BasePlanScore implements Serializable {
	private static final long serialVersionUID = 1L;
	public void clear(){
		this.setSAAScore(0.0);
		this.setTAAScore(0.0);
		this.setSAAReturn(0.0);
		this.setTAAReturn(0.0);
		this.setFundQualityScore(0.0);
		this.setCoverageScore(0.0);
		this.setCapabilityScore(0.0);
		this.setInvestmentScore(0.0);
		this.setStatus(0);
		this.setCoverageValue(0.0);
		this.setCapabilityValue(0.0);
		this.setFundQualityValue(0.0);
		this.setInvestmentValue(0.0);
	}
}
