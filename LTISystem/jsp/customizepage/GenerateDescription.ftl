[#ftl]
[#list names as name]
<tr>
	<td class="title">
	<s:text name="${name}"></s:text>
	</td>
	<td>
		<s:if test="%{isOwner}">
		<input type="button" onclick="Show${name?cap_first}()" id="${name}_button" value='<s:text name="edit"></s:text>' />
		<input type="button" onclick="Show${name?cap_first}Src()"  id="${name}_button_src" value='Edit Source'/>
		<input type="button" onclick="Clear${name?cap_first}()"  id="${name}_button_clr" value='Clear'/>
		</s:if>
	</td>
</tr>				
<tr>
	<td colspan="2">
		<div id="${name}_div_text" style=''><s:property value="${name}" escape="false"/></div>
		<s:if test="%{isOwner}">
		<div id="${name}_div" style="display:none">
			<s:hidden  id="${name?cap_first}" name="${name}" ></s:hidden>
			<input type="hidden" id="${name?cap_first}___Config" value="">
			<iframe id="${name?cap_first}___Frame" src="" width="100%" height="300" frameborder="no" scrolling="no">
			</iframe>
		</div>
		<div id='${name}_div_src'  style="display:none">
			<textarea id='${name}_src' style="width:100%;height:400px" onchange="Change${name?cap_first}Src()">
			<s:property value="${name}" escape="true"/>
			</textarea>
		</div>
		</s:if>
	</td>
</tr>			
<script>
	function Show${name?cap_first}Src(){
		$('#${name}_div_text').hide();
		$('#${name}_div_src').show();
		$('#${name}_button').attr({disabled:"yes"});
		$('#${name}_button_src').attr({disabled:"yes"});
		$('#${name}_button_clr').attr({disabled:"yes"});
	}
	function Change${name?cap_first}Src(){
		$('#${name?cap_first}').val($('#${name}_src').val());
	}
	function Show${name?cap_first}(){
		$("#${name?cap_first}___Frame").attr({src:"/LTISystem/jsp/strategy/FCKEditor/editor/fckeditor.html?InstanceName=${name?cap_first}&Toolbar=Default"});
		$('#${name}_div_text').hide();
		$('#${name}_div').show();
		$('#${name}_button').attr({disabled:"yes"});
		$('#${name}_button_src').attr({disabled:"yes"});
		$('#${name}_button_clr').attr({disabled:"yes"});
		
	}		
	function Clear${name?cap_first}(){
		$('#${name}_div_text').html("clear");
		$('#${name?cap_first}').val('');
		$('#${name}_button').attr({disabled:"yes"});
		$('#${name}_button_src').attr({disabled:"yes"});
		$('#${name}_button_clr').attr({disabled:"yes"});
	}	
			
</script>
[/#list]