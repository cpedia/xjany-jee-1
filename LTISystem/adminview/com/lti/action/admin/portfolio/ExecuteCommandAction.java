package com.lti.action.admin.portfolio;



import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.lti.util.ProcessUtil;
import com.opensymphony.xwork2.ActionSupport;

public class ExecuteCommandAction extends ActionSupport implements Action {

	private static final long serialVersionUID = 1L;

	static Log log = LogFactory.getLog(ExecuteCommandAction.class);
	
	public void validate(){
		
	}
	private String keyWords;
	public String getKeyWords() {
		return keyWords;
	}

	public void setKeyWords(String keyWords) {
		this.keyWords = keyWords;
	}

	public String getUpdateMode() {
		return updateMode;
	}

	public void setUpdateMode(String updateMode) {
		this.updateMode = updateMode;
	}
	private String updateMode;
	private String resetMode;
	
	private String command;
	
	private String modeInterval;
	
	private String prioritys;

	public String getPrioritys() {
		return prioritys;
	}

	public void setPrioritys(String prioritys) {
		this.prioritys = prioritys;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}
	
	private Long portfolioID;
	


	public Long getPortfolioID() {
		return portfolioID;
	}

	public void setPortfolioID(Long portfolioID) {
		this.portfolioID = portfolioID;
	}
	
	private String resultString;

	@Deprecated
	@Override
	public String execute() throws Exception {
		resultString="Invalid command!";
		if(command==null){
			
		}else if(command.equals("stopExecution")){
			ProcessUtil.stop();
			resultString="Send stop command.";
			
		}else if(command.equals("startExecution")){
			ProcessUtil.start();
			resultString="Send start command.";
		}else if(command.equals("restartExecution")){
			ProcessUtil.stop();
			resultString="Send stop command.\nSend start command";
			try {
				Thread.sleep(10000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ProcessUtil.start();
		}else if(command.equals("getExecution")){
			resultString=ProcessUtil.get();
		}else if(command.equals("stopCurrentExecution")){
			if(portfolioID==null){
				resultString="stop current execution ... error.";
			}else{
				try{
					//ExecutorPool.getInstance().stopExecution(portfolioID);
					resultString="stop current execution["+portfolioID+"] ... ok.";
				}catch(Exception e){
					resultString="stop current execution["+portfolioID+"] ... It have already stopped.";
				}
			}

		}else if(command.equals("resetState")){
			PortfolioManager pm=ContextHolder.getPortfolioManager();
			List<Portfolio> mps=pm.getSimplePortfolios(-1, -1);
			for(int i=0;i<mps.size();i++){
				Portfolio p=mps.get(i);
			}
			resultString="OK";
		}else if(command.equals("updateResetMode")){
			if(resetMode!=null&&!resetMode.equals("")){
				Configuration.set(Configuration.DAILY_EXECUTION_RESET_MODE, resetMode);
				resultString="OK.";
			}else{
				resultString="Not Ok.";
			}
		}else if(command.equals("getResetMode")){
			resetMode=(String)Configuration.get(Configuration.DAILY_EXECUTION_RESET_MODE);
			if(resetMode!=null&&!resetMode.equals("")){
				resultString=resetMode;
			}else{
				resultString="none";
			}
		}else if(command.equals("updateIntervalMode")){
			if(modeInterval!=null&&!modeInterval.equals("")){
				String[] updateIntervals = modeInterval.split("_");
				if(updateIntervals.length!=8){
					resultString ="Not OK. Need 8 interval days";
				}else{
					Configuration.set("PORTFOLIO_UPDATE_MODE", modeInterval);
					resultString = "ok";
				}
			}
		}else if(command.equals("getUpdateInterval")){
			resultString = (String) Configuration.get("PORTFOLIO_UPDATE_MODE");
		}else if(command.equals("updatePriorityArray")){
			if(prioritys!=null&&!prioritys.equals("")){
				Configuration.set("PORTFOLIO_UPDATE_PRIORITY", prioritys);
				resultString = "ok";
			}
		}else if(command.equals("getPriorityArray")){
			resultString = (String) Configuration.get("PORTFOLIO_UPDATE_PRIORITY");
		}else if(command.equals("setPortfolioState")){
//			PortfolioManager pm=ContextHolder.getPortfolioManager();
//			List<Portfolio> portfolioList = pm.getSimplePortfolios(-1, -1;
//			boolean[] has = new boolean[portfolioList.size()];
//			for(int i=0;i<portfolioList.size();++i)
//				has[i] = false;
//			String[] keyWord = null;
//			int mode=0;
//			if(keyWords!=null){
//				keyWord = keyWords.split(",");
//			}
//			if(updateMode!=null && updateMode!=""){
//				mode = Integer.parseInt(updateMode);
//			}
//			for(int i=0;i<keyWord.length;++i){
//				keyWord[i] = keyWord[i].toLowerCase();
//				for(int j=0;j<portfolioList.size();++j){
//					Portfolio p = portfolioList.get(j);
//					if(!has[j] && p.getUpdateMode() == mode){
//						if(p.getName().indexOf(keyWord[i])!= -1){
//							//has[i] = true;
//							//PortfolioState ps = pm.getPortfolioState(p.getID());
//							//ps.setState(Configuration.PORTFOLIO_RUNNING_STATE_INACTIVE);
//							//pm.updatePortfolioState(ps);
//						}
//					}
//				}
//			}
			resultString = "this action is not used any more.";
		}
		return Action.SUCCESS;

	}

	public String getResultString() {
		return resultString;
	}

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public String getResetMode() {
		return resetMode;
	}

	public void setResetMode(String resetMode) {
		this.resetMode = resetMode;
	}

	public String getModeInterval() {
		return modeInterval;
	}

	public void setModeInterval(String modeInterval) {
		this.modeInterval = modeInterval;
	}

}
