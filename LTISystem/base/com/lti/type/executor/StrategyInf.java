package com.lti.type.executor;

import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import com.lti.type.executor.iface.IStrategyInfProcessor;

public class StrategyInf implements Serializable{

	private static final long serialVersionUID = 1L;

	protected StrategyBasicInf AssetAllocationStrategy=new StrategyBasicInf();

	protected StrategyBasicInf CashFlowStrategy=new StrategyBasicInf();

	protected StrategyBasicInf RebalancingStrategy=new StrategyBasicInf();
	
	public List<StrategyBasicInf> getAssetStrategies() {
		return AssetStrategies;
	}

	public void setAssetStrategies(List<StrategyBasicInf> assetStrategies) {
		AssetStrategies = assetStrategies;
	}

	public List<Object> process(IStrategyInfProcessor processor) {

		List<Object> objs = new ArrayList<Object>();
		objs.add(processor.process(AssetAllocationStrategy));
		objs.add(processor.process(CashFlowStrategy));
		objs.add(processor.process(RebalancingStrategy));
		if (AssetStrategies != null) {
			for (int i = 0; i < AssetStrategies.size(); i++) {
				objs.add(processor.process(AssetStrategies.get(i)));
			}
		}

		return objs;
	}
	protected List<StrategyBasicInf> AssetStrategies=new ArrayList<StrategyBasicInf>();
	
	

	public StrategyBasicInf getAssetAllocationStrategy() {
		return AssetAllocationStrategy;
	}

	public void setAssetAllocationStrategy(StrategyBasicInf assetAllocationStrategy) {
		AssetAllocationStrategy = assetAllocationStrategy;
	}

	public StrategyBasicInf getCashFlowStrategy() {
		return CashFlowStrategy;
	}

	public void setCashFlowStrategy(StrategyBasicInf cashFlowStrategy) {
		CashFlowStrategy = cashFlowStrategy;
	}

	public StrategyBasicInf getRebalancingStrategy() {
		return RebalancingStrategy;
	}

	public void setRebalancingStrategy(StrategyBasicInf rebalancingStrategy) {
		RebalancingStrategy = rebalancingStrategy;
	}
	
	public String toXML() {
		try {
			StringWriter sw = new StringWriter();
			Marshaller marshaller = new Marshaller(sw);
			marshaller.marshal(this);
			return sw.toString();
		} catch (Exception e) {
		}
		return "";
	}
	
	public String toInformation(){
		final StringBuffer sb=new StringBuffer();
		process(new IStrategyInfProcessor() {
			
			@Override
			public Object process(StrategyBasicInf bi) {
				sb.append("Strategy Name: ");
				sb.append(bi.getName());
				sb.append("\r\nStrategyID: ");
				sb.append(bi.getID());
				if(bi.getAssetName()!=null){
					sb.append("\r\nAsset Name: ");
					sb.append(bi.getAssetName());
				}
				sb.append("\r\n\r\n");
				return "";
			}
		});
		return sb.toString();
	}

	public static StrategyInf getInstance(Reader reader) {
		try {
			Unmarshaller unmarshaller = new Unmarshaller(StrategyInf.class);
			unmarshaller.setWhitespacePreserve(true);
			StrategyInf hinf = (StrategyInf) unmarshaller.unmarshal(reader);
			return hinf;
		} catch (Exception e) {
		}
		return null;
	}

	public StrategyBasicInf getAssetStrategy(String name) {
		for(int i=0;i<AssetStrategies.size();i++){
			if(AssetStrategies.get(i).getAssetName()!=null&&AssetStrategies.get(i).getAssetName().trim().equals(name.trim()))return AssetStrategies.get(i);
		}
		return null;
	}

	public StrategyBasicInf getStrategy(long strategyID) {
		if(this.AssetAllocationStrategy.getID()==strategyID)return this.AssetAllocationStrategy;
		if(this.CashFlowStrategy.getID()==strategyID)return this.CashFlowStrategy;
		if(this.RebalancingStrategy.getID()==strategyID)return this.RebalancingStrategy;
		if(this.AssetStrategies!=null){
			for(StrategyBasicInf sbi:this.AssetStrategies){
				if(sbi.getID()==strategyID)return sbi;
			}
		}
		return null;
	}
}
