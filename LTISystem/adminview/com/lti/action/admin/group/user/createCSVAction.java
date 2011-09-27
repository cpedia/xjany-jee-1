package com.lti.action.admin.group.user;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.lti.action.Action;
import com.lti.service.GroupManager;
import com.lti.service.bo.User;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;
import com.opensymphony.xwork2.ActionSupport;
import org.supercsv.io.CsvListWriter;
import org.supercsv.io.ICsvListWriter;
import org.supercsv.prefs.CsvPreference;

public class createCSVAction extends ActionSupport implements Action {
	private GroupManager groupManager;
	private String action="";
	private Long groupID;
	public InputStream inputStream;
	public String fileName;
	private PropertyDescriptor[] propertyDescriptors; 
	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}
	
	public Long getGroupID() {
		return groupID;
	}

	public void setGroupID(Long groupID) {
		this.groupID = groupID;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	
	public void setGroupManager(GroupManager groupManager) {
		this.groupManager = groupManager;
	}
	
	@Override
	public String execute() throws Exception {
		List<User> userList = new ArrayList<User>();
		userList = groupManager.getUsersByGroupID(groupID);
		String groupName = groupManager.get(groupID).getName();
		createFile(userList,groupName);
		return Action.SUCCESS;
	}
	
	public void createFile(List<User> userList,String groupName)throws Exception{	
		fileName = groupName+".csv";
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(bos);
		createCSVContent(userList,osw);
		bos.flush();
		inputStream = new ByteArrayInputStream(bos.toByteArray());
		osw.close();
		bos.close();
	}
	
	public static void createCSVContent(List<User> userList,OutputStreamWriter os){
		try{
			ICsvListWriter clw = new CsvListWriter(os, CsvPreference.STANDARD_PREFERENCE);
			List<String> headers = com.lti.util.CSVEncoder.getHeaders(userList.get(0).getClass());
			if(headers!=null)clw.write(headers);
			for (int i = 0; i < userList.size(); i++) {
				User user = (User)userList.get(i);
				//List<String> row=readAllProperties(user);
				List<String> row = com.lti.util.CSVEncoder.getRow(user);
				if(row!=null)
					clw.write(row);
			}
			clw.close();
		}catch(Exception e) {
			e.printStackTrace();
		}		
	}

	
//	public static List<String> readAllProperties(User obj){
//		if (obj == null)
//			return null;
//		List<String> row=new ArrayList<String>();
//
//		BeanInfo sourceBean = null;
//		PropertyDescriptor[] propertyDescriptors = null;
//		try {
//			sourceBean = Introspector.getBeanInfo(obj.getClass());
//			propertyDescriptors = sourceBean.getPropertyDescriptors();
//			if (propertyDescriptors == null || propertyDescriptors.length == 0) {
//				return null;
//			}
//		} catch (Exception e) {
//			return null;
//		}
//		for (int i = 0; i < propertyDescriptors.length; i++) {   
//            Method method = propertyDescriptors[i].getReadMethod();   
//            if (method != null) {   
//                try {   
//                    System.out.println(method.getName()+"的值是:\t"+method.invoke(obj, new Object[]{}));
//                    String rowString = null;
//                    if(method.invoke(obj, new Object[]{})==null) rowString = "";
//                    	else rowString = method.invoke(obj, new Object[]{}).toString();
//                    row.add(rowString);
//                } catch (IllegalArgumentException e) {   
//                    e.printStackTrace();   
//                } catch (IllegalAccessException e) {   
//                    e.printStackTrace();   
//                } catch (InvocationTargetException e) {   
//                    e.printStackTrace();   
//                }   
//            }   
//        }   
//		return row;
//	}
}
