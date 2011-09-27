package com.lti.action.admin.portfolio;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.UserManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.PortfolioState;
import com.lti.system.Configuration;
import com.opensymphony.xwork2.ActionSupport;

public class MonitorMainAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(MonitorMainAction.class);
	
	private PortfolioManager portfolioManager;

	private UserManager userManager;
	
	class MonitorPortfolioState{
		private Long ID;
		private String name;
		private String state;
		private String message;
		private String lastValidDate;
		private Boolean isLive;
		public String getLastValidDate() {
			return lastValidDate;
		}
		public void setLastValidDate(String lastValidDate) {
			this.lastValidDate = lastValidDate;
		}
		public Boolean getIsLive() {
			return isLive;
		}
		public void setIsLive(Boolean isLive) {
			this.isLive = isLive;
		}
		public Long getID() {
			return ID;
		}
		public void setID(Long id) {
			ID = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}


	public void setPortfolioManager(PortfolioManager portfolioManager) {
		this.portfolioManager = portfolioManager;
	}

	public void validate(){
		
		
	}
	
	private Integer failedNumber;
	private Integer finishedNumber;
	private Integer totalNumber;
	private Integer othersNumber;

	public Integer getFailedNumber() {
		return failedNumber;
	}

	public void setFailedNumber(Integer failedNumber) {
		this.failedNumber = failedNumber;
	}

	public Integer getFinishedNumber() {
		return finishedNumber;
	}

	public void setFinishedNumber(Integer finishedNumber) {
		this.finishedNumber = finishedNumber;
	}

	public Integer getTotalNumber() {
		return totalNumber;
	}

	public void setTotalNumber(Integer totalNumber) {
		this.totalNumber = totalNumber;
	}

	public Integer getOthersNumber() {
		return othersNumber;
	}

	public void setOthersNumber(Integer othersNumber) {
		this.othersNumber = othersNumber;
	}

	
	private List<MonitorPortfolioState> portfolioStates;
	@Override
	public String execute() throws Exception {
//		List<PortfolioState> pses=portfolioManager.getPortfolioStates();
//		
//		portfolioStates=new ArrayList<MonitorPortfolioState>();
//		
//		if(pses!=null&&pses.size()!=0){
//			totalNumber=pses.size();
//			failedNumber=0;
//			othersNumber=0;
//			finishedNumber=0;
//			for(int i=0;i<pses.size();i++){
//				PortfolioState ps=pses.get(i);
//				MonitorPortfolioState m=new MonitorPortfolioState();
//				m.setID(ps.getPortfolioID());
//				Portfolio p=portfolioManager.get(ps.getPortfolioID());
//				if(p==null)continue;
//				m.setName(p.getName());
//				DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
//				try{m.setLastValidDate(df.format(p.getEndDate()));}catch(Exception e){}
//				if(p.getState()==1){
//					m.setIsLive(true);
//					othersNumber++;
//				}
//				else m.setIsLive(false);
//				String message=ps.getMessage();
//				if(message!=null){
//					try {
//						int index=message.indexOf('\n');
//						if(index>10||index==-1)index=10;
//						message=message.substring(0, index);
//						message+="...";
//					} catch (RuntimeException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				m.setMessage(message);
//				if(ps!=null){
//					switch(ps.getState()){
//					case Configuration.PORTFOLIO_RUUNING_STATE_DAILYEXECUTION_FAILED:
//						m.setState("FAILED_DAILYEXECUTION");
//						failedNumber++;
//						break;
//					case Configuration.PORTFOLIO_RUUNING_STATE_DAILYEXECUTION_FINISHED:
//						m.setState("FINISHED_DAILYEXECUTION");
//						finishedNumber++;
//						break;
//					case Configuration.PORTFOLIO_RUNNING_STATE_FAILED:
//						m.setState("FAILED_MONITOR");
//						failedNumber++;
//						break;
//					case Configuration.PORTFOLIO_RUNNING_STATE_FINISHED:
//						m.setState("FINISHED_MONITOR");
//						finishedNumber++;
//						break;
//					case Configuration.PORTFOLIO_RUNNING_STATE_COMPUTMPTS:
//						m.setState("Computing MPT!");
//						break;
//					case Configuration.PORTFOLIO_RUNNING_STATE_EXECUTING:
//						m.setState("Running");
//						break;
//					case Configuration.PORTFOLIO_RUNNING_STATE_PREPARING:
//						m.setState("Not Compiled Strategy?");
//						//failedNumber++;
//						break;
//					case Configuration.PORTFOLIO_RUNNING_STATE_INACTIVE:
//						m.setState("Not yet Monitored");
//						//failedNumber++;
//						break;
//					default:
//						m.setState("Not yet Monitored or it's updatest!");
//						break;
//					}
//				}else{
//				}
//				
//				portfolioStates.add(m);
//			}
//		}
		
		return Action.SUCCESS;

	}


	public List<MonitorPortfolioState> getPortfolioStates() {
		return portfolioStates;
	}


	public void setPortfolioStates(List<MonitorPortfolioState> portfolioStates) {
		this.portfolioStates = portfolioStates;
	}



	

}
