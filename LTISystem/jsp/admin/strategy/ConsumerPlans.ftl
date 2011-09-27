[#ftl]
[#assign authz=JspTaglibs["/WEB-INF/authz.tld"]]
[#assign ltiauthz=JspTaglibs["/WEB-INF/lti_authz.tld"]]

<link href="/LTISystem/jsp/template/ed/css/style.css" rel="stylesheet" type="text/css" />
	<link href="/LTISystem/jsp/template/ed/css/layout.css" rel="stylesheet" type="text/css" />

	<link href="/LTISystem/jsp/template/ed/css/jquery_UI/moss/jquery-ui-1.8.custom.css" rel="stylesheet" type="text/css" />
	<link href="/LTISystem/jsp/template/ed/css/tablesorter/tablesorter.css" rel="stylesheet" type="text/css"/>
	
	
	<script src="/LTISystem/jsp/template/ed/js/jquery/jquery-1.4.2.min.js" type="text/javascript"></script>
	<script src="/LTISystem/jsp/template/ed/js/jquery_UI/jquery-ui-1.8.custom.min.js" type="text/javascript"></script>
	<script src="/LTISystem/jsp/template/ed/js/tablesorter/jquery.tablesorter.js" type="text/javascript"></script>
	
[@s.action name="GetStrategyTable" namespace="/jsp/strategy" executeResult=true]
    [@s.param name="includeCategory"]false[/@s.param]
    [@s.param name="includeLastValidDate"]true[/@s.param]
    [@s.param name="includeLastTransactionDate"]true[/@s.param]
    [@s.param name="includeClassName"]false[/@s.param]
    [@s.param name="includePortfolio"]true[/@s.param]
    [@s.param name="year"]-1,-3,-5[/@s.param]
    [@s.param name="mpt"]ar,sharperatio[/@s.param]
    [@s.param name="sortColumn"]6[/@s.param]
    [@s.param name="size"]0[/@s.param]
    [@s.param name="type"]64[/@s.param]
    [@s.param name="admin"]true[/@s.param]
    [@s.param name="permission"]true[/@s.param]
    [@s.param name="showUser"]true[/@s.param]
    [@s.param name="urlPrefix"]/LTISystem/f401k_view.action?ID=[/@s.param]
    [@s.param name="includeApprove"]true[/@s.param]
[/@s.action]		