package com.lti.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.lti.service.PortfolioManager;
import com.lti.service.bo.GlobalObject;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class PersistentUtil {

	public static void writeObject(Object o,String id)throws Exception{
		File file=new File(Configuration.getObjectDir(),id);
		ObjectOutputStream oos=new ObjectOutputStream(new FileOutputStream(file));
		oos.writeObject(o);
		oos.flush();
		oos.close();
	}
	
	public static Object readObject(String id)throws Exception{
		File file=new File(Configuration.getObjectDir(),id);
		ObjectInputStream ois=new ObjectInputStream(new FileInputStream(file));
		Object o = ois.readObject();
		ois.close();
		return o;
	}
	
	public static Object readGlobalObject(String id)throws Exception{
		GlobalObject go=ContextHolder.getPortfolioManager().getGlobalObject(id);
		ObjectInputStream ois=new ObjectInputStream(new ByteArrayInputStream(go.getBytes()));
		Object o = ois.readObject();
		ois.close();
		return o;
	}
	
	public static void writeGlobalObject(Object o,String id,Long strategyid,Long portfolioid)throws Exception{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ObjectOutputStream oos=new ObjectOutputStream(baos);
		oos.writeObject(o);
		oos.flush();
		oos.close();
		PortfolioManager pm=ContextHolder.getPortfolioManager();
		GlobalObject go=new GlobalObject();
		go.setKey(id);
		go.setBytes(baos.toByteArray());
		go.setStrategyID(strategyid);
		go.setPortfolioID(portfolioid);
		pm.updateGlobalObject(go);
	}
	
	
	
	
	
}
