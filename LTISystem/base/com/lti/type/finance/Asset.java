package com.lti.type.finance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lti.service.bo.AssetClass;
import com.lti.service.bo.HoldingItem;
import com.lti.type.executor.StrategyBasicInf;

public class Asset implements Serializable,Cloneable{

	protected static final long serialVersionUID = 1L;
	
	protected java.lang.String Name;

	protected double TargetPercentage=0.0;

	protected java.lang.Long AssetClassID;
	
	protected String AssetClassName;

	protected double Amount=0.0;
	
	protected java.util.List<HoldingItem> HoldingItems=new ArrayList<HoldingItem>();
	
	protected double Percentage;
	
	

	public java.lang.String getName() {
		return Name;
	}

	public void setName(java.lang.String name) {
		Name = name;
	}

	public double getTargetPercentage() {
		return TargetPercentage;
	}

	public void setTargetPercentage(double targetPercentage) {
		TargetPercentage = targetPercentage;
	}

	public java.lang.Long getAssetClassID() {
		return AssetClassID;
	}

	public void setAssetClassID(java.lang.Long assetClassID) {
		AssetClassID = assetClassID;
	}

	public double getAmount() {
		return Amount;
	}

	public void setAmount(double amount) {
		Amount = amount;
	}


	public double getPercentage() {
		return Percentage;
	}

	public void setPercentage(double percentage) {
		Percentage = percentage;
	}

	public String getAssetClassName() {
		return AssetClassName;
	}

	public void setAssetClassName(String assetClassName) {
		AssetClassName = assetClassName;
	}


	public java.util.List<HoldingItem> getHoldingItems() {
		return HoldingItems;
	}

	public void setHoldingItems(java.util.List<HoldingItem> holdingItems) {
		HoldingItems = holdingItems;
	}

	public HoldingItem getHoldingItem(String symbol){
		for(HoldingItem hi:HoldingItems){
			if(hi.getSymbol().equalsIgnoreCase(symbol))return hi;
		}
		return null;
	}
	
	public double getSecurityShare(String symbol){
		HoldingItem hi = getHoldingItem(symbol);
		if(hi ==  null)
			return 0.0;
		return hi.getShare();
	}
	
	public double getSecurityAmount(String symbol){
		HoldingItem hi=getHoldingItem(symbol);
		if(hi==null)return 0.0;
		return hi.getPrice()*hi.getShare();
	}
	
	public List<String> getSymbols(){
		List<String> symbols=new ArrayList<String>();
		for(HoldingItem hi:HoldingItems){
			symbols.add(hi.getSymbol());
		}
		return symbols;
	}
	
	public Asset clone(){
		Asset o = null;
		try {
			o = (Asset) super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}
		
		List<HoldingItem> list=new ArrayList<HoldingItem>();
		
		if(getHoldingItems()!=null){
			Iterator<HoldingItem> iter=getHoldingItems().iterator();
			while(iter.hasNext()){
				list.add((HoldingItem)iter.next().clone());
			}
		}
		o.setHoldingItems(list);
		return o;
	}
	@Deprecated
	public void setAssetStrategyID(long strategyID) {
		
	}

	public void setAssetClass(AssetClass assetClassID2) {
		this.setAssetClassID(assetClassID2.getID());
		this.setAssetClassName(assetClassID2.getName());
	}
	@Deprecated
	public void setClassID(AssetClass assetClassID2) {
		if(assetClassID2!=null){
			this.setAssetClassID(assetClassID2.getID());
			this.setAssetClassName(assetClassID2.getName());
		}else{
			this.setAssetClassID(0l);
			this.setAssetClassName("ROOT");
		}
		
	}
	
	@Deprecated
	public void setClassID(Long l) {
		this.setAssetClassID(l);
		this.setAssetClassName(""+l);
		
	}
	

}