package com.lti.type.finance;

import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import com.lti.Exception.Executor.AssetErrorException;
import com.lti.Exception.Executor.OperationErrorException;
import com.lti.Exception.Executor.SimulateException;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.HoldingRecord;
import com.lti.service.bo.Transaction;
import com.lti.system.Configuration;
import com.lti.type.executor.iface.ITransactionProcessor;
import com.lti.util.FileOperator;
import com.lti.util.FormatUtil;
import com.lti.util.LTIDate;

/**
 *
 *　由于序列化的一些问题，此类添加add/set/get函数时需要注意
 *
 */
public class HoldingInf implements Serializable,Cloneable {

	private static final long serialVersionUID = 4900706009119122361L;

	private final double ERROR_PERCENTAGE = 0.01 / 10000.0;

	protected java.util.Date CurrentDate;

	public HoldingInf(long portfolioID,double cash,Date date) {
		this.PortfolioID=portfolioID;
		this.Cash=cash;
		this.Amount=this.Cash;
		this.CurrentDate=date;
	}

	protected double Cash = 0.0;

	protected double Amount = 0.0;

	protected long BenchmarkID=14l;

	protected long PortfolioID;

	protected String PortfolioName;

	protected String BenchmarkSymbol;

	protected List<Asset> Assets = new ArrayList<Asset>();
	
	protected List<HoldingRecord> holdingRecords = new ArrayList<HoldingRecord>();
	
	
	protected List<Transaction> personalTransactions;
	
	public List<Transaction> getPersonalTransactions() {
		return personalTransactions;
	}

	public void setPersonalTransactions(List<Transaction> personalTransactions) {
		this.personalTransactions = personalTransactions;
	}

	public List<HoldingItem> getHoldingItems(){
		List<HoldingItem> his=new ArrayList<HoldingItem>();
		if(this.Assets!=null&&this.Assets.size()!=0){
			for(Asset a:this.Assets){
				if(a.getHoldingItems()!=null&&a.getHoldingItems().size()>0){
					his.addAll(a.getHoldingItems());
				}
			}
		}
		return his;
	}

	protected int PriceType = Configuration.PRICE_TYPE_CLOSE;
	
	public double getCash() {
		return Cash;
	}

	public void setCash(double cash) {
		Cash = cash;
	}

	public double getAmount() {
		return Amount;
	}

	public void setAmount(double amount) {
		Amount = amount;
	}

	public long getBenchmarkID() {
		return BenchmarkID;
	}

	public void setBenchmarkID(long benchmarkID) {
		BenchmarkID = benchmarkID;
	}

	public List<Asset> getAssets() {
		return Assets;
	}

	public void setAssets(List<Asset> assets) {
		Assets = assets;
	}

	public String toXML() {
		try {
			List<HoldingRecord> holdingRecords_bk = this.holdingRecords;
			this.setHoldingRecords(null);
			StringWriter sw = new StringWriter();
			Marshaller marshaller = new Marshaller(sw);
			marshaller.marshal(this);
			this.setHoldingRecords(holdingRecords_bk);
			return sw.toString();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return "";
	}
	
	public HoldingInf(){
		this(0, 0.0, new Date());
	}

	public HoldingInf(Portfolio p) {
		this(p.getID(),10000.0,p.getStartingDate());
		this.setPortfolioName(p.getName());
	}

	public static HoldingInf getInstance(Reader reader) {
		try {
			Unmarshaller unmarshaller = new Unmarshaller(HoldingInf.class);
			unmarshaller.setWhitespacePreserve(true);
			HoldingInf hinf = (HoldingInf) unmarshaller.unmarshal(reader);
			return hinf;
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) throws Exception{
		String xml=FileOperator.getContent("f:/test.txt");
		System.out.println(HoldingInf.getInstance(new StringReader(xml)).getAmount());
		System.out.println(HoldingInf.getInstance(new StringReader(xml)).toXML());
	}
	public long getPortfolioID() {
		return PortfolioID;
	}

	public void setPortfolioID(long portfolioID) {
		PortfolioID = portfolioID;
	}

	public String getPortfolioName() {
		return PortfolioName;
	}

	public void setPortfolioName(String portfolioName) {
		PortfolioName = portfolioName;
	}

	public String getBenchmarkSymbol() {
		return BenchmarkSymbol;
	}

	public void setBenchmarkSymbol(String benchmarkSymbol) {
		BenchmarkSymbol = benchmarkSymbol;
	}

	public ITransactionProcessor getTransactionProcessor() {
		return transactionProcessor;
	}

	public void setTransactionProcessor(ITransactionProcessor transactionProcessor) {
		this.transactionProcessor = transactionProcessor;
	}

	private boolean containsAsset(String assetname) {
		for (Asset asset : Assets) {
			if (asset.getName().equals(assetname))
				return true;
		}
		return false;
	}

	public Asset getAsset(String assetname) {
		for (Asset asset : Assets) {
			if (asset.getName().equals(assetname))
				return asset;
		}
		return null;
	}

	public void addAsset(Asset asset) throws SimulateException {
		if (asset == null || asset.getName() == null || asset.getName().equals("")) {
			throw new AssetErrorException("Asset is null or name is null!", PortfolioID, PortfolioName);
		}

		if (this.Assets == null) {
			this.Assets = new ArrayList<Asset>();
		}

		if (!containsAsset(asset.getName())) {
			this.Assets.add(asset);
			return;
		} else {
			Asset oa=this.getAsset(asset.getName());
			oa.setTargetPercentage(asset.getTargetPercentage());
			oa.setAssetClassID(asset.getAssetClassID());
			oa.setAssetClassName(asset.getAssetClassName());
		}
	}

	private ITransactionProcessor transactionProcessor;


	public void deposit(double amount) throws SimulateException {
		this.Cash += amount;
		this.Amount += amount;
		addTransaction(PortfolioID, null, 0l, null, null, CurrentDate, Configuration.DEPOSIT, amount, 1, amount / Amount, Configuration.TRANSACTION_TYPE_REAL);
	}

	@Deprecated
	public void deposit(double amount, Date date) throws SimulateException {
		deposit(amount);

	}

	@Deprecated
	public double calculateTotalAmount(Date curDate) {
		return this.Amount;
	}


	public void withdraw(double amount) throws SimulateException {
		try {
			if (this.Cash + this.Amount * ERROR_PERCENTAGE < amount) {
				throw new OperationErrorException("Cannot perform the 'withdray' operation, out of cash.", PortfolioID, PortfolioName, Configuration.WITHDRAW, amount, CurrentDate);
			}
			this.Cash -= amount;
			this.Amount -= amount;
			addTransaction(PortfolioID, null, 0l, null, null, CurrentDate, Configuration.WITHDRAW, amount, 1,amount / Amount, Configuration.TRANSACTION_TYPE_REAL);
		} catch (Exception e) {
			throw new OperationErrorException("Cannot perform the 'withdray' operation.", e, PortfolioID, PortfolioName, Configuration.WITHDRAW, amount, CurrentDate);
		}
	}

	@Deprecated
	public void withdraw(double amount, Date date) throws SimulateException {
		withdraw(amount);

	}

	public double getTargetPercentage(String assetName) {
		try {
			return this.getAsset(assetName).getTargetPercentage();
		} catch (Exception e) {
		}
		return 0.0;
	}

	@Deprecated
	public List<Asset> getAssetList(String name, Date date) {
		return this.Assets;
	}

	@Deprecated
	public double getSecurityAmount(String assetName, String symbol, Date date1) {
		return getSecurityAmount(assetName, symbol);
	}

	public double getSecurityAmount(String assetName, String symbol) {
		Asset asset = this.getAsset(assetName);
		if (asset == null)
			return 0.0;

		return asset.getSecurityAmount(symbol);
	}
	
	public double getSecurityShare(String assetName, String symbol){
		Asset asset = this.getAsset(assetName);
		if(asset == null)
			return 0.0;
		return asset.getSecurityShare(symbol);
	}
	
	public HoldingItem getHoldingItem(String assetName, String symbol){
		Asset asset = this.getAsset(assetName);
		if(asset == null)
			return null;
		return asset.getHoldingItem(symbol);
	}
	
	public List<String> getSymbols() {
		List<String> symbols = new ArrayList<String>();
		for (Asset a : Assets) {
			symbols.addAll(a.getSymbols());
		}
		return symbols;
	}

	public static class compareToSecurityItem implements Comparator<HoldingInf> {
		public int compare(HoldingInf s1, HoldingInf s2) {
			int rulst = s1.getPortfolioID() > s2.getPortfolioID() ? 1 : (s1.getPortfolioID() == s2.getPortfolioID() ? 0 : -1);
			return rulst;
		}
	}

	public int compareTo(Object o) {
		int result;
		HoldingInf s = (HoldingInf) o;
		if (s == null)
			return 1;

		result = getPortfolioID() > s.getPortfolioID() ? 1 : (getPortfolioID() == s.getPortfolioID() ? 0 : -1);
		return result;
	}

	@Deprecated
	public String getInformation(Date pdate) {
		return getInformation();
	}

	public String getInformation() {
		StringBuffer sb = new StringBuffer();
		sb.append("[Portfolio " + this.getPortfolioID() + " ] " + this.getPortfolioName() + "\n");
		sb.append("\t[Date]" + CurrentDate + "\n");
		sb.append("\t[Total Amount]" + Amount + "\n");
		sb.append("\t[Cash]" + this.getCash() + "\n");
		if (this.Assets != null) {
			for (int i = 0; i < Assets.size(); i++) {
				Asset a = Assets.get(i);
				// a.getAmount();
				sb.append("\t[Asset " + i + " ] [Name: " + a.getName());
				sb.append("][Amount:" + a.getAmount() + "]\n");
				if (a.getHoldingItems() != null) {
					for (int j = 0; j < a.getHoldingItems().size(); j++) {
						HoldingItem hi = a.getHoldingItems().get(j);
						String perTotal = FormatUtil.formatPercentage(hi.getPercentage());
						String perAsset = FormatUtil.formatPercentage(hi.getShare() * hi.getPrice() / a.getAmount());
						sb.append("\t\t[Security " + j + "][ID:" + hi.getSecurityID() + "][Symbol:" + hi.getSymbol() + "][shares:" + hi.getShare() + "][price:" + hi.getPrice() + "][total:" + hi.getShare() * hi.getPrice() + "]");
						sb.append("[perAsset:" + perAsset + "]");
						sb.append("[perPort:" + perTotal + "]\n");
					}
				}
			}
		}
		return sb.toString();
	}

	public java.lang.String getPortfolioSymbol() {
		return "P_" + this.PortfolioID;
	}

	public HoldingInf clone() {
		HoldingInf o = null;
		try {
			o = (HoldingInf) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}

		List<Asset> list = new ArrayList<Asset>();

		if (getAssets() != null) {
			Iterator<Asset> iter = getAssets().iterator();
			while (iter.hasNext()) {
				list.add((Asset) iter.next().clone());
			}
		}
		o.setAssets(list);
		
		List<HoldingRecord> holdingRecords = new ArrayList<HoldingRecord>();
		if(this.getHoldingRecords() != null){
			for(HoldingRecord hr: this.getHoldingRecords())
				holdingRecords.add(hr.clone());
		}
		o.setHoldingRecords(holdingRecords);
		
		List<Transaction> trs=new ArrayList<Transaction>();
		if(this.personalTransactions!=null&&this.personalTransactions.size()>0){
			for(int i=0;i<this.personalTransactions.size();i++)
				trs.add(getPersonalTransactions().get(i).clone());
			o.setPersonalTransactions(trs);
		}
		
		return o;
	}

	public int getTotalPosition() {
		int total = 0;
		if (this.Assets != null) {
			for (Asset a : Assets) {
				total += a.getHoldingItems().size();
			}
		}
		return total;
	}

	private double[] top10;

	private void insertTop10(double d) {
		int target = -1;
		for (int i = 0; i < top10.length; i++) {
			if (d > top10[i]) {
				target = i;
				break;
			}
		}

		if (target != -1) {
			for (int i = top10.length - 1; i > target; i--) {
				top10[i] = top10[i - 1];
			}
			top10[target] = d;
		}
	}

	public double getTop10() {
		top10 = new double[10];
		if (this.Assets != null) {
			Iterator<Asset> iter_asset = this.Assets.iterator();
			while (iter_asset.hasNext()) {
				Asset a = iter_asset.next();
				if (a.getHoldingItems() != null) {
					Iterator<HoldingItem> iter_si = a.getHoldingItems().iterator();
					while (iter_si.hasNext()) {
						HoldingItem si = iter_si.next();
						double securityamount = si.getShare() * si.getPrice();
						insertTop10(securityamount);
					}
				}
			}
		}
		double d = 0.0;
		for (int i = 0; i < top10.length; i++) {
			d += top10[i];
		}
		return d / this.Amount;
	}

	@Deprecated
	public double getTop10(Date date) {
		return getTop10();
	}
	/**
	 * we sell the holding item one by one
	 * @param assetName
	 * @throws SimulateException
	 */
	public void sellAsset(String assetName) throws SimulateException {
		Asset asset = this.getAsset(assetName);
		if (asset == null) {
			throw new OperationErrorException("Cannot perform the 'sell asset' operation.", PortfolioID, PortfolioName, Configuration.TRANSACTION_SELL_ASSET, 0.0, CurrentDate);
		}
		for(HoldingItem hi: asset.getHoldingItems())
			this.baseSell(asset.getName(), hi.getSecurityID(), hi.getSymbol(), hi.getShare() * hi.getPrice(), Configuration.TRANSACTION_TYPE_REAL);
		asset.setAmount(0.0);
	}

	public void sellAssetCollection() throws SimulateException {
		for(Asset asset: this.Assets){
			sellAsset(asset.getName());
		}
		//if(this.Assets!=null)this.Assets.clear();
	}
	
	public void sellAndDelAsset(String assetname) throws SimulateException {
		for(Asset asset: this.Assets){
			sellAsset(asset.getName());
		}
		this.Assets.clear();
	}

	public void shortSellAtOpen(String assetName, long securityID, String symbol, double amount, double close, double open) throws SimulateException {
		switchPrice(Configuration.PRICE_TYPE_OPEN);
		this.shortSell(assetName, securityID, symbol, amount, close, open);
		switchPrice(Configuration.PRICE_TYPE_CLOSE);
	}
	
	public void sellAssetAtOpen(String assetName) throws SimulateException {
		switchPrice(Configuration.PRICE_TYPE_OPEN);
		this.sellAsset(assetName);
		switchPrice(Configuration.PRICE_TYPE_CLOSE);
	}
		
	public void shortSell(String assetName, long securityID, String symbol, double amount, double close, double open) throws SimulateException {
		String operation = null;
		if (PriceType == Configuration.PRICE_TYPE_CLOSE)
			operation = Configuration.TRANSACTION_SHORT_SELL;
		else if (PriceType == Configuration.PRICE_TYPE_OPEN)
			operation = Configuration.TRANSACTION_SHORT_SELL_AT_OPEN;

		amount = -amount;
		double price = 0.0;
		if (this.PriceType == Configuration.PRICE_TYPE_CLOSE) {
			price = close;
		} else if (this.PriceType == Configuration.PRICE_TYPE_OPEN) {
			price = open;
		} else {
			throw new OperationErrorException("Cannot perform the '" + operation + "' operation, price type error.", PortfolioID, PortfolioName, operation, 0.0, CurrentDate);
		}
		double shareDouble = amount / price;
		double share = -Math.floor(-shareDouble);
		amount = share * price;
		if (shareDouble != share) {
			if (transactionProcessor != null) {
				transactionProcessor.addLog(PortfolioID, CurrentDate, "Share Number is not a Integer!\r\n" + "Share Number:" + shareDouble + "\r\n");
			}
		}
		Asset shortAsset = getAsset(assetName);
		if (shortAsset == null) {
			throw new OperationErrorException("Cannot perform the 'short asset' operation.", PortfolioID, PortfolioName, Configuration.TRANSACTION_SELL_ASSET, 0.0, CurrentDate);

		}

		boolean flag = false;
		if (shortAsset.getHoldingItems() != null) {
			java.util.Iterator<HoldingItem> iter_s = shortAsset.getHoldingItems().iterator();
			while (iter_s.hasNext()) {
				HoldingItem si = iter_s.next();
				if (si.getSecurityID() == securityID) {
					flag = true;
					si.setShare(si.getShare() + share);
					this.setCash(this.getCash() - amount);
					addTransaction(PortfolioID, securityID, 0l, symbol, assetName, CurrentDate, operation, amount, share, amount / this.Amount, Configuration.TRANSACTION_TYPE_REAL);
					return;
				}
			}
		} else {
			List<HoldingItem> newItems = new ArrayList<HoldingItem>();
			shortAsset.setHoldingItems(newItems);
		}
		if (flag == false) {
			HoldingItem newSI = new HoldingItem();
			newSI.setShare(new Double(share));
			newSI.setSecurityID(securityID);
			newSI.setAssetName(assetName);
			newSI.setClose(close);
			newSI.setDate(CurrentDate);
			newSI.setLastDividendDate(null);
			newSI.setOpen(open);
			newSI.setPercentage(amount / this.Amount);
			newSI.setPortfolioID(PortfolioID);
			newSI.setPrice(price);
			newSI.setReInvest(false);
			newSI.setSymbol(symbol);
			this.setCash(this.getCash() - amount);
			shortAsset.getHoldingItems().add(newSI);
			addTransaction(PortfolioID, securityID, 0l, symbol, assetName, CurrentDate, operation, amount, share, amount / this.Amount, Configuration.TRANSACTION_TYPE_REAL);
			return;
		}
	}

	public void shortSellByShareNumber(String assetName, long securityID, String symbol, double share, double close, double open) throws SimulateException {
		String operation = null;
		if (PriceType == Configuration.PRICE_TYPE_CLOSE)
			operation = Configuration.TRANSACTION_SHORT_SELL;
		else if (PriceType == Configuration.PRICE_TYPE_OPEN)
			operation = Configuration.TRANSACTION_SHORT_SELL_AT_OPEN;

		double price = 0.0;
		if (this.PriceType == Configuration.PRICE_TYPE_CLOSE) {
			price = close;
		} else if (this.PriceType == Configuration.PRICE_TYPE_OPEN) {
			price = open;
		} else {
			throw new OperationErrorException("Cannot perform the '" + operation + "' operation, price type error.", PortfolioID, PortfolioName, operation, 0.0, CurrentDate);
		}
		double amount = price * share;
		this.shortSell(assetName, securityID, symbol, amount, close, open);

	}

	public void buyAtNextOpen(String assetname, long securityID, String symbol, double amount, double open, double close, boolean reinvest)throws SimulateException{
		switchPrice(Configuration.PRICE_TYPE_OPEN);
		this.baseBuy(assetname, securityID, symbol, amount, open, close, reinvest, Configuration.TRANSACTION_TYPE_SCHEDULE);
		switchPrice(Configuration.PRICE_TYPE_CLOSE);
	}
	public void sellAtNextOpen(String assetname, long securityID, String symbol, double amount) throws SimulateException {
		switchPrice(Configuration.PRICE_TYPE_OPEN);
		this.baseSell(assetname, securityID, symbol, amount, Configuration.TRANSACTION_TYPE_SCHEDULE);
		switchPrice(Configuration.PRICE_TYPE_CLOSE);
	}
	public void buyAtOpen(String assetname, long securityID, String symbol, double amount, double open, double close, boolean reinvest)throws SimulateException{
		switchPrice(Configuration.PRICE_TYPE_OPEN);
		this.baseBuy(assetname, securityID, symbol, amount, open, close, reinvest, Configuration.TRANSACTION_TYPE_REAL);
		switchPrice(Configuration.PRICE_TYPE_CLOSE);
	}
	public void sellAtOpen(String assetname, long securityID, String symbol, double amount) throws SimulateException {
		switchPrice(Configuration.PRICE_TYPE_OPEN);
		this.baseSell(assetname, securityID, symbol, amount, Configuration.TRANSACTION_TYPE_REAL);
		switchPrice(Configuration.PRICE_TYPE_CLOSE);
	}
	//CD：这里少了一个set share
	public void changeSmallCashToCASH(String assetName, Long cashID, String cashSymbol, double share, boolean reinvest){
		//if cash, share = amount
		Asset asset = this.getAsset(assetName);
		if(asset != null){
			HoldingItem hi = asset.getHoldingItem(cashSymbol);
			if(hi != null){
				hi.setShare(hi.getShare() + share);
				hi.setReInvest(reinvest);
			}else{
				hi = new HoldingItem();
				hi.setAssetName(assetName);
				hi.setSecurityID(cashID);
				hi.setSymbol(cashSymbol);
				hi.setAssetName(assetName);
				hi.setClose(1.0);
				hi.setDate(CurrentDate);
				hi.setLastDividendDate(null);
				hi.setOpen(1.0);
				hi.setPercentage(share / this.Amount);
				hi.setPortfolioID(PortfolioID);
				hi.setPrice(1.0);
				hi.setShare(share);
				hi.setReInvest(reinvest);
				hi.setDividend(0.0);
				asset.getHoldingItems().add(hi);
			}
			asset.setAmount(asset.getAmount() + share);
			addTransaction(PortfolioID, cashID, 0l, cashSymbol, assetName, CurrentDate, Configuration.TRANSACTION_REINVEST, share, share, share / this.Amount, Configuration.TRANSACTION_TYPE_REINVEST);
		}else{//为了适应那些非TAA，SAA的策略，他们没有ASSET CASH
			this.Cash += share;
		}
		
	}
	
	
	public void baseBuy(String assetName, long securityID, String symbol, double amount, double open, double close, boolean reinvest, int transactionType) throws SimulateException {
		baseBuy(assetName, securityID, symbol, amount, open, close, reinvest, transactionType, true);
	}
	/**
	 * 
	 * @param assetName
	 * @param securityID
	 * @param symbol
	 * @param amount
	 * @param open
	 * @param close
	 * @param reinvest
	 * @param transactionType
	 * @param addTransactionFlag 如果为false,说明是计算expected holding的时候调用，生成的transaction不保存，否则，保存transaction
	 * @throws SimulateException
	 */
	public void baseBuy(String assetName, long securityID, String symbol, double amount, double open, double close, boolean reinvest, int transactionType, boolean addTransactionFlag) throws SimulateException {
		
		double price = 0.0;
		String operation = null;
		if (this.PriceType == Configuration.PRICE_TYPE_OPEN) {
			operation = Configuration.TRANSACTION_BUY_AT_OPEN;
			price = open;
		} else if (this.PriceType == Configuration.PRICE_TYPE_CLOSE) {
			operation = Configuration.TRANSACTION_BUY;
			price = close;
		}
		if (transactionType == Configuration.TRANSACTION_TYPE_SCHEDULE) {
			price = close;
			operation = Configuration.TRANSACTION_BUY_AT_OPEN;
		}
		if (Double.isNaN(amount)) {
			throw new OperationErrorException("Cannot perform the '" + operation + "' operation, the amount is "+amount+".", PortfolioID, PortfolioName, operation, 0.0, CurrentDate);

		}
		
		if(amount==0.0)return;
		
		Asset asset = this.getAsset(assetName);
		if (asset == null) {
			throw new OperationErrorException("Cannot perform the '" + operation + "' operation, cannot find the asset.", PortfolioID, PortfolioName, operation, 0.0, CurrentDate);
		}
		HoldingItem hi = asset.getHoldingItem(symbol);
		
		double buyShare = amount / price;
		//System.out.println("buy: " + assetName + " " + symbol + " " + amount + " " + buyShare);
		if (transactionType == Configuration.TRANSACTION_TYPE_REAL) {
			if ((this.getCash() - amount) < -ERROR_PERCENTAGE * this.Amount) {
				throw new OperationErrorException("Cannot perform the '" + operation + "' operation, out of cash. cash:"+this.Cash+", amount:"+amount + ",portfolio amount:" + this.Amount + ",date:" + CurrentDate + ",securityID:" + securityID, PortfolioID, PortfolioName, operation, 0.0, CurrentDate);

			}
			if (hi != null) {
				hi.setShare(hi.getShare() + buyShare);
				hi.setReInvest(reinvest);
			} else {
				HoldingItem newSI = new HoldingItem();
				newSI.setShare(buyShare);
				newSI.setSecurityID(securityID);
				newSI.setSymbol(symbol);
				
				newSI.setAssetName(assetName);
				newSI.setClose(close);
				newSI.setDate(CurrentDate);
				newSI.setLastDividendDate(null);
				newSI.setOpen(open);
				newSI.setPercentage(amount / this.Amount);
				newSI.setPortfolioID(PortfolioID);
				newSI.setPrice(price);
				newSI.setReInvest(reinvest);
				newSI.setDividend(0.0);
				asset.getHoldingItems().add(newSI);
				if(addTransactionFlag && securityID != Configuration.CASHID){
					//当前没有持有该security, 生成一条新的holding record记录,注意，cash的dividend是每天都做的，不用生成记录
					this.addHoldingRecord(securityID);
				}
			}
			this.setCash(this.getCash() - amount);
			asset.setAmount(asset.getAmount()+amount);
		}
		if(addTransactionFlag)
			addTransaction(PortfolioID, securityID, 0l, symbol, assetName, CurrentDate, operation, amount, buyShare, amount / this.Amount, transactionType);
	}
	
	public void baseSell(String assetname, long securityID, String symbol, double amount, int transactionType) throws SimulateException {
		baseSell(assetname, securityID, symbol, amount, transactionType, true);
	}
	/**
	 * 
	 * @param assetname
	 * @param securityID
	 * @param symbol
	 * @param amount
	 * @param transactionType
	 * @param addTransactionFlag 如果为false,说明是计算expected holding的时候调用，生成的transaction不保存，否则，保存transaction
	 * @throws SimulateException
	 */
	public void baseSell(String assetname, long securityID, String symbol, double amount, int transactionType, boolean addTransactionFlag) throws SimulateException {
		String operation = null;
		if (this.PriceType == Configuration.PRICE_TYPE_OPEN) {
			operation = Configuration.TRANSACTION_SELL_AT_OPEN;
		} else if (this.PriceType == Configuration.PRICE_TYPE_CLOSE) {
			operation = Configuration.TRANSACTION_SELL;
		}
		Asset asset = this.getAsset(assetname);
		if (asset == null) {
			throw new OperationErrorException("Cannot perform the '" + operation + "' operation, cannot find the asset["+assetname+"].", PortfolioID, PortfolioName, operation, 0.0, CurrentDate);
		}
		HoldingItem hi = asset.getHoldingItem(symbol);
		if (hi == null) {
			throw new OperationErrorException("Cannot perform the '" + operation + "' operation, cannot find the holding item ["+asset.getName()+"]["+symbol+"].\n"+this.getInformation(), PortfolioID, PortfolioName, operation, 0.0, CurrentDate);
		}

		double price = 0.0;

		if (this.PriceType == Configuration.PRICE_TYPE_OPEN) {
			price = hi.getOpen();
		} else if (this.PriceType == Configuration.PRICE_TYPE_CLOSE) {
			price = hi.getClose();
		}

		if (transactionType == Configuration.TRANSACTION_TYPE_SCHEDULE) {
			price = hi.getClose();
			operation = Configuration.TRANSACTION_SELL_AT_OPEN;
		}
		double sellShare = amount / price;
		double delta = Math.abs(hi.getShare() * ERROR_PERCENTAGE);
		//System.out.println("sell: " + assetname + " " + symbol + " " + amount + " " + sellShare);
		if(hi.getShare() - sellShare < delta && hi.getShare() - sellShare > -delta){
			sellShare = hi.getShare();
			amount = sellShare * price;
		}
		if (hi.getShare() < sellShare) {
			throw new OperationErrorException("Cannot perform the '" + operation + "' operation, sell shares: "+sellShare+", shares: "+hi.getShare()+"." + "date: " + CurrentDate + " symbol: " + symbol, PortfolioID, PortfolioName, operation, 0.0, CurrentDate);
		}
		if (transactionType == Configuration.TRANSACTION_TYPE_REAL) {
			double leftShare = hi.getShare() - sellShare;
			if (leftShare / hi.getShare() < 0.05 || leftShare < 1) {
				leftShare = 0;
				sellShare = hi.getShare();
			}
			hi.setShare(leftShare);
			amount = sellShare * price;
			this.setCash(this.getCash() + amount);
			if(addTransactionFlag && leftShare == 0){
				//将关于该security的最新的holding record的记录的enddate设置为今天
				this.updateLatestHoldingRecord(securityID);
			}
		}
		if(addTransactionFlag)
			addTransaction(PortfolioID, securityID, 0l, symbol, assetname, CurrentDate, operation, amount, sellShare, amount / this.Amount, transactionType);
	}
	public void addTransaction(long portfolioID, Long securityID, long strategyID, String symbol, String assetname, Date date, String operation, double amount, double share, double percentage, int transactionType){
		if(transactionProcessor!=null)
			transactionProcessor.addTransaction(portfolioID, securityID, strategyID, symbol, assetname, date, operation, amount, share, percentage, transactionType);
	}
	public void doReinvestByTransaction(Transaction transaction){
		for(Asset asset: this.Assets){
			if(asset.getName().equals(transaction.getAssetName())){
				for(HoldingItem hi: asset.getHoldingItems()){
					if(hi.getSecurityID().equals(transaction.getSecurityID())){
						hi.setShare(hi.getShare() + transaction.getShare());
						return;
					}
				}
			}
		}
	}
	
	public void doSplitByTransaction(Transaction transaction){
		for(Asset asset: this.Assets){
			if(asset.getName().equals(transaction.getAssetName())){
				for(HoldingItem hi: asset.getHoldingItems()){
					if(hi.getSecurityID().equals(transaction.getSecurityID())){
						hi.setShare(hi.getShare() * transaction.getShare());
						return;
					}
				}
			}
		}
	}
	/**
	 * do reinvest and then refresh amount
	 * 如果是reinvest后cashAdded是负的,那我们这里暂时先忽略不考虑
	 * @param preHoldingInf
	 */
	public void doReinvest(HoldingInf preHoldingInf){
		double cashAdded = 0;
		for (Asset asset : preHoldingInf.getAssets()) {
			for (HoldingItem preHi : asset.getHoldingItems()) {
				HoldingItem curHi = this.getHoldingItem(preHi.getAssetName(), preHi.getSymbol());
				if(curHi == null)
					continue;
				Double dividend = curHi.getDividend();
				if(dividend!=null && dividend != 0.0){
					double preShare = preHi.getShare();
					double curShare = curHi.getShare();
					double deltaShare = preShare - curShare;
					double price = curHi.getClose() - preHi.getDividend();
					double preLeftShare = preShare;
					if (preShare <= 0) // just sub some cash is ok.
						cashAdded += preShare * dividend;
					else{
						if(deltaShare > 0){//otherwise, we just buy more
							//we sell delta share, the dividend of this part return as cash
							if(deltaShare >= preShare)
								deltaShare = preShare;
							cashAdded += deltaShare * dividend;
							//we left preLeftShare, the dividend of this part return as new shares if reInvest is true,otherwise as cash
							preLeftShare = preShare - deltaShare;
						}
						double amount = preLeftShare * dividend;
						if(curHi.getReInvest() && amount > 0){
							double reinvestShare = amount / price;
							//这里不是preShare
							curHi.setShare(curShare + reinvestShare);
							//这个percentage不精确，因为this.Amount 中并没有考虑dividend部分的总值
							addTransaction(this.PortfolioID, curHi.getSecurityID(), 0l, curHi.getSymbol(), asset.getName(), curHi.getDate(), Configuration.TRANSACTION_REINVEST, amount, reinvestShare, amount / this.Amount, Configuration.TRANSACTION_TYPE_REINVEST);
						}else
							cashAdded += amount;
					}
				}
			}
		}
		if(cashAdded > 0){
			changeSmallCashToCASH("CASH", Configuration.CASHID, Configuration.CASH_SYMBOL, cashAdded, true);
		}
		clearEmptyHolding();
	}
	
	/**
	 * update the enddate of the latest holding record of security
	 * @param securityID
	 */
	public void updateLatestHoldingRecord(Long securityID){
		if(this.holdingRecords != null){
			for(HoldingRecord hr: holdingRecords){
				if(hr.getSecurityID().equals(securityID) && hr.getEndDate() == null){
					hr.setEndDate(CurrentDate);
					return;
				}
			}
		}
	}
	/**
	 * add a new holding record
	 * @param securityID
	 */
	public void addHoldingRecord(Long securityID){
		HoldingRecord hr = new HoldingRecord();
		hr.setPortfolioID(this.PortfolioID);
		hr.setSecurityID(securityID);
		hr.setStartDate(CurrentDate);
		holdingRecords.add(hr);
	}
	
	public void refreshAmounts() {
		double total = this.Cash;
		for (Asset asset : Assets) {
			double atotal = 0.0;
			Iterator<HoldingItem> it_holdingItem = asset.getHoldingItems().iterator();
			while(it_holdingItem.hasNext()){
				HoldingItem hi = it_holdingItem.next();
				hi.setDate(this.CurrentDate);
				double amount = hi.getShare() * hi.getPrice();
				atotal += amount;
			}
			asset.setAmount(atotal);
			total += atotal;
		}
		this.Amount = total;

		for (Asset asset : Assets) {
			asset.setPercentage(asset.getAmount() / this.Amount);
			for (HoldingItem hi : asset.getHoldingItems()) {
				hi.setPercentage(hi.getShare() * hi.getPrice() / this.Amount);
			}
		}
	}
	/**
	 * clear the holding item which amount is too small
	 */
	public void clearEmptyHolding(){
		for (Asset asset : Assets) {
			Iterator<HoldingItem> it_holdingItem = asset.getHoldingItems().iterator();
			while(it_holdingItem.hasNext()){
				HoldingItem hi = it_holdingItem.next();
				hi.setDate(this.CurrentDate);
				if(hi.getShare()==null||hi.getPrice()==null){
					System.out.println(hi.getSymbol());
				}
				double amount = hi.getShare() * hi.getPrice();
				if (amount < ERROR_PERCENTAGE * this.Amount && amount > -ERROR_PERCENTAGE * this.Amount) {
					it_holdingItem.remove();
					continue;
				}
			}
		}
		refreshAmounts();
	}

	private double getPrice(HoldingItem hi) {
		if (this.PriceType == Configuration.PRICE_TYPE_OPEN) {
			return hi.getOpen();

		} else if (this.PriceType == Configuration.PRICE_TYPE_CLOSE) {
			return hi.getClose();
		} else {
			return 0.0;
		}
	}

	public void switchPrice(int pricetype) {
		if(this.PriceType == pricetype)
			return;
		if (pricetype == Configuration.PRICE_TYPE_OPEN || pricetype == Configuration.PRICE_TYPE_CLOSE) {
			this.PriceType = pricetype;
			for (Asset asset : Assets) {
				for (HoldingItem hi : asset.getHoldingItems()) {
					hi.setPrice(getPrice(hi));
				}
			}
			refreshAmounts();
		}
	}

	public void balance() throws SimulateException {
		StringBuffer sb = new StringBuffer();
		sb.append("Before Balancing : \r\n");
		sb.append(this.getInformation());

		double leftAmount = this.Amount;
		Iterator<Asset> iter = this.getAssets().iterator();
		while (iter.hasNext()) {
			Asset asset = iter.next();
			if(asset.getHoldingItems()==null||asset.getHoldingItems().size()==0)continue;
			double targetAmount = this.Amount * asset.getTargetPercentage();
			if (targetAmount != 0.0 ) {
				leftAmount -= targetAmount;
				balance(asset, targetAmount);
			}
		}
		this.setCash(leftAmount);

		sb.append("\r\n\r\nAfter Balancing : \r\n");
		sb.append(this.getInformation());
		if (transactionProcessor != null) {
			transactionProcessor.addLog(PortfolioID, CurrentDate, sb.toString());
		}

	}

	private void balance(Asset asset, double targetAmount) throws SimulateException {

		Iterator<HoldingItem> iter = asset.getHoldingItems().iterator();
		while (iter.hasNext()) {
			HoldingItem hi = iter.next();
			double originalAmount = hi.getPrice() * hi.getShare();
			double realPencentage = originalAmount / asset.getAmount();
			hi.setShare(targetAmount * realPencentage / hi.getPrice());
			double tradeAmount = targetAmount * realPencentage - originalAmount;
			String operation = null;
			if (tradeAmount > Amount * ERROR_PERCENTAGE) {
				operation = Configuration.TRANSACTION_BUY;
			} else if (tradeAmount < -Amount * ERROR_PERCENTAGE) {
				operation = Configuration.TRANSACTION_SELL;

			} else {
				return;
			}
			addTransaction(PortfolioID, hi.getSecurityID(), 0l,  hi.getSymbol(), asset.getName(), CurrentDate, operation, tradeAmount, tradeAmount/hi.getPrice(), tradeAmount / this.Amount, Configuration.TRANSACTION_TYPE_REAL);

		}

	}
	public java.util.Date getCurrentDate() {
		return CurrentDate;
	}

	public void setCurrentDate(java.util.Date currentDate) {
		CurrentDate = currentDate;
	}

	public HoldingItem getHoldingItem(String trueFundName) {
		List<HoldingItem> his=this.getHoldingItems();
		if(his!=null&&his.size()>0){
			for(HoldingItem hi:his){
				if(hi.getSymbol().toLowerCase().equals(trueFundName.toLowerCase()))return hi;
			}
		}
		return null;
	}

	public HoldingItem getHoldingItem(Long securityID) {
		List<HoldingItem> his=this.getHoldingItems();
		if(his!=null&&his.size()>0){
			for(HoldingItem hi:his){
				if(hi.getSecurityID().equals(securityID))return hi;
			}
		}
		return null;
	}
	
	public List<HoldingRecord> getHoldingRecords() {
		return holdingRecords;
	}

	public void setHoldingRecords(List<HoldingRecord> holdingRecords) {
		this.holdingRecords = holdingRecords;
	}

	


}
