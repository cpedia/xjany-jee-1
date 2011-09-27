package com.lti.action.admin.user;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Group;
import com.lti.service.bo.UserPayItem;
import com.lti.service.bo.UserProfile;
import com.lti.service.bo.UserTransaction;
import com.lti.system.ContextHolder;
import com.lti.util.LTIDate;
import com.opensymphony.xwork2.ActionSupport;

public class UserProfileAction extends ActionSupport implements Action{

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(UserProfileAction.class);
	
	private UserManager userManager;
	private GroupManager groupManager;
	private List<UserProfile> userprofile;
	private List<UserTransaction> userTransaction;
	private List<UserProfile> validtimes;
	private List<UserProfile> expireds;
	private List<Group> group;
	private List<String> itemsname;
	private Map<String,List<String>> payitems;
	private Map<String,List<UserProfile>> statuslists;
	List<Map.Entry<String,Integer>> vCountPairs;
	private Long userID;	
	private String itemName;
	private String oldItemName;
	private Long[] selectedArray;
	private String action;
	
	
	public List<UserTransaction> getUserTransaction() {
		return userTransaction;
	}

	public void setUserTransaction(List<UserTransaction> userTransaction) {
		this.userTransaction = userTransaction;
	}

	public Long getUserID() {
		return userID;
	}

	public void setUserID(Long userID) {
		this.userID = userID;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public List<UserProfile> getUserprofile() {
		return userprofile;
	}

	public void setUserprofile(List<UserProfile> userprofile) {
		this.userprofile = userprofile;
	}
	
	public List<Group> getGroup() {
		return group;
	}

	public void setGroup(List<Group> group) {
		this.group = group;
	}
	
	public Map<String, List<String>> getPayitems() {
		return payitems;
	}

	public void setPayitems(Map<String, List<String>> payitems) {
		this.payitems = payitems;
	}
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Long[] getSelectedArray() {
		return selectedArray;
	}

	public void setSelectedArray(Long[] selectedArray) {
		this.selectedArray = selectedArray;
	}
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public GroupManager getGroupManager() {
		return groupManager;
	}

	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}
	
	public List<UserProfile> getValidtimes() {
		return validtimes;
	}

	public void setValidtimes(List<UserProfile> validtimes) {
		this.validtimes = validtimes;
	}
	
	public List<Map.Entry<String, Integer>> getVCountPairs() {
		return vCountPairs;
	}

	public void setVCountPairs(List<Map.Entry<String, Integer>> countPairs) {
		vCountPairs = countPairs;
	}
	
	public List<UserProfile> getExpireds() {
		return expireds;
	}

	public void setExpireds(List<UserProfile> expireds) {
		this.expireds = expireds;
	}

	public Map<String, List<UserProfile>> getStatuslists() {
		return statuslists;
	}

	public void setStatuslists(Map<String, List<UserProfile>> statuslists) {
		this.statuslists = statuslists;
	}

	@Override
	public String execute() throws Exception{
		userManager = ContextHolder.getUserManager();
		groupManager = ContextHolder.getGroupManager();
		if(action!=null && action.equals("payitem")){
			payitems = userManager.getAllItem();
			group = groupManager.getGroups();
			return Action.PAYITEM;
		}
		if(action!=null && action.equals("cpayitem")){
			UserPayItem item = new UserPayItem();
			item.setItemName(itemName);
			List groupIDs = Arrays.asList(selectedArray);
			userManager.savePayItem(itemName, groupIDs);
			payitems = userManager.getAllItem();
			group = groupManager.getGroups();
			return Action.PAYITEM;
		}
		if(action!=null && action.equals("delete")){
			userManager.deletePayItem(itemName);
			List<UserProfile> uds = userManager.getAllUserProfile();
			List<UserProfile> udt = new ArrayList<UserProfile>();
			for(int i=0;i<uds.size();i++){
				if(uds.get(i).getItemName()!=null && uds.get(i).getItemName().equals(itemName)){
					udt.add(uds.get(i));
				}	
			}
			for(int j=0;j<udt.size();j++){
				Long uID = udt.get(j).getUserID();
				groupManager.removeByUserID(uID);
			}
			payitems = userManager.getAllItem();
			group = groupManager.getGroups();
			return Action.PAYITEM;
		}
		if(action!=null && action.equals("modify")){
			if(itemName.equals(oldItemName))
				userManager.deletePayItem(itemName);
			else
				userManager.deletePayItem(oldItemName);
			saveItem();
			List<UserProfile> us = userManager.getAllUserProfile();
			List<UserProfile> ut = new ArrayList<UserProfile>();
			//List<Long> gIDs = Arrays.asList(selectedArray);
			for(int i=0;i<us.size();i++){
				if(itemName.equals(oldItemName)){
					if(us.get(i).getItemName()!=null && us.get(i).getItemName().equals(itemName)){
						ut.add(us.get(i));
					}	
				}
				else{
					if(us.get(i).getItemName()!=null && us.get(i).getItemName().equals(oldItemName)){
						ut.add(us.get(i));
					}		
				}		
			}
			for(int j=0;j<ut.size();j++){
				Long uID = ut.get(j).getUserID();
				groupManager.removeByUserID(uID);
				userManager.changUserItemName(uID, itemName);
			}
			payitems = userManager.getAllItem();
			group = groupManager.getGroups();
			return Action.PAYITEM;
		}
		if(action!=null && action.equals("validtime")){
			Date today = new Date();
			LTIDate ltidate = new LTIDate();
			validtimes = userManager.getUserProfileInTime(today);
			List<Integer> vcs = new ArrayList<Integer>();
			for(int i=0;i<validtimes.size();i++){
				Date start = validtimes.get(i).getPaymentStartDate();
				Date end = validtimes.get(i).getPaymentEnddate();
				int date = ltidate.calculateInterval(start, end);
				int vdate = ltidate.calculateInterval(end, today);
				validtimes.get(i).setValidtime(vdate);
				validtimes.get(i).setTimeperiod(date);
				vcs.add(vdate);
			}
			vCountPairs= this.validtimeCount(vcs);
			return Action.VALIDTIME;
		}
		if(action!=null && action.equals("expired")){
			Date today = new Date();
			LTIDate ltidate = new LTIDate();
			expireds = userManager.getUserProfileOutTime(today);
			for(int i=0;i<expireds.size();i++){
				Date end = expireds.get(i).getPaymentEnddate();
				int timeout = ltidate.calculateInterval(end, today);
				expireds.get(i).setTimeout(timeout);
			}
			return Action.EXPIRED;
		}
		if(action!=null && action.equals("transaction")){
			userTransaction = userManager.getUserTransaction(userID);
			return Action.STATUS;
		}
		
		if(action!=null && action.equals("clearTransactions")){
			userManager.deleteUserTransaction(userID);
			userManager.deleteUserProfile(userID);
			message="ok";
			return Action.MESSAGE;
		}
		
		
		if(action!=null && action.equals("chaItem")){
			
			UserProfile up=userManager.getUserProfile(userID);
			if(up==null){
				up=new UserProfile();
				up.setUserID(userID);
				
			}else{
				userManager.removeGroupByItem(userID);
				userManager.changUserItemName(userID, itemName);
				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
				up.setItemName(itemName);
				up.setUserStatus(status);
				try {
					up.setPaymentStartDate(df.parse(startDate));
					up.setPaymentEnddate(df.parse(endDate));
				} catch (Exception e) {
				}
			}
			userManager.saveOrUpdateUserProfile(up);
			//userprofile = userManager.getAllUserProfile();	
			//payitems = userManager.getAllItem();
			//itemsname = userManager.getAllItemName();
			message="ok";
			return Action.MESSAGE;
		}
		else{
			userprofile = userManager.getAllUserProfile();	
			payitems = userManager.getAllItem();
			itemsname = userManager.getAllItemName();
			return Action.SUCCESS;
		}
	}
	
	
	private String status;
	private String startDate;
	private String endDate;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	private String message;
	
	
	public List<Map.Entry<String, Integer>> validtimeCount(List<Integer> vcs){
		Collections.sort(vcs);
		Set<Integer> vCounts = new HashSet<Integer>();
		vCounts.addAll(vcs);
		Map<String,Integer> Pairs = new HashMap<String,Integer>();	
		int count = 0;
		int m = 0;
		Iterator ivs = vCounts.iterator();
		while(ivs.hasNext()){			
			Integer iv = (Integer)ivs.next();
			for(int i=m;i<vcs.size();i++){
				if(iv.intValue()==vcs.get(i).intValue())
					count++;
				else{
					m=i;break;
				}
			}
			Pairs.put(iv.toString(), count);	
			count=0;
		}
		List<Map.Entry<String,Integer>> vCPairs = new ArrayList<Map.Entry<String, Integer>>(Pairs.entrySet()); 

		Collections.sort(vCPairs, new Comparator<Map.Entry<String, Integer>>(){ 
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2){ 
				return (Integer.parseInt(o1.getKey()) - Integer.parseInt(o2.getKey())); 
			} 
		}); 
		return vCPairs;
	}
	
	public void saveItem(){
		UserPayItem item = new UserPayItem();
		item.setItemName(itemName);
		List groupIDs = Arrays.asList(selectedArray);
		userManager.savePayItem(itemName, groupIDs);
		payitems = userManager.getAllItem();
	}
	
	public String viewUserTransaction(){
		userManager = ContextHolder.getUserManager();
		userTransaction = userManager.getUserTransaction(userID);
		return Action.SUCCESS;
	}
	
	public Map<String,List<UserProfile>> viewUserStatus(){
		Map<String,List<UserProfile>> userstatus = new HashMap<String,List<UserProfile>>();
		String[] status = new String[5];
		status[0] = UserProfile.PAYMENT_STATUS_EXPIRED;
		status[1] = UserProfile.PAYMENT_STATUS_NORMAL;
		status[2] = UserProfile.PAYMENT_STATUS_INACTIVE;
		status[3] = UserProfile.PAYMENT_STATUS_LOCKED;
		status[4] = UserProfile.PAYMENT_STATUS_WAITING;
		for(int i=0;i<status.length;i++){
			userstatus.put(status[i],userManager.getUserProfileByStatus(status[i]));
		}
		return userstatus;
	}
	
	public static void main(String agrs[]){
		UserManager userManager = ContextHolder.getUserManager();
		Date today = new Date();
		LTIDate ltidate = new LTIDate();
		List<UserProfile> validtimes = userManager.getAllUserProfile();
		List<Integer> vcs = new ArrayList<Integer>();
		for(int i=0;i<validtimes.size();i++){
			Date start = validtimes.get(i).getPaymentStartDate();
			Date end = validtimes.get(i).getPaymentEnddate();
			System.out.println(start+"//"+end);
			int date = ltidate.calculateInterval(start, end);
			int vdate = ltidate.calculateInterval(start, today);
			int bdate = Math.abs(vdate-date);
			validtimes.get(i).setValidtime(bdate);
			validtimes.get(i).setTimeperiod(date);
			vcs.add(bdate);
		}
		Collections.sort(vcs);
		Set<Integer> vCounts = new HashSet<Integer>();
		vCounts.addAll(vcs);
		Map<String,Integer> vCountPairs = new HashMap<String,Integer>();	
		int count = 0;
		int m = 0;
		Iterator ivs = vCounts.iterator();
		while(ivs.hasNext()){			
			Integer iv = (Integer)ivs.next();
			for(int i=m;i<vcs.size();i++){
				if(iv.intValue()==vcs.get(i).intValue())
					count++;
				else{
					m=i;break;
				}
			}
			vCountPairs.put(iv.toString(), count);	
			count=0;
		}
		List<Map.Entry<String, Integer>> infoIds = new ArrayList<Map.Entry<String, Integer>>(vCountPairs.entrySet()); 

		Collections.sort(infoIds, new Comparator<Map.Entry<String, Integer>>() { 
			public int compare(Map.Entry<String, Integer> o1, 
			Map.Entry<String, Integer> o2) { 
			return (Integer.parseInt(o1.getKey()) - Integer.parseInt(o2.getKey())); 
			} 
			}); 

		System.out.println("done");
	}

	public List<String> getItemsname() {
		return itemsname;
	}

	public void setItemsname(List<String> itemsname) {
		this.itemsname = itemsname;
	}

	public String getOldItemName() {
		return oldItemName;
	}

	public void setOldItemName(String oldItemName) {
		this.oldItemName = oldItemName;
	}

}
