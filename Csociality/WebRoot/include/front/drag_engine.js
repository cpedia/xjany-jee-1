// JavaScript Document
/* Dragging functionality */
/* @author: Rostislav Brizgunov */
/* @date: 08.04.2007 */

// Determine browser and version.
function Browser() {

	var ua = navigator.userAgent;
	this.isIE = ua.indexOf('MSIE') != -1;
	this.isNS = ua.indexOf('Netscape') != -1 || ua.indexOf('Gecko') != -1;
	this.isOpera = ua.indexOf('Opera') != -1;

	// Fake IE on Opera. If Opera fakes IE, Netscape cancel those
	if (this.isOpera) {
		this.isIE = true;
		this.isNS = false;
	}

}

var browser = new Browser();

// Global object to hold drag information.
var dragObj = new Object();
dragObj.zIndex = 0;

function dragStart(event, id, dir) {

	var el;
	var x, y, dx = 0, dy = 0;

	// If an element id was given, find it. Otherwise use the element being clicked on.
	if (id)
		dragObj.elNode = document.getElementById(id);
	else {
		if (browser.isIE)
			dragObj.elNode = event.srcElement;
		if (browser.isNS)
			dragObj.elNode = event.target;
		// If this is a text node, use its parent element.
		if (dragObj.elNode.nodeType == 3)
			dragObj.elNode = dragObj.elNode.parentNode;
		// Not stable
		while (dragObj.elNode.parentNode && (!dragObj.elNode.onmousedown || dragObj.elNode.onmousedown.toString().toLowerCase().indexOf("dragstart")==-1 )) {
			dragObj.elNode = dragObj.elNode.parentNode;
		}
		if (!dragObj.elNode.parentNode)
			return;
	}
	dragObj.elNode.style.position = "absolute";

	// Get cursor position with respect to the page.
	x = event.clientX;
	y = event.clientY;
	if (browser.isIE) {
		dx = document.documentElement.scrollLeft; // Works in the MSIE 5 and not in the MSIE 6
		dx += document.body.scrollLeft; // Works in the MSIE 5 and not in the MSIE 6
		dy = document.documentElement.scrollTop;
		dy += document.body.scrollTop;
	}
	if (browser.isNS) {
		dx = window.scrollX;
		dy = window.scrollY;
	}
	x += dx;
	y += dy;

	// Save starting positions of cursor and element.
	dragObj.prevX = x;
	dragObj.prevY = y;
	dragObj.elStartLeft  = parseInt(dragObj.elNode.style.left, 10);
	dragObj.elStartTop   = parseInt(dragObj.elNode.style.top,  10);

	if (isNaN(dragObj.elStartLeft)) {
		dragObj.elStartLeft = x - 5;
		dragObj.elNode.style.left = dragObj.elStartLeft;
	}
	if (isNaN(dragObj.elStartTop)) {
		dragObj.elStartTop  = y - 5;
		dragObj.elNode.style.top = dragObj.elStartTop;
	}

	// Update element's z-index.
	//dragObj.elNode.style.zIndex = ++dragObj.zIndex;
	
	// Move direction
	if (!dir || (dir != "h" && dir != "v"))
		dir = "hv"
	dragObj.moveDir = dir;

	// Capture mousemove and mouseup events on the page.
	if (browser.isIE) {
		document.attachEvent("onmousemove", dragGo);
		document.attachEvent("onmouseup",   dragStop);
		// Prevents text selection
		event.cancelBubble = true;
		event.returnValue = false;
	}
	if (browser.isNS) {
		document.addEventListener("mousemove", dragGo,   true);
		document.addEventListener("mouseup",   dragStop, true);
		// Prevents text selection
		event.preventDefault();
	}

}

function dragGo(event) {

	var x, y, dx = 0, dy = 0;

	// Get cursor position with respect to the page.
	x = event.clientX;
	y = event.clientY;
	if (browser.isIE) {
		dx = document.documentElement.scrollLeft; // Works in the MSIE 5 and not in the MSIE 6
		dx += document.body.scrollLeft; // Works in the MSIE 5 and not in the MSIE 6
		dy = document.documentElement.scrollTop;
		dy += document.body.scrollTop;
	}
	if (browser.isNS) {
		dx = window.scrollX;
		dy = window.scrollY;
	}
	x += dx;
	y += dy;

	// Move drag element by the same amount the cursor has moved.
	dx = x - dragObj.prevX;
	dy = y - dragObj.prevY;
	if (dragObj.moveDir.indexOf("h") != -1) dragObj.elNode.style.left = parseInt(dragObj.elNode.style.left, 10) + dx + "px";
	if (dragObj.moveDir.indexOf("v") != -1) dragObj.elNode.style.top = parseInt(dragObj.elNode.style.top, 10) + dy + "px";
	dragObj.prevX = x;
	dragObj.prevY = y;
	/*info.innerHTML="x: "+dragObj.elNode.style.left+"<br/>";
	info.innerHTML+="y: "+dragObj.elNode.style.top;*/
	
	if (browser.isIE) {
		event.cancelBubble = true;
		event.returnValue = false;
	}
	if (browser.isNS) {
		event.preventDefault();
	}

}

function dragStop(event) {

	// Stop capturing mousemove and mouseup events.
	if (browser.isIE) {
		document.detachEvent("onmousemove", dragGo);
		document.detachEvent("onmouseup",   dragStop);
	}
	if (browser.isNS) {
		document.removeEventListener("mousemove", dragGo,   true);
		document.removeEventListener("mouseup",   dragStop, true);
	}
	
}

/* Spec hint mechanism */
function hintMoveStart(event, over_elm_id, hint_id) {

	var el;
	var x, y, dx = 0, dy = 0;

	dragObj.overElement = document.getElementById(over_elm_id);

	dragObj.elNode = document.getElementById(hint_id);
	dragObj.elNode.style.display = "block";
	dragObj.elNode.style.position = "absolute";
	
	x = event.clientX;
	y = event.clientY;
	if (browser.isIE) {
		dx = document.documentElement.scrollLeft;
		dx += document.body.scrollLeft;
		dy = document.documentElement.scrollTop;
		dy += document.body.scrollTop;
	}
	if (browser.isNS) {
		dx = window.scrollX;
		dy = window.scrollY;
	}
	x += dx;
	y += dy;

	dragObj.prevX = x;
	dragObj.prevY = y;
	dragObj.elStartLeft  = x - 140;
	dragObj.elStartTop   = y + 2;
	dragObj.elNode.style.left = dragObj.elStartLeft + "px";
	dragObj.elNode.style.top = dragObj.elStartTop + "px";
	dragObj.elNode.style.zIndex = 5;
	var	dir = "hv";
	dragObj.moveDir = dir;

	if (browser.isIE) {
		document.attachEvent("onmousemove", hintMoveGo);
		event.cancelBubble = true;
		event.returnValue = false;
	}
	if (browser.isNS) {
		document.addEventListener("mousemove", hintMoveGo,   true);
		event.preventDefault();
	}

}

function hintMoveGo(event) {

	var x, y, dx = 0, dy = 0;

	x = event.clientX;
	y = event.clientY;
	if (browser.isIE) {
		dx = document.documentElement.scrollLeft;
		dx += document.body.scrollLeft;
		dy = document.documentElement.scrollTop;
		dy += document.body.scrollTop;
	}
	if (browser.isNS) {
		dx = window.scrollX;
		dy = window.scrollY;
	}
	x += dx;
	y += dy;

	dx = x - dragObj.prevX;
	dy = y - dragObj.prevY;
	if (dragObj.moveDir.indexOf("h") != -1) dragObj.elNode.style.left = parseInt(dragObj.elNode.style.left, 10) + dx + "px";
	if (dragObj.moveDir.indexOf("v") != -1) dragObj.elNode.style.top = parseInt(dragObj.elNode.style.top, 10) + dy + "px";
	dragObj.prevX = x;
	dragObj.prevY = y;
	
	if (browser.isIE) {
		event.cancelBubble = true;
		event.returnValue = false;
	}
	if (browser.isNS) {
		event.preventDefault();
	}

	var over_elm;
	if (browser.isIE)
		over_elm = event.srcElement;
	if (browser.isNS)
		over_elm = event.target;

	if (over_elm != dragObj.overElement && over_elm.parentNode != dragObj.overElement && over_elm != dragObj.elNode) {
		hintMoveStop();
	}
}

function hintMoveStop() {
	if (browser.isIE) {
		document.detachEvent("onmousemove", hintMoveGo);
	}
	if (browser.isNS) {
		document.removeEventListener("mousemove", hintMoveGo,   true);
	}
	dragObj.elNode.style.display = "none";
}