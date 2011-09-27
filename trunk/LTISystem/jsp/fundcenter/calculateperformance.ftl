[#ftl]
[#assign s=JspTaglibs["/WEB-INF/struts-tags.tld"]]
<div id="calculateresult" style="width:100%">
[#if securityMPT??]
	<div id="left" style="float:left;width:49%">
		[#if AR==true]
		<p width="100%"><label>AR</label><span>[#if (securityMPT.AR)??]${(securityMPT.AR*100)?string}%[#else]NA[/#if]</span></p>
		<p class="clear"/>
		[/#if]
		[#if alpha==true]
		<p width="100%"><label>Alpha</label><span>[#if (securityMPT.alpha)??]${(securityMPT.alpha*100)?string}%[#else]NA[/#if]</span></p>
		<p class="clear"/>
		[/#if]
		[#if sharpeRatio==true]
		<p width="100%"><label>Sharpe Ratio</label><span>[#if (securityMPT.sharpeRatio)??]${(securityMPT.sharpeRatio*100)?string}%[#else]NA[/#if]</span></p>
		<p class="clear"/>
		[/#if]
		[#if standardDeviation==true]
		<p width="100%"><label>Standard Deviation</label><span>[#if (securityMPT.standardDeviation)??]${(securityMPT.standardDeviation*100)?string}%[#else]NA[/#if]</span></p>
		<p class="clear"/>
		[/#if]
		[#if drawDown==true]
		<p width="100%"><label>Draw Down</label><span>[#if (securityMPT.drawDown)??]${(securityMPT.drawDown*100)?string}%[#else]NA[/#if]</span></p>
		[/#if]
	</div>
	
	<div id="right" style="float:right;width:49%">
		[#if totalReturn==true]
		<p width="100%"><label>Return</label><span>[#if (securityMPT.return)??]${(securityMPT.return*100)?string}%[#else]NA[/#if]</span></p>
		<p class="clear"/>
		[/#if]
		[#if beta==true]
		<p width="100%"><label>Beta</label><span>[#if (securityMPT.beta)??]${(securityMPT.beta*100)?string}%[#else]NA[/#if]</span></p>
		<p class="clear"/>
		[/#if]
		[#if RSquared==true]
		<p width="100%"><label>RSquared</label><span>[#if (securityMPT.RSquared)??]${(securityMPT.RSquared*100)?string}%[#else]NA[/#if]</span></p>
		<p class="clear"/>
		[/#if]
		[#if treynorRatio==true]
		<p width="100%"><label>Treynor Ratio</label><span>[#if (securityMPT.treynorRatio)??]${(securityMPT.treynorRatio*100)?string}%[#else]NA[/#if]</span></p>
		[/#if]
	</div>
	<p class="clear"/>
[#else]
[@s.actionerror/]
[/#if]
</div>