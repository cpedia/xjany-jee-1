/**
 * 
 */
package com.lti.service.bo;

/**
 * @author CCD
 *
 */
public class KeyValuePair {
	private Long key;
	private Double value;
	public KeyValuePair(Long key, Double value){
		this.key = key;
		this.value = value;
	}
	public Long getKey() {
		return key;
	}
	public void setKey(Long key) {
		this.key = key;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
}
