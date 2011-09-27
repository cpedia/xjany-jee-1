package com.lti.executor;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lti.compiler.LTIClassLoader;
import com.lti.system.ContextHolder;
import com.lti.type.Quarter;
import com.lti.type.Quaternion;
import com.lti.type.executor.CodeInf;
import com.lti.type.executor.SimulateStrategy;
import com.lti.util.StringUtil;

public class Compiler {
	public static String templatename=(String)com.lti.system.Configuration.get("COMPILED_STRATEGY_TEMPLATE_NAME");;
	public static String templateDirectory = StringUtil.getPath(new String[] { ContextHolder.getServletPath(), (String) com.lti.system.Configuration.get("COMPILED_STRATEGY_SRC_PATH") });
	public static String[] classpath = getClassPath();
	public static String srcpath = StringUtil.getPath(new String[] { ContextHolder.getServletPath(), (String) com.lti.system.Configuration.get("COMPILED_STRATEGY_SRC_PATH") });
	public static String destpath = StringUtil.getPath(new String[] { ContextHolder.getServletPath(), (String) com.lti.system.Configuration.get("CLASS_ROOT_PATH") });
	public static String packagename = (String) com.lti.system.Configuration.get("COMPILED_STRATEGY_PACKAGE_NAME");
	
	public static com.lti.compiler.LTICompiler lticompiler = new com.lti.compiler.LTICompiler(templateDirectory, classpath, srcpath, destpath);
	public static String absoluteclasspath=StringUtil.getPath(new String[]{destpath,packagename.replaceAll("\\.", "\\\\")});
	
	public static void setFormatSource(boolean isFormatSource){
		lticompiler.setFormatSource(isFormatSource);
	}
	
	public static SimulateStrategy getStrategyInstance(CodeInf strategy, Long strategyClassID) throws Exception{
		return getStrategyInstance(strategy,null, strategyClassID);
	}
	
	public static String[] getClassPath(){
		String[] classpath=((String)com.lti.system.Configuration.get("CLASS_PATH")).split(",");
		for(int i=0;i<classpath.length;i++){
			classpath[i]=StringUtil.getPath(new String[]{ContextHolder.getServletPath(),classpath[i]});
		}
		return classpath;
	}
	
	public static SimulateStrategy getStrategyInstance(CodeInf strategy,LTIClassLoader classLoader, Long strategyClassID) throws Exception{
		return getStrategyInstance(strategy, classLoader, strategyClassID, true);
	}
	
	/**
	 * @param strategy
	 * @param classLoader
	 * @param recompile　此参数无效，在运行portfolio之前需要自己编译strategy
	 * @return
	 * @throws Exception
	 */
	public static SimulateStrategy getStrategyInstance(com.lti.type.executor.CodeInf strategy,LTIClassLoader classLoader, Long strategyClassID, boolean recompile) throws Exception{
		if(classLoader==null){
			classLoader=new LTIClassLoader();
		}
		String compiledOutput="";
		//不再编译
		//if(recompile)compiledOutput=Compiler.complie(strategy, strategyClassID);
		String classname=CompilerPreProcessor.getValidateName(strategy);
		String classAbsoluteName = packagename + "." + classname;
		Class assetAllocationClass = null;
		//String jarURL = new File(".").getCanonicalFile()+"\\WEB-INF\\lib\\ltisystem_compiledstrategy.jar";
		//String fullFileName = packagename + "." + classname;
		try {
			//assetAllocationClass = Class.forName(fullFileName);
			assetAllocationClass=classLoader.loadUserClass(StringUtil.getPath(new String[]{absoluteclasspath,classname+".class"}), classAbsoluteName, true);
		} catch (Exception e) {
			Exception ee=new Exception(e.getMessage()+"\nOutput message from compiler:\n"+compiledOutput,e);
			throw ee;
		}
		SimulateStrategy instance = (SimulateStrategy) assetAllocationClass.newInstance();
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public static String complie(com.lti.type.executor.CodeInf strategy, Long strategyClassID) throws Exception {
		Map codemap = new HashMap();

		String classNmae = CompilerPreProcessor.getValidateName(strategy);

		codemap.put("ID", strategy.getID());
		
		codemap.put("strategyClassID", strategyClassID);
		
		codemap.put("name", classNmae);

		List<Quaternion> pars=strategy.getParameter();
		if(pars!=null&&pars.size()>0){
			for(int i=0;i<pars.size();i++){
				Quaternion p=pars.get(i);
				if(p.getFirst().trim().matches("^String\\s*\\[\\s*\\]$")){
					p.setThird(p.getThird().replaceAll("\\\"", ""));
				}
			}
		}
		codemap.put("parameters", strategy.getParameter());

		codemap.put("variableCode", strategy.getVariable());

		List variables = null;
		try {
			variables = CompilerPreProcessor.parseVariables(strategy.getVariable());
			if (variables != null)
				codemap.put("variables", variables);
			codemap.put("persistable", "true");
		} catch (Exception e) {
			codemap.put("persistable", "false");
		}

		codemap.put("functions", CompilerPreProcessor.parseFunction(strategy.getFunction()));

		codemap.put("initCode", strategy.getInit());
		
		codemap.put("reinitCode", strategy.getReInit());

		codemap.put("commonActionCode", strategy.getCommentAction());

		codemap.put("actions", strategy.getConditionAction());

		codemap.put("defaultActionCode", strategy.getDefaultAction());
		
		String name=CompilerPreProcessor.getValidateName(strategy);
		
		return lticompiler.complile(name, templatename, codemap);
	}

	public static void delete(com.lti.type.executor.CodeInf strategy) throws Exception{
		String name=CompilerPreProcessor.getValidateName(strategy);
		lticompiler.clean(name);
	}
}
