/* -------------------------------------------------- *
 * ToggleVal Plugin for jQuery                        *
 * Version 1.0                                        *
 * -------------------------------------------------- *
 * Author:   Aaron Kuzemchak                          *
 * URL:      http://kuzemchak.net/                    *
 * E-mail:   afkuzemchak@gmail.com                    *
 * Date:     8/18/2007                                *
 * -------------------------------------------------- */

jQuery.fn.toggleVal = function(focusClass) {
	this.each(function() {
		var content =  $(this).val();
		$(this).dblclick(function() {
			// clear value if it is double clicked
			$(this).val("");
			$(this).attr("readonly",false);
			if(focusClass) { $(this).addClass(focusClass); }
		}).blur(function() {
			// restore to the default value if current value is empty
			var content_changed = $(this).val();
			if(content_changed != "")
				content = content_changed;
			//alert(content);
			if($(this).val() == "") { $(this).val(content); }
			// if focusClass is set, remove class
			if(focusClass) { $(this).removeClass(focusClass); }
		});
	});
}