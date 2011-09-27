[#ftl]
$(function(){
	$('.expandable').each(function(){
		$(this).toggle(
			function(){
				$(this).parent().children('.expandcontent').show();
				$(this).css({ background: '#fff url(/LTISystem/jsp/template/ed/images/faq_s.png) 0px 9px no-repeat' });
			},
			function(){
				$(this).parent().children('.expandcontent').hide();
				$(this).css({ background: '#fafafa url(/LTISystem/jsp/template/ed/images/faq_c.png) 0px 9px no-repeat' });
			}
		);
		$(this).parent().children('.expandcontent').hide();
	}); 
});
 
 $(function() {
 	var indexCount = 1;
 	
 	while(true){
 		$("#index" + indexCount).suggest("jsp/ajax/GetSecuritySuggestTxt.action",
 									 	{ 	haveSubTokens: true, 
 									 		onSelect: function(){}});
 		indexCount++;
 		if(indexCount>4)break;
 	}
 	$('#index1').val("VFINX");
 	$('#index2').val("VBINX");
 	
 	$.ajax({
		url: 'profile_getperformance.action?includeHeader=false&portfolioID=${Parameters.portfolioID}',
		success: function(resulthtml){
			$('#div_performance').html(resulthtml);
		}
	});
 	$.ajax({type: "post",  
        url:'/LTISystem/jsp/clonecenter/ViewHolding.action?includeHeader=false&includeJS=false&ID=${Parameters.portfolioID}&holdingDate=${Parameters.endDate}',
        dataType: "html",  
        success: function(result){
        	$("#holdingPanel").html(result);
        }
    });	
    $.ajax({type: "post",  
        url:'/LTISystem/jsp/clonecenter/ViewTransaction.action?includeHeader=false&includeJS=false&ID=${Parameters.portfolioID}&holdingDate=${Parameters.endDate}',
        dataType: "html",  
        success: function(result){
        	$("#transactionPanel").html(result);
        }   
    });
    $.ajax({type: "post",  
        url:'profile_getpiecharturl.action?includeHeader=false&portfolioID=${Parameters.portfolioID}',
        dataType: "html",  
        success: function(result){
    		$('#piechart').attr({'src':'/LTISystem/jsp/ajax/FetchImage.action?ID=${Parameters.portfolioID}&isImageCache=true&imageType='+$.trim(result)});
        }   
    });
    $('#div_modelportfolios').load('profile_list.action?includeHeader=false');
 	
 	
 });
 
 function compare(){
 	 $.ajax({type: "post",  
          url:'profile_getflashchart.action?includeHeader=false&portfolioID=${Parameters.portfolioID}',
          data: $('#flash_chart_form').serialize(),
          dataType: "html",  
          success: function(result){
          	$('#inner_flash_chart').html(result);
          	$('#inner_flash_chart').css({height:'700px'});
          }   
      });
 }


