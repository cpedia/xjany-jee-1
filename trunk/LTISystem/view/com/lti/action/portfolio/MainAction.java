package com.lti.action.portfolio;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.bean.PortfolioItem;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.GroupManager;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MainAction.class);

	// private PaginationSupport modelPortfolio;

	// private PaginationSupport privatePortfolio;

	private List<PortfolioItem> publicPortfolios;

	private List<PortfolioItem> myPortfolios;

	private PortfolioManager portfolioManager;

	private UserManager userManager;

	private GroupManager groupManager;

	private CustomizeRegionManager customizeRegionManager;

	private Map session;

	private CustomizeRegion customizeRegion;

	private String resultString;

	private Boolean isHolding;

	private String holdingDate;

	private Long userID;
	@Deprecated
	private Boolean realtime = false;

	private Boolean publicPortfolio = false;
	private Boolean myPortfolio = false;
	private Boolean premiumsPortfolio = false;
	private Boolean allPortfolio = false;

	private String menuItem;

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public void setCustomizeRegionManager(CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}

	@SuppressWarnings("unchecked")
	public String view() throws Exception {
		/*
		 * Long t1 = System.currentTimeMillis(); User user =
		 * userManager.getLoginUser(); Long userID; if (user == null) userID =
		 * 0l; else userID = user.getID(); Date today = new Date(); today =
		 * LTIDate.clearHMSM(today); Date legalDate =
		 * userManager.getLegalDate(userID, today); if(legalDate.equals(today))
		 * isHolding = false; else { isHolding = true; SimpleDateFormat sdf =
		 * new SimpleDateFormat("yyyy/MM/dd"); holdingDate =
		 * sdf.format(legalDate); } publicPortfolios=new ArrayList<PortfolioItem>();
		 * myPortfolios=new ArrayList<PortfolioItem>(); List<Portfolio>
		 * pubPorts = portfolioManager.getPublicPortfolios(userID);
		 * publicPortfolios = PortfolioUtil.setPortfolioItems(pubPorts,
		 * legalDate); List<Portfolio> priPorts =
		 * portfolioManager.getPrivatePortfolios(userID); myPortfolios =
		 * PortfolioUtil.setPortfolioItems(priPorts, legalDate); customizeRegion =
		 * customizeRegionManager.get(CustomizeUtil.PORTFOLIO_MAIN);
		 * CustomizeUtil.setRegion(customizeRegion, userID);
		 * 
		 * ActionContext ac = ActionContext.getContext(); session =
		 * ac.getSession(); session.put("publicPortfolios", publicPortfolios);
		 * session.put("myPortfolios", myPortfolios);
		 * 
		 * Long t2 = System.currentTimeMillis(); System.out.println("Times t:" +
		 * (t2 - t1));
		 */
		// return Action.SUCCESS;
		User user = userManager.getLoginUser();
		if (user == null) {
			userID = Configuration.USER_ANONYMOUS;
		} else
			userID = user.getID();
		if (!userID.equals(Configuration.USER_ANONYMOUS)) {
			realtime = true;
		}
		if (publicPortfolio)
			menuItem = Configuration.PORTFOLIO_TYPE_PUBLIC;
		else if (myPortfolio)
			menuItem = Configuration.PORTFOLIO_TYPE_MY;
		else if (premiumsPortfolio)
			menuItem = Configuration.PORTFOLIO_TYPE_PREMIUMS;
		else if (allPortfolio)
			menuItem = Configuration.PORTFOLIO_TYPE_ALL;
		else
			menuItem = "portfoliodesc";
		return Action.SUCCESS;

	}

	@SuppressWarnings("unchecked")
	public String modelPortfolioOutput() {
		session = (Map) ActionContext.getContext().get(ActionContext.SESSION);
		publicPortfolios = (List<PortfolioItem>) session.get("publicPortfolios");
		// privatePortfolios = (List<PortfolioItem>)
		// session.get("privatePortfolios");
		//resultString = PortfolioUtil.generateXML(publicPortfolios, false);
		// System.out.println(resultString);
		return Action.SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String privatePortfolioOutput() {
		session = (Map) ActionContext.getContext().get(ActionContext.SESSION);
		myPortfolios = (List<PortfolioItem>) session.get("myPortfolios");
		//resultString = PortfolioUtil.generateXML(myPortfolios, true);
		// System.out.println(resultString);
		return Action.SUCCESS;
	}

	public Map getSession() {
		return session;
	}

	public void setSession(Map session) {
		this.session = session;
	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public List<PortfolioItem> getPublicPortfolios() {
		return publicPortfolios;
	}

	public void setPublicPortfolios(List<PortfolioItem> publicPortfolios) {
		this.publicPortfolios = publicPortfolios;
	}

	public List<PortfolioItem> getMyPortfolios() {
		return myPortfolios;
	}

	public void setMyPortfolios(List<PortfolioItem> myPortfolios) {
		this.myPortfolios = myPortfolios;
	}

	public Boolean getIsHolding() {
		return isHolding;
	}

	public void setIsHolding(Boolean isHolding) {
		this.isHolding = isHolding;
	}

	public String getHoldingDate() {
		return holdingDate;
	}

	public void setHoldingDate(String holdingDate) {
		this.holdingDate = holdingDate;
	}

	public Boolean getPublicPortfolio() {
		return publicPortfolio;
	}

	public void setPublicPortfolio(Boolean publicPortfolio) {
		this.publicPortfolio = publicPortfolio;
	}

	public Boolean getMyPortfolio() {
		return myPortfolio;
	}

	public void setMyPortfolio(Boolean myPortfolio) {
		this.myPortfolio = myPortfolio;
	}

	public Boolean getPremiumsPortfolio() {
		return premiumsPortfolio;
	}

	public void setPremiumsPortfolio(Boolean premiumsPortfolio) {
		this.premiumsPortfolio = premiumsPortfolio;
	}

	public String getMenuItem() {
		return menuItem;
	}

	public void setMenuItem(String menuItem) {
		this.menuItem = menuItem;
	}

	public Boolean getAllPortfolio() {
		return allPortfolio;
	}

	public void setAllPortfolio(Boolean allPortfolio) {
		this.allPortfolio = allPortfolio;
	}

	public Boolean getRealtime() {
		return realtime;
	}

	public void setRealtime(Boolean realtime) {
		this.realtime = realtime;
	}

}
