package com.lti.util;

import java.io.*;
import java.awt.Frame;
import java.awt.FileDialog;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.rosuda.JRI.Rengine;
import org.rosuda.JRI.REXP;
import org.rosuda.JRI.RList;
import org.rosuda.JRI.RVector;
import org.rosuda.JRI.RMainLoopCallbacks;

class TextConsole implements RMainLoopCallbacks
{
    public void rWriteConsole(Rengine re, String text, int oType) 
    {
        System.out.print(text);
    }
    
    public void rBusy(Rengine re, int which)
    {
        System.out.println("rBusy("+which+")");
    }
    
    public String rReadConsole(Rengine re, String prompt, int addToHistory) 
    {
        System.out.print(prompt);
        
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
            
            String s=br.readLine();
            
            return (s==null||s.length()==0)?s:s+"\n";
            
        } catch (Exception e) {
        	
            System.out.println("jriReadConsole exception: "+e.getMessage());
            
        }
        return null;
    }
    
    public void rShowMessage(Rengine re, String message) 
    {
        System.out.println("rShowMessage \""+message+"\"");
    }
	
    public String rChooseFile(Rengine re, int newFile) 
    {
		FileDialog fd = new FileDialog(new Frame(), (newFile==0)?"Select a file":"Select a new file", (newFile==0)?FileDialog.LOAD:FileDialog.SAVE);
		
		fd.show();
		
		String res=null;
		
		if (fd.getDirectory()!=null) res=fd.getDirectory();
		
		if (fd.getFile()!=null) res=(res==null)?fd.getFile():(res+fd.getFile());
		
		return res;
    }
    
    public void   rFlushConsole (Rengine re) 
    {
    	
    }
	
    public void   rLoadHistory  (Rengine re, String filename) 
    {
    	
    }			
    
    public void   rSaveHistory  (Rengine re, String filename) 
    {
    	
    }			
}


public class  LTIRInterface {

	private static Rengine re;
	
	private static LTIRInterface instance = null;
	
	public synchronized static LTIRInterface getInstance() {
		if(instance==null){
			instance=new LTIRInterface();
			System.out.println("NEW!!!");
		}
		return instance;
	}
	
	private LTIRInterface()
	{		
		String[] args = new String[1];
		//this line is very very very important!
		args[0] = "--no-save";
		re=new Rengine(args, false, null);//new TextConsole());
	}
	
	public synchronized String[] RCall(String[] cmds, int num)
	{
		String[] returnString = new String[num];
		
		if (!Rengine.versionCheck())
		{
		    System.err.println("** Version mismatch - Java files don't match library version.");
		    
		    System.exit(1);
		}		
			
        
        if (!re.waitForR()) 
        {
        	String[] args = new String[1];
        	//this line is very very very important!
			args[0] = "--no-save";
			re=new Rengine(args, false, null);//new TextConsole());
        }
        
        for(int i=0;i<cmds.length;i++)
        {
        	re.eval(cmds[i]);
        }
        
        for(int i=1;i<=num;i++)
        {
        	String tmp = Integer.toString(i);
        	tmp = "LTI_return"+tmp;
        	REXP r = re.eval(tmp);
        	returnString[i-1] = r.toString();
        }
		
        
		return returnString;
	}
	
	public synchronized REXP[] RCall2(String[] cmds, int num)
	{		
		String[] args = new String[1];
		REXP[] returnEXP = new REXP[num];
		
		if (!Rengine.versionCheck())
		{
		    System.err.println("** Version mismatch - Java files don't match library version.");
		    
		    System.exit(1);
		}		
		        
        if (!re.waitForR()) 
        {
			args[0] = "--no-save";
			re=new Rengine(args, false, new TextConsole());
        }
        
        for(int i=0;i<cmds.length;i++)
        {
        	re.eval(cmds[i]);
        }        
        
        
        for(int i=1;i<=num;i++)
        {
        	String tmp = Integer.toString(i);
        	tmp = "LTI_return"+tmp;
        	REXP r = re.eval(tmp);
        	returnEXP[i-1] = r;
        }
		
        
		return returnEXP;
	}
	
	public synchronized void closeEngine()
	{
		//re.end();
		//re.stop();
	}
	
	public synchronized static void main(String[] args)
	{
		System.out.println(System.getProperty("java.library.path"));
		String[] a = new String[5];
		
		REXP[] b = new REXP[2];
		
		String[] bb = new String[2];
		

		a[0] = "2;";//library(\"polynom\",character.only=TRUE)";
		
		a[1] = "pbase=2";//poly.calc(0:0)";
		
		a[2] = "LTI_return1=4*pbase";
		
		a[3] = "LTI_return2=6*pbase";
		
		a[4] = "LTI_return1=GCD(LTI_return1,LTI_return2)";
				
		LTIRInterface li = LTIRInterface.getInstance();
		
		bb = li.RCall(a, 1);
		
		for(int i=0;i<bb.length;i++)
		{
			System.out.println(bb[i]);
		}		
		
		li.closeEngine();
		
	}
	
	public synchronized double parseDouble(REXP r)
	{
		return r.asDouble();
	}
	
	public synchronized int parseInteger(REXP r)
	{
		return r.asInt();
	}
	
	public synchronized double[] parseDoubleArray(REXP r)
	{
		return r.asDoubleArray();
	}
	
	public synchronized int[] parseIntegerArray(REXP r)
	{
		return r.asIntArray();
	}
	
	public synchronized double[][] parseDoubleMatrix(REXP r)
	{
		return r.asDoubleMatrix();
	}
	
	public synchronized boolean parseBool(REXP r)
	{
		return r.asBool().isTRUE();
	}
	
	public synchronized Double[] parseVector(REXP r)
	{
		double[] m;
		RVector rv = r.asVector();
		
		List<Double> ld = new ArrayList<Double>();
		for(int i=0;i<rv.size();i++){
			REXP tmp = (REXP)rv.get(i);
			
			m = this.parseDoubleArray(tmp);
			for(int j=0;j<m.length;j++)
			{
				ld.add(m[j]);
			}
		}
		
		Double[] value = new Double[ld.size()];
		
		for(int k=0;k<ld.size();k++)
		{
			value[k] = ld.get(k);
		}
		
		return value;
	}
	
}
