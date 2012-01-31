package com.xjany.bbs.action.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.springframework.stereotype.Controller;


import com.xjany.common.XjanyConstants;
import com.xjany.common.util.GrenricUtil;

@Controller
public class TempleteAction {
	
	
	public static void main(String[] args){
		Map<String,Object> objs=new HashMap<String, Object>();
		List list = new ArrayList();
		list.add("a");
		list.add("a");
		objs.put("tr", list);
		GrenricUtil._get_code(objs,XjanyConstants.GRENERIPATH,XjanyConstants.TEMPLETPATH, "test.html","test.js");
	}
}
