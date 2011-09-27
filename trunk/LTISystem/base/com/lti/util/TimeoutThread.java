package com.lti.util;

public class TimeoutThread{
    long milliseconds;
    private void notifyObj(){
        synchronized(this){
            notify();
        }
    }
    
    public TimeoutThread(long milliseconds){
        this.milliseconds = milliseconds;
    }
    
    public void mydo(){
        
    }
    
    Thread runthread = null;
    private void myrun(){
        
        class MyThread extends Thread{
            public void run(){
                try{
                    Thread.sleep(milliseconds);
                }catch(Exception exp){
                	//exp.printStackTrace();
                }
              
                int i=0;
                while(runthread.isAlive()){
                    try{
                    	Thread.sleep(50);
                    }catch(Exception exp){}
                    runthread.stop();
                    i++;
                    if(i>20){
                        break;
                    }
                }
                
                notifyObj();
            }
        }
        
        runthread = Thread.currentThread();
        MyThread timeOutThread = new MyThread();
        timeOutThread.setDaemon(true);
        timeOutThread.start();
        mydo();
        timeOutThread.interrupt();
    }
    
    public void run(){
        try{
            new Thread(){
                public void run(){
                    try{
                        myrun();
                        notifyObj();
                    }catch(Exception exp){
                        notifyObj();
                        exp.printStackTrace();
                    }
                }
            }.start();
            synchronized(this){
                wait();
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}