/**
 * 
 */
package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseShillerSP500;


/**
 * @author CCD
 *
 */
public class ShillerSP500 extends BaseShillerSP500 implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String toString(){
		return SPDate + " " + RealEarnings + " " + PriceEarningsRatio;
	}
	
}
