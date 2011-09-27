package com.lti.edgar;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class EdgarUtilTest extends TestCase {

	public void testDownloadFile() {
		try {
			//EdgarUtil.downloadFile("http://www.baidu.com/", "c:\\a\\b\\c\\t.txt");
			//EdgarUtil.downloadFile("http://www.baidu.com/", "c:/a/b/c/d/t.txt");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}



	public void testGetSymbolByFidelity() {
		try {
			//assertEquals(EdgarUtil.getSymbolByFidelity("001084102"), "AGCO");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testGetSymbolByNameOfIssuer() {
		try {
			//assertEquals(EdgarUtil.getSymbolByNameOfIssuer("AGCO Corp (AG)"), "AG");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testGetCUSIP() {
		try {
			//assertEquals(EdgarUtil.getCUSIP("001084102","AGCO Corp (AG)").getSymbol(), "AGCO");
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testConvertToFile() {
		fail("Not yet implemented");
	}

	public void testConvertToOFXStringListOfCompanyIndex() {
		fail("Not yet implemented");
	}

	public void testConvertToOFXStringCompanyIndex() {
		fail("Not yet implemented");
	}

	public void testUpdateCompanyIndexDateBase() {
		try {
			EdgarUtil.updateCompanyIndexDateBase();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}

	public void testUpdateOFX() {
		fail("Not yet implemented");
	}
	
	public void testContainQuarter(){
		List<String> names=new ArrayList<String>();
		names.add("C:\\Documents and Settings\\koko\\workspace\\ParseEdgar\\WALL_STREET_ASSOCIATES.20020930.20021010.xml");
		names.add("C:\\Documents and Settings\\koko\\workspace\\ParseEdgar\\WALL_STREET_ASSOCIATES.20031231.20040211.xml");
		assertEquals(EdgarUtil.containQuarter(names, 2004, 3), false);
		assertEquals(EdgarUtil.containQuarter(names, 2002, 3), true);
		assertEquals(EdgarUtil.containQuarter(names, 2003, 4), true);
	}
}
