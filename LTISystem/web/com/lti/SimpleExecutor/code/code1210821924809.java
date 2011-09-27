package com.lti.SimpleExecutor.code;

import com.lti.service.bo.*;
import java.util.*;
import com.lti.type.*;
import com.lti.util.*;

public class code1210821924809 extends com.lti.SimpleExecutor.BaseCode{
	public code1210821924809(){super();}

	public void execute() throws Exception{

		try{
			String[] ses={"spy","vfinx"};
Date d=securityManager.getValidStartDate(ses);
print("valid start date :"+d);

print("spy start date:"+securityManager.getStartDate(securityManager.get("spy").getID()));
print("vfinx start date:"+securityManager.getStartDate(securityManager.get("vfinx").getID()));


		}catch(Exception e){
			print(getStackTraceString(e));
			throw e;
		}
	}

}
