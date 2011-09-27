package com.lti.bean;

public class BLAttributeBean {
	/*Extra Security or Portfolio atrributes according to different types*/
	public final static String DISCOUNTRATE = "Discount Rate";
	public final static String ASSETCLASS = "Asset Class";
	public final static String NAV_AR = "NAV AR";
	public final static String NAV_ALPHA = "NAV Alpha";
	public final static String NAV_BETA = "NAV Beta";
	public final static String NAV_SHARPE = "NAV Sharpe";
	public final static String NAV_RSQUARED = "NAV RSQUARED";
	public final static String NAV_TREYNOR = "NAV TREYNOR";
	public final static String NAV_STANDARDDIVIATION = "NAV Standard Diviation";
	public final static String NAV_DRAWDOWN = "NAV Drow Down";
	public final static String NAV_RETURN = "NAV Return";
	
	/*Extra Security or Portfolio attributes in DB name*/
	public final static String DB_ASSETCLASS = "ClassID";
	
	/*Extra Security or Portfolio attributes' XML names*/
	public final static String XML_DISCOUNTRATE = "DiscountRate";
	public final static String XML_ASSETCLASS = "AssetClass";
	public final static String XML_NAV_AR = "NAVAR";
	public final static String XML_NAV_ALPHA = "NAVALPHA";
	public final static String XML_NAV_BETA = "NAVBETA";
	public final static String XML_NAV_SHARPE = "NAVSHARPE";
	public final static String XML_NAV_RSQUARED = "NAVRSQUARED";
	public final static String XML_NAV_TREYNOR = "NAVTREYNOR";
	public final static String XML_NAV_STANDARDDIVIATION = "NAVSTANDARDDIVIATION";
	public final static String XML_NAV_DRAWDOWN = "NAVDRAWDOWN";
	public final static String XML_NAV_RETURN = "NAVRETURN";
	
	/*Data Type in the Extra Attribute*/
	public final static int DataType_I = 1;
	public final static int DataType_D = 2;
	public final static int DataType_S = 3;
	public final static int DataType_L = 4;
	
	/*Atrtibutes Show Type*/
	public final static Integer Show_List = 1;
	public final static Integer Show_TextField = 2;
	
	/*Security Type Except portfolio and stock*/
	public final static Integer SECURITY_NOT_PORTFOLIO = 10;
	
	/*Relative Attributes in securities*/
	//public final static int SECURITY_DISCOUTRATE = 1;
	
	private java.lang.Integer SecurityType;
	
	private java.lang.String AttributeName;
	
	//use single value or between setting, it's single value by default
	private java.lang.Boolean IsSingleValue;
	
	//record the attribute's data type
	private java.lang.Integer DataType;
	
	private Object MaxValue;
	
	private Object MinValue;
	
	//private java.lang.Integer MaxValueForInt;
	
	//private java.lang.Integer MinValueForInt;
	
	private java.lang.String MaxValueStr;
	
	private java.lang.String MinValueStr;
	
	private java.lang.String ValueForStr;
	
	private java.lang.Integer ValueForInt;
	
	private java.lang.Long ValueForLong;
	
	private java.lang.Double ValueForDouble;
	
	private java.lang.Boolean IsFromDatabase;
	
	private java.lang.String DataBaseName;
	
	private java.lang.String XMLName;
	
	private java.lang.Boolean Choosed;
	
	private java.lang.Integer Period;
	
	private java.lang.Integer ShowType;
	
	private java.lang.String sort;	//0 as asc, 1 as desc
	
	private java.util.Date startDate;
	
	private java.util.Date endDate;
	
	private java.lang.Long year;
	
	private java.lang.String time;

	public java.util.Date getStartDate() {
		return startDate;
	}

	public void setStartDate(java.util.Date startDate) {
		this.startDate = startDate;
	}

	public java.util.Date getEndDate() {
		return endDate;
	}

	public void setEndDate(java.util.Date endDate) {
		this.endDate = endDate;
	}


	public java.lang.String getSort() {
		return sort;
	}

	public void setSort(java.lang.String sort) {
		this.sort = sort;
	}

	public java.lang.Integer getSecurityType() {
		return SecurityType;
	}

	public void setSecurityType(java.lang.Integer securityType) {
		SecurityType = securityType;
	}

	public java.lang.String getValueForStr() {
		return ValueForStr;
	}

	public void setValueForStr(java.lang.String valueForStr) {
		ValueForStr = valueForStr;
	}

	public java.lang.Integer getValueForInt() {
		return ValueForInt;
	}

	public void setValueForInt(java.lang.Integer valueForInt) {
		ValueForInt = valueForInt;
	}

	public java.lang.Long getValueForLong() {
		return ValueForLong;
	}

	public void setValueForLong(java.lang.Long valueForLong) {
		ValueForLong = valueForLong;
	}

	public java.lang.Boolean getChoosed() {
		return Choosed;
	}

	public void setChoosed(java.lang.Boolean choosed) {
		Choosed = choosed;
	}

	public java.lang.String getAttributeName() {
		return AttributeName;
	}

	public void setAttributeName(java.lang.String attributeName) {
		AttributeName = attributeName;
	}

	public java.lang.Boolean getIsFromDatabase() {
		return IsFromDatabase;
	}

	public void setIsFromDatabase(java.lang.Boolean isFromDatabase) {
		IsFromDatabase = isFromDatabase;
	}

	public java.lang.Boolean getIsSingleValue() {
		return IsSingleValue;
	}

	public void setIsSingleValue(java.lang.Boolean isSingleValue) {
		IsSingleValue = isSingleValue;
	}

	public java.lang.String getMaxValueStr() {
		return MaxValueStr;
	}

	public void setMaxValueStr(java.lang.String maxValueStr) {
		MaxValueStr = maxValueStr;
	}

	public java.lang.String getMinValueStr() {
		return MinValueStr;
	}

	public void setMinValueStr(java.lang.String minValueStr) {
		MinValueStr = minValueStr;
	}

	public java.lang.String getDataBaseName() {
		return DataBaseName;
	}

	public void setDataBaseName(java.lang.String dataBaseName) {
		DataBaseName = dataBaseName;
	}

	public java.lang.Integer getDataType() {
		return DataType;
	}

	public void setDataType(java.lang.Integer dataType) {
		DataType = dataType;
	}

	public java.lang.Double getValueForDouble() {
		return ValueForDouble;
	}

	public void setValueForDouble(java.lang.Double valueForDouble) {
		ValueForDouble = valueForDouble;
	}

	public Object getMaxValue() {
		return MaxValue;
	}

	public void setMaxValue(Object maxValue) {
		MaxValue = maxValue;
	}

	public Object getMinValue() {
		return MinValue;
	}

	public void setMinValue(Object minValue) {
		MinValue = minValue;
	}

	public java.lang.Integer getPeriod() {
		return Period;
	}

	public void setPeriod(java.lang.Integer period) {
		Period = period;
	}

	public java.lang.Integer getShowType() {
		return ShowType;
	}

	public void setShowType(java.lang.Integer showType) {
		ShowType = showType;
	}

	public java.lang.String getXMLName() {
		return XMLName;
	}

	public void setXMLName(java.lang.String name) {
		XMLName = name;
	}

	public java.lang.Long getYear() {
		return year;
	}

	public void setYear(java.lang.Long year) {
		this.year = year;
	}

	public java.lang.String getTime() {
		return time;
	}

	public void setTime(java.lang.String time) {
		this.time = time;
	}
}
