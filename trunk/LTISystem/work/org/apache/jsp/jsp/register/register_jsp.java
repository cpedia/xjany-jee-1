package org.apache.jsp.jsp.register;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.io.File;
import java.io.FileInputStream;
import com.lti.system.*;;

public final class register_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList(1);
    _jspx_dependants.add("/WEB-INF/struts-tags.tld");
  }

  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005fif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005felseif_0026_005ftest;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005felse;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005fform_0026_005ftheme_005fnamespace_005fname_005fid_005fenctype;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fname_005fid_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fid_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005ffielderror_0026_005fname_005fid_005fcssStyle_005fnobody;
  private org.apache.jasper.runtime.TagHandlerPool _005fjspx_005ftagPool_005fs_005furl_0026_005fnamespace_005fid_005faction_005fnobody;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005felseif_0026_005ftest = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005felse = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005fform_0026_005ftheme_005fnamespace_005fname_005fid_005fenctype = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fname_005fid_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fid_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005ffielderror_0026_005fname_005fid_005fcssStyle_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _005fjspx_005ftagPool_005fs_005furl_0026_005fnamespace_005fid_005faction_005fnobody = org.apache.jasper.runtime.TagHandlerPool.getTagHandlerPool(getServletConfig());
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.release();
    _005fjspx_005ftagPool_005fs_005felseif_0026_005ftest.release();
    _005fjspx_005ftagPool_005fs_005felse.release();
    _005fjspx_005ftagPool_005fs_005fform_0026_005ftheme_005fnamespace_005fname_005fid_005fenctype.release();
    _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fname_005fid_005fnobody.release();
    _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fid_005fnobody.release();
    _005fjspx_005ftagPool_005fs_005ffielderror_0026_005fname_005fid_005fcssStyle_005fnobody.release();
    _005fjspx_005ftagPool_005fs_005furl_0026_005fnamespace_005fid_005faction_005fnobody.release();
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<html>\r\n");
      out.write("<head>\r\n");
      out.write("\r\n");
      out.write("<META HTTP-EQUIV=\"Pragma\" CONTENT=\"no-cache\"> \r\n");
      out.write("<META HTTP-EQUIV=\"Cache-Control\" CONTENT=\"no-cache,must-revalidate\"> \r\n");
      out.write("<META HTTP-EQUIV=\"Expires\" CONTENT=\"0\"> \r\n");
      out.write("<META HTTP-EQUIV=\"expires\" CONTENT=\"wed,26feb199708:21:57gmt\"> \r\n");
      out.write("\r\n");
      out.write("<script type=\"text/javascript\" src=\"/LTISystem/jsp/images/tiny_mce/tiny_mce.js\"></script>\r\n");
      out.write("\r\n");
      out.write("<script type=\"text/javascript\" src=\"/LTISystem/jsp/register/verificationCode.js\"></script>  \r\n");
      out.write("<script type=\"text/javascript\" src=\"/LTISystem/jsp/register/loadXMLdata.js?id=2\"></script>\r\n");
      out.write("<script type=\"text/javascript\" src=\"../images/md5.js\"></script>\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");

String url= request.getRequestURL().toString();
boolean flag=false;
String[] strs=Configuration.get("f401kdomain").toString().split("\\|");
for(int i=0;i<strs.length;i++){
	if(url.toLowerCase().contains(strs[i].trim())){
		flag=true;
		break;
	}
}
if(flag==false){

      out.write("\r\n");
      out.write("<link href=\"/LTISystem/jsp/template/ed/css/tablesorter/tablesorter.css\" rel=\"stylesheet\" type=\"text/css\"/>\r\n");
      out.write("<link rel=\"stylesheet\" href=\"/LTISystem/jsp/images/jquery.ui.theme/redmond/jquery-ui-1.7.1.custom.css\" type=\"text/css\" />\r\n");
      out.write("<script type=\"text/javascript\" src=\"/LTISystem/jsp/images/jquery.ui/1.7.1/ui.tabs.js\"></script>\r\n");

}else{

      out.write("\r\n");
      out.write("<link href=\"/LTISystem/jsp/template/ed/css/jquery_UI/moss/jquery-ui-1.8.custom.css\" rel=\"stylesheet\" type=\"text/css\" />\r\n");

}		


      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<!--<link rel=\"stylesheet\" href=\"/LTISystem/jsp/images/jquery.ui.theme/redmond/jquery-ui-1.7.1.custom.css\" type=\"text/css\" />  -->\r\n");
      out.write("<!-- these JSes are necessary START-->\r\n");
      out.write("<!--<script type=\"text/javascript\" src=\"../images/jquery.ui/1.7.1/ui.tabs.js\"></script>  -->\r\n");
      out.write("<!--<SCRIPT src=\"../images/jquery.ToggleVal/jquery.toggleval.js\" type=\"text/javascript\" ></SCRIPT>  -->\r\n");
      out.write("<!--<link href=\"../images/jquery.ToggleVal/screen.css\" rel=\"stylesheet\" type=\"text/css\" media=\"screen\"/>  -->\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("<script type=\"text/javascript\">\r\n");
      out.write("var _state;\r\n");
      out.write("var _country;\r\n");
      out.write("$(function () {\r\n");
      out.write("\tvar d = new Date();\r\n");
      out.write("\r\n");
      out.write("\t $(\".fbbl_center\").tabs({ fx: { opacity: 'toggle' } });\r\n");
      out.write("\t");
      if (_jspx_meth_s_005fif_005f0(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("});\r\n");
      out.write("function onclk(){\r\n");
      out.write("\t\t");
      if (_jspx_meth_s_005fif_005f1(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\t\t");
      if (_jspx_meth_s_005felseif_005f0(_jspx_page_context))
        return;
      out.write(" \r\n");
      out.write("};\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("function ReImgSize() {\r\n");
      out.write("    var await = document.getElementById(\"chart\"); \r\n");
      out.write("    var imgall = await.getElementsByTagName(\"img\"); \r\n");
      out.write("    for (i = 0; i < imgall.length; i++) {\r\n");
      out.write("        if (imgall[i].height > 180) \r\n");
      out.write("        {\r\n");
      out.write("            var oWidth = imgall[i].width; \r\n");
      out.write("            var oHeight = imgall[i].height;\r\n");
      out.write("            var num = oWidth/oHeight; \r\n");
      out.write("            imgall[i].width = 180*num; \r\n");
      out.write("            imgall[i].height = \"180\"; \r\n");
      out.write("        }\r\n");
      out.write("    }\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("\r\n");
      out.write("$(function(){ ReImgSize();});\r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("<style>\r\n");
      out.write("p\r\n");
      out.write("{\r\n");
      out.write("\tfont-family: Arial, Helvetica, sans-serif;\r\n");
      out.write("\tfont-size: 12px;\r\n");
      out.write("}\r\n");
      out.write("label\r\n");
      out.write("{\r\n");
      out.write("float:left;\r\n");
      out.write("width:10em;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("#userInfo input,#userInfo select{\r\n");
      out.write("\twidth:400px;\r\n");
      out.write("\tborder:1px solid #ccc;\r\n");
      out.write("\theight:22px;\r\n");
      out.write("}\r\n");
      out.write("#userInfo textarea{\r\n");
      out.write("\twidth:400px;\r\n");
      out.write("\theight: 120px;\r\n");
      out.write("\tborder:1px solid #ccc;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("span.write_own {\r\n");
      out.write("color:#757575;\r\n");
      out.write("display:block;\r\n");
      out.write("font-size:1.2em;\r\n");
      out.write("margin:0.1em 0;\r\n");
      out.write("padding-left: 9em;\r\n");
      out.write("}\r\n");
      out.write("\r\n");
      out.write("#verifyCode\r\n");
      out.write("{\r\n");
      out.write("text-align:bottom;\r\n");
      out.write("}\r\n");
      out.write(".ui-widget-content { border: 1px solid #72b42d; background: #ffffff url(images/ui-bg_inset-soft_20_566f57_1x100.png) 50% bottom repeat-x; color: #2A5333; }\r\n");
      out.write(".ui-widget-content a { color: #2A5333; }\r\n");
      out.write("</style>\r\n");
      out.write("\r\n");
      if (_jspx_meth_s_005fif_005f3(_jspx_page_context))
        return;
      out.write('\r');
      out.write('\n');
      if (_jspx_meth_s_005felse_005f1(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\r\n");
      out.write("</head>\r\n");
      out.write("<body>\r\n");
      out.write("<div class=\"fbbl_center\">\r\n");
      out.write("\t<ul>\r\n");
      out.write("\t   ");
      if (_jspx_meth_s_005fif_005f4(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\t\t");
      if (_jspx_meth_s_005felseif_005f1(_jspx_page_context))
        return;
      out.write(" \r\n");
      out.write("\t</ul>\r\n");
      out.write("\t<div id=\"userInfo\">\r\n");
      out.write("\t\t");
      if (_jspx_meth_s_005fproperty_005f2(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("\t\t");
      //  s:form
      org.apache.struts2.views.jsp.ui.FormTag _jspx_th_s_005fform_005f0 = (org.apache.struts2.views.jsp.ui.FormTag) _005fjspx_005ftagPool_005fs_005fform_0026_005ftheme_005fnamespace_005fname_005fid_005fenctype.get(org.apache.struts2.views.jsp.ui.FormTag.class);
      _jspx_th_s_005fform_005f0.setPageContext(_jspx_page_context);
      _jspx_th_s_005fform_005f0.setParent(null);
      // /jsp/register/register.jsp(270,2) name = namespace type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_s_005fform_005f0.setNamespace("/jsp/register");
      // /jsp/register/register.jsp(270,2) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_s_005fform_005f0.setId("registerForm");
      // /jsp/register/register.jsp(270,2) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_s_005fform_005f0.setName("registerForm");
      // /jsp/register/register.jsp(270,2) name = theme type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_s_005fform_005f0.setTheme("simple");
      // /jsp/register/register.jsp(270,2) name = enctype type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
      _jspx_th_s_005fform_005f0.setEnctype("multipart/form-data");
      int _jspx_eval_s_005fform_005f0 = _jspx_th_s_005fform_005f0.doStartTag();
      if (_jspx_eval_s_005fform_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
        if (_jspx_eval_s_005fform_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.pushBody();
          _jspx_th_s_005fform_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
          _jspx_th_s_005fform_005f0.doInitBody();
        }
        do {
          out.write("\r\n");
          out.write("\t\t");
          //  s:if
          org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f5 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
          _jspx_th_s_005fif_005f5.setPageContext(_jspx_page_context);
          _jspx_th_s_005fif_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
          // /jsp/register/register.jsp(271,2) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
          _jspx_th_s_005fif_005f5.setTest("action=='openRegister'||action=='register'");
          int _jspx_eval_s_005fif_005f5 = _jspx_th_s_005fif_005f5.doStartTag();
          if (_jspx_eval_s_005fif_005f5 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
            if (_jspx_eval_s_005fif_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.pushBody();
              _jspx_th_s_005fif_005f5.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
              _jspx_th_s_005fif_005f5.doInitBody();
            }
            do {
              out.write("\r\n");
              out.write("\t\t");
              if (_jspx_meth_s_005fhidden_005f0(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t");
              if (_jspx_meth_s_005fhidden_005f1(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\r\n");
              out.write("\t\t\r\n");
              out.write("\t\t\r\n");
              out.write("\t\t");
              if (_jspx_meth_s_005fif_005f6(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t<p>\r\n");
              out.write("\t\t \t<label>Invitation Code:</label>\r\n");
              out.write("\t\t \t<input type=\"text\" name=\"inviteCodeId\" value='");
              if (_jspx_meth_s_005fproperty_005f3(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("'>\r\n");
              out.write("\t\t </p>\r\n");
              out.write("\t\t<p style=\"color:red\">Items with * are required!</p>\r\n");
              out.write("\t\t<span>");
              if (_jspx_meth_s_005ffielderror_005f0(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("</span><br>\r\n");
              out.write("\t\t");
              if (_jspx_meth_s_005fif_005f7(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t<p>\r\n");
              out.write("\t\t\t <label> <font color=\"red\">* </font> Email:</label>\r\n");
              out.write("\t\t\t <span><input type=\"text\" name=\"user.EMail\" id=\"u_email\" value='");
              if (_jspx_meth_s_005fproperty_005f5(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("'></span>\r\n");
              out.write("\t\t\t <font color=\"red\">You will use this Email to log in to our system.</font>\r\n");
              out.write("\t \t</p>\r\n");
              out.write("\t \t<p>\r\n");
              out.write("\t\t\t<label><font color=\"red\">* </font>UserName:</label>\r\n");
              out.write("\t\t\t<span><input type=\"text\" id=\"u_username\" name=\"user.UserName\" value='");
              if (_jspx_meth_s_005fproperty_005f6(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("'> </span><font color=\"red\">You can also use this UserName to log in to our system.</font>\r\n");
              out.write("\t\t</p>\r\n");
              out.write("\t\t<p>\r\n");
              out.write("\t\t\t<label><font color=\"red\">* </font> Password:</label>\r\n");
              out.write("\t\t\t<input type=\"password\" name=\"i_password\" value=\"\" id=\"i_password\">\r\n");
              out.write("\t\t\t<input type=\"hidden\" name=\"user.Password\" value=\"\" id=\"t_password\">\r\n");
              out.write("\t\t</p>\r\n");
              out.write("\t\t<p>\r\n");
              out.write("\t\t\t<label><font color=\"red\">* </font>Confirm Password:</label>\r\n");
              out.write("\t\t\t<input type=\"password\" name=\"c_user.Password\" value=\"\" id=\"c_password\">\r\n");
              out.write("\t    </p>\r\n");
              out.write("\t    \r\n");
              out.write("\t     <p>\r\n");
              out.write("\t\t\t <label>First Name:</label>\r\n");
              out.write("\t\t\t <input type=\"text\" name=\"user.firstName\" value='");
              if (_jspx_meth_s_005fproperty_005f7(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("'>\r\n");
              out.write("\t\t </p>\r\n");
              out.write("\t\t <p>\r\n");
              out.write("\t\t\t <label>Last Name:</label>\r\n");
              out.write("\t\t\t <input type=\"text\" name=\"user.lastName\" value='");
              if (_jspx_meth_s_005fproperty_005f8(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("'>\r\n");
              out.write("\t\t </p>\r\n");
              out.write("\t\t <p>\r\n");
              out.write("\t\t\t <label>Telephone:</label>\r\n");
              out.write("\t\t\t <input type=\"text\" name=\"user.Telephone\" value='");
              if (_jspx_meth_s_005fproperty_005f9(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("'>\r\n");
              out.write("\t\t </p>\r\n");
              out.write("\t\t <p>\r\n");
              out.write("\t\t\t  <label>Detailed Address:</label>\r\n");
              out.write("\t\t\t  <textarea  name=\"user.Address\" >");
              if (_jspx_meth_s_005fproperty_005f10(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("</textarea>\r\n");
              out.write("\t\t </p>\r\n");
              out.write("\t\t <p>\r\n");
              out.write("\t\t\t <label>City:</label>\r\n");
              out.write("\t\t\t <input type=\"text\" name=\"user.addressCity\" value='");
              if (_jspx_meth_s_005fproperty_005f11(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("'>\r\n");
              out.write("\t\t </p>\r\n");
              out.write("\t\t <p>\r\n");
              out.write("\t\t\t <label>State:</label>\r\n");
              out.write("\t\t\t <div id=\"div_select_state\">\r\n");
              out.write("\t\t\t\t<select id=\"select_state\" name=\"user.addressState\" onchange=\"getstateValue();\"></select>\r\n");
              out.write("\t\t\t\t\r\n");
              out.write("\t\t\t</div>\r\n");
              out.write("\t\t\t\r\n");
              out.write("\t\t </p>\r\n");
              out.write("\t\t <p style=\"display:none\">\r\n");
              out.write("\t\t \t<label>&nbsp;</label>\r\n");
              out.write("\t\t \t<input id=\"write_state\" type=\"text\" name=\"\"> Other?\r\n");
              out.write("\t\t </p>\r\n");
              out.write("\t\t <p></p>\r\n");
              out.write("\t\t <p>\r\n");
              out.write("\t\t\t <label>ZIP/Postal Code:</label>\r\n");
              out.write("\t\t\t <input type=\"text\" name=\"user.addressZip\" value='");
              if (_jspx_meth_s_005fproperty_005f12(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("'>\r\n");
              out.write("\t\t </p>\r\n");
              out.write("\t\t <p>\r\n");
              out.write("\t\t\t <label>Country:</label>\r\n");
              out.write("\t\t\t <div id=\"div_select_country\">\r\n");
              out.write("\t\t\t\t\t<select id=\"select_country\" name=\"user.addressCountry\"></select>\r\n");
              out.write("\t\t\t</div>  \t\r\n");
              out.write("\t\t </p>\r\n");
              out.write("\t\t <p style=\"display:none\">\r\n");
              out.write("\t\t \t<label>&nbsp;</label>\r\n");
              out.write("\t\t \t<input id=\"write_country\" type=\"text\" name=\"\"> Other?\r\n");
              out.write("\t\t </p>\r\n");
              out.write("\t\t <p></p>\r\n");
              out.write("\t\t <p>\r\n");
              out.write("\t\t      <label id=\"verifyCode\"> Verify Image:</label>\r\n");
              out.write("\t\t      ");
              if (_jspx_meth_s_005furl_005f0(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("\r\n");
              out.write("\t\t\t  <img id=\"imgObj\" alt=\"\" src=\"");
              if (_jspx_meth_s_005fproperty_005f13(_jspx_th_s_005fif_005f5, _jspx_page_context))
                return;
              out.write("\"/> \r\n");
              out.write("\t\t\t   &nbsp;\r\n");
              out.write("\t\t\t  <a href=\"javascript:void(0)\" class=\"uiButton\" onclick=\"changeImg()\">change</a>  \r\n");
              out.write("\t\t  </p>\r\n");
              out.write("          <p>\r\n");
              out.write("\t\t\t  <label id=\"verifyCode\"><font color=\"red\">* </font> Verify Code:</label>\r\n");
              out.write("\t\t\t  <input id=\"veryCode\" name=\"veryCode\" type=\"text\"/>\r\n");
              out.write("\t\t  </p>\r\n");
              out.write("\t\t  \r\n");
              out.write("\t\t  \r\n");
              out.write("\t\t  \r\n");
              out.write("\t\t  \r\n");
              out.write("\t\t  \r\n");
              out.write("\t\t  \r\n");
              out.write("\t\t  \r\n");
              out.write("\t\t  <p>\r\n");
              out.write("\t\t      <font color=\"red\">* </font><strong>Please read &nbsp;</strong>\r\n");
              out.write("\t\t\t");

				if(flag==false){
			
              out.write("\r\n");
              out.write("\t\t\t\t<a target=\"_blank\" href=\"http://www.validfi.com/LTISystem/register__termuse1.action\">\r\n");
              out.write("\t\t      \t\t<strong>ValidFi.com's Terms of Use</strong>\r\n");
              out.write("\t\t\t \t</a>\r\n");
              out.write("\t\t\t");

				}else{
			
              out.write("\r\n");
              out.write("\t\t\t\t<a style=\"text-decoration: none;\" target=\"_blank\" href=\"http://www.myplaniq.com/LTISystem/register__termuse2.action\">\r\n");
              out.write("\t\t      \t\t<strong>MyPlanIQ.com's Terms of Use</strong>\r\n");
              out.write("\t\t\t \t</a>\r\n");
              out.write("\t\t\t");
  
				}		
				
				
				
              out.write("\r\n");
              out.write("\t\t  </p>\r\n");
              out.write("\t\t  <p>\r\n");
              out.write("\t\t      <td colspan=\"1\" align=\"center\">\r\n");
              out.write("              <input style=\"width:280px;height:26px\" id=\"submitbutton\" class=\"uiButton\" name=\"submitbutton\" type=\"button\"  value=\"I agree the Terms of Use, create an account.\" onclick=\"onclk()\"/>\r\n");
              out.write("              </td>\r\n");
              out.write("\t\t\t  <input type=\"reset\" class=\"uiButton\" name=\"reset\" value=\"Reset\" style=\"width:80px;height:26px\">\r\n");
              out.write("\t\t  </p>\r\n");
              out.write("\t\t <p>\r\n");
              out.write("\t\t       <div id=\"Message\"><input type=\"hidden\" size=\"55\" readonly id=\"actionMessage\"/></div> \r\n");
              out.write("\t\t </p>\r\n");
              out.write("\t\t");
              int evalDoAfterBody = _jspx_th_s_005fif_005f5.doAfterBody();
              if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
                break;
            } while (true);
            if (_jspx_eval_s_005fif_005f5 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
              out = _jspx_page_context.popBody();
            }
          }
          if (_jspx_th_s_005fif_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
            _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f5);
            return;
          }
          _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f5);
          out.write("\r\n");
          out.write("\t\t");
          if (_jspx_meth_s_005felseif_005f2(_jspx_th_s_005fform_005f0, _jspx_page_context))
            return;
          out.write(" \r\n");
          out.write("\t\t");
          int evalDoAfterBody = _jspx_th_s_005fform_005f0.doAfterBody();
          if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
            break;
        } while (true);
        if (_jspx_eval_s_005fform_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
          out = _jspx_page_context.popBody();
        }
      }
      if (_jspx_th_s_005fform_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
        _005fjspx_005ftagPool_005fs_005fform_0026_005ftheme_005fnamespace_005fname_005fid_005fenctype.reuse(_jspx_th_s_005fform_005f0);
        return;
      }
      _005fjspx_005ftagPool_005fs_005fform_0026_005ftheme_005fnamespace_005fname_005fid_005fenctype.reuse(_jspx_th_s_005fform_005f0);
      out.write("\r\n");
      out.write("\t</div>\r\n");
      out.write("\t");
      if (_jspx_meth_s_005fif_005f11(_jspx_page_context))
        return;
      out.write("\r\n");
      out.write("</div>\r\n");
      out.write("\r\n");
      out.write("</body>\r\n");
      out.write("</html>");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_s_005fif_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:if
    org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f0 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
    _jspx_th_s_005fif_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005fif_005f0.setParent(null);
    // /jsp/register/register.jsp(65,1) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fif_005f0.setTest("action=='viewUserDetails'||action=='updateUserDetails'");
    int _jspx_eval_s_005fif_005f0 = _jspx_th_s_005fif_005f0.doStartTag();
    if (_jspx_eval_s_005fif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fif_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fif_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fif_005f0.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t$(\"#emailsettings\").html(\"Loading Data...\");\r\n");
        out.write("\t$(\"#emailsettings\").load(\"ViewEmailList.action?includeHeader=false&time=\"+d.getTime());\r\n");
        out.write("\t$(\"#user_myaccount\").html(\"Loading Data...\");\r\n");
        out.write("\t$(\"#user_myaccount\").load(\"userMyAccount.action?includeHeader=false&time=\"+d.getTime());\r\n");
        out.write("\t$(\"#user_subscr\").html(\"Loading Data...\");\r\n");
        out.write("\t$(\"#user_subscr\").load(\"/LTISystem/paypal_center.action?includeHeader=false&time=\"+d.getTime());\r\n");
        out.write("\t_state='");
        if (_jspx_meth_s_005fproperty_005f0(_jspx_th_s_005fif_005f0, _jspx_page_context))
          return true;
        out.write("';\r\n");
        out.write("\t_country='");
        if (_jspx_meth_s_005fproperty_005f1(_jspx_th_s_005fif_005f0, _jspx_page_context))
          return true;
        out.write("';\r\n");
        out.write("\t");
        int evalDoAfterBody = _jspx_th_s_005fif_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fif_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f0);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f0 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f0);
    // /jsp/register/register.jsp(72,9) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f0.setValue("user.addressState");
    int _jspx_eval_s_005fproperty_005f0 = _jspx_th_s_005fproperty_005f0.doStartTag();
    if (_jspx_th_s_005fproperty_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f0);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f1 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f1.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f0);
    // /jsp/register/register.jsp(73,11) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f1.setValue("user.addressCountry");
    int _jspx_eval_s_005fproperty_005f1 = _jspx_th_s_005fproperty_005f1.doStartTag();
    if (_jspx_th_s_005fproperty_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f1);
    return false;
  }

  private boolean _jspx_meth_s_005fif_005f1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:if
    org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f1 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
    _jspx_th_s_005fif_005f1.setPageContext(_jspx_page_context);
    _jspx_th_s_005fif_005f1.setParent(null);
    // /jsp/register/register.jsp(77,2) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fif_005f1.setTest("action=='openRegister'||action=='register'");
    int _jspx_eval_s_005fif_005f1 = _jspx_th_s_005fif_005f1.doStartTag();
    if (_jspx_eval_s_005fif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fif_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fif_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fif_005f1.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\tvar email=$('#u_email').val();\r\n");
        out.write("\t\tvar password=$('#i_password').val();\r\n");
        out.write("\t\tvar c_password=$('#c_password').val();\r\n");
        out.write("\t\tvar u_name=$('#u_username').val();\r\n");
        out.write("\t\tvar r_state=$('#select_state').val();\r\n");
        out.write("\t\tvar r_country=$('#select_country').val();\r\n");
        out.write("\t\tvar se_country=$('#write_country').val();\r\n");
        out.write("\t\tvar select = document.getElementById(\"select_state\"); \r\n");
        out.write("\t\tvar select1 = document.getElementById(\"select_country\")\r\n");
        out.write("\t\tvar input = document.getElementById(\"write_state\");\r\n");
        out.write("\t\tvar input1 = document.getElementById(\"write_country\");\r\n");
        out.write("\t\tif(email==''){\r\n");
        out.write("\t\t\talert(\"Please input your email address.\");\r\n");
        out.write("\t\t\treturn;\r\n");
        out.write("\t\t}\r\n");
        out.write("\t\tif(u_name==''){\r\n");
        out.write("\t\t\talert(\"Please input your username.\");\r\n");
        out.write("\t\t\treturn;\r\n");
        out.write("\t\t}\r\n");
        out.write("\t\tvar rName=new RegExp(\"@\",\"g\");\r\n");
        out.write("\t\tif(rName.test(u_name)){\r\n");
        out.write("\t\t\talert(\"The \\\"@\\\" shouldn't be in username.Please input your username.\");\r\n");
        out.write("\t\t\treturn;\r\n");
        out.write("\t\t}\r\n");
        out.write("\t\tif(u_name.indexOf(\" \")!=-1){\r\n");
        out.write("\t\t\talert(\"The white space shouldn't be in username.Please input your username.\");\r\n");
        out.write("\t\t\treturn;\r\n");
        out.write("\t\t}\r\n");
        out.write("\t\tif(password==''||password.length<6){\r\n");
        out.write("\t\t\talert(\"Please input your new password with more than 6 characters.\");\r\n");
        out.write("\t\t\treturn;\r\n");
        out.write("\t\t}\r\n");
        out.write("\t\tif(c_password==''||c_password.length<6){\r\n");
        out.write("\t\t\talert(\"Please input your confirm password correctly.\");\r\n");
        out.write("\t\t\treturn;\r\n");
        out.write("\t\t}\r\n");
        out.write("\t\tif(c_password!=password){\r\n");
        out.write("\t\t\talert(\"Your confirm password is not the same as the password, please check!\");\r\n");
        out.write("\t\t\treturn;\r\n");
        out.write("\t\t}\r\n");
        out.write("\t\t$('#t_password').val(MD5(password));\r\n");
        out.write("\t\t//if(r_state!=\"\" && r_country!=\"United States\"){\r\n");
        out.write("\t\t//\talert(\"The selection of the country is not the United States.\\n You should not choose the state.\");\r\n");
        out.write("\t\t//\treturn;\r\n");
        out.write("\t\t//}\r\n");
        out.write("\t\tif(r_state==\"\"){\r\n");
        out.write("\t\t\tselect.name=\"\";\r\n");
        out.write("\t\t\tinput.name=\"user.addressState\";\r\n");
        out.write("\t\t\tinput.readOnly=false;\r\n");
        out.write("\t\t}\r\n");
        out.write("\t\tif(se_country!=null && se_country!=\"\"){\r\n");
        out.write("\t\t\tselect1.name=\"\";\r\n");
        out.write("\t\t\tinput1.name=\"user.addressCountry\";\r\n");
        out.write("\t\t}\r\n");
        out.write("\t\t\r\n");
        out.write("\t\tisRightCode();\r\n");
        out.write("\t\t");
        int evalDoAfterBody = _jspx_th_s_005fif_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fif_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f1);
    return false;
  }

  private boolean _jspx_meth_s_005felseif_005f0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:elseif
    org.apache.struts2.views.jsp.ElseIfTag _jspx_th_s_005felseif_005f0 = (org.apache.struts2.views.jsp.ElseIfTag) _005fjspx_005ftagPool_005fs_005felseif_0026_005ftest.get(org.apache.struts2.views.jsp.ElseIfTag.class);
    _jspx_th_s_005felseif_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005felseif_005f0.setParent(null);
    // /jsp/register/register.jsp(135,2) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005felseif_005f0.setTest("action=='viewUserDetails'||action=='updateUserDetails'");
    int _jspx_eval_s_005felseif_005f0 = _jspx_th_s_005felseif_005f0.doStartTag();
    if (_jspx_eval_s_005felseif_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005felseif_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005felseif_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005felseif_005f0.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\tvar  message=\"\";\r\n");
        out.write("\t\tvar r_state=$('#select_state').val();\r\n");
        out.write("\t\tvar r_country=$('#select_country').val();\r\n");
        out.write("\t\t//var ur_name=$('#ur_name').val();\r\n");
        out.write("\t\t//var ur_email=$('#u_email').val();\r\n");
        out.write("\t\tvar se_state=$('#write_state').val();\r\n");
        out.write("\t\tvar se_country=$('#write_country').val();\r\n");
        out.write("\t\tvar select = document.getElementById(\"select_state\"); \r\n");
        out.write("\t\tvar select1 = document.getElementById(\"select_country\")\r\n");
        out.write("\t\tvar input = document.getElementById(\"write_state\");\r\n");
        out.write("\t\tvar input1 = document.getElementById(\"write_country\");\r\n");
        out.write("\t\t//if(ur_name==''){\r\n");
        out.write("\t\t//\talert(\"The username should not be null.\");\r\n");
        out.write("\t\t//\treturn;\r\n");
        out.write("\t\t//}\r\n");
        out.write("\t\t//if(ur_email==''){\r\n");
        out.write("\t\t//\talert(\"The email should not be null.\");\r\n");
        out.write("\t\t//\treturn;\r\n");
        out.write("\t\t//}\r\n");
        out.write("\t\t//var rName=new RegExp(\"@\",\"g\");\r\n");
        out.write("\t\t//if(rName.test(ur_name)){\r\n");
        out.write("\t\t//\talert(\"The \\\"@\\\" shouldn't be in username.Please input your username.\");\r\n");
        out.write("\t\t//\treturn;\r\n");
        out.write("\t\t//}\r\n");
        out.write("\t\t//if(r_state!=\"\" && r_country!=\"United States\"){\r\n");
        out.write("\t\t//\tmessage=\"The selection of the country is not the United States.\\n You should not choose the state.\";\r\n");
        out.write("\t\t//\t//alert(message);\r\n");
        out.write("\t\t//\treturn message;\r\n");
        out.write("\t\t//}\r\n");
        out.write("\t\tif(r_state==\"\"){\r\n");
        out.write("\t\t\tselect.name=\"\";\r\n");
        out.write("\t\t\tinput.name=\"user.addressState\";\r\n");
        out.write("\t\t\tinput.readOnly=false;\r\n");
        out.write("\t\t}\r\n");
        out.write("\t\tif(se_country!=null && se_country!=\"\"){\r\n");
        out.write("\t\t\tselect1.name=\"\";\r\n");
        out.write("\t\t\tinput1.name=\"user.addressCountry\";\r\n");
        out.write("\t\t}\r\n");
        out.write("\t\t");
        if (_jspx_meth_s_005fif_005f2(_jspx_th_s_005felseif_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t");
        if (_jspx_meth_s_005felse_005f0(_jspx_th_s_005felseif_005f0, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t");
        int evalDoAfterBody = _jspx_th_s_005felseif_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005felseif_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005felseif_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005felseif_0026_005ftest.reuse(_jspx_th_s_005felseif_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005felseif_0026_005ftest.reuse(_jspx_th_s_005felseif_005f0);
    return false;
  }

  private boolean _jspx_meth_s_005fif_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:if
    org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f2 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
    _jspx_th_s_005fif_005f2.setPageContext(_jspx_page_context);
    _jspx_th_s_005fif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f0);
    // /jsp/register/register.jsp(174,2) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fif_005f2.setTest("isfacebook");
    int _jspx_eval_s_005fif_005f2 = _jspx_th_s_005fif_005f2.doStartTag();
    if (_jspx_eval_s_005fif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fif_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fif_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fif_005f2.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t  if(changePassword_fb()){\r\n");
        out.write("\t\t\tisRightCode();\r\n");
        out.write("\t\t  }\r\n");
        out.write("\t\t");
        int evalDoAfterBody = _jspx_th_s_005fif_005f2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fif_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f2);
    return false;
  }

  private boolean _jspx_meth_s_005felse_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:else
    org.apache.struts2.views.jsp.ElseTag _jspx_th_s_005felse_005f0 = (org.apache.struts2.views.jsp.ElseTag) _005fjspx_005ftagPool_005fs_005felse.get(org.apache.struts2.views.jsp.ElseTag.class);
    _jspx_th_s_005felse_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005felse_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f0);
    int _jspx_eval_s_005felse_005f0 = _jspx_th_s_005felse_005f0.doStartTag();
    if (_jspx_eval_s_005felse_005f0 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005felse_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005felse_005f0.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005felse_005f0.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\tif(checkPassword()){\r\n");
        out.write("\t\t\tisRightCode();\r\n");
        out.write("\t\t}\r\n");
        out.write("\t\t");
        int evalDoAfterBody = _jspx_th_s_005felse_005f0.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005felse_005f0 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005felse_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005felse.reuse(_jspx_th_s_005felse_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005felse.reuse(_jspx_th_s_005felse_005f0);
    return false;
  }

  private boolean _jspx_meth_s_005fif_005f3(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:if
    org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f3 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
    _jspx_th_s_005fif_005f3.setPageContext(_jspx_page_context);
    _jspx_th_s_005fif_005f3.setParent(null);
    // /jsp/register/register.jsp(246,0) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fif_005f3.setTest("action=='viewUserDetails'||action=='updateUserDetails'");
    int _jspx_eval_s_005fif_005f3 = _jspx_th_s_005fif_005f3.doStartTag();
    if (_jspx_eval_s_005fif_005f3 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fif_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fif_005f3.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fif_005f3.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("<title>My Account</title>\r\n");
        int evalDoAfterBody = _jspx_th_s_005fif_005f3.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fif_005f3 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fif_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f3);
    return false;
  }

  private boolean _jspx_meth_s_005felse_005f1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:else
    org.apache.struts2.views.jsp.ElseTag _jspx_th_s_005felse_005f1 = (org.apache.struts2.views.jsp.ElseTag) _005fjspx_005ftagPool_005fs_005felse.get(org.apache.struts2.views.jsp.ElseTag.class);
    _jspx_th_s_005felse_005f1.setPageContext(_jspx_page_context);
    _jspx_th_s_005felse_005f1.setParent(null);
    int _jspx_eval_s_005felse_005f1 = _jspx_th_s_005felse_005f1.doStartTag();
    if (_jspx_eval_s_005felse_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005felse_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005felse_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005felse_005f1.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("<title>Register Page</title>\r\n");
        int evalDoAfterBody = _jspx_th_s_005felse_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005felse_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005felse_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005felse.reuse(_jspx_th_s_005felse_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005felse.reuse(_jspx_th_s_005felse_005f1);
    return false;
  }

  private boolean _jspx_meth_s_005fif_005f4(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:if
    org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f4 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
    _jspx_th_s_005fif_005f4.setPageContext(_jspx_page_context);
    _jspx_th_s_005fif_005f4.setParent(null);
    // /jsp/register/register.jsp(257,4) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fif_005f4.setTest("action=='openRegister'||action=='register'");
    int _jspx_eval_s_005fif_005f4 = _jspx_th_s_005fif_005f4.doStartTag();
    if (_jspx_eval_s_005fif_005f4 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fif_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fif_005f4.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fif_005f4.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t\r\n");
        out.write("\t\t<li><a href=\"#userInfo\"><span>Register</span></a></li>\r\n");
        out.write("\t\t");
        int evalDoAfterBody = _jspx_th_s_005fif_005f4.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fif_005f4 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fif_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f4);
    return false;
  }

  private boolean _jspx_meth_s_005felseif_005f1(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:elseif
    org.apache.struts2.views.jsp.ElseIfTag _jspx_th_s_005felseif_005f1 = (org.apache.struts2.views.jsp.ElseIfTag) _005fjspx_005ftagPool_005fs_005felseif_0026_005ftest.get(org.apache.struts2.views.jsp.ElseIfTag.class);
    _jspx_th_s_005felseif_005f1.setPageContext(_jspx_page_context);
    _jspx_th_s_005felseif_005f1.setParent(null);
    // /jsp/register/register.jsp(261,2) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005felseif_005f1.setTest("action=='viewUserDetails'||action=='updateUserDetails'");
    int _jspx_eval_s_005felseif_005f1 = _jspx_th_s_005felseif_005f1.doStartTag();
    if (_jspx_eval_s_005felseif_005f1 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005felseif_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005felseif_005f1.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005felseif_005f1.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t    <li><a href=\"#userInfo\"><span>My Account Profile</span></a></li>\r\n");
        out.write("\t\t    <li><a href=\"#user_myaccount\"><span>My Risk Profile</span></a></li>\r\n");
        out.write("\t\t\t<li><a href=\"#emailsettings\"><span>My Email Alert Setting</span></a></li>\t\t\r\n");
        out.write("\t\t\t<li><a href=\"#user_subscr\"><span>My Subscription Center</span></a></li>\r\n");
        out.write("\t\t");
        int evalDoAfterBody = _jspx_th_s_005felseif_005f1.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005felseif_005f1 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005felseif_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005felseif_0026_005ftest.reuse(_jspx_th_s_005felseif_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005felseif_0026_005ftest.reuse(_jspx_th_s_005felseif_005f1);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f2(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f2 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f2.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f2.setParent(null);
    // /jsp/register/register.jsp(269,2) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f2.setValue("actionMessage");
    int _jspx_eval_s_005fproperty_005f2 = _jspx_th_s_005fproperty_005f2.doStartTag();
    if (_jspx_th_s_005fproperty_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f2);
    return false;
  }

  private boolean _jspx_meth_s_005fhidden_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:hidden
    org.apache.struts2.views.jsp.ui.HiddenTag _jspx_th_s_005fhidden_005f0 = (org.apache.struts2.views.jsp.ui.HiddenTag) _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fname_005fid_005fnobody.get(org.apache.struts2.views.jsp.ui.HiddenTag.class);
    _jspx_th_s_005fhidden_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005fhidden_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(272,2) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fhidden_005f0.setId("id");
    // /jsp/register/register.jsp(272,2) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fhidden_005f0.setName("user.ID");
    // /jsp/register/register.jsp(272,2) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fhidden_005f0.setValue("%{user.ID}");
    int _jspx_eval_s_005fhidden_005f0 = _jspx_th_s_005fhidden_005f0.doStartTag();
    if (_jspx_th_s_005fhidden_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fname_005fid_005fnobody.reuse(_jspx_th_s_005fhidden_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fname_005fid_005fnobody.reuse(_jspx_th_s_005fhidden_005f0);
    return false;
  }

  private boolean _jspx_meth_s_005fhidden_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:hidden
    org.apache.struts2.views.jsp.ui.HiddenTag _jspx_th_s_005fhidden_005f1 = (org.apache.struts2.views.jsp.ui.HiddenTag) _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fid_005fnobody.get(org.apache.struts2.views.jsp.ui.HiddenTag.class);
    _jspx_th_s_005fhidden_005f1.setPageContext(_jspx_page_context);
    _jspx_th_s_005fhidden_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(273,2) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fhidden_005f1.setId("cAction");
    // /jsp/register/register.jsp(273,2) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fhidden_005f1.setValue("%{action}");
    int _jspx_eval_s_005fhidden_005f1 = _jspx_th_s_005fhidden_005f1.doStartTag();
    if (_jspx_th_s_005fhidden_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fid_005fnobody.reuse(_jspx_th_s_005fhidden_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fid_005fnobody.reuse(_jspx_th_s_005fhidden_005f1);
    return false;
  }

  private boolean _jspx_meth_s_005fif_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:if
    org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f6 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
    _jspx_th_s_005fif_005f6.setPageContext(_jspx_page_context);
    _jspx_th_s_005fif_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(277,2) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fif_005f6.setTest("regType!=null && regType=='subscr'");
    int _jspx_eval_s_005fif_005f6 = _jspx_th_s_005fif_005f6.doStartTag();
    if (_jspx_eval_s_005fif_005f6 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fif_005f6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fif_005f6.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fif_005f6.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t<p style=\"color:red; font-size:1.3em\"><strong>Before you subscribe to MyPlanIQ, you need to register first. Afterward, you will be directed to the subscription page.</strong></p>\r\n");
        out.write("                <p>See <a href=\"/LTISystem/f401k__plancompare.action\">Subscription Plan Comparison</a> for subscription benefits.</p>\r\n");
        out.write("\t\t");
        if (_jspx_meth_s_005fhidden_005f2(_jspx_th_s_005fif_005f6, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t");
        int evalDoAfterBody = _jspx_th_s_005fif_005f6.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fif_005f6 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fif_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f6);
    return false;
  }

  private boolean _jspx_meth_s_005fhidden_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f6, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:hidden
    org.apache.struts2.views.jsp.ui.HiddenTag _jspx_th_s_005fhidden_005f2 = (org.apache.struts2.views.jsp.ui.HiddenTag) _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fname_005fid_005fnobody.get(org.apache.struts2.views.jsp.ui.HiddenTag.class);
    _jspx_th_s_005fhidden_005f2.setPageContext(_jspx_page_context);
    _jspx_th_s_005fhidden_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f6);
    // /jsp/register/register.jsp(280,2) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fhidden_005f2.setId("typeid");
    // /jsp/register/register.jsp(280,2) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fhidden_005f2.setName("type");
    // /jsp/register/register.jsp(280,2) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fhidden_005f2.setValue("subscrlogin");
    int _jspx_eval_s_005fhidden_005f2 = _jspx_th_s_005fhidden_005f2.doStartTag();
    if (_jspx_th_s_005fhidden_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fname_005fid_005fnobody.reuse(_jspx_th_s_005fhidden_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fname_005fid_005fnobody.reuse(_jspx_th_s_005fhidden_005f2);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f3 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f3.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(284,50) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f3.setValue("inviteCodeId");
    int _jspx_eval_s_005fproperty_005f3 = _jspx_th_s_005fproperty_005f3.doStartTag();
    if (_jspx_th_s_005fproperty_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f3);
    return false;
  }

  private boolean _jspx_meth_s_005ffielderror_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:fielderror
    org.apache.struts2.views.jsp.ui.FieldErrorTag _jspx_th_s_005ffielderror_005f0 = (org.apache.struts2.views.jsp.ui.FieldErrorTag) _005fjspx_005ftagPool_005fs_005ffielderror_0026_005fname_005fid_005fcssStyle_005fnobody.get(org.apache.struts2.views.jsp.ui.FieldErrorTag.class);
    _jspx_th_s_005ffielderror_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005ffielderror_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(287,8) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ffielderror_005f0.setName("username");
    // /jsp/register/register.jsp(287,8) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ffielderror_005f0.setId("username");
    // /jsp/register/register.jsp(287,8) name = cssStyle type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ffielderror_005f0.setCssStyle("color:red");
    int _jspx_eval_s_005ffielderror_005f0 = _jspx_th_s_005ffielderror_005f0.doStartTag();
    if (_jspx_th_s_005ffielderror_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ffielderror_0026_005fname_005fid_005fcssStyle_005fnobody.reuse(_jspx_th_s_005ffielderror_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ffielderror_0026_005fname_005fid_005fcssStyle_005fnobody.reuse(_jspx_th_s_005ffielderror_005f0);
    return false;
  }

  private boolean _jspx_meth_s_005fif_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:if
    org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f7 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
    _jspx_th_s_005fif_005f7.setPageContext(_jspx_page_context);
    _jspx_th_s_005fif_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(288,2) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fif_005f7.setTest("errString!=null");
    int _jspx_eval_s_005fif_005f7 = _jspx_th_s_005fif_005f7.doStartTag();
    if (_jspx_eval_s_005fif_005f7 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fif_005f7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fif_005f7.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fif_005f7.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t<p style=\"color:red\">");
        if (_jspx_meth_s_005fproperty_005f4(_jspx_th_s_005fif_005f7, _jspx_page_context))
          return true;
        out.write("</p>\r\n");
        out.write("\t\t");
        int evalDoAfterBody = _jspx_th_s_005fif_005f7.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fif_005f7 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fif_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f7);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f7, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f4 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f4.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f7);
    // /jsp/register/register.jsp(289,23) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f4.setValue("errString");
    int _jspx_eval_s_005fproperty_005f4 = _jspx_th_s_005fproperty_005f4.doStartTag();
    if (_jspx_th_s_005fproperty_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f4);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f5(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f5 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f5.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f5.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(293,67) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f5.setValue("user.EMail");
    int _jspx_eval_s_005fproperty_005f5 = _jspx_th_s_005fproperty_005f5.doStartTag();
    if (_jspx_th_s_005fproperty_005f5.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f5);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f5);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f6(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f6 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f6.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f6.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(298,72) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f6.setValue("user.UserName");
    int _jspx_eval_s_005fproperty_005f6 = _jspx_th_s_005fproperty_005f6.doStartTag();
    if (_jspx_th_s_005fproperty_005f6.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f6);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f6);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f7(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f7 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f7.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f7.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(312,52) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f7.setValue("user.firstName");
    int _jspx_eval_s_005fproperty_005f7 = _jspx_th_s_005fproperty_005f7.doStartTag();
    if (_jspx_th_s_005fproperty_005f7.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f7);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f7);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f8 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f8.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(316,51) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f8.setValue("user.lastName");
    int _jspx_eval_s_005fproperty_005f8 = _jspx_th_s_005fproperty_005f8.doStartTag();
    if (_jspx_th_s_005fproperty_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f8);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f9 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f9.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(320,52) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f9.setValue("user.Telephone");
    int _jspx_eval_s_005fproperty_005f9 = _jspx_th_s_005fproperty_005f9.doStartTag();
    if (_jspx_th_s_005fproperty_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f9);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f10 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f10.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(324,37) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f10.setValue("user.Address");
    int _jspx_eval_s_005fproperty_005f10 = _jspx_th_s_005fproperty_005f10.doStartTag();
    if (_jspx_th_s_005fproperty_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f10);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f11(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f11 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f11.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f11.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(328,54) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f11.setValue("user.addressCity");
    int _jspx_eval_s_005fproperty_005f11 = _jspx_th_s_005fproperty_005f11.doStartTag();
    if (_jspx_th_s_005fproperty_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f11);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f12(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f12 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f12.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f12.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(345,53) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f12.setValue("user.addressZip");
    int _jspx_eval_s_005fproperty_005f12 = _jspx_th_s_005fproperty_005f12.doStartTag();
    if (_jspx_th_s_005fproperty_005f12.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f12);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f12);
    return false;
  }

  private boolean _jspx_meth_s_005furl_005f0(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.apache.struts2.views.jsp.URLTag _jspx_th_s_005furl_005f0 = (org.apache.struts2.views.jsp.URLTag) _005fjspx_005ftagPool_005fs_005furl_0026_005fnamespace_005fid_005faction_005fnobody.get(org.apache.struts2.views.jsp.URLTag.class);
    _jspx_th_s_005furl_005f0.setPageContext(_jspx_page_context);
    _jspx_th_s_005furl_005f0.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(360,8) name = action type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005furl_005f0.setAction("Generate.action");
    // /jsp/register/register.jsp(360,8) name = namespace type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005furl_005f0.setNamespace("/jsp/verifycode");
    // /jsp/register/register.jsp(360,8) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005furl_005f0.setId("generateCode");
    int _jspx_eval_s_005furl_005f0 = _jspx_th_s_005furl_005f0.doStartTag();
    if (_jspx_th_s_005furl_005f0.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005furl_0026_005fnamespace_005fid_005faction_005fnobody.reuse(_jspx_th_s_005furl_005f0);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005furl_0026_005fnamespace_005fid_005faction_005fnobody.reuse(_jspx_th_s_005furl_005f0);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f13(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fif_005f5, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f13 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f13.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f13.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fif_005f5);
    // /jsp/register/register.jsp(361,34) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f13.setValue("generateCode");
    int _jspx_eval_s_005fproperty_005f13 = _jspx_th_s_005fproperty_005f13.doStartTag();
    if (_jspx_th_s_005fproperty_005f13.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f13);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f13);
    return false;
  }

  private boolean _jspx_meth_s_005felseif_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005fform_005f0, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:elseif
    org.apache.struts2.views.jsp.ElseIfTag _jspx_th_s_005felseif_005f2 = (org.apache.struts2.views.jsp.ElseIfTag) _005fjspx_005ftagPool_005fs_005felseif_0026_005ftest.get(org.apache.struts2.views.jsp.ElseIfTag.class);
    _jspx_th_s_005felseif_005f2.setPageContext(_jspx_page_context);
    _jspx_th_s_005felseif_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005fform_005f0);
    // /jsp/register/register.jsp(406,2) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005felseif_005f2.setTest("action=='viewUserDetails'||action=='updateUserDetails'");
    int _jspx_eval_s_005felseif_005f2 = _jspx_th_s_005felseif_005f2.doStartTag();
    if (_jspx_eval_s_005felseif_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005felseif_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005felseif_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005felseif_005f2.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t");
        if (_jspx_meth_s_005fhidden_005f3(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t");
        if (_jspx_meth_s_005fhidden_005f4(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t   <p>\r\n");
        out.write("\t\t       <span>");
        if (_jspx_meth_s_005ffielderror_005f1(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("</span>\r\n");
        out.write("\t\t\t   <label><font color=\"red\">* </font>UserName:</label>\r\n");
        out.write("\t\t\t   <input type=\"text\" id=\"ur_name\"name=\"user.UserName\" value='");
        if (_jspx_meth_s_005fproperty_005f14(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("' readOnly>\r\n");
        out.write("\t\t   </p>\r\n");
        out.write("\t\t    <p>\r\n");
        out.write("\t\t\t     <label> <font color=\"red\">* </font> Email:</label>\r\n");
        out.write("\t\t\t     <input type=\"text\" id=\"ur_email\" name=\"user.EMail\" value='");
        if (_jspx_meth_s_005fproperty_005f15(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("' readOnly>\r\n");
        out.write("\t\t     </p>\r\n");
        out.write("\t\t   <p style=\"display:none\">\r\n");
        out.write("\t\t\t\t<label>Current Password:</label>\r\n");
        out.write("\t\t\t\t<input type=\"password\" name=\"i_oldPassword\" id=\"i_oldPassword\"> \r\n");
        out.write("\t\t\t\t<input type=\"hidden\" name=\"oldPassword\" id=\"oldPassword\">\r\n");
        out.write("\t\t\t</p>\r\n");
        out.write("\t\t\t<p>\r\n");
        out.write("\t\t\t\t<label>New Password:</label>\r\n");
        out.write("\t\t\t\t<input type=\"password\" name=\"i_newPassword\" id=\"i_newPassword\">\r\n");
        out.write("\t\t\t\t<input type=\"hidden\" name=\"newPassword\" id=\"newPassword\"> \r\n");
        out.write("\t\t\t</p>\r\n");
        out.write("\t\t\t<p>\r\n");
        out.write("\t\t\t\t<label>Confirm Password:</label>\r\n");
        out.write("\t\t\t\t<input type=\"password\" name=\"i_c_newPassword\" id=\"i_c_newPassword\">\r\n");
        out.write("\t\t\t</p>\t\t\t\r\n");
        out.write("\t\t\r\n");
        out.write("\t\t");
        if (_jspx_meth_s_005ffielderror_005f2(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t    \r\n");
        out.write("\t\t    <p>\r\n");
        out.write("\t\t\t <label>First Name:</label>\r\n");
        out.write("\t\t\t <input type=\"text\" name=\"user.firstName\" value='");
        if (_jspx_meth_s_005fproperty_005f16(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("'>\r\n");
        out.write("\t\t\t </p>\r\n");
        out.write("\t\t \t<p>\r\n");
        out.write("\t\t\t <label>Last Name:</label>\r\n");
        out.write("\t\t\t <input type=\"text\" name=\"user.lastName\" value='");
        if (_jspx_meth_s_005fproperty_005f17(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("'>\r\n");
        out.write("\t\t\t </p>\r\n");
        out.write("\t\t\t<p>\r\n");
        out.write("\t\t\t\t<label>Telephone:</label>\r\n");
        out.write("\t\t\t\t<input type=\"text\" name=\"user.Telephone\" value='");
        if (_jspx_meth_s_005fproperty_005f18(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("'>\r\n");
        out.write("\t\t\t</p>\r\n");
        out.write("\t\t\t<p>\r\n");
        out.write("\t\t\t\t<label>Address:</label>\r\n");
        out.write("\t\t\t\t<textarea  name=\"user.Address\">");
        if (_jspx_meth_s_005fproperty_005f19(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("</textarea>\r\n");
        out.write("\t\t\t</p>\r\n");
        out.write("\t\t\t<p>\r\n");
        out.write("\t\t\t <label>City:</label>\r\n");
        out.write("\t\t\t <input type=\"text\" name=\"user.addressCity\" value='");
        if (_jspx_meth_s_005fproperty_005f20(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("'>\r\n");
        out.write("\t\t \t</p>\r\n");
        out.write("\t\t \t<p>\r\n");
        out.write("\t\t\t <label>State:</label>\r\n");
        out.write("\t\t\t <div id=\"div_select_state\">\r\n");
        out.write("\t\t\t \t<input type=\"hidden\" value='");
        if (_jspx_meth_s_005fproperty_005f21(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("' readOnly>\r\n");
        out.write("\t\t\t\t<select id=\"select_state\" name=\"user.addressState\" onchange=\"getstateValue();\"></select>\r\n");
        out.write("\t\t\t</div>\r\n");
        out.write("\t\t \t</p>\r\n");
        out.write("\t\t \t<p style=\"display:none\">\r\n");
        out.write("\t\t \t\t<label>&nbsp;</label>\r\n");
        out.write("\t\t \t\t\t<input id=\"write_state\" type=\"text\" name=\"\"> Other?\r\n");
        out.write("\t\t \t</p>\t\t \t\t\r\n");
        out.write("\t\t \t<p></p>\r\n");
        out.write("\t\t \t<p>\r\n");
        out.write("\t\t\t <label>ZIP/Postal Code:</label>\r\n");
        out.write("\t\t\t <input type=\"text\" name=\"user.addressZip\" value='");
        if (_jspx_meth_s_005fproperty_005f22(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("'>\r\n");
        out.write("\t\t \t</p>\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t\t <p>\r\n");
        out.write("\t\t\t <label>Country:</label>\r\n");
        out.write("\t\t\t <div id=\"div_select_country\">\r\n");
        out.write("\t\t\t \t\t<input type=\"hidden\" value='");
        if (_jspx_meth_s_005fproperty_005f23(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("'readOnly>\r\n");
        out.write("\t\t\t\t\t<select id=\"select_country\" name=\"user.addressCountry\"></select>\r\n");
        out.write("\t\t\t</div>\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t \t</p>\r\n");
        out.write("\t\t \t<p style=\"display:none\">\r\n");
        out.write("\t\t \t<label>&nbsp;</label>\r\n");
        out.write("\t\t\t\t<input id=\"write_country\" type=\"text\" name=\"\"> Other?\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t</p>\r\n");
        out.write("\t\t \t<p></p>\r\n");
        out.write("\t\t\t<p>\r\n");
        out.write("\t\t\t    <label id=\"verifyCode\"> Verify Image:</label>\r\n");
        out.write("\t\t      \t");
        if (_jspx_meth_s_005furl_005f1(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t  \t<img id=\"imgObj\" alt=\"\" src=\"");
        if (_jspx_meth_s_005fproperty_005f24(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("\"/> \r\n");
        out.write("\t\t\t   \t&nbsp;\r\n");
        out.write("\t\t\t  <a href=\"javascript:void(0)\" class=\"uiButton\" onclick=\"changeImg()\">change</a> \r\n");
        out.write("\t\t\t </p>\r\n");
        out.write("\t         <p>\r\n");
        out.write("\t\t\t\t<label id=\"verifyCode\"><font color=\"red\">* </font> Verify Code:</label>\r\n");
        out.write("\t\t\t\t<input id=\"veryCode\" name=\"veryCode\" type=\"text\"/>\r\n");
        out.write("\t\t\t</p>\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t<div ");
        if (_jspx_meth_s_005fif_005f8(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write(">\r\n");
        out.write("\t\t \t<fieldset style=\"border:1px solid #ccc;padding-left:5px\">\r\n");
        out.write("\t\t\t    <legend style=\"margin-left:10px\">For Advisor</legend>\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t<p>\r\n");
        out.write("\t\t \t<div id=\"chart\">\r\n");
        out.write("\t\t \t<img alt=\"\" src=\"/LTISystem/");
        if (_jspx_meth_s_005fproperty_005f25(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("\" border=0>\r\n");
        out.write("\t\t \t</div>\t\t \t \r\n");
        out.write("\t\t\t <label>Logo:</label>\r\n");
        out.write("\t\t\t <input type=\"hidden\" name=\"user.logo\" value='");
        if (_jspx_meth_s_005fproperty_005f26(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("'>\r\n");
        out.write("\t\t\t <input type=\"file\" name=\"logoFile\">\r\n");
        out.write("\t\t \t</p>\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t<p>\r\n");
        out.write("\t\t\t <label>Company:</label>\r\n");
        out.write("\t\t\t <input type=\"text\" name=\"user.company\" value='");
        if (_jspx_meth_s_005fproperty_005f27(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("'>\r\n");
        out.write("\t\t \t</p>\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t<p>\r\n");
        out.write("\t\t\t <label>License:</label>\r\n");
        out.write("\t\t\t <input type=\"text\" name=\"user.license\" value='");
        if (_jspx_meth_s_005fproperty_005f28(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("'>\r\n");
        out.write("\t\t \t</p>\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t<p>\r\n");
        out.write("\t\t\t <label>Description:</label>\r\n");
        out.write("\t\t\t <textarea id=\"description\" name=\"user.description\" >");
        if (_jspx_meth_s_005fproperty_005f29(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("</textarea>\r\n");
        out.write("\t\t \t</p>\r\n");
        out.write("\t\t \t<script>\r\n");
        out.write("\r\n");
        out.write("\t\t \ttinyMCE.init({\r\n");
        out.write("\t\t\t    mode : \"none\",\r\n");
        out.write("\t\t\t    theme : \"advanced\",\r\n");
        out.write("\t\t\t    plugins : \"save\",\r\n");
        out.write("\t\t\t\ttheme_advanced_buttons1 : \"bold,italic,underline,strikethrough,|,fontselect,fontsizeselect\",\r\n");
        out.write("\t\t\t\ttheme_advanced_buttons2 : \"bullist,numlist,|,outdent,indent,blockquote,link,unlink,anchor,image,|,forecolor,backcolor\",\r\n");
        out.write("\t\t\t\ttheme_advanced_buttons3 : \"\",\r\n");
        out.write("\t\t\t\ttheme_advanced_toolbar_location : \"top\",\r\n");
        out.write("\t\t\t\ttheme_advanced_toolbar_align : \"left\",\r\n");
        out.write("\t\t\t\ttheme_advanced_statusbar_location : \"bottom\",\r\n");
        out.write("\t\t\t\tsave_onsavecallback : \"save_for_mce\"\r\n");
        out.write("\t\t\t});\r\n");
        out.write("\t\t\t$(function() {\r\n");
        out.write("\t\t\t\ttinyMCE.execCommand('mceAddControl', false, \"description\");\r\n");
        out.write("\t\t\t});\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t</script>\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t</fieldset>\r\n");
        out.write("\t\t \t\r\n");
        out.write("\t\t \t</div>\r\n");
        out.write("\t\t \t");
        if (_jspx_meth_s_005fif_005f9(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t<input type=\"hidden\" name=\"user.createdDate\" value='");
        if (_jspx_meth_s_005fproperty_005f30(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("'>\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t\r\n");
        out.write("\t\t\t<div align=\"right\" style=\"width:298px;text-align:left\">\r\n");
        out.write("\t\t\t\t<!--<input type=\"button\" class=\"uiButton\" name=\"Submit\" value=\"Submit\" onclick=\"onclk()\">  -->\r\n");
        out.write("\t\t\t\t<a href=\"javascript:void(0);\" class=\"uiButton\" id=\"pass_dialog\"> Please Submit</a>\r\n");
        out.write("\t\t\t</div>\r\n");
        out.write("\t\t");
        if (_jspx_meth_s_005fif_005f10(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t");
        if (_jspx_meth_s_005felse_005f2(_jspx_th_s_005felseif_005f2, _jspx_page_context))
          return true;
        out.write("\r\n");
        out.write("\t\t\t<div id=\"password_dialog\" height=\"250px\" style=\"display:none\">\r\n");
        out.write("\t\t\t\t<div id=\"dialog-confirm\" title=\"Please enter the current password\" align='left' style=\"text-align: left\">\r\n");
        out.write("\t\t\t\t<br>\r\n");
        out.write("\t\t\t\t\t<p><span class=\"ui-icon ui-icon-alert\" style=\"float:left; margin:0 7px 20px 0;\"></span>\r\n");
        out.write("\t\t\t\t\t\t<b>Please enter the current password to continue the operation.</b>\r\n");
        out.write("\t\t\t\t\t</p>\r\n");
        out.write("\t\t\t\t\t<p>\r\n");
        out.write("\t\t\t\t\t\t<label>Current Password:</label>\r\n");
        out.write("\t\t\t\t\t\t<input type=\"password\" name=\"i_curPassword\" id=\"i_curPassword\">\r\n");
        out.write("\t\t\t\t\t</p>\r\n");
        out.write("\t\t\t\t\t<div id=\"password_message\"></div>\r\n");
        out.write("\t\t\t\t</div>\r\n");
        out.write("\t\t\t</div>\r\n");
        out.write("\t\t<div id=\"Message\"><input type=\"hidden\" size=\"41\" readonly id=\"actionMessage\"/></div> \r\n");
        out.write("\t\t");
        int evalDoAfterBody = _jspx_th_s_005felseif_005f2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005felseif_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005felseif_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005felseif_0026_005ftest.reuse(_jspx_th_s_005felseif_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005felseif_0026_005ftest.reuse(_jspx_th_s_005felseif_005f2);
    return false;
  }

  private boolean _jspx_meth_s_005fhidden_005f3(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:hidden
    org.apache.struts2.views.jsp.ui.HiddenTag _jspx_th_s_005fhidden_005f3 = (org.apache.struts2.views.jsp.ui.HiddenTag) _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fname_005fid_005fnobody.get(org.apache.struts2.views.jsp.ui.HiddenTag.class);
    _jspx_th_s_005fhidden_005f3.setPageContext(_jspx_page_context);
    _jspx_th_s_005fhidden_005f3.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(407,2) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fhidden_005f3.setId("id");
    // /jsp/register/register.jsp(407,2) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fhidden_005f3.setName("user.ID");
    // /jsp/register/register.jsp(407,2) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fhidden_005f3.setValue("%{user.ID}");
    int _jspx_eval_s_005fhidden_005f3 = _jspx_th_s_005fhidden_005f3.doStartTag();
    if (_jspx_th_s_005fhidden_005f3.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fname_005fid_005fnobody.reuse(_jspx_th_s_005fhidden_005f3);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fname_005fid_005fnobody.reuse(_jspx_th_s_005fhidden_005f3);
    return false;
  }

  private boolean _jspx_meth_s_005fhidden_005f4(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:hidden
    org.apache.struts2.views.jsp.ui.HiddenTag _jspx_th_s_005fhidden_005f4 = (org.apache.struts2.views.jsp.ui.HiddenTag) _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fid_005fnobody.get(org.apache.struts2.views.jsp.ui.HiddenTag.class);
    _jspx_th_s_005fhidden_005f4.setPageContext(_jspx_page_context);
    _jspx_th_s_005fhidden_005f4.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(408,2) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fhidden_005f4.setId("cAction");
    // /jsp/register/register.jsp(408,2) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fhidden_005f4.setValue("%{action}");
    int _jspx_eval_s_005fhidden_005f4 = _jspx_th_s_005fhidden_005f4.doStartTag();
    if (_jspx_th_s_005fhidden_005f4.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fid_005fnobody.reuse(_jspx_th_s_005fhidden_005f4);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fhidden_0026_005fvalue_005fid_005fnobody.reuse(_jspx_th_s_005fhidden_005f4);
    return false;
  }

  private boolean _jspx_meth_s_005ffielderror_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:fielderror
    org.apache.struts2.views.jsp.ui.FieldErrorTag _jspx_th_s_005ffielderror_005f1 = (org.apache.struts2.views.jsp.ui.FieldErrorTag) _005fjspx_005ftagPool_005fs_005ffielderror_0026_005fname_005fid_005fcssStyle_005fnobody.get(org.apache.struts2.views.jsp.ui.FieldErrorTag.class);
    _jspx_th_s_005ffielderror_005f1.setPageContext(_jspx_page_context);
    _jspx_th_s_005ffielderror_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(410,15) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ffielderror_005f1.setName("username");
    // /jsp/register/register.jsp(410,15) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ffielderror_005f1.setId("username");
    // /jsp/register/register.jsp(410,15) name = cssStyle type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ffielderror_005f1.setCssStyle("color:red");
    int _jspx_eval_s_005ffielderror_005f1 = _jspx_th_s_005ffielderror_005f1.doStartTag();
    if (_jspx_th_s_005ffielderror_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ffielderror_0026_005fname_005fid_005fcssStyle_005fnobody.reuse(_jspx_th_s_005ffielderror_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ffielderror_0026_005fname_005fid_005fcssStyle_005fnobody.reuse(_jspx_th_s_005ffielderror_005f1);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f14(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f14 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f14.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f14.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(412,65) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f14.setValue("user.UserName");
    int _jspx_eval_s_005fproperty_005f14 = _jspx_th_s_005fproperty_005f14.doStartTag();
    if (_jspx_th_s_005fproperty_005f14.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f14);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f14);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f15(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f15 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f15.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f15.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(416,66) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f15.setValue("user.EMail");
    int _jspx_eval_s_005fproperty_005f15 = _jspx_th_s_005fproperty_005f15.doStartTag();
    if (_jspx_th_s_005fproperty_005f15.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f15);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f15);
    return false;
  }

  private boolean _jspx_meth_s_005ffielderror_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:fielderror
    org.apache.struts2.views.jsp.ui.FieldErrorTag _jspx_th_s_005ffielderror_005f2 = (org.apache.struts2.views.jsp.ui.FieldErrorTag) _005fjspx_005ftagPool_005fs_005ffielderror_0026_005fname_005fid_005fcssStyle_005fnobody.get(org.apache.struts2.views.jsp.ui.FieldErrorTag.class);
    _jspx_th_s_005ffielderror_005f2.setPageContext(_jspx_page_context);
    _jspx_th_s_005ffielderror_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(433,2) name = name type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ffielderror_005f2.setName("password");
    // /jsp/register/register.jsp(433,2) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ffielderror_005f2.setId("password");
    // /jsp/register/register.jsp(433,2) name = cssStyle type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005ffielderror_005f2.setCssStyle("color:red");
    int _jspx_eval_s_005ffielderror_005f2 = _jspx_th_s_005ffielderror_005f2.doStartTag();
    if (_jspx_th_s_005ffielderror_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005ffielderror_0026_005fname_005fid_005fcssStyle_005fnobody.reuse(_jspx_th_s_005ffielderror_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005ffielderror_0026_005fname_005fid_005fcssStyle_005fnobody.reuse(_jspx_th_s_005ffielderror_005f2);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f16(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f16 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f16.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f16.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(437,52) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f16.setValue("user.firstName");
    int _jspx_eval_s_005fproperty_005f16 = _jspx_th_s_005fproperty_005f16.doStartTag();
    if (_jspx_th_s_005fproperty_005f16.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f16);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f16);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f17(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f17 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f17.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f17.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(441,51) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f17.setValue("user.lastName");
    int _jspx_eval_s_005fproperty_005f17 = _jspx_th_s_005fproperty_005f17.doStartTag();
    if (_jspx_th_s_005fproperty_005f17.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f17);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f17);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f18(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f18 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f18.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f18.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(445,52) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f18.setValue("user.Telephone");
    int _jspx_eval_s_005fproperty_005f18 = _jspx_th_s_005fproperty_005f18.doStartTag();
    if (_jspx_th_s_005fproperty_005f18.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f18);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f18);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f19(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f19 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f19.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f19.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(449,35) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f19.setValue("user.Address");
    int _jspx_eval_s_005fproperty_005f19 = _jspx_th_s_005fproperty_005f19.doStartTag();
    if (_jspx_th_s_005fproperty_005f19.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f19);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f19);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f20(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f20 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f20.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f20.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(453,54) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f20.setValue("user.addressCity");
    int _jspx_eval_s_005fproperty_005f20 = _jspx_th_s_005fproperty_005f20.doStartTag();
    if (_jspx_th_s_005fproperty_005f20.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f20);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f20);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f21(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f21 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f21.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f21.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(458,33) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f21.setValue("user.addressState");
    int _jspx_eval_s_005fproperty_005f21 = _jspx_th_s_005fproperty_005f21.doStartTag();
    if (_jspx_th_s_005fproperty_005f21.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f21);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f21);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f22(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f22 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f22.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f22.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(469,53) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f22.setValue("user.addressZip");
    int _jspx_eval_s_005fproperty_005f22 = _jspx_th_s_005fproperty_005f22.doStartTag();
    if (_jspx_th_s_005fproperty_005f22.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f22);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f22);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f23(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f23 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f23.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f23.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(485,34) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f23.setValue("user.addressCountry");
    int _jspx_eval_s_005fproperty_005f23 = _jspx_th_s_005fproperty_005f23.doStartTag();
    if (_jspx_th_s_005fproperty_005f23.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f23);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f23);
    return false;
  }

  private boolean _jspx_meth_s_005furl_005f1(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:url
    org.apache.struts2.views.jsp.URLTag _jspx_th_s_005furl_005f1 = (org.apache.struts2.views.jsp.URLTag) _005fjspx_005ftagPool_005fs_005furl_0026_005fnamespace_005fid_005faction_005fnobody.get(org.apache.struts2.views.jsp.URLTag.class);
    _jspx_th_s_005furl_005f1.setPageContext(_jspx_page_context);
    _jspx_th_s_005furl_005f1.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(498,9) name = action type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005furl_005f1.setAction("Generate.action");
    // /jsp/register/register.jsp(498,9) name = namespace type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005furl_005f1.setNamespace("/jsp/verifycode");
    // /jsp/register/register.jsp(498,9) name = id type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005furl_005f1.setId("generateCode");
    int _jspx_eval_s_005furl_005f1 = _jspx_th_s_005furl_005f1.doStartTag();
    if (_jspx_th_s_005furl_005f1.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005furl_0026_005fnamespace_005fid_005faction_005fnobody.reuse(_jspx_th_s_005furl_005f1);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005furl_0026_005fnamespace_005fid_005faction_005fnobody.reuse(_jspx_th_s_005furl_005f1);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f24(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f24 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f24.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f24.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(499,35) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f24.setValue("generateCode");
    int _jspx_eval_s_005fproperty_005f24 = _jspx_th_s_005fproperty_005f24.doStartTag();
    if (_jspx_th_s_005fproperty_005f24.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f24);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f24);
    return false;
  }

  private boolean _jspx_meth_s_005fif_005f8(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:if
    org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f8 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
    _jspx_th_s_005fif_005f8.setPageContext(_jspx_page_context);
    _jspx_th_s_005fif_005f8.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(511,8) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fif_005f8.setTest("advisor==false");
    int _jspx_eval_s_005fif_005f8 = _jspx_th_s_005fif_005f8.doStartTag();
    if (_jspx_eval_s_005fif_005f8 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fif_005f8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fif_005f8.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fif_005f8.doInitBody();
      }
      do {
        out.write("style=\"display:none\"");
        int evalDoAfterBody = _jspx_th_s_005fif_005f8.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fif_005f8 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fif_005f8.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f8);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f8);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f25(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f25 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f25.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f25.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(518,32) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f25.setValue("user.logo");
    int _jspx_eval_s_005fproperty_005f25 = _jspx_th_s_005fproperty_005f25.doStartTag();
    if (_jspx_th_s_005fproperty_005f25.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f25);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f25);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f26(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f26 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f26.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f26.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(521,49) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f26.setValue("user.logo");
    int _jspx_eval_s_005fproperty_005f26 = _jspx_th_s_005fproperty_005f26.doStartTag();
    if (_jspx_th_s_005fproperty_005f26.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f26);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f26);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f27(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f27 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f27.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f27.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(527,50) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f27.setValue("user.company");
    int _jspx_eval_s_005fproperty_005f27 = _jspx_th_s_005fproperty_005f27.doStartTag();
    if (_jspx_th_s_005fproperty_005f27.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f27);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f27);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f28(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f28 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f28.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f28.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(532,50) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f28.setValue("user.license");
    int _jspx_eval_s_005fproperty_005f28 = _jspx_th_s_005fproperty_005f28.doStartTag();
    if (_jspx_th_s_005fproperty_005f28.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f28);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f28);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f29(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f29 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f29.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f29.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(537,56) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f29.setValue("user.description");
    int _jspx_eval_s_005fproperty_005f29 = _jspx_th_s_005fproperty_005f29.doStartTag();
    if (_jspx_th_s_005fproperty_005f29.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f29);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f29);
    return false;
  }

  private boolean _jspx_meth_s_005fif_005f9(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:if
    org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f9 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
    _jspx_th_s_005fif_005f9.setPageContext(_jspx_page_context);
    _jspx_th_s_005fif_005f9.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(562,4) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fif_005f9.setTest("advisor==true");
    int _jspx_eval_s_005fif_005f9 = _jspx_th_s_005fif_005f9.doStartTag();
    if (_jspx_eval_s_005fif_005f9 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fif_005f9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fif_005f9.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fif_005f9.doInitBody();
      }
      do {
        out.write("<br>");
        int evalDoAfterBody = _jspx_th_s_005fif_005f9.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fif_005f9 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fif_005f9.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f9);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f9);
    return false;
  }

  private boolean _jspx_meth_s_005fproperty_005f30(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:property
    org.apache.struts2.views.jsp.PropertyTag _jspx_th_s_005fproperty_005f30 = (org.apache.struts2.views.jsp.PropertyTag) _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.get(org.apache.struts2.views.jsp.PropertyTag.class);
    _jspx_th_s_005fproperty_005f30.setPageContext(_jspx_page_context);
    _jspx_th_s_005fproperty_005f30.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(565,55) name = value type = java.lang.String reqTime = false required = false fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fproperty_005f30.setValue("user.createdDate");
    int _jspx_eval_s_005fproperty_005f30 = _jspx_th_s_005fproperty_005f30.doStartTag();
    if (_jspx_th_s_005fproperty_005f30.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f30);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fproperty_0026_005fvalue_005fnobody.reuse(_jspx_th_s_005fproperty_005f30);
    return false;
  }

  private boolean _jspx_meth_s_005fif_005f10(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:if
    org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f10 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
    _jspx_th_s_005fif_005f10.setPageContext(_jspx_page_context);
    _jspx_th_s_005fif_005f10.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    // /jsp/register/register.jsp(588,2) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fif_005f10.setTest("isfacebook");
    int _jspx_eval_s_005fif_005f10 = _jspx_th_s_005fif_005f10.doStartTag();
    if (_jspx_eval_s_005fif_005f10 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fif_005f10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fif_005f10.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fif_005f10.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t   <script type=\"text/javascript\">\r\n");
        out.write("\t\t\t$('#pass_dialog').click(function(){\r\n");
        out.write("\t\t\t  onclk();\r\n");
        out.write("\t\t\t});\r\n");
        out.write("\t\t\t</script>\r\n");
        out.write("\t\t");
        int evalDoAfterBody = _jspx_th_s_005fif_005f10.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fif_005f10 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fif_005f10.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f10);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f10);
    return false;
  }

  private boolean _jspx_meth_s_005felse_005f2(javax.servlet.jsp.tagext.JspTag _jspx_th_s_005felseif_005f2, PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:else
    org.apache.struts2.views.jsp.ElseTag _jspx_th_s_005felse_005f2 = (org.apache.struts2.views.jsp.ElseTag) _005fjspx_005ftagPool_005fs_005felse.get(org.apache.struts2.views.jsp.ElseTag.class);
    _jspx_th_s_005felse_005f2.setPageContext(_jspx_page_context);
    _jspx_th_s_005felse_005f2.setParent((javax.servlet.jsp.tagext.Tag) _jspx_th_s_005felseif_005f2);
    int _jspx_eval_s_005felse_005f2 = _jspx_th_s_005felse_005f2.doStartTag();
    if (_jspx_eval_s_005felse_005f2 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005felse_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005felse_005f2.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005felse_005f2.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t\t<script type=\"text/javascript\">\r\n");
        out.write("\t\t\t$('#pass_dialog').click(function(){\r\n");
        out.write("\t\t\t\t$(\"#dialog-confirm\").dialog({\r\n");
        out.write("\t\t\t\t\tautoOpen: false,\r\n");
        out.write("\t\t    \t\t//resizable: true,\r\n");
        out.write("\t\t\t\t\theight:210,\r\n");
        out.write("\t\t\t\t\twidth: 420,\r\n");
        out.write("\t\t\t\t\tmodal: true,\r\n");
        out.write("\t\t\t\t\tbuttons: {\r\n");
        out.write("\t\t\t\t\t\tCancel: function() {\r\n");
        out.write("\t\t\t\t\t\t\t$(this).dialog('close');\r\n");
        out.write("\t\t\t\t\t\t},\r\n");
        out.write("\t\t\t\t\t\t'Confirm': function() {\r\n");
        out.write("\t\t\t\t\t\t\tcurrentpassword = $(\"#i_curPassword\").val();\r\n");
        out.write("\t\t\t\t\t\t\t$('#i_oldPassword').val(currentpassword);\r\n");
        out.write("\t\t\t\t\t\t\tcode = \"action=permission&includeHeader=false&curPassword=\"+currentpassword;\r\n");
        out.write("\t\t\t\t\t\t\t$(\"#password_message\").html(\"Connecting to the server...\");\r\n");
        out.write("\t\t\t\t\t\t\t$.ajax({   \r\n");
        out.write("           \t\t\t\t\t\ttype:\"POST\",   \r\n");
        out.write("           \t\t\t\t\t\turl:\"PasswordReset.action\",   \r\n");
        out.write("           \t\t\t\t\t\tdata:code, \r\n");
        out.write("           \t\t\t\t\t\terror: function(){\r\n");
        out.write("    \t   \t\t\t\t\t\t\t$(\"#password_message\").val(\"Failed to access the server\"); \r\n");
        out.write("       \t\t\t\t\t\t\t},\r\n");
        out.write("           \t\t\t\t\t\tsuccess: function(mesg){\r\n");
        out.write("         \t\t\t\t\t\t\t$(\"#password_message\").html(mesg);\r\n");
        out.write("         \t\t\t\t\t\t\tsumesg = \"You current password is right.\";\r\n");
        out.write("         \t\t\t\t\t\t\tif(mesg.indexOf(sumesg)>0){\r\n");
        out.write("         \t\t\t\t\t\t\t\t$(\"#password_message\").html(sumesg);\r\n");
        out.write("         \t\t\t\t\t\t\t\tmsg=onclk();\r\n");
        out.write("         \t\t\t\t\t\t\t\tif(msg.toString()!=\"\")\r\n");
        out.write("         \t\t\t\t\t\t\t\t\t$(\"#password_message\").html(sumesg+\"<br/>\"+msg);\r\n");
        out.write("         \t\t\t\t\t\t\t\t$(this).dialog('close');\t\t\r\n");
        out.write("         \t\t\t\t\t\t\t}\r\n");
        out.write("         \t\t\t\t\t\t}\r\n");
        out.write("       \t\t\t\t\t\t});  \r\n");
        out.write("\t\t\t\t\t\t}\r\n");
        out.write("\t\t\t\t\t}\r\n");
        out.write("\t\t\t\t});\r\n");
        out.write("\t\t\t\t$(\"#dialog-confirm\").dialog(\"open\");\r\n");
        out.write("   \t\t\t});\r\n");
        out.write("   \t\t\t</script>\r\n");
        out.write("   \t\t");
        int evalDoAfterBody = _jspx_th_s_005felse_005f2.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005felse_005f2 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005felse_005f2.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005felse.reuse(_jspx_th_s_005felse_005f2);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005felse.reuse(_jspx_th_s_005felse_005f2);
    return false;
  }

  private boolean _jspx_meth_s_005fif_005f11(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  s:if
    org.apache.struts2.views.jsp.IfTag _jspx_th_s_005fif_005f11 = (org.apache.struts2.views.jsp.IfTag) _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.get(org.apache.struts2.views.jsp.IfTag.class);
    _jspx_th_s_005fif_005f11.setPageContext(_jspx_page_context);
    _jspx_th_s_005fif_005f11.setParent(null);
    // /jsp/register/register.jsp(656,1) name = test type = java.lang.String reqTime = false required = true fragment = false deferredValue = false expectedTypeName = null deferredMethod = false methodSignature = null
    _jspx_th_s_005fif_005f11.setTest("action=='viewUserDetails'||action=='updateUserDetails'");
    int _jspx_eval_s_005fif_005f11 = _jspx_th_s_005fif_005f11.doStartTag();
    if (_jspx_eval_s_005fif_005f11 != javax.servlet.jsp.tagext.Tag.SKIP_BODY) {
      if (_jspx_eval_s_005fif_005f11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.pushBody();
        _jspx_th_s_005fif_005f11.setBodyContent((javax.servlet.jsp.tagext.BodyContent) out);
        _jspx_th_s_005fif_005f11.doInitBody();
      }
      do {
        out.write("\r\n");
        out.write("\t\t<div id=\"emailsettings\">\t\r\n");
        out.write("\t\t</div>\r\n");
        out.write("\t\t<div id=\"user_myaccount\">\r\n");
        out.write("\t\t</div>\r\n");
        out.write("\t\t<div id=\"user_subscr\"></div>\r\n");
        out.write("\t");
        int evalDoAfterBody = _jspx_th_s_005fif_005f11.doAfterBody();
        if (evalDoAfterBody != javax.servlet.jsp.tagext.BodyTag.EVAL_BODY_AGAIN)
          break;
      } while (true);
      if (_jspx_eval_s_005fif_005f11 != javax.servlet.jsp.tagext.Tag.EVAL_BODY_INCLUDE) {
        out = _jspx_page_context.popBody();
      }
    }
    if (_jspx_th_s_005fif_005f11.doEndTag() == javax.servlet.jsp.tagext.Tag.SKIP_PAGE) {
      _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f11);
      return true;
    }
    _005fjspx_005ftagPool_005fs_005fif_0026_005ftest.reuse(_jspx_th_s_005fif_005f11);
    return false;
  }
}
