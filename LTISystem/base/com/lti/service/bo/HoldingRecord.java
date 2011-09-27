/**
 * 
 */
package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseHoldingRecord;

/**
 * @author CCD
 *
 */
public class HoldingRecord extends BaseHoldingRecord implements Serializable, Cloneable  {
	private static final long serialVersionUID = 1L;
	
	@Override
	public HoldingRecord clone(){
		HoldingRecord hr = null;
		try {
			hr = (HoldingRecord) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
		return hr;
	}
}
