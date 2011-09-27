package com.lti.bean;

import java.util.ArrayList;
import java.util.List;

import com.lti.type.BLItem;

public class BLRowBean {
	List<BLItem> row;

	public List<BLItem> getRow() {
		return row;
	}

	public void setRow(List<BLItem> row) {
		this.row = row;
	}

	public BLRowBean(){
		row = new ArrayList<BLItem>();
	}
	
}
