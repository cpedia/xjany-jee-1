package com.lti.database;

import com.lti.service.bo.HoldingItem;
import com.lti.type.finance.Asset;
import com.lti.type.finance.HoldingInf;
import com.lti.util.CopyUtil;

public class Output {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CopyUtil.printObject(new HoldingInf());
		CopyUtil.printObject(new Asset());
		CopyUtil.printObject(new HoldingItem());
	}

}
