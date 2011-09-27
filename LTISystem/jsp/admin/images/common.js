function adjHiehgt()  
{  
	if(document.body.scrollHeight+10>window.parent.document.getElementById('Sidebar').scrollHeight){
	  	window.parent.document.getElementById('detail').height   =   document.body.scrollHeight+10;
	  	window.parent.document.getElementById('Sidebar').style.height   =   document.body.scrollHeight+10;
	  	window.parent.document.getElementById('MainBody').style.height   =   document.body.scrollHeight+10;        
	  	window.parent.document.getElementById('PageBody').style.height   =   document.body.scrollHeight+10;
	}
	
}  


function   backup()  
{  
  history.back();
  self.location.reload();
}   