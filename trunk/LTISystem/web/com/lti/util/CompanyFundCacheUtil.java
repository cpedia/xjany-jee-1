/**
 * 
 */
package com.lti.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

import com.lti.service.AssetClassManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.CompanyFund;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.VAFund;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

/**
 * @author Administrator
 *
 */
public class CompanyFundCacheUtil {
	
	private static File indexDir = new File(Configuration.getCompanyFundCacheDir());
	//private static Analyzer luceneAnalyzer = new SimpleAnalyzer();
	private static Analyzer luceneAnalyzer = new StandardAnalyzer();
	private static IndexWriter indexWriter = null;
	private static SecurityManager securityManager = (SecurityManager) ContextHolder.getSecurityManager();
	private static AssetClassManager assetClassManager = (AssetClassManager) ContextHolder.getAssetClassManager();
	
	
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
	
	
	public static void initialize() throws Exception {
		create();
		List<CompanyFund> companyFundList = securityManager.getCompanyFundList();
		index(companyFundList);
		commit();
	}
	
	private static void create() throws Exception {
		indexWriter = new IndexWriter(indexDir, luceneAnalyzer, true);
		indexWriter.setMergeFactor(2000);
		indexWriter.setMaxBufferedDocs(2000);
	}
	
	private static void index(CompanyFund companyFund) throws Exception {
		Document document = null;
		document = new Document();
		document.add(new Field("ticker", companyFund.getTicker(), Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("msname", companyFund.getMSName(), Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("tickername", companyFund.getTickerName(), Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("assetname", companyFund.getAssetName(), Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("msfullname", companyFund.getMSFullName(), Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("mslink", companyFund.getMSLink(), Field.Store.YES, Field.Index.TOKENIZED));
		indexWriter.addDocument(document);
	}
	
	public static void index(List<CompanyFund> companyFundList)throws Exception{
		for(int i=0;i<companyFundList.size();i++){
			index(companyFundList.get(i));
		}
	}
	
	public static CompanyFund find(String field, String keyword, boolean hasTicker) {
		try {
			List<String[]> querys = new ArrayList<String[]>();
			keyword = keyword.replaceAll("\\(|\\)", " ");
			keyword = keyword.replaceAll("[' ']+", " ");
			String[] splits = keyword.trim().split(" ");
			for (int i = 0; i < splits.length; i++) {
				String s = splits[i];
				querys.add(new String[] {field, s});
			}
			
			List<CompanyFund> companyFundList = CompanyFundCacheUtil.find(querys, 1, hasTicker);
			if(companyFundList != null && companyFundList.size() > 0){
				CompanyFund companyFund = companyFundList.get(0);
				System.out.println(companyFund);
				return companyFund;
			}
			return null;
		} catch (Exception e) {
			return new CompanyFund();
		}
	}
	
	public static List<CompanyFund> find(String field, String keyword, boolean hasTicker, int size) {
		List<CompanyFund> companyFundList = new ArrayList<CompanyFund>();
		try {
			List<String[]> querys = new ArrayList<String[]>();
			keyword = keyword.replaceAll("\\(|\\)", " ");
			keyword = keyword.replaceAll("[' ']+", " ");
			String[] splits = keyword.trim().split(" ");
			for (int i = 0; i < splits.length; i++) {
				String s = splits[i];
				querys.add(new String[] {field, s});
			}
			companyFundList = CompanyFundCacheUtil.find(querys, size, hasTicker);
		} catch (Exception e) {
		}
		return companyFundList;
	}
	
	
	public static List<CompanyFund> find(List<String[]> querys, int size, boolean hasTicker) throws Exception {
		if (!indexDir.exists()) {
			throw new Exception("The Lucene index is not exist");
		}
		String[] keywords = new String[querys.size() + 1];
		String[] fields = new String[keywords.length];
		BooleanClause.Occur[] flags = new BooleanClause.Occur[keywords.length];
		for (int i = 0; i < querys.size(); i++) {
			keywords[i] = querys.get(i)[1];
			fields[i] = querys.get(i)[0];
			flags[i] = BooleanClause.Occur.SHOULD;
		}
		keywords[querys.size()] = "NoTicker";
		fields[querys.size()] = "ticker";
		if(hasTicker)
			flags[querys.size()] = BooleanClause.Occur.MUST_NOT;
		else
			flags[querys.size()] = BooleanClause.Occur.MUST;
		org.apache.lucene.search.Query q = MultiFieldQueryParser.parse(keywords, fields, flags, luceneAnalyzer);
		
		Hits hits = null;
		FSDirectory directory = FSDirectory.getDirectory(indexDir, false);
		IndexSearcher searcher = new IndexSearcher(directory);
		hits = searcher.search(q);
		List<CompanyFund> companyFundList = new ArrayList<CompanyFund>();
		for (int i = 0; i < hits.length(); i++) {
			Document document = hits.doc(i);
			CompanyFund companyFund = new CompanyFund();
			companyFund.setTicker(document.get("ticker"));
			companyFund.setMSName(document.get("msname"));
			companyFund.setAssetName(document.get("assetname"));
			companyFund.setTickerName(document.get("tickername"));
			companyFund.setMSFullName(document.get("msfullname"));
			companyFund.setMSLink(document.get("mslink"));
			companyFundList.add(companyFund);
			if (i >= size - 1)
				break;
		}
		return companyFundList;
	}
	
	public static CompanyFund fullNameSearch(String fullName, boolean hasTicker) {
		return CompanyFundCacheUtil.find("msfullname", fullName, hasTicker);
	}
	
	public static List<CompanyFund> fullNameSearch(String fullName, boolean hasTicker, int size) {
		return CompanyFundCacheUtil.find("msfullname", fullName, hasTicker, size);
	}
	
	public static void setTickerInformation() {
		List<CompanyFund> companyFundList = securityManager.getCompanyFundList();
		List<CompanyFund> newList = new ArrayList<CompanyFund>();
		if(companyFundList != null && companyFundList.size() > 0){
			for(CompanyFund cf: companyFundList) {
				if(!cf.getTicker().equals("NoTicker")){
					Security se = securityManager.getBySymbol(cf.getTicker());
					if( se != null){
						cf.setTickerName(se.getName());
						cf.setStartDate(se.getStartDate());
						if(se.getClassID() != null) {
							AssetClass ac = assetClassManager.get(se.getClassID());
							if(ac != null) {
								cf.setAssetName( ac.getName());
							}
						}
						newList.add(cf);
					}
				}
			}
			securityManager.saveOrUpdateAllCompanyFund(newList);
		}
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		//CompanyFundCacheUtil.setTickerInformation();
		CompanyFundCacheUtil.initialize();
		return;
	}

}
