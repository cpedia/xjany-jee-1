package com.lti.action.admin.portfolio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.lti.action.Action;
import com.lti.service.PortfolioManager;

public class CheckPortfolioAction
{
	/**
	 * 
	 */
	private String message;
	private PortfolioManager portfolioManager;
	private String portfolioIDs;
	private String advancedPortfolio;
	private String smartPortfolio;
	private String marketPortfolio;
	private String classesPortfolio;
	private String indicatorsPortfolio;
	private String emialStatistics;

	private File portfolioIdFile;
	private String uploadFileFileName;
	private InputStream fis;
	private String planName;

	public File getPortfolioIdFile()
	{
		return portfolioIdFile;
	}

	public void setPortfolioIdFile(File portfolioIdFile)
	{
		this.portfolioIdFile = portfolioIdFile;
	}

	public String getUploadFileFileName()
	{
		return uploadFileFileName;
	}

	public void setUploadFileFileName(String uploadFileFileName)
	{
		this.uploadFileFileName = uploadFileFileName;
	}

	public InputStream getFis()
	{
		return fis;
	}

	public void setFis(InputStream fis)
	{
		this.fis = fis;
	}

	public String getPlanName()
	{
		return planName;
	}

	public void setPlanName(String planName)
	{
		this.planName = planName;
	}

	public String getEmialStatistics()
	{
		return emialStatistics;
	}

	public void setEmialStatistics(String emialStatistics)
	{
		this.emialStatistics = emialStatistics;
	}

	public String getIndicatorsPortfolio()
	{
		return indicatorsPortfolio;
	}

	public void setIndicatorsPortfolio(String indicatorsPortfolio)
	{
		this.indicatorsPortfolio = indicatorsPortfolio;
	}

	public String getClassesPortfolio()
	{
		return classesPortfolio;
	}

	public void setClassesPortfolio(String classesPortfolio)
	{
		this.classesPortfolio = classesPortfolio;
	}

	public String getMarketPortfolio()
	{
		return marketPortfolio;
	}

	public void setMarketPortfolio(String marketPortfolio)
	{
		this.marketPortfolio = marketPortfolio;
	}

	public String getSmartPortfolio()
	{
		return smartPortfolio;
	}

	public void setSmartPortfolio(String smartPortfolio)
	{
		this.smartPortfolio = smartPortfolio;
	}

	public String getAdvancedPortfolio()
	{
		return advancedPortfolio;
	}

	public void setAdvancedPortfolio(String advancedPortfolio)
	{
		this.advancedPortfolio = advancedPortfolio;
	}

	public String getPortfolioIDs()
	{
		return portfolioIDs;
	}

	public void setPortfolioIDs(String portfolioIDs)
	{
		this.portfolioIDs = portfolioIDs;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public PortfolioManager getPortfolioManager()
	{
		return portfolioManager;
	}

	public void setPortfolioManager(PortfolioManager portfolioManager)
	{
		this.portfolioManager = portfolioManager;
	}

	public String execute()
	{
		try
		{
//			String systemPath;
//			String sysPath = System.getenv("windir");
//			String str = System.getProperty("os.name").toUpperCase();
//			if (str.indexOf("WINDOWS") == -1)
//				systemPath = "/var/tmp/";
//			else
//				systemPath = sysPath + "\\temp\\";
//			if (portfolioIdFile != null)
//			{
//				if (uploadFileFileName.equals("porfolioid.csv"))
//				{
//					InputStream stream = new FileInputStream(portfolioIdFile);
//					OutputStream bos = new FileOutputStream(systemPath + uploadFileFileName);
//					int bytesRead = 0;
//					byte[] buffer = new byte[8192];
//					while ((bytesRead = stream.read(buffer, 0, 8192)) != -1)
//					{
//						bos.write(buffer, 0, bytesRead);
//					}
//					bos.close();
//					stream.close();
//					message = "uploadFile success";
//				} else
//				{
//					message = "the file name must be porfolioid.csv";
//				}
//			}
			 if(!portfolioIDs.equals("null"))
				 message = portfolioManager.checkPlanName(portfolioIDs);
			 else if(!advancedPortfolio.equals("null"))
				 message = portfolioManager.returnPortfolio(advancedPortfolio);
			 else if(!smartPortfolio.equals("null"))
				 message = portfolioManager.returnPortfolio(smartPortfolio);
			 else if(!marketPortfolio.equals("null"))
				 message = portfolioManager.returnPortfolio(marketPortfolio);
			 else if(!classesPortfolio.equals("null"))
				 message = portfolioManager.returnPortfolio(classesPortfolio);
			 else if(!indicatorsPortfolio.equals("null"))
				 message = portfolioManager.returnPortfolio(indicatorsPortfolio);
			 else if(!emialStatistics.equals("null"))
				 message = portfolioManager.returnPortfolioStatistics(emialStatistics);
			return Action.MESSAGE;
		} catch (Exception e)
		{
			e.printStackTrace();
			message = "error";
			return Action.MESSAGE;
		}

	}

}
