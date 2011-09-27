/**
 * 
 */
package com.lti.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

import com.lti.service.StrategyManager;
import com.lti.service.SecurityManager;
import com.lti.service.bo.AssetClass;
import com.lti.service.bo.Security;
import com.lti.service.bo.Strategy;
import com.lti.service.bo.VariableFor401k;
import com.lti.system.Configuration;
import com.lti.system.ContextHolder;

/**
 * @author CCD
 *
 */
public class FundTableCachingUtil {

	
	private static File indexDir = new File(Configuration.getFundTableCacheDir());
	//private static Analyzer luceneAnalyzer = new SimpleAnalyzer();
	private static Analyzer luceneAnalyzer = new StandardAnalyzer();
	private static IndexWriter indexWriter = null;
	private static SecurityManager securityManager = (SecurityManager) ContextHolder.getSecurityManager();
	
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
	
	/**
	 * 先将所有plan的type的索引标记去掉
	 * @param planIDList
	 * @throws Exception
	 */
	public static void initialize(List<Long> planIDList) throws Exception{
		create();
		StrategyManager strategyManager = (StrategyManager) ContextHolder.getInstance().getApplicationContext().getBean("strategyManager");
		List<Strategy> planList = strategyManager.getStrategiesByType(Configuration.STRATEGY_TYPE_401K);
		for(Strategy s: planList){
			s.setIndexed(false);
		}
		strategyManager.updateStrategyType(planList);
		addIndexByPlanID(planIDList);
	}
	
	public static void addIndexByPlanID(List<Long> planIDList) throws Exception{
		StrategyManager strategyManager = (StrategyManager) ContextHolder.getInstance().getApplicationContext().getBean("strategyManager");
		List<VariableFor401k>  variableList = strategyManager.getVariable401Ks(planIDList);
		List<Strategy> planList = strategyManager.getStrategyByIDs(planIDList);
		for(Strategy s: planList){
			s.setIndexed(true);
		}
		strategyManager.updateStrategyType(planList);
		if(variableList != null && variableList.size() > 0){
			index(variableList);
		}
		commit();
	}
	
	/**
	 * 同时要更新strategy的type,将对应位置为0
	 * @param planIDList
	 * @throws Exception
	 */
	public static void deleteIndexByPlanID(List<Long> planIDList) throws Exception{
		StrategyManager strategyManager = (StrategyManager) ContextHolder.getStrategyManager();
		start();
		if(planIDList != null && planIDList.size() > 0){
			List<Strategy> planList = strategyManager.getStrategyByIDs(planIDList);
			String[] keywords = new String[planIDList.size()];
			String[] fields = new String[planIDList.size()];
			BooleanClause.Occur[] flags = new BooleanClause.Occur[planIDList.size()];
			for (int i = 0; i < keywords.length; i++) {
				keywords[i] = planIDList.get(i).toString();
				fields[i] = "planid";
				flags[i] = BooleanClause.Occur.SHOULD;
			}
			for(Strategy s: planList){
				s.setIndexed(false);
			}
			strategyManager.updateStrategyType(planList);
			org.apache.lucene.search.Query q = MultiFieldQueryParser.parse(keywords, fields, flags, luceneAnalyzer);
			indexWriter.deleteDocuments(q);
		}
		commit();
		optimize();
	}
	
	private static void index(VariableFor401k variable) throws Exception {
		Document document = null;
		document = new Document();
		String securityType;
		if(variable.getSymbol() == null)
			return;
		Security se = securityManager.getBySymbol(variable.getSymbol());
		if(se != null && se.getSecurityType() != null)
			securityType = Configuration.getSecurityTypeName(se.getSecurityType());
		else
			securityType = Configuration.UNDEFINED;
		document.add(new Field("planid", variable.getStrategyID()+"", Field.Store.YES, Field.Index.UN_TOKENIZED));
		document.add(new Field("symbol", variable.getSymbol(), Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("name", variable.getName(), Field.Store.YES,Field.Index.TOKENIZED));
		document.add(new Field("securitytype", securityType, Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("assetclassname", variable.getAssetClassName(), Field.Store.YES, Field.Index.TOKENIZED));
		document.add(new Field("description", variable.getDescription(), Field.Store.YES,Field.Index.TOKENIZED));
		indexWriter.addDocument(document);
	}
	
	public static void index(List<VariableFor401k> variableList)throws Exception{
		for(int i=0;i<variableList.size();i++){
			index(variableList.get(i));
		}
	}
	
	
	public static VariableFor401k find(String keyword) {
		try {
			List<String[]> querys = new ArrayList<String[]>();
			keyword = keyword.replaceAll("\\(|\\)", " ");
			keyword = keyword.replaceAll("[' ']+", " ");
			String[] splits = keyword.trim().split(" ");
			for (int i = 0; i < splits.length; i++) {
				String s = splits[i];
				querys.add(new String[] {"description", s});
			}
			List<VariableFor401k> variableList = FundTableCachingUtil.find(querys, 1);
			if(variableList != null && variableList.size() > 0){
				VariableFor401k variable = variableList.get(0);
				System.out.println(variable.getSymbol());
				System.out.println(variable.getName());
				System.out.println(variable.getDescription());
				System.out.println(variable.getAssetClassName());
				return variableList.get(0);
			}
			return new VariableFor401k();
		} catch (Exception e) {
			return new VariableFor401k();
		}
	}
	
	public static List<VariableFor401k> finds(String keyword) {
		try {
			List<String[]> querys = new ArrayList<String[]>();
			keyword = keyword.replaceAll("\\(|\\)", " ");
			keyword = keyword.replaceAll("[' ']+", " ");
			String[] splits = keyword.trim().split(" ");
			for (int i = 0; i < splits.length; i++) {
				String s = splits[i];
				querys.add(new String[] {"description", s});
			}
			return FundTableCachingUtil.find(querys, 10);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static List<VariableFor401k> find(List<String[]> querys, int size) throws Exception {
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
		List<VariableFor401k> variableList = new ArrayList<VariableFor401k>();
		for (int i = 0; i < hits.length(); i++) {
			Document document = hits.doc(i);
			VariableFor401k variable = new VariableFor401k();
			variable.setName(document.get("name"));
			variable.setAssetClassName(document.get("assetclassname"));
			variable.setDescription(document.get("description"));
			variable.setSymbol(document.get("symbol"));
			variableList.add(variable);
			if (i >= size - 1)
				break;
		}
		return variableList;
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		List<Long> planIDList = new ArrayList<Long>();
		planIDList.add(991L);
		FundTableCachingUtil.initialize(planIDList);
		//VariableFor401k variable = FundTableCachingUtil.find("Fidelity Freedom 2045 Fund Mutual fund");
//		List<VariableFor401k> variableList = FundTableCachingUtil.finds("VDC (Vanguard Consumer Staples ETF)");
//		for(VariableFor401k variable: variableList){
//			System.out.println(variable.getSymbol());
//			System.out.println(variable.getName());
//			System.out.println(variable.getDescription());
//			System.out.println(variable.getAssetClassName());
//		}
		
//		long type = 255l;
//		long type1 = 32l;
//		System.out.println(type & type1);
//		System.out.println(type | type1);
//		System.out.println(type & (~type1));
	}

}
