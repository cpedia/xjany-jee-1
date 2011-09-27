package com.lti.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.record.formula.functions.T;

import com.lti.service.bo.Transaction;
import com.lti.system.Configuration;

import junit.framework.TestCase;

public class TransactionUtilTest extends TestCase {

	public void testMergeTransactions1() {
		List<Transaction> trs1=new ArrayList<Transaction>();
		Transaction t1=new Transaction();
		t1.setOperation(Configuration.TRANSACTION_BUY);
		t1.setSecurityID(890l);
		t1.setAmount(200.0);
		t1.setDate(LTIDate.getDate(2009, 8, 1));
		trs1.add(t1);
		Transaction t2=t1.clone();
		t2.setAmount(300.0);
		trs1.add(t2);
		
		List<Transaction> trs2=TransactionUtil.MergeTransactions(trs1);
		assertEquals(trs2.size(), 1);
		assertEquals(trs2.get(0).getAmount(), 500.0);
		assertEquals(trs2.get(0).getOperation(), Configuration.TRANSACTION_BUY);
	}
	public void testMergeTransactions2() {
		List<Transaction> trs1=new ArrayList<Transaction>();
		Transaction t1=new Transaction();
		t1.setOperation(Configuration.TRANSACTION_BUY);
		t1.setSecurityID(890l);
		t1.setAmount(200.0);
		t1.setDate(LTIDate.getDate(2009, 8, 1));
		trs1.add(t1);
		Transaction t2=t1.clone();
		t2.setOperation(Configuration.TRANSACTION_SELL);
		t2.setAmount(150.0);
		trs1.add(t2);
		Transaction t3=t1.clone();
		t3.setAmount(300.0);
		trs1.add(t3);
		
		List<Transaction> trs2=TransactionUtil.MergeTransactions(trs1);
		assertEquals(trs2.size(), 1);
		assertEquals(trs2.get(0).getAmount(), 350.0);
		assertEquals(trs2.get(0).getOperation(), Configuration.TRANSACTION_BUY);
	}
	public void testMergeTransactions3() {
		List<Transaction> trs1=new ArrayList<Transaction>();
		Transaction t1=new Transaction();
		t1.setOperation(Configuration.TRANSACTION_BUY);
		t1.setSecurityID(890l);
		t1.setAmount(200.0);
		t1.setDate(LTIDate.getDate(2009, 8, 1));
		trs1.add(t1);
		Transaction t2=t1.clone();
		t2.setOperation(Configuration.TRANSACTION_SELL);
		t2.setAmount(150.0);
		trs1.add(t2);
		Transaction t3=t1.clone();
		t3.setAmount(300.0);
		trs1.add(t3);
		
		Transaction t4=t1.clone();
		t4.setDate(LTIDate.getDate(2009, 8, 2));
		trs1.add(t4);
		
		List<Transaction> trs2=TransactionUtil.MergeTransactions(trs1);
		assertEquals(trs2.size(), 2);
		assertEquals(trs2.get(0).getAmount(), 350.0);
		assertEquals(trs2.get(0).getOperation(), Configuration.TRANSACTION_BUY);
	}
	
	public static String[][] INPUT=new String[][]{
		
		//test WITHDRAW
		{"2007-01-01",Configuration.WITHDRAW,null,"200.0"},
		{"2007-01-01",Configuration.TRANSACTION_BUY,"90","250.0"},
		{"2007-01-01",Configuration.WITHDRAW,null,"100.0"},
		{"2007-01-01",Configuration.TRANSACTION_BUY,"90","450.0"},
		
		//test DEPOSIT
		{"2007-01-02",Configuration.DEPOSIT,null,"200.0"},
		{"2007-01-02",Configuration.TRANSACTION_BUY,"90","250.0"},
		{"2007-01-02",Configuration.DEPOSIT,null,"100.0"},
		{"2007-01-02",Configuration.TRANSACTION_BUY,"90","450.0"},
		
		//test Merge WITHDRAW & DEPOSIT
		{"2007-01-03",Configuration.WITHDRAW,null,"200.0"},
		{"2007-01-03",Configuration.TRANSACTION_BUY,"90","250.0"},
		{"2007-01-03",Configuration.DEPOSIT,null,"400.0"},
		{"2007-01-03",Configuration.TRANSACTION_BUY,"90","450.0"},
		
		//test Merge WITHDRAW & DEPOSIT
		{"2007-01-04",Configuration.WITHDRAW,null,"800.0"},
		{"2007-01-04",Configuration.TRANSACTION_BUY,"90","250.0"},
		{"2007-01-04",Configuration.DEPOSIT,null,"400.0"},
		{"2007-01-04",Configuration.TRANSACTION_BUY,"90","450.0"},
		
		//test Merge DEPOSIT & WITHDRAW
		{"2007-01-05",Configuration.DEPOSIT,null,"200.0"},
		{"2007-01-05",Configuration.TRANSACTION_BUY,"90","250.0"},
		{"2007-01-05",Configuration.WITHDRAW,null,"400.0"},
		{"2007-01-05",Configuration.TRANSACTION_BUY,"90","450.0"},	
		
		//test Merge DEPOSIT & WITHDRAW
		{"2007-01-06",Configuration.DEPOSIT,null,"800.0"},
		{"2007-01-06",Configuration.TRANSACTION_BUY,"90","250.0"},
		{"2007-01-06",Configuration.WITHDRAW,null,"400.0"},
		{"2007-01-06",Configuration.TRANSACTION_BUY,"90","450.0"},	
		
		
		//test BUY
		{"2007-01-07",Configuration.TRANSACTION_BUY,"90","100.0"},
		{"2007-01-07",Configuration.TRANSACTION_BUY,"90","200.0"},
		{"2007-01-07",Configuration.TRANSACTION_BUY,"90","300.0"},
		{"2007-01-07",Configuration.TRANSACTION_BUY,"91","450.0"},
		
		//test SELL
		{"2007-01-08",Configuration.TRANSACTION_SELL,"90","100.0"},
		{"2007-01-08",Configuration.TRANSACTION_SELL,"90","200.0"},
		{"2007-01-08",Configuration.TRANSACTION_SELL,"90","300.0"},
		{"2007-01-08",Configuration.TRANSACTION_SELL,"91","450.0"},
		{"2007-01-08",Configuration.TRANSACTION_SELL,"92","450.0"},
		
		//test Merge BUY & SELL
		{"2007-01-09",Configuration.TRANSACTION_BUY,"89","450.0"},
		{"2007-01-09",Configuration.TRANSACTION_BUY,"90","100.0"},
		{"2007-01-09",Configuration.TRANSACTION_BUY,"90","200.0"},
		{"2007-01-09",Configuration.TRANSACTION_BUY,"90","300.0"},
		{"2007-01-09",Configuration.TRANSACTION_SELL,"91","450.0"},
		{"2007-01-09",Configuration.TRANSACTION_SELL,"90","450.0"},
		{"2007-01-09",Configuration.TRANSACTION_SELL,"90","450.0"},
		
		//test Merge BUY & SELL
		{"2007-01-10",Configuration.TRANSACTION_BUY,"89","450.0"},
		{"2007-01-10",Configuration.TRANSACTION_BUY,"90","300.0"},
		{"2007-01-10",Configuration.TRANSACTION_BUY,"90","400.0"},
		{"2007-01-10",Configuration.TRANSACTION_BUY,"90","500.0"},
		{"2007-01-10",Configuration.TRANSACTION_SELL,"91","450.0"},
		{"2007-01-10",Configuration.TRANSACTION_SELL,"90","450.0"},
		{"2007-01-10",Configuration.TRANSACTION_SELL,"90","450.0"},
		
		//test Merge SELL & BUY
		{"2007-01-11",Configuration.TRANSACTION_SELL,"89","450.0"},
		{"2007-01-11",Configuration.TRANSACTION_SELL,"90","100.0"},
		{"2007-01-11",Configuration.TRANSACTION_SELL,"90","200.0"},
		{"2007-01-11",Configuration.TRANSACTION_SELL,"90","300.0"},
		{"2007-01-11",Configuration.TRANSACTION_BUY,"91","450.0"},
		{"2007-01-11",Configuration.TRANSACTION_BUY,"90","450.0"},
		{"2007-01-11",Configuration.TRANSACTION_BUY,"90","450.0"},
		
		//test Merge SELL &BUY
		{"2007-01-12",Configuration.TRANSACTION_SELL,"89","450.0"},
		{"2007-01-12",Configuration.TRANSACTION_SELL,"90","300.0"},
		{"2007-01-12",Configuration.TRANSACTION_SELL,"90","400.0"},
		{"2007-01-12",Configuration.TRANSACTION_SELL,"90","500.0"},
		{"2007-01-12",Configuration.TRANSACTION_BUY,"91","450.0"},
		{"2007-01-12",Configuration.TRANSACTION_BUY,"90","450.0"},
		{"2007-01-12",Configuration.TRANSACTION_BUY,"90","450.0"},
		
		//test ShortSell
		{"2007-01-13",Configuration.TRANSACTION_SHORT_SELL,"89","450.0"},
		{"2007-01-13",Configuration.TRANSACTION_SHORT_SELL,"90","300.0"},
		{"2007-01-13",Configuration.TRANSACTION_SHORT_SELL,"90","400.0"},
		{"2007-01-13",Configuration.TRANSACTION_SHORT_SELL,"90","500.0"},
		{"2007-01-13",Configuration.TRANSACTION_SHORT_SELL,"91","450.0"},
		{"2007-01-13",Configuration.TRANSACTION_SHORT_SELL,"90","450.0"},
		{"2007-01-13",Configuration.TRANSACTION_SHORT_SELL,"90","450.0"},	
			
	};
	
	public static String[][] OUTPUT=new String[][]{
		{"2007-01-01","WITHDRAW","null","300.0"},
		{"2007-01-01","BUY","90","700.0"},
		
		{"2007-01-02","DEPOSIT","null","300.0"},
		{"2007-01-02","BUY","90","700.0"},
		
		{"2007-01-03","DEPOSIT","null","200.0"},
		{"2007-01-03","BUY","90","700.0"},
		
		{"2007-01-04","WITHDRAW","null","400.0"},
		{"2007-01-04","BUY","90","700.0"},
		
		{"2007-01-05","WITHDRAW","null","200.0"},
		{"2007-01-05","BUY","90","700.0"},
		
		{"2007-01-06","DEPOSIT","null","400.0"},
		{"2007-01-06","BUY","90","700.0"},
		
		{"2007-01-07","BUY","90","600.0"},
		{"2007-01-07","BUY","91","450.0"},
		
		{"2007-01-08","SELL","90","600.0"},
		{"2007-01-08","SELL","91","450.0"},
		{"2007-01-08","SELL","92","450.0"},
		
		{"2007-01-09","BUY","89","450.0"},
		{"2007-01-09","SELL","91","450.0"},
		{"2007-01-09","SELL","90","300.0"},
		
		{"2007-01-10","BUY","89","450.0"},
		{"2007-01-10","BUY","90","300.0"},
		{"2007-01-10","SELL","91","450.0"},
		
		{"2007-01-11","SELL","89","450.0"},
		{"2007-01-11","BUY","91","450.0"},
		{"2007-01-11","BUY","90","300.0"},
		
		{"2007-01-12","SELL","89","450.0"},
		{"2007-01-12","SELL","90","300.0"},
		{"2007-01-12","BUY","91","450.0"},
		
		//test ShortSell
		{"2007-01-13",Configuration.TRANSACTION_SHORT_SELL,"89","450.0"},
		{"2007-01-13",Configuration.TRANSACTION_SHORT_SELL,"90","2100.0"},
		{"2007-01-13",Configuration.TRANSACTION_SHORT_SELL,"91","450.0"},
	};


	
	public static List<Transaction> getTransaction(String[][] strss){
		List<Transaction> trs=new ArrayList<Transaction>();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		for(int i=0;i<strss.length;i++){
			try {
				String[] strs=strss[i];
				Transaction t=new Transaction();
				t.setDate(df.parse(strs[0]));
				t.setOperation(strs[1]);
				try {
					t.setSecurityID(Long.parseLong(strs[2]));
				} catch (Exception e) {
				}
				t.setAmount(Double.parseDouble(strs[3]));
				trs.add(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return trs;
	}
	
	public static void main(String[] args)throws Exception{
		List<Transaction> trs1=getTransaction(INPUT);
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		List<Transaction> trs2=TransactionUtil.MergeTransactions(trs1);
		TransactionUtil.compareTransactions(trs1, trs2);
		System.out.println("public static String[][] OUTPUT=new String[][]{");
		for(int i=0;i<trs2.size();i++){
			Transaction t=trs2.get(i);
			//{"2007-01-12",Configuration.TRANSACTION_BUY,"90","450.0"},
			StringBuffer sb=new StringBuffer();
			sb.append("{\"");
			sb.append(df.format(t.getDate()));
			sb.append("\",");
			sb.append("\"");
			sb.append(t.getOperation());
			sb.append("\",");
			sb.append("\"");
			sb.append(String.valueOf(t.getSecurityID()));
			sb.append("\",");
			sb.append("\"");
			sb.append(t.getAmount());
			sb.append("\"},");
			System.out.println(sb.toString());
		}
		System.out.println("};");
	}
	
	public void testMergeTransactions4() {
		List<Transaction> in=getTransaction(INPUT);
		List<Transaction> out=getTransaction(OUTPUT);
		List<Transaction> merge=TransactionUtil.MergeTransactions(in);
		for(int i=0;i<out.size();i++){
			Transaction t1=out.get(i);
			Transaction t2=merge.get(i);
			assertEquals(t1.getAmount(), t2.getAmount());
			assertEquals(t1.getOperation(), t2.getOperation());
			assertEquals(t1.getDate(), t2.getDate());
			assertEquals(t1.getSecurityID(), t2.getSecurityID());
		}
	}

}

















