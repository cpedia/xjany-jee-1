package com.lti.cache;

import java.util.Date;

public class LTICache {
	public final static int Max_Count = 200;
	public final static int Time_Out = 3600 * 1000;
	public final static int Busy_Time = 5*60 * 1000;
	public final static int Sleep_Time= 5*60 *1000;
	
	
	private static LTICache securityDailyDataCache = new LTICache();
	private static LTICache newsCache = new LTICache();
	private boolean isDebug = false;

	private long[] putTimes = new long[Max_Count];
	private long previosPutTime ;
	private long totalLoadCount = 0;

	public LTICache() {
		previosPutTime=System.currentTimeMillis()-Busy_Time;
		putTimes[0]=System.currentTimeMillis();
	}

	private Object[][] cache = new Object[Max_Count][3];

	private int curpos = 0;

	public void put(Object key, Object value) {
		synchronized(this){
			if (key == null || value == null)
				throw new RuntimeException("Key or value is null.");

			remove(key);

			
			cache[curpos][0] = key;
			cache[curpos][1] = value;
			cache[curpos][2] = System.currentTimeMillis();
			
			curpos = (curpos + 1) % Max_Count;
			
			previosPutTime=putTimes[curpos];
			putTimes[curpos ]=System.currentTimeMillis();
			
			totalLoadCount++;
			if (isDebug) {
				System.out.println("[" + new Date() + "]Put for key:" + key);
				System.out.println("Historical load items:" + totalLoadCount);
			}
		}
		
	}

	public boolean isBusy() {
		if (putTimes[curpos]-previosPutTime < Busy_Time) {
			if (System.currentTimeMillis() - previosPutTime < Sleep_Time) {
				if (isDebug)System.out.println("[" + new Date() + "]True");
				return true;
			}
		}
		if (isDebug)System.out.println("[" + new Date() + "]False");
		return false;
	}

	public void remove(Object key) {
		if (key == null)
			throw new RuntimeException("Key is null.");
		for (int i = 0; i < Max_Count; i++) {
			if (key.equals(cache[i][0])) {
				if (isDebug)
					System.out.println("Remove for key:" + key);
				cache[i][0] = null;
				cache[i][1] = null;
				cache[i][2] = null;
				break;
			}
		}
	}

	public  void removeAll() {
		synchronized(this){
			for (int i = 0; i < Max_Count; i++) {
				cache[i][0] = null;
				cache[i][1] = null;
				cache[i][2] = null;
			}
			previosPutTime=System.currentTimeMillis()-Busy_Time;
			putTimes[0]=System.currentTimeMillis();
			curpos = 0;
			System.gc();
		}
		
	}

	public Object get(Object key) {
		if (key == null)
			throw new RuntimeException("Key is null.");
		for (int i = 0; i < Max_Count; i++) {
			if (key.equals(cache[i][0])) {
				long t = (Long) cache[i][2];
				if (System.currentTimeMillis() - t > Time_Out) {
					if (isDebug)
						System.out.println("Time out for key:" + key);
					cache[i][0] = null;
					cache[i][1] = null;
					cache[i][2] = null;
					break;
				}

				return cache[i][1];
			}
		}
		return null;
	}

	public static LTICache getSecurityDailyDataCache() {
		return securityDailyDataCache;
	}

	public static LTICache getNewsCache() {
		return newsCache;
	}

}
