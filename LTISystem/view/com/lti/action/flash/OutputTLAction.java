package com.lti.action.flash;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.SecurityManager;
import com.lti.service.StrategyManager;
import com.lti.service.bo.PortfolioDailyData;
import com.lti.service.bo.SecurityDailyData;
import com.lti.service.bo.Transaction;
import com.opensymphony.xwork2.ActionSupport;
import com.lti.util.FormatUtil;
import com.lti.util.LTIDate;
import com.lti.system.Configuration;

public class OutputTLAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(OutputXMLAction.class);

	private String resultString;

	private String portfolioID;

	private String DataKind;

	private PortfolioManager portfolioManager;
	private SecurityManager securityManager;
	private StrategyManager strategyManager;

	public String getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(String portfolioID) {
		this.portfolioID = portfolioID;
	}

	public String getDataKind() {
		return DataKind;
	}

	public void setDataKind(String dataKind) {
		DataKind = dataKind;
	}

	@Override
	public String execute() throws Exception {

		StringBuffer sb = new StringBuffer();

		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><TL>");
		if (portfolioID != null & !portfolioID.equals("")) {
			long id = Long.parseLong(portfolioID.replace(",", ""));

			if (Integer.parseInt(DataKind) == 99) {
				List<com.lti.service.bo.Transaction> transactionResultList = portfolioManager.getTransactions(id);
				Long securityid;
				// Long protfolioid;

				for (int i = 0; i < transactionResultList.size(); i++) {
					Transaction t = transactionResultList.get(i);
					if (transactionResultList.get(i).getOperation() != null) {
						sb.append("<e ");

						// add the date.month.year
						sb.append("d=\"");
						sb.append(transactionResultList.get(i).getDate().toString().substring(0, 10));
						sb.append("\"");

						// add the portfolioAmount in order to position the
						// transaction in flash
						sb.append("t=\""); // totalAmount
						Double per = t.getPercentage();
						try {
							double total = t.getAmount() / per;
							sb.append(FormatUtil.formatQuantity(total, 2));
						} catch (Exception e) {
							//sb.append(FormatUtil.formatQuantity(portfolioManager.getDailydata(id, t.getDate()).getAmount(), 2));
							sb.append("NA");
						}

						sb.append("\"");

						sb.append("o=\"");
						sb.append(transactionResultList.get(i).getOperation());
						sb.append("\"");

						// add asset name
						sb.append("ass=\"");
						sb.append(transactionResultList.get(i).getAssetName());
						sb.append("\"");

						if (transactionResultList.get(i).getOperation().equals(Configuration.TRANSACTION_BUY)) {
							sb.append("s=\"");
							sb.append(t.getSymbol());
							sb.append("\"");
						} else {
							sb.append("s=\"");
							if (transactionResultList.get(i).getSecurityID() != null) {
								sb.append(t.getSymbol());
							} else {
								sb.append("ALL");
							}
							sb.append("\"");
						}

						// add operation money
						sb.append("am=\"");

						if (per == null) {
							Double amount = t.getAmount();
							sb.append(FormatUtil.formatQuantity(amount, 2));
						} else {
							sb.append(FormatUtil.formatPercentage(per));
							sb.append("%");
						}
						sb.append("\"");
						sb.append("/>"); // end of each line

					}
				}
			} // end if DataKind ==99
			if (Integer.parseInt(DataKind) == 98) {// 98 for log message
				List<com.lti.service.bo.Log> logList = portfolioManager.getLogs(id);
				List<PortfolioDailyData> pdds = portfolioManager.getDailydatas(id);

				for (int i = 0; i < logList.size(); i++) {
					if (logList.get(i).getMessage() != null && logList.get(i).getLogDate() != null) {
						sb.append("<e ");

						// add the date.month.year
						sb.append("d=\"");
						// com.lti.service.bo.Log log=logList.get(i);
						// Date logDate=log.getLogDate();
						// if(logDate!=null){

						sb.append(logList.get(i).getLogDate().toString().substring(0, 10));

						sb.append("\"");
						// }
						// System.out.print(
						// logList.get(i).getLogDate().toString().substring(0,
						// 10) );

						// add the portfolioAmount in order to position the
						// transaction in flash
						sb.append("t=\""); // totalAmount
						// System.out.print(portfolioManager.getDailydata(id,
						// logList.get(i).getLogDate()) );
						// log=logList.get(i);
						// logDate=log.getLogDate();
						// if(logDate!=null){
						sb.append(FormatUtil.formatQuantity(getAmount(pdds, logList.get(i).getLogDate()), 2)); // save
						// 3
						// bit
						// after
						// dot
						// point
						sb.append("\"");
						// }

						sb.append("l=\"");

						// if the log type is 1,add the strategy name
						if (logList.get(i).getType().equals(Configuration.LOG_TYPE_USER)) {
							/*
							 * if(logList.get(i).getStrategyID()!=null){
							 * if(strategyManager.get(logList.get(i).getStrategyID()).getName()!=null){
							 * sb.append("\n(User log)\nStrategy
							 * name:"+strategyManager.get(logList.get(i).getStrategyID()).getName()+"\n"; } }
							 * else { sb.append("\n(User log)\nStrategy
							 * name:unknown"; }
							 */
							sb.append("\n(User log)\n");
						} else if (logList.get(i).getType().equals(Configuration.LOG_TYPE_SYSTEM)) {
							sb.append("\n(System log)\n");
						}
						sb.append(logList.get(i).getMessage());
						sb.append("\"");

						sb.append("/>"); // end of each line
					}

				}

			} // end when DataKind == 98
		}

		sb.append("</TL>");// end of the XML file
		resultString = sb.toString();
		return Action.SUCCESS;

	}// end excute

	public double getAmount(List<PortfolioDailyData> pdds, Date date) {

		double result = 0.0;

		if (pdds == null || pdds.size() == 0)
			return result;

		if (LTIDate.before(date, pdds.get(0).getDate()))
			return result;

		int start = 0;

		int end = pdds.size() - 1;

		while (end >= start) {

			int m = (start + end) / 2;

			PortfolioDailyData pdd = pdds.get(m);

			if (LTIDate.equals(date, pdd.getDate())) {

				return pdd.getAmount();

			} else {

				if (pdd.getDate().before(date)) {

					start = m + 1;

				} else {

					end = m - 1;

				}
			}

		}// end while

		return result;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public void setStrategyManager(StrategyManager strategyManager) {
		this.strategyManager = strategyManager;
	}

}
