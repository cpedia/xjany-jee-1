<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	<title>Portfolio Main Page</title>
	<link rel="stylesheet" type="text/css" href="../ext-2.0.2/resources/css/core.css" />
	<link rel="stylesheet" type="text/css" href="../ext-2.0.2/resources/css/layout.css" />
	<link rel="stylesheet" type="text/css" href="../ext-2.0.2/resources/css/panel.css" />
	<script src="../ext-2.0.2/ext.js" type="text/javascript"></script>
	
<script type="text/javascript">
var viewport;
$(document).ready(function(){
    Ext.state.Manager.setProvider(new Ext.state.CookieProvider());
    window.viewport = new Ext.Viewport({
        layout:'border',
        layoutConfig:document.body,
        items:[
	        {
		        region:'north',
		        id:"north_region",
		        height:60,
		        layout:'fit',
		        contentEl:"top"
	        },
	        <s:if test="customizeRegion!=null && customizeRegion.eastRegionName!=null">
	        {
	            region:'east',
	            id:"east_region",
	            width:300,
	            layout:'fit',
	            deferredRender:false,
	            contentEl:"east",
	            title:'<s:property value="customizeRegion.EastTitle"/>',
	            split:true,
	            collapsible:true
			},
			</s:if>
			{
	            region:'center',
	            id:'center_region',
	            title:'Portfolio List',
	            deferredRender:false,
	            layout:'fit',
	            contentEl:"center"
	            //autoScroll:true
			}<s:if test="customizeRegion!=null && customizeRegion.southRegionName!=null">,
			
			{
	            region:'south',
	            id:'south_region',
	            title:'<s:property value="customizeRegion.SouthTitle"/>',
	            deferredRender:false,
	            layout:'fit',
	            contentEl:"south",
	            //autoScroll:true,
	            height: 100,
        		minSize: 75,
        		maxSize: 250,
        		margins: '0 5 5 5',
        		collapsible:true
			}</s:if><s:if test="customizeRegion!=null && customizeRegion.westRegionName!=null">,			
			{
				region:'west',
				id:'west_region',
				title:'<s:property value="customizeRegion.WestTitle"/>',
				deferredRender:false,
				contentEl:'west',
				split:true,
			    //margins: '5 0 0 5',
        		//cmargins: '5 5 0 5',
        		width: 200,
        		minSize: 100,
        		maxSize: 300,
        		collapsible:true
			}
			</s:if>
        ]
	})
});
</script>
</head>
<body>

<s:url id="center_url" action="Main" namespace="/jsp/portfolio"  includeParams="none" escapeAmp="false"></s:url>
<s:if test="customizeRegion!=null && customizeRegion.eastRegionName!=null">
	<s:url id="east_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
		<s:param name="pageName"><s:property value='customizeRegion.eastRegionName'/></s:param>
	</s:url>
	<iframe id="east" scrolling="yes" width="100%" height="99%" src='<s:property value="east_url"/>'></iframe>
</s:if>
<s:if test="customizeRegion!=null && customizeRegion.westRegionName!=null">
	<s:url id="west_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
		<s:param name="pageName"><s:property value='customizeRegion.westRegionName'/></s:param>
	</s:url>
	<iframe id="west" scrolling="yes" width="100%" height="99%" src='<s:property value="west_url"/>'></iframe>
</s:if>
<s:if test="customizeRegion!=null && customizeRegion.southRegionName!=null">
	<s:url id="south_url" action="CustomizePage" namespace="/jsp/ajax" includeParams="none">
		<s:param name="pageName"><s:property value='customizeRegion.southRegionName'/></s:param>
	</s:url>
	<iframe id="south" scrolling="yes" width="100%" height="99%" src='<s:property value="south_url"/>'></iframe>
</s:if>	
<iframe id="center" scrolling="yes" width="100%" height="99%" src='<s:property value="center_url"/>'></iframe>
</body>
</html>
