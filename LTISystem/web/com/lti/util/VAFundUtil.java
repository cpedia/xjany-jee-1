/**
 * 
 */
package com.lti.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

import com.lti.service.SecurityManager;
import com.lti.service.bo.CompanyFund;
import com.lti.service.bo.VAFund;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

/**
 * @author Administrator
 *
 */
public class VAFundUtil {
	
	private static File indexDir = new File(Configuration.getVAFundCacheDir());
	//private static Analyzer luceneAnalyzer = new SimpleAnalyzer();
	private static Analyzer luceneAnalyzer = new StandardAnalyzer();
	private static IndexWriter indexWriter = null;
	private static SecurityManager securityManager = (SecurityManager) ContextHolder.getSecurityManager();
	
	private static Map<String, String> sortMap = new TreeMap<String, String>();
	private static List<String> keyList = new ArrayList<String>();
	
	/**
	 * 初始化字符串等价替换文件
	 * @param fileName
	 * @return 
	 * @throws IOException
	 */
	public static void initialMapFromSourceFile() throws IOException{
		if(sortMap == null || sortMap.size() == 0) {
			HashMap<String, String> equalMap = new HashMap<String, String>();
			File file = new File(Configuration.get13FDir(), Configuration.MATCHING_RULE_VAFUND_FILENAME);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String line = br.readLine();
			while(line != null && !line.equals("")){
				String[] pairs = line.trim().split(",");
				if(pairs.length == 2){
					equalMap.put(pairs[0].trim(), pairs[1].trim());
					keyList.add(pairs[0].trim());
				}
				line = br.readLine();
			}
			System.out.println(equalMap.size());
			Collections.sort(keyList, new Comparator<String>(){
					@Override
					public int compare(String o1, String o2) {
						int spaceNum1 = o1.split(" ").length;
						int spaceNum2 = o2.split(" ").length;
						return (spaceNum1 < spaceNum2 ? 1 : 0);
					}
				}
			);
			System.out.println(equalMap.size());
			if(equalMap != null && equalMap.size() > 0){
				for(int i=0; i<keyList.size(); i++){
					sortMap.put(keyList.get(i), equalMap.get(keyList.get(i)));
					//System.out.println(keyStrList.get(i) + "------" + equalMap.get(keyStrList.get(i)));
				}
			}
			fr.close();
			br.close();
		}
	}
	
	private static void start() throws Exception {
		boolean indexExist = IndexReader.indexExists(indexDir);
		if (indexExist) {
			indexWriter = new IndexWriter(indexDir, luceneAnalyzer, false);
		} else {
			indexWriter = new IndexWriter(indexDir, luceneAnalyzer, true);
		}
		indexWriter.setMergeFactor(2000);
		indexWriter.setMaxBufferedDocs(2000);
	}
	
	private static void commit() throws Exception {
		try {
			indexWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void optimize() {
		try {
			boolean indexExist = IndexReader.indexExists(indexDir);
			if (indexExist) {
				indexWriter = new IndexWriter(indexDir, luceneAnalyzer, false);
			} else {
				indexWriter = new IndexWriter(indexDir, luceneAnalyzer, true);
			}
			indexWriter.setMergeFactor(2000);
			indexWriter.setMaxBufferedDocs(2000);
			indexWriter.optimize();
			indexWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void create() throws Exception {
		indexWriter = new IndexWriter(indexDir, luceneAnalyzer, true);
		indexWriter.setMergeFactor(2000);
		indexWriter.setMaxBufferedDocs(2000);
	}
	
	public static void initialize() throws Exception {
		create();
		List<VAFund> vaFundList = securityManager.getVAFundList();
		index(vaFundList);
		commit();
	}
	
	private static void index(VAFund vaFund) throws Exception {
		Document document = null;
		document = new Document();
		document.add(new Field("ticker", vaFund.getTicker(), Field.Store.YES, Field.Index.UN_TOKENIZED));
		document.add(new Field("fundname", vaFund.getFundName(), Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("barronname", vaFund.getBarronName(), Field.Store.YES,Field.Index.TOKENIZED));
		document.add(new Field("fullname", vaFund.getFullName(), Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("assetname", vaFund.getAssetName(), Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("msvaname", vaFund.getMSVAName(), Field.Store.YES,Field.Index.TOKENIZED));
		indexWriter.addDocument(document);
	}
	
	public static void index(List<VAFund> vaFundList)throws Exception{
		for(int i=0;i<vaFundList.size();i++){
			index(vaFundList.get(i));
		}
	}
	
	public static VAFund find(String field, String keyword) {
		try {
			List<String[]> querys = new ArrayList<String[]>();
			keyword = keyword.replaceAll("\\(|\\)", " ");
			keyword = keyword.replaceAll("[' ']+", " ");
			String[] splits = keyword.trim().split(" ");
			for (int i = 0; i < splits.length; i++) {
				String s = splits[i];
				querys.add(new String[] {field, s});
			}
			List<VAFund> vaFundList = find(querys, 1);
			if(vaFundList != null && vaFundList.size() > 0){
				VAFund vaFund = vaFundList.get(0);
				System.out.println(vaFund);
				return vaFund;
			}
			return null;
		} catch (Exception e) {
			return new VAFund();
		}
	}
	
	public static List<VAFund> find(String field, String keyword, int size) {
		List<VAFund> vaFundList = new ArrayList<VAFund>();
		try {
			List<String[]> querys = new ArrayList<String[]>();
			keyword = keyword.replaceAll("\\(|\\)", " ");
			keyword = keyword.replaceAll("[' ']+", " ");
			String[] splits = keyword.trim().split(" ");
			for (int i = 0; i < splits.length; i++) {
				String s = splits[i];
				querys.add(new String[] {field, s});
			}
			vaFundList = find(querys, size);
			return vaFundList;
		} catch (Exception e) {
		}
		return vaFundList;
	}
	
	
	public static List<VAFund> find(List<String[]> querys, int size) throws Exception {
		if (!indexDir.exists()) {
			throw new Exception("The Lucene index is not exist");
		}
		String[] keywords = new String[querys.size()];
		String[] fields = new String[keywords.length];
		BooleanClause.Occur[] flags = new BooleanClause.Occur[keywords.length];
		for (int i = 0; i < keywords.length; i++) {
			keywords[i] = querys.get(i)[1];
			fields[i] = querys.get(i)[0];
			flags[i] = BooleanClause.Occur.SHOULD;
		}
		
		org.apache.lucene.search.Query q = MultiFieldQueryParser.parse(keywords, fields, flags, luceneAnalyzer);
		
		Hits hits = null;
		FSDirectory directory = FSDirectory.getDirectory(indexDir, false);
		IndexSearcher searcher = new IndexSearcher(directory);
		hits = searcher.search(q);
		List<VAFund> vaFundList = new ArrayList<VAFund>();
		for (int i = 0; i < hits.length(); i++) {
			Document document = hits.doc(i);
			VAFund vaFund = new VAFund();
			vaFund.setTicker(document.get("ticker"));
			vaFund.setBarronName(document.get("barronname"));
			vaFund.setAssetName(document.get("assetname"));
			vaFund.setFullName(document.get("fullname"));
			vaFund.setFundName(document.get("fundname"));
			vaFund.setMSVAName(document.get("msvaname"));
			vaFundList.add(vaFund);
			if (i >= size - 1)
				break;
		}
		return vaFundList;
	}
	
	/**
	 * 第一种搜索方式: 根据barronName进行完全匹配
	 * @param barronName
	 * @return
	 */
	public static VAFund BarronNameSearch(String barronName) {
		return securityManager.getVAFundByBarronName(barronName);
	}
	/**
	 * 第二种搜索方式：根据fullName在fullName中进行模糊搜索
	 * @param fullName
	 * @return
	 */
	public static VAFund fullNameSearch(String fullName) {
		return find("fullname", fullName);
	}
	/**
	 * 第二种搜索方式，搜索出20个结果
	 * @param fullName
	 * @param size
	 * @return
	 * @throws IOException 
	 */
	public static List<VAFund> fullNameSearch(String fullName, int size) throws IOException {
		return find("fullname", fullName, 20);
	}
	/**
	 * 第三种搜索方式：根据fullName在company fund 的索引表中进行模糊搜索，搜索出20个结果
	 * @param fullName
	 * @param size
	 * @return
	 * @throws IOException 
	 */
	public static List<VAFund> MSFullNameSearch(String fullName, int size) throws IOException {
		int halfSize = size / 2;
		List<CompanyFund> companyFundList = new ArrayList<CompanyFund>();
		List<VAFund> vaFundList = new ArrayList<VAFund>();
		companyFundList = CompanyFundCacheUtil.fullNameSearch(fullName, false, halfSize);
		for(CompanyFund companyFund : companyFundList) {
			VAFund vaFund = new VAFund();
			vaFund.setMSVAName(companyFund.getMSName());
			vaFund.setMSLink(companyFund.getMSLink());
			vaFundList.add(vaFund);
		}
		companyFundList = CompanyFundCacheUtil.fullNameSearch(fullName, true, halfSize);
		for(CompanyFund companyFund : companyFundList) {
			VAFund vaFund = new VAFund();
			vaFund.setTicker(companyFund.getTicker());
			vaFund.setFundName(companyFund.getTickerName());
			vaFund.setAssetName(companyFund.getAssetName());
			if(vaFund.getTicker().equals("NoTicker")){
				vaFund.setAssetName("NoResult");
				vaFund.setFundName("NoResult");
			}
			vaFund.setMSVAName(companyFund.getMSName());
			vaFund.setMSLink(companyFund.getMSLink());
			vaFundList.add(vaFund);
		}
		return vaFundList;
	}
	public static List<VAFund> MSFullNameSearchHasTicker(String fullName, int size) throws IOException {
		List<CompanyFund> companyFundList = new ArrayList<CompanyFund>();
		List<VAFund> vaFundList = new ArrayList<VAFund>();
		companyFundList = CompanyFundCacheUtil.fullNameSearch(fullName, true, size);
		for(CompanyFund companyFund : companyFundList) {
			VAFund vaFund = new VAFund();
			vaFund.setTicker(companyFund.getTicker());
			vaFund.setFundName(companyFund.getTickerName());
			vaFund.setAssetName(companyFund.getAssetName());
			vaFund.setMSVAName(companyFund.getMSName());
			vaFund.setMSLink(companyFund.getMSLink());
			vaFundList.add(vaFund);
		}
		return vaFundList;
	}
	/**
	 * 第三种搜索方式：根据fullName在company fund 的索引表中进行模糊搜索
	 * @param fullName
	 * @return
	 */
	public static VAFund MSFullNameSearch(String fullName) {
		CompanyFund companyFund1 = CompanyFundCacheUtil.fullNameSearch(fullName, false);
		CompanyFund companyFund2 = CompanyFundCacheUtil.fullNameSearch(fullName, true);
		VAFund vaFund = new VAFund();
		if(companyFund1 != null){
			vaFund.setMSVAName(companyFund1.getMSName());
		}else{
			vaFund.setMSVAName("NoResult");
		}
		if(companyFund2 != null){
			vaFund.setTicker(companyFund2.getTicker());
			vaFund.setAssetName(companyFund2.getAssetName());
			vaFund.setFundName(companyFund2.getTickerName());
			if(vaFund.getTicker().equals("NoTicker")){
				vaFund.setAssetName("NoResult");
				vaFund.setFundName("NoResult");
			}
			
		}
		return vaFund;
	}
	
	public static void setMSFullName() throws IOException {
		List<CompanyFund> companyFundList = securityManager.getCompanyFundList();
		if(companyFundList != null && companyFundList.size() > 0){
			for(CompanyFund cf: companyFundList){
				String msName = cf.getMSName();
				String msFullName = getFullName(msName);
				cf.setMSFullName(msFullName);
			}
		}
		securityManager.saveOrUpdateAllCompanyFund(companyFundList);
	}
	
	public static String getFullName(String barronName) throws IOException {
		String fullName = barronName;
		fullName = fullName.replaceAll(":", " ");
		if(sortMap == null || sortMap.size() ==0){
			initialMapFromSourceFile();
		}
		if(sortMap != null || sortMap.size() >0){
			for(String key : keyList){
				System.out.println(key);
				if(key.equals("Cap Appr")){
					System.out.println("here");
				}
				//fullName = fullName.replaceAll("[^\\w\\d\\s&/-]" + key + "\\b", sortMap.get(key));
				fullName = fullName.replaceAll("^" + key + "\\s|" + "\\s" + key + "\\s|" + "\\s" + key + "$" , " " + sortMap.get(key)+ " ").trim();
			}
		}
		return fullName;
	}
	
	public static VAFund vaFundSearch(String barronName) throws IOException {
		String fullName = getFullName(barronName);
		VAFund vaFund = BarronNameSearch(barronName);
		if(vaFund != null) {
			vaFund.setSearchType('A');
			return vaFund;
		}
		vaFund = fullNameSearch(fullName);
		if(vaFund != null) {
			vaFund.setSearchType('B');
			return vaFund;
		}
		vaFund = MSFullNameSearch(fullName);
		vaFund.setBarronName(barronName);
		vaFund.setFullName(fullName);
		vaFund.setSearchType('C');
		return vaFund;
	}
	
	public static void getBarronFullName() throws IOException {
		File inFile = new File("barronname.csv");
		File outFile = new File("barronname_fullname.csv");
		FileReader fr = new FileReader(inFile);
		BufferedReader br = new BufferedReader(fr);
		BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
		String line = br.readLine();
		while(line != null && !line.equals("")){
			String barronName = line.trim();
			String fullName = VAFundUtil.getFullName(barronName);
			if(!barronName.equals(fullName))
				System.out.println(barronName+"         "+fullName);
			bw.write(barronName+","+fullName+"\n");
			line = br.readLine();
		}
		bw.close();
		fr.close();
		br.close();
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		VAFundUtil.getBarronFullName();
//		String barronName = "Lincoln Jan Cap Appr St";
//		String fullName = VAFundUtil.getFullName(barronName);
//		System.out.println(fullName);
		//String key = "Cap Appr";
		//String fullName = barronName.replaceAll("^" + key + "\\s|" + "\\s" + key + "\\s|" + "\\s" + key + "$" , " Capital Appreciation ").trim();
		//System.out.println(fullName);
		//VAFundUtil.setMSFullName();
		return;
	}

}
