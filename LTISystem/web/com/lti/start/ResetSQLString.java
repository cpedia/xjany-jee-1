package com.lti.start;
import com.lti.system.ContextHolder;


public class ResetSQLString {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			ContextHolder.setSQLString(args[0]);
			ContextHolder.setLanguage(args[1]);
			ContextHolder.setConfigFile(args[2]);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
