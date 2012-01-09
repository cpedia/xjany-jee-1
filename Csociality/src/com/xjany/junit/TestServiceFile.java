package com.xjany.junit;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.xjany.bbs.service.FileService;


public class TestServiceFile extends JUnitBase{  
    
    
    @Autowired
	private FileService fileService;
  
    @Test  
    public void list() throws Exception {  
    	fileService.findById(1);
    }  
}  