package com.lti.service.bo.base;

import java.io.Serializable;

public abstract class BaseCompany implements Serializable{
	
	private java.lang.Long ID;
	
	private java.lang.String Symbol;
	
	private java.lang.String Sector;
	
	private java.lang.String Industry;

	/**
	 * @return the iD
	 */
	public java.lang.Long getID() {
		return ID;
	}

	/**
	 * @param id the iD to set
	 */
	public void setID(java.lang.Long id) {
		ID = id;
	}

	/**
	 * @return the symbol
	 */
	public java.lang.String getSymbol() {
		return Symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(java.lang.String symbol) {
		Symbol = symbol;
	}

	/**
	 * @return the sector
	 */
	public java.lang.String getSector() {
		return Sector;
	}

	/**
	 * @param sector the sector to set
	 */
	public void setSector(java.lang.String sector) {
		Sector = sector;
	}

	/**
	 * @return the industry
	 */
	public java.lang.String getIndustry() {
		return Industry;
	}

	/**
	 * @param industry the industry to set
	 */
	public void setIndustry(java.lang.String industry) {
		Industry = industry;
	}
	
	

}
