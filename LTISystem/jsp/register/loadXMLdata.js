var req = null;  
var sreq = null; 

function loadcountryXMLDoc() {  
    if(window.ActiveXObject){
		req=new ActiveXObject("Microsoft.XMLHTTP");
	}
	else if(window.XMLHttpRequest){
		req=new XMLHttpRequest();
	} 
	var url  =  "/LTISystem/jsp/register/country.xml";   
	req.onreadystatechange = processReqChange;   
	req.open('GET', url, true);   
	req.send(null);     
}   
function loadstateXMLDoc() {   
    if(window.ActiveXObject){
		sreq=new ActiveXObject("Microsoft.XMLHTTP");
	}
	else if(window.XMLHttpRequest){
		sreq=new XMLHttpRequest();
	} 
	var url  =  "/LTISystem/jsp/register/state.xml?id=1";   
	sreq.onreadystatechange = processSReqChange;   
	sreq.open('GET', url, true);   
	sreq.send(null);     
}   
function processReqChange(){
	//alert(req.readyState);
	if (req.readyState == 4 ){   //&& req.status == 200 && req.responseXML 
		var select = document.getElementById( 'select_country' );  		
		var countries = req.responseXML.getElementsByTagName( 'country' ); 		
		for( var i=0; i<countries.length; i++ ){    
			var countryName = countries[i];   		   
			var optionvalue = countryName.childNodes[0].nodeValue
			select.options[i] = new Option(optionvalue, optionvalue);
			if(select.options[i].value==_country)
				select.options[i].selected=true;
		} 
		//select.onchange = getcountryValue;
	} 	
}
function processSReqChange(){
	//alert(req.readyState);
	if (sreq.readyState == 4 ){
		var select2 = document.getElementById('select_state');  		
		var states = sreq.responseXML.getElementsByTagName( 'state' ); 
		for( var i=0; i<states.length; i++ ){    
			var stateName = states[i];   
			var optionvalue = stateName.childNodes[0].nodeValue;
			select2.options[i] = new Option(optionvalue, optionvalue);
			if(select2.options[i].value==_state){
				select2.options[i].value="";
				select2.options[i].selected=true;
			}	
		} 
		//select2.onchange = getstateValue;
	}
}   
function getcountryValue(){
	var select = document.getElementById( 'select_country' );	
	var input = document.getElementById("write_counttry");
    //input.value = select.value;
	
}
function getstateValue(){
	var select = document.getElementById('select_state'); 
	var input = document.getElementById("write_state");
	if(select.value!="" ){	
		input.value = "Chosen";
		input.setAttribute("name","")
		var inp=$(input);
		inp.hide();
	}
	else{
		input.value = "";
		var inp=$(input);
		inp.show();
	}
}
window.onload=function(){
	loadstateXMLDoc();
	loadcountryXMLDoc();
}