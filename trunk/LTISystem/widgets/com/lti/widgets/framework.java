package com.lti.widgets;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.struts2.ServletActionContext;

import com.lti.action.Action;
import com.lti.service.StrategyManager;
import com.lti.system.ContextHolder;
import com.lti.widgets.bean.WidgetBean;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class framework {
	
	/**
	 * 请求的widget，可以是多个，
	 * 
	 * 可能需要在数据库加一张widget的表
	 * w_id,w_params
	 * 
	 * 可以从w_params中构造framework；
	 * 用户请求时，只需要传递w_id，(相当于face_book上的application_id)
	 * 这样客户端就不需要保存一长串参数，
	 * 主要是日后改动时方便测试及重新构造参数
	 * 
	 */
	protected List<WidgetBean> widgets;
	

	public List<WidgetBean> getWidgets() {
		return widgets;
	}

	public void setWidgets(List<WidgetBean> widgets) {
		this.widgets = widgets;
	}
	
	protected Long thirdPartyID;

	public Long getThirdPartyID() {
		return thirdPartyID;
	}

	public void setThirdPartyID(Long thirdPartyID) {
		this.thirdPartyID = thirdPartyID;
	}

	/**
	 * 主机名，为方便调试使用
	 */
	protected String serverName;
	
	
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/**
	 * 主机端口，为方便调试使用
	 */
	private String port;
	
	
	
	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	/**
	 * 对于framework是没有widget name
	 * 此方法只是为了方便继承此类的widget使用，因为请求该widget时并不一定会将widget name传入，
	 * 
	 * 如：请求的widget url为/LTISystem/framework/getstarted/widget.action
	 * widget name就是getstarted
	 */
	protected String widgetName;
	
	
	
	public String getWidgetName() {
		return widgetName;
	}

	public void setWidgetName(String widgetName) {
		this.widgetName = widgetName;
	}

	/**
	 * 如果返回json数据的话，统一赋给json变量
	 * 直接转到json result即/jsp/framework/json.uftl
	 */
	protected String json;
	
	
	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}
	
	/**
	 * 跨域请求是不能直接处理json，
	 * 是需要以callback_fn(json)的形式返回
	 * 
	 */
	protected String jsoncallback;
	protected String callback;
	

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getJsoncallback() {
		return jsoncallback;
	}

	public void setJsoncallback(String jsoncallback) {
		this.jsoncallback = jsoncallback;
	}

	
	/**
	 * 请求时间，方便区别ID
	 */
	public String time=System.currentTimeMillis()+"";
	
	
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	private int getIndex(String str){
		String regx = "\\[(\\d)\\]";
		Pattern p = Pattern.compile(regx);
		Matcher m = p.matcher(str);
		if(m.find()){
			return Integer.parseInt(m.group(1));
		}
		return -1;
	}
	
	/**
	 * 请求url: http://localhost/LTISystem/framework/framework.action
	 * 
	 * 返回jsp/framework/framework.js，framework.js是构建widget**框架**的基本js，
	 * 只需要转入framework，让其装载相应的widget
	 * 
	 * 每个widget拥有jsp/framework/xxx/widget.js，是构建widget的js，
	 * widget使用传入的params参数，请求相应的json数据源
	 * 
	 * 因此，一个widget拥有一个构建widget的js，及若干的json数据源
	 * @return
	 */
	public String execute() throws Exception{
		if(thirdPartyID!=null){
			
			//需要从这里开始构造widget的参数，待完全
			StrategyManager sm=ContextHolder.getStrategyManager();
			com.lti.service.bo.ThirdParty tr=sm.getThirdParty(thirdPartyID);
			if(tr!=null){
				String _params=tr.getParameters();
				widgets = new ArrayList<WidgetBean>();
				//http://222.200.180.114/LTISystem/widgets/framework.action?widgets[0].id=mpiq_id123&widgets[0].name=getstartedwidget&widgets[0].params=noparams&widgets[1].id=noid&widgets[1].name=createplanwidget&widgets[1].params=noparams&widgets[1].autoloading=false&widgets[2].id=noid&widgets[2].name=planwidget&widgets[2].params=noparams&widgets[2].autoloading=false&widgets[3].id=noid&widgets[3].name=portfoliowidget&widgets[3].params=noparams&widgets[3].autoloading=false&widgets[4].id=noid&widgets[4].name=customizewidget&widgets[4].params=noparams&widgets[4].autoloading=false&ThirdParty=abc.com
				String[] params=_params.split("&");
				for(String pair:params){
					String[] pp=pair.split("=");
					String key=pp[0].toLowerCase();
					String value=pp[1];
					if(key.equals("thirdparty")){
						ThirdParty=value;
					}else{
						int index=getIndex(key);
						if(index!=-1){
							WidgetBean wb=null;
							if(index>=widgets.size()){
								for(int i=widgets.size();i<=index;i++){
									widgets.add( new WidgetBean());
								}
							}
							wb=widgets.get(index);
							
							if(key.indexOf(".id")!=-1){
								wb.setId(value);
							}else if(key.indexOf(".name")!=-1){
								wb.setName(value);
							}else if (key.indexOf(".params")!=-1){
								wb.setParams(URLDecoder.decode(value,"utf8"));
							}else if(key.indexOf(".autoloading")!=-1){
								if(value.toLowerCase().equals("true")){
									wb.setAutoloading(true);
								}else{
									wb.setAutoloading(false);
								}//end if-else
							}//end if
						}//end if index != -1
					}//end if-else
				}//end for
			}//end tr != null
			
		}
		serverName=ServletActionContext.getRequest().getServerName();
		port=ServletActionContext.getRequest().getServerPort()+"";
		//String[] strs=ServletActionContext.getRequest().getRequestURI().split("/");
		//widgetName=strs[strs.length-2];
		
		Map<String,Boolean> loaded=new HashMap<String, Boolean>();
		StringBuffer sb=new StringBuffer();
		for(int i=0;i<widgets.size();i++){
			widgetName=widgets.get(i).getName();
			if(loaded.get(widgetName)!=null){
				continue;
			}
			loaded.put(widgetName, true);
			
			Map<String,Object> objs=new HashMap<String, Object>();
			objs.put("serverName", serverName);
			objs.put("port", port);
			objs.put("time", time);
			objs.put("widgetName", widgetName);
			
			ui=_get_code(objs,"ui.html");
			if(ui!=null){
				objs.put("ui", ui.replaceAll("[\\n\\r]+", "").replaceAll("\"", "\\\\\\\""));
			}
			
			js=_get_code(objs,"widget.js");
			
			sb.append(js).append("\n\r");
		}
		
		
		
		generatedJS=sb.toString();
		
		
		
		return Action.JS;
	}
	
	/**
	 * 加载widget相应的js的全体
	 */
	private String generatedJS;
	
	
	
	public String getGeneratedJS() {
		return generatedJS;
	}

	public void setGeneratedJS(String generatedJS) {
		this.generatedJS = generatedJS;
	}

	/**
	 * 产生widget的html，
	 * 注意任何与数据有关显示部分都不建议嵌入其中，
	 * 应该通过javascript请求相应的json数据源，并处理之
	 */
	private String ui;
	
	
	public String getUi() {
		return ui;
	}

	public void setUi(String ui) {
		this.ui = ui;
	}
	
	
	/**
	 * 产生widget的javascript
	 */
	private String js;
	
	

	public String getJs() {
		return js;
	}

	public void setJs(String js) {
		this.js = js;
	}

	/**
	 * 此函数很重要，用来产生widget的html，
	 * 如果全部用js来构造html，编程工作非常繁重，
	 * 此函数自动将widget目录下的ui.html转为json,
	 * 
	 * 对于framework，此函数无作用
	 * @return
	 */
	public String _get_code(Map<String,Object> objs,String filename){
		try {
			StringWriter sw=new StringWriter();
			Configuration conf=new Configuration();
			conf.setDirectoryForTemplateLoading(new File(ContextHolder.getServletPath(),"jsp/widgets/"+widgetName+"/"));
			Template t = conf.getTemplate(filename);
			t.process(objs, sw);
			//StringWriter jsw=new StringWriter();
			//JSONBuilder js=new JSONBuilder(jsw);
			//js.object().key("html").value(sw.toString()).endObject();
			return sw.toString();
		} catch (IOException e) {
			//e.printStackTrace();
			//e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	protected static String ThirdParty="myplaniq.com";
	
	
	public String getThirdParty() {
		return ThirdParty;
	}

	public void setThirdParty(String thirdParty) {
		ThirdParty = thirdParty;
	}

	public static void main(String[] args){
		System.out.println("\"aa b c a df  fan\n adddd\n\n aaaa\r\n ddd\r ddd\r\rbc\"".replaceAll("[\\n\\r]+", ""));
	}
}
