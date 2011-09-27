package com.lti.executor.web;

import java.util.List;
import java.util.Map;

import com.lti.service.bo.Portfolio;

public interface PortfoliosFilter {
	public List<com.lti.service.bo.Portfolio> getPortfolios(boolean forceMonitor);
	public void setParamters(Map parameters);
}
