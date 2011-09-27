package com.lti.executor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.lti.Exception.Strategy.ParseVariableException;
import com.lti.service.bo.Strategy;
import com.lti.type.Pair;
import com.lti.type.executor.CodeInf;
import com.lti.util.ChineseToSpell;
import com.lti.util.StringUtil;

public class CompilerPreProcessor {

	public static String str_replace(String source, String search, String replace) {
		int sind = -1;
		String aux = "", s = source;
		while (s != null && !s.equals("")) {
			sind = s.indexOf(search);
			if (sind != -1) {
				aux += s.substring(0, sind) + replace;
				s = s.substring(sind + search.length());
			} else {
				aux += s;
				s = "";
			}
		}
		return aux;
	}

	public static final String TYPE_TABLE="(String|Long|Double|Integer|Float|Boolean|Date|" +
			"java.util.String|java.util.Long|java.util.Double|java.util.Integer|java.util.Float|java.util.Boolean|java.util.Date|" +
			"long|double|int|float|boolean|" +
			")";

	
	public static java.util.List<Pair> parseVariables(String source) throws ParseVariableException{

		if(source==null)return null;
		
		String regEx1 = "\\A\\s*("+TYPE_TABLE+"(\\s*\\[\\s*\\])*)\\s+((\\w[\\w\\d]*)\\s*(=\\s*.+\\s*)?(,\\s*(\\w[\\w\\d]*)\\s*(=\\s*.+\\s*)?)*)\\z";
		Pattern p1 = Pattern.compile(regEx1, Pattern.CASE_INSENSITIVE);
		
		String text=source;
		
		
		//remove ["298-19875-91459kanfdknzuer"] to [] 
		text=text.replaceAll("\\\"[^\\\"]*\\\"", "");
		
		//remove [{{{}},{}}] to [{{},}] to [{,}] to []
		text=text.replaceAll("\\{[^\\{\\}]*\\}", "");
		text=text.replaceAll("\\{[^\\{\\}]*\\}", "");
		text=text.replaceAll("\\{[^\\{\\}]*\\}", "");
		
		//remove comments
		text=text.replaceAll("//.*\\r?\\n", "");
		text=text.replaceAll("(?m)/\\*.*\\*/", "");
		
		
		//int a=1; to int a;
		text=text.replaceAll("(?m)=[^=;,]*;", ";");
		
		
		//int a=1,b=2; to int a,b;
		text=text.replaceAll("(?m)=[^=;,]*,", ",");
		
		
		text=text.replaceAll(";+", ";");
		String[] strs = text.split(";");
		
		String regEx2 = "(\\w[\\w\\d]*)\\s*(=\\s*.+\\s*)?$";
		Pattern p2 = Pattern.compile(regEx2, Pattern.CASE_INSENSITIVE);
		List<Pair> list = new ArrayList<Pair>();
		
		for (int i = 0; i < strs.length; i++) {
			String str = strs[i];
			if(str==null||str.trim().length()==0)continue;
			Matcher m1 = p1.matcher(str);
			
			boolean rs1 = m1.find();

			if (rs1) {
				String type=m1.group(1);
				String[] names=m1.group(4).split(",");
				
				for(int j=0;j<names.length;j++){
					
					Matcher m2 = p2.matcher(names[j]);

					boolean rs2 = m2.find();
					if(rs2){
						com.lti.type.Pair pair = new Pair(type,m2.group(1));
						list.add(pair);
						
					}else{
						throw new ParseVariableException("Parse vairables error when parsing: "+str);
					}
				}
			} else {
				throw new ParseVariableException("Parse vairables error when parsing: "+str);
			}
		}
		return list;
	}
	public static void main(String[] args){
		try {
			List<Pair> list=parseVariables("String[] aaa={\"abc\",\",;=doubeakljfierjkc,qour./*230974\"};String sa=\"*/\";long[] sb={1L,2L};Long[][] longa={{1l,2l},{3l,4l}},longb={{},{}};;boolean position;//a\r\n//b\r\ndouble d1=0.1,d2,d3=0.2;/* double notype;*ef//abcdefg\r\n*/int a,b,c;;double a1=0.2;");
			for(int i=0;i<list.size();i++){
				Pair p=list.get(i);
				System.out.println("type: "+p.Pre);
				System.out.println("name: "+p.Post);
			}
		} catch (ParseVariableException e) {
			//e.printStackTrace();
			System.out.println(StringUtil.getStackTraceString(e));
		}
	}
	public static String parseFunction(String source) {

		String[] strs = com.lti.executor.CompilerPreProcessor.str_replace(source, "\r", "").split("\n");
		String returnString = "";
		boolean flag = false;
		if (strs != null) {
			for (int i = 0; i < strs.length; i++) {
				String str = strs[i];
				if (str.indexOf("public") != -1)
					flag = true;

				if (flag) {
					if (str.indexOf("{") != -1) {
						returnString += com.lti.executor.CompilerPreProcessor.str_replace(str, "{", " throws Exception {")   + "\r\n";
						flag = false;
						continue;
					}
				}
				returnString += str + "\r\n";

			}
		}
		return returnString;
	}

	public static String ObjectToString(Object obj) {
		try {
			ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream(2048);
			ObjectOutputStream out = new ObjectOutputStream(baos);
			out.writeObject(obj);
			out.flush();
			out.close();
			String result = baos.toString();
			return result;
		} catch (IOException e) {
			//e.printStackTrace();
			System.out.println(StringUtil.getStackTraceString(e));
			return "";
		}
	}

	public static Object StringToObject(String str) {
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(str.getBytes());
			ObjectInputStream in = new ObjectInputStream(bais);
			Object result = in.readObject();
			return result;
		} catch (Exception e) {
			//e.printStackTrace();
			System.out.println(StringUtil.getStackTraceString(e));
			return null;
		}
	}

	public static String removeType(String source) {

		String resultString = "";
		String regEx = "(\\w(\\w|\\d)*( *\\[\\])?) +((\\w(\\w|\\d)*)( )*(=( )*.+)?)$";
		Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
		String[] strs = source.split(";");
		for (int i = 0; i < strs.length; i++) {
			String str = strs[i];
			Matcher m = p.matcher(str);

			boolean rs = m.find();

			if (rs) {
				String subRegEx = "(\\w(\\w||\\d)*)( )*(=( )*.+)$";
				Pattern subP = Pattern.compile(subRegEx, Pattern.CASE_INSENSITIVE);
				Matcher subM = subP.matcher(str);
				if (subM.find()) {
					resultString += m.group(4) + ";\r\n";
				}
			} else {
				resultString += str + ";\r\n";
			}
		}
		return resultString;
	}

	public static String DateToString(Date date) {

		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateFormat.format(date);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return "2007-01-01";
		}
	}

	public static Date parseDate(String date) {
		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
		try {
			return dateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			java.util.Calendar ca = java.util.Calendar.getInstance();
			ca.set(2007, 0, 1, 0, 0, 0);
			return ca.getTime();
		}
	}

	public static int[] parseIntArray(String source) throws com.lti.Exception.ParseException {
		if (source == null || source.length() == 0)
			return null;
		try {
			String[] strs = source.split("&");
			int[] r = new int[strs.length];
			for (int i = 0; i < strs.length; i++) {
				r[i] = Integer.parseInt(strs[i]);
			}
			return r;
		} catch (Exception e) {
			com.lti.Exception.ParseException p = new com.lti.Exception.ParseException();
			p.setStackTrace(e.getStackTrace());
			p.setDetail(com.lti.Exception.ParseException.VARIABLE_INTEGER_ARRAY_ERROR);
			throw p;
		}
	}

	public static double[] parseDoubleArray(String source) throws com.lti.Exception.ParseException {
		if (source == null || source.length() == 0)
			return null;
		try {
			String[] strs = source.split("&");
			double[] r = new double[strs.length];
			for (int i = 0; i < strs.length; i++) {
				r[i] = Double.parseDouble(strs[i]);
			}
			return r;
		} catch (Exception e) {
			com.lti.Exception.ParseException p = new com.lti.Exception.ParseException();
			p.setStackTrace(e.getStackTrace());
			p.setDetail(com.lti.Exception.ParseException.VARIABLE_DOUBLE_ARRAY_ERROR);
			throw p;
		}
	}

	public static String[] parseStringArray(String source) throws com.lti.Exception.ParseException {
		if (source == null || source.length() == 0)
			return null;
		try {
			String[] strs = source.split("&");
			return strs;
		} catch (Exception e) {
			com.lti.Exception.ParseException p = new com.lti.Exception.ParseException();
			p.setStackTrace(e.getStackTrace());
			p.setDetail(com.lti.Exception.ParseException.VARIABLE_INTEGER_ARRAY_ERROR);
			throw p;
		}
	}

	public static String IntArrayToString(int[] a) throws com.lti.Exception.ParseException {
		try {
			String returnString = "";
			for (int i = 0; i < a.length; i++) {
				returnString += String.valueOf(a[i]) + "&";
			}
			return returnString;
		} catch (Exception e) {
			com.lti.Exception.ParseException p = new com.lti.Exception.ParseException();
			p.setStackTrace(e.getStackTrace());
			p.setDetail(com.lti.Exception.ParseException.VARIABLE_INTEGER_ARRAY_ERROR);
			throw p;
		}
	}

	public static String DoubleArrayToString(double[] a) throws com.lti.Exception.ParseException {
		try {
			String returnString = "";
			for (int i = 0; i < a.length; i++) {
				returnString += String.valueOf(a[i]) + "&";
			}
			return returnString;
		} catch (Exception e) {
			com.lti.Exception.ParseException p = new com.lti.Exception.ParseException();
			p.setStackTrace(e.getStackTrace());
			p.setDetail(com.lti.Exception.ParseException.VARIABLE_DOUBLE_ARRAY_ERROR);
			throw p;
		}
	}

	public static String StringArrayToString(String[] a) throws com.lti.Exception.ParseException {
		try {
			String returnString = "";
			for (int i = 0; i < a.length; i++) {
				returnString += a[i] + "&";
			}
			return returnString;
		} catch (Exception e) {
			com.lti.Exception.ParseException p = new com.lti.Exception.ParseException();
			p.setStackTrace(e.getStackTrace());
			p.setDetail(com.lti.Exception.ParseException.VARIABLE_STRING_ARRAY_ERROR);
			throw p;
		}
	}
	
	public static String getValidateName(CodeInf str){
		if(str==null)return "";
		String vn=str.getName();
		vn=vn.replace(' ', '_');
		vn=vn.replace('\'', '_');
		vn=vn.replace('\\', '_');
		vn=vn.replace('/', '_');
		vn=vn.replace('(', '_');
		vn=vn.replace(')', '_');
		vn=vn.replace('[', '_');
		vn=vn.replace(']', '_');
		vn=vn.replace('.', '_');
		if(str.getID()!=null){
			try {
				return ChineseToSpell.getFullSpell(vn)+str.getID();
			} catch (UnsupportedEncodingException e) {
				return vn;
			}
		}
		else return vn;
	}
}
