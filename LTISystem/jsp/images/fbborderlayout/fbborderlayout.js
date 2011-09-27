/*
 * FBBorderLayout - a jQuery "border layout"
 * Version 0.2 - 18th July 2008
 *
 * Copyright (c) 2008 Fabrizio Balliano (http://fabrizioballiano.net).
 * Dual licensed under the GPL (http://www.gnu.org/licenses/gpl.html)
 * and MIT (http://www.opensource.org/licenses/mit-license.php) licenses.
 */

jQuery.FBBorderLayout = function (custom_config)
{
	var defaultConfig = {
		spacing: 5,
		north_collapsable: true,
		east_width: 200,
		east_collapsable: true,
		south_collapsable: true,
		west_width: 200,
		west_collapsable: true,
		north_collapsed: false,
		east_collapsed: false,
		south_collapsed: false,
		west_collapsed: false
	}
	
	var calPara = function (str) {

		if(isNaN(str)&&str!=null)
		{
			str=str.substr(0,str.length-1);
			return $window.width()*parseInt(str,10)*0.01;
		}
		else
		return parseInt(str, 10) || 0;
	}
	
	var numCurCSS = function (object, property) {
		return parseInt(jQuery.curCSS(object[0], property, true), 10) || 0;
	}


	
	var collapseNorth = function () {
		$center.css("top", config.spacing);
		$east.css("top", config.spacing);
		$west.css("top", config.spacing);
		jQuery(".fbbl_north_collapser").css("top", 0);
		$north.css({
			zIndex:1000
		}).hide();
		$('#north_collapser_img').attr({src:'../images/go_down.gif',width:50,height:5});
		apply();
	}
	
	var collapseEast = function () {
		$center.css("right", config.spacing);
		jQuery(".fbbl_east_collapser").css("right", 0);
		$east.css({
				zIndex: 1000
		}).hide();
		$('#east_collapser_img').attr({src:'../images/go_left.gif',width:5,height:50});
		apply();
	}
	
	var collapseSouth = function () {
		$center.css("bottom", config.spacing);
		$east.css("bottom", config.spacing);
		$west.css("bottom", config.spacing);
		jQuery(".fbbl_south_collapser").css("bottom", 0);
		$south.css({
			zIndex:1000
		}).hide();
		$('#south_collapser_img').attr({src:'../images/go_up.gif',width:50,height:5});
		apply();
	}
	
	var collapseWest = function () {
		$center.css("left", config.spacing);
		jQuery(".fbbl_west_collapser").css("left", 0);
		$west.css({
				zIndex: 1000
		}).hide();
		$('#west_collapser_img').attr({src:'../images/go_right.gif',width:5,height:50});
		apply();
	}
	
	var expandNorth = function () {
		if ($north.is(':visible')) return;
		$('#north_collapser_img').attr({src:'../images/go_up.gif',width:50,height:5});
		$north.show();
		apply();
	}
	
	var expandEast = function () {
		if ($east.is(':visible')) return;
		$('#east_collapser_img').attr({src:'../images/go_right.gif',width:5,height:50});
		$east.show();
		apply();
	}
	
	var expandSouth = function () {
		if ($south.is(':visible')) return;
		$('#south_collapser_img').attr({src:'../images/go_down.gif',width:50,height:5});
		$south.show();
		apply();
	}
	
	var expandWest = function () {
		if ($west.is(':visible')) return;
		$('#west_collapser_img').attr({src:'../images/go_left.gif',width:5,height:50});
		$west.show();
		apply();
	}
	
	var apply = function () {

		center_top = config.spacing;
		center_right = config.spacing;
		center_bottom = config.spacing;
		center_left = config.spacing;
		$body.css({
			position: 'relative',
			overflow: 'hidden',
			height: '100%',
			margin: 0,
			padding: 0
		});
		config.north_width=calPara(config.north_width);
		config.east_width=calPara(config.east_width);
		config.south_width=calPara(config.south_width);
		config.west_width=calPara(config.west_width);
		if ($north.length) {
			if ($north.is(':visible')) {
				$north.css({
					zIndex: 0
				});
				if ($.browser.msie) {
				center_top += $north.height();
		        }else
				center_top += $north.height()+40;
			}
			if (config.north_collapsable) {
				jQuery('.fbbl_north_collapser').css({
					position: 'absolute',
					cursor: 'pointer',
					fontSize: '1px',
					width: 50,
					height: config.spacing,
					top: center_top - config.spacing,
					left: ($window.width()-50)/2,
					border : '1px solid #9cbaef'
				}).appendTo($body).toggle(collapseNorth, expandNorth);
			}
		}
		
		if ($south.length) {
			if ($south.is(':visible')) {
				$south.css({
					position: 'absolute',
					zIndex: 0,
					bottom: 0,
					left: 0,
					right: 0,
					border : '1px solid #9cbaef',
					height:config.south_width
				});
				center_bottom += $south.outerHeight();
				if ($.browser.msie) $south.width('100%');
			}
			if (config.south_collapsable) {
				jQuery('.fbbl_south_collapser').css({
					position: 'absolute',
					cursor: 'pointer',
					fontSize: '1px',
					width: 50,
					height: config.spacing,
					bottom: center_bottom - config.spacing,
					left: ($window.width()-50)/2,
					border : '1px solid #9cbaef'
				}).appendTo($body).toggle(collapseSouth, expandSouth);
			}
		}
	
		if ($west.length) {
			if ($west.is(':visible')) {
				$west.css({
					position: 'absolute',
					overflow: 'auto',
					zIndex: 0,
					top: center_top,
					bottom: center_bottom,
					left: 0,
					width: config.west_width - numCurCSS($west, 'borderLeftWidth') - numCurCSS($west, 'borderRightWidth') - numCurCSS($west, 'paddingLeft') - numCurCSS($west, 'paddingRight')
				});
				if ($.browser.msie) {
					$west.width(config.west_width).height($window.height() - center_top - center_bottom);
				} else if ($.browser.opera) {
					$west.height($window.height() - center_top - center_bottom - numCurCSS($west, 'borderTopWidth') - numCurCSS($west, 'borderBottomWidth') - numCurCSS($west, 'paddingTop') - numCurCSS($west, 'paddingBottom'));
				}
				center_left += $west.outerWidth();
			}
			if (config.west_collapsable) {
				jQuery('.fbbl_west_collapser').css({
					position: 'absolute',
					cursor: 'pointer',
					width: config.spacing,
					height: 50,
					top: ($window.height()-50)/2,
					left: center_left - config.spacing,
					border : '1px solid #9cbaef'
				}).appendTo($body).toggle(collapseWest, expandWest);
			}
			
			
		}
	
		if ($east.length) {
			if ($east.is(':visible')) {
				$east.css({
					position: 'absolute',
					overflow: 'auto',
					zIndex: 0,
					top: center_top,
					bottom: center_bottom,
					right: 0,
					//width: $window.width()*0.5 - numCurCSS($east, 'borderLeftWidth') - numCurCSS($east, 'borderRightWidth') - numCurCSS($east, 'paddingLeft') - numCurCSS($east, 'paddingRight')
					width: config.east_width - numCurCSS($east, 'borderLeftWidth') - numCurCSS($east, 'borderRightWidth') - numCurCSS($east, 'paddingLeft') - numCurCSS($east, 'paddingRight')
				});
				if ($.browser.msie) {
					$east.width(config.east_width).css({
						left: $window.width() - $east.outerWidth(),
						height: $window.height() - center_top - center_bottom
					});
				} else if ($.browser.opera) {
					$east.height($window.height() - center_top - center_bottom - numCurCSS($east, 'borderTopWidth') - numCurCSS($east, 'borderBottomWidth') - numCurCSS($east, 'paddingTop') - numCurCSS($east, 'paddingBottom'));
				}
				center_right += $east.outerWidth();
			}
			if (config.east_collapsable) {
				jQuery('.fbbl_east_collapser').css({
					position: 'absolute',
					cursor: 'pointer',
					width: config.spacing,
					height: 50,
					top: ($window.height()-50)/2,
					border : '1px solid #9cbaef',
					right: center_right - config.spacing
				}).appendTo($body).toggle(collapseEast, expandEast);
			}
			
			
		}
	
		if ($center.length) {
			$center.css({
				position: 'absolute',
				zIndex: 0,
				top: center_top,
				right: center_right,
				bottom: center_bottom,
				left: center_left,
				overflow: 'auto'
			});
			
			if ($.browser.msie) {
				$center.width($window.width() - center_left - center_right);
				$center.height($window.height() - center_top - center_bottom);
			}
			
			
			jQuery('#fbbl_west_bar').css({
					position: 'absolute',
					cursor: 'pointer',
					fontSize: '1px',
					width: $west.outerWidth(),
					height: 25,
					top: center_top-25,
					left: 0,
					border : '1px solid #9cbaef',
					background:  'url(/LTISystem/jsp/images/bar.png)'
				}).appendTo($body);
			
			
			jQuery('#fbbl_east_bar').css({
					position: 'absolute',
					cursor: 'pointer',
					fontSize: '1px',
					width: $east.outerWidth(),
					height: 25,
					top: center_top-25,
					left: center_left+$center.outerWidth()+config.spacing,
					border : '1px solid #9cbaef',
					background:  'url(/LTISystem/jsp/images/bar.png)'
				}).appendTo($body);
				
			jQuery('#fbbl_center_bar').css({
					position: 'absolute',
					cursor: 'pointer',
					fontSize: '1px',
					width: $center.outerWidth()-1,
					height: 25,
					top: center_top-25,
					left: center_left,
					border : '1px solid #9cbaef',
					background:  'url(/LTISystem/jsp/images/bar.png)'
				}).appendTo($body);
			
			
		}
	}
	
	var $window = jQuery(window);
	var $body = jQuery("body");
	var config = $.extend(defaultConfig, custom_config);
	var $north = jQuery("body > .fbbl_north");
	var $east = jQuery("body > .fbbl_east");
	var $south = jQuery("body > .fbbl_south");
	var $west = jQuery("body > .fbbl_west");
	var $center = jQuery("body > .fbbl_center");
	var center_top = config.spacing;
	var center_right = config.spacing;
	var center_bottom = config.spacing;
	var center_left = config.spacing;

	$window.resize(apply);
	if (config.north_collapsable && $north.length) jQuery("<div class='fbbl_north_collapser'><img src='../images/go_up.gif' id='north_collapser_img' border=0 width=50 height=5></div>").appendTo($body);
	if (config.east_collapsable && $east.length) jQuery("<div class='fbbl_east_collapser'><img src='../images/go_right.gif' id='east_collapser_img' border=0 width=5 height=50></div>").appendTo($body);
	if (config.south_collapsable && $south.length) jQuery("<div class='fbbl_south_collapser'><img src='../images/go_down.gif' id='south_collapser_img' border=0 width=50 height=5></div>").appendTo($body);
	if (config.west_collapsable && $west.length) jQuery("<div class='fbbl_west_collapser'><img src='../images/go_left.gif' id='west_collapser_img' border=0 width=5 height=50></div>").appendTo($body);
	
	jQuery("<div id='fbbl_east_bar'></div>").appendTo($body);
	jQuery("<div id='fbbl_west_bar'></div>").appendTo($body);
	jQuery("<div id='fbbl_center_bar'></div>").appendTo($body);
	
	
	apply();
	
	if(config.north_collapsed)collapseNorth();
	if(config.east_collapsed)collapseEast();
	if(config.south_collapsed)collapseSouth();
	if(config.west_collapsed)collapseWest();
}