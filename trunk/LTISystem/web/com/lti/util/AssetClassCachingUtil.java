package com.lti.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

import com.lti.service.AssetClassManager;
import com.lti.service.bo.AssetClass;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

public class AssetClassCachingUtil {

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

	private static File indexDir = new File(Configuration.getAssetClassCacheDir());
	private static Analyzer luceneAnalyzer = new SimpleAnalyzer();
	private static IndexWriter indexWriter = null;


	private static void index(AssetClass ac) throws Exception {

		Document document = null;
		document = new Document();
		document.add(new Field("id", ac.getID()+"", Field.Store.YES,Field.Index.TOKENIZED));
		document.add(new Field("name", ac.getName(), Field.Store.YES,Field.Index.TOKENIZED));
		document.add(new Field("benchmarkid", ac.getBenchmarkID()+"", Field.Store.YES,	Field.Index.TOKENIZED));
		indexWriter.addDocument(document);
	}
	
	private static void create() throws Exception {
		indexWriter = new IndexWriter(indexDir, luceneAnalyzer, true);
		indexWriter.setMergeFactor(2000);
		indexWriter.setMaxBufferedDocs(2000);
	}
	
	public static void initialize() throws Exception{
		create();
		AssetClassManager acm = (AssetClassManager) ContextHolder.getInstance().getApplicationContext().getBean("assetClassManager");
		List<AssetClass> assetClassList = acm.getClasses();
		index(assetClassList);
		commit();
	}
	
	private static void index(List<AssetClass> acs)throws Exception{
		for(int i=0;i<acs.size();i++){
			index(acs.get(i));
		}
	}

	public static AssetClass find(String keyword){
		try {
			List<String[]> querys=new ArrayList<String[]>();
			String[] splits=keyword.trim().split(" ");
			for(int i=0;i<splits.length;i++){
				String s=splits[i];
				querys.add(new String[]{"name",s});
			}
			List<AssetClass> acs=AssetClassCachingUtil.find(querys,1);
			return acs.get(0);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static List<AssetClass> find(List<String[]> querys,int size)
			throws Exception {
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

		org.apache.lucene.search.Query q = MultiFieldQueryParser.parse(	keywords, fields, flags, luceneAnalyzer);

		Hits hits = null;
		FSDirectory directory = FSDirectory.getDirectory(indexDir, false);
		IndexSearcher searcher = new IndexSearcher(directory);
		hits = searcher.search(q);
		List<AssetClass> acs = new ArrayList<AssetClass>();
		for (int i = 0; i < hits.length(); i++) {
			Document document = hits.doc(i);
			AssetClass ac=new AssetClass();
			ac.setID(Long.parseLong(document.get("id")));
			ac.setBenchmarkID(Long.parseLong(document.get("benchmarkid")));
			ac.setName(document.get("name"));
			acs.add(ac);
			if(i>=size-1)break;
		}
		return acs;
	}

	public static void main(String[] args) throws Exception {
		//AssetClassManager acm=ContextHolder.getAssetClassManager();
		//List<AssetClass> acs=acm.getClasses();
		//AssetClassCachingUtil.index(acs);
		AssetClassCachingUtil.initialize();
		String keyword="HEDGES Value";
		List<String[]> querys=new ArrayList<String[]>();
		String[] splits=keyword.trim().split(" ");
		for(int i=0;i<splits.length;i++){
			String s=splits[i];
			querys.add(new String[]{"name",s});
		}
		List<AssetClass> acs=AssetClassCachingUtil.find(querys,1);
		for(int i=0;i<acs.size();i++){
			System.out.println(acs.get(i).getName());
		}
	}
}
