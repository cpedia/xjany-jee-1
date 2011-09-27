package com.lti.executor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lti.Exception.Executor.SimulateException;
import com.lti.Exception.Security.NoPriceException;
import com.lti.listener.Listener;
import com.lti.listener.impl.SimulatorLogPrinter;
import com.lti.listener.impl.SimulatorTransactionProcessor;
import com.lti.service.PortfolioHoldingManager;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.GlobalObject;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.HoldingRecord;
import com.lti.service.bo.Log;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.PortfolioState;
import com.lti.service.bo.Security;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.Transaction;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.Interval;
import com.lti.type.TimeUnit;
import com.lti.type.executor.SimulateStrategy;
import com.lti.type.finance.Asset;
import com.lti.type.finance.HoldingInf;
import com.lti.util.BaseFormulaUtil;
import com.lti.util.EmailUtil;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;
import com.lti.util.TransactionUtil;
/**
 * Portfolio模拟器，不改变任何数据
 * 
 * @author Michael Chua
 * 
 */
public class Simulator {

	private Listener listener;

	public void setListener(Listener listener) {
		this.listener = listener;
	}

	/**
	 * 模拟器停止的日期，如果为空，则默认为今天
	 */
	private Date expectedStopDate;

	/**
	 * 模拟器运行的策略List
	 */
	private List<SimulateStrategy> simulateStrategies;

	/**
	 * 待模拟的Portfolio
	 */
	private Portfolio simulatePortfolio;

	/**
	 * 记录待模拟的Portfolio的Holding
	 */
	private HoldingInf simulateHolding;

	/**
	 * 记录待模拟的Portfolio的Expected Holding
	 */
	private HoldingInf simulateExpectedHolding;

	/**
	 * 记录待模拟的Portfolio的历史 Holding
	 */
	private List<HoldingInf> simulateHistoryHoldings;

	/**
	 * 保存待模拟Portfolio的相关状态，例如persistent的bytes
	 */
	private PortfolioState portfolioState;
	/**
	 * 已经存在的transaction列表，用于重构portfolio
	 */
	private List<Transaction> existTransactionList;

	private List<GlobalObject> globalObjects = new ArrayList<GlobalObject>();
	/**
	 * 已经存在的transaction列表的当前访问下标
	 */
	private int index = 0;
	private boolean isCustomized = false;

	/**
	 * 模拟器构造器
	 * 
	 * @param expectedStopDate
	 *            期待停止日期，为null，则默认为当前物理时间
	 * @param simulateStrategies
	 *            模拟的策略集合
	 * @param simulatePortfolio
	 *            待模拟的Portfolio
	 * @param simulateHoldings
	 *            待模拟的Portfolio的Holding
	 * @param portfolioState
	 *            保存待模拟Portfolio的相关状态，例如persistent的bytes
	 * @param forceMonitor
	 *            是否强制执行monitor
	 */
	public Simulator(Date expectedStopDate, List<SimulateStrategy> simulateStrategies, Portfolio simulatePortfolio, HoldingInf simulateHoldings, PortfolioState portfolioState, boolean forceMonitor, boolean isCustomized, Listener listener) {
		super();
		this.expectedStopDate = expectedStopDate;
		this.simulateStrategies = simulateStrategies;
		this.simulatePortfolio = simulatePortfolio;
		this.simulateHolding = simulateHoldings;
		this.portfolioState = portfolioState;
		this.forceMonitor = forceMonitor;
		this.listener = listener;
		this.isCustomized = isCustomized;
		for (SimulateStrategy s : simulateStrategies) {
			s.setSimulator(this);
			s.setSimulatePortfolio(simulatePortfolio);
		}
	}
	
	public Simulator(Date expectedStopDate, List<SimulateStrategy> simulateStrategies, Portfolio simulatePortfolio, HoldingInf simulateHoldings, PortfolioState portfolioState, boolean forceMonitor, Listener listener) {
		super();
		this.expectedStopDate = expectedStopDate;
		this.simulateStrategies = simulateStrategies;
		this.simulatePortfolio = simulatePortfolio;
		this.simulateHolding = simulateHoldings;
		this.portfolioState = portfolioState;
		this.forceMonitor = forceMonitor;
		this.listener = listener;
		for (SimulateStrategy s : simulateStrategies) {
			s.setSimulator(this);
			s.setSimulatePortfolio(simulatePortfolio);
		}
	}

	/**
	 * construct时用的构造器
	 * 
	 * @param expectedStopDate
	 * @param simulateStrategies
	 * @param existTransactionList
	 * @param simulatePortfolio
	 * @param simulateHoldings
	 * @param portfolioState
	 * @param listener
	 */
	public Simulator(Date expectedStopDate, List<SimulateStrategy> simulateStrategies, List<Transaction> existTransactionList, Portfolio simulatePortfolio, HoldingInf simulateHoldings, PortfolioState portfolioState, Listener listener) {
		super();
		this.expectedStopDate = expectedStopDate;
		this.simulateStrategies = simulateStrategies;
		this.simulatePortfolio = simulatePortfolio;
		this.simulateHolding = simulateHoldings;
		this.portfolioState = portfolioState;
		this.existTransactionList = existTransactionList;
		this.listener = listener;

	}

	/**
	 * 执行所有策略的readObject函数
	 * 
	 * @throws Exception
	 */
	private void _readObject(ObjectInputStream stream) throws Exception {
		for (SimulateStrategy simulateStrategy : simulateStrategies) {
			simulateStrategy.readObject(stream);
		}
	}

	/**
	 * 执行所有策略的writeObject函数
	 * 
	 * @throws Exception
	 */
	private void _writeObject(ObjectOutputStream stream) throws Exception {
		for (SimulateStrategy simulateStrategy : simulateStrategies) {
			simulateStrategy.writeObject(stream);
		}
	}

	/**
	 * 执行所有策略的init函数
	 * 
	 * @throws Exception
	 */
	private void _init() throws Exception {
		simulateHolding.setCurrentDate(LTIDate.clearHMSM(simulateHolding.getCurrentDate()));
		for (SimulateStrategy simulateStrategy : simulateStrategies) {
			times.put(simulateStrategy.StrategyID, System.currentTimeMillis());
			simulateStrategy.init();
		}
	}

	/**
	 * 执行update/re-init/monitor之前的预处理
	 * 
	 * @throws Exception
	 */
	private void _start() throws Exception {
		simulateHolding.setCurrentDate(LTIDate.clearHMSM(simulateHolding.getCurrentDate()));
		for (SimulateStrategy simulateStrategy : simulateStrategies) {
			times.put(simulateStrategy.StrategyID, System.currentTimeMillis());
		}
	}

	/**
	 * 执行所有策略的action函数
	 * 
	 * @throws Exception
	 */
	private void _action() throws Exception {
		for (SimulateStrategy simulateStrategy : simulateStrategies) {
			simulateStrategy.action();
		}
	}

	/**
	 * 检查所有策略的版本
	 * 
	 * @throws Exception
	 */
	private boolean _checkVersion() throws SimulateException {
		for (SimulateStrategy simulateStrategy : simulateStrategies) {
			boolean b = simulateStrategy.checkVersion();
			if (b == false)
				return b;
		}
		return true;
	}

	/**
	 * 执行所有策略的reinit函数
	 * 
	 * @throws Exception
	 */
	private void _reinit() throws Exception {
		for (SimulateStrategy simulateStrategy : simulateStrategies) {
			simulateStrategy.reinit();
		}
	}

	/**
	 * 执行所策略的end函数 confidence?
	 * 
	 * @throws Exception
	 */
	private void _end() throws Exception {
		for (SimulateStrategy simulateStrategy : simulateStrategies) {
			simulateStrategy.end();
			GlobalObject go = new GlobalObject();
			go.setStrategyID(simulateStrategy.StrategyID);
			go.setPortfolioID(simulatePortfolio.getID());
			if(simulateStrategy.getSimulateAsset()!=null){
				go.setKey(simulateStrategy.StrategyID + "." + simulatePortfolio.getID() + simulateStrategy.getSimulateAsset().getName() + ".runtime");
			}else{
				go.setKey(simulateStrategy.StrategyID + "." + simulatePortfolio.getID() + ".runtime");
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			Long time = System.currentTimeMillis() - times.get(simulateStrategy.StrategyID);
			oos.writeObject(time);
			go.setBytes(baos.toByteArray());
			this.getGlobalObjects().add(go);
			simulateStrategy.getSimulatePortfolio().getStrategies().getStrategy(simulateStrategy.StrategyID).getParameter().put("version", simulateStrategy.getVersion() + "");
		}
	}

	/**
	 * 执行所策略的afterExecuting函数，注意在portfolio Monitor成功执行，Update不会
	 * 
	 * @throws Exception
	 */
	private void _afterExecuting() throws Exception {
		for (SimulateStrategy simulateStrategy : simulateStrategies) {
			simulateStrategy.afterExecuting();
		}
	}

	/**
	 * 执行所策略的afterExecuting函数，注意在portfolio Update成功执行，Monitor不会
	 * 
	 * @throws Exception
	 */
	private void _afterUpdating() throws Exception {
		for (SimulateStrategy simulateStrategy : simulateStrategies) {
			simulateStrategy.afterUpdating();
		}
	}

	Map<Long, Long> times = new HashMap<Long, Long>();

	private SecurityManager securityManager;
	private PortfolioManager portfolioManager;
	private PortfolioHoldingManager portfolioHoldingManager;

	/**
	 * 调整Portfolio Holding的Price和Amount信息 Split和Dividend的预处理在此函数处理
	 * 
	 * @param date
	 *            要调整到的日期
	 * @throws Exception 
	 */
	public void forward(Date date) throws Exception {
		removeClosedSecurity(date);
		refreshprice(date);
		for (SimulateStrategy ss : simulateStrategies) {
			ss.setCurrentDate(date);
		}
	}
	
	private void refreshprice(Date date) throws Exception{
		simulateHolding.setCurrentDate(date);
		for (int i = 0; i < simulateHolding.getAssets().size(); i++) {
			Asset asset = simulateHolding.getAssets().get(i);
			for (int j = 0; j < asset.getHoldingItems().size(); j++) {
				HoldingItem hi = asset.getHoldingItems().get(j);
				double open = securityManager.getOpenPrice(hi.getSecurityID(), date);
				double close = securityManager.getPrice(hi.getSecurityID(), date);
				double dividend = securityManager.getDividend(hi.getSecurityID(), date);
				Double split = securityManager.getSplit(hi.getSecurityID(), date);
				if (split != null && Math.abs(split - 1) > 0.05) {
					hi.setShare(hi.getShare() * split);
					simulateHolding.addTransaction(simulatePortfolio.getID(), hi.getSecurityID(), 0l, hi.getSymbol(), hi.getAssetName(), date, Configuration.TRANSACTION_SPLIT, split, split, split, Configuration.TRANSACTION_TYPE_SPLIT);
				}
				hi.setOpen(open);
				hi.setClose(close);
				hi.setPrice(close);
				hi.setDividend(dividend);
				hi.setAssetName(asset.getName());
				hi.setPortfolioID(simulatePortfolio.getID());
			}
		}
		simulateHolding.refreshAmounts();
	}

	/**
	 * 模拟器初始化
	 * 
	 * @throws Exception
	 */
	private void init() throws Exception {
		logProcessor = new SimulatorLogPrinter();

		transactionProcessor = (SimulatorTransactionProcessor) simulateHolding.getTransactionProcessor();
		if (transactionProcessor == null)
			transactionProcessor = new SimulatorTransactionProcessor();

		for (SimulateStrategy simulateStrategy : simulateStrategies) {
			simulateStrategy.setSimulateHoldings(simulateHolding);
			simulateStrategy.fetchParameters();
			simulateStrategy.setCurrentDate(simulatePortfolio.getStartingDate());
			simulateStrategy.setPortfolioID(this.simulatePortfolio.getID());
			simulateStrategy.setLogProccessor(logProcessor);
		}
		if (portfolioState == null) {
			portfolioState = new PortfolioState(simulatePortfolio.getID(), Configuration.PORTFOLIO_RUNNING_STATE_INACTIVE);
		} else {
			if (portfolioState.getState() == null) {
				portfolioState.setState(Configuration.PORTFOLIO_RUNNING_STATE_INACTIVE);
			}
		}

		securityManager = ContextHolder.getSecurityManager();

		portfolioManager = ContextHolder.getPortfolioManager();

		simulateHolding.setTransactionProcessor(transactionProcessor);

		simulateTransactions = new ArrayList<Transaction>();

		simulateScheduleTransactions = new ArrayList<Transaction>();
		
		simulateHoldingRecords = new ArrayList<HoldingRecord>();

		simulateLogs = new ArrayList<Log>();

		simulateHistoryHoldings = new ArrayList<HoldingInf>();

		holdingItems = new ArrayList<HoldingItem>();

		if (listener != null) {
			listener.afterInit(this);
		}
	}

	/**
	 * transaction driven模拟器初始化
	 * 
	 * @throws Exception
	 */
	private void initconstruct() throws Exception {
		logProcessor = new SimulatorLogPrinter();

		transactionProcessor = (SimulatorTransactionProcessor) simulateHolding.getTransactionProcessor();
		if (transactionProcessor == null)
			transactionProcessor = new SimulatorTransactionProcessor();

		if (portfolioState == null) {
			portfolioState = new PortfolioState(simulatePortfolio.getID(), Configuration.PORTFOLIO_RUNNING_STATE_INACTIVE);
		} else {
			if (portfolioState.getState() == null) {
				portfolioState.setState(Configuration.PORTFOLIO_RUNNING_STATE_INACTIVE);
			}
		}

		securityManager = ContextHolder.getSecurityManager();

		portfolioManager = ContextHolder.getPortfolioManager();

		simulateHolding.setTransactionProcessor(transactionProcessor);

		simulateTransactions = new ArrayList<Transaction>();

		simulateScheduleTransactions = new ArrayList<Transaction>();

		simulateLogs = new ArrayList<Log>();

		simulateHistoryHoldings = new ArrayList<HoldingInf>();

		holdingItems = new ArrayList<HoldingItem>();

		if (listener != null) {
			listener.afterInit(this);
		}
	}

	private boolean forceMonitor = false;

	/**
	 * 记录模拟过程中生成的logs
	 */
	private List<com.lti.service.bo.Log> simulateLogs;

	/**
	 * 记录模拟过程中生成的transactions
	 */
	private List<com.lti.service.bo.Transaction> simulateTransactions;
	/**
	 * 记录模拟过程中生成的schedule transactions
	 */
	private List<com.lti.service.bo.Transaction> simulateScheduleTransactions;
	/**
	 * 记录模拟过程中生成的daily data
	 */
	private List<PortfolioDailyData> simulateDailydatas;
	/**
	 * 记录dividend adjustment 时最早进行调整的的dailydata 的index,保证最小更新
	 */
	private int firstIndex = -1;
	/**
	 * 记录模拟过程中生成的mpt
	 */
	private List<PortfolioMPT> simulateMPTs;
	/**
	 * 记录模拟过程中生成的holding item? 记录模拟过程的holdings?
	 */
	private List<HoldingItem> holdingItems;
	/**
	 * 记录模拟过程生成的holding record记录
	 */
	private List<HoldingRecord> simulateHoldingRecords;

	private SimulatorLogPrinter logProcessor;

	private SimulatorTransactionProcessor transactionProcessor;

	/**
	 * 执行所有策略的action之前的操作，每一个模拟日期执行一次 do schedule and dividend
	 */
	private void beforeaction(Date date) throws SimulateException, NoPriceException {
		doScheduleTransaction(transactionProcessor.getScheduleTransactions(), date);
	}

	/**
	 * do the Schedule transaction
	 * 
	 * @param scheduleTransactionList
	 * @param date
	 * @throws SimulateException
	 * @throws NoPriceException
	 */
	private void doScheduleTransaction(List<Transaction> scheduleTransactionList, Date date) throws SimulateException, NoPriceException {
		Long cashID = Configuration.CASHID;
		if (scheduleTransactionList != null && scheduleTransactionList.size() > 0) {
			// if have buy at open transaction on CASH, we just put the
			// transaction at the last of the list
			int size = scheduleTransactionList.size();
			int pos = -1;
			for (int i = 0; i < size; ++i) {
				Transaction t = scheduleTransactionList.get(i);
				String operation = t.getOperation();
				if (operation.equalsIgnoreCase(Configuration.TRANSACTION_BUY_AT_OPEN)) {
					if (t.getSecurityID().equals(cashID)) {
						pos = i;
						break;
					}
				}
			}
			if (pos != -1) {
				Transaction cashBuy = scheduleTransactionList.get(pos);
				Transaction last = scheduleTransactionList.get(size - 1);
				scheduleTransactionList.set(size - 1, cashBuy);
				scheduleTransactionList.set(pos, last);
			}

			simulateHolding.switchPrice(Configuration.PRICE_TYPE_OPEN);
			for (int i = 0; i < scheduleTransactionList.size(); ++i) {
				Transaction transaction = scheduleTransactionList.get(i);
				double open = securityManager.getOpenPrice(transaction.getSecurityID(), date);
				double close = securityManager.getPrice(transaction.getSecurityID(), date);
				if (transaction.getOperation().equals(Configuration.TRANSACTION_BUY_AT_OPEN)) {
					if (i == size - 1)// use all the left cash to buy the last fund
						transaction.setAmount(simulateHolding.getCash());
					else if (transaction.getAmount() > simulateHolding.getCash())
						transaction.setAmount(simulateHolding.getCash());
					simulateHolding.buyAtOpen(transaction.getAssetName(), transaction.getSecurityID(), transaction.getSymbol(), transaction.getAmount(), open, close, transaction.getReInvest());
				} else
					simulateHolding.sellAtOpen(transaction.getAssetName(), transaction.getSecurityID(), transaction.getSymbol(), transaction.getShare() * open);
			}
			simulateHolding.switchPrice(Configuration.PRICE_TYPE_CLOSE);
		}
	}

	/**
	 * 执行reinvest,每一个模拟日期执行一次
	 * 
	 * @param holdingInf
	 */
	public void doReinvest(HoldingInf holdingInf) {
		simulateHolding.doReinvest(holdingInf);
	}

	public boolean hasRealTransaction(){
		if(transactionProcessor.getTransactions()!=null){
			for(Transaction tr: transactionProcessor.getTransactions()){
				if(tr.getTransactionType() == Configuration.TRANSACTION_TYPE_REAL)
					return true;
			}
		}
		return false;
	}
	
	/**
	 * 执行所有策略的action之后的操作，每一个模拟日期执行一次
	 * 
	 * @throws SimulateException
	 */
	private void afteraction(Date date) throws SimulateException {
	
		if (LTIDate.isHoldingDate(date) || hasRealTransaction()) {
			simulateHistoryHoldings.add(simulateHolding.clone());
			List<HoldingItem> his = simulateHolding.getHoldingItems();
			for (HoldingItem hi : his) {
				HoldingItem nhi = hi.clone();
				nhi.setID(null);
				holdingItems.add(nhi);
			}
		}

		// calculate the expected holding and if not null, save it
		calculateExpectedHoldingInf(date);
		arrangeTransactions(date);
		simulateLogs.addAll(logProcessor.getLogs());
		logProcessor.clear();

		if (listener != null) {
			listener.afterAction(this, date);
		}
	}

	private void calculateExpectedHoldingInf(Date date) throws SimulateException {
		boolean flag = false;
		List<Transaction> scheTransactions = transactionProcessor.getScheduleTransactions();
		if (scheTransactions != null && scheTransactions.size() > 0) {
			simulateExpectedHolding = simulateHolding.clone();
			simulateExpectedHolding.switchPrice(Configuration.PRICE_TYPE_CLOSE);
			for (int i = 0; i < scheTransactions.size(); ++i) {
				Transaction t = scheTransactions.get(i);
				if (LTIDate.equals(t.getDate(), date)) {
					flag = true;
					String operation = t.getOperation();
					if (operation.equals(Configuration.TRANSACTION_BUY_AT_OPEN)) {
						// we set open price to 0 because we don't use it to
						// calculate totalamount today or do other things.
						simulateExpectedHolding.baseBuy(t.getAssetName(), t.getSecurityID(), t.getSymbol(), t.getAmount(), 0, t.getAmount() / t.getShare(), t.getReInvest(), Configuration.TRANSACTION_TYPE_REAL, false);
					} else if (operation.equals(Configuration.TRANSACTION_SELL_AT_OPEN)) {
						simulateExpectedHolding.baseSell(t.getAssetName(), t.getSecurityID(), t.getSymbol(), t.getAmount(), Configuration.TRANSACTION_TYPE_REAL, false);
					}
				} else
					continue;
			}
		}
		if (!flag)
			simulateExpectedHolding = null;
		else {
			simulateExpectedHolding.clearEmptyHolding();
			simulateExpectedHolding.refreshAmounts();
		}
		
	}
	/**
	 * 获得startDate 开始的portfolio daily data, 注意记录下最早的index,以备update的时候用
	 * @param startDate
	 * @return
	 */
	private List<PortfolioDailyData> getPortfolioDailyData(Date startDate){
		if(simulateDailydatas != null){
			int startIndex = this.binarySearch(startDate);
			if(startIndex != -1 &&(firstIndex == -1 || startIndex < firstIndex))
				firstIndex = startIndex;
			if(startIndex != -1)
				return simulateDailydatas.subList(startIndex, simulateDailydatas.size());
		}
		return null;
	}
	/**
	 * 二分搜索对应于startDate的simulateDailydatas的index
	 * @param startDate
	 * @return
	 */
	private int binarySearch(Date startDate){
		int low = 0, high = simulateDailydatas.size() -1;
		while(low <= high){
			int mid = (low + high) / 2;
			Date midDate = simulateDailydatas.get(mid).getDate();
			if(LTIDate.equals(midDate, startDate))
				return mid;
			if(LTIDate.before(midDate, startDate))
				low = mid + 1;
			else
				high = mid - 1;
		}
		return -1;
	}
	/**
	 * 完成dividend adjustment
	 * 其中holdingItems中保存有需要save or update 的holding item
	 * 其中simulateDailydatas中部分数据是要更新的，这可以从 firstIndex 找到起始下标
	 * simulateHolding 中产生的transaction是要保存的reinvest transactions
	 * simulateHolding 也得保存
	 * @author CCD
	 */
	public void dividendAdjustment(){
		portfolioHoldingManager = (PortfolioHoldingManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioHoldingManager");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for(HoldingRecord hr: simulateHoldingRecords){
			if(hr.getDividendDateStr()== null)
				continue;
			mode = DIVIDENDADJUSTMENT;
			String[] dateStrs = hr.getDividendDateStr().split(",");
			List<SecurityDailyData> sdds = null;
			List<PortfolioDailyData> pdds = null;
			for(String dateStr: dateStrs){
				try {
					Date startDate = sdf.parse(dateStr);
					if(LTIDate.before(simulatePortfolio.getEndDate(), startDate))
						break;
					Date startDateForCalculatePrice = LTIDate.getNewNYSETradingDay(startDate, -1);
					pdds = getPortfolioDailyData(startDate);
					HoldingItem curHoldingItem = portfolioHoldingManager.getOneHoldItemBefore(hr.getPortfolioID(), hr.getSecurityID(), startDate);
					HoldingItem holdingItem = simulateHolding.getHoldingItem(hr.getSecurityID());
					if(hr.getEndDate() == null && holdingItem != null){
						//我们从dividend date 那天起直到现在都持有该fund,所以要调整share
						sdds = securityManager.getDailyDatas(hr.getSecurityID(), startDateForCalculatePrice, new Date());
						if(sdds != null && sdds.size() > 1){
							boolean found = false;
							//注意,我们从有dividend那天的前一工作日开始取数据,所以这里是1
							double dividend = sdds.get(1).getDividend();
							//调整holding item, 获得那一天的share，并且之后有它的holding item都要加上同样的share
							List<HoldingItem> his = portfolioHoldingManager.getHoldingItemsAfter(hr.getPortfolioID(), hr.getSecurityID(), startDate);
							double addedAmount = curHoldingItem.getShare() * dividend;
							double addedShare = addedAmount / (sdds.get(0).getClose() - dividend);
							if(his != null){
								for(HoldingItem hi: his){
									found = false;
									for(HoldingItem hii: holdingItems){
										if(hii.getSecurityID().equals(hi.getSecurityID()) && LTIDate.equals(hii.getDate(), hi.getDate())){
											hii.setShare(hii.getShare() + addedShare);
											found = true;
											break;
										}
									}
									if(!found){
										hi.setShare(hi.getShare() + addedShare);
										holdingItems.add(hi);
									}
								}
							}
							double totalAmount = pdds.get(0).getAmount();
							//调整portfoliodailydata
							for(int i=0;i<pdds.size();++i){
								try{
									PortfolioDailyData pdd = pdds.get(i);
									pdd.setAmount(pdd.getAmount() + sdds.get(i).getClose() * addedShare);
								}catch(Exception e){
									
								}
							}
							//调整当前的holding
							holdingItem.setShare(holdingItem.getShare() + addedShare);
							//要产生一条transaction reinvest 的记录
							simulateHolding.addTransaction(hr.getPortfolioID(), hr.getSecurityID(), 0l, curHoldingItem.getSymbol(), curHoldingItem.getAssetName(), startDate, Configuration.TRANSACTION_REINVEST, addedAmount, addedShare, addedAmount / totalAmount, Configuration.TRANSACTION_TYPE_REINVEST);
						}
					}else{
						//我们从dividend date 那天起没有一直持有该fund,所以直接加为大CASH,而且大CASH每天都有dividend
						SecurityDailyData sdd = securityManager.getDailydata(hr.getSecurityID(), startDate, false);
						sdds = securityManager.getDailyDatas(Configuration.CASHID, startDate, new Date());
						//注意,我们从dividend date 那天取数据,因为大CASH的dividend 要从第二天才开始
						boolean found = false;
						//调整portfoliodailydata
						//同时调整今天过后所有holding date的holding,如果当天持有大CASH,要调整share,否则要创建一个holding item
						double addedShare = curHoldingItem.getShare() * sdd.getDividend();
						double addedAmount = addedShare;
						double preClose = 0;
						for(int i=0;i<pdds.size();++i){
							PortfolioDailyData pdd = pdds.get(i);
							SecurityDailyData ssdd = sdds.get(i);
							double dd = ssdd.getDividend();
							if(i>0){//addedShare = addedShare + addedShare * dd / (preClose -dd);
								addedShare = addedAmount * dd / (preClose - dd);
								addedAmount += addedShare;
							}
							if(LTIDate.isHoldingDate(pdd.getDate())){
								HoldingItem hi = null;
								for(HoldingItem hii : holdingItems){
									if(hii.getSecurityID().equals(Configuration.CASHID) && LTIDate.equals(hii.getDate(), pdd.getDate())){
										found = true;
										hi = hii;
										break;
									}
								}
								if(!found)
									hi = portfolioHoldingManager.getHoldingItem(hr.getPortfolioID(), Configuration.CASHID, pdd.getDate());
								if(hi != null)
									hi.setShare(hi.getShare() + addedShare);
								else{
									hi = new HoldingItem();
									hi.setAssetName("CASH");
									hi.setDate(pdd.getDate());
									hi.setPortfolioID(hr.getPortfolioID());
									hi.setSecurityID(Configuration.CASHID);
									hi.setShare(addedShare);
									hi.setSymbol("CASH");
									hi.setReInvest(true);
									hi.setPrice(1.0);
									hi.setPercentage(addedShare / pdd.getAmount());
								}
								if(!found)
									holdingItems.add(hi);
							}
							pdd.setAmount(pdd.getAmount() + addedAmount);
							preClose = ssdd.getClose();
							//要产生一条transaction reinvest 的记录
							simulateHolding.addTransaction(hr.getPortfolioID(), Configuration.CASHID, 0l, "CASH", "CASH", pdd.getDate(), Configuration.TRANSACTION_REINVEST, addedShare, addedShare, addedShare / pdd.getAmount(), Configuration.TRANSACTION_TYPE_REINVEST);
						}
						//调整当前的holding
						HoldingItem hi = simulateHolding.getHoldingItem(Configuration.CASHID);
						if(hi == null){
							try {
								// 加的小cash跟它要买的大CASH是等量的
								simulateHolding.setCash(simulateHolding.getCash() + addedShare);
								simulateHolding.baseBuy("CASH", Configuration.CASHID, "CASH", addedShare, 1.0, 1.0, true, Configuration.TRANSACTION_TYPE_REAL, false);
							} catch (SimulateException e) {
								e.printStackTrace();
							}
						}
					}
					hr.setStartDate(startDate);
				}catch (ParseException e) {
				}
			}
		}
		simulateTransactions.addAll(transactionProcessor.getTransactions());
		transactionProcessor.getTransactions().clear();
		//transactionProcessor.clearAll();
	}
	
	/**
	 * add the finished transactions to simulation list and clear them in
	 * transactionProcessor
	 * 
	 * @param date
	 */
	private void arrangeTransactions(Date date) {
		arrangeScheduleTransactions(date);
		simulateTransactions.addAll(transactionProcessor.getTransactions());
		transactionProcessor.clear(date);
	}

	/**
	 * save or recover the schedule transactions
	 * 
	 * @param date
	 */
	private void arrangeScheduleTransactions(Date date) {
		List<Transaction> scheduleTransactions = transactionProcessor.getScheduleTransactions();
		for (int i = 0; i < scheduleTransactions.size(); ++i) {
			Transaction tr = scheduleTransactions.get(i);
			if (LTIDate.before(tr.getDate(), date))
				simulateTransactions.add(tr);
		}
	}

	/**
	 * 强制执行
	 */
	public final static int MONITOR = 0;
	/**
	 * 更新
	 */
	public final static int UPDATE = 1;
	/**
	 * 重新初始化后，更新
	 */
	public final static int REINT = 2;
	/**
	 * 无改变
	 */
	public final static int UNCHANGE = 3;
	/**
	 * 
	 */
	public final static int DIVIDENDADJUSTMENT = 4;
	/**
	 * 当前模拟器的运行Mode，强制执行、更新、重新初始化
	 */
	private int mode = UPDATE;

	/**
	 * 获取当前运行Mode在
	 * 
	 * @return
	 */
	public int getMode() {
		return mode;
	}

	/**
	 * 未测试
	 * @throws Exception
	 */
	private void removeClosedSecurity(Date date) throws Exception {
		SecurityManager sm = ContextHolder.getSecurityManager();
		List<HoldingItem> operations = new ArrayList<HoldingItem>();
		List<HoldingItem> his = simulateHolding.getHoldingItems();
		if (his != null && his.size() > 0) {
			for(HoldingItem hi:his){
				Security s = sm.get(hi.getSecurityID());
				if (s != null && s.getIsClosed() != null && s.getIsClosed()) {
					Date enddate=s.getEndDate();
					if(enddate==null||!enddate.after(simulateHolding.getCurrentDate())){
						operations.add(hi);
					}
					
				}// end of s!=null
			}//end for
			
		}// end if
		if (operations.size() != 0) {
			for (HoldingItem hi : operations) {
				double amount=hi.getClose()*hi.getShare();
				simulateHolding.baseSell(hi.getAssetName(), hi.getSecurityID(),hi.getSymbol() ,amount, Configuration.TRANSACTION_TYPE_REAL);
				simulateHolding.baseBuy(hi.getAssetName(), 890l, "CASH", amount, 1.0, 1.0, true, Configuration.TRANSACTION_TYPE_REAL);
			}
			simulateHolding.clearEmptyHolding();
		}
	}

	/**
	 * 执行模拟过程
	 * 
	 * @throws Exception
	 */
	public void simulate() throws Exception {
		
		//Configuration.writePortfolioUpdateLog("Start portfolio update daily data", new Date(), "\n************************************\n start portfolio update daily data\n************************************\n");
		long startTime = System.currentTimeMillis();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long t1=System.currentTimeMillis();
		
		//Configuration.writePortfolioUpdateLog("Part 1:prepare data", new Date(), "\n************************************\n part 1:parepare data start\n************************************\n");
		init();
		_start();
		simulateDailydatas = new ArrayList<PortfolioDailyData>();
		
		if (expectedStopDate == null)
			expectedStopDate = new Date();
		int state = portfolioState.getState();
		if (state == Configuration.PORTFOLIO_RUNNING_STATE_INACTIVE || forceMonitor) {
			refreshprice(simulatePortfolio.getStartingDate());
			_init();
			mode = MONITOR;
			if (LTIDate.isNYSETradingDay(simulatePortfolio.getStartingDate())) {
				simulatePortfolio.setEndDate(simulatePortfolio.getStartingDate());

				PortfolioDailyData pdd = new PortfolioDailyData();
				pdd.setAmount(simulateHolding.getAmount());
				pdd.setPortfolioID(simulatePortfolio.getID());
				pdd.setDate(simulateHolding.getCurrentDate());
				simulateDailydatas.add(pdd);
				forward(simulatePortfolio.getEndDate());
			} else {
				simulatePortfolio.setEndDate(simulatePortfolio.getStartingDate());
			}
		} else if (!_checkVersion()) {
			refreshprice(LTIDate.getNewNYSETradingDay(simulatePortfolio.getEndDate(), 1));
			_reinit();
			portfolioState.setPersistentBytes(null);
			mode = REINT;
		} else {
			refreshprice(simulatePortfolio.getEndDate());
			byte[] arr=portfolioState.getPersistentBytes();
			ObjectInputStream stream = null;
			if(arr!=null){
				 stream = new ObjectInputStream(new ByteArrayInputStream(arr));
			}else{
				ByteArrayOutputStream baos=new ByteArrayOutputStream();
				ObjectOutputStream oos=new ObjectOutputStream(baos);
				oos.flush();
				stream= new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
			}
			
			_readObject(stream);
			//here mode is UPDATE, we get the holding records and then do the dividend adjustment in the before action
			simulateHoldingRecords = portfolioManager.getHoldingRecords(simulatePortfolio.getID());
			firstIndex = -1;
			//做dividend adjustment
			if(simulateHoldingRecords != null && simulateHoldingRecords.size()>0){
				//必须有一些初始化信息,拿出所有的portfolio daily data
				this.simulateDailydatas = portfolioManager.getDailydatas(simulatePortfolio.getID());
				dividendAdjustment();
				simulateHolding.setHoldingRecords(simulateHoldingRecords);
			}
			if(mode != DIVIDENDADJUSTMENT)
				simulateDailydatas.clear();
		}
		
		Date _startDate = LTIDate.getNewNYSETradingDay(simulatePortfolio.getEndDate(), 1);

		List<Date> dateList = LTIDate.getTradingDates(_startDate, expectedStopDate);
		
		//Configuration.writePortfolioUpdateLog("Part 1:prepare data", new Date(), "\n************************************\n part 1:parepare data End\n************************************\n");
		
		
		//没有要更新的数据并且没有dividend adjustment时，直接返回
		if ((dateList == null || dateList.size() == 0) && (firstIndex == -1)) {
			mode = UNCHANGE;
			if (listener != null) {
				listener.afterFinish(this);
			}
			return;
		}
		
		if(dateList != null && dateList.size() > 0){
			HoldingInf backupHoldings = simulateHolding.clone();
			Date _endDate = dateList.get(0);
			boolean issent=false;
			for (int i = 0; i < dateList.size(); i++) {
				try {
					//Configuration.writePortfolioUpdateLog("Part 2:update portfolioDailyData", new Date(), "\n************************************\n part 2:update portfolioDailyData start \n************************************\n");
					forward(dateList.get(i));
					beforeaction(dateList.get(i));
					_action();
					doReinvest(backupHoldings);
					afteraction(dateList.get(i));
					PortfolioDailyData pdd = new PortfolioDailyData();
					pdd.setAmount(simulateHolding.getAmount());
					pdd.setDate(dateList.get(i));
					pdd.setPortfolioID(simulatePortfolio.getID());
					simulateDailydatas.add(pdd);
					
					if(mode == UPDATE && !issent && pdd.getAmount() == 10000.0 && dateList.get(i).getTime()-simulatePortfolio.getStartingDate().getTime()>60*24*3600*1000 && simulateHolding.getHoldingItems().size()==0){
						EmailUtil.sendMail(new String[]{"wyjfly@gmail.com","jany.wuhui@gmail.com"}, "The amount is abnormal.[10000.0]["+10000+"]", 
								"["+simulatePortfolio.getID()+"]"+simulatePortfolio.getName()+"<br>\nDate: "+dateList.get(i));
						issent=true;
						//throw new SimulateException("["+dateList.get(i)+"]No holding itmes and the amount is 10000.0 after 2 months", simulatePortfolio.getID(), simulatePortfolio.getName());
					}
					if(simulateDailydatas.size()>2){
						double amount1=simulateDailydatas.get(simulateDailydatas.size()-1).getAmount();
						double amount2=simulateDailydatas.get(simulateDailydatas.size()-2).getAmount();
						if((amount1-amount2)/amount2>0.15||(amount1-amount2)/amount2<-0.15){
							//throw new SimulateException("The amount is abnormal.["+amount1+"]["+amount2+"]", simulatePortfolio.getID(), simulatePortfolio.getName());
							EmailUtil.sendMail(new String[]{"wyjfly@gmail.com","jany.wuhui@gmail.com"}, "The amount is abnormal.["+amount1+"]["+amount2+"]", 
									"["+simulatePortfolio.getID()+"]"+simulatePortfolio.getName()+"<br>\nDate: "+dateList.get(i));
						}
					}
				} catch (NoPriceException npe) {
					simulateHolding = backupHoldings;
					arrangeScheduleTransactions(dateList.get(i));
					printToLog("["+df.format(new Date())+"]\r\n"+StringUtil.getStackTraceString(npe),simulatePortfolio.getEndDate());
					break; 
				}
				_endDate = dateList.get(i);
				backupHoldings = simulateHolding.clone();
				simulatePortfolio.setEndDate(_endDate);
			}
			
		}

		if(simulateTransactions!=null&&simulateTransactions.size()>0){
			simulatePortfolio.setLastTransactionDate(getTransactionDate());
		}
		
		if (simulateDailydatas.size() == 0) {
			mode = UNCHANGE;
			if (listener != null) {
				listener.afterFinish(this);
			}
			return;
		}

		_end();

		if (mode == MONITOR) {
			_afterExecuting();
			portfolioState.setState(Configuration.PORTFOLIO_RUNNING_STATE_FINISHED);
		} else {
			_afterUpdating();
			portfolioState.setState(Configuration.PORTFOLIO_RUUNING_STATE_DAILYEXECUTION_FINISHED);
		}

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream stream = new ObjectOutputStream(baos);
		_writeObject(stream);
		stream.flush();
		portfolioState.setPersistentBytes(baos.toByteArray());
		portfolioState.setUpdateTime(new Date());
		
		//Configuration.writePortfolioUpdateLog("Part 2:update portfolioDailyData", new Date(), "\n************************************\n part 2:update portfolioDailyData End \n************************************\n");
		long MPTStartTime = 0;
		long MPTEndTime = 0;
		if(mode == UPDATE || mode == REINT ){
			if(LTIDate.isLastNYSETradingDayOfWeek(simulateHolding.getCurrentDate())){//如果是每周最后一个交易日
				List<PortfolioDailyData> pdds = portfolioManager.getDailydatas(simulatePortfolio.getID());
				if(pdds != null){
					pdds.addAll(simulateDailydatas);
					MPTStartTime = System.currentTimeMillis();
					computeMPT(pdds);
					MPTEndTime = System.currentTimeMillis();
				}
			}else{//否则只更新AR
				//Configuration.writePortfolioUpdateLog("Part 3:update portfolioMPT", new Date(), "\n************************************\n part 3:update portfolioMPT start \n************************************\n");
				
				computeMPT(simulateDailydatas.get(simulateDailydatas.size() - 1));
				
				//Configuration.writePortfolioUpdateLog("Part 3:update portfolioMPT", new Date(), "\n************************************\n part 3:update portfolioMPT End \n************************************\n");
			}
		}else{// include mode == DIVIDENDADJUSTMENT
			MPTStartTime = System.currentTimeMillis();
			computeMPT(simulateDailydatas);
			MPTEndTime = System.currentTimeMillis();
		}
		simulateTransactions = TransactionUtil.MergeTransactions(simulateTransactions);
		long t2=System.currentTimeMillis();
		
		
		if(mode==UPDATE){
			printToLog("Update time: "+(t2-t1)/1000+" secs at ["+df.format(new Date())+"]",simulatePortfolio.getEndDate());
		}else{
			printToLog("Execute time: "+(t2-t1)/1000+" secs at ["+df.format(new Date())+"]",simulatePortfolio.getEndDate());
		}
		if(mode==UPDATE){
			long endTime = System.currentTimeMillis();
			printToLog("Update time: " + (endTime - startTime)/1000 + " secs" , simulatePortfolio.getEndDate());
			printToLog("Calculate MPT time: " + (MPTEndTime - MPTStartTime)/1000 + " secs" , simulatePortfolio.getEndDate());
			printToLog("Percentage: " + (MPTEndTime - MPTStartTime)*1.0 / (endTime - startTime), simulatePortfolio.getEndDate());
		}
	}
	
	private Date getTransactionDate() {
		if(simulateTransactions==null||simulateTransactions.size()==0){
			return simulatePortfolio.getLastTransactionDate();
		}
		for(int i=simulateTransactions.size()-1;i>=0;i--){
			if(simulateTransactions.get(i).getTransactionType()==Configuration.TRANSACTION_TYPE_REAL)
			{
				return simulateTransactions.get(i).getDate();
			}
		}
		return simulatePortfolio.getLastTransactionDate();
	}

	private void printToLog(String mess,Date date){
		Log l=new Log();
		l.setLogDate(date);
		l.setMessage(mess);
		l.setPortfolioID(simulatePortfolio.getID());
		this.simulateLogs.add(l);
	}

	/**
	 * 
	 * @param date
	 * @throws NoPriceException
	 */
	private void _construct_forward(Date date) throws NoPriceException {
		simulateHolding.setCurrentDate(date);
		for (int i = 0; i < simulateHolding.getAssets().size(); i++) {
			Asset asset = simulateHolding.getAssets().get(i);
			for (int j = 0; j < asset.getHoldingItems().size(); j++) {
				HoldingItem hi = asset.getHoldingItems().get(j);
				double open = securityManager.getOpenPrice(hi.getSecurityID(), date);
				double close = securityManager.getPrice(hi.getSecurityID(), date);
				hi.setOpen(open);
				hi.setClose(close);
				hi.setPrice(close);
			} 
		}
		simulateHolding.refreshAmounts();
	}

	/**
	 * 
	 * @throws NoPriceException
	 * @throws SimulateException
	 */
	private void _construct_init() throws NoPriceException, SimulateException {
		simulateHolding.setCurrentDate(LTIDate.clearHMSM(simulateHolding.getCurrentDate()));
		Date curDate = simulateHolding.getCurrentDate();
		if (!LTIDate.isNYSETradingDay(curDate)) {
			for (int i = 0; i < existTransactionList.size(); ++i) {
				Transaction tr = existTransactionList.get(i);
				if (LTIDate.equals(tr.getDate(), curDate)) {
					simulateTransactions.add(tr);
				} else {
					index = i;
					break;
				}
			}
			curDate = LTIDate.getRecentNYSETradingDayForward(curDate);
		}
		simulateHolding.setCurrentDate(curDate);
		// 非工作日要向前推一天
		_construct_forward(curDate);
		for (int i = index; i < existTransactionList.size(); ++i) {
			Transaction tr = existTransactionList.get(i);
			if (LTIDate.equals(tr.getDate(), curDate)) {
				if (tr.getTransactionType() == Configuration.TRANSACTION_TYPE_REAL) {
					if (tr.getOperation().equals(Configuration.TRANSACTION_BUY)) {
						if (simulateHolding.getAsset(tr.getAssetName()) == null) {
							Asset asset = new Asset();
							asset.setName(tr.getAssetName());
							simulateHolding.addAsset(asset);
						}
						double open = securityManager.getOpenPrice(tr.getSecurityID(), simulateHolding.getCurrentDate());
						double close = securityManager.getPrice(tr.getSecurityID(), simulateHolding.getCurrentDate());
						simulateHolding.baseBuy(tr.getAssetName(), tr.getSecurityID(), tr.getSymbol(), tr.getAmount(), open, close, tr.getReInvest(), tr.getTransactionType());
					} else if (tr.getOperation().equals(Configuration.TRANSACTION_SELL)) {
						simulateHolding.baseSell(tr.getAssetName(), tr.getSecurityID(), tr.getSymbol(), tr.getAmount(), Configuration.TRANSACTION_TYPE_REAL);
					} else if (tr.getOperation().equalsIgnoreCase(Configuration.TRANSACTION_BUY_AT_OPEN)) {
						if (simulateHolding.getAsset(tr.getAssetName()) == null) {
							Asset asset = new Asset();
							asset.setName(tr.getAssetName());
							simulateHolding.addAsset(asset);
						}
						double open =  securityManager.getOpenPrice(tr.getSecurityID(), simulateHolding.getCurrentDate());
						double close = securityManager.getPrice(tr.getSecurityID(), simulateHolding.getCurrentDate());
						simulateHolding.buyAtOpen(tr.getAssetName(), tr.getSecurityID(), tr.getSymbol(), tr.getAmount(), open, close, tr.getReInvest());
					} else if (tr.getOperation().equalsIgnoreCase(Configuration.TRANSACTION_SELL_AT_OPEN)) {
						simulateHolding.sellAtOpen(tr.getAssetName(), tr.getSecurityID(), tr.getSymbol(), tr.getAmount());
					} else if (tr.getOperation().equals(Configuration.DEPOSIT)) {
						simulateHolding.deposit(tr.getAmount());
					} else if (tr.getOperation().equals(Configuration.WITHDRAW)) {
						simulateHolding.withdraw(tr.getAmount());
					}
				}
				simulateTransactions.add(tr);
			} else {
				index = i;
				break;
			}
		}
		simulateHolding.refreshAmounts();
	}

	/**
	 * 
	 * @param date
	 * @throws SimulateException
	 * @throws NoPriceException
	 */
	private void _construct_beforeaction(Date date) throws SimulateException, NoPriceException {
		for (int i = index; i < existTransactionList.size(); ++i) {
			Transaction tr = existTransactionList.get(i);
			if (LTIDate.equals(LTIDate.clearHMSM(tr.getDate()), date) && tr.getTransactionType() == Configuration.TRANSACTION_TYPE_REAL) {
				simulateHolding.switchPrice(Configuration.PRICE_TYPE_OPEN);
				if (tr.getOperation().equals(Configuration.TRANSACTION_BUY_AT_OPEN)) {
					if (simulateHolding.getAsset(tr.getAssetName()) == null) {
						Asset asset = new Asset();
						asset.setName(tr.getAssetName());
						simulateHolding.addAsset(asset);
					}
					double close = securityManager.getPrice(tr.getSecurityID(), date);
					simulateHolding.buyAtOpen(tr.getAssetName(), tr.getSecurityID(), tr.getSymbol(), -tr.getAmount(), tr.getAmount() / tr.getShare(), close, tr.getReInvest());
				} else if (tr.getOperation().equals(Configuration.TRANSACTION_SELL_AT_OPEN)) {
					simulateHolding.sellAtOpen(tr.getAssetName(), tr.getSecurityID(), tr.getSymbol(), tr.getAmount());
				}
				simulateTransactions.add(tr);
			} else {
				index = i;
				break;
			}
		}
		simulateHolding.switchPrice(Configuration.PRICE_TYPE_CLOSE);
	}

	/**
	 * transaction driven时，模拟执行所有策略的action，每一个模拟日期执行一次
	 * 
	 * @param date
	 */
	private void _construct_action(Date date) {
		for (int i = index; i < existTransactionList.size(); ++i) {
			Transaction tr = existTransactionList.get(i);
			if (LTIDate.equals(LTIDate.clearHMSM(tr.getDate()), date) && tr.getTransactionType() == Configuration.TRANSACTION_TYPE_SCHEDULE) {
				simulateTransactions.add(tr);
			} else {
				index = i;
				break;
			}
		}
	}

	/**
	 * transaction driven时，执行所有reinvest之后的操作，每一个模拟日期执行一次
	 * 
	 * @param date
	 */
	private void _construct_doReinvest(Date date) {
		for (int i = index; i < existTransactionList.size(); ++i) {
			Transaction tr = existTransactionList.get(i);
			if (tr.getTransactionType() == Configuration.TRANSACTION_TYPE_REINVEST && LTIDate.equals(tr.getDate(), date)) {
				simulateTransactions.add(tr);
				simulateHolding.doReinvestByTransaction(tr);
			} else {
				index = i;
				break;
			}
		}
	}

	/**
	 * transaction driven时，执行所有策略的action之后的操作，每一个模拟日期执行一次
	 * 
	 * @param date
	 */
	private void _construct_afteraction(Date date) {
		if (LTIDate.isHoldingDate(date)) {
			simulateHolding.clearEmptyHolding();
			simulateHistoryHoldings.add(simulateHolding.clone());
			List<HoldingItem> his = simulateHolding.getHoldingItems();
			for (HoldingItem hi : his) {
				HoldingItem nhi = hi.clone();
				nhi.setID(null);
				holdingItems.add(nhi);
			}
		}
		transactionProcessor.clearAll();
	}
	
	public Date getValidDate(int day){
		return LTIDate.getValidDate(simulateHolding.getCurrentDate(), day);
	}
	
	
	/**
	 * 获得最后一次持有至今该fund的平均购买成本
	 * @param securityID
	 * @throws NoPriceException 
	 */
	public double getAverageCost(long securityID) throws NoPriceException{
		List<Transaction> transactionList = null;
		double curCost = 0.0;
		double curShare = 0.0;
		for(HoldingRecord hr: simulateHolding.getHoldingRecords()){
			if(hr.getSecurityID() == securityID && hr.getEndDate() == null){
				Date startHoldDate = hr.getStartDate();
				if(mode != MONITOR){
					transactionList = portfolioManager.getRealTransactions(simulatePortfolio.getID(), securityID, startHoldDate);
				}
				if (transactionList == null)
					transactionList = new ArrayList<Transaction>();
				if (simulateTransactions != null) {
					for (int i = 0; i < simulateTransactions.size(); ++i) {
						Transaction tr = simulateTransactions.get(i);
						if (tr.getTransactionType() == Configuration.TRANSACTION_TYPE_REAL && tr.getSecurityID() == securityID && !tr.getIsIgnore())
							transactionList.add(tr);
					}
				}
				if (transactionList != null && transactionList.size() > 0){
					for(Transaction t: transactionList){
						String operation = t.getOperation();
						double share = t.getShare();
						if(operation.equals(Configuration.TRANSACTION_BUY_AT_OPEN) || operation.equals(Configuration.TRANSACTION_BUY)){
							double price = securityManager.getPrice(securityID, t.getDate());
							curCost = (curCost * curShare + share * price)/(curShare + share);
							curShare += share;
						}
						else if(operation.equals(Configuration.TRANSACTION_SELL_AT_OPEN) || operation.equals(Configuration.TRANSACTION_SELL)){
							curShare -= share;
						}
					}
				}
			}
		}
		return curCost;
	}

	public double getAvailableTradingAmount(String symbol, int month, int priceType) {
		HoldingItem hi = simulateHolding.getHoldingItem(symbol);
		if (hi == null || hi.getShare() == 0)
			return 0;
		double price = 0;
		if (priceType == Configuration.PRICE_TYPE_CLOSE)
			price = hi.getClose();
		else if (priceType == Configuration.PRICE_TYPE_OPEN)
			price = hi.getOpen();
		Date curDate = simulateHolding.getCurrentDate();
		Date middleDate = LTIDate.getNDaysAgo(LTIDate.getNewTradingDate(curDate, TimeUnit.DAILY, 1), 30 * month);
		middleDate = LTIDate.getRecentNYSETradingDay(middleDate);
		Security security = securityManager.getBySymbol(symbol);
		long securityID = security.getID();
		double availShare = 0;
		List<Transaction> transactionList = null;
		if (mode != MONITOR)// if monitor, we don't need the transaction in database
			transactionList = portfolioManager.getNonScheduleTransactions(simulatePortfolio.getID(), securityID, curDate);
		if (transactionList == null)
			transactionList = new ArrayList<Transaction>();

		if (simulateTransactions != null) {
			for (int i = 0; i < simulateTransactions.size(); ++i) {
				Transaction tr = simulateTransactions.get(i);
				if (tr.getTransactionType() != Configuration.TRANSACTION_TYPE_SCHEDULE && tr.getSecurityID() == securityID && !tr.getIsIgnore())
					transactionList.add(tr.clone());
			}
		}

		if (transactionList != null && transactionList.size() > 0) {
			int size = transactionList.size();
			int lastIndex = 0;
			double shares = 0.0;
			for (int i = 0; i < size; ++i) {
				Transaction transaction = transactionList.get(i);
				String operation = transaction.getOperation();
				if (operation.equals(Configuration.TRANSACTION_SELL_AT_OPEN) || operation.equals(Configuration.TRANSACTION_SELL)) {
					shares = transaction.getShare();
					for (int j = lastIndex; j < i; ++j) {
						Transaction buyTransaction = transactionList.get(j);
						double innerShares = buyTransaction.getShare();
						String innerOperation = buyTransaction.getOperation();
						if (innerOperation.equals(Configuration.TRANSACTION_BUY_AT_OPEN) || innerOperation.equals(Configuration.TRANSACTION_REINVEST) || innerOperation.equals(Configuration.TRANSACTION_BUY) || innerOperation.equals(Configuration.TRANSACTION_VIRTUAL)) {
							if (shares < innerShares) {// we just sell shares  number share in buyTransaction
								buyTransaction.setShare(innerShares - shares);
								lastIndex = j;
								break;
							} else {
								shares -= innerShares;
								buyTransaction.setShare(0.0);
							}
						}
					}
				} else if (operation.equals(Configuration.TRANSACTION_SPLIT)) {
					double split = transaction.getShare();
					for (int j = lastIndex; j < i; ++j) {
						Transaction t = transactionList.get(j);
						if (!t.getOperation().equals(Configuration.TRANSACTION_SHORT_SELL_AT_OPEN))
							t.setShare(t.getShare() * split);
					}
				}
			}
			int middleDateIndex = size;
			for (int i = 0; i < size; ++i) {
				Transaction transaction = transactionList.get(i);
				if (LTIDate.before(middleDate, transaction.getDate())) {
					middleDateIndex = i;
					break;
				}
				String operation = transaction.getOperation();
				if (operation.equals(Configuration.TRANSACTION_BUY) || operation.equals(Configuration.TRANSACTION_BUY_AT_OPEN) || operation.equals(Configuration.TRANSACTION_REINVEST) || operation.equals(Configuration.TRANSACTION_VIRTUAL))
					availShare += transaction.getShare();
			}
			for (int i = middleDateIndex; i < size; ++i) {
				Transaction transaction = transactionList.get(i);
				if (!LTIDate.before(transaction.getDate(), curDate))
					break;
				String operation = transaction.getOperation();
				if (operation.equals(Configuration.TRANSACTION_REINVEST))
					availShare += transaction.getShare();
			}
		}
		List<Transaction> _transactions = transactionProcessor.getTransactions();// curDate's transactionList
		if (_transactions != null) {
			for (int j = 0; j < _transactions.size(); ++j) {
				Transaction transaction = _transactions.get(j);
				if (transaction.getTransactionType() == Configuration.TRANSACTION_TYPE_REAL && transaction.getSecurityID() == securityID) {
					String operation = transaction.getOperation();
					if (operation.equals(Configuration.TRANSACTION_BUY) || operation.equals(Configuration.TRANSACTION_BUY_AT_OPEN))
						availShare += transaction.getShare();
					else if (operation.equals(Configuration.TRANSACTION_SELL) || operation.equals(Configuration.TRANSACTION_SELL_AT_OPEN))
						availShare -= transaction.getShare();

				}
			}
		}
		return availShare * price;
	}

	public List<Transaction> getCashFlowTransaction() {
		List<Transaction> transactionList = null;
		if (mode != MONITOR)// if monitor, we don't need the transaction in database
			transactionList = portfolioManager.getCashFlowTransaction(simulatePortfolio.getID());
		if (transactionList == null)
			transactionList = new ArrayList<Transaction>();
		if (simulateTransactions != null) {
			for (int i = 0; i < simulateTransactions.size(); ++i) {
				Transaction tr = simulateTransactions.get(i);
				String operation = tr.getOperation();
				if (operation.equals(Configuration.WITHDRAW) || operation.equals(Configuration.DEPOSIT)) {
					transactionList.add(tr);
				}
			}
		}
		List<Transaction> _transactions = transactionProcessor.getTransactions();
		if (_transactions != null) {
			for (int i = 0; i < _transactions.size(); ++i) {
				Transaction tr = _transactions.get(i);
				String operation = tr.getOperation();
				if (operation.equals(Configuration.WITHDRAW) || operation.equals(Configuration.DEPOSIT)) {
					transactionList.add(tr);
				}
			}
		}
		return transactionList;
	}

	public int[] transactionDetection(Date startDate, Date endDate, String[] symbols) {
		int[] result = null;
		Long[] IDs = null;
		if (symbols != null && symbols.length > 0) {
			IDs = new Long[symbols.length];
			result = new int[symbols.length];
			for (int i = 0; i < symbols.length; ++i) {
				String symbol = symbols[i];
				Security se = securityManager.getBySymbol(symbol);
				IDs[i] = se.getID();
				result[i] = 0;
			}
			Interval interval = new Interval();
			interval.setStartDate(startDate);
			interval.setEndDate(endDate);
			List<Transaction> transactionList = null;
			if (mode != MONITOR)
				transactionList = portfolioManager.getTransactions(simulatePortfolio.getID(), interval);
			if (transactionList != null) {
				for (Transaction tr : transactionList) {
					for (int j = 0; j < IDs.length; ++j) {
						if (tr.getSecurityID().equals(IDs[j])) {
							String operation = tr.getOperation();
							if (operation.equalsIgnoreCase(Configuration.TRANSACTION_BUY) || operation.equalsIgnoreCase(Configuration.TRANSACTION_BUY_AT_OPEN))
								result[j] |= 1;
							else if (operation.equalsIgnoreCase(Configuration.TRANSACTION_SELL) || operation.equalsIgnoreCase(Configuration.TRANSACTION_SELL_AT_OPEN))
								result[j] |= 2;
							break;
						}
					}
				}
			}
			if (simulateTransactions != null) {
				for (int i = 0; i < simulateTransactions.size(); ++i) {
					Transaction tr = simulateTransactions.get(i);
					if (LTIDate.before(tr.getDate(), startDate))
						continue;
					if (LTIDate.after(tr.getDate(), endDate))
						break;
					String operation = tr.getOperation();
					for (int j = 0; j < IDs.length; ++j) {
						if (tr.getSecurityID().equals(IDs[j])) {
							if (operation.equalsIgnoreCase(Configuration.TRANSACTION_BUY) || operation.equalsIgnoreCase(Configuration.TRANSACTION_BUY_AT_OPEN))
								result[j] |= 1;
							else if (operation.equalsIgnoreCase(Configuration.TRANSACTION_SELL) || operation.equalsIgnoreCase(Configuration.TRANSACTION_SELL_AT_OPEN))
								result[j] |= 2;
							break;
						}
					}
				}
			}
			return result;
		}
		return null;
	}

	public void _construct_action_all(Date date) throws SimulateException, NoPriceException {
		for (int i = index; i < existTransactionList.size(); ++i) {
			Transaction tr = existTransactionList.get(i);
			String operation = tr.getOperation();
			if (LTIDate.equals(tr.getDate(), date)) {
				if (tr.getTransactionType() != Configuration.TRANSACTION_TYPE_SCHEDULE) {
					if (operation.equals(Configuration.TRANSACTION_BUY_AT_OPEN)) {
						simulateHolding.switchPrice(Configuration.PRICE_TYPE_OPEN);
						if (simulateHolding.getAsset(tr.getAssetName()) == null) {
							Asset asset = new Asset();
							asset.setName(tr.getAssetName());
							simulateHolding.addAsset(asset);
						}
						double open = securityManager.getOpenPrice(tr.getSecurityID(), date);
						double close = securityManager.getPrice(tr.getSecurityID(), date);
						double delta = tr.getAmount() - simulateHolding.getCash(); 
						if(delta >= 10)
							throw new SimulateException("Error: out of cash");
						double amount = tr.getAmount() > simulateHolding.getCash() ? simulateHolding.getCash(): tr.getAmount();
						simulateHolding.buyAtOpen(tr.getAssetName(), tr.getSecurityID(), tr.getSymbol(), amount, open, close, tr.getReInvest());
					} else if (operation.equals(Configuration.TRANSACTION_SELL_AT_OPEN)) {
						simulateHolding.switchPrice(Configuration.PRICE_TYPE_OPEN);
						HoldingItem hi = simulateHolding.getHoldingItem(tr.getAssetName(), tr.getSymbol());
						double share = hi.getShare();
						double delta = tr.getShare() - share;
						if(delta >= 2)
							throw new SimulateException("Error: out of share");
						double amount = tr.getAmount();
						if(delta > 0) amount = share * hi.getOpen();
						simulateHolding.sellAtOpen(tr.getAssetName(), tr.getSecurityID(), tr.getSymbol(), amount);
					} else if (operation.equals(Configuration.TRANSACTION_BUY)) {
						simulateHolding.switchPrice(Configuration.PRICE_TYPE_CLOSE);
						if (simulateHolding.getAsset(tr.getAssetName()) == null) {
							Asset asset = new Asset();
							asset.setName(tr.getAssetName());
							simulateHolding.addAsset(asset);
						}
						double delta = tr.getAmount() - simulateHolding.getCash();
						if(delta >= 10)
							throw new SimulateException("Error: out of cash");
						double open = securityManager.getOpenPrice(tr.getSecurityID(), date);
						double close = securityManager.getPrice(tr.getSecurityID(), date);
						double amount = tr.getAmount() > simulateHolding.getCash()? simulateHolding.getCash(): tr.getAmount();
						simulateHolding.baseBuy(tr.getAssetName(), tr.getSecurityID(), tr.getSymbol(), amount, open, close, tr.getReInvest(), tr.getTransactionType());
					} else if (operation.equals(Configuration.TRANSACTION_SELL)) {
						simulateHolding.switchPrice(Configuration.PRICE_TYPE_CLOSE);
						HoldingItem hi = simulateHolding.getHoldingItem(tr.getAssetName(), tr.getSymbol());
						double share = hi.getShare();
						double delta = tr.getShare() - share;
						if(delta >= 2)
							throw new SimulateException("Error: out of share");
						double amount = tr.getAmount();
						if(delta > 0) amount = hi.getClose() * share;
						simulateHolding.baseSell(tr.getAssetName(), tr.getSecurityID(), tr.getSymbol(), amount, Configuration.TRANSACTION_TYPE_REAL);
					} else if (operation.equals(Configuration.TRANSACTION_REINVEST)) {
						simulateHolding.doReinvestByTransaction(tr);
					} else if (operation.equals(Configuration.TRANSACTION_SHORT_SELL)) {
						simulateHolding.switchPrice(Configuration.PRICE_TYPE_CLOSE);
						double open = securityManager.getOpenPrice(tr.getSecurityID(), date);
						simulateHolding.shortSell(tr.getAssetName(), tr.getSecurityID(), tr.getSymbol(), -tr.getAmount(), tr.getAmount() / tr.getShare(), open);
					} else if (operation.equals(Configuration.TRANSACTION_SHORT_SELL_AT_OPEN)) {
						simulateHolding.switchPrice(Configuration.PRICE_TYPE_OPEN);
						double close = securityManager.getPrice(tr.getSecurityID(), date);
						simulateHolding.shortSell(tr.getAssetName(), tr.getSecurityID(), tr.getSymbol(), -tr.getAmount(), close, tr.getAmount() / tr.getShare());
					} else if (operation.equals(Configuration.TRANSACTION_SPLIT)) {
						simulateHolding.doSplitByTransaction(tr);
					} else if (operation.equals(Configuration.DEPOSIT)) {
						simulateHolding.deposit(tr.getAmount());
					} else if (operation.equals(Configuration.WITHDRAW)) {
						simulateHolding.withdraw(tr.getAmount());
					}
				}
				simulateTransactions.add(tr);
			} else {
				index = i;
				break;
			}
		}
		simulateHolding.switchPrice(Configuration.PRICE_TYPE_CLOSE);
	}

	public void construct() throws Exception {
		initconstruct();

		simulateDailydatas = new ArrayList<PortfolioDailyData>();

		if (expectedStopDate == null)
			expectedStopDate = new Date();

		mode = MONITOR;
		_construct_init();
		simulatePortfolio.setEndDate(LTIDate.getRecentNYSETradingDayForward(simulatePortfolio.getStartingDate()));
		// simulatePortfolio.setEndDate(LTIDate.getNewNYSETradingDay(simulatePortfolio.getStartingDate(), 1));
		PortfolioDailyData pdd = new PortfolioDailyData();
		pdd.setAmount(simulateHolding.getAmount());
		pdd.setPortfolioID(simulatePortfolio.getID());
		pdd.setDate(simulateHolding.getCurrentDate());
		simulateDailydatas.add(pdd);

		Date _startDate = LTIDate.getNewNYSETradingDay(simulatePortfolio.getEndDate(), 1);

		List<Date> dateList = LTIDate.getTradingDates(_startDate, expectedStopDate);

		HoldingInf backupHoldings = simulateHolding.clone();
		Date _endDate = dateList.get(0);
		for (int i = 0; i < dateList.size(); i++) {
			try {
				_construct_forward(dateList.get(i));
				// _construct_beforeaction(dateList.get(i));
				// _construct_action(dateList.get(i));
				// _construct_doReinvest(dateList.get(i));
				_construct_action_all(dateList.get(i));
				_construct_afteraction(dateList.get(i));
				PortfolioDailyData pddDate = new PortfolioDailyData();
				pddDate.setAmount(simulateHolding.getAmount());
				pddDate.setDate(dateList.get(i));
				pddDate.setPortfolioID(simulatePortfolio.getID());
				simulateDailydatas.add(pddDate);
			} catch (NoPriceException npe) {
				System.out.println(StringUtil.getStackTraceString(npe));
				simulateHolding = backupHoldings;
				arrangeScheduleTransactions(dateList.get(i));
				break;
			}
			_endDate = dateList.get(i);
			backupHoldings = simulateHolding.clone();
		}
		simulatePortfolio.setEndDate(_endDate);
		if(simulateTransactions!=null&&simulateTransactions.size()>0){
			simulatePortfolio.setLastTransactionDate(getTransactionDate());
		}

		if (LTIDate.equals(_startDate, _endDate)) {
			mode = UNCHANGE;
			return;
		}

		_end();

		portfolioState.setState(Configuration.PORTFOLIO_RUNNING_STATE_FINISHED);

		portfolioState.setUpdateTime(new Date());

		computeMPT(simulateDailydatas);
	}

	
	
	public void computeMPT(PortfolioDailyData curPdd) {
		BaseFormulaUtil bf = new BaseFormulaUtil();
		Long portfolioID = simulatePortfolio.getID();
		List<PortfolioMPT> portfolioMPTList = portfolioManager.getEveryYearsMPT(portfolioID);
		//只需要最新以及1年、3年、5年前以及startDate的dailydata
		Long t1 = System.currentTimeMillis();
		Configuration.writePortfolioUpdateLog("Part 3.1:update portfolioMPT entry", new Date(), "\n************************************\n part 3.1:update portfolioMPT entry start \n************************************\n");
		simulateMPTs = bf.computePortfolioMPTsForAR(simulatePortfolio.getID(), simulatePortfolio.getStartingDate(), curPdd, portfolioMPTList);
		Long t2 = System.currentTimeMillis();

		System.out.println("compute MPT Time:" + (t2 - t1));

		if (listener != null) {
			listener.afterComputeMPT(this);
		}
	}
	/**
	 * 计算MPT
	 * 
	 * @param amounts
	 *            每天的Daily amount
	 * @throws Exception
	 */
	public void computeMPT(List<PortfolioDailyData> pdds) {
		BaseFormulaUtil bf = new BaseFormulaUtil();
		Long portfolioID = simulatePortfolio.getID();
		long benchmarkID = simulateHolding.getBenchmarkID();
		long cashID = securityManager.get("CASH").getID();
		if (pdds == null || pdds.size() == 0)
			return;

		Date startDate = pdds.get(0).getDate();
		Date endDate = pdds.get(pdds.size() - 1).getDate();

		List<SecurityDailyData> sdds = securityManager.getDailyDatas(benchmarkID, LTIDate.getNewWeekDay(startDate, -1), LTIDate.getNewWeekDay(endDate, 1));
		List<SecurityDailyData> fdds = securityManager.getDailyDatas(cashID, startDate, endDate);
		List<PortfolioMPT> portfolioMPTList = portfolioManager.getEveryYearsMPT(portfolioID);
		if (sdds == null || sdds.size() == 0)
			return;
		//
		Long classID = null;
		if (simulateStrategies != null && simulateStrategies.size() > 0) {
			classID = simulateStrategies.get(0).StrategyClassID;
		}

		Long t1 = System.currentTimeMillis();

		List<Transaction> transactionList = null;
		if (classID != null && classID == 5l) {
			transactionList = getCashFlowTransaction();
		}

		simulateMPTs = bf.computePortfolioMPTs(simulatePortfolio, classID, pdds, sdds, fdds, portfolioMPTList, transactionList);
		// HashMap<Integer, PortfolioMPT> portfolioMPTMap = bf.computePortfolioMPTs(simulatePortfolio, 0l, pdds, sdds,fdds, portfolioMPTList);

		Long t2 = System.currentTimeMillis();

		System.out.println("compute MPT Time:" + (t2 - t1));

		if (listener != null) {
			listener.afterComputeMPT(this);
		}
	}

	public List<SimulateStrategy> getSimulateStrategies() {
		return simulateStrategies;
	}

	public Portfolio getSimulatePortfolio() {
		return simulatePortfolio;
	}

	public List<com.lti.service.bo.Log> getSimulateLogs() {
		return simulateLogs;
	}

	public List<com.lti.service.bo.Transaction> getSimulateTransactions() {
		return simulateTransactions;
	}

	public List<PortfolioDailyData> getSimulateDailydatas() {
		return simulateDailydatas;
	}

	public List<PortfolioMPT> getSimulateMPTs() {
		return simulateMPTs;
	}

	public List<HoldingItem> getHoldingItems() {
		return holdingItems;
	}

	public SimulatorTransactionProcessor getTransactionProcessor() {
		return transactionProcessor;
	}

	public HoldingInf getSimulateHolding() {
		return simulateHolding;
	}

	public PortfolioState getPortfolioState() {
		return portfolioState;
	}

	/**
	 * 返回delay的holding
	 * 
	 * @return
	 */
	public HoldingInf getSimulateDelayHolding() {
		Date delayDate = LTIDate.getHoldingDateMonthEnd(simulatePortfolio.getEndDate());
		if (simulateHistoryHoldings.size() == 0) {
			portfolioHoldingManager = (PortfolioHoldingManager) ContextHolder.getInstance().getApplicationContext().getBean("portfolioHoldingManager");
			List<HoldingItem> his=portfolioHoldingManager.getHoldingItems(simulatePortfolio.getID(), delayDate);
			HoldingInf hinf=simulateHolding.clone();
			hinf.setCurrentDate(delayDate);
			hinf.getAssets().clear();
			if(his!=null){
				for(HoldingItem hi:his){
					Asset a = hinf.getAsset(hi.getAssetName());
					if(a==null){
						a=new Asset();
						a.setName(hi.getAssetName());
						try {
							hinf.addAsset(a);
						} catch (SimulateException e) {
							e.printStackTrace();
						}
					}
					List<HoldingItem> holdings=a.getHoldingItems();
					if(holdings==null){
						holdings=new ArrayList<HoldingItem>();
					}
					a.getHoldingItems().add(hi);
				}
			}
			return hinf;
		}
		
		for(HoldingInf hif:simulateHistoryHoldings){
			if(LTIDate.equals(hif.getCurrentDate(),delayDate)){
				return hif.clone();
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		// PortfolioManager
		// portfolioManager=ContextHolder.getPortfolioManager();
		// Portfolio p=portfolioManager.get(17342l);
		// PortfolioInf pif=portfolioManager.getPortfolioInf(17342l,
		// Configuration.PORTFOLIO_HOLDING_ORIGINAL);
		// List<SimulateStrategy> simulateStrategies=new
		// ArrayList<SimulateStrategy>();
		// StrategyInf sif=p.getStrategies();
		// if(sif.getAssetAllocationStrategy().getID()!=0l){
		// StrategyCode
		// sc=ContextHolder.getStrategyManager().getLatestStrategyCode(sif.getAssetAllocationStrategy().getID());
		// SimulateStrategy ss=Compiler.getStrategyInstance(sc.getCode());
		// ss.setSimulateParameters(p.getStrategies().getAssetAllocationStrategy().getParameter());
		// simulateStrategies.add(ss);
		// }
		// Simulator sim=new Simulator(null, simulateStrategies, p,
		// pif.getHolding(), null, true,null);
		// sim.simulate();
		List<String> strList = new ArrayList<String>();
		strList.add(Configuration.TRANSACTION_BUY);
		strList.add(Configuration.TRANSACTION_SELL);
		strList.add(Configuration.TRANSACTION_BUY_AT_OPEN);
		strList.add(Configuration.TRANSACTION_SELL_AT_OPEN);
		strList.add(Configuration.TRANSACTION_REINVEST);
		strList.add(Configuration.TRANSACTION_SPLIT);
		strList.add(Configuration.TRANSACTION_SHORT_SELL);
		strList.add(Configuration.TRANSACTION_SHORT_SELL_AT_OPEN);
		strList.add(Configuration.TRANSACTION_VIRTUAL);
		Collections.sort(strList);
		for (String str : strList)
			System.out.println(str);
	}

	public List<GlobalObject> getGlobalObjects() {
		return globalObjects;
	}

	public HoldingInf getSimulateExpectedHolding() {
		return simulateExpectedHolding;
	}

	public List<HoldingRecord> getSimulateHoldingRecords() {
		return simulateHolding.getHoldingRecords();
	}

	public int getFirstIndex() {
		return firstIndex;
	}

	public boolean isCustomized() {
		return isCustomized;
	}

	public void setCustomized(boolean isCustomized) {
		this.isCustomized = isCustomized;
	}

}
