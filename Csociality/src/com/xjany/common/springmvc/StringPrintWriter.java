package com.xjany.common.springmvc;

import java.io.PrintWriter;  
import java.io.StringWriter;  
 
/**
 * 辅助ExceptionHandler类的类，因为e.printStackTrace参数只有个PrintWriter类型
 * @author Lixiang
 *
 */
public class StringPrintWriter extends PrintWriter{  
  
    public StringPrintWriter(){  
        super(new StringWriter());  
    }  
     
    public StringPrintWriter(int initialSize) {  
          super(new StringWriter(initialSize));  
    }  
     
    public String getString() {  
          flush();  
          return ((StringWriter) this.out).toString();  
    }  
     
    @Override  
    public String toString() {  
        return getString();  
    }  
}  