<%@taglib prefix="s" uri="/WEB-INF/struts-tags.tld"%>
<html>
	<head>
		<title>Validate Page</title>
		<link href="../images/style.css" rel="stylesheet" type="text/css" />		
		<script type="text/javascript" src="../images/jquery-1.2.6.min.js"></script>
	</head>
	<body >
	<p class="title">Check Mpt</p>
	
	
	

	<fieldset id="banchmark_table">
		 <legend><s:text name="Check Security Mpt"/></legend>
	<div  align="left">
	<p align="left">
	        <table width="100%"  class="tablesorter" border="0" cellpadding="0" cellspacing="1">
                <tr> 
                 <td>
       
                 	<s:form action="DownloadTotalReturns.action" method="post" namespace="/jsp/admin/validate" enctype="multipart/form-data">
					    <table width="100%"  class="tablesorter" border="0" cellpadding="0" cellspacing="1">
						<label><s:text name="Download morningstar TotalReturns"></s:text>:</label>
					   <s:submit value="Download"  theme="simple"></s:submit>
					</s:form>
					</td>
				 </tr>
				   </table>
				   
    	<table width="100%"  class="tablesorter" border="0" cellpadding="0" cellspacing="1">
				 <tr>
					<td>
		           <div>	
		           check all security: 		
					<s:form action="CheckAllSecurity.action" method="post" namespace="/jsp/admin/validate" enctype="multipart/form-data">
					  <table  cellSpacing=0 cellPadding=3 width="100%" align="left">	
						<label><s:text name="difference" ></s:text>:</label>
						<s:textfield name="difference" theme="simple" value="0.2"></s:textfield>
					    <s:submit value="Check" theme="simple"></s:submit>
					</s:form>
		        	</div>		
					</td>
				 </tr>
	      </table>
	     
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="100%" align="left">	
				 <tr>
				 <td>
				<s:url namespace="/jsp/portfolio" action="Edit" id="create_url">
		              <s:param name="ID" value="0"></s:param>
	             </s:url>
	             
	            <p align="left">
		          <div align="left">
			       <s:a id="create" theme="simple" href="%{create_url}"><font size="2"><s:text name="create.portfolio"></s:text></font></s:a>
		          </div>
	            </p>
	            check single security:
					<s:form action="CheckSingleSecurity.action" method="post" namespace="/jsp/admin/validate" enctype="multipart/form-data">
						<table  cellSpacing=0 cellPadding=3 width="100%" align="left">	
						<label><s:text name="portfolio ID"></s:text>:</label>
						<s:textfield name="portfolioID" theme="simple"></s:textfield>
						<label><s:text name="symbol"></s:text>:</label>
						<s:textfield name="symbol" theme="simple"></s:textfield>
						<label><s:text name="difference"></s:text>:</label>
						<s:textfield name="difference" theme="simple" value="0.002"></s:textfield>
						<s:submit value="Check" theme="simple"></s:submit>
					 </s:form>	
				 </td>									
				</tr>
		   </table>						
	</p>
	</div>
		
    <p id="banchmarkParagraph"></p>
	</fieldset>		


   <fieldset id="banchmark_table">
		 <legend><s:text name="Check Portfolio Mpt and daily Data"/></legend>
    <div  align="left">
	  <p align="left">
	  	
	  
      <table width="100%"  class="tablesorter" border="0" cellpadding="0" cellspacing="1">	
				 <tr>
				    <td>
				     Store portfolio Information and Check
					<div>
					<s:form action="StorePortfolioInfor.action" method="post" namespace="/jsp/admin/validate" enctype="multipart/form-data">					
						<table  cellSpacing=0 cellPadding=3 width="100%" align="left">	
						<label><s:text name="portfolio ID"></s:text>:</label>
						<s:textfield name="portfolioID" theme="simple"></s:textfield>
						<s:submit value="Store" theme="simple"></s:submit>
					</s:form>
					</div>
				  </td>	
				</tr>
				   <tr>
				    <td>	
					<div>
					<s:form action="CheckPortfolio.action" method="post" namespace="/jsp/admin/validate" enctype="multipart/form-data">
						<table  cellSpacing=0 cellPadding=3 width="100%" align="left">	
						<label><s:text name="portfolio ID"></s:text>:</label>
						<s:textfield name="portfolioID" theme="simple"></s:textfield>
						<s:submit value="Check" theme="simple"></s:submit>
					</s:form>
					</div>
				</td>	
				</tr>

		   </table>	
    <tr>
      <td>
      <div>
	    <s:form action="ImportDataFile.action" method="post" namespace="/jsp/admin/validate" enctype="multipart/form-data">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="30%" align="left">
				<s:actionerror label="ExcuteMessage"/>
				<s:file name="uploadFile" label="Data File "></s:file>
	 			<s:submit></s:submit>
		</s:form>
	  <div>	
		</td>
     </tr>
 
		</p>
	</div>
	 <p id="banchmarkParagraph"></p>
	</fieldset>	  
	  
	
    Check Mpt Log<br>
    <tr>   		
    <td>
   		<div>
   		<s:form action="CheckMptLog.action" method="post" namespace="/jsp/admin/validate" enctype="multipart/form-data">
			<table style="BORDER-RIGHT: #a6a398 1px solid; BORDER-TOP: #a6a398 1px solid; BORDER-LEFT: #a6a398 1px solid; BORDER-BOTTOM: #a6a398 1px solid" cellSpacing=0 cellPadding=3 width="30%" align="left">
				<s:textfield id="Name" name="name" label="check  log"/>
				<s:submit></s:submit>
		</s:form>
		</div>
       </td>
      </tr>
	
	
	</body>
	
</html>