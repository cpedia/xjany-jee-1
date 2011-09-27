package com.lti.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.lti.system.ContextHolder;

public class ProcessUtil {
	public static String get(){
		try {
			Process p = Runtime.getRuntime().exec(new String[]{"ps","aux"});
			BufferedReader bw = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String pstr = "";
			StringBuffer sb=new StringBuffer();
			while ((pstr = bw.readLine()) != null) {
				if(pstr.contains("java")){
					sb.append(pstr);
					sb.append("\n");
				}

			}
			p.destroy();
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public static void stop(){
		try {
			Process p = Runtime.getRuntime().exec(new String[]{"ps","aux"});
			BufferedReader bw = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String pstr = "";
			while ((pstr = bw.readLine()) != null) {
				if(pstr.contains("com.lti.executor.web.Start")){
					String[] items=pstr.replaceAll("\\s+", " ").split(" ");
					try {
						Runtime.getRuntime().exec(new String[]{"kill","-9",items[1]});
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			p.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isRun(){
		try {
			Process p = Runtime.getRuntime().exec(new String[]{"ps","aux"});
			BufferedReader bw = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String pstr = "";
			while ((pstr = bw.readLine()) != null) {
				if(pstr.contains("com.lti.executor.web.Start")){
					return true;
				}
			}
			p.destroy();
		} catch (Exception e) {
		}
		return false;
	}
	
	public static String start(){
		try{
			Process p1=Runtime.getRuntime().exec(new String[]{"chmod","777",StringUtil.getPath(new String[]{ContextHolder.getServletPath(),"startEE.sh"})});
			Process p2=Runtime.getRuntime().exec(StringUtil.getPath(new String[]{ContextHolder.getServletPath(),"startEE.sh"}));
			return "Send start command OK.";
		}catch(Exception e){
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	public static void main(String[] args){
		stop();
	}

}
