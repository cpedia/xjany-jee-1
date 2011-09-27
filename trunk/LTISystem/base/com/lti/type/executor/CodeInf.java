package com.lti.type.executor;

import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

import com.lti.type.Quaternion;

public class CodeInf implements Serializable{

	private static final long serialVersionUID = 1L;

	protected java.lang.Long ID;

	protected java.lang.String Name;

	protected List<com.lti.type.Quaternion> Parameter;

	protected java.lang.String Variable;

	protected java.lang.String Function;

	protected java.lang.String Init;

	protected java.lang.String DefaultAction;

	protected java.lang.String CommentAction;

	protected java.lang.String ReInit;

	protected List<com.lti.type.Pair> ConditionAction;

	public String toXML() {
		try {
			StringWriter sw = new StringWriter();
			Marshaller marshaller = new Marshaller(sw);
			marshaller.marshal(this);
			return sw.toString();
		} catch (Exception e) {
			return "";
		}
	}

	public String getParameterStructure() {
		List<Quaternion> list = new ArrayList<Quaternion>();
		if(this.getParameter()!=null){
			list.addAll(this.getParameter());
		}
		list.add(new Quaternion("double", "version", "1.0", "verison"));
		StringBuffer sb = new StringBuffer();
		sb.append("{\"map\":[");
		boolean first = true;
		for (Quaternion q : list) {
			if (!first) {
				sb.append(",");
			}
			first = false;
			sb.append("{\"name\":\"");
			sb.append(q.getSecond().replace("\"", "\\\""));
			sb.append("\",\"title\":\"");
			sb.append(q.getFirst().replace("\"", "\\\""));
			sb.append("\",\"type\":\"");
			sb.append("text");
			sb.append("\",\"css\":\"");
			sb.append("\",\"default2\":\"");
			sb.append(q.getThird().replace("\"", "\\\""));
			sb.append("\"}");
		}
		sb.append("]}");
		return sb.toString();
	}

	public static CodeInf getInstance(Reader reader) {
		try {
			Unmarshaller unmarshaller = new Unmarshaller(CodeInf.class);
			unmarshaller.setWhitespacePreserve(true);
			CodeInf hinf = (CodeInf) unmarshaller.unmarshal(reader);
			return hinf;
		} catch (Exception e) {
		}
		return null;
	}

	public static void main(String args[]) {
		CodeInf ci = new CodeInf();
		ci.setCommentAction("aaa\r\nbbbb");
		System.out.println(ci.toXML());
		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<code-inf><comment-action>aaa&#xd;\r\nbbbb</comment-action></code-inf>";
		CodeInf ci2 = CodeInf.getInstance(new StringReader(xml));
		System.out.println(ci2.getCommentAction());
	}

	public java.lang.Long getID() {
		return ID;
	}

	public void setID(java.lang.Long iD) {
		ID = iD;
	}

	public java.lang.String getName() {
		return Name;
	}

	public void setName(java.lang.String name) {
		Name = name;
	}

	public List<com.lti.type.Quaternion> getParameter() {
		return Parameter;
	}

	public void setParameter(List<com.lti.type.Quaternion> parameter) {
		Parameter = parameter;
	}

	public java.lang.String getVariable() {
		return Variable;
	}

	public void setVariable(java.lang.String variable) {
		Variable = variable;
	}

	public java.lang.String getFunction() {
		return Function;
	}

	public void setFunction(java.lang.String function) {
		Function = function;
	}

	public java.lang.String getInit() {
		return Init;
	}

	public void setInit(java.lang.String init) {
		Init = init;
	}

	public java.lang.String getDefaultAction() {
		return DefaultAction;
	}

	public void setDefaultAction(java.lang.String defaultAction) {
		DefaultAction = defaultAction;
	}

	public java.lang.String getCommentAction() {
		return CommentAction;
	}

	public void setCommentAction(java.lang.String commentAction) {
		CommentAction = commentAction;
	}

	public List<com.lti.type.Pair> getConditionAction() {
		return ConditionAction;
	}

	public void setConditionAction(List<com.lti.type.Pair> conditionAction) {
		ConditionAction = conditionAction;
	}

	public java.lang.String getReInit() {
		return ReInit;
	}

	public void setReInit(java.lang.String reInit) {
		ReInit = reInit;
	}

}
