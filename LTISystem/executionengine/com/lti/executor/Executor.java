package com.lti.executor;

import java.io.File;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.lti.listener.Listener;
import com.lti.listener.SimulatorProcessor;
import com.lti.service.PortfolioHoldingManager;
import com.lti.service.PortfolioManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.CachePortfolioItem;
import com.lti.service.bo.CacheStrategyItem;
import com.lti.service.bo.GlobalObject;
import com.lti.service.bo.HoldingItem;
import com.lti.service.bo.HoldingRecord;
import com.lti.service.bo.Log;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.PortfolioInf;
import com.lti.service.bo.PortfolioMPT;
import com.lti.service.bo.PortfolioState;
import com.lti.service.bo.Security;
import com.lti.service.bo.StrategyCode;
import com.lti.service.bo.Transaction;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.type.executor.SimulateStrategy;
import com.lti.type.executor.StrategyBasicInf;
import com.lti.type.executor.StrategyInf;
import com.lti.type.executor.iface.IStrategyInfProcessor;
import com.lti.type.finance.Asset;
import com.lti.type.finance.HoldingInf;
import com.lti.type.finance.PortfolioInfo;
import com.lti.util.ChartUtil;
import com.lti.util.CurveChartUtil;
import com.lti.util.LTIDate;
import com.lti.util.StringUtil;

public class Executor {

	private PortfolioManager portfolioManager;
	private StrategyManager strategyManager;

	public Simulator getSimulator() {
		return simulator;
	}

	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}

	public Executor() {
		portfolioManager = ContextHolder.getPortfolioManager();
		strategyManager = ContextHolder.getStrategyManager();
	}

	// private List<String> createClearStatements() {
	// List<String> sqls = new ArrayList<String>();
	// long portfolioid = simulator.getSimulatePortfolio().getID();
	// sqls.add("delete from " + Configuration.TABLE_LOG + " where portfolioid="
	// + portfolioid);
	// sqls.add("delete from " + Configuration.TABLE_PORTFOLIO_DAILY_DATA + "
	// where portfolioid=" + portfolioid);
	// sqls.add("delete from " + Configuration.TABLE_PORTFOLIO_MPT + " where
	// portfolioid=" + portfolioid);
	// sqls.add("delete from " + Configuration.TABLE_TRANACTION + " where
	// portfolioid=" + portfolioid);
	// sqls.add("delete from " + Configuration.TABLE_PORTFOLIO_HOLDINGS + "
	// where portfolioid=" + portfolioid);
	// sqls.add("delete from " + Configuration.TABLE_PORTFOLIO_HOLDINGITEM + "
	// where portfolioid=" + portfolioid);
	// sqls.add("delete from " + Configuration.TABLE_HOLDINGRECORD + " where
	// portfolioid=" + portfolioid);
	// return sqls;
	// }

	// public void executeUpdate(Session session, List<String> sqls) {
	// for (int i = 0; i < sqls.size(); i++) {
	// Query query = session.createSQLQuery(sqls.get(i));
	// query.executeUpdate();
	// }
	// }
	//
	// public void update(Session session, List objs) {
	// if (objs == null || objs.size() == 0)
	// return;
	// for (int i = 0; i < objs.size(); i++) {
	// session.save(((List) objs).get(i));
	// }
	// }
	//
	// public void saveOrUpdate(Session session, List objs) {
	// if (objs == null || objs.size() == 0)
	// return;
	// for (int i = 0; i < objs.size(); i++) {
	// session.saveOrUpdate(((List) objs).get(i));
	// }
	// }

	/**
	 * 不考虑Delay的情况
	 * 
	 * @param cpi
	 * @param p
	 * @param mpts
	 */
	private void fillMPT(CachePortfolioItem cpi, Portfolio p, Map<Integer, PortfolioMPT> mpts) {
		PortfolioMPT mpt1 = mpts.get(PortfolioMPT.LAST_ONE_YEAR);
		if (mpt1 != null) {
			cpi.setAR1(mpt1.getAR());
			cpi.setSharpeRatio1(mpt1.getSharpeRatio());
		}

		PortfolioMPT mpt3 = mpts.get(PortfolioMPT.LAST_THREE_YEAR);
		if (mpt3 != null) {
			cpi.setAR3(mpt3.getAR());
			cpi.setSharpeRatio3(mpt3.getSharpeRatio());
		}

		PortfolioMPT mpt5 = mpts.get(PortfolioMPT.LAST_FIVE_YEAR);
		if (mpt5 != null) {
			cpi.setAR5(mpt5.getAR());
			cpi.setSharpeRatio5(mpt5.getSharpeRatio());
		}

		cpi.setLastTransactionDate(p.getLastTransactionDate());
		cpi.setEndDate(p.getEndDate());

		cpi.setPortfolioID(p.getID());
		cpi.setUserID(p.getUserID());

		cpi.setMainStrategyID(p.getMainStrategyID());
		cpi.setType(p.getType());
		cpi.setPortfolioName(p.getName());
		cpi.setUserName(p.getUserName());
		cpi.setState(p.getState());
	}

	/**
	 * 对CachePortfolioItem和CacheStrategyItem进行处理
	 * 
	 * @param cpi_d
	 * @param cpi_r
	 * @param cpis
	 * @param csis
	 * @param p
	 * @param mptlist
	 */
	private void manageMPTs(CachePortfolioItem cpi_d, CachePortfolioItem cpi_r, List<CachePortfolioItem> cpis, List<CacheStrategyItem> csis, Portfolio p, List<PortfolioMPT> mptlist) {
		Map<Integer, PortfolioMPT> mpts = new HashMap<Integer, PortfolioMPT>();
		for (PortfolioMPT m : mptlist) {
			mpts.put(m.getYear(), m);
		}
		if (cpi_d == null) {
			cpi_d = new CachePortfolioItem();
			cpi_d.setPortfolioID(p.getID());
			cpi_d.setGroupID(0l);
			cpi_d.setRoleID(Configuration.ROLE_PORTFOLIO_DELAYED_ID);
		}
		fillMPT(cpi_d, p, mpts);
		if (cpi_r == null) {
			cpi_r = new CachePortfolioItem();
			cpi_r.setGroupID(0l);
			cpi_r.setPortfolioID(p.getID());
			cpi_r.setRoleID(Configuration.ROLE_PORTFOLIO_REALTIME_ID);
		}
		fillMPT(cpi_r, p, mpts);
		if (cpis != null && cpis.size() > 0) {
			for (CachePortfolioItem cpi : cpis) {
				if (cpi.getGroupID() == 0l)
					continue;
				fillMPT(cpi, p, mpts);
			}
		}
		if (p.isModel() && p.getMainStrategyID() != null) {

			Map<Long, CacheStrategyItem> csis_map = new HashMap<Long, CacheStrategyItem>();
			if (csis != null && csis.size() > 0) {
				for (CacheStrategyItem csi : csis) {
					csis_map.put(csi.getGroupID(), csi);
				}
				// 注意，排序是所有实时与非实时一起排序
				for (CachePortfolioItem cpi : cpis) {
					CacheStrategyItem csi = csis_map.get(cpi.getGroupID());
					if (csi == null || cpi.getSharpeRatio1() == null) {
						continue;
					}
					if (csi.getSharpeRatio1() == null || csi.getSharpeRatio1() < cpi.getSharpeRatio1()) {
						csi.setPortfolioID(cpi.getPortfolioID());
						csi.setAR1(cpi.getAR1());
						csi.setAR3(cpi.getAR3());
						csi.setAR5(cpi.getAR5());
						csi.setSharpeRatio1(cpi.getSharpeRatio1());
						csi.setSharpeRatio3(cpi.getSharpeRatio3());
						csi.setSharpeRatio5(cpi.getSharpeRatio5());
						csi.setEndDate(cpi.getEndDate());
						csi.setLastTransactionDate(cpi.getLastTransactionDate());
						csi.setPortfolioName(cpi.getPortfolioName());
					}
				}
			}
		} else
			csis = null;
	}

	public void save() {

		if (simulator.getMode() == Simulator.UNCHANGE) {
			if (simulator.getSimulateLogs() != null && simulator.getSimulateLogs().size() > 0)
				portfolioManager.saveLogs(simulator.getSimulateLogs());
			return;
		}

		boolean deleteExpectedHolding = false;
		PortfolioInf pif = portfolioManager.getPortfolioInf(simulator.getSimulatePortfolio().getID(), Configuration.PORTFOLIO_HOLDING_CURRENT);
		PortfolioInf dpif = portfolioManager.getPortfolioInf(simulator.getSimulatePortfolio().getID(), Configuration.PORTFOLIO_HOLDING_DELAY);
		PortfolioInf expectedpif = portfolioManager.getPortfolioInf(simulator.getSimulatePortfolio().getID(), Configuration.PORTFOLIO_HOLDING_EXPECTED);

		CachePortfolioItem cpi_d = portfolioManager.getCachePortfolioItem(0, Configuration.ROLE_PORTFOLIO_DELAYED_ID, simulator.getSimulatePortfolio().getID());
		CachePortfolioItem cpi_r = portfolioManager.getCachePortfolioItem(0, Configuration.ROLE_PORTFOLIO_REALTIME_ID, simulator.getSimulatePortfolio().getID());
		List<CachePortfolioItem> cpis = portfolioManager.getCachePortfolioItems(simulator.getSimulatePortfolio().getID());
		List<CacheStrategyItem> csis = null;
		if (simulator.getSimulatePortfolio().getMainStrategyID() != null) {
			csis = strategyManager.getCacheStrategyItems(simulator.getSimulatePortfolio().getMainStrategyID());
		}
		if (pif == null) {
			pif = new PortfolioInf();
			pif.setPortfolioID(simulator.getSimulatePortfolio().getID());
			pif.setType(Configuration.PORTFOLIO_HOLDING_CURRENT);
			pif.setHolding(simulator.getSimulateHolding());
		} else {
			pif.setHolding(simulator.getSimulateHolding());
		}
		HoldingInf _delayHolding = simulator.getSimulateDelayHolding();
		if (_delayHolding != null) {
			if (dpif == null) {
				dpif = new PortfolioInf();
				dpif.setPortfolioID(simulator.getSimulatePortfolio().getID());
				dpif.setType(Configuration.PORTFOLIO_HOLDING_DELAY);
				dpif.setHolding(_delayHolding);
			} else
				dpif.setHolding(_delayHolding);
		}
		if (simulator.getSimulateExpectedHolding() != null) {
			if (expectedpif == null) {
				expectedpif = new PortfolioInf();
				expectedpif.setPortfolioID(simulator.getSimulatePortfolio().getID());
				expectedpif.setType(Configuration.PORTFOLIO_HOLDING_EXPECTED);
				expectedpif.setHolding(simulator.getSimulateExpectedHolding());
			} else
				expectedpif.setHolding(simulator.getSimulateExpectedHolding());
		} else if (expectedpif != null)
			deleteExpectedHolding = true;

		Security security;
		String portfolioID = simulator.getSimulatePortfolio().getID().toString();
		security = ContextHolder.getSecurityManager().get("P_" + portfolioID);
		if (security == null)
			security = new Security();
		security.setName(simulator.getSimulatePortfolio().getName());
		security.setSymbol("P_" + portfolioID);
		security.setSecurityType(Configuration.SECURITY_TYPE_PORTFOLIO);
		security.setStartDate(simulator.getSimulatePortfolio().getStartingDate());
		security.setEndDate(simulator.getSimulatePortfolio().getEndDate());
		security.setMptLastDate(simulator.getSimulatePortfolio().getEndDate());

		// 更新MPT的各个值域，没有写入数据库
		manageMPTs(cpi_d, cpi_r, cpis, csis, simulator.getSimulatePortfolio(), simulator.getSimulateMPTs());
		// 更新各种图
		try {
			simulator.getPortfolioState().setDelayPieChart(plotHoldingChart(dpif.getHolding(), false));
			simulator.getPortfolioState().setRealtimePieChart(plotHoldingChart(pif.getHolding(), true));
			if (simulator.getMode() == Simulator.UPDATE || simulator.getMode() == Simulator.REINT) {
				List<PortfolioDailyData> pdds = portfolioManager.getDailydatas(simulator.getSimulatePortfolio().getID());
				pdds.addAll(simulator.getSimulateDailydatas());
				plotCurveChart(pdds, simulator.getPortfolioState());
			} else {
				plotCurveChart(simulator.getSimulateDailydatas(), simulator.getPortfolioState());
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		PortfolioInfo portfolioInfo = new PortfolioInfo();
		portfolioInfo.setSecurity(security);
		portfolioInfo.setPortfolio(simulator.getSimulatePortfolio());
		portfolioInfo.setMode(simulator.getMode());
		portfolioInfo.setSimulateLogs(simulator.getSimulateLogs());
		portfolioInfo.setSimulateHoldingRecords(simulator.getSimulateHoldingRecords());
		portfolioInfo.setSimulateDailyDatas(simulator.getSimulateDailydatas());
		portfolioInfo.setSimulatePortfolioMPTs(simulator.getSimulateMPTs());
		portfolioInfo.setSimulateHoldingItems(simulator.getHoldingItems());
		portfolioInfo.setSimulateTransactions(simulator.getSimulateTransactions());
		portfolioInfo.setGlobalObjects(simulator.getGlobalObjects());
		portfolioInfo.setPortfolioState(simulator.getPortfolioState());
		portfolioInfo.setFirstIndex(simulator.getFirstIndex());
		portfolioInfo.setSimulateHolding(simulator.getSimulateHolding());
		portfolioInfo.setSimulateDelayHolding(_delayHolding);
		portfolioInfo.setSimulateExpectedHolding(simulator.getSimulateExpectedHolding());
		portfolioInfo.setCurrentPI(pif);
		portfolioInfo.setDelayPI(dpif);
		portfolioInfo.setExpectedPI(expectedpif);
		portfolioInfo.setRealCPI(cpi_r);
		portfolioInfo.setDelayCPI(cpi_d);
		portfolioInfo.setCPIs(cpis);
		portfolioInfo.setCSIs(csis);
		portfolioInfo.setDeleteExpectedHolding(deleteExpectedHolding);
		portfolioInfo.setCustomized(simulator.isCustomized());

		portfolioManager.savePortfolioInformation(portfolioInfo);

		if (listener != null) {
			listener.afterFinish(simulator);
		}
	}

	// private void saveMPTs(CachePortfolioItem cpi_d, CachePortfolioItem cpi_r,
	// List<CachePortfolioItem> cpis, List<CacheStrategyItem> csis, Session
	// session, Portfolio p, List<PortfolioMPT> mptlist) {
	// Map<Integer, PortfolioMPT> mpts = new HashMap<Integer, PortfolioMPT>();
	// for (PortfolioMPT m : mptlist) {
	// mpts.put(m.getYear(), m);
	// }
	// if (cpi_d == null) {
	// cpi_d = new CachePortfolioItem();
	// cpi_d.setPortfolioID(p.getID());
	// cpi_d.setGroupID(0l);
	// cpi_d.setRoleID(Configuration.ROLE_PORTFOLIO_DELAYED_ID);
	//
	// }
	// fillMPT(cpi_d, p, mpts);
	// // 这里update cpi_d
	// session.saveOrUpdate(cpi_d);
	// if (cpi_r == null) {
	// cpi_r = new CachePortfolioItem();
	// cpi_r.setGroupID(0l);
	// cpi_r.setPortfolioID(p.getID());
	// cpi_r.setRoleID(Configuration.ROLE_PORTFOLIO_REALTIME_ID);
	// }
	// // 这里update cpi_r
	// fillMPT(cpi_r, p, mpts);
	// session.saveOrUpdate(cpi_r);
	// if (cpis != null && cpis.size() > 0) {
	// for (CachePortfolioItem cpi : cpis) {
	// if (cpi.getGroupID() == 0l)
	// continue;
	// fillMPT(cpi, p, mpts);
	// session.update(cpi);
	// }
	// // 这里update cpis
	//
	// }
	//
	// if (p.isModel() && p.getMainStrategyID() != null) {
	//
	// Map<Long, CacheStrategyItem> csis_map = new HashMap<Long,
	// CacheStrategyItem>();
	// if (csis != null && csis.size() > 0) {
	// for (CacheStrategyItem csi : csis) {
	// csis_map.put(csi.getGroupID(), csi);
	// }
	// // 注意，排序是所有实时与非实时一起排序
	// for (CachePortfolioItem cpi : cpis) {
	// CacheStrategyItem csi = csis_map.get(cpi.getGroupID());
	// if (csi == null || cpi.getSharpeRatio1() == null) {
	// continue;
	// }
	// if (csi.getSharpeRatio1() == null || csi.getSharpeRatio1() <
	// cpi.getSharpeRatio1()) {
	// csi.setPortfolioID(cpi.getPortfolioID());
	// csi.setAR1(cpi.getAR1());
	// csi.setAR3(cpi.getAR3());
	// csi.setAR5(cpi.getAR5());
	// csi.setSharpeRatio1(cpi.getSharpeRatio1());
	// csi.setSharpeRatio3(cpi.getSharpeRatio3());
	// csi.setSharpeRatio5(cpi.getSharpeRatio5());
	// csi.setEndDate(cpi.getEndDate());
	// csi.setLastTransactionDate(cpi.getLastTransactionDate());
	// csi.setPortfolioName(cpi.getPortfolioName());
	// }
	// }// CachePortfolioItem cpi:cpis
	// // 这里update csis
	// for (CacheStrategyItem csi : csis) {
	// session.update(csi);
	// }
	// }// csis!=null&&csis.size()>0
	// }// p.isModel()&&p.getMainStrategyID()!=null
	// }

	// // 是否要删除schedule transaction
	// public void save() {
	//
	// HibernateDaoSupport hds = (HibernateDaoSupport) portfolioManager;
	//
	// if (simulator.getMode() == Simulator.UNCHANGE) {
	// if (simulator.getSimulateLogs() != null &&
	// simulator.getSimulateLogs().size() > 0) {
	// portfolioManager.saveLogs(simulator.getSimulateLogs());
	// }
	// return;
	// }
	//
	// PortfolioInf pif =
	// portfolioManager.getPortfolioInf(simulator.getSimulatePortfolio().getID(),
	// Configuration.PORTFOLIO_HOLDING_CURRENT);
	// PortfolioInf dpif =
	// portfolioManager.getPortfolioInf(simulator.getSimulatePortfolio().getID(),
	// Configuration.PORTFOLIO_HOLDING_DELAY);
	// PortfolioInf expectedpif =
	// portfolioManager.getPortfolioInf(simulator.getSimulatePortfolio().getID(),
	// Configuration.PORTFOLIO_HOLDING_EXPECTED);
	//
	// CachePortfolioItem cpi_d = portfolioManager.getCachePortfolioItem(0,
	// Configuration.ROLE_PORTFOLIO_DELAYED_ID,
	// simulator.getSimulatePortfolio().getID());
	// CachePortfolioItem cpi_r = portfolioManager.getCachePortfolioItem(0,
	// Configuration.ROLE_PORTFOLIO_REALTIME_ID,
	// simulator.getSimulatePortfolio().getID());
	// List<CachePortfolioItem> cpis =
	// portfolioManager.getCachePortfolioItems(simulator.getSimulatePortfolio().getID());
	// List<CacheStrategyItem> csis = null;
	// if (simulator.getSimulatePortfolio().getMainStrategyID() != null) {
	// csis =
	// strategyManager.getCacheStrategyItems(simulator.getSimulatePortfolio().getMainStrategyID());
	// }
	//
	// Security se;
	// String portfolioID = simulator.getSimulatePortfolio().getID().toString();
	// se = ContextHolder.getSecurityManager().get("P_" + portfolioID);
	// if (se == null) {
	// se = new Security();
	// }
	// se.setName(simulator.getSimulatePortfolio().getName());
	// se.setSymbol("P_" + portfolioID);
	// se.setSecurityType(Configuration.SECURITY_TYPE_PORTFOLIO);
	//
	// Session session = null;
	// org.hibernate.Transaction tx = null;
	//
	// try {
	// session = hds.getSessionFactory().openSession();
	// tx = session.beginTransaction();
	// if (simulator.getMode() == Simulator.MONITOR) {
	// executeUpdate(session, createClearStatements());
	// }
	//
	// session.update(simulator.getSimulatePortfolio());
	// if (simulator.getFirstIndex() != -1) {// 做了DIVIDENDADJUSTMENT
	// saveOrUpdate(session,
	// simulator.getSimulateDailydatas().subList(simulator.getFirstIndex(),
	// simulator.getSimulateDailydatas().size()));
	// } else
	// update(session, simulator.getSimulateDailydatas());
	// if (simulator.getMode() == Simulator.MONITOR) {
	// update(session, simulator.getSimulateLogs());
	// update(session, simulator.getSimulateMPTs());
	// update(session, simulator.getSimulateTransactions());
	// update(session, simulator.getHoldingItems());
	// } else {
	// saveOrUpdate(session, simulator.getSimulateLogs());
	// saveOrUpdate(session, simulator.getSimulateMPTs());
	// saveOrUpdate(session, simulator.getSimulateTransactions());
	// saveOrUpdate(session, simulator.getHoldingItems());
	// }
	// // 处理holding records,只保存最近三个月内持有的fund
	// Date towMonthsAgoDate = LTIDate.getNDaysAgo(new Date(), 90);
	// if (simulator.getSimulateHoldingRecords() != null) {
	// for (HoldingRecord hr : simulator.getSimulateHoldingRecords()) {
	// if (hr.getEndDate() == null || (!LTIDate.after(hr.getStartDate(),
	// hr.getEndDate()) && LTIDate.before(towMonthsAgoDate, hr.getEndDate()))) {
	// hr.setDividendDateStr(null);
	// session.saveOrUpdate(hr);
	// } else
	// session.delete(hr);
	// }
	// }
	//
	// if (pif == null) {
	// pif = new PortfolioInf();
	// pif.setPortfolioID(simulator.getSimulatePortfolio().getID());
	// pif.setType(Configuration.PORTFOLIO_HOLDING_CURRENT);
	// pif.setHolding(simulator.getSimulateHolding());
	// session.save(pif);
	// } else {
	// pif.setHolding(simulator.getSimulateHolding());
	// session.update(pif);
	// }
	// // update的时候可能没有delay holding
	// if (simulator.getSimulateDelayHolding() != null) {
	// if (dpif == null) {
	// dpif = new PortfolioInf();
	// dpif.setPortfolioID(simulator.getSimulatePortfolio().getID());
	// dpif.setType(Configuration.PORTFOLIO_HOLDING_DELAY);
	// dpif.setHolding(simulator.getSimulateDelayHolding());
	// session.save(dpif);
	// } else {
	// dpif.setHolding(simulator.getSimulateDelayHolding());
	// session.update(dpif);
	// }
	//
	// }
	//
	// if (simulator.getSimulateExpectedHolding() != null) {
	// if (expectedpif == null) {
	// expectedpif = new PortfolioInf();
	// expectedpif.setPortfolioID(simulator.getSimulatePortfolio().getID());
	// expectedpif.setType(Configuration.PORTFOLIO_HOLDING_EXPECTED);
	// expectedpif.setHolding(simulator.getSimulateExpectedHolding());
	// session.save(expectedpif);
	// } else {
	// expectedpif.setHolding(simulator.getSimulateExpectedHolding());
	// session.update(expectedpif);
	// }
	// } else {
	// if (expectedpif != null) {
	// session.delete(expectedpif);
	// }
	// }
	//
	// saveMPTs(cpi_d, cpi_r, cpis, csis, session,
	// simulator.getSimulatePortfolio(), simulator.getSimulateMPTs());
	//
	// se.setStartDate(simulator.getSimulatePortfolio().getStartingDate());
	// se.setEndDate(simulator.getSimulatePortfolio().getEndDate());
	// se.setMptLastDate(simulator.getSimulatePortfolio().getEndDate());
	//
	// session.saveOrUpdate(se);
	//
	// // List<String> deletegos=new ArrayList<String>();
	// // for(GlobalObject go:simulator.getGlobalObjects()){
	// // deletegos.add("delete from ltisystem_globalobject where
	// `key`='"+go.getKey()+"'");
	// // }
	// // executeUpdate(session, deletegos);
	//
	// for (GlobalObject go : simulator.getGlobalObjects()) {
	// session.saveOrUpdate(go);
	// }
	//
	// // 更新各种图
	// simulator.getPortfolioState().setDelayPieChart(plotHoldingChart(dpif.getHolding(),
	// false));
	// simulator.getPortfolioState().setRealtimePieChart(plotHoldingChart(pif.getHolding(),
	// true));
	// if (simulator.getMode() == Simulator.UPDATE || simulator.getMode() ==
	// Simulator.REINT) {
	// List<PortfolioDailyData> pdds =
	// portfolioManager.getDailydatas(simulator.getSimulatePortfolio().getID());
	// pdds.addAll(simulator.getSimulateDailydatas());
	// plotCurveChart(pdds, simulator.getPortfolioState());
	// } else {
	// plotCurveChart(simulator.getSimulateDailydatas(),
	// simulator.getPortfolioState());
	// }
	//
	// session.saveOrUpdate(simulator.getPortfolioState());
	//
	// session.flush();
	// tx.commit();
	// session.clear();
	// session.close();
	//
	// } catch (Throwable e) {
	// if (tx.isActive())
	// tx.rollback();
	// if (session.isOpen())
	// session.close();
	// throw new RuntimeException("Save data error where update portfolio [" +
	// simulator.getSimulatePortfolio().getID() + "].", e);
	// }
	//
	// if (listener != null) {
	// listener.afterFinish(simulator);
	// }
	// }

	private static byte[] plotHoldingChart(HoldingInf hif, boolean realtime) throws Exception {
		PortfolioHoldingManager portfolioHoldingManager = ContextHolder.getPortfolioHoldingManager();
		PortfolioManager pm = ContextHolder.getPortfolioManager();
		Map<String, Double> sectors = new TreeMap<String, Double>();
		double total = 0.0;
		boolean shortSell = false;
		double totalAmount = 0.0;
		if (hif.getAssets() != null) {
			Iterator<Asset> iter_asset = hif.getAssets().iterator();
			List<HoldingItem> holdingItems = new ArrayList<HoldingItem>();
			while (iter_asset.hasNext()) {
				Asset a = iter_asset.next();
				if (a.getHoldingItems() != null) {
					Iterator<HoldingItem> iter_si = a.getHoldingItems().iterator();
					while (iter_si.hasNext()) {
						HoldingItem si = iter_si.next();
						String symbol = si.getSymbol();
						Double per = sectors.get(symbol);

						long pid = -1;
						try {
							pid = Long.parseLong(symbol.substring(2));

						} catch (Exception e) {
							// e.printStackTrace();
						}
						if (pid != -1) {
							HoldingInf _phif = null;

							if (realtime) {
								_phif = pm.getPortfolioInf(pid, Configuration.PORTFOLIO_HOLDING_CURRENT).getHolding();
							} else {
								_phif = pm.getPortfolioInf(pid, Configuration.PORTFOLIO_HOLDING_DELAY).getHolding();
							}

							List<HoldingItem> hids = _phif.getHoldingItems();

							for (HoldingItem hi : hids) {
								totalAmount = totalAmount + hi.getPrice() * hi.getShare() * si.getShare();
								hi.setShare(hi.getShare() * si.getShare());
								holdingItems.add(hi);
							}
						} else {
							if (per == null)
								per = 0.0;
							if (si.getPercentage() > 0.0) {
								double np = si.getPercentage() + per;
								if (np > 1)
									np = 1;
								sectors.put(symbol, np);
								total += si.getPercentage();
							} else {
								shortSell = true;
							}
							// 计算cash
							if (hif.getCash() != 0.0) {
								Double perp = sectors.get("CASH");
								if (perp == null)
									perp = 0.0;
								double nper = hif.getCash() / hif.getAmount();
								if (nper + perp < 0.0001) {
									sectors.remove("CASH");
								} else {
									sectors.put("CASH", nper + perp);
								}

								total += nper;
							}
						}
					}
				}
			}
			if (holdingItems != null && holdingItems.size() > 0) {
				HoldingItem pre = new HoldingItem();
				// 计算每个security的percentage
				// double totalp = 0.0;
				for (HoldingItem hi : holdingItems) {
					hi.setPercentage(hi.getShare() * hi.getPrice() / totalAmount);
					// totalp = totalp +hi.getPercentage();
				}
				// System.out.println(totalp);
				// 合并同样symbol的percentage
				for (int i = 0; i < holdingItems.size() - 1; i++) {
					String symbol = holdingItems.get(i).getSymbol();
					for (int j = i + 1; j < holdingItems.size(); j++) {
						if (symbol.equals(holdingItems.get(j).getSymbol())) {
							double percentage = holdingItems.get(i).getPercentage();
							holdingItems.get(i).setPercentage(percentage + holdingItems.get(j).getPercentage());
							holdingItems.remove(j);
							j--;
						}
					}
				}
				// 对security根据percentage进行升序
				for (int i = 0; i < holdingItems.size() - 1; i++) {
					for (int j = 0; j < holdingItems.size() - 1; j++) {
						if (holdingItems.get(j).getPercentage() < holdingItems.get(j + 1).getPercentage()) {
							pre = holdingItems.get(j);
							holdingItems.remove(j);
							holdingItems.add(j, holdingItems.get(j));
							holdingItems.remove(j + 1);
							holdingItems.add(j + 1, pre);
						}
					}
				}
				double other = 0.0;
				for (int i = 0; i < holdingItems.size(); i++) {
					if (i > 5 && i < holdingItems.size() - 1) {
						other = other + holdingItems.get(i).getPercentage();
					}
					if (i == holdingItems.size() - 1) {
						other = other + holdingItems.get(holdingItems.size() - 1).getPercentage();
						sectors.put("Other", other);
					}
					if (i <= 5) {
						sectors.put(holdingItems.get(i).getSymbol(), holdingItems.get(i).getPercentage());
					}
				}
				if (hif.getCash() != 0.0) {
					Double perAggregate = sectors.get("CASH");
					if (perAggregate == null)
						perAggregate = 0.0;
					double nper = hif.getCash() / totalAmount;
					if (nper + perAggregate < 0.0001) {
						sectors.remove("CASH");
					} else {
						sectors.put("CASH", nper + perAggregate);
					}
				}
			}

		}
		if (shortSell) {
			Iterator<String> iter = sectors.keySet().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				double p = sectors.get(key) / total;
				if (p > 1)
					p = 1;
				sectors.put(key, p);
			}
		}

		return ChartUtil.PlotPie(hif.getPortfolioName(), sectors);
	}

	Simulator simulator;

	private SimulatorProcessor processor = new SimulatorProcessor() {

		@Override
		public void success(Simulator sim) {
			// TODO Auto-generated method stub

		}

		@Override
		public void error(Portfolio p, Throwable e) {
			System.out.println(StringUtil.getStackTraceString(e));
			ContextHolder.addException(e);
			if (listener != null) {
				listener.afterFail(p, e);
			}
			if (new Boolean(false)) {
				try {
					StringBuffer sb = new StringBuffer();
					sb.append("<a target='_blank' href='http://localhost/LTISystem/jsp/portfolio/ViewPortfolio.action?ID=" + p.getID() + "'>");
					sb.append(p.getName());
					sb.append("</a><br>\r\n");
					sb.append("<a target='_blank' href='http://localhost/LTISystem/jsp/portfolio/LogMain.action?portfolioID=" + p.getID() + "'>");
					sb.append("logs");
					sb.append("</a><br>\r\n<br>\r\n");

					sb.append("Strategy: <br>\r\n<pre>");
					sb.append(p.getStrategies().toInformation());
					sb.append("</pre><br>\r\n<br>\r\n");

					sb.append("Exception:<br>\r\n<pre>");
					sb.append(StringUtil.getStackTraceString(e));
					sb.append("</pre><br>\r\n<br>\r\n");
					sb.append("from " + InetAddress.getLocalHost().getHostName());

				} catch (Exception e1) {
					System.out.println(e1.getMessage());
				}
			}

		}
	};

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
	 * @param listener
	 *            监听器
	 * @throws Exception
	 */
	public void execute(Date expectedStopDate, List<SimulateStrategy> simulateStrategies, Portfolio simulatePortfolio, HoldingInf simulateHoldings, PortfolioState portfolioState, boolean forceMonitor, boolean isCustomized, Listener listener) throws Exception {
		this.listener = listener;
		simulator = new Simulator(expectedStopDate, simulateStrategies, simulatePortfolio, simulateHoldings, portfolioState, forceMonitor, isCustomized, listener);
		try {
			simulator.simulate();
			save();
		} catch (Exception e) {
			if (processor != null) {
				processor.error(simulator.getSimulatePortfolio(), e);
			}
			Log l = new Log();
			l.setLogDate(simulatePortfolio.getEndDate());
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			l.setMessage("[" + df.format(new Date()) + "]\r\n" + StringUtil.getStackTraceString(e));
			l.setPortfolioID(simulatePortfolio.getID());
			portfolioManager.addSystemLog(l);
			throw e;
		}
	}

	public void execute(Date expectedStopDate, List<SimulateStrategy> simulateStrategies, Portfolio simulatePortfolio, HoldingInf simulateHoldings, PortfolioState portfolioState, boolean forceMonitor, Listener listener) throws Exception {
		execute(expectedStopDate, simulateStrategies, simulatePortfolio, simulateHoldings, portfolioState, forceMonitor, false, listener);
	}

	/**
	 * transaction driven monitor
	 * 
	 * @param expectedStopDate
	 *            期待停止日期，为null，则默认为当前物理时间
	 * @param simulateStrategies
	 *            模拟的策略集合
	 * @param transactionList
	 *            已有交易列表
	 * @param simulatePortfolio
	 *            待模拟的Portfolio
	 * @param simulateHoldings
	 *            待模拟的Portfolio的Holding
	 * @param portfolioState
	 *            保存待模拟Portfolio的相关状态，例如persistent的bytes
	 * @param listener
	 */
	public void construct(Date expectedStopDate, List<SimulateStrategy> simulateStrategies, List<Transaction> transactionList, Portfolio simulatePortfolio, HoldingInf simulateHoldings, PortfolioState portfolioState, Listener listener) {
		this.listener = listener;
		simulator = new Simulator(expectedStopDate, simulateStrategies, transactionList, simulatePortfolio, simulateHoldings, portfolioState, listener);
		try {
			simulator.construct();
			save();
		} catch (Exception e) {
			if (processor != null) {
				processor.error(simulator.getSimulatePortfolio(), e);
			}
			return;
		}
	}

	private void plotCurveChart(List<PortfolioDailyData> pdds, PortfolioState ps) throws Exception {
		double[] values = new double[pdds.size()];
		Date[] dates = new Date[pdds.size()];

		// 计算欲显示的纵坐标
		Calendar ca = Calendar.getInstance();
		ca.setTime(pdds.get(0).getDate());
		Date delayDate = LTIDate.getHoldingDateMonthEnd(pdds.get(pdds.size() - 1).getDate());
		int year = ca.get(Calendar.YEAR);
		int len = 0;
		for (int i = 0; i < pdds.size(); i++) {
			if (i == 0) {
				dates[i] = pdds.get(i).getDate();
			} else if (i == pdds.size() - 1) {
				dates[i] = pdds.get(i).getDate();
			} else {
				ca.setTime(pdds.get(i).getDate());
				int nyear = ca.get(Calendar.YEAR);
				if (nyear != year) {
					year = nyear;
					dates[i - 1] = pdds.get(i - 1).getDate();
				}
			}
			if (!delayDate.before(pdds.get(i).getDate()))
				len = i + 1;
			values[i] = pdds.get(i).getAmount();
		}
		ps.setRealtimeChart(CurveChartUtil.drawChart(simulator.getSimulatePortfolio().getName(), dates, values, values.length));
		if (len != 0)
			dates[len - 1] = pdds.get(len - 1).getDate();
		ps.setDelayChart(CurveChartUtil.drawChart(simulator.getSimulatePortfolio().getName(), dates, values, len));

	}

	/**
	 * 模拟器构造器，更新操作
	 * 
	 * @param portfolioid
	 *            需要模拟的Portfolio的ID
	 * @param expectedStopDate
	 *            期待停止日期，为null，则默认为当前物理时间
	 */
	public void execute(long portfolioid, Date expectedStopDate) {
		execute(portfolioid, expectedStopDate, false, null);
	}

	/**
	 * 监听器
	 */
	private Listener listener;

	public Object execute(long portfolioid, Date expectedStopDate, boolean forceMonitor, Listener listner) {
		return execute(portfolioid, expectedStopDate, forceMonitor, listner, false);
	}

	/**
	 * 模拟器构造器
	 * 
	 * @param portfolioid
	 *            需要模拟的Portfolio的ID
	 * @param expectedStopDate
	 *            期待停止日期，为null，则默认为当前物理时间
	 * @param forceMonitor
	 *            是否强制执行monitor
	 */
	public Object execute(long portfolioid, Date expectedStopDate, boolean forceMonitor, Listener listner, boolean customized) {
		this.listener = listner;
		Portfolio p = null;
		try {
			p = portfolioManager.get(portfolioid);

			PortfolioInf pif = null;
			if (forceMonitor) {
				pif = portfolioManager.getPortfolioInf(portfolioid, Configuration.PORTFOLIO_HOLDING_ORIGINAL);
			} else {
				pif = portfolioManager.getPortfolioInf(portfolioid, Configuration.PORTFOLIO_HOLDING_CURRENT);
			}
			// 相当于构造Origianl portfolio
			// TODO: 过后应该删除
			if (pif == null) {
				pif = new PortfolioInf();
				pif.setPortfolioID(portfolioid);
			}
			if (pif.getHolding() == null) {
				HoldingInf hif = new HoldingInf(portfolioid, 10000.0, p.getStartingDate());
				hif.setBenchmarkID(14l);
				hif.setBenchmarkSymbol("^GSPC");
				pif.setHolding(hif);
			}

			final HoldingInf hif = pif.getHolding();
			final List<SimulateStrategy> simulateStrategies = new ArrayList<SimulateStrategy>();
			StrategyInf sif = p.getStrategies();
			sif.process(new IStrategyInfProcessor() {
				@Override
				public Object process(StrategyBasicInf bi) {
					if (bi.getID() != 0l) {
						try {

							com.lti.service.bo.Strategy strategy = strategyManager.get(bi.getID());
							Long strategyClassID = 0l;
							if (strategy.getStrategyClassID() != null)
								strategyClassID = strategy.getStrategyClassID();

							StrategyCode sc = strategyManager.getLatestStrategyCode(bi.getID());
							SimulateStrategy ss = Compiler.getStrategyInstance(sc.getCode(), strategyClassID);
							ss.setSimulateParameters(bi.getParameter());
							ss.setCurAsset(bi.getAssetName());
							ss.setSimulateAsset(hif.getAsset(bi.getAssetName()));
							ss.initDefaultParameters(sc.getCode().getParameter());
							simulateStrategies.add(ss);
							return ss;
						} catch (Throwable e) {
							throw new RuntimeException("Error while compiling strategies...", e);
						}
					}
					return null;
				}
			});
			PortfolioState ps = portfolioManager.getPortfolioState(portfolioid);
			execute(expectedStopDate, simulateStrategies, p, pif.getHolding(), ps, forceMonitor, customized, listner);
		} catch (Throwable e) {
			if (processor != null) {
				processor.error(p, e);
			}
			return e;
		}
		return p;
	}

	/**
	 * Transaction driven
	 * 
	 * @param portfolioID
	 *            需要模拟的Portfolio的ID
	 * @param expectedStopDate
	 *            期待停止日期，为null，则默认为当前物理时间
	 * @param listner
	 */
	public void construct(long portfolioID, Date expectedStopDate, Listener listner) {
		this.listener = listner;
		Portfolio p = null;
		try {
			p = portfolioManager.get(portfolioID);
			PortfolioInf pif = null;
			pif = portfolioManager.getPortfolioInf(portfolioID, Configuration.PORTFOLIO_HOLDING_ORIGINAL);
			if (pif == null) {
				pif = new PortfolioInf();
				pif.setPortfolioID(portfolioID);
			}
			if (pif.getHolding() == null) {
				HoldingInf hif = new HoldingInf(portfolioID, 10000.0, p.getStartingDate());
				pif.setHolding(hif);
			}
			List<SimulateStrategy> simulateStrategies = new ArrayList<SimulateStrategy>();
			PortfolioState ps = portfolioManager.getPortfolioState(portfolioID);
			List<Transaction> transactionList = portfolioManager.getAllTransactions(portfolioID);
			construct(expectedStopDate, simulateStrategies, transactionList, p, pif.getHolding(), ps, listner);
		} catch (Exception e) {
			if (processor != null) {
				processor.error(p, e);
			}
			return;
		}
	}

	public static void main(String[] args) {

		Map<String, Integer> map = new TreeMap<String, Integer>(new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				if (o1.toString().equals("other"))
					return -1;
				if (o2.toString().equals("other"))
					return 1;
				return o1.compareTo(o2);
			}
		});

		map.put("abc", 1);
		map.put("abc2", 1);
		map.put("other", 1);
		map.put("zabc", 1);

		Iterator<String> iter = map.keySet().iterator();
		while (iter.hasNext()) {
			System.out.println(iter.next());
		}
		if (true)
			return;

		List<Portfolio> portfolios = ContextHolder.getPortfolioManager().getSimplePortfolios(-1, -1);
		// for (Portfolio p : portfolios) {
		// Executor executor = new Executor();
		// executor.construct(p.getID(), null, null);
		// }
		Executor executor = new Executor();
		executor.construct(20378, null, null);
		// executor.execute(17342l, endDate,true,null);
		// Executor executor1=new Executor();
		// executor1.construct(3l, null,null);
		//
		// GroupManager gm=ContextHolder.getGroupManager();
		// List<GroupRole> grs=gm.getAllGroupRoles();
		// PortfolioManager pm=ContextHolder.getPortfolioManager();
		// long tt1=System.currentTimeMillis();
		// List<Portfolio> portfolios = pm.getPortfolios();
		// long tt2=System.currentTimeMillis();
		// System.out.println("load portfolio: "+(tt2-tt1)/1000);
		// Map<Long,Long> map=new HashMap<Long,Long>();
		// for(Portfolio p:portfolios){
		// p.setAttributes(new HashMap<String, String>());
		// p.getAttributes().put("ID", p.getID()+"");
		// map.put(p.getID(), p.getID());
		// }
		//
		//
		// StrategyManager sm=ContextHolder.getStrategyManager();
		// List<Strategy>
		// strs=sm.getStrategiesByType(Configuration.STRATEGY_TYPE_NORMAL);
		// for(Strategy s:strs){
		// sm.update(s);
		// System.out.println(s.getID());
		// }

		// for(Portfolio p:portfolios){
		// if(p.getID().equals(0l))continue;
		// long t1=System.currentTimeMillis();
		// try {
		// Executor executor=new Executor();
		// executor.construct(p.getID(), null,null);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// long t2=System.currentTimeMillis();
		// System.out.println(p.getID()+"."+p.getName()+" :"+(t2-t1)/1000.0);
		// }
		// System.out.println("update group.....");
		// for(GroupRole gr:grs){
		// if(gr.getResourceID()!=null){
		// try {
		// gm.deleteGroupRole(gr.getGroupID(), gr.getRoleID(),
		// gr.getResourceID(), gr.getResourceType());
		// gr.setID(null);
		// if(gr.getRoleID()==4l||gr.getRoleID()==8l){
		// if(map.get(gr.getResourceID())==null)continue;
		// }
		// gm.addGroupRole(gr);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }
		// }
	}
}
