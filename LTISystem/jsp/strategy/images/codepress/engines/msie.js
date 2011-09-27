/*
 * CodePress - Real Time Syntax Highlighting Editor written in JavaScript - http://codepress.org/
 * 
 * Copyright (C) 2007 Fernando M.A.d.S. <fermads@gmail.com>
 *
 * Developers:
 *		Fernando M.A.d.S. <fermads@gmail.com>
 *		Michael Hurni <michael.hurni@gmail.com>
 * Contributors:
 *		Martin D. Kirk
 *
 * This program is free software; you can redistribute it and/or modify it under the terms of the 
 * GNU Lesser General Public License as published by the Free Software Foundation.
 *
 * Read the full licence: http://www.opensource.org/licenses/lgpl-license.php
 */

/*
 * Michael Cai developed this version
 * is03cxg@mail2.sysu.edu.cn
 */

CodePress = {
	scrolling : false,
	autocomplete : true,
	
	// set initial vars and start sh
	initialize : function() {
		if(typeof(editor)=='undefined' && !arguments[0]) return;
		chars = '|32|46|62|'; // charcodes that trigger syntax highlighting
		cc = '\u2009'; // carret char
		editor = document.getElementsByTagName('pre')[0];
		editor.contentEditable = 'true';
		offsetLeft=0;
		offsetTop=0;
    suggest = document.createElement("div");
    suggest.style.position="absolute";
    suggest.style.display="none";
    suggest.style.width="200px";
    suggest.style.border="1px solid #50A5F1";
    suggest.style.backgroundColor="#D3E6FE";
    suggest.id="suggest";
    document.body.appendChild(suggest);
		document.getElementsByTagName('body')[0].onfocus = function() {editor.focus();}
		document.attachEvent('onkeydown', this.metaHandler);
		document.attachEvent('onkeypress', this.keyHandler);
    document.attachEvent('onmousedown', this.mousedown);
		window.attachEvent('onscroll', function() { if(!CodePress.scrolling) setTimeout(function(){CodePress.syntaxHighlight('scroll')},1)});
		completeChars = this.getCompleteChars();
		completeEndingChars =  this.getCompleteEndingChars();
		setTimeout(function() { window.scroll(0,0) },50); // scroll IE to top
	},
  mousedown : function(evt) {
    var suggest=document.getElementById("suggest");
    suggest.style.display= 'none';
  },
	// treat key bindings
	keyHandler : function(evt) {
		charCode = evt.keyCode;
		fromChar = String.fromCharCode(charCode);

		if( (completeEndingChars.indexOf('|'+fromChar+'|')!= -1 || completeChars.indexOf('|'+fromChar+'|')!=-1  )&& CodePress.autocomplete) { // auto complete
			if(!CodePress.completeEnding(fromChar))
			     CodePress.complete(fromChar);
		}
	    else if(chars.indexOf('|'+charCode+'|')!=-1||charCode==13) { // syntax highlighting
		 	CodePress.syntaxHighlight('generic');
			if(charCode==13){
        if(CodePress.getPre(0).length>=2){
  				setTimeout(function () { CodePress.insertCode(CodePress.getPre(0),false)},0);

        }
			}
		}
	},
	
	metaHandler : function(evt) {
		keyCode = evt.keyCode;
		
		if(keyCode==9 || evt.tabKey) { 
			CodePress.snippets();
		}
		else if((keyCode==122||keyCode==121||keyCode==90) && evt.ctrlKey) { // undo and redo
			(keyCode==121||evt.shiftKey) ? CodePress.actions.redo() :  CodePress.actions.undo(); 
			evt.returnValue = false;
		}
		else if(keyCode==34||keyCode==33) { // handle page up/down for IE
			self.scrollBy(0, (keyCode==34) ? 200 : -200);
			evt.returnValue = false;
		}
		else if(keyCode==46||keyCode==8) { // save to history when delete or backspace pressed
		 	CodePress.actions.history[CodePress.actions.next()] = editor.innerHTML;
      if(keyCode==8){
        if(CodePress.getLastWord_withpoint().length!=0){
          setTimeout(function () {CodePress.getSuggestList(CodePress.getLastWord_withpoint(),1)},100);
        }

      }
		}
		else if((evt.ctrlKey || evt.metaKey) && evt.shiftKey && keyCode!=90)  { // shortcuts = ctrl||appleKey+shift+key!=z(undo)
			CodePress.shortcuts(keyCode);
			evt.returnValue = false;
		}
		else if(keyCode==86 && evt.ctrlKey)  { // handle paste
			window.clipboardData.setData('Text',window.clipboardData.getData('Text').replace(/\t/g,'\u2008'));
		 	top.setTimeout(function(){CodePress.syntaxHighlight('paste');},10);
		}
		else if(keyCode==67 && evt.ctrlKey)  { // handle cut
			window.clipboardData.setData('Text',CodePress.getCode());
			// code = window.clipboardData.getData('Text');
		}else{
		
      CodePress.getSuggestList(CodePress.getLastWord_withpoint()+String.fromCharCode(keyCode));



		}
	},

	// put cursor back to its original position after every parsing
scrollTop:	function ()
{
        var scrollTop;
        if (document.documentElement && document.documentElement.scrollTop){
                scrollTop = document.documentElement.scrollTop;
        }else if (document.body){
                scrollTop = document.body.scrollTop;
        }else if (window.pageYOffset){
                scrollTop = window.pageYOffset;
        }
        return scrollTop;
},
	// put cursor back to its original position after every parsing
scrollLeft:	function ()
{
        var scrollLeft;
        if (document.documentElement && document.documentElement.scrollLeft){
                scrollLeft = document.documentElement.scrollLeft;
        }else if (document.body){
                scrollLeft = document.body.scrollLeft;
        }else if (window.pageYOffset){
                scrollLeft = window.pageXOffset;
        }
        return scrollLeft;
},
	getSuggestList:function(pre){
		
    var range = document.selection.createRange();
		offsetLeft=range.offsetLeft;
		offsetTop=range.offsetTop;
    var suggest=document.getElementById("suggest");
    var suggestList=Language.suggest;
    var list=new Array();
    pre=pre.toLowerCase();
    for(var i=suggest.childNodes.length-1;i>=0;i--){
       suggest.removeChild(suggest.childNodes[i]);
    }

    var index=0;
    var width=200;
    for(var i=0;i<suggestList.length;i++){
      if(suggestList[i].toLowerCase().indexOf(pre)==0){
        list[list.length]=suggestList[i];
        index++;
        var oo = document.createElement("div");
        suggest.appendChild(oo);
        oo.innerText = suggestList[i];
        oo.style.height = "16px";
        oo.style.width = "99%";
        oo.style.overflow="hidden";
        oo.style.lineHeight="120%";
        oo.style.cursor = "hand";
        oo.style.fontSize = "9pt";
        oo.style.Color = "#FFFFFF";
        oo.style.fontFamily="Arial, Helvetica, sans-serif";
        oo.style.padding = "0 2 0 2";
        if(index %2 ==0)oo.style.backgroundColor="#C5DDFE";
        oo.setAttribute("sIndex",index);
        if(width<suggestList[i].length*8){
          width=suggestList[i].length*8+30;
        }
        oo.attachEvent("onmousedown",function(evt){
            CodePress.insertCode(evt["srcElement"]["insertCode"]);
            editor.focus();
        });
        oo.insertCode=suggestList[i].substr(pre.length);





        if(index>=15)break;
      }
    }
    if(index!=0){
      suggest.style.display= 'block';
      if(arguments[1]==1)
        suggest.style.left = (offsetLeft-(pre.length)*7)+"px";
      else
        suggest.style.left = (offsetLeft-(pre.length-1)*7+CodePress.scrollLeft())+"px";
      suggest.style.top = (offsetTop+20+CodePress.scrollTop())+"px";
      suggest.style.width=width;
    }else{
       suggest.style.display= 'none';
    }

    return list;
  },
	findString : function() {
		range = self.document.body.createTextRange();
		if(range.findText(cc)){
			range.select();
			range.text = '';
		}
	},
	
	// split big files, highlighting parts of it
	split : function(code,flag) {
		if(flag=='scroll') {
			this.scrolling = true;
			return code;
		}
		else {
			this.scrolling = false;
			mid = code.indexOf(cc);
			if(mid-2000<0) {ini=0;end=4000;}
			else if(mid+2000>code.length) {ini=code.length-4000;end=code.length;}
			else {ini=mid-2000;end=mid+2000;}
			code = code.substring(ini,end);
			return code.substring(code.indexOf('<P>'),code.lastIndexOf('</P>')+4);
		}
	},
	
	// syntax highlighting parser
	syntaxHighlight : function(flag) {
		if(flag!='init') document.selection.createRange().text = cc;
		o = editor.innerHTML;
		if(flag=='paste') { // fix pasted text
			o = o.replace(/<BR>/g,'\r\n'); 
			o = o.replace(/\u2008/g,'\t');
		}
		o = o.replace(/<P>/g,'\n');
		o = o.replace(/<\/P>/g,'\r');
		o = o.replace(/<.*?>/g,'');
		o = o.replace(/&nbsp;/g,' ');			
		o = '<PRE><P>'+o+'</P></PRE>';
		o = o.replace(/\n\r/g,'<P></P>');
		o = o.replace(/\n/g,'<P>');
		o = o.replace(/\r/g,'<\/P>');
		o = o.replace(/<P>(<P>)+/,'<P>');
		o = o.replace(/<\/P>(<\/P>)+/,'</P>');
		o = o.replace(/<P><\/P>/g,'<P><BR/></P>');
		x = z = this.split(o,flag);
		
		if(arguments[1]&&arguments[2]){
			x = x.replace(arguments[1],arguments[2]);
		}
	
		for(i=0;i<Language.syntax.length;i++) 
			x = x.replace(Language.syntax[i].input,Language.syntax[i].output);
			
		editor.innerHTML = this.actions.history[this.actions.next()] = (flag=='scroll') ? x : o.replace(z,x);
		if(flag!='init') this.findString();
	},

	snippets : function(evt) {
		var snippets = Language.snippets;
		var trigger = this.getLastWord();
		for (var i=0; i<snippets.length; i++) {
			if(snippets[i].input == trigger) {
				var content = snippets[i].output.replace(/</g,'&lt;');
				content = content.replace(/>/g,'&gt;');
				if(content.indexOf('$0')<0) content += cc;
				else content = content.replace(/\$0/,cc);
				content = content.replace(/\n/g,'</P><P>');
				var pattern = new RegExp(trigger+cc,"gi");
				this.syntaxHighlight('snippets',pattern,content);
			}
		}
	},
	
	readOnly : function() {
		editor.contentEditable = (arguments[0]) ? 'false' : 'true';
	},
	
	getPre : function(count) {

		var k_lastline=this.getLastLine(count);
		var k_c=0;
		if(k_lastline!=""){
			if(k_lastline.indexOf("\t")!=0&&k_lastline.indexOf(" ")!=0){
				return "";
			}
			
			for(var i=0;i<k_lastline.length;i++){
				var temp=k_lastline.charCodeAt(i);
				if(temp!=0x0009&&temp!=0x0020){
					
					break;
				}
				if(temp==0x0009)k_c+=9;
				if(temp==0x0020)k_c+=1;
			}

		}

		var pre="";
		for(var i=0;i<k_c;i++){
			pre+=" ";
		}
		return pre;
	},
	complete : function(trigger) {
		var pre=this.getPre(0);
		
		var complete = Language.complete;
		for (var i=0; i<complete.length; i++) {
			if(complete[i].input == trigger) {
				var pattern = new RegExp('\\'+trigger+cc);
				var content = complete[i].output.replace(/pre/g,pre).replace(/\$0/g,cc);
				setTimeout(function () { CodePress.syntaxHighlight('complete',pattern,content)},0); // wait for char to appear on screen
			}
		}
	},
	
	getCompleteChars : function() {
		var cChars = '';
		for(var i=0;i<Language.complete.length;i++)
			cChars += '|'+Language.complete[i].input;
		return cChars+'|';
	},

	getCompleteEndingChars : function() {
		var cChars = '';
		for(var i=0;i<Language.complete.length;i++)
			cChars += '|'+Language.complete[i].output.charAt(Language.complete[i].output.length-1);
		return cChars+'|';
	},

	completeEnding : function(trigger) {
		var range = document.selection.createRange();
		try {
			range.moveEnd('character', 1)
		}
		catch(e) {
			return false;
		}
		var next_character = range.text
		range.moveEnd('character', -1)
		if(next_character != trigger )  return false;
		else {
			range.moveEnd('character', 1)
			range.text=''
			return true;
		}
	},	

	shortcuts : function() {
		var cCode = arguments[0];
		if(cCode==13) cCode = '[enter]';
		else if(cCode==32) cCode = '[space]';
		else cCode = '['+String.fromCharCode(keyCode).toLowerCase()+']';
		for(var i=0;i<Language.shortcuts.length;i++)
			if(Language.shortcuts[i].input == cCode)
				this.insertCode(Language.shortcuts[i].output,false);
	},
	
	getLastLine : function(count) {
		var rangeAndCaret = CodePress.getRangeAndCaret();
		words = rangeAndCaret[0].substring(rangeAndCaret[1]-100,rangeAndCaret[1]);
		
		words = words.replace(/[\n\r\{\;;]/g,'\n').split('\n');
		if(words!='undefined'||words.length>0){
			var p=0;
			for(var j=words.length-1;j>=0;j--){
				if(words[j]=='')continue;
				if(p==count){
					return  words[j];
				}
				p++;
			}
			return "";
		}
		else return "";
	}, 
	getLastWord_withpoint : function() {
		var rangeAndCaret = CodePress.getRangeAndCaret();
		words = rangeAndCaret[0].substring(rangeAndCaret[1]-40,rangeAndCaret[1]);
		words = words.replace(/[\s\n\r\);]/g,'\n').split('\n');
		return words[words.length-1].toLowerCase();
	},
	getLastWord : function() {
		var rangeAndCaret = CodePress.getRangeAndCaret();
		words = rangeAndCaret[0].substring(rangeAndCaret[1]-40,rangeAndCaret[1]);
		words = words.replace(/[\s\n\r\);\W]/g,'\n').split('\n');
		return words[words.length-1].replace(/[\W]/gi,'').toLowerCase();
	},

	getRangeAndCaret : function() {	
		var range = document.selection.createRange();
		var caret = Math.abs(range.moveStart('character', -1000000)+1);
		range = this.getCode();
		range = range.replace(/\n\r/gi,'  ');
		range = range.replace(/\n/gi,'');
		return [range.toString(),caret];
	},
	
	
	insertCode : function(code,replaceCursorBefore) {
		var repdeb = '';
		var repfin = '';
		
		if(replaceCursorBefore) { repfin = code; }
		else { repdeb = code; }
		
		if(typeof document.selection != 'undefined') {
			var range = document.selection.createRange();
			range.text = repdeb + repfin;
			range = document.selection.createRange();
			range.move('character', -repfin.length);
			range.collapse(true);
			range.select();	
		}	
	},

	// get code from editor	
	getCode : function() {
		var code = editor.innerHTML;
		code = code.replace(/<br>/g,'\n');
		code = code.replace(/<\/p>/gi,'\r');
		code = code.replace(/<p>/i,''); // IE first line fix
		code = code.replace(/<p>/gi,'\n');
		code = code.replace(/&nbsp;/gi,' ');
		code = code.replace(/\u2009/g,'');
		code = code.replace(/<.*?>/g,'');
		code = code.replace(/&lt;/g,'<');
		code = code.replace(/&gt;/g,'>');
		code = code.replace(/&amp;/gi,'&');
		return code;
	},

	// put code inside editor
	setCode : function() {
		var code = arguments[0];
		
		code = code.replace(/\u2009/gi,'');
		code = code.replace(/&/gi,'&amp;');		
       	code = code.replace(/</g,'&lt;');
        code = code.replace(/>/g,'&gt;');
		editor.innerHTML = '<pre>'+code+'</pre>';
		
	},

	
	// undo and redo methods
	actions : {
		pos : -1, // actual history position
		history : [], // history vector
		
		undo : function() {
			if(editor.innerHTML.indexOf(cc)==-1){
				document.selection.createRange().text = cc;
			 	this.history[this.pos] = editor.innerHTML;
			}
			this.pos--;
			if(typeof(this.history[this.pos])=='undefined') this.pos++;
			editor.innerHTML = this.history[this.pos];
			CodePress.findString();
		},
		
		redo : function() {
			this.pos++;
			if(typeof(this.history[this.pos])=='undefined') this.pos--;
			editor.innerHTML = this.history[this.pos];
			CodePress.findString();
		},
		
		next : function() { // get next vector position and clean old ones
			if(this.pos>20) this.history[this.pos-21] = undefined;
			return ++this.pos;
		}
	}
}

Language={};
window.attachEvent('onload', function() { CodePress.initialize('new');});



