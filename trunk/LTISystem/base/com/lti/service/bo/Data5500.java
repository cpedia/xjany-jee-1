/**
 * 
 */
package com.lti.service.bo;

/**
 * @author YZW
 *
 */
public class Data5500 {
	private long ID;
	private String ACK_ID;
	private String FORM_PLAN_YEAR_BEGIN_DATE;
	private String PLAN_NAME;
	private String SPONSOR_DFE_NAME;
	private long PLAN_ID;
	private String SPONS_DFE_LOC_US_ADDRESS1;
	private String SPONS_DFE_LOC_US_ADDRESS2;
	private String SPONS_DFE_LOC_US_CITY;
	private String SPONS_DFE_LOC_US_STATE;
	private String SPONS_DFE_LOC_US_ZIP;
	private String SPONS_DFE_EIN;
	private String SPONS_DFE_PHONE_NUM;
	private String ADMIN_NAME;
	private String ADMIN_US_ADDRESS1;
	private String ADMIN_US_ADDRESS2;
	private String ADMIN_US_CITY;
	private String ADMIN_US_STATE;
	private String ADMIN_US_ZIP;
	private String ADMIN_PHONE_NUM;
	private String ADMIN_SIGNED_NAME;
	private String ADMIN_EIN;
	private String SPONS_SIGNED_NAME;
	private long TOT_PARTCP_BOY_CNT;
	private String TYPE_PENSION_BNFT_CODE;
	private double TOT_ASSETS_BOY_AMT;
	private double TOT_ASSETS_EOY_AMT;
	private double TOT_CONTRIB_AMT;
	private double TOT_INCOME_AMT;
	private double TOT_DISTRIB_BNFT_AMT;
	
	public double getTOT_ASSETS_BOY_AMT() {
		return TOT_ASSETS_BOY_AMT;
	}
	public void setTOT_ASSETS_BOY_AMT(double tot_assets_boy_amt) {
		TOT_ASSETS_BOY_AMT = tot_assets_boy_amt;
	}
	public double getTOT_ASSETS_EOY_AMT() {
		return TOT_ASSETS_EOY_AMT;
	}
	public void setTOT_ASSETS_EOY_AMT(double tot_assets_eoy_amt) {
		TOT_ASSETS_EOY_AMT = tot_assets_eoy_amt;
	}
	public double getTOT_CONTRIB_AMT() {
		return TOT_CONTRIB_AMT;
	}
	public void setTOT_CONTRIB_AMT(double tot_contrib_amt) {
		TOT_CONTRIB_AMT = tot_contrib_amt;
	}
	public double getTOT_INCOME_AMT() {
		return TOT_INCOME_AMT;
	}
	public void setTOT_INCOME_AMT(double tot_income_amt) {
		TOT_INCOME_AMT = tot_income_amt;
	}
	public double getTOT_DISTRIB_BNFT_AMT() {
		return TOT_DISTRIB_BNFT_AMT;
	}
	public void setTOT_DISTRIB_BNFT_AMT(double tot_distrib_bnft_amt) {
		TOT_DISTRIB_BNFT_AMT = tot_distrib_bnft_amt;
	}
	public void setTOT_DISTRIB_BNFT_AMT(long tot_distrib_bnft_amt) {
		TOT_DISTRIB_BNFT_AMT = tot_distrib_bnft_amt;
	}
	public String getTYPE_PENSION_BNFT_CODE() {
		return TYPE_PENSION_BNFT_CODE;
	}
	public void setTYPE_PENSION_BNFT_CODE(String type_pension_bnft_code) {
		TYPE_PENSION_BNFT_CODE = type_pension_bnft_code;
	}
	public Data5500(){
		
	}
	public Data5500(String ACK_ID){
		this.ACK_ID = ACK_ID;
	}
	public Data5500(long ID){
		this.ID = ID;
	}
	public long getID() {
		return ID;
	}
	public void setID(long id) {
		ID = id;
	}
	public String getACK_ID() {
		return ACK_ID;
	}
	public void setACK_ID(String ack_id) {
		ACK_ID = ack_id;
	}
	public String getFORM_PLAN_YEAR_BEGIN_DATE() {
		return FORM_PLAN_YEAR_BEGIN_DATE;
	}
	public void setFORM_PLAN_YEAR_BEGIN_DATE(String form_plan_year_begin_date) {
		FORM_PLAN_YEAR_BEGIN_DATE = form_plan_year_begin_date;
	}
	public String getPLAN_NAME() {
		return PLAN_NAME;
	}
	public void setPLAN_NAME(String plan_name) {
		PLAN_NAME = plan_name;
	}
	public String getSPONSOR_DFE_NAME() {
		return SPONSOR_DFE_NAME;
	}
	public void setSPONSOR_DFE_NAME(String sponsor_dfe_name) {
		SPONSOR_DFE_NAME = sponsor_dfe_name;
	}
	public long getPLAN_ID() {
		return PLAN_ID;
	}
	public void setPLAN_ID(long plan_id) {
		PLAN_ID = plan_id;
	}
	public String getSPONS_DFE_LOC_US_ADDRESS1() {
		return SPONS_DFE_LOC_US_ADDRESS1;
	}
	public void setSPONS_DFE_LOC_US_ADDRESS1(String spons_dfe_loc_us_address1) {
		SPONS_DFE_LOC_US_ADDRESS1 = spons_dfe_loc_us_address1;
	}
	public String getSPONS_DFE_LOC_US_ADDRESS2() {
		return SPONS_DFE_LOC_US_ADDRESS2;
	}
	public void setSPONS_DFE_LOC_US_ADDRESS2(String spons_dfe_loc_us_address2) {
		SPONS_DFE_LOC_US_ADDRESS2 = spons_dfe_loc_us_address2;
	}
	public String getSPONS_DFE_LOC_US_CITY() {
		return SPONS_DFE_LOC_US_CITY;
	}
	public void setSPONS_DFE_LOC_US_CITY(String spons_dfe_loc_us_city) {
		SPONS_DFE_LOC_US_CITY = spons_dfe_loc_us_city;
	}
	public String getSPONS_DFE_LOC_US_STATE() {
		return SPONS_DFE_LOC_US_STATE;
	}
	public void setSPONS_DFE_LOC_US_STATE(String spons_dfe_loc_us_state) {
		SPONS_DFE_LOC_US_STATE = spons_dfe_loc_us_state;
	}
	public String getSPONS_DFE_LOC_US_ZIP() {
		return SPONS_DFE_LOC_US_ZIP;
	}
	public void setSPONS_DFE_LOC_US_ZIP(String spons_dfe_loc_us_zip) {
		SPONS_DFE_LOC_US_ZIP = spons_dfe_loc_us_zip;
	}
	public String getSPONS_DFE_EIN() {
		return SPONS_DFE_EIN;
	}
	public void setSPONS_DFE_EIN(String spons_dfe_ein) {
		SPONS_DFE_EIN = spons_dfe_ein;
	}
	public String getSPONS_DFE_PHONE_NUM() {
		return SPONS_DFE_PHONE_NUM;
	}
	public void setSPONS_DFE_PHONE_NUM(String spons_dfe_phone_num) {
		SPONS_DFE_PHONE_NUM = spons_dfe_phone_num;
	}
	public String getADMIN_NAME() {
		return ADMIN_NAME;
	}
	public void setADMIN_NAME(String admin_name) {
		ADMIN_NAME = admin_name;
	}
	public String getADMIN_US_ADDRESS1() {
		return ADMIN_US_ADDRESS1;
	}
	public void setADMIN_US_ADDRESS1(String admin_us_address1) {
		ADMIN_US_ADDRESS1 = admin_us_address1;
	}
	public String getADMIN_US_ADDRESS2() {
		return ADMIN_US_ADDRESS2;
	}
	public void setADMIN_US_ADDRESS2(String admin_us_address2) {
		ADMIN_US_ADDRESS2 = admin_us_address2;
	}
	public String getADMIN_US_CITY() {
		return ADMIN_US_CITY;
	}
	public void setADMIN_US_CITY(String admin_us_city) {
		ADMIN_US_CITY = admin_us_city;
	}
	public String getADMIN_US_STATE() {
		return ADMIN_US_STATE;
	}
	public void setADMIN_US_STATE(String admin_us_state) {
		ADMIN_US_STATE = admin_us_state;
	}
	public String getADMIN_US_ZIP() {
		return ADMIN_US_ZIP;
	}
	public void setADMIN_US_ZIP(String admin_us_zip) {
		ADMIN_US_ZIP = admin_us_zip;
	}
	public String getADMIN_PHONE_NUM() {
		return ADMIN_PHONE_NUM;
	}
	public void setADMIN_PHONE_NUM(String admin_phone_num) {
		ADMIN_PHONE_NUM = admin_phone_num;
	}
	public String getADMIN_SIGNED_NAME() {
		return ADMIN_SIGNED_NAME;
	}
	public void setADMIN_SIGNED_NAME(String admin_signed_name) {
		ADMIN_SIGNED_NAME = admin_signed_name;
	}
	public String getSPONS_SIGNED_NAME() {
		return SPONS_SIGNED_NAME;
	}
	public void setSPONS_SIGNED_NAME(String spons_signed_name) {
		SPONS_SIGNED_NAME = spons_signed_name;
	}
	public long getTOT_PARTCP_BOY_CNT() {
		return TOT_PARTCP_BOY_CNT;
	}
	public void setTOT_PARTCP_BOY_CNT(long tot_partcp_boy_cnt) {
		TOT_PARTCP_BOY_CNT = tot_partcp_boy_cnt;
	}
	public String getADMIN_EIN() {
		return ADMIN_EIN;
	}
	public void setADMIN_EIN(String admin_ein) {
		ADMIN_EIN = admin_ein;
	}
	
}
