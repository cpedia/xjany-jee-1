package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.Date;



public abstract class CompanyIndex  implements Serializable {
	private String CompanyName;
	private String FormType;
	private long CIK;
	private Date DateFiled;
	private String FileName;
	public String getCompanyName() {
		return CompanyName;
	}
	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}
	public String getFormType() {
		return FormType;
	}
	public void setFormType(String formType) {
		FormType = formType;
	}
	public long getCIK() {
		return CIK;
	}
	public void setCIK(long cik) {
		CIK = cik;
	}
	public Date getDateFiled() {
		return DateFiled;
	}
	public void setDateFiled(Date dateFiled) {
		DateFiled = dateFiled;
	}
	public String getFileName() {
		return FileName;
	}
	public void setFileName(String fileName) {
		FileName = fileName;
	}
	

}