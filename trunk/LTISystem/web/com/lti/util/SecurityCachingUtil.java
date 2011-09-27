package com.lti.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;

import com.lti.service.SecurityManager;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class SecurityCachingUtil {

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

	private static File indexDir = new File(Configuration.getSecurityCacheDir());
	private static Analyzer luceneAnalyzer = new SimpleAnalyzer();
	private static IndexWriter indexWriter = null;

	private static void index(Security se) throws Exception {

		if(se.getName() == null || se.getClassID() == null)
			return;
		Document document = null;
		document = new Document();
		document.add(new Field("id", se.getID() + "", Field.Store.YES,
				Field.Index.TOKENIZED));
		document.add(new Field("name", se.getName(), Field.Store.YES,
				Field.Index.TOKENIZED));
		document.add(new Field("symbol", se.getSymbol(), Field.Store.YES,
				Field.Index.TOKENIZED));
		document.add(new Field("classid", se.getClassID() + "",
				Field.Store.YES, Field.Index.TOKENIZED));
		String securityType = null;
		if(se != null && se.getSecurityType() != null)
			securityType = Configuration.getSecurityTypeName(se.getSecurityType());
		else
			securityType = Configuration.UNDEFINED;
		document.add(new Field("securitytype", securityType, Field.Store.YES, Field.Index.TOKENIZED));
		indexWriter.addDocument(document);
	}

	private static void index(List<Security> securityList) throws Exception {
		for(Security se: securityList)
			index(se);
	}

	private static void create() throws Exception {
		indexWriter = new IndexWriter(indexDir, luceneAnalyzer, true);
		indexWriter.setMergeFactor(2000);
		indexWriter.setMaxBufferedDocs(2000);
	}
	
	public static void intialize() throws Exception {
		create();
		SecurityManager sm = (SecurityManager) ContextHolder.getInstance().getApplicationContext().getBean("securityManager");
		List<Security> securityList = sm.getSecurities();
		index(securityList);
		commit();
	}
	
	public static Security find(String keyword) {
		try {
			List<String[]> querys = new ArrayList<String[]>();
			String[] splits = keyword.trim().split(" ");
			for (int i = 0; i < splits.length; i++) {
				String s = splits[i];
				querys.add(new String[] { "name", s });
			}
			List<Security> acs = SecurityCachingUtil.find(querys, 1);
			return acs.get(0);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static List<Security> find(String keyword, int size){
		return find(keyword, size, false);
	}

	public static List<Security> find(String keyword,int size, boolean isSymbol) {
		try {
			List<String[]> querys = new ArrayList<String[]>();
			String[] splits = keyword.trim().split(" ");
			for (int i = 0; i < splits.length; i++) {
				String s = splits[i];
				querys.add(new String[] { "name", s });
			}
			List<Security> acs = SecurityCachingUtil.find(querys, size, isSymbol);
			return acs;
		} catch (Exception e) {
			return null;
		}
	}

	
	public static List<Security> find(List<String[]> querys, int size) throws Exception{
		return find(querys, size, false);
	}
	
	public static List<Security> find(List<String[]> querys, int size, boolean isSymbol)
			throws Exception {
		if (!indexDir.exists()) {
			throw new Exception("The Lucene index is not exist");
		}
		int num = querys.size();
		if(!isSymbol) ++num;
		
		String[] keywords = new String[num];
		String[] fields = new String[num];
		BooleanClause.Occur[] flags = new BooleanClause.Occur[num];
		for (int i = 0; i < querys.size(); i++) {
			keywords[i] = querys.get(i)[1];
			fields[i] = querys.get(i)[0];
			flags[i] = BooleanClause.Occur.SHOULD;
		}
		if(!isSymbol){
			keywords[querys.size()] = "CEF";
			fields[querys.size()] = "securitytype";
			flags[querys.size()] = BooleanClause.Occur.MUST_NOT;
		}

		org.apache.lucene.search.Query q = MultiFieldQueryParser.parse(
				keywords, fields, flags, luceneAnalyzer);

		Hits hits = null;
		FSDirectory directory = FSDirectory.getDirectory(indexDir, false);
		IndexSearcher searcher = new IndexSearcher(directory);
		hits = searcher.search(q);
		List<Security> acs = new ArrayList<Security>();
		for (int i = 0; i < hits.length(); i++) {
			Document document = hits.doc(i);
			Security ac = new Security();
			ac.setID(Long.parseLong(document.get("id")));
			try {
				ac.setClassID(Long.parseLong(document.get("classid")));
			} catch (Exception e) {
			}
			ac.setName(document.get("name"));
			ac.setSymbol(document.get("symbol"));
			acs.add(ac);
			if (i >= size - 1)
				break;
		}
		return acs;
	}

	public static void main(String[] args) throws Exception {
		SecurityCachingUtil.intialize();
		// AssetClassManager acm=ContextHolder.getAssetClassManager();
		// List<AssetClass> acs=acm.getClasses();
		// AssetClassCachingUtil.index(acs);
		// SecurityCachingUtil.index(ContextHolder.getSecurityManager().getSecurities());
		String keyword = "OCC";
		List<String[]> querys = new ArrayList<String[]>();
		String[] splits = keyword.trim().split(" ");
		for (int i = 0; i < splits.length; i++) {
			String s = splits[i];
			querys.add(new String[] { "name", s });
		}
		List<Security> acs = SecurityCachingUtil.find(querys, 5);
		for (int i = 0; i < acs.size(); i++) {
			System.out.println(acs.get(i).getName());
			System.out.println(acs.get(i).getSymbol());
		}
	}
}
