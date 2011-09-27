package com.lti.action;


/**
 * 
 * 记录Struts2返回的页面的名称
 * 
 * @category 页面
 *
 */
public interface Action {
	
	public static final String VIEW = "view";
	public static final String SUCCESS = "success";
	public static final String MESSAGE = "message";
	public static final String REDIRECT_EDIT = "redirect_edit";
	public static final String REDIRECT = "redirect";
	public static final String HTML = "html";
	public static final String BASICUNIT = "basicunit";
	public static final String NONE = "none";
	public static final String ERROR = "error";
	public static final String INPUT = "input";
	public static final String OUTPUT = "output";
	public static final String LOGIN = "login";
	public static final String JSON = "json";
	public static final String JS = "js";
	public static final String DATA = "data";
	public static final String MONITOR = "monitor";
	public static final String EVALUATE = "evaluate";
	
	public static final String PAYITEM = "payitem";
	public static final String VALIDTIME = "validtime";
	public static final String EXPIRED = "expired";
	public static final String STATUS = "status";

	public static final String DEL_SUCCESS="del_success";
	public static final String DELETE = "delete";
	public static final String DEL_MODEL = "del_model";
	public static final String DOWNLOAD = "download";
	public static final String MAIN = "main";
	public static final String OFXVIEW = "ofxview";
	
	public static final String IMAGE = "image";
	
	
	@Deprecated
	public static String ACTION_SAVE="save";
	@Deprecated
	public static String ACTION_UPDATE="update";
	@Deprecated
	public static String ACTION_DELETE="delete";
	@Deprecated
	public static String ACTION_PREDELETE="predelete";
	@Deprecated
	public static String ACTION_VIEW="view";
	@Deprecated
	public static String ACTION_CREATE="create";
	@Deprecated
	public static String ACTION_SAVEAS="saveas";
	@Deprecated
	public static String ACTION_STOP="stop";
	@Deprecated
	public static final String ACTION_REDIRECT = "redirect";
	
	
	public String execute() throws Exception;
	
}
