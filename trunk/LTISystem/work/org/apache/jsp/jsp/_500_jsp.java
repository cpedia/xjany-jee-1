package org.apache.jsp.jsp;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import com.lti.system.*;;

public final class _500_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
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

      out.write('\r');
      out.write('\n');

String url= request.getRequestURL().toString();
boolean flag=false;
String[] strs=Configuration.get("f401kdomain").toString().split("\\|");
for(int i=0;i<strs.length;i++){
	if(url.toLowerCase().contains(strs[i].trim())){
		flag=true;
		break;
	}
}
String domainname="validfi.com";
if(flag==false){
	
}else{
	domainname="myplaniq.com";
}		


      out.write("\r\n");
      out.write("<HTML>\r\n");
      out.write("<HEAD>\r\n");
      out.write("<META http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">\r\n");
      out.write("<TITLE>www.");
      out.print(domainname);
      out.write(" 500 Error</TITLE>\r\n");
      out.write("</HEAD>\r\n");
      out.write("<BODY background=/LTISystem/jsp/images/error-bg.gif>\r\n");
      out.write("<script type=\"text/javascript\">\r\n");
      out.write("//<!--\r\n");
      out.write("function isIFrameSelf(){try{if(window.top ==window){return false;}else{return true;}}catch(e){return true;}}\r\n");
      out.write("function toHome(){ if(!isIFrameSelf()){ window.location.href=\"http://www.");
      out.print(domainname);
      out.write("\";}}\r\n");
      out.write("window.setTimeout(\"toHome()\",30000);\r\n");
      out.write("//-->\r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("<table border=0 cellpadding=0 cellspacing=0 >\r\n");
      out.write(" <tr><td height=134></td></tr>\r\n");
      out.write("</table>\r\n");
      out.write("<table width=544 height=157 border=0 cellpadding=0 cellspacing=0 align=center>\r\n");
      out.write("  <tr valign=middle align=middle>\r\n");
      out.write("\r\n");
      out.write("\t<td background=/LTISystem/jsp/images/error-block.gif>\r\n");
      out.write("\t    <table border=0 cellpadding=0 cellspacing=0 >\r\n");
      out.write("\t\t <tr>\r\n");
      out.write("\t\t    <td  style=padding-left:10px;padding-right:10px;padding-top:10px>\r\n");
      out.write("\t\t\tSorry, there are some errors in our system, if you have any problems please contact us with email <a href='mailto:support@");
      out.print(domainname);
      out.write("'>support@");
      out.print(domainname);
      out.write("</a>.\r\n");
      out.write("\t\t<br>\r\n");
      out.write("\t\tOr go back to <a href=http://www.");
      out.print(domainname);
      out.write(">www.");
      out.print(domainname);
      out.write("</a>.\t\t\t\t\r\n");
      out.write("\t\t\t</td>\r\n");
      out.write("                 </tr>\r\n");
      out.write("            </table>\r\n");
      out.write("\t</td>\r\n");
      out.write("  </tr>\r\n");
      out.write("\r\n");
      out.write("</table>\r\n");
      out.write("<br>\r\n");
      out.write("</BODY>\r\n");
      out.write("</HTML>\r\n");
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
}
