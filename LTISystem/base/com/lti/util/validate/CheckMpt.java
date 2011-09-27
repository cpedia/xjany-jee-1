package com.lti.util.validate;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import com.lti.system.Configuration;

public class CheckMpt {

	private String logFile;

	public CheckMpt() {

		String sysPath = System.getenv("windir");
		Date date = new Date();
		Integer year = date.getYear() + 1900;
		Integer month = date.getMonth() + 1;
		Integer day = date.getDate();
		String monthS = month > 9 ? (month.toString()) : ("0" + month.toString());
		String dayS = day > 9 ? (day.toString()) : ("0" + day.toString());
		this.logFile = Configuration.getTempDir() + year.toString() + monthS + dayS + "CheckMpt.log";

	}

	public void writeLog(String log) {

		try {

			FileOutputStream stream = new FileOutputStream(this.logFile, true);
			OutputStreamWriter writer = new OutputStreamWriter(stream);
			writer.write(log + "\n");
			writer.close();
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean isLinux() {
		String str = System.getProperty("os.name").toUpperCase();
		if (str.indexOf("WINDOWS") == -1) {
			return true;
		}
		return false;
	}

	public static void main(String[] args) throws Exception {

	}
}
