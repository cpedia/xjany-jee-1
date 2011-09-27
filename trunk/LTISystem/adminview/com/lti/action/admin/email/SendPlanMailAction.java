package com.lti.action.admin.email;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletContext;

import org.apache.struts2.ServletActionContext;
import org.springframework.web.struts.ActionServletAwareProcessor;

import com.lti.action.Action;
import com.lti.service.UserManager;
import com.lti.service.bo.User;
import com.lti.system.ContextHolder;
import com.lti.util.EmailUtil;

public class SendPlanMailAction {
	private Long planID;

	private String content;

	public Long getPlanID() {
		return planID;
	}

	public void setPlanID(Long planID) {
		this.planID = planID;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	private String title;

	public String execute() {
		final ServletContext session = ServletActionContext.getServletContext();
		final String _title=title;
		new Thread() {
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			public void add(String value) {
				Map<String, String> send_plan_mail_messages = (Map<String, String>) session.getAttribute("send_plan_mail_messages");
				if (send_plan_mail_messages == null) {
					send_plan_mail_messages = new TreeMap<String, String>();
					session.setAttribute("send_plan_mail_messages", send_plan_mail_messages);
				}
				send_plan_mail_messages.put("[" + df.format(new Date()) + "]" + (System.currentTimeMillis() % 1000), value);
			}

			public void run() {
				List<Long> portfolioids = ContextHolder.getStrategyManager().getModelPortfolioIDs(planID);

				add("[Starting]");
				if (portfolioids != null && portfolioids.size() > 0) {
					Long[] pids = new Long[portfolioids.size()];
					portfolioids.toArray(pids);
					UserManager um = ContextHolder.getUserManager();
					List<Long> uids = um.getUserIDListByPlanIDs(pids);
					if (uids != null && uids.size() > 0) {
						Map<Long, Boolean> map = new HashMap<Long, Boolean>();
						for (int i = 0; i < uids.size(); i++) {
							User user = um.get(uids.get(i));
							if (user == null)
								continue;
							try {
								if (map.get(user) == null) {
									EmailUtil.sendMail(new String[]{user.getEMail()}, _title, content.replaceAll("#username#", user.getUserName()));
									map.put(user.getID(), true);
									add("[Sended]" + user.getUserName());
								}
							} catch (Exception e) {
								// e.printStackTrace();
								add("[" + e.getMessage() + "]" + user.getUserName());
							}
						}

					}

				}
				add("[End]");
			}// end run
		}.start();

		message = "<a href='ViewSendingPlanProc.action'>View Process</a>";
		return Action.MESSAGE;
	}
}
