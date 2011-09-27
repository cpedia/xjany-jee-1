package com.lti.start;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import com.lti.customizepage.PageCompiler;
import com.lti.service.CustomizePageManager;
import com.lti.service.bo.CustomizePage;
import com.lti.system.ContextHolder;


public class CompiledAllPages {
	public static void compile(){
		CustomizePageManager cm=(CustomizePageManager) ContextHolder.getInstance().getApplicationContext().getBean("customizePageManager");
		
		List<CustomizePage> cps=cm.getCustomizePages();
		
		System.out.println(cps.size());
		for(int i=0;i<cps.size();i++){
			CustomizePage s=cps.get(i);
			System.out.println("Number "+i+": "+s.getName());
			if(s.getName()==null||s.getName().length()==0)continue;
			
			try {
				String s1=PageCompiler.Complie(cps.get(i), ContextHolder.getServletPath());
				if(!s1.equals("")){
					try{
						//PageCompiler.Delete(cps.get(i).getName(), ContextHolder.getServletPath());
					}catch(Exception ex){
						
					}
				}
				//PageCompiler.GenerateTemplate(cps.get(i).getName(), ContextHolder.getServletPath(), cps.get(i).getTemplate());
			} catch (Exception e) {
				e.printStackTrace();
				
			}
		}
	}
	public static void update(){
		CustomizePageManager cm=(CustomizePageManager) ContextHolder.getInstance().getApplicationContext().getBean("customizePageManager");
		
		List<CustomizePage> cps=cm.getCustomizePages();
		
		System.out.println(cps.size());
		for(int i=0;i<cps.size();i++){
			CustomizePage cp=cps.get(i);
			StringBuffer sb = new StringBuffer();;
			try {
				File f=new File(ContextHolder.getServletPath()+"/jsp/customizepage/",cp.getName()+".ftl");
				BufferedReader br=new BufferedReader(new FileReader(f));
				String line=br.readLine();
				while(line!=null){
					sb.append(line);
					sb.append("\r\n");
					line=br.readLine();
				}
				br.close();
				if(!sb.toString().equals(cp.getTemplate())){
					System.out.println(cp.getName());
				}
			} catch (Exception e) {
				//System.out.println("delete: "+cp.getName());
			}
			
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		compile();
	}

}
