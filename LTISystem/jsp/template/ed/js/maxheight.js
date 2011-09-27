function alignHeight() {
	var tallest = 0;
	var thisheight;
	$('#content .maxheight').each(function(i, val){
		thisheight = $(this).height();
		if (thisheight > tallest) {
			tallest = thisheight;
		}
		
		if (i == $('#content .maxheight').length-1) {
			$('#content .maxheight').each(function(){
				$(this).height(tallest);
			});
		}
	});
}