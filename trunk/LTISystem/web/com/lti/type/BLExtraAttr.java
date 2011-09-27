package com.lti.type;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lti.bean.BLAttributeBean;
import com.lti.system.Configuration;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

public class BLExtraAttr {
	
	List<BLAttributeBean> ExtraAttributes;

	public BLExtraAttr(){
		ExtraAttributes = new ArrayList<BLAttributeBean>();
		
		BLAttributeBean bean = new BLAttributeBean();
		
		bean.setAttributeName(BLAttributeBean.DISCOUNTRATE);
		
		bean.setXMLName(BLAttributeBean.XML_DISCOUNTRATE);
		
		bean.setIsFromDatabase(false);
		
		bean.setIsSingleValue(false);
		
		bean.setChoosed(false);
		
		bean.setDataType(BLAttributeBean.DataType_D);
		
		bean.setShowType(BLAttributeBean.Show_TextField);
		
		bean.setSecurityType(Configuration.SECURITY_TYPE_CLOSED_END_FUND);
		
		bean.setEndDate(LTIDate.clearHMSM(new Date()));	//default end date is today
		
		ExtraAttributes.add(bean);
		
		
		BLAttributeBean blBean = new BLAttributeBean();
		
		blBean.setAttributeName(BLAttributeBean.ASSETCLASS);
		blBean.setDataBaseName(BLAttributeBean.DB_ASSETCLASS);
		blBean.setDataType(BLAttributeBean.DataType_L);
		blBean.setIsFromDatabase(true);
		blBean.setIsSingleValue(true);
		blBean.setShowType(BLAttributeBean.Show_List);
		blBean.setSecurityType(BLAttributeBean.SECURITY_NOT_PORTFOLIO);
		
		ExtraAttributes.add(blBean);
	}
	
	private void setACAttributes(BLAttributeBean blBean, Integer securityType){
		int type_ref = securityType;
		blBean.setAttributeName(BLAttributeBean.ASSETCLASS);
		blBean.setDataBaseName(BLAttributeBean.DB_ASSETCLASS);
		blBean.setDataType(BLAttributeBean.DataType_L);
		blBean.setIsFromDatabase(true);
		blBean.setIsSingleValue(true);
		blBean.setShowType(BLAttributeBean.Show_List);
		switch (type_ref){
			case Configuration.SECURITY_TYPE_BENCHMARK:{
				blBean.setSecurityType(Configuration.SECURITY_TYPE_BENCHMARK);
				break;
			}
			case Configuration.SECURITY_TYPE_CLOSED_END_FUND:{
				blBean.setSecurityType(Configuration.SECURITY_TYPE_CLOSED_END_FUND);
				break;
			}
			case Configuration.SECURITY_TYPE_ETF:{
				blBean.setSecurityType(Configuration.SECURITY_TYPE_ETF);
				break;
			}
			case Configuration.SECURITY_TYPE_MUTUAL_FUND:{
				blBean.setSecurityType(Configuration.SECURITY_TYPE_MUTUAL_FUND);
				break;
			}
		}
	}

	public List<BLAttributeBean> getExtraAttributes() {
		return ExtraAttributes;
	}

	public void setExtraAttributes(List<BLAttributeBean> extraAttributes) {
		ExtraAttributes = extraAttributes;
	}
	
	public void translateAttributes(){
		if(this.ExtraAttributes == null || this.ExtraAttributes.size() == 0)
			return;
		for(int i = 0; i < ExtraAttributes.size(); i++){
			BLAttributeBean blBean = ExtraAttributes.get(i);
			if(blBean.getChoosed() == null || blBean.getChoosed() == false){
				ExtraAttributes.remove(i);
				i--;
				continue;
			}
			if(blBean.getIsSingleValue() == true){
				translateSingleItem(blBean);
			}
			else
			{
				translateNotSingleItem(blBean);
			}
		}
	}
	
	private void translateSingleItem(BLAttributeBean blBean){
		int DataTypeRef = blBean.getDataType();
		String value = blBean.getValueForStr();
		if(value == null || value.equals(""))
			return;
		switch (DataTypeRef) {
			case BLAttributeBean.DataType_D:{
				blBean.setValueForDouble(Double.parseDouble(value));
				break;
			}
			case BLAttributeBean.DataType_I:{
				blBean.setValueForInt(Integer.parseInt(value));
				break;
			}
			case BLAttributeBean.DataType_L:{
				blBean.setValueForLong(Long.parseLong(value));
				break;
			}
		}
	}
	
	private void translateNotSingleItem(BLAttributeBean blBean){
		int DataTypeRef = blBean.getDataType();
		String MaxValue = blBean.getMaxValueStr();
		String MinValue = blBean.getMinValueStr();
		switch (DataTypeRef) {
			case BLAttributeBean.DataType_D:{
				if(MaxValue != null && !MaxValue.equals("")){
					blBean.setMaxValue(StringUtil.percentageToDouble(MaxValue));					
				}
				if(MinValue != null && !MinValue.equals("")){
					blBean.setMinValue(StringUtil.percentageToDouble(MinValue));
				}
				break;
			}
			case BLAttributeBean.DataType_I:{
				if(MaxValue != null && !MaxValue.equals("")){
					blBean.setMaxValue(Integer.parseInt(MaxValue));
				}
				if(MinValue != null && !MinValue.equals("")){
					blBean.setMinValue(Integer.parseInt(MinValue));
				}
				break;
			}
			case BLAttributeBean.DataType_L:{
				if(MaxValue != null && !MaxValue.equals("")){
					blBean.setMaxValue(Long.parseLong(MaxValue));
				}
				if(MinValue != null && !MinValue.equals("")){
					blBean.setMinValue(Long.parseLong(MinValue));
				}
				break;
			}
		}
	}
}
