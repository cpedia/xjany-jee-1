package com.lti.action.BLApp;

import java.util.ArrayList;
import java.util.List;

import com.lti.action.Action;
import com.lti.bean.BLAppBean;
import com.lti.bean.BLCovarienceBean;
import com.lti.bean.BLRowBean;
import com.lti.bean.BLViewBean;
import com.lti.bean.BLWeightsBean;
import com.lti.service.CustomizeRegionManager;
import com.lti.service.UserManager;
import com.lti.service.bo.CustomizeRegion;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.type.BLItem;
import com.lti.type.TimeUnit;
import com.lti.util.CustomizeUtil;
import com.lti.util.FormatUtil;
import com.lti.util.LTIBLInterface;
import com.lti.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;

public class MainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;
	
	private BLAppBean BLForm;
	
	private BLViewBean View;
	
	private List<BLViewBean> ViewList;
	
	private Double Weight;
	
	private UserManager userManager;
	
	private CustomizeRegionManager customizeRegionManager;
	
	private CustomizeRegion customizeRegion;
	
	private String action;
	
	private List<BLWeightsBean> Result;
	
	private BLCovarienceBean covarience;
	

	public BLAppBean getBLForm() {
		return BLForm;
	}

	public void setBLForm(BLAppBean form) {
		BLForm = form;
	}

	public BLViewBean getView() {
		return View;
	}

	public void setView(BLViewBean view) {
		View = view;
	}

	public Double getWeight() {
		return Weight;
	}

	public void setWeight(Double weight) {
		Weight = weight;
	}
	
	@Override
	public void validate() {
		// TODO Auto-generated method stub
		User user = userManager.getLoginUser();
		Long userID;
		if(user == null){
			userID = 0l;
		}
		else
			userID = user.getID();
		
		boolean raa = userManager.HasRole(Configuration.ROLE_RAA, userID);
		if(raa == false){
			addActionError("You can't execute the MVO tool, because you have no right!");
			return;
		}
	}

	public String CreateBasic(){
		Long userID = userManager.getLoginUser().getID();
		customizeRegion = customizeRegionManager.get(CustomizeUtil.BLAPP_INFO);
		CustomizeUtil.setRegion(customizeRegion, userID);
		BLForm = new BLAppBean();
		BLForm.setBackward(24);
		BLForm.setRiskAversion(3.000);
		BLForm.setRiskFreeSymbol("^IRX");
		BLForm.setForward(12);
		return Action.SUCCESS;
	}
	
	public String BLBasics(){
		Long userID = userManager.getLoginUser().getID();
		customizeRegion = customizeRegionManager.get(CustomizeUtil.BLAPP_INFO);
		CustomizeUtil.setRegion(customizeRegion, userID);
		if(BLForm.getRiskAversion() == null){
			addActionError("Risk Aversion is required!");
			action = "error";
			return Action.INPUT;
		}
		if(BLForm.getRiskFreeSymbol() == null || BLForm.getRiskFreeSymbol().equals("")){
			addActionError("Risk Free Symbol is required!");
			action = "error";
			return Action.INPUT;
		}
		if(BLForm.getForward() == null){
			addActionError("Forward Horizon is required!");
			action = "error";
			return Action.INPUT;
		}
		if(BLForm.getBackward() == null){
			addActionError("Backward Horizon field is required!");
			action = "error";
			return Action.INPUT;
		}
		if(BLForm.getInvestList() == null){
			addActionError("InvestList can't be empty!");
			action = "error";
			return Action.INPUT;
		}
		List<BLItem> investList = BLForm.getInvestList();
		Double sum = 0.0;
		for(int i = 0; i < investList.size(); i++){
			BLItem item = investList.get(i);
			if(item.getSymbol() == null || item.getSymbol().equals("") || (item.getWeightStr() == null && item.getWeight() != null)){
				investList.remove(i);
				i--;
				continue;
			}
			Double weight;
			if(item.getWeight() == null && item.getWeightStr() != null && !item.getWeightStr().equals("")){
				String weightStr = item.getWeightStr();
				weight = StringUtil.percentageToDouble(weightStr);
				if(weight != null){
					item.setWeight(weight);
				}
				else{
					investList.remove(i);
					i--;
					continue;
				}
			}
			
			sum += item.getWeight();
		}
		if(BLForm.getInvestList().size() == 0){
			addActionError("InvestList can't be empty!");
			action = "error";
			return Action.INPUT;
		}
		if(sum > 1.0){
			addActionError("The sum of the weights of the securities must be less than 1.0");
			action = "error";
			return Action.INPUT;
		}
		action = null;
		BLForm.setInvestList(investList);
		ViewList = new ArrayList<BLViewBean>();
		View = new BLViewBean();
		View.setName("New View");
		if(BLForm != null || ViewList == null)
			translateBasicToView(BLForm, View);
		else
			return Action.ERROR;
		return Action.SUCCESS;
	}

	public String BLView(){
		Long userID = userManager.getLoginUser().getID();
		customizeRegion = customizeRegionManager.get(CustomizeUtil.BLAPP_INFO);
		CustomizeUtil.setRegion(customizeRegion, userID);
		if(View != null){
			if(isViewValid(View) == true){
				if(ViewList == null)
					ViewList = new ArrayList<BLViewBean>();
				View.setViewID(ViewList.size() + 1);
				ViewList.add(View);
			}
		}
		action = null;
		View = new BLViewBean();
		View.setName("New View");
		if(BLForm != null || ViewList == null)
			translateBasicToView(BLForm, View);
		else
			return Action.ERROR;
		return Action.SUCCESS;
	}
	
	public String Calculation(){
		Long userID = userManager.getLoginUser().getID();
		customizeRegion = customizeRegionManager.get(CustomizeUtil.BLAPP_RESULT);
		CustomizeUtil.setRegion(customizeRegion, userID);
		List<BLWeightsBean> result = new ArrayList<BLWeightsBean>();
		if(ViewList == null || ViewList.size() == 0){
			List<BLItem> investList = BLForm.getInvestList();
			for(int i = 0; i < investList.size(); i++){
				BLItem blitem = investList.get(i);
				BLWeightsBean item = new BLWeightsBean();
				item.setSymbol(blitem.getSymbol());
				item.setNormalizedWeight(blitem.getWeight());
				item.setOrdinaryWeight(blitem.getWeight());
				result.add(item);
			}
			Result = result;
			return Action.SUCCESS;
		}
		
		List<String> Symbols = getSecuritySymbol(BLForm);
		List<Double> MarketWeights = getMarketWeight(BLForm);
		List<Double> ViewTargets = getViewTarget(ViewList, BLForm.getForward());
		List<Double> ViewOnWeights = getViewOnWeight(ViewList);
		List<List<Double>> ViewWeights = getWeights(ViewList);
		LTIBLInterface blInterface = new LTIBLInterface();
		List<Double> ordinaryWeights = blInterface.getBLWeight(Symbols, MarketWeights, ViewWeights, ViewTargets, ViewOnWeights,BLForm.getBackward()*(-30), TimeUnit.DAILY);
		List<Double> normalizedWeights = blInterface.normalize(ordinaryWeights);
		List<List<Double>> covariences = blInterface.getSigma();
		
		if(covariences != null && covariences.size() != 0){
			covarience = new BLCovarienceBean();
			for(int i = 0; i < covariences.size(); i++){
				List<Double> row = covariences.get(i);
				BLRowBean co_row = new BLRowBean();
				for(int j = 0; j < row.size(); j++){
					BLItem bl = new BLItem();
					bl.setWeight(row.get(j));
					co_row.getRow().add(bl);
				}
				covarience.getCovarience().add(co_row);
			}
		}
		
		for(int i = 0; i < Symbols.size(); i++){
			BLWeightsBean item = new BLWeightsBean();
			item.setSymbol(Symbols.get(i));
			item.setOrdinaryWeight(ordinaryWeights.get(i));
			item.setNormalizedWeight(normalizedWeights.get(i));
			result.add(item);
		}
		Result = result;
		return Action.SUCCESS;
	}

	public static void translateBasicToView(BLAppBean ab, BLViewBean v){
		if(ab.getInvestList() == null)
			return;
		if(v.getView() == null)
			v.setView(new ArrayList<BLItem>());
		List<BLItem> items = new ArrayList<BLItem>();
		List<BLItem> invested = ab.getInvestList();
		for(int i = 0; i < invested.size(); i++){
			BLItem item = new BLItem();
			item.setSymbol(invested.get(i).getSymbol());
			item.setWeight(0.0);
			items.add(item);
		}
		v.setView(items);
	}
	
	public static List<String> getSecuritySymbol(BLAppBean ab){
		List<BLItem> basic = ab.getInvestList();
		if(basic == null)
			return null;
		List<String> symbols = new ArrayList<String>();
		for(int i = 0; i < basic.size(); i++){
			BLItem b = basic.get(i);
			symbols.add(b.getSymbol());
		}
		return symbols;
	}
	
	public static List<Double> getMarketWeight(BLAppBean ab){
		List<BLItem> basic = ab.getInvestList();
		if(basic == null)
			return null;
		List<Double> marketWeights = new ArrayList<Double>();
		for(int i = 0; i < basic.size(); i++){
			BLItem b = basic.get(i);
			marketWeights.add(b.getWeight());
		}
		return marketWeights;
	}
	
	public static List<Double> getViewTarget(List<BLViewBean> viewList, Integer Forward ){
		if(viewList == null)
			return null;
		List<Double> targets = new ArrayList<Double>();
		for(int i = 0; i < viewList.size(); i++){
			targets.add(viewList.get(i).getTargetValue()/(Forward * 30));
		}
		return targets;
	}
	
	public static List<Double> getViewOnWeight(List<BLViewBean> viewList){
		if(viewList == null)
			return null;
		List<Double> views = new ArrayList<Double>();
		for(int i = 0; i < viewList.size(); i++){
			views.add(viewList.get(i).getMyViewOnWeight());
		}
		return views;
	}
	
	public static List<List<Double>> getWeights(List<BLViewBean> viewList){
		if(viewList == null)
			return null;
		List<List<Double>> weights = new ArrayList<List<Double>>();
		for(int i = 0; i < viewList.size(); i++){
			List<BLItem> view = viewList.get(i).getView();
			List<Double> weight = new ArrayList<Double>();
			for(int j = 0; j < view.size(); j++){
				weight.add(view.get(j).getWeight());
			}
			weights.add(weight);
		}
		return weights;
	}
		
	private Boolean isViewValid(BLViewBean view){
		Boolean flag = false;
		if(view.getTargetValue() == null || view.getMyViewOnWeight() == null){
			return false;
		}
		List<BLItem> weights = view.getView();
		for(int i = 0; i < weights.size(); i++){
			if(weights.get(i).getWeight() != 0){
				flag = true;
				break;
			}
		}
		return flag;
	}
	
	public static void main(String[] args){
		List<BLItem> investList = new ArrayList<BLItem>();
		BLItem item = new BLItem();
		item.setSymbol("AGG");
		item.setWeight(0.2);
		investList.add(item);
		item = new BLItem();
		item.setSymbol("BEGBX");
		item.setWeight(0.2);
		investList.add(item);
		item.setSymbol("IYR");
		item.setWeight(0.2);
		investList.add(item);
		item.setSymbol("LMVTX");
		item.setWeight(0.2);
		investList.add(item);
		item.setSymbol("MPGFX");
		item.setWeight(0.2);
		investList.add(item);
		BLAppBean blapp = new BLAppBean();
		blapp.setInvestList(investList);
		BLViewBean view = new BLViewBean();
		translateBasicToView(blapp, view);
		view.setMyViewOnWeight(0.5);
		view.setTargetValue(0.1);
		List<BLViewBean> viewList = new ArrayList<BLViewBean>();
		viewList.add(view);
		view = new BLViewBean();
		translateBasicToView(blapp, view);
		List<BLItem> viewData2 = view.getView();
		viewData2.get(0).setWeight(1.0);
		viewData2.get(1).setWeight(0.0);
		viewData2.get(2).setWeight(0.0);
		viewData2.get(3).setWeight(0.0);
		viewData2.get(4).setWeight(0.0);
		view.setMyViewOnWeight(0.5);
		view.setTargetValue(0.1);
		viewList.add(view);
		List<String> SecuritySymbols = getSecuritySymbol(blapp);
		List<Double> marketWeight = getMarketWeight(blapp);
		List<Double> viewTarget = getViewTarget(viewList, blapp.getForward());
		List<Double> myViewWeight = getViewOnWeight(viewList);
		List<List<Double>> ViewWeight = getWeights(viewList);
		List<Double> weights = new ArrayList<Double>();
		LTIBLInterface blInterface = new LTIBLInterface();
		weights = blInterface.getBLWeight(SecuritySymbols, marketWeight, ViewWeight, viewTarget, myViewWeight, -100, TimeUnit.DAILY);
		for(int i = 0; i < weights.size(); i++){
			System.out.println(weights.get(i));
		}
	}

	public List<BLViewBean> getViewList() {
		return ViewList;
	}

	public void setViewList(List<BLViewBean> viewList) {
		ViewList = viewList;
	}

	public CustomizeRegion getCustomizeRegion() {
		return customizeRegion;
	}

	public void setCustomizeRegion(CustomizeRegion customizeRegion) {
		this.customizeRegion = customizeRegion;
	}

	public void setCustomizeRegionManager(
			CustomizeRegionManager customizeRegionManager) {
		this.customizeRegionManager = customizeRegionManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public List<BLWeightsBean> getResult() {
		return Result;
	}

	public void setResult(List<BLWeightsBean> result) {
		Result = result;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public BLCovarienceBean getCovarience() {
		return covarience;
	}

	public void setCovarience(BLCovarienceBean covarience) {
		this.covarience = covarience;
	}

}
