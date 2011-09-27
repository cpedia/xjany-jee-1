package com.lti.executor.web.action;

import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;

import com.lti.executor.web.BasePage;
import com.lti.service.PortfolioManager;
import com.lti.service.bo.Portfolio;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.User;
import com.lti.service.bo.UserTransaction;
import com.lti.system.ContextHolder;

public class Statistic extends BasePage{

	private String info;
	
	
	@Override
	public String execute() throws Exception {
		info="Welcome to validfi.com.";
		return "info.ftl";
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	
}
