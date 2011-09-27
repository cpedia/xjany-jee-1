/**
 * 
 */
package com.lti.service.bo;

import java.io.Serializable;

import com.lti.service.bo.base.BaseSecurityMPTIncData;

/**
 * @author CCD
 *
 */
public class SecurityMPTIncData extends BaseSecurityMPTIncData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	public String toString(){
		return SecurityID + " " + Year + " " + SigmaS + " " + SigmaB + " " + SigmaR + " ";
	}
}
