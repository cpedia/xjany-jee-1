package com.lti.service.bo.base;

import java.io.Serializable;
import java.util.List;

public abstract class BaseAssetClass implements Serializable{

	protected java.lang.Long ID;

	protected java.lang.Long BenchmarkID;

	protected List<com.lti.type.Pair> DynamicAttribute;

	protected java.lang.String Name;

	protected java.lang.Long ParentID;

	protected List<com.lti.type.Pair> StaticAttribute;

	public java.lang.Long getID() {
		return ID;
	}

	public void setID(java.lang.Long id) {
		ID = id;
	}

	public java.lang.Long getBenchmarkID() {
		return BenchmarkID;
	}

	public void setBenchmarkID(java.lang.Long benchmarkID) {
		BenchmarkID = benchmarkID;
	}

	public List<com.lti.type.Pair> getDynamicAttribute() {
		return DynamicAttribute;
	}

	public void setDynamicAttribute(List<com.lti.type.Pair> dynamicAttribute) {
		DynamicAttribute = dynamicAttribute;
	}

	public java.lang.String getName() {
		return Name;
	}

	public void setName(java.lang.String name) {
		Name = name;
	}

	public java.lang.Long getParentID() {
		return ParentID;
	}

	public void setParentID(java.lang.Long parentID) {
		ParentID = parentID;
	}

	public List<com.lti.type.Pair> getStaticAttribute() {
		return StaticAttribute;
	}

	public void setStaticAttribute(List<com.lti.type.Pair> staticAttribute) {
		StaticAttribute = staticAttribute;
	}
	


}