package com.lti.util;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;

import com.lti.Exception.VfofxHasBeenUpdatedException;
import com.lti.system.Configuration;

import junit.framework.TestCase;

public class OFXUtilTest extends TestCase {

	public void testGetZipEntry() throws Exception{
		String[] tests={
			"WALL_STREET_ASSOCIATES.19991231.20000211.xml",
			"WALL_STREET_ASSOCIATES.20000331.20000512.xml",
			"WALL_STREET_ASSOCIATES.20000731.20000814.xml",
			"WALL_STREET_ASSOCIATES.20000930.20001114.xml",
			"WALL_STREET_ASSOCIATES.20001231.20010208.xml",
			"WALL_STREET_ASSOCIATES.20010331.20010514.xml",
			"WALL_STREET_ASSOCIATES.20010630.20010725.xml",
			"WALL_STREET_ASSOCIATES.20010930.20011030.xml",
			"WALL_STREET_ASSOCIATES.20011231.20020215.xml",
			"WALL_STREET_ASSOCIATES.20020331.20020418.xml",
			"WALL_STREET_ASSOCIATES.20020603.20020726.xml",
			"WALL_STREET_ASSOCIATES.20020930.20021010.xml",
			"WALL_STREET_ASSOCIATES.20021231.20030212.xml",
			"WALL_STREET_ASSOCIATES.20030331.20030502.xml",
			"WALL_STREET_ASSOCIATES.20030630.20030805.xml",
			"WALL_STREET_ASSOCIATES.20030930.20031105.xml",
			"WALL_STREET_ASSOCIATES.20031231.20040211.xml",
			"WALL_STREET_ASSOCIATES.20040430.20040512.xml",
			"WALL_STREET_ASSOCIATES.20040630.20040810.xml",
			"WALL_STREET_ASSOCIATES.20040930.20041115.xml",
			"WALL_STREET_ASSOCIATES.20041231.20050210.xml",
			"WALL_STREET_ASSOCIATES.20050331.20050517.xml",
			"WALL_STREET_ASSOCIATES.20050630.20050812.xml",
			"WALL_STREET_ASSOCIATES.20050930.20051109.xml",
			"WALL_STREET_ASSOCIATES.20051231.20060208.xml",
			"WALL_STREET_ASSOCIATES.20060331.20060511.xml",
			"WALL_STREET_ASSOCIATES.20060630.20060810.xml",
			"WALL_STREET_ASSOCIATES.20060930.20061114.xml",
			"WALL_STREET_ASSOCIATES.20061231.20070213.xml",
			"WALL_STREET_ASSOCIATES.20070331.20070412.xml",
			"WALL_STREET_ASSOCIATES.20070630.20070814.xml",
			"WALL_STREET_ASSOCIATES.20070930.20071113.xml",
			"WALL_STREET_ASSOCIATES.20071231.20080212.xml",
			"WALL_STREET_ASSOCIATES.20080331.20080514.xml",
			"WALL_STREET_ASSOCIATES.20080630.20080814.xml",
			
			"WALL_STREET_ASSOCIATES.19990630.19990816.xml",
			"WALL_STREET_ASSOCIATES.19990930.19991109.xml",
			"WALL_STREET_ASSOCIATES.20080930.20081113.xml",
			"WALL_STREET_ASSOCIATES.20081231.20090212.xml",
			"WALL_STREET_ASSOCIATES.20090331.20090514.xml"
		};
		
		int length=tests.length;
		
		List<String> names=new ArrayList<String>();
		for(int i=0;i<length-2;i++){
			names.add(tests[i]);
		}
		
		List<Date> hitDates=new ArrayList<Date>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
		for(int i=0;i<length-2;i++){
			String[] strs=tests[i].split("\\.");
			try {
				hitDates.add(sdf.parse(strs[2]));
			} catch (ParseException e) {
				throw new RuntimeException("Parse Error.",e);
			}
		}
		
		Date updateDate1=LTIDate.getDate(2009, 2, 27);
		Date updateDate2=LTIDate.getDate(2009,6, 1);
		
		
		List<Date> tradingDates=LTIDate.getTradingDates(LTIDate.getDate(1999, 8, 16), new Date());
		Date lastUpdateDate=null;
		for(int i=0;i<tradingDates.size();i++){
			Date CurrentDate=tradingDates.get(i);
			System.out.println(CurrentDate);
			
			if(CurrentDate.getTime()<=hitDates.get(length-3).getTime()){
				String name=OFXUtil.getZipEntryName(names, CurrentDate, lastUpdateDate, true);
				if(hitDates.contains(CurrentDate)){
					assertNotNull(name);
					String[] strs=name.split("\\.");
					assertEquals(strs[2], sdf.format(CurrentDate));
					lastUpdateDate=CurrentDate;
					System.out.println(name);
				}else{
					assertNull(name);
				}
			}else if(CurrentDate.getTime()<updateDate1.getTime()){
				String name=OFXUtil.getZipEntryName(names, CurrentDate, lastUpdateDate, true);
				assertNull(name);
			}else if(CurrentDate.getTime()==updateDate1.getTime()){
				names.add(tests[length-2]);
				boolean exception=false;
				try {
					String name=OFXUtil.getZipEntryName(names, CurrentDate, lastUpdateDate, true);
				} catch (VfofxHasBeenUpdatedException e) {
					System.out.println("back");
					exception=true;
				}
				assertTrue(exception);
				lastUpdateDate=LTIDate.getDate(2009, 02, 12);
				
			}else if(CurrentDate.getTime()<updateDate2.getTime()){
				String name=OFXUtil.getZipEntryName(names, CurrentDate, lastUpdateDate, true);
				assertNull(name);
			}else if(CurrentDate.getTime()==updateDate2.getTime()){
				names.add(tests[length-1]);
				boolean exception=false;
				try {
					String name=OFXUtil.getZipEntryName(names, CurrentDate, lastUpdateDate, true);
				} catch (VfofxHasBeenUpdatedException e) {
					System.out.println("back");
					exception=true;
				}
				assertTrue(exception);
				lastUpdateDate=LTIDate.getDate(2009, 05, 14);
			}else{
				String name=OFXUtil.getZipEntryName(names, CurrentDate, lastUpdateDate, true);
				assertNull(name);
			}
			
		
		}
	}

}
